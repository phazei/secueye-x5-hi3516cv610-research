package com.aliyun.alink.linksdk.tmp.utils;

import android.util.Log;
import com.aliyun.alink.linksdk.tools.ALog;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* JADX INFO: loaded from: classes2.dex */
public class Secure {
    private static final String TAG = "[Tmp]Secure";

    public static byte[] getHMacSha1Str(String str, String str2) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(str2.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(secretKeySpec);
            return mac.doFinal(str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException | InvalidKeyException | NoSuchAlgorithmException unused) {
            return null;
        }
    }

    public static String md5(String str) {
        try {
            byte[] bytes = str.getBytes("ASCII");
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            byte[] bArrDigest = messageDigest.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b2 : bArrDigest) {
                String hexString = Integer.toHexString(b2 & 255);
                if (hexString.length() == 1) {
                    stringBuffer.append('0');
                }
                stringBuffer.append(hexString);
            }
            return stringBuffer.toString();
        } catch (Exception unused) {
            return null;
        }
    }

    public static String getSignValue(String str, String str2, String str3, String str4) {
        ALog.d(TAG, "clientId:" + str + " pk:" + str2 + " dn:" + str2 + " devSec:" + str4);
        String str5 = TmpConstant.KEY_CLIENT_ID + str + "deviceName" + str3 + "deviceSecret" + str4 + "productKey" + str2;
        String strByte2hex = null;
        try {
            ALog.d(TAG, "input:" + str5);
            MessageDigest messageDigest = MessageDigest.getInstance(MessageDigestAlgorithms.SHA_256);
            messageDigest.update(str5.getBytes());
            byte[] bArrDigest = messageDigest.digest();
            byte[] bArrCopyOfRange = Arrays.copyOfRange(bArrDigest, 0, bArrDigest.length);
            strByte2hex = TextHelper.byte2hex(bArrCopyOfRange, bArrCopyOfRange.length);
            Log.d(TAG, "digest:" + strByte2hex);
            return strByte2hex;
        } catch (Exception e) {
            e.printStackTrace();
            return strByte2hex;
        }
    }
}
