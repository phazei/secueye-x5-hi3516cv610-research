package com.aliyun.alink.business.devicecenter.api.add;

import android.text.TextUtils;
import java.io.Serializable;

/* JADX INFO: loaded from: classes.dex */
public class AwssVersion implements Serializable {
    public String ap;
    public String router;
    public String smartconfig;
    public String softap;
    public String zconfig;

    public AwssVersion copy() {
        AwssVersion awssVersion = new AwssVersion();
        awssVersion.smartconfig = this.smartconfig;
        awssVersion.zconfig = this.zconfig;
        awssVersion.router = this.router;
        awssVersion.ap = this.ap;
        awssVersion.softap = this.softap;
        return awssVersion;
    }

    public boolean isValid() {
        return (TextUtils.isEmpty(this.smartconfig) && TextUtils.isEmpty(this.zconfig) && TextUtils.isEmpty(this.router) && TextUtils.isEmpty(this.ap)) ? false : true;
    }

    public boolean onLySupportZero() {
        return TextUtils.isEmpty(this.smartconfig) && !TextUtils.isEmpty(this.zconfig) && TextUtils.isEmpty(this.router) && TextUtils.isEmpty(this.ap);
    }

    public boolean supportPhoneAp() {
        return !TextUtils.isEmpty(this.ap);
    }

    public boolean supportRouter() {
        return !TextUtils.isEmpty(this.router);
    }

    public boolean supportSmartConfig() {
        return !TextUtils.isEmpty(this.smartconfig);
    }

    public boolean supportZero() {
        return !TextUtils.isEmpty(this.zconfig);
    }

    public String toString() {
        return "{\"smartconfig\":\"" + this.smartconfig + "\",\"zconfig\":\"" + this.zconfig + "\",\"router\":\"" + this.router + "\",\"ap\":\"" + this.ap + "\",\"softap\":\"" + this.softap + "\"}";
    }
}
