package com.aliyun.alink.linksdk.tmp.error;

import com.aliyun.alink.linksdk.tmp.utils.ErrorCode;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public class NotSupportError extends AError {
    public NotSupportError() {
        setCode(ErrorCode.ERROR_CODE_NOTSUPPORT);
        setMsg(ErrorCode.ERROR_MSG_NOTSUPPORT);
    }
}
