package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import datasource.bean.Sigmesh;
import java.util.List;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class IotDeviceControlRespData extends BaseDataBean implements IMTOPDataObject {
    public Extensions extensions;
    public String model;

    public static class Extensions {
        public List<Sigmesh> sigmesh;

        public List<Sigmesh> getSigmesh() {
            return this.sigmesh;
        }

        public void setSigmesh(List<Sigmesh> list) {
            this.sigmesh = list;
        }
    }

    public Extensions getExtensions() {
        return this.extensions;
    }

    public String getModel() {
        return this.model;
    }

    public void setExtensions(Extensions extensions) {
        this.extensions = extensions;
    }

    public void setModel(String str) {
        this.model = str;
    }
}
