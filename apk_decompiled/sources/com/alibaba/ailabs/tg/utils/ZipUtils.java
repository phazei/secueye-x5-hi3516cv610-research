package com.alibaba.ailabs.tg.utils;

import android.text.TextUtils;
import com.alibaba.ailabs.tg.storage.IOUtils;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/* JADX INFO: loaded from: classes.dex */
public final class ZipUtils {
    private static final int BUFFER_LEN = 8192;

    private ZipUtils() {
    }

    public static boolean zipFiles(Collection<String> collection, String str) throws IOException {
        return zipFiles(collection, str, (String) null);
    }

    public static boolean zipFiles(Collection<String> collection, String str, String str2) throws Throwable {
        ZipOutputStream zipOutputStream;
        if (collection == null || str == null) {
            return false;
        }
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(str));
        } catch (Throwable th) {
            th = th;
            zipOutputStream = null;
        }
        try {
            Iterator<String> it = collection.iterator();
            while (it.hasNext()) {
                File fileByPath = FileUtils.getFileByPath(it.next());
                if (fileByPath != null && !zipFile(fileByPath, "", zipOutputStream, str2)) {
                    zipOutputStream.finish();
                    IOUtils.closeQuietly(zipOutputStream);
                    return false;
                }
            }
            zipOutputStream.finish();
            IOUtils.closeQuietly(zipOutputStream);
            return true;
        } catch (Throwable th2) {
            th = th2;
            if (zipOutputStream != null) {
                zipOutputStream.finish();
                IOUtils.closeQuietly(zipOutputStream);
            }
            throw th;
        }
    }

    public static boolean zipFiles(Collection<File> collection, File file) throws IOException {
        return zipFiles(collection, file, (String) null);
    }

    public static boolean zipFiles(Collection<File> collection, File file, String str) throws Throwable {
        ZipOutputStream zipOutputStream;
        if (collection == null || file == null) {
            return false;
        }
        try {
            zipOutputStream = new ZipOutputStream(new FileOutputStream(file));
            try {
                Iterator<File> it = collection.iterator();
                while (it.hasNext()) {
                    if (!zipFile(it.next(), "", zipOutputStream, str)) {
                        zipOutputStream.finish();
                        IOUtils.closeQuietly(zipOutputStream);
                        return false;
                    }
                }
                zipOutputStream.finish();
                IOUtils.closeQuietly(zipOutputStream);
                return true;
            } catch (Throwable th) {
                th = th;
                if (zipOutputStream != null) {
                    zipOutputStream.finish();
                    IOUtils.closeQuietly(zipOutputStream);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            zipOutputStream = null;
        }
    }

    public static boolean zipFile(String str, String str2) throws IOException {
        return zipFile(FileUtils.getFileByPath(str), FileUtils.getFileByPath(str2), (String) null);
    }

    public static boolean zipFile(String str, String str2, String str3) throws IOException {
        return zipFile(FileUtils.getFileByPath(str), FileUtils.getFileByPath(str2), str3);
    }

    public static boolean zipFile(File file, File file2) throws IOException {
        return zipFile(file, file2, (String) null);
    }

    public static boolean zipFile(File file, File file2, String str) throws Throwable {
        if (file == null || file2 == null) {
            return false;
        }
        ZipOutputStream zipOutputStream = null;
        try {
            ZipOutputStream zipOutputStream2 = new ZipOutputStream(new FileOutputStream(file2));
            try {
                boolean zZipFile = zipFile(file, "", zipOutputStream2, str);
                IOUtils.closeQuietly(zipOutputStream2);
                return zZipFile;
            } catch (Throwable th) {
                th = th;
                zipOutputStream = zipOutputStream2;
                if (zipOutputStream != null) {
                    IOUtils.closeQuietly(zipOutputStream);
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    private static boolean zipFile(File file, String str, ZipOutputStream zipOutputStream, String str2) throws Throwable {
        BufferedInputStream bufferedInputStream;
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(TextUtils.isEmpty(str) ? "" : File.separator);
        sb.append(file.getName());
        String string = sb.toString();
        if (file.isDirectory()) {
            File[] fileArrListFiles = file.listFiles();
            if (fileArrListFiles == null || fileArrListFiles.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(string + '/');
                zipEntry.setComment(str2);
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.closeEntry();
                return true;
            }
            for (File file2 : fileArrListFiles) {
                if (!zipFile(file2, string, zipOutputStream, str2)) {
                    return false;
                }
            }
            return true;
        }
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        } catch (Throwable th) {
            th = th;
            bufferedInputStream = null;
        }
        try {
            ZipEntry zipEntry2 = new ZipEntry(string);
            zipEntry2.setComment(str2);
            zipOutputStream.putNextEntry(zipEntry2);
            byte[] bArr = new byte[8192];
            while (true) {
                int i = bufferedInputStream.read(bArr, 0, 8192);
                if (i != -1) {
                    zipOutputStream.write(bArr, 0, i);
                } else {
                    zipOutputStream.closeEntry();
                    IOUtils.closeQuietly(bufferedInputStream);
                    return true;
                }
            }
        } catch (Throwable th2) {
            th = th2;
            IOUtils.closeQuietly(bufferedInputStream);
            throw th;
        }
    }

    public static List<File> unzipFile(String str, String str2) throws IOException {
        return unzipFileByKeyword(str, str2, (String) null);
    }

    public static List<File> unzipFile(File file, File file2) throws IOException {
        return unzipFileByKeyword(file, file2, (String) null);
    }

    public static List<File> unzipFileByKeyword(String str, String str2, String str3) throws IOException {
        return unzipFileByKeyword(FileUtils.getFileByPath(str), FileUtils.getFileByPath(str2), str3);
    }

    public static List<File> unzipFileByKeyword(File file, File file2, String str) throws IOException {
        if (file == null || file2 == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> enumerationEntries = zipFile.entries();
        if (TextUtils.isEmpty(str)) {
            while (enumerationEntries.hasMoreElements()) {
                ZipEntry zipEntryNextElement = enumerationEntries.nextElement();
                if (!unzipChildFile(file2, arrayList, zipFile, zipEntryNextElement, zipEntryNextElement.getName())) {
                    return arrayList;
                }
            }
        } else {
            while (enumerationEntries.hasMoreElements()) {
                ZipEntry zipEntryNextElement2 = enumerationEntries.nextElement();
                String name = zipEntryNextElement2.getName();
                if (name.contains(str) && !unzipChildFile(file2, arrayList, zipFile, zipEntryNextElement2, name)) {
                    return arrayList;
                }
            }
        }
        return arrayList;
    }

    private static boolean unzipChildFile(File file, List<File> list, ZipFile zipFile, ZipEntry zipEntry, String str) throws Throwable {
        BufferedInputStream bufferedInputStream;
        File file2 = new File(file + File.separator + str);
        list.add(file2);
        if (zipEntry.isDirectory()) {
            if (!FileUtils.createOrExistsDir(file2)) {
                return false;
            }
        } else {
            if (!FileUtils.createOrExistsFile(file2)) {
                return false;
            }
            BufferedOutputStream bufferedOutputStream = null;
            try {
                bufferedInputStream = new BufferedInputStream(zipFile.getInputStream(zipEntry));
                try {
                    BufferedOutputStream bufferedOutputStream2 = new BufferedOutputStream(new FileOutputStream(file2));
                    try {
                        byte[] bArr = new byte[8192];
                        while (true) {
                            int i = bufferedInputStream.read(bArr);
                            if (i == -1) {
                                break;
                            }
                            bufferedOutputStream2.write(bArr, 0, i);
                        }
                        IOUtils.closeQuietly(bufferedInputStream, bufferedOutputStream2);
                    } catch (Throwable th) {
                        th = th;
                        bufferedOutputStream = bufferedOutputStream2;
                        IOUtils.closeQuietly(bufferedInputStream, bufferedOutputStream);
                        throw th;
                    }
                } catch (Throwable th2) {
                    th = th2;
                }
            } catch (Throwable th3) {
                th = th3;
                bufferedInputStream = null;
            }
        }
        return true;
    }

    public static List<String> getFilesPath(String str) throws IOException {
        return getFilesPath(FileUtils.getFileByPath(str));
    }

    public static List<String> getFilesPath(File file) throws IOException {
        if (file == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Enumeration<? extends ZipEntry> enumerationEntries = new ZipFile(file).entries();
        while (enumerationEntries.hasMoreElements()) {
            arrayList.add(enumerationEntries.nextElement().getName());
        }
        return arrayList;
    }

    public static List<String> getComments(String str) throws IOException {
        return getComments(FileUtils.getFileByPath(str));
    }

    public static List<String> getComments(File file) throws IOException {
        if (file == null) {
            return null;
        }
        ArrayList arrayList = new ArrayList();
        Enumeration<? extends ZipEntry> enumerationEntries = new ZipFile(file).entries();
        while (enumerationEntries.hasMoreElements()) {
            arrayList.add(enumerationEntries.nextElement().getComment());
        }
        return arrayList;
    }
}
