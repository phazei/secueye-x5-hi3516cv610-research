package com.aliyun.alink.linksdk.channel.mobile.api;

import android.text.TextUtils;
import java.io.InputStream;

/* JADX INFO: loaded from: classes2.dex */
public class MobileConnectConfig {
    public static InputStream channelRootCrtFile;
    public String appkey;
    public String authServer;
    public String securityGuardAuthcode;
    public String channelHost = null;
    public boolean isCheckChannelRootCrt = true;
    public boolean autoSelectChannelHost = false;
    public String serverUrlForAutoSelectChannel = null;

    public boolean checkValid() {
        return !TextUtils.isEmpty(this.appkey);
    }

    public String toString() {
        return "MobileConnectConfig:[appkey=" + this.appkey + ", secAuthCode=" + this.securityGuardAuthcode + ", authServer=" + this.authServer + ", channelHost = " + this.channelHost + ", isCheckChannelRootCrt =" + this.isCheckChannelRootCrt + ", autoSelectChannelHost =" + this.autoSelectChannelHost + ", serverUrlForAutoSelectChannel" + this.serverUrlForAutoSelectChannel + "]";
    }
}
