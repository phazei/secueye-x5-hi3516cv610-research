package com.aliyun.alink.linksdk.tmp.device.request;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.io.Serializable;

/* JADX INFO: loaded from: classes2.dex */
public class GateWayResponse<T> implements Serializable {
    public int code;
    public T data;
    public String localizedMsg;
    public String message;

    public void setData(T t) {
        this.data = t;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("code:");
        sb.append(this.code);
        sb.append("message:");
        sb.append(TextUtils.isEmpty(this.message) ? TmpConstant.GROUP_ROLE_UNKNOWN : this.message);
        sb.append("data:");
        sb.append(this.data == null ? TmpConstant.GROUP_ROLE_UNKNOWN : this.message);
        return sb.toString();
    }
}
