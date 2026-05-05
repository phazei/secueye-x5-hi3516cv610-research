package com.aliyun.alink.linksdk.tmp.utils;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class CheckMeshMessage {
    private static final String TAG = "CheckMeshMessage";
    private static final long UPDATE_DEVICE_TIME = 70000;
    private static IPanelEventCallback iPanelEventCallback;
    private static final HashMap<String, Long> meshControlSeq = new HashMap<>();
    private static final Map<String, Long> scheduledFutureMap = new HashMap();

    public static void updateDeviceProperties(String str, String str2, IPanelEventCallback iPanelEventCallback2) {
    }

    public static boolean messageEffectiveness(String str, JSONObject jSONObject) {
        JSONObject jSONObject2;
        Long l;
        ALog.d("checkMessage", "checkMessageEffectiveness() called with: data = [" + jSONObject.toJSONString() + "]");
        if (containsMessage(str)) {
            return true;
        }
        if (jSONObject.keySet().size() < 2) {
            ALog.d(TAG, "checkMessageEffectiveness() called  数据错误，不作处理");
            return false;
        }
        JSONObject jSONObject3 = jSONObject.containsKey("CloudSequence") ? jSONObject.getJSONObject("CloudSequence") : null;
        if (jSONObject.containsKey("LocalSequence")) {
            jSONObject3 = jSONObject.getJSONObject("LocalSequence");
        }
        if (jSONObject3 == null || (jSONObject2 = jSONObject3.getJSONObject("value")) == null || (l = jSONObject2.getLong("SeqNum")) == null) {
            return false;
        }
        Long l2 = meshControlSeq.get(str);
        if (l2 == null) {
            l2 = 0L;
        }
        boolean z = l.longValue() > l2.longValue() || l.longValue() < l2.longValue() - 1000;
        if (z) {
            meshControlSeq.put(str, l);
        }
        ALog.d(TAG, "messageEffectiveness() called with: iotId = [" + str + "]\n,  items = [" + jSONObject.toJSONString() + "]]\n, 是否处理 = [" + z + "]");
        return !z;
    }

    public static void setiPanelEventCallback(IPanelEventCallback iPanelEventCallback2) {
        iPanelEventCallback = iPanelEventCallback2;
    }

    public static void refreshAppDevice(String str, String str2) {
        refreshAppDevice(str, str2, null);
    }

    /* JADX WARN: Type inference fix 'apply assigned field type' failed
    java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
    	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
    	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
    	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
    	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
     */
    public static void refreshAppDevice(String str, String str2, String str3) {
        ALog.d(TAG, "refreshAppDevice() called with: mIotId = [" + str + "], params = [" + str2 + "], group = [" + str3 + "]");
        if (iPanelEventCallback != null) {
            try {
                JSONObject object = JSONObject.parseObject(str2);
                String props = DeviceShadowMgr.getInstance().getProps(str);
                JSONObject jSONObject = new JSONObject();
                JSONObject jSONObject2 = new JSONObject();
                boolean z = true;
                boolean z2 = !SwitchManager.hasSwitch(str2);
                jSONObject.put("iotId", object.getString("iotId"));
                if (!TextUtils.isEmpty(props)) {
                    JSONObject jSONObject3 = JSON.parseObject(props).getJSONObject("data");
                    for (String str4 : jSONObject3.keySet()) {
                        JSONObject jSONObject4 = new JSONObject();
                        if (object.getJSONObject("items").containsKey(str4)) {
                            jSONObject4.put("value", object.getJSONObject("items").get(str4));
                        } else {
                            jSONObject4.put("value", (Object) jSONObject3.getJSONObject(str4).getString("value"));
                            if (SwitchManager.hasSwitch(str4) && z2) {
                                jSONObject4.put("value", (Object) "1");
                            }
                        }
                        jSONObject2.put(str4, (Object) jSONObject4);
                    }
                } else {
                    for (String str5 : object.getJSONObject("items").keySet()) {
                        JSONObject jSONObject5 = new JSONObject();
                        jSONObject5.put("value", object.getJSONObject("items").get(str5));
                        jSONObject2.put(str5, (Object) jSONObject5);
                    }
                }
                jSONObject.put("items", (Object) jSONObject2);
                if (TextUtils.isEmpty(str3)) {
                    z = false;
                }
                jSONObject.put("group", Boolean.valueOf(z));
                ALog.d(TAG, "refreshAppDevice: prop= " + jSONObject2.toJSONString());
                if (!TextUtils.isEmpty(str3)) {
                    List<String> meshGroupItem = TgMeshHelper.getMeshGroupItem(str3);
                    if (meshGroupItem != null) {
                        for (String str6 : meshGroupItem) {
                            DeviceShadowMgr.getInstance().optimisticUpdateMeshDevice(str6, jSONObject2);
                            ALog.d(TAG, "refreshAppDevice: [ " + str6 + " ] this device was control by group , add 70000l time");
                            scheduledFutureMap.put(str6, Long.valueOf(System.currentTimeMillis() + UPDATE_DEVICE_TIME));
                        }
                        return;
                    }
                    return;
                }
                DeviceShadowMgr.getInstance().optimisticUpdateMeshDevice(str, jSONObject2);
                scheduledFutureMap.put(str, Long.valueOf(System.currentTimeMillis() + UPDATE_DEVICE_TIME));
                if (iPanelEventCallback != null) {
                    iPanelEventCallback.onNotify(str, TmpConstant.MQTT_TOPIC_PROPERTIES, jSONObject);
                }
            } catch (Exception e) {
                ALog.d(TAG, "refreshAppDevice: " + e.getLocalizedMessage());
            }
        }
    }

    public static boolean containsMessage(String str) {
        synchronized (scheduledFutureMap) {
            if (scheduledFutureMap.containsKey(str)) {
                Long l = scheduledFutureMap.get(str);
                ALog.d(TAG, "containsMessage() called with: notifyIotId = [" + str + "[time ] = " + l + "[now]  =" + System.currentTimeMillis());
                if (l != null) {
                    if (l.longValue() - System.currentTimeMillis() > 0) {
                        return true;
                    }
                    ALog.d(TAG, "containsMessage() called with:  remove time notifyIotId = " + str);
                    scheduledFutureMap.remove(str);
                }
            }
            ALog.d(TAG, "containsMessage() called with: notifyIotId = [" + str + "] return flase");
            return false;
        }
    }

    public static void removeMessage(String str) {
        ALog.d(TAG, "removeMessage: disconnect device remove");
        synchronized (scheduledFutureMap) {
            scheduledFutureMap.remove(str);
        }
    }

    public static boolean compareMeshPropertyValue(String str, JSONObject jSONObject) {
        ALog.d(TAG, "compareMeshPropertyValue() called with: iotId = [" + str + "], newPropertyValue = [" + jSONObject + "]");
        boolean z = false;
        if (jSONObject == null) {
            ALog.w(TAG, "comparePropertyValue newPropertyValue empty false");
            return false;
        }
        String cachedProps = DeviceShadowMgr.getInstance().getCachedProps(str);
        if (TextUtils.isEmpty(cachedProps)) {
            ALog.w(TAG, "comparePropertyValue oldPropertyValue empty true");
            return true;
        }
        JSONObject object = JSONObject.parseObject(cachedProps);
        if (object == null) {
            ALog.w(TAG, "comparePropertyValue oldPropertyValue empty true");
            return true;
        }
        for (String str2 : object.keySet()) {
            if (!"CloudSequence".equalsIgnoreCase(str2) && !"LocalSequence".equalsIgnoreCase(str2)) {
                JSONObject jSONObject2 = jSONObject.getJSONObject(str2);
                JSONObject jSONObject3 = object.getJSONObject(str2);
                if (jSONObject2 != null && jSONObject3 != null && jSONObject2.containsKey("value") && jSONObject3.containsKey("value")) {
                    String string = jSONObject2.getString("value");
                    String string2 = jSONObject3.getString("value");
                    if (TextUtils.isEmpty(string) || !string.equals(string2)) {
                        z = true;
                        break;
                    }
                }
            }
        }
        ALog.i("checkMessageEffectiveness", "comparePropertyValue isNeedUpdate:" + z);
        return z;
    }

    public static void sendMessage(String str, String str2, JSONObject jSONObject) {
        Log.d(TAG, "sendMessage() called with: iotId = [" + str + "], mqttTopicNotify = [" + str2 + "], paramsJson = [" + jSONObject + "]");
        IPanelEventCallback iPanelEventCallback2 = iPanelEventCallback;
        if (iPanelEventCallback2 != null) {
            iPanelEventCallback2.onNotify(str, str2, jSONObject);
        }
    }
}
