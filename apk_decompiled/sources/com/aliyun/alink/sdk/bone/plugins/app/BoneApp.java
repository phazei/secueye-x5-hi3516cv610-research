package com.aliyun.alink.sdk.bone.plugins.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.aliyun.iot.aep.sdk.bridge.base.BaseBoneService;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneMethod;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneService;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.ServiceMode;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
@BoneService(mode = ServiceMode.ALWAYS_NEW, name = BoneApp.API_NAME)
public class BoneApp extends BaseBoneService {
    public static final String API_NAME = "BoneApp";

    @BoneMethod
    public void reload(JSContext jSContext, BoneCallback boneCallback) {
        if (jSContext == null) {
            boneCallback.failed("608", "FAILED_NO_INITIALIZED", "");
        } else {
            jSContext.reload();
            boneCallback.success(new JSONObject());
        }
    }

    @BoneMethod
    public void exit(JSContext jSContext, JSONObject jSONObject, BoneCallback boneCallback) {
        if (jSContext == null || jSContext.getCurrentActivity() == null) {
            boneCallback.failed("608", "FAILED_NO_INITIALIZED", "");
            return;
        }
        Activity currentActivity = jSContext.getCurrentActivity();
        if (jSONObject != null) {
            Bundle bundle = new Bundle();
            try {
                bundle = ConvertUtils.toBundle(jSONObject);
            } catch (JSONException e) {
                Log.e(API_NAME, "exception happens in exitWithResult:");
                e.printStackTrace();
            }
            if (bundle != null) {
                Intent intent = new Intent();
                intent.putExtras(bundle);
                currentActivity.setResult(-1, intent);
            }
        }
        currentActivity.finish();
        boneCallback.success(new JSONObject());
    }
}
