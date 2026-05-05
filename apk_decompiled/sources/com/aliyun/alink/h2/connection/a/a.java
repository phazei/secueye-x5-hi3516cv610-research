package com.aliyun.alink.h2.connection.a;

import com.aliyun.alink.h2.api.CompletableListener;
import com.aliyun.alink.h2.api.Http2StreamListener;
import com.aliyun.alink.h2.api.StreamWriteContext;
import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.connection.ConnectionStatus;
import com.aliyun.alink.h2.connection.d;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http2.Http2Connection;
import io.netty.handler.codec.http2.Http2ConnectionDecoder;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2Exception;
import io.netty.handler.codec.http2.Http2Flags;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Settings;
import io.netty.handler.codec.http2.Http2Stream;
import io.netty.handler.codec.http2.Http2StreamVisitor;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: ConnectionImpl.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements Connection {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Http2Connection.PropertyKey f3820a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Http2Connection f3821b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ChannelHandlerContext f3822c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private Http2ConnectionDecoder f3823d;
    private Http2ConnectionEncoder e;
    private com.aliyun.alink.h2.connection.b f;
    private ConnectionStatus g;
    private Map<String, Http2Connection.PropertyKey> h = new ConcurrentHashMap();

    @Override // com.aliyun.alink.h2.connection.ConnectionReader
    public void onGoAwayRead(ChannelHandlerContext channelHandlerContext, int i, long j, ByteBuf byteBuf) {
    }

    public a(com.aliyun.alink.h2.netty.b bVar, ChannelHandlerContext channelHandlerContext) {
        this.f3821b = bVar.connection();
        this.f3823d = bVar.decoder();
        this.e = bVar.encoder();
        this.f3822c = channelHandlerContext;
        this.f3820a = this.f3821b.newKey();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(Http2Stream http2Stream, Http2StreamListener http2StreamListener) {
        http2Stream.setProperty(this.f3820a, http2StreamListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Http2Stream a(int i) {
        return this.f3821b.stream(i);
    }

    @Override // com.aliyun.alink.h2.connection.ConnectionReader
    public int onDataRead(ChannelHandlerContext channelHandlerContext, int i, ByteBuf byteBuf, int i2, final boolean z) {
        final byte[] bArrCopyOf;
        final Http2Stream http2StreamA = a(i);
        int i3 = byteBuf.readableBytes();
        if (i2 == 0 && byteBuf.hasArray()) {
            bArrCopyOf = byteBuf.readBytes(i3).array();
        } else {
            int i4 = i3 - i2;
            byte[] bArr = new byte[i3];
            byteBuf.readBytes(bArr, 0, i3);
            bArrCopyOf = i2 == 0 ? bArr : Arrays.copyOf(bArr, i4);
        }
        a(http2StreamA, new d<Http2StreamListener>() { // from class: com.aliyun.alink.h2.connection.a.a.1
            @Override // com.aliyun.alink.h2.connection.d
            public void a(Http2StreamListener http2StreamListener) {
                http2StreamListener.onDataRead(a.this, http2StreamA, bArrCopyOf, z);
            }
        });
        return i3;
    }

    @Override // com.aliyun.alink.h2.connection.ConnectionReader
    public void onHeadersRead(ChannelHandlerContext channelHandlerContext, int i, final Http2Headers http2Headers, int i2, short s, boolean z, int i3, final boolean z2) {
        final Http2Stream http2StreamA = a(i);
        if (a(http2StreamA) == null && a() != null) {
            com.aliyun.alink.h2.b.a.a("ConnectionImpl", "set default stream listener for streamId:{}" + http2StreamA.id());
            a(http2StreamA, a());
        }
        if (a(http2StreamA, new d<Http2StreamListener>() { // from class: com.aliyun.alink.h2.connection.a.a.9
            @Override // com.aliyun.alink.h2.connection.d
            public void a(Http2StreamListener http2StreamListener) {
                http2StreamListener.onHeadersRead(a.this, http2StreamA, http2Headers, z2);
            }
        })) {
            return;
        }
        writeGoAway(i, 2, ("no handler for stream " + i).getBytes(), new CompletableListener<Connection>() { // from class: com.aliyun.alink.h2.connection.a.a.10
            @Override // com.aliyun.alink.h2.api.CompletableListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void complete(Connection connection) {
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            public void completeExceptionally(Throwable th) {
            }
        });
    }

    private Http2StreamListener a() {
        return a(this.f3821b.connectionStream());
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public void setConnectionListener(com.aliyun.alink.h2.connection.b bVar) {
        this.f = bVar;
        if (bVar != null) {
            bVar.onStatusChange(getStatus(), this);
        }
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public void removeConnectListener() {
        setConnectionListener(null);
    }

    @Override // com.aliyun.alink.h2.connection.ConnectionReader
    public void onSettingsRead(ChannelHandlerContext channelHandlerContext, Http2Settings http2Settings) {
        com.aliyun.alink.h2.connection.b bVar = this.f;
        if (bVar != null) {
            bVar.onSettingReceive(this, http2Settings);
        }
    }

    @Override // com.aliyun.alink.h2.connection.ConnectionReader
    public void onRstStreamRead(ChannelHandlerContext channelHandlerContext, final int i, final long j) {
        a(a(i), new d<Http2StreamListener>() { // from class: com.aliyun.alink.h2.connection.a.a.11
            @Override // com.aliyun.alink.h2.connection.d
            public void a(Http2StreamListener http2StreamListener) {
                a aVar = a.this;
                http2StreamListener.onStreamError(aVar, aVar.a(i), new IOException("rst frame received, code : " + j));
            }
        });
    }

    @Override // com.aliyun.alink.h2.connection.ConnectionReader
    public void onUnknownFrame(ChannelHandlerContext channelHandlerContext, byte b2, final int i, Http2Flags http2Flags, final ByteBuf byteBuf) {
        a(a(i), new d<Http2StreamListener>() { // from class: com.aliyun.alink.h2.connection.a.a.12
            @Override // com.aliyun.alink.h2.connection.d
            public void a(Http2StreamListener http2StreamListener) {
                a aVar = a.this;
                http2StreamListener.onStreamError(aVar, aVar.a(i), new IOException("unknown frame received, hex dump: " + ByteBufUtil.hexDump(byteBuf)));
            }
        });
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public void onConnectionClosed() {
        setStatus(ConnectionStatus.CLOSED);
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public void setStatus(ConnectionStatus connectionStatus) {
        com.aliyun.alink.h2.connection.b bVar = this.f;
        if (bVar != null) {
            bVar.onStatusChange(connectionStatus, this);
        }
        this.g = connectionStatus;
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public ConnectionStatus getStatus() {
        return this.g;
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public boolean isAuthorized() {
        return this.g.equals(ConnectionStatus.AUTHORIZED);
    }

    public String toString() {
        return this.f3822c.channel().id().asShortText();
    }

    private Http2StreamListener a(Http2Stream http2Stream) {
        return (Http2StreamListener) http2Stream.getProperty(this.f3820a);
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public void onError(ChannelHandlerContext channelHandlerContext, boolean z, final Throwable th) {
        try {
            this.f3821b.forEachActiveStream(new Http2StreamVisitor() { // from class: com.aliyun.alink.h2.connection.a.a.13
                @Override // io.netty.handler.codec.http2.Http2StreamVisitor
                public boolean visit(final Http2Stream http2Stream) {
                    a.this.a(http2Stream, new d<Http2StreamListener>() { // from class: com.aliyun.alink.h2.connection.a.a.13.1
                        @Override // com.aliyun.alink.h2.connection.d
                        public void a(Http2StreamListener http2StreamListener) {
                            http2StreamListener.onStreamError(a.this, http2Stream, new IOException(th));
                        }
                    });
                    return true;
                }
            });
        } catch (Http2Exception e) {
            com.aliyun.alink.h2.b.a.a("ConnectionImpl", "error occurs when notify listener. exception: ", e);
        }
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public Http2Connection.PropertyKey getPropertyKey(String str) {
        Map<String, Http2Connection.PropertyKey> map = this.h;
        if (map != null && map.get(str) == null) {
            this.h.put(str, this.f3821b.newKey());
        }
        return this.h.get(str);
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public void setProperty(Http2Connection.PropertyKey propertyKey, Object obj) {
        this.f3821b.connectionStream().setProperty(propertyKey, obj);
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public Object getProperty(Http2Connection.PropertyKey propertyKey) {
        return this.f3821b.connectionStream().getProperty(propertyKey);
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public void setDefaultStreamListener(Http2StreamListener http2StreamListener) {
        a(this.f3821b.connectionStream(), http2StreamListener);
    }

    @Override // com.aliyun.alink.h2.connection.Connection
    public void close() {
        this.f3822c.close().syncUninterruptibly();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a(Http2Stream http2Stream, d<Http2StreamListener> dVar) {
        Http2StreamListener http2StreamListenerA = a(http2Stream);
        if (http2StreamListenerA == null) {
            return false;
        }
        dVar.a(http2StreamListenerA);
        return true;
    }

    @Override // com.aliyun.alink.h2.connection.ConnectionWriter
    public void writeHeaders(final Http2Headers http2Headers, final boolean z, final Http2StreamListener http2StreamListener, final CompletableListener<StreamWriteContext> completableListener) {
        a(new com.aliyun.alink.h2.connection.a<AbstractC0212a<StreamWriteContext>, ChannelPromise>() { // from class: com.aliyun.alink.h2.connection.a.a.14
            @Override // com.aliyun.alink.h2.connection.a
            public void a(AbstractC0212a<StreamWriteContext> abstractC0212a, ChannelPromise channelPromise) {
                int iIncrementAndGetNextStreamId = a.this.f3821b.local().incrementAndGetNextStreamId();
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", "write headers, streamId:" + iIncrementAndGetNextStreamId + ", headers:" + http2Headers);
                Http2Stream http2StreamStream = a.this.f3821b.stream(iIncrementAndGetNextStreamId);
                if (http2StreamStream == null) {
                    try {
                        http2StreamStream = a.this.f3821b.local().createStream(iIncrementAndGetNextStreamId, false);
                    } catch (Http2Exception e) {
                        channelPromise.tryFailure(e);
                    }
                }
                Http2StreamListener http2StreamListener2 = http2StreamListener;
                if (http2StreamListener2 != null) {
                    a.this.a(http2StreamStream, http2StreamListener2);
                }
                abstractC0212a.a(new StreamWriteContext(http2StreamStream, a.this));
                a.this.e.writeHeaders(a.this.f3822c, iIncrementAndGetNextStreamId, http2Headers, 0, z, channelPromise);
                a.this.f3822c.pipeline().flush();
            }
        }, new AbstractC0212a<StreamWriteContext>() { // from class: com.aliyun.alink.h2.connection.a.a.15
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void complete(StreamWriteContext streamWriteContext) {
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", "writeHeaders complete() called with: result = [" + streamWriteContext + "]");
                CompletableListener completableListener2 = completableListener;
                if (completableListener2 != null) {
                    completableListener2.complete(streamWriteContext);
                }
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            public void completeExceptionally(Throwable th) {
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", "writeHeaders completeExceptionally() called with: throwable = [" + th + "]");
                CompletableListener completableListener2 = completableListener;
                if (completableListener2 != null) {
                    completableListener2.completeExceptionally(th);
                }
            }
        });
    }

    @Override // com.aliyun.alink.h2.connection.ConnectionWriter
    public void writeData(final int i, final byte[] bArr, final boolean z, final CompletableListener<StreamWriteContext> completableListener) {
        a(new com.aliyun.alink.h2.connection.a<AbstractC0212a<StreamWriteContext>, ChannelPromise>() { // from class: com.aliyun.alink.h2.connection.a.a.16
            @Override // com.aliyun.alink.h2.connection.a
            public void a(AbstractC0212a<StreamWriteContext> abstractC0212a, ChannelPromise channelPromise) {
                com.aliyun.alink.h2.b.a.b("ConnectionImpl", "write data on connection " + a.this.f3822c.channel().id() + ", stream id: " + i + ", size : " + bArr.length);
                abstractC0212a.a(new StreamWriteContext(a.this.f3821b.stream(i), a.this));
                a.this.e.writeData(a.this.f3822c, i, Unpooled.wrappedBuffer(bArr), 0, z, channelPromise);
                a.this.f3822c.pipeline().flush();
            }
        }, new AbstractC0212a<StreamWriteContext>() { // from class: com.aliyun.alink.h2.connection.a.a.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void complete(StreamWriteContext streamWriteContext) {
                StringBuilder sb = new StringBuilder();
                sb.append("writeData complete() called with: streamId=");
                sb.append(i);
                sb.append(",dataLen=");
                byte[] bArr2 = bArr;
                sb.append(bArr2 == null ? 0 : bArr2.length);
                sb.append(",result = [");
                sb.append(streamWriteContext);
                sb.append("]");
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", sb.toString());
                CompletableListener completableListener2 = completableListener;
                if (completableListener2 != null) {
                    completableListener2.complete(streamWriteContext);
                }
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            public void completeExceptionally(Throwable th) {
                StringBuilder sb = new StringBuilder();
                sb.append("writeData completeExceptionally() called with: streamId=");
                sb.append(i);
                sb.append(",dataLen=");
                byte[] bArr2 = bArr;
                sb.append(bArr2 == null ? 0 : bArr2.length);
                sb.append(",throwable = [");
                sb.append(th);
                sb.append("]");
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", sb.toString());
                CompletableListener completableListener2 = completableListener;
                if (completableListener2 != null) {
                    completableListener2.completeExceptionally(th);
                }
            }
        });
    }

    @Override // com.aliyun.alink.h2.connection.ConnectionWriter
    public void writeRst(final int i, final int i2, final CompletableListener<Connection> completableListener) {
        a(new com.aliyun.alink.h2.connection.a<AbstractC0212a<Connection>, ChannelPromise>() { // from class: com.aliyun.alink.h2.connection.a.a.3
            @Override // com.aliyun.alink.h2.connection.a
            public void a(AbstractC0212a<Connection> abstractC0212a, ChannelPromise channelPromise) {
                com.aliyun.alink.h2.b.a.b("ConnectionImpl", "write data on connection " + a.this.f3822c.channel().id() + ", stream id: " + i + ", error code: " + i2);
                abstractC0212a.a(a.this);
                a.this.e.writeRstStream(a.this.f3822c, i, (long) i2, channelPromise);
                a.this.f3822c.pipeline().flush();
            }
        }, new AbstractC0212a<Connection>() { // from class: com.aliyun.alink.h2.connection.a.a.4
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void complete(Connection connection) {
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", "complete() called with: result = [" + connection + "]");
                CompletableListener completableListener2 = completableListener;
                if (completableListener2 != null) {
                    completableListener2.complete(connection);
                }
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            public void completeExceptionally(Throwable th) {
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", "completeExceptionally() called with: throwable = [" + th + "]");
                CompletableListener completableListener2 = completableListener;
                if (completableListener2 != null) {
                    completableListener2.completeExceptionally(th);
                }
            }
        });
    }

    @Override // com.aliyun.alink.h2.connection.ConnectionWriter
    public void writeGoAway(final int i, final int i2, final byte[] bArr, final CompletableListener<Connection> completableListener) {
        com.aliyun.alink.h2.b.a.a("ConnectionImpl", "writeGoAway() called with: lastStreamId = [" + i + "], errorCode = [" + i2 + "], debugData = [" + bArr + "], completableListener = [" + completableListener + "]");
        StringBuilder sb = new StringBuilder();
        sb.append("who called");
        sb.append(new Throwable().getStackTrace());
        com.aliyun.alink.h2.b.a.a("ConnectionImpl", sb.toString());
        a(new com.aliyun.alink.h2.connection.a<AbstractC0212a<Connection>, ChannelPromise>() { // from class: com.aliyun.alink.h2.connection.a.a.5
            @Override // com.aliyun.alink.h2.connection.a
            public void a(AbstractC0212a<Connection> abstractC0212a, ChannelPromise channelPromise) {
                com.aliyun.alink.h2.b.a.b("ConnectionImpl", "write goaway on connection " + a.this.f3822c.channel().id() + ", stream id: " + i + ", size : " + bArr.length);
                abstractC0212a.a(a.this);
                a.this.e.writeGoAway(a.this.f3822c, i, (long) i2, Unpooled.wrappedBuffer(bArr), channelPromise);
                a.this.f3822c.pipeline().flush();
            }
        }, new AbstractC0212a<Connection>() { // from class: com.aliyun.alink.h2.connection.a.a.6
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super();
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public void complete(Connection connection) {
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", "complete() called with: result = [" + connection + "]");
                CompletableListener completableListener2 = completableListener;
                if (completableListener2 != null) {
                    completableListener2.complete(connection);
                }
            }

            @Override // com.aliyun.alink.h2.api.CompletableListener
            public void completeExceptionally(Throwable th) {
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", "completeExceptionally() called with: throwable = [" + th + "]");
                CompletableListener completableListener2 = completableListener;
                if (completableListener2 != null) {
                    completableListener2.completeExceptionally(th);
                }
            }
        });
    }

    private <R> void a(final com.aliyun.alink.h2.connection.a<AbstractC0212a<R>, ChannelPromise> aVar, final AbstractC0212a<R> abstractC0212a) {
        final ChannelPromise channelPromiseNewPromise = this.f3822c.newPromise();
        channelPromiseNewPromise.addListener((GenericFutureListener<? extends Future<? super Void>>) new GenericFutureListener<Future<? super Void>>() { // from class: com.aliyun.alink.h2.connection.a.a.7
            @Override // io.netty.util.concurrent.GenericFutureListener
            public void operationComplete(Future<? super Void> future) {
                com.aliyun.alink.h2.b.a.a("ConnectionImpl", "operationComplete() called with: future = [" + future + "]");
                if (future.isSuccess()) {
                    abstractC0212a.a();
                } else {
                    abstractC0212a.completeExceptionally(future.cause());
                }
            }
        });
        if (this.f3822c.channel().eventLoop().inEventLoop()) {
            aVar.a(abstractC0212a, channelPromiseNewPromise);
        } else {
            this.f3822c.channel().eventLoop().submit(new Runnable() { // from class: com.aliyun.alink.h2.connection.a.a.8
                @Override // java.lang.Runnable
                public void run() {
                    aVar.a(abstractC0212a, channelPromiseNewPromise);
                }
            });
        }
    }

    /* JADX INFO: renamed from: com.aliyun.alink.h2.connection.a.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: ConnectionImpl.java */
    abstract class AbstractC0212a<T> implements CompletableListener<T> {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private T f3874a;

        AbstractC0212a() {
        }

        public void a(T t) {
            this.f3874a = t;
        }

        void a() {
            complete(this.f3874a);
        }
    }
}
