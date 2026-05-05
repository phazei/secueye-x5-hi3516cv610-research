package com.aliyun.alink.linksdk.tmp.data.auth;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;

/* JADX INFO: loaded from: classes2.dex */
public class ProvisionAuthData {
    public String accessKey;
    public String accessToken;
    public String authCode;
    public String authSecret;
    public String deviceName;
    public String productKey;

    public String getId() {
        return TextHelper.combineStr(this.productKey, this.deviceName);
    }

    public ProvisionAuthData(String str, String str2, AuthPairData authPairData) {
        this.productKey = str;
        this.deviceName = str2;
        this.accessKey = authPairData.accessKey;
        this.accessToken = authPairData.accessToken;
        this.authCode = authPairData.authCode;
        this.authSecret = authPairData.authSecret;
    }

    public ProvisionAuthData() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TextUtils.isEmpty(this.productKey) ? TmpConstant.GROUP_ROLE_UNKNOWN : this.productKey);
        sb.append(TextUtils.isEmpty(this.deviceName) ? TmpConstant.GROUP_ROLE_UNKNOWN : this.deviceName);
        sb.append(TextUtils.isEmpty(this.accessKey) ? TmpConstant.GROUP_ROLE_UNKNOWN : this.accessKey);
        sb.append(TextUtils.isEmpty(this.accessToken) ? TmpConstant.GROUP_ROLE_UNKNOWN : this.accessToken);
        sb.append(TextUtils.isEmpty(this.authCode) ? TmpConstant.GROUP_ROLE_UNKNOWN : this.authCode);
        sb.append(TextUtils.isEmpty(this.authSecret) ? TmpConstant.GROUP_ROLE_UNKNOWN : this.authSecret);
        return sb.toString();
    }
}
