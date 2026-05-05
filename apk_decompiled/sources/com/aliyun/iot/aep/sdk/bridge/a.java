package com.aliyun.iot.aep.sdk.bridge;

import android.text.TextUtils;
import android.util.Log;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCall;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneMethod;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: MethodInvokerHelper.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    Map<String, C0241a> f4599a = new HashMap(5);

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    boolean f4600b = true;

    public void a(Object obj) {
        try {
            for (Method method : obj.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(BoneMethod.class)) {
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                    if (Arrays.binarySearch(method.getParameterTypes(), BoneCallback.class) < 0) {
                        ALog.e("APluginRegistry", String.format(Locale.ENGLISH, "%s must have  parameter BoneCallback", method));
                    } else {
                        BoneMethod boneMethod = (BoneMethod) method.getAnnotation(BoneMethod.class);
                        if (TextUtils.isEmpty(boneMethod.name())) {
                            this.f4599a.put(method.getName().toLowerCase(), new C0241a(method));
                        } else {
                            this.f4599a.put(boneMethod.name().toLowerCase(), new C0241a(method));
                        }
                    }
                }
            }
        } catch (Exception unused) {
        }
        this.f4600b = true;
    }

    public void a() {
        this.f4599a.clear();
    }

    public boolean a(Object obj, JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) throws Exception {
        if (!this.f4600b) {
            return false;
        }
        String lowerCase = boneCall.methodName.toLowerCase();
        if (!this.f4599a.containsKey(lowerCase)) {
            boneCallback.failed("608", "method not found", "方法未实现");
            return false;
        }
        this.f4599a.get(lowerCase).a(obj, jSContext, boneCall, boneCallback);
        return true;
    }

    /* JADX INFO: renamed from: com.aliyun.iot.aep.sdk.bridge.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: MethodInvokerHelper.java */
    static class C0241a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        Method f4601a;

        public C0241a(Method method) {
            this.f4601a = method;
        }

        public void a(Object obj, JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) throws Exception {
            Class<?>[] parameterTypes = this.f4601a.getParameterTypes();
            Object[] objArr = new Object[parameterTypes.length];
            int i = -1;
            int i2 = 0;
            while (i2 < parameterTypes.length) {
                Class<?> cls = parameterTypes[i2];
                if (!a(parameterTypes[i2])) {
                    i++;
                }
                int i3 = i;
                objArr[i2] = a(cls, i3, jSContext, boneCall, boneCallback);
                i2++;
                i = i3;
            }
            try {
                this.f4601a.invoke(obj, objArr);
            } catch (Exception e) {
                Log.e("APluginRegistry", String.format(Locale.ENGLISH, "can not invoke method:object=%s,method=%s, parameters=%s", obj, this.f4601a, boneCall.args));
                boneCallback.failed("608", "can not invoke method:" + this.f4601a.getName(), "方法执行出错");
                e.printStackTrace();
                throw e;
            }
        }

        private boolean a(Class cls) {
            return cls == JSContext.class || cls == BoneCall.class || cls == BoneCallback.class;
        }

        private Object a(Class cls, int i, JSContext jSContext, BoneCall boneCall, BoneCallback boneCallback) {
            if (cls == JSContext.class) {
                return jSContext;
            }
            if (cls == BoneCall.class) {
                return boneCall;
            }
            if (cls == BoneCallback.class) {
                return boneCallback;
            }
            if (cls == Boolean.class || cls == Boolean.TYPE) {
                return Boolean.valueOf(boneCall.args.optBoolean(i));
            }
            if (cls == Long.class || cls == Long.TYPE) {
                return Long.valueOf(boneCall.args.optLong(i));
            }
            if (cls == Double.class || cls == Double.TYPE || cls == Float.TYPE || cls == Float.class) {
                return Double.valueOf(boneCall.args.optDouble(i));
            }
            if (cls == Integer.class || cls == Integer.TYPE || cls == Short.class || cls == Short.TYPE) {
                return Integer.valueOf(boneCall.args.optInt(i));
            }
            if (cls == String.class) {
                return boneCall.args.optString(i);
            }
            if (cls == JSONObject.class) {
                return boneCall.args.optJSONObject(i);
            }
            if (cls == JSONArray.class) {
                return boneCall.args.optJSONArray(i);
            }
            throw new IllegalArgumentException(String.format("method %s not support parameter %s", this.f4601a.getName(), cls.getCanonicalName()));
        }
    }
}
