package com.aliyun.alink.linksdk.tmp.storage;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsProvider;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsQeuryCallback;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.api.DeviceManager;
import com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener;
import com.aliyun.alink.linksdk.tmp.data.script.ScriptResponseItem;
import com.aliyun.alink.linksdk.tmp.data.script.ScriptResponsePayload;
import com.aliyun.alink.linksdk.tmp.utils.CloudUtils;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/* JADX INFO: loaded from: classes2.dex */
public class JSQueryApiGateProvider implements IJsProvider {
    public static final String TAG = "[Tmp]JSQueryApiGateProvider";
    protected Map<String, String> mScriptList = new ConcurrentHashMap();

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IJsProvider
    public void queryJsCode(final String str, String str2, final IJsQeuryCallback iJsQeuryCallback) {
        String str3 = this.mScriptList.get(str);
        if (!TextUtils.isEmpty(str3)) {
            iJsQeuryCallback.onLoad(str, str3);
            return;
        }
        TmpSdk.getCloudProxy().queryProtocolScript(DeviceManager.getInstance().getDevIotId(TextHelper.combineStr(str, str2)), TmpStorage.getInstance().getDigest(str), TmpStorage.getInstance().getDigestMethod(str), new ICloudProxyListener() { // from class: com.aliyun.alink.linksdk.tmp.storage.JSQueryApiGateProvider.1
            @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener
            public void onResponse(String str4, Object obj) {
                if (obj == null) {
                    ALog.e(JSQueryApiGateProvider.TAG, "queryJsCode onResponse data null");
                    iJsQeuryCallback.onLoad(str, null);
                    return;
                }
                ALog.d(JSQueryApiGateProvider.TAG, "queryProductJsCode payload:" + obj.toString());
                ScriptResponsePayload scriptResponsePayload = (ScriptResponsePayload) GsonUtils.fromJson(obj.toString(), new TypeToken<ScriptResponsePayload>() { // from class: com.aliyun.alink.linksdk.tmp.storage.JSQueryApiGateProvider.1.1
                }.getType());
                if (scriptResponsePayload == null || scriptResponsePayload.data == null || scriptResponsePayload.data.size() <= 0) {
                    ALog.e(JSQueryApiGateProvider.TAG, "queryJsCode onResponse payload data null");
                    iJsQeuryCallback.onLoad(str, null);
                    return;
                }
                ScriptResponseItem scriptResponseItem = scriptResponsePayload.data.get(0);
                if (TextUtils.isEmpty(scriptResponseItem.url)) {
                    String script = TmpStorage.getInstance().getScript(str);
                    if (!TextUtils.isEmpty(script)) {
                        JSQueryApiGateProvider.this.mScriptList.put(str, script);
                    }
                    iJsQeuryCallback.onLoad(str, script);
                    return;
                }
                CloudUtils.queryHttpUrl(scriptResponseItem.url, new Callback() { // from class: com.aliyun.alink.linksdk.tmp.storage.JSQueryApiGateProvider.1.2
                    @Override // okhttp3.Callback
                    public void onFailure(Call call, IOException iOException) {
                        iOException.printStackTrace();
                        ALog.e(JSQueryApiGateProvider.TAG, "queryJsCode queryHttpUrl onFailure");
                        iJsQeuryCallback.onLoad(str, null);
                    }

                    @Override // okhttp3.Callback
                    public void onResponse(Call call, Response response) throws IOException {
                        if (response == null || response.body() == null) {
                            ALog.e(JSQueryApiGateProvider.TAG, "queryJsCode queryHttpUrl response null");
                            iJsQeuryCallback.onLoad(str, null);
                            return;
                        }
                        String strString = response.body().string();
                        if (!TextUtils.isEmpty(strString)) {
                            ALog.d(JSQueryApiGateProvider.TAG, "queryHttpUrl onResponse body notempty");
                            JSQueryApiGateProvider.this.mScriptList.put(str, strString);
                            TmpStorage.getInstance().saveScript(str, strString);
                        }
                        iJsQeuryCallback.onLoad(str, strString);
                    }
                });
            }

            @Override // com.aliyun.alink.linksdk.tmp.component.cloud.ICloudProxyListener
            public void onFailure(String str4, AError aError) {
                StringBuilder sb = new StringBuilder();
                sb.append("queryJsCode onFailure error:");
                sb.append(aError);
                ALog.e(JSQueryApiGateProvider.TAG, sb.toString() == null ? TmpConstant.GROUP_ROLE_UNKNOWN : aError.toString());
                String script = TmpStorage.getInstance().getScript(str);
                if (!TextUtils.isEmpty(script)) {
                    JSQueryApiGateProvider.this.mScriptList.put(str, script);
                }
                iJsQeuryCallback.onLoad(str, script);
            }
        });
    }
}
