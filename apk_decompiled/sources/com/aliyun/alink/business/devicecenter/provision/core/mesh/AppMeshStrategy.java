package com.aliyun.alink.business.devicecenter.provision.core.mesh;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.mesh.MeshStatusCallback;
import com.alibaba.ailabs.iot.mesh.TgMeshManager;
import com.alibaba.ailabs.iot.mesh.TgScanManager;
import com.alibaba.ailabs.iot.mesh.UnprovisionedBluetoothMeshDevice;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import com.alibaba.fastjson.JSONArray;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepository;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.cache.EnrolleeMeshDeviceCacheModel;
import com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.model.FilterData;
import com.aliyun.alink.business.devicecenter.provision.core.A;
import com.aliyun.alink.business.devicecenter.provision.core.B;
import com.aliyun.alink.business.devicecenter.provision.core.C;
import com.aliyun.alink.business.devicecenter.provision.core.C0487u;
import com.aliyun.alink.business.devicecenter.provision.core.C0488v;
import com.aliyun.alink.business.devicecenter.provision.core.C0489w;
import com.aliyun.alink.business.devicecenter.provision.core.C0490x;
import com.aliyun.alink.business.devicecenter.provision.core.C0491y;
import com.aliyun.alink.business.devicecenter.provision.core.C0492z;
import com.aliyun.alink.business.devicecenter.provision.core.D;
import com.aliyun.alink.business.devicecenter.provision.core.E;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;
import com.aliyun.alink.business.devicecenter.utils.NetworkTypeUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_APP_MESH)
public class AppMeshStrategy extends BaseProvisionStrategy implements IConfigStrategy {
    public static String TAG = "AppMeshStrategy";
    public final int MAX_RETRY_COUNT;
    public AtomicBoolean isScanStartedFirstQuery;
    public Context mContext;
    public int mCurrentRetryCount;
    public DeviceInfo mDeviceInfo;
    public AtomicBoolean mInFilterProcess;
    public String meshProvisionErrorMessage;
    public int meshProvisionStatus;
    public MeshStatusCallback meshStatusCallback;
    public AtomicBoolean provisionHasStarted;
    public List<String> repeatPidArray;
    public UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice;
    public AtomicBoolean unprovisionedDeviceFound;

    /* JADX INFO: Access modifiers changed from: private */
    public interface a {
        void onFail(int i, String str);

        void onSuccess(Object obj);
    }

    public AppMeshStrategy() {
        this.mContext = null;
        this.meshProvisionStatus = 0;
        this.meshProvisionErrorMessage = null;
        this.provisionHasStarted = new AtomicBoolean(false);
        this.unprovisionedBluetoothMeshDevice = null;
        this.unprovisionedDeviceFound = new AtomicBoolean(false);
        this.mInFilterProcess = new AtomicBoolean(false);
        this.isScanStartedFirstQuery = new AtomicBoolean(true);
        this.mDeviceInfo = null;
        this.MAX_RETRY_COUNT = 3;
        this.mCurrentRetryCount = 1;
        this.meshStatusCallback = new C(this);
    }

    private void filterDevice(String str, a aVar) {
        FilterData filterData = new FilterData();
        filterData.setProductId(str);
        ArrayList arrayList = new ArrayList();
        arrayList.add(filterData);
        ProvisionRepository.cloudToFilterDevice(arrayList, new C0488v(this, aVar));
    }

    private String getDeviceName() {
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        if (dCAlibabaConfigParams == null) {
            return null;
        }
        if (!TextUtils.isEmpty(dCAlibabaConfigParams.deviceName)) {
            return this.mConfigParams.deviceName;
        }
        if (!TextUtils.isEmpty(this.mConfigParams.mac)) {
            DCAlibabaConfigParams dCAlibabaConfigParams2 = this.mConfigParams;
            dCAlibabaConfigParams2.deviceName = dCAlibabaConfigParams2.mac.replace(":", "").toLowerCase();
            return this.mConfigParams.deviceName;
        }
        if (TextUtils.isEmpty(this.unprovisionedBluetoothMeshDevice.getAddress())) {
            return null;
        }
        this.mConfigParams.deviceName = this.unprovisionedBluetoothMeshDevice.getAddress().replace(":", "").toLowerCase();
        return this.mConfigParams.deviceName;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getProvisionTimeoutErrorInfo() {
        this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT);
        String str = "[mobile=" + NetworkTypeUtils.isMobileNetwork(this.mContext) + ", WiFi=" + NetworkTypeUtils.isWiFi(this.mContext) + ", meshStatus= " + this.meshProvisionStatus + ", meshErrorMsg=" + this.meshProvisionErrorMessage + "].";
        if (NetworkTypeUtils.isMobileNetwork(this.mContext)) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_NE_NETWORK_UNAVAILABLE).setMsg("provision timeout, network unavailable " + str);
            return;
        }
        this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_MESH_DEVICE_PROVISION_FAIL_OR_TIMEOUT).setMsg("provision timeout, mesh device not success until now,  " + str);
    }

    private void pidReturnToPk(String str, a aVar) {
        ProvisionRepositoryV2.pidReturnToPk(str, new C0487u(this, aVar));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void provisionSuccess() {
        DeviceInfo deviceInfo = new DeviceInfo();
        String strValueOf = String.valueOf(this.unprovisionedBluetoothMeshDevice.getSigMeshProductID());
        deviceInfo.productId = strValueOf;
        deviceInfo.deviceName = getDeviceName();
        deviceInfo.comboMeshFlag = LinkType.ALI_APP_COMBO_MESH.equals(this.mConfigParams.linkType);
        deviceInfo.linkType = this.mConfigParams.linkType.getName();
        deviceInfo.deviceId = MeshParserUtils.bytesToHex(this.unprovisionedBluetoothMeshDevice.getUUID(), false).toLowerCase();
        if (!TextUtils.isEmpty(this.mConfigParams.productKey) || DCEnvHelper.isTgEnv()) {
            deviceInfo.productKey = this.mConfigParams.productKey;
            ALog.d(TAG, "provisionSuccess call");
            provisionResCallback(deviceInfo);
        } else {
            pidReturnToPk(strValueOf, new E(this, deviceInfo));
        }
        if (LinkType.ALI_APP_COMBO_MESH.equals(this.mConfigParams.linkType)) {
            return;
        }
        TgMeshManager.getInstance().stopAddNode();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startMeshDeviceProvision() {
        try {
            ALog.d(TAG, "TgMeshManager setOnReadyToBindHandler");
            if (this.provisionHasStarted.compareAndSet(false, true)) {
                TgMeshManager.getInstance().setOnReadyToBindHandler(new D(this));
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("TgMeshManager bindDevice dev=");
                sb.append(this.unprovisionedBluetoothMeshDevice);
                ALog.d(str, sb.toString());
                if (!LinkType.ALI_APP_COMBO_MESH.equals(this.mConfigParams.linkType)) {
                    TgMeshManager.getInstance().bindDevice(this.unprovisionedBluetoothMeshDevice);
                    return;
                }
                LinkedHashMap linkedHashMap = new LinkedHashMap();
                linkedHashMap.put("ssid", this.mConfigParams.ssid);
                linkedHashMap.put("password", this.mConfigParams.password);
                if (this.mConfigParams.regionInfo != null) {
                    linkedHashMap.put(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_REGION_INDEX, Byte.valueOf((byte) this.mConfigParams.regionInfo.shortRegionId));
                }
                ExtendedBluetoothDevice extendedBluetoothDevice = new ExtendedBluetoothDevice(this.unprovisionedBluetoothMeshDevice.getMeshScanResult());
                extendedBluetoothDevice.setConfigurationInfo(this.unprovisionedBluetoothMeshDevice.getmUnprovisionedMeshNodeData().getConfigurationInfo());
                TgMeshManager.getInstance().addDevice(extendedBluetoothDevice, linkedHashMap);
            }
        } catch (Exception e) {
            this.provisionErrorInfo = new DCErrorCode("SdkError", DCErrorCode.PF_PROVISION_APP_MESH_ERROR).setMsg("mesh sdk exception, state=" + this.meshProvisionStatus + ", msg=" + this.meshProvisionStatus + ", e=" + e).setSubcode(DCErrorCode.SUBCODE_MESH_SDK_PROVISION_EXCEPTION);
            provisionResultCallback(null);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startProvision() {
        getDeviceName();
        this.provisionHasStarted.set(false);
        this.provisionHasStopped.set(false);
        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
        this.provisionHasStopped.set(false);
        startProvisionTimer(new A(this));
        ALog.d(TAG, "TgMeshManager init.");
        if (TgMeshManager.getInstance().isInitialized()) {
            ALog.d(TAG, "TgMeshManager already inited, mesh sdk init success.");
            startMeshDeviceProvision();
        } else {
            ALog.d(TAG, "TgMeshManager need init.");
            TgMeshManager.getInstance().init(this.mContext, new B(this));
        }
        ALog.d(TAG, "TgMeshManager registerCallback callback=" + this.meshStatusCallback);
        TgMeshManager.getInstance().registerCallback(this.meshStatusCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startSupportDeviceProvision(UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice) {
        ALog.d(TAG, "Unprovisioned mesh device found, start filter, unprovisionedDeviceFound flag: " + this.unprovisionedDeviceFound.get());
        if (DeviceInfoUtils.isSupportFastProvisioningV2(MeshParserUtils.bytesToHex(unprovisionedBluetoothMeshDevice.getUUID(), false))) {
            filterDispatchDevice(unprovisionedBluetoothMeshDevice);
            return;
        }
        String strValueOf = String.valueOf(unprovisionedBluetoothMeshDevice.getSigMeshProductID());
        if (this.repeatPidArray.contains(strValueOf) && this.unprovisionedDeviceFound.get()) {
            return;
        }
        this.repeatPidArray.add(strValueOf);
        filterDevice(strValueOf, new C0490x(this, unprovisionedBluetoothMeshDevice));
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void continueConfig(Map map) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void doExtraPrepareWork(IConfigExtraCallback iConfigExtraCallback) {
    }

    public void filterDispatchDevice(UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice) {
        if (!this.mInFilterProcess.compareAndSet(false, true)) {
            ALog.e(TAG, "Cloud filter, current in filter process, return");
            return;
        }
        ArrayList arrayList = new ArrayList();
        if (unprovisionedBluetoothMeshDevice != null) {
            String strValueOf = String.valueOf(unprovisionedBluetoothMeshDevice.getSigMeshProductID());
            String lowerCase = MeshParserUtils.bytesToHex(unprovisionedBluetoothMeshDevice.getUUID(), false).toLowerCase();
            String authFlag = unprovisionedBluetoothMeshDevice.getAuthFlag();
            String lowerCase2 = unprovisionedBluetoothMeshDevice.getAuthDev().toLowerCase();
            String lowerCase3 = unprovisionedBluetoothMeshDevice.getRandom().toLowerCase();
            String strReplaceAll = unprovisionedBluetoothMeshDevice.getmUnprovisionedMeshNodeData().getDeviceMac().replaceAll(":", "");
            int rssi = unprovisionedBluetoothMeshDevice.getMeshScanResult().getRssi();
            if (TextUtils.isEmpty(strValueOf) || TextUtils.isEmpty(lowerCase) || TextUtils.isEmpty(authFlag) || TextUtils.isEmpty(lowerCase3) || TextUtils.isEmpty(lowerCase2)) {
                ALog.e(TAG, "app mesh query, params pid or deviceId or authFlag or random or authDev is null");
                this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR);
                this.provisionErrorInfo.setMsg("app mesh query, params pid or deviceId or authFlag or random or authDev is null").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
                provisionResultCallback(null);
                return;
            }
            HashMap map = new HashMap();
            map.put(AlinkConstants.KEY_SUB_DEVICE_ID, lowerCase);
            map.put(AlinkConstants.KEY_PRODUCT_ID, strValueOf);
            map.put(AlinkConstants.KEY_MAC, strReplaceAll);
            map.put("rssi", Integer.valueOf(rssi));
            if ("1".equals(unprovisionedBluetoothMeshDevice.getAuthFlag())) {
                map.put(AlinkConstants.KEY_AUTH_FLAG, true);
                map.put(AlinkConstants.KEY_AUTH_DEVICE, lowerCase2);
                map.put(AlinkConstants.KEY_RANDOM, lowerCase3);
                ALog.d(TAG, "filterDispatchDevice mac = " + strReplaceAll + "; authDevice = " + lowerCase2 + "; random = " + lowerCase3);
            }
            ALog.d(TAG, "filterDispatchDevice mac = rssi = " + rssi);
            arrayList.add(map);
        }
        ALog.d(TAG, "Cloud Filter, request size: " + arrayList.size() + ", chain: " + this);
        ProvisionRepository.getDiscoveredMeshDevice(null, 1, 200, Boolean.valueOf(this.isScanStartedFirstQuery.get()), arrayList, new JSONArray(), new C0491y(this, unprovisionedBluetoothMeshDevice));
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_APP_MESH.getName();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean hasExtraPrepareWork() {
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean isSupport() {
        return true;
    }

    public boolean needWiFiSsidPwd() {
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void preConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) {
    }

    public void startConcurrentAddDevice(List<DeviceInfo> list) {
        ALog.d(TAG, "startConcurrentAddDevice() call.");
        if (list == null || list.size() == 0) {
            ALog.e(TAG, "Illegal parameter, deviceInfos cannot be null");
            return;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        ArrayList arrayList = new ArrayList();
        for (DeviceInfo deviceInfo : list) {
            if (!TextUtils.isEmpty(deviceInfo.mac)) {
                linkedHashMap.put(DeviceInfoUtils.getMeshDeviceUniqueIDByMac(deviceInfo.mac), deviceInfo);
            }
            if (!TextUtils.isEmpty(deviceInfo.deviceId)) {
                arrayList.add(deviceInfo.deviceId);
            }
        }
        ALog.d(TAG, "To be provision device size: " + list.size());
        ProvisionRepositoryV2.provisionSLB(arrayList, new C0492z(this, linkedHashMap));
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void startConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) throws Exception {
        this.mCurrentRetryCount = 1;
        this.mConfigCallback = iConfigCallback;
        if (!(dCConfigParams instanceof DCAlibabaConfigParams)) {
            ALog.w(TAG, "startConfig params error.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR);
            this.provisionErrorInfo.setMsg("configParams error:").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        ALog.d(TAG, "startConfig() called with: callback = [" + iConfigCallback + "], configParams = [" + dCConfigParams + "]");
        this.mConfigParams = (DCAlibabaConfigParams) dCConfigParams;
        Map map = this.mConfigParams.extraInfoMap;
        if (map == null || !(map.get(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE) instanceof EnrolleeMeshDeviceCacheModel)) {
            List cachedModel = CacheCenter.getInstance().getCachedModel(CacheType.BLE_MESH_DISCOVERED_DEVICE, this.mConfigParams.mac);
            if (cachedModel != null && !cachedModel.isEmpty() && cachedModel.get(0) != null) {
                this.unprovisionedBluetoothMeshDevice = ((EnrolleeMeshDeviceCacheModel) cachedModel.get(0)).getMeshDevice();
            }
        } else {
            this.unprovisionedBluetoothMeshDevice = ((EnrolleeMeshDeviceCacheModel) this.mConfigParams.extraInfoMap.get(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE)).getMeshDevice();
        }
        UnprovisionedBluetoothMeshDevice unprovisionedBluetoothMeshDevice = this.unprovisionedBluetoothMeshDevice;
        if (unprovisionedBluetoothMeshDevice == null) {
            this.repeatPidArray = new ArrayList();
            this.unprovisionedDeviceFound.set(false);
            C0489w c0489w = new C0489w(this);
            if (LinkType.ALI_APP_COMBO_MESH.equals(this.mConfigParams.linkType)) {
                TgScanManager.getInstance().getRemoteSpecifiedPIDUnprovisionedComboMeshDeviceWithScan(this.mContext, this.mConfigParams.productId, DefaultLoadControl.DEFAULT_MAX_BUFFER_MS, c0489w);
                return;
            } else {
                TgScanManager.getInstance().getRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan(this.mContext, this.mConfigParams.productId, DefaultLoadControl.DEFAULT_MAX_BUFFER_MS, c0489w);
                return;
            }
        }
        unprovisionedBluetoothMeshDevice.getmUnprovisionedMeshNodeData().setConfigurationInfo(this.mConfigParams.configurationInfo);
        this.unprovisionedBluetoothMeshDevice.setPk(this.mConfigParams.productKey);
        String lowerCase = MeshParserUtils.bytesToHex(this.unprovisionedBluetoothMeshDevice.getUUID(), false).toLowerCase();
        if (!DeviceInfoUtils.isSupportFastProvisioningV2(lowerCase)) {
            startProvision();
            return;
        }
        LinkedList linkedList = new LinkedList();
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.deviceId = lowerCase;
        deviceInfo.mac = this.mConfigParams.mac;
        linkedList.add(deviceInfo);
        startConcurrentAddDevice(linkedList);
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void stopConfig() {
        this.provisionHasStopped.set(true);
        stopProvisionTimer();
        TgMeshManager.getInstance().unregisterCallback(this.meshStatusCallback);
        TgMeshManager.getInstance().setOnReadyToBindHandler(null);
        TgScanManager.getInstance().stopGetRemoteSpecifiedPIDUnprovisionedSigMeshDeviceWithScan();
        this.provisionHasStarted.set(true);
        TgMeshManager.getInstance().stopAddNode();
    }

    public AppMeshStrategy(Context context) {
        this.mContext = null;
        this.meshProvisionStatus = 0;
        this.meshProvisionErrorMessage = null;
        this.provisionHasStarted = new AtomicBoolean(false);
        this.unprovisionedBluetoothMeshDevice = null;
        this.unprovisionedDeviceFound = new AtomicBoolean(false);
        this.mInFilterProcess = new AtomicBoolean(false);
        this.isScanStartedFirstQuery = new AtomicBoolean(true);
        this.mDeviceInfo = null;
        this.MAX_RETRY_COUNT = 3;
        this.mCurrentRetryCount = 1;
        this.meshStatusCallback = new C(this);
        this.mContext = context;
    }
}
