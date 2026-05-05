package com.aliyun.alink.business.devicecenter.api.add;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.BuildConfig;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConcurrentConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.track.DCUserTrackV2;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;
import com.aliyun.alink.business.devicecenter.utils.NetworkTypeUtils;
import com.aliyun.alink.business.devicecenter.utils.WifiManagerUtil;
import datasource.bean.ConfigurationData;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class ConcurrentAddDeviceBiz {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static volatile ConcurrentAddDeviceBiz f3270a;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Context f3273d;
    public IConcurrentAddDeviceStatusListener k;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IConcurrentAddDeviceListener f3271b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Map<String, List<String>> f3272c = new LinkedHashMap();
    public InnerConfigCallback e = new InnerConfigCallback();
    public List<String> f = new LinkedList();
    public List<DeviceInfo> g = new LinkedList();
    public int h = 0;
    public IConfigStrategy i = null;
    public Map<String, WeakReference<DeviceInfo>> j = new LinkedHashMap();
    public ConcurrentHashMap<String, ConfigurationData> l = new ConcurrentHashMap<>();

    /* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.api.add.ConcurrentAddDeviceBiz$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f3278a = new int[AddDeviceState.values().length];

        static {
            try {
                f3278a[AddDeviceState.AddStatePrechecking.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3278a[AddDeviceState.AddStateProvisionPreparing.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3278a[AddDeviceState.AddStateProvisioning.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3278a[AddDeviceState.AddStateProvisionOver.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private class InnerConfigCallback implements IConfigCallback {
        public InnerConfigCallback() {
        }

        @Override // com.aliyun.alink.business.devicecenter.config.IDCFailCallback
        public void onFailure(DCErrorCode dCErrorCode) {
            ALog.e("ConcurrentAddDeviceBiz", "onFailure provision fail Callback, " + dCErrorCode);
            ConcurrentAddDeviceBiz.this.a(AddDeviceState.AddStateProvisionOver, -1, false, null, dCErrorCode);
        }

        @Override // com.aliyun.alink.business.devicecenter.config.IConfigCallback
        public void onStatus(final ProvisionStatus provisionStatus) {
            ALog.i("ConcurrentAddDeviceBiz", "onStatus status=" + provisionStatus + ",addDeviceListener=" + ConcurrentAddDeviceBiz.this.f3271b);
            if (provisionStatus != ProvisionStatus.PROVISION_START_IN_CONCURRENT_MODE) {
                final DeviceInfo deviceInfo = null;
                DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.api.add.ConcurrentAddDeviceBiz.InnerConfigCallback.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (ConcurrentAddDeviceBiz.this.f3271b != null) {
                            ConcurrentAddDeviceBiz.this.f3271b.onProvisionStatus(deviceInfo, provisionStatus);
                        }
                    }
                });
                return;
            }
            PerformanceLog.trace("ConcurrentAddDeviceBiz", "startProvision");
            Object extraParams = provisionStatus.getExtraParams(AlinkConstants.KEY_CACHE_START_PROVISION_DEVICE_INFO);
            if (extraParams instanceof DeviceInfo) {
                DeviceInfo deviceInfo2 = (DeviceInfo) extraParams;
                ConcurrentAddDeviceBiz.this.a(AddDeviceState.AddStatePrechecking, -1, true, deviceInfo2, null);
                ConcurrentAddDeviceBiz.this.a(AddDeviceState.AddStateProvisioning, -1, true, deviceInfo2, null);
                String meshDeviceUniqueIDByDeviceInfo = DeviceInfoUtils.getMeshDeviceUniqueIDByDeviceInfo(deviceInfo2);
                ConcurrentAddDeviceBiz.this.a(false, meshDeviceUniqueIDByDeviceInfo, AlinkConstants.KEY_START_TIME_PROVISION, String.valueOf(System.currentTimeMillis()));
                ConcurrentAddDeviceBiz concurrentAddDeviceBiz = ConcurrentAddDeviceBiz.this;
                concurrentAddDeviceBiz.a(false, meshDeviceUniqueIDByDeviceInfo, AlinkConstants.KEY_WIFI_TYPE, concurrentAddDeviceBiz.a(concurrentAddDeviceBiz.f3273d));
                ConcurrentAddDeviceBiz concurrentAddDeviceBiz2 = ConcurrentAddDeviceBiz.this;
                concurrentAddDeviceBiz2.a(false, meshDeviceUniqueIDByDeviceInfo, AlinkConstants.KEY_HAS_SIM, String.valueOf(NetworkTypeUtils.hasSimCard(concurrentAddDeviceBiz2.f3273d)));
                ConcurrentAddDeviceBiz.this.a(false, meshDeviceUniqueIDByDeviceInfo, "sdkVersion", BuildConfig.SDK_VERSION);
                ConcurrentAddDeviceBiz.this.a(false, meshDeviceUniqueIDByDeviceInfo, AlinkConstants.KEY_LINKTYPE, deviceInfo2.linkType);
            }
        }

        @Override // com.aliyun.alink.business.devicecenter.config.IConfigCallback
        public void onSuccess(DeviceInfo deviceInfo) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("success,info=");
                sb.append(deviceInfo);
                ALog.i("ConcurrentAddDeviceBiz", sb.toString());
                if (deviceInfo == null) {
                    return;
                }
                ConcurrentAddDeviceBiz.this.a(AddDeviceState.AddStateProvisionOver, -1, true, deviceInfo, null);
            } catch (Exception e) {
                ALog.w("ConcurrentAddDeviceBiz", "onSuccess recv Callback，but parse error,e = " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public static ConcurrentAddDeviceBiz getInstance() {
        if (f3270a == null) {
            synchronized (ConcurrentAddDeviceBiz.class) {
                if (f3270a == null) {
                    f3270a = new ConcurrentAddDeviceBiz();
                }
            }
        }
        return f3270a;
    }

    public ConcurrentHashMap<String, ConfigurationData> getConfigurationInfoCache() {
        return this.l;
    }

    public ConfigurationData getConfigurationInfoCacheValue(String str) {
        if (!TextUtils.isEmpty(str)) {
            return this.l.get(str);
        }
        ALog.w("ConcurrentAddDeviceBiz", "getConfigurationInfoCache key  is null");
        return null;
    }

    public void setConfigurationInfoCache(String str, ConfigurationData configurationData) {
        if (TextUtils.isEmpty(str) || configurationData == null) {
            ALog.w("ConcurrentAddDeviceBiz", "setConfigurationInfoCache key or configurationData is null");
        } else {
            this.l.put(str, configurationData);
        }
    }

    public void stopConfig() {
        this.g.clear();
        this.f.clear();
        this.l.clear();
        this.h = 0;
        IConfigStrategy iConfigStrategy = this.i;
        if (iConfigStrategy != null) {
            iConfigStrategy.stopConfig();
            this.i = null;
        }
    }

    public void a(IConcurrentAddDeviceStatusListener iConcurrentAddDeviceStatusListener) {
        this.k = iConcurrentAddDeviceStatusListener;
    }

    public void a(Context context, List<DeviceInfo> list, IConcurrentAddDeviceListener iConcurrentAddDeviceListener) {
        ALog.i("ConcurrentAddDeviceBiz", "startConcurrentAddDevice() call.");
        if (context != null) {
            this.f3273d = context;
            DeviceCenterBiz.getInstance().setAppContext(context);
            this.f3271b = iConcurrentAddDeviceListener;
            if (list != null && list.size() != 0) {
                this.g.addAll(list);
                DCAlibabaConcurrentConfigParams dCAlibabaConcurrentConfigParams = null;
                for (DeviceInfo deviceInfo : list) {
                    this.j.put(DeviceInfoUtils.getMeshDeviceUniqueIDByDeviceInfo(deviceInfo), new WeakReference<>(deviceInfo));
                    this.f.remove(DeviceInfoUtils.getMeshDeviceUniqueIDByDeviceInfo(deviceInfo));
                    if (LinkType.ALI_APP_MESH.getName().equalsIgnoreCase(deviceInfo.linkType)) {
                        if (dCAlibabaConcurrentConfigParams == null) {
                            dCAlibabaConcurrentConfigParams = new DCAlibabaConcurrentConfigParams();
                            dCAlibabaConcurrentConfigParams.linkType = LinkType.ALI_BATCH_APP_MESH;
                            dCAlibabaConcurrentConfigParams.configParamsList = new LinkedList();
                        }
                        if (!TextUtils.isEmpty(deviceInfo.familyId)) {
                            dCAlibabaConcurrentConfigParams.familyId = deviceInfo.familyId;
                        }
                        dCAlibabaConcurrentConfigParams.configParamsList.add(deviceInfo.getDCConfigParams());
                    }
                    LinkType.ALI_GATEWAY_MESH.getName().equalsIgnoreCase(deviceInfo.linkType);
                }
                if (dCAlibabaConcurrentConfigParams != null) {
                    if (this.i == null) {
                        this.i = DeviceCenterBiz.getInstance().getConfigStrategy(LinkType.ALI_BATCH_APP_MESH);
                    }
                    try {
                        this.i.startConfig(this.e, dCAlibabaConcurrentConfigParams);
                        return;
                    } catch (Exception e) {
                        ALog.e("ConcurrentAddDeviceBiz", e.toString());
                        for (DeviceInfo deviceInfo2 : list) {
                            a(AddDeviceState.AddStateProvisionOver, -1, false, null, new DCErrorCode("SDKError", DCErrorCode.PF_SDK_ERROR).setSubcode(DCErrorCode.SUBCODE_SKE_START_CONFIG_EXCEPTION).setMsg("startConfig" + e).setExtra(deviceInfo2));
                        }
                        return;
                    }
                }
                ALog.e("ConcurrentAddDeviceBiz", "Could not find provision params for concurrent-provision");
                return;
            }
            ALog.e("ConcurrentAddDeviceBiz", "startAddDevice, params error");
            a(AddDeviceState.AddStateProvisionOver, -1, false, null, new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_PE_PRODUCTKEY_EMPTY).setMsg("pkError"));
            return;
        }
        ALog.e("ConcurrentAddDeviceBiz", "startConcurrentAddDevice context=null.");
        throw new RuntimeException("startAddDeviceParamContextNull");
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0035  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void a(final com.aliyun.alink.business.devicecenter.api.add.AddDeviceState r9, final int r10, final boolean r11, com.aliyun.alink.business.devicecenter.api.add.DeviceInfo r12, final com.aliyun.alink.business.devicecenter.base.DCErrorCode r13) {
        /*
            r8 = this;
            if (r12 != 0) goto L35
            java.lang.Object r0 = r13.extra
            boolean r1 = r0 instanceof com.aliyun.alink.business.devicecenter.api.add.DeviceInfo
            if (r1 == 0) goto Ld
            r12 = r0
            com.aliyun.alink.business.devicecenter.api.add.DeviceInfo r12 = (com.aliyun.alink.business.devicecenter.api.add.DeviceInfo) r12
            r3 = r12
            goto L36
        Ld:
            boolean r1 = r0 instanceof java.util.Map
            if (r1 == 0) goto L35
            java.util.Map r0 = (java.util.Map) r0
            java.lang.String r1 = "mesh_device_unique_id"
            boolean r1 = r0.containsKey(r1)
            if (r1 == 0) goto L35
            java.lang.String r1 = "mesh_device_unique_id"
            java.lang.Object r0 = r0.get(r1)
            java.lang.String r0 = (java.lang.String) r0
            java.util.Map<java.lang.String, java.lang.ref.WeakReference<com.aliyun.alink.business.devicecenter.api.add.DeviceInfo>> r1 = r8.j
            java.lang.Object r0 = r1.get(r0)
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0
            if (r0 == 0) goto L35
            java.lang.Object r12 = r0.get()
            com.aliyun.alink.business.devicecenter.api.add.DeviceInfo r12 = (com.aliyun.alink.business.devicecenter.api.add.DeviceInfo) r12
            r3 = r12
            goto L36
        L35:
            r3 = r12
        L36:
            com.aliyun.alink.business.devicecenter.api.add.AddDeviceState r12 = com.aliyun.alink.business.devicecenter.api.add.AddDeviceState.AddStateProvisionOver
            if (r9 != r12) goto L6d
            if (r11 != 0) goto L6d
            if (r13 == 0) goto L6d
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r0 = "state="
            r12.append(r0)
            r12.append(r9)
            java.lang.String r0 = ",isSuccess="
            r12.append(r0)
            r12.append(r11)
            java.lang.String r0 = ",info="
            r12.append(r0)
            r12.append(r3)
            java.lang.String r0 = ",error="
            r12.append(r0)
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            java.lang.String r0 = "ConcurrentAddDeviceBiz"
            com.aliyun.alink.business.devicecenter.log.ALog.e(r0, r12)
            goto L9b
        L6d:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r0 = "state="
            r12.append(r0)
            r12.append(r9)
            java.lang.String r0 = ",isSuccess="
            r12.append(r0)
            r12.append(r11)
            java.lang.String r0 = ",info="
            r12.append(r0)
            r12.append(r3)
            java.lang.String r0 = ",error="
            r12.append(r0)
            r12.append(r13)
            java.lang.String r12 = r12.toString()
            java.lang.String r0 = "ConcurrentAddDeviceBiz"
            com.aliyun.alink.business.devicecenter.log.ALog.i(r0, r12)
        L9b:
            com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz r12 = com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz.getInstance()
            com.aliyun.alink.business.devicecenter.api.add.ConcurrentAddDeviceBiz$1 r7 = new com.aliyun.alink.business.devicecenter.api.add.ConcurrentAddDeviceBiz$1
            r0 = r7
            r1 = r8
            r2 = r9
            r4 = r11
            r5 = r13
            r6 = r10
            r0.<init>()
            r12.runOnUIThread(r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.business.devicecenter.api.add.ConcurrentAddDeviceBiz.a(com.aliyun.alink.business.devicecenter.api.add.AddDeviceState, int, boolean, com.aliyun.alink.business.devicecenter.api.add.DeviceInfo, com.aliyun.alink.business.devicecenter.base.DCErrorCode):void");
    }

    public final void a(boolean z, String str, String... strArr) {
        if (TextUtils.isDigitsOnly(str)) {
            return;
        }
        List<String> linkedList = this.f3272c.get(str);
        if (linkedList == null) {
            linkedList = new LinkedList<>();
        }
        this.f3272c.put(str, linkedList);
        linkedList.addAll(Arrays.asList(strArr));
        if (z) {
            DCUserTrackV2 dCUserTrackV2 = new DCUserTrackV2();
            dCUserTrackV2.resetTrackData();
            dCUserTrackV2.addTrackData((String[]) linkedList.toArray(new String[0]));
            dCUserTrackV2.sendEvent();
        }
    }

    public final void a(Object obj) {
        ALog.d("ConcurrentAddDeviceBiz", "provisionTrack obj=" + obj);
        try {
            if (obj instanceof DeviceInfo) {
                String meshDeviceUniqueIDByDeviceInfo = DeviceInfoUtils.getMeshDeviceUniqueIDByDeviceInfo((DeviceInfo) obj);
                String[] strArr = new String[2];
                strArr[0] = AlinkConstants.KEY_END_TIME_PROVISION;
                strArr[1] = String.valueOf(System.currentTimeMillis());
                a(false, meshDeviceUniqueIDByDeviceInfo, strArr);
                String[] strArr2 = new String[2];
                strArr2[0] = AlinkConstants.KEY_PK;
                strArr2[1] = ((DeviceInfo) obj).productKey;
                a(false, meshDeviceUniqueIDByDeviceInfo, strArr2);
                String[] strArr3 = new String[2];
                strArr3[0] = AlinkConstants.KEY_DN;
                strArr3[1] = ((DeviceInfo) obj).deviceName;
                a(false, meshDeviceUniqueIDByDeviceInfo, strArr3);
                a(true, meshDeviceUniqueIDByDeviceInfo, AlinkConstants.KEY_PROVISION_RESULT, "1");
                return;
            }
            if (obj instanceof DCErrorCode) {
                DCErrorCode dCErrorCode = (DCErrorCode) obj;
                if (dCErrorCode.extra instanceof DeviceInfo) {
                    DeviceInfo deviceInfo = (DeviceInfo) dCErrorCode.extra;
                    String meshDeviceUniqueIDByDeviceInfo2 = DeviceInfoUtils.getMeshDeviceUniqueIDByDeviceInfo(deviceInfo);
                    if (!TextUtils.isEmpty(deviceInfo.productKey)) {
                        String[] strArr4 = new String[2];
                        strArr4[0] = AlinkConstants.KEY_PK;
                        strArr4[1] = deviceInfo.productKey;
                        a(true, meshDeviceUniqueIDByDeviceInfo2, strArr4);
                    }
                    if (!TextUtils.isEmpty(deviceInfo.deviceName)) {
                        String[] strArr5 = new String[2];
                        strArr5[0] = AlinkConstants.KEY_DN;
                        strArr5[1] = deviceInfo.deviceName;
                        a(true, meshDeviceUniqueIDByDeviceInfo2, strArr5);
                    }
                    String[] strArr6 = new String[2];
                    strArr6[0] = AlinkConstants.KEY_END_TIME_PROVISION;
                    strArr6[1] = String.valueOf(System.currentTimeMillis());
                    a(false, meshDeviceUniqueIDByDeviceInfo2, strArr6);
                    String[] strArr7 = new String[2];
                    strArr7[0] = "errorCode";
                    strArr7[1] = dCErrorCode.code;
                    a(false, meshDeviceUniqueIDByDeviceInfo2, strArr7);
                    String[] strArr8 = new String[2];
                    strArr8[0] = "subErrorCode";
                    strArr8[1] = dCErrorCode.subcode;
                    a(false, meshDeviceUniqueIDByDeviceInfo2, strArr8);
                    String[] strArr9 = new String[2];
                    strArr9[0] = AlinkConstants.KEY_SUB_ERROR_MSG;
                    strArr9[1] = dCErrorCode.msg;
                    a(false, meshDeviceUniqueIDByDeviceInfo2, strArr9);
                    String[] strArr10 = new String[2];
                    strArr10[0] = "extra";
                    strArr10[1] = String.valueOf(dCErrorCode.extra);
                    a(true, meshDeviceUniqueIDByDeviceInfo2, strArr10);
                    return;
                }
                DCUserTrackV2 dCUserTrackV2 = new DCUserTrackV2();
                dCUserTrackV2.resetTrackData();
                String[] strArr11 = new String[2];
                strArr11[0] = AlinkConstants.KEY_END_TIME_PROVISION;
                strArr11[1] = String.valueOf(System.currentTimeMillis());
                dCUserTrackV2.addTrackData(strArr11);
                String[] strArr12 = new String[2];
                strArr12[0] = "errorCode";
                strArr12[1] = dCErrorCode.code;
                dCUserTrackV2.addTrackData(strArr12);
                String[] strArr13 = new String[2];
                strArr13[0] = "subErrorCode";
                strArr13[1] = dCErrorCode.subcode;
                dCUserTrackV2.addTrackData(strArr13);
                String[] strArr14 = new String[2];
                strArr14[0] = AlinkConstants.KEY_SUB_ERROR_MSG;
                strArr14[1] = dCErrorCode.msg;
                dCUserTrackV2.addTrackData(strArr14);
                dCUserTrackV2.addTrackData(AlinkConstants.KEY_PROVISION_RESULT, "0");
                String[] strArr15 = new String[2];
                strArr15[0] = "extra";
                strArr15[1] = String.valueOf(dCErrorCode.extra);
                dCUserTrackV2.addTrackData(strArr15);
                if (!String.valueOf(DCErrorCode.SUBCODE_PT_SAP_NO_SOFTAP).equals(dCErrorCode.code) && !String.valueOf(DCErrorCode.SUBCODE_PT_SAP_CONNECT_DEV_AP_FAILED).equals(dCErrorCode.code)) {
                    dCUserTrackV2.sendEvent();
                    return;
                }
                dCUserTrackV2.sendEvent(AlinkConstants.KEY_DC_PROVISION_DISCOVER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final String a(Context context) {
        if (context != null) {
            return new WifiManagerUtil(context).getWifiType();
        }
        throw new IllegalArgumentException("context=null");
    }
}
