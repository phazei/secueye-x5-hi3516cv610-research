package anet.channel;

import android.text.TextUtils;
import anet.channel.entity.ENV;
import anet.channel.security.ISecurity;
import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public final class Config {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f1621b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f1622c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private ENV f1623d = ENV.ONLINE;
    private ISecurity e;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static Map<String, Config> f1620a = new HashMap();
    public static final Config DEFAULT_CONFIG = new Builder().setTag("[default]").setAppkey("[default]").setEnv(ENV.ONLINE).build();

    protected Config() {
    }

    public static Config getConfigByTag(String str) {
        Config config2;
        synchronized (f1620a) {
            config2 = f1620a.get(str);
        }
        return config2;
    }

    public static Config getConfig(String str, ENV env) {
        synchronized (f1620a) {
            for (Config config2 : f1620a.values()) {
                if (config2.f1623d == env && config2.f1622c.equals(str)) {
                    return config2;
                }
            }
            return null;
        }
    }

    public String getTag() {
        return this.f1621b;
    }

    public String getAppkey() {
        return this.f1622c;
    }

    public ENV getEnv() {
        return this.f1623d;
    }

    public ISecurity getSecurity() {
        return this.e;
    }

    public String toString() {
        return this.f1621b;
    }

    /* JADX INFO: compiled from: Taobao */
    public static class Builder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private String f1624a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f1625b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private ENV f1626c = ENV.ONLINE;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private String f1627d;
        private String e;

        public Builder setTag(String str) {
            this.f1624a = str;
            return this;
        }

        public Builder setAppkey(String str) {
            this.f1625b = str;
            return this;
        }

        public Builder setEnv(ENV env) {
            this.f1626c = env;
            return this;
        }

        public Builder setAuthCode(String str) {
            this.f1627d = str;
            return this;
        }

        public Builder setAppSecret(String str) {
            this.e = str;
            return this;
        }

        public Config build() {
            if (!TextUtils.isEmpty(this.f1625b)) {
                synchronized (Config.f1620a) {
                    for (Config config2 : Config.f1620a.values()) {
                        if (config2.f1623d == this.f1626c && config2.f1622c.equals(this.f1625b)) {
                            ALog.w("awcn.Config", "duplicated config exist!", null, "appkey", this.f1625b, "env", this.f1626c);
                            if (!TextUtils.isEmpty(this.f1624a)) {
                                Config.f1620a.put(this.f1624a, config2);
                            }
                            return config2;
                        }
                    }
                    Config config3 = new Config();
                    config3.f1622c = this.f1625b;
                    config3.f1623d = this.f1626c;
                    if (TextUtils.isEmpty(this.f1624a)) {
                        config3.f1621b = StringUtils.concatString(this.f1625b, "$", this.f1626c.toString());
                    } else {
                        config3.f1621b = this.f1624a;
                    }
                    if (!TextUtils.isEmpty(this.e)) {
                        config3.e = anet.channel.security.c.a().createNonSecurity(this.e);
                    } else {
                        config3.e = anet.channel.security.c.a().createSecurity(this.f1627d);
                    }
                    synchronized (Config.f1620a) {
                        Config.f1620a.put(config3.f1621b, config3);
                    }
                    return config3;
                }
            }
            throw new RuntimeException("appkey can not be null or empty!");
        }
    }
}
