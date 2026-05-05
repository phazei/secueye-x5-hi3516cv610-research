package com.aliyun.iot.aep.sdk.framework.log;

import java.io.EOFException;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.platform.Platform;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;

/* JADX INFO: loaded from: classes2.dex */
public final class HttpLoggingInterceptor implements Interceptor {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final Charset f4685a = Charset.forName("UTF-8");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final Logger f4686b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private volatile Set<String> f4687c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private volatile Level f4688d;

    public enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY
    }

    public interface Logger {
        public static final Logger DEFAULT = new Logger() { // from class: com.aliyun.iot.aep.sdk.framework.log.HttpLoggingInterceptor.Logger.1
            @Override // com.aliyun.iot.aep.sdk.framework.log.HttpLoggingInterceptor.Logger
            public void log(String str) {
                Platform.get().log(4, str, null);
            }
        };

        void log(String str);
    }

    public HttpLoggingInterceptor() {
        this(Logger.DEFAULT);
    }

    public HttpLoggingInterceptor(Logger logger) {
        this.f4687c = Collections.emptySet();
        this.f4688d = Level.NONE;
        this.f4686b = logger;
    }

    public void redactHeader(String str) {
        TreeSet treeSet = new TreeSet(String.CASE_INSENSITIVE_ORDER);
        treeSet.addAll(this.f4687c);
        treeSet.add(str);
        this.f4687c = treeSet;
    }

    public HttpLoggingInterceptor setLevel(Level level) {
        if (level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        }
        this.f4688d = level;
        return this;
    }

    public Level getLevel() {
        return this.f4688d;
    }

    @Override // okhttp3.Interceptor
    public Response intercept(Interceptor.Chain chain) throws Exception {
        Long lValueOf;
        Level level = this.f4688d;
        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }
        boolean z = level == Level.BODY;
        boolean z2 = z || level == Level.HEADERS;
        RequestBody requestBodyBody = request.body();
        boolean z3 = requestBodyBody != null;
        Connection connection = chain.connection();
        StringBuilder sb = new StringBuilder();
        sb.append("--> ");
        sb.append(request.method());
        sb.append(' ');
        sb.append(request.url());
        sb.append(connection != null ? " " + connection.protocol() : "");
        String string = sb.toString();
        if (!z2 && z3) {
            string = string + " (" + requestBodyBody.contentLength() + "-byte body)";
        }
        this.f4686b.log(string);
        if (z2) {
            if (z3) {
                if (requestBodyBody.contentType() != null) {
                    this.f4686b.log("Content-Type: " + requestBodyBody.contentType());
                }
                if (requestBodyBody.contentLength() != -1) {
                    this.f4686b.log("Content-Length: " + requestBodyBody.contentLength());
                }
            }
            Headers headers = request.headers();
            int size = headers.size();
            for (int i = 0; i < size; i++) {
                String strName = headers.name(i);
                if (!"Content-Type".equalsIgnoreCase(strName) && !"Content-Length".equalsIgnoreCase(strName)) {
                    a(headers, i);
                }
            }
            if (!z || !z3) {
                this.f4686b.log("--> END " + request.method());
            } else if (a(request.headers())) {
                this.f4686b.log("--> END " + request.method() + " (encoded body omitted)");
            } else {
                Buffer buffer = new Buffer();
                requestBodyBody.writeTo(buffer);
                Charset charset = f4685a;
                MediaType mediaTypeContentType = requestBodyBody.contentType();
                if (mediaTypeContentType != null) {
                    charset = mediaTypeContentType.charset(f4685a);
                }
                this.f4686b.log("");
                if (a(buffer)) {
                    if (charset != null) {
                        this.f4686b.log(buffer.readString(charset));
                    }
                    this.f4686b.log("--> END " + request.method() + " (" + requestBodyBody.contentLength() + "-byte body)");
                } else {
                    this.f4686b.log("--> END " + request.method() + " (binary " + requestBodyBody.contentLength() + "-byte body omitted)");
                }
            }
        }
        long jNanoTime = System.nanoTime();
        try {
            Response responseProceed = chain.proceed(request);
            long millis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - jNanoTime);
            ResponseBody responseBodyBody = responseProceed.body();
            long jContentLength = responseBodyBody.contentLength();
            String str = jContentLength != -1 ? jContentLength + "-byte" : "unknown-length";
            Logger logger = this.f4686b;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("<-- ");
            sb2.append(responseProceed.code());
            sb2.append(responseProceed.message().isEmpty() ? "" : ' ' + responseProceed.message());
            sb2.append(' ');
            sb2.append(responseProceed.request().url());
            sb2.append(" (");
            sb2.append(millis);
            sb2.append("ms");
            sb2.append(z2 ? "" : ", " + str + " body");
            sb2.append(')');
            logger.log(sb2.toString());
            if (z2) {
                Headers headers2 = responseProceed.headers();
                int size2 = headers2.size();
                for (int i2 = 0; i2 < size2; i2++) {
                    a(headers2, i2);
                }
                if (!z || !HttpHeaders.hasBody(responseProceed)) {
                    this.f4686b.log("<-- END HTTP");
                } else if (a(responseProceed.headers())) {
                    this.f4686b.log("<-- END HTTP (encoded body omitted)");
                } else {
                    BufferedSource bufferedSourceSource = responseBodyBody.source();
                    bufferedSourceSource.request(Long.MAX_VALUE);
                    Buffer buffer2 = bufferedSourceSource.buffer();
                    GzipSource gzipSource = null;
                    if ("gzip".equalsIgnoreCase(headers2.get("Content-Encoding"))) {
                        lValueOf = Long.valueOf(buffer2.size());
                        try {
                            GzipSource gzipSource2 = new GzipSource(buffer2.clone());
                            try {
                                buffer2 = new Buffer();
                                buffer2.writeAll(gzipSource2);
                                gzipSource2.close();
                            } catch (Throwable th) {
                                th = th;
                                gzipSource = gzipSource2;
                                if (gzipSource != null) {
                                    gzipSource.close();
                                }
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                        }
                    } else {
                        lValueOf = null;
                    }
                    Charset charset2 = f4685a;
                    MediaType mediaTypeContentType2 = responseBodyBody.contentType();
                    if (mediaTypeContentType2 != null) {
                        charset2 = mediaTypeContentType2.charset(f4685a);
                    }
                    if (!a(buffer2)) {
                        this.f4686b.log("");
                        this.f4686b.log("<-- END HTTP (binary " + buffer2.size() + "-byte body omitted)");
                        return responseProceed;
                    }
                    if (jContentLength != 0) {
                        this.f4686b.log("");
                        this.f4686b.log(buffer2.clone().readString(charset2));
                    }
                    if (lValueOf != null) {
                        this.f4686b.log("<-- END HTTP (" + buffer2.size() + "-byte, " + lValueOf + "-gzipped-byte body)");
                    } else {
                        this.f4686b.log("<-- END HTTP (" + buffer2.size() + "-byte body)");
                    }
                }
            }
            return responseProceed;
        } catch (Exception e) {
            this.f4686b.log("<-- HTTP FAILED: " + e);
            throw e;
        }
    }

    private void a(Headers headers, int i) {
        String strValue = this.f4687c.contains(headers.name(i)) ? "██" : headers.value(i);
        this.f4686b.log(headers.name(i) + ": " + strValue);
    }

    static boolean a(Buffer buffer) {
        try {
            Buffer buffer2 = new Buffer();
            buffer.copyTo(buffer2, 0L, buffer.size() < 64 ? buffer.size() : 64L);
            for (int i = 0; i < 16; i++) {
                if (buffer2.exhausted()) {
                    return true;
                }
                int utf8CodePoint = buffer2.readUtf8CodePoint();
                if (Character.isISOControl(utf8CodePoint) && !Character.isWhitespace(utf8CodePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException unused) {
            return false;
        }
    }

    private static boolean a(Headers headers) {
        String str = headers.get("Content-Encoding");
        return (str == null || str.equalsIgnoreCase("identity") || str.equalsIgnoreCase("gzip")) ? false : true;
    }
}
