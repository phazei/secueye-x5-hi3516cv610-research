package com.aliyun.alink.business.devicecenter.provision.other.softap;

import android.content.Context;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Pair;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.api.add.DeviceServiceType;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.api.add.ProtocolVersion;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.api.config.ProvisionConfigCenter;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.biz.ProvisionRepository;
import com.aliyun.alink.business.devicecenter.cache.WiFiScanResultsCache;
import com.aliyun.alink.business.devicecenter.channel.coap.CoAPClient;
import com.aliyun.alink.business.devicecenter.channel.coap.request.CoapRequestPayload;
import com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DeviceReportTokenType;
import com.aliyun.alink.business.devicecenter.diagnose.SoftApDiagnose;
import com.aliyun.alink.business.devicecenter.discover.scanap.SoftAPDiscoverChain;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.other.b;
import com.aliyun.alink.business.devicecenter.provision.other.c;
import com.aliyun.alink.business.devicecenter.provision.other.d;
import com.aliyun.alink.business.devicecenter.provision.other.f;
import com.aliyun.alink.business.devicecenter.provision.other.g;
import com.aliyun.alink.business.devicecenter.provision.other.i;
import com.aliyun.alink.business.devicecenter.provision.other.j;
import com.aliyun.alink.business.devicecenter.provision.other.k;
import com.aliyun.alink.business.devicecenter.provision.other.l;
import com.aliyun.alink.business.devicecenter.provision.other.m;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.NetworkTypeUtils;
import com.aliyun.alink.business.devicecenter.utils.PermissionCheckerUtils;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.alink.business.devicecenter.utils.WiFiConnectiveUtils;
import com.aliyun.alink.business.devicecenter.utils.WiFiUtils;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPRequest;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_SOFT_AP)
public class SoftAPConfigStrategy extends BaseProvisionStrategy implements IConfigStrategy {
    public static String TAG = "SoftAPConfigStrategy";
    public String configWifiBssid;
    public int configWifiNetworkId;
    public long deviceInfoCoapMessageId;
    public DeviceReportTokenType deviceReportTokenType;
    public long deviceStatusMessageId;
    public AlcsCoAPRequest deviceStatusRequest;
    public SoftAPDiscoverChain discoverChain;
    public Future<?> discoveryFuture;
    public String encryptRandom;
    public AtomicBoolean foundDeviceAp;
    public AlcsCoAPRequest getDeviceInfoRequest;
    public AtomicBoolean hasAddConnectAPListener;
    public AtomicBoolean hasCallbackService;
    public AtomicBoolean hasCallbackToApp;
    public AtomicBoolean hasConnectedDevAp;
    public AtomicBoolean hasNotifiedUser2ConnectDevAp;
    public AtomicBoolean hasNotifiedUser2RecoverWiFi;
    public AtomicBoolean ignoreConnectDevAP;
    public AtomicBoolean isRecoveringWiFi;
    public AtomicBoolean isSendingConnectInfo;
    public AtomicBoolean isWiFiRecovered;
    public Context mContext;
    public NetworkInfo.State mCurrentWiFiState;
    public String mDeviceApBssid;
    public String mDeviceApSsid;
    public String mLastConnectedWiFiBSSID;
    public NetworkInfo.State mLastWiFiState;
    public String mRandom;
    public WiFiConnectiveUtils mWiFiConnectiveUtils;
    public WiFiConnectiveUtils.IWiFiConnectivityCallback mWiFiStateListener;
    public AtomicBoolean needDisconnectFirst;
    public AtomicBoolean needReconnectSoftApAB;
    public AtomicBoolean recvSwitchAPAck;
    public AtomicLong recvSwitchAPAckTime;
    public String securityAesKey;
    public AtomicBoolean sendAppToken2DeviceAB;
    public SoftApState softApState;
    public Future switchWiFiTask;
    public ScanResult toConnectAPResult;
    public AtomicBoolean triedToConnectDeviceAp;
    public AtomicBoolean userSetIgnoreRecoverWiFi;
    public WifiManager wifiManager;

    public SoftAPConfigStrategy() {
        this.mContext = null;
        this.wifiManager = null;
        this.mWiFiConnectiveUtils = null;
        this.getDeviceInfoRequest = null;
        this.discoveryFuture = null;
        this.deviceInfoCoapMessageId = -1L;
        this.securityAesKey = null;
        this.encryptRandom = "";
        this.isSendingConnectInfo = new AtomicBoolean(false);
        this.isRecoveringWiFi = new AtomicBoolean(false);
        this.isWiFiRecovered = new AtomicBoolean(false);
        this.triedToConnectDeviceAp = new AtomicBoolean(false);
        this.hasAddConnectAPListener = new AtomicBoolean(false);
        this.recvSwitchAPAck = new AtomicBoolean(false);
        this.recvSwitchAPAckTime = new AtomicLong(0L);
        this.needReconnectSoftApAB = new AtomicBoolean(true);
        this.hasCallbackToApp = new AtomicBoolean(false);
        this.ignoreConnectDevAP = new AtomicBoolean(false);
        this.configWifiBssid = null;
        this.configWifiNetworkId = -1;
        this.toConnectAPResult = null;
        this.mDeviceApSsid = null;
        this.discoverChain = null;
        this.foundDeviceAp = new AtomicBoolean(false);
        this.hasNotifiedUser2ConnectDevAp = new AtomicBoolean(false);
        this.hasConnectedDevAp = new AtomicBoolean(false);
        this.mRandom = null;
        this.switchWiFiTask = null;
        this.mCurrentWiFiState = null;
        this.mLastWiFiState = null;
        this.mLastConnectedWiFiBSSID = null;
        this.deviceStatusMessageId = -1L;
        this.userSetIgnoreRecoverWiFi = new AtomicBoolean(false);
        this.hasCallbackService = new AtomicBoolean(false);
        this.needDisconnectFirst = new AtomicBoolean(false);
        this.mDeviceApBssid = null;
        this.hasNotifiedUser2RecoverWiFi = new AtomicBoolean(false);
        this.sendAppToken2DeviceAB = new AtomicBoolean(true);
        this.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
        this.softApState = SoftApState.INITIAL;
        this.mWiFiStateListener = new k(this);
    }

    private void addListener() {
        WiFiConnectiveUtils wiFiConnectiveUtils = this.mWiFiConnectiveUtils;
        if (wiFiConnectiveUtils != null) {
            wiFiConnectiveUtils.startListener(this.mWiFiStateListener);
        }
    }

    private void addPrvisionOverListener(IConfigCallback iConfigCallback) {
        ALog.d(TAG, "addPrvisionOverListener");
        if (this.hasAddConnectAPListener.compareAndSet(false, true)) {
            addProvisionOverListener(new j(this));
            if (this.sendAppToken2DeviceAB.get()) {
                this.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
                updateBackupCheckType(this.deviceReportTokenType);
            } else {
                this.deviceReportTokenType = DeviceReportTokenType.UNKNOWN;
                updateBackupCheckType(this.deviceReportTokenType);
            }
            startBackupCheck(true, 0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean android10plus() {
        return Build.VERSION.SDK_INT > 28;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelTask() {
        try {
            if (this.discoveryFuture != null && !this.discoveryFuture.isCancelled() && !this.discoveryFuture.isDone()) {
                this.discoveryFuture.cancel(true);
            }
            this.discoveryFuture = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int connectDeviceAp(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            ALog.w(TAG, "toScanResult ssid is null. return.");
            return -1;
        }
        this.foundDeviceAp.set(true);
        this.triedToConnectDeviceAp.set(true);
        this.isSendingConnectInfo.set(false);
        if (this.needDisconnectFirst.get()) {
            WiFiUtils.disconnectCurrentWifi(this.wifiManager);
        }
        ALog.d(TAG, "connectDeviceAp called ssid=" + str + ", bssid=" + str2 + ", capabilities=" + str3);
        DCUserTrack.addTrackData(AlinkConstants.KEY_START_TIME_CONNECT_DEV_AP, String.valueOf(System.currentTimeMillis()));
        return WiFiUtils.connect(this.mContext, str, AlinkConstants.SOFT_AP_DEFAULT_PASSWORD, str2, str3, -1);
    }

    private void getCipher(IConfigCallback iConfigCallback) {
        updateSoftApState(SoftApState.GET_CIPHER);
        if (!TextUtils.isEmpty(this.mConfigParams.password) && TextUtils.isEmpty(this.mConfigParams.productEncryptKey)) {
            this.securityAesKey = null;
            ThreadPool.submit(new f(this));
        } else {
            ALog.d(TAG, "no product version.");
            this.securityAesKey = this.mConfigParams.productEncryptKey;
            getCloudToken();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getCloudToken() {
        this.sendAppToken2DeviceAB.set(true);
        this.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
        if (ProvisionConfigCenter.getInstance().enableGlobalCloudToken()) {
            ProvisionRepository.getCloudProvisionToken(null, new g(this));
        } else {
            ALog.i(TAG, "enableGlobalCloudToken = false");
            provisioning(this.mConfigCallback);
        }
    }

    private void getDeviceStatus() {
        ALog.d(TAG, "getDeviceStatus() called");
        cancelRequest(this.deviceStatusRequest, this.deviceStatusMessageId);
        try {
            CoapRequestPayload coapRequestPayload = new CoapRequestPayload();
            coapRequestPayload.getClass();
            CoapRequestPayload coapRequestPayloadBuild = new CoapRequestPayload.Builder().version("1.0").params(new HashMap()).method(AlinkConstants.COAP_METHOD_AWSS_GET_DEVICE_INFO).build();
            this.deviceStatusRequest = new AlcsCoAPRequest(AlcsCoAPConstant.Code.GET, AlcsCoAPConstant.Type.NON);
            String broadcastIp = getBroadcastIp();
            StringBuilder sb = new StringBuilder();
            sb.append(broadcastIp);
            sb.append(":");
            sb.append(5683);
            sb.append(AlinkConstants.COAP_PATH_AWSS_GET_DEVICE_INFO);
            String string = sb.toString();
            this.deviceStatusRequest.setPayload(coapRequestPayloadBuild.toString());
            String str = TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("setPayload=");
            sb2.append(coapRequestPayloadBuild.toString());
            sb2.append(",getPayload=");
            sb2.append(this.deviceStatusRequest.getPayloadString());
            ALog.llog((byte) 3, str, sb2.toString());
            this.deviceStatusRequest.setMulticast(1);
            this.deviceStatusRequest.setURI(string);
            String str2 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("coapUri=");
            sb3.append(string);
            ALog.d(str2, sb3.toString());
        } catch (Exception e) {
            ALog.w(TAG, "pre getDeviceStatus params exception=" + e);
        }
        this.deviceStatusMessageId = CoAPClient.getInstance().sendRequest(this.deviceStatusRequest, new b(this));
    }

    private Pair<String, String> getPkAndMac(String str) {
        String[] strArrSplit = str.split(OpenAccountUIConstants.UNDER_LINE);
        if (strArrSplit.length != 3 || TextUtils.isEmpty(strArrSplit[0]) || TextUtils.isEmpty(strArrSplit[1]) || TextUtils.isEmpty(strArrSplit[2])) {
            return null;
        }
        return new Pair<>(strArrSplit[1], strArrSplit[2]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getSSIDAndStartConnectDevAp() {
        ALog.d(TAG, "getSSIDAndStartConnectDevAp() called");
        if (TextUtils.isEmpty(this.mConfigParams.deviceApSsid)) {
            WiFiScanResultsCache wiFiScanResultsCache = WiFiScanResultsCache.getInstance();
            DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
            this.toConnectAPResult = wiFiScanResultsCache.getCache(dCAlibabaConfigParams.productKey, dCAlibabaConfigParams.id);
            if (this.toConnectAPResult == null) {
                this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR).setMsg("SAP device id invalid.");
                provisionResultCallback(null);
                return;
            }
            if (TextUtils.isEmpty(this.mConfigParams.productKey) && !TextUtils.isEmpty(this.toConnectAPResult.SSID)) {
                this.mConfigParams.productKey = AlinkHelper.getPkFromAp(this.toConnectAPResult.SSID);
                ALog.d(TAG, "update pk with ssid=" + this.mConfigParams.productKey);
            }
            ScanResult scanResult = this.toConnectAPResult;
            this.mDeviceApSsid = scanResult.SSID;
            this.mDeviceApBssid = scanResult.BSSID;
        } else {
            if (TextUtils.isEmpty(this.mConfigParams.productKey)) {
                DCAlibabaConfigParams dCAlibabaConfigParams2 = this.mConfigParams;
                dCAlibabaConfigParams2.productKey = AlinkHelper.getPkFromAp(dCAlibabaConfigParams2.deviceApSsid);
                ALog.d(TAG, "update pk with ssid=" + this.mConfigParams.productKey);
            }
            this.mDeviceApSsid = this.mConfigParams.deviceApSsid;
            this.mDeviceApBssid = null;
        }
        ALog.d(TAG, "to connect " + this.mDeviceApSsid);
        PerformanceLog.trace(TAG, "connectDevAp");
        if (-1 == connectDeviceAp(this.mDeviceApSsid, this.mDeviceApBssid, null)) {
            ALog.w(TAG, "connect failed.");
            PerformanceLog.trace(TAG, "connectDevApResult", PerformanceLog.getJsonObject("result", "fail"));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getSofApProvisionTimeoutErrorInfo() {
        this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT);
        StringBuilder sb = new StringBuilder();
        sb.append("[mobile=");
        sb.append(NetworkTypeUtils.isMobileNetwork(this.mContext));
        sb.append(", WiFi=");
        sb.append(NetworkTypeUtils.isWiFi(this.mContext));
        sb.append(", location= ");
        sb.append(PermissionCheckerUtils.isLocationServiceEnable(this.mContext));
        sb.append(", gps=");
        sb.append(PermissionCheckerUtils.isLocationPermissionAvailable(this.mContext));
        sb.append(", findAp=");
        sb.append((TextUtils.isEmpty(this.mDeviceApSsid) || TextUtils.isEmpty(this.mDeviceApBssid)) ? false : true);
        sb.append(", switchApAck=");
        sb.append(this.recvSwitchAPAck.get());
        sb.append("].");
        String string = sb.toString();
        if (NetworkTypeUtils.isMobileNetwork(this.mContext) && this.softApState == SoftApState.SWITCH_AP) {
            updateSoftApState(SoftApState.NETWORK_RECONNECTED);
        }
        SoftApState softApState = this.softApState;
        if (softApState == SoftApState.GET_CIPHER) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_GET_CIPHER_TIMEOUT).setMsg("get cipher timeout " + string);
            return;
        }
        if (softApState == SoftApState.CONNECT_DEV_AP) {
            if (TextUtils.isEmpty(this.mDeviceApSsid) || TextUtils.isEmpty(this.mDeviceApBssid)) {
                this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_SAP_NO_SOFTAP).setMsg("find device ap failed " + string);
                return;
            }
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_SAP_CONNECT_DEV_AP_FAILED).setMsg("connect device ap failed " + string);
            return;
        }
        if (softApState == SoftApState.CONNECTED_DEV_AP) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_SAP_DEVICE_AP_CONNECTED_NO_SWITCH_AP).setMsg("device ap connected and no switchAp send " + string);
            return;
        }
        if (softApState == SoftApState.SWITCH_AP) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_SAP_SWITCHAP_AND_NETWORK_UNAVAILABLE).setMsg("send switchAp and network unavailable " + string);
            return;
        }
        if (softApState == SoftApState.NETWORK_RECONNECTED) {
            this.provisionErrorInfo.setSubcode(DCErrorCode.SUBCODE_PT_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL).setMsg("no connectApNotify and checkToken failed " + string);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void handleWiFiStateChange(NetworkInfo networkInfo) {
        String str;
        NetworkInfo.State state = networkInfo.getState();
        this.mLastWiFiState = this.mCurrentWiFiState;
        this.mCurrentWiFiState = state;
        if (state != NetworkInfo.State.CONNECTED) {
            if (state == NetworkInfo.State.DISCONNECTED) {
                if (!PermissionCheckerUtils.hasLocationAccess(this.mContext) && ProvisionConfigCenter.getInstance().ignoreLocationPermissionCheck()) {
                    ALog.d(TAG, "no location permission, some wifi disconnected, do nothing.");
                    return;
                }
                SoftApState softApState = this.softApState;
                if (softApState == null || softApState.ordinal() < SoftApState.CONNECTED_DEV_AP.ordinal()) {
                    return;
                }
                ALog.d(TAG, "ignore connect device ap. has ever connected device ap.");
                this.recvSwitchAPAckTime.set(System.currentTimeMillis());
                this.ignoreConnectDevAP.set(true);
                SoftApDiagnose.getInstance().stopDiagnose();
                return;
            }
            return;
        }
        ALog.d(TAG, "mLastWiFiState=" + this.mLastWiFiState + ", mCurrentWiFiState=" + this.mCurrentWiFiState + ", lastB=" + this.mLastConnectedWiFiBSSID + ", cB=" + WiFiUtils.getCurrentApBssid());
        if (this.mLastWiFiState == this.mCurrentWiFiState && (str = this.mLastConnectedWiFiBSSID) != null && str.equals(WiFiUtils.getCurrentApBssid())) {
            return;
        }
        this.mLastConnectedWiFiBSSID = WiFiUtils.getCurrentApBssid();
        onWiFiConnected();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isConnectedWiFiValid() {
        String wifiSsid = AlinkHelper.getWifiSsid(this.mContext);
        return (TextUtils.isEmpty(wifiSsid) || AlinkHelper.isValidSoftAp(wifiSsid, false)) ? false : true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean isDeviceApConnected(String str) {
        if (TextUtils.isEmpty(str) || this.mConfigParams == null || !AlinkHelper.isValidSoftAp(str.substring(1), false)) {
            return false;
        }
        String strSubstring = str.substring(1, str.length() - 1);
        Pair<String, String> pkAndMac = getPkAndMac(strSubstring);
        if (pkAndMac == null || TextUtils.isEmpty((CharSequence) pkAndMac.first) || TextUtils.isEmpty((CharSequence) pkAndMac.second)) {
            if (!TextUtils.isEmpty(strSubstring) && strSubstring.startsWith(AlinkConstants.SOFT_AP_SSID_PREFIX)) {
                ALog.w(TAG, "adh_ softap with no pk&mac is not allowed.");
                return false;
            }
            ALog.i(TAG, "user define softap with no pk&mac is allowed.");
        }
        if (ProtocolVersion.NO_PRODUCT.getVersion().equals(this.mConfigParams.protocolVersion)) {
            if (TextUtils.isEmpty(this.mConfigParams.deviceApSsid)) {
                this.mDeviceApSsid = strSubstring;
                this.mDeviceApBssid = WiFiUtils.getCurrentApBssid();
                if (pkAndMac == null) {
                    ALog.i(TAG, "device ap only with contains ap prefix.");
                }
                return true;
            }
            if (!strSubstring.equals(this.mConfigParams.deviceApSsid)) {
                ALog.d(TAG, "connected device ap not equal to user set device ap.");
                return false;
            }
            this.mDeviceApSsid = strSubstring;
            this.mDeviceApBssid = WiFiUtils.getCurrentApBssid();
            if (pkAndMac == null) {
                ALog.i(TAG, "device ap only with contains ap prefix.");
            }
            return true;
        }
        if (TextUtils.isEmpty(this.mConfigParams.deviceApSsid)) {
            if (!TextUtils.isEmpty(this.mConfigParams.id)) {
                return pkAndMac != null && this.mConfigParams.productKey.equals(pkAndMac.first) && this.mConfigParams.id.equals(pkAndMac.second);
            }
            if (pkAndMac != null || strSubstring.startsWith(AlinkConstants.SOFT_AP_SSID_PREFIX)) {
                return (pkAndMac == null || TextUtils.isEmpty(this.mConfigParams.productKey) || !this.mConfigParams.productKey.equals(pkAndMac.first)) ? false : true;
            }
            return true;
        }
        if (!strSubstring.equals(this.mConfigParams.deviceApSsid)) {
            return false;
        }
        this.mDeviceApSsid = strSubstring;
        this.mDeviceApBssid = WiFiUtils.getCurrentApBssid();
        if (pkAndMac == null) {
            ALog.i(TAG, "device ap only with contains ap prefix.");
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needReconnectSoftAp() {
        SoftApState softApState;
        return !this.recvSwitchAPAck.get() && this.needReconnectSoftApAB.get() && (softApState = this.softApState) != null && softApState.ordinal() < SoftApState.CONNECTED_DEV_AP.ordinal();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean needRecoverWifi() {
        if (this.recvSwitchAPAck.get()) {
            return true;
        }
        return this.ignoreConnectDevAP.get();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyConnectApByUser(String str, String str2) {
        boolean z;
        ALog.d(TAG, "notifyConnectApByUser() called");
        if (this.provisionHasStopped.get() || this.mConfigParams == null) {
            ALog.d(TAG, "provision stopped, ignore notifyConnectApByUser.");
            return;
        }
        if (this.hasNotifiedUser2ConnectDevAp.get()) {
            ALog.d(TAG, "notifyConnectApByUser has notified.");
            return;
        }
        ProvisionStatus provisionStatus = ProvisionStatus.SAP_NEED_USER_TO_CONNECT_DEVICE_AP;
        if (android10plus()) {
            ALog.d(TAG, "android 10+, connect device ap by user.");
            provisionStatus.setMessage("android 10+, connect device ap by user.");
            z = true;
        } else if (!this.foundDeviceAp.get()) {
            ALog.d(TAG, "scan device ap failed, connect device ap by user.");
            provisionStatus.setMessage("scan device ap failed, connect device ap by user.");
            z = true;
        } else if (this.hasConnectedDevAp.get()) {
            z = false;
            ALog.d(TAG, "do nothing.");
        } else {
            ALog.d(TAG, "connect device ap failed, connect device ap by user.");
            provisionStatus.setMessage("connect device ap failed, connect device ap by user.");
            z = true;
        }
        if (z) {
            if (this.mConfigParams != null) {
                ALog.d(TAG, "notify user to connect ap, set id = null.");
                DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
                dCAlibabaConfigParams.id = null;
                dCAlibabaConfigParams.deviceApSsid = null;
            }
            if (ProtocolVersion.NO_PRODUCT.getVersion().equals(this.mConfigParams.protocolVersion)) {
                ALog.d(TAG, "no productKey version, set productKey = null.");
                this.mConfigParams.productKey = null;
            }
            this.hasNotifiedUser2ConnectDevAp.set(true);
            if (!TextUtils.isEmpty(str)) {
                provisionStatus.addExtraParams("productKey", str);
            }
            provisionStatusCallback(provisionStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifySupportProvisionService(DeviceInfo deviceInfo) {
        ALog.d(TAG, "notifySupportProvisionService() called with: deviceInfoPayload = [" + deviceInfo + "], provisionHasStopped = [" + this.provisionHasStopped + "], hasCallbackService = [" + this.hasCallbackService + "]");
        if (deviceInfo == null || TextUtils.isEmpty(deviceInfo.service) || !TextUtils.isDigitsOnly(deviceInfo.service) || this.provisionHasStopped.get()) {
            return;
        }
        int i = 0;
        if (this.hasCallbackService.compareAndSet(false, true)) {
            ALog.d(TAG, "do notifySupportProvisionService!");
            ProvisionStatus provisionStatus = ProvisionStatus.DEVICE_SUPPORT_SERVICE;
            provisionStatus.addExtraParams("deviceSSID", this.mDeviceApSsid);
            provisionStatus.addExtraParams("deviceName", deviceInfo.deviceName);
            provisionStatus.addExtraParams("productKey", deviceInfo.productKey);
            ArrayList arrayList = new ArrayList();
            try {
                i = Integer.parseInt(deviceInfo.service);
            } catch (Exception unused) {
                ALog.w(TAG, "parse device service error, service = " + deviceInfo.service);
            }
            if ((i & 1) == 1) {
                arrayList.add(DeviceServiceType.GET_ERROR_CODE);
            }
            if ((i & 2) == 2) {
                arrayList.add(DeviceServiceType.OFFLINE_OTA);
            }
            if ((i & 4) == 4) {
                arrayList.add(DeviceServiceType.OFFLINE_LOG);
            }
            if ((i & 8) == 8) {
                arrayList.add(DeviceServiceType.BATCH_ZERO_PROVISION);
            }
            provisionStatus.addExtraParams("service", arrayList);
            provisionStatus.addExtraParams(AlinkConstants.KEY_FW_VERSION, deviceInfo.fwVersion);
            provisionStatusCallback(provisionStatus);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notifyUser2RecoverWiFi(boolean z) {
        ALog.d(TAG, "notifyUser2RecoverWiFi() called hasNotifiedUser2RecoverWiFi=" + this.hasNotifiedUser2RecoverWiFi.get() + ", hasNotifiedUser2ConnectDevAp=" + this.hasNotifiedUser2ConnectDevAp.get() + ", force=" + z);
        if (this.hasNotifiedUser2RecoverWiFi.get()) {
            return;
        }
        if (!this.hasNotifiedUser2ConnectDevAp.get() && !z) {
            ALog.d(TAG, "connect device ap automatic.");
            return;
        }
        this.hasNotifiedUser2RecoverWiFi.set(true);
        ProvisionStatus provisionStatus = ProvisionStatus.SAP_NEED_USER_TO_RECOVER_WIFI;
        provisionStatus.addExtraParams("ssid", this.mConfigParams.ssid);
        provisionStatusCallback(provisionStatus);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onWiFiConnected() {
        WifiInfo connectionInfo;
        WifiManager wifiManager = this.wifiManager;
        if (wifiManager == null || wifiManager.getConnectionInfo() == null || this.mConfigParams == null || (connectionInfo = this.wifiManager.getConnectionInfo()) == null) {
            return;
        }
        String ssid = connectionInfo.getSSID();
        ALog.d(TAG, "WIFI " + ssid + " connected,startSsid=" + this.mConfigParams.ssid);
        if (ProvisionConfigCenter.getInstance().ignoreLocationPermissionCheck() && !PermissionCheckerUtils.hasLocationAccess(this.mContext)) {
            ALog.d(TAG, "wifi connected, cannot get ssid because no location permission, send connect info.");
            sendConnectInfo();
            return;
        }
        if (!TextUtils.isEmpty(ssid) && isDeviceApConnected(ssid)) {
            ALog.d(TAG, "SAP connected device ap. devId=" + this.mConfigParams.id);
            if (!isProvisionTimerStarted()) {
                startProvisionTimer(new l(this));
            }
            this.isRecoveringWiFi.set(false);
            this.isWiFiRecovered.set(false);
            this.mDeviceApBssid = connectionInfo.getBSSID();
            DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_CONNECT_DEV_AP, String.valueOf(System.currentTimeMillis()));
            PerformanceLog.trace(TAG, "connectDevApResult", PerformanceLog.getJsonObject("result", "success"));
            DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
            if (this.softApState == SoftApState.CONNECT_DEV_AP) {
                updateSoftApState(SoftApState.CONNECTED_DEV_AP);
                provisionStatusCallback(ProvisionStatus.SAP_CONNECTED_TO_DEVICE_AP);
            }
            sendConnectInfo();
            return;
        }
        if (!TextUtils.isEmpty(ssid)) {
            if (ssid.equals("\"" + this.mConfigParams.ssid + "\"")) {
                ALog.d(TAG, "SAP wifi recovered.");
                if (needRecoverWifi()) {
                    this.isWiFiRecovered.set(true);
                    addPrvisionOverListener(this.mConfigCallback);
                    PerformanceLog.trace(TAG, "wifiRecovered");
                    DCUserTrack.addTrackData(AlinkConstants.KEY_END_TIME_WIFI_CONNECTED, String.valueOf(System.currentTimeMillis()));
                    if (this.softApState == SoftApState.SWITCH_AP) {
                        updateSoftApState(SoftApState.NETWORK_RECONNECTED);
                        provisionStatusCallback(ProvisionStatus.SAP_RECOVERED_WIFI);
                    }
                }
                this.isRecoveringWiFi.set(false);
                ALog.d(TAG, "triedToConnectDeviceAp=" + this.triedToConnectDeviceAp.get() + ", needRecoverWiFi=" + needRecoverWifi() + ", receivedAck=" + this.recvSwitchAPAck.get());
                return;
            }
        }
        this.isRecoveringWiFi.set(false);
        this.isWiFiRecovered.set(false);
        if (needReconnectSoftAp() && PermissionCheckerUtils.hasLocationAccess(this.mContext)) {
            ALog.w(TAG, "unknow wifi connected, to connect device ap again.");
            connectDeviceAp(this.mDeviceApSsid, this.mDeviceApBssid, null);
        } else if (needRecoverWifi() && this.isRecoveringWiFi.compareAndSet(false, true) && PermissionCheckerUtils.hasLocationAccess(this.mContext)) {
            this.isWiFiRecovered.set(true);
            ALog.w(TAG, "unknow wifi connected, has send ap info, to recover wifi.");
            if (this.softApState == SoftApState.SWITCH_AP) {
                updateSoftApState(SoftApState.NETWORK_RECONNECTED);
                provisionStatusCallback(ProvisionStatus.SAP_RECOVERED_WIFI);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void provisioning(IConfigCallback iConfigCallback) {
        ALog.d(TAG, "provisioning() called with: configCallback = [" + iConfigCallback + "]");
        if (this.provisionHasStopped.get()) {
            ALog.d(TAG, "provisionHasStopped=true, provisioning ignored.");
            return;
        }
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        if (dCAlibabaConfigParams == null) {
            ALog.d(TAG, "mConfigParams=null, provisioning ignored.");
            return;
        }
        this.mDeviceApSsid = dCAlibabaConfigParams.deviceApSsid;
        updateSoftApState(SoftApState.CONNECT_DEV_AP);
        addListener();
        if (!PermissionCheckerUtils.hasLocationAccess(this.mContext)) {
            DCAlibabaConfigParams dCAlibabaConfigParams2 = this.mConfigParams;
            notifyConnectApByUser(dCAlibabaConfigParams2.productKey, dCAlibabaConfigParams2.id);
            return;
        }
        startPatch();
        if (android10plus()) {
            DCAlibabaConfigParams dCAlibabaConfigParams3 = this.mConfigParams;
            notifyConnectApByUser(dCAlibabaConfigParams3.productKey, dCAlibabaConfigParams3.id);
            return;
        }
        if (!TextUtils.isEmpty(this.mDeviceApSsid)) {
            this.mConfigParams.id = null;
        }
        if (TextUtils.isEmpty(this.mConfigParams.id) && TextUtils.isEmpty(this.mConfigParams.deviceApSsid)) {
            ALog.d(TAG, "device id and device ap ssid are empty, to discover.");
            startDiscover();
            return;
        }
        if (!TextUtils.isEmpty(this.mConfigParams.productKey) && !TextUtils.isEmpty(this.mConfigParams.deviceApSsid) && !TextUtils.isEmpty(AlinkHelper.getPkFromAp(this.mConfigParams.deviceApSsid))) {
            DCAlibabaConfigParams dCAlibabaConfigParams4 = this.mConfigParams;
            if (!dCAlibabaConfigParams4.productKey.equals(AlinkHelper.getPkFromAp(dCAlibabaConfigParams4.deviceApSsid))) {
                ALog.d(TAG, "device ap ssid and pk not match, return.");
                this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("device ap ssid and pk not match.").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
                provisionResultCallback(null);
                return;
            }
        }
        getSSIDAndStartConnectDevAp();
    }

    private void recoverWifiConnect(String str) {
        recoverWifiConnect(str, false);
    }

    private void sendConnectInfo() {
        ALog.d(TAG, "sendConnectInfo() called");
        this.hasConnectedDevAp.set(true);
        if (this.isSendingConnectInfo.get()) {
            ALog.d(TAG, "sendConnectInfo running.");
            return;
        }
        if (this.recvSwitchAPAck.get()) {
            ALog.d(TAG, "reveived switchap ack, return.");
            return;
        }
        addPrvisionOverListener(this.mConfigCallback);
        this.isSendingConnectInfo.set(true);
        cancelTask();
        PerformanceLog.trace(TAG, "switchap");
        DCUserTrack.addTrackData(AlinkConstants.KEY_START_TIME_SWITCH_AP, String.valueOf(System.currentTimeMillis()));
        getDeviceStatus();
        updateSoftApState(SoftApState.SWITCH_AP);
        this.discoveryFuture = ThreadPool.scheduleAtFixedRate(new i(this), 0L, 3000L, TimeUnit.MILLISECONDS);
    }

    private void startPatch() {
        ALog.d(TAG, "startPatch() called");
        AtomicInteger atomicInteger = new AtomicInteger(0);
        this.ignoreConnectDevAP.set(false);
        this.switchWiFiTask = ThreadPool.scheduleAtFixedRate(new m(this, atomicInteger), 8L, 5L, TimeUnit.SECONDS);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopDiscover() {
        SoftAPDiscoverChain softAPDiscoverChain = this.discoverChain;
        if (softAPDiscoverChain != null) {
            softAPDiscoverChain.stopDiscover();
        }
    }

    private void stopPatch() {
        ALog.d(TAG, "stopPatch() called.");
        try {
            if (this.switchWiFiTask != null) {
                this.switchWiFiTask.cancel(true);
                this.switchWiFiTask = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterAPBroadcast() {
        try {
            if (this.mWiFiConnectiveUtils != null) {
                this.mWiFiConnectiveUtils.stopListener();
            }
        } catch (Exception e) {
            ALog.w(TAG, "unregisterAPBroadcast exception=" + e);
        }
    }

    private void updateSoftApState(SoftApState softApState) {
        if (softApState != SoftApState.INITIAL && softApState.ordinal() < this.softApState.ordinal()) {
            ALog.d(TAG, "updateSoftApState() called with: state = [" + softApState + "], ignore");
            return;
        }
        this.softApState = softApState;
        ALog.d(TAG, "updateSoftApState() called with: state = [" + softApState + "]");
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void continueConfig(Map map) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void doExtraPrepareWork(IConfigExtraCallback iConfigExtraCallback) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_SOFT_AP.getName();
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
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void preConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void startConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) throws Exception {
        this.mConfigCallback = iConfigCallback;
        if (!(dCConfigParams instanceof DCAlibabaConfigParams)) {
            ALog.w(TAG, "startConfig params error.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("configParams error.").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        if (AlinkHelper.isValidSoftAp(AlinkHelper.getWifiSsid(this.mContext), false)) {
            ALog.w(TAG, "startConfig connected wifi error.");
            this.provisionErrorInfo = new DCErrorCode("UserFail", DCErrorCode.PF_USER_FAIL).setMsg("cannot start softAp provision when a device ap is connected.").setSubcode(DCErrorCode.SUBCODE_UF_DEVICE_AP_CONNECTED);
            provisionResultCallback(null);
            return;
        }
        updateSoftApState(SoftApState.INITIAL);
        this.mConfigParams = (DCAlibabaConfigParams) dCConfigParams;
        this.hasCallbackToApp.set(false);
        this.sendAppToken2DeviceAB.set(true);
        this.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
        if (!TextUtils.isEmpty(this.mConfigParams.deviceApSsid) && !AlinkHelper.isValidSoftAp(this.mConfigParams.deviceApSsid, false)) {
            ALog.w(TAG, "startConfig set deviceApSsid invalid.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("provision with invalid params, device ap ssid invalid.").setSubcode(DCErrorCode.SUBCODE_PE_SET_INVALID_DEV_AP_SSID);
            provisionResultCallback(null);
            return;
        }
        if (!TextUtils.isEmpty(this.mConfigParams.deviceApSsid) && this.mConfigParams.deviceApSsid.startsWith(AlinkConstants.SOFT_AP_SSID_PREFIX) && getPkAndMac(this.mConfigParams.deviceApSsid) != null && TextUtils.isEmpty((CharSequence) getPkAndMac(this.mConfigParams.deviceApSsid).first)) {
            ALog.w(TAG, "startConfig set deviceApSsid invalid, no pk.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("provision with invalid params, device ap ssid invalid, no pk.").setSubcode(DCErrorCode.SUBCODE_PE_SET_INVALID_DEV_AP_SSID);
            provisionResultCallback(null);
            return;
        }
        if (!TextUtils.isEmpty(this.mConfigParams.deviceApSsid)) {
            this.mConfigParams.id = null;
        }
        if (TextUtils.isEmpty(this.mConfigParams.productEncryptKey)) {
            this.mRandom = StringUtils.getHexString(32);
            if (ProtocolVersion.NO_PRODUCT.getVersion().equals(this.mConfigParams.protocolVersion)) {
                ALog.d(TAG, "No product soft ap version.");
                try {
                    this.encryptRandom = StringUtils.getMDData(this.mRandom, MessageDigestAlgorithms.SHA_256);
                } catch (UnsupportedEncodingException e) {
                    ALog.w(TAG, "SAP e get message digest exception=" + e);
                } catch (NoSuchAlgorithmException e2) {
                    ALog.w(TAG, "SAP get message digest exception=" + e2);
                }
                DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
                dCAlibabaConfigParams.securityRandom = this.mRandom;
                dCAlibabaConfigParams.productEncryptKey = this.encryptRandom.substring(0, 32);
            }
        } else {
            ALog.d(TAG, "use user random. securityRandom=" + this.mConfigParams.securityRandom);
            this.mRandom = this.mConfigParams.securityRandom;
            if (TextUtils.isEmpty(this.mRandom)) {
                this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("securityRandom empty:").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
                provisionResultCallback(null);
                return;
            }
        }
        this.mConfigParams.bindToken = StringUtils.getHexString(32);
        this.userSetIgnoreRecoverWiFi.set(ProvisionConfigCenter.getInstance().ignoreSoftAPRecoverWiFi());
        ALog.i(TAG, "userSetIgnoreRecoverWiFi = " + ProvisionConfigCenter.getInstance().ignoreSoftAPRecoverWiFi());
        ALog.d(TAG, "R=" + this.mConfigParams.securityRandom + ", E=" + this.mConfigParams.productEncryptKey + ", T=" + this.mConfigParams.bindToken);
        if (PermissionCheckerUtils.hasLocationAccess(this.mContext)) {
            ALog.d(TAG, "startConfig() location permission granted and gps enabled.");
            this.configWifiBssid = WiFiUtils.getCurrentApBssid();
            WifiManager wifiManager = this.wifiManager;
            if (wifiManager != null && wifiManager.getConnectionInfo() != null) {
                this.configWifiNetworkId = this.wifiManager.getConnectionInfo().getNetworkId();
            }
        } else {
            ALog.i(TAG, "location permission or gps service not granted.");
        }
        ALog.d(TAG, "configWifiBssid=" + this.configWifiBssid);
        this.provisionHasStopped.set(false);
        this.recvSwitchAPAck.set(false);
        this.isSendingConnectInfo.set(false);
        this.isRecoveringWiFi.set(false);
        this.isWiFiRecovered.set(false);
        this.triedToConnectDeviceAp.set(false);
        this.hasAddConnectAPListener.set(false);
        this.ignoreConnectDevAP.set(false);
        this.hasCallbackService.set(false);
        this.hasConnectedDevAp.set(false);
        this.hasNotifiedUser2ConnectDevAp.set(false);
        this.hasNotifiedUser2RecoverWiFi.set(false);
        this.needDisconnectFirst.set(false);
        startProvisionTimer(new c(this));
        getCipher(iConfigCallback);
    }

    public void startDiscover() {
        this.foundDeviceAp.set(false);
        this.discoverChain = new SoftAPDiscoverChain(this.mContext);
        this.discoverChain.startDiscover(new d(this));
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void stopConfig() {
        unregisterAPBroadcast();
        stopDiscover();
        cancelRequest(this.retryTransitoryClient);
        cancelRequest(this.getDeviceInfoRequest, this.deviceInfoCoapMessageId);
        cancelRequest(this.deviceStatusRequest, this.deviceStatusMessageId);
        this.hasCallbackService.set(false);
        this.hasNotifiedUser2ConnectDevAp.set(false);
        this.hasNotifiedUser2RecoverWiFi.set(false);
        this.hasConnectedDevAp.set(false);
        this.provisionErrorInfo = null;
        stopProvisionTimer();
        stopPatch();
        if (this.triedToConnectDeviceAp.compareAndSet(true, false) && this.isRecoveringWiFi.compareAndSet(false, true) && !this.isWiFiRecovered.get() && !android10plus()) {
            ALog.i(TAG, "triedToConnectDeviceAp=true, to recover wifi.");
            recoverWifiConnect("stopConfig");
        }
        this.provisionHasStopped.set(true);
        this.mConfigParams = null;
        this.isSendingConnectInfo.set(false);
        this.hasCallbackToApp.set(false);
        this.isWiFiRecovered.set(false);
        this.toConnectAPResult = null;
        cancelTask();
        removeProvisionOverListener();
        this.foundDeviceAp.set(false);
        this.recvSwitchAPAck.set(false);
        this.hasAddConnectAPListener.set(false);
        this.ignoreConnectDevAP.set(false);
        this.sendAppToken2DeviceAB.set(true);
        this.needDisconnectFirst.set(false);
        updateSoftApState(SoftApState.INITIAL);
        stopBackupCheck();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recoverWifiConnect(String str, boolean z) {
        DCAlibabaConfigParams dCAlibabaConfigParams;
        ALog.i(TAG, "recoverWifiConnect() called from = " + str + ", force = " + z);
        if (this.userSetIgnoreRecoverWiFi.get() && !z) {
            ALog.d(TAG, "userSetIgnoreRecoverWiFi = true, ignore recover wifi.");
            return;
        }
        if (ProvisionConfigCenter.getInstance().ignoreLocationPermissionCheck()) {
            ALog.d(TAG, "ignoreLocationPermissionCheck = true, ignore recover wifi.");
            return;
        }
        if (!"stopConfig".equals(str) && !z) {
            notifyUser2RecoverWiFi(false);
        }
        if (this.wifiManager == null || (dCAlibabaConfigParams = this.mConfigParams) == null) {
            return;
        }
        WiFiUtils.connect(this.mContext, dCAlibabaConfigParams.ssid, this.mConfigParams.password, this.configWifiBssid, "", this.configWifiNetworkId);
    }

    public SoftAPConfigStrategy(Context context) {
        this.mContext = null;
        this.wifiManager = null;
        this.mWiFiConnectiveUtils = null;
        this.getDeviceInfoRequest = null;
        this.discoveryFuture = null;
        this.deviceInfoCoapMessageId = -1L;
        this.securityAesKey = null;
        this.encryptRandom = "";
        this.isSendingConnectInfo = new AtomicBoolean(false);
        this.isRecoveringWiFi = new AtomicBoolean(false);
        this.isWiFiRecovered = new AtomicBoolean(false);
        this.triedToConnectDeviceAp = new AtomicBoolean(false);
        this.hasAddConnectAPListener = new AtomicBoolean(false);
        this.recvSwitchAPAck = new AtomicBoolean(false);
        this.recvSwitchAPAckTime = new AtomicLong(0L);
        this.needReconnectSoftApAB = new AtomicBoolean(true);
        this.hasCallbackToApp = new AtomicBoolean(false);
        this.ignoreConnectDevAP = new AtomicBoolean(false);
        this.configWifiBssid = null;
        this.configWifiNetworkId = -1;
        this.toConnectAPResult = null;
        this.mDeviceApSsid = null;
        this.discoverChain = null;
        this.foundDeviceAp = new AtomicBoolean(false);
        this.hasNotifiedUser2ConnectDevAp = new AtomicBoolean(false);
        this.hasConnectedDevAp = new AtomicBoolean(false);
        this.mRandom = null;
        this.switchWiFiTask = null;
        this.mCurrentWiFiState = null;
        this.mLastWiFiState = null;
        this.mLastConnectedWiFiBSSID = null;
        this.deviceStatusMessageId = -1L;
        this.userSetIgnoreRecoverWiFi = new AtomicBoolean(false);
        this.hasCallbackService = new AtomicBoolean(false);
        this.needDisconnectFirst = new AtomicBoolean(false);
        this.mDeviceApBssid = null;
        this.hasNotifiedUser2RecoverWiFi = new AtomicBoolean(false);
        this.sendAppToken2DeviceAB = new AtomicBoolean(true);
        this.deviceReportTokenType = DeviceReportTokenType.APP_TOKEN;
        this.softApState = SoftApState.INITIAL;
        this.mWiFiStateListener = new k(this);
        this.mContext = context;
        this.wifiManager = (WifiManager) context.getApplicationContext().getSystemService("wifi");
        this.mWiFiConnectiveUtils = new WiFiConnectiveUtils(context);
    }
}
