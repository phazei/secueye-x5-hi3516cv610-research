package com.alibaba.sdk.android.push.common.util.a;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public abstract class c extends AsyncTask<Map<String, String>, Void, b> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    static AmsLogger f3059a = AmsLogger.getLogger("MPS:SendRequestTask");

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Context f3061c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f3062d;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f3060b = "POST";
    private int e = 0;

    public c(Context context, String str) {
        this.f3061c = context;
        this.f3062d = str;
    }

    private void a(String str, Map<String, String> map) {
        try {
            f3059a.d("request url :" + str);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                f3059a.d("key: " + entry.getKey() + " value: " + entry.getValue());
            }
        } catch (Throwable unused) {
        }
    }

    public int a() {
        return this.e;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public b doInBackground(Map<String, String>... mapArr) {
        b bVar;
        Map<String, String> map = mapArr[0];
        if (map.containsKey(d.u)) {
            this.e = Integer.parseInt(map.get(d.u));
            bVar = new b(Integer.parseInt(map.get(d.u)));
        } else {
            bVar = new b();
        }
        try {
            String strA = a(this.f3061c, this.f3062d, map);
            bVar.f3056b = 200;
            bVar.f3055a = strA;
        } catch (a e) {
            bVar.f3057c = e.a();
            bVar.f3056b = -1;
            bVar.f3055a = e.getMessage();
        }
        return bVar;
    }

    public String a(Context context, String str, Map<String, String> map) {
        int i;
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                try {
                    Map<String, String> mapA = a(context, map);
                    a(str, mapA);
                    HttpURLConnection httpURLConnectionA = com.alibaba.sdk.android.ams.common.util.b.a(str, mapA, this.f3060b);
                    if (httpURLConnectionA == null) {
                        f3059a.e("failed to access VIP service.");
                        throw new a(com.alibaba.sdk.android.push.common.a.d.p.copy().msg("创建请求连接失败").build());
                    }
                    if (httpURLConnectionA.getResponseCode() != 200) {
                        throw new a(com.alibaba.sdk.android.push.common.a.d.p.copy().msg("请求失败：" + httpURLConnectionA.getResponseCode()).build());
                    }
                    InputStream inputStream = httpURLConnectionA.getInputStream();
                    byte[] bArr = new byte[1024];
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(1024);
                    while (!Thread.interrupted() && (i = inputStream.read(bArr)) != -1) {
                        byteArrayOutputStream.write(bArr, 0, i);
                    }
                    String str2 = new String(byteArrayOutputStream.toByteArray(), "utf-8");
                    if (httpURLConnectionA != null) {
                        httpURLConnectionA.disconnect();
                    }
                    return str2;
                } catch (a e) {
                    throw e;
                }
            } catch (Throwable th) {
                f3059a.e("VIP API failed! error: ", th);
                throw new a(com.alibaba.sdk.android.push.common.a.d.p.copy().msg(th.getMessage()).detail(Log.getStackTraceString(th)).build());
            }
        } catch (Throwable th2) {
            if (0 != 0) {
                httpURLConnection.disconnect();
            }
            throw th2;
        }
    }

    protected abstract Map<String, String> a(Context context, Map<String, String> map);

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.os.AsyncTask
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void onPostExecute(b bVar) {
        f3059a.i("HTTP Return code: " + bVar.f3056b);
    }
}
