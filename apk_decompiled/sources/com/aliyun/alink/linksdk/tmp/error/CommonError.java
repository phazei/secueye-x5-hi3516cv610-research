package com.aliyun.alink.linksdk.tmp.error;

import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public class CommonError extends AError {
    public CommonError(ErrorInfo errorInfo) {
        setCode(errorInfo == null ? 304 : errorInfo.getErrorCode());
        setMsg(errorInfo == null ? "unknown error" : errorInfo.getErrorMsg());
    }
}
