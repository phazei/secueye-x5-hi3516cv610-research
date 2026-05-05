package com.aliyun.alink.linksdk.cmp.connect.channel;

import com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig;
import java.io.InputStream;

/* JADX INFO: loaded from: classes2.dex */
public class PersistentConnectConfig extends AConnectConfig {
    public String appkey;
    public String deviceName;
    public String deviceSecret;
    public String productKey;
    public String productSecret;
    public String securityGuardAuthcode;
    public boolean receiveOfflineMsg = false;
    public String channelHost = null;
    public boolean isCheckChannelRootCrt = true;
    public InputStream channelRootCrtFile = null;
    public int secureMode = 2;
    public boolean isInitUpdateFlag = false;

    @Override // com.aliyun.alink.linksdk.cmp.core.base.AConnectConfig
    public boolean checkVaild() {
        return true;
    }
}
