package com.aliyun.iot.aep.sdk.bridge.validator;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.bridge.core.context.JSContext;
import com.aliyun.iot.aep.sdk.bridge.core.service.BoneCall;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class BoneValidatorManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static List<BoneValidator> f4629a = new LinkedList();

    public static synchronized BoneValidator getDefaultValidator() {
        ALog.d("BoneValidatorManager", "getDefaultValidator is called");
        return new a(f4629a);
    }

    public static synchronized void add(BoneValidator boneValidator) {
        if (boneValidator == null) {
            throw new IllegalArgumentException("validator can not be null");
        }
        ALog.d("BoneValidatorManager", "add " + boneValidator);
        if (f4629a.contains(boneValidator)) {
            return;
        }
        ALog.d("BoneValidatorManager", "add " + boneValidator);
        f4629a.add(boneValidator);
    }

    public static synchronized void remove(BoneValidator boneValidator) {
        if (boneValidator == null) {
            throw new IllegalArgumentException("validator can not be null");
        }
        f4629a.remove(boneValidator);
    }

    static class a implements BoneValidator {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private int f4630a = 0;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private int f4631b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private List<BoneValidator> f4632c;

        static /* synthetic */ int a(a aVar) {
            int i = aVar.f4630a;
            aVar.f4630a = i + 1;
            return i;
        }

        a(List<BoneValidator> list) {
            this.f4631b = list.size();
            this.f4632c = new ArrayList(list);
        }

        @Override // com.aliyun.iot.aep.sdk.bridge.validator.BoneValidator
        public void validate(JSContext jSContext, BoneCall boneCall, BoneValidateListener boneValidateListener) {
            if (jSContext == null) {
                throw new IllegalArgumentException("context can not be null");
            }
            if (jSContext.getCurrentActivity() == null) {
                ALog.d("DefaultBoneValidator", "ignore call after destroy");
                return;
            }
            if (TextUtils.isEmpty(jSContext.getCurrentUrl())) {
                throw new IllegalArgumentException("jsContext.getCurrentUrl can not be empty");
            }
            if (boneCall == null) {
                throw new IllegalArgumentException("call can not be null");
            }
            if (TextUtils.isEmpty(boneCall.serviceId)) {
                throw new IllegalArgumentException("call.serviceId can not be empty");
            }
            if (TextUtils.isEmpty(boneCall.methodName)) {
                throw new IllegalArgumentException("call.methodName can not be empty");
            }
            if (boneCall.mode == null) {
                throw new IllegalArgumentException("call.mode can not be null");
            }
            if (boneValidateListener == null) {
                throw new IllegalArgumentException("listener can not be null");
            }
            a(jSContext, boneCall, boneValidateListener);
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void a(final JSContext jSContext, final BoneCall boneCall, final BoneValidateListener boneValidateListener) {
            int i = this.f4630a;
            if (i == this.f4631b) {
                try {
                    boneValidateListener.onAuthorized();
                    return;
                } catch (Exception e) {
                    ALog.e("BoneValidatorManager", "exception happens when call listener.onAuthorized()", e);
                    e.printStackTrace();
                    return;
                }
            }
            this.f4632c.get(i).validate(jSContext, boneCall, new BoneValidateListener() { // from class: com.aliyun.iot.aep.sdk.bridge.validator.BoneValidatorManager.a.1
                @Override // com.aliyun.iot.aep.sdk.bridge.validator.BoneValidateListener
                public void onAuthorized() {
                    a.a(a.this);
                    a.this.a(jSContext, boneCall, boneValidateListener);
                }

                @Override // com.aliyun.iot.aep.sdk.bridge.validator.BoneValidateListener
                public void onPermissionDie(String str, String str2, String str3) {
                    try {
                        boneValidateListener.onPermissionDie(str, str2, str3);
                    } catch (Exception e2) {
                        ALog.e("BoneValidatorManager", "exception happens when call listener.onAuthorized()", e2);
                        e2.printStackTrace();
                    }
                }
            });
        }
    }
}
