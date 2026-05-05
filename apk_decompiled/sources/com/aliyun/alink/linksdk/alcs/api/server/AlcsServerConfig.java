package com.aliyun.alink.linksdk.alcs.api.server;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public final class AlcsServerConfig {
    protected String mBlacklist;
    protected String mDeviceName;
    protected int mHeartBeatTimeout;
    protected boolean mIsSecurityPost;
    protected List<PrefixSecretNode> mPreSecList;
    protected String mProductKey;
    protected int mSafePort;
    protected int mUnSafePort;
    private int sessionExpireValue;

    private AlcsServerConfig() {
        this.mIsSecurityPost = true;
        this.sessionExpireValue = 86400;
        this.mHeartBeatTimeout = 60000;
        this.mUnSafePort = 5683;
        this.mSafePort = 5684;
        this.mPreSecList = new ArrayList();
    }

    public int getSessionExpireValue() {
        return this.sessionExpireValue;
    }

    @Deprecated
    public boolean isSecurityPost() {
        return this.mIsSecurityPost;
    }

    public int getHeartBeatTimeout() {
        return this.mHeartBeatTimeout;
    }

    public int getUnSafePort() {
        return this.mUnSafePort;
    }

    public int getSafePort() {
        return this.mSafePort;
    }

    public String getProductKey() {
        return this.mProductKey;
    }

    public String getDeviceName() {
        return this.mDeviceName;
    }

    public List<PrefixSecretNode> getPreSecList() {
        return this.mPreSecList;
    }

    public static final class Builder {

        /* JADX INFO: renamed from: config, reason: collision with root package name */
        private AlcsServerConfig f3948config = new AlcsServerConfig();

        public Builder setSessionExpireValue(int i) {
            this.f3948config.sessionExpireValue = i;
            return this;
        }

        @Deprecated
        public Builder setIsSecurityPort(boolean z) {
            this.f3948config.mIsSecurityPost = z;
            return this;
        }

        public Builder setHeartBeatTimeout(int i) {
            this.f3948config.mHeartBeatTimeout = i;
            return this;
        }

        public Builder setUnsafePort(int i) {
            this.f3948config.mUnSafePort = i;
            return this;
        }

        public Builder setSafePort(int i) {
            this.f3948config.mSafePort = i;
            return this;
        }

        public Builder setProdKey(String str) {
            this.f3948config.mProductKey = str;
            return this;
        }

        public Builder setDevcieName(String str) {
            this.f3948config.mDeviceName = str;
            return this;
        }

        public Builder addPrefixSec(String str, String str2) {
            this.f3948config.mPreSecList.add(new PrefixSecretNode(str, str2));
            return this;
        }

        public Builder setBlackList(String str) {
            this.f3948config.mBlacklist = str;
            return this;
        }

        public AlcsServerConfig build() {
            return this.f3948config;
        }
    }
}
