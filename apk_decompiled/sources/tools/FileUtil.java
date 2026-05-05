package tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes4.dex */
public class FileUtil {
    private static final String SEPARATOR = File.separator;

    public static boolean isExistFile(String str) {
        return new File(str).exists();
    }

    public static boolean createDir(String str) {
        File file = new File(str);
        if (file.exists()) {
            return true;
        }
        return file.mkdirs();
    }

    public static String readFile(InputStream inputStream) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            try {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String readFile(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            File file = new File(str);
            if (!new File(str.substring(0, str.lastIndexOf(47))).mkdirs()) {
                return "";
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                sb.append(line);
            }
        } catch (Exception e) {
            System.out.println("读取记录出错");
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static String readFile(Context context, int i) {
        InputStream inputStreamOpenRawResource = context.getResources().openRawResource(i);
        try {
            byte[] bArr = new byte[inputStreamOpenRawResource.available()];
            inputStreamOpenRawResource.read(bArr);
            inputStreamOpenRawResource.close();
            return new String(bArr, "utf8").replace("\r\n", SdkConstant.CLOUDAPI_LF);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFilesPath(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = context.getExternalFilesDir("").getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "/" + Utils.getUserPhone();
    }

    public static void copyFilesFromRaw(Context context, int i, String str, String str2) {
        InputStream inputStreamOpenRawResource = context.getResources().openRawResource(i);
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        readInputStream(str2 + SEPARATOR + str, inputStreamOpenRawResource);
    }

    public static void readInputStream(String str, InputStream inputStream) {
        File file = new File(str);
        try {
            if (file.exists()) {
                return;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bArr = new byte[inputStream.available()];
            while (true) {
                int i = inputStream.read(bArr);
                if (i != -1) {
                    fileOutputStream.write(bArr, 0, i);
                } else {
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    inputStream.close();
                    return;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public static void saveBitmap(Context context, Bitmap bitmap) {
        File file = new File(getFilesPath(context) + "/photo/");
        if (file.exists() || file.mkdirs()) {
            String str = System.currentTimeMillis() + ".jpg";
            File file2 = new File(file, str);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), file2.getAbsolutePath(), str, (String) null);
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            }
        }
    }

    public static String readMETAINFO(Context context) {
        ClassLoader classLoader = context.getClassLoader();
        if (classLoader.getResource("META-INF/channel") == null) {
            return null;
        }
        return readFile(classLoader.getResourceAsStream("META-INF/channel"));
    }

    public static String getSDPath() {
        return Environment.getExternalStorageDirectory() + "/";
    }

    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static File[] getDirArray(String str) {
        return new File(str).listFiles();
    }

    public static List<File> getDirList(String str) {
        return Arrays.asList(new File(str).listFiles());
    }

    public static boolean delete(String str) {
        File file = new File(str);
        if (!file.exists()) {
            System.out.println("删除文件失败:" + str + "不存在！");
            return false;
        }
        if (file.isFile()) {
            return deleteFile(str);
        }
        return deleteDirectory(str);
    }

    public static boolean deleteFile(String str) {
        File file = new File(str);
        try {
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    System.out.println("删除单个文件" + str + "成功！");
                    return true;
                }
                System.out.println("删除单个文件" + str + "失败！");
                return false;
            }
            System.out.println("删除单个文件失败：" + str + "不存在！");
            return false;
        } finally {
            System.gc();
        }
    }

    public static boolean deleteDirectory(String str) {
        if (!str.endsWith(File.separator)) {
            str = str + File.separator;
        }
        File file = new File(str);
        if (!file.exists() || !file.isDirectory()) {
            System.out.println("删除目录失败：" + str + "不存在！");
            return false;
        }
        File[] fileArrListFiles = file.listFiles();
        boolean zDeleteDirectory = true;
        for (int i = 0; i < fileArrListFiles.length; i++) {
            try {
                if (fileArrListFiles[i].isFile()) {
                    zDeleteDirectory = deleteFile(fileArrListFiles[i].getAbsolutePath());
                    if (!zDeleteDirectory) {
                        break;
                    }
                } else {
                    if (fileArrListFiles[i].isDirectory() && !(zDeleteDirectory = deleteDirectory(fileArrListFiles[i].getAbsolutePath()))) {
                        break;
                    }
                }
            } catch (NullPointerException unused) {
            }
        }
        if (!zDeleteDirectory) {
            System.out.println("删除目录失败！");
            return false;
        }
        if (!file.delete()) {
            return false;
        }
        System.out.println("删除目录" + str + "成功！");
        return true;
    }

    public static String getFilterPath(String str, String str2, String str3) {
        return str.concat(str2 + OpenAccountUIConstants.UNDER_LINE + str3 + File.separator);
    }

    public static String saveCrashInfo2File(Throwable th, String str) {
        StringBuilder sb = new StringBuilder();
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        th.printStackTrace(printWriter);
        sb.append(stringWriter.toString());
        printWriter.close();
        try {
            String str2 = "crash-" + System.currentTimeMillis() + ".log";
            if (Environment.getExternalStorageState().equals("mounted")) {
                File file = new File(str);
                if (!file.exists()) {
                    file.mkdirs();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(new File(str, str2));
                fileOutputStream.write(sb.toString().getBytes());
                fileOutputStream.close();
            }
            return str2;
        } catch (Exception unused) {
            return null;
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [tools.FileUtil$1] */
    public static void extLogcat(final String str) {
        if (hasSDCard()) {
            new Thread() { // from class: tools.FileUtil.1
                /* JADX WARN: Can't wrap try/catch for region: R(14:0|2|(7:94|3|(1:5)|6|(1:10)|11|(6:115|12|111|13|108|14))|(5:113|15|(1:17)(1:117)|93|69)|18|106|19|98|23|(1:28)|29|93|69|(1:(0))) */
                /* JADX WARN: Code restructure failed: missing block: B:21:0x007e, code lost:
                
                    r0 = move-exception;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:22:0x007f, code lost:
                
                    r0.printStackTrace();
                 */
                /* JADX WARN: Code restructure failed: missing block: B:25:0x0086, code lost:
                
                    r0 = move-exception;
                 */
                /* JADX WARN: Code restructure failed: missing block: B:26:0x0087, code lost:
                
                    r0.printStackTrace();
                 */
                /* JADX WARN: Removed duplicated region for block: B:104:0x00fb A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:119:? A[SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:82:0x010f  */
                /* JADX WARN: Removed duplicated region for block: B:91:0x0114 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Removed duplicated region for block: B:96:0x0105 A[EXC_TOP_SPLITTER, SYNTHETIC] */
                /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:68:0x00f4 -> B:93:0x00f7). Please report as a decompilation issue!!! */
                @Override // java.lang.Thread, java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public void run() throws java.lang.Throwable {
                    /*
                        Method dump skipped, instruction units count: 285
                        To view this dump add '--comments-level debug' option
                    */
                    throw new UnsupportedOperationException("Method not decompiled: tools.FileUtil.AnonymousClass1.run():void");
                }
            }.start();
        }
    }

    public static void mkdirs(File file) {
        if (file.exists() && file.isDirectory()) {
            return;
        }
        if (file.exists()) {
            file.delete();
            file.mkdirs();
        } else {
            file.mkdirs();
        }
    }
}
