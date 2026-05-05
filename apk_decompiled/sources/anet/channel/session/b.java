package anet.channel.session;

import android.os.Build;
import android.util.Pair;
import anet.channel.RequestCb;
import anet.channel.appmonitor.AppMonitor;
import anet.channel.bytes.ByteArray;
import anet.channel.bytes.a;
import anet.channel.request.Request;
import anet.channel.statist.ExceptionStatistic;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.util.ALog;
import anet.channel.util.ErrorConstant;
import anet.channel.util.HttpConstant;
import anet.channel.util.StringUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.zip.GZIPInputStream;
import javax.net.ssl.HttpsURLConnection;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: compiled from: Taobao */
    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public int f1817a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public byte[] f1818b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public Map<String, List<String>> f1819c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public int f1820d;
        public boolean e;
    }

    private b() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:141:0x043a A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:158:? A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r19v0, types: [anet.channel.RequestCb] */
    /* JADX WARN: Type inference failed for: r5v33, types: [java.net.HttpURLConnection] */
    /* JADX WARN: Type inference failed for: r5v35 */
    /* JADX WARN: Type inference failed for: r5v37 */
    /* JADX WARN: Type inference failed for: r6v1, types: [boolean] */
    /* JADX WARN: Type inference failed for: r6v13 */
    /* JADX WARN: Type inference failed for: r6v35, types: [int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static anet.channel.session.b.a a(anet.channel.request.Request r18, anet.channel.RequestCb r19) {
        /*
            Method dump skipped, instruction units count: 1115
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.session.b.a(anet.channel.request.Request, anet.channel.RequestCb):anet.channel.session.b$a");
    }

    private static void a(Request request, a aVar, RequestCb requestCb, int i, Throwable th) {
        String errMsg = ErrorConstant.getErrMsg(i);
        ALog.e("awcn.HttpConnector", "onException", request.getSeq(), "errorCode", Integer.valueOf(i), AlinkConstants.KEY_ERR_MSG, errMsg, "url", request.getUrlString(), "host", request.getHost());
        if (aVar != null) {
            aVar.f1817a = i;
        }
        if (!request.f1794a.isDone.get()) {
            request.f1794a.statusCode = i;
            request.f1794a.msg = errMsg;
            request.f1794a.rspEnd = System.currentTimeMillis();
            if (i != -204) {
                AppMonitor.getInstance().commitStat(new ExceptionStatistic(i, errMsg, request.f1794a, th));
            }
        }
        if (requestCb != null) {
            requestCb.onFinish(i, errMsg, request.f1794a);
        }
    }

    private static HttpURLConnection a(Request request) throws IOException {
        HttpURLConnection httpURLConnection;
        Pair<String, Integer> wifiProxy = NetworkStatusHelper.getWifiProxy();
        Proxy proxy = wifiProxy != null ? new Proxy(Proxy.Type.HTTP, new InetSocketAddress((String) wifiProxy.first, ((Integer) wifiProxy.second).intValue())) : null;
        anet.channel.util.g gVarA = anet.channel.util.g.a();
        if (NetworkStatusHelper.getStatus().isMobile() && gVarA != null) {
            proxy = gVarA.b();
        }
        URL url = request.getUrl();
        if (proxy != null) {
            httpURLConnection = (HttpURLConnection) url.openConnection(proxy);
        } else {
            httpURLConnection = (HttpURLConnection) url.openConnection();
        }
        httpURLConnection.setConnectTimeout(request.getConnectTimeout());
        httpURLConnection.setReadTimeout(request.getReadTimeout());
        httpURLConnection.setRequestMethod(request.getMethod());
        if (request.containsBody()) {
            httpURLConnection.setDoOutput(true);
        }
        Map<String, String> headers = request.getHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            httpURLConnection.addRequestProperty(entry.getKey(), entry.getValue());
        }
        String host = headers.get("Host");
        if (host == null) {
            host = request.getHost();
        }
        String strConcatString = request.getHttpUrl().containsNonDefaultPort() ? StringUtils.concatString(host, ":", String.valueOf(request.getHttpUrl().getPort())) : host;
        httpURLConnection.setRequestProperty("Host", strConcatString);
        if (NetworkStatusHelper.getApn().equals("cmwap")) {
            httpURLConnection.setRequestProperty(HttpConstant.X_ONLINE_HOST, strConcatString);
        }
        if (!headers.containsKey("Accept-Encoding")) {
            httpURLConnection.setRequestProperty("Accept-Encoding", "gzip");
        }
        if (gVarA != null) {
            httpURLConnection.setRequestProperty("Authorization", gVarA.c());
        }
        if (url.getProtocol().equalsIgnoreCase(HttpConstant.HTTPS)) {
            a(httpURLConnection, request, host);
        }
        httpURLConnection.setInstanceFollowRedirects(false);
        return httpURLConnection;
    }

    private static void a(HttpURLConnection httpURLConnection, Request request, String str) {
        if (Integer.parseInt(Build.VERSION.SDK) < 8) {
            ALog.e("awcn.HttpConnector", "supportHttps", "[supportHttps]Froyo 以下版本不支持https", new Object[0]);
            return;
        }
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
        if (request.getSslSocketFactory() != null) {
            httpsURLConnection.setSSLSocketFactory(request.getSslSocketFactory());
        } else if (anet.channel.util.b.f1937a != null) {
            httpsURLConnection.setSSLSocketFactory(anet.channel.util.b.f1937a);
            if (ALog.isPrintLog(2)) {
                ALog.i("awcn.HttpConnector", "HttpSslUtil", request.getSeq(), "SslSocketFactory", anet.channel.util.b.f1937a);
            }
        }
        if (request.getHostnameVerifier() != null) {
            httpsURLConnection.setHostnameVerifier(request.getHostnameVerifier());
            return;
        }
        if (anet.channel.util.b.f1938b != null) {
            httpsURLConnection.setHostnameVerifier(anet.channel.util.b.f1938b);
            if (ALog.isPrintLog(2)) {
                ALog.i("awcn.HttpConnector", "HttpSslUtil", request.getSeq(), "HostnameVerifier", anet.channel.util.b.f1938b);
                return;
            }
            return;
        }
        httpsURLConnection.setHostnameVerifier(new c(str));
    }

    private static int a(HttpURLConnection httpURLConnection, Request request) {
        int i = 0;
        if (request.containsBody()) {
            OutputStream outputStream = null;
            try {
                try {
                    outputStream = httpURLConnection.getOutputStream();
                    int iPostBody = request.postBody(outputStream);
                    if (outputStream != null) {
                        try {
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e) {
                            ALog.e("awcn.HttpConnector", "postData", request.getSeq(), e, new Object[0]);
                        }
                    }
                    i = iPostBody;
                } catch (Exception e2) {
                    ALog.e("awcn.HttpConnector", "postData error", request.getSeq(), e2, new Object[0]);
                    if (outputStream != null) {
                        try {
                            outputStream.flush();
                            outputStream.close();
                        } catch (IOException e3) {
                            ALog.e("awcn.HttpConnector", "postData", request.getSeq(), e3, new Object[0]);
                        }
                    }
                }
                long j = i;
                request.f1794a.reqBodyInflateSize = j;
                request.f1794a.reqBodyDeflateSize = j;
                request.f1794a.sendDataSize = j;
            } catch (Throwable th) {
                if (outputStream != null) {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e4) {
                        ALog.e("awcn.HttpConnector", "postData", request.getSeq(), e4, new Object[0]);
                    }
                }
                throw th;
            }
        }
        return i;
    }

    private static void a(HttpURLConnection httpURLConnection, Request request, a aVar, RequestCb requestCb) throws Throwable {
        InputStream errorStream;
        ByteArrayOutputStream byteArrayOutputStream;
        anet.channel.util.a aVar2;
        httpURLConnection.getURL().toString();
        try {
            errorStream = httpURLConnection.getInputStream();
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                ALog.w("awcn.HttpConnector", "File not found", request.getSeq(), "url", request.getUrlString());
            }
            try {
                errorStream = httpURLConnection.getErrorStream();
            } catch (Exception e2) {
                ALog.e("awcn.HttpConnector", "get error stream failed.", request.getSeq(), e2, new Object[0]);
                errorStream = null;
            }
        }
        if (errorStream == null) {
            a(request, aVar, requestCb, -404, null);
            return;
        }
        if (requestCb == null) {
            byteArrayOutputStream = new ByteArrayOutputStream(aVar.f1820d <= 0 ? 1024 : aVar.e ? 2 * aVar.f1820d : aVar.f1820d);
        } else {
            byteArrayOutputStream = null;
        }
        try {
            aVar2 = new anet.channel.util.a(errorStream);
            try {
                InputStream gZIPInputStream = aVar.e ? new GZIPInputStream(aVar2) : aVar2;
                ByteArray byteArrayA = null;
                while (!Thread.currentThread().isInterrupted()) {
                    if (byteArrayA == null) {
                        byteArrayA = a.C0170a.f1676a.a(2048);
                    }
                    int from = byteArrayA.readFrom(gZIPInputStream);
                    if (from != -1) {
                        if (byteArrayOutputStream != null) {
                            byteArrayA.writeTo(byteArrayOutputStream);
                        } else {
                            requestCb.onDataReceive(byteArrayA, false);
                            byteArrayA = null;
                        }
                        long j = from;
                        request.f1794a.recDataSize += j;
                        request.f1794a.rspBodyInflateSize += j;
                    } else {
                        if (byteArrayOutputStream != null) {
                            byteArrayA.recycle();
                        } else {
                            requestCb.onDataReceive(byteArrayA, true);
                        }
                        if (byteArrayOutputStream != null) {
                            aVar.f1818b = byteArrayOutputStream.toByteArray();
                        }
                        request.f1794a.recDataTime = System.currentTimeMillis() - request.f1794a.rspStart;
                        request.f1794a.rspBodyDeflateSize = aVar2.a();
                        try {
                            gZIPInputStream.close();
                            return;
                        } catch (IOException unused) {
                            return;
                        }
                    }
                }
                throw new CancellationException("task cancelled");
            } catch (Throwable th) {
                th = th;
                request.f1794a.recDataTime = System.currentTimeMillis() - request.f1794a.rspStart;
                request.f1794a.rspBodyDeflateSize = aVar2.a();
                if (errorStream != null) {
                    try {
                        errorStream.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            aVar2 = null;
        }
    }
}
