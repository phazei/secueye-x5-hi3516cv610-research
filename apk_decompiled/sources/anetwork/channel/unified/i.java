package anetwork.channel.unified;

import android.support.v4.media.session.PlaybackStateCompat;
import anet.channel.RequestCb;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.flow.FlowStat;
import anet.channel.flow.NetworkAnalysis;
import anet.channel.request.Request;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.statist.RequestStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.thread.ThreadPoolExecutorFactory;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpHelper;
import anet.channel.util.HttpUrl;
import anetwork.channel.aidl.DefaultFinishEvent;
import anetwork.channel.cache.Cache;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.cookie.CookieManager;
import anetwork.channel.unified.e;
import com.aliyun.alink.linksdk.alcs.coap.resources.LinkFormat;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import io.netty.handler.codec.http.HttpHeaders;
import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class i implements RequestCb {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Request f2075a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ RequestStatistic f2076b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ e f2077c;

    i(e eVar, Request request, RequestStatistic requestStatistic) {
        this.f2077c = eVar;
        this.f2075a = request;
        this.f2076b = requestStatistic;
    }

    @Override // anet.channel.RequestCb
    public void onResponseCode(int i, Map<String, List<String>> map) {
        String singleHeaderFieldByKey;
        if (this.f2077c.h.get()) {
            return;
        }
        if (ALog.isPrintLog(2)) {
            ALog.i(e.TAG, "onResponseCode", this.f2075a.getSeq(), "code", Integer.valueOf(i));
            ALog.i(e.TAG, "onResponseCode", this.f2075a.getSeq(), "headers", map);
        }
        if (HttpHelper.checkRedirect(this.f2075a, i) && (singleHeaderFieldByKey = HttpHelper.getSingleHeaderFieldByKey(map, "Location")) != null) {
            HttpUrl httpUrl = HttpUrl.parse(singleHeaderFieldByKey);
            if (httpUrl != null) {
                if (this.f2077c.h.compareAndSet(false, true)) {
                    httpUrl.lockScheme();
                    this.f2077c.f2059a.f2078a.a(httpUrl);
                    this.f2077c.f2059a.f2081d = new AtomicBoolean();
                    this.f2077c.f2059a.e = new e(this.f2077c.f2059a, null, null);
                    this.f2076b.recordRedirect(i, httpUrl.simpleUrlString());
                    this.f2076b.locationUrl = singleHeaderFieldByKey;
                    ThreadPoolExecutorFactory.submitPriorityTask(this.f2077c.f2059a.e, ThreadPoolExecutorFactory.Priority.HIGH);
                    return;
                }
                return;
            }
            ALog.e(e.TAG, "redirect url is invalid!", this.f2075a.getSeq(), "redirect url", singleHeaderFieldByKey);
        }
        try {
            this.f2077c.f2059a.a();
            CookieManager.setCookie(this.f2077c.f2059a.f2078a.g(), map);
            this.f2077c.i = HttpHelper.parseContentLength(map);
            String strG = this.f2077c.f2059a.f2078a.g();
            if (this.f2077c.f2061c != null && i == 304) {
                this.f2077c.f2061c.responseHeaders.putAll(map);
                Cache.Entry entryA = anetwork.channel.cache.a.a(map);
                if (entryA != null && entryA.ttl > this.f2077c.f2061c.ttl) {
                    this.f2077c.f2061c.ttl = entryA.ttl;
                }
                this.f2077c.f2059a.f2079b.onResponseCode(200, this.f2077c.f2061c.responseHeaders);
                this.f2077c.f2059a.f2079b.onDataReceiveSize(1, this.f2077c.f2061c.data.length, ByteArray.wrap(this.f2077c.f2061c.data));
                long jCurrentTimeMillis = System.currentTimeMillis();
                this.f2077c.f2060b.put(strG, this.f2077c.f2061c);
                ALog.i(e.TAG, "update cache", this.f2077c.f2059a.f2080c, "cost", Long.valueOf(System.currentTimeMillis() - jCurrentTimeMillis), "key", strG);
                return;
            }
            if (this.f2077c.f2060b != null) {
                if (HttpHeaders.Values.NO_STORE.equals(HttpHelper.getSingleHeaderFieldByKey(map, "Cache-Control"))) {
                    this.f2077c.f2060b.remove(strG);
                } else {
                    e eVar = this.f2077c;
                    Cache.Entry entryA2 = anetwork.channel.cache.a.a(map);
                    eVar.f2061c = entryA2;
                    if (entryA2 != null) {
                        HttpHelper.removeHeaderFiledByKey(map, "Cache-Control");
                        map.put("Cache-Control", Arrays.asList(HttpHeaders.Values.NO_STORE));
                        this.f2077c.f2062d = new ByteArrayOutputStream(this.f2077c.i != 0 ? this.f2077c.i : 5120);
                    }
                }
            }
            map.put(HttpConstant.X_PROTOCOL, Arrays.asList(this.f2076b.protocolType));
            if (!"open".equalsIgnoreCase(HttpHelper.getSingleHeaderFieldByKey(map, HttpConstant.STREAMING_PARSER)) && NetworkConfigCenter.isResponseBufferEnable() && this.f2077c.i <= 131072) {
                this.f2077c.m = new e.a(i, map);
            } else {
                this.f2077c.f2059a.f2079b.onResponseCode(i, map);
                this.f2077c.k = true;
            }
        } catch (Exception e) {
            ALog.w(e.TAG, "[onResponseCode] error.", this.f2077c.f2059a.f2080c, e, new Object[0]);
        }
    }

    @Override // anet.channel.RequestCb
    public void onDataReceive(ByteArray byteArray, boolean z) {
        if (this.f2077c.h.get()) {
            return;
        }
        if (this.f2077c.j == 0) {
            ALog.i(e.TAG, "[onDataReceive] receive first data chunk!", this.f2077c.f2059a.f2080c, new Object[0]);
        }
        if (z) {
            ALog.i(e.TAG, "[onDataReceive] receive last data chunk!", this.f2077c.f2059a.f2080c, new Object[0]);
        }
        this.f2077c.j++;
        try {
            if (this.f2077c.m != null) {
                this.f2077c.m.f2065c.add(byteArray);
                if (this.f2076b.recDataSize > PlaybackStateCompat.ACTION_PREPARE_FROM_URI || z) {
                    this.f2077c.j = this.f2077c.m.a(this.f2077c.f2059a.f2079b, this.f2077c.i);
                    this.f2077c.k = true;
                    this.f2077c.l = this.f2077c.j > 1;
                    this.f2077c.m = null;
                }
            } else {
                this.f2077c.f2059a.f2079b.onDataReceiveSize(this.f2077c.j, this.f2077c.i, byteArray);
                this.f2077c.l = true;
            }
            if (this.f2077c.f2062d != null) {
                this.f2077c.f2062d.write(byteArray.getBuffer(), 0, byteArray.getDataLength());
                if (z) {
                    String strG = this.f2077c.f2059a.f2078a.g();
                    this.f2077c.f2061c.data = this.f2077c.f2062d.toByteArray();
                    long jCurrentTimeMillis = System.currentTimeMillis();
                    this.f2077c.f2060b.put(strG, this.f2077c.f2061c);
                    ALog.i(e.TAG, "write cache", this.f2077c.f2059a.f2080c, "cost", Long.valueOf(System.currentTimeMillis() - jCurrentTimeMillis), "size", Integer.valueOf(this.f2077c.f2061c.data.length), "key", strG);
                }
            }
        } catch (Exception e) {
            ALog.w(e.TAG, "[onDataReceive] error.", this.f2077c.f2059a.f2080c, e, new Object[0]);
        }
    }

    @Override // anet.channel.RequestCb
    public void onFinish(int i, String str, RequestStatistic requestStatistic) {
        String strValueOf;
        DefaultFinishEvent defaultFinishEvent;
        if (this.f2077c.h.getAndSet(true)) {
            return;
        }
        int i2 = 3;
        if (ALog.isPrintLog(2)) {
            ALog.i(e.TAG, "[onFinish]", this.f2077c.f2059a.f2080c, "code", Integer.valueOf(i), "msg", str);
        }
        if (i < 0) {
            try {
                if (this.f2077c.f2059a.f2078a.d()) {
                    if (!this.f2077c.k && !this.f2077c.l) {
                        ALog.e(e.TAG, "clear response buffer and retry", this.f2077c.f2059a.f2080c, new Object[0]);
                        if (this.f2077c.m != null) {
                            if (!this.f2077c.m.f2065c.isEmpty()) {
                                i2 = 4;
                            }
                            requestStatistic.roaming = i2;
                            this.f2077c.m.a();
                            this.f2077c.m = null;
                        }
                        if (this.f2077c.f2059a.f2078a.f2044a == 0) {
                            requestStatistic.firstProtocol = requestStatistic.protocolType;
                            requestStatistic.firstErrorCode = requestStatistic.tnetErrorCode != 0 ? requestStatistic.tnetErrorCode : i;
                        }
                        this.f2077c.f2059a.f2078a.k();
                        this.f2077c.f2059a.f2081d = new AtomicBoolean();
                        this.f2077c.f2059a.e = new e(this.f2077c.f2059a, this.f2077c.f2060b, this.f2077c.f2061c);
                        if (requestStatistic.tnetErrorCode != 0) {
                            strValueOf = i + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + requestStatistic.protocolType + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + requestStatistic.tnetErrorCode;
                            requestStatistic.tnetErrorCode = 0;
                        } else {
                            strValueOf = String.valueOf(i);
                        }
                        requestStatistic.appendErrorTrace(strValueOf);
                        long jCurrentTimeMillis = System.currentTimeMillis();
                        requestStatistic.retryCostTime += jCurrentTimeMillis - requestStatistic.start;
                        requestStatistic.start = jCurrentTimeMillis;
                        ThreadPoolExecutorFactory.submitPriorityTask(this.f2077c.f2059a.e, ThreadPoolExecutorFactory.Priority.HIGH);
                        return;
                    }
                    requestStatistic.msg += ":回调后触发重试";
                    if (this.f2077c.l) {
                        requestStatistic.roaming = 2;
                    } else if (this.f2077c.k) {
                        requestStatistic.roaming = 1;
                    }
                    ALog.e(e.TAG, "Cannot retry request after onHeader/onDataReceived callback!", this.f2077c.f2059a.f2080c, new Object[0]);
                }
            } catch (Exception unused) {
                return;
            }
        }
        if (this.f2077c.m != null) {
            this.f2077c.m.a(this.f2077c.f2059a.f2079b, this.f2077c.i);
        }
        this.f2077c.f2059a.a();
        requestStatistic.isDone.set(true);
        if (this.f2077c.f2059a.f2078a.j() && requestStatistic.contentLength != 0 && requestStatistic.contentLength != requestStatistic.rspBodyDeflateSize) {
            requestStatistic.ret = 0;
            requestStatistic.statusCode = -206;
            str = ErrorConstant.getErrMsg(-206);
            requestStatistic.msg = str;
            ALog.e(e.TAG, "received data length not match with content-length", this.f2077c.f2059a.f2080c, "content-length", Integer.valueOf(this.f2077c.i), "recDataLength", Long.valueOf(requestStatistic.rspBodyDeflateSize));
            ExceptionStatistic exceptionStatistic = new ExceptionStatistic(-206, str, LinkFormat.RESOURCE_TYPE);
            exceptionStatistic.url = this.f2077c.f2059a.f2078a.g();
            AppMonitor.getInstance().commitStat(exceptionStatistic);
            i = -206;
        }
        if (i == 304 && this.f2077c.f2061c != null) {
            requestStatistic.protocolType = "cache";
            defaultFinishEvent = new DefaultFinishEvent(200, str, this.f2075a);
        } else {
            defaultFinishEvent = new DefaultFinishEvent(i, str, this.f2075a);
        }
        this.f2077c.f2059a.f2079b.onFinish(defaultFinishEvent);
        if (i >= 0) {
            anet.channel.monitor.b.a().a(requestStatistic.sendStart, requestStatistic.rspEnd, requestStatistic.rspHeadDeflateSize + requestStatistic.rspBodyDeflateSize);
        } else {
            requestStatistic.netType = NetworkStatusHelper.getNetworkSubType();
        }
        NetworkAnalysis.getInstance().commitFlow(new FlowStat(this.f2077c.e, requestStatistic));
    }
}
