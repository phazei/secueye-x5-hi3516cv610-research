package lvbyte;

import android.net.Uri;
import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;

/* JADX INFO: loaded from: classes4.dex */
public class lvnew {
    public static String lvdo(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String strSubstring = str.substring(0, str.lastIndexOf("/"));
        if (TextUtils.isEmpty(strSubstring)) {
            return "";
        }
        String queryParameter = Uri.parse(strSubstring).getQueryParameter(UTConstants.E_SDK_CONNECT_SESSION_ACTION);
        return !TextUtils.isEmpty(queryParameter) ? queryParameter : "";
    }
}
