package com.aliyun.alink.linksdk.tmp.error;

import com.aliyun.alink.linksdk.tmp.utils.ErrorCode;
import com.aliyun.alink.linksdk.tools.AError;

/* JADX INFO: loaded from: classes2.dex */
public class AuthSignIllegalError extends AError {
    public AuthSignIllegalError() {
        setCode(4106);
        setMsg(ErrorCode.AUGHSIGN_ILLEGAL_MSG);
    }
}
