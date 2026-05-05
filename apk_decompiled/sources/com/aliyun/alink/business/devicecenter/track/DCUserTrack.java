package com.aliyun.alink.business.devicecenter.track;

import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class DCUserTrack {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static Map<String, String> f3749a = new ConcurrentHashMap();

    public static HashMap<String, String> a() {
        HashMap<String, String> map = new HashMap<>();
        map.putAll(f3749a);
        return map;
    }

    public static void addTrackData(String... strArr) {
        if (strArr == null || strArr.length < 2 || strArr.length % 2 == 1) {
            return;
        }
        for (int i = 0; i < strArr.length / 2; i += 2) {
            if (strArr[i] != null) {
                int i2 = i + 1;
                if (strArr[i2] != null) {
                    f3749a.put(strArr[i], strArr[i2]);
                }
            }
        }
    }

    public static boolean hasKey(String str) {
        Map<String, String> map = f3749a;
        return map != null && map.containsKey(str);
    }

    public static void removeTrackData(String str) {
        Map<String, String> map;
        if (TextUtils.isEmpty(str) || (map = f3749a) == null) {
            return;
        }
        map.remove(str);
    }

    public static void resetTrackData() {
        Map<String, String> map = f3749a;
        if (map != null) {
            map.clear();
        }
    }

    public static void sendEvent() {
        ALog.d("DCUserTrack", "sendEvent() called track data = " + f3749a);
        if (!f3749a.containsKey(AlinkConstants.KEY_PROVISION_STARTED)) {
            ALog.w("DCUserTrack", "sendEvent provision not started, do not send event.");
            resetTrackData();
        } else {
            removeTrackData(AlinkConstants.KEY_PROVISION_STARTED);
            AUserTrack.record(AlinkConstants.KEY_DC_PROVISION, a());
            resetTrackData();
        }
    }

    public static void sendEvent(String str) {
        ALog.d("DCUserTrack", "sendEvent() called with: event = [" + str + "], trackData = [" + f3749a + "]");
        AUserTrack.record(AlinkConstants.KEY_DC_PROVISION_DISCOVER, a());
    }
}
