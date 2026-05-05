package com.aliyun.alink.linksdk.tmp.device.deviceshadow;

import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.connect.d;
import com.aliyun.alink.linksdk.tmp.connect.e;
import com.aliyun.alink.linksdk.tmp.event.EventManager;
import com.aliyun.alink.linksdk.tmp.event.INotifyHandler;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.ALog;
import java.lang.ref.WeakReference;
import java.util.Set;

/* JADX INFO: loaded from: classes2.dex */
public class TPropEventHandler implements INotifyHandler {
    protected static final String TAG = "[Tmp]TPropEventHandler";
    protected INotifyHandler mEventCallback;
    protected WeakReference<TDeviceShadow> mShadowRef;

    public TPropEventHandler(TDeviceShadow tDeviceShadow) {
        this.mShadowRef = new WeakReference<>(tDeviceShadow);
    }

    @Override // com.aliyun.alink.linksdk.tmp.event.INotifyHandler
    public void onMessage(d dVar, e eVar) {
        JSONObject jSONObject;
        TDeviceShadow tDeviceShadow = this.mShadowRef.get();
        long jCurrentTimeMillis = System.currentTimeMillis();
        ALog.d(TAG, "onMessage shadow:" + tDeviceShadow + " response:" + eVar + " mEventCallback:" + this.mEventCallback + " curTime:" + jCurrentTimeMillis);
        if (tDeviceShadow == null || eVar == null) {
            LogCat.e(TAG, "onMessage null");
            return;
        }
        eVar.b(tDeviceShadow.getIotId());
        try {
            JSONObject object = JSONObject.parseObject(eVar.e());
            String string = object.getString("method");
            if (!TextUtils.isEmpty(string) && string.contains(TmpConstant.METHOD_PROPERTY_POST) && (jSONObject = object.getJSONObject("params")) != null) {
                Set<String> setKeySet = jSONObject.keySet();
                if (setKeySet != null) {
                    for (String str : setKeySet) {
                        Object obj = jSONObject.get(str);
                        if (obj != null) {
                            if (obj instanceof JSONObject) {
                                JSONObject jSONObject2 = (JSONObject) obj;
                                if (!jSONObject2.containsKey("time")) {
                                    JSONObject jSONObject3 = new JSONObject();
                                    jSONObject3.put("time", (Object) Long.valueOf(jCurrentTimeMillis));
                                    jSONObject3.put("value", (Object) jSONObject2);
                                    jSONObject.put(str, (Object) jSONObject3);
                                }
                            } else {
                                JSONObject jSONObject4 = new JSONObject();
                                jSONObject4.put("time", (Object) Long.valueOf(jCurrentTimeMillis));
                                jSONObject4.put("value", obj);
                                jSONObject.put(str, (Object) jSONObject4);
                            }
                        }
                    }
                }
                object.put("params", (Object) jSONObject);
                eVar.a(object.toString());
            }
        } catch (Exception e) {
            ALog.w(TAG, e.toString());
        }
        DeviceShadowMgr.getInstance().onMessage(dVar, eVar);
        EventManager.getInstance().onMessage(dVar, eVar);
    }

    public boolean subEvent(INotifyHandler iNotifyHandler) {
        this.mEventCallback = iNotifyHandler;
        return true;
    }

    public boolean unsubEvent(INotifyHandler iNotifyHandler) {
        this.mEventCallback = null;
        return true;
    }
}
