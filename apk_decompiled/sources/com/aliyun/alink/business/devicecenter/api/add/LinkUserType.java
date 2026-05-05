package com.aliyun.alink.business.devicecenter.api.add;

import com.google.firebase.analytics.FirebaseAnalytics;

/* JADX INFO: loaded from: classes.dex */
public enum LinkUserType {
    LINK_USER_TYPE_LOGIN(FirebaseAnalytics.Event.LOGIN),
    LINK_USER_TYPE_TOURISTS("tourists"),
    LINK_USER_TYPE_OPEN_API("openapi");

    public String userType;

    LinkUserType(String str) {
        this.userType = str;
    }

    public String getUserType() {
        return this.userType;
    }
}
