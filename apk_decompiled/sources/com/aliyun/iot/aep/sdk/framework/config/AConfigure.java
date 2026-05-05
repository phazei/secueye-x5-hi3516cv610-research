package com.aliyun.iot.aep.sdk.framework.config;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.aliyun.iot.aep.sdk.log.ALog;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class AConfigure {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f4672a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private HashMap<String, String> f4673b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Context f4674c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private SharedPreferences f4675d;

    static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        @SuppressLint({"StaticFieldLeak"})
        private static final AConfigure f4676a = new AConfigure();
    }

    private AConfigure() {
        this.f4672a = "release";
        this.f4673b = new HashMap<>();
    }

    public static AConfigure getInstance() {
        return a.f4676a;
    }

    public void init(Context context) {
        this.f4674c = context;
        this.f4675d = context.getSharedPreferences("globalConfig", 0);
        a();
        String str = this.f4673b.get("env");
        String str2 = this.f4673b.get("suffix");
        if (TextUtils.isEmpty(str2)) {
            str2 = "114d";
        }
        String str3 = str + OpenAccountUIConstants.UNDER_LINE + str2;
        if ("true".equalsIgnoreCase(this.f4673b.get("publish")) || TextUtils.isEmpty(str)) {
            str3 = "114d";
            str = "release";
        }
        this.f4673b.put("env", str);
        this.f4673b.put("suffix", str2);
        this.f4673b.put("securityIndex", str3);
    }

    public String getEnv() {
        return this.f4672a;
    }

    public void putConfig(String str, String str2) {
        this.f4673b.put(str, str2);
    }

    public void updateSpConfig(String str, String str2) {
        this.f4675d.edit().putString(str, str2).apply();
    }

    public String getSpConfig(String str) {
        return this.f4675d.getString(str, "");
    }

    public String getConfig(String str) {
        return this.f4673b.get(str);
    }

    public Map getConfig() {
        return this.f4673b;
    }

    void a(String str) {
        if (!"test".equals(str) && !"pre".equals(str) && !"release".equals(str)) {
            Log.e("SDKManager", "env value error: " + str);
            str = "release";
        }
        this.f4672a = str;
    }

    private void a() {
        String strA = a(this.f4674c);
        if (TextUtils.isEmpty(strA)) {
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject(strA);
            Iterator<String> itKeys = jSONObject.keys();
            while (itKeys.hasNext()) {
                String next = itKeys.next();
                String strOptString = jSONObject.optString(next);
                if (next.equals("env")) {
                    a(strOptString);
                }
                String string = this.f4675d.getString(next, "");
                if (!TextUtils.isEmpty(string)) {
                    strOptString = string;
                }
                this.f4673b.put(next, strOptString);
            }
        } catch (JSONException e) {
            ALog.w("SDKManager", "failed to parse json config, " + e.getLocalizedMessage());
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    private String a(Context context) {
        AssetManager assets = context.getAssets();
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assets.open("globalConfig/config.json")));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
        } catch (FileNotFoundException unused) {
            ALog.w("SDKManager", "config file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
