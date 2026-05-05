package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import datasource.bean.Sigmesh;
import java.util.List;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class IoTWakeUpData extends BaseDataBean implements IMTOPDataObject {
    public Extensions model;

    public static class Extensions {
        public List<Sigmesh> sigmesh;

        public List<Sigmesh> getSigmesh() {
            return this.sigmesh;
        }

        public void setSigmesh(List<Sigmesh> list) {
            this.sigmesh = list;
        }
    }

    public Extensions getModel() {
        return this.model;
    }

    public void setModel(Extensions extensions) {
        this.model = extensions;
    }
}
