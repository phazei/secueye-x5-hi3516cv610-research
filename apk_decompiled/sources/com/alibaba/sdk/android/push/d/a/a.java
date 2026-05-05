package com.alibaba.sdk.android.push.d.a;

import android.content.Context;
import android.content.SharedPreferences;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.push.common.util.AppInfoUtil;
import com.alibaba.sdk.android.push.common.util.a.d;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.aliyun.alink.linksdk.securesigner.SecurityImpl;
import com.aliyun.alink.linksdk.securesigner.SecuritySourceContext;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import com.heytap.mcssdk.constant.IntentConstant;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class a implements com.alibaba.sdk.android.ams.common.b.b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AmsLogger f3067a = AmsLogger.getLogger("MPS:AliPushSecurityBoxService");

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f3068c = "seed_key";
    private Object g;
    private Object h;
    private Object i;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private String f3070d = null;
    private String e = null;
    private String f = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private SharedPreferences f3069b = com.alibaba.sdk.android.ams.common.a.a.k();

    public a() {
        if (Utils.hasSecurityGuardDep()) {
            try {
                Context contextB = com.alibaba.sdk.android.ams.common.a.a.b();
                SecurityGuardManager.getInitializer().initialize(contextB);
                SecurityGuardManager securityGuardManager = SecurityGuardManager.getInstance(contextB);
                this.g = securityGuardManager.getSecureSignatureComp();
                this.h = securityGuardManager.getStaticDataStoreComp();
                this.i = securityGuardManager.getStaticKeyEncryptComp();
            } catch (SecException e) {
                throw new RuntimeException("SecurityGuardManager init failed!", e);
            }
        }
    }

    private static int a(Context context, String str) {
        return context.getResources().getIdentifier(str, "string", context.getPackageName());
    }

    private String a(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        ArrayList<String> arrayList = new ArrayList(map.keySet());
        Collections.sort(arrayList);
        for (String str : arrayList) {
            sb.append(str);
            sb.append(map.get(str));
        }
        return sb.toString();
    }

    private static String b(Context context, String str) {
        try {
            return context.getResources().getString(a(context, str));
        } catch (Exception unused) {
            return null;
        }
    }

    private String f() {
        return "mps_deviceId_" + a();
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0080  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0083 A[RETURN] */
    @Override // com.alibaba.sdk.android.ams.common.b.b
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String a() {
        /*
            r7 = this;
            boolean r0 = com.aliyun.alink.linksdk.securesigner.util.Utils.hasSecurityGuardDep()
            if (r0 == 0) goto L86
            java.lang.String r0 = r7.f3070d
            boolean r0 = com.alibaba.sdk.android.ams.common.util.StringUtil.isEmpty(r0)
            if (r0 != 0) goto L11
            java.lang.String r0 = r7.f3070d
            return r0
        L11:
            java.lang.Object r0 = r7.h     // Catch: java.lang.Exception -> L50
            java.lang.Class r0 = r0.getClass()     // Catch: java.lang.Exception -> L50
            java.lang.String r1 = "getAppKeyByIndex"
            r2 = 2
            java.lang.Class[] r3 = new java.lang.Class[r2]     // Catch: java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            java.lang.Class r4 = java.lang.Integer.TYPE     // Catch: java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            r5 = 0
            r3[r5] = r4     // Catch: java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            java.lang.Class<java.lang.String> r4 = java.lang.String.class
            r6 = 1
            r3[r6] = r4     // Catch: java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            java.lang.reflect.Method r0 = r0.getMethod(r1, r3)     // Catch: java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            java.lang.Object r1 = r7.h     // Catch: java.lang.reflect.InvocationTargetException -> L41 java.lang.IllegalAccessException -> L46 java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            java.lang.Object[] r2 = new java.lang.Object[r2]     // Catch: java.lang.reflect.InvocationTargetException -> L41 java.lang.IllegalAccessException -> L46 java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            java.lang.Integer r3 = java.lang.Integer.valueOf(r5)     // Catch: java.lang.reflect.InvocationTargetException -> L41 java.lang.IllegalAccessException -> L46 java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            r2[r5] = r3     // Catch: java.lang.reflect.InvocationTargetException -> L41 java.lang.IllegalAccessException -> L46 java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            java.lang.String r3 = com.alibaba.sdk.android.ams.common.a.a.c()     // Catch: java.lang.reflect.InvocationTargetException -> L41 java.lang.IllegalAccessException -> L46 java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            r2[r6] = r3     // Catch: java.lang.reflect.InvocationTargetException -> L41 java.lang.IllegalAccessException -> L46 java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            java.lang.Object r0 = r0.invoke(r1, r2)     // Catch: java.lang.reflect.InvocationTargetException -> L41 java.lang.IllegalAccessException -> L46 java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            java.lang.String r0 = (java.lang.String) r0     // Catch: java.lang.reflect.InvocationTargetException -> L41 java.lang.IllegalAccessException -> L46 java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            return r0
        L41:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            goto L70
        L46:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.NoSuchMethodException -> L4b java.lang.Exception -> L50
            goto L70
        L4b:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Exception -> L50
            goto L70
        L50:
            r0 = move-exception
            r0.printStackTrace()
            com.alibaba.sdk.android.ams.common.logger.AmsLogger r0 = com.alibaba.sdk.android.ams.common.logger.AmsLogger.getImportantLogger()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "authcode:"
            r1.append(r2)
            java.lang.String r2 = com.alibaba.sdk.android.ams.common.a.a.c()
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            r0.i(r1)
        L70:
            java.lang.String r0 = "com.alibaba.app.appkey"
            java.lang.String r0 = com.alibaba.sdk.android.ams.common.a.a.a(r0)
            r7.f3070d = r0
            java.lang.String r0 = r7.f3070d
            boolean r0 = com.alibaba.sdk.android.ams.common.util.StringUtil.isEmpty(r0)
            if (r0 != 0) goto L83
            java.lang.String r0 = r7.f3070d
            return r0
        L83:
            java.lang.String r0 = ""
            return r0
        L86:
            com.aliyun.alink.linksdk.securesigner.SecurityImpl r0 = new com.aliyun.alink.linksdk.securesigner.SecurityImpl
            r0.<init>()
            java.lang.String r0 = r0.getAppKey()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.push.d.a.a.a():java.lang.String");
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public String a(String str) {
        byte[] bArrA = com.alibaba.sdk.android.ams.common.util.a.a();
        if (Utils.hasSecurityGuardDep()) {
            try {
                try {
                    try {
                        this.i.getClass().getMethod("saveSecret", String.class, byte[].class).invoke(this.i, str, bArrA);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e2) {
                        e2.printStackTrace();
                    }
                } catch (NoSuchMethodException e3) {
                    e3.printStackTrace();
                }
            } catch (Exception e4) {
                throw new IllegalStateException(e4.getMessage() + ", ErrorCode:", e4);
            }
        }
        return com.alibaba.sdk.android.ams.common.util.a.a(bArrA);
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public String a(Map<String, String> map, String str) {
        String strD;
        StringBuilder sb = new StringBuilder();
        ArrayList<String> arrayList = new ArrayList(map.keySet());
        arrayList.add(IntentConstant.APP_SECRET);
        Collections.sort(arrayList);
        for (String str2 : arrayList) {
            if (!str2.equals(d.u)) {
                if (IntentConstant.APP_SECRET.equals(str2)) {
                    sb.append(str2);
                    strD = d();
                } else {
                    sb.append(str2);
                    strD = map.get(str2);
                }
                sb.append(strD);
            }
        }
        if (map.containsKey(d.u)) {
            int i = Integer.parseInt(map.get(d.u));
            map.remove(d.u);
            if (i > d.b()) {
                return com.alibaba.sdk.android.ams.common.util.c.a().a(sb.toString());
            }
        }
        return com.alibaba.sdk.android.ams.common.util.c.a().b(sb.toString());
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public String a(Map<String, String> map, String str, String str2) {
        if (!Utils.hasSecurityGuardDep()) {
            HashMap map2 = new HashMap();
            map2.put("INPUT", a(map));
            if (str == null) {
                str = f3068c;
            }
            map2.put("SEEDKEY", str);
            return new SecurityImpl().sign(String.valueOf(map2), "MD5");
        }
        HashMap map3 = new HashMap();
        map3.put("INPUT", a(map));
        map3.put("SEEDKEY", str == null ? f3068c : str);
        Method method = null;
        try {
            method = this.i.getClass().getMethod("saveSecret", String.class, byte[].class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        try {
            Object obj = this.i;
            Object[] objArr = new Object[2];
            if (str == null) {
                str = f3068c;
            }
            objArr[0] = str;
            objArr[1] = com.alibaba.sdk.android.ams.common.util.a.a(str2);
            method.invoke(obj, objArr);
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
        SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
        securityGuardParamContext.appKey = a();
        securityGuardParamContext.paramMap = map3;
        securityGuardParamContext.requestType = 0;
        try {
            try {
                return this.g.getClass().getMethod("signRequest", SecurityGuardParamContext.class, String.class).invoke(this.g, securityGuardParamContext, com.alibaba.sdk.android.ams.common.a.a.c()).toString();
            } catch (IllegalAccessException e4) {
                e4.printStackTrace();
                return "";
            } catch (InvocationTargetException e5) {
                e5.printStackTrace();
                return "";
            }
        } catch (NoSuchMethodException e6) {
            e6.printStackTrace();
            return "";
        }
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public void a(String str, String str2) {
        this.f3069b.edit().putString(str, str2).commit();
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public String b() {
        String str = this.f;
        if (str != null) {
            return str;
        }
        String string = this.f3069b.getString(f(), "");
        if (System.currentTimeMillis() - this.f3069b.getLong("mps_device_store_time", 0L) > 604800000) {
            return "";
        }
        this.f = string;
        return string;
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public void b(String str) {
        this.f = str;
        this.f3069b.edit().putString(f(), str).putLong("mps_device_store_time", System.currentTimeMillis()).commit();
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public String c() {
        return this.f3069b.getString("mps_utdid", "");
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public void c(String str) {
        this.f3069b.edit().putString("mps_utdid", str).commit();
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public String d() {
        if (!SecuritySourceContext.getInstance().getAppSecretKey().isEmpty()) {
            return SecuritySourceContext.getInstance().getAppSecretKey();
        }
        if (!StringUtil.isEmpty(this.e)) {
            return this.e;
        }
        this.e = com.alibaba.sdk.android.ams.common.a.a.a(OpenAccountConstants.APP_SECRET);
        if (!StringUtil.isEmpty(this.e)) {
            return this.e;
        }
        this.e = b(com.alibaba.sdk.android.ams.common.a.a.b(), "ams_appSecret");
        return this.e;
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public String d(String str) {
        return this.f3069b.getString(str, "");
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public String e() {
        return AppInfoUtil.getAppVersionName(com.alibaba.sdk.android.ams.common.a.a.b());
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public void e(String str) {
        this.f3070d = str;
    }

    @Override // com.alibaba.sdk.android.ams.common.b.b
    public void f(String str) {
        this.e = str;
    }
}
