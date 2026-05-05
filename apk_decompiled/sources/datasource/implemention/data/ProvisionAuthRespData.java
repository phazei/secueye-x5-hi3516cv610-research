package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class ProvisionAuthRespData extends BaseDataBean implements IMTOPDataObject {
    public Boolean model;

    public Boolean getModel() {
        return this.model;
    }

    public void setModel(Boolean bool) {
        this.model = bool;
    }
}
