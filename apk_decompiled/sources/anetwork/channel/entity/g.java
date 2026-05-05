package anetwork.channel.entity;

import anet.channel.request.Request;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.HttpUrl;
import anet.channel.util.Utils;
import anetwork.channel.aidl.ParcelableRequest;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.util.RequestConstant;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class g {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public RequestStatistic f2045b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final int f2046c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public final int f2047d;
    public final String e;
    public final int f;
    private ParcelableRequest g;
    private Request h;
    private int j;
    private final boolean k;
    private int i = 0;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f2044a = 0;

    public g(ParcelableRequest parcelableRequest, int i, boolean z) {
        this.h = null;
        this.j = 0;
        if (parcelableRequest == null) {
            throw new IllegalArgumentException("request is null");
        }
        this.g = parcelableRequest;
        this.f = i;
        this.k = z;
        this.e = anetwork.channel.util.a.a(parcelableRequest.seqNo, this.f == 0 ? "HTTP" : "DGRD");
        this.f2046c = parcelableRequest.connectTimeout <= 0 ? (int) (Utils.getNetworkTimeFactor() * 12000.0f) : parcelableRequest.connectTimeout;
        this.f2047d = parcelableRequest.readTimeout <= 0 ? (int) (Utils.getNetworkTimeFactor() * 12000.0f) : parcelableRequest.readTimeout;
        this.j = (parcelableRequest.retryTime < 0 || parcelableRequest.retryTime > 3) ? 2 : parcelableRequest.retryTime;
        HttpUrl httpUrlL = l();
        this.f2045b = new RequestStatistic(httpUrlL.host(), String.valueOf(parcelableRequest.bizId));
        this.f2045b.url = httpUrlL.simpleUrlString();
        this.h = b(httpUrlL);
    }

    public Request a() {
        return this.h;
    }

    public void a(Request request) {
        this.h = request;
    }

    private HttpUrl l() {
        HttpUrl httpUrl = HttpUrl.parse(this.g.url);
        if (httpUrl == null) {
            throw new IllegalArgumentException("url is invalid. url=" + this.g.url);
        }
        if (!NetworkConfigCenter.isSSLEnabled()) {
            ALog.i("anet.RequestConfig", "request ssl disabled.", this.e, new Object[0]);
            httpUrl.downgradeSchemeAndLock();
        } else if (RequestConstant.FALSE.equalsIgnoreCase(this.g.getExtProperty(RequestConstant.ENABLE_SCHEME_REPLACE))) {
            httpUrl.lockScheme();
        }
        return httpUrl;
    }

    private Request b(HttpUrl httpUrl) {
        Request.Builder requestStatistic = new Request.Builder().setUrl(httpUrl).setMethod(this.g.method).setBody(this.g.bodyEntry).setReadTimeout(this.f2047d).setConnectTimeout(this.f2046c).setRedirectEnable(this.g.allowRedirect).setRedirectTimes(this.i).setBizId(this.g.bizId).setSeq(this.e).setRequestStatistic(this.f2045b);
        requestStatistic.setParams(this.g.params);
        if (this.g.charset != null) {
            requestStatistic.setCharset(this.g.charset);
        }
        requestStatistic.setHeaders(c(httpUrl));
        return requestStatistic.build();
    }

    public int b() {
        return this.f2047d * (this.j + 1);
    }

    public boolean c() {
        return this.k;
    }

    public String a(String str) {
        return this.g.getExtProperty(str);
    }

    public boolean d() {
        return this.f2044a < this.j;
    }

    public boolean e() {
        return NetworkConfigCenter.isHttpSessionEnable() && !RequestConstant.FALSE.equalsIgnoreCase(this.g.getExtProperty(RequestConstant.ENABLE_HTTP_DNS)) && (NetworkConfigCenter.isAllowHttpIpRetry() || this.f2044a == 0);
    }

    public HttpUrl f() {
        return this.h.getHttpUrl();
    }

    public String g() {
        return this.h.getUrlString();
    }

    public Map<String, String> h() {
        return this.h.getHeaders();
    }

    private Map<String, String> c(HttpUrl httpUrl) {
        String strHost = httpUrl.host();
        boolean z = !anet.channel.strategy.utils.c.a(strHost);
        if (strHost.length() > 2 && strHost.charAt(0) == '[' && strHost.charAt(strHost.length() - 1) == ']' && anet.channel.strategy.utils.c.b(strHost.substring(1, strHost.length() - 1))) {
            z = false;
        }
        HashMap map = new HashMap();
        if (this.g.headers != null) {
            for (Map.Entry<String, String> entry : this.g.headers.entrySet()) {
                String key = entry.getKey();
                if (!"Host".equalsIgnoreCase(key) && !":host".equalsIgnoreCase(key)) {
                    boolean zEqualsIgnoreCase = "true".equalsIgnoreCase(this.g.getExtProperty(RequestConstant.KEEP_CUSTOM_COOKIE));
                    if (!"Cookie".equalsIgnoreCase(key) || zEqualsIgnoreCase) {
                        map.put(key, entry.getValue());
                    }
                } else if (!z) {
                    map.put("Host", entry.getValue());
                }
            }
        }
        return map;
    }

    public boolean i() {
        return !RequestConstant.FALSE.equalsIgnoreCase(this.g.getExtProperty(RequestConstant.ENABLE_COOKIE));
    }

    public boolean j() {
        return "true".equals(this.g.getExtProperty(RequestConstant.CHECK_CONTENT_LENGTH));
    }

    public void k() {
        this.f2044a++;
        this.f2045b.retryTimes = this.f2044a;
    }

    public void a(HttpUrl httpUrl) {
        ALog.i("anet.RequestConfig", "redirect", this.e, "to url", httpUrl.toString());
        this.i++;
        this.f2045b.url = httpUrl.simpleUrlString();
        this.h = b(httpUrl);
    }
}
