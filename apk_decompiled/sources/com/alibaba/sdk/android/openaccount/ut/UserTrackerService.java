package com.alibaba.sdk.android.openaccount.ut;

import android.app.Activity;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public interface UserTrackerService {
    String getDefaultUserTrackerId();

    void sendCustomHit(String str, int i, String str2, long j, String str3, Map<String, String> map);

    void sendCustomHit(String str, long j, String str2, Map<String, String> map);

    void sendCustomHit(String str, Activity activity2);

    void sendCustomHit(String str, String str2, int i, String str3, long j, String str4, Map<String, String> map);

    void sendCustomHit(String str, String str2, Map<String, String> map);

    void updateUserTrackerProperties(Map<String, Object> map);
}
