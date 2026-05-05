package datasource.bean;

import com.alibaba.fastjson.JSONObject;
import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class IoTGatewayEvent {

    /* JADX INFO: renamed from: event, reason: collision with root package name */
    public EventBean f7859event;

    public static class EventBean {
        public String name;
        public String namespace;
        public JSONObject payload;

        public static class PayloadBean {
            public List<String> abilities;
            public String connectType;
            public List<String> dataTags;
            public String model = "Android";
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

        public String getName() {
            return this.name;
        }

        public String getNamespace() {
            return this.namespace;
        }

        public JSONObject getPayload() {
            return this.payload;
        }

        public void setName(String str) {
            this.name = str;
        }

        public void setNamespace(String str) {
            this.namespace = str;
        }

        public void setPayload(JSONObject jSONObject) {
            this.payload = jSONObject;
        }
    }

    public EventBean getEvent() {
        return this.f7859event;
    }

    public void setEvent(EventBean eventBean) {
        this.f7859event = eventBean;
    }
}
