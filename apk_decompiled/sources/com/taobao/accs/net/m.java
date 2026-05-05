package com.taobao.accs.net;

import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.NoAvailStrategyException;
import anet.channel.Session;
import anet.channel.SessionCenter;
import anet.channel.entity.ConnType;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.logger.ILog;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.AccsState;
import com.taobao.accs.common.Constants;
import com.taobao.accs.data.Message;
import com.taobao.accs.ut.monitor.TrafficsMonitor;
import com.taobao.accs.utl.AppMonitorAdapter;
import com.taobao.accs.utl.BaseMonitor;
import com.taobao.accs.utl.UtilityImpl;
import java.net.ConnectException;
import java.security.InvalidParameterException;
import java.util.concurrent.TimeoutException;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class m implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Message f6388a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ j f6389b;

    m(j jVar, Message message) {
        this.f6389b = jVar;
        this.f6388a = message;
    }

    @Override // java.lang.Runnable
    public void run() {
        ILog iLog;
        Object[] objArr;
        boolean z;
        Session throwsException;
        Message message = this.f6388a;
        if (message != null) {
            if (message.e() != null) {
                this.f6388a.e().onTakeFromQueue();
            }
            ErrorCode errorCodeBuild = AccsErrorCode.SUCCESS;
            int iA = this.f6388a.a();
            try {
                try {
                    this.f6389b.t.d("sendMessage start", Constants.KEY_DATA_ID, this.f6388a.q, "type", Message.c.b(iA));
                    if (iA != 1) {
                        this.f6389b.t.w("sendMessage skip", "type", Message.c.b(iA));
                        z = true;
                    } else if (this.f6388a.f == null) {
                        this.f6389b.e.a(this.f6388a, AccsErrorCode.MESSAGE_HOST_NULL);
                        z = true;
                    } else {
                        SessionCenter sessionCenter = SessionCenter.getInstance(this.f6389b.i.getAppKey());
                        this.f6389b.a(sessionCenter, this.f6388a.f.getHost(), false);
                        try {
                            throwsException = sessionCenter.getThrowsException(this.f6388a.f.toString(), ConnType.TypeLevel.SPDY, 60000L);
                        } catch (NoAvailStrategyException e) {
                            errorCodeBuild = AccsErrorCode.NETWORK_INAPP_NO_STRATEGY.copy().detail(e.getMessage()).build();
                            throwsException = null;
                        } catch (ConnectException e2) {
                            errorCodeBuild = AccsErrorCode.NETWORK_INAPP_TIMEOUT.copy().detail(AccsErrorCode.getAllDetails(e2.getMessage())).build();
                            throwsException = null;
                        } catch (InvalidParameterException e3) {
                            errorCodeBuild = AccsErrorCode.NETWORK_INAPP_ARGS_INVALID.copy().detail(e3.getMessage()).build();
                            throwsException = null;
                        } catch (TimeoutException e4) {
                            errorCodeBuild = AccsErrorCode.NETWORK_INAPP_TIMEOUT.copy().detail(AccsErrorCode.getAllDetails(e4.getMessage())).build();
                            throwsException = null;
                        } catch (Throwable th) {
                            if (UtilityImpl.g(this.f6389b.f6366d)) {
                                errorCodeBuild = AccsErrorCode.NETWORK_INAPP_EXCEPTION.copy().detail(AccsErrorCode.getAllDetails(AccsErrorCode.getExceptionInfo(th))).build();
                                throwsException = null;
                            } else {
                                errorCodeBuild = AccsErrorCode.NO_NETWORK.copy().detail(AccsErrorCode.getExceptionInfo(th)).build();
                                throwsException = null;
                            }
                        }
                        if (throwsException != null) {
                            byte[] bArrA = this.f6388a.a(this.f6389b.f6366d, this.f6389b.f6365c);
                            ILog iLog2 = this.f6389b.t;
                            Object[] objArr2 = new Object[11];
                            objArr2[0] = "sendMessage";
                            objArr2[1] = Constants.KEY_DATA_ID;
                            objArr2[2] = this.f6388a.b();
                            objArr2[3] = "command";
                            objArr2[4] = this.f6388a.t;
                            objArr2[5] = "host";
                            objArr2[6] = this.f6388a.f;
                            objArr2[7] = "len";
                            objArr2[8] = Integer.valueOf(bArrA == null ? 0 : bArrA.length);
                            objArr2[9] = "utdid";
                            objArr2[10] = this.f6389b.j;
                            iLog2.i(objArr2);
                            this.f6388a.a(System.currentTimeMillis());
                            if (bArrA.length > 16384 && this.f6388a.t.intValue() != 102) {
                                this.f6389b.e.a(this.f6388a, AccsErrorCode.MESSAGE_TOO_LARGE);
                            } else {
                                this.f6389b.e.a(this.f6388a);
                                int iA2 = this.f6388a.f6299c ? -this.f6388a.d().a() : this.f6388a.d().a();
                                if (this.f6388a.f6299c) {
                                    this.f6389b.l.put(Integer.valueOf(iA2), this.f6388a);
                                }
                                AccsState.getInstance().a(this.f6389b.m, AccsState.LAST_MSG_SEND_TIME, Integer.valueOf(iA2));
                                throwsException.sendCustomFrame(iA2, bArrA, 200);
                                if (this.f6388a.e() != null) {
                                    this.f6388a.e().onSendData();
                                }
                                this.f6389b.a(this.f6388a.b(), this.f6389b.i.isQuickReconnect(), this.f6388a.S);
                                this.f6389b.e.a(new TrafficsMonitor.a(this.f6388a.H, GlobalAppRuntimeInfo.isAppBackground(), this.f6388a.f.toString(), bArrA.length));
                            }
                            z = true;
                        } else {
                            if (errorCodeBuild.getCodeInt() != AccsErrorCode.SUCCESS.getCodeInt()) {
                                this.f6389b.t.e(errorCodeBuild.toString());
                                AccsState.getInstance().b(this.f6389b.m, AccsState.RECENT_ERRORS, Integer.valueOf(errorCodeBuild.getCodeInt()));
                            } else {
                                this.f6389b.t.e("sendMessage session is null");
                                AccsState.getInstance().b(this.f6389b.m, AccsState.RECENT_ERRORS, "send session null");
                            }
                            z = false;
                        }
                    }
                    if (!z) {
                        if (errorCodeBuild.getCodeInt() == AccsErrorCode.SUCCESS.getCodeInt()) {
                            errorCodeBuild = AccsErrorCode.INAPP_CON_DISCONNECTED.copy().detail(AccsErrorCode.getAllDetails(null)).build();
                        }
                        if (iA == 1) {
                            if (this.f6388a.g() || !this.f6389b.a(this.f6388a, 2000)) {
                                this.f6389b.e.a(this.f6388a, errorCodeBuild);
                            }
                            if (this.f6388a.R == 1 && this.f6388a.e() != null) {
                                AppMonitorAdapter.commitCount("accs", BaseMonitor.COUNT_POINT_RESEND, "total_accs", 0.0d);
                            }
                        } else {
                            this.f6389b.e.a(this.f6388a, errorCodeBuild);
                        }
                    }
                    iLog = this.f6389b.t;
                    objArr = new Object[]{"sendMessage end", Constants.KEY_DATA_ID, this.f6388a.b(), "status", Boolean.valueOf(z)};
                } catch (Throwable th2) {
                    AppMonitorAdapter.commitAlarmFail("accs", BaseMonitor.ALARM_POINT_REQ_ERROR, this.f6388a.H, "", this.f6389b.f6365c + th2.toString());
                    this.f6389b.t.e("sendMessage", th2);
                    iLog = this.f6389b.t;
                    objArr = new Object[]{"sendMessage end", Constants.KEY_DATA_ID, this.f6388a.b(), "status", true};
                }
                iLog.i(objArr);
            } catch (Throwable th3) {
                this.f6389b.t.i("sendMessage end", Constants.KEY_DATA_ID, this.f6388a.b(), "status", true);
                throw th3;
            }
        }
    }
}
