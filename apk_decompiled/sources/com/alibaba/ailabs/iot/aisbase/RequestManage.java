package com.alibaba.ailabs.iot.aisbase;

import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.env.AppEnv;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import datasource.AuthManager;
import datasource.NetworkCallback;
import datasource.implemention.DefaultAuthManager;
import datasource.implemention.FeiyanAuthManager;
import datasource.implemention.data.DeviceVersionInfo;
import datasource.implemention.data.GetDeviceUUIDRespData;
import datasource.implemention.data.OtaProgressRespData;
import datasource.implemention.data.UpdateDeviceVersionRespData;

/* JADX INFO: loaded from: classes.dex */
public class RequestManage {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2518a = "RequestManage";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public AuthInfoListener f2519b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public AuthManager f2520c;

    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final RequestManage f2521a = new RequestManage();
    }

    public RequestManage() {
        this.f2520c = AppEnv.IS_GENIE_ENV ? new DefaultAuthManager() : new FeiyanAuthManager();
    }

    public static RequestManage getInstance() {
        return a.f2521a;
    }

    public void authCheckAndGetBleKey(@NonNull String str, @NonNull String str2, @NonNull String str3, @NonNull boolean z, NetworkCallback<String> networkCallback) {
        if (this.f2520c == null) {
            networkCallback.onFailure("", "");
            return;
        }
        AuthInfoListener authInfoListener = this.f2519b;
        String authInfo = authInfoListener != null ? authInfoListener.getAuthInfo() : "";
        if (z) {
            this.f2520c.authCipherCheckThenGetKeyForBLEDevice(authInfo, str, str2, str3, networkCallback);
        } else {
            this.f2520c.authCheckAndGetBleKey(authInfo, str, str2, str3, networkCallback);
        }
    }

    public void getAuthRandomId(@NonNull String str, @NonNull String str2, @NonNull boolean z, NetworkCallback<String> networkCallback) {
        LogUtils.i(f2518a, "getAuthRandomId " + str);
        if (this.f2520c == null) {
            networkCallback.onFailure("", "");
            return;
        }
        AuthInfoListener authInfoListener = this.f2519b;
        String authInfo = authInfoListener != null ? authInfoListener.getAuthInfo() : "";
        if (z) {
            this.f2520c.getAuthRandomIdForBLEDevice(authInfo, str, str2, networkCallback);
        } else {
            this.f2520c.getAuthRandomId(authInfo, str, str2, networkCallback);
        }
    }

    public void getDeviceUUIDViaProductId(@NonNull String str, @NonNull String str2, NetworkCallback<GetDeviceUUIDRespData> networkCallback) {
        if (this.f2520c == null) {
            networkCallback.onFailure(String.valueOf(-303), "Network not initialized");
        } else {
            AuthInfoListener authInfoListener = this.f2519b;
            this.f2520c.getDeviceUUID(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, networkCallback);
        }
    }

    public String getUserId() {
        AuthInfoListener authInfoListener = this.f2519b;
        if (authInfoListener == null) {
            LogUtils.e(f2518a, "mAuthInfoListener is null");
            return "";
        }
        try {
            JSONObject object = JSONObject.parseObject(authInfoListener.getAuthInfo());
            if (object != null) {
                return object.getString("userId");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUtdId() {
        AuthInfoListener authInfoListener = this.f2519b;
        if (authInfoListener == null) {
            LogUtils.e(f2518a, "mAuthInfoListener is null");
            return "";
        }
        try {
            JSONObject object = JSONObject.parseObject(authInfoListener.getAuthInfo());
            if (object != null) {
                return object.getString("utdId");
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void gmaOtaProgressReport(@NonNull String str, @NonNull String str2, @NonNull String str3, NetworkCallback<OtaProgressRespData> networkCallback) {
        if (this.f2520c == null) {
            networkCallback.onFailure(String.valueOf(-303), "Network not initialized");
            return;
        }
        AuthInfoListener authInfoListener = this.f2519b;
        this.f2520c.gmaOtaProgressReport(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, str3, networkCallback);
    }

    public void init(AuthInfoListener authInfoListener, AuthManager authManager) {
        LogUtils.d(f2518a, "init...");
        this.f2519b = authInfoListener;
        this.f2520c = authManager;
    }

    public void queryOtaInfo(@NonNull String str, @NonNull String str2, NetworkCallback<DeviceVersionInfo> networkCallback) {
        if (this.f2520c == null) {
            networkCallback.onFailure(String.valueOf(-303), "Network not initialized");
        } else {
            AuthInfoListener authInfoListener = this.f2519b;
            this.f2520c.queryOtaInfo(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, networkCallback);
        }
    }

    public void updateDeviceVersion(@NonNull String str, @NonNull String str2, NetworkCallback<UpdateDeviceVersionRespData> networkCallback) {
        if (this.f2520c == null) {
            networkCallback.onFailure(String.valueOf(-303), "Network not initialized");
        } else {
            AuthInfoListener authInfoListener = this.f2519b;
            this.f2520c.updateDeviceVersion(authInfoListener != null ? authInfoListener.getAuthInfo() : "", str, str2, networkCallback);
        }
    }
}
