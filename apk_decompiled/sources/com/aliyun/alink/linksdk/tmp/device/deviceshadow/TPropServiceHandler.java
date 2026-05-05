package com.aliyun.alink.linksdk.tmp.device.deviceshadow;

import com.aliyun.alink.linksdk.tmp.api.ListInputParams;
import com.aliyun.alink.linksdk.tmp.api.MapInputParams;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.listener.ITResRequestHandler;
import com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback;
import com.aliyun.alink.linksdk.tmp.resource.b;
import com.aliyun.alink.linksdk.tmp.utils.ErrorInfo;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class TPropServiceHandler implements b {
    protected static final String TAG = "[Tmp]TPropServiceHandler";
    protected ITResRequestHandler mGetServiceHandler;
    protected ITResRequestHandler mSetServiceHandler;
    protected WeakReference<TDeviceShadow> mShadowRef;

    public TPropServiceHandler(TDeviceShadow tDeviceShadow) {
        this.mShadowRef = new WeakReference<>(tDeviceShadow);
    }

    @Override // com.aliyun.alink.linksdk.tmp.resource.b
    public void onProcess(String str, String str2, Object obj, ITResResponseCallback iTResResponseCallback) {
        LogCat.d(TAG, "onProcess callback null identifier:" + str + " topic:" + str2 + " payload:" + obj + " callback:" + iTResResponseCallback);
        TDeviceShadow tDeviceShadow = this.mShadowRef.get();
        if (iTResResponseCallback == null || tDeviceShadow == null) {
            LogCat.e(TAG, "onProcess callback null");
            return;
        }
        if (obj == null) {
            iTResResponseCallback.onComplete(str2, null, null);
            return;
        }
        if (TmpConstant.PROPERTY_IDENTIFIER_GET.compareToIgnoreCase(str) == 0) {
            CommonRequestPayload commonRequestPayload = (CommonRequestPayload) GsonUtils.fromJson(String.valueOf(obj), new TypeToken<CommonRequestPayload<List<String>>>() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.TPropServiceHandler.1
            }.getType());
            if (commonRequestPayload.getParams() != null) {
                List<String> list = (List) commonRequestPayload.getParams();
                OutputParams outputParams = new OutputParams();
                for (String str3 : list) {
                    outputParams.put(str3, tDeviceShadow.getPropertyValue(str3));
                }
                LogCat.d(TAG, "onProcess get callback onComplete mGetServiceHandler:" + this.mGetServiceHandler);
                iTResResponseCallback.onComplete(str, null, outputParams);
                ITResRequestHandler iTResRequestHandler = this.mGetServiceHandler;
                if (iTResRequestHandler != null) {
                    iTResRequestHandler.onProcess(str, new ListInputParams(list), new GetPropCbWrapper());
                    return;
                }
                return;
            }
            LogCat.d(TAG, "onProcess get callback onComplete getParams null");
            iTResResponseCallback.onComplete(str, new ErrorInfo(300, "param is invalid"), null);
            return;
        }
        if (TmpConstant.PROPERTY_IDENTIFIER_SET.compareToIgnoreCase(str) == 0) {
            LogCat.d(TAG, "onProcess set callback mSetServiceHandler:" + this.mSetServiceHandler);
            CommonRequestPayload commonRequestPayload2 = (CommonRequestPayload) GsonUtils.fromJson(String.valueOf(obj), new TypeToken<CommonRequestPayload<Map<String, ValueWrapper>>>() { // from class: com.aliyun.alink.linksdk.tmp.device.deviceshadow.TPropServiceHandler.2
            }.getType());
            if (commonRequestPayload2 != null && this.mSetServiceHandler != null) {
                MapInputParams mapInputParams = new MapInputParams((Map) commonRequestPayload2.getParams());
                this.mSetServiceHandler.onProcess(str, mapInputParams, new SetPropCbWrapper(this.mShadowRef, mapInputParams, iTResResponseCallback));
                return;
            } else {
                new SetPropCbWrapper(this.mShadowRef, new MapInputParams((Map) commonRequestPayload2.getParams()), iTResResponseCallback).onComplete(str, null, null);
                return;
            }
        }
        if ("post".compareToIgnoreCase(str) == 0) {
            LogCat.d(TAG, "onProcess post callback ");
            iTResResponseCallback.onComplete(str, null, null);
        } else {
            LogCat.e(TAG, "identifier error identifier:" + str);
        }
    }

    public boolean setPropGetServiceHandler(ITResRequestHandler iTResRequestHandler) {
        LogCat.d(TAG, "setPropGetServiceHandler handler:" + iTResRequestHandler);
        this.mGetServiceHandler = iTResRequestHandler;
        return true;
    }

    public boolean setPropSetServiceHandler(ITResRequestHandler iTResRequestHandler) {
        LogCat.d(TAG, "setPropSetServiceHandler handler:" + iTResRequestHandler);
        this.mSetServiceHandler = iTResRequestHandler;
        return true;
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onSuccess(Object obj, OutputParams outputParams) {
        ALog.d(TAG, "onSuccess returnValue:" + outputParams);
    }

    @Override // com.aliyun.alink.linksdk.tmp.listener.IDevListener
    public void onFail(Object obj, ErrorInfo errorInfo) {
        ALog.d(TAG, "onFail errorInfo:" + errorInfo);
    }

    public static class GetPropCbWrapper implements ITResResponseCallback {
        @Override // com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback
        public void onComplete(String str, ErrorInfo errorInfo, Object obj) {
            LogCat.d(TPropServiceHandler.TAG, "GetPropCbWrapper onComplete");
        }
    }

    public static class SetPropCbWrapper implements ITResResponseCallback {
        protected ITResResponseCallback mCb;
        protected MapInputParams mInputParams;
        protected WeakReference<TDeviceShadow> mShadowRef;

        public SetPropCbWrapper(WeakReference<TDeviceShadow> weakReference, MapInputParams mapInputParams, ITResResponseCallback iTResResponseCallback) {
            this.mCb = iTResResponseCallback;
            this.mShadowRef = weakReference;
            this.mInputParams = mapInputParams;
        }

        @Override // com.aliyun.alink.linksdk.tmp.listener.ITResResponseCallback
        public void onComplete(String str, ErrorInfo errorInfo, Object obj) {
            LogCat.d(TPropServiceHandler.TAG, "onComplete identifier:" + str + " errorInfo:" + errorInfo + " result:" + obj + " mCb:" + this.mCb);
            ITResResponseCallback iTResResponseCallback = this.mCb;
            if (iTResResponseCallback != null) {
                iTResResponseCallback.onComplete(str, errorInfo, obj);
            }
            ALog.d(TPropServiceHandler.TAG, "onComplete runnable setprop and notify");
            TDeviceShadow tDeviceShadow = this.mShadowRef.get();
            if (tDeviceShadow == null) {
                return;
            }
            if (errorInfo == null || errorInfo.isSuccess()) {
                tDeviceShadow.setPropertyValue((Map<String, ValueWrapper>) new OutputParams(this.mInputParams.getData()), true, (IPublishResourceListener) null);
            }
        }
    }
}
