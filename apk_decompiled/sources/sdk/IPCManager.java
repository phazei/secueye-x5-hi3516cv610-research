package sdk;

import android.content.Context;
import bean.DevPictureFile;
import bean.TimeSection;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import java.util.List;

/* JADX INFO: loaded from: classes4.dex */
public class IPCManager {
    private IPCManager() {
    }

    private static class IPCManagerHolder {
        private static IPCManager ipcManager = new IPCManager();

        private IPCManagerHolder() {
        }
    }

    public static IPCManager getInstance() {
        return IPCManagerHolder.ipcManager;
    }

    public void init(Context context, String str) {
        DevManager.getDevManager().init(context);
        LinkVisionAPI.getInstance().init(str);
    }

    public IPCDevice getDevice(String str, IPanelCallback iPanelCallback) {
        return DevManager.getDevManager().getIPCDevice(str, iPanelCallback);
    }

    public IPCDevice getDevice(String str) {
        return DevManager.getDevManager().getIPCDevice(str);
    }

    public void queryTimeTemplate(int i, int i2, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().queryTimeTemplate(i, i2, ioTCallback);
    }

    public void createTimeTemplate(String str, boolean z, List<TimeSection> list, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().createTimeTemplate(str, z, list, ioTCallback);
    }

    public void updateTimeTemplate(String str, String str2, boolean z, List<TimeSection> list, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().updateTimeTemplate(str, str2, z, list, ioTCallback);
    }

    public void getTimeTemplate(String str, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().getTimeTemplate(str, ioTCallback);
    }

    public void deleteTimeTemplate(String str, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().deleteTimeTemplate(str, ioTCallback);
    }

    public void setEventRecordPlan(String str, String str2, int i, int i2, String str3, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().setRecordPlan(str, str2, i, i2, str3, ioTCallback);
    }

    public void updateEventRecordPlan(String str, String str2, String str3, int i, int i2, String str4, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().updateRecordPlan(str, str2, str3, i, i2, str4, ioTCallback);
    }

    public void deleteEventRecordPlan(String str, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().deleteRecordPlan(str, ioTCallback);
    }

    public void getEventRecordPlan(String str, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().getRecordPlan(str, ioTCallback);
    }

    public void queryEventRecordPlan(int i, int i2, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().queryRecordPlan(i, i2, ioTCallback);
    }

    public void batchDeleteDevPictureFile(List<DevPictureFile> list, IoTCallback ioTCallback) {
        LinkVisionAPI.getInstance().batchDeleteDevPictureFile(list, ioTCallback);
    }
}
