package datasource.channel.reqeust;

import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes3.dex */
public class ProvisionAuthRequest extends BaseApiRequest {
    public String REQUEST_METHOD = "POST";
    public String MTOP_API_NAME = "mtop.alibaba.aicloud.sigMesh.provisionAuth";
    public String MTOP_VERSION = "1.0";
    public boolean MTOP_NEED_ECODE = false;
    public boolean MTOP_NEED_SESSION = false;
    public String API_HOST = "";
    public String API_PATH = "/living/awss/bt/mesh/provision/confirmation/auth";
    public String API_VERSION = "1.0.0";
    public String provisionAuthorizationReq = null;

    public String getProvisionAuthorizationReq() {
        return this.provisionAuthorizationReq;
    }

    public void setProvisionAuthorizationReq(String str) {
        this.provisionAuthorizationReq = str;
    }
}
