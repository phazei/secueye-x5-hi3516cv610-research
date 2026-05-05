package com.aliyun.alink.linksdk.tmp.utils;

import android.content.Context;
import android.os.Environment;
import com.aliyun.alink.linksdk.tools.ALog;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;

/* JADX INFO: loaded from: classes2.dex */
public class DiskUtil {
    private static final String TAG = "[Tmp]DiskUtil";

    public static File getDiskCacheDirWithAppend(Context context, String str) {
        return new File(getDiskCacheDir(context) + File.separator + str);
    }

    public static String getDiskCacheDir(Context context) {
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            return context.getExternalCacheDir().getPath();
        }
        return context.getCacheDir().getPath();
    }

    public static String readFully(Reader reader) throws IOException {
        try {
            StringWriter stringWriter = new StringWriter();
            char[] cArr = new char[1024];
            while (true) {
                int i = reader.read(cArr);
                if (i != -1) {
                    stringWriter.write(cArr, 0, i);
                } else {
                    return stringWriter.toString();
                }
            }
        } finally {
            reader.close();
        }
    }

    public static String readFully(InputStream inputStream) {
        ByteArrayOutputStream byteArrayOutputStream;
        byte[] bArr;
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                bArr = new byte[1024];
            } catch (Exception e) {
                ALog.e(TAG, "readFully InputStream:" + e.toString());
                try {
                    inputStream.close();
                    return null;
                } catch (Exception e2) {
                    ALog.e(TAG, "readFully close:" + e2.toString());
                    return null;
                }
            }
        } catch (Throwable th) {
            try {
                inputStream.close();
            } catch (Exception e3) {
                ALog.e(TAG, "readFully close:" + e3.toString());
            }
            throw th;
        }
        while (true) {
            int i = inputStream.read(bArr);
            if (i == -1) {
                break;
            }
            byteArrayOutputStream.write(bArr, 0, i);
            inputStream.close();
            throw th;
        }
        String string = byteArrayOutputStream.toString("UTF-8");
        try {
            inputStream.close();
        } catch (Exception e4) {
            ALog.e(TAG, "readFully close:" + e4.toString());
        }
        return string;
    }
}
