package com.aliyun.ams.emas.push.data;

import android.app.NotificationManager;
import android.content.Context;
import com.aliyun.ams.emas.push.notification.AgooCPushNotification;
import com.huawei.hms.push.constant.RemoteMessageConst;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
public class NotificationDataManager {
    private static final ConcurrentHashMap<String, List<AgooCPushNotification>> GROUP_NOTIFICATION_MAP = new ConcurrentHashMap<>();
    private static NotificationDataManager sInstance;
    private static List<Integer> sNotificationList;

    private NotificationDataManager() {
        sNotificationList = new ArrayList();
    }

    public static NotificationDataManager getInstance() {
        if (sInstance == null) {
            sInstance = new NotificationDataManager();
        }
        return sInstance;
    }

    public void addNotification(int i) {
        sNotificationList.add(Integer.valueOf(i));
    }

    public void clearNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(RemoteMessageConst.NOTIFICATION);
        while (!sNotificationList.isEmpty()) {
            notificationManager.cancel(sNotificationList.get(r0.size() - 1).intValue());
            sNotificationList.remove(r0.size() - 1);
        }
        clearGroupAll();
    }

    private void clearGroupAll() {
        if (GROUP_NOTIFICATION_MAP.isEmpty()) {
            return;
        }
        try {
            Iterator<Map.Entry<String, List<AgooCPushNotification>>> it = GROUP_NOTIFICATION_MAP.entrySet().iterator();
            while (it.hasNext()) {
                List<AgooCPushNotification> value = it.next().getValue();
                if (value != null) {
                    value.clear();
                }
            }
            GROUP_NOTIFICATION_MAP.clear();
        } catch (Exception unused) {
        }
    }

    public void addGroupNotification(String str, AgooCPushNotification agooCPushNotification) {
        List<AgooCPushNotification> list = GROUP_NOTIFICATION_MAP.get(str);
        if (list == null || list.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(agooCPushNotification);
            GROUP_NOTIFICATION_MAP.put(str, arrayList);
            return;
        }
        list.add(agooCPushNotification);
    }

    public void clearGroup(String str) {
        List<AgooCPushNotification> list = GROUP_NOTIFICATION_MAP.get(str);
        if (list != null) {
            list.clear();
            GROUP_NOTIFICATION_MAP.remove(str);
        }
    }

    public int getGroupNotifyCount(String str) {
        List<AgooCPushNotification> list = GROUP_NOTIFICATION_MAP.get(str);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        return list.size();
    }
}
