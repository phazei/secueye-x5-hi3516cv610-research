package com.alibaba.sdk.android.openaccount.ut.impl;

import android.app.Activity;
import android.content.Context;
import com.alibaba.sdk.android.openaccount.Environment;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.config.ConfigService;
import com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener;
import com.alibaba.sdk.android.openaccount.model.User;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.alibaba.sdk.android.openaccount.ut.UserTrackerService;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.ut.device.UTDevice;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AlibabaUserTrackerService implements UserTrackerService, EnvironmentChangeListener {
    public static final String DISABLE_UT_INITIALIZATION_KEY = "disableUTInit";
    private static final String TAG = "oa_ut";
    public static final String USE_BASE_REQUEST_AUTHENTICATION_KEY = "useBaseRequestAuthentication";
    private String appKey;
    private String appVersion;

    @Autowired
    private ConfigService configService;
    private Context context;

    private void initBaseRequestAuthentication() {
    }

    private void updateUTUser(User user) {
    }

    @Override // com.alibaba.sdk.android.openaccount.ut.UserTrackerService
    public void sendCustomHit(String str, long j, String str2, Map<String, String> map) {
    }

    @Override // com.alibaba.sdk.android.openaccount.ut.UserTrackerService
    public void sendCustomHit(String str, String str2, int i, String str3, long j, String str4, Map<String, String> map) {
    }

    @Override // com.alibaba.sdk.android.openaccount.ut.UserTrackerService
    public void updateUserTrackerProperties(Map<String, Object> map) {
    }

    @Override // com.alibaba.sdk.android.openaccount.ut.UserTrackerService
    public void sendCustomHit(String str, Activity activity2) {
        sendCustomHit(str, 60L, activity2 != null ? activity2.getTitle().toString() : str, null);
    }

    @Override // com.alibaba.sdk.android.openaccount.ut.UserTrackerService
    public void sendCustomHit(String str, String str2, Map<String, String> map) {
        sendCustomHit(str, 60L, str2, map);
    }

    @Override // com.alibaba.sdk.android.openaccount.ut.UserTrackerService
    public void sendCustomHit(String str, int i, String str2, long j, String str3, Map<String, String> map) {
        sendCustomHit(UTConstants.TRACKER_ID, str, i, str2, j, str3, map);
    }

    public synchronized void init(Context context) {
        try {
            AliSDKLogger.i(TAG, "do NOT ini ut in this version");
        } catch (Exception e) {
            AliSDKLogger.printStackTraceAndMore(e);
            throw new RuntimeException(e);
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.config.EnvironmentChangeListener
    public void onEnvironmentChange(Environment environment, Environment environment2) {
        init(OpenAccountSDK.getAndroidContext());
    }

    private boolean isSecurityGuardAvaiable() {
        try {
            Class.forName("com.alibaba.wireless.security.open.SecurityGuardManager");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    private boolean is2ndUTLibrary() {
        try {
            Class.forName("com.ut.mini.IUTApplication");
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    @Override // com.alibaba.sdk.android.openaccount.ut.UserTrackerService
    public String getDefaultUserTrackerId() {
        return UTDevice.getUtdid(this.context);
    }
}
