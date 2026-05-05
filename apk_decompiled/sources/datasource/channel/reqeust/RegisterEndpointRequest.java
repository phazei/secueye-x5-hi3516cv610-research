package datasource.channel.reqeust;

import com.aliyun.alink.linksdk.connectsdk.BaseApiRequest;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class RegisterEndpointRequest extends BaseApiRequest {
    public RegisterRequest request;
    public String REQUEST_METHOD = "POST";
    public String MTOP_API_NAME = "IotCommonService.invokeEventMethod";
    public String MTOP_VERSION = "1.0";
    public boolean MTOP_NEED_ECODE = false;
    public boolean MTOP_NEED_SESSION = false;
    public String API_HOST = "";
    public String API_PATH = "/living/awss/bt/devices/register";
    public String API_VERSION = "1.0.0";

    public static class RegisterRequest {
        public List<String> abilities;
        public String connectType;
        public List<String> dataTags;
        public String model = "Android";
        public String productKey;
        public String type;
        public String uuid;
        public String version;

        public List<String> getAbilities() {
            return this.abilities;
        }

        public String getConnectType() {
            return this.connectType;
        }

        public List<String> getDataTags() {
            return this.dataTags;
        }

        public String getModel() {
            return this.model;
        }

        public String getProductKey() {
            return this.productKey;
        }

        public String getType() {
            return this.type;
        }

        public String getUuid() {
            return this.uuid;
        }

        public String getVersion() {
            return this.version;
        }

        public void setAbilities(List<String> list) {
            this.abilities = list;
        }

        public void setConnectType(String str) {
            this.connectType = str;
        }

        public void setDataTags(List<String> list) {
            this.dataTags = list;
        }

        public void setModel(String str) {
            this.model = str;
        }

        public void setProductKey(String str) {
            this.productKey = str;
        }

        public void setType(String str) {
            this.type = str;
        }

        public void setUuid(String str) {
            this.uuid = str;
        }

        public void setVersion(String str) {
            this.version = str;
        }
    }

    public RegisterRequest getRequest() {
        return this.request;
    }

    public void setRequest(RegisterRequest registerRequest) {
        this.request = registerRequest;
    }
}
