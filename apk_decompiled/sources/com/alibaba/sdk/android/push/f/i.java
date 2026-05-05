package com.alibaba.sdk.android.push.f;

import android.util.Log;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.ReportProgressUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class i {
    public static String a(int i, int i2, String str) throws com.alibaba.sdk.android.push.b.f {
        try {
            if (i2 == 200) {
                JSONObject jSONObject = new JSONObject(str);
                String string = jSONObject.getString("code");
                if (string.equals(ReportProgressUtil.CODE_OK)) {
                    return jSONObject.has("data") ? a(i, jSONObject.getString("data")) : "";
                }
                throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.b(string, jSONObject.getString("message")));
            }
            throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.p.copy().msg("请求失败" + i2).detail("requestType:" + i).build());
        } catch (JSONException e) {
            throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.i.copy().msg(e.getMessage()).detail("content: " + str).build());
        }
    }

    public static String a(int i, String str) throws com.alibaba.sdk.android.push.b.f {
        try {
            JSONObject jSONObject = new JSONObject(str);
            if (i == com.alibaba.sdk.android.push.common.util.a.d.LIST_TAGS.a()) {
                return jSONObject.getString("tags");
            }
            if (i == com.alibaba.sdk.android.push.common.util.a.d.LIST_ALIASES.a()) {
                return jSONObject.getString("alias");
            }
            if (i == com.alibaba.sdk.android.push.common.util.a.d.CONFIG.a()) {
                return jSONObject.getString("deviceId");
            }
            if (i == com.alibaba.sdk.android.push.common.util.a.d.CHECK_PUSH_STATUS.a()) {
                return jSONObject.getBoolean("status") ? "on" : "off";
            }
            throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.i.copy().detail("unknown request type " + i).detail("data : " + str).build());
        } catch (JSONException e) {
            throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.i.copy().detail(e.getMessage()).detail("data : " + str).build());
        }
    }

    public static String a(int i, HttpURLConnection httpURLConnection) throws com.alibaba.sdk.android.push.b.f {
        try {
            if (httpURLConnection.getResponseCode() == 200) {
                JSONObject jSONObject = new JSONObject(a(httpURLConnection));
                String string = jSONObject.getString("code");
                if (string.equals(ReportProgressUtil.CODE_OK)) {
                    return jSONObject.has("data") ? a(i, jSONObject.getString("data")) : "";
                }
                throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.b(string, jSONObject.getString("message")));
            }
            throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.p.copy().msg("请求失败" + httpURLConnection.getResponseCode()).detail("requestType:" + i).build());
        } catch (IOException e) {
            throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.h.copy().msg(e.getMessage()).detail(Log.getStackTraceString(e)).build());
        } catch (JSONException e2) {
            throw new com.alibaba.sdk.android.push.b.f(com.alibaba.sdk.android.push.common.a.d.i.copy().msg(e2.getMessage()).detail("content: " + ((String) null)).build());
        }
    }

    private static String a(HttpURLConnection httpURLConnection) {
        int i;
        InputStream inputStream = httpURLConnection.getInputStream();
        byte[] bArr = new byte[1024];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
        while (!Thread.interrupted() && (i = inputStream.read(bArr)) != -1) {
            byteArrayOutputStream.write(bArr, 0, i);
        }
        return new String(byteArrayOutputStream.toByteArray(), "utf-8");
    }
}
