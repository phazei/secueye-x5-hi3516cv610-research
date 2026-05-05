package anetwork.channel.http;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.SessionCenter;
import anet.channel.entity.ENV;
import anet.channel.util.ALog;
import anet.channel.util.Utils;
import anetwork.channel.config.NetworkConfigCenter;
import anetwork.channel.cookie.CookieManager;
import anetwork.channel.monitor.Monitor;
import com.taobao.accs.common.Constants;
import java.io.Serializable;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class NetworkSdkSetting implements Serializable {
    private static final String TAG = "anet.NetworkSdkSetting";
    private static Context context;
    public static ENV CURRENT_ENV = ENV.ONLINE;
    private static AtomicBoolean isInit = new AtomicBoolean(false);
    private static HashMap<String, Object> initParams = null;

    public static void init(Context context2) {
        if (context2 == null) {
            return;
        }
        try {
            if (isInit.compareAndSet(false, true)) {
                ALog.e(TAG, "NetworkSdkSetting init", null, new Object[0]);
                context = context2;
                GlobalAppRuntimeInfo.setInitTime(System.currentTimeMillis());
                GlobalAppRuntimeInfo.setContext(context2);
                NetworkConfigCenter.init();
                initTaobaoAdapter();
                Monitor.init();
                if (!AwcnConfig.isTbNextLaunch()) {
                    CookieManager.setup(context2);
                }
                SessionCenter.init(context2);
            }
        } catch (Throwable th) {
            ALog.e(TAG, "Network SDK initial failed!", null, th, new Object[0]);
        }
    }

    public static void init(Application application, HashMap<String, Object> map) {
        try {
            GlobalAppRuntimeInfo.setTtid((String) map.get(Constants.KEY_TTID));
            GlobalAppRuntimeInfo.setUtdid((String) map.get("deviceId"));
            String str = (String) map.get("process");
            if (!TextUtils.isEmpty(str)) {
                GlobalAppRuntimeInfo.setCurrentProcess(str);
            }
            initParams = new HashMap<>(map);
            init(application.getApplicationContext());
            initParams = null;
        } catch (Exception e) {
            ALog.e(TAG, "Network SDK initial failed!", null, e, new Object[0]);
        }
    }

    public static void setTtid(String str) {
        GlobalAppRuntimeInfo.setTtid(str);
    }

    public static Context getContext() {
        return context;
    }

    private static void initTaobaoAdapter() {
        try {
            Utils.invokeStaticMethodThrowException("anet.channel.TaobaoNetworkAdapter", "init", new Class[]{Context.class, HashMap.class}, context, initParams);
            ALog.i(TAG, "init taobao adapter success", null, new Object[0]);
        } catch (Exception e) {
            ALog.i(TAG, "initTaobaoAdapter failed. maybe not taobao app", null, e);
        }
    }
}
