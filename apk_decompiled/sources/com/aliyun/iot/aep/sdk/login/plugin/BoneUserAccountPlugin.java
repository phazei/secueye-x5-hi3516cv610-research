package com.aliyun.iot.aep.sdk.login.plugin;

import com.aliyun.alink.sdk.jsbridge.BoneCallback;
import com.aliyun.alink.sdk.jsbridge.methodexport.BaseBonePlugin;
import com.aliyun.alink.sdk.jsbridge.methodexport.MethodExported;
import com.aliyun.iot.aep.sdk.log.ALog;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.login.R;
import com.aliyun.iot.aep.sdk.login.data.UserInfo;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
public class BoneUserAccountPlugin extends BaseBonePlugin {
    public static final String API_NAME = "BoneUserAccount";

    @MethodExported
    public void login(final BoneCallback boneCallback) {
        LoginBusiness.login(new ILoginCallback() { // from class: com.aliyun.iot.aep.sdk.login.plugin.BoneUserAccountPlugin.1
            @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
            public void onLoginSuccess() {
                JSONObject jSONObject = new JSONObject();
                try {
                    UserInfo userInfo = LoginBusiness.getUserInfo();
                    if (userInfo != null) {
                        jSONObject.put("userId", userInfo.userId);
                        jSONObject.put("avatarUrl", userInfo.userAvatarUrl);
                        jSONObject.put("nickName", userInfo.userNick);
                        boneCallback.success(jSONObject);
                    } else {
                        boneCallback.success(null);
                    }
                } catch (Exception unused) {
                    boneCallback.success(null);
                }
            }

            @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
            public void onLoginFailed(int i, String str) {
                if (i == 10003) {
                    boneCallback.failed("420", BoneUserAccountPlugin.this.context.getString(R.string.account_not_login));
                } else {
                    boneCallback.failed("421", str);
                }
            }
        });
    }

    @MethodExported
    public void isLogin(BoneCallback boneCallback) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("logined", LoginBusiness.isLogin());
            boneCallback.success(jSONObject);
        } catch (Exception e) {
            boneCallback.failed("", this.context.getString(R.string.account_data_exception) + e.toString());
        }
    }

    @MethodExported
    public void logout(final BoneCallback boneCallback) {
        LoginBusiness.logout(new ILogoutCallback() { // from class: com.aliyun.iot.aep.sdk.login.plugin.BoneUserAccountPlugin.2
            @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
            public void onLogoutSuccess() {
                boneCallback.success(null);
            }

            @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
            public void onLogoutFailed(int i, String str) {
                if (!LoginBusiness.isLogin()) {
                    boneCallback.failed("401", str);
                    return;
                }
                boneCallback.failed(i + "", str);
            }
        });
    }

    @MethodExported
    public void getAccountInfo(BoneCallback boneCallback) {
        ALog.i(API_NAME, "getAccountInfo in");
        try {
            JSONObject jSONObject = new JSONObject();
            if (!LoginBusiness.isLogin()) {
                boneCallback.failed("401", this.context.getString(R.string.account_not_login));
                return;
            }
            UserInfo userInfo = LoginBusiness.getUserInfo();
            if (userInfo != null) {
                jSONObject.put("userId", userInfo.userId);
                jSONObject.put("avatarUrl", userInfo.userAvatarUrl);
                jSONObject.put("nickName", userInfo.userNick == null ? "" : userInfo.userNick);
                try {
                    jSONObject.put("token", LoginBusiness.getLoginAdapter().getSessionId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                boneCallback.success(jSONObject);
                ALog.i(API_NAME, "getAccountInfo success");
                return;
            }
            boneCallback.failed("", this.context.getString(R.string.account_get_userinfo_exception));
            ALog.i(API_NAME, "getAccountInfo failed");
        } catch (Exception unused) {
            boneCallback.failed("", this.context.getString(R.string.account_get_userinfo_exception));
            ALog.i(API_NAME, "getAccountInfo exception");
        }
    }
}
