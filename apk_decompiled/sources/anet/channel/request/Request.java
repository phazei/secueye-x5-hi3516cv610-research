package anet.channel.request;

import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.statist.RequestStatistic;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import kotlin.text.Typography;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class Request {
    public static final String DEFAULT_CHARSET = "UTF-8";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final RequestStatistic f1794a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private HttpUrl f1795b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private HttpUrl f1796c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private HttpUrl f1797d;
    private URL e;
    private String f;
    private Map<String, String> g;
    private Map<String, String> h;
    private String i;
    private BodyEntry j;
    private boolean k;
    private String l;
    private String m;
    private int n;
    private int o;
    private int p;
    private HostnameVerifier q;
    private SSLSocketFactory r;
    private boolean s;

    /* JADX INFO: compiled from: Taobao */
    public static final class Method {
        public static final String DELETE = "DELETE";
        public static final String GET = "GET";
        public static final String HEAD = "HEAD";
        public static final String OPTION = "OPTIONS";
        public static final String POST = "POST";
        public static final String PUT = "PUT";

        static boolean a(String str) {
            return str.equals("POST") || str.equals(PUT);
        }

        static boolean b(String str) {
            return a(str) || str.equals("DELETE") || str.equals(OPTION);
        }
    }

    private Request(Builder builder) {
        this.f = "GET";
        this.k = true;
        this.n = 0;
        this.o = 10000;
        this.p = 10000;
        this.f = builder.f1800c;
        this.g = builder.f1801d;
        this.h = builder.e;
        this.j = builder.g;
        this.i = builder.f;
        this.k = builder.h;
        this.n = builder.i;
        this.q = builder.j;
        this.r = builder.k;
        this.l = builder.l;
        this.m = builder.m;
        this.o = builder.n;
        this.p = builder.o;
        this.f1795b = builder.f1798a;
        this.f1796c = builder.f1799b;
        if (this.f1796c == null) {
            b();
        }
        this.f1794a = builder.p != null ? builder.p : new RequestStatistic(getHost(), this.l);
        this.s = builder.q;
    }

    public Builder newBuilder() {
        Builder builder = new Builder();
        builder.f1800c = this.f;
        builder.f1801d = a();
        builder.e = this.h;
        builder.g = this.j;
        builder.f = this.i;
        builder.h = this.k;
        builder.i = this.n;
        builder.j = this.q;
        builder.k = this.r;
        builder.f1798a = this.f1795b;
        builder.f1799b = this.f1796c;
        builder.l = this.l;
        builder.m = this.m;
        builder.n = this.o;
        builder.o = this.p;
        builder.p = this.f1794a;
        builder.q = this.s;
        return builder;
    }

    private Map<String, String> a() {
        if (AwcnConfig.isCookieHeaderRedundantFix()) {
            return new HashMap(this.g);
        }
        return this.g;
    }

    public HttpUrl getHttpUrl() {
        return this.f1796c;
    }

    public String getUrlString() {
        return this.f1796c.urlString();
    }

    public URL getUrl() {
        if (this.e == null) {
            HttpUrl httpUrl = this.f1797d;
            if (httpUrl == null) {
                httpUrl = this.f1796c;
            }
            this.e = httpUrl.toURL();
        }
        return this.e;
    }

    public void setDnsOptimize(String str, int i) {
        if (str != null) {
            if (this.f1797d == null) {
                this.f1797d = new HttpUrl(this.f1796c);
            }
            this.f1797d.replaceIpAndPort(str, i);
        } else {
            this.f1797d = null;
        }
        this.e = null;
        this.f1794a.setIPAndPort(str, i);
    }

    public void setUrlScheme(boolean z) {
        if (this.f1797d == null) {
            this.f1797d = new HttpUrl(this.f1796c);
        }
        this.f1797d.setScheme(z ? HttpConstant.HTTPS : HttpConstant.HTTP);
        this.e = null;
    }

    public int getRedirectTimes() {
        return this.n;
    }

    public String getHost() {
        return this.f1796c.host();
    }

    public String getMethod() {
        return this.f;
    }

    public Map<String, String> getHeaders() {
        return Collections.unmodifiableMap(this.g);
    }

    public String getContentEncoding() {
        String str = this.i;
        return str != null ? str : "UTF-8";
    }

    public boolean isRedirectEnable() {
        return this.k;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.q;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return this.r;
    }

    public int postBody(OutputStream outputStream) throws IOException {
        BodyEntry bodyEntry = this.j;
        if (bodyEntry != null) {
            return bodyEntry.writeTo(outputStream);
        }
        return 0;
    }

    public byte[] getBodyBytes() {
        if (this.j == null) {
            return null;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(128);
        try {
            postBody(byteArrayOutputStream);
        } catch (IOException unused) {
        }
        return byteArrayOutputStream.toByteArray();
    }

    public boolean containsBody() {
        return this.j != null;
    }

    public String getBizId() {
        return this.l;
    }

    public String getSeq() {
        return this.m;
    }

    public int getReadTimeout() {
        return this.p;
    }

    public int getConnectTimeout() {
        return this.o;
    }

    public boolean isAllowRequestInBg() {
        return this.s;
    }

    private void b() {
        String strA = anet.channel.strategy.utils.c.a(this.h, getContentEncoding());
        if (!TextUtils.isEmpty(strA)) {
            if (!Method.a(this.f) || this.j != null) {
                String strUrlString = this.f1795b.urlString();
                StringBuilder sb = new StringBuilder(strUrlString);
                if (sb.indexOf("?") == -1) {
                    sb.append('?');
                } else if (strUrlString.charAt(strUrlString.length() - 1) != '&') {
                    sb.append(Typography.amp);
                }
                sb.append(strA);
                HttpUrl httpUrl = HttpUrl.parse(sb.toString());
                if (httpUrl != null) {
                    this.f1796c = httpUrl;
                }
            } else {
                try {
                    this.j = new ByteArrayEntry(strA.getBytes(getContentEncoding()));
                    this.g.put("Content-Type", "application/x-www-form-urlencoded; charset=" + getContentEncoding());
                } catch (UnsupportedEncodingException unused) {
                }
            }
        }
        if (this.f1796c == null) {
            this.f1796c = this.f1795b;
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class Builder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private HttpUrl f1798a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private HttpUrl f1799b;
        private Map<String, String> e;
        private String f;
        private BodyEntry g;
        private HostnameVerifier j;
        private SSLSocketFactory k;
        private String l;
        private String m;
        private boolean q;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private String f1800c = "GET";

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        private Map<String, String> f1801d = new HashMap();
        private boolean h = true;
        private int i = 0;
        private int n = 10000;
        private int o = 10000;
        private RequestStatistic p = null;

        public Builder setUrl(HttpUrl httpUrl) {
            this.f1798a = httpUrl;
            this.f1799b = null;
            return this;
        }

        public Builder setUrl(String str) {
            this.f1798a = HttpUrl.parse(str);
            this.f1799b = null;
            if (this.f1798a != null) {
                return this;
            }
            throw new IllegalArgumentException("toURL is invalid! toURL = " + str);
        }

        public Builder setMethod(String str) {
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("method is null or empty");
            }
            if ("GET".equalsIgnoreCase(str)) {
                this.f1800c = "GET";
            } else if ("POST".equalsIgnoreCase(str)) {
                this.f1800c = "POST";
            } else if (Method.OPTION.equalsIgnoreCase(str)) {
                this.f1800c = Method.OPTION;
            } else if (Method.HEAD.equalsIgnoreCase(str)) {
                this.f1800c = Method.HEAD;
            } else if (Method.PUT.equalsIgnoreCase(str)) {
                this.f1800c = Method.PUT;
            } else if ("DELETE".equalsIgnoreCase(str)) {
                this.f1800c = "DELETE";
            } else {
                this.f1800c = "GET";
            }
            return this;
        }

        public Builder setHeaders(Map<String, String> map) {
            this.f1801d.clear();
            if (map != null) {
                this.f1801d.putAll(map);
            }
            return this;
        }

        public Builder addHeader(String str, String str2) {
            this.f1801d.put(str, str2);
            return this;
        }

        public Builder setParams(Map<String, String> map) {
            this.e = map;
            this.f1799b = null;
            return this;
        }

        public Builder addParam(String str, String str2) {
            if (this.e == null) {
                this.e = new HashMap();
            }
            this.e.put(str, str2);
            this.f1799b = null;
            return this;
        }

        public Builder setCharset(String str) {
            this.f = str;
            this.f1799b = null;
            return this;
        }

        public Builder setBody(BodyEntry bodyEntry) {
            this.g = bodyEntry;
            return this;
        }

        public Builder setRedirectEnable(boolean z) {
            this.h = z;
            return this;
        }

        public Builder setRedirectTimes(int i) {
            this.i = i;
            return this;
        }

        public Builder setHostnameVerifier(HostnameVerifier hostnameVerifier) {
            this.j = hostnameVerifier;
            return this;
        }

        public Builder setSslSocketFactory(SSLSocketFactory sSLSocketFactory) {
            this.k = sSLSocketFactory;
            return this;
        }

        public Builder setBizId(String str) {
            this.l = str;
            return this;
        }

        public Builder setSeq(String str) {
            this.m = str;
            return this;
        }

        public Builder setReadTimeout(int i) {
            if (i > 0) {
                this.o = i;
            }
            return this;
        }

        public Builder setConnectTimeout(int i) {
            if (i > 0) {
                this.n = i;
            }
            return this;
        }

        public Builder setRequestStatistic(RequestStatistic requestStatistic) {
            this.p = requestStatistic;
            return this;
        }

        public Builder setAllowRequestInBg(boolean z) {
            this.q = z;
            return this;
        }

        public Request build() {
            if (this.g == null && this.e == null && Method.a(this.f1800c)) {
                ALog.e("awcn.Request", "method " + this.f1800c + " must have a request body", null, new Object[0]);
            }
            if (this.g != null && !Method.b(this.f1800c)) {
                ALog.e("awcn.Request", "method " + this.f1800c + " should not have a request body", null, new Object[0]);
                this.g = null;
            }
            BodyEntry bodyEntry = this.g;
            if (bodyEntry != null && bodyEntry.getContentType() != null) {
                addHeader("Content-Type", this.g.getContentType());
            }
            return new Request(this);
        }
    }
}
