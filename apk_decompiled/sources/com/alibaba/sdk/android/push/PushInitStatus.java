package com.alibaba.sdk.android.push;

/* JADX INFO: loaded from: classes.dex */
public class PushInitStatus {
    public boolean isInitPush;
    public IPushInitListener listener;

    public interface IPushInitListener {
        void onInitPush(boolean z);
    }

    private PushInitStatus() {
        this.isInitPush = false;
    }

    private static class SingleHolder {
        public static final PushInitStatus INSTANCE = new PushInitStatus();

        private SingleHolder() {
        }
    }

    public static PushInitStatus getInstance() {
        return SingleHolder.INSTANCE;
    }
}
