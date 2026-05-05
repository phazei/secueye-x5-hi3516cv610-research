package com.aliyun.alink.business.devicecenter.api.add;

/* JADX INFO: loaded from: classes.dex */
public enum ProtocolVersion {
    DEAFULT("1.0"),
    NO_PRODUCT("2.0");

    public String version;

    ProtocolVersion(String str) {
        this.version = "1.0";
        this.version = str;
    }

    public static boolean isValidVersion(String str) {
        for (ProtocolVersion protocolVersion : values()) {
            if (protocolVersion != null && protocolVersion.getVersion().equals(str)) {
                return true;
            }
        }
        return false;
    }

    public String getVersion() {
        return this.version;
    }
}
