package com.aliyun.alink.h2.connection;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http2.Http2Flags;
import io.netty.handler.codec.http2.Http2Headers;
import io.netty.handler.codec.http2.Http2Settings;

/* JADX INFO: loaded from: classes2.dex */
public interface ConnectionReader {
    int onDataRead(ChannelHandlerContext channelHandlerContext, int i, ByteBuf byteBuf, int i2, boolean z);

    void onGoAwayRead(ChannelHandlerContext channelHandlerContext, int i, long j, ByteBuf byteBuf);

    void onHeadersRead(ChannelHandlerContext channelHandlerContext, int i, Http2Headers http2Headers, int i2, short s, boolean z, int i3, boolean z2);

    void onRstStreamRead(ChannelHandlerContext channelHandlerContext, int i, long j);

    void onSettingsRead(ChannelHandlerContext channelHandlerContext, Http2Settings http2Settings);

    void onUnknownFrame(ChannelHandlerContext channelHandlerContext, byte b2, int i, Http2Flags http2Flags, ByteBuf byteBuf);
}
