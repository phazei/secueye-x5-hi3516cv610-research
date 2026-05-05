package com.aliyun.alink.linksdk.tmp.connect.entity.cmp;

import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.cmp.api.CommonRequest;
import com.aliyun.alink.linksdk.tmp.data.device.Option;

/* JADX INFO: compiled from: CpRequest.java */
/* JADX INFO: loaded from: classes2.dex */
public class h extends com.aliyun.alink.linksdk.tmp.connect.d<CommonRequest> {
    @Override // com.aliyun.alink.linksdk.tmp.connect.d
    public boolean a() {
        return false;
    }

    public h(CommonRequest commonRequest) {
        super(commonRequest);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void a(Option option) {
        if (this.f4244a == 0) {
            return;
        }
        if (option == null) {
            ((CommonRequest) this.f4244a).type = AlcsCoAPConstant.Type.CON;
            ((CommonRequest) this.f4244a).rspType = 0;
        } else {
            ((CommonRequest) this.f4244a).type = AlcsCoAPConstant.Type.valueOf(option.getQoSLevel().getValue());
            ((CommonRequest) this.f4244a).rspType = Integer.valueOf(!option.isNeedRsp() ? 1 : 0);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.aliyun.alink.linksdk.tmp.connect.d
    public String d() {
        return ((CommonRequest) this.f4244a).topic;
    }
}
