package com.aliyun.alink.linksdk.cmp.core.util;

import com.aliyun.alink.linksdk.cmp.core.base.CmpError;
import com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener;

/* JADX INFO: loaded from: classes2.dex */
public class CallbackHelper {
    public static void paramError(IBaseListener iBaseListener, String str) {
        if (iBaseListener == null) {
            return;
        }
        CmpError cmpErrorPARAMS_ERROR = CmpError.PARAMS_ERROR();
        cmpErrorPARAMS_ERROR.setSubMsg(str);
        iBaseListener.onFailure(cmpErrorPARAMS_ERROR);
    }
}
