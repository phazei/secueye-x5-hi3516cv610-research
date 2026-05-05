package com.alibaba.sdk.android.push.popup;

import android.content.Intent;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.JsonUtility;
import com.taobao.agoo.BaseNotifyClick;
import java.util.HashMap;
import java.util.Map;
import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class PopupNotifyClick extends BaseNotifyClick {
    static final String TAG = "PopupNotifyClick";
    private final PopupNotifyClickListener mPopupNotifyClickListener;

    public PopupNotifyClick(PopupNotifyClickListener popupNotifyClickListener) {
        this.mPopupNotifyClickListener = popupNotifyClickListener;
    }

    @Override // com.taobao.agoo.BaseNotifyClick
    public void onParseFailed(Intent intent) {
        PopupNotifyClickListener popupNotifyClickListener = this.mPopupNotifyClickListener;
        if (popupNotifyClickListener instanceof OnPushParseFailedListener) {
            ((OnPushParseFailedListener) popupNotifyClickListener).onParseFailed(intent);
        }
    }

    @Override // com.taobao.agoo.BaseNotifyClick
    public void onNotPushData(Intent intent) {
        PopupNotifyClickListener popupNotifyClickListener = this.mPopupNotifyClickListener;
        if (popupNotifyClickListener instanceof OnPushParseFailedListener) {
            ((OnPushParseFailedListener) popupNotifyClickListener).onNotPushData(intent);
        }
    }

    @Override // com.taobao.agoo.BaseNotifyClick
    public void onMessage(Intent intent) {
        if (intent == null) {
            ALog.e(TAG, "intent null, return", new Object[0]);
            return;
        }
        String stringExtra = intent.getStringExtra("body");
        if (stringExtra != null) {
            ALog.i(TAG, "Receive notification, body: " + stringExtra, new Object[0]);
            try {
                Map<String, String> map = JsonUtility.toMap(new JSONObject(stringExtra));
                String str = map.get("title");
                String str2 = map.get("content");
                String str3 = map.get("msg_id");
                int i = Integer.parseInt(map.get("type"));
                if (1 == i) {
                    Map<String, String> map2 = JsonUtility.toMap(new JSONObject(map.get("ext")));
                    map2.put(AgooConstants.MESSAGE_BODY_MSG_ID_ALIYUN_FLAG, str3);
                    if (this.mPopupNotifyClickListener != null) {
                        this.mPopupNotifyClickListener.onSysNoticeOpened(str, str2, map2);
                    } else {
                        ALog.e(TAG, "PopupNotifyClickListener is null", new Object[0]);
                    }
                } else if (2 == i) {
                    HashMap map3 = new HashMap();
                    map3.put(AgooConstants.MESSAGE_BODY_MSG_ID_ALIYUN_FLAG, str3);
                    if (this.mPopupNotifyClickListener != null) {
                        this.mPopupNotifyClickListener.onSysNoticeOpened(str, str2, map3);
                    } else {
                        ALog.e(TAG, "PopupNotifyClickListener is null", new Object[0]);
                    }
                }
            } catch (JSONException e) {
                ALog.e(TAG, "Parse json error, " + e, new Object[0]);
            }
        }
    }
}
