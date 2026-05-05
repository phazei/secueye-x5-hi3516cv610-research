package anet.channel.util;

import java.net.MalformedURLException;
import java.net.URL;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class HttpUrl {
    private String host;
    private volatile boolean isSchemeLocked;
    private String path;
    private int port;
    private String scheme;
    private String simpleUrl;
    private String url;

    private HttpUrl() {
        this.isSchemeLocked = false;
    }

    public HttpUrl(HttpUrl httpUrl) {
        this.isSchemeLocked = false;
        this.scheme = httpUrl.scheme;
        this.host = httpUrl.host;
        this.path = httpUrl.path;
        this.url = httpUrl.url;
        this.simpleUrl = httpUrl.simpleUrl;
        this.isSchemeLocked = httpUrl.isSchemeLocked;
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x00b3, code lost:
    
        if (r0.port > 65535) goto L53;
     */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00d2  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x00d9  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x00df  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static anet.channel.util.HttpUrl parse(java.lang.String r13) {
        /*
            Method dump skipped, instruction units count: 342
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.HttpUrl.parse(java.lang.String):anet.channel.util.HttpUrl");
    }

    public String scheme() {
        return this.scheme;
    }

    public String host() {
        return this.host;
    }

    public String path() {
        return this.path;
    }

    public int getPort() {
        return this.port;
    }

    public String urlString() {
        return this.url;
    }

    public String simpleUrlString() {
        return this.simpleUrl;
    }

    public URL toURL() {
        try {
            return new URL(this.url);
        } catch (MalformedURLException unused) {
            return null;
        }
    }

    public boolean containsNonDefaultPort() {
        return this.port != 0 && ((HttpConstant.HTTP.equals(this.scheme) && this.port != 80) || (HttpConstant.HTTPS.equals(this.scheme) && this.port != 443));
    }

    public void downgradeSchemeAndLock() {
        this.isSchemeLocked = true;
        if (HttpConstant.HTTP.equals(this.scheme)) {
            return;
        }
        this.scheme = HttpConstant.HTTP;
        String str = this.scheme;
        String str2 = this.url;
        this.url = StringUtils.concatString(str, ":", str2.substring(str2.indexOf("//")));
    }

    public boolean isSchemeLocked() {
        return this.isSchemeLocked;
    }

    public void lockScheme() {
        this.isSchemeLocked = true;
    }

    public void setScheme(String str) {
        if (this.isSchemeLocked || str.equalsIgnoreCase(this.scheme)) {
            return;
        }
        this.scheme = str;
        String str2 = this.url;
        this.url = StringUtils.concatString(str, ":", str2.substring(str2.indexOf("//")));
        this.simpleUrl = StringUtils.concatString(str, ":", this.simpleUrl.substring(this.url.indexOf("//")));
    }

    public void replaceIpAndPort(String str, int i) {
        if (str != null) {
            int iIndexOf = this.url.indexOf("//") + 2;
            while (iIndexOf < this.url.length() && this.url.charAt(iIndexOf) != '/') {
                iIndexOf++;
            }
            boolean zB = anet.channel.strategy.utils.c.b(str);
            StringBuilder sb = new StringBuilder(this.url.length() + str.length());
            sb.append(this.scheme);
            sb.append(HttpConstant.SCHEME_SPLIT);
            if (zB) {
                sb.append('[');
            }
            sb.append(str);
            if (zB) {
                sb.append(']');
            }
            if (i != 0) {
                sb.append(':');
                sb.append(i);
            } else if (this.port != 0) {
                sb.append(':');
                sb.append(this.port);
            }
            sb.append(this.url.substring(iIndexOf));
            this.url = sb.toString();
        }
    }

    public String toString() {
        return this.url;
    }
}
