package com.aliyun.alink.business.devicecenter.provision.core.mesh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.add.DeviceBindResultInfo;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2;
import com.aliyun.alink.business.devicecenter.channel.http.RetryTransitoryClient;
import com.aliyun.alink.business.devicecenter.config.ConfigCallbackWrapper;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IExtraConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConcurrentGateWayConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.UnprovisionedGateMeshDevice;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.L;
import com.aliyun.alink.business.devicecenter.provision.core.M;
import com.aliyun.alink.business.devicecenter.provision.core.O;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.AppUtils;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;
import com.aliyun.alink.business.devicecenter.utils.NetworkTypeUtils;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;
import com.facebook.internal.NativeProtocol;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_BATCH_GATEWAY_MESH)
public class ConcurrentGateMeshStrategy implements IExtraConfigStrategy {
    public static String TAG = "ConcurrentGateMeshStrategy";
    public String gatewayIotId;
    public a mBindReceiver;
    public DCAlibabaConcurrentGateWayConfigParams mConfigParams;
    public Context mContext;
    public List<String> mTaskIds;
    public UnprovisionedGateMeshDevice mUnprovisionedGateMeshDevice;
    public TimerUtils provisionTimer;
    public RetryTransitoryClient retryTransitoryClient;
    public final AtomicBoolean provisionHasStarted = new AtomicBoolean(false);
    public Future futureTask = null;
    public IConfigCallback mConfigCallback = null;
    public final AtomicBoolean provisionHasStopped = new AtomicBoolean(false);
    public int mSerialExecuteIndex = 0;
    public boolean registerReceiver = false;
    public final Object LOCK = new Object();

    private class a extends BroadcastReceiver {
        public a() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            DCErrorCode subcode;
            Log.d(ConcurrentGateMeshStrategy.TAG, "onReceive() called with: context = [" + context + "], intent = [" + intent + "]");
            JSONObject object = JSONObject.parseObject(intent.getStringExtra("bindData"));
            if (object == null) {
                ALog.d(ConcurrentGateMeshStrategy.TAG, "onReceive: data is null return ");
                return;
            }
            boolean z = object.getIntValue("owned") == 1;
            String string = object.getString("gatewayIotId");
            String string2 = object.getString("iotId");
            String string3 = object.getString("deviceName");
            String string4 = object.getString("productKey");
            String string5 = object.getString(AlinkConstants.KEY_PAGE_ROUTER_URL);
            String string6 = object.getString("code");
            int intFromString = StringUtils.getIntFromString(string6);
            String string7 = object.getString(AlinkConstants.KEY_LOCALIZED_MSG);
            ALog.d(ConcurrentGateMeshStrategy.TAG, "push mesh device provisioning " + string2 + " state=FINISH, isSuccess=" + z);
            if (!ConcurrentGateMeshStrategy.this.mUnprovisionedGateMeshDevice.containerDevice(string2)) {
                Log.d(ConcurrentGateMeshStrategy.TAG, "被过滤了");
                return;
            }
            Log.d(ConcurrentGateMeshStrategy.TAG, "开始处理");
            DeviceInfo deviceInfoFromIotId = ConcurrentGateMeshStrategy.this.mUnprovisionedGateMeshDevice.getDeviceInfoFromIotId(string2);
            deviceInfoFromIotId.regIotId = string;
            deviceInfoFromIotId.productKey = string4;
            deviceInfoFromIotId.deviceName = string3;
            deviceInfoFromIotId.bindResultInfo = new DeviceBindResultInfo();
            DeviceBindResultInfo deviceBindResultInfo = deviceInfoFromIotId.bindResultInfo;
            deviceBindResultInfo.productKey = string4;
            deviceBindResultInfo.deviceName = string3;
            deviceBindResultInfo.iotId = string2;
            deviceBindResultInfo.bindResult = 1;
            deviceBindResultInfo.pageRouterUrl = string5;
            deviceBindResultInfo.errorCode = string6;
            deviceBindResultInfo.localizedMsg = string7;
            if (z) {
                subcode = null;
            } else {
                DCErrorCode msg = new DCErrorCode(DCErrorCode.SERVER_ERROR_MSG, DCErrorCode.PF_SERVER_FAIL).setMsg("mesh/gateway/discovery/device/query provision failed state=FINISH, msg=" + string7);
                if (intFromString == 0) {
                    intFromString = DCErrorCode.SUBCODE_SRE_GET_MESH_PROVISION_RESULT_BIZ_FAIL;
                }
                subcode = msg.setSubcode(intFromString);
            }
            ConcurrentGateMeshStrategy.this.provisionResultCallback(deviceInfoFromIotId, subcode);
        }

        public /* synthetic */ a(ConcurrentGateMeshStrategy concurrentGateMeshStrategy, L l) {
            this();
        }
    }

    public ConcurrentGateMeshStrategy(Context context) {
        this.mContext = context;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void allGateMeshProvisionFail(DCErrorCode dCErrorCode) {
        Log.d(TAG, "allGateMeshProvisionFail() called with: code = [" + dCErrorCode + "]");
        List<DeviceInfo> unProvisionDeviceList = this.mUnprovisionedGateMeshDevice.getUnProvisionDeviceList();
        if (this.mUnprovisionedGateMeshDevice == null || unProvisionDeviceList == null || unProvisionDeviceList.size() <= 0) {
            return;
        }
        Iterator<DeviceInfo> it = unProvisionDeviceList.iterator();
        while (it.hasNext()) {
            provisionResultCallback(it.next(), dCErrorCode);
        }
    }

    private void closeProvision() {
        List<String> list = this.mTaskIds;
        if (list == null || list.size() == 0) {
            return;
        }
        JSONArray jSONArray = new JSONArray();
        for (String str : this.mTaskIds) {
            Log.d(TAG, "run: s=" + str);
            jSONArray.add(str);
        }
        ProvisionRepositoryV2.closeBatchMeshProvision(jSONArray);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getProvisionTimeoutErrorInfo() {
        DCErrorCode dCErrorCode = new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT);
        String str = "[mobile=" + NetworkTypeUtils.isMobileNetwork(this.mContext) + ", WiFi=" + NetworkTypeUtils.isWiFi(this.mContext) + "].";
        if (NetworkTypeUtils.isMobileNetwork(this.mContext)) {
            dCErrorCode.setSubcode(DCErrorCode.SUBCODE_NE_NETWORK_UNAVAILABLE).setMsg("provision timeout, network unavailable " + str);
        } else {
            dCErrorCode.setSubcode(DCErrorCode.SUBCODE_PT_DEVICE_PROVISION_FAIL_OR_TIMEOUT).setMsg("provision timeout, device not success until now,  " + str);
        }
        allGateMeshProvisionFail(dCErrorCode);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void loopQueryMeshProvisionResult() {
        Log.d(TAG, "loopQueryMeshProvisionResult() called");
        resetFutureTask();
        if (this.provisionHasStopped.get()) {
            return;
        }
        this.futureTask = ThreadPool.scheduleAtFixedRate(new O(this), 0L, 3L, TimeUnit.SECONDS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void provisionResultCallback(DeviceInfo deviceInfo, DCErrorCode dCErrorCode) {
        Log.d(TAG, "provisionResultCallback() called with: deviceInfo = [" + deviceInfo + "], errorCode = [" + dCErrorCode + "]");
        if (deviceInfo == null) {
            return;
        }
        TimerUtils timerUtils = this.provisionTimer;
        if (timerUtils != null) {
            timerUtils.start(TimerUtils.MSG_PROVISION_TIMEOUT);
            Log.d(TAG, "provisionResultCallback: restart");
        }
        if (TextUtils.isEmpty(deviceInfo.productKey) || TextUtils.isEmpty(deviceInfo.deviceName) || dCErrorCode != null) {
            String meshDeviceUniqueIDByMac = DeviceInfoUtils.getMeshDeviceUniqueIDByMac(deviceInfo.mac);
            ALog.w(TAG, "provisionResultCallback params , mac: " + deviceInfo.mac);
            LinkedHashMap linkedHashMap = new LinkedHashMap();
            dCErrorCode.setExtra(linkedHashMap);
            linkedHashMap.put(AlinkConstants.KEY_CACHE_MESH_DEVICE_UNIQUE_ID, meshDeviceUniqueIDByMac);
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(false).error(dCErrorCode));
        } else {
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(true).result(deviceInfo));
        }
        UnprovisionedGateMeshDevice unprovisionedGateMeshDevice = this.mUnprovisionedGateMeshDevice;
        if (unprovisionedGateMeshDevice != null) {
            unprovisionedGateMeshDevice.addProvisionResult(deviceInfo.iotId, dCErrorCode == null);
            if (this.mUnprovisionedGateMeshDevice.endProvision()) {
                Log.d(TAG, "provisionResultCallback: endProvision");
                scheduleNextConfigTask();
                resetFutureTask();
            }
        }
    }

    private void provisionStatusCallback(ProvisionStatus provisionStatus) {
        ALog.i(TAG, "provisionStatusCallback, status: " + provisionStatus);
        DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).status(provisionStatus));
    }

    private void registerReceiver() {
        Log.d(TAG, "registerReceiver() called");
        try {
            if (this.registerReceiver || this.mBindReceiver != null) {
                return;
            }
            this.mBindReceiver = new a(this, null);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("iLop.bind.cloud.mesh");
            AppUtils.getContext().registerReceiver(this.mBindReceiver, intentFilter);
            this.registerReceiver = true;
        } catch (Exception e) {
            Log.d(TAG, "registerReceiver() called e=" + e);
        }
    }

    private void resetFutureTask() {
        Log.d(TAG, "resetFutureTask() called");
        Future future = this.futureTask;
        if (future != null && !future.isDone()) {
            this.futureTask.cancel(true);
        }
        if (this.retryTransitoryClient != null) {
            Log.d(TAG, "resetFutureTask() called retryTransitoryClient.cancelRequest");
            this.retryTransitoryClient.cancelRequest();
        }
        this.futureTask = null;
    }

    private void scheduleNextConfigTask() {
        Log.d(TAG, "scheduleNextConfigTask() called");
        synchronized (this.LOCK) {
            if (this.mConfigParams.mDeviceInfoList != null) {
                int i = this.mSerialExecuteIndex + 1;
                this.mSerialExecuteIndex = i;
                if (i < this.mConfigParams.mDeviceInfoList.size() && !this.provisionHasStopped.get()) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Serial schedule task index: ");
                    sb.append(this.mSerialExecuteIndex);
                    ALog.d(str, sb.toString());
                    startMeshDeviceProvision(this.mSerialExecuteIndex);
                    return;
                }
            }
            if (this.mConfigParams.mDeviceInfoList != null) {
                this.mConfigParams.mDeviceInfoList.clear();
            }
            stopConfig();
        }
    }

    private void startMeshDeviceProvision(int i) {
        ALog.d(TAG, "startMeshDeviceProvision() called with: deviceIndex = [" + i + "]");
        if (this.provisionHasStopped.get()) {
            ALog.e(TAG, "provision has stopped, return.");
            return;
        }
        List<UnprovisionedGateMeshDevice> list = this.mConfigParams.mDeviceInfoList;
        if (list == null || i >= list.size()) {
            ALog.e(TAG, "internal error");
            return;
        }
        this.mUnprovisionedGateMeshDevice = this.mConfigParams.mDeviceInfoList.get(i);
        startProvisionTimer();
        JSONArray jSONArray = new JSONArray();
        for (DeviceInfo deviceInfo : this.mUnprovisionedGateMeshDevice.getDeviceInfo()) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("gatewayIotId", (Object) this.gatewayIotId);
            jSONObject.put(AlinkConstants.KEY_CONFIRM_CLOUD, (Object) deviceInfo.confirmCloud);
            jSONObject.put(AlinkConstants.KEY_AUTH_DEVICE, (Object) deviceInfo.authDevice);
            jSONObject.put(AlinkConstants.KEY_RANDOM, (Object) deviceInfo.random);
            jSONObject.put(AlinkConstants.KEY_AUTH_FLAG, (Object) Boolean.valueOf(deviceInfo.authFlag));
            jSONObject.put(AlinkConstants.KEY_PRODUCT_ID, (Object) deviceInfo.productId);
            jSONObject.put(AlinkConstants.KEY_SUB_DEVICE_ID, (Object) deviceInfo.subDeviceId);
            if (TextUtils.isEmpty(deviceInfo.subDeviceId)) {
                provisionResultCallback(deviceInfo, new DCErrorCode(NativeProtocol.ERROR_NETWORK_ERROR, DCErrorCode.PF_NETWORK_ERROR).setSubcode(DCErrorCode.SUBCODE_API_REQUEST_ON_FAILURE).setMsg("subDevice is null ,need,retry"));
            } else {
                jSONObject.put(AlinkConstants.KEY_MAC, (Object) deviceInfo.mac);
                jSONObject.put(AlinkConstants.KEY_DISCOVERED_SOURCE, (Object) deviceInfo.discoveredSource);
                jSONObject.put("deviceName", (Object) deviceInfo.deviceName);
                jSONObject.put("iotId", (Object) deviceInfo.iotId);
                jSONArray.add(jSONObject);
                ProvisionStatus provisionStatus = ProvisionStatus.PROVISION_START_IN_CONCURRENT_MODE;
                provisionStatus.addExtraParams(AlinkConstants.KEY_CACHE_START_PROVISION_DEVICE_INFO, deviceInfo);
                provisionStatusCallback(provisionStatus);
            }
        }
        Log.d(TAG, "startMeshDeviceProvision: 配网数量" + jSONArray.size());
        this.mConfigParams.deviceCount = -this.mUnprovisionedGateMeshDevice.getDeviceInfo().size();
        ProvisionRepositoryV2.batchMeshDeviceProvisionTrigger(jSONArray, this.gatewayIotId, new M(this));
    }

    private void startProvision() {
        this.provisionHasStarted.set(true);
        this.provisionHasStopped.set(false);
        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
        this.provisionHasStopped.set(false);
        ALog.d(TAG, "startProvision called...");
        synchronized (this.LOCK) {
            this.mSerialExecuteIndex = 0;
            startMeshDeviceProvision(0);
        }
        registerReceiver();
    }

    private void unregisterReceiver() {
        try {
            if (this.mBindReceiver != null) {
                AppUtils.getContext().unregisterReceiver(this.mBindReceiver);
                this.registerReceiver = false;
                this.mBindReceiver = null;
            }
        } catch (Exception unused) {
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void continueConfig(Map map) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void doExtraPrepareWork(IConfigExtraCallback iConfigExtraCallback) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IExtraConfigStrategy
    public List<DeviceInfo> getPrepareProvisionDevices() {
        synchronized (this.LOCK) {
            if (this.mConfigParams != null && this.mConfigParams.mDeviceInfoList != null && this.mSerialExecuteIndex + 1 < this.mConfigParams.mDeviceInfoList.size()) {
                Iterator<UnprovisionedGateMeshDevice> it = this.mConfigParams.mDeviceInfoList.iterator();
                for (int i = 0; i < this.mSerialExecuteIndex + 1; i++) {
                    it.next();
                }
                while (it.hasNext()) {
                    UnprovisionedGateMeshDevice next = it.next();
                    if (next != null && next.getDeviceInfo() != null && next.getDeviceInfo().size() > 0) {
                        return next.getDeviceInfo();
                    }
                }
            }
            return null;
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_BATCH_GATEWAY_MESH.getName();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IExtraConfigStrategy
    public int getResetCount() {
        DCAlibabaConcurrentGateWayConfigParams dCAlibabaConcurrentGateWayConfigParams = this.mConfigParams;
        if (dCAlibabaConcurrentGateWayConfigParams != null) {
            return dCAlibabaConcurrentGateWayConfigParams.deviceCount;
        }
        return 0;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean hasExtraPrepareWork() {
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean isSupport() {
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean needWiFiSsidPwd() {
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void preConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void startConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) throws Exception {
        ALog.i(TAG, "Build.VERSION.RELEASE: " + Build.VERSION.RELEASE + ", Build.MODEL: " + Build.MODEL + ", Build.BOARD: " + Build.BOARD);
        if (!(dCConfigParams instanceof DCAlibabaConcurrentGateWayConfigParams)) {
            ALog.w(TAG, "startConfig params error.");
            return;
        }
        Log.d(TAG, "startConfig: configParams.toString()= " + dCConfigParams.toString());
        if (this.mConfigParams != null) {
            Log.d(TAG, "startConfig: mConfigParams.toString()=" + this.mConfigParams.toString());
        }
        if (this.provisionHasStarted.get()) {
            synchronized (this.LOCK) {
                Log.d(TAG, "startConfig: this strategy is provision now,add device");
                this.mConfigParams.addDevice(((DCAlibabaConcurrentGateWayConfigParams) dCConfigParams).getAllDevice(), this.mSerialExecuteIndex);
            }
            return;
        }
        BLEScannerProxy.getInstance().stopScan();
        this.mConfigCallback = iConfigCallback;
        this.mConfigParams = (DCAlibabaConcurrentGateWayConfigParams) dCConfigParams;
        this.gatewayIotId = this.mConfigParams.getGatewayIotId();
        List<UnprovisionedGateMeshDevice> list = this.mConfigParams.mDeviceInfoList;
        if (list == null || list.size() <= 0 || this.provisionHasStarted.get()) {
            return;
        }
        startProvision();
    }

    public void startProvisionTimer() {
        ALog.d(TAG, "startProvisionTimer() called");
        TimerUtils timerUtils = this.provisionTimer;
        if (timerUtils != null) {
            timerUtils.stop(TimerUtils.MSG_PROVISION_TIMEOUT);
        }
        int size = 1;
        UnprovisionedGateMeshDevice unprovisionedGateMeshDevice = this.mUnprovisionedGateMeshDevice;
        if (unprovisionedGateMeshDevice != null && unprovisionedGateMeshDevice.getDeviceInfo() != null) {
            size = this.mUnprovisionedGateMeshDevice.getDeviceInfo().size();
        }
        ALog.d(TAG, "startProvisionTimer() called,count=" + size);
        this.provisionTimer = new TimerUtils(60000);
        this.provisionTimer.setCallback(new L(this));
        this.provisionTimer.start(TimerUtils.MSG_PROVISION_TIMEOUT);
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void stopConfig() {
        Log.d(TAG, "stopConfig() called");
        this.mConfigCallback = null;
        allGateMeshProvisionFail(new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT).setMsg("provision timeout"));
        this.provisionHasStopped.set(true);
        this.provisionHasStarted.set(false);
        TimerUtils timerUtils = this.provisionTimer;
        if (timerUtils != null) {
            timerUtils.stop(TimerUtils.MSG_PROVISION_TIMEOUT);
        }
        this.mConfigParams = null;
        Future future = this.futureTask;
        if (future != null) {
            future.cancel(true);
        }
        this.mSerialExecuteIndex = 0;
        closeProvision();
        this.mTaskIds = null;
        unregisterReceiver();
    }
}
