package bluetooth.adddevice;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import bean.FoundDeviceListItem;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DeviceAddHandler extends Handler {
    private DeviceAddBusiness deviceAddBusiness;
    private OnDeviceAddListener onDeviceAddListener;

    public DeviceAddHandler(OnDeviceAddListener onDeviceAddListener) {
        super(Looper.getMainLooper());
        this.onDeviceAddListener = onDeviceAddListener;
        this.deviceAddBusiness = new DeviceAddBusiness(this);
    }

    public void getSupportDeviceListFromSever() {
        this.deviceAddBusiness.getSupportDeviceListFromSever();
    }

    public void filterDevice(List<FoundDeviceListItem> list) {
        this.deviceAddBusiness.filterDevice(list);
    }

    public void reset() {
        this.deviceAddBusiness.reset();
    }

    @Override // android.os.Handler
    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (this.onDeviceAddListener == null) {
            return;
        }
        int i = message.what;
        if (i == 69633) {
            this.onDeviceAddListener.onSupportDeviceSuccess((ArrayList) message.obj);
            return;
        }
        if (i != 135169) {
            if (i != 200705) {
                return;
            }
            this.onDeviceAddListener.onFilterComplete((List) message.obj);
            return;
        }
        Object obj = message.obj;
        if (message == null) {
            this.onDeviceAddListener.onSupportDeviceSuccess(new ArrayList());
        } else {
            this.onDeviceAddListener.showToast(obj.toString());
        }
    }

    public void onDestory() {
        removeCallbacksAndMessages(null);
        this.onDeviceAddListener = null;
    }
}
