package com.taobao.accs;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.logger.ILog;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.AccsLogger;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class AccsClientConfig {
    public static final String DEFAULT_CONFIGTAG = "default";
    public static final int SECURITY_OFF = 2;
    public static final int SECURITY_OPEN = 1;
    public static final int SECURITY_TAOBAO = 0;
    private static Context mContext;
    private long loopInterval;
    private boolean mAccsHeartbeatEnable;
    private String mAppKey;
    private String mAppSecret;
    private String mAuthCode;
    private boolean mAutoUnit;
    private String mChannelHost;
    private boolean mChannelLoopStart;
    private int mChannelPubKey;
    private int mConfigEnv;
    private boolean mDisableChannel;
    private String mInappHost;
    private int mInappPubKey;
    private boolean mKeepalive;
    private boolean mQuickReconnect;
    private int mSecurity;
    private String mStoreId;
    private String mTag;
    private static ILog log = AccsLogger.getLogger("AccsClientConfig");
    public static final String[] DEFAULT_CENTER_HOSTS = {"msgacs.m.taobao.com", "msgacs.wapa.taobao.com", "msgacs.waptest.taobao.com"};
    private static final String[] DEFAULT_CHANNEL_HOSTS = {"accscdn.m.taobao.com", "acs.wapa.taobao.com", "acs.waptest.taobao.com"};

    @Deprecated
    public static boolean loadedStaticConfig = true;

    @ENV
    public static int mEnv = 0;
    private static Map<String, AccsClientConfig> mReleaseConfigs = new ConcurrentHashMap(1);
    private static Map<String, AccsClientConfig> mPreviewConfigs = new ConcurrentHashMap(1);
    private static Map<String, AccsClientConfig> mDebugConfigs = new ConcurrentHashMap(1);

    /* JADX INFO: compiled from: Taobao */
    @Retention(RetentionPolicy.CLASS)
    public @interface ENV {
    }

    /* JADX INFO: compiled from: Taobao */
    @Retention(RetentionPolicy.CLASS)
    public @interface SECURITY_TYPE {
    }

    public static Context getContext() {
        Context context = mContext;
        if (context != null) {
            return context;
        }
        synchronized (AccsClientConfig.class) {
            if (mContext != null) {
                return mContext;
            }
            try {
                Class<?> cls = Class.forName("android.app.ActivityThread");
                Object objInvoke = cls.getMethod("currentActivityThread", new Class[0]).invoke(cls, new Object[0]);
                mContext = (Context) objInvoke.getClass().getMethod("getApplication", new Class[0]).invoke(objInvoke, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mContext;
        }
    }

    protected AccsClientConfig() {
    }

    @Deprecated
    public static AccsClientConfig getConfig(String str) {
        Map<String, AccsClientConfig> map;
        switch (mEnv) {
            case 1:
                map = mPreviewConfigs;
                break;
            case 2:
                map = mDebugConfigs;
                break;
            default:
                map = mReleaseConfigs;
                break;
        }
        for (AccsClientConfig accsClientConfig : map.values()) {
            if (accsClientConfig.mAppKey.equals(str) && accsClientConfig.mConfigEnv == mEnv) {
                return accsClientConfig;
            }
        }
        log.e("getConfigByTag return null", "appKey", str);
        return null;
    }

    public static AccsClientConfig getConfigByTag(String str) {
        Map<String, AccsClientConfig> map;
        switch (mEnv) {
            case 1:
                map = mPreviewConfigs;
                break;
            case 2:
                map = mDebugConfigs;
                break;
            default:
                map = mReleaseConfigs;
                break;
        }
        AccsClientConfig accsClientConfig = map.get(str);
        if (accsClientConfig == null) {
            log.w("getConfigByTag return null", "env", Integer.valueOf(mEnv), Constants.KEY_CONFIG_TAG, str);
        }
        return accsClientConfig;
    }

    public static List<String> tags() {
        Map<String, AccsClientConfig> map;
        switch (mEnv) {
            case 1:
                map = mPreviewConfigs;
                break;
            case 2:
                map = mDebugConfigs;
                break;
            default:
                map = mReleaseConfigs;
                break;
        }
        return new ArrayList(map.keySet());
    }

    public String getAppKey() {
        return this.mAppKey;
    }

    public String getAppSecret() {
        return this.mAppSecret;
    }

    public String getInappHost() {
        return this.mInappHost;
    }

    public String getChannelHost() {
        return this.mChannelHost;
    }

    public int getSecurity() {
        return this.mSecurity;
    }

    public String getAuthCode() {
        return this.mAuthCode;
    }

    public int getInappPubKey() {
        return this.mInappPubKey;
    }

    public int getChannelPubKey() {
        return this.mChannelPubKey;
    }

    public boolean isKeepalive() {
        return this.mKeepalive;
    }

    public boolean isAutoUnit() {
        return this.mAutoUnit;
    }

    public String getTag() {
        return this.mTag;
    }

    public int getConfigEnv() {
        return this.mConfigEnv;
    }

    public boolean getDisableChannel() {
        return this.mDisableChannel;
    }

    public boolean isQuickReconnect() {
        return this.mQuickReconnect;
    }

    public String getStoreId() {
        return this.mStoreId;
    }

    public boolean isAccsHeartbeatEnable() {
        return this.mAccsHeartbeatEnable;
    }

    public boolean isChannelLoopStart() {
        return this.mChannelLoopStart;
    }

    public long getLoopInterval() {
        return this.loopInterval;
    }

    public String toString() {
        return "AccsClientConfig{Tag=" + this.mTag + ", ConfigEnv=" + this.mConfigEnv + ", AppKey=" + this.mAppKey + ", AppSecret=" + this.mAppSecret + ", InappHost=" + this.mInappHost + ", ChannelHost=" + this.mChannelHost + ", Security=" + this.mSecurity + ", AuthCode=" + this.mAuthCode + ", InappPubKey=" + this.mInappPubKey + ", ChannelPubKey=" + this.mChannelPubKey + ", Keepalive=" + this.mKeepalive + ", AutoUnit=" + this.mAutoUnit + ", DisableChannel=" + this.mDisableChannel + ", QuickReconnect=" + this.mQuickReconnect + "}";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AccsClientConfig accsClientConfig = (AccsClientConfig) obj;
        if (!this.mInappHost.equals(accsClientConfig.mInappHost) || this.mInappPubKey != accsClientConfig.mInappPubKey || !this.mChannelHost.equals(accsClientConfig.mChannelHost) || this.mChannelPubKey != accsClientConfig.mChannelPubKey || this.mSecurity != accsClientConfig.mSecurity || this.mConfigEnv != accsClientConfig.mConfigEnv || !this.mAppKey.equals(accsClientConfig.mAppKey) || this.mKeepalive != accsClientConfig.mKeepalive || this.mDisableChannel != accsClientConfig.mDisableChannel) {
            return false;
        }
        String str = this.mAuthCode;
        if (str == null ? accsClientConfig.mAuthCode != null : !str.equals(accsClientConfig.mAuthCode)) {
            return false;
        }
        String str2 = this.mAppSecret;
        if (str2 == null ? accsClientConfig.mAppSecret == null : str2.equals(accsClientConfig.mAppSecret)) {
            return this.mTag.equals(accsClientConfig.mTag);
        }
        return false;
    }

    /* JADX INFO: compiled from: Taobao */
    public static class Builder {
        private String mAppKey;
        private String mAppSecret;
        private String mAuthCode;
        private String mChannelHost;
        private String mInappHost;
        private String mStoreId;
        private String mTag;
        private int mInappPubKey = -1;
        private int mChannelPubKey = -1;
        private boolean mKeepalive = true;
        private boolean mAutoUnit = true;
        private int mConfigEnv = -1;
        private boolean mDisableChannel = false;
        private boolean mQuickReconnect = false;
        private boolean mAccsHeartbeatEnable = false;
        private boolean mChannelLoopStart = false;
        private long loopInterval = 300000;

        public Builder setAppKey(String str) {
            this.mAppKey = str;
            return this;
        }

        public Builder setAppSecret(String str) {
            this.mAppSecret = str;
            return this;
        }

        public Builder setInappHost(String str) {
            this.mInappHost = str;
            return this;
        }

        public Builder setChannelHost(String str) {
            this.mChannelHost = str;
            return this;
        }

        public Builder setAutoCode(String str) {
            this.mAuthCode = str;
            return this;
        }

        public Builder setInappPubKey(int i) {
            this.mInappPubKey = i;
            return this;
        }

        public Builder setChannelPubKey(int i) {
            this.mChannelPubKey = i;
            return this;
        }

        public Builder setKeepAlive(boolean z) {
            this.mKeepalive = z;
            return this;
        }

        public Builder setAutoUnit(boolean z) {
            this.mAutoUnit = z;
            return this;
        }

        public Builder setConfigEnv(@ENV int i) {
            this.mConfigEnv = i;
            return this;
        }

        public Builder setStoreId(String str) {
            this.mStoreId = str;
            return this;
        }

        public Builder setTag(String str) {
            this.mTag = str;
            return this;
        }

        public Builder setDisableChannel(boolean z) {
            this.mDisableChannel = z;
            return this;
        }

        public Builder setQuickReconnect(boolean z) {
            this.mQuickReconnect = z;
            return this;
        }

        public Builder setAccsHeartbeatEnable(boolean z) {
            this.mAccsHeartbeatEnable = z;
            return this;
        }

        public Builder loopChannelStart(boolean z) {
            this.mChannelLoopStart = z;
            return this;
        }

        public Builder loopChannelInterval(long j) {
            this.loopInterval = j;
            return this;
        }

        public AccsClientConfig build() throws AccsException {
            Map map;
            if (TextUtils.isEmpty(this.mAppKey)) {
                throw new AccsException("appkey null");
            }
            AccsClientConfig accsClientConfig = new AccsClientConfig();
            accsClientConfig.mAppKey = this.mAppKey;
            accsClientConfig.mAppSecret = this.mAppSecret;
            accsClientConfig.mAuthCode = this.mAuthCode;
            accsClientConfig.mKeepalive = this.mKeepalive;
            accsClientConfig.mAutoUnit = this.mAutoUnit;
            accsClientConfig.mInappPubKey = this.mInappPubKey;
            accsClientConfig.mChannelPubKey = this.mChannelPubKey;
            accsClientConfig.mInappHost = this.mInappHost;
            accsClientConfig.mChannelHost = this.mChannelHost;
            accsClientConfig.mTag = this.mTag;
            accsClientConfig.mStoreId = this.mStoreId;
            accsClientConfig.mConfigEnv = this.mConfigEnv;
            accsClientConfig.mDisableChannel = this.mDisableChannel;
            accsClientConfig.mQuickReconnect = this.mQuickReconnect;
            accsClientConfig.mAccsHeartbeatEnable = this.mAccsHeartbeatEnable;
            accsClientConfig.mChannelLoopStart = this.mChannelLoopStart;
            accsClientConfig.loopInterval = this.loopInterval;
            if (accsClientConfig.mConfigEnv < 0) {
                accsClientConfig.mConfigEnv = AccsClientConfig.mEnv;
            }
            if (TextUtils.isEmpty(accsClientConfig.mAppSecret)) {
                accsClientConfig.mSecurity = 0;
            } else {
                accsClientConfig.mSecurity = 2;
            }
            if (TextUtils.isEmpty(accsClientConfig.mInappHost)) {
                accsClientConfig.mInappHost = AccsClientConfig.DEFAULT_CENTER_HOSTS[accsClientConfig.mConfigEnv];
            }
            if (TextUtils.isEmpty(accsClientConfig.mChannelHost)) {
                accsClientConfig.mChannelHost = AccsClientConfig.DEFAULT_CHANNEL_HOSTS[accsClientConfig.mConfigEnv];
            }
            if (TextUtils.isEmpty(accsClientConfig.mTag)) {
                accsClientConfig.mTag = accsClientConfig.mAppKey;
            }
            switch (accsClientConfig.mConfigEnv) {
                case 1:
                    map = AccsClientConfig.mPreviewConfigs;
                    break;
                case 2:
                    map = AccsClientConfig.mDebugConfigs;
                    break;
                default:
                    map = AccsClientConfig.mReleaseConfigs;
                    break;
            }
            AccsClientConfig.log.d("build config", accsClientConfig);
            AccsClientConfig accsClientConfig2 = (AccsClientConfig) map.get(accsClientConfig.getTag());
            if (accsClientConfig2 != null) {
                AccsClientConfig.log.w("build cover", "old", accsClientConfig2, "new", accsClientConfig);
            }
            map.put(accsClientConfig.getTag(), accsClientConfig);
            return accsClientConfig;
        }
    }
}
