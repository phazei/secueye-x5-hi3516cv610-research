package com.aliyun.alink.linksdk.tmp.data.cloud;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class EdgeGatewaysResponsePayload {
    public int code;
    public EdgeGatewaysData data;
    public String message;

    public static class EdgeGatewaysData {
        public List<EdgeGateway> edgeGateways;
        public int pageNo;
        public int pageSize;
        public int total;

        public static class EdgeGateway {
            public String iotId;
            public List<Model> models;
            public String name;

            public static class Model {
                public String deviceName;
                public String modelName;
                public List<String> productKeys;
            }
        }
    }
}
