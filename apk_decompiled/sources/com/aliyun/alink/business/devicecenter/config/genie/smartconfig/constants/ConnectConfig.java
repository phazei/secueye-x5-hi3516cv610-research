package com.aliyun.alink.business.devicecenter.config.genie.smartconfig.constants;

/* JADX INFO: loaded from: classes.dex */
public class ConnectConfig {
    public static final int BLE = 4;
    public static final int SOUND = 2;
    public static final int WIFI = 1;
    public static final int WIFI_AND_SOUND_AND_BLE = 7;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static ConnectConfig f3566a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f3567b = 7;

    public static ConnectConfig getInstance() {
        if (f3566a == null) {
            synchronized (ConnectConfig.class) {
                if (f3566a == null) {
                    f3566a = new ConnectConfig();
                }
            }
        }
        return f3566a;
    }

    public int getModel() {
        return this.f3567b;
    }

    public void setModel(int i) {
        this.f3567b = i;
    }
}
