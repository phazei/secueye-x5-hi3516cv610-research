package com.aliyun.alink.linksdk.tmp.device.panel.listener;

import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.deviceshadow.DeviceShadowMgr;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class SerializePropCallback implements IPanelEventCallback {
    private static final String TAG = "[Tmp]SerializePropCallback";
    protected IPanelEventCallback mListener;

    public SerializePropCallback(IPanelEventCallback iPanelEventCallback) {
        this.mListener = iPanelEventCallback;
    }

    @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelEventCallback
    public void onNotify(String str, String str2, Object obj) {
        if (this.mListener == null) {
            ALog.e(TAG, "onNotify listener empty return");
            return;
        }
        if (obj != null && !TextUtils.isEmpty(str2) && str2.endsWith(TmpConstant.MQTT_TOPIC_PROPERTIES)) {
            JSONObject object = JSONObject.parseObject(obj.toString());
            JSONObject jSONObject = object.getJSONObject("items");
            String cachedProps = DeviceShadowMgr.getInstance().getCachedProps(str);
            boolean z = false;
            try {
                if (jSONObject != null) {
                    JSONObject jSONObject2 = cachedProps == null ? new JSONObject() : JSON.parseObject(cachedProps);
                    for (String str3 : jSONObject.keySet()) {
                        JSONObject jSONObject3 = jSONObject.getJSONObject(str3);
                        JSONObject jSONObject4 = jSONObject2.getJSONObject(str3);
                        if (DeviceShadowMgr.getInstance().comparePropertyValue(jSONObject3, jSONObject4)) {
                            ALog.w(TAG, "onNotify new prop is new, key:" + str3 + " newPropertyValue:" + jSONObject3 + " oldPropertyValue:" + jSONObject4);
                        } else {
                            ALog.w(TAG, "onNotify new prop is older, key:" + str3 + " newPropertyValue:" + jSONObject3 + " oldPropertyValue:" + jSONObject4);
                            jSONObject.put(str3, (Object) jSONObject4);
                            z = true;
                        }
                    }
                }
                if (z) {
                    object.put("items", (Object) jSONObject);
                }
            } catch (Exception e) {
                ALog.e(TAG, "onNotify error:" + e.toString());
            }
            this.mListener.onNotify(str, str2, object.toString());
            return;
        }
        this.mListener.onNotify(str, str2, obj);
    }
}
