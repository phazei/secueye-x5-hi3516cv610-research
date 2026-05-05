package sdk;

import android.content.Context;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public class DevManager {
    private Context context;
    private Map<String, IPCDevice> deviceList;

    private DevManager() {
        this.deviceList = new HashMap();
    }

    private static class PanelDeviceHolder {
        private static final DevManager devManager = new DevManager();

        private PanelDeviceHolder() {
        }
    }

    public static DevManager getDevManager() {
        return PanelDeviceHolder.devManager;
    }

    public void init(Context context) {
        this.context = context;
    }

    public IPCDevice getIPCDevice(String str) {
        Map<String, IPCDevice> map = this.deviceList;
        if (map != null) {
            if (!map.isEmpty() && this.deviceList.containsKey(str)) {
                IPCDevice iPCDevice = this.deviceList.get(str);
                if (iPCDevice.isValid()) {
                    return iPCDevice;
                }
                this.deviceList.remove(str);
                IPCDevice iPCDevice2 = new IPCDevice(this.context, str);
                this.deviceList.put(str, iPCDevice2);
                return iPCDevice2;
            }
        } else {
            this.deviceList = new HashMap();
        }
        IPCDevice iPCDevice3 = new IPCDevice(this.context, str);
        this.deviceList.put(str, iPCDevice3);
        return iPCDevice3;
    }

    public IPCDevice getIPCDevice(String str, IPanelCallback iPanelCallback) {
        Map<String, IPCDevice> map = this.deviceList;
        if (map != null) {
            if (!map.isEmpty() && this.deviceList.containsKey(str)) {
                IPCDevice iPCDevice = this.deviceList.get(str);
                if (iPCDevice.isValid() && iPCDevice.getPanelCallback() == iPanelCallback) {
                    return iPCDevice;
                }
                this.deviceList.remove(str);
                IPCDevice iPCDevice2 = new IPCDevice(this.context, str, iPanelCallback);
                this.deviceList.put(str, iPCDevice2);
                return iPCDevice2;
            }
        } else {
            this.deviceList = new HashMap();
        }
        IPCDevice iPCDevice3 = new IPCDevice(this.context, str, iPanelCallback);
        this.deviceList.put(str, iPCDevice3);
        return iPCDevice3;
    }
}
