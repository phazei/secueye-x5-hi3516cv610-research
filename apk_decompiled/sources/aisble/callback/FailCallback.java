package aisble.callback;

import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public interface FailCallback {
    public static final int REASON_AUTH_FAILED = -500;
    public static final int REASON_BLUETOOTH_DISABLED = -100;
    public static final int REASON_BOUND_FAILED = -204;
    public static final int REASON_DEVICE_DISCONNECTED = -1;
    public static final int REASON_DEVICE_NOT_SUPPORTED = -2;
    public static final int REASON_DEVICE_NOT_SUPPORT_ABILITY = -207;
    public static final int REASON_EXIST_DOWNLOAD_TASK = -400;
    public static final int REASON_ILLEGAL_PARAMETER = -302;
    public static final int REASON_ILLEGAL_REPLY = -206;
    public static final int REASON_IN_CONNECTING = -203;
    public static final int REASON_IO_EXCEPTION = -200;
    public static final int REASON_MD5_NOT_MATCH = -402;
    public static final int REASON_NETWORK_FAILED = -300;
    public static final int REASON_NETWORK_NOT_INITIALIZED = -303;
    public static final int REASON_NOT_INITIALIZED = -202;
    public static final int REASON_NOT_SUPPORT_BIND_OPERATION = -205;
    public static final int REASON_NO_WRITE_PERMISSION_OR_INSUFFICIENT_DISK = -401;
    public static final int REASON_NULL_ATTRIBUTE = -3;
    public static final int REASON_OTHER = -201;
    public static final int REASON_REPLY_OPERATION_FAILED = -301;
    public static final int REASON_REQUEST_FAILED = -4;
    public static final int REASON_TIMEOUT = -5;
    public static final int REASON_VALIDATION = -6;

    void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i);
}
