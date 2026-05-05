package com.taobao.accs.net;

import anet.channel.NoAvailStrategyException;
import anet.channel.Session;
import anet.channel.SessionCenter;
import anet.channel.entity.ConnType;
import com.alibaba.sdk.android.error.ErrorCode;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.AccsState;
import com.taobao.accs.utl.UtilityImpl;
import java.net.ConnectException;
import java.security.InvalidParameterException;
import java.util.concurrent.TimeoutException;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class r implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ j f6403a;

    r(j jVar) {
        this.f6403a = jVar;
    }

    @Override // java.lang.Runnable
    public void run() {
        ErrorCode errorCodeBuild = AccsErrorCode.SUCCESS;
        try {
            SessionCenter sessionCenter = SessionCenter.getInstance(this.f6403a.i.getAppKey());
            this.f6403a.a(sessionCenter, this.f6403a.i.getInappHost(), false);
            Session throwsException = null;
            try {
                throwsException = sessionCenter.getThrowsException(this.f6403a.b((String) null), ConnType.TypeLevel.SPDY, 60000L);
            } catch (NoAvailStrategyException e) {
                errorCodeBuild = AccsErrorCode.NETWORK_INAPP_NO_STRATEGY.copy().detail(e.getMessage()).build();
            } catch (ConnectException e2) {
                errorCodeBuild = AccsErrorCode.NETWORK_INAPP_CONNECT_FAIL.copy().detail(AccsErrorCode.getAllDetails(e2.getMessage())).build();
            } catch (InvalidParameterException e3) {
                errorCodeBuild = AccsErrorCode.NETWORK_INAPP_ARGS_INVALID.copy().detail(e3.getMessage()).build();
            } catch (TimeoutException e4) {
                errorCodeBuild = AccsErrorCode.NETWORK_INAPP_TIMEOUT.copy().detail(AccsErrorCode.getAllDetails(e4.getMessage())).build();
            } catch (Throwable th) {
                if (UtilityImpl.g(this.f6403a.f6366d)) {
                    errorCodeBuild = AccsErrorCode.NETWORK_INAPP_EXCEPTION.copy().detail(AccsErrorCode.getAllDetails(AccsErrorCode.getExceptionInfo(th))).build();
                } else {
                    errorCodeBuild = AccsErrorCode.NO_NETWORK.copy().detail(AccsErrorCode.getExceptionInfo(th)).build();
                }
            }
            boolean z = true;
            if (throwsException != null) {
                throwsException.ping(true);
            } else {
                if (errorCodeBuild.getCodeInt() != AccsErrorCode.SUCCESS.getCodeInt()) {
                    this.f6403a.t.e(errorCodeBuild.toString());
                    AccsState.getInstance().b(this.f6403a.m, AccsState.RECENT_ERRORS, Integer.valueOf(errorCodeBuild.getCodeInt()));
                } else {
                    this.f6403a.t.e("reconnect fail");
                    AccsState.getInstance().b(this.f6403a.m, AccsState.RECENT_ERRORS, "reconnect session null");
                }
                z = false;
            }
            if (z) {
                return;
            }
            this.f6403a.r();
        } catch (Throwable th2) {
            this.f6403a.t.e("sendMessage", th2);
        }
    }
}
