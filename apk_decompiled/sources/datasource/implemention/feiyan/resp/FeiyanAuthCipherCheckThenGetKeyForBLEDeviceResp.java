package datasource.implemention.feiyan.resp;

import com.aliyun.alink.linksdk.connectsdk.BaseApiResponse;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanAuthCipherCheckThenGetKeyForBLEDeviceResp extends BaseApiResponse {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f7863a;

    public FeiyanAuthCipherCheckThenGetKeyForBLEDeviceResp(String str) {
        this.f7863a = str;
    }

    public String getData() {
        return this.f7863a;
    }

    public void setData(String str) {
        this.f7863a = str;
    }
}
