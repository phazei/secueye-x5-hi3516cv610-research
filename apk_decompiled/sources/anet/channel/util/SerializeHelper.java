package anet.channel.util;

import android.content.Context;
import anet.channel.GlobalAppRuntimeInfo;
import java.io.File;
import java.io.Serializable;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class SerializeHelper {
    private static final String TAG = "awcn.SerializeHelper";
    private static File cacheDir;

    public static File getCacheFiles(String str) {
        Context context;
        if (cacheDir == null && (context = GlobalAppRuntimeInfo.getContext()) != null) {
            cacheDir = context.getCacheDir();
        }
        return new File(cacheDir, str);
    }

    public static synchronized void persist(Serializable serializable, File file) {
        persist(serializable, file, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:79:0x0105 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized void persist(java.io.Serializable r17, java.io.File r18, anet.channel.statist.StrategyStatObject r19) {
        /*
            Method dump skipped, instruction units count: 279
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.SerializeHelper.persist(java.io.Serializable, java.io.File, anet.channel.statist.StrategyStatObject):void");
    }

    public static synchronized <T> T restore(File file) {
        return (T) restore(file, null);
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x00b9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static synchronized <T> T restore(java.io.File r14, anet.channel.statist.StrategyStatObject r15) {
        /*
            java.lang.Class<anet.channel.util.SerializeHelper> r0 = anet.channel.util.SerializeHelper.class
            monitor-enter(r0)
            if (r15 == 0) goto Lf
            java.lang.String r1 = java.lang.String.valueOf(r14)     // Catch: java.lang.Throwable -> Lc
            r15.readStrategyFilePath = r1     // Catch: java.lang.Throwable -> Lc
            goto Lf
        Lc:
            r14 = move-exception
            goto Lbd
        Lf:
            r1 = 0
            r2 = 3
            r3 = 0
            boolean r4 = r14.exists()     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            r5 = 2
            r6 = 1
            if (r4 != 0) goto L35
            boolean r4 = anet.channel.util.ALog.isPrintLog(r2)     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            if (r4 == 0) goto L33
            java.lang.String r4 = "awcn.SerializeHelper"
            java.lang.String r7 = "file not exist."
            java.lang.Object[] r5 = new java.lang.Object[r5]     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            java.lang.String r8 = "file"
            r5[r1] = r8     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            java.lang.String r14 = r14.getName()     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            r5[r6] = r14     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            anet.channel.util.ALog.w(r4, r7, r3, r5)     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
        L33:
            monitor-exit(r0)
            return r3
        L35:
            if (r15 == 0) goto L39
            r15.isFileExists = r6     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
        L39:
            long r7 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            java.io.FileInputStream r4 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            r4.<init>(r14)     // Catch: java.lang.Throwable -> L95 java.lang.Throwable -> L98
            java.io.ObjectInputStream r9 = new java.io.ObjectInputStream     // Catch: java.lang.Throwable -> L92 java.lang.Throwable -> Lb6
            java.io.BufferedInputStream r10 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L92 java.lang.Throwable -> Lb6
            r10.<init>(r4)     // Catch: java.lang.Throwable -> L92 java.lang.Throwable -> Lb6
            r9.<init>(r10)     // Catch: java.lang.Throwable -> L92 java.lang.Throwable -> Lb6
            java.lang.Object r10 = r9.readObject()     // Catch: java.lang.Throwable -> L92 java.lang.Throwable -> Lb6
            r9.close()     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            long r11 = java.lang.System.currentTimeMillis()     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            long r11 = r11 - r7
            if (r15 == 0) goto L5e
            r15.isReadObjectSucceed = r6     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            r15.readCostTime = r11     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
        L5e:
            java.lang.String r7 = "awcn.SerializeHelper"
            java.lang.String r8 = "restore end."
            r9 = 6
            java.lang.Object[] r9 = new java.lang.Object[r9]     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            java.lang.String r13 = "file"
            r9[r1] = r13     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            java.io.File r13 = r14.getAbsoluteFile()     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            r9[r6] = r13     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            java.lang.String r6 = "size"
            r9[r5] = r6     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            long r5 = r14.length()     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            java.lang.Long r14 = java.lang.Long.valueOf(r5)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            r9[r2] = r14     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            r14 = 4
            java.lang.String r5 = "cost"
            r9[r14] = r5     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            r14 = 5
            java.lang.Long r5 = java.lang.Long.valueOf(r11)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            r9[r14] = r5     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
            anet.channel.util.ALog.i(r7, r8, r3, r9)     // Catch: java.lang.Throwable -> L90 java.lang.Throwable -> Lb6
        L8c:
            r4.close()     // Catch: java.lang.Throwable -> Lc java.io.IOException -> Lb4
            goto Lb4
        L90:
            r14 = move-exception
            goto L9b
        L92:
            r14 = move-exception
            r10 = r3
            goto L9b
        L95:
            r14 = move-exception
            r4 = r3
            goto Lb7
        L98:
            r14 = move-exception
            r4 = r3
            r10 = r4
        L9b:
            boolean r2 = anet.channel.util.ALog.isPrintLog(r2)     // Catch: java.lang.Throwable -> Lb6
            if (r2 == 0) goto Laa
            java.lang.String r2 = "awcn.SerializeHelper"
            java.lang.String r5 = "restore file fail."
            java.lang.Object[] r1 = new java.lang.Object[r1]     // Catch: java.lang.Throwable -> Lb6
            anet.channel.util.ALog.w(r2, r5, r3, r14, r1)     // Catch: java.lang.Throwable -> Lb6
        Laa:
            if (r15 == 0) goto Lb1
            java.lang.String r1 = "SerializeHelper.restore()"
            r15.appendErrorTrace(r1, r14)     // Catch: java.lang.Throwable -> Lb6
        Lb1:
            if (r4 == 0) goto Lb4
            goto L8c
        Lb4:
            monitor-exit(r0)
            return r10
        Lb6:
            r14 = move-exception
        Lb7:
            if (r4 == 0) goto Lbc
            r4.close()     // Catch: java.lang.Throwable -> Lc java.io.IOException -> Lbc
        Lbc:
            throw r14     // Catch: java.lang.Throwable -> Lc
        Lbd:
            monitor-exit(r0)
            throw r14
        */
        throw new UnsupportedOperationException("Method not decompiled: anet.channel.util.SerializeHelper.restore(java.io.File, anet.channel.statist.StrategyStatObject):java.lang.Object");
    }
}
