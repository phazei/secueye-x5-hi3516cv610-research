package com.aliyun.alink.business.devicecenter.config.model;

import com.aliyun.alink.business.devicecenter.api.add.LinkType;

/* JADX INFO: loaded from: classes.dex */
public class DCConfigParams {
    public LinkType linkType = null;
    public String ssid = null;
    public String password = null;
    public String protocolVersion = "1.0";

    public String toString() {
        return " DCConfigParams:[linkType=" + this.linkType + "ssid=" + this.ssid + "protocolVersion=" + this.protocolVersion + "]";
    }
}
