package com.aliyun.alink.linksdk.tmp.device.c;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.config.ProvisionReceiver;
import com.aliyun.alink.linksdk.tmp.data.auth.ProvisionAuthData;
import com.aliyun.alink.linksdk.tmp.data.auth.SetupData;
import com.aliyun.alink.linksdk.tmp.data.config.LocalConfigData;
import com.aliyun.alink.linksdk.tmp.device.payload.setup.SetupRequestPayload;
import com.aliyun.alink.linksdk.tmp.listener.IProvisionListener;
import com.aliyun.alink.linksdk.tmp.listener.IProvisionResponser;
import com.aliyun.alink.linksdk.tmp.storage.TmpStorage;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.google.gson.reflect.TypeToken;

/* JADX INFO: compiled from: ConfigMgr.java */
/* JADX INFO: loaded from: classes2.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f4385a = "Xtau@iot";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public static final String f4386b = "Yx3DdsyetbSezlvc";

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static final String f4387c = "[Tmp]ConfigMgr";

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private LocalConfigData f4388d;

    private a() {
    }

    public static a a() {
        return C0227a.f4391a;
    }

    /* JADX INFO: renamed from: com.aliyun.alink.linksdk.tmp.device.c.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: ConfigMgr.java */
    public static class C0227a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static a f4391a = new a();
    }

    public void b() {
        ProvisionReceiver.getInstance().addListener(new IProvisionListener() { // from class: com.aliyun.alink.linksdk.tmp.device.c.a.1
            @Override // com.aliyun.alink.linksdk.tmp.listener.IProvisionListener
            public void onMsg(String str, String str2, Object obj, IProvisionResponser iProvisionResponser) {
                if (TextUtils.isEmpty(str2) || !str2.endsWith(TmpConstant.PATH_SETUP)) {
                    return;
                }
                SetupRequestPayload setupRequestPayload = (SetupRequestPayload) GsonUtils.fromJson(String.valueOf(obj), new TypeToken<SetupRequestPayload>() { // from class: com.aliyun.alink.linksdk.tmp.device.c.a.1.1
                }.getType());
                if (a.this.a(setupRequestPayload.getParams())) {
                    if (setupRequestPayload.getParams().configType.equalsIgnoreCase("ServerAuthInfo")) {
                        for (int i = 0; i < setupRequestPayload.getParams().configValue.size(); i++) {
                            com.aliyun.alink.linksdk.tmp.connect.a.b(setupRequestPayload.getParams().configValue.get(i).authCode, setupRequestPayload.getParams().configValue.get(i).authSecret);
                            com.aliyun.alink.linksdk.tmp.connect.a.b("Xtau@iot");
                        }
                    }
                    iProvisionResponser.onComplete(str, null, null);
                }
            }
        });
    }

    public void a(LocalConfigData localConfigData) {
        this.f4388d = localConfigData;
    }

    public LocalConfigData c() {
        return this.f4388d;
    }

    public boolean a(SetupData setupData) {
        int i = 0;
        if (setupData == null || setupData.configValue == null || setupData.configValue.isEmpty() || TextUtils.isEmpty(setupData.configType)) {
            return false;
        }
        if (setupData.configType.equalsIgnoreCase(TmpConstant.CONFIG_TYPE_CLIENT)) {
            while (i < setupData.configValue.size()) {
                ProvisionAuthData provisionAuthData = setupData.configValue.get(i);
                TmpStorage.getInstance().saveAccessInfo(provisionAuthData.getId(), provisionAuthData.accessKey, provisionAuthData.accessToken, true, "local");
                i++;
            }
            return true;
        }
        if (!setupData.configType.equalsIgnoreCase("ServerAuthInfo")) {
            return true;
        }
        while (i < setupData.configValue.size()) {
            ProvisionAuthData provisionAuthData2 = setupData.configValue.get(i);
            TmpStorage.getInstance().saveServerEnptInfo(provisionAuthData2.getId(), provisionAuthData2.authCode, provisionAuthData2.authSecret, "local");
            i++;
        }
        return true;
    }
}
