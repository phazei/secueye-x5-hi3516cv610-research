package com.aliyun.alink.sdk.jsbridge;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class BoneBridgeNative {
    public static final String ERROR_CODE_GATEWAY = "502";
    public static final String ERROR_LOCALIZED_MESSAGE_GATEWAY = "BoneBridge 内部执行错误";
    public static final String ERROR_MESSAGE_GATEWAY = "bone bridge invoke error";
    public static final String ERROR_SUB_CODE_EXCEPTION = "502";
    public static final String ERROR_SUB_CODE_GATEWAY_NOT_MATCH_ARGUMENT_NUMBER = "405";
    public static final String ERROR_SUB_CODE_GATEWAY_NO_HANDLER = "404";
    public static final String ERROR_SUB_LOCALIZED_MESSAGE_GATEWAY_EXCEPTION = "执行时异常";
    public static final String ERROR_SUB_LOCALIZED_MESSAGE_GATEWAY_NO_HANDLER = "未能找到处理类";
    public static final String ERROR_SUB_LOCALIZED_MESSAGE_GATEWAY_NO_MATCH_ARGUMENT_NUMBER = "参数类型或者参数数量与接口不符";
    public static final String ERROR_SUB_LOCALIZED_MESSAGE_GATEWAY_NO_METHOD = "未能找到处理方法";
    public static final String ERROR_SUB_MESSAGE_GATEWAY_EXCEPTION = "runtime exception";
    public static final String ERROR_SUB_MESSAGE_GATEWAY_NO_HANDLER = "no handler";
    public static final String ERROR_SUB_MESSAGE_GATEWAY_NO_MATCH_ARGUMENT_NUMBER = "argument not match input of method";
    public static final String ERROR_SUB_MESSAGE_GATEWAY_NO_METHOD = "no method";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Context f4492c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private IJSBridge f4493d;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f4490a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f4491b = false;
    private Map<String, IBonePlugin> e = new HashMap();
    private a f = new a();

    public BoneBridgeNative(Context context, IJSBridge iJSBridge) {
        this.f4492c = context;
        this.f4493d = iJSBridge;
        for (Map.Entry<String, Class<? extends IBonePlugin>> entry : BonePluginRegistry.f4495a.entrySet()) {
            String key = entry.getKey();
            if (entry.getValue().isAnnotationPresent(Preload.class)) {
                IBonePlugin iBonePluginFindAPlugin = BonePluginRegistry.findAPlugin(key);
                this.f.a(iBonePluginFindAPlugin, context, iJSBridge);
                this.e.put(key, iBonePluginFindAPlugin);
            }
        }
    }

    public void call(String str, String str2, Object[] objArr, BoneCallback boneCallback) {
        JSONObject jSONObject = new JSONObject();
        IBonePlugin iBonePluginFindAPlugin = this.e.containsKey(str) ? this.e.get(str) : null;
        if (iBonePluginFindAPlugin == null) {
            iBonePluginFindAPlugin = BonePluginRegistry.findAPlugin(str);
        }
        boolean zCall = false;
        if (iBonePluginFindAPlugin != null) {
            if (!this.e.containsKey(str)) {
                this.f.a(iBonePluginFindAPlugin, this.f4492c, this.f4493d);
                if (this.f4490a) {
                    this.f.a(iBonePluginFindAPlugin);
                }
                this.e.put(str, iBonePluginFindAPlugin);
            }
            try {
                zCall = iBonePluginFindAPlugin.call(str2, objArr, boneCallback);
                if (!zCall) {
                    try {
                        jSONObject.put("code", "404");
                        jSONObject.put("message", ERROR_SUB_MESSAGE_GATEWAY_NO_METHOD);
                        jSONObject.put(AlinkConstants.KEY_LOCALIZED_MSG, ERROR_SUB_LOCALIZED_MESSAGE_GATEWAY_NO_METHOD);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IllegalAccessException unused) {
            } catch (IllegalArgumentException e2) {
                try {
                    jSONObject.put("code", ERROR_SUB_CODE_GATEWAY_NOT_MATCH_ARGUMENT_NUMBER);
                    jSONObject.put("message", ERROR_SUB_MESSAGE_GATEWAY_NO_MATCH_ARGUMENT_NUMBER);
                    jSONObject.put(AlinkConstants.KEY_LOCALIZED_MSG, ERROR_SUB_LOCALIZED_MESSAGE_GATEWAY_NO_MATCH_ARGUMENT_NUMBER);
                    jSONObject.put("exception", e2.getMessage());
                } catch (JSONException e3) {
                    e3.printStackTrace();
                }
            } catch (InvocationTargetException e4) {
                try {
                    jSONObject.put("code", "502");
                    jSONObject.put("message", ERROR_SUB_MESSAGE_GATEWAY_EXCEPTION);
                    jSONObject.put(AlinkConstants.KEY_LOCALIZED_MSG, ERROR_SUB_LOCALIZED_MESSAGE_GATEWAY_EXCEPTION);
                    jSONObject.put("exception", e4.getMessage());
                } catch (JSONException e5) {
                    e5.printStackTrace();
                }
            } catch (Throwable th) {
                try {
                    jSONObject.put("code", "502");
                    jSONObject.put("message", ERROR_SUB_MESSAGE_GATEWAY_EXCEPTION);
                    jSONObject.put(AlinkConstants.KEY_LOCALIZED_MSG, ERROR_SUB_LOCALIZED_MESSAGE_GATEWAY_EXCEPTION);
                    jSONObject.put("exception", th.getMessage());
                } catch (JSONException e6) {
                    e6.printStackTrace();
                }
            }
        } else {
            try {
                jSONObject.put("code", "404");
                jSONObject.put("message", ERROR_SUB_MESSAGE_GATEWAY_NO_HANDLER);
                jSONObject.put(AlinkConstants.KEY_LOCALIZED_MSG, ERROR_SUB_LOCALIZED_MESSAGE_GATEWAY_NO_HANDLER);
            } catch (JSONException e7) {
                e7.printStackTrace();
            }
            Log.e("BoneBridgeNative", "can not find handler for " + str + "." + str2);
        }
        if (zCall) {
            return;
        }
        boneCallback.failed("502", ERROR_MESSAGE_GATEWAY, ERROR_LOCALIZED_MESSAGE_GATEWAY, jSONObject);
    }

    public void dispatchOnActivityResult(int i, int i2, Intent intent) {
        Iterator<Map.Entry<String, IBonePlugin>> it = this.e.entrySet().iterator();
        while (it.hasNext()) {
            IBonePlugin value = it.next().getValue();
            if (value != null) {
                this.f.a(value, i, i2, intent);
            }
        }
    }

    public void dispatchOnResume() {
        this.f4490a = true;
        this.f4491b = false;
        Iterator<Map.Entry<String, IBonePlugin>> it = this.e.entrySet().iterator();
        while (it.hasNext()) {
            IBonePlugin value = it.next().getValue();
            if (value != null) {
                this.f.a(value);
            }
        }
    }

    public void dispatchOnPause() {
        this.f4490a = false;
        Iterator<Map.Entry<String, IBonePlugin>> it = this.e.entrySet().iterator();
        while (it.hasNext()) {
            IBonePlugin value = it.next().getValue();
            if (value != null) {
                this.f.b(value);
            }
        }
    }

    public void dispatchOnDestroy() {
        this.f4491b = true;
        Iterator<Map.Entry<String, IBonePlugin>> it = this.e.entrySet().iterator();
        while (it.hasNext()) {
            IBonePlugin value = it.next().getValue();
            if (value != null) {
                this.f.c(value);
            }
        }
    }

    public void destroy() {
        if (this.f4490a) {
            dispatchOnPause();
        }
        if (!this.f4491b) {
            dispatchOnDestroy();
        }
        Iterator<Map.Entry<String, IBonePlugin>> it = this.e.entrySet().iterator();
        while (it.hasNext()) {
            IBonePlugin value = it.next().getValue();
            if (value != null) {
                this.f.d(value);
            }
        }
        this.e.clear();
        this.f4492c = null;
        this.f4493d = null;
    }

    class a {
        private a() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(IBonePlugin iBonePlugin, Context context, IJSBridge iJSBridge) {
            try {
                iBonePlugin.onInitialize(context, iJSBridge);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(IBonePlugin iBonePlugin, int i, int i2, Intent intent) {
            try {
                iBonePlugin.onActivityResult(i, i2, intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(IBonePlugin iBonePlugin) {
            try {
                iBonePlugin.onResume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void b(IBonePlugin iBonePlugin) {
            try {
                iBonePlugin.onPause();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void c(IBonePlugin iBonePlugin) {
            try {
                iBonePlugin.onDestroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void d(IBonePlugin iBonePlugin) {
            try {
                iBonePlugin.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
