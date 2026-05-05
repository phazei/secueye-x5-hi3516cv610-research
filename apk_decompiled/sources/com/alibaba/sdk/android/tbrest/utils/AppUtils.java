package com.alibaba.sdk.android.tbrest.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.os.Debug;
import android.os.Environment;
import android.os.StatFs;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.sdk.android.tool.ProcessUtils;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: loaded from: classes.dex */
public class AppUtils {

    public interface ReaderListener {
        boolean onReadLine(String str);
    }

    public static String getMyProcessNameByAppProcessInfo(Context context) {
        if (context == null) {
            return null;
        }
        try {
            return ProcessUtils.getProcessName(context);
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getGMT8Time(long j) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
            return simpleDateFormat.format(new Date(j));
        } catch (Exception e) {
            LogUtil.e("getGMT8Time", e);
            return "";
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                LogUtil.e("close.", e);
            }
        }
    }

    public static String getMyProcessNameByCmdline() {
        try {
            return readLine("/proc/self/cmdline").trim();
        } catch (Exception e) {
            LogUtil.e("get my process name by cmd line failure .", e);
            return null;
        }
    }

    public static String getMyStatus() {
        return readFully(new File("/proc/self/status")).trim();
    }

    public static String getMeminfo() {
        return readFully(new File("/proc/meminfo")).trim();
    }

    public static String dumpThread(Thread thread) {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(String.format("Thread Name: '%s'\n", thread.getName()));
            sb.append(String.format("\"%s\" prio=%d tid=%d %s\n", thread.getName(), Integer.valueOf(thread.getPriority()), Long.valueOf(thread.getId()), thread.getState()));
            for (StackTraceElement stackTraceElement : thread.getStackTrace()) {
                sb.append(String.format("\tat %s\n", stackTraceElement.toString()));
            }
        } catch (Exception e) {
            LogUtil.e("dumpThread", e);
        }
        return sb.toString();
    }

    public static String dumpMeminfo(Context context) {
        String str;
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(AgooConstants.OPEN_ACTIIVTY_NAME);
            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            Integer numValueOf = null;
            if (activityManager != null) {
                activityManager.getMemoryInfo(memoryInfo);
                numValueOf = Integer.valueOf((int) (memoryInfo.threshold >> 10));
            }
            StringBuilder sb = new StringBuilder();
            sb.append("JavaTotal:");
            sb.append(Runtime.getRuntime().totalMemory());
            sb.append(" JavaFree:");
            sb.append(Runtime.getRuntime().freeMemory());
            sb.append(" NativeHeap:");
            sb.append(Debug.getNativeHeapSize());
            sb.append(" NativeAllocated:");
            sb.append(Debug.getNativeHeapAllocatedSize());
            sb.append(" NativeFree:");
            sb.append(Debug.getNativeHeapFreeSize());
            sb.append(" threshold:");
            if (numValueOf != null) {
                str = numValueOf + " KB";
            } else {
                str = "not valid";
            }
            sb.append(str);
            return sb.toString();
        } catch (Exception e) {
            LogUtil.e("dumpMeminfo", e);
            return "";
        }
    }

    private static long[] getSizes(String str) {
        long blockSizeLong;
        long blockCountLong;
        long freeBlocksLong;
        long availableBlocksLong;
        long[] jArr = {-1, -1, -1};
        try {
            StatFs statFs = new StatFs(str);
            if (Build.VERSION.SDK_INT < 18) {
                blockSizeLong = statFs.getBlockSize();
                blockCountLong = statFs.getBlockCount();
                freeBlocksLong = statFs.getFreeBlocks();
                availableBlocksLong = statFs.getAvailableBlocks();
            } else {
                blockSizeLong = statFs.getBlockSizeLong();
                blockCountLong = statFs.getBlockCountLong();
                freeBlocksLong = statFs.getFreeBlocksLong();
                availableBlocksLong = statFs.getAvailableBlocksLong();
            }
            jArr[0] = blockCountLong * blockSizeLong;
            jArr[1] = freeBlocksLong * blockSizeLong;
            jArr[2] = blockSizeLong * availableBlocksLong;
        } catch (Exception e) {
            LogUtil.e("getSizes", e);
        }
        return jArr;
    }

    public static String dumpStorage(Context context) {
        boolean zEquals;
        boolean z;
        StringBuilder sb = new StringBuilder();
        try {
            zEquals = "mounted".equals(Environment.getExternalStorageState());
        } catch (Exception e) {
            LogUtil.w("hasSDCard", e);
            zEquals = false;
        }
        try {
            z = (context.getApplicationInfo().flags & 262144) != 0;
        } catch (Exception e2) {
            LogUtil.w("installSDCard", e2);
            z = false;
        }
        sb.append("hasSDCard: ");
        sb.append(zEquals);
        sb.append(SdkConstant.CLOUDAPI_LF);
        sb.append("installSDCard: ");
        sb.append(z);
        sb.append(SdkConstant.CLOUDAPI_LF);
        try {
            File rootDirectory = Environment.getRootDirectory();
            long[] sizes = getSizes(rootDirectory.getAbsolutePath());
            sb.append("RootDirectory: ");
            sb.append(rootDirectory.getAbsolutePath());
            sb.append(" ");
            sb.append(String.format("TotalSize: %s FreeSize: %s AvailableSize: %s \n", Long.valueOf(sizes[0]), Long.valueOf(sizes[1]), Long.valueOf(sizes[2])));
            File dataDirectory = Environment.getDataDirectory();
            if (dataDirectory != null) {
                long[] sizes2 = getSizes(dataDirectory.getAbsolutePath());
                sb.append("DataDirectory: ");
                sb.append(dataDirectory.getAbsolutePath());
                sb.append(" ");
                sb.append(String.format("TotalSize: %s FreeSize: %s AvailableSize: %s \n", Long.valueOf(sizes2[0]), Long.valueOf(sizes2[1]), Long.valueOf(sizes2[2])));
            }
            File externalStorageDirectory = Environment.getExternalStorageDirectory();
            if (dataDirectory != null) {
                sb.append("ExternalStorageDirectory: ");
                sb.append(externalStorageDirectory.getAbsolutePath());
                sb.append(" ");
                long[] sizes3 = getSizes(externalStorageDirectory.getAbsolutePath());
                sb.append(String.format("TotalSize: %s FreeSize: %s AvailableSize: %s \n", Long.valueOf(sizes3[0]), Long.valueOf(sizes3[1]), Long.valueOf(sizes3[2])));
            }
            File downloadCacheDirectory = Environment.getDownloadCacheDirectory();
            if (downloadCacheDirectory != null) {
                sb.append("DownloadCacheDirectory: ");
                sb.append(downloadCacheDirectory.getAbsolutePath());
                sb.append(" ");
                long[] sizes4 = getSizes(downloadCacheDirectory.getAbsolutePath());
                sb.append(String.format("TotalSize: %s FreeSize: %s AvailableSize: %s \n", Long.valueOf(sizes4[0]), Long.valueOf(sizes4[1]), Long.valueOf(sizes4[2])));
            }
        } catch (Exception e3) {
            LogUtil.e("getSizes", e3);
        }
        return sb.toString();
    }

    public static Boolean isBackgroundRunning(Context context) {
        try {
            if (Integer.parseInt(readLine("/proc/self/oom_adj").trim()) == 0) {
                return false;
            }
            return true;
        } catch (Exception unused) {
            return false;
        }
    }

    public static boolean writeFile(File file, String str) {
        return writeFile(file, str, false);
    }

    public static boolean writeFile(File file, String str, boolean z) throws Throwable {
        FileWriter fileWriter;
        FileWriter fileWriter2 = null;
        try {
            try {
                fileWriter = new FileWriter(file, z);
            } catch (IOException e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            fileWriter.write(str);
            fileWriter.flush();
            closeQuietly(fileWriter);
            return true;
        } catch (IOException e2) {
            e = e2;
            fileWriter2 = fileWriter;
            LogUtil.e("writeFile", e);
            closeQuietly(fileWriter2);
            return false;
        } catch (Throwable th2) {
            th = th2;
            fileWriter2 = fileWriter;
            closeQuietly(fileWriter2);
            throw th;
        }
    }

    public static String readLine(String str) {
        return readLine(new File(str));
    }

    public static String readLine(File file) throws Throwable {
        List<String> lines = readLines(file, 1);
        return !lines.isEmpty() ? lines.get(0) : "";
    }

    public static List<String> readLines(File file, int i) throws Throwable {
        ArrayList arrayList = new ArrayList();
        BufferedReader bufferedReader = null;
        try {
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                int i2 = 0;
                while (true) {
                    try {
                        String line = bufferedReader2.readLine();
                        if (line == null) {
                            break;
                        }
                        i2++;
                        arrayList.add(line);
                        if (i > 0 && i2 >= i) {
                            break;
                        }
                    } catch (IOException e) {
                        e = e;
                        bufferedReader = bufferedReader2;
                        LogUtil.e("readLine", e);
                        closeQuietly(bufferedReader);
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader = bufferedReader2;
                        closeQuietly(bufferedReader);
                        throw th;
                    }
                }
                closeQuietly(bufferedReader2);
            } catch (IOException e2) {
                e = e2;
            }
            return arrayList;
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String readLineAndDel(File file) throws Throwable {
        try {
            String line = readLine(file);
            file.delete();
            return line;
        } catch (Exception e) {
            LogUtil.e("readLineAndDel", e);
            return null;
        }
    }

    public static void readLine(File file, ReaderListener readerListener) throws Throwable {
        BufferedReader bufferedReader;
        String line;
        BufferedReader bufferedReader2 = null;
        try {
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        }
        do {
            try {
                line = bufferedReader.readLine();
            } catch (IOException e2) {
                e = e2;
                bufferedReader2 = bufferedReader;
                LogUtil.e("readLine", e);
                closeQuietly(bufferedReader2);
            } catch (Throwable th2) {
                th = th2;
                bufferedReader2 = bufferedReader;
                closeQuietly(bufferedReader2);
                throw th;
            }
            if (line == null) {
                closeQuietly(bufferedReader);
                return;
            }
        } while (!readerListener.onReadLine(line));
        closeQuietly(bufferedReader);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v0 */
    /* JADX WARN: Type inference failed for: r1v1 */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v2, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v6 */
    public static String readFully(File file) throws Throwable {
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;
        StringBuilder sb = new StringBuilder();
        ?? r1 = 0;
        inputStreamReader = null;
        InputStreamReader inputStreamReader2 = null;
        r1 = 0;
        try {
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    inputStreamReader = new InputStreamReader(fileInputStream);
                } catch (Exception e) {
                    e = e;
                }
            } catch (Throwable th) {
                th = th;
            }
            try {
                char[] cArr = new char[4096];
                while (true) {
                    int i = inputStreamReader.read(cArr);
                    if (-1 == i) {
                        break;
                    }
                    sb.append(cArr, 0, i);
                }
                closeQuietly(inputStreamReader);
                r1 = cArr;
            } catch (Exception e2) {
                inputStreamReader2 = inputStreamReader;
                e = e2;
                LogUtil.e("readFully.", e);
                closeQuietly(inputStreamReader2);
                r1 = inputStreamReader2;
            } catch (Throwable th2) {
                th = th2;
                r1 = inputStreamReader;
                closeQuietly(r1);
                closeQuietly(fileInputStream);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            fileInputStream = null;
        } catch (Throwable th3) {
            th = th3;
            fileInputStream = null;
        }
        closeQuietly(fileInputStream);
        return sb.toString();
    }
}
