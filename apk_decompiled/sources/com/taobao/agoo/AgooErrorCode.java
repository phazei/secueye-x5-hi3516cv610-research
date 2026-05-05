package com.taobao.agoo;

import android.util.Log;
import com.alibaba.sdk.android.error.ErrorBuilder;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.error.ErrorDefine;

/* JADX INFO: loaded from: classes3.dex */
public class AgooErrorCode {
    private static final ErrorDefine define = new ErrorDefine("EAGOO");
    public static final ErrorCode SUCCESS = define.defineSdkError("success").msg("success").build();
    public static final ErrorCode REMOVE_ALIAS_FAIL_NO_TOKEN = define.defineSdkError("remove_alias_fail_no_token").msg("移除别名失败，本地没有别名记录").solution("请检查输入的别名是否正确").solution("低版本推送有概率出现，添加别名后，应用的数据被清除，导致sdk内部存储的别名信息丢失，无法移除").build();
    public static final ErrorCode REMOVE_ALIAS_FAIL_NO_ALIAS = define.defineSdkError("remove_alias_fail_no_alias").msg("移除别名失败，本地没有别名记录").solution("请检查输入的别名是否正确").solution("低版本推送有概率出现，添加别名后，应用的数据被清除，导致sdk内部存储的别名信息丢失，无法移除").build();
    public static final ErrorCode INVALID_ARG = define.defineSdkError("invalid_arg").msg("请求参数错误").solution("请检查输入参数").build();
    public static final ErrorCode ACCS_CHECK_ERROR = define.defineSdkError("accs_disabled").msg("accs检查不通过").solution("请检查初始化是否成功").solution("请检查配置是否正确").solution("请检查请求是否是在主进程").build();
    public static final ErrorCode AGOO_NOT_BIND = define.defineSdkError("agoo_not_bind").msg("请先注册初始化agoo").solution("请检查初始化是否成功").build();
    public static final ErrorCode REGISTER_DATA_ERROR = define.defineSdkError("register_data_error").msg("构造注册数据失败").solution("请检查配置参数是否正确，初始化是否成功").build();
    public static final ErrorCode[] codes = {SUCCESS, REMOVE_ALIAS_FAIL_NO_TOKEN, REMOVE_ALIAS_FAIL_NO_ALIAS, INVALID_ARG, ACCS_CHECK_ERROR, AGOO_NOT_BIND, converAccsErrorCode(123, "accs 错误信息").solution("格式EAGOO_ACCS_123, 123为accs错误码，请结合accs错误码排查").build(), converAgooServerErrorCode("XXX", "服务错误信息").solution("格式EAGOO_SERVER_XXX, XXX为agoo服务错误码，请联系阿里云技术支持排查").build()};

    public static ErrorBuilder converAccsErrorCode(int i, String str) {
        return define.defineError("ACCS", String.valueOf(i)).msg(str).solution("accs底层错误，需要根据错误码排查");
    }

    public static ErrorBuilder converAgooServerErrorCode(String str, String str2) {
        return define.defineServerError(str).msg(str2).solution("agoo 服务报错，请联系技术支持排查");
    }

    public static void printErrorCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("AGOO错误码，一共" + codes.length + "个");
        sb.append('\n');
        sb.append(ErrorCode.docTitle());
        sb.append(ErrorCode.docContent(codes));
        Log.w("AGOO_ERROR_CODE", sb.toString());
    }
}
