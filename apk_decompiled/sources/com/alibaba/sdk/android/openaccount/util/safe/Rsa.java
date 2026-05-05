package com.alibaba.sdk.android.openaccount.util.safe;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;

/* JADX INFO: loaded from: classes.dex */
public class Rsa {
    private static final String ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    private static PublicKey getPublicKeyFromX509(String str, String str2) throws Exception {
        KeyFactory keyFactory;
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.decode(str2));
        try {
            try {
                keyFactory = KeyFactory.getInstance(str);
            } catch (Throwable unused) {
                keyFactory = KeyFactory.getInstance(str, "BC");
            }
        } catch (Throwable unused2) {
            keyFactory = KeyFactory.getInstance(str);
        }
        if (keyFactory == null) {
            keyFactory = KeyFactory.getInstance(str);
        }
        return keyFactory.generatePublic(x509EncodedKeySpec);
    }

    public static String encrypt(String str, String str2) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
        } catch (Throwable th) {
            th = th;
        }
        try {
            try {
                PublicKey publicKeyFromX509 = getPublicKeyFromX509(ALGORITHM, str2);
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(1, publicKeyFromX509);
                byte[] bytes = str.getBytes("UTF-8");
                int blockSize = cipher.getBlockSize();
                byteArrayOutputStream = new ByteArrayOutputStream();
                for (int i = 0; i < bytes.length; i += blockSize) {
                    try {
                        byteArrayOutputStream.write(cipher.doFinal(bytes, i, bytes.length - i < blockSize ? bytes.length - i : blockSize));
                    } catch (Exception e) {
                        e = e;
                        e.printStackTrace();
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        th.printStackTrace();
                        if (byteArrayOutputStream != null) {
                            byteArrayOutputStream.close();
                        }
                        return null;
                    }
                }
                String str3 = new String(Base64.encode(byteArrayOutputStream.toByteArray()));
                try {
                    byteArrayOutputStream.close();
                    return str3;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return str3;
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                return null;
            }
        } catch (Exception e4) {
            e = e4;
            byteArrayOutputStream = null;
        } catch (Throwable th3) {
            th = th3;
            byteArrayOutputStream = null;
        }
    }

    /* JADX WARN: Not initialized variable reg: 2, insn: 0x007b: MOVE (r0 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:39:0x007b */
    public static String decrypt(String str, String str2) throws Throwable {
        ByteArrayOutputStream byteArrayOutputStream;
        ByteArrayOutputStream byteArrayOutputStream2;
        ByteArrayOutputStream byteArrayOutputStream3 = null;
        try {
        } catch (Throwable th) {
            th = th;
            byteArrayOutputStream3 = byteArrayOutputStream;
        }
        try {
            try {
                PrivateKey privateKeyGeneratePrivate = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(str2)));
                Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                cipher.init(2, privateKeyGeneratePrivate);
                byte[] bArrDecode = Base64.decode(str);
                int blockSize = cipher.getBlockSize();
                byteArrayOutputStream2 = new ByteArrayOutputStream();
                for (int i = 0; i < bArrDecode.length; i += blockSize) {
                    try {
                        byteArrayOutputStream2.write(cipher.doFinal(bArrDecode, i, bArrDecode.length - i < blockSize ? bArrDecode.length - i : blockSize));
                    } catch (Exception e) {
                        e = e;
                        e.printStackTrace();
                        if (byteArrayOutputStream2 != null) {
                            byteArrayOutputStream2.close();
                        }
                        return null;
                    } catch (Throwable th2) {
                        th = th2;
                        th.printStackTrace();
                        if (byteArrayOutputStream2 != null) {
                            byteArrayOutputStream2.close();
                        }
                        return null;
                    }
                }
                String str3 = new String(byteArrayOutputStream2.toByteArray(), Charset.forName("UTF-8"));
                try {
                    byteArrayOutputStream2.close();
                    return str3;
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return str3;
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                return null;
            }
        } catch (Exception e4) {
            e = e4;
            byteArrayOutputStream2 = null;
        } catch (Throwable th3) {
            th = th3;
            byteArrayOutputStream2 = null;
        }
    }

    public static String sign(String str, String str2) {
        try {
            PrivateKey privateKeyGeneratePrivate = KeyFactory.getInstance(ALGORITHM).generatePrivate(new PKCS8EncodedKeySpec(Base64.decode(str2)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(privateKeyGeneratePrivate);
            signature.update(str.getBytes("utf-8"));
            return Base64.encode(signature.sign());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean doCheck(String str, String str2, String str3) {
        try {
            PublicKey publicKeyGeneratePublic = KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(Base64.decode(str3)));
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);
            signature.initVerify(publicKeyGeneratePublic);
            signature.update(str.getBytes("utf-8"));
            return signature.verify(Base64.decode(str2));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
