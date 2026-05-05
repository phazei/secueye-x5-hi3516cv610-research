package com.aliyun.alink.linksdk.tmp.device.a.c;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.config.GroupConfig;
import com.aliyun.alink.linksdk.tmp.data.auth.AccessInfo;
import com.aliyun.alink.linksdk.tmp.device.a.e;
import com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener;
import com.aliyun.alink.linksdk.tmp.device.request.localgroup.GetLocalControlInfoRequest;
import com.aliyun.alink.linksdk.tmp.listener.IDevListener;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: CreateGroupConnect.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends e<a> {
    public a(com.aliyun.alink.linksdk.tmp.device.b bVar, IDevListener iDevListener) {
        super(bVar, iDevListener);
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.a.a
    public synchronized boolean a() {
        final com.aliyun.alink.linksdk.tmp.device.b bVar = this.f4360c.get();
        if (bVar == null) {
            ALog.e("[Tmp]AsyncTask", "action groupImpl empty");
            b((Object) null, new ErrorInfo(300, "param is invalid"));
            return false;
        }
        if (bVar.b() != null) {
            a((Object) null, (Object) null);
            return true;
        }
        final GroupConfig groupConfigA = bVar.a();
        if (groupConfigA != null && !TextUtils.isEmpty(groupConfigA.groupId)) {
            if (groupConfigA.accessInfo != null && !TextUtils.isEmpty(groupConfigA.accessInfo.mAccessKey) && !TextUtils.isEmpty(groupConfigA.accessInfo.mAccessToken)) {
                a(bVar, groupConfigA.groupId, groupConfigA.accessInfo);
                return true;
            }
            AccessInfo groupAccessInfo = TmpStorage.getInstance().getGroupAccessInfo(groupConfigA.groupId);
            String localGroupId = TmpStorage.getInstance().getLocalGroupId(groupConfigA.groupId);
            if (groupAccessInfo != null) {
                groupConfigA.accessInfo = groupAccessInfo;
                groupConfigA.localGroupId = localGroupId;
                a(bVar, localGroupId, groupAccessInfo);
                return true;
            }
            GetLocalControlInfoRequest getLocalControlInfoRequest = new GetLocalControlInfoRequest();
            getLocalControlInfoRequest.controlGroupId = groupConfigA.groupId;
            getLocalControlInfoRequest.sendRequest(getLocalControlInfoRequest, new IGateWayRequestListener<GetLocalControlInfoRequest, GetLocalControlInfoRequest.GetLocalControlInfoResponse>() { // from class: com.aliyun.alink.linksdk.tmp.device.a.c.a.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
                /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                public void onSuccess(GetLocalControlInfoRequest getLocalControlInfoRequest2, GetLocalControlInfoRequest.GetLocalControlInfoResponse getLocalControlInfoResponse) {
                    if (getLocalControlInfoResponse == null || getLocalControlInfoResponse.data == 0) {
                        ALog.e("[Tmp]AsyncTask", "GetLocalControlInfoRequest onSuccess result or data empty");
                        onFail(getLocalControlInfoRequest2, (AError) null);
                        return;
                    }
                    AccessInfo accessInfo = new AccessInfo(((GetLocalControlInfoRequest.GetLocalControlInfoResponse.GetLocalControlInfoData) getLocalControlInfoResponse.data).accessKey, ((GetLocalControlInfoRequest.GetLocalControlInfoResponse.GetLocalControlInfoData) getLocalControlInfoResponse.data).accessToken);
                    TmpStorage.getInstance().saveGroupAccessInfo(groupConfigA.groupId, accessInfo);
                    TmpStorage.getInstance().saveLocalGroupId(groupConfigA.groupId, ((GetLocalControlInfoRequest.GetLocalControlInfoResponse.GetLocalControlInfoData) getLocalControlInfoResponse.data).localGroupId);
                    GroupConfig groupConfig = groupConfigA;
                    groupConfig.accessInfo = accessInfo;
                    groupConfig.localGroupId = ((GetLocalControlInfoRequest.GetLocalControlInfoResponse.GetLocalControlInfoData) getLocalControlInfoResponse.data).localGroupId;
                    a.this.a(bVar, ((GetLocalControlInfoRequest.GetLocalControlInfoResponse.GetLocalControlInfoData) getLocalControlInfoResponse.data).localGroupId, accessInfo);
                }

                @Override // com.aliyun.alink.linksdk.tmp.device.request.IGateWayRequestListener
                /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
                public void onFail(GetLocalControlInfoRequest getLocalControlInfoRequest2, AError aError) {
                    a.this.b((Object) null, (ErrorInfo) null);
                }
            });
            return true;
        }
        ALog.e("[Tmp]AsyncTask", "action groupConfig or groupId empty");
        b((Object) null, new ErrorInfo(300, "param is invalid"));
        return false;
    }

    protected void a(com.aliyun.alink.linksdk.tmp.device.b bVar, String str, AccessInfo accessInfo) {
        if (bVar == null || accessInfo == null) {
            ALog.e("[Tmp]AsyncTask", "createGroupConnect groupImpl empty");
            b((Object) null, (ErrorInfo) null);
        } else if (bVar.b() != null) {
            a((Object) null, (Object) null);
        } else {
            bVar.a(new com.aliyun.alink.linksdk.tmp.connect.b.a(str, accessInfo));
            a((Object) null, (Object) null);
        }
    }
}
