package datasource.implemention.feiyan.resp;

import com.aliyun.alink.linksdk.connectsdk.BaseApiResponse;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanGetAuthRandomIdForBLEDeviceResp extends BaseApiResponse {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f7864a;

    public FeiyanGetAuthRandomIdForBLEDeviceResp(String str) {
        this.f7864a = str;
    }

    public String getData() {
        return this.f7864a;
    }

    public void setData(String str) {
        this.f7864a = str;
    }
}
