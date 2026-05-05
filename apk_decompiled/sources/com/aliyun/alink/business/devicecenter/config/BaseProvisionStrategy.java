package com.aliyun.alink.business.devicecenter.config;

import android.text.TextUtils;
import com.alibaba.ailabs.tg.basebiz.user.UserManager;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.parser.Feature;
import com.aliyun.alink.business.devicecenter.api.add.DeviceBindResultInfo;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.LKDeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.base.LocalDevice;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepository;
import com.aliyun.alink.business.devicecenter.biz.model.CheckBindTokenMtopResponse;
import com.aliyun.alink.business.devicecenter.biz.model.CheckBindTokenRequest;
import com.aliyun.alink.business.devicecenter.biz.model.CheckBindTokenResponse;
import com.aliyun.alink.business.devicecenter.cache.CacheCenter;
import com.aliyun.alink.business.devicecenter.cache.CacheType;
import com.aliyun.alink.business.devicecenter.cache.DeviceInfoICacheModel;
import com.aliyun.alink.business.devicecenter.cache.ProvisionDeviceInfoCache;
import com.aliyun.alink.business.devicecenter.channel.coap.CoAPClient;
import com.aliyun.alink.business.devicecenter.channel.coap.request.CoapRequestPayload;
import com.aliyun.alink.business.devicecenter.channel.coap.response.CoapResponsePayload;
import com.aliyun.alink.business.devicecenter.channel.coap.response.DevicePayload;
import com.aliyun.alink.business.devicecenter.channel.http.ApiRequestClient;
import com.aliyun.alink.business.devicecenter.channel.http.DCError;
import com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback;
import com.aliyun.alink.business.devicecenter.channel.http.RetryTransitoryClient;
import com.aliyun.alink.business.devicecenter.channel.http.TransitoryClient;
import com.aliyun.alink.business.devicecenter.config.model.BackupCheckType;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.model.CheckTokenModel;
import com.aliyun.alink.business.devicecenter.utils.NetworkEnvironmentUtils;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;
import com.aliyun.alink.business.devicecenter.utils.WiFiUtils;
import com.aliyun.alink.business.devicecenter.utils.WifiManagerUtil;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPContext;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPResponse;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseProvisionStrategy {
    public static String TAG = "BaseProvisionStrategy";
    public ConcurrentHashMap<String, Object> g;
    public List<CheckTokenModel> h;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Future f3535a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public AlcsCoAPRequest f3536b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public long f3537c = -1;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public IAlcsCoAPResHandler f3538d = null;
    public IDeviceInfoNotifyListener mNotifyListner = null;
    public AtomicBoolean e = new AtomicBoolean(true);
    public AtomicBoolean waitForResult = new AtomicBoolean(false);
    public AtomicBoolean f = new AtomicBoolean(false);
    public AtomicBoolean provisionHasStopped = new AtomicBoolean(false);
    public RetryTransitoryClient retryTransitoryClient = null;
    public DCAlibabaConfigParams mConfigParams = null;
    public IConfigCallback mConfigCallback = null;
    public DCErrorCode provisionErrorInfo = null;
    public TimerUtils provisionTimer = null;
    public TimerUtils provisionNetInfoTimer = null;
    public int i = 10;
    public int j = 3;
    public EnumSet<BackupCheckType> k = EnumSet.of(BackupCheckType.CHECK_APP_TOKEN, BackupCheckType.CHECK_COAP_GET);
    public ApiRequestClient l = new ApiRequestClient(false);
    public IRequestCallback m = null;
    public IAlcsCoAPReqHandler n = new IAlcsCoAPReqHandler() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.4
        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPReqHandler
        public void onReqComplete(AlcsCoAPContext alcsCoAPContext, int i, AlcsCoAPResponse alcsCoAPResponse) {
            CoapResponsePayload coapResponsePayload;
            CoAPClient.getInstance().printResponse(alcsCoAPContext, alcsCoAPResponse);
            if (alcsCoAPResponse == null || TextUtils.isEmpty(alcsCoAPResponse.getPayloadString())) {
                return;
            }
            ALog.llog((byte) 3, BaseProvisionStrategy.TAG, "waitForResult = " + BaseProvisionStrategy.this.waitForResult.get() + ", responseString=" + alcsCoAPResponse.getPayloadString());
            try {
                if (BaseProvisionStrategy.this.waitForResult.get() && (coapResponsePayload = (CoapResponsePayload) JSONObject.parseObject(alcsCoAPResponse.getPayloadString(), new TypeReference<CoapResponsePayload<LocalDevice>>() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.4.1
                }.getType(), new Feature[0])) != null && coapResponsePayload.data != 0 && BaseProvisionStrategy.this.waitForResult.get()) {
                    DeviceInfo deviceInfoConvertLocalDevice = DeviceInfo.convertLocalDevice((LocalDevice) coapResponsePayload.data);
                    if (deviceInfoConvertLocalDevice != null && !TextUtils.isEmpty(deviceInfoConvertLocalDevice.productKey) && !TextUtils.isEmpty(deviceInfoConvertLocalDevice.deviceName)) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(deviceInfoConvertLocalDevice.productKey);
                        sb.append("&");
                        sb.append(deviceInfoConvertLocalDevice.deviceName);
                        String string = sb.toString();
                        if (!BaseProvisionStrategy.this.e.get() || !BaseProvisionStrategy.this.g.containsKey(string)) {
                            ALog.d(BaseProvisionStrategy.TAG, "coAP provision success");
                            if (BaseProvisionStrategy.this.mNotifyListner != null) {
                                BaseProvisionStrategy.this.g.put(string, true);
                                BaseProvisionStrategy.this.mNotifyListner.onDeviceFound(deviceInfoConvertLocalDevice);
                                return;
                            }
                            return;
                        }
                        String str = BaseProvisionStrategy.TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("device=");
                        sb2.append(string);
                        sb2.append(" has already returned.");
                        ALog.i(str, sb2.toString());
                        return;
                    }
                    String str2 = BaseProvisionStrategy.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("pk or dn invalid, device=");
                    sb3.append(deviceInfoConvertLocalDevice);
                    ALog.i(str2, sb3.toString());
                }
            } catch (Exception e) {
                ALog.w(BaseProvisionStrategy.TAG, "startDiscovery device.info.get parsePayloadException= " + e);
            }
        }
    };

    public BaseProvisionStrategy() {
        this.g = null;
        this.h = null;
        this.g = new ConcurrentHashMap<>();
        this.h = Collections.synchronizedList(new ArrayList());
    }

    public void addProvisionOverListener(IDeviceInfoNotifyListener iDeviceInfoNotifyListener) {
        addProvisionOverListener(iDeviceInfoNotifyListener, true);
    }

    public void cancelRequest(AlcsCoAPRequest alcsCoAPRequest, long j) {
        if (alcsCoAPRequest != null) {
            alcsCoAPRequest.cancel();
        }
        if (j != -1) {
            CoAPClient.getInstance().cancelMessage(j);
        }
    }

    public String getBroadcastIp() {
        return WiFiUtils.getBroadcastIp();
    }

    public boolean isProvisionTimerStarted() {
        TimerUtils timerUtils = this.provisionTimer;
        return timerUtils != null && timerUtils.isStart(TimerUtils.MSG_PROVISION_TIMEOUT);
    }

    public void provisionResCallback(DeviceInfo deviceInfo) {
        if (deviceInfo == null || ((TextUtils.isEmpty(deviceInfo.productId) && TextUtils.isEmpty(deviceInfo.productKey)) || TextUtils.isEmpty(deviceInfo.deviceName))) {
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(false).error(this.provisionErrorInfo));
        } else {
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(true).result(deviceInfo));
        }
    }

    public void provisionResultCallback(DeviceInfo deviceInfo) {
        if (deviceInfo == null || TextUtils.isEmpty(deviceInfo.productKey) || TextUtils.isEmpty(deviceInfo.deviceName)) {
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(false).error(this.provisionErrorInfo));
        } else {
            DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).success(true).result(deviceInfo));
        }
    }

    public void provisionStatusCallback(ProvisionStatus provisionStatus) {
        ALog.i(TAG, "provisionStatusCallback, status: " + provisionStatus);
        DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(this.mConfigCallback).status(provisionStatus));
    }

    public void removeProvisionOverListener() {
        ALog.d(TAG, "removePOverListener() called");
        this.waitForResult.set(false);
        this.g.clear();
        cancelRequest(this.f3536b, this.f3537c);
        this.mNotifyListner = null;
        a();
        stopBackupCheck();
    }

    public void setCallbackOnce(boolean z) {
        this.e.set(z);
    }

    public void setProvisionResultCallback() {
        this.provisionNetInfoTimer = new TimerUtils(15000);
        this.provisionNetInfoTimer.setCallback(new TimerUtils.ITimerCallback() { // from class: com.aliyun.alink.business.devicecenter.config.-$$Lambda$BaseProvisionStrategy$xjsm4TgHvbiHUrp1pzP3OTnAPTE
            @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
            public final void onTimeout() {
                this.f$0.c();
            }
        });
        this.provisionNetInfoTimer.start(TimerUtils.MSG_GET_NETWORK_ENV_TIMEOUT);
        ThreadPool.execute(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.config.-$$Lambda$BaseProvisionStrategy$M1o_Nr2cQ2tIWE0EaQQRB7VmJOM
            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.d();
            }
        });
    }

    public void startBackupCheck(boolean z, long j) {
        ALog.d(TAG, "startBackupCheck() called with: needSend = [" + z + "], delay = [" + j + "]], checkTypeList = [" + this.k + "]");
        if (z && this.f.get()) {
            ALog.d(TAG, "startBackupCheck has already started.");
            return;
        }
        EnumSet<BackupCheckType> enumSet = this.k;
        if (enumSet == null || enumSet.isEmpty()) {
            ALog.d(TAG, "startBackupCheck  invalid, return.");
            return;
        }
        this.f.set(z);
        try {
            if (!this.f.get()) {
                this.h.clear();
                a(this.f3535a);
                return;
            }
            if (this.waitForResult.get() && this.mConfigParams != null && !TextUtils.isEmpty(this.mConfigParams.bindToken)) {
                a(this.mConfigCallback, this.mConfigParams.bindToken);
            }
            a(this.f3535a);
            this.f3535a = ThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.1
                @Override // java.lang.Runnable
                public void run() {
                    ALog.d(BaseProvisionStrategy.TAG, "run waitForResult=" + BaseProvisionStrategy.this.waitForResult.get() + ",needBackupCheck=" + BaseProvisionStrategy.this.f.get() + ", mCheckTypeEnumSet=" + BaseProvisionStrategy.this.k);
                    if (BaseProvisionStrategy.this.waitForResult.get() && BaseProvisionStrategy.this.f.get()) {
                        synchronized (BaseProvisionStrategy.this.k) {
                            if (BaseProvisionStrategy.this.k != null && !BaseProvisionStrategy.this.k.isEmpty()) {
                                boolean zContains = BaseProvisionStrategy.this.k.contains(BackupCheckType.CHECK_APP_TOKEN);
                                boolean zContains2 = BaseProvisionStrategy.this.k.contains(BackupCheckType.CHECK_CLOUD_TOKEN);
                                boolean zContains3 = BaseProvisionStrategy.this.k.contains(BackupCheckType.CHECK_COAP_GET);
                                String str = BaseProvisionStrategy.TAG;
                                StringBuilder sb = new StringBuilder();
                                sb.append("startBackupCheck checkToken=");
                                sb.append(zContains);
                                sb.append(", checkILopToken=");
                                sb.append(zContains2);
                                sb.append(", checkCoAPGet=");
                                sb.append(zContains3);
                                ALog.d(str, sb.toString());
                                if (zContains3) {
                                    BaseProvisionStrategy.this.b();
                                }
                                if (BaseProvisionStrategy.this.mConfigParams != null && !TextUtils.isEmpty(BaseProvisionStrategy.this.mConfigParams.bindToken)) {
                                    if (zContains && !BaseProvisionStrategy.this.mConfigParams.isInSide) {
                                        BaseProvisionStrategy.this.c(BaseProvisionStrategy.this.mConfigParams.productKey, BaseProvisionStrategy.this.mConfigParams.deviceName, BaseProvisionStrategy.this.mConfigParams.bindToken);
                                    }
                                    if (zContains2) {
                                        BaseProvisionStrategy.this.a(BaseProvisionStrategy.this.mConfigParams.productKey, BaseProvisionStrategy.this.mConfigParams.deviceName, BaseProvisionStrategy.this.mConfigParams.bindToken);
                                        BaseProvisionStrategy.this.b(BaseProvisionStrategy.this.mConfigParams.productKey, BaseProvisionStrategy.this.mConfigParams.deviceName, BaseProvisionStrategy.this.mConfigParams.bindToken);
                                    }
                                }
                            }
                        }
                    }
                }
            }, j, this.j, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startProvisionTimer() {
        startProvisionTimer(true, null);
    }

    public void stopBackupCheck() {
        startBackupCheck(false, 0L);
    }

    public void stopProvisionTimer() {
        ALog.d(TAG, "stopProvisionTimer() called");
        TimerUtils timerUtils = this.provisionTimer;
        if (timerUtils != null) {
            timerUtils.stop(TimerUtils.MSG_PROVISION_TIMEOUT);
            this.provisionTimer = null;
        }
        TimerUtils timerUtils2 = this.provisionNetInfoTimer;
        if (timerUtils2 != null) {
            timerUtils2.stop(TimerUtils.MSG_GET_NETWORK_ENV_TIMEOUT);
            this.provisionNetInfoTimer = null;
        }
    }

    public void updateBackupCheckType(DeviceReportTokenType deviceReportTokenType) {
        updateBackupCheckType(deviceReportTokenType, true);
    }

    public void updateBackupCheckTypeSet(EnumSet<BackupCheckType> enumSet) {
        ALog.d(TAG, "updateBackupCheckTypeSet() called with: BackupCheckType = [" + enumSet + "]");
        this.k = enumSet;
    }

    public void updateCache(DeviceInfo deviceInfo, DeviceReportTokenType deviceReportTokenType) {
        DCAlibabaConfigParams dCAlibabaConfigParams;
        int i;
        if (deviceInfo == null || TextUtils.isEmpty(deviceInfo.productKey) || TextUtils.isEmpty(deviceInfo.deviceName)) {
            return;
        }
        DeviceBindResultInfo deviceBindResultInfo = deviceInfo.bindResultInfo;
        if (deviceBindResultInfo != null && !TextUtils.isEmpty(deviceBindResultInfo.productKey) && !TextUtils.isEmpty(deviceInfo.bindResultInfo.deviceName) && ((i = deviceInfo.bindResultInfo.bindResult) == 1 || i == 2)) {
            ALog.d(TAG, "bind result returned, do not cache.");
            return;
        }
        ProvisionDeviceInfoCache.getInstance().clearCache();
        DevicePayload devicePayload = new DevicePayload();
        devicePayload.productKey = deviceInfo.productKey;
        devicePayload.deviceName = deviceInfo.deviceName;
        if (TextUtils.isEmpty(deviceInfo.token)) {
            devicePayload.token = null;
            devicePayload.remainTime = null;
        } else {
            devicePayload.token = deviceInfo.token;
            devicePayload.remainTime = TextUtils.isEmpty(deviceInfo.remainTime) ? String.valueOf(30000) : deviceInfo.remainTime;
        }
        ProvisionDeviceInfoCache.getInstance().updateCache(devicePayload);
        CacheCenter.getInstance().clearCache(CacheType.APP_SEND_TOKEN);
        if (!TextUtils.isEmpty(deviceInfo.token) || (dCAlibabaConfigParams = this.mConfigParams) == null || TextUtils.isEmpty(dCAlibabaConfigParams.bindToken)) {
            return;
        }
        ALog.d(TAG, "update TryCheckTokenCache with bindToken=" + this.mConfigParams.bindToken + ",deviceInfo=" + deviceInfo);
        ArrayList arrayList = new ArrayList();
        DeviceInfoICacheModel deviceInfoICacheModel = new DeviceInfoICacheModel();
        deviceInfoICacheModel.productKey = deviceInfo.productKey;
        deviceInfoICacheModel.deviceName = deviceInfo.deviceName;
        deviceInfoICacheModel.token = this.mConfigParams.bindToken;
        deviceInfoICacheModel.aliveTime = System.currentTimeMillis() + (a(deviceInfo.remainTime) < 0 ? 45000L : a(deviceInfo.remainTime));
        deviceInfoICacheModel.deviceReportTokenType = deviceReportTokenType;
        arrayList.add(deviceInfoICacheModel);
        CacheCenter.getInstance().updateCache(CacheType.APP_SEND_TOKEN, (List) arrayList);
    }

    /* JADX INFO: Access modifiers changed from: private */
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
    public /* synthetic */ void d() {
        try {
            if (this.provisionHasStopped.get()) {
                return;
            }
            HashMap<String, String> mapPing = NetworkEnvironmentUtils.ping("g-aicloud.alibaba.com", false);
            if (mapPing != null && TextUtils.isEmpty(mapPing.get("res"))) {
                mapPing.put("res", "ping error");
            }
            if (mapPing != null) {
                mapPing.remove("res");
            }
            HashMap<String, String> mapPing2 = NetworkEnvironmentUtils.ping("cn-hangzhou.log.aliyuncs.com", false);
            if (mapPing2 != null && TextUtils.isEmpty(mapPing2.get("res"))) {
                mapPing2.put("res", "ping error");
            }
            if (mapPing != null) {
                mapPing2.remove("res");
            }
            JSONArray jSONArray = new JSONArray();
            JSONObject jSONObject = new JSONObject();
            jSONObject.putAll(mapPing);
            JSONObject jSONObject2 = new JSONObject();
            jSONObject2.putAll(mapPing2);
            jSONArray.add(jSONObject);
            jSONArray.add(jSONObject2);
            this.provisionErrorInfo.setExtra(jSONArray);
            if (mapPing != null) {
                for (String str : mapPing.keySet()) {
                    String str2 = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("setProvisionResultCallback: key：");
                    sb.append(str);
                    sb.append(";value=");
                    sb.append(mapPing.get(str));
                    ALog.d(str2, sb.toString());
                }
            }
            if (mapPing2 != null) {
                for (String str3 : mapPing2.keySet()) {
                    String str4 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("setProvisionResultCallback: key：");
                    sb2.append(str3);
                    sb2.append(";value=");
                    sb2.append(mapPing2.get(str3));
                    ALog.d(str4, sb2.toString());
                }
            }
            if (this.provisionNetInfoTimer != null) {
                this.provisionNetInfoTimer.stop(TimerUtils.MSG_GET_NETWORK_ENV_TIMEOUT);
                this.provisionNetInfoTimer = null;
            }
            provisionResultCallback(null);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void addProvisionOverListener(final IDeviceInfoNotifyListener iDeviceInfoNotifyListener, boolean z) {
        removeProvisionOverListener();
        this.g.clear();
        this.mNotifyListner = iDeviceInfoNotifyListener;
        this.waitForResult.set(true);
        if (!z || this.mConfigParams.isInSide) {
            return;
        }
        this.f3538d = new CoAPProvisionOverNotifyHandler(new IDeviceInfoNotifyListener() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.3
            @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
            public void onDeviceFound(DeviceInfo deviceInfo) {
                ALog.d(BaseProvisionStrategy.TAG, "waitForResult=" + BaseProvisionStrategy.this.waitForResult.get() + ", listener=" + iDeviceInfoNotifyListener);
                if (deviceInfo == null || TextUtils.isEmpty(deviceInfo.productKey) || TextUtils.isEmpty(deviceInfo.deviceName) || !BaseProvisionStrategy.this.waitForResult.get()) {
                    return;
                }
                String str = deviceInfo.productKey + "&" + deviceInfo.deviceName;
                if (!BaseProvisionStrategy.this.e.get() || !BaseProvisionStrategy.this.g.containsKey(str)) {
                    if (iDeviceInfoNotifyListener != null) {
                        BaseProvisionStrategy.this.g.put(str, true);
                        iDeviceInfoNotifyListener.onDeviceFound(deviceInfo);
                        return;
                    }
                    return;
                }
                ALog.i(BaseProvisionStrategy.TAG, "device=" + str + " has already returned.");
            }

            @Override // com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener
            public void onFailure(DCErrorCode dCErrorCode) {
            }
        });
        CoAPClient.getInstance().addNotifyListener(this.f3538d);
    }

    public void startProvisionTimer(TimerUtils.ITimerCallback iTimerCallback) {
        startProvisionTimer(true, iTimerCallback);
    }

    public void updateBackupCheckType(DeviceReportTokenType deviceReportTokenType, boolean z) {
        ALog.d(TAG, "updateBackupCheckTypeSet() called with: type = [" + deviceReportTokenType + "]");
        if (deviceReportTokenType == DeviceReportTokenType.APP_TOKEN) {
            updateBackupCheckTypeSet(z ? EnumSet.of(BackupCheckType.CHECK_COAP_GET, BackupCheckType.CHECK_APP_TOKEN) : EnumSet.of(BackupCheckType.CHECK_APP_TOKEN));
        } else if (deviceReportTokenType == DeviceReportTokenType.CLOUD_TOKEN) {
            updateBackupCheckTypeSet(z ? EnumSet.of(BackupCheckType.CHECK_COAP_GET, BackupCheckType.CHECK_CLOUD_TOKEN) : EnumSet.of(BackupCheckType.CHECK_CLOUD_TOKEN));
        } else if (deviceReportTokenType == DeviceReportTokenType.UNKNOWN) {
            updateBackupCheckTypeSet(z ? EnumSet.of(BackupCheckType.CHECK_COAP_GET, BackupCheckType.CHECK_APP_TOKEN, BackupCheckType.CHECK_CLOUD_TOKEN) : EnumSet.of(BackupCheckType.CHECK_COAP_GET, BackupCheckType.CHECK_APP_TOKEN, BackupCheckType.CHECK_CLOUD_TOKEN));
        }
    }

    public final void b() {
        try {
            CoapRequestPayload coapRequestPayload = new CoapRequestPayload();
            coapRequestPayload.getClass();
            CoapRequestPayload coapRequestPayloadBuild = new CoapRequestPayload.Builder().version("1.0").params(new HashMap()).method(AlinkConstants.COAP_METHOD_AWSS_CONNECTAP_GET).build();
            cancelRequest(this.f3536b, this.f3537c);
            this.f3536b = new AlcsCoAPRequest(AlcsCoAPConstant.Code.GET, AlcsCoAPConstant.Type.NON);
            InetAddress ipAddress = WifiManagerUtil.getIpAddress(WifiManagerUtil.NetworkType.WLAN);
            if (ipAddress == null) {
                ALog.w(TAG, "getIpAddress address=null.");
                try {
                    ipAddress = InetAddress.getByName(WifiManagerUtil.getWifiIP(DeviceCenterBiz.getInstance().getAppContext()));
                } catch (UnknownHostException e) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("getWifiIP  getByName exception=");
                    sb.append(e);
                    ALog.w(str, sb.toString());
                }
            }
            InetAddress broadcast = null;
            if (ipAddress != null) {
                String str2 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("address not null, ip=");
                sb2.append(ipAddress.getHostAddress());
                ALog.d(str2, sb2.toString());
                try {
                    broadcast = WifiManagerUtil.getBroadcast(ipAddress);
                } catch (Exception e2) {
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("getBroadcast exception=");
                    sb3.append(e2);
                    ALog.w(str3, sb3.toString());
                }
            }
            String hostAddress = broadcast == null ? "255.255.255.255" : broadcast.getHostAddress();
            StringBuilder sb4 = new StringBuilder();
            sb4.append(hostAddress);
            sb4.append(":");
            sb4.append(5683);
            sb4.append(AlinkConstants.COAP_PATH_AWSS_CONNECTAP_GET);
            String string = sb4.toString();
            this.f3536b.setPayload(coapRequestPayloadBuild.toString());
            String str4 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("setPayload=");
            sb5.append(coapRequestPayloadBuild.toString());
            sb5.append(",getPayload=");
            sb5.append(this.f3536b.getPayloadString());
            ALog.llog((byte) 3, str4, sb5.toString());
            this.f3536b.setMulticast(1);
            this.f3536b.setURI(string);
            String str5 = TAG;
            StringBuilder sb6 = new StringBuilder();
            sb6.append("coapUri=");
            sb6.append(string);
            ALog.d(str5, sb6.toString());
        } catch (Exception e3) {
            ALog.w(TAG, "pre sendRequest params exception=" + e3);
        }
        this.f3537c = CoAPClient.getInstance().sendRequest(this.f3536b, this.n);
    }

    public final void c(String str, String str2, final String str3) {
        ALog.d(TAG, "checkToken() called with: pk = [" + str + "], dn = [" + str2 + "], token = [" + str3 + "]");
        try {
            if (!DCEnvHelper.hasApiClient()) {
                ALog.w(TAG, "checkToken no apiclient, return.");
            } else {
                ProvisionRepository.checkToken(str, str2, str3, new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.6
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest, Exception exc) {
                        ALog.w(BaseProvisionStrategy.TAG, "checkToken onFailure e=" + exc);
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                        ALog.d(BaseProvisionStrategy.TAG, "checkToken onResponse request=" + TransitoryClient.getInstance().requestToStr(ioTRequest) + ",response=" + TransitoryClient.getInstance().responseToStr(ioTResponse));
                        try {
                            if (BaseProvisionStrategy.this.waitForResult.get() && ioTResponse != null && ioTResponse.getCode() == 200 && ioTResponse.getData() != null) {
                                DeviceInfo deviceInfo = new DeviceInfo();
                                LKDeviceInfo lKDeviceInfo = (LKDeviceInfo) JSONObject.parseObject(ioTResponse.getData().toString(), LKDeviceInfo.class);
                                if (lKDeviceInfo == null) {
                                    String str4 = BaseProvisionStrategy.TAG;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append("invalid data:");
                                    sb.append(ioTResponse.getData());
                                    ALog.w(str4, sb.toString());
                                    return;
                                }
                                deviceInfo.deviceName = lKDeviceInfo.deviceName;
                                deviceInfo.productKey = lKDeviceInfo.productKey;
                                deviceInfo.token = str3;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(deviceInfo.productKey);
                                sb2.append("&");
                                sb2.append(deviceInfo.deviceName);
                                String string = sb2.toString();
                                if (!BaseProvisionStrategy.this.e.get() || !BaseProvisionStrategy.this.g.containsKey(string)) {
                                    ALog.i(BaseProvisionStrategy.TAG, "Provision success from check token. ");
                                    if (BaseProvisionStrategy.this.mNotifyListner != null) {
                                        BaseProvisionStrategy.this.g.put(string, true);
                                        BaseProvisionStrategy.this.mNotifyListner.onDeviceFound(deviceInfo);
                                        return;
                                    }
                                    return;
                                }
                                String str5 = BaseProvisionStrategy.TAG;
                                StringBuilder sb3 = new StringBuilder();
                                sb3.append("device=");
                                sb3.append(string);
                                sb3.append(" has already returned.");
                                ALog.i(str5, sb3.toString());
                            }
                        } catch (Exception e) {
                            ALog.w(BaseProvisionStrategy.TAG, "checkToken exception= " + e);
                        }
                    }
                });
            }
        } catch (Throwable th) {
            ALog.i(TAG, "checkToken exception=" + th);
            th.printStackTrace();
        }
    }

    public void cancelRequest(RetryTransitoryClient retryTransitoryClient) {
        if (retryTransitoryClient != null) {
            retryTransitoryClient.cancelRequest();
        }
    }

    public void startProvisionTimer(final boolean z, final TimerUtils.ITimerCallback iTimerCallback) {
        ALog.d(TAG, "startProvisionTimer() called with: timerCallback = [" + iTimerCallback + "]");
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        if (dCAlibabaConfigParams == null) {
            return;
        }
        this.provisionTimer = new TimerUtils(dCAlibabaConfigParams.timeout * 1000);
        this.provisionTimer.setCallback(new TimerUtils.ITimerCallback() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.9
            @Override // com.aliyun.alink.business.devicecenter.utils.TimerUtils.ITimerCallback
            public void onTimeout() {
                TimerUtils.ITimerCallback iTimerCallback2 = iTimerCallback;
                if (iTimerCallback2 != null) {
                    iTimerCallback2.onTimeout();
                }
                if (z) {
                    BaseProvisionStrategy.this.provisionResultCallback(null);
                }
            }
        });
        this.provisionTimer.start(TimerUtils.MSG_PROVISION_TIMEOUT);
    }

    public final void a(IConfigCallback iConfigCallback, String str) {
        ALog.d(TAG, "notifyBindToken() called with: callback = [" + iConfigCallback + "], token = [" + str + "]");
        ProvisionStatus provisionStatus = ProvisionStatus.PROVISION_APP_TOKEN;
        provisionStatus.addExtraParams("appToken", str);
        DeviceCenterBiz.getInstance().onConfigCallback(new ConfigCallbackWrapper().callback(iConfigCallback).status(provisionStatus));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void c() {
        provisionResultCallback(null);
    }

    public final long a(String str) {
        try {
            return Long.valueOf(str).longValue();
        } catch (Exception unused) {
            return -1L;
        }
    }

    public final void a() {
        if (this.f3538d != null) {
            CoAPClient.getInstance().removeNotifyListener(this.f3538d);
            this.f3538d = null;
        }
    }

    public final void a(Future future) {
        if (future != null) {
            try {
                future.cancel(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startBackupCheck(boolean z, long j, List<CheckTokenModel> list) {
        ALog.d(TAG, "startBackupCheck() called with: needSend = [" + z + "], delay = [" + j + "] enrolleeList = [" + list + "]");
        if (list == null || list.size() < 1) {
            return;
        }
        if (z && this.f.get()) {
            ALog.d(TAG, "startBackupCheck has already started.");
            return;
        }
        this.f.set(z);
        try {
            if (this.f.get()) {
                if (this.waitForResult.get() && this.mConfigParams != null && !TextUtils.isEmpty(this.mConfigParams.bindToken)) {
                    a(this.mConfigCallback, this.mConfigParams.bindToken);
                }
                a(this.f3535a);
                this.h.addAll(list);
                this.f3535a = ThreadPool.scheduleAtFixedRate(new Runnable() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.2
                    @Override // java.lang.Runnable
                    public void run() {
                        ALog.d(BaseProvisionStrategy.TAG, "run waitForResult=" + BaseProvisionStrategy.this.waitForResult.get() + ",needBackupCheck=" + BaseProvisionStrategy.this.f.get());
                        if (BaseProvisionStrategy.this.waitForResult.get() && BaseProvisionStrategy.this.f.get()) {
                            BaseProvisionStrategy.this.b();
                            BaseProvisionStrategy baseProvisionStrategy = BaseProvisionStrategy.this;
                            baseProvisionStrategy.a((List<CheckTokenModel>) baseProvisionStrategy.h, BaseProvisionStrategy.this.mConfigParams.bindToken);
                        }
                    }
                }, j, this.j, TimeUnit.SECONDS);
                return;
            }
            this.h.clear();
            a(this.f3535a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final void a(List<CheckTokenModel> list, final String str) {
        ALog.d(TAG, "checkTokens() called with: checkTokenModelList = [" + list + "], token = [" + str + "]");
        try {
            ProvisionRepository.checkTokens(list, new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.5
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    ALog.w(BaseProvisionStrategy.TAG, "checkToken onFailure e=" + exc);
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    ALog.d(BaseProvisionStrategy.TAG, "checkToken onResponse request=" + TransitoryClient.getInstance().requestToStr(ioTRequest) + ",response=" + TransitoryClient.getInstance().responseToStr(ioTResponse));
                    try {
                        if (BaseProvisionStrategy.this.waitForResult.get() && ioTResponse != null && ioTResponse.getCode() == 200 && ioTResponse.getData() != null) {
                            JSONArray array = JSONArray.parseArray(ioTResponse.getData().toString());
                            for (int i = 0; i < array.size(); i++) {
                                if (array.getJSONObject(i) != null) {
                                    DeviceInfo deviceInfo = new DeviceInfo();
                                    deviceInfo.productKey = array.getJSONObject(i).getString("productKey");
                                    deviceInfo.deviceName = array.getJSONObject(i).getString("deviceName");
                                    deviceInfo.token = str;
                                    StringBuilder sb = new StringBuilder();
                                    sb.append(deviceInfo.productKey);
                                    sb.append("&");
                                    sb.append(deviceInfo.deviceName);
                                    String string = sb.toString();
                                    if (BaseProvisionStrategy.this.mNotifyListner != null && !BaseProvisionStrategy.this.g.containsKey(string)) {
                                        BaseProvisionStrategy.this.g.put(string, true);
                                        BaseProvisionStrategy.this.mNotifyListner.onDeviceFound(deviceInfo);
                                        CheckTokenModel checkTokenModel = new CheckTokenModel();
                                        checkTokenModel.productKey = deviceInfo.productKey;
                                        checkTokenModel.deviceName = deviceInfo.deviceName;
                                        checkTokenModel.bindToken = deviceInfo.token;
                                        BaseProvisionStrategy.this.h.remove(checkTokenModel);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        ALog.w(BaseProvisionStrategy.TAG, "checkToken exception= " + e);
                    }
                }
            });
        } catch (Exception e) {
            ALog.i(TAG, "checkToken exception=" + e);
            e.printStackTrace();
        }
    }

    public final void a(final String str, final String str2, final String str3) {
        ALog.d(TAG, "checkILopCloudToken() called with: pk = [" + str + "], dn = [" + str2 + "], token = [" + str3 + "]");
        try {
            if (!DCEnvHelper.hasApiClient()) {
                ALog.w(TAG, "checkToken no apiclient, return.");
            } else {
                ProvisionRepository.iLopTokenCheck(str, str2, str3, new IoTCallback() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.7
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest, Exception exc) {
                        ALog.w(BaseProvisionStrategy.TAG, "checkILopCloudToken onFailure e=" + exc);
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                        ALog.d(BaseProvisionStrategy.TAG, "checkILopCloudToken onResponse request=" + TransitoryClient.getInstance().requestToStr(ioTRequest) + ",response=" + TransitoryClient.getInstance().responseToStr(ioTResponse));
                        try {
                            if (BaseProvisionStrategy.this.waitForResult.get() && ioTResponse != null && ioTResponse.getCode() == 200 && ioTResponse.getData() != null) {
                                DeviceInfo deviceInfo = new DeviceInfo();
                                DeviceBindResultInfo firstBindResultInfo = DeviceBindResultInfo.getFirstBindResultInfo(str, str2, ioTResponse.getData().toString());
                                if (firstBindResultInfo != null && !TextUtils.isEmpty(firstBindResultInfo.productKey) && !TextUtils.isEmpty(firstBindResultInfo.deviceName)) {
                                    if (!TextUtils.isEmpty(str) && !str.equals(firstBindResultInfo.productKey)) {
                                        String str4 = BaseProvisionStrategy.TAG;
                                        StringBuilder sb = new StringBuilder();
                                        sb.append("pk not equal, local = ");
                                        sb.append(str);
                                        sb.append(", cloud = ");
                                        sb.append(firstBindResultInfo.productKey);
                                        ALog.w(str4, sb.toString());
                                        return;
                                    }
                                    if (!TextUtils.isEmpty(str2) && !str2.equals(firstBindResultInfo.deviceName)) {
                                        String str5 = BaseProvisionStrategy.TAG;
                                        StringBuilder sb2 = new StringBuilder();
                                        sb2.append("dn not equal, local = ");
                                        sb2.append(str2);
                                        sb2.append(", cloud = ");
                                        sb2.append(firstBindResultInfo.deviceName);
                                        ALog.w(str5, sb2.toString());
                                        return;
                                    }
                                    deviceInfo.productKey = firstBindResultInfo.productKey;
                                    deviceInfo.deviceName = firstBindResultInfo.deviceName;
                                    deviceInfo.token = str3;
                                    deviceInfo.bindResultInfo = firstBindResultInfo;
                                    StringBuilder sb3 = new StringBuilder();
                                    sb3.append(deviceInfo.productKey);
                                    sb3.append("&");
                                    sb3.append(deviceInfo.deviceName);
                                    String string = sb3.toString();
                                    if (BaseProvisionStrategy.this.e.get() && BaseProvisionStrategy.this.g.containsKey(string)) {
                                        String str6 = BaseProvisionStrategy.TAG;
                                        StringBuilder sb4 = new StringBuilder();
                                        sb4.append("device=");
                                        sb4.append(string);
                                        sb4.append(" has already returned.");
                                        ALog.i(str6, sb4.toString());
                                        return;
                                    }
                                    String str7 = BaseProvisionStrategy.TAG;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("checkingCloudToken fail，bindResult");
                                    sb5.append(firstBindResultInfo.insideResult);
                                    ALog.d(str7, sb5.toString());
                                    if (firstBindResultInfo.bindResult != 1) {
                                        if (firstBindResultInfo.bindResult == 2) {
                                            DCErrorCode subcode = new DCErrorCode("BindFail", DCErrorCode.PF_PROVISION_CLOUD_BIND_ERROR).setMsg(TextUtils.isEmpty(firstBindResultInfo.localizedMsg) ? "bind fail." : firstBindResultInfo.localizedMsg).setSubcode(firstBindResultInfo.insideResult);
                                            if (BaseProvisionStrategy.this.mNotifyListner != null) {
                                                BaseProvisionStrategy.this.mNotifyListner.onFailure(subcode);
                                                return;
                                            }
                                            return;
                                        }
                                        if (firstBindResultInfo.bindResult != 3) {
                                            ALog.d(BaseProvisionStrategy.TAG, "checkILopCloudToken device binding, return.");
                                            return;
                                        } else {
                                            if (BaseProvisionStrategy.this.mNotifyListner != null) {
                                                BaseProvisionStrategy.this.mNotifyListner.onDeviceFound(deviceInfo);
                                                return;
                                            }
                                            return;
                                        }
                                    }
                                    if (100 != firstBindResultInfo.insideResult && 1 != firstBindResultInfo.insideResult && BaseProvisionStrategy.this.mConfigParams.isInSide) {
                                        if (2 == firstBindResultInfo.insideResult) {
                                            DCErrorCode subcode2 = new DCErrorCode("BindFail", DCErrorCode.PF_PROVISION_CLOUD_BIND_ERROR).setMsg(TextUtils.isEmpty(firstBindResultInfo.localizedMsg) ? "inside bind err." : firstBindResultInfo.localizedMsg).setSubcode(firstBindResultInfo.insideResult);
                                            if (BaseProvisionStrategy.this.mNotifyListner != null) {
                                                BaseProvisionStrategy.this.mNotifyListner.onFailure(subcode2);
                                                return;
                                            }
                                            return;
                                        }
                                        DCErrorCode subcode3 = new DCErrorCode("BindFail", DCErrorCode.PF_PROVISION_INSIDE_BIND_ERROR).setMsg(TextUtils.isEmpty(firstBindResultInfo.localizedMsg) ? "inside bind err." : firstBindResultInfo.localizedMsg).setSubcode(firstBindResultInfo.insideResult);
                                        if (BaseProvisionStrategy.this.mNotifyListner != null) {
                                            BaseProvisionStrategy.this.mNotifyListner.onFailure(subcode3);
                                            return;
                                        }
                                        return;
                                    }
                                    ALog.i(BaseProvisionStrategy.TAG, "Provision success from check ilop token. ");
                                    if (BaseProvisionStrategy.this.mNotifyListner != null) {
                                        BaseProvisionStrategy.this.g.put(string, true);
                                        BaseProvisionStrategy.this.mNotifyListner.onDeviceFound(deviceInfo);
                                        return;
                                    }
                                    return;
                                }
                                String str8 = BaseProvisionStrategy.TAG;
                                StringBuilder sb6 = new StringBuilder();
                                sb6.append("invalid ilop data:");
                                sb6.append(ioTResponse.getData());
                                ALog.w(str8, sb6.toString());
                            }
                        } catch (Exception e) {
                            ALog.w(BaseProvisionStrategy.TAG, "checkILopCloudToken exception= " + e);
                        }
                    }
                });
            }
        } catch (Throwable th) {
            ALog.i(TAG, "checkILopCloudToken exception=" + th);
            th.printStackTrace();
        }
    }

    public final void b(final String str, final String str2, final String str3) {
        ALog.d(TAG, "checkILopTgCloudToken() called with: pk = [" + str + "], dn = [" + str2 + "], token = [" + str3 + "]");
        try {
            if (!DCEnvHelper.isTgEnv()) {
                ALog.d(TAG, "checkILopTgCloudToken not tg return.");
                return;
            }
            CheckBindTokenRequest checkBindTokenRequest = new CheckBindTokenRequest();
            checkBindTokenRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
            checkBindTokenRequest.setProductKey(str);
            checkBindTokenRequest.setDeviceName(str2);
            checkBindTokenRequest.setToken(str3);
            if (this.m == null) {
                this.m = new IRequestCallback() { // from class: com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy.8
                    @Override // com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback
                    public void onFail(DCError dCError, Object obj) {
                        ALog.d(BaseProvisionStrategy.TAG, "checkILopTgCloudToken onFail dcError=" + dCError + ", response=" + obj);
                    }

                    @Override // com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback
                    public void onSuccess(Object obj) {
                        ALog.d(BaseProvisionStrategy.TAG, "checkILopTgCloudToken onSuccess() called with: data = [" + obj + "] ,wait=" + BaseProvisionStrategy.this.waitForResult);
                        try {
                            if (BaseProvisionStrategy.this.waitForResult.get()) {
                                if (!(obj instanceof CheckBindTokenMtopResponse)) {
                                    ALog.d(BaseProvisionStrategy.TAG, "checkBindTokenMtopResponse == null.");
                                    return;
                                }
                                CheckBindTokenMtopResponse checkBindTokenMtopResponse = (CheckBindTokenMtopResponse) obj;
                                if (checkBindTokenMtopResponse.m34getData() == null) {
                                    ALog.d(BaseProvisionStrategy.TAG, "checkBindTokenMtopResponse.getData == null.");
                                    return;
                                }
                                CheckBindTokenMtopResponse.Data dataM34getData = checkBindTokenMtopResponse.m34getData();
                                if (!dataM34getData.isSuccess()) {
                                    ALog.d(BaseProvisionStrategy.TAG, "responseData.isSuccess=false.");
                                    return;
                                }
                                List<CheckBindTokenResponse> model = dataM34getData.getModel();
                                if (model != null && !model.isEmpty()) {
                                    int size = model.size();
                                    for (int i = 0; i < size; i++) {
                                        CheckBindTokenResponse checkBindTokenResponse = model.get(i);
                                        if (checkBindTokenResponse != null && !TextUtils.isEmpty(checkBindTokenResponse.getDeviceName()) && !TextUtils.isEmpty(checkBindTokenResponse.getProductKey()) && !TextUtils.isEmpty(checkBindTokenResponse.getDeviceName()) && !TextUtils.isEmpty(checkBindTokenResponse.getProductKey())) {
                                            DeviceInfo deviceInfo = new DeviceInfo();
                                            DeviceBindResultInfo tgBindResultInfo = DeviceBindResultInfo.getTgBindResultInfo(checkBindTokenResponse);
                                            if (!TextUtils.isEmpty(str) && !str.equals(tgBindResultInfo.productKey)) {
                                                String str4 = BaseProvisionStrategy.TAG;
                                                StringBuilder sb = new StringBuilder();
                                                sb.append("checkILopTgCloudToken pk not equal, local = ");
                                                sb.append(str);
                                                sb.append(", cloud = ");
                                                sb.append(tgBindResultInfo.productKey);
                                                ALog.w(str4, sb.toString());
                                                return;
                                            }
                                            if (!TextUtils.isEmpty(str2) && !str2.equals(tgBindResultInfo.deviceName)) {
                                                String str5 = BaseProvisionStrategy.TAG;
                                                StringBuilder sb2 = new StringBuilder();
                                                sb2.append("checkILopTgCloudToken dn not equal, local = ");
                                                sb2.append(str2);
                                                sb2.append(", cloud = ");
                                                sb2.append(tgBindResultInfo.deviceName);
                                                ALog.w(str5, sb2.toString());
                                                return;
                                            }
                                            deviceInfo.productKey = tgBindResultInfo.productKey;
                                            deviceInfo.deviceName = tgBindResultInfo.deviceName;
                                            deviceInfo.token = str3;
                                            deviceInfo.bindResultInfo = tgBindResultInfo;
                                            if (tgBindResultInfo.bindResult == 0) {
                                                ALog.d(BaseProvisionStrategy.TAG, "checkILopTgCloudToken device binding, return.");
                                                return;
                                            }
                                            StringBuilder sb3 = new StringBuilder();
                                            sb3.append(deviceInfo.productKey);
                                            sb3.append("&");
                                            sb3.append(deviceInfo.deviceName);
                                            String string = sb3.toString();
                                            if (BaseProvisionStrategy.this.e.get() && BaseProvisionStrategy.this.g.containsKey(string)) {
                                                String str6 = BaseProvisionStrategy.TAG;
                                                StringBuilder sb4 = new StringBuilder();
                                                sb4.append("checkILopTgCloudToken device=");
                                                sb4.append(string);
                                                sb4.append(" has already returned.");
                                                ALog.i(str6, sb4.toString());
                                                return;
                                            }
                                            ALog.i(BaseProvisionStrategy.TAG, "checkILopTgCloudToken Provision success from check ilop & tg token. ");
                                            if (BaseProvisionStrategy.this.mNotifyListner != null) {
                                                BaseProvisionStrategy.this.g.put(string, true);
                                                BaseProvisionStrategy.this.mNotifyListner.onDeviceFound(deviceInfo);
                                            }
                                        }
                                    }
                                    return;
                                }
                                ALog.d(BaseProvisionStrategy.TAG, "responseData.modelList=null.");
                            }
                        } catch (Exception e) {
                            ALog.w(BaseProvisionStrategy.TAG, "checkILopTgCloudToken exception= " + e);
                        }
                    }
                };
            }
            this.l.send(checkBindTokenRequest, CheckBindTokenMtopResponse.class, this.m);
        } catch (Throwable th) {
            ALog.i(TAG, "checkILopTgCloudToken exception=" + th);
            th.printStackTrace();
        }
    }
}
