package tools;

import android.content.Context;
import android.location.Address;
import android.os.Message;
import android.util.Log;
import androidx.annotation.NonNull;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.seculink.app.R;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import tools.LocateHandler;

/* JADX INFO: loaded from: classes4.dex */
public class LocateTask {
    public static final String TAG = "LocateTask";
    private Context context;
    private boolean start = true;
    private Timer timer = new Timer();
    private LocateHandler locateHandler = new LocateHandler();
    private int ellipsis_num = 0;
    private CopyOnWriteArrayList<IoTSmart.Country> countryList = new CopyOnWriteArrayList<>();

    public LocateTask(@NonNull Context context, List<IoTSmart.Country> list, LocateHandler.OnLocationListener onLocationListener) {
        this.context = context;
        if (list != null) {
            this.countryList.addAll(list);
        }
        this.locateHandler.setLocationListener(onLocationListener);
    }

    private IoTSmart.Country getCountryInfo(String str, String str2) {
        if (this.countryList.isEmpty()) {
            return null;
        }
        for (IoTSmart.Country country : this.countryList) {
            if (Objects.equals(str, country.code) || Objects.equals(str, country.domainAbbreviation) || Objects.equals(str2, country.areaName)) {
                return country;
            }
        }
        return null;
    }

    public synchronized void startLocation() {
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: tools.-$$Lambda$LocateTask$B6u_u1h75GtNLIPOtO_Bvs6_BGo
            @Override // java.lang.Runnable
            public final void run() {
                LocateTask.lambda$startLocation$0(this.f$0);
            }
        });
        if (this.timer == null) {
            this.timer = new Timer();
        }
        this.timer.schedule(new TimerTask() { // from class: tools.LocateTask.1
            @Override // java.util.TimerTask, java.lang.Runnable
            public void run() {
                LocateTask.this.start = false;
                Message message = new Message();
                message.what = 5;
                LocateTask.this.locateHandler.sendMessage(message);
            }
        }, 30000L);
    }

    public static /* synthetic */ void lambda$startLocation$0(LocateTask locateTask) {
        while (locateTask.start) {
            Log.e("定位", "获取定位信息");
            Address countryCode = LocationUtil.getCountryCode(locateTask.context.getApplicationContext());
            if (countryCode != null) {
                if (countryCode.getCountryName().equals("中国")) {
                    countryCode.setCountryName(locateTask.context.getResources().getString(R.string.china_name));
                }
                IoTSmart.Country countryInfo = locateTask.getCountryInfo(countryCode.getCountryCode(), countryCode.getCountryName());
                Log.d("LocateTask", "<====" + countryCode.getCountryCode() + ", " + countryCode.getCountryName() + " =====>");
                if (countryInfo != null) {
                    Message message = new Message();
                    message.obj = countryInfo;
                    message.what = 4;
                    locateTask.locateHandler.sendMessage(message);
                    locateTask.start = false;
                    locateTask.timer.cancel();
                    locateTask.timer = null;
                } else {
                    try {
                        Thread.sleep(500L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (locateTask.start) {
                        Message message2 = new Message();
                        int i = locateTask.ellipsis_num;
                        message2.what = i % 4;
                        locateTask.ellipsis_num = i + 1;
                        locateTask.locateHandler.sendMessage(message2);
                    }
                }
            } else {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
                if (locateTask.start) {
                    Message message3 = new Message();
                    int i2 = locateTask.ellipsis_num;
                    message3.what = i2 % 4;
                    locateTask.ellipsis_num = i2 + 1;
                    locateTask.locateHandler.sendMessage(message3);
                }
            }
        }
    }

    public synchronized void stopLocation() {
        this.start = false;
        if (this.timer != null) {
            this.timer.cancel();
        }
        if (this.locateHandler != null) {
            this.locateHandler.removeCallbacksAndMessages(null);
        }
    }
}
