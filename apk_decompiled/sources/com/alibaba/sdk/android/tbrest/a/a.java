package com.alibaba.sdk.android.tbrest.a;

import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import com.alibaba.sdk.android.tbrest.utils.MD5Utils;
import com.alibaba.sdk.android.tbrest.utils.RC4;
import io.netty.handler.codec.memcache.binary.BinaryMemcacheOpcodes;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/* JADX INFO: compiled from: RestBaseRequestAuthentication.java */
/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private String f3185b;

    /* JADX INFO: renamed from: b, reason: collision with other field name */
    private byte[] f19b = null;
    private boolean f;
    private String l;

    public a(String str, String str2, boolean z) {
        this.f = false;
        this.f3185b = null;
        this.l = null;
        this.f3185b = str;
        this.l = str2;
        this.f = z;
    }

    public static String a(byte[] bArr, byte[] bArr2) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA1");
        mac.init(new SecretKeySpec(bArr, mac.getAlgorithm()));
        return MD5Utils.toHexString(mac.doFinal(bArr2));
    }

    public String b(String str) throws Exception {
        String str2;
        if (this.f3185b == null || (str2 = this.l) == null) {
            LogUtil.e("There is no appkey,please check it!");
            return null;
        }
        if (str == null) {
            return null;
        }
        String strA = "";
        try {
            if (this.f) {
                strA = a(str2.getBytes(), str.getBytes());
            } else {
                strA = a(a(), str.getBytes());
            }
        } catch (Exception unused) {
        }
        return strA;
    }

    private byte[] a() {
        if (this.f19b == null) {
            this.f19b = RC4.rc4(new byte[]{66, Constants.CMD_TYPE.CMD_OTA_REQUEST_VERIFY, Constants.CMD_TYPE.CMD_GET_FIRMWARE_VERSION_RESEX, -119, 118, -104, -30, 4, -95, 15, -26, -12, -75, -102, 71, BinaryMemcacheOpcodes.QUITQ, -3, -120, -1, -57, Constants.CMD_TYPE.CMD_GET_FIRMWARE_VERSION_RESEX, 99, -16, -101, 103, -74, 93, -114, 112, -26, -24, -24});
        }
        return this.f19b;
    }
}
