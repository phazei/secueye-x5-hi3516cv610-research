package com.aliyun.alink.linksdk.securesigner;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.securesigner.util.Utils;

/* JADX INFO: loaded from: classes2.dex */
public class DefaultSecuritySourceImpl implements ISecuritySource {
    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String getAppKey() {
        return SecuritySourceContext.getInstance().getAppKey();
    }

    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String sign(String str, String str2) {
        String strHmacSha1;
        if (TextUtils.isEmpty(str2) || str2.equals("HmacSHA1")) {
            strHmacSha1 = Utils.hmacSha1(SecuritySourceContext.getInstance().getAppSecretKey(), str);
        } else {
            strHmacSha1 = str2.equals("MD5") ? Utils.getMD5String(str) : null;
        }
        return Utils.hexStr2Base64Str(strHmacSha1.toLowerCase());
    }
}
