package com.aliyun.alink.business.devicecenter.biz.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.aliyun.alink.business.devicecenter.utils.PermissionUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class SystemAbilityChangeBroadcast {
    public static final String ACTION_SYSTEM_ABILITY_CHANGE = "ACTION_SYSTEM_ABILITY_CHANGE";
    public static final String ACTION_SYSTEM_BLUETOOTH_STATE = "bluetooth_state";
    public static final String ACTION_SYSTEM_LOCATION_STATE = "location_state";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public Context f3398a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public AtomicBoolean f3399b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public BroadcastReceiver f3400c;

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final SystemAbilityChangeBroadcast f3402a = new SystemAbilityChangeBroadcast();
    }

    public static SystemAbilityChangeBroadcast getInstance() {
        return SingletonHolder.f3402a;
    }

    public void deInit() {
        ALog.d("SystemAbilityChangeBroa", "deInit() called");
        if (this.f3399b.get()) {
            Context context = this.f3398a;
            if (context != null) {
                context.unregisterReceiver(this.f3400c);
            }
            this.f3399b.set(false);
        }
    }

    public void init(Context context) {
        if (context == null) {
            return;
        }
        this.f3398a = context.getApplicationContext();
        if (this.f3399b.compareAndSet(false, true)) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.location.PROVIDERS_CHANGED");
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            Context context2 = this.f3398a;
            if (context2 != null) {
                context2.registerReceiver(this.f3400c, intentFilter);
            }
        }
    }

    public SystemAbilityChangeBroadcast() {
        this.f3398a = null;
        this.f3399b = new AtomicBoolean(false);
        this.f3400c = new BroadcastReceiver() { // from class: com.aliyun.alink.business.devicecenter.biz.service.SystemAbilityChangeBroadcast.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    try {
                        if (intent.getAction() == null) {
                            return;
                        }
                        String action = intent.getAction();
                        byte b2 = -1;
                        int iHashCode = action.hashCode();
                        if (iHashCode != -1530327060) {
                            if (iHashCode == -1184851779 && action.equals("android.location.PROVIDERS_CHANGED")) {
                                b2 = 1;
                            }
                        } else if (action.equals("android.bluetooth.adapter.action.STATE_CHANGED")) {
                            b2 = 0;
                        }
                        if (b2 != 0) {
                            if (b2 != 1) {
                                return;
                            }
                            if (PermissionUtils.isLocationEnabled(context)) {
                                SystemAbilityChangeBroadcast.this.a("location_state", "on");
                                return;
                            } else {
                                SystemAbilityChangeBroadcast.this.a("location_state", "off");
                                return;
                            }
                        }
                        int intExtra = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", 0);
                        if (intExtra == 10) {
                            SystemAbilityChangeBroadcast.this.a("bluetooth_state", "off");
                        } else if (intExtra == 12) {
                            SystemAbilityChangeBroadcast.this.a("bluetooth_state", "on");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ALog.e("SystemAbilityChangeBroa", "mReceiver error");
                    }
                }
            }
        };
    }

    public final void a(String... strArr) {
        if (strArr == null || strArr.length < 1) {
            return;
        }
        Intent intent = new Intent("ACTION_SYSTEM_ABILITY_CHANGE");
        int length = strArr.length;
        for (int i = 0; i < length / 2; i++) {
            if (!TextUtils.isEmpty(strArr[i])) {
                int i2 = i + 1;
                if (!TextUtils.isEmpty(strArr[i2])) {
                    intent.putExtra(strArr[i], strArr[i2]);
                }
            }
        }
        ALog.d("SystemAbilityChangeBroa", "sendBroadcast() called with: intent = [" + intent + "]");
        LocalBroadcastManager.getInstance(this.f3398a).sendBroadcast(intent);
    }
}
