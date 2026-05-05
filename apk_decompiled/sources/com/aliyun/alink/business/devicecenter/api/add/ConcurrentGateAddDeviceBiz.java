package com.aliyun.alink.business.devicecenter.api.add;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.BuildConfig;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.IExtraConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConcurrentGateWayConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.track.DCUserTrackV2;
import com.aliyun.alink.business.devicecenter.utils.DeviceInfoUtils;
import com.aliyun.alink.business.devicecenter.utils.NetworkTypeUtils;
import com.aliyun.alink.business.devicecenter.utils.WifiManagerUtil;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ConcurrentGateAddDeviceBiz {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static volatile ConcurrentGateAddDeviceBiz f3283a;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Context f3286d;
    public IConcurrentAddDeviceStatusListener k;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IConcurrentAddDeviceListener f3284b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Map<String, List<String>> f3285c = new LinkedHashMap();
    public InnerConfigCallback e = new InnerConfigCallback();
    public List<String> f = new LinkedList();
    public List<DeviceInfo> g = new LinkedList();
    public int h = 0;
    public final Map<String, IConfigStrategy> i = new HashMap();
    public Map<String, WeakReference<DeviceInfo>> j = new LinkedHashMap();

    /* JADX INFO: renamed from: com.aliyun.alink.business.devicecenter.api.add.ConcurrentGateAddDeviceBiz$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final /* synthetic */ int[] f3291a = new int[AddDeviceState.values().length];

        static {
            try {
                f3291a[AddDeviceState.AddStatePrechecking.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f3291a[AddDeviceState.AddStateProvisionPreparing.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f3291a[AddDeviceState.AddStateProvisioning.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f3291a[AddDeviceState.AddStateProvisionOver.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    private class InnerConfigCallback implements IConfigCallback {
        public InnerConfigCallback() {
        }

        @Override // com.aliyun.alink.business.devicecenter.config.IDCFailCallback
        public void onFailure(DCErrorCode dCErrorCode) {
            ALog.e("ConcurrentGateAddDeviceBiz", "onFailure provision fail Callback, " + dCErrorCode);
            ConcurrentGateAddDeviceBiz.this.a(AddDeviceState.AddStateProvisionOver, -1, false, null, dCErrorCode);
        }

        @Override // com.aliyun.alink.business.devicecenter.config.IConfigCallback
        public void onStatus(final ProvisionStatus provisionStatus) {
            ALog.i("ConcurrentGateAddDeviceBiz", "onStatus status=" + provisionStatus + ",addDeviceListener=" + ConcurrentGateAddDeviceBiz.this.f3284b);
            if (provisionStatus != ProvisionStatus.PROVISION_START_IN_CONCURRENT_MODE) {
                final DeviceInfo deviceInfo = null;
                DeviceCenterBiz.getInstance().runOnUIThread(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.api.add.ConcurrentGateAddDeviceBiz.InnerConfigCallback.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (ConcurrentGateAddDeviceBiz.this.f3284b != null) {
                            ConcurrentGateAddDeviceBiz.this.f3284b.onProvisionStatus(deviceInfo, provisionStatus);
                        }
                    }
                });
                return;
            }
            PerformanceLog.trace("ConcurrentGateAddDeviceBiz", "startProvision");
            Object extraParams = provisionStatus.getExtraParams(AlinkConstants.KEY_CACHE_START_PROVISION_DEVICE_INFO);
            if (extraParams instanceof DeviceInfo) {
                DeviceInfo deviceInfo2 = (DeviceInfo) extraParams;
                ConcurrentGateAddDeviceBiz.this.a(AddDeviceState.AddStateProvisioning, -1, true, deviceInfo2, null);
                String meshDeviceUniqueIDByDeviceInfo = DeviceInfoUtils.getMeshDeviceUniqueIDByDeviceInfo(deviceInfo2);
                ConcurrentGateAddDeviceBiz.this.a(false, meshDeviceUniqueIDByDeviceInfo, AlinkConstants.KEY_START_TIME_PROVISION, String.valueOf(System.currentTimeMillis()));
                ConcurrentGateAddDeviceBiz concurrentGateAddDeviceBiz = ConcurrentGateAddDeviceBiz.this;
                concurrentGateAddDeviceBiz.a(false, meshDeviceUniqueIDByDeviceInfo, AlinkConstants.KEY_WIFI_TYPE, concurrentGateAddDeviceBiz.a(concurrentGateAddDeviceBiz.f3286d));
                ConcurrentGateAddDeviceBiz concurrentGateAddDeviceBiz2 = ConcurrentGateAddDeviceBiz.this;
                concurrentGateAddDeviceBiz2.a(false, meshDeviceUniqueIDByDeviceInfo, AlinkConstants.KEY_HAS_SIM, String.valueOf(NetworkTypeUtils.hasSimCard(concurrentGateAddDeviceBiz2.f3286d)));
                ConcurrentGateAddDeviceBiz.this.a(false, meshDeviceUniqueIDByDeviceInfo, "sdkVersion", BuildConfig.SDK_VERSION);
                ConcurrentGateAddDeviceBiz.this.a(false, meshDeviceUniqueIDByDeviceInfo, AlinkConstants.KEY_LINKTYPE, deviceInfo2.linkType);
            }
        }

        @Override // com.aliyun.alink.business.devicecenter.config.IConfigCallback
        public void onSuccess(DeviceInfo deviceInfo) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("success,info=");
                sb.append(deviceInfo);
                ALog.i("ConcurrentGateAddDeviceBiz", sb.toString());
                if (deviceInfo == null) {
                    return;
                }
                ConcurrentGateAddDeviceBiz.this.a(AddDeviceState.AddStateProvisionOver, -1, true, deviceInfo, null);
            } catch (Exception e) {
                ALog.w("ConcurrentGateAddDeviceBiz", "onSuccess recv Callback，but parse error,e = " + e.toString());
                e.printStackTrace();
            }
        }
    }

    public static ConcurrentGateAddDeviceBiz getInstance() {
        if (f3283a == null) {
            synchronized (ConcurrentGateAddDeviceBiz.class) {
                if (f3283a == null) {
                    f3283a = new ConcurrentGateAddDeviceBiz();
                }
            }
        }
        return f3283a;
    }

    public List<DeviceInfo> onConcurrentProvisionerIdle() {
        if (this.i.size() <= 0) {
            return null;
        }
        IConfigStrategy configStrategy = DeviceCenterBiz.getInstance().getConfigStrategy(LinkType.ALI_BATCH_GATEWAY_MESH);
        Iterator<String> it = this.i.keySet().iterator();
        while (it.hasNext()) {
            IConfigStrategy iConfigStrategy = this.i.get(it.next());
            if ((iConfigStrategy instanceof IExtraConfigStrategy) && (configStrategy instanceof IExtraConfigStrategy)) {
                IExtraConfigStrategy iExtraConfigStrategy = (IExtraConfigStrategy) iConfigStrategy;
                if (iExtraConfigStrategy.getResetCount() > ((IExtraConfigStrategy) configStrategy).getResetCount()) {
                    configStrategy = iExtraConfigStrategy;
                }
            }
        }
        if (!(configStrategy instanceof IExtraConfigStrategy)) {
            return null;
        }
        IExtraConfigStrategy iExtraConfigStrategy2 = (IExtraConfigStrategy) configStrategy;
        if (iExtraConfigStrategy2.getResetCount() > 0) {
            return iExtraConfigStrategy2.getPrepareProvisionDevices();
        }
        return null;
    }

    public void stopConfig() {
        this.g.clear();
        this.f.clear();
        this.h = 0;
        Map<String, IConfigStrategy> map = this.i;
        if (map == null || map.size() <= 0) {
            return;
        }
        for (String str : this.i.keySet()) {
            if (this.i.get(str) != null) {
                this.i.get(str).stopConfig();
            }
        }
    }

    public void a(Context context, List<DeviceInfo> list, IConcurrentAddDeviceListener iConcurrentAddDeviceListener) {
        DCAlibabaConcurrentGateWayConfigParams dCAlibabaConcurrentGateWayConfigParams;
        ALog.i("ConcurrentGateAddDeviceBiz", "startConcurrentAddDevice() call.");
        if (context != null) {
            this.f3286d = context;
            DeviceCenterBiz.getInstance().setAppContext(context);
            this.f3284b = iConcurrentAddDeviceListener;
            if (list != null && list.size() != 0) {
                ALog.i("ConcurrentGateAddDeviceBiz", "startConcurrentAddDevice() call 配网的数量: " + list.size());
                this.g.addAll(list);
                HashMap map = new HashMap();
                for (DeviceInfo deviceInfo : list) {
                    this.j.put(DeviceInfoUtils.getMeshDeviceUniqueIDByDeviceInfo(deviceInfo), new WeakReference<>(deviceInfo));
                    this.f.remove(DeviceInfoUtils.getMeshDeviceUniqueIDByDeviceInfo(deviceInfo));
                    if (LinkType.ALI_GATEWAY_MESH.getName().equalsIgnoreCase(deviceInfo.linkType)) {
                        if (TextUtils.isEmpty(deviceInfo.regIotId)) {
                            ALog.d("ConcurrentGateAddDeviceBiz", "startAddDevices: regIotId 为空,不作处理");
                            a(AddDeviceState.AddStateProvisionOver, -1, false, null, new DCErrorCode("SDKError", DCErrorCode.PF_SDK_ERROR).setSubcode(DCErrorCode.SUBCODE_SKE_START_CONFIG_EXCEPTION).setMsg("regIotId is null can not gateway provision").setExtra(deviceInfo));
                        } else {
                            if (map.containsKey(deviceInfo.regIotId) && map.get(deviceInfo.regIotId) != null) {
                                dCAlibabaConcurrentGateWayConfigParams = (DCAlibabaConcurrentGateWayConfigParams) map.get(deviceInfo.regIotId);
                            } else {
                                dCAlibabaConcurrentGateWayConfigParams = new DCAlibabaConcurrentGateWayConfigParams();
                                dCAlibabaConcurrentGateWayConfigParams.setGatewayIotId(deviceInfo.regIotId);
                                map.put(deviceInfo.regIotId, dCAlibabaConcurrentGateWayConfigParams);
                            }
                            dCAlibabaConcurrentGateWayConfigParams.linkType = LinkType.ALI_GATEWAY_MESH;
                            dCAlibabaConcurrentGateWayConfigParams.addDevice(deviceInfo);
                            map.put(deviceInfo.regIotId, dCAlibabaConcurrentGateWayConfigParams);
                            if (!this.i.containsKey(deviceInfo.regIotId)) {
                                this.i.put(deviceInfo.regIotId, DeviceCenterBiz.getInstance().getConfigStrategy(LinkType.ALI_BATCH_GATEWAY_MESH));
                            }
                        }
                    }
                }
                ALog.d("ConcurrentGateAddDeviceBiz", "startAddDevices() called with: context =, deviceInfos = [" + list.size() + "], listener = [" + iConcurrentAddDeviceListener + "]");
                StringBuilder sb = new StringBuilder();
                sb.append("startAddDevices: ateWayConfigParamsList.size()=");
                sb.append(map.size());
                ALog.d("ConcurrentGateAddDeviceBiz", sb.toString());
                if (map.size() > 0) {
                    try {
                        for (String str : this.i.keySet()) {
                            if (this.i.get(str) != null) {
                                this.i.get(str).startConfig(this.e, (DCConfigParams) map.get(str));
                            }
                        }
                        return;
                    } catch (Exception e) {
                        ALog.e("ConcurrentGateAddDeviceBiz", e.toString());
                        for (DeviceInfo deviceInfo2 : list) {
                            a(AddDeviceState.AddStateProvisionOver, -1, false, null, new DCErrorCode("SDKError", DCErrorCode.PF_SDK_ERROR).setSubcode(DCErrorCode.SUBCODE_SKE_START_CONFIG_EXCEPTION).setMsg("startConfig" + e).setExtra(deviceInfo2));
                        }
                        return;
                    }
                }
                ALog.e("ConcurrentGateAddDeviceBiz", "gateWayConfigParamsList is 0, for concurrent-provision");
                return;
            }
            ALog.e("ConcurrentGateAddDeviceBiz", "startAddDevice, params error");
            a(AddDeviceState.AddStateProvisionOver, -1, false, null, new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_PE_PRODUCTKEY_EMPTY).setMsg("pkError"));
            return;
        }
        ALog.e("ConcurrentGateAddDeviceBiz", "startConcurrentAddDevice context=null.");
        throw new RuntimeException("startAddDeviceParamContextNull");
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0036  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final synchronized void a(final com.aliyun.alink.business.devicecenter.api.add.AddDeviceState r9, final int r10, final boolean r11, com.aliyun.alink.business.devicecenter.api.add.DeviceInfo r12, final com.aliyun.alink.business.devicecenter.base.DCErrorCode r13) {
        /*
            r8 = this;
            monitor-enter(r8)
            if (r12 != 0) goto L36
            java.lang.Object r0 = r13.extra     // Catch: java.lang.Throwable -> Lb0
            boolean r1 = r0 instanceof com.aliyun.alink.business.devicecenter.api.add.DeviceInfo     // Catch: java.lang.Throwable -> Lb0
            if (r1 == 0) goto Le
            r12 = r0
            com.aliyun.alink.business.devicecenter.api.add.DeviceInfo r12 = (com.aliyun.alink.business.devicecenter.api.add.DeviceInfo) r12     // Catch: java.lang.Throwable -> Lb0
            r3 = r12
            goto L37
        Le:
            boolean r1 = r0 instanceof java.util.Map     // Catch: java.lang.Throwable -> Lb0
            if (r1 == 0) goto L36
            java.util.Map r0 = (java.util.Map) r0     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r1 = "mesh_device_unique_id"
            boolean r1 = r0.containsKey(r1)     // Catch: java.lang.Throwable -> Lb0
            if (r1 == 0) goto L36
            java.lang.String r1 = "mesh_device_unique_id"
            java.lang.Object r0 = r0.get(r1)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.Throwable -> Lb0
            java.util.Map<java.lang.String, java.lang.ref.WeakReference<com.aliyun.alink.business.devicecenter.api.add.DeviceInfo>> r1 = r8.j     // Catch: java.lang.Throwable -> Lb0
            java.lang.Object r0 = r1.get(r0)     // Catch: java.lang.Throwable -> Lb0
            java.lang.ref.WeakReference r0 = (java.lang.ref.WeakReference) r0     // Catch: java.lang.Throwable -> Lb0
            if (r0 == 0) goto L36
            java.lang.Object r12 = r0.get()     // Catch: java.lang.Throwable -> Lb0
            com.aliyun.alink.business.devicecenter.api.add.DeviceInfo r12 = (com.aliyun.alink.business.devicecenter.api.add.DeviceInfo) r12     // Catch: java.lang.Throwable -> Lb0
            r3 = r12
            goto L37
        L36:
            r3 = r12
        L37:
            com.aliyun.alink.business.devicecenter.api.add.AddDeviceState r12 = com.aliyun.alink.business.devicecenter.api.add.AddDeviceState.AddStateProvisionOver     // Catch: java.lang.Throwable -> Lb0
            if (r9 != r12) goto L6e
            if (r11 != 0) goto L6e
            if (r13 == 0) goto L6e
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lb0
            r12.<init>()     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = "state="
            r12.append(r0)     // Catch: java.lang.Throwable -> Lb0
            r12.append(r9)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = ",isSuccess="
            r12.append(r0)     // Catch: java.lang.Throwable -> Lb0
            r12.append(r11)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = ",info="
            r12.append(r0)     // Catch: java.lang.Throwable -> Lb0
            r12.append(r3)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = ",error="
            r12.append(r0)     // Catch: java.lang.Throwable -> Lb0
            r12.append(r13)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r12 = r12.toString()     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = "ConcurrentGateAddDeviceBiz"
            com.aliyun.alink.business.devicecenter.log.ALog.e(r0, r12)     // Catch: java.lang.Throwable -> Lb0
            goto L9c
        L6e:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> Lb0
            r12.<init>()     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = "state="
            r12.append(r0)     // Catch: java.lang.Throwable -> Lb0
            r12.append(r9)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = ",isSuccess="
            r12.append(r0)     // Catch: java.lang.Throwable -> Lb0
            r12.append(r11)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = ",info="
            r12.append(r0)     // Catch: java.lang.Throwable -> Lb0
            r12.append(r3)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = ",error="
            r12.append(r0)     // Catch: java.lang.Throwable -> Lb0
            r12.append(r13)     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r12 = r12.toString()     // Catch: java.lang.Throwable -> Lb0
            java.lang.String r0 = "ConcurrentGateAddDeviceBiz"
            com.aliyun.alink.business.devicecenter.log.ALog.i(r0, r12)     // Catch: java.lang.Throwable -> Lb0
        L9c:
            com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz r12 = com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz.getInstance()     // Catch: java.lang.Throwable -> Lb0
            com.aliyun.alink.business.devicecenter.api.add.ConcurrentGateAddDeviceBiz$1 r7 = new com.aliyun.alink.business.devicecenter.api.add.ConcurrentGateAddDeviceBiz$1     // Catch: java.lang.Throwable -> Lb0
            r0 = r7
            r1 = r8
            r2 = r9
            r4 = r11
            r5 = r13
            r6 = r10
            r0.<init>()     // Catch: java.lang.Throwable -> Lb0
            r12.runOnUIThread(r7)     // Catch: java.lang.Throwable -> Lb0
            monitor-exit(r8)
            return
        Lb0:
            r9 = move-exception
            monitor-exit(r8)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.business.devicecenter.api.add.ConcurrentGateAddDeviceBiz.a(com.aliyun.alink.business.devicecenter.api.add.AddDeviceState, int, boolean, com.aliyun.alink.business.devicecenter.api.add.DeviceInfo, com.aliyun.alink.business.devicecenter.base.DCErrorCode):void");
    }

    public final void a(boolean z, String str, String... strArr) {
        if (TextUtils.isDigitsOnly(str)) {
            return;
        }
        List<String> linkedList = this.f3285c.get(str);
        if (linkedList == null) {
            linkedList = new LinkedList<>();
        }
        this.f3285c.put(str, linkedList);
        linkedList.addAll(Arrays.asList(strArr));
        if (z) {
            DCUserTrackV2 dCUserTrackV2 = new DCUserTrackV2();
            dCUserTrackV2.resetTrackData();
            dCUserTrackV2.addTrackData((String[]) linkedList.toArray(new String[0]));
            dCUserTrackV2.sendEvent();
        }
    }

    public final void a(Object obj) {
        ALog.d("ConcurrentGateAddDeviceBiz", "provisionTrack obj=" + obj);
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
