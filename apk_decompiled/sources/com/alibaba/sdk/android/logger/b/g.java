package com.alibaba.sdk.android.logger.b;

import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.taobao.accs.AccsClientConfig;

/* JADX INFO: loaded from: classes.dex */
public class g {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private String f2915a;

    public g(String str) {
        this.f2915a = str;
        if (str == null) {
            this.f2915a = AccsClientConfig.DEFAULT_CONFIGTAG;
        }
    }

    public String a(Object obj) {
        String simpleName;
        if (obj == null) {
            simpleName = "";
        } else if (obj instanceof Class) {
            simpleName = ((Class) obj).getSimpleName();
        } else if (obj instanceof String) {
            simpleName = (String) obj;
        } else {
            simpleName = obj.getClass().getSimpleName() + "@" + obj.hashCode();
        }
        return this.f2915a + OpenAccountUIConstants.UNDER_LINE + simpleName;
    }
}
