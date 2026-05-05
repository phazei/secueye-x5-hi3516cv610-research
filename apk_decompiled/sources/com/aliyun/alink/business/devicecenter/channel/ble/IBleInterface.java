package com.aliyun.alink.business.devicecenter.channel.ble;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.api.add.DeviceInfo;
import com.aliyun.alink.business.devicecenter.channel.http.DCError;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;

/* JADX INFO: loaded from: classes.dex */
public interface IBleInterface {

    public enum BleChannelState {
        NONE,
        BT_OPEN,
        BT_CLOSED,
        UNBIND,
        BINDING,
        BOUND,
        CONNECTING,
        CONNECTED,
        AUTH_FAILED,
        AUTH_SUCCESSFUL,
        DISCONNECTING,
        DISCONNECTED,
        A2DP_CONNECTING,
        A2DP_CONNECTED,
        A2DP_DISCONNECTING,
        A2DP_DISCONNECTED
    }

    public static class BleDeviceInfo {
        public String deviceName = "";
        public String productKey = "";
        public String sign = "";
        public String version = "";
        public String random = "";

        public String toString() {
            return "BleDeviceInfo{deviceName='" + this.deviceName + "', productKey='" + this.productKey + "', sign='" + this.sign + "', version='" + this.version + "', random='" + this.random + "'}";
        }
    }

    public interface IBleActionCallback {
        void onResponse(int i, byte[] bArr);
    }

    public interface IBleChannelDevice {
        Object getChannelDevice();
    }

    public interface IBleConnectionCallback {
        void onChannelStateChanged(IBleChannelDevice iBleChannelDevice, BleChannelState bleChannelState);
    }

    public interface IBleDeviceInfoCallback {
        void onDeviceInfo(BleDeviceInfo bleDeviceInfo);

        void onError(DCError dCError);
    }

    public interface IBleReceiverCallback {
        void onDataReceived(byte[] bArr);
    }

    public interface IBleScanCallback {
        void onBLEDeviceFound(DeviceInfo deviceInfo);

        void onStartScan();

        void onStopScan();
    }

    boolean channelEncrypt(IBleChannelDevice iBleChannelDevice);

    void connect(String str, IBleConnectionCallback iBleConnectionCallback);

    void deinit();

    void disconnect(String str, IBleConnectionCallback iBleConnectionCallback);

    void getDeviceName(IBleChannelDevice iBleChannelDevice, IBleDeviceInfoCallback iBleDeviceInfoCallback);

    String getType();

    void init(Context context);

    boolean isSupport();

    boolean needGetDeviceName(IBleChannelDevice iBleChannelDevice);

    void registerOnReceivedListener(IBleChannelDevice iBleChannelDevice, IBleReceiverCallback iBleReceiverCallback);

    void sendMessage(IBleChannelDevice iBleChannelDevice, int i, byte[] bArr, IBleActionCallback iBleActionCallback);

    void setConfigParams(DCConfigParams dCConfigParams);

    boolean startScan(IBleScanCallback iBleScanCallback);

    void stopScan(IBleScanCallback iBleScanCallback);

    void unregisterOnReceivedListener(IBleChannelDevice iBleChannelDevice, IBleReceiverCallback iBleReceiverCallback);
}
