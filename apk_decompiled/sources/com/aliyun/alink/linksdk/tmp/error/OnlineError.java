package com.aliyun.alink.linksdk.tmp.error;

import com.aliyun.alink.linksdk.tmp.utils.ErrorCode;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public class OnlineError extends AError {
    public OnlineError() {
        setCode(ErrorCode.ERROR_CODE_ONLINEFAIL);
        setMsg(ErrorCode.ERROR_MSG_ONLINEFAILD);
    }
}
