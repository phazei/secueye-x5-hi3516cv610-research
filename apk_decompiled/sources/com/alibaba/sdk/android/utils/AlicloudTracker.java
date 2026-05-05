package com.alibaba.sdk.android.utils;

import android.text.TextUtils;
import android.util.Log;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class AlicloudTracker {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private c f3208a;

    /* JADX INFO: renamed from: a, reason: collision with other field name */
    private String f32a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f3209b;

    /* JADX INFO: renamed from: b, reason: collision with other field name */
    private Map<String, String> f33b = new HashMap();

    AlicloudTracker(c cVar, String str, String str2) {
        this.f3208a = cVar;
        this.f32a = str;
        this.f3209b = str2;
    }

    public void sendCustomHit(String str, long j, Map<String, String> map) {
        try {
            if (this.f3208a == null) {
                Log.e("AlicloudTracker", "dataTracker is null, can not sendCustomHit");
                return;
            }
            if (map == null) {
                map = new HashMap<>();
            }
            map.putAll(this.f33b);
            map.put("sdkId", this.f32a);
            map.put("sdkVersion", this.f3209b);
            this.f3208a.sendCustomHit(this.f32a + OpenAccountUIConstants.UNDER_LINE + str, j, map);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCustomHit(String str, Map<String, String> map) {
        sendCustomHit(str, 0L, map);
    }

    public void setGlobalProperty(String str, String str2) {
        if (!TextUtils.isEmpty(str) && str2 != null) {
            if (this.f33b.containsKey(str)) {
                this.f33b.remove(str);
            }
            this.f33b.put(str, str2);
            return;
        }
        Log.e("AlicloudTracker", "key is null or key is empty or value is null,please check it!");
    }

    public void removeGlobalProperty(String str) {
        if (!TextUtils.isEmpty(str) && this.f33b.containsKey(str)) {
            this.f33b.remove(str);
        } else {
            Log.e("AlicloudTracker", "key is null or key is empty,please check it!");
        }
    }
}
