package datasource.implemention.data;

import com.alibaba.ailabs.tg.mtop.data.BaseDataBean;
import datasource.implemention.model.GenieBoxOnlineStatus;
import java.util.Map;
import mtopsdk.mtop.domain.IMTOPDataObject;

/* JADX INFO: loaded from: classes3.dex */
public class IotGetListDeviceBaseInfoWithStatusRespData extends BaseDataBean implements IMTOPDataObject {
    public Map<String, GenieBoxOnlineStatus> model;

    public Map<String, GenieBoxOnlineStatus> getModel() {
        return this.model;
    }

    public void setModel(Map<String, GenieBoxOnlineStatus> map) {
        this.model = map;
    }
}
