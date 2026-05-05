package com.aliyun.iot.aep.component.router;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes2.dex */
public class Router {
    static final String TAG = "Router";
    DefaultUrlHandler mDefaultUrlHandler;
    Map<String, IUrlHandler> mModuleUrlHandlers;
    List<String> mRegexUrls;
    List<UrlInterceptor> mUrlInterceptors;

    public interface UrlInterceptor {
        RouterRequest onIntercept(RouterRequest routerRequest);
    }

    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final Router f4500a = new Router();
    }

    private Router() {
        this.mModuleUrlHandlers = new ConcurrentHashMap();
        this.mUrlInterceptors = new ArrayList();
        this.mDefaultUrlHandler = new DefaultUrlHandler();
        this.mRegexUrls = new ArrayList();
    }

    private String convertUrl(String str) {
        try {
            return str.split("\\?")[0];
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final Router getInstance() {
        return a.f4500a;
    }

    public synchronized void addUrlInterceptor(UrlInterceptor urlInterceptor) {
        this.mUrlInterceptors.add(urlInterceptor);
    }

    public IUrlHandler getModuleUrlHandler(String str) {
        IUrlHandler iUrlHandler = this.mModuleUrlHandlers.get(convertUrl(str));
        if (iUrlHandler != null) {
            return iUrlHandler;
        }
        for (String str2 : this.mRegexUrls) {
            if (Pattern.matches(str2, str)) {
                return this.mModuleUrlHandlers.get(str2);
            }
        }
        return iUrlHandler;
    }

    public synchronized void registerModuleUrlHandler(String str, IUrlHandler iUrlHandler) {
        String strConvertUrl = convertUrl(str);
        if (!TextUtils.isEmpty(strConvertUrl)) {
            this.mModuleUrlHandlers.put(strConvertUrl, iUrlHandler);
        }
    }

    public synchronized void registerModuleUrlHandler(List<String> list, IUrlHandler iUrlHandler) {
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String strConvertUrl = convertUrl(it.next());
            if (!TextUtils.isEmpty(strConvertUrl)) {
                this.mModuleUrlHandlers.put(strConvertUrl, iUrlHandler);
            }
        }
    }

    public synchronized void registerRegexUrlHandler(String str, IUrlHandler iUrlHandler) {
        if (!TextUtils.isEmpty(str)) {
            this.mRegexUrls.add(str);
            this.mModuleUrlHandlers.put(str, iUrlHandler);
        }
    }

    public synchronized void removeUrlInterceptor(UrlInterceptor urlInterceptor) {
        this.mUrlInterceptors.remove(urlInterceptor);
    }

    public void setDefaultUrlHandler(DefaultUrlHandler defaultUrlHandler) {
        this.mDefaultUrlHandler = defaultUrlHandler;
    }

    public boolean toUrl(Context context, String str) {
        return toUrl(context, str, null);
    }

    public boolean toUrl(Context context, String str, Bundle bundle) {
        return toUrl(context, str, bundle, -1);
    }

    public boolean toUrl(Context context, String str, Bundle bundle, int i) {
        return toUrl(context, str, bundle, i, null);
    }

    public boolean toUrl(Context context, String str, Bundle bundle, int i, IAsyncHandlerCallback iAsyncHandlerCallback) {
        IUrlHandler iUrlHandler;
        RouterRequest routerRequest = new RouterRequest(str, bundle);
        if (this.mUrlInterceptors.size() != 0) {
            Iterator<UrlInterceptor> it = this.mUrlInterceptors.iterator();
            while (it.hasNext()) {
                routerRequest = it.next().onIntercept(routerRequest);
            }
        }
        String url = routerRequest.getUrl();
        Bundle bundle2 = routerRequest.getBundle();
        try {
            String strConvertUrl = convertUrl(url);
            IUrlHandler iUrlHandler2 = this.mModuleUrlHandlers.get(strConvertUrl);
            if (iUrlHandler2 == null) {
                for (String str2 : this.mRegexUrls) {
                    if (Pattern.matches(str2, strConvertUrl)) {
                        iUrlHandler = this.mModuleUrlHandlers.get(str2);
                        break;
                    }
                }
                iUrlHandler = iUrlHandler2;
            } else {
                iUrlHandler = iUrlHandler2;
            }
            if (iUrlHandler != null) {
                iUrlHandler.onUrlHandle(context, url, bundle2, -1 != i, i);
                if (iAsyncHandlerCallback != null) {
                    iAsyncHandlerCallback.asyncHandle(true);
                }
            } else {
                this.mDefaultUrlHandler.onUrlHandle(context, url, bundle2, -1 != i, i, iAsyncHandlerCallback);
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Url err");
            if (url != null) {
                Log.e(TAG, "url => " + url);
            }
            if (bundle2 != null) {
                Log.e(TAG, "bundle-> " + bundle2.toString());
            }
            if (context != null) {
                Log.e(TAG, context.getClass().getSimpleName());
            }
            e.printStackTrace();
            if (iAsyncHandlerCallback != null) {
                iAsyncHandlerCallback.asyncHandle(false);
            }
            return false;
        }
    }

    public boolean toUrlForResult(Activity activity2, String str, int i) {
        return toUrl(activity2, str, null, i);
    }

    public boolean toUrlForResult(Activity activity2, String str, int i, Bundle bundle) {
        return toUrl(activity2, str, bundle, i);
    }
}
