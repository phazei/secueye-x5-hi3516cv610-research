package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import datasource.bean.ProvisionInfo;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class GetProvisionInfoRespData extends BaseDataBean implements IMTOPDataObject {
    public ProvisionInfo model;

    public ProvisionInfo getModel() {
        return this.model;
    }

    public void setModel(ProvisionInfo provisionInfo) {
        this.model = provisionInfo;
    }
}
