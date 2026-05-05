package com.taobao.accs.utl;

import com.alibaba.ailabs.iot.aisbase.Constants;
import io.netty.handler.codec.http.HttpConstants;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public final class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static byte[] f6453a = {82, BinaryMemcacheOpcodes.DECREMENTQ, Constants.CMD_TYPE.CMD_SEND_DEVICE_INFO, HttpConstants.COMMA, -16, 124, -40, -114, -87, -40, Constants.CMD_TYPE.CMD_OTA_REQUEST_VERIFY, BinaryMemcacheOpcodes.QUITQ, -56, BinaryMemcacheOpcodes.QUITQ, -33, 75};

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static ThreadLocal<Cipher> f6454b = new ThreadLocal<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final AlgorithmParameterSpec f6455c = new IvParameterSpec(f6453a);

    public static final byte[] a(byte[] bArr) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bArr);
            return messageDigest.digest();
        } catch (Throwable th) {
            throw new RuntimeException("md5 value Throwable", th);
        }
    }
}
