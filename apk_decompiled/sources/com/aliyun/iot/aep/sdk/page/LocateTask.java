package com.aliyun.iot.aep.sdk.page;

import android.content.Context;
import android.location.Address;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.page.LocateHandler;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/* JADX INFO: loaded from: classes2.dex */
public class LocateTask {
    public static final String TAG = "LocateTask";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private boolean f4840a = true;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Timer f4841b = new Timer();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private LocateHandler f4842c = new LocateHandler();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private int f4843d = 0;
    private CopyOnWriteArrayList<IoTSmart.Country> e = new CopyOnWriteArrayList<>();
    private Context f;

    static /* synthetic */ int f(LocateTask locateTask) {
        int i = locateTask.f4843d;
        locateTask.f4843d = i + 1;
        return i;
    }

    public LocateTask(@NonNull Context context, List<IoTSmart.Country> list, LocateHandler.OnLocationListener onLocationListener) {
        this.f = context;
        if (list != null) {
            this.e.addAll(list);
        }
        this.f4842c.a(onLocationListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public IoTSmart.Country a(String str, String str2) {
        if (this.e.isEmpty()) {
            return null;
        }
        for (IoTSmart.Country country : this.e) {
            if (Objects.equals(str, country.code) || Objects.equals(str, country.domainAbbreviation) || Objects.equals(str2, country.areaName)) {
                return country;
            }
        }
        return null;
    }

    public synchronized void startLocation() {
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: com.aliyun.iot.aep.sdk.page.LocateTask.1
            @Override // java.lang.Runnable
            public void run() {
                while (LocateTask.this.f4840a) {
                    Address addressA = LocationUtil.a(LocateTask.this.f.getApplicationContext());
                    if (addressA != null) {
                        String countryCode = addressA.getCountryCode();
                        if (TextUtils.isEmpty(countryCode) && addressA.getLocale() != null) {
                            countryCode = addressA.getLocale().getCountry();
                        }
                        IoTSmart.Country countryA = LocateTask.this.a(countryCode, addressA.getCountryName());
                        Log.d("LocateTask", "<====" + addressA.getCountryCode() + ", " + addressA.getCountryName() + " =====>");
                        if (countryA != null) {
                            Message message = new Message();
                            message.obj = countryA;
                            message.what = 4;
                            LocateTask.this.f4842c.sendMessage(message);
                            LocateTask.this.f4840a = false;
                            LocateTask.this.f4841b.cancel();
                            LocateTask.this.f4841b = null;
                        } else {
                            try {
                                Thread.sleep(500L);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (LocateTask.this.f4840a) {
                                Message message2 = new Message();
                                message2.what = LocateTask.this.f4843d % 4;
                                LocateTask.f(LocateTask.this);
                                LocateTask.this.f4842c.sendMessage(message2);
                            }
                        }
                    } else {
                        try {
                            Thread.sleep(500L);
                        } catch (InterruptedException e2) {
                            e2.printStackTrace();
                        }
                        if (LocateTask.this.f4840a) {
                            Message message3 = new Message();
                            message3.what = LocateTask.this.f4843d % 4;
                            LocateTask.f(LocateTask.this);
                            LocateTask.this.f4842c.sendMessage(message3);
                        }
                    }
                }
            }
        });
        if (this.f4841b == null) {
            this.f4841b = new Timer();
        }
        this.f4841b.schedule(new TimerTask() { // from class: com.aliyun.iot.aep.sdk.page.LocateTask.2
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                LocateTask.this.f4840a = false;
                Message message = new Message();
                message.what = 5;
                LocateTask.this.f4842c.sendMessage(message);
            }
        }, 30000L);
    }

    public synchronized void stopLocation() {
        this.f4840a = false;
        if (this.f4841b != null) {
            this.f4841b.cancel();
        }
        if (this.f4842c != null) {
            this.f4842c.removeCallbacksAndMessages(null);
        }
    }
}
