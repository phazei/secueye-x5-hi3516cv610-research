package tools;

import android.app.Application;
import android.content.Context;

/* JADX INFO: loaded from: classes4.dex */
public class MyApplication extends Application {
    private static Context mContext;

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context getContext() {
        return mContext;
    }
}
