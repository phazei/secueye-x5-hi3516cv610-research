package com.aliyun.alink.h2.api;

import com.aliyun.alink.h2.a.a;
import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.connection.ConnectionStatus;
import com.aliyun.alink.h2.connection.b;
import com.aliyun.alink.h2.connection.c;
import com.aliyun.alink.h2.entity.Http2Request;
import com.aliyun.alink.h2.netty.g;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import io.netty.handler.codec.http2.DefaultHttp2Headers;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Settings;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes2.dex */
public class IotHttp2Client {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static long[] f3801b = {0, 1000, 10000, 60000, 600000};

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final SocketAddress f3802a;
    private int e;
    private ScheduledExecutorService f;
    private c h;
    private a i;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f3803c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private AtomicInteger f3804d = new AtomicInteger(1);
    private AtomicBoolean g = new AtomicBoolean(false);
    private b j = new b() { // from class: com.aliyun.alink.h2.api.IotHttp2Client.1
        @Override // com.aliyun.alink.h2.connection.b
        public void onSettingReceive(Connection connection, Http2Settings http2Settings) {
            if (http2Settings.containsKey('c')) {
                int iIntValue = http2Settings.getIntValue('c').intValue();
                if (IotHttp2Client.this.e != -1 && IotHttp2Client.this.e >= 0) {
                    iIntValue = Math.min(IotHttp2Client.this.e, iIntValue);
                    com.aliyun.alink.h2.b.a.b("IotHttp2Client", "maxConnectionCount: " + IotHttp2Client.this.e + ", server setting: " + iIntValue);
                }
                IotHttp2Client.this.f3804d.set(iIntValue);
                com.aliyun.alink.h2.b.a.b("IotHttp2Client", "receive setting, connection: " + connection + ", subscription count : " + IotHttp2Client.this.f3804d);
            }
        }

        @Override // com.aliyun.alink.h2.connection.b
        public void onStatusChange(ConnectionStatus connectionStatus, Connection connection) {
            com.aliyun.alink.h2.b.a.b("IotHttp2Client", "connection status changed, connection: " + connection + ", status: " + connectionStatus);
            if (connectionStatus == ConnectionStatus.AUTHORIZED && IotHttp2Client.this.g.compareAndSet(false, true) && IotHttp2Client.this.f != null) {
                IotHttp2Client.this.f.scheduleWithFixedDelay(new Runnable() { // from class: com.aliyun.alink.h2.api.IotHttp2Client.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        IotHttp2Client.this.a();
                    }
                }, 10000L, 10000L, TimeUnit.MILLISECONDS);
            }
            if (connection.getStatus() == ConnectionStatus.AUTHORIZED && connectionStatus == ConnectionStatus.CLOSED) {
                IotHttp2Client.this.f.submit(new Runnable() { // from class: com.aliyun.alink.h2.api.IotHttp2Client.1.2
                    @Override // java.lang.Runnable
                    public void run() {
                        IotHttp2Client.this.a();
                    }
                });
            }
        }
    };

    public IotHttp2Client(Profile profile, int i) {
        this.e = -1;
        this.f3802a = new InetSocketAddress(profile.getHost(), profile.getPort());
        this.h = new com.aliyun.alink.h2.connection.a.b(true, profile.getHeartBeatInterval(), profile.getHeartBeatTimeOut());
        this.h.a(this.j);
        this.e = i;
        this.f = new ScheduledThreadPoolExecutor(1, new g().a(true).a("iot-client-thread-%d").a());
        this.i = com.aliyun.alink.h2.a.b.a(profile);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        long size = allConnections().size();
        int iMin = this.f3804d.get();
        int i = this.e;
        if (i >= 0) {
            iMin = Math.min(i, iMin);
        }
        long j = iMin;
        if (size >= j) {
            return;
        }
        com.aliyun.alink.h2.b.a.b("IotHttp2Client", "update connection count, current count " + size + ", expected count " + iMin);
        int i2 = this.f3803c;
        if (i2 != 0) {
            long[] jArr = f3801b;
            long j2 = jArr[i2 % jArr.length];
            try {
                com.aliyun.alink.h2.b.a.b("IotHttp2Client", "backoff, create connection after " + j2 + "ms");
                Thread.sleep(j2);
            } catch (InterruptedException e) {
                com.aliyun.alink.h2.b.a.a("IotHttp2Client", "error occurs while backoff, exception: ", e);
            }
        }
        for (int i3 = 0; i3 < j - size; i3++) {
            try {
                newConnection();
            } catch (Throwable th) {
                this.f3803c++;
                com.aliyun.alink.h2.b.a.d("failed to create connection, {}", th.getMessage());
            }
        }
        long size2 = allConnections().size();
        if (size2 == j) {
            this.f3803c = 0;
        }
        com.aliyun.alink.h2.b.a.b("IotHttp2Client", "finish updating connection count, current count " + size2);
    }

    public Connection newConnection() {
        return this.h.a(this.f3802a);
    }

    public Http2Headers authHeader() {
        DefaultHttp2Headers defaultHttp2Headers = new DefaultHttp2Headers();
        Map<String, String> mapA = this.i.a();
        if (mapA != null) {
            for (Map.Entry<String, String> entry : mapA.entrySet()) {
                defaultHttp2Headers.set("x-auth-" + entry.getKey(), entry.getValue());
            }
        }
        return defaultHttp2Headers;
    }

    public void shutdown() {
        com.aliyun.alink.h2.b.a.b("IotHttp2Client", "shutdown http2 client");
        List<Connection> listAllConnections = allConnections();
        if (listAllConnections != null) {
            for (int i = 0; i < listAllConnections.size(); i++) {
                Connection connection = listAllConnections.get(i);
                if (connection != null) {
                    connection.removeConnectListener();
                }
            }
        }
        this.h.b(this.j);
        this.h.b();
        ScheduledExecutorService scheduledExecutorService = this.f;
        if (scheduledExecutorService != null) {
            scheduledExecutorService.shutdown();
        }
    }

    public void addConnectionListener(b bVar) {
        this.h.a(bVar);
    }

    public void removeConnectionListener(b bVar) {
        this.h.b(bVar);
    }

    public List<Connection> allConnections() {
        return this.h.a();
    }

    public void sendRequest(Connection connection, Http2Request http2Request, Http2StreamListener http2StreamListener, final CompletableListener<StreamWriteContext> completableListener) {
        if (http2Request == null) {
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("param request is invalid."));
                return;
            }
            return;
        }
        final boolean zIsEndOfStream = http2Request.isEndOfStream();
        final byte[] content = http2Request.getContent();
        Http2Headers headers = http2Request.getHeaders();
        int length = content != null ? content.length : 0;
        if (!headers.contains("content-length")) {
            headers.set("content-length", String.valueOf(length));
        }
        if (http2Request.getH2StreamId() != 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("sendRequest send data=");
            sb.append(content == null ? TmpConstant.GROUP_ROLE_UNKNOWN : Integer.valueOf(content.length));
            com.aliyun.alink.h2.b.a.b("IotHttp2Client", sb.toString());
            if (content != null && content.length > 0) {
                connection.writeData(http2Request.getH2StreamId(), content, zIsEndOfStream, completableListener);
                return;
            } else {
                if (completableListener != null) {
                    completableListener.completeExceptionally(new H2ClientException("send stream with stream id, data is null."));
                    return;
                }
                return;
            }
        }
        boolean z = (content == null || content.length < 1) && zIsEndOfStream;
        final boolean z2 = z;
        connection.writeHeaders(headers, z, http2StreamListener, new CompletableListener<StreamWriteContext>() { // from class: com.aliyun.alink.h2.api.IotHttp2Client.2
            @Override // com.aliyun.alink.h2.api.CompletableListener
            public void complete(StreamWriteContext streamWriteContext) {
                com.aliyun.alink.h2.b.a.a("IotHttp2Client", "sendAsync complete() called with: result = [" + streamWriteContext + "]");
                byte[] bArr = content;
                if (bArr != null && bArr.length > 0) {
                    IotHttp2Client.this.a(streamWriteContext, bArr, zIsEndOfStream, completableListener);
                    return;
                }
                CompletableListener completableListener2 = completableListener;
                if (completableListener2 != null) {
                    completableListener2.complete(streamWriteContext);
                }
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            public void completeExceptionally(Throwable th) {
                CompletableListener completableListener2;
                com.aliyun.alink.h2.b.a.a("IotHttp2Client", "sendAsync completeExceptionally() called with: throwable = [" + th + "]");
                if (!z2 || (completableListener2 = completableListener) == null) {
                    return;
                }
                completableListener2.completeExceptionally(th);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(StreamWriteContext streamWriteContext, byte[] bArr, boolean z, CompletableListener<StreamWriteContext> completableListener) {
        com.aliyun.alink.h2.b.a.a("IotHttp2Client", "writeData() called with: streamWriteContext = [" + streamWriteContext + "], data = [" + bArr + "], endStream = [" + z + "], completableListener = [" + completableListener + "]");
        if (streamWriteContext != null) {
            streamWriteContext.writeData(bArr, z, completableListener);
        } else if (completableListener != null) {
            completableListener.completeExceptionally(new H2ClientException("writeData streamWriteContext is null."));
        }
    }

    public Connection randomConnection() {
        List<Connection> listAllConnections = allConnections();
        ArrayList arrayList = new ArrayList();
        if (listAllConnections == null || listAllConnections.isEmpty()) {
            return null;
        }
        for (int i = 0; i < listAllConnections.size(); i++) {
            Connection connection = listAllConnections.get(i);
            if (connection != null && connection.isAuthorized()) {
                arrayList.add(connection);
            }
        }
        return (Connection) arrayList.get(new Random().nextInt(arrayList.size()));
    }
}
