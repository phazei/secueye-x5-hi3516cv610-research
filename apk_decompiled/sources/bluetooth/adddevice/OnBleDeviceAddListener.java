package bluetooth.adddevice;

import bean.BleFoundDevice;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public interface OnBleDeviceAddListener {
    void onBleDeviceFilterSuccess(List<BleFoundDevice> list);
}
