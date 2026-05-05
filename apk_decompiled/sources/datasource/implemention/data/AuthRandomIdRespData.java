package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class AuthRandomIdRespData extends BaseDataBean implements IMTOPDataObject {
    public String model;

    public String getModel() {
        return this.model;
    }

    public void setModel(String str) {
        this.model = str;
    }
}
