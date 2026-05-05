package com.alibaba.sdk.android.error;

import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;

/* JADX INFO: loaded from: classes.dex */
@Deprecated
public class SdkError extends ErrorCode {
    public static final String TYPE_SDK = "SDK";

    public SdkError(String str, String str2, String str3, String[] strArr, boolean z) {
        super(str + OpenAccountUIConstants.UNDER_LINE + "SDK" + OpenAccountUIConstants.UNDER_LINE + str2, str3, null, strArr, z);
    }
}
