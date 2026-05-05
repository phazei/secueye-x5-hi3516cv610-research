package com.aliyun.alink.linksdk.securesigner;

/* JADX INFO: loaded from: classes2.dex */
public class CustomSecuritySourceImpl implements ISecuritySource {
    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String getAppKey() {
        return SecuritySourceContext.getInstance().getISecuritySource().getAppKey();
    }

    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String sign(String str, String str2) {
        return SecuritySourceContext.getInstance().getISecuritySource().sign(str, str2);
    }
}
