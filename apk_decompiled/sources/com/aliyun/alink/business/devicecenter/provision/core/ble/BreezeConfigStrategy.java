package com.aliyun.alink.business.devicecenter.provision.core.ble;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.tg.basebiz.user.UserManager;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.api.add.ProtocolVersion;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.api.add.RegionInfo;
import com.aliyun.alink.business.devicecenter.api.config.ProvisionConfigCenter;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.model.AliyunIoTRequest;
import com.aliyun.alink.business.devicecenter.biz.model.GetBindTokenMtopResponse;
import com.aliyun.alink.business.devicecenter.biz.model.GetBindTokenRequest;
import com.aliyun.alink.business.devicecenter.channel.ble.BleChannelClient;
import com.aliyun.alink.business.devicecenter.channel.ble.IBleInterface;
import com.aliyun.alink.business.devicecenter.channel.ble.TLV;
import com.aliyun.alink.business.devicecenter.channel.http.ApiRequestClient;
import com.aliyun.alink.business.devicecenter.channel.http.IRequest;
import com.aliyun.alink.business.devicecenter.channel.http.IRequestCallback;
import com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy;
import com.aliyun.alink.business.devicecenter.config.DeviceCenterBiz;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.IDeviceInfoNotifyListener;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.ble.BreezeConstants;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.config.phoneap.AlinkAESHelper;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.core.C0467a;
import com.aliyun.alink.business.devicecenter.provision.core.C0469c;
import com.aliyun.alink.business.devicecenter.provision.core.C0470d;
import com.aliyun.alink.business.devicecenter.provision.core.C0471e;
import com.aliyun.alink.business.devicecenter.provision.core.C0473g;
import com.aliyun.alink.business.devicecenter.provision.core.C0474h;
import com.aliyun.alink.business.devicecenter.provision.core.C0475i;
import com.aliyun.alink.business.devicecenter.provision.core.C0476j;
import com.aliyun.alink.business.devicecenter.provision.core.C0477k;
import com.aliyun.alink.business.devicecenter.provision.core.C0478l;
import com.aliyun.alink.business.devicecenter.provision.core.C0479m;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.ut.LinkUtHelper;
import com.aliyun.alink.business.devicecenter.ut.UtLinkInfo;
import com.aliyun.alink.business.devicecenter.utils.NetworkEnvironmentUtils;
import com.aliyun.alink.business.devicecenter.utils.PermissionCheckerUtils;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.business.devicecenter.utils.TimerUtils;
import com.aliyun.alink.business.devicecenter.utils.WiFiUtils;
import com.aliyun.iot.breeze.mix.MixBleDevice;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_BLE)
public class BreezeConfigStrategy extends BaseProvisionStrategy implements IConfigStrategy {
    public static final int MAX_BLE_RETRY_COUNT = 4;
    public static final int MSG_RETRY_CONNECT_BLE_DEVICE = 17767;
    public static final int SCAN_BLE_TIMEOUT = 10000;
    public static final int SHORT_RANDOM_LENGTH = 6;
    public static String TAG = "BreezeConfigStrategy";
    public static final int deviceErrorCodeNeedRetry = 50404;
    public BleChannelClient mBleChannelClient;
    public Context mContext;
    public final a mHandler;
    public long utStartTime;
    public String mBssid = null;
    public String comboDeviceMac = null;
    public IBleInterface.IBleChannelDevice mBleChannelDevice = null;
    public BreezeConfigState breezeConfigState = BreezeConfigState.BLE_IDLE;
    public AtomicBoolean needBreezeScan = new AtomicBoolean(false);
    public AtomicBoolean hasBleEverConnectedAB = new AtomicBoolean(false);
    public AtomicBoolean hasNotifiedScanTimeout = new AtomicBoolean(false);
    public TimerUtils scanTimeoutTimer = null;
    public int bindTokenByteLen = 16;
    public int subErrorCode = 0;
    public int devSubErrorCodeFromBleReceived = 0;
    public String devInfoFromBleReceived = null;
    public ByteBuffer devWiFiMFromFromBleReceivedByteBuffer = null;
    public AtomicBoolean hasAllocateWiFiByteBuffer = new AtomicBoolean(false);
    public String devWiFiMFromOssObjectName = null;
    public int comboDeviceProvisionState = 0;
    public String breezeResponseInfo = null;
    public final Object lockHandleDeviceNotifyLock = new Object();
    public AtomicBoolean sendAppToken2DeviceAB = new AtomicBoolean(true);
    public AtomicBoolean hasNotifiedSwitchApAck = new AtomicBoolean(true);
    public DeviceReportTokenType deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
    public ScheduledFuture getCloudTokenTask = null;
    public AtomicBoolean needRetryGetCloudTokenAB = new AtomicBoolean(false);
    public ApiRequestClient apiRequestClient = new ApiRequestClient(true);
    public HashMap<String, String> pingEnvInfo = null;
    public String deviceConnection = "0";
    public TimerUtils provisionNetInfoTimer = null;
    public String pt = null;
    public String cstep = null;
    public AtomicInteger mBleRetryConnectCount = new AtomicInteger(0);
    public IDeviceInfoNotifyListener deviceInfoNotifyListener = new C0471e(this);
    public IBleInterface.IBleScanCallback bleScanCallback = new C0473g(this);
    public IRequestCallback requestCallback = new C0474h(this);
    public IBleInterface.IBleConnectionCallback connectionCallback = new C0475i(this);
    public IBleInterface.IBleReceiverCallback bleReceiverCallback = new C0478l(this);

    /* JADX INFO: Access modifiers changed from: private */
    public static class a extends Handler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public BreezeConfigStrategy f3693a;

        public a(Looper looper, WeakReference<BreezeConfigStrategy> weakReference) {
            super(looper);
            this.f3693a = null;
            if (weakReference == null || weakReference.get() == null) {
                return;
            }
            this.f3693a = weakReference.get();
        }

        @Override // android.os.Handler
        public void handleMessage(@NonNull Message message) {
            BreezeConfigStrategy breezeConfigStrategy;
            if (message == null || message.what != 17767 || (breezeConfigStrategy = this.f3693a) == null || breezeConfigStrategy.provisionHasStopped.get()) {
                return;
            }
            ALog.d(BreezeConfigStrategy.TAG, "do retry to connect bre device " + this.f3693a.comboDeviceMac);
            this.f3693a.connectBreDevice();
        }
    }

    public BreezeConfigStrategy(Context context) {
        this.mContext = null;
        this.mBleChannelClient = null;
        this.mContext = context;
        this.mBleChannelClient = new BleChannelClient(context);
        this.mBleChannelClient.init(context);
        HandlerThread handlerThread = new HandlerThread("scene task handler thread");
        handlerThread.start();
        this.mHandler = new a(handlerThread.getLooper(), new WeakReference(this));
    }

    private boolean bleChannelWithNoEncryption() {
        return !this.mBleChannelClient.channelEncrypt(this.mBleChannelDevice);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connectBreDevice() {
        ALog.i(TAG, "connectBreDevice breeze state=connectBleDevice. " + this.mBleRetryConnectCount.get());
        this.needBreezeScan.set(false);
        updateProvisionState(BreezeConfigState.BLE_CONNECTING);
        PerformanceLog.trace(TAG, "connectBle");
        DCUserTrack.addTrackData(AlinkConstants.KEY_START_TIME_CONNECT_BLE, String.valueOf(System.currentTimeMillis()));
        if (this.provisionHasStopped.get()) {
            ALog.w(TAG, "provisionHasStopped=true, return.");
            return;
        }
        this.utStartTime = System.currentTimeMillis();
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        if (dCAlibabaConfigParams != null) {
            LinkUtHelper.connectEvent(LinkUtHelper.CONNECT_START, new UtLinkInfo(dCAlibabaConfigParams.userId, dCAlibabaConfigParams.productKey, dCAlibabaConfigParams.linkType.getName()));
        }
        this.cstep = "32";
        this.mBleChannelClient.connect(this.comboDeviceMac, this.connectionCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getBleProvisionTimeoutErrorInfo() {
        this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT);
        JSONObject extraErrorInfo = getExtraErrorInfo();
        BreezeConfigState breezeConfigState = this.breezeConfigState;
        if (breezeConfigState == BreezeConfigState.BLE_SCANNING) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_BLE_FOUND_DEV_FAILED).setMsg("found target combo device fail ").setExtra(extraErrorInfo);
            return;
        }
        if (breezeConfigState == BreezeConfigState.BLE_GET_CLOUD_TOKEN) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_BLE_COMBO_GET_CLOUD_TOKEN_FAILED).setMsg("get cloud token fail ").setExtra(extraErrorInfo);
            return;
        }
        if (breezeConfigState == BreezeConfigState.BLE_CONNECTING) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_BLE_CONNECT_DEV_FAILED).setMsg("connect target combo device failed ").setExtra(extraErrorInfo);
            return;
        }
        if (breezeConfigState == BreezeConfigState.BLE_CONNECTED) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_BLE_COMBO_CONNECTED_NO_GET_DEVICE_INFO).setMsg("target combo device connected, no get device info ").setExtra(extraErrorInfo);
            return;
        }
        if (breezeConfigState == BreezeConfigState.BLE_GET_DEVICE_INFO) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_BLE_GET_DEVICE_NAME_TIMEOUT).setMsg("combo device connected, get device name failed ").setExtra(extraErrorInfo);
            return;
        }
        if (breezeConfigState != null && breezeConfigState.ordinal() >= BreezeConfigState.BLE_SWITCH_AP.ordinal()) {
            int i = this.devSubErrorCodeFromBleReceived;
            if (i != 0) {
                if (i > 200) {
                    DCErrorCode dCErrorCode = this.provisionErrorInfo;
                    int i2 = this.subErrorCode;
                    if (i2 != 0) {
                        i = i2;
                    }
                    dCErrorCode.setSubcode(i).setMsg("provision timeout but get device sub error code! ").setExtra(extraErrorInfo);
                    return;
                }
                DCErrorCode dCErrorCode2 = this.provisionErrorInfo;
                int i3 = this.subErrorCode;
                if (i3 == 0) {
                    i3 = DCErrorCode.SUBCODE_PT_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL;
                }
                dCErrorCode2.setSubcode(i3).setMsg("provision timeout receive errorCode or no error response! ").setExtra(extraErrorInfo);
                return;
            }
            int i4 = this.subErrorCode;
            if (i4 != 0) {
                this.provisionErrorInfo.setSubcode(i4).setMsg("provision timeout but get device error code! ").setExtra(extraErrorInfo);
                return;
            }
        }
        BreezeConfigState breezeConfigState2 = this.breezeConfigState;
        if (breezeConfigState2 == BreezeConfigState.BLE_SWITCH_AP) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_DF_BLE_RESPONSE_SUCCESS_NO_CONNECT_AP).setMsg("no ack received! ").setExtra(extraErrorInfo);
        } else if (breezeConfigState2 == BreezeConfigState.BLE_SUCCESS) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_DF_BLE_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL).setMsg("no connectApNotify and checkToken failed ").setExtra(extraErrorInfo);
        }
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
    public void getCloudToken() {
        IRequest iRequest;
        this.sendAppToken2DeviceAB.set(true);
        this.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
        if (!ProvisionConfigCenter.getInstance().enableGlobalCloudToken()) {
            ALog.i(TAG, "enableGlobalCloudToken = false");
            connectBreDevice();
            return;
        }
        if (this.provisionHasStopped.get()) {
            return;
        }
        ALog.d(TAG, "getCloudToken() called");
        this.needBreezeScan.set(false);
        BreezeConfigState breezeConfigState = this.breezeConfigState;
        if (breezeConfigState != null && breezeConfigState.ordinal() >= BreezeConfigState.BLE_GET_CLOUD_TOKEN.ordinal()) {
            ALog.d(TAG, "getCloudProvisionTokenForMtop already started.");
            return;
        }
        this.needRetryGetCloudTokenAB.set(true);
        updateProvisionState(BreezeConfigState.BLE_GET_CLOUD_TOKEN);
        Class<?> cls = null;
        if (DCEnvHelper.isTgEnv()) {
            GetBindTokenRequest getBindTokenRequest = new GetBindTokenRequest();
            getBindTokenRequest.setAuthInfo(UserManager.getInstance().getAuthInfoStr());
            getBindTokenRequest.setBssid(null);
            cls = GetBindTokenMtopResponse.class;
            iRequest = getBindTokenRequest;
        } else if (!DCEnvHelper.isILopEnv()) {
            this.provisionErrorInfo = new DCErrorCode("UserFail", DCErrorCode.PF_USER_FAIL).setMsg("sth wrong with mtop & apiclient dep.").setSubcode(DCErrorCode.SUBCODE_APICLIENT_AND_MTOP_DEP_ERROR);
            provisionResultCallback(null);
            stopConfig();
            return;
        } else {
            AliyunIoTRequest aliyunIoTRequest = new AliyunIoTRequest();
            aliyunIoTRequest.setPath(AlinkConstants.HTTP_PATH_ILOP_TOKEN_REQUEST);
            aliyunIoTRequest.setApiVersion("1.0.0");
            aliyunIoTRequest.setAuthType(AlinkConstants.KEY_IOT_AUTH);
            aliyunIoTRequest.addParam("bssid", null);
            iRequest = aliyunIoTRequest;
        }
        this.apiRequestClient.send(iRequest, cls, this.requestCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getDeviceName(IBleInterface.IBleChannelDevice iBleChannelDevice) {
        ALog.d(TAG, "getDeviceName() called");
        if (this.mConfigParams == null) {
            return;
        }
        if (this.comboDeviceMac == null || needGetDeviceName(iBleChannelDevice)) {
            updateProvisionState(BreezeConfigState.BLE_GET_DEVICE_INFO);
            DCUserTrack.addTrackData(AlinkConstants.KEY_START_TIME_GET_DEVICE_INFO, String.valueOf(System.currentTimeMillis()));
            this.mBleChannelClient.getDeviceName(iBleChannelDevice, new C0476j(this));
        } else {
            ALog.i(TAG, "device do not need to get device info, deviceName=mac. subType=12");
            this.mConfigParams.deviceName = this.comboDeviceMac.replace(":", "").toLowerCase();
            DCUserTrack.addTrackData(AlinkConstants.KEY_PK, this.mConfigParams.productKey);
            DCUserTrack.addTrackData(AlinkConstants.KEY_DN, this.mConfigParams.deviceName);
            this.hasBleEverConnectedAB.set(true);
            handleBreBiz();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public JSONObject getExtraErrorInfo() {
        Object objValueOf;
        String string;
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("location", (Object) Boolean.valueOf(PermissionCheckerUtils.isLocationServiceEnable(this.mContext)));
            jSONObject.put("gps", (Object) Boolean.valueOf(PermissionCheckerUtils.isLocationPermissionAvailable(this.mContext)));
            jSONObject.put("switchapRes", (Object) this.breezeResponseInfo);
            if (this.mConfigParams != null && this.mConfigParams.regionInfo != null) {
                jSONObject.put("regionId", (Object) Integer.valueOf(this.mConfigParams.regionInfo.shortRegionId));
            }
            jSONObject.put("phi", (Object) NetworkEnvironmentUtils.getPhoneWiFiInfo(this.mContext));
            if (!TextUtils.isEmpty(this.devInfoFromBleReceived)) {
                jSONObject.put("devInfo", (Object) this.devInfoFromBleReceived);
            }
            if (!TextUtils.isEmpty(this.devWiFiMFromOssObjectName)) {
                jSONObject.put("devOssKey", (Object) this.devWiFiMFromOssObjectName);
            }
            if (!TextUtils.isEmpty(this.comboDeviceMac)) {
                jSONObject.put(AlinkConstants.KEY_MAC, (Object) this.comboDeviceMac);
            }
            jSONObject.put("devSEC", (Object) Integer.valueOf(this.devSubErrorCodeFromBleReceived));
            if (this.mConfigParams != null && !TextUtils.isEmpty(this.mConfigParams.password)) {
                try {
                    try {
                        String mDData = StringUtils.getMDData(this.mConfigParams.password, "MD5");
                        int length = this.mConfigParams.password.length();
                        if (TextUtils.isEmpty(mDData)) {
                            string = null;
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append(mDData.substring(0, 10));
                            if (length < 10) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("0");
                                sb2.append(length);
                                objValueOf = sb2.toString();
                            } else {
                                objValueOf = Integer.valueOf(length);
                            }
                            sb.append(objValueOf);
                            string = sb.toString();
                        }
                        jSONObject.put("pm5", (Object) string);
                    } catch (Exception e) {
                        String str = TAG;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("pm5 getMDData e=");
                        sb3.append(e);
                        ALog.w(str, sb3.toString());
                    }
                } catch (UnsupportedEncodingException e2) {
                    String str2 = TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("pm5 getMDData uee=");
                    sb4.append(e2);
                    ALog.w(str2, sb4.toString());
                } catch (NoSuchAlgorithmException e3) {
                    String str3 = TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("pm5 getMDData nsae=");
                    sb5.append(e3);
                    ALog.w(str3, sb5.toString());
                }
            } else if (this.mConfigParams != null) {
                jSONObject.put("pm5", (Object) "0");
            }
            if (DCEnvHelper.isTgEnv()) {
                return jSONObject;
            }
            jSONObject.put("ping", this.pingEnvInfo == null ? "" : this.pingEnvInfo);
            HashMap<String, String> bleInfo = NetworkEnvironmentUtils.getBleInfo(this.mContext, this.mBleChannelDevice);
            if (bleInfo == null) {
                bleInfo = new HashMap<>();
            }
            bleInfo.put("state", this.hasBleEverConnectedAB.get() ? "2" : TextUtils.isEmpty(this.comboDeviceMac) ? "0" : "1");
            bleInfo.put("conn", this.deviceConnection);
            bleInfo.put("pt", this.pt);
            bleInfo.put("cstep", this.cstep);
            if (this.mBleChannelDevice != null && (this.mBleChannelDevice.getChannelDevice() instanceof MixBleDevice)) {
                bleInfo.put("cstep", String.valueOf(((MixBleDevice) this.mBleChannelDevice.getChannelDevice()).getAuthState()));
            }
            jSONObject.put("ble", (Object) bleInfo);
        } catch (Exception unused) {
        }
        return jSONObject;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleBreBiz() {
        String upperCase;
        byte[] bArrHexStringTobytes;
        ALog.d(TAG, "handleBreBiz() called");
        ArrayList arrayList = new ArrayList();
        boolean zBleChannelWithNoEncryption = bleChannelWithNoEncryption();
        String hexString = StringUtils.getHexString(6);
        byte[] bArrHexStringTobytes2 = StringUtils.hexStringTobytes(hexString);
        if (!TextUtils.isEmpty(this.mConfigParams.ssid)) {
            byte[] bytes = this.mConfigParams.ssid.getBytes();
            arrayList.add(new TLV.Element((byte) 1, bytes));
            ALog.d(TAG, "handleBreBiz ssid byteArray=" + StringUtils.byteArray2String(bytes));
        }
        try {
            if (!TextUtils.isEmpty(this.mConfigParams.password)) {
                if (zBleChannelWithNoEncryption) {
                    String mDData = StringUtils.getMDData(hexString, MessageDigestAlgorithms.SHA_256);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("random = ");
                    sb.append(hexString);
                    sb.append(", A2=");
                    sb.append(mDData);
                    ALog.d(str, sb.toString());
                    String strEncrypt128CBC = AlinkAESHelper.encrypt128CBC(this.mConfigParams.password, "00000000000000000000000000000000", mDData.substring(0, 32));
                    arrayList.add(new TLV.Element((byte) 2, strEncrypt128CBC.getBytes()));
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("handleBreBiz password=");
                    sb2.append(strEncrypt128CBC);
                    sb2.append(", byteArray=");
                    sb2.append(StringUtils.byteArray2String(strEncrypt128CBC.getBytes()));
                    ALog.d(str2, sb2.toString());
                } else {
                    byte[] bytes2 = this.mConfigParams.password.getBytes();
                    arrayList.add(new TLV.Element((byte) 2, bytes2));
                    String str3 = TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("handleBreBiz password byteArray=");
                    sb3.append(StringUtils.byteArray2String(bytes2));
                    ALog.d(str3, sb3.toString());
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            ALog.w(TAG, "provision with psd info, NSAE exception=" + e);
        } catch (Exception e2) {
            e2.printStackTrace();
            ALog.w(TAG, "provision with psd info, exception=" + e2);
        }
        if (this.sendAppToken2DeviceAB.get()) {
            this.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
        } else {
            this.deviceReportTokenType = DeviceReportTokenType.UNKNOWN;
        }
        byte[] bssidHexByteArray = StringUtils.getBssidHexByteArray(this.mBssid);
        ALog.d(TAG, "handleBreBiz bssid byteArray=" + StringUtils.bytesToHexString(bssidHexByteArray));
        if (bssidHexByteArray != null && bssidHexByteArray.length == 6) {
            arrayList.add(new TLV.Element((byte) 3, bssidHexByteArray));
        }
        try {
            String hexString2 = this.sendAppToken2DeviceAB.get() ? StringUtils.getHexString(this.bindTokenByteLen * 2) : this.mConfigParams.bindToken;
            String str4 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("bleNoEncryptionVersion = ");
            sb4.append(zBleChannelWithNoEncryption);
            sb4.append(", sendAppToken2DeviceAB=");
            sb4.append(this.sendAppToken2DeviceAB);
            sb4.append(", bindToken = ");
            sb4.append(hexString2);
            ALog.i(str4, sb4.toString());
            if (!TextUtils.isEmpty(hexString2) && (bArrHexStringTobytes = StringUtils.hexStringTobytes((upperCase = hexString2.toUpperCase()))) != null && bArrHexStringTobytes.length == this.bindTokenByteLen) {
                if (this.mConfigParams != null) {
                    this.mConfigParams.bindToken = upperCase;
                }
                arrayList.add(new TLV.Element((byte) 4, bArrHexStringTobytes));
            }
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            RegionInfo regionInfo = this.mConfigParams.regionInfo;
            if (regionInfo != null) {
                if (TextUtils.isEmpty(regionInfo.mqttUrl)) {
                    String str5 = TAG;
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("ble provision with region ");
                    sb5.append(regionInfo.shortRegionId);
                    ALog.d(str5, sb5.toString());
                    if (regionInfo.shortRegionId > -1) {
                        arrayList.add(new TLV.Element((byte) 5, new byte[]{(byte) (regionInfo.shortRegionId & 255)}));
                    }
                } else {
                    String str6 = TAG;
                    StringBuilder sb6 = new StringBuilder();
                    sb6.append("ble provision with full url ");
                    sb6.append(regionInfo.mqttUrl);
                    ALog.d(str6, sb6.toString());
                    arrayList.add(new TLV.Element((byte) 6, regionInfo.mqttUrl.getBytes()));
                }
            }
        } catch (Exception e4) {
            e4.printStackTrace();
            ALog.w(TAG, "provision with no region info, exception=" + e4);
        }
        if (zBleChannelWithNoEncryption) {
            try {
                arrayList.add(new TLV.Element(BreezeConstants.BREEZE_PROVISION_VERSION, new byte[]{2}));
            } catch (Exception e5) {
                ALog.w(TAG, "provision with version info, exception=" + e5);
            }
            try {
                arrayList.add(new TLV.Element((byte) 7, bArrHexStringTobytes2));
            } catch (Exception e6) {
                ALog.w(TAG, "provision with random info, exception=" + e6);
            }
        }
        try {
            byte[] bArr = new byte[1];
            if (this.sendAppToken2DeviceAB.get()) {
                bArr[0] = 0;
            } else {
                bArr[0] = 1;
            }
            arrayList.add(new TLV.Element((byte) 8, bArr));
        } catch (Exception e7) {
            ALog.w(TAG, "provision with tokenType info, exception=" + e7);
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add(new TLV.Element((byte) 6, TLV.toPayload(arrayList)));
        byte[] payload = TLV.toPayload(arrayList2);
        ALog.i(TAG, "handleBreBiz sendHexString=" + StringUtils.getHexStringFromByteArray(payload));
        PerformanceLog.trace(TAG, AlinkConstants.KEY_BROADCAST);
        DCUserTrack.addTrackData(AlinkConstants.KEY_START_TIME_SWITCH_AP, String.valueOf(System.currentTimeMillis()));
        updateProvisionState(BreezeConfigState.BLE_SWITCH_AP);
        updateBackupCheckType(this.deviceReportTokenType, isIlop());
        startBackupCheck(true, 0L);
        this.mBleChannelClient.registerOnReceivedListener(this.mBleChannelDevice, this.bleReceiverCallback);
        ALog.d(TAG, "handleBreBiz sendMessage mBleChannelDevice=" + this.mBleChannelDevice);
        this.mBleChannelClient.sendMessage(this.mBleChannelDevice, 13, payload, new C0477k(this));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isIlop() {
        BleChannelClient bleChannelClient = this.mBleChannelClient;
        return bleChannelClient == null || "ilop".equals(bleChannelClient.getType());
    }

    private boolean needGetDeviceName(IBleInterface.IBleChannelDevice iBleChannelDevice) {
        if (!isIlop()) {
            return false;
        }
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        if (dCAlibabaConfigParams == null || TextUtils.isEmpty(dCAlibabaConfigParams.devType) || !AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_12.equals(this.mConfigParams.devType)) {
            return this.mBleChannelClient.needGetDeviceName(iBleChannelDevice);
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyReceiveSwitchApAck() {
        ALog.d(TAG, "notifyReceiveSwitchApAck() called");
        if (this.provisionHasStopped.get()) {
            ALog.w(TAG, "notifyReceiveSwitchApAck provisionHasStopped=true, return.");
        } else if (this.hasNotifiedSwitchApAck.compareAndSet(false, true)) {
            provisionStatusCallback(ProvisionStatus.BLE_DEVICE_RECEIVE_SWITCHAP_ACK);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void provisionFailFromBleNotify(int i, String str) {
        try {
            if (this.provisionHasStopped.get()) {
                return;
            }
            if (this.devWiFiMFromFromBleReceivedByteBuffer == null) {
                this.provisionErrorInfo = new DCErrorCode("ProvisionFailFromDevice", DCErrorCode.PF_PROVISION_FAIL_FROM_DEVICE).setSubcode(i).setMsg(str).setExtra(getExtraErrorInfo());
                provisionResultCallback(null);
                stopConfig();
                return;
            }
            String str2 = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("valid byte position=");
            sb.append(this.devWiFiMFromFromBleReceivedByteBuffer.position());
            ALog.d(str2, sb.toString());
            byte[] bArr = new byte[this.devWiFiMFromFromBleReceivedByteBuffer.position()];
            for (int i2 = 0; i2 < bArr.length; i2++) {
                bArr[i2] = this.devWiFiMFromFromBleReceivedByteBuffer.get(i2);
            }
            String str3 = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("wifi data = ");
            sb2.append(StringUtils.bytesToHexString(bArr));
            ALog.d(str3, sb2.toString());
            AlinkHelper.uploadData2Oss(bArr, new C0479m(this, i, str));
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(TAG, "onMessage close breeze connection exception=" + e);
        }
    }

    private void setGetNetworkInfoTimeout() {
        int i = this.mConfigParams.timeout - 10;
        if (i > 12) {
            this.provisionNetInfoTimer = new TimerUtils(i * 1000);
            this.provisionNetInfoTimer.setCallback(new C0469c(this));
            this.provisionNetInfoTimer.start(TimerUtils.MSG_GET_NETWORK_ENV_TIMEOUT);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopScanNotifyTimer() {
        TimerUtils timerUtils = this.scanTimeoutTimer;
        if (timerUtils != null) {
            timerUtils.stop(TimerUtils.MSG_SCAN_BLE_TIMEOUT);
            this.scanTimeoutTimer = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateProvisionState(BreezeConfigState breezeConfigState) {
        ALog.d(TAG, "updateProvisionState() called with: state = [" + breezeConfigState + "]");
        this.breezeConfigState = breezeConfigState;
        if (breezeConfigState == BreezeConfigState.BLE_GET_CLOUD_TOKEN || breezeConfigState == BreezeConfigState.BLE_SWITCH_AP) {
            ProvisionStatus provisionStatus = breezeConfigState == BreezeConfigState.BLE_GET_CLOUD_TOKEN ? ProvisionStatus.BLE_DEVICE_CONNECTING : ProvisionStatus.BLE_DEVICE_CONNECTED;
            provisionStatus.addExtraParams(AlinkConstants.KEY_DEV_TYPE, this.mConfigParams.devType);
            provisionStatus.addExtraParams(AlinkConstants.KEY_BLE_MAC, this.comboDeviceMac);
            provisionStatus.addExtraParams(AlinkConstants.KEY_PRODUCT_ID, this.mConfigParams.productId);
            provisionStatusCallback(provisionStatus);
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void continueConfig(Map map) {
        ALog.d(TAG, "continueConfig() called with: provisionParams = [" + map + "]");
        if (this.provisionHasStopped.get() || this.mConfigParams == null) {
            ALog.w(TAG, "provisionHasStopped=true return.");
            return;
        }
        if (TextUtils.isEmpty(this.comboDeviceMac)) {
            ALog.w(TAG, "continueConfig called in illegalState, device has not been found.");
        } else if (AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_3.equals(this.mConfigParams.devType)) {
            getCloudToken();
        } else {
            ALog.w(TAG, "only subType=3 support continueProvision interface.");
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void doExtraPrepareWork(IConfigExtraCallback iConfigExtraCallback) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_BLE.getName();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean hasExtraPrepareWork() {
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean isSupport() {
        return Build.VERSION.SDK_INT >= 18;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean needWiFiSsidPwd() {
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void preConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy
    public void provisionResultCallback(DeviceInfo deviceInfo) {
        try {
            stopBleProvisionTimer();
        } catch (Exception unused) {
        }
        DCErrorCode dCErrorCode = this.provisionErrorInfo;
        if (dCErrorCode != null) {
            dCErrorCode.setExtra(getExtraErrorInfo());
        }
        super.provisionResultCallback(deviceInfo);
    }

    public void startBleProvisionTimer() {
        ALog.d(TAG, "startBleProvisionTimer() called");
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        if (dCAlibabaConfigParams == null) {
            return;
        }
        this.provisionTimer = new TimerUtils(dCAlibabaConfigParams.timeout * 1000);
        this.provisionTimer.setCallback(new C0467a(this));
        this.provisionTimer.start(TimerUtils.MSG_PROVISION_TIMEOUT);
        setGetNetworkInfoTimeout();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void startConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) throws Exception {
        this.mConfigCallback = iConfigCallback;
        if (!(dCConfigParams instanceof DCAlibabaConfigParams)) {
            ALog.e(TAG, "startConfig params error.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("configParams error:").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        if (!this.mBleChannelClient.isSupport()) {
            this.provisionErrorInfo = new DCErrorCode("UserError", DCErrorCode.PF_USER_INVOKE_ERROR).setMsg(this.mBleChannelClient.getType() + " need ble dependency").setSubcode(DCErrorCode.SUBCODE_UIE_NEED_BREEZE_BIZ_DEPENDENCY);
            provisionResultCallback(null);
            return;
        }
        this.mConfigParams = (DCAlibabaConfigParams) dCConfigParams;
        this.mBleChannelClient.setConfigParams(this.mConfigParams);
        ALog.d(TAG, "bleStartConfig params:" + this.mConfigParams.toString());
        this.sendAppToken2DeviceAB.set(true);
        this.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
        if (!ProtocolVersion.NO_PRODUCT.getVersion().equals(this.mConfigParams.protocolVersion) && TextUtils.isEmpty(this.mConfigParams.productId) && TextUtils.isEmpty(this.mConfigParams.mac)) {
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("productId and mac cannot be both empty, when protocolVersion!=2.0").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        if (!TextUtils.isEmpty(this.mConfigParams.mac)) {
            this.mConfigParams.id = null;
        }
        updateProvisionState(BreezeConfigState.BLE_IDLE);
        if (!PermissionCheckerUtils.isLocationPermissionAvailable(DeviceCenterBiz.getInstance().getAppContext())) {
            ALog.w(TAG, "startConfig location permission not granted.");
            ProvisionStatus provisionStatus = ProvisionStatus.BLE_NEED_LOCATION_PERMISSION;
            provisionStatus.setMessage("need location permission, otherwise provision may fail.");
            provisionStatusCallback(provisionStatus);
        }
        if (!PermissionCheckerUtils.isLocationServiceEnable(DeviceCenterBiz.getInstance().getAppContext())) {
            ALog.w(TAG, "startConfig location service disabled.");
            ProvisionStatus provisionStatus2 = ProvisionStatus.BLE_NEED_LOCATION_SERVICE_ENABLED;
            provisionStatus2.setMessage("set location service to be enabled, otherwise provision may fail.");
            provisionStatusCallback(provisionStatus2);
        }
        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
        startBleProvisionTimer();
        this.hasBleEverConnectedAB.set(false);
        this.provisionHasStopped.set(false);
        this.mBssid = WiFiUtils.getCurrentApBssid();
        this.needBreezeScan.set(true);
        updateProvisionState(BreezeConfigState.BLE_SCANNING);
        stopScanNotifyTimer();
        if (TextUtils.isEmpty(this.mConfigParams.mac)) {
            if (TextUtils.isEmpty(this.mConfigParams.productKey)) {
                this.pt = "0";
            } else {
                this.pt = "1";
            }
        } else if (TextUtils.isEmpty(this.mConfigParams.productKey)) {
            this.pt = "2";
        } else {
            this.pt = "3";
        }
        if (!TextUtils.isEmpty(this.mConfigParams.mac) && !AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_3.equals(this.mConfigParams.devType)) {
            ALog.i(TAG, "provision combo device ignore to scan, to connect.");
            ProvisionStatus provisionStatus3 = ProvisionStatus.BLE_DEVICE_SCAN_SUCCESS;
            this.comboDeviceMac = this.mConfigParams.mac;
            provisionStatus3.setMessage("scan target ble device success from user.");
            provisionStatus3.addExtraParams(AlinkConstants.KEY_DEV_TYPE, this.mConfigParams.devType);
            provisionStatus3.addExtraParams(AlinkConstants.KEY_BLE_MAC, this.comboDeviceMac);
            provisionStatus3.addExtraParams(AlinkConstants.KEY_PRODUCT_ID, this.mConfigParams.productId);
            provisionStatusCallback(provisionStatus3);
            getCloudToken();
        } else if (!AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_3.equals(this.mConfigParams.devType) || TextUtils.isEmpty(this.mConfigParams.mac)) {
            ALog.i(TAG, "provision combo device need to scan first.");
            this.scanTimeoutTimer = new TimerUtils(10000);
            this.scanTimeoutTimer.setCallback(new C0470d(this));
            this.scanTimeoutTimer.start(TimerUtils.MSG_SCAN_BLE_TIMEOUT);
            PerformanceLog.trace(TAG, "scanBle");
            DCUserTrack.addTrackData(AlinkConstants.KEY_START_TIME_SCAN, String.valueOf(System.currentTimeMillis()));
            ALog.i(TAG, "startConfig breeze state=startLeScan.");
            this.mBleChannelClient.startScan(this.bleScanCallback);
        } else {
            if (!AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_3.equals(this.mConfigParams.devType) || TextUtils.isEmpty(this.mConfigParams.mac)) {
                ALog.e(TAG, "startConfig params error, invalid devType.");
                this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("device type invalid").setSubcode(DCErrorCode.SUBCODE_PE_DEVICETYPE_ERROR);
                provisionResultCallback(null);
                return;
            }
            ALog.i(TAG, "provision combo device ignore to scan, wait for continueProvision.");
            ProvisionStatus provisionStatus4 = ProvisionStatus.BLE_DEVICE_SCAN_SUCCESS;
            this.comboDeviceMac = this.mConfigParams.mac;
            provisionStatus4.setMessage("scan target ble device success from user.");
            provisionStatus4.addExtraParams(AlinkConstants.KEY_DEV_TYPE, this.mConfigParams.devType);
            provisionStatus4.addExtraParams(AlinkConstants.KEY_BLE_MAC, this.comboDeviceMac);
            provisionStatus4.addExtraParams(AlinkConstants.KEY_PRODUCT_ID, this.mConfigParams.productId);
            provisionStatusCallback(provisionStatus4);
        }
        DCUserTrack.addTrackData(AlinkConstants.KEY_PK, this.mConfigParams.productKey);
        addProvisionOverListener(this.deviceInfoNotifyListener, isIlop());
    }

    public void stopBleProvisionTimer() {
        ALog.d(TAG, "stopBleProvisionTimer() called");
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

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void stopConfig() {
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        boolean z = dCAlibabaConfigParams != null && AlinkConstants.DEVICE_TYPE_COMBO_SUBTYPE_3.equals(dCAlibabaConfigParams.devType);
        this.mConfigParams = null;
        cancelRequest(this.retryTransitoryClient);
        removeProvisionOverListener();
        this.hasBleEverConnectedAB.set(false);
        this.provisionHasStopped.set(true);
        a aVar = this.mHandler;
        if (aVar != null) {
            aVar.removeCallbacksAndMessages(true);
        }
        stopScanNotifyTimer();
        ApiRequestClient apiRequestClient = this.apiRequestClient;
        if (apiRequestClient != null) {
            apiRequestClient.cancelRequest();
        }
        this.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
        try {
            if (this.mBleChannelDevice != null) {
                this.mBleChannelClient.unregisterOnReceivedListener(this.mBleChannelDevice, this.bleReceiverCallback);
            }
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("stopConfig breeze close connection when connect called. breezeConfigState=");
            sb.append(this.breezeConfigState);
            sb.append(", needDisconnect=");
            sb.append(z);
            sb.append(", comboDeviceMac=");
            sb.append(this.comboDeviceMac);
            ALog.i(str, sb.toString());
            if (!TextUtils.isEmpty(this.comboDeviceMac) && this.breezeConfigState.ordinal() > BreezeConfigState.BLE_SCANNING.ordinal() && !z) {
                this.mBleChannelClient.disconnect(this.comboDeviceMac, this.connectionCallback);
            }
            ALog.i(TAG, "stopConfig breeze stopLeScan.");
            this.mBleChannelClient.stopScan(this.bleScanCallback);
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(TAG, "stopConfig exception=" + e);
        }
        this.comboDeviceMac = null;
        this.needBreezeScan.set(false);
        this.provisionErrorInfo = null;
        stopBleProvisionTimer();
        this.mConfigCallback = null;
        this.sendAppToken2DeviceAB.set(false);
        this.hasNotifiedSwitchApAck.set(false);
        updateProvisionState(BreezeConfigState.BLE_IDLE);
        stopBackupCheck();
        ByteBuffer byteBuffer = this.devWiFiMFromFromBleReceivedByteBuffer;
        if (byteBuffer != null) {
            byteBuffer.clear();
            this.devWiFiMFromFromBleReceivedByteBuffer = null;
        }
    }
}
