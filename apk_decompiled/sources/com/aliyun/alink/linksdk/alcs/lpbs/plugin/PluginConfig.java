package com.aliyun.alink.linksdk.alcs.lpbs.plugin;

import android.content.Context;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ILpbsCloudProxy;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalInitData;

/* JADX INFO: loaded from: classes2.dex */
public class PluginConfig {
    public IAuthProvider authProvider;
    public Context context;
    public PalInitData initData;
    public ILpbsCloudProxy lpbsCloudProxy;

    public String toString() {
        return "PluginConfig{context=" + this.context + ", initData=" + this.initData + ", lpbsCloudProxy=" + this.lpbsCloudProxy + ", authProvider=" + this.authProvider + '}';
    }
}
