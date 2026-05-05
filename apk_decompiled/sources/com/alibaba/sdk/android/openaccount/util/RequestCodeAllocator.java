package com.alibaba.sdk.android.openaccount.util;

import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import java.util.concurrent.atomic.AtomicInteger;

/* JADX INFO: loaded from: classes.dex */
public class RequestCodeAllocator {
    private static final AtomicInteger REQUEST_CODE = new AtomicInteger(59994);
    private static final String TAG = "oa_codeAllocator";

    public static void setStartRequestCodeIndex(int i) {
        REQUEST_CODE.set(i);
        AliSDKLogger.i(TAG, "Request code start index is set with " + i);
    }

    public static int allocateRequestCode(String str) {
        int andIncrement = REQUEST_CODE.getAndIncrement();
        AliSDKLogger.i(TAG, andIncrement + " is allocated for onActivityResult request code for " + str);
        return andIncrement;
    }
}
