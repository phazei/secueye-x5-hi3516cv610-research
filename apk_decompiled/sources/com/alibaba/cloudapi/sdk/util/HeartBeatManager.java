package com.alibaba.cloudapi.sdk.util;

import android.util.Log;
import com.alibaba.cloudapi.sdk.client.WebSocketApiClient;

/* JADX INFO: loaded from: classes.dex */
public class HeartBeatManager implements Runnable {
    int heartbeatInterval;
    boolean isStop;
    WebSocketApiClient webSocketApiClient;

    public HeartBeatManager(WebSocketApiClient webSocketApiClient, int i) {
        this.heartbeatInterval = 25000;
        this.isStop = false;
        this.webSocketApiClient = webSocketApiClient;
        this.heartbeatInterval = i;
        this.isStop = false;
    }

    public void stop() {
        this.isStop = true;
    }

    @Override // java.lang.Runnable
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.heartbeatInterval);
            } catch (Exception e) {
                Log.e("SDK", "SEND HEARTBEAT FAILED", e);
            }
            if (this.isStop) {
                return;
            } else {
                this.webSocketApiClient.sendHeatbeart();
            }
        }
    }
}
