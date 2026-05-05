package datasource.channel.reqeust;

import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;

/* JADX INFO: loaded from: classes3.dex */
public class GetProvisionInfo4MasterRequest extends BaseApiRequest {
    public String REQUEST_METHOD = "POST";
    public String MTOP_API_NAME = "mtop.alibaba.aicloud.sigMesh.getProvisionInfo4MasterV1";
    public String MTOP_VERSION = "1.0";
    public boolean MTOP_NEED_ECODE = false;
    public boolean MTOP_NEED_SESSION = false;
    public String API_HOST = "";
    public String API_PATH = "/living/awss/bt/mesh/provisioner/config/get";
    public String API_VERSION = "1.0.2";
    public String uuid = null;

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String str) {
        this.uuid = str;
    }

    public String toString() {
        return "GetProvisionInfo4MasterRequest{REQUEST_METHOD='" + this.REQUEST_METHOD + "', MTOP_API_NAME='" + this.MTOP_API_NAME + "', MTOP_VERSION='" + this.MTOP_VERSION + "', MTOP_NEED_ECODE=" + this.MTOP_NEED_ECODE + ", MTOP_NEED_SESSION=" + this.MTOP_NEED_SESSION + ", API_HOST='" + this.API_HOST + "', API_PATH='" + this.API_PATH + "', API_VERSION='" + this.API_VERSION + "', uuid='" + this.uuid + "'}";
    }
}
