package com.aliyun.alink.linksdk.alcs.lpbs.bridge;

import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;

/* JADX INFO: loaded from: classes2.dex */
public interface IPalGroupConnect {
    boolean asyncSendRequest(PalGroupReqMessage palGroupReqMessage, PalMsgListener palMsgListener);
}
