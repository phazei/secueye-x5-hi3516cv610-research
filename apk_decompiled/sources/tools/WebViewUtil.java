package tools;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Process;
import android.text.TextUtils;
import android.webkit.WebView;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;
import java.util.List;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes4.dex */
public class WebViewUtil {
    public static void handleWebViewDir(Context context) {
        if (Build.VERSION.SDK_INT < 28) {
            return;
        }
        try {
            String str = "";
            String curProcessName = getCurProcessName(context);
            if (!TextUtils.equals(context.getPackageName(), curProcessName)) {
                if (TextUtils.isEmpty(curProcessName)) {
                    curProcessName = context.getPackageName();
                }
                WebView.setDataDirectorySuffix(curProcessName);
                str = OpenAccountUIConstants.UNDER_LINE + curProcessName;
            }
            tryLockOrRecreateFile(context, str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @TargetApi(28)
    private static void tryLockOrRecreateFile(Context context, String str) {
        File file = new File(context.getDataDir().getAbsolutePath() + "/app_webview" + str + "/webview_data.lock");
        if (file.exists()) {
            try {
                FileLock fileLockTryLock = new RandomAccessFile(file, "rw").getChannel().tryLock();
                if (fileLockTryLock != null) {
                    fileLockTryLock.close();
                } else {
                    createFile(file, file.delete());
                }
            } catch (Exception e) {
                e.printStackTrace();
                createFile(file, file.exists() ? file.delete() : false);
            }
        }
    }

    private static void createFile(File file, boolean z) {
        if (z) {
            try {
                if (file.exists()) {
                    return;
                }
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String getCurProcessName(Context context) {
        int iMyPid = Process.myPid();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((android.app.ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME)).getRunningAppProcesses();
        if (runningAppProcesses == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (runningAppProcessInfo != null && runningAppProcessInfo.pid == iMyPid) {
                return runningAppProcessInfo.processName;
            }
        }
        return null;
    }
}
