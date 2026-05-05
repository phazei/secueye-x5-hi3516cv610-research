package com.aliyun.alink.linksdk.tmp.device.a.a;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import anetwork.channel.util.RequestConstant;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.manager.discovery.DiscoveryMessage;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.DevFoundOutputParams;
import com.aliyun.alink.linksdk.tmp.api.DeviceBasicData;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.api.IDiscoveryFilter;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.connect.entity.cmp.i;
import com.aliyun.alink.linksdk.tmp.data.cloud.CloudLcaRequestParams;
import com.aliyun.alink.linksdk.tmp.data.cloud.EdgeGatewaysResponsePayload;
import com.aliyun.alink.linksdk.tmp.data.discovery.DiscoveryConfig;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr;
import com.aliyun.alink.linksdk.tmp.device.payload.discovery.DiscoveryRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.request.other.GetDeviceNetTypesSupportedRequest;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.listener.IProcessListener;
import com.aliyun.alink.linksdk.tmp.service.DevService;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;
import com.aliyun.alink.linksdk.tmp.utils.WifiManagerUtil;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.breeze.mix.ConnectionCallback;
import com.aliyun.iot.breeze.mix.MixBleDelegate;
import com.aliyun.linksdk.alcs.AlcsCmpSDK;
import com.google.gson.reflect.TypeToken;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: compiled from: DiscoveryTask.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends com.aliyun.alink.linksdk.tmp.device.a.d<a> implements INotifyHandler {
    private static final String y = "[Tmp]DiscoveryTask";
    protected b n;
    protected RunnableC0223a o;
    protected Handler p;
    protected long q;
    protected com.aliyun.alink.linksdk.tmp.connect.d r;
    protected IDiscoveryFilter s;
    protected Object t;
    protected Map<String, DeviceBasicData> u;
    protected List<c> v;
    protected DiscoveryConfig w;
    protected volatile boolean x;

    public a(com.aliyun.alink.linksdk.tmp.connect.b bVar, IDevListener iDevListener) {
        super(null, iDevListener);
        this.i = bVar;
        this.p = new Handler(Looper.getMainLooper());
        this.n = new b(this);
        this.o = new RunnableC0223a(this);
        this.u = new ConcurrentHashMap();
        this.v = new CopyOnWriteArrayList();
        this.x = true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public a a(IDiscoveryFilter iDiscoveryFilter) {
        this.s = iDiscoveryFilter;
        return (a) this.f4346d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public a b(Object obj) {
        this.t = obj;
        return (a) this.f4346d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public a a(DiscoveryConfig discoveryConfig) {
        this.w = discoveryConfig;
        DiscoveryConfig discoveryConfig2 = this.w;
        if (discoveryConfig2 != null) {
            this.t = discoveryConfig2.cloudLcaRequestParams;
        }
        return (a) this.f4346d;
    }

    /* JADX INFO: renamed from: a, reason: avoid collision after fix types in other method */
    public void a2(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        if (eVar == null || eVar.a() == null) {
            LogCat.e(y, "addDevice error response null or unsuccess");
            return;
        }
        DiscoveryMessage discoveryMessage = (DiscoveryMessage) ((i) eVar).a().data;
        if (discoveryMessage == null) {
            ALog.e(y, "onDeviceFound discoveryMessage or deviceInfo null");
        } else {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(new AnonymousClass1(discoveryMessage));
        }
    }

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.device.a.a.a$1, reason: invalid class name */
    /* JADX INFO: compiled from: DiscoveryTask.java */
    class AnonymousClass1 implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ DiscoveryMessage f4311a;

        AnonymousClass1(DiscoveryMessage discoveryMessage) {
            this.f4311a = discoveryMessage;
        }

        @Override // java.lang.Runnable
        public void run() {
            String deviceSupportedNetTypesByPk = DeviceShadowMgr.getInstance().getDeviceSupportedNetTypesByPk(this.f4311a.productKey);
            if (TextUtils.isEmpty(deviceSupportedNetTypesByPk)) {
                DeviceShadowMgr.getInstance().updateDeviceNetTypesSupportedByPk(this.f4311a.productKey, true, new IProcessListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.a.a.1.1
                    @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                    public void onSuccess(Object obj) {
                        final int deviceNetType;
                        try {
                            deviceNetType = TmpEnum.DeviceNetType.formatDeviceNetType((List) ((GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse) JSON.parseObject(obj.toString(), GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse.class)).data);
                        } catch (Exception e) {
                            ALog.e(a.y, "  e:" + e.toString());
                            deviceNetType = 0;
                        }
                        final String macByDn = null;
                        if (TmpEnum.DeviceNetType.isWifiBtCombo(deviceNetType)) {
                            if (AnonymousClass1.this.f4311a.modelType.equalsIgnoreCase("2")) {
                                macByDn = TmpStorage.getInstance().getDnByMac(AnonymousClass1.this.f4311a.deviceName);
                            } else {
                                macByDn = TmpStorage.getInstance().getMacByDn(AnonymousClass1.this.f4311a.deviceName);
                            }
                        }
                        TmpSdk.mHandler.post(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.a.a.a.1.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                a.this.a(AnonymousClass1.this.f4311a, deviceNetType, macByDn);
                            }
                        });
                    }

                    @Override // com.aliyun.alink.linksdk.tmp.listener.IProcessListener
                    public void onFail(ErrorInfo errorInfo) {
                        TmpSdk.mHandler.post(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.a.a.a.1.1.2
                            @Override // java.lang.Runnable
                            public void run() {
                                a.this.a(AnonymousClass1.this.f4311a, TmpEnum.DeviceNetType.NET_UNKNOWN.getValue(), (String) null);
                            }
                        });
                    }
                });
                return;
            }
            final int deviceNetType = 0;
            try {
                deviceNetType = TmpEnum.DeviceNetType.formatDeviceNetType((List) ((GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse) JSON.parseObject(deviceSupportedNetTypesByPk, GetDeviceNetTypesSupportedRequest.GetDeviceNetTypesSupportedResponse.class)).data);
            } catch (Exception e) {
                ALog.e(a.y, "cached updateDeviceNetTypesSupported e:" + e.toString());
            }
            final String macByDn = null;
            if (TmpEnum.DeviceNetType.isWifiBtCombo(deviceNetType)) {
                if (this.f4311a.modelType.equalsIgnoreCase("2")) {
                    macByDn = TmpStorage.getInstance().getDnByMac(this.f4311a.deviceName);
                } else {
                    macByDn = TmpStorage.getInstance().getMacByDn(this.f4311a.deviceName);
                }
            }
            TmpSdk.mHandler.post(new Runnable() { // from class: com.aliyun.alink.linksdk.tmp.device.a.a.a.1.2
                @Override // java.lang.Runnable
                public void run() {
                    a.this.a(AnonymousClass1.this.f4311a, deviceNetType, macByDn);
                }
            });
        }
    }

    protected void a(DiscoveryMessage discoveryMessage, int i, String str) {
        boolean z = true;
        DeviceBasicData deviceBasicData = new DeviceBasicData(true);
        deviceBasicData.setProductKey(discoveryMessage.productKey);
        deviceBasicData.setDeviceName(discoveryMessage.deviceName);
        deviceBasicData.setModelType(discoveryMessage.modelType);
        deviceBasicData.setAddr(discoveryMessage.getIp());
        deviceBasicData.setPort(discoveryMessage.getPort());
        deviceBasicData.setSupportedNetType(i);
        deviceBasicData.mac = discoveryMessage.mac;
        deviceBasicData.isPluginFound = true;
        deviceBasicData.extraData = discoveryMessage.extraData;
        if (!"1".equalsIgnoreCase(discoveryMessage.modelType) && "2".equalsIgnoreCase(discoveryMessage.modelType)) {
            deviceBasicData.localDiscoveryType = TmpEnum.DeviceNetType.NET_BT.getValue();
        } else {
            deviceBasicData.localDiscoveryType = TmpEnum.DeviceNetType.NET_WIFI.getValue();
        }
        this.u.put(deviceBasicData.getDevId(), deviceBasicData);
        if (TmpEnum.DeviceNetType.isWifiBtCombo(i) && !TextUtils.isEmpty(str)) {
            DeviceBasicData deviceBasicData2 = DeviceManager.getInstance().getDeviceBasicData(TextHelper.combineStr(discoveryMessage.productKey, str));
            if (deviceBasicData2 != null) {
                if (deviceBasicData.localDiscoveryType == TmpEnum.DeviceNetType.NET_WIFI.getValue() && (deviceBasicData2.localDiscoveryType | TmpEnum.DeviceNetType.NET_BT.getValue()) > 0) {
                    ALog.d(y, "discovery combo wifi, try to close ble anotherComboDevData:" + deviceBasicData2 + " basicData:" + deviceBasicData);
                    MixBleDelegate.getInstance().close(deviceBasicData2.mac, (ConnectionCallback) null);
                }
                deviceBasicData2.localDiscoveryType |= deviceBasicData.localDiscoveryType;
                if (deviceBasicData2.extraData == null) {
                    deviceBasicData2.extraData = deviceBasicData.extraData;
                } else {
                    deviceBasicData2.extraData.putAll(deviceBasicData.extraData);
                }
                deviceBasicData.localDiscoveryType = deviceBasicData2.localDiscoveryType;
            }
        }
        DeviceBasicData deviceBasicData3 = DeviceManager.getInstance().getDeviceBasicData(deviceBasicData.getDevId());
        if (deviceBasicData3 != null && !deviceBasicData3.isPluginFound) {
            deviceBasicData3.isPluginFound = true;
        }
        DeviceManager.getInstance().addDeviceBasicData(deviceBasicData);
        IDiscoveryFilter iDiscoveryFilter = this.s;
        if (iDiscoveryFilter != null && !iDiscoveryFilter.doFilter(deviceBasicData)) {
            z = false;
        }
        com.aliyun.alink.linksdk.tmp.device.d.a.a().a(deviceBasicData, TmpEnum.DiscoveryDeviceState.DISCOVERY_STATE_ONLINE);
        IDevListener iDevListener = this.f;
        ALog.d(y, "onDeviceFound tmpHander:" + iDevListener + " isNeedNotify:" + z + " mFilter:" + this.s + " basicData:" + deviceBasicData + " comboDeviceName:" + str + " nettype:" + i);
        if (iDevListener == null || !z) {
            return;
        }
        DevFoundOutputParams devFoundOutputParams = new DevFoundOutputParams();
        devFoundOutputParams.setProductKey(deviceBasicData.getProductKey());
        devFoundOutputParams.setDeviceName(deviceBasicData.getDeviceName());
        devFoundOutputParams.setModelType(deviceBasicData.getModelType());
        iDevListener.onSuccess(this.e, devFoundOutputParams);
    }

    public void a(long j) {
        this.q = j;
    }

    public void b() {
        LogCat.d(y, "onTimeOut");
        b(true);
    }

    public boolean c() {
        return this.x;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d
    protected void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar, ErrorInfo errorInfo) {
        this.p.removeCallbacks(this.n);
        this.i.b();
        if (this.f == null) {
            LogCat.e(y, "onFlowComplete handler empty error");
            return;
        }
        IDevListener iDevListener = this.f;
        this.f = null;
        iDevListener.onSuccess(this.e, null);
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.aliyun.alink.linksdk.tmp.device.a.d
    protected void b(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        this.p.removeCallbacks(this.n);
        this.i.b();
        if (this.f == null) {
            LogCat.w(y, "onFlowError empty error");
            return;
        }
        IDevListener iDevListener = this.f;
        this.f = null;
        iDevListener.onSuccess(this.e, null);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: b, reason: merged with bridge method [inline-methods] */
    public void a(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        d();
        this.x = false;
        super.a(dVar, eVar);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void b(com.aliyun.alink.linksdk.tmp.connect.d dVar, ErrorInfo errorInfo) {
        this.x = false;
        super.b(dVar, errorInfo);
    }

    protected void d() {
        ALog.d(y, "startProbeDifferentDeivces");
        List<DeviceBasicData> allDeviceDataList = DeviceManager.getInstance().getAllDeviceDataList();
        if (allDeviceDataList == null || allDeviceDataList.isEmpty()) {
            ALog.d(y, "startProbeDifferentDeivces allFoundDeviceList empty");
            return;
        }
        for (final DeviceBasicData deviceBasicData : allDeviceDataList) {
            if (!this.u.containsKey(deviceBasicData.getDevId())) {
                c cVar = new c(this.h, this.i, deviceBasicData, new IDevListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.a.a.2
                    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
                    public void onSuccess(Object obj, OutputParams outputParams) {
                        ALog.d(a.y, "ProbeTask onSuccess basicData:" + deviceBasicData);
                        com.aliyun.alink.linksdk.tmp.device.d.a.a().a(deviceBasicData, TmpEnum.DiscoveryDeviceState.DISCOVERY_STATE_ONLINE);
                    }

                    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
                    public void onFail(Object obj, ErrorInfo errorInfo) {
                        if (DevService.isDeviceWifiAndBleCombo(deviceBasicData.supportedNetType) && "2".equalsIgnoreCase(deviceBasicData.modelType)) {
                            DeviceBasicData deviceBasicData2 = DeviceManager.getInstance().getDeviceBasicData(TextHelper.combineStr(deviceBasicData.productKey, TmpStorage.getInstance().getDnByMac(deviceBasicData.mac)));
                            if (deviceBasicData2 != null) {
                                deviceBasicData2.localDiscoveryType &= ~TmpEnum.DeviceNetType.NET_BT.getValue();
                                ALog.d(a.y, " combo ble offline device localDiscoveryType :" + deviceBasicData2.localDiscoveryType + deviceBasicData2);
                            }
                        }
                        DeviceManager.getInstance().removeDeviceBasicData(deviceBasicData.getDevId());
                        ALog.d(a.y, "ProbeTask onFail basicData:" + deviceBasicData);
                        com.aliyun.alink.linksdk.tmp.device.d.a.a().a(deviceBasicData, TmpEnum.DiscoveryDeviceState.DISCOVERY_STATE_OFFLINE);
                    }
                });
                new com.aliyun.alink.linksdk.tmp.device.a.c().b(cVar).a();
                this.v.add(cVar);
            }
        }
    }

    protected void e() {
        ALog.d(y, "stopProbeDifferentDeivces");
        this.p.removeCallbacks(this.o);
        List<c> list = this.v;
        if (list == null || list.isEmpty()) {
            ALog.d(y, "stopProbeDifferentDeivces mProbeTaskFlowList empty");
            return;
        }
        Iterator<c> it = this.v.iterator();
        while (it.hasNext()) {
            it.next().b();
        }
        this.v.clear();
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.d, com.aliyun.alink.linksdk.tmp.device.a.a
    public boolean a() {
        super.a();
        if (TmpSdk.getTmpAbilityConfigValue(TmpSdk.KEY_ENABLE_LOCAL_SEARCH_GET_IP_ADDRESS) != null && RequestConstant.FALSE.equalsIgnoreCase(String.valueOf(TmpSdk.getTmpAbilityConfigValue(TmpSdk.KEY_ENABLE_LOCAL_SEARCH_GET_IP_ADDRESS)))) {
            ALog.w(y, "KEY_ENABLE_LOCAL_SEARCH_GET_IP_ADDRESS = false local control is disabled.");
            return false;
        }
        f();
        InetAddress broadcast = WifiManagerUtil.getBroadcast(WifiManagerUtil.getIpAddress(WifiManagerUtil.NetworkType.ETHERNET));
        if (broadcast != null) {
            AlcsCmpSDK.DISCOVERY_ADDR = broadcast.getHostAddress();
        }
        DiscoveryRequestPayload discoveryRequestPayload = new DiscoveryRequestPayload();
        discoveryRequestPayload.setMethod("core.service.dev");
        this.r = com.aliyun.alink.linksdk.tmp.connect.a.c.d().a(this.e).b(discoveryRequestPayload).a(broadcast != null ? broadcast.getHostAddress() : AlcsCmpSDK.DISCOVERY_ADDR).c();
        this.i.a((int) this.q, this.w, this);
        this.p.postDelayed(this.n, this.q + 1000);
        this.p.postDelayed(this.o, this.q);
        return true;
    }

    protected void f() {
        Object obj = this.t;
        if (obj == null || !(obj instanceof CloudLcaRequestParams)) {
            return;
        }
        CloudLcaRequestParams cloudLcaRequestParams = (CloudLcaRequestParams) obj;
        CloudUtils.getLcaDeviceList(cloudLcaRequestParams.groupId, cloudLcaRequestParams.gatewayIotId, new IConnectSendListener() { // from class: com.aliyun.alink.linksdk.tmp.device.a.a.a.3
            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onResponse(ARequest aRequest, AResponse aResponse) {
                ALog.d(a.y, "queryCloudLcaDeviceList onResponse aRequest:" + aRequest + " aResponse:" + aResponse);
                if (aResponse == null || aResponse.data == null) {
                    ALog.e(a.y, "getLcaDeviceList onResponse null");
                    return;
                }
                EdgeGatewaysResponsePayload edgeGatewaysResponsePayload = (EdgeGatewaysResponsePayload) GsonUtils.fromJson(String.valueOf(aResponse.data), new TypeToken<EdgeGatewaysResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.a.a.3.1
                }.getType());
                if (edgeGatewaysResponsePayload == null || edgeGatewaysResponsePayload.data == null || edgeGatewaysResponsePayload.data.edgeGateways == null || edgeGatewaysResponsePayload.data.edgeGateways.isEmpty()) {
                    ALog.e(a.y, "getLcaDeviceList payload null");
                    return;
                }
                boolean z = false;
                for (EdgeGatewaysResponsePayload.EdgeGatewaysData.EdgeGateway edgeGateway : edgeGatewaysResponsePayload.data.edgeGateways) {
                    if (edgeGateway.models != null && !edgeGateway.models.isEmpty()) {
                        for (EdgeGatewaysResponsePayload.EdgeGatewaysData.EdgeGateway.Model model : edgeGateway.models) {
                            if (model.productKeys != null && !model.productKeys.isEmpty()) {
                                for (String str : model.productKeys) {
                                    DeviceBasicData deviceBasicData = new DeviceBasicData(false);
                                    deviceBasicData.setProductKey(str);
                                    deviceBasicData.setDeviceName(model.deviceName);
                                    deviceBasicData.setModelType("4");
                                    if (a.this.s != null) {
                                        ALog.w(a.y, "mFilter true basicData:" + deviceBasicData.getDevId());
                                        if (a.this.s.doFilter(deviceBasicData)) {
                                            z = true;
                                        }
                                    } else {
                                        z = true;
                                    }
                                    IDevListener iDevListener = a.this.f;
                                    ALog.d(a.y, "queryCloudLcaDeviceList outputParams callback:" + iDevListener + " isNeedNotify:" + z + " basicData：" + deviceBasicData);
                                    if (iDevListener != null && z) {
                                        DevFoundOutputParams devFoundOutputParams = new DevFoundOutputParams();
                                        devFoundOutputParams.setProductKey(deviceBasicData.getProductKey());
                                        devFoundOutputParams.setDeviceName(deviceBasicData.getDeviceName());
                                        devFoundOutputParams.setModelType(deviceBasicData.getModelType());
                                        devFoundOutputParams.setStringValue("gatewayIotId", edgeGateway.iotId);
                                        devFoundOutputParams.setStringValue(DevFoundOutputParams.PARAMS_GATEWAY_NAME, edgeGateway.name);
                                        devFoundOutputParams.setStringValue(DevFoundOutputParams.PARAMS_MODEL_NAME, model.modelName);
                                        iDevListener.onSuccess(a.this.e, devFoundOutputParams);
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
            public void onFailure(ARequest aRequest, AError aError) {
                ALog.e(a.y, "queryCloudLcaDeviceList onFailure aRequest:" + aRequest + " aError:" + aError);
            }
        });
    }

    public boolean b(boolean z) {
        ALog.d(y, "stop isTimeout:" + z);
        if (!z) {
            e();
        }
        b(this.r, (ErrorInfo) null);
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.event.INotifyHandler
    public void onMessage(com.aliyun.alink.linksdk.tmp.connect.d dVar, e eVar) {
        a2(dVar, eVar);
    }

    /* JADX INFO: compiled from: DiscoveryTask.java */
    public static class b implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        protected WeakReference<a> f4326a;

        public b(a aVar) {
            this.f4326a = new WeakReference<>(aVar);
        }

        @Override // java.lang.Runnable
        public void run() {
            a aVar = this.f4326a.get();
            if (aVar != null) {
                aVar.b();
            }
        }
    }

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.device.a.a.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: DiscoveryTask.java */
    public static class RunnableC0223a implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        protected WeakReference<a> f4325a;

        public RunnableC0223a(a aVar) {
            this.f4325a = new WeakReference<>(aVar);
        }

        @Override // java.lang.Runnable
        public void run() {
            a aVar = this.f4325a.get();
            if (aVar != null) {
                aVar.d();
            }
        }
    }
}
