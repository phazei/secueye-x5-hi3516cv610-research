package com.aliyun.alink.linksdk.tmp.device.deviceshadow;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.ailabs.iot.mesh.MeshStatusCallback;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.iot.mesh.bean.MeshAccessPayload;
import com.alibaba.ailabs.iot.mesh.callback.DeviceOnlineStatusListener;
import com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnect;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.CmpNotifyManager;
import com.aliyun.alink.linksdk.tmp.utils.TgMeshHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ThreadTools;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class MeshManager {
    private static final String TAG = "[Tmp]MeshManager";
    private boolean inReconnectionTime;
    private boolean initialized;
    private final ConcurrentHashMap<String, DeviceStatus> mDeviceCloudStatusMap;
    private final List<String> mDeviceListMap;
    MeshNetWorkStatusCallback mMeshNetWorkStatusCallback;
    private final List<String> mProvisionDeviceListMap;
    private String meshMsg;
    private final MeshMsgListener meshMsgListener;
    private int meshStatus;
    private final MeshStatusCallback meshStatusCallback;
    private final DeviceOnlineStatusListener onlineStatusListener;
    int x;

    public enum DeviceStatus {
        stateless,
        online,
        offline
    }

    public interface MeshNetWorkStatusCallback {
        void isConnect(boolean z);
    }

    public void removeDevice(String str) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mDeviceListMap.remove(str);
        this.mDeviceCloudStatusMap.remove(str);
        this.mProvisionDeviceListMap.remove(str);
    }

    public void addProvisionDevice(String str) {
        Log.d(TAG, "addProvisionDevice() called with: mIotId = [" + str + "]");
        this.mProvisionDeviceListMap.add(str);
    }

    public static MeshManager getInstance() {
        return SingletonClassInstance.instance();
    }

    public void setStatusCallback(MeshNetWorkStatusCallback meshNetWorkStatusCallback) {
        this.mMeshNetWorkStatusCallback = meshNetWorkStatusCallback;
    }

    private static class SingletonClassInstance {
        private static final MeshManager instance = new MeshManager();

        private SingletonClassInstance() {
        }

        public static MeshManager instance() {
            return instance;
        }
    }

    public boolean isMeshDevice(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return this.mDeviceListMap.contains(str);
    }

    private MeshManager() {
        this.meshStatus = -2;
        this.meshMsg = "";
        this.initialized = false;
        this.inReconnectionTime = false;
        this.meshMsgListener = new MeshMsgListener() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.MeshManager.1
            @Override // com.alibaba.ailabs.iot.mesh.callback.MeshMsgListener
            public void onReceiveMeshMessage(byte[] bArr, MeshAccessPayload meshAccessPayload) {
                short s = ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort();
                ALog.i(MeshManager.TAG, "onReceiveMeshMessage opcode = " + meshAccessPayload.getOpCode() + ", address = " + ((int) s));
                if (meshAccessPayload.getTranslatedTSLDesc() != null) {
                    Log.i(MeshManager.TAG, "onReceiveMeshMessage translatedTSLDesc = " + meshAccessPayload.getTranslatedTSLDesc());
                }
            }
        };
        this.meshStatusCallback = new MeshStatusCallback() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.MeshManager.2
            @Override // com.alibaba.ailabs.iot.mesh.StatusCallback
            public void onStatus(int i, String str) {
                Log.d(MeshManager.TAG, "meshStatusCallback onStatus= " + i + ", " + str);
                if ((i == 2 || i == -2) && i != MeshManager.this.meshStatus) {
                    MeshManager.this.meshStatus = i;
                    if (i != 2) {
                        MeshManager.this.inReconnectionTime = true;
                        ThreadTools.submitTask(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.MeshManager.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                MeshManager.this.inReconnectionTime = false;
                                if (MeshManager.this.meshStatus != -2 || MeshManager.this.mMeshNetWorkStatusCallback == null) {
                                    return;
                                }
                                MeshManager.this.mMeshNetWorkStatusCallback.isConnect(false);
                            }
                        }, true, 30000);
                        if (MeshManager.this.mDeviceListMap.size() > 0) {
                            TgMeshHelper.connect((String) MeshManager.this.mDeviceListMap.get(0));
                            return;
                        }
                        return;
                    }
                    if (MeshManager.this.mMeshNetWorkStatusCallback != null) {
                        MeshManager.this.mMeshNetWorkStatusCallback.isConnect(true);
                    }
                }
            }
        };
        this.onlineStatusListener = new DeviceOnlineStatusListener() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.MeshManager.3
            @Override // com.alibaba.ailabs.iot.mesh.callback.DeviceOnlineStatusListener
            public void onOnlineStatusChange(String str, TgMeshManager.DevOnlineStatus devOnlineStatus) {
                String strConvertDevIdToIotId = MeshDeviceInfoManager.getInstance().convertDevIdToIotId(str);
                if (TextUtils.isEmpty(strConvertDevIdToIotId)) {
                    return;
                }
                if (!MeshManager.this.mDeviceListMap.contains(strConvertDevIdToIotId)) {
                    MeshManager.this.mDeviceListMap.add(strConvertDevIdToIotId);
                }
                Log.d(MeshManager.TAG, "onOnlineStatusChange() called with: iotId = [" + strConvertDevIdToIotId + "], devOnlineStatus = [" + devOnlineStatus + "]");
                MeshManager.this.updateDeviceStatus(strConvertDevIdToIotId);
            }
        };
        this.x = 0;
        this.mDeviceListMap = new ArrayList();
        this.mProvisionDeviceListMap = new ArrayList();
        this.mDeviceCloudStatusMap = new ConcurrentHashMap<>();
    }

    private synchronized void allDeviceSetStatus() {
    }

    public boolean isMeshNetworkOK() {
        return TgMeshManager.getInstance().isConnectedToMesh();
    }

    public synchronized void initMeshManager() {
        Log.d(TAG, "initMeshManager");
        if (!this.initialized) {
            TgMeshManager.getInstance().registerMeshMessageListener(this.meshMsgListener);
            TgMeshManager.getInstance().registerCallback(this.meshStatusCallback);
            TgMeshManager.getInstance().registerDeviceOnlineStatusListener(this.onlineStatusListener);
            this.initialized = true;
        }
    }

    private void updateCloudStatus(String str, DeviceStatus deviceStatus) {
        Log.d(TAG, "updateCloudStatus() called with: iotId = [" + str + "], stateless = [" + deviceStatus + "]");
        this.mDeviceCloudStatusMap.put(str, deviceStatus);
    }

    public void updateCloudStatus(String str, int i) {
        DeviceStatus deviceStatus;
        Log.d(TAG, "updateCloudStatus() called with: iotId = [" + str + "], value = [" + i + "]");
        if (i == 1) {
            deviceStatus = DeviceStatus.online;
        } else {
            deviceStatus = i == 3 ? DeviceStatus.offline : DeviceStatus.stateless;
        }
        updateCloudStatus(str, deviceStatus);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDeviceStatus(String str) {
        DeviceStatus meshCurrentStatus;
        Log.d(TAG, "updateDeviceStatus() called with: iotId = [" + str + "]");
        if (TextUtils.isEmpty(str) || (meshCurrentStatus = getMeshCurrentStatus(str)) == null) {
            return;
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("value", (Object) Integer.valueOf(meshCurrentStatus == DeviceStatus.online ? 1 : meshCurrentStatus == DeviceStatus.offline ? 3 : 2));
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("iotId", (Object) str);
        jSONObject2.put("status", (Object) jSONObject);
        jSONObject2.put(TmpConstant.TMP_LOCAL_STATUS, (Object) true);
        JSONObject jSONObject3 = new JSONObject();
        jSONObject3.put("params", (Object) jSONObject2);
        jSONObject3.put("iotId", (Object) str);
        AMessage aMessage = new AMessage();
        aMessage.data = jSONObject3;
        CmpNotifyManager.getInstance().onNotify(PersistentConnect.CONNECT_ID, TmpConstant.MQTT_TOPIC_STATUS, aMessage);
    }

    public DeviceStatus getMeshCurrentStatus(String str) {
        Log.d(TAG, "getMeshCurrentStatus() called with: iotId = [" + str + "]");
        DeviceStatus deviceStatus = DeviceStatus.stateless;
        DeviceStatus deviceStatusQueryDeviceOnlineStatus = queryDeviceOnlineStatus(str);
        if (this.mDeviceCloudStatusMap.containsKey(str)) {
            deviceStatus = this.mDeviceCloudStatusMap.get(str);
        }
        boolean zIsMeshNetworkOK = isMeshNetworkOK();
        if (deviceStatusQueryDeviceOnlineStatus == DeviceStatus.stateless && zIsMeshNetworkOK) {
            ALog.d(TAG, "getMeshCurrentStatus: localStatus is stateless but mesh network is ok ");
            deviceStatusQueryDeviceOnlineStatus = DeviceStatus.online;
        }
        if (!zIsMeshNetworkOK) {
            ALog.d(TAG, "getMeshCurrentStatus: mesh network is offline");
            deviceStatusQueryDeviceOnlineStatus = DeviceStatus.offline;
        }
        if (this.inReconnectionTime && deviceStatusQueryDeviceOnlineStatus == DeviceStatus.offline) {
            deviceStatusQueryDeviceOnlineStatus = DeviceStatus.stateless;
        }
        if (!Utils.isBleEnabled()) {
            deviceStatusQueryDeviceOnlineStatus = DeviceStatus.offline;
        }
        if (zIsMeshNetworkOK && deviceStatusQueryDeviceOnlineStatus == DeviceStatus.offline && this.mProvisionDeviceListMap.contains(str)) {
            ALog.d(TAG, "刚配网的设备,认为在线");
            return DeviceStatus.online;
        }
        ALog.d(TAG, "getMeshCurrentStatus() called with: deviceId = [" + str + "], localStatus = [ " + deviceStatusQueryDeviceOnlineStatus + " ], cloudStatus = [ " + deviceStatus + " ]");
        return getCurrentStatus(deviceStatusQueryDeviceOnlineStatus, deviceStatus);
    }

    public DeviceStatus getLocalStatus(String str) {
        if (!Utils.isBleEnabled()) {
            return DeviceStatus.offline;
        }
        return queryDeviceOnlineStatus(str);
    }

    public DeviceStatus getCloudStatus(String str) {
        return this.mDeviceCloudStatusMap.get(str);
    }

    private DeviceStatus getCurrentStatus(DeviceStatus deviceStatus, DeviceStatus deviceStatus2) {
        if (deviceStatus == DeviceStatus.online || deviceStatus2 == DeviceStatus.online) {
            return DeviceStatus.online;
        }
        if (deviceStatus2 == DeviceStatus.offline && deviceStatus == DeviceStatus.offline) {
            return DeviceStatus.offline;
        }
        return DeviceStatus.stateless;
    }

    public DeviceStatus queryDeviceOnlineStatus(String str) {
        String strCoverIotIdToDevId = MeshDeviceInfoManager.getInstance().coverIotIdToDevId(str);
        LogUtils.d(TAG, "queryDeviceOnlineStatus() called with: iotId = [" + str + "],devId = [" + strCoverIotIdToDevId + "]");
        if (TextUtils.isEmpty(strCoverIotIdToDevId)) {
            return DeviceStatus.stateless;
        }
        TgMeshManager.DevOnlineStatus devOnlineStatusQueryDeviceOnlineStatus = TgMeshManager.getInstance().queryDeviceOnlineStatus(strCoverIotIdToDevId);
        LogUtils.d(TAG, "queryDeviceOnlineStatus: localStatus=" + devOnlineStatusQueryDeviceOnlineStatus);
        if (devOnlineStatusQueryDeviceOnlineStatus == null) {
            return DeviceStatus.stateless;
        }
        if (TgMeshManager.DevOnlineStatus.DEV_ST_ONLINE == devOnlineStatusQueryDeviceOnlineStatus) {
            return DeviceStatus.online;
        }
        if (TgMeshManager.DevOnlineStatus.DEV_ST_OFFLINE == devOnlineStatusQueryDeviceOnlineStatus) {
            return DeviceStatus.offline;
        }
        return DeviceStatus.stateless;
    }

    public void addMeshDevice(String str, int i) {
        Log.d(TAG, "addMeshDevice() called with: iotId = [" + str + "], status = [" + i + "]");
        if (TextUtils.isEmpty(str)) {
            return;
        }
        this.mDeviceListMap.add(str);
        this.mDeviceCloudStatusMap.put(str, i == 1 ? DeviceStatus.online : DeviceStatus.offline);
    }

    public void test() {
        Log.d(TAG, "test() called");
        for (String str : this.mDeviceListMap) {
            DeviceStatus deviceStatus = DeviceStatus.offline;
            JSONObject jSONObject = new JSONObject();
            if (this.x % 2 == 0) {
                deviceStatus = DeviceStatus.online;
            }
            jSONObject.put("value", (Object) Integer.valueOf(deviceStatus == DeviceStatus.online ? 1 : deviceStatus == DeviceStatus.offline ? 3 : 2));
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.put("iotId", (Object) str);
            jSONObject2.put("status", (Object) jSONObject);
            jSONObject2.put(TmpConstant.TMP_LOCAL_STATUS, (Object) true);
            JSONObject jSONObject3 = new JSONObject();
            jSONObject3.put("params", (Object) jSONObject2);
            jSONObject3.put("iotId", (Object) str);
            AMessage aMessage = new AMessage();
            aMessage.data = jSONObject3;
            CmpNotifyManager.getInstance().onNotify(PersistentConnect.CONNECT_ID, TmpConstant.MQTT_TOPIC_STATUS, aMessage);
        }
        this.x++;
    }
}
