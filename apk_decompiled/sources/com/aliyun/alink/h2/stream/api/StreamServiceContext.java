package com.aliyun.alink.h2.stream.api;

import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.entity.BaseHttpEntity;
import com.aliyun.alink.h2.stream.utils.StreamUtil;

/* JADX INFO: loaded from: classes2.dex */
public class StreamServiceContext {
    private static final String RESPONSE_STATUS = "x-response-status";
    private Connection connection;
    private String dataStreamId;
    private String requestId;
    private String requestPath;
    private String serviceName;

    public StreamServiceContext(Connection connection, BaseHttpEntity baseHttpEntity, String str) {
        this.connection = connection;
        this.serviceName = str;
        updateContext(baseHttpEntity);
    }

    public void updateContext(BaseHttpEntity baseHttpEntity) {
        this.requestId = baseHttpEntity.getRequestId();
        this.requestPath = baseHttpEntity.getHeaders().path().toString();
        this.dataStreamId = StreamUtil.getDataStreamId(baseHttpEntity.getHeaders());
    }

    public Connection getConnection() {
        return this.connection;
    }

    public String getRequestId() {
        return this.requestId;
    }

    public String getRequestPath() {
        return this.requestPath;
    }

    public String getDataStreamId() {
        return this.dataStreamId;
    }

    public String getServiceName() {
        return this.serviceName;
    }
}
