package com.aliyun.alink.linksdk.alcs.lpbs.data.cloud;

import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class TgMeshConvertResult {
    public String deviceType;
    public boolean operateAll;
    public List<SigmeshInfo> sigmesh;

    public static class ActionIfo {
        public String opcode;
        public String parameters;
    }

    public static class DeviceInfo {
        public int appKeyIndex;
        public int destAddr;
        public int netKeyIndex;
        public int ttl;
    }

    public static class SigmeshInfo {
        public ActionIfo action;
        public boolean compareParameters;
        public DeviceInfo device;
    }
}
