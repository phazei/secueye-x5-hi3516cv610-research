package datasource.bean.local;

import java.util.List;

/* JADX INFO: loaded from: classes3.dex */
public class DeviceBindModel extends BaseDeviceInfoModel {
    public List<DeviceBindItem> data;

    public List<DeviceBindItem> getData() {
        return this.data;
    }

    public void setData(List<DeviceBindItem> list) {
        this.data = list;
    }
}
