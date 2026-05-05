package com.aliyun.alink.business.devicecenter.api.add;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.activate.DeviceActivationRtosBind;
import com.aliyun.alink.business.devicecenter.activate.IActivateRtosDeviceCallback;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepositoryV2;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.cache.ProvisionDeviceInfoCache;
import com.aliyun.alink.business.devicecenter.channel.http.mtop.data.BindIotDeviceResult;
import com.aliyun.alink.business.devicecenter.channel.http.top.DefaultTopRtosBindRequestService;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.model.ProvisionSLBItem;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.CompatUtil;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;
import com.aliyun.alink.business.devicecenter.utils.NetworkTypeUtils;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.business.devicecenter.utils.WifiManagerUtil;
import com.aliyun.alink.linksdk.connectsdk.ApiCallBack;
import datasource.bean.ConfigurationData;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AddDeviceBiz implements IAddDeviceBiz {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static IAddDeviceBiz f3243a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public DeviceInfo f3244b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public AddDeviceState f3245c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f3246d = 60;
    public IAddDeviceListener e = null;
    public DCAlibabaConfigParams f = null;
    public int g = 0;
    public boolean h = false;
    public final Object i = new Object();

    /* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz$4, reason: invalid class name */
    public class AnonymousClass4 implements IAddDeviceListener {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public final /* synthetic */ IConcurrentAddDeviceListener f3255a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public final /* synthetic */ DeviceInfo f3256b;

        @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
        public void onPreCheck(boolean z, DCErrorCode dCErrorCode) {
            IConcurrentAddDeviceListener iConcurrentAddDeviceListener = this.f3255a;
            if (iConcurrentAddDeviceListener != null) {
                iConcurrentAddDeviceListener.onPreCheck(this.f3256b, z, dCErrorCode);
            }
        }

        @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
        public void onProvisionPrepare(int i) {
            IConcurrentAddDeviceListener iConcurrentAddDeviceListener = this.f3255a;
            if (iConcurrentAddDeviceListener != null) {
                iConcurrentAddDeviceListener.onProvisionPrepare(this.f3256b, i);
            }
        }

        @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
        public void onProvisionStatus(ProvisionStatus provisionStatus) {
            IConcurrentAddDeviceListener iConcurrentAddDeviceListener = this.f3255a;
            if (iConcurrentAddDeviceListener != null) {
                iConcurrentAddDeviceListener.onProvisionStatus(this.f3256b, provisionStatus);
            }
        }

        @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
        public void onProvisionedResult(boolean z, DeviceInfo deviceInfo, DCErrorCode dCErrorCode) {
            if (deviceInfo == null) {
                deviceInfo = this.f3256b;
            } else {
                deviceInfo.mac = this.f3256b.mac;
            }
            ALog.w("AddDeviceBiz", deviceInfo.mac + ", on Provision result: " + z);
            IConcurrentAddDeviceListener iConcurrentAddDeviceListener = this.f3255a;
            if (iConcurrentAddDeviceListener != null) {
                iConcurrentAddDeviceListener.onProvisionedResult(z, deviceInfo, dCErrorCode);
            }
        }

        @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceListener
        public void onProvisioning() {
            IConcurrentAddDeviceListener iConcurrentAddDeviceListener = this.f3255a;
            if (iConcurrentAddDeviceListener != null) {
                iConcurrentAddDeviceListener.onProvisioning(this.f3256b);
            }
        }
    }

    /* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz$9, reason: invalid class name */
    static /* synthetic */ class AnonymousClass9 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f3266a = new int[AddDeviceState.values().length];

        static {
            try {
                f3266a[AddDeviceState.AddStatePrechecking.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3266a[AddDeviceState.AddStateProvisionPreparing.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3266a[AddDeviceState.AddStateProvisioning.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3266a[AddDeviceState.AddStateProvisionOver.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private class MyConfigCallback implements IConfigCallback {
        public MyConfigCallback() {
        }

        @Override // com.aliyun.alink.business.devicecenter.config.IDCFailCallback
        public void onFailure(DCErrorCode dCErrorCode) {
            ALog.e("AddDeviceBiz", "onFailure provision fail Callback, " + dCErrorCode);
            AddDeviceBiz.this.f3245c = AddDeviceState.AddStateProvisionOver;
            AddDeviceBiz addDeviceBiz = AddDeviceBiz.this;
            addDeviceBiz.a(addDeviceBiz.f3245c, -1, false, null, dCErrorCode);
        }

        @Override // com.aliyun.alink.business.devicecenter.config.IConfigCallback
        public void onStatus(final ProvisionStatus provisionStatus) {
            ALog.i("AddDeviceBiz", "onStatus status=" + provisionStatus + ",addDeviceListener=" + AddDeviceBiz.this.e);
            DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz.MyConfigCallback.1
                @Override // java.lang.Runnable
                public void run() {
                    if (AddDeviceBiz.this.e != null) {
                        AddDeviceBiz.this.e.onProvisionStatus(provisionStatus);
                    }
                    if (provisionStatus == ProvisionStatus.MESH_COMBO_WIFI_CONNECT_CLOUD_STATUS) {
                        AddDeviceBiz.this.f3245c = AddDeviceState.AddStateProvisionOver;
                        AddDeviceBiz.this.stopAddDevice();
                    }
                }
            });
        }

        @Override // com.aliyun.alink.business.devicecenter.config.IConfigCallback
        public void onSuccess(DeviceInfo deviceInfo) {
            try {
                if (AddDeviceBiz.this.f3245c != AddDeviceState.AddStateProvisioning && !AlinkHelper.isBatch(AddDeviceBiz.this.f)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("not in provisioning state, not batch provision mode, ignore. curState=");
                    sb.append(AddDeviceBiz.this.f3245c);
                    ALog.d("AddDeviceBiz", sb.toString());
                    return;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("success,info=");
                sb2.append(deviceInfo);
                ALog.i("AddDeviceBiz", sb2.toString());
                if (deviceInfo == null) {
                    return;
                }
                AddDeviceBiz.this.f3245c = AddDeviceState.AddStateProvisionOver;
                AddDeviceBiz.this.a(AddDeviceBiz.this.f3245c, -1, true, deviceInfo, null);
            } catch (Exception e) {
                ALog.w("AddDeviceBiz", "onSuccess recv Callback，but parse error,e = " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public static IAddDeviceBiz getInstance() {
        if (f3243a == null) {
            synchronized (AddDeviceBiz.class) {
                if (f3243a == null) {
                    f3243a = new AddDeviceBiz();
                }
            }
        }
        return f3243a;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void continueProvision(Map map) {
        ALog.d("AddDeviceBiz", "continueProvision() called with: provisionParams = [" + map + "], curState=" + this.f3245c);
        AddDeviceState addDeviceState = this.f3245c;
        if (addDeviceState == null || addDeviceState == AddDeviceState.AddStateProvisionOver) {
            return;
        }
        DeviceCenterBiz.getInstance().continueConfig(map);
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public String getCurrentSsid(Context context) {
        return AlinkHelper.getWifiSsid(context);
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public AddDeviceState getProcedureState() {
        return this.f3245c;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public int getWifiRssid(Context context) {
        if (context != null) {
            return new WifiManagerUtil(context).getWifiRssid();
        }
        throw new IllegalArgumentException("context=null");
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public String getWifiType(Context context) {
        if (context != null) {
            return new WifiManagerUtil(context).getWifiType();
        }
        throw new IllegalArgumentException("context=null");
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void setAliProvisionMode(String str) {
        ALog.i("AddDeviceBiz", "setAliProvisionMode() call. linkType=" + str);
        if (this.f3244b == null) {
            ALog.w("AddDeviceBiz", "setAliProvisionMode error, deviceInfo=null.");
            throw new IllegalStateException("call setDevice first");
        }
        if (!a(str)) {
            throw new IllegalStateException("linkType invalid.");
        }
        this.f3244b.linkType = str;
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void setDevice(DeviceInfo deviceInfo) {
        ALog.i("AddDeviceBiz", "setDevice() call. devInfo=" + deviceInfo);
        if (deviceInfo == null) {
            ALog.e("AddDeviceBiz", "setDevice(),emtpy");
        } else {
            this.f3244b = deviceInfo;
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void setExtraInfo(Map map) {
        ALog.i("AddDeviceBiz", "setExtraInfo called() extraInfo=" + map);
        DeviceCenterBiz.getInstance().setExtraInfo(map);
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void setProvisionTimeOut(int i) {
        ALog.d("AddDeviceBiz", "setProvisionTimeOut()  call. timeout=" + i);
        if (i < 0) {
            this.f3246d = -1;
        } else if (i < 60) {
            this.f3246d = 58;
        } else {
            this.f3246d = i - 2;
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void startAddDevice(Context context, final IAddDeviceListener iAddDeviceListener) {
        String str;
        boolean z = LinkType.ALI_APP_MESH.getName().equals(this.f3244b.linkType) || LinkType.ALI_GATEWAY_MESH.getName().equals(this.f3244b.linkType);
        boolean zIsEmpty = TextUtils.isEmpty(this.f3244b.mac);
        if (!zIsEmpty && DeviceInfoUtils.isSupportFastProvisioningV2(this.f3244b.deviceId)) {
            DeviceInfo deviceInfo = this.f3244b;
            deviceInfo.subDeviceId = deviceInfo.deviceId;
            deviceInfo.configurationInfo = ConcurrentAddDeviceBiz.getInstance().getConfigurationInfoCacheValue(this.f3244b.deviceId);
        }
        if (z && !zIsEmpty) {
            ALog.i("AddDeviceBiz", "startAddDevice to startConcurrentAddDevice call.");
            ArrayList arrayList = new ArrayList(1);
            arrayList.add(this.f3244b);
            startConcurrentAddDevice(context, arrayList, new IConcurrentAddDeviceListener() { // from class: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz.6
                @Override // com.aliyun.alink.business.devicecenter.api.add.IConcurrentAddDeviceListener
                public void onPreCheck(DeviceInfo deviceInfo2, boolean z2, DCErrorCode dCErrorCode) {
                    IAddDeviceListener iAddDeviceListener2 = iAddDeviceListener;
                    if (iAddDeviceListener2 != null) {
                        iAddDeviceListener2.onPreCheck(z2, dCErrorCode);
                    }
                }

                @Override // com.aliyun.alink.business.devicecenter.api.add.IConcurrentAddDeviceListener
                public void onProvisionPrepare(DeviceInfo deviceInfo2, int i) {
                    IAddDeviceListener iAddDeviceListener2 = iAddDeviceListener;
                    if (iAddDeviceListener2 != null) {
                        iAddDeviceListener2.onProvisionPrepare(i);
                    }
                }

                @Override // com.aliyun.alink.business.devicecenter.api.add.IConcurrentAddDeviceListener
                public void onProvisionStatus(DeviceInfo deviceInfo2, ProvisionStatus provisionStatus) {
                    IAddDeviceListener iAddDeviceListener2 = iAddDeviceListener;
                    if (iAddDeviceListener2 != null) {
                        iAddDeviceListener2.onProvisionStatus(provisionStatus);
                    }
                }

                @Override // com.aliyun.alink.business.devicecenter.api.add.IConcurrentAddDeviceListener
                public void onProvisionedResult(boolean z2, DeviceInfo deviceInfo2, DCErrorCode dCErrorCode) {
                    IAddDeviceListener iAddDeviceListener2 = iAddDeviceListener;
                    if (iAddDeviceListener2 != null) {
                        iAddDeviceListener2.onProvisionedResult(z2, deviceInfo2, dCErrorCode);
                    }
                }

                @Override // com.aliyun.alink.business.devicecenter.api.add.IConcurrentAddDeviceListener
                public void onProvisioning(DeviceInfo deviceInfo2) {
                    IAddDeviceListener iAddDeviceListener2 = iAddDeviceListener;
                    if (iAddDeviceListener2 != null) {
                        iAddDeviceListener2.onProvisioning();
                    }
                }
            });
            return;
        }
        ALog.i("AddDeviceBiz", "startAddDevice() call.");
        if (context == null) {
            ALog.e("AddDeviceBiz", "startAddDevice context=null.");
            throw new RuntimeException("startAddDeviceParamContextNull");
        }
        AddDeviceState addDeviceState = this.f3245c;
        if (addDeviceState != null && addDeviceState != AddDeviceState.AddStateProvisionOver) {
            ALog.e("AddDeviceBiz", "startAddDevice running, return.");
            if (iAddDeviceListener != null) {
                DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz.7
                    @Override // java.lang.Runnable
                    public void run() {
                        if (iAddDeviceListener != null) {
                            iAddDeviceListener.onProvisionedResult(false, null, new DCErrorCode("USER_INVOKE_ERROR", DCErrorCode.PF_USER_INVOKE_ERROR).setSubcode(DCErrorCode.SUBCODE_UIE_PROVISION_RUNNING).setMsg("startAddDevice running, return."));
                        }
                    }
                });
                return;
            }
            return;
        }
        DeviceCenterBiz.getInstance().setAppContext(context);
        PerformanceLog.trace("AddDeviceBiz", "startProvision");
        this.e = iAddDeviceListener;
        this.f3245c = AddDeviceState.AddStatePrechecking;
        if (this.f3244b == null || !(LinkType.ALI_APP_MESH.getName().equalsIgnoreCase(this.f3244b.linkType) || LinkType.ALI_APP_COMBO_MESH.getName().equalsIgnoreCase(this.f3244b.linkType) || this.f3244b.isValid())) {
            ALog.e("AddDeviceBiz", "startAddDevice, params error");
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_PE_PRODUCTKEY_EMPTY).setMsg("pkError"));
            return;
        }
        if (!NetworkTypeUtils.isWiFi(DeviceCenterBiz.getInstance().getAppContext()) && ((!LinkType.ALI_PHONE_AP.getName().equalsIgnoreCase(this.f3244b.linkType) || !CompatUtil.isAlinkPhoneApConfigStrategyFromOldHotspotFlow()) && !LinkType.ALI_GENIE_SOUND_BOX.getName().equalsIgnoreCase(this.f3244b.linkType) && !LinkType.ALI_APP_MESH.getName().equalsIgnoreCase(this.f3244b.linkType) && !LinkType.ALI_APP_COMBO_MESH.getName().equalsIgnoreCase(this.f3244b.linkType))) {
            ALog.w("AddDeviceBiz", "startAddDevice, Wifi not enabled.");
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode("NETWORK_ERROR", DCErrorCode.PF_NETWORK_ERROR).setSubcode(DCErrorCode.SUBCODE_NE_WIFI_NOT_CONNECTED).setMsg("wifiNotConnected"));
            return;
        }
        ProvisionDeviceInfoCache.getInstance().clearCache();
        CacheCenter.getInstance().clearCache(CacheType.APP_SEND_TOKEN);
        if (AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_4.equals(this.f3244b.devType)) {
            ALog.e("AddDeviceBiz", "startAddDevice, devType error, " + this.f3244b.devType + " don't support.");
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_PE_DEVICETYPE_ERROR).setMsg("devTypeError"));
            return;
        }
        RegionInfo regionInfo = this.f3244b.regionInfo;
        if (regionInfo != null && (str = regionInfo.mqttUrl) != null && str.length() > 256) {
            ALog.w("AddDeviceBiz", "startAddDevice, mqttUrl is too long.");
        }
        this.f = this.f3244b.getDCConfigParams();
        DCAlibabaConfigParams dCAlibabaConfigParams = this.f;
        if (dCAlibabaConfigParams == null) {
            ALog.e("AddDeviceBiz", "startAddDevice, linkType not support or not match addDeviceFrom.");
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR).setMsg("dcParamsError"));
            return;
        }
        if (!ProtocolVersion.isValidVersion(dCAlibabaConfigParams.protocolVersion)) {
            ALog.e("AddDeviceBiz", "startAddDevice, protocol version invalid.");
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_PE_VERSION_INVALID).setMsg("protocolVersionError"));
            return;
        }
        DCUserTrack.resetTrackData();
        DCUserTrack.addTrackData(AlinkConstants.KEY_START_TIME_PROVISION, String.valueOf(System.currentTimeMillis()));
        DCUserTrack.addTrackData(AlinkConstants.KEY_WIFI_TYPE, getWifiType(context));
        DCUserTrack.addTrackData(AlinkConstants.KEY_HAS_SIM, String.valueOf(NetworkTypeUtils.hasSimCard(context)));
        DeviceCenterBiz.getInstance().selectStrategy(this.f.linkType);
        a(this.f3245c, -1, true, null, null);
        if (DeviceCenterBiz.getInstance().needWiFiSsidPwd()) {
            this.f3245c = AddDeviceState.AddStateProvisionPreparing;
            a(this.f3245c, 1, true, null, null);
            return;
        }
        try {
            this.f3245c = AddDeviceState.AddStateProvisioning;
            this.f.timeout = this.f3246d;
            a(this.f3245c, -1, true, null, null);
            DeviceCenterBiz.getInstance().startConfig(new MyConfigCallback(), this.f);
        } catch (Exception e) {
            e.printStackTrace();
            ALog.e("AddDeviceBiz", "startAddDevice,provisioning error , e" + e.toString());
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode("SDKError", DCErrorCode.PF_SDK_ERROR).setSubcode(DCErrorCode.SUBCODE_SKE_START_CONFIG_EXCEPTION).setMsg("startConfig" + e));
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void startBindDevice(Context context, IAddDeviceListener iAddDeviceListener) {
        if (this.f3244b == null) {
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode("deviceInfo is null", 0));
            return;
        }
        DeviceCenterBiz.getInstance().setAppContext(context);
        this.e = iAddDeviceListener;
        this.f3245c = AddDeviceState.AddStatePrechecking;
        a(this.f3245c, -1, true, null, null);
        this.f3245c = AddDeviceState.AddStateProvisioning;
        a(this.f3245c, -1, true, null, null);
        Map<String, Object> extraDeviceInfo = this.f3244b.getExtraDeviceInfo();
        DeviceActivationRtosBind.getInstance().init(new DefaultTopRtosBindRequestService());
        DeviceActivationRtosBind.getInstance().activateDevice(extraDeviceInfo, new IActivateRtosDeviceCallback() { // from class: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz.5
            @Override // com.aliyun.alink.business.devicecenter.activate.IActivateRtosDeviceCallback
            public void onFailed(DCErrorCode dCErrorCode) {
                AddDeviceBiz.this.f3245c = AddDeviceState.AddStateProvisionOver;
                AddDeviceBiz addDeviceBiz = AddDeviceBiz.this;
                addDeviceBiz.a(addDeviceBiz.f3245c, -1, false, null, dCErrorCode);
            }

            @Override // com.aliyun.alink.business.devicecenter.activate.IActivateRtosDeviceCallback
            public void onSuccess(BindIotDeviceResult bindIotDeviceResult) {
                AddDeviceBiz.this.f3245c = AddDeviceState.AddStateProvisionOver;
                if (bindIotDeviceResult != null) {
                    AddDeviceBiz.this.f3244b.deviceId = bindIotDeviceResult.getDevId();
                    AddDeviceBiz addDeviceBiz = AddDeviceBiz.this;
                    addDeviceBiz.a(addDeviceBiz.f3245c, -1, true, AddDeviceBiz.this.f3244b, null);
                }
            }
        });
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void startConcurrentAddDevice(final Context context, final List<DeviceInfo> list, final IConcurrentAddDeviceListener iConcurrentAddDeviceListener) {
        ALog.d("AddDeviceBiz", "startConcurrentAddDevice() call.");
        if (list == null || list.size() == 0) {
            ALog.e("AddDeviceBiz", "Illegal parameter, deviceInfos cannot be null");
            return;
        }
        if (list.size() == 1 && list.get(0) != null && !LinkType.ALI_GATEWAY_MESH.getName().equals(list.get(0).linkType)) {
            if (list.get(0).configurationInfo == null) {
                ALog.d("AddDeviceBiz", "reProvision SLB configurationInfo is null ");
            } else {
                ALog.d("AddDeviceBiz", "reProvision SLB configurationInfo serverConfirmation : " + list.get(0).configurationInfo.getServerConfirmation() + "; AppKey : " + list.get(0).configurationInfo.getConfigResultMap().getSigmeshKeys().get(0).getProvisionAppKeys().get(0).getAppKey());
            }
            if (ConcurrentAddDeviceBiz.getInstance().getConfigurationInfoCache().size() > 0 && ConcurrentAddDeviceBiz.getInstance().getConfigurationInfoCacheValue(list.get(0).deviceId) != null && DeviceInfoUtils.isSupportFastProvisioningV2(list.get(0).deviceId)) {
                list.get(0).subDeviceId = list.get(0).deviceId;
                list.get(0).configurationInfo = ConcurrentAddDeviceBiz.getInstance().getConfigurationInfoCacheValue(list.get(0).deviceId);
                list.get(0).authFlag = true;
                a(context, list, iConcurrentAddDeviceListener);
                return;
            }
            if (!DeviceInfoUtils.isSupportFastProvisioningV2(list.get(0).deviceId)) {
                a(context, list, iConcurrentAddDeviceListener);
                return;
            }
        }
        final LinkedHashMap linkedHashMap = new LinkedHashMap();
        ArrayList arrayList = new ArrayList();
        for (DeviceInfo deviceInfo : list) {
            if (!TextUtils.isEmpty(deviceInfo.mac)) {
                linkedHashMap.put(DeviceInfoUtils.getMeshDeviceUniqueIDByMac(deviceInfo.mac), deviceInfo);
            }
            if (!TextUtils.isEmpty(deviceInfo.deviceId)) {
                arrayList.add(deviceInfo.deviceId);
            }
        }
        ALog.d("AddDeviceBiz", "To be provision device size: " + list.size());
        ProvisionRepositoryV2.provisionSLB(arrayList, new ApiCallBack<List<ProvisionSLBItem>>() { // from class: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz.1
            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onFail(int i, String str) {
                ALog.e("AddDeviceBiz", "provision SLB, error: " + str);
                AddDeviceBiz.this.a(context, list, iConcurrentAddDeviceListener);
            }

            @Override // com.aliyun.alink.linksdk.connectsdk.BaseCallBack
            public void onSuccess(List<ProvisionSLBItem> list2) {
                ALog.i("AddDeviceBiz", "############### Provision SLB Strategy ###############");
                ALog.d("AddDeviceBiz", "SLB result size: " + list2.size());
                for (ProvisionSLBItem provisionSLBItem : list2) {
                    if (TextUtils.isEmpty(provisionSLBItem.getMac())) {
                        ALog.w("AddDeviceBiz", "Illegal SLB result for device: " + provisionSLBItem.getDeviceName() + ", mac address cannot be null");
                    } else {
                        DeviceInfo deviceInfo2 = (DeviceInfo) linkedHashMap.get(DeviceInfoUtils.getMeshDeviceUniqueIDByMac(provisionSLBItem.getMac()));
                        if (provisionSLBItem.getConfigurationInfo() != null && !TextUtils.isEmpty(provisionSLBItem.getConfirmCloud())) {
                            provisionSLBItem.getConfigurationInfo().setServerConfirmation(provisionSLBItem.getConfirmCloud());
                        }
                        if (deviceInfo2 != null) {
                            deviceInfo2.authFlag = provisionSLBItem.isAuthFlag();
                            deviceInfo2.deviceId = provisionSLBItem.getSubDeviceId();
                            deviceInfo2.random = provisionSLBItem.getRandom();
                            deviceInfo2.subDeviceId = provisionSLBItem.getSubDeviceId();
                            deviceInfo2.authDevice = provisionSLBItem.getAuthDevice();
                            deviceInfo2.confirmCloud = provisionSLBItem.getConfirmCloud();
                            deviceInfo2.configurationInfo = provisionSLBItem.getConfigurationInfo();
                            ConfigurationData configurationData = deviceInfo2.configurationInfo;
                            if (configurationData != null) {
                                configurationData.setServerConfirmation(provisionSLBItem.getConfirmCloud());
                            }
                            if ("app".equals(provisionSLBItem.getDiscoveredSource())) {
                                ALog.d("AddDeviceBiz", String.format("%s: app, previous: %s", provisionSLBItem.getMac(), deviceInfo2.linkType));
                                deviceInfo2.linkType = LinkType.ALI_APP_MESH.getName();
                                if (provisionSLBItem.getConfigurationInfo() != null) {
                                    deviceInfo2.configurationInfo = provisionSLBItem.getConfigurationInfo();
                                    ConcurrentAddDeviceBiz.getInstance().setConfigurationInfoCache(deviceInfo2.deviceId, deviceInfo2.configurationInfo);
                                }
                            } else if ("meshGw".equals(provisionSLBItem.getDiscoveredSource())) {
                                ALog.d("AddDeviceBiz", String.format("%s: meshGw, gwIotId: %s, previous: %s", provisionSLBItem.getMac(), provisionSLBItem.getGatewayIotId(), deviceInfo2.linkType));
                                deviceInfo2.linkType = LinkType.ALI_GATEWAY_MESH.getName();
                                deviceInfo2.regIotId = provisionSLBItem.getGatewayIotId();
                                deviceInfo2.iotId = provisionSLBItem.getIotId();
                                deviceInfo2.configurationInfo = provisionSLBItem.getConfigurationInfo();
                            } else {
                                ALog.w("AddDeviceBiz", "Unknown discoverdSource " + provisionSLBItem.getDiscoveredSource());
                            }
                        } else {
                            ALog.w("AddDeviceBiz", "Cannot find provision info for device: " + provisionSLBItem.getDeviceName());
                        }
                    }
                }
                ALog.i("AddDeviceBiz", "############### Provision SLB Strategy End ###############");
                AddDeviceBiz.this.a(context, list, iConcurrentAddDeviceListener);
            }
        });
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void stopAddDevice() {
        ALog.i("AddDeviceBiz", "stopAddDevice() call.");
        this.e = null;
        this.f = null;
        setProvisionTimeOut(60);
        this.f3245c = AddDeviceState.AddStateProvisionOver;
        try {
            DeviceCenterBiz.getInstance().stopConfig();
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w("AddDeviceBiz", "stopProvision,error," + e);
        }
        DeviceCenterBiz.getInstance().setExtraInfo(null);
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void stopConcurrentAddDevice() {
        this.g = 0;
        stopAddDevice();
        ConcurrentAddDeviceBiz.getInstance().stopConfig();
        ConcurrentGateAddDeviceBiz.getInstance().stopConfig();
    }

    @Override // com.aliyun.alink.business.devicecenter.api.add.IAddDeviceBiz
    public void toggleProvision(String str, String str2, int i) {
        ALog.i("AddDeviceBiz", "toggleProvision() call. ssid= " + str + ", len(p)=" + StringUtils.getStringLength(str2) + ", timeout =" + i);
        PerformanceLog.trace("AddDeviceBiz", AlinkConstants.KEY_TOGGLE_PROVISION);
        if (TextUtils.isEmpty(str)) {
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_PE_SSID_EMPTY).setMsg("ssidEmpty"));
            return;
        }
        DeviceInfo deviceInfo = this.f3244b;
        if (deviceInfo == null || !(deviceInfo.isValid() || LinkType.ALI_APP_COMBO_MESH.getName().equalsIgnoreCase(this.f3244b.linkType))) {
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_PE_PRODUCTKEY_EMPTY).setMsg("tpDeviceInfoInvalid"));
            return;
        }
        if (this.f == null) {
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_WRONG_CALL).setMsg("tpProvisionParamsNull"));
            return;
        }
        DeviceCenterBiz.getInstance().selectStrategy(this.f.linkType);
        if (!DeviceCenterBiz.getInstance().needWiFiSsidPwd()) {
            ALog.w("AddDeviceBiz", "do not need to call this interface for " + this.f.linkType);
            return;
        }
        DCUserTrack.addTrackData(AlinkConstants.KEY_TOGGLE_PROVISION, String.valueOf(System.currentTimeMillis()));
        DCAlibabaConfigParams dCAlibabaConfigParams = this.f;
        dCAlibabaConfigParams.ssid = str;
        dCAlibabaConfigParams.password = str2;
        try {
            setProvisionTimeOut(i);
            this.f.timeout = this.f3246d;
            this.f3245c = AddDeviceState.AddStateProvisioning;
            a(this.f3245c, -1, true, null, null);
            DeviceCenterBiz.getInstance().startConfig(new MyConfigCallback(), this.f);
        } catch (Exception e) {
            e.printStackTrace();
            ALog.e("AddDeviceBiz", "toggleProvision,provisioning error , e" + e);
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            a(this.f3245c, -1, false, null, new DCErrorCode("SDKError", DCErrorCode.PF_SDK_ERROR).setMsg("startConfigException=" + e));
        }
    }

    public final void a(Context context, List<DeviceInfo> list, final IConcurrentAddDeviceListener iConcurrentAddDeviceListener) {
        LinkedList linkedList = new LinkedList();
        Iterator<DeviceInfo> it = list.iterator();
        while (it.hasNext()) {
            final DeviceInfo next = it.next();
            if (!TextUtils.isEmpty(next.mac) && (LinkType.ALI_GATEWAY_MESH.getName().equalsIgnoreCase(next.linkType) || LinkType.ALI_APP_MESH.getName().equalsIgnoreCase(next.linkType))) {
                if (LinkType.ALI_GATEWAY_MESH.getName().equalsIgnoreCase(next.linkType)) {
                    linkedList.add(next);
                    it.remove();
                }
            } else {
                DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (iConcurrentAddDeviceListener != null) {
                            iConcurrentAddDeviceListener.onProvisionedResult(false, next, new DCErrorCode("USER_INVOKE_ERROR", DCErrorCode.PF_PARAMS_ERROR).setSubcode(60805).setMsg("startAddDevice running, return."));
                        }
                    }
                });
            }
        }
        if (list.size() > 0) {
            ConcurrentAddDeviceBiz.getInstance().a(new IConcurrentAddDeviceStatusListener() { // from class: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz.3
                @Override // com.aliyun.alink.business.devicecenter.api.add.IConcurrentAddDeviceStatusListener
                public void onIdle() {
                }
            });
            ConcurrentAddDeviceBiz.getInstance().a(context, list, iConcurrentAddDeviceListener);
        }
        if (linkedList.size() > 0) {
            ALog.d("AddDeviceBiz", "startConcurrentAddDeviceInner onResponse: 开始急速配网");
            ConcurrentGateAddDeviceBiz.getInstance().a(context, linkedList, iConcurrentAddDeviceListener);
        }
    }

    public final boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        for (LinkType linkType : LinkType.values()) {
            if (str.equals(linkType.getName())) {
                return true;
            }
        }
        return false;
    }

    public final void a(final AddDeviceState addDeviceState, final int i, final boolean z, final DeviceInfo deviceInfo, final DCErrorCode dCErrorCode) {
        if (addDeviceState == AddDeviceState.AddStateProvisionOver && !z && dCErrorCode != null) {
            ALog.e("AddDeviceBiz", "state=" + addDeviceState + ",isSuccess=" + z + ",info=" + deviceInfo + ",error=" + dCErrorCode);
        } else {
            ALog.i("AddDeviceBiz", "state=" + addDeviceState + ",isSuccess=" + z + ",info=" + deviceInfo + ",error=" + dCErrorCode);
        }
        DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.api.add.AddDeviceBiz.8
            @Override // java.lang.Runnable
            public void run() {
                switch (AnonymousClass9.f3266a[addDeviceState.ordinal()]) {
                    case 1:
                        if (AddDeviceBiz.this.e != null) {
                            AddDeviceBiz.this.e.onPreCheck(z, dCErrorCode);
                        }
                        break;
                    case 2:
                        if (AddDeviceBiz.this.e != null) {
                            AddDeviceBiz.this.e.onProvisionPrepare(i);
                        }
                        break;
                    case 3:
                        if (AddDeviceBiz.this.e != null) {
                            AddDeviceBiz.this.e.onProvisioning();
                        }
                        break;
                    case 4:
                        if (z) {
                            AddDeviceBiz.this.a(deviceInfo);
                        } else {
                            AddDeviceBiz.this.a(dCErrorCode);
                        }
                        String[] strArr = new String[2];
                        strArr[0] = "result";
                        strArr[1] = z ? "success" : "fail";
                        PerformanceLog.trace("AddDeviceBiz", "provisionResult", PerformanceLog.getJsonObject(strArr));
                        ALog.d("AddDeviceBiz", "onProvisionedResult addDeviceListener=" + AddDeviceBiz.this.e);
                        if (AddDeviceBiz.this.e != null) {
                            AddDeviceBiz.this.e.onProvisionedResult(z, deviceInfo, dCErrorCode);
                        }
                        if (!AddDeviceBiz.this.h) {
                            if ((!AlinkHelper.isBatch(AddDeviceBiz.this.f) || !z) && !deviceInfo.comboMeshFlag) {
                                AddDeviceBiz.this.stopAddDevice();
                            }
                        }
                        break;
                }
            }
        });
    }

    public final void a(Object obj) {
        ALog.d("AddDeviceBiz", "provisionTrack obj=" + obj);
        try {
            if (this.f3244b != null && !TextUtils.isEmpty(this.f3244b.productKey)) {
                String[] strArr = new String[2];
                strArr[0] = AlinkConstants.KEY_PK;
                strArr[1] = this.f3244b.productKey;
                DCUserTrack.addTrackData(strArr);
            }
            if (this.f3244b != null && !TextUtils.isEmpty(this.f3244b.deviceName)) {
                String[] strArr2 = new String[2];
                strArr2[0] = AlinkConstants.KEY_DN;
                strArr2[1] = this.f3244b.deviceName;
                DCUserTrack.addTrackData(strArr2);
            }
            String[] strArr3 = new String[2];
            strArr3[0] = AlinkConstants.KEY_END_TIME_PROVISION;
            strArr3[1] = String.valueOf(System.currentTimeMillis());
            DCUserTrack.addTrackData(strArr3);
            this.f3245c = AddDeviceState.AddStateProvisionOver;
            if (obj instanceof DeviceInfo) {
                String[] strArr4 = new String[2];
                strArr4[0] = AlinkConstants.KEY_PK;
                strArr4[1] = ((DeviceInfo) obj).productKey;
                DCUserTrack.addTrackData(strArr4);
                String[] strArr5 = new String[2];
                strArr5[0] = AlinkConstants.KEY_DN;
                strArr5[1] = ((DeviceInfo) obj).deviceName;
                DCUserTrack.addTrackData(strArr5);
                DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_RESULT, "1");
                DCUserTrack.sendEvent();
                return;
            }
            if (obj instanceof DCErrorCode) {
                DCErrorCode dCErrorCode = (DCErrorCode) obj;
                String[] strArr6 = new String[2];
                strArr6[0] = "errorCode";
                strArr6[1] = dCErrorCode.code;
                DCUserTrack.addTrackData(strArr6);
                String[] strArr7 = new String[2];
                strArr7[0] = "subErrorCode";
                strArr7[1] = dCErrorCode.subcode;
                DCUserTrack.addTrackData(strArr7);
                String[] strArr8 = new String[2];
                strArr8[0] = AlinkConstants.KEY_SUB_ERROR_MSG;
                strArr8[1] = dCErrorCode.msg;
                DCUserTrack.addTrackData(strArr8);
                DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_RESULT, "0");
                String[] strArr9 = new String[2];
                strArr9[0] = "extra";
                strArr9[1] = String.valueOf(dCErrorCode.extra);
                DCUserTrack.addTrackData(strArr9);
                if (!String.valueOf(DCErrorCode.SUBCODE_PT_SAP_NO_SOFTAP).equals(dCErrorCode.code) && !String.valueOf(DCErrorCode.SUBCODE_PT_SAP_CONNECT_DEV_AP_FAILED).equals(dCErrorCode.code)) {
                    DCUserTrack.sendEvent();
                    return;
                }
                DCUserTrack.sendEvent(AlinkConstants.KEY_DC_PROVISION_DISCOVER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
