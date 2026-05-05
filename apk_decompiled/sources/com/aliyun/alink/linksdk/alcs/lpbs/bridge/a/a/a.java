package com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.a;

import com.aliyun.alink.linksdk.alcs.data.ica.ICAAuthParams;
import com.aliyun.alink.linksdk.alcs.data.ica.ICAGroupReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalGroupConnect;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.c;
import com.aliyun.alink.linksdk.alcs.lpbs.bridge.a.k;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalRspMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupInfo;
import com.aliyun.alink.linksdk.alcs.lpbs.data.group.PalGroupReqMessage;
import com.aliyun.alink.linksdk.alcs.lpbs.data.ica.group.ICAGroupOption;
import com.aliyun.alink.linksdk.alcs.lpbs.listener.PalMsgListener;
import com.aliyun.alink.linksdk.alcs.pal.ica.ICAAlcsNative;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: ICAAlcsGroupConnect.java */
/* JADX INFO: loaded from: classes2.dex */
public class a implements IPalGroupConnect {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4033a = "[AlcsLPBS]ICAAlcsGroupConnect";

    public a(c cVar, PalGroupInfo palGroupInfo) {
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.bridge.IPalGroupConnect
    public boolean asyncSendRequest(PalGroupReqMessage palGroupReqMessage, PalMsgListener palMsgListener) {
        ALog.d(f4033a, "ICAAlcsGroupConnect asyncSendRequest  reqMessageInfo:" + palGroupReqMessage + " callback:" + palMsgListener);
        if (palGroupReqMessage == null || palGroupReqMessage.groupInfo == null || palGroupReqMessage.palOptions == null || !(palGroupReqMessage.palOptions instanceof ICAGroupOption)) {
            ALog.e(f4033a, "asyncSendRequest error params empty");
            if (palMsgListener != null) {
                palMsgListener.onLoad(new PalRspMessage(1));
            }
            return false;
        }
        ICAGroupOption iCAGroupOption = (ICAGroupOption) palGroupReqMessage.palOptions;
        if (iCAGroupOption == null || iCAGroupOption.icaOptions == null || iCAGroupOption.icaAuthParams == null) {
            ALog.e(f4033a, "asyncSendRequest error icaGroupOption empty");
            if (palMsgListener != null) {
                palMsgListener.onLoad(new PalRspMessage(1));
            }
            return false;
        }
        ICAGroupReqMessage iCAGroupReqMessage = new ICAGroupReqMessage();
        iCAGroupReqMessage.groupId = palGroupReqMessage.groupInfo.groupId;
        iCAGroupReqMessage.deviceInfo = palGroupReqMessage.groupInfo.getIcaGroupInfo();
        iCAGroupReqMessage.code = iCAGroupOption.icaOptions.code;
        iCAGroupReqMessage.type = iCAGroupOption.icaOptions.type;
        iCAGroupReqMessage.rspType = iCAGroupOption.icaOptions.rspType;
        iCAGroupReqMessage.topic = palGroupReqMessage.topic;
        iCAGroupReqMessage.payload = palGroupReqMessage.payload;
        ICAAuthParams iCAAuthParams = new ICAAuthParams();
        iCAAuthParams.accessKey = iCAGroupOption.icaAuthParams.accessKey;
        iCAAuthParams.accessToken = iCAGroupOption.icaAuthParams.accessToken;
        return ICAAlcsNative.sendGroupRequest(iCAGroupReqMessage, iCAAuthParams, new k(palMsgListener));
    }
}
