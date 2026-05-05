package com.aliyun.alink.h2.stream.utils;

import com.aliyun.alink.h2.api.Http2StreamListener;
import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.stream.api.StreamServiceContext;
import com.aliyun.alink.h2.utils.StringUtil;
import io.netty.handler.codec.http2.Http2Headers;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class StreamUtil {
    public static final String DATA_STREAM_ID = "x-data-stream-id";
    public static final String PATH_STREAM_CLOSE = "/stream/close";
    public static final String PATH_STREAM_OPEN = "/stream/open";
    public static final String PATH_STREAM_REGISTER = "/stream/register";
    public static final String PATH_STREAM_SEND = "/stream/send";
    public static final String PATH_STREAM_UNREGISTER = "/stream/unregister";

    public static void setupConnection(Connection connection, Http2StreamListener http2StreamListener) {
        connection.setDefaultStreamListener(http2StreamListener);
        connection.setProperty(connection.getPropertyKey("streams"), new ConcurrentHashMap());
    }

    public static Map<String, StreamServiceContext> getDataStreamMaps(Connection connection) {
        return (Map) connection.getProperty(connection.getPropertyKey("streams"));
    }

    public static StreamServiceContext getDataStreamContext(List<Connection> list, String str) {
        if (list == null || list.size() < 1) {
            throw new IllegalArgumentException("getDataStreamContext connections null");
        }
        if (StringUtil.isEmpty(str)) {
            throw new IllegalArgumentException("getDataStreamContext dataStreamId empty");
        }
        for (int i = 0; i < list.size(); i++) {
            Connection connection = list.get(i);
            if (hasDataStream(connection, str)) {
                return getDataStreamContext(connection, str);
            }
        }
        return null;
    }

    private static boolean hasDataStream(Connection connection, String str) {
        if (getDataStreamMaps(connection) == null) {
            return false;
        }
        return getDataStreamMaps(connection).containsKey(str);
    }

    public static void putDataStreamContext(Connection connection, String str, StreamServiceContext streamServiceContext) {
        getDataStreamMaps(connection).put(str, streamServiceContext);
    }

    private static StreamServiceContext getDataStreamContext(Connection connection, String str) {
        return getDataStreamMaps(connection).get(str);
    }

    public static void removeDataStreamContext(Connection connection, String str) {
        getDataStreamMaps(connection).remove(str);
    }

    public static String getDataStreamId(Http2Headers http2Headers) {
        if (http2Headers.contains(DATA_STREAM_ID)) {
            return http2Headers.get(DATA_STREAM_ID).toString();
        }
        return null;
    }

    public static void checkServiceName(String str) {
        if (!str.startsWith("/")) {
            throw new IllegalArgumentException("service name should start with '/'");
        }
    }
}
