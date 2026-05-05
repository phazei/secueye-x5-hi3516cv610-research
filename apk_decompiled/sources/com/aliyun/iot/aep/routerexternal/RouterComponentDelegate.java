package com.aliyun.iot.aep.routerexternal;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.aliyun.iot.aep.component.router.DefaultUrlHandler;
import com.aliyun.iot.aep.component.router.IAsyncHandlerCallback;
import com.aliyun.iot.aep.component.router.IUrlHandler;
import com.aliyun.iot.aep.component.router.Router;
import com.aliyun.iot.aep.component.router.RouterRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes2.dex */
class RouterComponentDelegate {
    static final String TAG = "RouterComponentDelegate";
    private Map<String, String> mRouterTable = new ConcurrentHashMap();

    RouterComponentDelegate() {
    }

    int init(Context context) {
        Router router = Router.getInstance();
        router.setDefaultUrlHandler(new DefaultUrlHandler() { // from class: com.aliyun.iot.aep.routerexternal.RouterComponentDelegate.1
            @Override // com.aliyun.iot.aep.component.router.DefaultUrlHandler
            @TargetApi(5)
            public void onUrlHandle(Context context2, String str, Bundle bundle, boolean z, int i, IAsyncHandlerCallback iAsyncHandlerCallback) {
                RouterComponentDelegate.this.defaultHandle(context2, str, bundle, z, i);
            }
        });
        router.addUrlInterceptor(new Router.UrlInterceptor() { // from class: com.aliyun.iot.aep.routerexternal.RouterComponentDelegate.2
            @Override // com.aliyun.iot.aep.component.router.Router.UrlInterceptor
            public RouterRequest onIntercept(RouterRequest routerRequest) {
                return RouterComponentDelegate.this.handlerCodeToUrl(routerRequest);
            }
        });
        return 0;
    }

    void registerNativeCodeUrl(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return;
        }
        this.mRouterTable.put(str, str2);
    }

    void removeRouterCache(String str) {
        this.mRouterTable.remove(str);
    }

    void registerNativePages(List<String> list, IUrlHandler iUrlHandler) {
        if (list == null || list.isEmpty() || iUrlHandler == null) {
            return;
        }
        Router.getInstance().registerModuleUrlHandler(list, iUrlHandler);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void defaultHandle(Context context, String str, Bundle bundle, boolean z, int i) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        if (bundle != null) {
            intent.putExtras(bundle);
            if (context != null) {
                try {
                    if (bundle.containsKey("internalUri") && bundle.getBoolean("internalUri")) {
                        Log.d(TAG, "defaultHandle add package name for " + str + ", name = " + context.getPackageName());
                        intent.setPackage(context.getPackageName());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "defaultHandle excep = " + e);
                }
            }
        }
        if (z && (context instanceof Activity)) {
            ((Activity) context).startActivityForResult(intent, i);
            return;
        }
        try {
            context.startActivity(intent);
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    @TargetApi(5)
    public RouterRequest handlerCodeToUrl(RouterRequest routerRequest) {
        String str = this.mRouterTable.get(routerRequest.getUrl());
        if (TextUtils.isEmpty(str)) {
            return routerRequest;
        }
        routerRequest.setUrl(str);
        return routerRequest;
    }
}
