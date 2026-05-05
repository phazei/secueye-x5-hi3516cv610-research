package com.taobao.accs;

import android.util.Log;
import anet.channel.GlobalAppRuntimeInfo;
import com.alibaba.sdk.android.error.ErrorBuilder;
import com.alibaba.sdk.android.error.ErrorDefine;
import com.alibaba.sdk.android.error.IntCodeGenerator;
import com.taobao.accs.utl.g;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class AccsErrorCode {
    private static final int HTTP_CODE_DM_APPKEY_INVALID = 303;
    private static final int HTTP_CODE_DM_DEVICEID_INVALID = 302;
    private static final int HTTP_CODE_DM_PACKAGENAME_INVALID = 304;
    private static final int HTTP_CODE_DM_TAIR_ERROR = 102;
    private static final int HTTP_CODE_SUCCESS = 200;
    private static final ErrorDefine define = new ErrorDefine("EACCS", new IntCodeGenerator());
    public static final com.alibaba.sdk.android.error.ErrorCode SUCCESS = define.defineSdkError(com.aliyun.alink.linksdk.alcs.api.utils.ErrorCode.UNKNOWN_SUCCESS_CODE).msg("成功").build();
    public static final com.alibaba.sdk.android.error.ErrorCode APP_NOT_BIND = define.defineSdkError(com.aliyun.alink.linksdk.alcs.api.utils.ErrorCode.UNKNOWN_ERROR_CODE).msg("通道未建立").solution("请先初始化，bindApp，再调用其它api").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SPDY_CONNECTION_DISCONNECTED_WHEN_SEND_DATA = define.defineSdkError("-1").msg("静默连接中断，无法发送消息").solution("内部会重试，如果一直失败，需要排查下静默通道是否正常").build();
    public static final com.alibaba.sdk.android.error.ErrorCode PARAMETER_ERROR = define.defineSdkError("-2").msg("参数错误,发送的msg为null").solution("请检查发起请求的参数是否正确").build();
    public static final com.alibaba.sdk.android.error.ErrorCode RESPONSE_PARSE_ERROR = define.defineSdkError("-3").msg("服务返回数据异常").solution("请关注错误信息中的服务返回数据，并联系阿里云技术支持同学确认原因").build();
    public static final com.alibaba.sdk.android.error.ErrorCode MESSAGE_TOO_LARGE = define.defineSdkError("-4").msg("单次发送数据过大").solution("请减少一次发送的数据量，封装之后总的数据量要小于16KB").build();
    public static final com.alibaba.sdk.android.error.ErrorCode MESSAGE_HOST_NULL = define.defineSdkError("-5").msg("发送服务地址为null").solution("请检查下初始化配置是否正确").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SPDY_AUTH_PARAM_ERROR = define.defineSdkError("-6").msg("静默通道长连接认证参数错误").solution("请检查初始化参数配置是否正确").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SPDY_AUTH_EXCEPTION = define.defineSdkError("-7").msg("静默通道长连接认证异常").solution("请查看错误信息，确认具体异常信息").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SEND_LOCAL_EXCEPTION = define.defineSdkError("-8").msg("发送数据异常").solution("请查看错误信息，确认具体异常信息").build();
    public static final com.alibaba.sdk.android.error.ErrorCode REQ_TIME_OUT = define.defineSdkError("-9").msg("发送消息超时").solution("需要结合具体是查看为什么超时").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SPDY_CON_DISCONNECTED = define.defineSdkError("-10").msg("静默通道长连接断连").solution("断连需要查看之前的日志").build();
    public static final com.alibaba.sdk.android.error.ErrorCode INAPP_CON_DISCONNECTED = define.defineSdkError("-11").msg("应用内长连接断开").solution("一般为长连接建连失败造成，需要看日志分析").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SPDY_PING_TIME_OUT = define.defineSdkError("-12").msg("静默通道长连接ping超时").build();
    public static final com.alibaba.sdk.android.error.ErrorCode NO_NETWORK = define.defineSdkError("-13").msg("无网络").solution("请检查网络连接").build();
    public static final com.alibaba.sdk.android.error.ErrorCode APPKEY_NULL = define.defineSdkError("-14").msg("appKey不存在").solution("请检查初始化配置是否正确").build();
    public static final com.alibaba.sdk.android.error.ErrorCode APPSECRET_NULL = define.defineSdkError("-15").msg("appSecret不存在").solution("请检查初始化配置是否正确").build();
    public static final com.alibaba.sdk.android.error.ErrorCode MESSAGE_QUEUE_FULL = define.defineServerError("70008").msg("长连接发送队列已满").solution("请确认是否有高并发发送消息，如果有，请限制发送频次").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SERVIER_LOW_LIMIT = define.defineServerError("70020").msg("低级别限流").solution("请和部署同学确认限流策略").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SERVIER_HIGH_LIMIT = define.defineServerError("70021").msg("高级别限流,不发送").solution("请和部署同学确认限流策略").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SERVIER_HIGH_LIMIT_BRUSH = define.defineServerError("70023").msg("防刷解封后触发的限流，不发送").solution("请和部署同学确认限流策略").build();
    public static final com.alibaba.sdk.android.error.ErrorCode DM_TAIR_ERROR = define.defineServerError("102").msg("设备无效").solution("如果是测试时发现的，请清除应用数据重新尝试").build();
    public static final com.alibaba.sdk.android.error.ErrorCode DM_DEVICEID_INVALID = define.defineServerError("302").msg("设备无效").solution("如果是测试时发现的，请清除应用数据重新尝试").build();
    public static final com.alibaba.sdk.android.error.ErrorCode DM_APPKEY_INVALID = define.defineServerError("303").msg("appkey配置错误").solution("请检查appKey配置是否正确").build();
    public static final com.alibaba.sdk.android.error.ErrorCode DM_PACKAGENAME_INVALID = define.defineServerError("304").msg("包名错误").solution("请检查appKey和应用包名是否匹配").build();
    public static final com.alibaba.sdk.android.error.ErrorCode SERVER_UNKNOWN_ERROR = define.defineServerError("-20").msg("服务返回错误").solution("请关注下错误信息中的服务返回的错误码，并联系阿里云技术支持同学确认原因").build();
    public static final com.alibaba.sdk.android.error.ErrorCode NETWORKSDK_SPDY_CLOSE_ERROR = define.defineSdkError("-22").msg("底层sdk连接关闭").solution("请关注下错误信息中的底层sdk返回的错误信息，并联系阿里云技术支持同学确认原因").build();
    public static final com.alibaba.sdk.android.error.ErrorCode NETWORKSDK_SPDY_RES_ERROR = define.defineSdkError("-23").msg("发送数据返回错误").solution("请关注下错误信息中的底层sdk返回的错误信息，并联系阿里云技术支持同学确认原因").build();
    public static final com.alibaba.sdk.android.error.ErrorCode ERROR_SHOULD_NEVER_HAPPEN = define.defineSdkError("-25").msg("不应该发生的错误").solution("请关注下错误信息，检查初始化是否存在错误").build();
    public static final com.alibaba.sdk.android.error.ErrorCode NETWORK_INAPP_ARGS_INVALID = define.defineSdkError("-26").msg("建连参数错误").solution("请检查初始化配置是否正确").build();
    public static final com.alibaba.sdk.android.error.ErrorCode NETWORK_INAPP_TIMEOUT = define.defineSdkError("-27").msg("建连超时").solution("请查看具体错误信息排查").solution("请检查网络是否正常").build();
    public static final com.alibaba.sdk.android.error.ErrorCode NETWORK_INAPP_CONNECT_FAIL = define.defineSdkError("-28").msg("建连失败").solution("请查看具体错误信息排查").solution("请检查网络是否正常").build();
    public static final com.alibaba.sdk.android.error.ErrorCode NETWORK_INAPP_NO_STRATEGY = define.defineSdkError("-29").msg("连接地址不存在").solution("当前网络下无法解析长链接地址").solution("请检查网络是否正常").build();
    public static final com.alibaba.sdk.android.error.ErrorCode NETWORK_INAPP_EXCEPTION = define.defineSdkError("-30").msg("建连异常").solution("请查看具体错误信息排查").build();
    public static final com.alibaba.sdk.android.error.ErrorCode[] codes = {SUCCESS, APP_NOT_BIND, SPDY_CONNECTION_DISCONNECTED_WHEN_SEND_DATA, PARAMETER_ERROR, RESPONSE_PARSE_ERROR, MESSAGE_TOO_LARGE, MESSAGE_HOST_NULL, SPDY_AUTH_PARAM_ERROR, SPDY_AUTH_EXCEPTION, SEND_LOCAL_EXCEPTION, REQ_TIME_OUT, SPDY_CON_DISCONNECTED, INAPP_CON_DISCONNECTED, SPDY_PING_TIME_OUT, NO_NETWORK, APPKEY_NULL, APPSECRET_NULL, MESSAGE_QUEUE_FULL, SERVIER_LOW_LIMIT, SERVIER_HIGH_LIMIT, SERVIER_HIGH_LIMIT_BRUSH, DM_TAIR_ERROR, DM_DEVICEID_INVALID, DM_APPKEY_INVALID, DM_PACKAGENAME_INVALID, SERVER_UNKNOWN_ERROR, NETWORKSDK_SPDY_CLOSE_ERROR, NETWORKSDK_SPDY_RES_ERROR, ERROR_SHOULD_NEVER_HAPPEN, NETWORK_INAPP_ARGS_INVALID, NETWORK_INAPP_TIMEOUT, NETWORK_INAPP_CONNECT_FAIL, NETWORK_INAPP_NO_STRATEGY, NETWORK_INAPP_EXCEPTION, convertNetworkSdkError(0, "底层网络库信息").solution("小于-10000时，加上10000是底层网络库对应的错误码，请接口底层网络库错误码信息排查").build()};

    public static ErrorBuilder convertNetworkSdkError(int i, String str) {
        return define.defineSdkError(String.valueOf(i - 10000)).msg(str);
    }

    public static void printErrorCode() {
        StringBuilder sb = new StringBuilder();
        sb.append("ACCS错误码，一共" + codes.length + "个");
        sb.append('\n');
        sb.append(com.alibaba.sdk.android.error.ErrorCode.docTitle());
        sb.append(com.alibaba.sdk.android.error.ErrorCode.docContent(codes));
        Log.w("ACCS_ERROR_CODE", sb.toString());
    }

    public static com.alibaba.sdk.android.error.ErrorCode parseHttpCode(int i) {
        if (i == 102) {
            return DM_TAIR_ERROR;
        }
        if (i == 200) {
            return SUCCESS;
        }
        switch (i) {
            case 302:
                return DM_DEVICEID_INVALID;
            case 303:
                return DM_APPKEY_INVALID;
            case 304:
                return DM_PACKAGENAME_INVALID;
            default:
                return SERVER_UNKNOWN_ERROR.copy().detail("code:" + i).build();
        }
    }

    public static boolean isChannelError(int i) {
        return i == SPDY_CONNECTION_DISCONNECTED_WHEN_SEND_DATA.getCodeInt() || i == SPDY_AUTH_PARAM_ERROR.getCodeInt() || i == SPDY_AUTH_EXCEPTION.getCodeInt() || i == REQ_TIME_OUT.getCodeInt() || i == SPDY_CON_DISCONNECTED.getCodeInt() || i == INAPP_CON_DISCONNECTED.getCodeInt() || i == SPDY_PING_TIME_OUT.getCodeInt() || i == NETWORKSDK_SPDY_CLOSE_ERROR.getCodeInt() || i == NETWORK_INAPP_ARGS_INVALID.getCodeInt() || i == NETWORK_INAPP_TIMEOUT.getCodeInt() || i == NETWORK_INAPP_CONNECT_FAIL.getCodeInt() || i == NETWORK_INAPP_NO_STRATEGY.getCodeInt() || i == NETWORK_INAPP_EXCEPTION.getCodeInt();
    }

    public static String getAllDetails(String str) {
        return "[" + AccsState.getInstance().getState() + "][" + g.a().b() + "][" + GlobalAppRuntimeInfo.isAppBackground() + "]" + str;
    }

    public static String getExceptionInfo(Throwable th) {
        return th == null ? "throwable null" : addThrowableInfo(new StringBuilder(), th).toString();
    }

    private static StringBuilder addThrowableInfo(StringBuilder sb, Throwable th) {
        while (true) {
            sb.append(th.getMessage());
            sb.append('\t');
            StackTraceElement[] stackTrace = th.getStackTrace();
            if (stackTrace != null && stackTrace.length > 0) {
                sb.append(stackTrace[0].getClassName());
                sb.append(".");
                sb.append(stackTrace[0].getMethodName());
                sb.append('\t');
                for (int i = 1; i < stackTrace.length; i++) {
                    if (stackTrace[i].getClassName().startsWith("com.taobao") || stackTrace[i].getClassName().startsWith("com.aliyun") || stackTrace[i].getClassName().startsWith("org.android.agoo") || stackTrace[i].getClassName().startsWith("org.alibaba")) {
                        sb.append(stackTrace[i].getClassName());
                        sb.append(".");
                        sb.append(stackTrace[i].getMethodName());
                        sb.append("(");
                        sb.append(stackTrace[i].getFileName());
                        sb.append(":");
                        sb.append(stackTrace[i].getLineNumber());
                        sb.append(")");
                        sb.append('\t');
                        break;
                    }
                }
            }
            th = th.getCause();
            if (th == null) {
                return sb;
            }
            sb.append("caused by ");
            sb.append('\t');
        }
    }
}
