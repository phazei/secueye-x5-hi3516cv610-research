package com.alibaba.sdk.android.push.noonesdk;

import android.app.Application;
import com.alibaba.sdk.android.push.e.a;

/* JADX INFO: loaded from: classes.dex */
public class PushInitConfig {
    private String accsAppConnectHost;
    private String accsSilentConnectHost;
    private String amdcHost;
    private String appKey;
    private String appSecret;
    private Application application;
    private boolean disableChannelProcess;
    private boolean disableChannelProcessHeartbeat;
    private boolean disableForegroundCheck;
    private long loopInterval;
    private boolean loopStartChannel;
    private a.InterfaceC0201a mLargeIconDownloadListener;
    private String pushHost;

    public static class Builder {
        private Application application;
        private a.InterfaceC0201a mLargeIconDownloadListener;
        private String appKey = null;
        private String appSecret = null;
        private boolean disableChannelProcess = false;
        private boolean disableChannelProcessHeartbeat = true;
        private boolean loopStartChannel = false;
        private long loopInterval = 300000;
        private boolean disableForegroundCheck = false;
        private String pushHost = null;
        private String accsAppConnectHost = null;
        private String accsSilentConnectHost = null;
        private String amdcHost = null;

        public Builder accsAppConnectHost(String str) {
            this.accsAppConnectHost = str;
            return this;
        }

        public Builder accsSilentConnectHost(String str) {
            this.accsSilentConnectHost = str;
            return this;
        }

        public Builder amdcHost(String str) {
            this.amdcHost = str;
            return this;
        }

        public Builder appKey(String str) {
            this.appKey = str;
            return this;
        }

        public Builder appSecret(String str) {
            this.appSecret = str;
            return this;
        }

        public Builder application(Application application) {
            this.application = application;
            return this;
        }

        public PushInitConfig build() {
            return new PushInitConfig(this);
        }

        @Deprecated
        public Builder disableChannelProcess(boolean z) {
            this.disableChannelProcess = z;
            return this;
        }

        @Deprecated
        public Builder disableChannelProcessheartbeat(boolean z) {
            this.disableChannelProcessHeartbeat = z;
            return this;
        }

        public Builder disableForegroundCheck(boolean z) {
            this.disableForegroundCheck = z;
            return this;
        }

        public Builder largeIconDownloadListener(a.InterfaceC0201a interfaceC0201a) {
            this.mLargeIconDownloadListener = interfaceC0201a;
            return this;
        }

        public Builder loopInterval(long j) {
            this.loopInterval = j;
            return this;
        }

        public Builder loopStartChannel(boolean z) {
            this.loopStartChannel = z;
            return this;
        }

        public Builder pushHost(String str) {
            this.pushHost = str;
            return this;
        }
    }

    protected PushInitConfig(Builder builder) {
        this.application = builder.application;
        this.appKey = builder.appKey;
        this.appSecret = builder.appSecret;
        this.disableChannelProcess = builder.disableChannelProcess;
        this.loopStartChannel = builder.loopStartChannel;
        this.loopInterval = builder.loopInterval;
        this.disableForegroundCheck = builder.disableForegroundCheck;
        this.disableChannelProcessHeartbeat = builder.disableChannelProcessHeartbeat;
        this.pushHost = builder.pushHost;
        this.accsAppConnectHost = builder.accsAppConnectHost;
        this.accsSilentConnectHost = builder.accsSilentConnectHost;
        this.amdcHost = builder.amdcHost;
        this.mLargeIconDownloadListener = builder.mLargeIconDownloadListener;
    }

    public String getAccsAppConnectHost() {
        return this.accsAppConnectHost;
    }

    public String getAccsSilentConnectHost() {
        return this.accsSilentConnectHost;
    }

    public String getAmdcHost() {
        return this.amdcHost;
    }

    public String getAppKey() {
        return this.appKey;
    }

    public String getAppSecret() {
        return this.appSecret;
    }

    public Application getApplication() {
        return this.application;
    }

    public a.InterfaceC0201a getLargeIconDownloadListener() {
        return this.mLargeIconDownloadListener;
    }

    public long getLoopInterval() {
        return this.loopInterval;
    }

    public String getPushHost() {
        return this.pushHost;
    }

    public boolean isDisableChannelProcess() {
        return this.disableChannelProcess;
    }

    public boolean isDisableChannelProcessHeartbeat() {
        return this.disableChannelProcessHeartbeat;
    }

    public boolean isDisableForegroundCheck() {
        return this.disableForegroundCheck;
    }

    public boolean isLoopStartChannel() {
        return this.loopStartChannel;
    }
}
