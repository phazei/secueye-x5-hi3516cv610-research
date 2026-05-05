package com.alibaba.sdk.android.error;

import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class ErrorCode implements Serializable {
    private final Code code;
    private String detail;
    private String msg;
    private String[] solutions;

    protected ErrorCode(Code code) {
        this.code = code;
        this.msg = null;
        this.detail = null;
        this.solutions = null;
    }

    @Deprecated
    public ErrorCode(String str, String str2, String str3, String[] strArr, boolean z) {
        this.code = new Code(str, null);
        this.msg = str2;
        this.detail = str3;
        this.solutions = strArr;
    }

    public static String docContent(ErrorCode[] errorCodeArr) {
        StringBuilder sb = new StringBuilder();
        sb.append(SdkConstant.CLOUDAPI_LF);
        for (ErrorCode errorCode : errorCodeArr) {
            sb.append(HiAnalyticsConstant.REPORT_VAL_SEPARATOR);
            sb.append(errorCode.code);
            sb.append(HiAnalyticsConstant.REPORT_VAL_SEPARATOR);
            sb.append(errorCode.msg);
            sb.append(HiAnalyticsConstant.REPORT_VAL_SEPARATOR);
            if (errorCode.solutions != null) {
                int i = 0;
                while (i < errorCode.solutions.length) {
                    if (i != 0) {
                        sb.append("<br />");
                    }
                    int i2 = i + 1;
                    sb.append(i2);
                    sb.append(". ");
                    sb.append(errorCode.solutions[i]);
                    i = i2;
                }
            }
            sb.append("|\n");
        }
        return sb.toString();
    }

    public static String docTitle() {
        return SdkConstant.CLOUDAPI_LF + "| 错误码 | 错误描述 | 备注            |" + SdkConstant.CLOUDAPI_LF + "| ------ | -------- | ------------------- |" + SdkConstant.CLOUDAPI_LF;
    }

    private static String toStringWithAllInfo(ErrorCode errorCode) {
        StringBuilder sb = new StringBuilder();
        sb.append("错误码：");
        sb.append(errorCode.code);
        sb.append(", ");
        sb.append("错误：");
        sb.append(errorCode.msg);
        String str = errorCode.detail;
        if (str != null && !str.isEmpty()) {
            sb.append("(");
            sb.append(errorCode.detail);
            sb.append("), ");
        }
        String[] strArr = errorCode.solutions;
        if (strArr != null && strArr.length > 0) {
            sb.append("请检查一下几点：");
            for (String str2 : errorCode.solutions) {
                sb.append(str2);
                sb.append("; ");
            }
        }
        return sb.toString();
    }

    public ErrorBuilder copy() {
        ErrorBuilder errorBuilderBuilder = ErrorBuilder.builder(this.code);
        String str = this.msg;
        if (str != null) {
            errorBuilderBuilder.msg(str);
        }
        String str2 = this.detail;
        if (str2 != null) {
            errorBuilderBuilder.detail(str2);
        }
        String[] strArr = this.solutions;
        if (strArr != null) {
            errorBuilderBuilder.solutions(strArr);
        }
        return errorBuilderBuilder;
    }

    @Deprecated
    public ErrorCode create(String str) {
        return create(str, false);
    }

    @Deprecated
    protected ErrorCode create(String str, boolean z) {
        return new ErrorCode(this.code.getCodeStr(), this.msg, str, this.solutions, z);
    }

    public String getCode() {
        return this.code.getCodeStr();
    }

    public int getCodeInt() {
        return this.code.getCodeInt();
    }

    public String getMsg() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.msg);
        String str = this.detail;
        if (str != null && !str.isEmpty()) {
            sb.append("(");
            sb.append(this.detail);
            sb.append(")");
        }
        return sb.toString();
    }

    protected void setDetail(String str) {
        this.detail = str;
    }

    protected void setMsg(String str) {
        this.msg = str;
    }

    protected void setSolutions(String[] strArr) {
        this.solutions = strArr;
    }

    public String toShortString() {
        return "(" + this.code + "," + this.msg + "," + this.detail + ")";
    }

    public String toString() {
        return toStringWithAllInfo(this);
    }
}
