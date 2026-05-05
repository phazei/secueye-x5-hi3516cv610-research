package com.aliyun.alink.h2.stream.a;

import com.aliyun.alink.h2.api.CompletableListener;
import com.aliyun.alink.h2.api.H2ClientException;
import com.aliyun.alink.h2.api.IotHttp2Client;
import com.aliyun.alink.h2.api.Profile;
import com.aliyun.alink.h2.api.StreamWriteContext;
import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.connection.ConnectionStatus;
import com.aliyun.alink.h2.entity.Http2Request;
import com.aliyun.alink.h2.entity.Http2Response;
import com.aliyun.alink.h2.stream.api.IDownStreamListener;
import com.aliyun.alink.h2.stream.api.IStreamSender;
import com.aliyun.alink.h2.stream.api.StreamServiceContext;
import com.aliyun.alink.h2.stream.entity.StreamData;
import com.aliyun.alink.h2.stream.utils.StreamUtil;
import com.aliyun.alink.h2.utils.ThreadPool;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Stream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: StreamSenderImpl.java */
/* JADX INFO: loaded from: classes2.dex */
public class d implements IStreamSender {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private IotHttp2Client f3910a;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Profile f3913d;
    private Connection e = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private AtomicBoolean f3911b = new AtomicBoolean(false);

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private AtomicBoolean f3912c = new AtomicBoolean(false);

    public d(Profile profile) {
        this.f3913d = profile;
    }

    @Override // com.aliyun.alink.h2.stream.api.IStreamSender
    public void connect(final CompletableListener completableListener) {
        com.aliyun.alink.h2.stream.c.a.b("StreamSenderImpl", "connect() called");
        if (this.f3911b.compareAndSet(false, true)) {
            ThreadPool.execute(new Runnable() { // from class: com.aliyun.alink.h2.stream.a.d.1
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        d.this.f3910a = new IotHttp2Client(d.this.f3913d, 1);
                        Connection connectionRandomConnection = d.this.f3910a.randomConnection();
                        if (connectionRandomConnection == null) {
                            d.this.e = d.this.f3910a.newConnection();
                            com.aliyun.alink.h2.stream.c.a.a("StreamSenderImpl", "connect new connection " + d.this.e);
                        } else {
                            com.aliyun.alink.h2.stream.c.a.a("StreamSenderImpl", "connect reuse connection " + connectionRandomConnection);
                            d.this.e = connectionRandomConnection;
                        }
                        StreamUtil.setupConnection(d.this.e, null);
                        d.this.f3912c.set(true);
                        if (completableListener != null) {
                            completableListener.complete(true);
                        }
                    } catch (Exception e) {
                        d.this.f3912c.set(false);
                        e.printStackTrace();
                        CompletableListener completableListener2 = completableListener;
                        if (completableListener2 != null) {
                            completableListener2.complete(e);
                        }
                    }
                }
            });
        } else if (!this.f3912c.compareAndSet(true, true)) {
            com.aliyun.alink.h2.stream.c.a.c("StreamSenderImpl", "is connecting.");
        } else if (completableListener != null) {
            completableListener.complete(true);
        }
    }

    @Override // com.aliyun.alink.h2.stream.api.IStreamSender
    public void openStream(String str, Http2Request http2Request, CompletableListener<Http2Response> completableListener) {
        com.aliyun.alink.h2.stream.c.a.b("StreamSenderImpl", "openStream() called with: serviceName = [" + str + "], request = [" + http2Request + "], listener = [" + completableListener + "]");
        try {
            StreamUtil.checkServiceName(str);
            if (!isConnected()) {
                if (completableListener != null) {
                    completableListener.completeExceptionally(new H2ClientException("not connected."));
                    return;
                }
                return;
            }
            if (http2Request == null) {
                com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "openStream request is null.");
                if (completableListener != null) {
                    completableListener.completeExceptionally(new H2ClientException("openStream request is null."));
                    return;
                }
                return;
            }
            Connection connection = this.e;
            if (connection == null || this.f3910a == null) {
                com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "openStream connection is not created.");
                if (completableListener != null) {
                    completableListener.completeExceptionally(new H2ClientException("openStream failed. connection not created."));
                    return;
                }
                return;
            }
            synchronized (connection) {
                if (!this.e.isAuthorized()) {
                    http2Request.getHeaders().add(this.f3910a.authHeader());
                    a(str, http2Request, this.e, completableListener);
                } else {
                    a(str, http2Request, this.e, completableListener);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (completableListener != null) {
                completableListener.completeExceptionally(e);
            }
        }
    }

    @Override // com.aliyun.alink.h2.stream.api.IStreamSender
    public boolean isConnected() {
        Connection connection = this.e;
        if (connection != null && connection.getStatus() != ConnectionStatus.CLOSED) {
            return (!this.f3912c.get() || this.f3910a == null || this.e == null) ? false : true;
        }
        this.f3912c.set(false);
        return false;
    }

    @Override // com.aliyun.alink.h2.stream.api.IStreamSender
    public void sendStream(final String str, final Http2Request http2Request, final IDownStreamListener iDownStreamListener, final CompletableListener<StreamWriteContext> completableListener) {
        IotHttp2Client iotHttp2Client;
        com.aliyun.alink.h2.stream.c.a.b("StreamSenderImpl", "sendStream() called with: dataStreamId = [" + str + "], request = [" + http2Request + "], downStreamListener = [" + iDownStreamListener + "], completableListener = [" + completableListener + "]");
        if (!isConnected()) {
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("not connected."));
                return;
            }
            return;
        }
        if (http2Request == null) {
            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "sendStream request is null.");
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("sendStream request is null."));
                return;
            }
            return;
        }
        if (this.e == null || (iotHttp2Client = this.f3910a) == null) {
            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "sendStream connection is not created.");
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("sendStream failed. connection not created."));
                return;
            }
            return;
        }
        final StreamServiceContext dataStreamContext = StreamUtil.getDataStreamContext(iotHttp2Client.allConnections(), str);
        if (dataStreamContext == null) {
            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "sendStream StreamServiceContext=null");
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("send stream StreamServiceContext is null."));
                return;
            }
            return;
        }
        ThreadPool.submit(new Runnable() { // from class: com.aliyun.alink.h2.stream.a.d.2
            @Override // java.lang.Runnable
            public void run() {
                Connection connection = dataStreamContext.getConnection();
                String serviceName = dataStreamContext.getServiceName();
                Http2Headers headers = http2Request.getHeaders();
                headers.path(StreamUtil.PATH_STREAM_SEND + serviceName);
                headers.set(StreamUtil.DATA_STREAM_ID, str);
                d.this.f3910a.sendRequest(connection, http2Request, new a(iDownStreamListener), completableListener);
            }
        });
    }

    @Override // com.aliyun.alink.h2.stream.api.IStreamSender
    public void closeStream(final String str, final Http2Request http2Request, final CompletableListener<Http2Response> completableListener) {
        IotHttp2Client iotHttp2Client;
        com.aliyun.alink.h2.stream.c.a.b("StreamSenderImpl", "closeStream() called with: dataStreamId = [" + str + "], request = [" + http2Request + "], listener = [" + completableListener + "]");
        if (!isConnected()) {
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("not connected."));
                return;
            }
            return;
        }
        if (http2Request == null) {
            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "closeStream request is null.");
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("closeStream request is null."));
                return;
            }
            return;
        }
        if (this.e == null || (iotHttp2Client = this.f3910a) == null) {
            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "closeStream connection is not created.");
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("closeStream failed. connection not created."));
                return;
            }
            return;
        }
        final StreamServiceContext dataStreamContext = StreamUtil.getDataStreamContext(iotHttp2Client.allConnections(), str);
        if (dataStreamContext == null) {
            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "closeStream StreamServiceContext=null");
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("close stream StreamServiceContext is null."));
                return;
            }
            return;
        }
        ThreadPool.submit(new Runnable() { // from class: com.aliyun.alink.h2.stream.a.d.3
            @Override // java.lang.Runnable
            public void run() {
                Connection connection = dataStreamContext.getConnection();
                String serviceName = dataStreamContext.getServiceName();
                final Http2Headers headers = http2Request.getHeaders();
                headers.path(StreamUtil.PATH_STREAM_CLOSE + serviceName);
                headers.set(StreamUtil.DATA_STREAM_ID, str);
                StreamUtil.removeDataStreamContext(connection, str);
                d.this.f3910a.sendRequest(connection, http2Request, new com.aliyun.alink.h2.stream.b.a() { // from class: com.aliyun.alink.h2.stream.a.d.3.1
                    @Override // com.aliyun.alink.h2.api.Http2StreamListener
                    public void onStreamError(Connection connection2, Http2Stream http2Stream, IOException iOException) {
                        if (completableListener != null) {
                            completableListener.completeExceptionally(iOException);
                        }
                    }

                    @Override // com.aliyun.alink.h2.stream.b.a
                    public void a(Connection connection2, Http2Stream http2Stream, StreamData streamData) {
                        c cVar = new c(new Http2Response(streamData.getHeaders(), streamData.readAllData()));
                        if (HttpResponseStatus.OK.equals(cVar.getStatus())) {
                            cVar.getHeaders().path(headers.path());
                            if (completableListener != null) {
                                completableListener.complete(cVar);
                            }
                            com.aliyun.alink.h2.stream.c.a.b("StreamSenderImpl", "close stream success, streamId: " + cVar.a());
                            return;
                        }
                        com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "onStreamDataReceived closeStream failed " + cVar);
                        if (completableListener != null) {
                            completableListener.completeExceptionally(new H2ClientException("close stream failed " + cVar));
                        }
                    }
                }, new CompletableListener<StreamWriteContext>() { // from class: com.aliyun.alink.h2.stream.a.d.3.2
                    @Override // com.aliyun.alink.h2.api.CompletableListener
                    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                    public void complete(StreamWriteContext streamWriteContext) {
                    }

                    @Override // com.aliyun.alink.h2.api.CompletableListener
                    public void completeExceptionally(Throwable th) {
                        com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "completeExceptionally closeStream failed " + th);
                        if (completableListener != null) {
                            completableListener.completeExceptionally(new H2ClientException("close stream failed " + th));
                        }
                    }
                });
            }
        });
    }

    @Override // com.aliyun.alink.h2.stream.api.IStreamSender
    public void disconnect(final CompletableListener completableListener) {
        com.aliyun.alink.h2.stream.c.a.b("StreamSenderImpl", "disconnect() called");
        if (this.f3911b.compareAndSet(true, false)) {
            ThreadPool.execute(new Runnable() { // from class: com.aliyun.alink.h2.stream.a.d.4
                @Override // java.lang.Runnable
                public void run() {
                    try {
                        d.this.f3912c.set(false);
                        if (d.this.f3910a != null) {
                            d.this.f3910a.shutdown();
                            d.this.f3910a = null;
                        }
                        if (completableListener != null) {
                            completableListener.complete(true);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        CompletableListener completableListener2 = completableListener;
                        if (completableListener2 != null) {
                            completableListener2.completeExceptionally(e);
                        }
                    }
                }
            });
            return;
        }
        com.aliyun.alink.h2.stream.c.a.b("StreamSenderImpl", "client has already disconnected");
        if (completableListener != null) {
            completableListener.complete(true);
        }
    }

    private void a(final String str, final Http2Request http2Request, final Connection connection, final CompletableListener<Http2Response> completableListener) {
        if (isConnected()) {
            ThreadPool.submit(new Runnable() { // from class: com.aliyun.alink.h2.stream.a.d.5
                @Override // java.lang.Runnable
                public void run() {
                    final Http2Headers headers = http2Request.getHeaders();
                    headers.path(StreamUtil.PATH_STREAM_OPEN + str);
                    d.this.f3910a.sendRequest(connection, http2Request, new com.aliyun.alink.h2.stream.b.a() { // from class: com.aliyun.alink.h2.stream.a.d.5.1
                        @Override // com.aliyun.alink.h2.api.Http2StreamListener
                        public void onStreamError(Connection connection2, Http2Stream http2Stream, IOException iOException) {
                            if (completableListener != null) {
                                completableListener.completeExceptionally(iOException);
                            }
                        }

                        @Override // com.aliyun.alink.h2.stream.b.a
                        public void a(Connection connection2, Http2Stream http2Stream, StreamData streamData) {
                            c cVar = new c(new Http2Response(streamData.getHeaders(), streamData.readAllData()));
                            if (HttpResponseStatus.OK.equals(cVar.getStatus())) {
                                connection2.setStatus(ConnectionStatus.AUTHORIZED);
                                cVar.getHeaders().path(headers.path());
                                StreamUtil.putDataStreamContext(connection2, cVar.a(), new StreamServiceContext(connection2, cVar, str));
                                if (completableListener != null) {
                                    completableListener.complete(cVar);
                                }
                                com.aliyun.alink.h2.stream.c.a.b("StreamSenderImpl", "open stream success, streamId: " + cVar.a());
                                return;
                            }
                            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "onStreamDataReceived openStream failed " + cVar);
                            if (completableListener != null) {
                                completableListener.completeExceptionally(new H2ClientException("open stream failed " + cVar));
                            }
                        }
                    }, new CompletableListener<StreamWriteContext>() { // from class: com.aliyun.alink.h2.stream.a.d.5.2
                        @Override // com.aliyun.alink.h2.api.CompletableListener
                        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                        public void complete(StreamWriteContext streamWriteContext) {
                        }

                        @Override // com.aliyun.alink.h2.api.CompletableListener
                        public void completeExceptionally(Throwable th) {
                            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "completeExceptionally openStream failed " + th);
                            if (completableListener != null) {
                                completableListener.completeExceptionally(new H2ClientException("open stream failed " + th));
                            }
                        }
                    });
                }
            });
        } else if (completableListener != null) {
            completableListener.completeExceptionally(new H2ClientException("not connected."));
        }
    }

    @Override // com.aliyun.alink.h2.stream.api.IH2FileManager
    public void upload(final String str, final String str2, final Http2Request http2Request, final CompletableListener<Http2Response> completableListener) {
        if (!isConnected()) {
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("not connected."));
                return;
            }
            return;
        }
        if (str2 == null || str2.isEmpty()) {
            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "upload file failed path is null.");
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("file path is null."));
                return;
            }
            return;
        }
        if (http2Request == null) {
            com.aliyun.alink.h2.stream.c.a.c("StreamSenderImpl", "upload file request is null.");
            if (completableListener != null) {
                completableListener.completeExceptionally(new H2ClientException("request is null."));
                return;
            }
            return;
        }
        try {
            ThreadPool.submit(new Runnable() { // from class: com.aliyun.alink.h2.stream.a.d.6
                @Override // java.lang.Runnable
                public void run() throws Throwable {
                    new b().a(d.this.f3910a, str2, str, http2Request, completableListener);
                }
            });
        } catch (Exception e) {
            com.aliyun.alink.h2.stream.c.a.d("StreamSenderImpl", "upload failed exception=" + e);
            e.printStackTrace();
            if (completableListener != null) {
                completableListener.completeExceptionally(e);
            }
        }
    }
}
