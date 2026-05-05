package com.aliyun.alink.h2.connection.a;

import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.connection.ConnectionStatus;
import com.aliyun.alink.h2.connection.c;
import com.aliyun.alink.h2.netty.d;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http2.Http2Settings;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.net.SocketAddress;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/* JADX INFO: compiled from: ConnectionManagerImpl.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f3875a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Bootstrap f3876b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ChannelGroup f3877c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private EventLoopGroup f3878d;
    private final long e;
    private final long f;
    private com.aliyun.alink.h2.connection.b h = new com.aliyun.alink.h2.connection.b() { // from class: com.aliyun.alink.h2.connection.a.b.1
        @Override // com.aliyun.alink.h2.connection.b
        public void onSettingReceive(Connection connection, Http2Settings http2Settings) {
            if (b.this.g == null) {
                return;
            }
            for (int i = 0; i < b.this.g.size(); i++) {
                ((com.aliyun.alink.h2.connection.b) b.this.g.get(i)).onSettingReceive(connection, http2Settings);
            }
        }

        @Override // com.aliyun.alink.h2.connection.b
        public void onStatusChange(ConnectionStatus connectionStatus, Connection connection) {
            if (b.this.g == null) {
                return;
            }
            for (int i = 0; i < b.this.g.size(); i++) {
                ((com.aliyun.alink.h2.connection.b) b.this.g.get(i)).onStatusChange(connectionStatus, connection);
            }
        }
    };
    private List<com.aliyun.alink.h2.connection.b> g = new ArrayList();

    public b(boolean z, long j, long j2) {
        this.f3875a = z;
        this.e = j;
        this.f = j2;
        c();
    }

    private void c() {
        com.aliyun.alink.h2.b.a.b("ConnectionManagerImpl", "[ConnectionManagerImpl]initialize netty");
        d dVarD = d();
        this.f3878d = new NioEventLoopGroup();
        this.f3876b = new Bootstrap();
        this.f3876b.group(this.f3878d);
        this.f3876b.channel(NioSocketChannel.class);
        this.f3876b.option(ChannelOption.SO_KEEPALIVE, true);
        this.f3876b.handler(dVarD);
        this.f3877c = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    }

    private d d() {
        try {
            return new d(new com.aliyun.alink.h2.netty.c(this.f), this.f3875a);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            com.aliyun.alink.h2.b.a.a("ConnectionManagerImpl", "failed to initialize netty, {}", e);
            return null;
        }
    }

    @Override // com.aliyun.alink.h2.connection.c
    public List<Connection> a() {
        ArrayList arrayList = new ArrayList();
        List<Channel> listE = e();
        if (listE == null || listE.size() < 1) {
            return null;
        }
        for (int i = 0; i < listE.size(); i++) {
            arrayList.add(a(listE.get(i)));
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Connection a(Channel channel) {
        return ((com.aliyun.alink.h2.netty.b) channel.pipeline().get(com.aliyun.alink.h2.netty.b.class)).a();
    }

    @Override // com.aliyun.alink.h2.connection.c
    public Connection a(SocketAddress socketAddress) throws ExecutionException {
        com.aliyun.alink.h2.b.a.b("ConnectionManagerImpl", "connecting to {}" + socketAddress);
        ChannelFuture channelFutureConnect = this.f3876b.connect(socketAddress);
        channelFutureConnect.get();
        if (channelFutureConnect.isSuccess()) {
            Channel channel = channelFutureConnect.channel();
            this.f3877c.add(channel);
            channel.closeFuture().addListener((GenericFutureListener<? extends Future<? super Void>>) new ChannelFutureListener() { // from class: com.aliyun.alink.h2.connection.a.b.2
                @Override // io.netty.util.concurrent.GenericFutureListener
                /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                public void operationComplete(ChannelFuture channelFuture) {
                    b.this.a(channelFuture.channel()).onConnectionClosed();
                }
            });
            channel.pipeline().addFirst("heartBeatHandler", new IdleStateHandler(this.e, 0L, 0L, TimeUnit.MILLISECONDS));
            Connection connectionA = a(channel);
            connectionA.setConnectionListener(this.h);
            return connectionA;
        }
        throw new ExecutionException(channelFutureConnect.cause());
    }

    private List<Channel> e() {
        ArrayList arrayList = new ArrayList();
        for (Channel channel : this.f3877c) {
            if (channel != null && channel.isOpen() && channel.isActive()) {
                arrayList.add(channel);
            }
        }
        return arrayList;
    }

    @Override // com.aliyun.alink.h2.connection.c
    public void b() {
        this.f3877c.close();
        this.f3878d.shutdownGracefully();
    }

    @Override // com.aliyun.alink.h2.connection.c
    public void a(com.aliyun.alink.h2.connection.b bVar) {
        this.g.add(bVar);
    }

    @Override // com.aliyun.alink.h2.connection.c
    public void b(com.aliyun.alink.h2.connection.b bVar) {
        this.g.remove(bVar);
    }
}
