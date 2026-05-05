package com.aliyun.alink.linksdk.alcs.lpbs.data.breeze;

/* JADX INFO: loaded from: classes2.dex */
public class ComboAuthInfo {
    public AccessInfo accessInfo;
    public SyncAccessInfo syncAccessInfo;

    public ComboAuthInfo(String str, String str2, SyncAccessInfo syncAccessInfo) {
        this.accessInfo = new AccessInfo(str, str2);
        this.syncAccessInfo = syncAccessInfo;
    }

    public static class AccessInfo {
        public String accessKey;
        public String accessToken;

        public AccessInfo(String str, String str2) {
            this.accessKey = str;
            this.accessToken = str2;
        }
    }

    public static class SyncAccessInfo extends AccessInfo {
        public String authCode;

        public SyncAccessInfo(String str, String str2, String str3) {
            super(str, str2);
            this.authCode = str3;
        }
    }
}
