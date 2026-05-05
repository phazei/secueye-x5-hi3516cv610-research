package com.alibaba.sdk.android.push.f;

import android.content.Context;
import anetwork.channel.util.RequestConstant;
import com.alibaba.sdk.android.ams.common.logger.AmsLogger;
import com.alibaba.sdk.android.ams.common.util.StringUtil;
import com.alibaba.sdk.android.error.ErrorCode;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.f.f;
import com.alibaba.sdk.android.push.report.ReportManager;
import com.taobao.accs.common.Constants;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static AmsLogger f3104a = AmsLogger.getLogger("MPS:VipRequestManager");

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static g f3105b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Context f3106c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private f f3107d = new f();

    private g() {
    }

    public static g a() {
        if (f3105b == null) {
            f3105b = new g();
        }
        return f3105b;
    }

    private String a(int i) {
        f fVar = this.f3107d;
        f.a aVarA = fVar != null ? fVar.a(i) : null;
        if (aVarA == null) {
            return null;
        }
        return aVarA.a();
    }

    private Map<String, String> a(String str, String str2, String[] strArr, Map<String, String> map) throws com.alibaba.sdk.android.push.b.c {
        String str3;
        String string;
        String str4;
        if (str.equals("deviceId")) {
            string = e();
            if (StringUtil.isEmpty(string)) {
                throw new com.alibaba.sdk.android.push.b.c("deviceId is empty.");
            }
            str3 = "deviceId";
        } else {
            if (!str.equals("account")) {
                if (str.equals("alias")) {
                    if (StringUtil.isEmpty(str2)) {
                        throw new com.alibaba.sdk.android.push.b.c("alias is empty");
                    }
                    map.put("alias", str2);
                } else if (str.equals("tags")) {
                    if (strArr == null) {
                        throw new com.alibaba.sdk.android.push.b.c("tags array is empty");
                    }
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < strArr.length; i++) {
                        if (i != strArr.length - 1 && !StringUtil.isEmpty(strArr[i])) {
                            sb.append(strArr[i]);
                            str4 = ",";
                        } else if (i == strArr.length - 1 && !StringUtil.isEmpty(strArr[i])) {
                            str4 = strArr[i];
                        }
                        sb.append(str4);
                    }
                    if (StringUtil.isEmpty(sb.toString())) {
                        throw new com.alibaba.sdk.android.push.b.c("tags array is empty");
                    }
                    str3 = "tags";
                    string = sb.toString();
                }
                return map;
            }
            string = f();
            if (StringUtil.isEmpty(string)) {
                throw new com.alibaba.sdk.android.push.b.c("account is empty");
            }
            str3 = "account";
        }
        map.put(str3, string);
        return map;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(int i, String str) {
        f fVar = this.f3107d;
        if (fVar != null) {
            fVar.a(i, str);
        }
    }

    public static void a(Context context) {
        f3106c = context;
        if (f3105b == null) {
            f3105b = a();
        }
    }

    private void a(com.alibaba.sdk.android.push.b.c cVar, String str, CommonCallback commonCallback) {
        a((Throwable) cVar, str, commonCallback);
    }

    private void a(com.alibaba.sdk.android.push.b.d dVar, String str, CommonCallback commonCallback) {
        a((Throwable) dVar, str, commonCallback);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str) {
        com.alibaba.sdk.android.ams.common.b.c.a().a("mps_account", str);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, long j) {
        ReportManager reportManager = ReportManager.getInstance();
        com.alibaba.sdk.android.ams.common.b.b bVarA = com.alibaba.sdk.android.ams.common.b.c.a();
        if (reportManager == null || bVarA == null) {
            return;
        }
        reportManager.reportVipRequestTimeCost(str, bVarA.b(), j);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(String str, String str2, String str3) {
        ReportManager reportManager = ReportManager.getInstance();
        com.alibaba.sdk.android.ams.common.b.b bVarA = com.alibaba.sdk.android.ams.common.b.c.a();
        if (reportManager == null || bVarA == null) {
            return;
        }
        reportManager.reportErrorVipRequest(str, str2, bVarA.b(), str3);
    }

    private void a(Throwable th, String str, CommonCallback commonCallback) {
        ErrorCode errorCodeBuild = com.alibaba.sdk.android.push.common.a.d.q.copy().msg(th.getMessage()).build();
        f3104a.e(str + " Fail: errorCode:" + errorCodeBuild, th);
        if (commonCallback != null) {
            commonCallback.onFailed(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
        }
        a(errorCodeBuild.getCode(), errorCodeBuild.getMsg(), str);
    }

    private static boolean c(Context context) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(com.alibaba.sdk.android.push.common.util.b.a(context, "KEY_LAUNCH_MARK"));
        Calendar calendar2 = Calendar.getInstance();
        return calendar.get(6) == calendar2.get(6) && calendar.get(1) == calendar2.get(1);
    }

    private String e() {
        return com.alibaba.sdk.android.ams.common.b.c.a().b();
    }

    private String f() {
        return com.alibaba.sdk.android.ams.common.b.c.a().d("mps_account");
    }

    private Map<String, String> g() {
        String strB = b();
        HashMap map = new HashMap();
        map.put("appKey", strB);
        map.put(Constants.KEY_OS_VERSION, "2");
        map.put("version", "-SNAPSHOT");
        return map;
    }

    public void a(final int i, final CommonCallback commonCallback) {
        String strA;
        final long jCurrentTimeMillis = System.currentTimeMillis();
        f3104a.d("listTags");
        if (1 == i && (strA = a(2)) != null) {
            f3104a.d("get from cache");
            if (commonCallback != null) {
                commonCallback.onSuccess(strA);
                return;
            }
            return;
        }
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/list-tag", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.10
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str, String str2) {
                    g.this.a(str, str2, "/list-tag");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str, str2);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str) {
                    g.this.a("/list-tag", System.currentTimeMillis() - jCurrentTimeMillis);
                    if (1 == i) {
                        g.f3104a.d("store cache");
                        g.this.a(2, str);
                    }
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str);
                    }
                }
            });
            Map<String, String> mapG = g();
            if (i != 1) {
                throw new com.alibaba.sdk.android.push.b.d("target is invalid.");
            }
            Map<String, String> mapA = a("deviceId", (String) null, (String[]) null, mapG);
            mapA.put(Constants.KEY_TARGET, String.valueOf(i));
            mapA.put(com.alibaba.sdk.android.push.common.util.a.d.u, com.alibaba.sdk.android.push.common.util.a.d.LIST_TAGS.a() + "");
            hVar.execute(new Map[]{mapA});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/list-tag", commonCallback);
        } catch (com.alibaba.sdk.android.push.b.d e2) {
            a(e2, "/list-tag", commonCallback);
        }
    }

    public void a(int i, String[] strArr, String str, final CommonCallback commonCallback) {
        Map<String, String> mapA;
        String str2;
        String str3;
        final long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/bind-tag", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.8
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str4, String str5) {
                    g.this.a(str4, str5, "/bind-tag");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str4, str5);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str4) {
                    g.this.a("/bind-tag", System.currentTimeMillis() - jCurrentTimeMillis);
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str4);
                    }
                }
            });
            if (strArr == null || strArr.length == 0) {
                throw new com.alibaba.sdk.android.push.b.d("tags is empty.");
            }
            Map<String, String> mapG = g();
            switch (i) {
                case 1:
                    f3104a.d("Binding tag to device.");
                    mapA = a("deviceId", (String) null, (String[]) null, mapG);
                    str2 = com.alibaba.sdk.android.push.common.util.a.d.u;
                    str3 = com.alibaba.sdk.android.push.common.util.a.d.BIND_TAG_TO_DEVICE.a() + "";
                    break;
                case 2:
                    f3104a.d("Binding tag to account.");
                    mapA = a("account", (String) null, (String[]) null, mapG);
                    str2 = com.alibaba.sdk.android.push.common.util.a.d.u;
                    str3 = com.alibaba.sdk.android.push.common.util.a.d.BIND_TAG_TO_ACCOUNT.a() + "";
                    break;
                case 3:
                    mapA = a("alias", str, (String[]) null, mapG);
                    str2 = com.alibaba.sdk.android.push.common.util.a.d.u;
                    str3 = com.alibaba.sdk.android.push.common.util.a.d.BIND_TAG_TO_ALIAS.a() + "";
                    break;
                default:
                    throw new com.alibaba.sdk.android.push.b.d("target is invalid.");
            }
            mapA.put(str2, str3);
            Map<String, String> mapA2 = a("tags", (String) null, strArr, mapA);
            mapA2.put(Constants.KEY_TARGET, String.valueOf(i));
            hVar.execute(new Map[]{mapA2});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/bind-tag", commonCallback);
        } catch (com.alibaba.sdk.android.push.b.d e2) {
            a(e2, "/bind-tag", commonCallback);
        }
    }

    public void a(final CommonCallback commonCallback) {
        f3104a.d("unbinding account");
        final long jCurrentTimeMillis = System.currentTimeMillis();
        h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/unbind-account", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.7
            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onFailed(String str, String str2) {
                g.this.a(str, str2, "/unbind-account");
                CommonCallback commonCallback2 = commonCallback;
                if (commonCallback2 != null) {
                    commonCallback2.onFailed(str, str2);
                }
            }

            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onSuccess(String str) {
                g.this.a("");
                g.this.a("/unbind-account", System.currentTimeMillis() - jCurrentTimeMillis);
                CommonCallback commonCallback2 = commonCallback;
                if (commonCallback2 != null) {
                    commonCallback2.onSuccess(str);
                }
            }
        });
        try {
            Map<String, String> mapG = g();
            mapG.put("account", "");
            mapG.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.UNBIND_ACCOUNT.a()));
            hVar.execute(new Map[]{a("deviceId", (String) null, (String[]) null, mapG)});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/unbind-account", commonCallback);
        }
    }

    public void a(final String str, final CommonCallback commonCallback) {
        f3104a.d("binding account" + str);
        final long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/bind-account", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.1
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str2, String str3) {
                    g.this.a(str2, str3, "/bind-account");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str2, str3);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str2) {
                    g.this.a(str);
                    g.this.a("/bind-account", System.currentTimeMillis() - jCurrentTimeMillis);
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str2);
                    }
                }
            });
            Map<String, String> mapG = g();
            if (StringUtil.isEmpty(str)) {
                throw new com.alibaba.sdk.android.push.b.d("account input is empty!");
            }
            mapG.put("account", str);
            mapG.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.BIND_ACCOUNT.a()));
            hVar.execute(new Map[]{a("deviceId", (String) null, (String[]) null, mapG)});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/bind-account", commonCallback);
        } catch (com.alibaba.sdk.android.push.b.d e2) {
            a(e2, "/bind-account", commonCallback);
        }
    }

    public String b() {
        return com.alibaba.sdk.android.ams.common.b.c.a().a();
    }

    public void b(int i, String[] strArr, String str, final CommonCallback commonCallback) {
        Map<String, String> mapA;
        String str2;
        String str3;
        final long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/unbind-tag", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.9
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str4, String str5) {
                    g.this.a(str4, str5, "/unbind-tag");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str4, str5);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str4) {
                    if (commonCallback != null) {
                        g.this.a("/unbind-tag", System.currentTimeMillis() - jCurrentTimeMillis);
                        commonCallback.onSuccess(str4);
                    }
                }
            });
            Map<String, String> mapG = g();
            switch (i) {
                case 1:
                    f3104a.d("Unbinding tag from device.");
                    mapA = a("deviceId", (String) null, (String[]) null, mapG);
                    str2 = com.alibaba.sdk.android.push.common.util.a.d.u;
                    str3 = com.alibaba.sdk.android.push.common.util.a.d.UNBIND_TAG_TO_DEVICE.a() + "";
                    break;
                case 2:
                    f3104a.d("Unbinding tag from account.");
                    mapA = a("account", (String) null, (String[]) null, mapG);
                    str2 = com.alibaba.sdk.android.push.common.util.a.d.u;
                    str3 = com.alibaba.sdk.android.push.common.util.a.d.UNBIND_TAG_TO_ACCOUNT.a() + "";
                    break;
                case 3:
                    f3104a.d("Unbinding tag from alias.");
                    mapA = a("alias", str, (String[]) null, mapG);
                    str2 = com.alibaba.sdk.android.push.common.util.a.d.u;
                    str3 = com.alibaba.sdk.android.push.common.util.a.d.UNBIND_TAG_TO_ALIAS.a() + "";
                    break;
                default:
                    throw new com.alibaba.sdk.android.push.b.d("target is invalid.");
            }
            mapA.put(str2, str3);
            Map<String, String> mapA2 = a("tags", (String) null, strArr, mapA);
            mapA2.put(Constants.KEY_TARGET, String.valueOf(i));
            hVar.execute(new Map[]{mapA2});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/unbind-tag", commonCallback);
        } catch (com.alibaba.sdk.android.push.b.d e2) {
            a(e2, "/unbind-tag", commonCallback);
        }
    }

    public void b(Context context) {
        if (c(context)) {
            f3104a.e("onAppStart has already sent today");
            return;
        }
        f3104a.d("onAppStart");
        h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/active", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.6
            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onFailed(String str, String str2) {
                g.f3104a.e("onAppStart failed. errorCode:" + str + " errorMsg:" + str2);
            }

            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onSuccess(String str) {
                g.f3104a.d("onAppStart success");
                try {
                    com.alibaba.sdk.android.push.common.util.b.a(g.f3106c, "KEY_LAUNCH_MARK", System.currentTimeMillis());
                } catch (Throwable th) {
                    g.f3104a.e("onAppStart success", th);
                }
            }
        });
        try {
            Map<String, String> mapG = g();
            mapG.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.ON_APP_START.a()));
            hVar.execute(new Map[]{a("deviceId", (String) null, (String[]) null, mapG)});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/active", (CommonCallback) null);
        }
    }

    public void b(final CommonCallback commonCallback) {
        final long jCurrentTimeMillis = System.currentTimeMillis();
        f3104a.d("listAliases");
        String strA = a(1);
        if (strA != null) {
            f3104a.d("get from cache");
            if (commonCallback != null) {
                commonCallback.onSuccess(strA);
                return;
            }
            return;
        }
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/list-alias", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.13
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str, String str2) {
                    g.this.a(str, str2, "/list-alias");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str, str2);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str) {
                    g.f3104a.d("store cache");
                    g.this.a(1, str);
                    g.this.a("/list-alias", System.currentTimeMillis() - jCurrentTimeMillis);
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str);
                    }
                }
            });
            Map<String, String> mapG = g();
            mapG.put(com.alibaba.sdk.android.push.common.util.a.d.u, com.alibaba.sdk.android.push.common.util.a.d.LIST_ALIASES.a() + "");
            hVar.execute(new Map[]{a("deviceId", (String) null, (String[]) null, mapG)});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/list-alias", commonCallback);
        }
    }

    public void b(String str, final CommonCallback commonCallback) {
        final long jCurrentTimeMillis = System.currentTimeMillis();
        f3104a.d("Adding alias to device");
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/add-alias", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.11
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str2, String str3) {
                    g.this.a(str2, str3, "/add-alias");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str2, str3);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str2) {
                    g.this.a("/add-alias", System.currentTimeMillis() - jCurrentTimeMillis);
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str2);
                    }
                }
            });
            Map<String, String> mapA = a("alias", str, (String[]) null, a("deviceId", (String) null, (String[]) null, g()));
            mapA.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.BIND_ALIAS.a()));
            hVar.execute(new Map[]{mapA});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/add-alias", commonCallback);
        }
    }

    public void c(final CommonCallback commonCallback) {
        f3104a.d("check vip push status");
        final long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/push-status", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.14
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str, String str2) {
                    g.f3104a.d("fail to check vip push");
                    g.this.a(str, str2, "/push-status");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str, str2);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str) {
                    g.f3104a.d("check vip push successfully");
                    g.this.a("/push-status", System.currentTimeMillis() - jCurrentTimeMillis);
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str);
                    }
                }
            });
            Map<String, String> mapA = a("deviceId", (String) null, (String[]) null, g());
            mapA.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.CHECK_PUSH_STATUS.a()));
            hVar.execute(new Map[]{mapA});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/push-status", commonCallback);
        }
    }

    public void c(String str, final CommonCallback commonCallback) {
        final long jCurrentTimeMillis = System.currentTimeMillis();
        f3104a.d("Removing alias from device");
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/remove-alias", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.12
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str2, String str3) {
                    g.this.a(str2, str3, "/remove-alias");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str2, str3);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str2) {
                    g.this.a("/remove-alias", System.currentTimeMillis() - jCurrentTimeMillis);
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str2);
                    }
                }
            });
            Map<String, String> mapA = a("deviceId", (String) null, (String[]) null, g());
            if (StringUtil.isEmpty(str)) {
                str = "";
            }
            mapA.put("alias", str);
            mapA.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.UNBIND_ALIAS.a()));
            hVar.execute(new Map[]{mapA});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/remove-alias", commonCallback);
        }
    }

    public void d(final CommonCallback commonCallback) {
        final long jCurrentTimeMillis = System.currentTimeMillis();
        f3104a.d("unbinding vip");
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/push-switch", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.2
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str, String str2) {
                    g.f3104a.d("unbindVip fail");
                    g.this.a(str, str2, "/push-switch");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str, str2);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str) {
                    g.f3104a.d("unbindVip success");
                    g.this.a("/push-switch", System.currentTimeMillis() - jCurrentTimeMillis);
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str);
                    }
                }
            });
            Map<String, String> mapA = a("deviceId", (String) null, (String[]) null, g());
            mapA.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.TURN_OFF_PUSH.a()));
            mapA.put("enable", RequestConstant.FALSE);
            hVar.execute(new Map[]{mapA});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/push-switch false", commonCallback);
        }
    }

    public void d(String str, final CommonCallback commonCallback) {
        f3104a.d("binding phoneNumber:" + str);
        final long jCurrentTimeMillis = System.currentTimeMillis();
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/set-phone", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.4
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str2, String str3) {
                    g.this.a(str2, str3, "/set-phone");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str2, str3);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str2) {
                    g.this.a("/set-phone", System.currentTimeMillis() - jCurrentTimeMillis);
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str2);
                    }
                }
            });
            Map<String, String> mapG = g();
            if (StringUtil.isEmpty(str)) {
                throw new com.alibaba.sdk.android.push.b.d("account input is empty!");
            }
            mapG.put("mob", str);
            mapG.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.BIND_PHONE_NUMBER.a()));
            hVar.execute(new Map[]{a("deviceId", (String) null, (String[]) null, mapG)});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/set-phone", commonCallback);
        } catch (com.alibaba.sdk.android.push.b.d e2) {
            a(e2, "/set-phone", commonCallback);
        }
    }

    public void e(final CommonCallback commonCallback) {
        final long jCurrentTimeMillis = System.currentTimeMillis();
        f3104a.d("binding vip push");
        try {
            h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/push-switch", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.3
                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onFailed(String str, String str2) {
                    g.this.a(str, str2, "/push-switch");
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onFailed(str, str2);
                    }
                }

                @Override // com.alibaba.sdk.android.push.CommonCallback
                public void onSuccess(String str) {
                    g.this.a("/push-switch", System.currentTimeMillis() - jCurrentTimeMillis);
                    CommonCallback commonCallback2 = commonCallback;
                    if (commonCallback2 != null) {
                        commonCallback2.onSuccess(str);
                    }
                }
            });
            Map<String, String> mapA = a("deviceId", (String) null, (String[]) null, g());
            mapA.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.TURN_ON_PUSH.a()));
            mapA.put("enable", "true");
            hVar.execute(new Map[]{mapA});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/push-switch true", commonCallback);
        }
    }

    public void f(final CommonCallback commonCallback) {
        final long jCurrentTimeMillis = System.currentTimeMillis();
        f3104a.d("unbinding phone number");
        h hVar = new h(f3106c, "https://" + com.alibaba.sdk.android.ams.common.a.a.e() + "/unset-phone", new CommonCallback() { // from class: com.alibaba.sdk.android.push.f.g.5
            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onFailed(String str, String str2) {
                g.this.a(str, str2, "/unset-phone");
                CommonCallback commonCallback2 = commonCallback;
                if (commonCallback2 != null) {
                    commonCallback2.onFailed(str, str2);
                }
            }

            @Override // com.alibaba.sdk.android.push.CommonCallback
            public void onSuccess(String str) {
                g.this.a("/unset-phone", System.currentTimeMillis() - jCurrentTimeMillis);
                CommonCallback commonCallback2 = commonCallback;
                if (commonCallback2 != null) {
                    commonCallback2.onSuccess(str);
                }
            }
        });
        try {
            Map<String, String> mapG = g();
            mapG.put(com.alibaba.sdk.android.push.common.util.a.d.u, String.valueOf(com.alibaba.sdk.android.push.common.util.a.d.UNBIND_PHONE_NUMBER.a()));
            hVar.execute(new Map[]{a("deviceId", (String) null, (String[]) null, mapG)});
        } catch (com.alibaba.sdk.android.push.b.c e) {
            a(e, "/unset-phone", commonCallback);
        }
    }
}
