package com.alibaba.ailabs.tg.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.content.FileProvider;
import com.hjq.permissions.Permission;
import io.netty.handler.codec.http.multipart.HttpPostBodyUtil;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class IntentUtils {
    public static String getQueryParameter(Intent intent, String str) {
        Uri data;
        if (intent == null || TextUtils.isEmpty(str) || (data = intent.getData()) == null || !data.isHierarchical()) {
            return null;
        }
        String queryParameter = data.getQueryParameter(str);
        LogUtils.v("name: " + str + ", param: " + queryParameter);
        return queryParameter;
    }

    public static Intent getInstallAppIntent(@NonNull Context context, String str, String str2) {
        return getInstallAppIntent(context, FileUtils.getFileByPath(str), str2);
    }

    public static Intent getInstallAppIntent(@NonNull Context context, File file, String str) {
        return getInstallAppIntent(context, file, str, false);
    }

    public static Intent getInstallAppIntent(@NonNull Context context, File file, String str, boolean z) {
        Uri uriForFile;
        if (file == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        if (Build.VERSION.SDK_INT < 24) {
            uriForFile = Uri.fromFile(file);
        } else {
            intent.setFlags(1);
            uriForFile = FileProvider.getUriForFile(context, str, file);
        }
        intent.setDataAndType(uriForFile, "application/vnd.android.package-archive");
        return getIntent(intent, z);
    }

    public static Intent getUninstallAppIntent(String str) {
        return getUninstallAppIntent(str, false);
    }

    public static Intent getUninstallAppIntent(String str, boolean z) {
        Intent intent = new Intent("android.intent.action.DELETE");
        intent.setData(Uri.parse("package:" + str));
        return getIntent(intent, z);
    }

    public static Intent getLaunchAppIntent(@NonNull Context context, String str) {
        return getLaunchAppIntent(context, str, false);
    }

    public static Intent getLaunchAppIntent(@NonNull Context context, String str, boolean z) {
        Intent launchIntentForPackage = context.getPackageManager().getLaunchIntentForPackage(str);
        if (launchIntentForPackage == null) {
            return null;
        }
        return getIntent(launchIntentForPackage, z);
    }

    public static Intent getLaunchAppDetailsSettingsIntent(String str) {
        return getLaunchAppDetailsSettingsIntent(str, false);
    }

    public static Intent getLaunchAppDetailsSettingsIntent(String str, boolean z) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + str));
        return getIntent(intent, z);
    }

    public static Intent getShareTextIntent(String str) {
        return getShareTextIntent(str, false);
    }

    public static Intent getShareTextIntent(String str, boolean z) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType(HttpPostBodyUtil.DEFAULT_TEXT_CONTENT_TYPE);
        intent.putExtra("android.intent.extra.TEXT", str);
        return getIntent(intent, z);
    }

    public static Intent getShareImageIntent(String str, String str2) {
        return getShareImageIntent(str, str2, false);
    }

    public static Intent getShareImageIntent(String str, String str2, boolean z) {
        if (str2 == null || str2.length() == 0) {
            return null;
        }
        return getShareImageIntent(str, new File(str2), z);
    }

    public static Intent getShareImageIntent(String str, File file) {
        return getShareImageIntent(str, file, false);
    }

    public static Intent getShareImageIntent(String str, File file, boolean z) {
        if (file == null || !file.isFile()) {
            return getShareImageIntent(str, Uri.fromFile(file), z);
        }
        return null;
    }

    public static Intent getShareImageIntent(String str, Uri uri) {
        return getShareImageIntent(str, uri, false);
    }

    public static Intent getShareImageIntent(String str, Uri uri, boolean z) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.putExtra("android.intent.extra.TEXT", str);
        intent.putExtra("android.intent.extra.STREAM", uri);
        intent.setType("image/*");
        return getIntent(intent, z);
    }

    public static Intent getComponentIntent(String str, String str2) {
        return getComponentIntent(str, str2, null, false);
    }

    public static Intent getComponentIntent(String str, String str2, boolean z) {
        return getComponentIntent(str, str2, null, z);
    }

    public static Intent getComponentIntent(String str, String str2, Bundle bundle) {
        return getComponentIntent(str, str2, bundle, false);
    }

    public static Intent getComponentIntent(String str, String str2, Bundle bundle, boolean z) {
        Intent intent = new Intent("android.intent.action.VIEW");
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        intent.setComponent(new ComponentName(str, str2));
        return getIntent(intent, z);
    }

    public static Intent getShutdownIntent() {
        return getShutdownIntent(false);
    }

    public static Intent getShutdownIntent(boolean z) {
        Intent intent = new Intent("android.intent.action.ACTION_REQUEST_SHUTDOWN");
        intent.putExtra("android.intent.extra.KEY_CONFIRM", false);
        return getIntent(intent, z);
    }

    public static Intent getDialIntent(String str) {
        return getDialIntent(str, false);
    }

    public static Intent getDialIntent(String str, boolean z) {
        return getIntent(new Intent("android.intent.action.DIAL", Uri.parse("tel:" + str)), z);
    }

    @RequiresPermission(Permission.CALL_PHONE)
    public static Intent getCallIntent(String str) {
        return getCallIntent(str, false);
    }

    @RequiresPermission(Permission.CALL_PHONE)
    public static Intent getCallIntent(String str, boolean z) {
        return getIntent(new Intent("android.intent.action.CALL", Uri.parse("tel:" + str)), z);
    }

    public static Intent getSendSmsIntent(String str, String str2) {
        return getSendSmsIntent(str, str2, false);
    }

    public static Intent getSendSmsIntent(String str, String str2, boolean z) {
        Intent intent = new Intent("android.intent.action.SENDTO", Uri.parse("smsto:" + str));
        intent.putExtra("sms_body", str2);
        return getIntent(intent, z);
    }

    public static Intent getCaptureIntent(Uri uri) {
        return getCaptureIntent(uri, false);
    }

    public static Intent getCaptureIntent(Uri uri, boolean z) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("output", uri);
        intent.addFlags(1);
        return getIntent(intent, z);
    }

    private static Intent getIntent(Intent intent, boolean z) {
        return z ? intent.addFlags(268435456) : intent;
    }

    @NonNull
    public static Intent getSettingsIntent() {
        return new Intent("android.settings.SETTINGS");
    }
}
