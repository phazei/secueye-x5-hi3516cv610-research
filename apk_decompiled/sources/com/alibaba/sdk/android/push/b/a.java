package com.alibaba.sdk.android.push.b;

import android.content.Context;
import android.graphics.Bitmap;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.f.g;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.taobao.agoo.ICallback;
import com.taobao.agoo.TaobaoRegister;
import com.ut.device.UTDevice;
import com.xiaomi.mipush.sdk.Constants;

/* JADX INFO: loaded from: classes.dex */
public class a {

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static AmsLogger f2937b = AmsLogger.getLogger("MPS:CloudPushService");

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f2938a;

    /* JADX INFO: renamed from: com.alibaba.sdk.android.push.b.a$2, reason: invalid class name */
    class AnonymousClass2 implements CommonCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ CommonCallback f2941a;

        /* JADX INFO: renamed from: com.alibaba.sdk.android.push.b.a$2$1, reason: invalid class name */
        class AnonymousClass1 extends ICallback {
            AnonymousClass1() {
            }

            @Override // com.taobao.agoo.ICallback
            public void onFailure(String str, String str2) {
                a.f2937b.d("unbindAgoo fail");
                ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.a(str, str2).detail("turnOffPushChannel unbindAgoo").build();
                if (AnonymousClass2.this.f2941a != null) {
                    AnonymousClass2.this.f2941a.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
                }
            }

            @Override // com.taobao.agoo.ICallback
            public void onSuccess() {
                g.a().d(new CommonCallback() { // from class: com.alibaba.sdk.android.push.b.a.2.1.1
                    @Override // com.alibaba.sdk.android.push.CommonCallback
                    public void onFailed(final String str, final String str2) {
                        TaobaoRegister.bindAgoo(a.this.f2938a, new ICallback() { // from class: com.alibaba.sdk.android.push.b.a.2.1.1.1
                            @Override // com.taobao.agoo.ICallback
                            public void onFailure(String str3, String str4) {
                                ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.a(str3, str4).detail("turnOffPushChannel bindAgoo").build();
                                if (AnonymousClass2.this.f2941a != null) {
                                    AnonymousClass2.this.f2941a.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
                                }
                            }

                            @Override // com.taobao.agoo.ICallback
                            public void onSuccess() {
                                if (AnonymousClass2.this.f2941a != null) {
                                    AnonymousClass2.this.f2941a.onFailed(str, str2);
                                }
                            }
                        });
                    }

                    @Override // com.alibaba.sdk.android.push.CommonCallback
                    public void onSuccess(String str) {
                        if (AnonymousClass2.this.f2941a != null) {
                            AnonymousClass2.this.f2941a.onSuccess(str);
                        }
                    }
                });
            }
        }

        AnonymousClass2(CommonCallback commonCallback) {
            this.f2941a = commonCallback;
        }

        @Override // com.alibaba.sdk.android.push.CommonCallback
        public void onFailed(String str, String str2) {
            CommonCallback commonCallback = this.f2941a;
            if (commonCallback != null) {
                commonCallback.onFailed(str, str2);
            }
        }

        @Override // com.alibaba.sdk.android.push.CommonCallback
        public void onSuccess(String str) {
            if (!str.equals("off")) {
                TaobaoRegister.unbindAgoo(a.this.f2938a, new AnonymousClass1());
                return;
            }
            a.f2937b.d("already off. return");
            CommonCallback commonCallback = this.f2941a;
            if (commonCallback != null) {
                commonCallback.onSuccess(str);
            }
        }
    }

    /* JADX INFO: renamed from: com.alibaba.sdk.android.push.b.a$3, reason: invalid class name */
    class AnonymousClass3 implements CommonCallback {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        final /* synthetic */ CommonCallback f2948a;

        /* JADX INFO: renamed from: com.alibaba.sdk.android.push.b.a$3$1, reason: invalid class name */
        class AnonymousClass1 extends ICallback {
            AnonymousClass1() {
            }

            @Override // com.taobao.agoo.ICallback
            public void onFailure(String str, String str2) {
                ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.a(str, str2).detail("turnOnPushChannel bindAgoo").build();
                if (AnonymousClass3.this.f2948a != null) {
                    AnonymousClass3.this.f2948a.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
                }
            }

            @Override // com.taobao.agoo.ICallback
            public void onSuccess() {
                g.a().e(new CommonCallback() { // from class: com.alibaba.sdk.android.push.b.a.3.1.1
                    @Override // com.alibaba.sdk.android.push.CommonCallback
                    public void onFailed(final String str, final String str2) {
                        TaobaoRegister.unbindAgoo(a.this.f2938a, new ICallback() { // from class: com.alibaba.sdk.android.push.b.a.3.1.1.1
                            @Override // com.taobao.agoo.ICallback
                            public void onFailure(String str3, String str4) {
                                ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.a(str3, str4).detail("turnOnPushChannel unbindAgoo").build();
                                if (AnonymousClass3.this.f2948a != null) {
                                    AnonymousClass3.this.f2948a.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
                                }
                            }

                            @Override // com.taobao.agoo.ICallback
                            public void onSuccess() {
                                if (AnonymousClass3.this.f2948a != null) {
                                    AnonymousClass3.this.f2948a.onFailed(str, str2);
                                }
                            }
                        });
                    }

                    @Override // com.alibaba.sdk.android.push.CommonCallback
                    public void onSuccess(String str) {
                        if (AnonymousClass3.this.f2948a != null) {
                            AnonymousClass3.this.f2948a.onSuccess(str);
                        }
                    }
                });
            }
        }

        AnonymousClass3(CommonCallback commonCallback) {
            this.f2948a = commonCallback;
        }

        @Override // com.alibaba.sdk.android.push.CommonCallback
        public void onFailed(String str, String str2) {
            CommonCallback commonCallback = this.f2948a;
            if (commonCallback != null) {
                commonCallback.onFailed(str, str2);
            }
        }

        @Override // com.alibaba.sdk.android.push.CommonCallback
        public void onSuccess(String str) {
            TaobaoRegister.bindAgoo(a.this.f2938a, new AnonymousClass1());
        }
    }

    public a(Context context) {
        this.f2938a = context;
        g.a(context);
    }

    public String a() {
        return com.alibaba.sdk.android.ams.common.b.c.a().b();
    }

    public String a(Context context) {
        return UTDevice.getUtdid(context);
    }

    public void a(int i) {
        com.alibaba.sdk.android.push.common.a.c.a(i);
    }

    public void a(int i, int i2, int i3, int i4, final CommonCallback commonCallback) {
        f2937b.d("setDoNotDisturb " + i + ":" + i2 + Constants.ACCEPT_TIME_SEPARATOR_SERVER + i3 + ":" + i4);
        TaobaoRegister.setDoNotDisturb(i, i2, i3, i4, new com.aliyun.ams.emas.push.CommonCallback() { // from class: com.alibaba.sdk.android.push.b.a.1
            @Override // com.aliyun.ams.emas.push.CommonCallback
            public void onFailed(String str, String str2) {
                ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.a(str, str2).build();
                commonCallback.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
            }

            @Override // com.aliyun.ams.emas.push.CommonCallback
            public void onSuccess(String str) {
                commonCallback.onSuccess(str);
            }
        });
    }

    public void a(int i, CommonCallback commonCallback) {
        g.a().a(i, commonCallback);
    }

    public void a(int i, String[] strArr, String str, CommonCallback commonCallback) {
        g.a().a(i, strArr, str, commonCallback);
    }

    public void a(Bitmap bitmap) {
        com.alibaba.sdk.android.push.common.a.c.a(bitmap);
    }

    public void a(CommonCallback commonCallback) {
        g.a().a(commonCallback);
    }

    public void a(CPushMessage cPushMessage) {
        TaobaoRegister.clickMessage(CPushMessage.to(cPushMessage));
    }

    public void a(Class cls) {
        TaobaoRegister.setPushMsgReceiveService(cls);
    }

    public void a(String str) {
        com.alibaba.sdk.android.push.common.a.c.a(str);
    }

    public void a(String str, CommonCallback commonCallback) {
        g.a().a(str, commonCallback);
    }

    public void a(boolean z) {
        TaobaoRegister.setDoNotDisturbMode(z);
    }

    public void b() {
        TaobaoRegister.clearNotificationCreatedByAliyun(this.f2938a);
    }

    public void b(int i, String[] strArr, String str, CommonCallback commonCallback) {
        g.a().b(i, strArr, str, commonCallback);
    }

    public void b(Context context) {
        g.a().b(context);
    }

    public void b(CommonCallback commonCallback) {
        g.a().b(commonCallback);
    }

    public void b(CPushMessage cPushMessage) {
        TaobaoRegister.dismissMessage(CPushMessage.to(cPushMessage));
    }

    public void b(String str) {
        com.alibaba.sdk.android.ams.common.b.c.a().e(str);
    }

    public void b(String str, CommonCallback commonCallback) {
        g.a().b(str, commonCallback);
    }

    public void b(boolean z) {
        com.alibaba.sdk.android.push.common.a.c.a(z);
    }

    public void c(CommonCallback commonCallback) {
        g.a().f(commonCallback);
    }

    public void c(String str) {
        com.alibaba.sdk.android.ams.common.b.c.a().f(str);
    }

    public void c(String str, CommonCallback commonCallback) {
        g.a().c(str, commonCallback);
    }

    public void d(CommonCallback commonCallback) {
        f(new AnonymousClass2(commonCallback));
    }

    public void d(String str) {
        com.alibaba.sdk.android.ams.common.a.b.e(str);
    }

    public void d(String str, CommonCallback commonCallback) {
        g.a().d(str, commonCallback);
    }

    public void e(CommonCallback commonCallback) {
        f(new AnonymousClass3(commonCallback));
    }

    public void f(CommonCallback commonCallback) {
        g.a().c(commonCallback);
    }
}
