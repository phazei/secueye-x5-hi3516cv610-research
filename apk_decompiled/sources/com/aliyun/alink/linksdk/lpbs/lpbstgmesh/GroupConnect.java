package com.aliyun.alink.linksdk.lpbs.lpbstgmesh;

import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalGroupConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class GroupConnect implements IPalGroupConnect {
    private static final String TAG = Utils.TAG + "GroupConnect";

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalGroupConnect
    public boolean asyncSendRequest(PalGroupReqMessage palGroupReqMessage, PalMsgListener palMsgListener) {
        ALog.w(TAG, "asyncSendRequest palGroupReqMessage:" + palGroupReqMessage + " palMsgListener:" + palMsgListener);
        return false;
    }
}
