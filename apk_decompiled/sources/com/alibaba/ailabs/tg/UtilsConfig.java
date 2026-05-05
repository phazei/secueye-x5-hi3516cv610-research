package com.alibaba.ailabs.tg;

import android.annotation.SuppressLint;
import android.content.Context;

/* JADX INFO: loaded from: classes.dex */
public class UtilsConfig {
    private Context mContext;

    @SuppressLint({"StaticFieldLeak"})
    private static class SingletonHolder {
        private static final UtilsConfig INSTANCE = new UtilsConfig();

        private SingletonHolder() {
        }
    }

    private UtilsConfig() {
    }

    public static UtilsConfig getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void setAppContext(Context context) {
        this.mContext = context;
    }

    public Context getAppContext() {
        return this.mContext;
    }
}
