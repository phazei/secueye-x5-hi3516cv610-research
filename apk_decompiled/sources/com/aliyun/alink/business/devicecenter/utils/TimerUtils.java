package com.aliyun.alink.business.devicecenter.utils;

import android.os.Handler;
import android.os.Message;
import com.aliyun.alink.business.devicecenter.log.ALog;

/* JADX INFO: loaded from: classes2.dex */
public class TimerUtils {
    public static final int MSG_DIAGNOSE = 1056769;
    public static final int MSG_GET_NETWORK_ENV_TIMEOUT = 1060866;
    public static final int MSG_GET_TOKEN_TIMEOUT = 1054982;
    public static final int MSG_PROVISION_TIMEOUT = 1054981;
    public static final int MSG_SCAN_BLE_TIMEOUT = 1060865;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public InternalHandler f3784a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f3785b;

    public interface ITimerCallback {
        void onTimeout();
    }

    private static final class InternalHandler extends Handler {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public ITimerCallback f3786a;

        public InternalHandler(ITimerCallback iTimerCallback) {
            super(HandlerThreadUtils.getInstance().getLooper());
            this.f3786a = null;
            this.f3786a = iTimerCallback;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message == null) {
            }
            switch (message.what) {
                case TimerUtils.MSG_PROVISION_TIMEOUT /* 1054981 */:
                case TimerUtils.MSG_GET_TOKEN_TIMEOUT /* 1054982 */:
                case TimerUtils.MSG_DIAGNOSE /* 1056769 */:
                case TimerUtils.MSG_SCAN_BLE_TIMEOUT /* 1060865 */:
                case TimerUtils.MSG_GET_NETWORK_ENV_TIMEOUT /* 1060866 */:
                    ALog.d("TimerUtils", "onTimeout timerCallback=" + this.f3786a + ", what=" + message.what);
                    try {
                        if (this.f3786a != null) {
                            this.f3786a.onTimeout();
                        }
                        this.f3786a = null;
                    } catch (Exception e) {
                        ALog.w("TimerUtils", "onTimeout exception " + e);
                        this.f3786a = null;
                        return;
                    }
                    break;
            }
        }
    }

    public TimerUtils(int i) {
        this.f3785b = 60000;
        this.f3785b = i;
    }

    public int getTimeout() {
        return this.f3785b;
    }

    public boolean isStart(int i) {
        InternalHandler internalHandler = this.f3784a;
        if (internalHandler != null) {
            return internalHandler.hasMessages(i);
        }
        return false;
    }

    public void setCallback(ITimerCallback iTimerCallback) {
        this.f3784a = new InternalHandler(iTimerCallback);
        ALog.d("TimerUtils", "TimerUtils internalHandler=" + this.f3784a + ", looper=" + this.f3784a.getLooper());
    }

    public void start(int i) {
        ALog.d("TimerUtils", "startTimer message=" + i + ", this=" + this + ", timeout=" + this.f3785b);
        InternalHandler internalHandler = this.f3784a;
        if (internalHandler != null) {
            internalHandler.removeMessages(i);
            this.f3784a.sendEmptyMessageDelayed(i, this.f3785b);
        }
    }

    public void stop(int i) {
        ALog.d("TimerUtils", "stopTimer message=" + i + ", this=" + this + ", timeout=" + this.f3785b);
        InternalHandler internalHandler = this.f3784a;
        if (internalHandler != null) {
            internalHandler.removeMessages(i);
        }
    }
}
