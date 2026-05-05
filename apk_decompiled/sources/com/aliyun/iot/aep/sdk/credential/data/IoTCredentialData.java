package com.aliyun.iot.aep.sdk.credential.data;

import android.text.TextUtils;
import com.aliyun.iot.aep.sdk.credential.IotCredentialManager.IoTCredentialManageImpl;
import com.aliyun.iot.aep.sdk.log.ALog;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class IoTCredentialData {
    public String identity;
    public String iotToken;
    public long iotTokenCreateTime;
    public long iotTokenExpireTime;
    public String refreshToken;
    public long refreshTokenCreateTime;
    public long refreshTokenExpireTime;

    public boolean isIotTokenExpire() {
        return isRefreshTokenExpire() || this.iotTokenCreateTime + this.iotTokenExpireTime <= System.currentTimeMillis();
    }

    public boolean isRefreshTokenExpire() {
        return this.refreshTokenCreateTime + this.refreshTokenExpireTime <= System.currentTimeMillis();
    }

    public void clear() {
        this.iotToken = "";
        this.refreshToken = "";
        this.identity = "";
        this.iotTokenCreateTime = 0L;
        this.refreshTokenCreateTime = 0L;
        this.iotTokenExpireTime = 0L;
        this.refreshTokenExpireTime = 0L;
        ALog.i("IoTCredentialData", "clear iotCredentialData");
    }

    public void update(JSONObject jSONObject) {
        if (jSONObject == null) {
            return;
        }
        try {
            if (jSONObject.has(IoTCredentialManageImpl.AUTH_IOT_TOKEN_IDENTITY_ID_KEY)) {
                this.identity = jSONObject.getString(IoTCredentialManageImpl.AUTH_IOT_TOKEN_IDENTITY_ID_KEY);
                if (!TextUtils.equals(this.iotToken, jSONObject.getString("iotToken"))) {
                    this.iotTokenCreateTime = System.currentTimeMillis();
                }
                if (!TextUtils.equals(this.refreshToken, jSONObject.getString("refreshToken"))) {
                    this.refreshTokenCreateTime = System.currentTimeMillis();
                }
                this.iotToken = jSONObject.getString("iotToken");
                this.refreshToken = jSONObject.getString("refreshToken");
                if (jSONObject.has("iotTokenExpire")) {
                    this.iotTokenExpireTime = Long.parseLong(jSONObject.getString("iotTokenExpire")) * 1000;
                }
                if (jSONObject.has("refreshTokenExpire")) {
                    this.refreshTokenExpireTime = Long.parseLong(jSONObject.getString("refreshTokenExpire")) * 1000;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return "IoTCredentialData{iotToken='" + this.iotToken + "', \niotTokenCreateTime=" + this.iotTokenCreateTime + ", \niotTokenExpireTime=" + this.iotTokenExpireTime + ", \nrefreshToken='" + this.refreshToken + "', \nrefreshTokenCreateTime=" + this.refreshTokenCreateTime + ", \nrefreshTokenExpireTime=" + this.refreshTokenExpireTime + ", \nidentity='" + this.identity + "'}";
    }
}
