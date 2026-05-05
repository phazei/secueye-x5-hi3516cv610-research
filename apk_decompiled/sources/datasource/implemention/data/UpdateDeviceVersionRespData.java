package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class UpdateDeviceVersionRespData extends BaseDataBean implements IMTOPDataObject {
    public boolean model;

    public boolean getModel() {
        return this.model;
    }

    public void setModel(boolean z) {
        this.model = z;
    }
}
