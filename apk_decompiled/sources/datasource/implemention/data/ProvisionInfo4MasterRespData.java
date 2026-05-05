package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import datasource.bean.ProvisionInfo4Master;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class ProvisionInfo4MasterRespData extends BaseDataBean implements IMTOPDataObject {
    public ProvisionInfo4Master model;

    public ProvisionInfo4Master getModel() {
        return this.model;
    }

    public void setModel(ProvisionInfo4Master provisionInfo4Master) {
        this.model = provisionInfo4Master;
    }
}
