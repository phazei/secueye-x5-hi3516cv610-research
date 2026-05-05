package com.aliyun.alink.linksdk.tmp.utils;

import com.aliyun.alink.apiclient.biz.ApiClientBiz;
import com.aliyun.alink.apiclient.biz.IApiClientBizCallback;
import com.aliyun.alink.apiclient.biz.IApiClientResponse;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes2.dex */
public class UsageTrackUtils {
    private static final AtomicBoolean mIsReported = new AtomicBoolean(false);
    private static final String TAG = TmpConstant.TAG + UsageTrackUtils.class.getSimpleName();

    public static void reportUsage() {
        if (mIsReported.compareAndSet(false, true)) {
            ALog.d(TAG, "reportUsage");
            try {
                ApiClientBiz.getInstance().usageTrack("appSdkTrack", "ALCS", "Connected", null, new IApiClientBizCallback() { // from class: com.aliyun.alink.linksdk.tmp.utils.UsageTrackUtils.1
                    @Override // com.aliyun.alink.apiclient.biz.IApiClientBizCallback
                    public void onFail(Exception exc) {
                        String str = UsageTrackUtils.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("usageTrack onFail e:");
                        sb.append(exc == null ? "empty error" : exc.toString());
                        ALog.e(str, sb.toString());
                    }

                    @Override // com.aliyun.alink.apiclient.biz.IApiClientBizCallback
                    public void onResponse(IApiClientResponse iApiClientResponse) {
                        String str = UsageTrackUtils.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("usageTrack onResponse:");
                        sb.append(iApiClientResponse == null ? "empty error" : iApiClientResponse.toString());
                        ALog.d(str, sb.toString());
                    }
                });
            } catch (Throwable th) {
                ALog.w(TAG, "reportUsage e:" + th.toString());
            }
        }
    }
}
