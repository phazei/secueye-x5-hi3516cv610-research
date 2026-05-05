package com.aliyun.iot.aep.sdk.framework.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import java.util.HashMap;

/* JADX INFO: loaded from: classes2.dex */
public class BroadCastUtil {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static HashMap<String, Intent> f4737a = new HashMap<>(100);

    public static void broadCast(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    public static void broadCastSticky(Context context, Intent intent) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        f4737a.put(intent.getAction(), intent);
    }

    public static void register(Context context, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        LocalBroadcastManager.getInstance(context).registerReceiver(broadcastReceiver, intentFilter);
        for (int i = 0; i < intentFilter.countActions(); i++) {
            Intent intent = f4737a.get(intentFilter.getAction(i));
            if (intent != null) {
                broadcastReceiver.onReceive(context, intent);
            }
        }
    }

    public static void unRegister(Context context, BroadcastReceiver broadcastReceiver) {
        LocalBroadcastManager.getInstance(context).unregisterReceiver(broadcastReceiver);
    }
}
