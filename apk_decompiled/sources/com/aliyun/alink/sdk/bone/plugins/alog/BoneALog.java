package com.aliyun.alink.sdk.bone.plugins.alog;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.logextra.upload.Log2Cloud;
import com.aliyun.alink.linksdk.logextra.upload.OSSManager;
import com.aliyun.iot.aep.sdk.bridge.base.BaseBoneService;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback;
import com.aliyun.iot.aep.sdk.bridge.invoker.SyncBoneInvoker;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneMethod;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneService;
import com.aliyun.iot.aep.sdk.jsbridge.annotation.ServiceMode;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes2.dex */
@BoneService(mode = ServiceMode.ALWAYS_NEW, name = BoneALog.API_NAME)
public class BoneALog extends BaseBoneService {
    public static final String API_NAME = "BoneLog";

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Removed duplicated region for block: B:20:0x003a  */
    @com.aliyun.iot.aep.sdk.jsbridge.annotation.BoneMethod
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void log(java.lang.String r1, java.lang.String r2, java.lang.String r3, com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback r4) {
        /*
            r0 = this;
            int r2 = r1.hashCode()
            switch(r2) {
                case -1505867908: goto L30;
                case 2283726: goto L26;
                case 65906227: goto L1c;
                case 67232232: goto L12;
                case 2015760738: goto L8;
                default: goto L7;
            }
        L7:
            goto L3a
        L8:
            java.lang.String r2 = "Verbose"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L3a
            r1 = 4
            goto L3b
        L12:
            java.lang.String r2 = "Error"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L3a
            r1 = 2
            goto L3b
        L1c:
            java.lang.String r2 = "Debug"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L3a
            r1 = 0
            goto L3b
        L26:
            java.lang.String r2 = "Info"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L3a
            r1 = 1
            goto L3b
        L30:
            java.lang.String r2 = "Warning"
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L3a
            r1 = 3
            goto L3b
        L3a:
            r1 = -1
        L3b:
            switch(r1) {
                case 0: goto L57;
                case 1: goto L51;
                case 2: goto L4b;
                case 3: goto L45;
                case 4: goto L3f;
                default: goto L3e;
            }
        L3e:
            goto L5c
        L3f:
            java.lang.String r1 = "BonePlugin"
            com.aliyun.alink.linksdk.tools.ALog.d(r1, r3)
            goto L5c
        L45:
            java.lang.String r1 = "BonePlugin"
            com.aliyun.alink.linksdk.tools.ALog.w(r1, r3)
            goto L5c
        L4b:
            java.lang.String r1 = "BonePlugin"
            com.aliyun.alink.linksdk.tools.ALog.e(r1, r3)
            goto L5c
        L51:
            java.lang.String r1 = "BonePlugin"
            com.aliyun.alink.linksdk.tools.ALog.i(r1, r3)
            goto L5c
        L57:
            java.lang.String r1 = "BonePlugin"
            com.aliyun.alink.linksdk.tools.ALog.d(r1, r3)
        L5c:
            org.json.JSONObject r1 = new org.json.JSONObject
            r1.<init>()
            r4.success(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.sdk.bone.plugins.alog.BoneALog.log(java.lang.String, java.lang.String, java.lang.String, com.aliyun.iot.aep.sdk.bridge.core.service.BoneCallback):void");
    }

    @BoneMethod
    public void uploadLog(JSONObject jSONObject, final BoneCallback boneCallback) {
        try {
            Log2Cloud.getInstance();
            Log2Cloud.getInstance().uploadLog(jSONObject, new OSSManager.OSSUploadCallback() { // from class: com.aliyun.alink.sdk.bone.plugins.alog.BoneALog.1
                /* JADX WARN: Type inference fix 'apply assigned field type' failed
                java.lang.UnsupportedOperationException: ArgType.getObject(), call class: class jadx.core.dex.instructions.args.ArgType$UnknownArg
                	at jadx.core.dex.instructions.args.ArgType.getObject(ArgType.java:593)
                	at jadx.core.dex.attributes.nodes.ClassTypeVarsAttr.getTypeVarsMapFor(ClassTypeVarsAttr.java:35)
                	at jadx.core.dex.nodes.utils.TypeUtils.replaceClassGenerics(TypeUtils.java:177)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.insertExplicitUseCast(FixTypesVisitor.java:397)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.tryFieldTypeWithNewCasts(FixTypesVisitor.java:359)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.applyFieldType(FixTypesVisitor.java:309)
                	at jadx.core.dex.visitors.typeinference.FixTypesVisitor.visit(FixTypesVisitor.java:94)
                 */
                public void onUploadSuccess(String str, String str2) {
                    JSONObject jSONObject2 = new JSONObject();
                    try {
                        jSONObject2.put("code", 200);
                        jSONObject2.put("msg", "ok");
                        JSONObject jSONObject3 = new JSONObject();
                        jSONObject3.put("name", str);
                        if (!TextUtils.isEmpty(str2)) {
                            jSONObject3.put("deviceLogFileId", str2);
                        }
                        jSONObject2.put("data", jSONObject3);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    boneCallback.success(jSONObject2);
                }

                public void onUploadFailed(int i, String str) {
                    boneCallback.failed(String.valueOf(i), str, "");
                }
            });
        } catch (Throwable unused) {
            boneCallback.failed(SyncBoneInvoker.ERROR_SUB_CODE_EXCEPTION, "Log2Cloud not exist", "");
        }
    }
}
