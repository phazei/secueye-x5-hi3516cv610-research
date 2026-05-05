package com.alibaba.sdk.android.push.common.a;

import com.alibaba.sdk.android.error.CodeGenerator;
import com.alibaba.sdk.android.error.ErrorBuilder;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.error.ErrorDefine;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;

/* JADX INFO: loaded from: classes.dex */
public class d {
    private static final ErrorDefine y = new ErrorDefine("PUSH", new a());

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final ErrorCode f3049a = y.defineSdkError("00000").msg("success").build();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final ErrorCode f3050b = y.defineServerError("10101").msg("参数缺失").solution("请检查请求参数是否正确").build();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public static final ErrorCode f3051c = y.defineServerError("10102").msg("参数无效").solution("请检查请求参数是否正确").build();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public static final ErrorCode f3052d = y.defineServerError("10103").msg("服务端签名与客户端不匹配").solution("请检查推送配置是否正确").build();
    public static final ErrorCode e = y.defineServerError("10104").msg("Tag相关错误").solution("请根据具体错误信息排查，如果不能解决，请联系阿里云技术支持").build();
    public static final ErrorCode f = y.defineServerError("10105").msg("Alias相关错误").solution("请根据具体错误信息排查，如果不能解决，请联系阿里云技术支持").build();
    public static final ErrorCode g = y.defineServerError("10106").msg("服务端内部错误").solution("请根据具体错误信息联系阿里云技术支持").build();
    public static final ErrorCode h = y.defineAndroidError("10107").msg("网络IO错误").solution("请检查网络是否可用").solution("请根据具体错误信息排查，如果不能解决，请联系阿里云技术支持").build();
    public static final ErrorCode i = y.defineSdkError("10108").msg("返回结果解析错误").solution("请保留具体错误信息，联系阿里云技术支持排查").build();
    public static final ErrorCode j = y.defineSdkError("10109").msg("网络连接失败,请检查网络配置").solution("请检查网络是否可用").build();
    public static final ErrorCode k = y.defineSdkError("10114").msg("内部错误").solution("请保留具体错误信息，联系阿里云技术支持排查").build();
    public static final ErrorCode l = y.defineSdkError("10115").msg("通道注册状态异常").solution("请保留具体错误信息，联系阿里云技术支持排查").build();
    public static final ErrorCode m = y.defineServerError("10118").msg("其它接口错误").solution("请根据具体错误信息联系阿里云技术支持").build();
    public static final ErrorCode n = y.defineSdkError("10119").msg("非主进程不用初始化").solution("在非主进程执行初始化时触发，可以忽略").build();
    public static final ErrorCode o = y.defineSdkError("10120").msg("推送注册超时").solution("请保留具体错误信息，联系阿里云技术支持排查").build();
    public static final ErrorCode p = y.defineAndroidError("10121").msg("网络请求失败，请检查网络是否可用").solution("请检查网络是否可用").solution("请根据具体错误信息排查，如果不能解决，请联系阿里云技术支持").build();
    public static final ErrorCode q = y.defineSdkError("20101").msg("参数输入非法").solution("请检查请求的输入参数是否正确").build();
    public static final ErrorCode r = y.defineSdkError("20103").msg("appversion参数错误,请检查您的版本号,版本号不能为null或长度不能超过32位").solution("开启debug会检查此错误，请检查应用版本号是否过长").build();
    public static final ErrorCode s = y.defineSdkError("20106").msg("核心组件未配置").solution("开启debug会检查此错误，请检查是否删除了推送组件的声明").build();
    public static final ErrorCode t = y.defineSdkError("20107").msg("连续crash，推送服务关闭").solution("应用初始化推送后崩溃，会在下次启动关闭推送服务。请检查应用的崩溃记录").solution("开发测试场景下，人为触发的，请清除应用数据恢复").solution("线上场景会尝试自动恢复，如果仍然崩溃，需要升级应用版本才会恢复").build();
    public static final ErrorCode u = y.defineSdkError("20108").msg("未初始化，请先调用 PushServiceFactory的init方法").solution("请确认是否正常初始化").build();
    public static final ErrorCode v = y.defineSdkError("20109").msg("废弃接口").solution("请查看文档，使用合适的api").build();
    public static final ErrorCode w = y.defineSdkError("20110").msg("已经调用注册，重复调用无效").solution("register方法如果失败了，会自动重试，一般情况下不需要重复调用").solution("如果希望内部重试失败的情况，由外部重新调用register，请至少在上一次register失败回调两次（确认内部重试还是失败）的情况下，先调用PushControlService的reset方法，然后再调用下一次register方法").build();
    public static final ErrorCode[] x = {f3049a, f3050b, f3051c, f3052d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, a(123, "accs错误信息").solution("格式ACCS_123, 123为accs错误码，请结合accs错误码排查").build(), a("xxx", "agoo错误信息").solution("格式AGOO_xxx, xxx为agoo错误码，请结合agoo错误码排查").build()};

    private static class a extends CodeGenerator {
        private a() {
        }

        @Override // com.alibaba.sdk.android.error.CodeGenerator
        public String generateCodeStr(String str, String str2, String str3) {
            return str + OpenAccountUIConstants.UNDER_LINE + str3;
        }
    }

    public static ErrorBuilder a(int i2, String str) {
        return y.defineSdkError("ACCS_" + i2).msg(str);
    }

    public static ErrorBuilder a(String str, String str2) {
        return y.defineSdkError(str).msg(str2);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:26:0x004e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static com.alibaba.sdk.android.error.ErrorCode b(java.lang.String r2, java.lang.String r3) {
        /*
            int r0 = r2.hashCode()
            switch(r0) {
                case -1693386453: goto L44;
                case -996611353: goto L3a;
                case -723241298: goto L30;
                case -265907281: goto L26;
                case 2524: goto L1c;
                case 39557560: goto L12;
                case 677129462: goto L8;
                default: goto L7;
            }
        L7:
            goto L4e
        L8:
            java.lang.String r0 = "InvalidParam"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L4e
            r0 = 2
            goto L4f
        L12:
            java.lang.String r0 = "AliasError"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L4e
            r0 = 5
            goto L4f
        L1c:
            java.lang.String r0 = "OK"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L4e
            r0 = 0
            goto L4f
        L26:
            java.lang.String r0 = "SignNotMatch"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L4e
            r0 = 3
            goto L4f
        L30:
            java.lang.String r0 = "TagError"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L4e
            r0 = 4
            goto L4f
        L3a:
            java.lang.String r0 = "MissingParam"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L4e
            r0 = 1
            goto L4f
        L44:
            java.lang.String r0 = "InternalError"
            boolean r0 = r2.equals(r0)
            if (r0 == 0) goto L4e
            r0 = 6
            goto L4f
        L4e:
            r0 = -1
        L4f:
            switch(r0) {
                case 0: goto L8f;
                case 1: goto L8c;
                case 2: goto L89;
                case 3: goto L86;
                case 4: goto L83;
                case 5: goto L80;
                case 6: goto L75;
                default: goto L52;
            }
        L52:
            com.alibaba.sdk.android.error.ErrorCode r0 = com.alibaba.sdk.android.push.common.a.d.m
            com.alibaba.sdk.android.error.ErrorBuilder r0 = r0.copy()
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r2)
            java.lang.String r2 = ":"
            r1.append(r2)
            r1.append(r3)
            java.lang.String r2 = r1.toString()
            com.alibaba.sdk.android.error.ErrorBuilder r2 = r0.msg(r2)
        L70:
            com.alibaba.sdk.android.error.ErrorCode r2 = r2.build()
            return r2
        L75:
            com.alibaba.sdk.android.error.ErrorCode r2 = com.alibaba.sdk.android.push.common.a.d.g
        L77:
            com.alibaba.sdk.android.error.ErrorBuilder r2 = r2.copy()
            com.alibaba.sdk.android.error.ErrorBuilder r2 = r2.msg(r3)
            goto L70
        L80:
            com.alibaba.sdk.android.error.ErrorCode r2 = com.alibaba.sdk.android.push.common.a.d.f
            goto L77
        L83:
            com.alibaba.sdk.android.error.ErrorCode r2 = com.alibaba.sdk.android.push.common.a.d.e
            goto L77
        L86:
            com.alibaba.sdk.android.error.ErrorCode r2 = com.alibaba.sdk.android.push.common.a.d.f3052d
            goto L77
        L89:
            com.alibaba.sdk.android.error.ErrorCode r2 = com.alibaba.sdk.android.push.common.a.d.f3051c
            goto L77
        L8c:
            com.alibaba.sdk.android.error.ErrorCode r2 = com.alibaba.sdk.android.push.common.a.d.f3050b
            goto L77
        L8f:
            com.alibaba.sdk.android.error.ErrorCode r2 = com.alibaba.sdk.android.push.common.a.d.f3049a
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.sdk.android.push.common.a.d.b(java.lang.String, java.lang.String):com.alibaba.sdk.android.error.ErrorCode");
    }
}
