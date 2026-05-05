package com.alibaba.ailabs.tg.utils;

import android.util.Base64;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.tg.storage.IOUtils;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/* JADX INFO: loaded from: classes.dex */
public class EncryptUtils {
    private EncryptUtils() {
    }

    public static String getMD5ForString(String str) {
        StringBuilder sb = new StringBuilder();
        try {
            byte[] bArrDigest = MessageDigest.getInstance("MD5").digest(str.getBytes());
            for (int i = 0; i < bArrDigest.length; i++) {
                int i2 = bArrDigest[i];
                if (i2 < 0) {
                    i2 += 256;
                }
                if (i2 < 16) {
                    sb.append("0");
                }
                sb.append(Integer.toHexString(i2));
            }
            return sb.toString();
        } catch (Exception unused) {
            return Integer.toString(str.hashCode());
        }
    }

    @Deprecated
    public static String md5(File file) throws Throwable {
        FileChannel channel;
        FileInputStream fileInputStream;
        byte[] bArrDigest;
        MessageDigest messageDigest;
        FileInputStream fileInputStream2 = null;
        try {
            try {
                messageDigest = MessageDigest.getInstance("MD5");
                fileInputStream = new FileInputStream(file);
            } catch (Throwable th) {
                th = th;
            }
            try {
                channel = fileInputStream.getChannel();
                try {
                    messageDigest.update(channel.map(FileChannel.MapMode.READ_ONLY, 0L, file.length()));
                    bArrDigest = messageDigest.digest();
                    IOUtils.closeQuietly(fileInputStream);
                    IOUtils.closeQuietly(channel);
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    IOUtils.closeQuietly(fileInputStream);
                    IOUtils.closeQuietly(channel);
                    bArrDigest = null;
                } catch (NoSuchAlgorithmException e2) {
                    e = e2;
                    fileInputStream2 = fileInputStream;
                    try {
                        throw new RuntimeException(e);
                    } catch (Throwable th2) {
                        th = th2;
                        IOUtils.closeQuietly(fileInputStream2);
                        IOUtils.closeQuietly(channel);
                        throw th;
                    }
                }
            } catch (IOException e3) {
                e = e3;
                channel = null;
            } catch (NoSuchAlgorithmException e4) {
                e = e4;
                channel = null;
            } catch (Throwable th3) {
                th = th3;
                channel = null;
                fileInputStream2 = fileInputStream;
                IOUtils.closeQuietly(fileInputStream2);
                IOUtils.closeQuietly(channel);
                throw th;
            }
        } catch (IOException e5) {
            e = e5;
            fileInputStream = null;
            channel = null;
        } catch (NoSuchAlgorithmException e6) {
            e = e6;
            channel = null;
        } catch (Throwable th4) {
            th = th4;
            channel = null;
            IOUtils.closeQuietly(fileInputStream2);
            IOUtils.closeQuietly(channel);
            throw th;
        }
        if (bArrDigest == null) {
            return null;
        }
        return ConvertUtils.bytes2HexString(bArrDigest);
    }

    public static String encryptMD5ToString(String str, String str2) {
        return ConvertUtils.bytes2HexString(encryptMD5((str + str2).getBytes()));
    }

    public static String encryptMD5ToString(@NonNull String str) {
        return encryptMD5ToString(str.getBytes());
    }

    public static String encryptMD5ToString(byte[] bArr) {
        return ConvertUtils.bytes2HexString(encryptMD5(bArr));
    }

    public static String encryptMD5ToString(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null) {
            return null;
        }
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return ConvertUtils.bytes2HexString(encryptMD5(bArr3));
    }

    public static byte[] encryptMD5(byte[] bArr) {
        return hashTemplate(bArr, "MD5");
    }

    public static String encryptMD5File2String(File file) {
        return ConvertUtils.bytes2HexString(encryptMD5File(file));
    }

    public static byte[] encryptMD5File(File file) throws Throwable {
        FileInputStream fileInputStream;
        try {
            if (file == null) {
                return null;
            }
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    DigestInputStream digestInputStream = new DigestInputStream(fileInputStream, MessageDigest.getInstance("MD5"));
                    while (digestInputStream.read(new byte[262144]) > 0) {
                    }
                    byte[] bArrDigest = digestInputStream.getMessageDigest().digest();
                    IOUtils.closeQuietly(fileInputStream);
                    return bArrDigest;
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    IOUtils.closeQuietly(fileInputStream);
                    return null;
                } catch (NoSuchAlgorithmException e2) {
                    e = e2;
                    e.printStackTrace();
                    IOUtils.closeQuietly(fileInputStream);
                    return null;
                }
            } catch (IOException e3) {
                e = e3;
                fileInputStream = null;
                e.printStackTrace();
                IOUtils.closeQuietly(fileInputStream);
                return null;
            } catch (NoSuchAlgorithmException e4) {
                e = e4;
                fileInputStream = null;
                e.printStackTrace();
                IOUtils.closeQuietly(fileInputStream);
                return null;
            } catch (Throwable th) {
                th = th;
                IOUtils.closeQuietly((Closeable) null);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String encryptSHA1ToString(byte[] bArr) {
        return ConvertUtils.bytes2HexString(encryptSHA1(bArr));
    }

    public static byte[] encryptSHA1(byte[] bArr) {
        return hashTemplate(bArr, "SHA1");
    }

    public static String encryptSHA256ToString(byte[] bArr) {
        return ConvertUtils.bytes2HexString(encryptSHA256(bArr));
    }

    public static byte[] encryptSHA256(byte[] bArr) {
        return hashTemplate(bArr, "SHA256");
    }

    private static byte[] hashTemplate(byte[] bArr, String str) {
        if (bArr == null || bArr.length <= 0) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(str);
            messageDigest.update(bArr);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSecureRandom() {
        byte[] bArr = new byte[16];
        new SecureRandom().nextBytes(bArr);
        return Base64.encodeToString(bArr, 11);
    }
}
