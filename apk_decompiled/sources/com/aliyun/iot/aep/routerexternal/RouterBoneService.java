package com.aliyun.iot.aep.routerexternal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import anet.channel.util.HttpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.sdk.bone.plugins.app.ConvertUtils;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.sdk.bridge.base.BaseBoneService;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.context.OnActivityResultManager;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneFactory;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneMethod;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneService;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
@BoneFactory(name = "RouterServiceFactory", sdkName = "BoneRouterSDK", sdkVersion = "0.0.6")
@BoneService(group = "RouterServiceFactory", name = RouterBoneService.API_NAME)
public class RouterBoneService extends BaseBoneService {
    public static final String API_NAME = "BoneRouter";

    @BoneMethod
    public void open(final JSContext jSContext, String str, JSONObject jSONObject, JSONObject jSONObject2, BoneCallback boneCallback) {
        boolean url;
        ALog.d(API_NAME, "open() called with: jsContext = [" + jSContext + "], url = [" + str + "], params = [" + jSONObject + "], config = [" + jSONObject2 + "], boneCallback = [" + boneCallback + "]");
        Activity currentActivity = jSContext.getCurrentActivity();
        Bundle bundle = new Bundle();
        if (jSONObject != null) {
            try {
                bundle.putAll(ConvertUtils.toBundle(jSONObject));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        boolean zOptBoolean = false;
        if (jSONObject2 != null && jSONObject2.optBoolean("h5Panel", false) && toSupportOpenH5(str, currentActivity)) {
            ALog.d(API_NAME, "open http or https page");
            JSONObject jSONObject3 = new JSONObject();
            try {
                jSONObject3.put("result", true);
                boneCallback.success(jSONObject3);
                return;
            } catch (JSONException e2) {
                e2.printStackTrace();
                return;
            }
        }
        if (jSONObject2 != null) {
            zOptBoolean = jSONObject2.optBoolean("expectReceiveResult", false);
            Bundle bundle2 = null;
            try {
                bundle2 = ConvertUtils.toBundle(jSONObject2);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            bundle.putBundle("bone-mobile-config", bundle2);
        }
        if (zOptBoolean && (currentActivity instanceof Activity)) {
            url = Router.getInstance().toUrlForResult(currentActivity, str, 1001, bundle);
            jSContext.addOnActivityResultListener(new OnActivityResultManager.OnActivityResultListener() { // from class: com.aliyun.iot.aep.routerexternal.RouterBoneService.1
                @Override // com.aliyun.iot.aep.sdk.bridge.core.context.OnActivityResultManager.OnActivityResultListener
                public void onActivityResult(Activity activity2, int i, int i2, Intent intent) {
                    if (i == 1001) {
                        JSONObject jSONObjectFromBundle = null;
                        if (intent != null) {
                            try {
                                jSONObjectFromBundle = ConvertUtils.fromBundle(intent.getExtras());
                            } catch (Exception e4) {
                                e4.printStackTrace();
                            }
                        }
                        jSContext.emitter("BoneReceivedResult", jSONObjectFromBundle);
                    }
                    jSContext.removeOnActivityResultListener(this);
                }
            });
        } else {
            url = Router.getInstance().toUrl(currentActivity, str, bundle);
        }
        if (url) {
            JSONObject jSONObject4 = new JSONObject();
            try {
                jSONObject4.put("result", true);
                boneCallback.success(jSONObject4);
                return;
            } catch (JSONException e4) {
                e4.printStackTrace();
                return;
            }
        }
        boneCallback.failed("404", "url is validate", "");
    }

    private boolean toSupportOpenH5(String str, Context context) {
        if (TextUtils.isEmpty(str) || str.length() < 6) {
            return false;
        }
        try {
            if (str.substring(0, 4).equalsIgnoreCase(HttpConstant.HTTP) || str.substring(0, 5).equalsIgnoreCase(HttpConstant.HTTPS)) {
                Intent intent = new Intent(context.getApplicationContext(), Class.forName("com.aliyun.sdk.lighter.runtime.activity.BHARootActivity"));
                Uri uri = Uri.parse(str.trim());
                if (uri != null && uri.getScheme() != null) {
                    if (uri.getScheme().equals(HttpConstant.HTTP) || uri.getScheme().equals(HttpConstant.HTTPS)) {
                        ALog.d(API_NAME, "open http/https page.");
                        intent.setData(uri);
                        if (context != null) {
                            context.startActivity(intent);
                            return true;
                        }
                    }
                }
                return false;
            }
            return false;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            e2.printStackTrace();
            return false;
        }
    }
}
