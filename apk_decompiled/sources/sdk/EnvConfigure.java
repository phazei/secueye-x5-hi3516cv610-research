package sdk;

import android.app.Application;
import android.content.SharedPreferences;
import com.aliyun.iot.aep.sdk.framework.config.AConfigure;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import java.util.HashSet;
import java.util.Iterator;

/* JADX INFO: loaded from: classes4.dex */
public class EnvConfigure {
    public static final String KEY_APPKEY = "KEY_APPKEY";
    public static final String KEY_DEVICE_ID = "KEY_DEVICE_ID";
    public static final String KEY_IS_DEBUG = "KEY_IS_DEBUG";
    public static final String KEY_LANGUAGE = "language";
    public static final String KEY_PRODUCT_ID = "KEY_PRODUCT_ID";
    public static final String KEY_TRACE_ID = "KEY_TRACE_ID";
    private static final String SHARED_PREFERENCES_NAME = "ENV_CONFIGURE";
    private static Application app;
    private static final HashSet<Listener> listeners = new HashSet<>();

    public interface Listener {
        boolean needInvoked(String str);

        boolean needUIThread();

        void onConfigureChanged(String str, String str2, String str3);
    }

    public static void init(Application application, HashSet<String> hashSet) {
        app = application;
        initConfiguresByConfigureDB(hashSet);
    }

    public static boolean hasEnvArg(String str) {
        return AConfigure.getInstance().getConfig().containsKey(str);
    }

    public static String getEnvArg(String str) {
        return AConfigure.getInstance().getConfig(str);
    }

    public static String getEnvArg(String str, boolean z) {
        if (!z) {
            return AConfigure.getInstance().getConfig(str);
        }
        SharedPreferences sharedPreferences = app.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME, 0);
        if (sharedPreferences == null) {
            return null;
        }
        String string = sharedPreferences.contains(str) ? sharedPreferences.getString(str, null) : null;
        AConfigure.getInstance().putConfig(str, string);
        return string;
    }

    public static void putEnvArg(String str, String str2) {
        putEnvArg(str, str2, false);
    }

    public static void putEnvArg(String str, String str2, boolean z) {
        String config2;
        if (str == null || str.length() <= 0) {
            return;
        }
        synchronized (EnvConfigure.class) {
            config2 = AConfigure.getInstance().getConfig(str);
            AConfigure.getInstance().putConfig(str, str2);
        }
        boolean z2 = false;
        if (config2 != null && str2 != null) {
            z2 = !config2.equals(str2);
        } else if (config2 != null && str2 == null) {
            z2 = true;
        } else if (config2 == null && str2 != null) {
            z2 = true;
        }
        if (z2) {
            if (z) {
                saveToDB(str, str2);
            }
            HashSet<Listener> hashSet = listeners;
            if (hashSet == null || hashSet.isEmpty()) {
                return;
            }
            ThreadPool.DefaultThreadPool.getInstance().submit(new InvokeListenerTask(str, config2, str2));
        }
    }

    public static void registerListener(Listener listener) {
        synchronized (EnvConfigure.class) {
            if (!listeners.contains(listener)) {
                listeners.add(listener);
            }
        }
    }

    public static void unRegisterListener(Listener listener) {
        synchronized (EnvConfigure.class) {
            if (listeners.contains(listener)) {
                listeners.remove(listener);
            }
        }
    }

    private static void initConfiguresByConfigureDB(HashSet<String> hashSet) {
        SharedPreferences sharedPreferences;
        if (hashSet == null || hashSet.isEmpty() || (sharedPreferences = app.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME, 0)) == null) {
            return;
        }
        for (String str : hashSet) {
            if (sharedPreferences.contains(str)) {
                putEnvArg(str, sharedPreferences.getString(str, ""));
            }
        }
    }

    private static void saveToDB(String str, String str2) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editorEdit;
        if (str == null || str.isEmpty() || (sharedPreferences = app.getApplicationContext().getSharedPreferences(SHARED_PREFERENCES_NAME, 0)) == null || (editorEdit = sharedPreferences.edit()) == null) {
            return;
        }
        editorEdit.putString(str, str2);
        editorEdit.commit();
    }

    private static class InvokeListenerTask implements Runnable {
        private final String key;
        private final String newValue;
        private final String oldValue;

        public InvokeListenerTask(String str, String str2, String str3) {
            this.key = str;
            this.oldValue = str2;
            this.newValue = str3;
        }

        @Override // java.lang.Runnable
        public void run() {
            synchronized (EnvConfigure.class) {
                if (this.key != null && !this.key.isEmpty()) {
                    if (EnvConfigure.listeners != null && !EnvConfigure.listeners.isEmpty()) {
                        Iterator it = EnvConfigure.listeners.iterator();
                        while (it.hasNext()) {
                            invoke((Listener) it.next());
                        }
                    }
                }
            }
        }

        private void invoke(final Listener listener) {
            if (listener == null) {
                return;
            }
            try {
                if (listener.needInvoked(this.key)) {
                    if (listener.needUIThread()) {
                        listener.onConfigureChanged(this.key, this.oldValue, this.newValue);
                    } else {
                        ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: sdk.EnvConfigure.InvokeListenerTask.1
                            @Override // java.lang.Runnable
                            public void run() {
                                listener.onConfigureChanged(InvokeListenerTask.this.key, InvokeListenerTask.this.oldValue, InvokeListenerTask.this.newValue);
                            }
                        });
                    }
                }
            } catch (Exception unused) {
            }
        }
    }
}
