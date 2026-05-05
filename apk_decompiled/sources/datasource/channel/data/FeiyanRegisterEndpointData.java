package datasource.channel.data;

import com.aliyun.alink.linksdk.connectsdk.BaseApiResponse;

/* JADX INFO: loaded from: classes3.dex */
public class FeiyanRegisterEndpointData extends BaseApiResponse {
    public int code;
    public String data;
    public String id;
    public String localizedMsg;
    public String message;

    public int getCode() {
        return this.code;
    }

    public String getData() {
        return this.data;
    }

    public String getId() {
        return this.id;
    }

    public String getLocalizedMsg() {
        return this.localizedMsg;
    }

    public String getMessage() {
        return this.message;
    }

    public void setCode(int i) {
        this.code = i;
    }

    public void setData(String str) {
        this.data = str;
    }

    public void setId(String str) {
        this.id = str;
    }

    public void setLocalizedMsg(String str) {
        this.localizedMsg = str;
    }

    public void setMessage(String str) {
        this.message = str;
    }
}
