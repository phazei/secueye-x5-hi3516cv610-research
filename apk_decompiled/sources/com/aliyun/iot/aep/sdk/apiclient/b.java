package com.aliyun.iot.aep.sdk.apiclient;

import android.app.Application;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.cloudapi.sdk.signature.ISignerFactory;
import com.alibaba.cloudapi.sdk.signature.ISinger;
import com.alibaba.wireless.security.open.SecException;
import com.alibaba.wireless.security.open.SecurityGuardManager;
import com.alibaba.wireless.security.open.SecurityGuardParamContext;
import com.alibaba.wireless.security.open.securesignature.ISecureSignatureComponent;
import com.aliyun.alink.linksdk.securesigner.SecurityImpl;
import com.aliyun.alink.linksdk.securesigner.util.Utils;
import java.util.HashMap;

/* JADX INFO: compiled from: SecuritySignerFactory.java */
/* JADX INFO: loaded from: classes2.dex */
public class b implements ISignerFactory {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static ISinger f4560a;

    /* JADX INFO: compiled from: SecuritySignerFactory.java */
    public static class a implements ISinger {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public Context f4561a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public String f4562b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public String f4563c;

        public a(Context context, String str, String str2) {
            this.f4561a = context;
            this.f4562b = str;
            this.f4563c = str2;
        }

        public static String a(String str) {
            int length = str.length() / 2;
            if (1 == length) {
                throw new RuntimeException("invalid hex string : " + str);
            }
            byte[] bArr = new byte[length];
            for (int i = 0; i < length; i++) {
                int i2 = i * 2;
                bArr[i] = (byte) (Integer.valueOf(str.substring(i2, i2 + 2), 16).intValue() & 255);
            }
            return new String(Base64.encode(bArr, 0), SdkConstant.CLOUDAPI_ENCODING);
        }

        @Override // com.alibaba.cloudapi.sdk.signature.ISinger
        public String sign(String str, String str2) {
            Log.d("SecuritySigner", "sign: strToSign:" + str + " secretKey:" + str2);
            if (!Utils.hasSecurityGuardDep()) {
                return a(new SecurityImpl().sign(str, "HmacSHA1"));
            }
            try {
                SecurityGuardManager.setSilentMode(true);
            } catch (Throwable th) {
                th.printStackTrace();
            }
            ISecureSignatureComponent secureSignatureComp = SecurityGuardManager.getInstance(this.f4561a).getSecureSignatureComp();
            HashMap map = new HashMap();
            map.put("INPUT", str);
            SecurityGuardParamContext securityGuardParamContext = new SecurityGuardParamContext();
            securityGuardParamContext.appKey = this.f4562b;
            securityGuardParamContext.paramMap = map;
            securityGuardParamContext.requestType = 3;
            try {
                return a(secureSignatureComp.signRequest(securityGuardParamContext, this.f4563c));
            } catch (SecException unused) {
                throw new RuntimeException();
            }
        }
    }

    public b(Application application, String str, String str2) {
        f4560a = new a(application, str, str2);
    }

    @Override // com.alibaba.cloudapi.sdk.signature.ISignerFactory
    public ISinger getSigner() {
        return f4560a;
    }
}
