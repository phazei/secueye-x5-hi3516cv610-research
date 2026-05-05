package com.aliyun.iot.aep.sdk.log;

import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;

/* JADX INFO: loaded from: classes2.dex */
public class UpLoadAgent {
    UpLoadTimerTask mUpLoadTimerTask;

    class UpLoadTimerTask extends TimerTask {
        UpLoadTimerTask() {
        }

        @Override // java.util.TimerTask, java.lang.Runnable
        public void run() {
            UpLoadAgent.this.startAgent("");
        }
    }

    public static void init(String str, String str2, String str3, String str4) {
    }

    public static void setRule(String str) {
    }

    private void worker() {
        new Handler().postDelayed(new Runnable() { // from class: com.aliyun.iot.aep.sdk.log.UpLoadAgent.1
            @Override // java.lang.Runnable
            public void run() {
            }
        }, 60000L);
    }

    public void closeAgent() {
    }

    public void startAgent(String str) {
    }

    public void startUpLoadTimerTask() {
        UpLoadTimerTask upLoadTimerTask = this.mUpLoadTimerTask;
        if (upLoadTimerTask != null) {
            upLoadTimerTask.cancel();
        }
        this.mUpLoadTimerTask = new UpLoadTimerTask();
        new Timer().schedule(this.mUpLoadTimerTask, 60000L, 60000L);
    }
}
