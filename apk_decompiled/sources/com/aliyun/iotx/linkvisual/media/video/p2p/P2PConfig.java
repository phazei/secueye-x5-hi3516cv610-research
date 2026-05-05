package com.aliyun.iotx.linkvisual.media.video.p2p;

import android.net.Uri;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;

/* JADX INFO: loaded from: classes2.dex */
public class P2PConfig {
    private String session;
    private String signalUrl;
    private String stunKey;
    private int stunPort;
    private String stunServer;
    private String stunUrl;

    public String getSession() {
        return this.session;
    }

    public String getSignalUrl() {
        return this.signalUrl;
    }

    public String getStunKey() {
        return this.stunKey;
    }

    public int getStunPort() {
        return this.stunPort;
    }

    public String getStunServer() {
        return this.stunServer;
    }

    public String getStunUrl() {
        return this.stunUrl;
    }

    public boolean isValid() {
        return (TextUtils.isEmpty(this.session) || TextUtils.isEmpty(this.stunServer) || TextUtils.isEmpty(this.stunKey) || this.stunPort < 0) ? false : true;
    }

    public void setSignalUrl(String str) {
        this.signalUrl = str;
        this.session = TextUtils.isEmpty(str) ? null : Uri.parse(str).getQueryParameter(UTConstants.E_SDK_CONNECT_SESSION_ACTION);
    }

    public void setStunUrl(String str) {
        this.stunUrl = str;
        if (TextUtils.isEmpty(str)) {
            this.stunServer = null;
            this.stunPort = 0;
            return;
        }
        if (str.indexOf(HttpConstant.SCHEME_SPLIT, 0) == -1) {
            str = "ip://" + str;
        }
        Uri uri = Uri.parse(str);
        this.stunServer = uri.getHost();
        this.stunPort = uri.getPort();
        this.stunKey = uri.getQueryParameter("key");
    }

    public String toString() {
        return "stunServer:" + this.stunServer + "\t stunPort:" + this.stunPort + "\t session:" + this.session;
    }
}
