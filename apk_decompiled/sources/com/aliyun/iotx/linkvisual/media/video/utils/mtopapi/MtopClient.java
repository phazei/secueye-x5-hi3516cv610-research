package com.aliyun.iotx.linkvisual.media.video.utils.mtopapi;

import android.text.TextUtils;
import com.alibaba.ailabs.tg.mtop.OnResponseListener;
import com.alibaba.ailabs.tg.network.mtop.inner.MtopHelper;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iotx.linkvisual.media.video.utils.IAPIHelperListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lvbyte.lvif;
import mtopsdk.mtop.domain.BaseOutDo;

/* JADX INFO: loaded from: classes2.dex */
public class MtopClient {
    private static final int SKILL_ID = 515;
    public static final String TAG = "MtopClient";
    private APIResponseListener apiResponseListener;

    private class APIResponseListener implements OnResponseListener {
        private ConcurrentHashMap<Integer, IAPIHelperListener> callbackHashMap;

        private APIResponseListener() {
            this.callbackHashMap = new ConcurrentHashMap<>();
        }

        private void unregisterCallBack(int i) {
            this.callbackHashMap.remove(Integer.valueOf(i));
        }

        public void onResponseFailed(int i, String str, String str2) {
            ALog.e(MtopClient.TAG, "APIResponseListener   onResponseFailed:" + str + "   msg:" + str2 + "   userFlag:" + i);
            if (this.callbackHashMap.containsKey(Integer.valueOf(i))) {
                IAPIHelperListener iAPIHelperListener = this.callbackHashMap.get(Integer.valueOf(i));
                if (iAPIHelperListener != null) {
                    MtopClient.this.processAPIResponseFailed(iAPIHelperListener, str, str2);
                } else {
                    ALog.e(MtopClient.TAG, "APIResponseListener   callback is null");
                }
                unregisterCallBack(i);
            }
        }

        public void onResponseSuccess(BaseOutDo baseOutDo, int i) {
            ALog.d(MtopClient.TAG, "APIResponseListener   onResponseSuccess:" + JSON.toJSONString(baseOutDo) + "   userFlag:" + i);
            if (this.callbackHashMap.containsKey(Integer.valueOf(i))) {
                IAPIHelperListener iAPIHelperListener = this.callbackHashMap.get(Integer.valueOf(i));
                if (iAPIHelperListener != null) {
                    MtopClient.this.processAPIResponseSuccess(iAPIHelperListener, baseOutDo);
                } else {
                    ALog.e("IPCDevice", "APIResponseListener   callback is null");
                }
                unregisterCallBack(i);
            }
        }

        public void registerCallBack(IAPIHelperListener iAPIHelperListener) {
            if (iAPIHelperListener != null) {
                this.callbackHashMap.put(Integer.valueOf(iAPIHelperListener.hashCode()), iAPIHelperListener);
            }
        }
    }

    private static class MtopClientHolder {
        private static MtopClient mtopClient = new MtopClient();

        private MtopClientHolder() {
        }
    }

    private MtopClient() {
        this.apiResponseListener = new APIResponseListener();
    }

    private void baseApiServiceInvoke(String str, OnResponseListener onResponseListener, int i, String str2) {
        MtopAlibabaAicloudIotAppAliyunPassthroughRequest mtopAlibabaAicloudIotAppAliyunPassthroughRequest = new MtopAlibabaAicloudIotAppAliyunPassthroughRequest();
        mtopAlibabaAicloudIotAppAliyunPassthroughRequest.setAuthInfo(str);
        mtopAlibabaAicloudIotAppAliyunPassthroughRequest.setAliyunPassthroughParam(str2);
        MtopHelper.getInstance().asyncRequestApi(mtopAlibabaAicloudIotAppAliyunPassthroughRequest, MtopAlibabaAicloudIotAppAliyunPassthroughResponse.class, "POST", onResponseListener, i);
    }

    public static MtopClient getInstance() {
        return MtopClientHolder.mtopClient;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAPIResponseFailed(IAPIHelperListener iAPIHelperListener, String str, String str2) {
        if (iAPIHelperListener != null) {
            lvif lvifVar = new lvif();
            lvifVar.lvdo((TextUtils.isEmpty(str) || !TextUtils.isDigitsOnly(str)) ? 500 : Integer.parseInt(str));
            lvifVar.lvif(str2);
            lvifVar.lvdo(str2);
            lvifVar.lvdo((Object) null);
            iAPIHelperListener.onFailed(lvifVar);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processAPIResponseSuccess(IAPIHelperListener iAPIHelperListener, BaseOutDo baseOutDo) {
        if (iAPIHelperListener != null) {
            if (baseOutDo == null) {
                lvif lvifVar = new lvif();
                lvifVar.lvdo(500);
                lvifVar.lvif("data is null");
                lvifVar.lvdo("data is null");
                lvifVar.lvdo((Object) null);
                iAPIHelperListener.onFailed(lvifVar);
                return;
            }
            MtopAlibabaAicloudIotAppAliyunPassthroughResponseData mtopAlibabaAicloudIotAppAliyunPassthroughResponseData = (MtopAlibabaAicloudIotAppAliyunPassthroughResponseData) baseOutDo.getData();
            lvif lvifVar2 = new lvif();
            lvifVar2.lvdo(200);
            lvifVar2.lvif(mtopAlibabaAicloudIotAppAliyunPassthroughResponseData.getMsgInfo());
            lvifVar2.lvdo(mtopAlibabaAicloudIotAppAliyunPassthroughResponseData.getMsgInfo());
            lvifVar2.lvdo(mtopAlibabaAicloudIotAppAliyunPassthroughResponseData.getModel());
            iAPIHelperListener.onResponse(lvifVar2);
        }
    }

    public void sendIoTRequest(Map<String, Object> map, String str, String str2, String str3, String str4, IAPIHelperListener iAPIHelperListener) {
        this.apiResponseListener.registerCallBack(iAPIHelperListener);
        HashMap map2 = new HashMap(5);
        map2.put("args", map == null ? "{}" : JSON.toJSONString(map));
        map2.put("skillId", 515);
        map2.put("path", str);
        map2.put("apiVersion", str4);
        map2.put("deviceId", str3);
        baseApiServiceInvoke(str2, this.apiResponseListener, iAPIHelperListener != null ? iAPIHelperListener.hashCode() : 0, JSON.toJSONString(map2));
    }
}
