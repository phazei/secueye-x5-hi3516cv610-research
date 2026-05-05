package com.aliyun.alink.linksdk.tmp.data.device;

import com.aliyun.alink.linksdk.tmp.utils.TmpEnum;

/* JADX INFO: loaded from: classes2.dex */
public class Option {
    protected boolean mNeedRsp = true;
    protected TmpEnum.QoSLevel mQoSLevel;

    public Option(TmpEnum.QoSLevel qoSLevel) {
        this.mQoSLevel = qoSLevel;
    }

    public TmpEnum.QoSLevel getQoSLevel() {
        return this.mQoSLevel;
    }

    public void setQoSLevel(TmpEnum.QoSLevel qoSLevel) {
        this.mQoSLevel = qoSLevel;
    }

    public boolean isNeedRsp() {
        return this.mNeedRsp;
    }

    public void setNeedRsp(boolean z) {
        this.mNeedRsp = z;
    }
}
