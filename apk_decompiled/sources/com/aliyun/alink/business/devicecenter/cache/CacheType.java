package com.aliyun.alink.business.devicecenter.cache;

import android.util.LruCache;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;

/* JADX INFO: loaded from: classes.dex */
public enum CacheType {
    CLOUD_ENROLLEE(AlinkConstants.KEY_CACHE_CLOUD_ENROLLEE, 256),
    BATCH_CLOUD_ENROLLEE(AlinkConstants.KEY_CACHE_BATCH_CLOUD_ENROLLEE, 256),
    DEVICE_NOTIFY_TOKEN(AlinkConstants.KEY_CACHE_DEVICE_NOTIFY_TOKEN, 256),
    APP_SEND_TOKEN(AlinkConstants.KEY_CACHE_APP_SEND_TOKEN, 256),
    SAP_PROVISIONED_SSID(AlinkConstants.KEY_CACHE_SAP_PROVISIONED_SSID, 256),
    BLE_DISCOVERED_DEVICE(AlinkConstants.KEY_CACHE_BLE_DISCOVERED_DEVICE, 256),
    GENIE_SOUND_BOX_DEVICE(AlinkConstants.KEY_CACHE_GENIE_SOUND_BOX, 256),
    BLE_MESH_DISCOVERED_DEVICE(AlinkConstants.KEY_CACHE_BLE_MESH_DISCOVERED_DEVICE, 256);

    public String keyName;
    public LruCache<String, Object> lruCache;

    CacheType(String str, int i) {
        this.lruCache = null;
        this.keyName = null;
        this.lruCache = new LruCache<>(256);
        this.keyName = str;
    }

    public String getCacheName() {
        return this.keyName;
    }

    public LruCache<String, Object> getLruCache() {
        return this.lruCache;
    }
}
