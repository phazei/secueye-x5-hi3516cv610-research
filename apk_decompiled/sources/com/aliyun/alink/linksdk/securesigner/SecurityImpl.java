package com.aliyun.alink.linksdk.securesigner;

import com.aliyun.alink.linksdk.securesigner.util.Utils;

/* JADX INFO: loaded from: classes2.dex */
public class SecurityImpl implements ISecuritySource {
    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String getAppKey() {
        if (SecuritySourceContext.getInstance().getAppKey() != null) {
            return SecuritySourceContext.getInstance().getAppKey();
        }
        if (SecuritySourceContext.getInstance().getISecuritySource() != null && SecuritySourceContext.getInstance().getISecuritySource().getAppKey() != null) {
            return SecuritySourceContext.getInstance().getISecuritySource().getAppKey();
        }
        throw new RuntimeException("can not get appKey");
    }

    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String sign(String str, String str2) {
        if (SecuritySourceContext.getInstance().getAppSecretKey() != null) {
            if ("MD5".equals(str2)) {
                return Utils.getMD5String(str).toLowerCase();
            }
            if ("HmacSHA256".equals(str2)) {
                return Utils.hmacSha256(SecuritySourceContext.getInstance().getAppSecretKey().getBytes(), str.getBytes()).toLowerCase();
            }
            return Utils.hmacSha1(SecuritySourceContext.getInstance().getAppSecretKey(), str).toLowerCase();
        }
        if (SecuritySourceContext.getInstance().getISecuritySource() != null) {
            return SecuritySourceContext.getInstance().getISecuritySource().sign(str, str2).toLowerCase();
        }
        throw new RuntimeException("can not get appSecretKey or CustomSecuritySource");
    }
}
