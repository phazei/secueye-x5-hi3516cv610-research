package com.aliyun.iot.aep.sdk.credential.IotCredentialManager;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;

/* JADX INFO: loaded from: classes2.dex */
public class IoTCredentialManageError {
    public static final int RESULT_CODE_ACCOUNT_TYPE_INVALID = 1;
    public static final int RESULT_CODE_AUTHCODE_CHECK_FAILED = 5;
    public static final int RESULT_CODE_AUTHCODE_INVALID = 2;
    public static final int RESULT_CODE_COMPANYID_PARAMS_CHECK_FAILED = 6;
    public static final int RESULT_CODE_NOT_LOGIN = 0;
    public static final int RESULT_CODE_OHTER = -1;
    public static final int RESULT_CODE_REFRESHTOKEN_INVALID = 3;
    public static final int RESULT_CODE_WRONG_RESPONSE_FORMAT = 4;
    public Object detail;
    public int errorCode;

    public IoTCredentialManageError(int i, Object obj) {
        this.errorCode = i;
        this.detail = obj;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("IoTCredentialManageError{errorCode=");
        sb.append(this.errorCode);
        sb.append(", detail=");
        sb.append(this.detail == null ? TmpConstant.GROUP_ROLE_UNKNOWN : a());
        sb.append('}');
        return sb.toString();
    }

    private String a() {
        String str;
        String str2;
        Object obj = this.detail;
        if (obj == null) {
            return "";
        }
        if (obj instanceof Exception) {
            return obj.toString();
        }
        if (!(obj instanceof IoTResponse)) {
            return "";
        }
        if (((IoTResponse) obj).getCode() == 200) {
            return ((IoTResponse) this.detail).getData() == null ? "" : ((IoTResponse) this.detail).getData().toString();
        }
        if (TextUtils.isEmpty(((IoTResponse) this.detail).getMessage())) {
            str = "";
        } else {
            str = "message:" + ((IoTResponse) this.detail).getMessage();
        }
        if (TextUtils.isEmpty(((IoTResponse) this.detail).getLocalizedMsg())) {
            str2 = "";
        } else {
            str2 = "message:" + ((IoTResponse) this.detail).getLocalizedMsg();
        }
        return str + str2;
    }
}
