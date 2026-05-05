package com.taobao.agoo.control;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.google.android.exoplayer2.text.ttml.TtmlNode;
import com.taobao.accs.AccsClientConfig;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.UtilityImpl;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.android.agoo.common.Config;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
public class AgooBindCache {
    private static final int BINDED = 2;
    private static final int BINDING = 1;
    public static final String SP_AGOO_BIND_FILE_NAME = "EMAS_AGOO_BIND";
    private static final String SP_BIND_KEY = "bind_status";
    private static final String TAG = "AgooBindCache";
    private static final int UNBINDED = 4;
    private static final int UNBINDING = 3;
    public static boolean registerDeviceEveryTime = true;
    private long agooLastFlushTime;
    private ConcurrentMap<String, Integer> mAgooBindStatus = new ConcurrentHashMap();
    private Context mContext;
    private String spAgooBindFileName;

    public AgooBindCache(Context context) {
        if (context == null) {
            throw new RuntimeException("Context is null!!");
        }
        this.mContext = context.getApplicationContext();
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("EMAS_AGOO_BIND");
            sb.append(Config.getAccsConfigTag(context));
            sb.append(AccsClientConfig.getConfigByTag(Config.getAccsConfigTag(context)).getInappHost());
            this.spAgooBindFileName = sb.toString();
        } catch (Throwable unused) {
        }
    }

    public void onAgooRegister(String str) {
        Integer num = this.mAgooBindStatus.get(str);
        if (num == null || num.intValue() != 2) {
            this.mAgooBindStatus.put(str, 2);
            saveClients(this.mContext, this.spAgooBindFileName, this.agooLastFlushTime, this.mAgooBindStatus);
        }
    }

    public void onAgooUnregister(String str) {
        ALog.i(TAG, "onAgooUnregister", new Object[0]);
        Integer num = this.mAgooBindStatus.get(str);
        if (num == null || num.intValue() != 4) {
            this.mAgooBindStatus.put(str, 4);
            saveClients(this.mContext, "EMAS_AGOO_BIND", this.agooLastFlushTime, this.mAgooBindStatus);
        }
    }

    public boolean isAgooRegistered(String str) {
        if (this.mAgooBindStatus.isEmpty()) {
            restoreAgooClients();
        }
        Integer num = this.mAgooBindStatus.get(str);
        ALog.i(TAG, "isAgooRegistered", Constants.KEY_PACKAGE_NAME, str, "appStatus", num, "agooBindStatus", this.mAgooBindStatus);
        if (registerDeviceEveryTime || UtilityImpl.utdidChanged(Config.PREFERENCES, this.mContext)) {
            return false;
        }
        return num != null && num.intValue() == 2;
    }

    private void restoreAgooClients() {
        try {
            String string = this.mContext.getSharedPreferences(this.spAgooBindFileName, 0).getString(SP_BIND_KEY, null);
            if (TextUtils.isEmpty(string)) {
                ALog.w(TAG, "restoreAgooClients packs null return", new Object[0]);
                return;
            }
            JSONArray jSONArray = new JSONArray(string);
            this.agooLastFlushTime = jSONArray.getLong(0);
            if (System.currentTimeMillis() < this.agooLastFlushTime + 86400000) {
                for (int i = 1; i < jSONArray.length(); i++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    this.mAgooBindStatus.put(jSONObject.getString(TtmlNode.TAG_P), Integer.valueOf(jSONObject.getInt("s")));
                }
                ALog.i(TAG, "restoreAgooClients", "mAgooBindStatus", this.mAgooBindStatus);
                return;
            }
            ALog.i(TAG, "restoreAgooClients expired", "agooLastFlushTime", Long.valueOf(this.agooLastFlushTime));
            this.agooLastFlushTime = 0L;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveClients(Context context, String str, long j, Map<String, Integer> map) {
        try {
            String[] strArr = (String[]) map.keySet().toArray(new String[0]);
            JSONArray jSONArray = new JSONArray();
            if (j > 0 && j < System.currentTimeMillis()) {
                jSONArray.put(j);
            } else {
                jSONArray.put(System.currentTimeMillis() - (Math.random() * 8.64E7d));
            }
            for (String str2 : strArr) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put(TtmlNode.TAG_P, str2);
                jSONObject.put("s", map.get(str2).intValue());
                jSONArray.put(jSONObject);
            }
            SharedPreferences.Editor editorEdit = context.getSharedPreferences(str, 0).edit();
            editorEdit.putString(SP_BIND_KEY, jSONArray.toString());
            editorEdit.apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        this.mAgooBindStatus.clear();
        this.agooLastFlushTime = 0L;
        try {
            this.mContext.getSharedPreferences(this.spAgooBindFileName, 0).edit().clear().commit();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
