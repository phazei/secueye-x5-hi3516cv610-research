package com.aliyun.iot.aep.component.router;

import android.os.Bundle;

/* JADX INFO: loaded from: classes2.dex */
public class RouterRequest {
    Bundle mBundle;
    String mUrl;

    public RouterRequest(String str, Bundle bundle) {
        this.mUrl = str;
        this.mBundle = bundle;
    }

    public Bundle getBundle() {
        return this.mBundle;
    }

    public String getUrl() {
        return this.mUrl;
    }

    public void setBundle(Bundle bundle) {
        this.mBundle = bundle;
    }

    public void setUrl(String str) {
        this.mUrl = str;
    }
}
