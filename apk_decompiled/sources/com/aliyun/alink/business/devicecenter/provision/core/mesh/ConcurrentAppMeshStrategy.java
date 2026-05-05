package com.aliyun.alink.business.devicecenter.provision.core.mesh;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import com.alibaba.ailabs.iot.aisbase.callback.ILeScanCallback;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.mesh.MeshStatusCallback;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.iot.mesh.TgScanManager;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.alibaba.ailabs.iot.mesh.delegate.OnReadyToBindHandler;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepository;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.cache.EnrolleeMeshDeviceCacheModel;
import com.aliyun.alink.business.devicecenter.config.ConfigCallbackWrapper;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConcurrentConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.model.FilterData;
import com.aliyun.alink.business.devicecenter.provision.core.F;
import com.aliyun.alink.business.devicecenter.provision.core.G;
import com.aliyun.alink.business.devicecenter.provision.core.H;
import com.aliyun.alink.business.devicecenter.provision.core.I;
import com.aliyun.alink.business.devicecenter.provision.core.J;
import com.aliyun.alink.business.devicecenter.provision.core.K;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_BATCH_APP_MESH)
public class ConcurrentAppMeshStrategy implements IConfigStrategy {
    public static String TAG = "ConcurrentAppMeshStrategy";
    public AtomicBoolean hasStartScanInBatchMode;
    public AtomicInteger lastBatchProvisionNumber;
    public IConfigCallback mConfigCallback;
    public Context mContext;
    public Map<String, Pair<UnprovisionedBluetoothMeshDevice, DCAlibabaConfigParams>> mDeviceMac2SubConfigParamsMap;
    public ILeScanCallback mForKeepHighDutyScanCallback;
    public Map<String, Object> mProvisioningExtensionsParams;
    public int mSerialExecuteIndex;
    public String meshProvisionErrorMessage;
    public int meshProvisionStatus;
    public MeshStatusCallback meshStatusCallback;
    public OnReadyToBindHandler onReadyToBindHandler;
    public DCErrorCode provisionErrorInfo;
    public AtomicBoolean provisionHasStarted;
    public AtomicBoolean provisionHasStopped;
    public TimerUtils provisionTimer;
    public List<String> repeatPidArray;
    public List<UnprovisionedBluetoothMeshDevice> unprovisionedBluetoothMeshDeviceList;
    public AtomicBoolean unprovisionedDeviceFound;
    public AtomicBoolean useBatchProvisionStrategy;

    /* JADX INFO: Access modifiers changed from: private */
    public interface a {
        void onFail(int i, String str);

        void onSuccess(Object obj);
    }

    public ConcurrentAppMeshStrategy() {
        this.mContext = null;
        this.meshProvisionStatus = 0;
        this.meshProvisionErrorMessage = null;
        this.provisionHasStarted = new AtomicBoolean(false);
        this.unprovisionedBluetoothMeshDeviceList = null;
        this.unprovisionedDeviceFound = new AtomicBoolean(false);
        this.mConfigCallback = null;
        this.provisionErrorInfo = null;
        this.provisionTimer = null;
        this.provisionHasStopped = new AtomicBoolean(false);
        this.mDeviceMac2SubConfigParamsMap = new LinkedHashMap();
        this.mSerialExecuteIndex = 0;
        this.useBatchProvisionStrategy = new AtomicBoolean(true);
        this.hasStartScanInBatchMode = new AtomicBoolean(false);
        this.lastBatchProvisionNumber = new AtomicInteger(0);
        this.meshStatusCallback = new G(this);
        this.onReadyToBindHandler = new H(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public DeviceInfo buildDeviceInfoViaMac(String str) {
        String strSubstring;
        Object obj;
        String lowerCase = str.toLowerCase();
        if (lowerCase.contains(":")) {
            strSubstring = lowerCase;
        } else {
            strSubstring = lowerCase.replaceAll("(.{2})", "$1:").substring(0, 17);
        }
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.mac = strSubstring;
        deviceInfo.linkType = LinkType.ALI_APP_MESH.getName();
        Pair<UnprovisionedBluetoothMeshDevice, DCAlibabaConfigParams> pair = this.mDeviceMac2SubConfigParamsMap.get(DeviceInfoUtils.getMeshDeviceUniqueIDByMac(lowerCase));
        if (pair == null || (obj = pair.first) == null || pair.second == null) {
            ALog.e(TAG, "Build device info: can not find config params for device: " + lowerCase);
            return null;
        }
        deviceInfo.productId = String.valueOf(((UnprovisionedBluetoothMeshDevice) obj).getSigMeshProductID());
        deviceInfo.deviceName = getDeviceName((UnprovisionedBluetoothMeshDevice) pair.first, (DCAlibabaConfigParams) pair.second);
        deviceInfo.deviceId = MeshParserUtils.bytesToHex(((UnprovisionedBluetoothMeshDevice) pair.first).getUUID(), false).toLowerCase();
        if (!TextUtils.isEmpty(((DCAlibabaConfigParams) pair.second).productKey)) {
            deviceInfo.productKey = ((DCAlibabaConfigParams) pair.second).productKey;
        }
        return deviceInfo;
    }

    private void filterDevice(String str, a aVar) {
        FilterData filterData = new FilterData();
        filterData.setProductId(str);
        ArrayList arrayList = new ArrayList();
        arrayList.add(filterData);
        ProvisionRepository.cloudToFilterDevice(arrayList, new K(this, aVar));
    }

    private String getDeviceName(UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice, DCAlibabaConfigParams dCAlibabaConfigParams) {
        if (dCAlibabaConfigParams == null) {
            return null;
        }
        if (!TextUtils.isEmpty(dCAlibabaConfigParams.deviceName)) {
            return dCAlibabaConfigParams.deviceName;
        }
        if (!TextUtils.isEmpty(dCAlibabaConfigParams.mac)) {
            dCAlibabaConfigParams.deviceName = dCAlibabaConfigParams.mac.replace(":", "").toLowerCase();
            return dCAlibabaConfigParams.deviceName;
        }
        if (TextUtils.isEmpty(unprovisionedBluetoothMeshDevice.getAddress())) {
            return null;
        }
        dCAlibabaConfigParams.deviceName = unprovisionedBluetoothMeshDevice.getAddress().replace(":", "").toLowerCase();
        return dCAlibabaConfigParams.deviceName;
    }

    private void pidReturnToPk(String str, a aVar) {
        ProvisionRepositoryV2.pidReturnToPk(str, new J(this, aVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void provisionResCallback(DeviceInfo deviceInfo) {
        if (deviceInfo == null || ((TextUtils.isEmpty(deviceInfo.productId) && TextUtils.isEmpty(deviceInfo.productKey)) || TextUtils.isEmpty(deviceInfo.deviceName))) {
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(false).error(this.provisionErrorInfo));
        } else {
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(true).result(deviceInfo));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void provisionResultCallback(DeviceInfo deviceInfo) {
        if (deviceInfo == null || TextUtils.isEmpty(deviceInfo.productKey) || TextUtils.isEmpty(deviceInfo.deviceName)) {
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(false).error(this.provisionErrorInfo));
        } else {
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(true).result(deviceInfo));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void provisionStatusCallback(ProvisionStatus provisionStatus) {
        ALog.i(TAG, "provisionStatusCallback, status: " + provisionStatus);
        DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).status(provisionStatus));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void provisionSuccess(String str) {
        Pair<UnprovisionedBluetoothMeshDevice, DCAlibabaConfigParams> pair = this.mDeviceMac2SubConfigParamsMap.get(DeviceInfoUtils.getMeshDeviceUniqueIDByMac(str));
        if (pair == null || pair.first == null || pair.second == null) {
            ALog.w(TAG, "Can not find config params for device: " + str);
            return;
        }
        DeviceInfo deviceInfoBuildDeviceInfoViaMac = buildDeviceInfoViaMac(str);
        String strValueOf = String.valueOf(((UnprovisionedBluetoothMeshDevice) pair.first).getSigMeshProductID());
        if (TextUtils.isEmpty(((DCAlibabaConfigParams) pair.second).productKey) && !DCEnvHelper.isTgEnv()) {
            pidReturnToPk(strValueOf, new I(this, deviceInfoBuildDeviceInfoViaMac));
        } else {
            ALog.d(TAG, "provisionSuccess call");
            provisionResCallback(deviceInfoBuildDeviceInfoViaMac);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void scheduleNextConfigTask() {
        if (this.useBatchProvisionStrategy.get()) {
            return;
        }
        List<UnprovisionedBluetoothMeshDevice> list = this.unprovisionedBluetoothMeshDeviceList;
        if (list != null) {
            int i = this.mSerialExecuteIndex + 1;
            this.mSerialExecuteIndex = i;
            if (i < list.size() && !this.provisionHasStopped.get()) {
                ALog.d(TAG, "Serial schedule task index: " + this.mSerialExecuteIndex);
                startMeshDeviceProvision(this.mSerialExecuteIndex);
                return;
            }
        }
        List<UnprovisionedBluetoothMeshDevice> list2 = this.unprovisionedBluetoothMeshDeviceList;
        if (list2 != null) {
            list2.clear();
        }
        stopConfig();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMeshDeviceProvision(int i) {
        if (this.provisionHasStopped.get()) {
            ALog.e(TAG, "provision has stopped, return.");
            return;
        }
        List<UnprovisionedBluetoothMeshDevice> list = this.unprovisionedBluetoothMeshDeviceList;
        if (list == null || i >= list.size()) {
            ALog.e(TAG, "internal error");
            return;
        }
        if (this.useBatchProvisionStrategy.get()) {
            ALog.d(TAG, "TgMeshManager setOnReadyToBindHandler");
            TgMeshManager.getInstance().setOnReadyToBindHandler(this.onReadyToBindHandler);
            if (this.lastBatchProvisionNumber.get() <= 0) {
                this.lastBatchProvisionNumber.set(this.unprovisionedBluetoothMeshDeviceList.size());
                TgMeshManager.getInstance().batchBindDevices(this.unprovisionedBluetoothMeshDeviceList, this.mProvisioningExtensionsParams);
                return;
            } else if (this.lastBatchProvisionNumber.get() >= this.unprovisionedBluetoothMeshDeviceList.size()) {
                ALog.e(TAG, "internal error, you should not be here.");
                return;
            } else {
                TgMeshManager.getInstance().batchBindDevices(this.unprovisionedBluetoothMeshDeviceList.subList(this.lastBatchProvisionNumber.get(), this.unprovisionedBluetoothMeshDeviceList.size()), this.mProvisioningExtensionsParams);
                this.lastBatchProvisionNumber.set(this.unprovisionedBluetoothMeshDeviceList.size());
                return;
            }
        }
        UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice = this.unprovisionedBluetoothMeshDeviceList.get(i);
        try {
            ALog.d(TAG, "TgMeshManager setOnReadyToBindHandler");
            TgMeshManager.getInstance().setOnReadyToBindHandler(this.onReadyToBindHandler);
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("TgMeshManager bindDevice dev=");
            sb.append(unprovisionedBluetoothMeshDevice);
            ALog.d(str, sb.toString());
            ProvisionStatus provisionStatus = ProvisionStatus.PROVISION_START_IN_CONCURRENT_MODE;
            provisionStatus.addExtraParams(AlinkConstants.KEY_CACHE_START_PROVISION_DEVICE_INFO, buildDeviceInfoViaMac(unprovisionedBluetoothMeshDevice.getAddress()));
            provisionStatusCallback(provisionStatus);
            TgMeshManager.getInstance().bindDevice(unprovisionedBluetoothMeshDevice);
        } catch (Exception e) {
            this.provisionErrorInfo = new DCErrorCode("SdkError", DCErrorCode.PF_PROVISION_APP_MESH_ERROR).setMsg("mesh sdk exception, state=" + this.meshProvisionStatus + ", msg=" + this.meshProvisionStatus + ", e=" + e).setSubcode(DCErrorCode.SUBCODE_MESH_SDK_PROVISION_EXCEPTION).setExtra(buildDeviceInfoViaMac(unprovisionedBluetoothMeshDevice.getAddress()));
            provisionResultCallback(null);
        }
    }

    private void startProvision() {
        this.provisionHasStarted.set(true);
        this.provisionHasStopped.set(false);
        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
        this.provisionHasStopped.set(false);
        ALog.d(TAG, "TgMeshManager init.");
        ALog.d(TAG, "TgMeshManager registerCallback callback=" + this.meshStatusCallback);
        TgMeshManager.getInstance().registerCallback(this.meshStatusCallback);
        if (!TgMeshManager.getInstance().isInitialized()) {
            ALog.d(TAG, "TgMeshManager need init.");
            TgMeshManager.getInstance().init(this.mContext, new F(this));
        } else {
            ALog.d(TAG, "TgMeshManager already inited, mesh sdk init success.");
            this.mSerialExecuteIndex = 0;
            startMeshDeviceProvision(0);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void continueConfig(Map map) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void doExtraPrepareWork(IConfigExtraCallback iConfigExtraCallback) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_BATCH_APP_MESH.getName();
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
        UnprovisionedBluetoothMeshDevice meshDevice;
        ALog.i(TAG, "Build.VERSION.RELEASE: " + Build.VERSION.RELEASE + ", Build.MODEL: " + Build.MODEL + ", Build.BOARD: " + Build.BOARD);
        BLEScannerProxy.getInstance().stopScan();
        this.mConfigCallback = iConfigCallback;
        if (!(dCConfigParams instanceof DCAlibabaConcurrentConfigParams)) {
            ALog.w(TAG, "startConfig params error.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR);
            this.provisionErrorInfo.setMsg("configParams error:").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        DCAlibabaConcurrentConfigParams dCAlibabaConcurrentConfigParams = (DCAlibabaConcurrentConfigParams) dCConfigParams;
        List<DCAlibabaConfigParams> list = dCAlibabaConcurrentConfigParams.configParamsList;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("startConfig() called with: callback = [");
        sb.append(iConfigCallback);
        sb.append("], configParams.size = [");
        sb.append(list == null ? 0 : list.size());
        sb.append("]");
        ALog.d(str, sb.toString());
        if (TextUtils.isEmpty(dCAlibabaConcurrentConfigParams.familyId)) {
            Map<String, Object> map = this.mProvisioningExtensionsParams;
            if (map != null) {
                map.remove("familyId");
            }
        } else {
            if (this.mProvisioningExtensionsParams == null) {
                this.mProvisioningExtensionsParams = new HashMap();
            }
            this.mProvisioningExtensionsParams.put("familyId", dCAlibabaConcurrentConfigParams.familyId);
        }
        if (list != null && list.size() > 0) {
            if (this.unprovisionedBluetoothMeshDeviceList == null) {
                this.unprovisionedBluetoothMeshDeviceList = new LinkedList();
            }
            for (DCAlibabaConfigParams dCAlibabaConfigParams : list) {
                if (TextUtils.isEmpty(dCAlibabaConfigParams.mac)) {
                    ALog.w(TAG, "startConfig params error. empty mac");
                } else {
                    Map map2 = dCAlibabaConfigParams.extraInfoMap;
                    if (map2 == null || !(map2.get(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE) instanceof EnrolleeMeshDeviceCacheModel)) {
                        List cachedModel = CacheCenter.getInstance().getCachedModel(CacheType.BLE_MESH_DISCOVERED_DEVICE, dCAlibabaConfigParams.mac);
                        meshDevice = (cachedModel == null || cachedModel.isEmpty() || cachedModel.get(0) == null) ? null : ((EnrolleeMeshDeviceCacheModel) cachedModel.get(0)).getMeshDevice();
                    } else {
                        meshDevice = ((EnrolleeMeshDeviceCacheModel) dCAlibabaConfigParams.extraInfoMap.get(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE)).getMeshDevice();
                    }
                    String meshDeviceUniqueIDByMac = DeviceInfoUtils.getMeshDeviceUniqueIDByMac(dCAlibabaConfigParams.mac);
                    if (meshDevice != null) {
                        meshDevice.getmUnprovisionedMeshNodeData().setConfigurationInfo(dCAlibabaConfigParams.configurationInfo);
                        meshDevice.setPk(dCAlibabaConfigParams.productKey);
                        this.unprovisionedBluetoothMeshDeviceList.add(meshDevice);
                        this.mDeviceMac2SubConfigParamsMap.put(meshDeviceUniqueIDByMac, new Pair<>(meshDevice, dCAlibabaConfigParams));
                        getDeviceName(meshDevice, dCAlibabaConfigParams);
                    } else {
                        ALog.w(TAG, "startConfig params error. empty unprovisionedBluetoothMeshDevice, mac: " + dCAlibabaConfigParams.mac);
                        LinkedHashMap linkedHashMap = new LinkedHashMap();
                        linkedHashMap.put(AlinkConstants.KEY_CACHE_MESH_DEVICE_UNIQUE_ID, meshDeviceUniqueIDByMac);
                        this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR);
                        this.provisionErrorInfo.setMsg("configParams error:").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR).setExtra(linkedHashMap);
                        provisionResultCallback(null);
                    }
                }
            }
        }
        List<UnprovisionedBluetoothMeshDevice> list2 = this.unprovisionedBluetoothMeshDeviceList;
        if (list2 == null || list2.size() <= 0) {
            return;
        }
        if (!this.provisionHasStarted.get()) {
            startProvision();
        } else if (this.useBatchProvisionStrategy.get()) {
            startMeshDeviceProvision(this.lastBatchProvisionNumber.get());
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void stopConfig() {
        this.mConfigCallback = null;
        this.provisionHasStopped.set(true);
        if (this.mForKeepHighDutyScanCallback != null) {
            BLEScannerProxy.getInstance().stopScan(this.mForKeepHighDutyScanCallback);
            this.mForKeepHighDutyScanCallback = null;
        }
        this.lastBatchProvisionNumber.set(0);
        TgMeshManager.getInstance().unregisterCallback(this.meshStatusCallback);
        TgMeshManager.getInstance().setOnReadyToBindHandler(null);
        TgScanManager.getInstance().stopGetRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan();
        this.provisionHasStarted.set(false);
        TgMeshManager.getInstance().stopAddNode();
    }

    public ConcurrentAppMeshStrategy(Context context) {
        this.mContext = null;
        this.meshProvisionStatus = 0;
        this.meshProvisionErrorMessage = null;
        this.provisionHasStarted = new AtomicBoolean(false);
        this.unprovisionedBluetoothMeshDeviceList = null;
        this.unprovisionedDeviceFound = new AtomicBoolean(false);
        this.mConfigCallback = null;
        this.provisionErrorInfo = null;
        this.provisionTimer = null;
        this.provisionHasStopped = new AtomicBoolean(false);
        this.mDeviceMac2SubConfigParamsMap = new LinkedHashMap();
        this.mSerialExecuteIndex = 0;
        this.useBatchProvisionStrategy = new AtomicBoolean(true);
        this.hasStartScanInBatchMode = new AtomicBoolean(false);
        this.lastBatchProvisionNumber = new AtomicInteger(0);
        this.meshStatusCallback = new G(this);
        this.onReadyToBindHandler = new H(this);
        this.mContext = context;
    }
}
