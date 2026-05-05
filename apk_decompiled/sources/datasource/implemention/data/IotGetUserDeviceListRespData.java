package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import datasource.bean.IotDeviceInfo;
import java.util.List;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class IotGetUserDeviceListRespData extends BaseDataBean implements IMTOPDataObject {
    public List<IotDeviceInfo> model;

    public List<IotDeviceInfo> getModel() {
        return this.model;
    }

    public void setModel(List<IotDeviceInfo> list) {
        this.model = list;
    }
}
