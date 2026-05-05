package com.aliyun.alink.business.devicecenter.track;

import com.aliyun.alink.apiclient.biz.ApiClientBiz;
import com.aliyun.alink.apiclient.biz.IApiClientBizCallback;
import com.aliyun.alink.apiclient.biz.IApiClientResponse;
import com.aliyun.alink.business.devicecenter.base.DCEnvHelper;
import com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants.WifiProvisionUtConst;
import com.aliyun.alink.business.devicecenter.log.ALog;
import java.util.HashMap;

/* JADX INFO: loaded from: classes2.dex */
public class UsageTrack {
    public static void sdkUsage(String str) {
        if (DCEnvHelper.hasApiClientBiz()) {
            ALog.d("UsageTrack", "sdkUsage() called");
            HashMap map = new HashMap();
            map.put("version", str);
            ApiClientBiz.getInstance().usageTrack("appSdkTrack", WifiProvisionUtConst.ARG_CONNECTION, "startProvision", map, new IApiClientBizCallback() { // from class: com.aliyun.alink.business.devicecenter.track.UsageTrack.1
                @Override // com.aliyun.alink.apiclient.biz.IApiClientBizCallback
                public void onFail(Exception exc) {
                }

                @Override // com.aliyun.alink.apiclient.biz.IApiClientBizCallback
                public void onResponse(IApiClientResponse iApiClientResponse) {
                    ALog.d("UsageTrack", "onResponse " + iApiClientResponse);
                }
            });
        }
    }
}
