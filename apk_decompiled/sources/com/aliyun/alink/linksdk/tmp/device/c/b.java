package com.aliyun.alink.linksdk.tmp.device.c;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.TmpSdk;
import com.aliyun.alink.linksdk.tmp.data.config.LocalConfigData;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.ResHelper;
import com.aliyun.alink.linksdk.tools.ALog;
import com.google.gson.reflect.TypeToken;
import java.io.File;

/* JADX INFO: compiled from: LocalConfigurator.java */
/* JADX INFO: loaded from: classes2.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f4392a = "[Tmp]LocalConfigurator";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static final String f4393b = "local_config";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f4394c = "tmp_config";

    public static void a(String str) {
        File externalFilesDir;
        File file = (!TextUtils.isEmpty(str) || (externalFilesDir = TmpSdk.getContext().getExternalFilesDir(null)) == null) ? null : new File(new File(externalFilesDir.getAbsolutePath(), f4394c).getAbsolutePath(), f4393b);
        if (file == null && !TextUtils.isEmpty(str)) {
            file = new File(str);
        }
        if (file == null || !file.exists()) {
            ALog.w(f4392a, "LocalConfigurator config not exist");
            return;
        }
        ALog.d(f4392a, "LocalConfigurator start path:" + file.getAbsolutePath());
        LocalConfigData localConfigData = (LocalConfigData) GsonUtils.fromJson(ResHelper.getFileStr(file), new TypeToken<LocalConfigData>() { // from class: com.aliyun.alink.linksdk.tmp.device.c.b.1
        }.getType());
        a.a().a(localConfigData);
        if (localConfigData != null && localConfigData.configReceiver != null && !localConfigData.configReceiver.autoRun) {
            ALog.i(f4392a, "configReceiver not autorun");
        } else {
            ALog.i(f4392a, "configReceiver end");
        }
    }
}
