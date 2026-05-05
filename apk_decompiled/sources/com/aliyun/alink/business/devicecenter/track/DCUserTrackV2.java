package com.aliyun.alink.business.devicecenter.track;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class DCUserTrackV2 {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Map<String, String> f3750a = new ConcurrentHashMap();

    public final HashMap<String, String> a() {
        HashMap<String, String> map = new HashMap<>();
        map.putAll(this.f3750a);
        return map;
    }

    public void addTrackData(String... strArr) {
        if (strArr == null || strArr.length < 2 || strArr.length % 2 == 1) {
            return;
        }
        for (int i = 0; i < strArr.length / 2; i += 2) {
            if (strArr[i] != null) {
                int i2 = i + 1;
                if (strArr[i2] != null) {
                    this.f3750a.put(strArr[i], strArr[i2]);
                }
            }
        }
    }

    public boolean hasKey(String str) {
        Map<String, String> map = this.f3750a;
        return map != null && map.containsKey(str);
    }

    public void removeTrackData(String str) {
        Map<String, String> map;
        if (TextUtils.isEmpty(str) || (map = this.f3750a) == null) {
            return;
        }
        map.remove(str);
    }

    public void resetTrackData() {
        Map<String, String> map = this.f3750a;
        if (map != null) {
            map.clear();
        }
    }

    public void sendEvent() {
        ALog.d("DCUserTrackV2", "sendEvent() called track data = " + this.f3750a);
        if (!this.f3750a.containsKey(AlinkConstants.KEY_PROVISION_STARTED)) {
            ALog.w("DCUserTrackV2", "sendEvent provision not started, do not send event.");
            resetTrackData();
        } else {
            removeTrackData(AlinkConstants.KEY_PROVISION_STARTED);
            AUserTrack.record(AlinkConstants.KEY_DC_PROVISION, a());
            resetTrackData();
        }
    }

    public void sendEvent(String str) {
        ALog.d("DCUserTrackV2", "sendEvent() called with: event = [" + str + "], trackData = [" + this.f3750a + "]");
        AUserTrack.record(AlinkConstants.KEY_DC_PROVISION_DISCOVER, a());
    }
}
