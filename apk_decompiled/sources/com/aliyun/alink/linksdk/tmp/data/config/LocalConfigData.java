package com.aliyun.alink.linksdk.tmp.data.config;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class LocalConfigData {
    public ClientConfig client;
    public DynamicConfig configReceiver;
    public ProvisionConfig provision;
    public ServerConfig server;

    public static class ClientAuthInfo {
        public String accessKey;
        public String accessToken;
        public String deviceName;
        public String productKey;
    }

    public static class ClientConfig {
        public List<ClientAuthInfo> AuthInfo;
    }

    public static class DynamicConfig {
        public String authcode;
        public boolean autoRun;
        public String deviceName;
        public String productKey;
        public String secret;
    }

    public static class ProvisionConfig {
        public List<ClientAuthInfo> AuthInfo;
    }

    public static class ServerAuthInfo {
        public String authcode;
        public String deviceName;
        public String productKey;
        public String secret;
    }

    public static class ServerConfig {
        public List<ServerAuthInfo> AuthInfo;
    }
}
