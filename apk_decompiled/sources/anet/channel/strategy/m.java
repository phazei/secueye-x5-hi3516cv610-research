package anet.channel.strategy;

import android.content.Context;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.statist.StrategyStatObject;
import anet.channel.util.ALog;
import anet.channel.util.SerializeHelper;
import java.io.File;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class m {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static File f1918a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static volatile boolean f1919b = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Comparator<File> f1920c = new n();

    m() {
    }

    public static void a(Context context) {
        if (context != null) {
            try {
                f1918a = new File(context.getFilesDir(), "awcn_strategy");
                if (!a(f1918a)) {
                    ALog.e("awcn.StrategySerializeHelper", "create directory failed!!!", null, "dir", f1918a.getAbsolutePath());
                }
                if (!GlobalAppRuntimeInfo.isTargetProcess()) {
                    String currentProcess = GlobalAppRuntimeInfo.getCurrentProcess();
                    f1918a = new File(f1918a, currentProcess.substring(currentProcess.indexOf(58) + 1));
                    if (!a(f1918a)) {
                        ALog.e("awcn.StrategySerializeHelper", "create directory failed!!!", null, "dir", f1918a.getAbsolutePath());
                    }
                }
                ALog.i("awcn.StrategySerializeHelper", "StrateyFolder", null, "path", f1918a.getAbsolutePath());
                if (f1919b) {
                    a();
                    f1919b = false;
                } else {
                    c();
                }
            } catch (Throwable th) {
                ALog.e("awcn.StrategySerializeHelper", "StrategySerializeHelper initialize failed!!!", null, th, new Object[0]);
            }
        }
    }

    private static boolean a(File file) {
        if (file == null || file.exists()) {
            return true;
        }
        return file.mkdir();
    }

    public static File a(String str) {
        a(f1918a);
        return new File(f1918a, str);
    }

    static synchronized void a() {
        ALog.i("awcn.StrategySerializeHelper", "clear start.", null, new Object[0]);
        if (f1918a == null) {
            ALog.w("awcn.StrategySerializeHelper", "folder path not initialized, wait to clear", null, new Object[0]);
            f1919b = true;
            return;
        }
        File[] fileArrListFiles = f1918a.listFiles();
        if (fileArrListFiles == null) {
            return;
        }
        for (File file : fileArrListFiles) {
            if (file.isFile()) {
                file.delete();
            }
        }
        ALog.i("awcn.StrategySerializeHelper", "clear end.", null, new Object[0]);
    }

    static synchronized File[] b() {
        if (f1918a == null) {
            return null;
        }
        File[] fileArrListFiles = f1918a.listFiles();
        if (fileArrListFiles != null) {
            Arrays.sort(fileArrListFiles, f1920c);
        }
        return fileArrListFiles;
    }

    static synchronized void c() {
        File[] fileArrB = b();
        if (fileArrB == null) {
            return;
        }
        int i = 0;
        for (File file : fileArrB) {
            if (!file.isDirectory()) {
                if (System.currentTimeMillis() - file.lastModified() > 172800000) {
                    file.delete();
                } else if (file.getName().startsWith("WIFI")) {
                    int i2 = i + 1;
                    if (i > 10) {
                        file.delete();
                    }
                    i = i2;
                }
            }
        }
    }

    static synchronized void a(Serializable serializable, String str, StrategyStatObject strategyStatObject) {
        SerializeHelper.persist(serializable, a(str), strategyStatObject);
    }

    static synchronized <T> T a(String str, StrategyStatObject strategyStatObject) {
        return (T) SerializeHelper.restore(a(str), strategyStatObject);
    }
}
