package com.aliyun.alink.business.devicecenter.provision.core.qr;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.api.add.ProvisionStatus;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.BackupCheckType;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.log.PerformanceLog;
import com.aliyun.alink.business.devicecenter.provision.core.aa;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.business.devicecenter.utils.WiFiUtils;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import java.security.NoSuchAlgorithmException;
import java.util.EnumSet;
import java.util.Map;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_QR)
public class QRConfigStrategy extends BaseProvisionStrategy implements IConfigStrategy {
    public static String TAG = "QRConfigStrategy";
    public String getTokenBssid;
    public Context mContext;
    public String sendToken;
    public int sendTokenLen;

    public QRConfigStrategy() {
        this.mContext = null;
        this.sendToken = null;
        this.sendTokenLen = 6;
        this.getTokenBssid = null;
    }

    private String getToken(String str, String str2) {
        String lastBssid = StringUtils.getLastBssid(this.getTokenBssid, 6);
        String mDData = null;
        if (TextUtils.isEmpty(lastBssid)) {
            ALog.w(TAG, "getTokenWithBssid=true, but get last bssid failed. no need check token.");
            return null;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        String str3 = lastBssid + str + StringUtils.bytesToHexString(str2.getBytes());
        try {
            mDData = StringUtils.getMDData(StringUtils.hexStringTobytes(str3), MessageDigestAlgorithms.SHA_256);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        ALog.d(TAG, "enc=" + str3 + ", token=" + mDData);
        return mDData;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void continueConfig(Map map) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void doExtraPrepareWork(IConfigExtraCallback iConfigExtraCallback) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_QR.getName();
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
            ALog.e(TAG, "startConfig params error.");
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("configParams error:").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        this.mConfigParams = (DCAlibabaConfigParams) dCConfigParams;
        this.getTokenBssid = WiFiUtils.getCurrentApBssid();
        if (TextUtils.isEmpty(this.getTokenBssid)) {
            this.getTokenBssid = "";
        }
        String upperCase = this.getTokenBssid.replace(":", "").toUpperCase();
        this.sendToken = StringUtils.getHexString(this.sendTokenLen);
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("v", "Ali_1");
        jSONObject.put("s", this.mConfigParams.ssid);
        jSONObject.put(TtmlNode.TAG_P, this.mConfigParams.password);
        if (TextUtils.isEmpty(upperCase)) {
            ALog.i(TAG, "bssid is null, maybe wifi not enabled or location permission not granted.");
        } else {
            jSONObject.put("b", upperCase);
        }
        jSONObject.put("t", this.sendToken);
        String string = jSONObject.toString();
        ALog.d(TAG, "qrContent=" + string);
        PerformanceLog.trace(TAG, "genQR");
        DCUserTrack.addTrackData(AlinkConstants.KEY_SHOW_QR_INFO, String.valueOf(System.currentTimeMillis()));
        provisionStatusCallback(ProvisionStatus.QR_PROVISION_READY.setMessage(string));
        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
        startProvisionTimer();
        this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT).setSubcode(DCErrorCode.SUBCODE_PT_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL).setMsg("scanQRCodeTimeout");
        addProvisionOverListener(new aa(this));
        DCAlibabaConfigParams dCAlibabaConfigParams = this.mConfigParams;
        if (dCAlibabaConfigParams != null) {
            String token = getToken(this.sendToken, dCAlibabaConfigParams.password);
            if (!TextUtils.isEmpty(token) && token.length() > 31) {
                this.mConfigParams.bindToken = token.substring(0, 32).toUpperCase();
            }
        }
        updateBackupCheckTypeSet(EnumSet.of(BackupCheckType.CHECK_COAP_GET, BackupCheckType.CHECK_APP_TOKEN));
        startBackupCheck(true, 0L);
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void stopConfig() {
        removeProvisionOverListener();
        this.mConfigParams = null;
        this.provisionErrorInfo = null;
        stopProvisionTimer();
        stopBackupCheck();
    }

    public QRConfigStrategy(Context context) {
        this.mContext = null;
        this.sendToken = null;
        this.sendTokenLen = 6;
        this.getTokenBssid = null;
        this.mContext = context;
    }
}
