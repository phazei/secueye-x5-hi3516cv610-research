package bluetooth.adddevice;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import bean.BleFoundDevice;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AddBleDeviceHandler extends Handler {
    private AddBleDeviceBusiness addBleDeviceBusiness;
    private OnBleDeviceAddListener onBleDeviceAddListener;

    public AddBleDeviceHandler(OnBleDeviceAddListener onBleDeviceAddListener) {
        super(Looper.getMainLooper());
        this.addBleDeviceBusiness = new AddBleDeviceBusiness(this);
        this.onBleDeviceAddListener = onBleDeviceAddListener;
    }

    public void reset() {
        this.addBleDeviceBusiness.reset();
    }

    public void addBleDevices(List<BleFoundDevice> list) {
        this.addBleDeviceBusiness.filterDevice(list);
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (this.onBleDeviceAddListener != null && message.what == 200705) {
            this.onBleDeviceAddListener.onBleDeviceFilterSuccess((List) message.obj);
        }
    }

    public void onDestory() {
        removeCallbacksAndMessages(null);
        this.onBleDeviceAddListener = null;
    }
}
