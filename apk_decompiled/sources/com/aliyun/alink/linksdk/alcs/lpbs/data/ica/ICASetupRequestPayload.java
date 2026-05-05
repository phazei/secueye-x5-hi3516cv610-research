package com.aliyun.alink.linksdk.alcs.lpbs.data.ica;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ICASetupRequestPayload {
    public String id;
    public String method;
    public ICASetupData params;
    public String version;

    public static class ICAProvisionAuthData {
        public String accessKey;
        public String accessToken;
        public String authCode;
        public String authSecret;
        public String deviceName;
        public String productKey;
    }

    public static class ICASetupData {
        public String configType;
        public List<ICAProvisionAuthData> configValue = new ArrayList();
    }
}
