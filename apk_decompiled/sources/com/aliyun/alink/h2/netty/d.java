package com.aliyun.alink.h2.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import java.security.KeyManagementException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;

/* JADX INFO: compiled from: NettyHttp2Initializer.java */
/* JADX INFO: loaded from: classes2.dex */
public class d extends ChannelInitializer<SocketChannel> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private c f3891a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private boolean f3892b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private SSLContext f3893c;

    public d(c cVar, boolean z) {
        this.f3892b = false;
        this.f3891a = cVar;
        this.f3892b = z;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // io.netty.channel.ChannelInitializer
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public void initChannel(SocketChannel socketChannel) throws KeyManagementException {
        com.aliyun.alink.h2.b.a.b("NettyHttp2Initializer", "init http2 handler. enable SSL : " + this.f3892b);
        if (this.f3892b) {
            if (this.f3893c == null) {
                this.f3893c = SSLContext.getInstance("TLSv1.2");
                this.f3893c.init(null, InsecureTrustManagerFactory.INSTANCE.getTrustManagers(), null);
            }
            SSLEngine sSLEngineCreateSSLEngine = this.f3893c.createSSLEngine();
            sSLEngineCreateSSLEngine.setUseClientMode(true);
            socketChannel.pipeline().addLast(new SslHandler(sSLEngineCreateSSLEngine));
        }
        socketChannel.pipeline().addLast(this.f3891a.build());
    }
}
