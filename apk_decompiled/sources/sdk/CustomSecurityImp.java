package sdk;

import com.aliyun.alink.linksdk.securesigner.ISecuritySource;
import com.aliyun.alink.linksdk.securesigner.util.Utils;

/* JADX INFO: loaded from: classes4.dex */
public class CustomSecurityImp implements ISecuritySource {
    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String getAppKey() {
        return "26001873";
    }

    @Override // com.aliyun.alink.linksdk.securesigner.ISecuritySource
    public String sign(String str, String str2) {
        if ("MD5".equals(str2)) {
            return Utils.getMD5String(str).toLowerCase();
        }
        if ("HmacSHA256".equals(str2)) {
            return Utils.hmacSha256("f4a90ebee699166af95b092dffadcfc3".getBytes(), str.getBytes()).toLowerCase();
        }
        return Utils.hmacSha1("f4a90ebee699166af95b092dffadcfc3", str).toLowerCase();
    }
}
