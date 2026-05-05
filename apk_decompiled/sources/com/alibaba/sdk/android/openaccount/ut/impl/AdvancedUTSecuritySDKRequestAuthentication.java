package com.alibaba.sdk.android.openaccount.ut.impl;

import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AdvancedUTSecuritySDKRequestAuthentication implements IUTRequestAuthentication {
    private static final String TAG = "ut";
    private String appKey;
    private Context context;
    private boolean initialized;
    private Object securityGuardManager = null;
    private Object securitySignatureComponent = null;
    private Class<?> securityGuardParamContextClazz = null;
    private Field contextAppKeyField = null;
    private Field contextParamMapField = null;
    private Field contextRequestTypeField = null;
    private Method signRequestMethod = null;

    public String getAppkey() {
        return this.appKey;
    }

    public AdvancedUTSecuritySDKRequestAuthentication(String str, Context context) {
        this.appKey = null;
        this.appKey = str;
        this.context = context;
        init();
    }

    private void init() {
        try {
            Class<?> cls = Class.forName("com.alibaba.wireless.security.open.SecurityGuardManager");
            this.securityGuardManager = cls.getMethod("getInstance", Context.class).invoke(null, this.context);
            this.securitySignatureComponent = cls.getMethod("getSecureSignatureComp", new Class[0]).invoke(this.securityGuardManager, new Object[0]);
            try {
                this.securityGuardParamContextClazz = Class.forName("com.alibaba.wireless.security.open.SecurityGuardParamContext");
                this.contextAppKeyField = this.securityGuardParamContextClazz.getDeclaredField("appKey");
                this.contextParamMapField = this.securityGuardParamContextClazz.getDeclaredField("paramMap");
                this.contextRequestTypeField = this.securityGuardParamContextClazz.getDeclaredField("requestType");
                this.signRequestMethod = Class.forName("com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent").getMethod("signRequest", this.securityGuardParamContextClazz, String.class);
                this.initialized = true;
            } catch (Throwable th) {
                Log.e(TAG, "Fail to init UT, the error message is " + th.getMessage());
                th.printStackTrace();
            }
        } catch (Throwable th2) {
            Log.e(TAG, "Fail to load security signature component", th2);
        }
    }

    public String getSign(String str) {
        if (this.appKey == null) {
            Log.e(TAG, "UTSecuritySDKRequestAuthentication:getSign, There is no appkey,please check it!");
            return null;
        }
        if (str == null || !this.initialized) {
            return null;
        }
        try {
            Object objNewInstance = this.securityGuardParamContextClazz.newInstance();
            this.contextAppKeyField.set(objNewInstance, this.appKey);
            ((Map) this.contextParamMapField.get(objNewInstance)).put("INPUT", str);
            this.contextRequestTypeField.set(objNewInstance, 1);
            return (String) this.signRequestMethod.invoke(this.securitySignatureComponent, objNewInstance, ConfigManager.getInstance().getSecurityImagePostfix());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
