package com.aliyun.ams.emas.push.notification;

import android.os.Build;
import com.taobao.accs.utl.ALog;
import java.util.ArrayList;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class AgooCPushNotification {
    public static final String TAG = "MPS:AgooCPushNotification";
    public static ArrayList<Integer> priorityMap = new ArrayList<>();
    private String appId;
    private String emasGroup;
    private String extData;
    private Map<String, String> extraMap;
    private String group;
    private String messageId;
    private String notificationChannel;
    private int notificationId;
    private String openActivity;
    private int openType;
    private String openUrl;
    private int priority;
    private String source;
    private String summary;
    private String taskId;
    private String title;

    public AgooCPushNotification() {
        int i = Build.VERSION.SDK_INT;
        this.priority = 0;
    }

    static {
        if (Build.VERSION.SDK_INT >= 16) {
            priorityMap.add(-2);
            priorityMap.add(0);
            priorityMap.add(1);
            priorityMap.add(-1);
            priorityMap.add(2);
            return;
        }
        priorityMap.add(-2);
        priorityMap.add(0);
        priorityMap.add(1);
        priorityMap.add(-1);
        priorityMap.add(2);
    }

    public int getOpenType() {
        return this.openType;
    }

    public void setOpenType(int i) {
        this.openType = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSummary() {
        return this.summary;
    }

    public void setSummary(String str) {
        this.summary = str;
    }

    public String getOpenUrl() {
        return this.openUrl;
    }

    public void setOpenUrl(String str) {
        this.openUrl = str;
    }

    public Map<String, String> getExtraMap() {
        return this.extraMap;
    }

    public void setExtraMap(Map<String, String> map) {
        this.extraMap = map;
    }

    public String getMessageId() {
        return this.messageId;
    }

    public void setMessageId(String str) {
        this.messageId = str;
    }

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String str) {
        this.appId = str;
    }

    public String getOpenActivity() {
        return this.openActivity;
    }

    public void setOpenActivity(String str) {
        this.openActivity = str;
    }

    public int getNotificationId() {
        return this.notificationId;
    }

    public void setNotificationId(int i) {
        if (i < 0) {
            this.notificationId = i * (-1);
        } else {
            this.notificationId = i;
        }
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(String str) {
        try {
            if (priorityMap.contains(Integer.valueOf(Integer.parseInt(str)))) {
                this.priority = Integer.parseInt(str);
            }
        } catch (NumberFormatException e) {
            ALog.e(TAG, "formar error:数字格式错误", e, new Object[0]);
        }
    }

    public String getTaskId() {
        return this.taskId;
    }

    public void setTaskId(String str) {
        this.taskId = str;
    }

    public String getExtData() {
        return this.extData;
    }

    public void setExtData(String str) {
        this.extData = str;
    }

    public String getNotificationChannel() {
        return this.notificationChannel;
    }

    public void setNotificationChannel(String str) {
        this.notificationChannel = str;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String str) {
        this.source = str;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String str) {
        this.group = str;
    }

    public String getEmasGroup() {
        return this.emasGroup;
    }

    public void setEmasGroup(String str) {
        this.emasGroup = str;
    }
}
