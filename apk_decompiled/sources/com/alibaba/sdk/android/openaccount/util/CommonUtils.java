package com.alibaba.sdk.android.openaccount.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Process;
import android.widget.Toast;
import com.alibaba.sdk.android.openaccount.ConfigManager;
import com.alibaba.sdk.android.openaccount.OpenAccountConstants;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.callback.FailureCallback;
import com.alibaba.sdk.android.openaccount.executor.impl.ExecutorServiceImpl;
import com.alibaba.sdk.android.openaccount.message.Message;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.model.OpenAccountSession;
import com.alibaba.sdk.android.openaccount.model.ResultCode;
import com.alibaba.sdk.android.openaccount.session.SessionManagerService;
import com.alibaba.sdk.android.openaccount.task.InitWaitTask;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import java.io.EOFException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.List;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class CommonUtils {
    private static String CURRENT_PROCESS_NAME = null;
    private static String PREFIX = "\\u";

    public static boolean sessionFail(FailureCallback failureCallback) {
        SessionManagerService sessionManagerService = (SessionManagerService) OpenAccountSDK.getService(SessionManagerService.class);
        if (sessionManagerService == null) {
            return true;
        }
        OpenAccountSession session = sessionManagerService.getSession();
        if (session != null && session.isLogin()) {
            return false;
        }
        Message messageCreateMessage = MessageUtils.createMessage(10011, new Object[0]);
        AliSDKLogger.log(OpenAccountConstants.LOG_TAG, messageCreateMessage);
        if (failureCallback != null) {
            failureCallback.onFailure(messageCreateMessage.code, messageCreateMessage.message);
        }
        return true;
    }

    public static boolean isDebuggable() {
        if (ConfigManager.getInstance().isDebugEnabled()) {
            return true;
        }
        try {
            return (OpenAccountSDK.getAndroidContext().getPackageManager().getApplicationInfo(OpenAccountSDK.getAndroidContext().getPackageName(), 16384).flags & 2) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onFailure(final FailureCallback failureCallback, final Message message) {
        if (failureCallback == null) {
            return;
        }
        ExecutorServiceImpl.INSTANCE.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.util.CommonUtils.1
            @Override // java.lang.Runnable
            public void run() {
                failureCallback.onFailure(message.code, message.message);
            }
        });
    }

    public static void onFailure(final FailureCallback failureCallback, final ResultCode resultCode) {
        if (failureCallback == null) {
            return;
        }
        ExecutorServiceImpl.INSTANCE.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.util.CommonUtils.2
            @Override // java.lang.Runnable
            public void run() {
                failureCallback.onFailure(resultCode.code, resultCode.message);
            }
        });
    }

    public static void onFailure(final FailureCallback failureCallback, final int i, final String str) {
        if (failureCallback == null) {
            return;
        }
        ExecutorServiceImpl.INSTANCE.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.util.CommonUtils.3
            @Override // java.lang.Runnable
            public void run() {
                failureCallback.onFailure(i, str);
            }
        });
    }

    public static void toastSystemException() {
        toast("ali_sdk_openaccount_dynamic_system_exception");
    }

    public static void toast(final String str) {
        ExecutorServiceImpl.INSTANCE.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.util.CommonUtils.4
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(OpenAccountSDK.getAndroidContext(), ResourceUtils.getString(str), 0).show();
            }
        });
    }

    public static void toastNetworkError() {
        ExecutorServiceImpl.INSTANCE.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.util.CommonUtils.5
            @Override // java.lang.Runnable
            public void run() {
                Toast.makeText(OpenAccountSDK.getAndroidContext(), MessageUtils.getMessageContent(MessageConstants.NETWORK_NOT_AVAILABLE, new Object[0]), 0).show();
            }
        });
    }

    public static boolean isNetworkAvailable() {
        if (OpenAccountSDK.getAndroidContext() == null) {
            return true;
        }
        return isNetworkAvailable(OpenAccountSDK.getAndroidContext());
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo[] allNetworkInfo;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
        if (connectivityManager == null || (allNetworkInfo = connectivityManager.getAllNetworkInfo()) == null) {
            return false;
        }
        for (NetworkInfo networkInfo : allNetworkInfo) {
            if (networkInfo != null && (networkInfo.getState() == NetworkInfo.State.CONNECTED || networkInfo.getState() == NetworkInfo.State.CONNECTING)) {
                return true;
            }
        }
        return false;
    }

    public static void startInitWaitTask(Context context, FailureCallback failureCallback, Runnable runnable, String str) {
        startInitWaitTask(context, failureCallback, runnable, str, false);
    }

    public static void startInitWaitTask(Context context, FailureCallback failureCallback, Runnable runnable, String str, boolean z) {
        new InitWaitTask(context, failureCallback, runnable, str, z).execute(new Void[0]);
    }

    public static boolean isEqual(Object obj, Object obj2) {
        if (obj == null) {
            return obj2 == null;
        }
        return obj.equals(obj2);
    }

    public static String getAndroidManifestMetadata(Context context, String str) {
        Object obj;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (applicationInfo.metaData != null && (obj = applicationInfo.metaData.get(str)) != null) {
                return obj.toString();
            }
            return null;
        } catch (Exception unused) {
            return null;
        }
    }

    public static String toString(Throwable th) {
        StringWriter stringWriter = new StringWriter();
        th.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    public static int isApplicationDefaultProcess() {
        String currentProcessName;
        if (OpenAccountSDK.getAndroidContext() == null || (currentProcessName = getCurrentProcessName()) == null) {
            return -1;
        }
        return currentProcessName.equals(OpenAccountSDK.getAndroidContext().getPackageName()) ? 1 : 0;
    }

    public static String getCurrentProcessName() {
        if (OpenAccountSDK.getAndroidContext() == null) {
            return null;
        }
        String str = CURRENT_PROCESS_NAME;
        if (str != null) {
            return str;
        }
        try {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) OpenAccountSDK.getAndroidContext().getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME)).getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return null;
            }
            int iMyPid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == iMyPid) {
                    CURRENT_PROCESS_NAME = runningAppProcessInfo.processName;
                    return runningAppProcessInfo.processName;
                }
            }
        } catch (Exception unused) {
        }
        return null;
    }

    public static boolean isConnectionTimeout(Throwable th) {
        if ((th instanceof SocketTimeoutException) || (th instanceof EOFException) || (th instanceof ConnectException)) {
            return true;
        }
        Throwable cause = th.getCause();
        if (cause == null) {
            return false;
        }
        return (cause instanceof SocketTimeoutException) || (cause instanceof EOFException) || (cause instanceof ConnectException);
    }

    public static int dp2px(Context context, float f) {
        return (int) (f * context.getResources().getDisplayMetrics().density);
    }

    public static int px2dp(Context context, float f) {
        return (int) ((f / context.getResources().getDisplayMetrics().density) + ((f >= 0.0f ? 1 : -1) * 0.5f));
    }
}
