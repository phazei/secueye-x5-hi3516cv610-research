package com.aliyun.alink.linksdk.tmp.device.payload.property;

import com.aliyun.alink.linksdk.tmp.device.payload.CommonRequestPayload;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class GetPropertyRequestPayload extends CommonRequestPayload<List<String>> {
    /* JADX WARN: Type inference failed for: r1v2, types: [T, java.util.ArrayList] */
    public GetPropertyRequestPayload(String str, String str2) {
        super(str, str2);
        setMethod("thing.service.property.get");
        this.params = new ArrayList();
    }

    public List<String> getProperty() {
        return (List) this.params;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void setProperty(List<String> list) {
        this.params = list;
    }
}
