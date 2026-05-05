package com.aliyun.alink.linksdk.alcs.lpbs.api;

import android.content.Context;
import com.aliyun.alink.linksdk.alcs.lpbs.component.auth.IAuthProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ICloudChannelFactory;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDevInfoTrans;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.ILpbsCloudProxy;
import com.aliyun.alink.linksdk.alcs.lpbs.component.jsengine.IJSEngine;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalInitData;

/* JADX INFO: loaded from: classes2.dex */
public class PluginMgrConfig {
    public IAuthProvider authProvider;
    public ICloudChannelFactory cloudChannelFactory;
    public Context context;
    public IDevInfoTrans devInfoTrans;
    public PalInitData initData;
    public IJSEngine jsEngine;
    public IJsProvider jsProvider;
    public ILpbsCloudProxy lpbsCloudProxy;
}
