package com.alibaba.sdk.android.openaccount.session;

import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.model.RefreshToken;
import com.alibaba.sdk.android.openaccount.model.ResultCode;
import com.alibaba.sdk.android.openaccount.model.SessionData;
import com.alibaba.sdk.android.openaccount.model.User;

/* JADX INFO: loaded from: classes.dex */
public interface SessionManagerService {
    RefreshToken getRefreshToken();

    OpenAccountSession getSession();

    Long getSessionCreationTime();

    Integer getSessionExpiredIn();

    String getSessionId();

    boolean isRefreshTokenExpired();

    boolean isSessionExpired();

    void registerSessionListener(SessionListener sessionListener);

    ResultCode removeSession();

    void unRegisterSessionListener(SessionListener sessionListener);

    ResultCode updateSession(SessionData sessionData);

    void updateSessionOnly(SessionData sessionData);

    void updateUser(User user);
}
