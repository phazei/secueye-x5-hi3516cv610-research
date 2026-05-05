package com.alibaba.sdk.android.beacon;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.sdk.android.beacon.Beacon;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.huawei.hms.framework.common.ContainerUtils;
import com.ta.utdid2.device.UTDevice;
import com.taobao.accs.common.Constants;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
final class c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f2846a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f2847b;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final Beacon f3a;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final List<Beacon.Config> f2848c = new ArrayList();

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private final a f4a = new a();

    private final class a {
        private a() {
        }

        /* JADX WARN: Removed duplicated region for block: B:48:0x00bd A[Catch: IOException -> 0x00c0, TRY_LEAVE, TryCatch #6 {IOException -> 0x00c0, blocks: (B:46:0x00b8, B:48:0x00bd), top: B:59:0x00b8 }] */
        /* JADX WARN: Removed duplicated region for block: B:59:0x00b8 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        java.lang.String a(java.lang.String r7, byte[] r8) throws java.lang.Throwable {
            /*
                r6 = this;
                r0 = 0
                java.net.URL r1 = new java.net.URL     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                r1.<init>(r7)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                java.net.URLConnection r7 = r1.openConnection()     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                java.net.HttpURLConnection r7 = (java.net.HttpURLConnection) r7     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                r1 = 10000(0x2710, float:1.4013E-41)
                r7.setReadTimeout(r1)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                r7.setConnectTimeout(r1)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                java.lang.String r1 = "POST"
                r7.setRequestMethod(r1)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                r1 = 1
                r7.setDoOutput(r1)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                r7.setDoInput(r1)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                r1 = 0
                r7.setUseCaches(r1)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                boolean r1 = com.alibaba.sdk.android.beacon.a.f2844a     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                if (r1 == 0) goto L2f
                java.lang.String r1 = "Host"
                java.lang.String r2 = "beacon-api.aliyuncs.com"
                r7.setRequestProperty(r1, r2)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
            L2f:
                java.io.OutputStream r1 = r7.getOutputStream()     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L92
                r1.write(r8)     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
                r1.flush()     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
                int r8 = r7.getResponseCode()     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
                boolean r2 = r6.a(r8)     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
                if (r2 == 0) goto L48
                java.io.InputStream r7 = r7.getInputStream()     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
                goto L4c
            L48:
                java.io.InputStream r7 = r7.getErrorStream()     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
            L4c:
                java.io.BufferedReader r3 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
                java.io.InputStreamReader r4 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
                java.lang.String r5 = "UTF-8"
                r4.<init>(r7, r5)     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
                r3.<init>(r4)     // Catch: java.lang.Throwable -> L87 java.lang.Exception -> L8b
                java.lang.StringBuilder r7 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L83 java.lang.Exception -> L85
                r7.<init>()     // Catch: java.lang.Throwable -> L83 java.lang.Exception -> L85
            L5d:
                java.lang.String r0 = r3.readLine()     // Catch: java.lang.Throwable -> L83 java.lang.Exception -> L85
                if (r0 == 0) goto L67
                r7.append(r0)     // Catch: java.lang.Throwable -> L83 java.lang.Exception -> L85
                goto L5d
            L67:
                if (r2 != 0) goto L76
                com.alibaba.sdk.android.beacon.c r0 = com.alibaba.sdk.android.beacon.c.this     // Catch: java.lang.Throwable -> L83 java.lang.Exception -> L85
                java.lang.String r8 = java.lang.String.valueOf(r8)     // Catch: java.lang.Throwable -> L83 java.lang.Exception -> L85
                java.lang.String r2 = r7.toString()     // Catch: java.lang.Throwable -> L83 java.lang.Exception -> L85
                com.alibaba.sdk.android.beacon.c.a(r0, r8, r2)     // Catch: java.lang.Throwable -> L83 java.lang.Exception -> L85
            L76:
                java.lang.String r7 = r7.toString()     // Catch: java.lang.Throwable -> L83 java.lang.Exception -> L85
                if (r1 == 0) goto L7f
                r1.close()     // Catch: java.io.IOException -> L82
            L7f:
                r3.close()     // Catch: java.io.IOException -> L82
            L82:
                return r7
            L83:
                r7 = move-exception
                goto L89
            L85:
                r7 = move-exception
                goto L8d
            L87:
                r7 = move-exception
                r3 = r0
            L89:
                r0 = r1
                goto Lb6
            L8b:
                r7 = move-exception
                r3 = r0
            L8d:
                r0 = r1
                goto L94
            L8f:
                r7 = move-exception
                r3 = r0
                goto Lb6
            L92:
                r7 = move-exception
                r3 = r0
            L94:
                java.lang.String r8 = "beacon"
                java.lang.String r1 = r7.getMessage()     // Catch: java.lang.Throwable -> Lb5
                com.alibaba.sdk.android.beacon.b.a(r8, r1, r7)     // Catch: java.lang.Throwable -> Lb5
                com.alibaba.sdk.android.beacon.c r8 = com.alibaba.sdk.android.beacon.c.this     // Catch: java.lang.Throwable -> Lb5
                java.lang.String r1 = "-100"
                java.lang.String r7 = r7.getMessage()     // Catch: java.lang.Throwable -> Lb5
                com.alibaba.sdk.android.beacon.c.a(r8, r1, r7)     // Catch: java.lang.Throwable -> Lb5
                if (r0 == 0) goto Lad
                r0.close()     // Catch: java.io.IOException -> Lb2
            Lad:
                if (r3 == 0) goto Lb2
                r3.close()     // Catch: java.io.IOException -> Lb2
            Lb2:
                java.lang.String r7 = ""
                return r7
            Lb5:
                r7 = move-exception
            Lb6:
                if (r0 == 0) goto Lbb
                r0.close()     // Catch: java.io.IOException -> Lc0
            Lbb:
                if (r3 == 0) goto Lc0
                r3.close()     // Catch: java.io.IOException -> Lc0
            Lc0:
                throw r7
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.beacon.c.a.a(java.lang.String, byte[]):java.lang.String");
        }

        boolean a(int i) {
            return i >= 200 && i < 300;
        }
    }

    private static final class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final Map<String, String> f2850a;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        final String f2851c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        final String f2852d;
        final String e;
        final String f;
        final String g;
        final String h;
        final String i;
        final String mAppKey;
        final Map<String, String> mExtras;

        static final class a {

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            Map<String, String> f2853b = new HashMap();
            String j;
            String k;
            String l;
            String m;
            String n;
            String o;
            String p;

            a() {
            }

            a a(String str) {
                this.j = str;
                return this;
            }

            a a(Map<String, String> map) {
                this.f2853b.putAll(map);
                return this;
            }

            public b a() {
                return new b(this);
            }

            a b(String str) {
                this.k = str;
                return this;
            }

            a c(String str) {
                this.l = str;
                return this;
            }

            a d(String str) {
                this.m = str;
                return this;
            }

            a e(String str) {
                this.n = str;
                return this;
            }

            a f(String str) {
                this.o = str;
                return this;
            }

            a g(String str) {
                this.p = str;
                return this;
            }
        }

        private b(a aVar) {
            this.f2850a = new TreeMap();
            this.mAppKey = aVar.j;
            this.f2851c = aVar.k;
            this.f2852d = aVar.l;
            this.e = aVar.m;
            this.f = aVar.n;
            this.g = aVar.o;
            this.h = aVar.p;
            this.mExtras = aVar.f2853b;
            this.i = a();
        }

        private String a() {
            this.f2850a.put("appKey", this.mAppKey);
            this.f2850a.put("appVer", this.f2852d);
            this.f2850a.put(Constants.KEY_OS_TYPE, this.e);
            this.f2850a.put("osVer", this.f);
            this.f2850a.put("deviceId", this.g);
            this.f2850a.put("beaconVer", this.h);
            for (String str : this.mExtras.keySet()) {
                this.f2850a.put(str, this.mExtras.get(str));
            }
            StringBuilder sb = new StringBuilder();
            for (String str2 : this.f2850a.keySet()) {
                sb.append(str2);
                sb.append(this.f2850a.get(str2));
            }
            String strA = d.a(this.f2851c, sb.toString());
            this.f2850a.put("sign", strA);
            return strA;
        }
    }

    static {
        f2846a = com.alibaba.sdk.android.beacon.a.f2844a ? "100.67.64.54" : "beacon-api.aliyuncs.com";
        f2847b = "https://" + f2846a + "/beacon/fetch/config";
    }

    c(Beacon beacon) {
        this.f3a = beacon;
    }

    private b a(Context context, String str, String str2, Map<String, String> map) {
        return new b.a().a(str).b(str2).c(d.a(context)).d("Android").e(String.valueOf(Build.VERSION.SDK_INT)).f(UTDevice.getUtdid(context)).g(AlinkConstants.HTTP_PATH_GET_CIPHER_VERSION).a(map).a();
    }

    private String a(b bVar) {
        Map<String, String> map = bVar.f2850a;
        StringBuilder sb = new StringBuilder();
        for (String str : map.keySet()) {
            sb.append(encode(str));
            sb.append(ContainerUtils.KEY_VALUE_DELIMITER);
            sb.append(encode(map.get(str)));
            sb.append("&");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    private void a(String str) {
        b(str);
    }

    private void b(String str) {
        JSONArray jSONArrayOptJSONArray;
        try {
            if (TextUtils.isEmpty(str) || (jSONArrayOptJSONArray = new JSONObject(str).optJSONArray("result")) == null || jSONArrayOptJSONArray.length() <= 0) {
                return;
            }
            this.f2848c.clear();
            for (int i = 0; i < jSONArrayOptJSONArray.length(); i++) {
                JSONObject jSONObject = (JSONObject) jSONArrayOptJSONArray.get(i);
                this.f2848c.add(new Beacon.Config(jSONObject.optString("key"), jSONObject.optString("value")));
            }
        } catch (Exception unused) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(String str, String str2) {
        this.f3a.a(new Beacon.Error(str, str2));
    }

    private String encode(String str) {
        try {
            return URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    List<Beacon.Config> a() {
        return Collections.unmodifiableList(this.f2848c);
    }

    /* JADX INFO: renamed from: a, reason: collision with other method in class */
    void m12a(Context context, String str, String str2, Map<String, String> map) {
        b bVarA = a(context, str, str2, map);
        String str3 = f2847b + "/byappkey";
        com.alibaba.sdk.android.beacon.b.a("beacon", "url=" + str3);
        String strA = this.f4a.a(str3, a(bVarA).getBytes());
        com.alibaba.sdk.android.beacon.b.a("beacon", "[fetchByAppKey] result: " + strA);
        a(strA);
    }
}
