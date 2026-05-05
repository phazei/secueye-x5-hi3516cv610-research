package com.alibaba.sdk.android.push.a;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.beacon.Beacon;
import java.util.HashMap;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AmsLogger f2931a = AmsLogger.getLogger("MPS:BeaconManager");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static a f2932b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Context f2933c = null;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private b f2934d = null;
    private Beacon e = null;
    private final Beacon.OnUpdateListener f = new Beacon.OnUpdateListener() { // from class: com.alibaba.sdk.android.push.a.a.1
        @Override // com.alibaba.sdk.android.beacon.Beacon.OnUpdateListener
        public void onUpdate(List<Beacon.Config> list) {
            a.this.a(list);
        }
    };
    private final Beacon.OnServiceErrListener g = new Beacon.OnServiceErrListener() { // from class: com.alibaba.sdk.android.push.a.a.2
        @Override // com.alibaba.sdk.android.beacon.Beacon.OnServiceErrListener
        public void onErr(Beacon.Error error) {
            a.f2931a.e("beacon error. errorCode:" + error.errCode + ", errorMsg:" + error.errMsg);
        }
    };

    private a() {
    }

    public static a a() {
        if (f2932b == null) {
            synchronized (a.class) {
                if (f2932b == null) {
                    f2932b = new a();
                }
            }
        }
        return f2932b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(List<Beacon.Config> list) {
        f2931a.d("parse beacon config");
        if (list == null || list.size() == 0) {
            return;
        }
        for (Beacon.Config config2 : list) {
            f2931a.d("beacon key:" + config2.key + "; beacon value:" + config2.value);
            if (config2.key.equalsIgnoreCase("___push_service___")) {
                a(config2);
            }
        }
    }

    private boolean a(Beacon.Config config2) {
        if (config2 == null || !config2.key.equalsIgnoreCase("___push_service___")) {
            return false;
        }
        String str = config2.value;
        if (str != null) {
            f2931a.d("push configs:" + str);
            try {
                JSONObject jSONObject = new JSONObject(str);
                if (jSONObject.has("ut")) {
                    a(jSONObject.getString("ut"));
                }
            } catch (JSONException e) {
                f2931a.e("parse push configs failed.", e);
                return false;
            }
        }
        return true;
    }

    private boolean a(String str) {
        if (str == null || this.f2934d == null) {
            return false;
        }
        f2931a.d("is report enabled:" + str);
        this.f2934d.a(str.equalsIgnoreCase("disabled") ^ true);
        return true;
    }

    public void a(Context context, String str, String str2, String str3) {
        this.f2933c = context;
        f2931a.d("appkey:" + str);
        if (this.f2933c == null) {
            f2931a.e("context is null !!");
            return;
        }
        HashMap map = new HashMap();
        map.put("sdkId", "push");
        map.put("sdkVer", str3);
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            this.e = new Beacon.Builder().appKey(str).appSecret(str2).extras(map).startPoll(false).build();
            this.e.addUpdateListener(this.f);
            this.e.addServiceErrListener(this.g);
            this.e.start(this.f2933c.getApplicationContext());
            return;
        }
        f2931a.e("invalid appkey or appsecret. appkey:" + str + ", appsecret:" + str2);
    }

    public void a(b bVar) {
        this.f2934d = bVar;
    }
}
