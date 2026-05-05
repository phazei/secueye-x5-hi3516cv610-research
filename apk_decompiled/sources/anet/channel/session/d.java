package anet.channel.session;

import android.content.Context;
import anet.channel.AwcnConfig;
import anet.channel.RequestCb;
import anet.channel.Session;
import anet.channel.entity.ConnType;
import anet.channel.request.Cancelable;
import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.Utils;
import io.netty.handler.codec.rtsp.RtspHeaders;
import javax.net.ssl.SSLSocketFactory;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class d extends Session {
    private SSLSocketFactory w;

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // anet.channel.Session
    public Runnable getRecvTimeOutRunnable() {
        return null;
    }

    public d(Context context, anet.channel.entity.a aVar) {
        super(context, aVar);
        if (this.k == null) {
            this.j = (this.f1634c == null || !this.f1634c.startsWith(HttpConstant.HTTPS)) ? ConnType.HTTP : ConnType.HTTPS;
        } else if (AwcnConfig.isHttpsSniEnable() && this.j.equals(ConnType.HTTPS)) {
            this.w = new anet.channel.util.j(this.f1635d);
        }
    }

    @Override // anet.channel.Session
    public boolean isAvailable() {
        return this.n == 4;
    }

    @Override // anet.channel.Session
    public void connect() {
        try {
            if (this.k != null && this.k.getIpSource() == 1) {
                notifyStatus(4, new anet.channel.entity.b(1));
                return;
            }
            Request.Builder redirectEnable = new Request.Builder().setUrl(this.f1634c).setSeq(this.p).setConnectTimeout((int) (this.r * Utils.getNetworkTimeFactor())).setReadTimeout((int) (this.s * Utils.getNetworkTimeFactor())).setRedirectEnable(false);
            if (this.w != null) {
                redirectEnable.setSslSocketFactory(this.w);
            }
            if (this.m) {
                redirectEnable.addHeader("Host", this.e);
            }
            if (anet.channel.util.c.a() && anet.channel.strategy.utils.c.a(this.e)) {
                try {
                    this.f = anet.channel.util.c.a(this.e);
                } catch (Exception unused) {
                }
            }
            ALog.i("awcn.HttpSession", "HttpSession connect", null, "host", this.f1634c, "ip", this.f, RtspHeaders.Values.PORT, Integer.valueOf(this.g));
            Request requestBuild = redirectEnable.build();
            requestBuild.setDnsOptimize(this.f, this.g);
            ThreadPoolExecutorFactory.submitPriorityTask(new e(this, requestBuild), ThreadPoolExecutorFactory.Priority.LOW);
        } catch (Throwable th) {
            ALog.e("awcn.HttpSession", "HTTP connect fail.", null, th, new Object[0]);
        }
    }

    @Override // anet.channel.Session
    public void close() {
        notifyStatus(6, null);
    }

    @Override // anet.channel.Session
    public void close(boolean z) {
        this.t = false;
        close();
    }

    @Override // anet.channel.Session
    public Cancelable request(Request request, RequestCb requestCb) {
        anet.channel.request.b bVar = anet.channel.request.b.NULL;
        Request.Builder builderNewBuilder = null;
        RequestStatistic requestStatistic = request != null ? request.f1794a : new RequestStatistic(this.f1635d, null);
        requestStatistic.setConnType(this.j);
        if (requestStatistic.start == 0) {
            long jCurrentTimeMillis = System.currentTimeMillis();
            requestStatistic.reqStart = jCurrentTimeMillis;
            requestStatistic.start = jCurrentTimeMillis;
        }
        if (request == null || requestCb == null) {
            if (requestCb != null) {
                requestCb.onFinish(-102, ErrorConstant.getErrMsg(-102), requestStatistic);
            }
            return bVar;
        }
        try {
            if (request.getSslSocketFactory() == null && this.w != null) {
                builderNewBuilder = request.newBuilder().setSslSocketFactory(this.w);
            }
            if (this.m) {
                if (builderNewBuilder == null) {
                    builderNewBuilder = request.newBuilder();
                }
                builderNewBuilder.addHeader("Host", this.e);
            }
            if (builderNewBuilder != null) {
                request = builderNewBuilder.build();
            }
            if (this.f == null) {
                String strHost = request.getHttpUrl().host();
                if (anet.channel.util.c.a() && anet.channel.strategy.utils.c.a(strHost)) {
                    try {
                        this.f = anet.channel.util.c.a(strHost);
                    } catch (Exception unused) {
                    }
                }
            }
            request.setDnsOptimize(this.f, this.g);
            request.setUrlScheme(this.j.isSSL());
            if (this.k != null) {
                request.f1794a.setIpInfo(this.k.getIpSource(), this.k.getIpType());
            } else {
                request.f1794a.setIpInfo(1, 1);
            }
            request.f1794a.unit = this.l;
            return new anet.channel.request.b(ThreadPoolExecutorFactory.submitPriorityTask(new f(this, request, requestCb, requestStatistic), anet.channel.util.h.a(request)), request.getSeq());
        } catch (Throwable th) {
            if (requestCb == null) {
                return bVar;
            }
            requestCb.onFinish(-101, ErrorConstant.formatMsg(-101, th.toString()), requestStatistic);
            return bVar;
        }
    }
}
