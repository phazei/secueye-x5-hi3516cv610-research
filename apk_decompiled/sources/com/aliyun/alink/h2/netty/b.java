package com.aliyun.alink.h2.netty;

import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.connection.ConnectionStatus;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2ConnectionDecoder;
import io.netty.handler.codec.http2.Http2ConnectionEncoder;
import io.netty.handler.codec.http2.Http2ConnectionHandler;
import io.netty.handler.codec.http2.Http2Flags;
import io.netty.handler.codec.http2.Http2FrameListener;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Settings;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import java.io.IOException;

/* JADX INFO: compiled from: NettyHttp2Handler.java */
/* JADX INFO: loaded from: classes2.dex */
public class b extends Http2ConnectionHandler implements Http2FrameListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private long f3885a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f3886b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private Connection f3887c;

    b(Http2ConnectionDecoder http2ConnectionDecoder, Http2ConnectionEncoder http2ConnectionEncoder, Http2Settings http2Settings, long j) {
        super(http2ConnectionDecoder, http2ConnectionEncoder, http2Settings);
        this.f3885a = j;
    }

    public Connection a() {
        if (this.f3887c == null) {
            com.aliyun.alink.h2.b.a.d("NettyHttp2Handler", "failed to get connection, netty handler not initialized correctly");
        }
        return this.f3887c;
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object obj) {
        super.channelRead(channelHandlerContext, obj);
        c();
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler
    public void handlerAdded(ChannelHandlerContext channelHandlerContext) throws Exception {
        super.handlerAdded(channelHandlerContext);
        this.f3887c = new com.aliyun.alink.h2.connection.a.a(this, channelHandlerContext);
        this.f3887c.setStatus(ConnectionStatus.CREATING);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public int onDataRead(ChannelHandlerContext channelHandlerContext, int i, ByteBuf byteBuf, int i2, boolean z) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onDataRead, streamId: " + i + ", size: " + byteBuf.readableBytes() + ", ES: " + z);
        return this.f3887c.onDataRead(channelHandlerContext, i, byteBuf, i2, z);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onHeadersRead(ChannelHandlerContext channelHandlerContext, int i, Http2Headers http2Headers, int i2, boolean z) {
        onHeadersRead(channelHandlerContext, i, http2Headers, connection().connectionStream().id(), (short) 16, false, i2, z);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onHeadersRead(ChannelHandlerContext channelHandlerContext, int i, Http2Headers http2Headers, int i2, short s, boolean z, int i3, boolean z2) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onHeadersRead, streamId: " + i + ", header: " + http2Headers + ", weight: " + ((int) s) + ", dependency: " + i2 + ", exclusive: " + z + ", isEnd: " + z2);
        this.f3887c.onHeadersRead(channelHandlerContext, i, http2Headers, i2, s, z, i3, z2);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onPriorityRead(ChannelHandlerContext channelHandlerContext, int i, int i2, short s, boolean z) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onPriorityRead, streamId: " + i + ", streamDependency: " + i2 + ", weight: " + ((int) s) + ", exclusive: " + z);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onRstStreamRead(ChannelHandlerContext channelHandlerContext, int i, long j) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onRstStreamRead, streamId: " + i + ", errorCode: " + j);
        this.f3887c.onRstStreamRead(channelHandlerContext, i, j);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onSettingsAckRead(ChannelHandlerContext channelHandlerContext) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onSettingsAckRead");
        this.f3887c.setStatus(ConnectionStatus.CREATED);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.handler.codec.http2.Http2LifecycleManager
    public void onError(ChannelHandlerContext channelHandlerContext, boolean z, Throwable th) {
        super.onError(channelHandlerContext, z, th);
        com.aliyun.alink.h2.b.a.d("NettyHttp2Handler", "error occurs, close channel. channel id: " + channelHandlerContext.channel() + ", outbound: " + z + ", error:" + th);
        this.f3887c.onError(channelHandlerContext, z, th);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onSettingsRead(ChannelHandlerContext channelHandlerContext, Http2Settings http2Settings) {
        com.aliyun.alink.h2.b.a.a("onSettingsRead, settings: {}", http2Settings.toString());
        this.f3887c.onSettingsRead(channelHandlerContext, http2Settings);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onPingRead(ChannelHandlerContext channelHandlerContext, long j) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onPingRead, data: " + j);
        encoder().frameWriter().writePing(channelHandlerContext, true, j, channelHandlerContext.voidPromise());
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onPingAckRead(ChannelHandlerContext channelHandlerContext, long j) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onPingAckRead, data: " + j);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onPushPromiseRead(ChannelHandlerContext channelHandlerContext, int i, int i2, Http2Headers http2Headers, int i3) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onPushPromiseRead, streamId: " + i + ", promisedStreamId: " + i2 + ", headers size: " + http2Headers.size());
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onGoAwayRead(ChannelHandlerContext channelHandlerContext, int i, long j, ByteBuf byteBuf) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onGoAwayRead, lastStreamId: " + i + ", errorCode: " + j + ", " + new String(ByteBufUtil.getBytes(byteBuf)));
        this.f3887c.onGoAwayRead(channelHandlerContext, i, j, byteBuf);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onWindowUpdateRead(ChannelHandlerContext channelHandlerContext, int i, int i2) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onWindowUpdateRead, streamId: " + i + ", increment size: " + i2);
    }

    @Override // io.netty.handler.codec.http2.Http2FrameListener
    public void onUnknownFrame(ChannelHandlerContext channelHandlerContext, byte b2, int i, Http2Flags http2Flags, ByteBuf byteBuf) {
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "onUnknownFrame, frameType: " + ((int) b2) + ", streamId: " + i + ", size: " + byteBuf.readableBytes() + ", flags: " + http2Flags.toString());
        this.f3887c.onUnknownFrame(channelHandlerContext, b2, i, http2Flags, byteBuf);
    }

    @Override // io.netty.handler.codec.http2.Http2ConnectionHandler, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelHandlerAdapter, io.netty.channel.ChannelHandler, io.netty.channel.ChannelInboundHandler
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable th) throws Exception {
        super.exceptionCaught(channelHandlerContext, th);
        com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "exceptionCaught: ", new Exception(th));
        this.f3887c.onError(channelHandlerContext, false, th);
        this.f3887c.close();
    }

    @Override // io.netty.handler.codec.ByteToMessageDecoder, io.netty.channel.ChannelInboundHandlerAdapter, io.netty.channel.ChannelInboundHandler
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object obj) throws IOException {
        if ((obj instanceof IdleStateEvent) && ((IdleStateEvent) obj).state() == IdleState.READER_IDLE) {
            if (b()) {
                com.aliyun.alink.h2.b.a.d("NettyHttp2Handler", "connection heartbeat timeout, channel:[" + channelHandlerContext.channel().id() + "], remote address:[" + channelHandlerContext.channel().remoteAddress() + "] ");
                throw new IOException("connection heartbeat timeout");
            }
            com.aliyun.alink.h2.b.a.a("NettyHttp2Handler", "send heartbeat, channel:[" + channelHandlerContext.channel().id() + "], remote address:[" + channelHandlerContext.channel().remoteAddress() + "] ");
            encoder().frameWriter().writePing(channelHandlerContext, false, System.currentTimeMillis(), channelHandlerContext.voidPromise());
            channelHandlerContext.pipeline().flush();
        }
    }

    private boolean b() {
        return System.currentTimeMillis() - this.f3886b > this.f3885a;
    }

    private void c() {
        this.f3886b = System.currentTimeMillis();
    }
}
