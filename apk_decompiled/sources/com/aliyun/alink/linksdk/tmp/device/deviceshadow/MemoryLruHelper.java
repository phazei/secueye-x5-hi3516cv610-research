package com.aliyun.alink.linksdk.tmp.device.deviceshadow;

import android.util.LruCache;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class MemoryLruHelper {
    public static final int MAX_LRU_SIZE = 5242880;
    public static final String TAG = "[Tmp]MemoryLruHelper";
    public Map<String, Boolean> isNeedRefreshPropertyMap;
    protected LruCache<String, String> mMemoryLruCahce = new LruCache<>(5242880);

    public String getString(String str) {
        String str2;
        try {
            str2 = this.mMemoryLruCahce.get(str);
        } catch (Exception e) {
            ALog.e(TAG, "mDiskLruCache get error:" + e.toString());
            str2 = null;
        }
        ALog.d(TAG, "getString key:" + str + " result:" + str2);
        return str2;
    }

    public void setRefreshProperty(String str, boolean z) {
        if (this.isNeedRefreshPropertyMap == null) {
            this.isNeedRefreshPropertyMap = new HashMap();
        }
        this.isNeedRefreshPropertyMap.put(str, Boolean.valueOf(z));
    }

    public Boolean isRefreshProperty(String str) {
        Map<String, Boolean> map = this.isNeedRefreshPropertyMap;
        if (map != null && map.containsKey(str)) {
            return this.isNeedRefreshPropertyMap.get(str);
        }
        return false;
    }

    public boolean saveValue(String str, String str2) {
        ALog.d(TAG, "saveValue key:" + str + " data:" + str2);
        try {
            this.mMemoryLruCahce.put(str, str2);
            return true;
        } catch (Exception e) {
            ALog.e(TAG, "mDiskLruCache saveValue error: " + e.toString());
            return false;
        }
    }

    public void deleteValue(String str) {
        ALog.d(TAG, "deleteValue key:" + str);
        try {
            this.mMemoryLruCahce.remove(str);
        } catch (Exception e) {
            ALog.e(TAG, "deleteValue error:" + e.toString());
        }
    }
}
