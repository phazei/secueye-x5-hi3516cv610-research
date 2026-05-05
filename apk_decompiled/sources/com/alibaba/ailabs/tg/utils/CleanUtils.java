package com.alibaba.ailabs.tg.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public final class CleanUtils {
    private CleanUtils() {
    }

    public static boolean cleanInternalCache(Context context) {
        return deleteFilesInDir(context.getCacheDir());
    }

    public static boolean cleanInternalFiles(Context context) {
        return deleteFilesInDir(context.getFilesDir());
    }

    public static boolean cleanInternalDbs(Context context) {
        return deleteFilesInDir(new File(context.getFilesDir().getParent(), "databases"));
    }

    public static boolean cleanInternalDbByName(Context context, String str) {
        return context.deleteDatabase(str);
    }

    public static boolean cleanInternalSp(Context context) {
        return deleteFilesInDir(new File(context.getFilesDir().getParent(), "shared_prefs"));
    }

    public static boolean cleanExternalCache(Context context) {
        return "mounted".equals(Environment.getExternalStorageState()) && deleteFilesInDir(context.getExternalCacheDir());
    }

    public static boolean cleanCustomDir(String str) {
        return deleteFilesInDir(str);
    }

    public static boolean cleanCustomDir(File file) {
        return deleteFilesInDir(file);
    }

    public static boolean deleteFilesInDir(String str) {
        return deleteFilesInDir(getFileByPath(str));
    }

    private static boolean deleteFilesInDir(File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            return false;
        }
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles != null && fileArrListFiles.length != 0) {
            for (File file2 : fileArrListFiles) {
                if (file2.isFile()) {
                    if (!file2.delete()) {
                        return false;
                    }
                } else if (file2.isDirectory() && !deleteDir(file2)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean deleteDir(File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return true;
        }
        if (!file.isDirectory()) {
            return false;
        }
        File[] fileArrListFiles = file.listFiles();
        if (fileArrListFiles != null && fileArrListFiles.length != 0) {
            for (File file2 : fileArrListFiles) {
                if (file2.isFile()) {
                    if (!file2.delete()) {
                        return false;
                    }
                } else if (file2.isDirectory() && !deleteDir(file2)) {
                    return false;
                }
            }
        }
        return file.delete();
    }

    private static File getFileByPath(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return new File(str);
    }
}
