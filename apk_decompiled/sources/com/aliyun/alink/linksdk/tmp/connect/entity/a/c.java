package com.aliyun.alink.linksdk.tmp.connect.entity.a;

import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.tmp.connect.d;
import com.aliyun.alink.linksdk.tmp.connect.f;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;

/* JADX INFO: compiled from: LpbsSendHandler.java */
/* JADX INFO: loaded from: classes2.dex */
public class c extends f implements PalMsgListener {
    public c(com.aliyun.alink.linksdk.tmp.connect.c cVar, d dVar) {
        super(cVar, dVar);
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener
    public void onLoad(PalRspMessage palRspMessage) {
        if (palRspMessage == null || palRspMessage.code != 1) {
            super.a(this.f4276d, new ErrorInfo(300, "errror"));
        } else {
            super.a(this.f4276d, new b((PalGroupRspMessage) palRspMessage));
        }
    }
}
