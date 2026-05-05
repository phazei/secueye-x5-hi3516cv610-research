package com.alibaba.ailabs.tg.storage;

import android.database.Cursor;
import com.alibaba.ailabs.tg.utils.FileUtils;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/* JADX INFO: loaded from: classes.dex */
public class IOUtils {
    private static int sBufferSize = 8192;

    private IOUtils() {
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Throwable unused) {
            }
        }
    }

    public static void closeQuietly(Closeable... closeableArr) {
        if (closeableArr == null) {
            return;
        }
        for (Closeable closeable : closeableArr) {
            closeQuietly(closeable);
        }
    }

    public static boolean writeFileFromInputStream(File file, InputStream inputStream, boolean z) throws Throwable {
        BufferedOutputStream bufferedOutputStream;
        if (!FileUtils.createOrExistsFile(file) || inputStream == null) {
            return false;
        }
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file, z));
            } catch (IOException e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            byte[] bArr = new byte[sBufferSize];
            while (true) {
                int i = inputStream.read(bArr, 0, sBufferSize);
                if (i != -1) {
                    bufferedOutputStream.write(bArr, 0, i);
                } else {
                    closeQuietly(inputStream, bufferedOutputStream);
                    return true;
                }
            }
        } catch (IOException e2) {
            e = e2;
            bufferedOutputStream2 = bufferedOutputStream;
            e.printStackTrace();
            closeQuietly(inputStream, bufferedOutputStream2);
            return false;
        } catch (Throwable th2) {
            th = th2;
            bufferedOutputStream2 = bufferedOutputStream;
            closeQuietly(inputStream, bufferedOutputStream2);
            throw th;
        }
    }

    public static boolean writeFileFromBytes(File file, byte[] bArr, boolean z) throws Throwable {
        BufferedOutputStream bufferedOutputStream;
        if (bArr == null || !FileUtils.createOrExistsFile(file)) {
            return false;
        }
        BufferedOutputStream bufferedOutputStream2 = null;
        try {
            try {
                bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file, z));
            } catch (Throwable th) {
                th = th;
            }
        } catch (IOException e) {
            e = e;
        }
        try {
            bufferedOutputStream.write(bArr);
            closeQuietly(bufferedOutputStream);
            return true;
        } catch (IOException e2) {
            e = e2;
            bufferedOutputStream2 = bufferedOutputStream;
            e.printStackTrace();
            closeQuietly(bufferedOutputStream2);
            return false;
        } catch (Throwable th2) {
            th = th2;
            bufferedOutputStream2 = bufferedOutputStream;
            closeQuietly(bufferedOutputStream2);
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static byte[] readFile2Bytes(File file) throws Throwable {
        FileInputStream fileInputStream;
        Throwable th;
        ByteArrayOutputStream byteArrayOutputStream;
        Closeable[] closeableArr;
        byte[] bArr;
        byte[] byteArray = null;
        if (!FileUtils.isFileExists(file)) {
            return null;
        }
        try {
            try {
                fileInputStream = new FileInputStream(file);
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
                try {
                    bArr = new byte[sBufferSize];
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    closeableArr = new Closeable[]{fileInputStream, byteArrayOutputStream};
                }
            } catch (IOException e2) {
                e = e2;
                byteArrayOutputStream = null;
            } catch (Throwable th3) {
                th = th3;
                file = null;
                closeQuietly(fileInputStream, file);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            byteArrayOutputStream = null;
            fileInputStream = null;
        } catch (Throwable th4) {
            fileInputStream = null;
            th = th4;
            file = null;
        }
        while (true) {
            int i = fileInputStream.read(bArr, 0, sBufferSize);
            if (i == -1) {
                break;
            }
            byteArrayOutputStream.write(bArr, 0, i);
            closeQuietly(closeableArr);
            return byteArray;
        }
        byteArray = byteArrayOutputStream.toByteArray();
        closeableArr = new Closeable[]{fileInputStream, byteArrayOutputStream};
        closeQuietly(closeableArr);
        return byteArray;
    }

    public static Object deserialization(String str) throws Throwable {
        ObjectInputStream objectInputStream;
        ObjectInputStream objectInputStream2 = null;
        Object object = null;
        try {
            objectInputStream = new ObjectInputStream(new FileInputStream(str));
            try {
                try {
                    object = objectInputStream.readObject();
                } catch (Exception e) {
                    e = e;
                    e.printStackTrace();
                }
            } catch (Throwable th) {
                th = th;
                objectInputStream2 = objectInputStream;
                closeQuietly(objectInputStream2);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            objectInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            closeQuietly(objectInputStream2);
            throw th;
        }
        closeQuietly(objectInputStream);
        return object;
    }

    public static void serialization(String str, Object obj) throws Throwable {
        ObjectOutputStream objectOutputStream;
        ObjectOutputStream objectOutputStream2 = null;
        try {
            try {
                objectOutputStream = new ObjectOutputStream(new FileOutputStream(str));
            } catch (Exception e) {
                e = e;
            }
        } catch (Throwable th) {
            th = th;
        }
        try {
            objectOutputStream.writeObject(obj);
            closeQuietly(objectOutputStream);
        } catch (Exception e2) {
            e = e2;
            objectOutputStream2 = objectOutputStream;
            e.printStackTrace();
            closeQuietly(objectOutputStream2);
        } catch (Throwable th2) {
            th = th2;
            objectOutputStream2 = objectOutputStream;
            closeQuietly(objectOutputStream2);
            throw th;
        }
    }
}
