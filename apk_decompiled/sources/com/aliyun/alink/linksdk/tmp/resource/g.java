package com.aliyun.alink.linksdk.tmp.resource;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.cmp.api.ResourceRequest;
import com.aliyun.alink.linksdk.cmp.connect.alcs.CoAPResource;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceRequestListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceResponseListener;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: compiled from: TResRequestListener.java */
/* JADX INFO: loaded from: classes2.dex */
public class g implements IResourceRequestListener {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f4425c = "[Tmp]TResRequestListener";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected b f4426a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected String f4427b;

    public g(String str, b bVar) {
        this.f4426a = bVar;
        this.f4427b = str;
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
    public void onSuccess() {
        ALog.d(f4425c, "onSuccess mIdentifer:" + this.f4427b);
        b bVar = this.f4426a;
        if (bVar != null) {
            bVar.onSuccess(null, null);
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IBaseListener
    public void onFailure(AError aError) {
        ALog.d(f4425c, "onFailure mIdentifer:" + this.f4427b);
        b bVar = this.f4426a;
        if (bVar != null) {
            bVar.onFail(null, new ErrorInfo(aError));
        }
    }

    @Override // com.aliyun.alink.linksdk.cmp.core.listener.IResourceRequestListener
    public void onHandleRequest(AResource aResource, ResourceRequest resourceRequest, IResourceResponseListener iResourceResponseListener) {
        ALog.d(f4425c, "onHandleRequest identifier:" + this.f4427b + " aResource:" + aResource + " request:" + resourceRequest + " mHandler:" + this.f4426a);
        String str = null;
        String str2 = (aResource == null || !(aResource instanceof CoAPResource)) ? null : ((CoAPResource) aResource).topic;
        if (resourceRequest != null && !TextUtils.isEmpty(this.f4427b) && this.f4427b.contains(TmpConstant.IDENTIFIER_RAW_DATA_DOWN)) {
            this.f4426a.onProcess(this.f4427b, str2, resourceRequest, new d(resourceRequest, aResource, iResourceResponseListener));
            return;
        }
        if (resourceRequest != null && (resourceRequest instanceof ResourceRequest)) {
            if (resourceRequest.payloadObj instanceof byte[]) {
                try {
                    str = new String((byte[]) resourceRequest.payloadObj, "UTF-8");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                str = String.valueOf(resourceRequest.payloadObj);
            }
            ALog.d(f4425c, "onHandleRequest payload:" + str);
            String string = "0";
            try {
                JSONObject object = JSONObject.parseObject(str);
                if (object != null) {
                    string = object.getString("id");
                }
            } catch (Exception e2) {
                ALog.w(f4425c, "onHandleRequest:" + e2.toString());
            }
            this.f4426a.onProcess(this.f4427b, resourceRequest.topic, str, new i(resourceRequest, aResource, string, iResourceResponseListener));
            return;
        }
        ALog.w(f4425c, "onHandleRequest request error" + resourceRequest);
    }
}
