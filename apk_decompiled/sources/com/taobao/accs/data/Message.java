package com.taobao.accs.data;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.huawei.hms.support.hianalytics.HiAnalyticsConstant;
import com.taobao.accs.ACCSManager;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.ut.monitor.NetPerformanceMonitor;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.AdapterUtilityImpl;
import com.taobao.accs.utl.JsonUtility;
import com.taobao.accs.utl.RomInfoCollecter;
import com.taobao.accs.utl.UtilityImpl;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class Message {
    public static final int EXT_HEADER_VALUE_MAX_LEN = 1023;
    public static final int FLAG_ACK_TYPE = 32;
    public static final int FLAG_BIZ_RET = 64;
    public static final int FLAG_DATA_TYPE = 32768;
    public static final int FLAG_ERR = 4096;
    public static final int FLAG_REQ_BIT1 = 16384;
    public static final int FLAG_REQ_BIT2 = 8192;
    public static final int FLAG_RET = 2048;
    public static final String KEY_BINDAPP = "ctrl_bindapp";
    public static final String KEY_BINDSERVICE = "ctrl_bindservice";
    public static final String KEY_BINDUSER = "ctrl_binduser";
    public static final String KEY_UNBINDAPP = "ctrl_unbindapp";
    public static final String KEY_UNBINDSERVICE = "ctrl_unbindservice";
    public static final String KEY_UNBINDUSER = "ctrl_unbinduser";
    public static final int MAX_RETRY_TIMES = 3;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static int f6297a = 5;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    static long f6298b = 1;
    byte[] N;
    public String O;
    int P;
    public long T;
    long U;
    transient NetPerformanceMonitor W;
    a Y;
    public URL f;
    short i;
    short j;
    short k;
    byte l;
    byte m;
    String n;
    String o;
    public String q;
    Map<Integer, String> r;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public boolean f6299c = false;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public boolean f6300d = false;
    public boolean e = false;
    byte g = 0;
    byte h = 0;
    int p = -1;
    String s = null;
    public Integer t = null;
    Integer u = 0;
    String v = null;
    public String w = null;
    Integer x = null;
    String y = null;
    String z = null;
    String A = null;
    String B = null;
    String C = null;
    Integer D = null;
    String E = null;
    String F = null;
    public String G = null;
    public String H = null;
    String I = null;
    String J = null;
    String K = null;
    String L = null;
    String M = null;
    public long Q = 0;
    public int R = 0;
    public int S = com.taobao.accs.net.b.ACCS_RECEIVE_TIMEOUT;
    public String V = null;
    String X = null;

    /* JADX INFO: compiled from: Taobao */
    public static class b {
        public static final int INVALID = -1;
        public static final int NEED_ACK = 1;
        public static final int NO_ACK = 0;

        public static int a(int i) {
            switch (i) {
            }
            return 1;
        }

        public static String b(int i) {
            switch (i) {
                case 0:
                    return "NO_ACK";
                case 1:
                    return "NEED_ACK";
                default:
                    return "INVALID";
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class c {
        public static final int CONTROL = 0;
        public static final int DATA = 1;
        public static final int HANDSHAKE = 3;
        public static final int INVALID = -1;
        public static final int PING = 2;

        public static int a(int i) {
            switch (i) {
            }
            return 0;
        }

        public static String b(int i) {
            switch (i) {
                case 0:
                    return "CONTROL";
                case 1:
                    return "DATA";
                case 2:
                    return "PING";
                case 3:
                    return "HANDSHAKE";
                default:
                    return "INVALID";
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public enum ReqType {
        DATA,
        ACK,
        REQ,
        RES;

        public static ReqType valueOf(int i) {
            switch (i) {
                case 0:
                    return DATA;
                case 1:
                    return ACK;
                case 2:
                    return REQ;
                case 3:
                    return RES;
                default:
                    return DATA;
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f6301a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private String f6302b;

        public a(int i, String str) {
            this.f6301a = i;
            this.f6302b = str;
        }

        public int a() {
            return this.f6301a;
        }

        public String b() {
            return this.f6302b;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            a aVar = (a) obj;
            return this.f6301a == aVar.a() || this.f6302b.equals(aVar.b());
        }

        public int hashCode() {
            return this.f6302b.hashCode();
        }
    }

    private Message() {
        synchronized (Message.class) {
            this.T = System.currentTimeMillis();
            this.q = String.valueOf(this.T) + "." + String.valueOf(f6298b);
            long j = f6298b;
            f6298b = 1 + j;
            this.Y = new a((int) j, this.q);
        }
    }

    public int a() {
        return this.p;
    }

    public String b() {
        return this.q;
    }

    public boolean c() {
        return Constants.TARGET_CONTROL.equals(this.n);
    }

    public a d() {
        return this.Y;
    }

    public void a(long j) {
        this.U = j;
    }

    public NetPerformanceMonitor e() {
        return this.W;
    }

    private String j() {
        return "Msg" + OpenAccountUIConstants.UNDER_LINE + this.X;
    }

    public String f() {
        String str = this.s;
        return str == null ? "" : str;
    }

    public boolean g() {
        boolean z = (System.currentTimeMillis() - this.T) + this.Q >= ((long) this.S);
        if (z) {
            ALog.e(j(), "delay time:" + this.Q + " beforeSendTime:" + (System.currentTimeMillis() - this.T) + " timeout" + this.S, new Object[0]);
        }
        return z;
    }

    public byte[] a(Context context, int i) throws Throwable {
        byte[] bytes;
        try {
            i();
        } catch (UnsupportedEncodingException e) {
            ALog.e(j(), "build2", e, new Object[0]);
        } catch (JSONException e2) {
            ALog.e(j(), "build1", e2, new Object[0]);
        }
        byte[] bArr = this.N;
        String str = bArr != null ? new String(bArr) : "";
        h();
        if (!this.f6299c) {
            StringBuilder sb = new StringBuilder();
            sb.append(UtilityImpl.getDeviceId(context));
            sb.append(HiAnalyticsConstant.REPORT_VAL_SEPARATOR);
            sb.append(this.s);
            sb.append(HiAnalyticsConstant.REPORT_VAL_SEPARATOR);
            String str2 = this.H;
            if (str2 == null) {
                str2 = "";
            }
            sb.append(str2);
            sb.append(HiAnalyticsConstant.REPORT_VAL_SEPARATOR);
            String str3 = this.G;
            if (str3 == null) {
                str3 = "";
            }
            sb.append(str3);
            this.o = sb.toString();
        }
        try {
            bytes = (this.q + "").getBytes("utf-8");
            this.m = (byte) this.o.getBytes("utf-8").length;
            this.l = (byte) this.n.getBytes("utf-8").length;
        } catch (Exception e3) {
            e3.printStackTrace();
            ALog.e(j(), "build3", e3, new Object[0]);
            bytes = (this.q + "").getBytes();
            this.m = (byte) this.o.getBytes().length;
            this.l = (byte) this.n.getBytes().length;
        }
        short sA = a(this.r);
        int length = this.l + 3 + 1 + this.m + 1 + bytes.length;
        byte[] bArr2 = this.N;
        this.j = (short) (length + (bArr2 == null ? 0 : bArr2.length) + sA + 2);
        this.i = (short) (this.j + 2);
        com.taobao.accs.utl.e eVar = new com.taobao.accs.utl.e(this.i + 2 + 4);
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(j(), "Build Message", Constants.KEY_DATA_ID, new String(bytes));
        }
        try {
            eVar.a((byte) (this.g | 32));
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\tversion:2 compress:" + ((int) this.g), new Object[0]);
            }
            if (i == 0) {
                eVar.a((byte) -128);
                if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d(j(), "\tflag: 0x80", new Object[0]);
                }
            } else {
                eVar.a(Constants.CMD_TYPE.CMD_DEV_LOG_NOTIFY);
                if (ALog.isPrintLog(ALog.Level.D)) {
                    ALog.d(j(), "\tflag: 0x40", new Object[0]);
                }
            }
            eVar.a(this.i);
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\ttotalLength:" + ((int) this.i), new Object[0]);
            }
            eVar.a(this.j);
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\tdataLength:" + ((int) this.j), new Object[0]);
            }
            eVar.a(this.k);
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\tflags:" + Integer.toHexString(this.k), new Object[0]);
            }
            eVar.a(this.l);
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\ttargetLength:" + ((int) this.l), new Object[0]);
            }
            eVar.write(this.n.getBytes("utf-8"));
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\ttarget:" + this.n, new Object[0]);
            }
            eVar.a(this.m);
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\tsourceLength:" + ((int) this.m), new Object[0]);
            }
            eVar.write(this.o.getBytes("utf-8"));
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\tsource:" + this.o, new Object[0]);
            }
            eVar.a((byte) bytes.length);
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\tdataIdLength:" + bytes.length, new Object[0]);
            }
            eVar.write(bytes);
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\tdataId:" + new String(bytes), new Object[0]);
            }
            eVar.a(sA);
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\textHeader len:" + ((int) sA), new Object[0]);
            }
            if (this.r != null) {
                Iterator<Integer> it = this.r.keySet().iterator();
                while (it.hasNext()) {
                    int iIntValue = it.next().intValue();
                    String str4 = this.r.get(Integer.valueOf(iIntValue));
                    if (!TextUtils.isEmpty(str4)) {
                        eVar.a((short) ((((short) iIntValue) << 10) | ((short) (str4.getBytes("utf-8").length & EXT_HEADER_VALUE_MAX_LEN))));
                        eVar.write(str4.getBytes("utf-8"));
                        if (ALog.isPrintLog(ALog.Level.D)) {
                            ALog.d(j(), "\textHeader key:" + iIntValue + " value:" + str4, new Object[0]);
                        }
                    }
                }
            }
            if (this.N != null) {
                eVar.write(this.N);
            }
            if (ALog.isPrintLog(ALog.Level.D)) {
                ALog.d(j(), "\toriData:" + str, new Object[0]);
            }
            eVar.flush();
        } catch (IOException e4) {
            ALog.e(j(), "build4", e4, new Object[0]);
        }
        byte[] byteArray = eVar.toByteArray();
        try {
            eVar.close();
        } catch (IOException e5) {
            ALog.e(j(), "build5", e5, new Object[0]);
        }
        return byteArray;
    }

    short a(Map<Integer, String> map) {
        short length = 0;
        if (map != null) {
            try {
                Iterator<Integer> it = map.keySet().iterator();
                while (it.hasNext()) {
                    String str = map.get(Integer.valueOf(it.next().intValue()));
                    if (!TextUtils.isEmpty(str)) {
                        length = (short) (length + ((short) (str.getBytes("utf-8").length & EXT_HEADER_VALUE_MAX_LEN)) + 2);
                    }
                }
            } catch (Exception e) {
                e.toString();
            }
        }
        return length;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x0053 A[Catch: Exception -> 0x0059, TRY_ENTER, TRY_LEAVE, TryCatch #2 {Exception -> 0x0059, blocks: (B:13:0x0029, B:14:0x002c, B:28:0x0053), top: B:40:0x0001 }] */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0059 A[ORIG_RETURN, RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:36:0x0062 A[Catch: Exception -> 0x0065, TRY_LEAVE, TryCatch #5 {Exception -> 0x0065, blocks: (B:34:0x005d, B:36:0x0062), top: B:45:0x005d }] */
    /* JADX WARN: Removed duplicated region for block: B:45:0x005d A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    void h() throws java.lang.Throwable {
        /*
            r7 = this;
            r0 = 0
            byte[] r1 = r7.N     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L41
            if (r1 != 0) goto L6
            return
        L6:
            java.io.ByteArrayOutputStream r1 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L41
            r1.<init>()     // Catch: java.lang.Throwable -> L3c java.lang.Throwable -> L41
            java.util.zip.GZIPOutputStream r2 = new java.util.zip.GZIPOutputStream     // Catch: java.lang.Throwable -> L32 java.lang.Throwable -> L37
            r2.<init>(r1)     // Catch: java.lang.Throwable -> L32 java.lang.Throwable -> L37
            byte[] r0 = r7.N     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L5a
            r2.write(r0)     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L5a
            r2.finish()     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L5a
            byte[] r0 = r1.toByteArray()     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L5a
            if (r0 == 0) goto L29
            int r3 = r0.length     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L5a
            byte[] r4 = r7.N     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L5a
            int r4 = r4.length     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L5a
            if (r3 >= r4) goto L29
            r7.N = r0     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L5a
            r0 = 1
            r7.g = r0     // Catch: java.lang.Throwable -> L30 java.lang.Throwable -> L5a
        L29:
            r2.close()     // Catch: java.lang.Exception -> L59
        L2c:
            r1.close()     // Catch: java.lang.Exception -> L59
            goto L59
        L30:
            r0 = move-exception
            goto L45
        L32:
            r2 = move-exception
            r6 = r2
            r2 = r0
            r0 = r6
            goto L5b
        L37:
            r2 = move-exception
            r6 = r2
            r2 = r0
            r0 = r6
            goto L45
        L3c:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
            goto L5b
        L41:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L45:
            java.lang.String r3 = r7.j()     // Catch: java.lang.Throwable -> L5a
            java.lang.String r4 = "compressData fail"
            r5 = 0
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L5a
            com.taobao.accs.utl.ALog.w(r3, r4, r0, r5)     // Catch: java.lang.Throwable -> L5a
            if (r2 == 0) goto L56
            r2.close()     // Catch: java.lang.Exception -> L59
        L56:
            if (r1 == 0) goto L59
            goto L2c
        L59:
            return
        L5a:
            r0 = move-exception
        L5b:
            if (r2 == 0) goto L60
            r2.close()     // Catch: java.lang.Exception -> L65
        L60:
            if (r1 == 0) goto L65
            r1.close()     // Catch: java.lang.Exception -> L65
        L65:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.accs.data.Message.h():void");
    }

    void i() throws JSONException, UnsupportedEncodingException {
        Integer num = this.t;
        if (num == null || num.intValue() == 100 || this.t.intValue() == 102) {
            return;
        }
        this.N = new JsonUtility.JsonObjectBuilder().put("command", this.t.intValue() == 100 ? null : this.t).put("appKey", this.v).put(com.taobao.accs.common.Constants.KEY_OS_TYPE, this.x).put("sign", this.w).put("sdkVersion", this.D).put("appVersion", this.C).put(com.taobao.accs.common.Constants.KEY_TTID, this.E).put("model", this.I).put("brand", this.J).put(com.taobao.accs.common.Constants.KEY_IMEI, this.K).put(com.taobao.accs.common.Constants.KEY_IMSI, this.L).put(com.taobao.accs.common.Constants.KEY_OS_VERSION, this.y).put(com.taobao.accs.common.Constants.KEY_EXTS, this.B).build().toString().getBytes("utf-8");
    }

    public static Message a(boolean z, int i) {
        Message message = new Message();
        message.p = 2;
        message.t = 201;
        message.f6300d = z;
        message.Q = i;
        return message;
    }

    public static Message a(com.taobao.accs.net.b bVar, Context context, Intent intent) {
        Message messageA = null;
        try {
            String stringExtra = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_PACKAGE_NAME);
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_USER_ID);
            String stringExtra2 = intent.getStringExtra("appKey");
            String stringExtra3 = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_TTID);
            intent.getStringExtra("sid");
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_ANTI_BRUSH_COOKIE);
            messageA = a(context, bVar.m, stringExtra2, intent.getStringExtra("app_sercet"), stringExtra, stringExtra3, intent.getStringExtra("appVersion"));
            a(bVar, messageA);
            return messageA;
        } catch (Exception e) {
            ALog.e("Msg", "buildBindApp", e.getMessage());
            return messageA;
        }
    }

    public static Message a(Context context, String str, String str2, String str3, String str4, String str5, String str6) {
        if (TextUtils.isEmpty(str4)) {
            return null;
        }
        Message message = new Message();
        message.P = 1;
        message.a(1, ReqType.DATA, 1);
        message.x = 1;
        message.y = Build.VERSION.SDK_INT + "";
        message.s = str4;
        message.n = com.taobao.accs.common.Constants.TARGET_CONTROL;
        message.t = 1;
        message.v = str2;
        message.w = UtilityImpl.a(context, str2, str3, UtilityImpl.getDeviceId(context), str);
        message.D = Integer.valueOf(com.taobao.accs.common.Constants.SDK_VERSION_CODE);
        message.C = str6;
        message.s = str4;
        message.E = str5;
        message.I = Build.MODEL;
        message.J = Build.BRAND;
        message.O = KEY_BINDAPP;
        message.X = str;
        message.B = new JsonUtility.JsonObjectBuilder().put("notifyEnable", UtilityImpl.k(context)).put("romInfo", RomInfoCollecter.getCollecter().collect()).build().toString();
        return message;
    }

    public static Message a(com.taobao.accs.net.b bVar, Intent intent) {
        ALog.e("Msg", "buildUnbindApp1" + UtilityImpl.a(new Exception()), new Object[0]);
        Message messageA = null;
        try {
            String stringExtra = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_PACKAGE_NAME);
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_USER_ID);
            intent.getStringExtra("sid");
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_ANTI_BRUSH_COOKIE);
            messageA = a(bVar, stringExtra);
            a(bVar, messageA);
            return messageA;
        } catch (Exception e) {
            ALog.e("Msg", "buildUnbindApp1", e.getMessage());
            return messageA;
        }
    }

    public static Message a(com.taobao.accs.net.b bVar, String str) {
        Message message;
        try {
            ALog.e("Msg", "buildUnbindApp" + UtilityImpl.a(new Exception()), new Object[0]);
        } catch (Exception e) {
            e = e;
            message = null;
        }
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        message = new Message();
        try {
            message.P = 1;
            message.a(1, ReqType.DATA, 1);
            message.s = str;
            message.n = com.taobao.accs.common.Constants.TARGET_CONTROL;
            message.t = 2;
            message.s = str;
            message.D = Integer.valueOf(com.taobao.accs.common.Constants.SDK_VERSION_CODE);
            message.O = KEY_UNBINDAPP;
            a(bVar, message);
        } catch (Exception e2) {
            e = e2;
            ALog.e("Msg", "buildUnbindApp", e.getMessage());
        }
        return message;
        ALog.e("Msg", "buildUnbindApp", e.getMessage());
        return message;
    }

    public static Message b(com.taobao.accs.net.b bVar, Intent intent) {
        Message messageA = null;
        try {
            String stringExtra = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_PACKAGE_NAME);
            String stringExtra2 = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_SERVICE_ID);
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_USER_ID);
            intent.getStringExtra("appKey");
            intent.getStringExtra("sid");
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_ANTI_BRUSH_COOKIE);
            messageA = a(stringExtra, stringExtra2);
            messageA.X = bVar.m;
            a(bVar, messageA);
            return messageA;
        } catch (Throwable th) {
            ALog.e("Msg", "buildBindService", th, new Object[0]);
            th.printStackTrace();
            return messageA;
        }
    }

    public static Message a(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        Message message = new Message();
        message.P = 1;
        message.a(1, ReqType.DATA, 1);
        message.s = str;
        message.H = str2;
        message.n = com.taobao.accs.common.Constants.TARGET_CONTROL;
        message.t = 5;
        message.s = str;
        message.H = str2;
        message.D = Integer.valueOf(com.taobao.accs.common.Constants.SDK_VERSION_CODE);
        message.O = KEY_BINDSERVICE;
        return message;
    }

    public static Message c(com.taobao.accs.net.b bVar, Intent intent) {
        Message messageB = null;
        try {
            String stringExtra = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_PACKAGE_NAME);
            String stringExtra2 = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_SERVICE_ID);
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_USER_ID);
            intent.getStringExtra("appKey");
            intent.getStringExtra("sid");
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_ANTI_BRUSH_COOKIE);
            messageB = b(stringExtra, stringExtra2);
            messageB.X = bVar.m;
            a(bVar, messageB);
            return messageB;
        } catch (Exception e) {
            ALog.e("Msg", "buildUnbindService", e, new Object[0]);
            e.printStackTrace();
            return messageB;
        }
    }

    public static Message b(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        Message message = new Message();
        message.P = 1;
        message.a(1, ReqType.DATA, 1);
        message.s = str;
        message.H = str2;
        message.n = com.taobao.accs.common.Constants.TARGET_CONTROL;
        message.t = 6;
        message.s = str;
        message.H = str2;
        message.D = Integer.valueOf(com.taobao.accs.common.Constants.SDK_VERSION_CODE);
        message.O = KEY_UNBINDSERVICE;
        return message;
    }

    public static Message d(com.taobao.accs.net.b bVar, Intent intent) {
        Message messageC = null;
        try {
            String stringExtra = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_PACKAGE_NAME);
            String stringExtra2 = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_USER_ID);
            intent.getStringExtra("appKey");
            intent.getStringExtra("sid");
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_ANTI_BRUSH_COOKIE);
            messageC = c(stringExtra, stringExtra2);
            if (messageC != null) {
                messageC.X = bVar.m;
                a(bVar, messageC);
            }
        } catch (Exception e) {
            ALog.e("Msg", "buildBindUser", e, new Object[0]);
            e.printStackTrace();
        }
        return messageC;
    }

    public static Message c(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return null;
        }
        Message message = new Message();
        message.P = 1;
        message.a(1, ReqType.DATA, 1);
        message.s = str;
        message.G = str2;
        message.n = com.taobao.accs.common.Constants.TARGET_CONTROL;
        message.t = 3;
        message.s = str;
        message.G = str2;
        message.D = Integer.valueOf(com.taobao.accs.common.Constants.SDK_VERSION_CODE);
        message.O = KEY_BINDUSER;
        return message;
    }

    public static Message a(String str, String str2, String str3, int i) {
        Message message = new Message();
        try {
            message.f = new URL(str3);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        message.n = com.taobao.accs.common.Constants.TARGET_SERVICE_ST;
        message.a(1, ReqType.DATA, 0);
        message.t = 100;
        message.N = ("0|" + i + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + str + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + AdapterUtilityImpl.getDeviceId(GlobalClientInfo.getContext()) + HiAnalyticsConstant.REPORT_VAL_SEPARATOR + str2).getBytes();
        return message;
    }

    public static Message e(com.taobao.accs.net.b bVar, Intent intent) {
        Message messageA = null;
        try {
            String stringExtra = intent.getStringExtra(com.taobao.accs.common.Constants.KEY_PACKAGE_NAME);
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_USER_ID);
            intent.getStringExtra("appKey");
            intent.getStringExtra("sid");
            intent.getStringExtra(com.taobao.accs.common.Constants.KEY_ANTI_BRUSH_COOKIE);
            messageA = a(stringExtra);
            messageA.X = bVar.m;
            a(bVar, messageA);
            return messageA;
        } catch (Exception e) {
            ALog.e("Msg", "buildUnbindUser", e, new Object[0]);
            e.printStackTrace();
            return messageA;
        }
    }

    public static Message a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Message message = new Message();
        message.P = 1;
        message.a(1, ReqType.DATA, 1);
        message.s = str;
        message.n = com.taobao.accs.common.Constants.TARGET_CONTROL;
        message.t = 4;
        message.D = Integer.valueOf(com.taobao.accs.common.Constants.SDK_VERSION_CODE);
        message.O = KEY_UNBINDUSER;
        return message;
    }

    public static Message a(com.taobao.accs.net.b bVar, Context context, String str, ACCSManager.AccsRequest accsRequest) {
        return a(bVar, context, str, accsRequest, true);
    }

    public static Message a(com.taobao.accs.net.b bVar, Context context, String str, ACCSManager.AccsRequest accsRequest, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Message message = new Message();
        message.P = 1;
        message.a(1, ReqType.DATA, 1);
        message.t = 100;
        message.s = str;
        message.H = accsRequest.serviceId;
        message.G = accsRequest.userId;
        message.N = accsRequest.data;
        String str2 = TextUtils.isEmpty(accsRequest.targetServiceName) ? accsRequest.serviceId : accsRequest.targetServiceName;
        StringBuilder sb = new StringBuilder();
        sb.append(com.taobao.accs.common.Constants.TARGET_SERVICE_PRE);
        sb.append(str2);
        sb.append(HiAnalyticsConstant.REPORT_VAL_SEPARATOR);
        sb.append(accsRequest.target == null ? "" : accsRequest.target);
        message.n = sb.toString();
        message.O = accsRequest.dataId;
        message.V = accsRequest.businessId;
        if (accsRequest.timeout > 0) {
            message.S = accsRequest.timeout;
        }
        if (z) {
            a(bVar, message, accsRequest);
        } else {
            message.f = accsRequest.host;
        }
        a(message, GlobalClientInfo.getInstance(context).getSid(bVar.m), GlobalClientInfo.getInstance(context).getUserId(bVar.m), bVar.i.getStoreId(), GlobalClientInfo.f6290b, accsRequest.businessId, accsRequest.tag);
        message.W = new NetPerformanceMonitor();
        message.W.setMsgType(0);
        message.W.setDataId(accsRequest.dataId);
        message.W.setServiceId(accsRequest.serviceId);
        NetPerformanceMonitor netPerformanceMonitor = message.W;
        URL url = message.f;
        netPerformanceMonitor.setHost(url != null ? url.toString() : "");
        message.X = bVar.m;
        return message;
    }

    public static Message a(com.taobao.accs.net.b bVar, Context context, String str, String str2, ACCSManager.AccsRequest accsRequest, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Message message = new Message();
        message.P = 1;
        message.a(1, ReqType.REQ, 1);
        message.t = 100;
        message.s = str;
        message.H = accsRequest.serviceId;
        message.G = accsRequest.userId;
        message.N = accsRequest.data;
        String str3 = TextUtils.isEmpty(accsRequest.targetServiceName) ? accsRequest.serviceId : accsRequest.targetServiceName;
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(str3);
        sb.append(HiAnalyticsConstant.REPORT_VAL_SEPARATOR);
        sb.append(accsRequest.target == null ? "" : accsRequest.target);
        message.n = sb.toString();
        message.O = accsRequest.dataId;
        message.V = accsRequest.businessId;
        message.X = bVar.m;
        if (accsRequest.timeout > 0) {
            message.S = accsRequest.timeout;
        }
        if (z) {
            a(bVar, message, accsRequest);
        } else {
            message.f = accsRequest.host;
        }
        a(message, GlobalClientInfo.getInstance(context).getSid(bVar.m), GlobalClientInfo.getInstance(context).getUserId(bVar.m), bVar.i.getStoreId(), GlobalClientInfo.f6290b, accsRequest.businessId, accsRequest.tag);
        message.W = new NetPerformanceMonitor();
        message.W.setDataId(accsRequest.dataId);
        message.W.setServiceId(accsRequest.serviceId);
        NetPerformanceMonitor netPerformanceMonitor = message.W;
        URL url = message.f;
        netPerformanceMonitor.setHost(url != null ? url.toString() : "");
        message.X = bVar.m;
        return message;
    }

    private static void a(com.taobao.accs.net.b bVar, Message message, ACCSManager.AccsRequest accsRequest) {
        if (accsRequest.host == null) {
            try {
                message.f = new URL(bVar.b((String) null));
                return;
            } catch (MalformedURLException e) {
                ALog.e("Msg", "setUnit", e, new Object[0]);
                e.printStackTrace();
                return;
            }
        }
        message.f = accsRequest.host;
    }

    private static void a(com.taobao.accs.net.b bVar, Message message) {
        try {
            message.f = new URL(bVar.b((String) null));
        } catch (Exception e) {
            ALog.e("Msg", "setControlHost", e, new Object[0]);
        }
    }

    public static Message a(com.taobao.accs.net.b bVar, String str, String str2, String str3, boolean z, short s, String str4, Map<Integer, String> map) {
        Message message = new Message();
        message.P = 1;
        message.a(s, z);
        message.o = str;
        message.n = str2;
        message.q = str3;
        message.f6299c = true;
        message.r = map;
        try {
            try {
                try {
                    if (TextUtils.isEmpty(str4)) {
                        message.f = new URL(bVar.b((String) null));
                    } else {
                        message.f = new URL(str4);
                    }
                    message.X = bVar.m;
                } catch (Throwable th) {
                    if (message.f == null) {
                        try {
                            message.f = new URL(bVar.b((String) null));
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                ALog.e("Msg", "buildPushAck", th2, new Object[0]);
                if (message.f == null) {
                    message.f = new URL(bVar.b((String) null));
                }
            }
            if (message.f == null) {
                message.f = new URL(bVar.b((String) null));
            }
        } catch (MalformedURLException e2) {
            e2.printStackTrace();
        }
        return message;
    }

    public static Message a(String str, int i) {
        Message message = new Message();
        message.a(1, ReqType.ACK, 0);
        message.t = Integer.valueOf(i);
        message.s = str;
        return message;
    }

    private static void a(Message message, String str, String str2, String str3, String str4, String str5, String str6) {
        if (TextUtils.isEmpty(str5) && TextUtils.isEmpty(str) && TextUtils.isEmpty(str2) && TextUtils.isEmpty(str6) && str4 == null) {
            return;
        }
        message.r = new HashMap();
        if (str5 != null && UtilityImpl.b(str5) <= 1023) {
            message.r.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_BUSINESS.ordinal()), str5);
        }
        if (str != null && UtilityImpl.b(str) <= 1023) {
            message.r.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_SID.ordinal()), str);
        }
        if (str2 != null && UtilityImpl.b(str2) <= 1023) {
            message.r.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_USERID.ordinal()), str2);
        }
        if (str6 != null && UtilityImpl.b(str6) <= 1023) {
            message.r.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_TAG.ordinal()), str6);
        }
        if (str4 != null && UtilityImpl.b(str4) <= 1023) {
            message.r.put(Integer.valueOf(TaoBaseService.ExtHeaderType.TYPE_COOKIE.ordinal()), str4);
        }
        if (str3 == null || UtilityImpl.b(str3) > 1023) {
            return;
        }
        message.r.put(19, str3);
    }

    private void a(int i, ReqType reqType, int i2) {
        this.p = i;
        if (i != 2) {
            this.k = (short) (((((i & 1) << 4) | (reqType.ordinal() << 2)) | i2) << 11);
        }
    }

    private void a(short s, boolean z) {
        this.p = 1;
        this.k = s;
        this.k = (short) (this.k & (-16385));
        this.k = (short) (this.k | 8192);
        this.k = (short) (this.k & (-2049));
        this.k = (short) (this.k & (-65));
        if (z) {
            this.k = (short) (this.k | 32);
        }
    }
}
