package com.aliyun.iot.aep.routerexternal;

import android.content.Context;
import android.util.Log;
import com.aliyun.iot.aep.component.router.IUrlHandler;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class RouterExternal {
    Context mCtx;
    String mEnv;
    RouterComponentDelegate mRouterComponentDelegate;

    private static class SingletonHolder {
        private static final RouterExternal INSTANCE = new RouterExternal();

        private SingletonHolder() {
        }
    }

    private RouterExternal() {
        this.mRouterComponentDelegate = new RouterComponentDelegate();
    }

    public static final RouterExternal getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init(Context context, String str) {
        this.mEnv = str;
        this.mRouterComponentDelegate.init(context);
        this.mCtx = context.getApplicationContext();
        Log.d("router_init", "--------------");
    }

    String getEnv() {
        return this.mEnv;
    }

    public void registerNativeCodeUrl(String str, String str2) {
        this.mRouterComponentDelegate.registerNativeCodeUrl(str, str2);
    }

    public void registerNativePages(List<String> list, IUrlHandler iUrlHandler) {
        this.mRouterComponentDelegate.registerNativePages(list, iUrlHandler);
    }

    public void removeRouterCache(String str) {
        this.mRouterComponentDelegate.removeRouterCache(str);
    }
}
