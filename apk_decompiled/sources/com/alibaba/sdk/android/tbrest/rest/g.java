package com.alibaba.sdk.android.tbrest.rest;

import android.content.Context;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/* JADX INFO: compiled from: RestSecuritySDKRequestAuthentication.java */
/* JADX INFO: loaded from: classes.dex */
public class g {

    /* JADX INFO: renamed from: b, reason: collision with other field name */
    private String f25b;
    private Context mContext;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private Object f23a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Object f3195b = null;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Class f3194a = null;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private Field f24a = null;

    /* JADX INFO: renamed from: b, reason: collision with other field name */
    private Field f26b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Field f3196c = null;

    /* JADX INFO: renamed from: b, reason: collision with other field name */
    private Method f27b = null;
    private int h = 1;
    private boolean e = false;

    public g(Context context, String str) {
        this.f25b = null;
        this.mContext = context;
        this.f25b = str;
    }

    private synchronized void g() {
        Class<?> cls;
        Method method;
        boolean zBooleanValue;
        if (this.e) {
            return;
        }
        Class<?> cls2 = null;
        try {
            cls = Class.forName("com.taobao.wireless.security.sdk.SecurityGuardManager");
        } catch (Throwable unused) {
            cls = null;
        }
        try {
            this.f23a = cls.getMethod("getInstance", Context.class).invoke(null, this.mContext);
            this.f3195b = cls.getMethod("getSecureSignatureComp", new Class[0]).invoke(this.f23a, new Object[0]);
        } catch (Throwable unused2) {
            LogUtil.i("initSecurityCheck failure, It's ok ");
        }
        if (cls != null) {
            try {
                this.f3194a = Class.forName("com.taobao.wireless.security.sdk.SecurityGuardParamContext");
                this.f24a = this.f3194a.getDeclaredField("appKey");
                this.f26b = this.f3194a.getDeclaredField("paramMap");
                this.f3196c = this.f3194a.getDeclaredField("requestType");
                try {
                    method = cls.getMethod("isOpen", new Class[0]);
                } catch (Throwable unused3) {
                    LogUtil.i("initSecurityCheck failure, It's ok");
                    method = null;
                }
                if (method != null) {
                    zBooleanValue = ((Boolean) method.invoke(this.f23a, new Object[0])).booleanValue();
                } else {
                    try {
                        cls2 = Class.forName("com.taobao.wireless.security.sdk.securitybody.ISecurityBodyComponent");
                    } catch (Throwable unused4) {
                        LogUtil.i("initSecurityCheck failure, It's ok");
                    }
                    zBooleanValue = cls2 == null;
                }
                this.h = zBooleanValue ? 1 : 12;
                this.f27b = Class.forName("com.taobao.wireless.security.sdk.securesignature.ISecureSignatureComponent").getMethod("signRequest", this.f3194a);
            } catch (Throwable unused5) {
                LogUtil.i("initSecurityCheck failure, It's ok");
            }
            this.e = true;
            return;
        }
        this.e = true;
        return;
    }

    public String b(String str) {
        Class cls;
        if (!this.e) {
            g();
        }
        if (this.f25b == null) {
            LogUtil.e("RestSecuritySDKRequestAuthentication:getSign There is no appkey,please check it!");
            return null;
        }
        if (str == null) {
            return null;
        }
        if (this.f23a != null && (cls = this.f3194a) != null && this.f24a != null && this.f26b != null && this.f3196c != null && this.f27b != null && this.f3195b != null) {
            try {
                Object objNewInstance = cls.newInstance();
                this.f24a.set(objNewInstance, this.f25b);
                ((Map) this.f26b.get(objNewInstance)).put("INPUT", str);
                this.f3196c.set(objNewInstance, Integer.valueOf(this.h));
                return (String) this.f27b.invoke(this.f3195b, objNewInstance);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e2) {
                e2.printStackTrace();
            } catch (InstantiationException e3) {
                e3.printStackTrace();
            } catch (InvocationTargetException e4) {
                e4.printStackTrace();
            }
        }
        return null;
    }
}
