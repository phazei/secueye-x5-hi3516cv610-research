package com.alibaba.sdk.android.push.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.taobao.accs.utl.ALog;
import com.taobao.agoo.BaseNotifyClickActivity;
import org.android.agoo.common.AgooConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class VivoMsgParseImpl implements BaseNotifyClickActivity.INotifyListener {
    public static final String TAG = "MPS:VivoMsgParseImpl";
    private Context context;

    @Override // com.taobao.agoo.BaseNotifyClickActivity.INotifyListener
    public String getMsgSource() {
        return "vivo";
    }

    @Override // com.taobao.agoo.BaseNotifyClickActivity.INotifyListener
    public String parseMsgFromIntent(Intent intent) {
        String strFixVivoMsg = null;
        if (intent == null) {
            ALog.e(TAG, "parseMsgFromIntent null", new Object[0]);
            return null;
        }
        try {
            String stringExtra = intent.getStringExtra(AgooConstants.MESSAGE_VIVO_PAYLOAD);
            ALog.i(TAG, "parseMsgFromIntent msg:" + stringExtra, new Object[0]);
            strFixVivoMsg = fixVivoMsg(stringExtra, intent);
            ALog.i(TAG, "after fixup msg:" + strFixVivoMsg, new Object[0]);
            return strFixVivoMsg;
        } catch (Throwable th) {
            ALog.e(TAG, "parseMsgFromIntent ", th, new Object[0]);
            return strFixVivoMsg;
        }
    }

    public void setContext(Context context) {
        this.context = context.getApplicationContext();
    }

    private String fixVivoMsg(String str, Intent intent) {
        ALog.i(TAG, "fixVivoMsg intent:" + printBundle(intent.getExtras(), 1), new Object[0]);
        if (str == null) {
            return null;
        }
        try {
            JSONArray jSONArray = new JSONArray(str);
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    if (!jSONObject.has(TtmlNode.TAG_P) && this.context != null) {
                        jSONObject.put(TtmlNode.TAG_P, this.context.getPackageName());
                    }
                    if (!jSONObject.has("ext")) {
                        jSONObject.put("ext", intent.getStringExtra("ext"));
                    }
                    if (!jSONObject.has("b")) {
                        jSONObject.put("b", intent.getStringExtra("b"));
                    }
                    if (!jSONObject.has("f")) {
                        jSONObject.put("f", intent.getLongExtra("f", 0L));
                    }
                    if (!jSONObject.has("i")) {
                        jSONObject.put("i", intent.getStringExtra("i"));
                    }
                    jSONArray.put(i, jSONObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jSONArray.toString();
        } catch (JSONException unused) {
            return str;
        }
    }

    private static String printBundle(Bundle bundle, int i) {
        StringBuilder sb = new StringBuilder();
        if (bundle != null) {
            for (String str : bundle.keySet()) {
                Object obj = bundle.get(str);
                for (int i2 = 0; i2 < i; i2++) {
                    sb.append('\t');
                }
                if (obj instanceof String) {
                    sb.append("String\t");
                    sb.append(str);
                    sb.append('\t');
                    sb.append(obj);
                    sb.append('\n');
                } else if (obj instanceof Integer) {
                    sb.append("int\t");
                    sb.append(str);
                    sb.append('\t');
                    sb.append(obj);
                    sb.append('\n');
                } else if (obj instanceof Long) {
                    sb.append("long\t");
                    sb.append(str);
                    sb.append('\t');
                    sb.append(obj);
                    sb.append('\n');
                } else if (obj instanceof Boolean) {
                    sb.append("boolean\t");
                    sb.append(str);
                    sb.append('\t');
                    sb.append(obj);
                    sb.append('\n');
                } else if (obj instanceof Bundle) {
                    sb.append("Bundle\t");
                    sb.append(str);
                    sb.append('\t');
                    sb.append('\n');
                    sb.append(printBundle((Bundle) obj, i + 1));
                } else {
                    sb.append("unknown\t");
                    sb.append(str);
                    sb.append('\t');
                    sb.append(obj);
                    sb.append('\n');
                }
            }
        }
        return sb.toString();
    }
}
