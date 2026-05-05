package com.aliyun.iot.aep.sdk.init;

import android.app.Application;
import com.aliyun.iot.aep.sdk.framework.sdk.SDKConfigure;
import com.aliyun.iot.aep.sdk.framework.sdk.SimpleSDKDelegateImp;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class PushSDKDelegate extends SimpleSDKDelegateImp {
    @Override // com.aliyun.iot.aep.sdk.framework.sdk.ISDKDelegate
    public int init(Application application, SDKConfigure sDKConfigure, Map<String, String> map) {
        PushManagerHelper.getInstance().init(application);
        return 0;
    }
}
