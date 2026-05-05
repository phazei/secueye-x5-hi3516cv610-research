package lvbyte;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.ALog;

/* JADX INFO: loaded from: classes4.dex */
public class lvint {
    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0068: MOVE (r0 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:27:0x0068 */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x005e A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String lvdo(java.lang.String r7) throws java.lang.Throwable {
        /*
            r0 = 0
            java.lang.ProcessBuilder r1 = new java.lang.ProcessBuilder     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L42
            r2 = 0
            java.lang.String[] r3 = new java.lang.String[r2]     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L42
            r1.<init>(r3)     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L42
            r3 = 2
            java.lang.String[] r3 = new java.lang.String[r3]     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L42
            java.lang.String r4 = "/system/bin/getprop"
            r3[r2] = r4     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L42
            r2 = 1
            r3[r2] = r7     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L42
            java.lang.ProcessBuilder r1 = r1.command(r3)     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L42
            java.lang.ProcessBuilder r1 = r1.redirectErrorStream(r2)     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L42
            java.lang.Process r1 = r1.start()     // Catch: java.lang.Throwable -> L3f java.lang.Exception -> L42
            java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            java.io.InputStream r4 = r1.getInputStream()     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            r3.<init>(r4)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L3a java.lang.Exception -> L3c
            java.lang.String r7 = r2.readLine()     // Catch: java.lang.Exception -> L38 java.lang.Throwable -> L67
            r2.close()     // Catch: java.io.IOException -> L34
        L34:
            r1.destroy()
            return r7
        L38:
            r3 = move-exception
            goto L46
        L3a:
            r7 = move-exception
            goto L69
        L3c:
            r2 = move-exception
            r3 = r2
            goto L45
        L3f:
            r7 = move-exception
            r1 = r0
            goto L69
        L42:
            r1 = move-exception
            r3 = r1
            r1 = r0
        L45:
            r2 = r0
        L46:
            java.lang.String r4 = "linksdk_lv_EnvUtils"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L67
            r5.<init>()     // Catch: java.lang.Throwable -> L67
            java.lang.String r6 = "Failed to read System Property "
            r5.append(r6)     // Catch: java.lang.Throwable -> L67
            r5.append(r7)     // Catch: java.lang.Throwable -> L67
            java.lang.String r7 = r5.toString()     // Catch: java.lang.Throwable -> L67
            com.aliyun.alink.linksdk.tools.ALog.e(r4, r7, r3)     // Catch: java.lang.Throwable -> L67
            if (r2 == 0) goto L61
            r2.close()     // Catch: java.io.IOException -> L61
        L61:
            if (r1 == 0) goto L66
            r1.destroy()
        L66:
            return r0
        L67:
            r7 = move-exception
            r0 = r2
        L69:
            if (r0 == 0) goto L6e
            r0.close()     // Catch: java.io.IOException -> L6e
        L6e:
            if (r1 == 0) goto L73
            r1.destroy()
        L73:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: lvbyte.lvint.lvdo(java.lang.String):java.lang.String");
    }

    public static boolean lvdo() throws Throwable {
        String strLvdo = lvdo("debug.lv.dump.enable");
        ALog.d("linksdk_lv_EnvUtils", "lvDumpEnable: " + strLvdo);
        return TextUtils.equals(strLvdo, "true");
    }
}
