package com.aliyun.alink.h2.connection;

import io.netty.handler.codec.http2.Http2Settings;

/* JADX INFO: compiled from: ConnectionListener.java */
/* JADX INFO: loaded from: classes2.dex */
public interface b {
    void onSettingReceive(Connection connection, Http2Settings http2Settings);

    void onStatusChange(ConnectionStatus connectionStatus, Connection connection);
}
