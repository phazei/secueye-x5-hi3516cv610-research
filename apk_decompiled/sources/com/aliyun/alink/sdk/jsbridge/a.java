package com.aliyun.alink.sdk.jsbridge;

import android.text.TextUtils;
import android.util.Log;
import com.aliyun.alink.sdk.jsbridge.methodexport.MethodExported;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* JADX INFO: compiled from: MethodInvokerHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Map<String, C0238a> f4496a = new HashMap(5);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    boolean f4497b = true;

    public void a(Object obj) {
        try {
            for (Method method : obj.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(MethodExported.class)) {
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes[parameterTypes.length - 1] == BoneCallback.class) {
                        MethodExported methodExported = (MethodExported) method.getAnnotation(MethodExported.class);
                        if (TextUtils.isEmpty(methodExported.name())) {
                            this.f4496a.put(method.getName(), new C0238a(method));
                        } else {
                            this.f4496a.put(methodExported.name(), new C0238a(method));
                        }
                    }
                }
            }
        } catch (Exception unused) {
        }
        this.f4497b = true;
    }

    public void a() {
        this.f4496a.clear();
    }

    public boolean a(Object obj, String str, Object[] objArr, BoneCallback boneCallback) throws IllegalAccessException, InvocationTargetException {
        if (!this.f4497b || !this.f4496a.containsKey(str)) {
            return false;
        }
        this.f4496a.get(str).a(obj, objArr, boneCallback);
        return true;
    }

    /* JADX INFO: renamed from: com.aliyun.alink.sdk.jsbridge.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: MethodInvokerHelper.java */
    static class C0238a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        Method f4498a;

        public C0238a(Method method) {
            this.f4498a = method;
        }

        public void a(Object obj, Object[] objArr, BoneCallback boneCallback) throws IllegalAccessException, InvocationTargetException {
            ArrayList arrayList = new ArrayList(objArr.length + 1);
            for (Object obj2 : objArr) {
                arrayList.add(obj2);
            }
            arrayList.add(boneCallback);
            try {
                this.f4498a.invoke(obj, arrayList.toArray());
            } catch (IllegalAccessException e) {
                Log.e("APluginRegistry", String.format(Locale.ENGLISH, "can not invoke method:object=%s,method=%s, parameters=%s", obj, this.f4498a, objArr));
                e.printStackTrace();
                throw e;
            } catch (InvocationTargetException e2) {
                Log.e("APluginRegistry", String.format(Locale.ENGLISH, "can not invoke method:object=%s,method=%s, parameters=%s", obj, this.f4498a, objArr));
                e2.printStackTrace();
                throw e2;
            } catch (Throwable th) {
                Log.e("APluginRegistry", String.format(Locale.ENGLISH, "can not invoke method:object=%s,method=%s, parameters=%s", obj, this.f4498a, objArr));
                th.printStackTrace();
                throw th;
            }
        }
    }
}
