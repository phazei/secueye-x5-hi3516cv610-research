package com.taobao.agoo.control.data;

import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.JsonUtility;

/* JADX INFO: loaded from: classes3.dex */
public class RegisterDO extends BaseDO {
    public static final String JSON_CMD_REGISTER = "register";
    private static final String TAG = "RegisterDO";
    public String appKey;
    public String appVersion;
    public String c0;
    public String c1;
    public String c2;
    public String c3;
    public String c4;
    public String c5;
    public String c6;
    public String notifyEnable;
    public String packageName;
    public String romInfo;
    public String sdkVersion = String.valueOf(Constants.SDK_VERSION_CODE);
    public String ttid;
    public String utdid;

    @Override // com.taobao.agoo.control.data.BaseDO
    public byte[] buildData() {
        try {
            String string = new JsonUtility.JsonObjectBuilder().put(BaseDO.JSON_CMD, this.cmd).put("appKey", this.appKey).put("utdid", this.utdid).put("appVersion", this.appVersion).put("sdkVersion", this.sdkVersion).put(Constants.KEY_TTID, this.ttid).put(Constants.KEY_PACKAGE_NAME, this.packageName).put("notifyEnable", this.notifyEnable).put("romInfo", this.romInfo).put("c0", this.c0).put("c1", this.c1).put("c2", this.c2).put("c3", this.c3).put("c4", this.c4).put("c5", this.c5).put("c6", this.c6).build().toString();
            ALog.i(TAG, "buildData", "data", string);
            return string.getBytes("utf-8");
        } catch (Throwable th) {
            ALog.e(TAG, "buildData", th, new Object[0]);
            return null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:27:0x009a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static byte[] buildRegister(android.content.Context r8, java.lang.String r9, java.lang.String r10) throws java.lang.Throwable {
        /*
            r0 = 0
            r1 = 1
            r2 = 0
            java.lang.String r3 = com.taobao.accs.utl.UtilityImpl.getDeviceId(r8)     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            java.lang.String r4 = r8.getPackageName()     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            com.taobao.accs.client.GlobalClientInfo r5 = com.taobao.accs.client.GlobalClientInfo.getInstance(r8)     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            android.content.pm.PackageInfo r5 = r5.getPackageInfo()     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            java.lang.String r5 = r5.versionName     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            boolean r6 = android.text.TextUtils.isEmpty(r9)     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            if (r6 != 0) goto L5a
            boolean r6 = android.text.TextUtils.isEmpty(r3)     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            if (r6 != 0) goto L5a
            boolean r6 = android.text.TextUtils.isEmpty(r5)     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            if (r6 == 0) goto L28
            goto L5a
        L28:
            com.taobao.agoo.control.data.RegisterDO r6 = new com.taobao.agoo.control.data.RegisterDO     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            r6.<init>()     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            java.lang.String r7 = "register"
            r6.cmd = r7     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.appKey = r9     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.utdid = r3     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.appVersion = r5     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.ttid = r10     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.packageName = r4     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            java.lang.String r9 = android.os.Build.BRAND     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.c0 = r9     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            java.lang.String r9 = android.os.Build.MODEL     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.c1 = r9     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.c2 = r2     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.c3 = r2     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            java.lang.String r8 = com.taobao.accs.utl.AdapterUtilityImpl.isNotificationEnabled(r8)     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.notifyEnable = r8     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            com.taobao.accs.utl.RomInfoCollecter r8 = com.taobao.accs.utl.RomInfoCollecter.getCollecter()     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            java.lang.String r8 = r8.collect()     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            r6.romInfo = r8     // Catch: java.lang.Throwable -> L58 java.lang.Throwable -> L97
            goto L92
        L58:
            r8 = move-exception
            goto L81
        L5a:
            java.lang.String r8 = "RegisterDO"
            java.lang.String r10 = "buildRegister param null"
            r4 = 6
            java.lang.Object[] r4 = new java.lang.Object[r4]     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            java.lang.String r6 = "appKey"
            r4[r0] = r6     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            r4[r1] = r9     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            r9 = 2
            java.lang.String r6 = "utdid"
            r4[r9] = r6     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            r9 = 3
            r4[r9] = r3     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            r9 = 4
            java.lang.String r3 = "appVersion"
            r4[r9] = r3     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            r9 = 5
            r4[r9] = r5     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            com.taobao.accs.utl.ALog.e(r8, r10, r4)     // Catch: java.lang.Throwable -> L7c java.lang.Throwable -> L7f
            return r2
        L7c:
            r8 = move-exception
            r6 = r2
            goto L98
        L7f:
            r8 = move-exception
            r6 = r2
        L81:
            java.lang.String r9 = "RegisterDO"
            java.lang.String r10 = "buildRegister"
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> L97
            java.lang.String r8 = r8.getMessage()     // Catch: java.lang.Throwable -> L97
            r1[r0] = r8     // Catch: java.lang.Throwable -> L97
            com.taobao.accs.utl.ALog.w(r9, r10, r1)     // Catch: java.lang.Throwable -> L97
            if (r6 == 0) goto L96
        L92:
            byte[] r2 = r6.buildData()
        L96:
            return r2
        L97:
            r8 = move-exception
        L98:
            if (r6 == 0) goto L9d
            r6.buildData()
        L9d:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.agoo.control.data.RegisterDO.buildRegister(android.content.Context, java.lang.String, java.lang.String):byte[]");
    }
}
