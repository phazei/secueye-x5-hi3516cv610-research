package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.tmp.service.TmpUTPointEx;
import com.aliyun.alink.linksdk.tmp.service.UTPoint;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class ConnectListenerWrapper implements IConnectSendListener {
    protected static final String TAG = "[Tmp]ConnectListenerWrapper";
    protected IPanelCallback mCallback;
    protected TmpUTPointEx mTmpUTPointEx;
    protected UTPoint mUTPoint;

    public ConnectListenerWrapper(UTPoint uTPoint, TmpUTPointEx tmpUTPointEx, IPanelCallback iPanelCallback) {
        this.mCallback = iPanelCallback;
        this.mUTPoint = uTPoint;
        UTPoint uTPoint2 = this.mUTPoint;
        if (uTPoint2 != null) {
            uTPoint2.trackStart();
        }
        this.mTmpUTPointEx = tmpUTPointEx;
        TmpUTPointEx tmpUTPointEx2 = this.mTmpUTPointEx;
        if (tmpUTPointEx2 != null) {
            tmpUTPointEx2.trackStart();
        }
    }

    public ConnectListenerWrapper(IPanelCallback iPanelCallback) {
        this(null, null, iPanelCallback);
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
    public void onResponse(ARequest aRequest, AResponse aResponse) {
        ALog.d(TAG, "onResponse aResponse mCallback:" + this.mCallback);
        UTPoint uTPoint = this.mUTPoint;
        if (uTPoint != null) {
            uTPoint.trackEnd();
        }
        TmpUTPointEx tmpUTPointEx = this.mTmpUTPointEx;
        boolean z = false;
        if (tmpUTPointEx != null) {
            tmpUTPointEx.trackEnd(0);
        }
        if (aResponse != null) {
            try {
                if (aResponse.data != null) {
                    JSONObject object = JSON.parseObject(aResponse.data.toString());
                    if ((object != null ? object.getIntValue("code") : 0) == 200) {
                        z = true;
                    }
                }
            } catch (Exception e) {
                ALog.d(TAG, "onResponse e:" + e.toString());
            } catch (Throwable th) {
                ALog.d(TAG, "onResponse t:" + th.toString());
            }
        }
        IPanelCallback iPanelCallback = this.mCallback;
        if (iPanelCallback != null) {
            iPanelCallback.onComplete(z, aResponse.data);
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener
    public void onFailure(ARequest aRequest, AError aError) {
        ALog.d(TAG, "onFailure aError mCallback:" + this.mCallback);
        UTPoint uTPoint = this.mUTPoint;
        if (uTPoint != null) {
            uTPoint.trackEnd();
        }
        TmpUTPointEx tmpUTPointEx = this.mTmpUTPointEx;
        if (tmpUTPointEx != null) {
            tmpUTPointEx.trackEnd(aError == null ? -1 : aError.getCode());
        }
        IPanelCallback iPanelCallback = this.mCallback;
        if (iPanelCallback != null) {
            iPanelCallback.onComplete(false, aError);
        }
    }
}
