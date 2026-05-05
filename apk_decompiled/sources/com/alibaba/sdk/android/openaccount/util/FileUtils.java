package com.alibaba.sdk.android.openaccount.util;

import java.io.File;

/* JADX INFO: loaded from: classes.dex */
public class FileUtils {
    public static void delete(File file) {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else {
                deleteDirectory(file);
            }
        }
    }

    private static void deleteDirectory(File file) {
        for (File file2 : file.listFiles()) {
            if (file2.isDirectory()) {
                deleteDirectory(file2);
            } else if (file2.isFile()) {
                file2.delete();
            }
        }
    }
}
