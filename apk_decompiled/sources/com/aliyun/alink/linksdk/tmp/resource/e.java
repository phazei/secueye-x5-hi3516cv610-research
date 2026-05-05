package com.aliyun.alink.linksdk.tmp.resource;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.api.OutputParams;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.listener.IPublishResourceListener;
import com.aliyun.alink.linksdk.tmp.resource.ResDescpt;
import com.aliyun.alink.linksdk.tmp.utils.TextHelper;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: compiled from: TResManager.java */
/* JADX INFO: loaded from: classes2.dex */
public class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected static final String f4419a = "[Tmp]TResManager";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected Map<String, b> f4420b;

    private e() {
        this.f4420b = new HashMap();
    }

    public static e a() {
        return a.f4421a;
    }

    /* JADX INFO: compiled from: TResManager.java */
    protected static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        protected static e f4421a = new e();

        protected a() {
        }
    }

    public String a(com.aliyun.alink.linksdk.tmp.connect.b bVar, String str, DeviceModel deviceModel, String str2, boolean z, b bVar2) {
        String topicStr = TextHelper.getTopicStr(deviceModel, str2);
        if (a(bVar, str, str2, topicStr, z, bVar2)) {
            return topicStr;
        }
        return null;
    }

    public String a(com.aliyun.alink.linksdk.tmp.connect.b bVar, DeviceModel deviceModel, String str, boolean z, b bVar2) {
        return a(bVar, (String) null, deviceModel, str, z, bVar2);
    }

    public boolean a(com.aliyun.alink.linksdk.tmp.connect.b bVar, DeviceModel deviceModel, String str) {
        return a(bVar, str, TextHelper.getTopicStr(deviceModel, str));
    }

    public boolean a(com.aliyun.alink.linksdk.tmp.connect.b bVar, DeviceModel deviceModel, String str, OutputParams outputParams, IPublishResourceListener iPublishResourceListener) {
        return a(bVar, str, deviceModel.getEventMethod(str), TextHelper.getTopicStr(deviceModel, str), outputParams, iPublishResourceListener);
    }

    public boolean a(com.aliyun.alink.linksdk.tmp.connect.b bVar, String str, String str2, String str3, boolean z, b bVar2) {
        return bVar.a(str, str2, str3, z, bVar2);
    }

    public boolean a(com.aliyun.alink.linksdk.tmp.connect.b bVar, String str, String str2, boolean z, b bVar2) {
        return a(bVar, (String) null, str, str2, z, bVar2);
    }

    public boolean a(com.aliyun.alink.linksdk.tmp.connect.b bVar, String str, String str2) {
        return bVar.a(str, str2);
    }

    protected boolean a(com.aliyun.alink.linksdk.tmp.connect.b bVar, String str, String str2, String str3, OutputParams outputParams, IPublishResourceListener iPublishResourceListener) {
        return bVar.a(str, str2, str3, outputParams, iPublishResourceListener);
    }

    public boolean a(com.aliyun.alink.linksdk.tmp.connect.b bVar, String str, String str2, byte[] bArr, IPublishResourceListener iPublishResourceListener) {
        return bVar.a(str, str2, bArr, iPublishResourceListener);
    }

    public static ResDescpt.ResElementType a(DeviceModel deviceModel, String str) {
        if (TextUtils.isEmpty(str)) {
            return ResDescpt.ResElementType.SERVICE;
        }
        if (str.equalsIgnoreCase("dev")) {
            return ResDescpt.ResElementType.DISCOVERY;
        }
        if (str.equalsIgnoreCase(TmpConstant.PROPERTY_IDENTIFIER_GET) || str.equalsIgnoreCase(TmpConstant.PROPERTY_IDENTIFIER_SET)) {
            return ResDescpt.ResElementType.PROPERTY;
        }
        if (deviceModel != null && !TextUtils.isEmpty(deviceModel.getEventMethod(str))) {
            return ResDescpt.ResElementType.EVENT;
        }
        if (deviceModel != null && !TextUtils.isEmpty(deviceModel.getServiceMethod(str))) {
            return ResDescpt.ResElementType.SERVICE;
        }
        return ResDescpt.ResElementType.ALCS;
    }
}
