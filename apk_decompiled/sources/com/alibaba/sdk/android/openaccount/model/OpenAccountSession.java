package com.alibaba.sdk.android.openaccount.model;

import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface OpenAccountSession {
    public static final int LOGIN = 1;
    public static final int NO_PASSWORD_LOGIN = 4;
    public static final int REGISTER = 2;
    public static final int RESET_PASSWORD = 3;
    public static final int UNKNOWN = 0;
    public static final int UPDATE_MOBILE = 5;
    public static final int UPDATE_PROFILE = 6;

    String getAuthorizationCode();

    String getEmail();

    String getLoginId();

    Long getLoginTime();

    String getMobile();

    String getNick();

    Map<String, Object> getOtherInfo();

    int getScenario();

    User getUser();

    String getUserId();

    boolean isLogin();
}
