package com.aliyun.alink.h2.api;

import android.text.TextUtils;
import com.aliyun.alink.h2.b.a;
import com.heytap.mcssdk.constant.IntentConstant;
import com.taobao.accs.common.Constants;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class Profile {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f3812a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f3813b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f3814c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f3815d = 30000;
    private int e = 60000;
    private boolean f = false;
    private int g = Runtime.getRuntime().availableProcessors();
    private int h = Runtime.getRuntime().availableProcessors() * 4;
    private int i = 1024;
    private IAuthSign j = null;
    private URL l = null;
    private Map<String, String> k = new HashMap();

    public String getHost() {
        URL url = this.l;
        if (url != null) {
            return url.getHost();
        }
        return this.f3813b;
    }

    public int getPort() {
        URL url = this.l;
        if (url != null) {
            return url.getPort();
        }
        int i = this.f3814c;
        return i == -1 ? Constants.PORT : i;
    }

    public int getHeartBeatInterval() {
        return this.f3815d;
    }

    public int getHeartBeatTimeOut() {
        return this.e;
    }

    public Map<String, String> getAuthParams() {
        return this.k;
    }

    private URL a() {
        try {
            this.l = new URL(this.f3812a);
            return this.l;
        } catch (MalformedURLException e) {
            a.d("Profile", "fail to parse url " + this.f3812a + "," + e.getMessage());
            this.l = null;
            return null;
        }
    }

    private Profile(String str) {
        this.f3812a = str;
        a();
    }

    public IAuthSign getAuthSign() {
        return this.j;
    }

    public static Profile getDeviceProfile(String str, String str2, String str3, String str4, String str5) {
        Profile profile = new Profile(str);
        profile.k.put("name", "devicename");
        profile.k.put("param-product-key", str2);
        profile.k.put("param-device-name", str3);
        profile.k.put("deviceSecret", str4);
        profile.k.put("param-client-id", str5);
        profile.f = false;
        return profile;
    }

    public static Profile getAppKeyProfile(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str3)) {
            throw new IllegalArgumentException("appSecret param error.");
        }
        Profile profile = new Profile(str);
        profile.k.put("name", "appkey");
        profile.k.put("param-app-key", str2);
        profile.k.put(IntentConstant.APP_SECRET, str3);
        profile.f = true;
        return profile;
    }

    public static Profile getAppKeyProfile(String str, String str2, IAuthSign iAuthSign) {
        if (iAuthSign == null || TextUtils.isEmpty(iAuthSign.getSignMethod())) {
            throw new IllegalArgumentException("authSign param error.");
        }
        Profile profile = new Profile(str);
        profile.k.put("name", "appkey");
        profile.k.put("param-sign-method", iAuthSign.getSignMethod());
        profile.k.put("param-app-key", str2);
        profile.f = true;
        profile.j = iAuthSign;
        return profile;
    }
}
