package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.iot.mesh.bean.MeshAccessPayload;
import com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.api.ConnectSDK;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnect;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.MeshManager;
import com.aliyun.alink.linksdk.tmp.listener.IDevStateChangeListener;
import com.aliyun.alink.linksdk.tmp.utils.CheckMeshMessage;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tools.ALog;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class CmpNotifyManager implements IConnectNotifyListener {
    protected static final String TAG = "[Tmp]CmpNotifyManager";
    private static long lastDeleteTime;
    protected Map<String, Map<Integer, IDevStateChangeListener>> mConnectChangeListenerList;
    protected Map<Integer, Map<String, Map<String, IConnectNotifyListener>>> mNotifyHandlerList;

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        protected static CmpNotifyManager f4250a = new CmpNotifyManager();
    }

    private CmpNotifyManager() {
        this.mNotifyHandlerList = new ConcurrentHashMap();
        this.mConnectChangeListenerList = new ConcurrentHashMap();
        ALog.d(TAG, "CmpNotifyManager registerNofityListener getPersistentConnectId,getAlcsDiscoveryConnectId,getAlcsServerConnectId");
        ConnectSDK.getInstance().registerNofityListener(ConnectSDK.getInstance().getPersistentConnectId(), this);
        ConnectSDK.getInstance().registerNofityListener(ConnectSDK.getInstance().getAlcsDiscoveryConnectId(), this);
        ConnectSDK.getInstance().registerNofityListener(ConnectSDK.getInstance().getAlcsServerConnectId(), this);
        TgMeshManager.getInstance().registerMeshMessageListener(new MeshMsgListener() { // from class: com.aliyun.alink.linksdk.tmp.connect.entity.cmp.CmpNotifyManager.1
            @Override // com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener
            public void onReceiveMeshMessage(byte[] bArr, MeshAccessPayload meshAccessPayload) {
                if (meshAccessPayload == null || TextUtils.isEmpty(meshAccessPayload.getTranslatedTSLDesc())) {
                    ALog.d(CmpNotifyManager.TAG, "onReceiveMeshMessage: 消息为空返回");
                    return;
                }
                ALog.d(CmpNotifyManager.TAG, "onReceiveMeshMessage() called with: bytes = [" + CmpNotifyManager.getUnicastAddressInt(bArr) + "], meshAccessPayload = [" + JSONObject.toJSONString(meshAccessPayload) + "]");
                JSONObject object = JSON.parseObject(meshAccessPayload.getTranslatedTSLDesc());
                if (object == null || object.keySet().size() == 0) {
                    ALog.d(CmpNotifyManager.TAG, "onReceiveMeshMessage: 消息为空返回");
                    return;
                }
                JSONObject jSONObject = new JSONObject();
                for (String str : object.keySet()) {
                    JSONObject jSONObject2 = new JSONObject();
                    jSONObject2.put("value", object.get(str));
                    jSONObject.put(str, (Object) jSONObject2);
                }
                JSONObject jSONObject3 = new JSONObject();
                JSONObject jSONObject4 = new JSONObject();
                jSONObject4.put("SeqNum", (Object) Integer.valueOf(meshAccessPayload.getSequenceNo()));
                jSONObject3.put("value", (Object) jSONObject4);
                jSONObject.put("LocalSequence", (Object) jSONObject3);
                JSONObject jSONObject5 = new JSONObject();
                jSONObject5.put("iotId", meshAccessPayload.getIotId());
                jSONObject5.put("items", (Object) jSONObject);
                jSONObject5.put("sequenceNo", Integer.valueOf(meshAccessPayload.getSequenceNo()));
                JSONObject jSONObject6 = new JSONObject();
                jSONObject6.put("method", "thing.properties");
                jSONObject6.put("params", (Object) jSONObject5);
                AMessage aMessage = new AMessage();
                aMessage.data = jSONObject6;
                ALog.d(CmpNotifyManager.TAG, jSONObject6.toJSONString());
                CmpNotifyManager.this.onNotify(PersistentConnect.CONNECT_ID, TmpConstant.MQTT_TOPIC_PROPERTIES, aMessage);
            }
        });
    }

    public static int getUnicastAddressInt(byte[] bArr) {
        return ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public static CmpNotifyManager getInstance() {
        return a.f4250a;
    }

    public String getRegistedTopic(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        if (!str.equalsIgnoreCase(ConnectSDK.getInstance().getPersistentConnectId())) {
            return str2;
        }
        if (str2.contains(TmpConstant.MQTT_TOPIC_PREFIX)) {
            return str2.substring(str2.indexOf(TmpConstant.MQTT_TOPIC_PREFIX));
        }
        return str2.contains(TmpConstant.MQTT_NOTIFY_TOPIC_PREFIX) ? str2.substring(str2.indexOf(TmpConstant.MQTT_NOTIFY_TOPIC_PREFIX)) : str2;
    }

    public void addConnectStateChangeListener(String str, IDevStateChangeListener iDevStateChangeListener) {
        Map<Integer, IDevStateChangeListener> concurrentHashMap = this.mConnectChangeListenerList.get(str);
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap<>();
            this.mConnectChangeListenerList.put(str, concurrentHashMap);
        }
        concurrentHashMap.put(Integer.valueOf(iDevStateChangeListener.hashCode()), iDevStateChangeListener);
    }

    public void removeConnectStateChangeListener(String str, IDevStateChangeListener iDevStateChangeListener) {
        Map<Integer, IDevStateChangeListener> map = this.mConnectChangeListenerList.get(str);
        if (map == null) {
            return;
        }
        map.remove(Integer.valueOf(iDevStateChangeListener.hashCode()));
    }

    public void addHandler(int i, String str, String str2, IConnectNotifyListener iConnectNotifyListener) {
        ALog.d(TAG, "addHandler :" + str + " topic:" + str2 + " handler:" + iConnectNotifyListener + " ownerId:" + i);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            ALog.w(TAG, "addHandler connectId or topic null");
            return;
        }
        Map<String, Map<String, IConnectNotifyListener>> concurrentHashMap = this.mNotifyHandlerList.get(Integer.valueOf(i));
        if (concurrentHashMap == null) {
            concurrentHashMap = new ConcurrentHashMap<>();
            this.mNotifyHandlerList.put(Integer.valueOf(i), concurrentHashMap);
        }
        Map<String, IConnectNotifyListener> map = concurrentHashMap.get(str);
        if (map == null) {
            map = new HashMap<>();
            concurrentHashMap.put(str, map);
        }
        map.put(str2, iConnectNotifyListener);
    }

    public void removeHandler(int i, String str, String str2) {
        Map<String, IConnectNotifyListener> map;
        ALog.d(TAG, "removeHandler connectId:" + str + " topic:" + str2 + " ownerId:" + i);
        Map<String, Map<String, IConnectNotifyListener>> map2 = this.mNotifyHandlerList.get(Integer.valueOf(i));
        if (map2 == null || (map = map2.get(str)) == null) {
            return;
        }
        map.remove(str2);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onNotify(String str, String str2, AMessage aMessage) {
        JSONObject jSONObject;
        String string;
        JSONObject jSONObject2;
        ALog.d(TAG, "onNotify connectId:" + str + " topic:" + str2);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            ALog.w(TAG, "onNotify null");
            return;
        }
        try {
            if (aMessage.data instanceof byte[]) {
                aMessage.data = new String((byte[]) aMessage.data, StandardCharsets.UTF_8);
            }
            ALog.d(TAG, "onNotify: CmpNotifyManager消息=" + aMessage.data);
            JSONObject object = JSONObject.parseObject(String.valueOf(aMessage.data));
            String string2 = null;
            if (object == null || !object.containsKey("params")) {
                jSONObject = null;
                string = null;
            } else {
                jSONObject = object.getJSONObject("params");
                string = jSONObject.getString("iotId");
            }
            if (jSONObject != null && (MeshManager.getInstance().isMeshDevice(string) || (jSONObject.containsKey("netType") && "NET_BT".equals(jSONObject.getString("netType"))))) {
                ALog.d(TAG, "onNotify: is mesh device");
                if (str2.endsWith(TmpConstant.MQTT_TOPIC_PROPERTIES)) {
                    JSONObject jSONObject3 = jSONObject.getJSONObject("items");
                    if (!CheckMeshMessage.messageEffectiveness(string, jSONObject3) && CheckMeshMessage.compareMeshPropertyValue(string, jSONObject3)) {
                        ALog.d("checkMessageEffectiveness", "下发消息 : " + jSONObject.toJSONString());
                        notifySuccess(str, str2, aMessage);
                        return;
                    }
                    return;
                }
                if (str2.endsWith(TmpConstant.MQTT_TOPIC_STATUS) && jSONObject.containsKey("status")) {
                    int intValue = jSONObject.getJSONObject("status").getIntValue("value");
                    notifySuccess(str, str2, aMessage);
                    if (jSONObject.containsKey(TmpConstant.TMP_LOCAL_STATUS) && jSONObject.getBoolean(TmpConstant.TMP_LOCAL_STATUS).booleanValue()) {
                        Log.d(TAG, "onNotify: this message from local do not update ");
                    } else {
                        MeshManager.getInstance().updateCloudStatus(string, intValue);
                    }
                }
            } else if (str2.endsWith(TmpConstant.MQTT_TOPIC_NOTIFY) && aMessage.data.toString().contains("Unbind")) {
                if (System.currentTimeMillis() - lastDeleteTime < 1000) {
                    Log.d(TAG, "delete message is repeat,return");
                    lastDeleteTime = System.currentTimeMillis();
                    return;
                }
                lastDeleteTime = System.currentTimeMillis();
                if (jSONObject.containsKey("value") && (jSONObject2 = jSONObject.getJSONObject("value")) != null && jSONObject2.containsKey("iotId")) {
                    string2 = jSONObject2.getString("iotId");
                }
                if (!TextUtils.isEmpty(string2)) {
                    CheckMeshMessage.sendMessage(string2, TmpConstant.MQTT_TOPIC_NOTIFY, jSONObject);
                }
            }
        } catch (Exception e) {
            ALog.d(TAG, "onNotify: 错误" + e.getLocalizedMessage());
        }
        notifySuccess(str, str2, aMessage);
    }

    private void notifySuccess(String str, String str2, AMessage aMessage) {
        String registedTopic = getRegistedTopic(str, str2);
        Map<Integer, Map<String, Map<String, IConnectNotifyListener>>> map = this.mNotifyHandlerList;
        if (map == null || map.isEmpty()) {
            ALog.w(TAG, "mNotifyHandlerList null");
            return;
        }
        Iterator<Map.Entry<Integer, Map<String, Map<String, IConnectNotifyListener>>>> it = this.mNotifyHandlerList.entrySet().iterator();
        while (it.hasNext()) {
            Map<String, IConnectNotifyListener> map2 = it.next().getValue().get(str);
            if (map2 != null) {
                IConnectNotifyListener iConnectNotifyListener = map2.get(registedTopic);
                ALog.d(TAG, "onNotify handler:" + iConnectNotifyListener + " realTopic:" + registedTopic);
                if (iConnectNotifyListener != null) {
                    iConnectNotifyListener.onNotify(str, registedTopic, aMessage);
                }
            }
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public boolean shouldHandle(String str, String str2) {
        ALog.d(TAG, "shouldHandle connectId:" + str + " topic:" + str2);
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            ALog.w(TAG, "shouldHandle null");
            return false;
        }
        Map<Integer, Map<String, Map<String, IConnectNotifyListener>>> map = this.mNotifyHandlerList;
        if (map != null && !map.isEmpty()) {
            return true;
        }
        ALog.w(TAG, "mNotifyHandlerList empty");
        return false;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener
    public void onConnectStateChange(String str, ConnectState connectState) {
        ALog.d(TAG, "onConnectStateChange s:" + str + " connectState:" + connectState);
        Map<Integer, IDevStateChangeListener> map = this.mConnectChangeListenerList.get(str);
        if (map == null || map.isEmpty()) {
            ALog.d(TAG, "onConnectStateChange devStateChangeListeners null");
            return;
        }
        Iterator it = new HashMap(map).entrySet().iterator();
        while (it.hasNext()) {
            ((IDevStateChangeListener) ((Map.Entry) it.next()).getValue()).onDevStateChange(TmpEnum.DeviceState.createConnectState(connectState));
        }
    }
}
