package com.aliyun.alink.linksdk.cmp.api;

import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Method;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class CommonRequest extends ARequest {
    public String alinkIdForTracker;
    public Object context;
    public String iotId;
    public String ip;
    public boolean isSecurity;
    public METHOD mothod = null;
    public Map<String, Object> params;
    public Object payload;
    public int port;
    public Object rspType;
    public String tgMeshType;
    public String topic;
    public String traceId;
    public Object type;

    public String getSpecificTopic(boolean z) {
        return this.topic;
    }

    public enum METHOD {
        POST,
        GET,
        DELETE,
        PUT;

        public Method toApiGwMethod() {
            switch (this) {
                case POST:
                    return Method.POST;
                case GET:
                    return Method.GET;
                case DELETE:
                    return Method.DELETE;
                case PUT:
                    return Method.PUT;
                default:
                    return null;
            }
        }

        public AlcsCoAPConstant.Code toCoapCode() {
            switch (this) {
                case POST:
                    return AlcsCoAPConstant.Code.POST;
                case GET:
                    return AlcsCoAPConstant.Code.GET;
                case DELETE:
                    return AlcsCoAPConstant.Code.DELETE;
                case PUT:
                    return AlcsCoAPConstant.Code.PUT;
                default:
                    return null;
            }
        }
    }
}
