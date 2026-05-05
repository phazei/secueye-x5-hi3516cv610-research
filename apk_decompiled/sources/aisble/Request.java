package aisble;

import aisble.callback.BeforeCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.SuccessCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.ConditionVariable;
import android.os.Looper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public abstract class Request {
    public BeforeCallback beforeCallback;
    public final BluetoothGattCharacteristic characteristic;
    public final BluetoothGattDescriptor descriptor;
    public boolean enqueued;
    public FailCallback failCallback;
    public boolean finished;
    public BeforeCallback internalBeforeCallback;
    public FailCallback internalFailCallback;
    public SuccessCallback internalSuccessCallback;
    public InvalidRequestCallback invalidRequestCallback;
    public BleManager manager;
    public SuccessCallback successCallback;
    public final ConditionVariable syncLock;
    public final Type type;

    final class RequestCallback implements SuccessCallback, FailCallback, InvalidRequestCallback {
        public static final int REASON_REQUEST_INVALID = -1000000;
        public int status = 0;

        public RequestCallback() {
        }

        public boolean isSuccess() {
            return this.status == 0;
        }

        @Override // aisble.callback.InvalidRequestCallback
        public void onInvalidRequest() {
            this.status = REASON_REQUEST_INVALID;
            Request.this.syncLock.open();
        }

        @Override // aisble.callback.SuccessCallback
        public void onRequestCompleted(@NonNull BluetoothDevice bluetoothDevice) {
            Request.this.syncLock.open();
        }

        @Override // aisble.callback.FailCallback
        public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
            this.status = i;
            Request.this.syncLock.open();
        }
    }

    enum Type {
        SET,
        CONNECT,
        DISCONNECT,
        CREATE_BOND,
        REMOVE_BOND,
        WRITE,
        READ,
        WRITE_DESCRIPTOR,
        READ_DESCRIPTOR,
        BEGIN_RELIABLE_WRITE,
        EXECUTE_RELIABLE_WRITE,
        ABORT_RELIABLE_WRITE,
        ENABLE_NOTIFICATIONS,
        ENABLE_INDICATIONS,
        DISABLE_NOTIFICATIONS,
        DISABLE_INDICATIONS,
        WAIT_FOR_NOTIFICATION,
        WAIT_FOR_INDICATION,
        READ_BATTERY_LEVEL,
        ENABLE_BATTERY_LEVEL_NOTIFICATIONS,
        DISABLE_BATTERY_LEVEL_NOTIFICATIONS,
        ENABLE_SERVICE_CHANGED_INDICATIONS,
        REQUEST_MTU,
        REQUEST_CONNECTION_PRIORITY,
        SET_PREFERRED_PHY,
        READ_PHY,
        READ_RSSI,
        REFRESH_CACHE,
        SLEEP
    }

    public Request(@NonNull Type type) {
        this.type = type;
        this.characteristic = null;
        this.descriptor = null;
        this.syncLock = new ConditionVariable(true);
    }

    public static void assertNotMainThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new IllegalStateException("Cannot execute synchronous operation from the UI thread.");
        }
    }

    @NonNull
    public static ConnectRequest connect(@NonNull BluetoothDevice bluetoothDevice) {
        return new ConnectRequest(Type.CONNECT, bluetoothDevice);
    }

    @NonNull
    @Deprecated
    public static SimpleRequest createBond() {
        return new SimpleRequest(Type.CREATE_BOND);
    }

    @NonNull
    public static DisconnectRequest disconnect() {
        return new DisconnectRequest(Type.DISCONNECT);
    }

    @NonNull
    public static SimpleRequest newAbortReliableWriteRequest() {
        return new SimpleRequest(Type.ABORT_RELIABLE_WRITE);
    }

    @NonNull
    public static SimpleRequest newBeginReliableWriteRequest() {
        return new SimpleRequest(Type.BEGIN_RELIABLE_WRITE);
    }

    @NonNull
    @Deprecated
    public static ConnectionPriorityRequest newConnectionPriorityRequest(int i) {
        return new ConnectionPriorityRequest(Type.REQUEST_CONNECTION_PRIORITY, i);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newDisableBatteryLevelNotificationsRequest() {
        return new WriteRequest(Type.DISABLE_BATTERY_LEVEL_NOTIFICATIONS);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newDisableIndicationsRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WriteRequest(Type.DISABLE_INDICATIONS, bluetoothGattCharacteristic);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newDisableNotificationsRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WriteRequest(Type.DISABLE_NOTIFICATIONS, bluetoothGattCharacteristic);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newEnableBatteryLevelNotificationsRequest() {
        return new WriteRequest(Type.ENABLE_BATTERY_LEVEL_NOTIFICATIONS);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newEnableIndicationsRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WriteRequest(Type.ENABLE_INDICATIONS, bluetoothGattCharacteristic);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newEnableNotificationsRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WriteRequest(Type.ENABLE_NOTIFICATIONS, bluetoothGattCharacteristic);
    }

    @NonNull
    public static WriteRequest newEnableServiceChangedIndicationsRequest() {
        return new WriteRequest(Type.ENABLE_SERVICE_CHANGED_INDICATIONS);
    }

    @NonNull
    public static SimpleRequest newExecuteReliableWriteRequest() {
        return new SimpleRequest(Type.EXECUTE_RELIABLE_WRITE);
    }

    @NonNull
    @Deprecated
    public static MtuRequest newMtuRequest(@IntRange(from = 23, to = 517) int i) {
        return new MtuRequest(Type.REQUEST_MTU, i);
    }

    @NonNull
    @Deprecated
    public static ReadRequest newReadBatteryLevelRequest() {
        return new ReadRequest(Type.READ_BATTERY_LEVEL);
    }

    @NonNull
    @Deprecated
    public static PhyRequest newReadPhyRequest() {
        return new PhyRequest(Type.READ_PHY);
    }

    @NonNull
    @Deprecated
    public static ReadRequest newReadRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new ReadRequest(Type.READ, bluetoothGattCharacteristic);
    }

    @NonNull
    @Deprecated
    public static ReadRssiRequest newReadRssiRequest() {
        return new ReadRssiRequest(Type.READ_RSSI);
    }

    @NonNull
    @Deprecated
    public static SimpleRequest newRefreshCacheRequest() {
        return new SimpleRequest(Type.REFRESH_CACHE);
    }

    @NonNull
    public static ReliableWriteRequest newReliableWriteRequest() {
        return new ReliableWriteRequest();
    }

    @NonNull
    @Deprecated
    public static PhyRequest newSetPreferredPhyRequest(int i, int i2, int i3) {
        return new PhyRequest(Type.SET_PREFERRED_PHY, i, i2, i3);
    }

    @NonNull
    @Deprecated
    public static SleepRequest newSleepRequest(@IntRange(from = 0) long j) {
        return new SleepRequest(Type.SLEEP, j);
    }

    @NonNull
    @Deprecated
    public static WaitForValueChangedRequest newWaitForIndicationRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WaitForValueChangedRequest(Type.WAIT_FOR_INDICATION, bluetoothGattCharacteristic);
    }

    @NonNull
    @Deprecated
    public static WaitForValueChangedRequest newWaitForNotificationRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new WaitForValueChangedRequest(Type.WAIT_FOR_NOTIFICATION, bluetoothGattCharacteristic);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newWriteRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr) {
        return new WriteRequest(Type.WRITE, bluetoothGattCharacteristic, bArr, 0, bArr != null ? bArr.length : 0, bluetoothGattCharacteristic != null ? bluetoothGattCharacteristic.getWriteType() : 2);
    }

    @NonNull
    @Deprecated
    public static SimpleRequest removeBond() {
        return new SimpleRequest(Type.REMOVE_BOND);
    }

    @NonNull
    public Request before(@NonNull BeforeCallback beforeCallback) {
        this.beforeCallback = beforeCallback;
        return this;
    }

    @NonNull
    public Request done(@NonNull SuccessCallback successCallback) {
        this.successCallback = successCallback;
        return this;
    }

    public void enqueue() {
        this.manager.enqueue(this);
    }

    @NonNull
    public Request fail(@NonNull FailCallback failCallback) {
        this.failCallback = failCallback;
        return this;
    }

    public void internalBefore(@NonNull BeforeCallback beforeCallback) {
        this.internalBeforeCallback = beforeCallback;
    }

    public void internalFail(@NonNull FailCallback failCallback) {
        this.internalFailCallback = failCallback;
    }

    public void internalSuccess(@NonNull SuccessCallback successCallback) {
        this.internalSuccessCallback = successCallback;
    }

    @NonNull
    public Request invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        this.invalidRequestCallback = invalidRequestCallback;
        return this;
    }

    public void notifyFail(@NonNull BluetoothDevice bluetoothDevice, int i) {
        if (this.finished) {
            return;
        }
        this.finished = true;
        FailCallback failCallback = this.failCallback;
        if (failCallback != null) {
            failCallback.onRequestFailed(bluetoothDevice, i);
        }
        FailCallback failCallback2 = this.internalFailCallback;
        if (failCallback2 != null) {
            failCallback2.onRequestFailed(bluetoothDevice, i);
        }
    }

    public void notifyInvalidRequest() {
        if (this.finished) {
            return;
        }
        this.finished = true;
        InvalidRequestCallback invalidRequestCallback = this.invalidRequestCallback;
        if (invalidRequestCallback != null) {
            invalidRequestCallback.onInvalidRequest();
        }
    }

    public void notifyStarted(@NonNull BluetoothDevice bluetoothDevice) {
        BeforeCallback beforeCallback = this.beforeCallback;
        if (beforeCallback != null) {
            beforeCallback.onRequestStarted(bluetoothDevice);
        }
        BeforeCallback beforeCallback2 = this.internalBeforeCallback;
        if (beforeCallback2 != null) {
            beforeCallback2.onRequestStarted(bluetoothDevice);
        }
    }

    public void notifySuccess(@NonNull BluetoothDevice bluetoothDevice) {
        if (this.finished) {
            return;
        }
        this.finished = true;
        SuccessCallback successCallback = this.successCallback;
        if (successCallback != null) {
            successCallback.onRequestCompleted(bluetoothDevice);
        }
        SuccessCallback successCallback2 = this.internalSuccessCallback;
        if (successCallback2 != null) {
            successCallback2.onRequestCompleted(bluetoothDevice);
        }
    }

    @NonNull
    public Request setManager(@NonNull BleManager bleManager) {
        this.manager = bleManager;
        return this;
    }

    @NonNull
    @Deprecated
    public static ReadRequest newReadRequest(@Nullable BluetoothGattDescriptor bluetoothGattDescriptor) {
        return new ReadRequest(Type.READ_DESCRIPTOR, bluetoothGattDescriptor);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newWriteRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr, int i) {
        return new WriteRequest(Type.WRITE, bluetoothGattCharacteristic, bArr, 0, bArr != null ? bArr.length : 0, i);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newWriteRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr, @IntRange(from = 0) int i, @IntRange(from = 0) int i2) {
        return new WriteRequest(Type.WRITE, bluetoothGattCharacteristic, bArr, i, i2, bluetoothGattCharacteristic != null ? bluetoothGattCharacteristic.getWriteType() : 2);
    }

    public Request(@NonNull Type type, @Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        this.type = type;
        this.characteristic = bluetoothGattCharacteristic;
        this.descriptor = null;
        this.syncLock = new ConditionVariable(true);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newWriteRequest(@Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr, @IntRange(from = 0) int i, @IntRange(from = 0) int i2, int i3) {
        return new WriteRequest(Type.WRITE, bluetoothGattCharacteristic, bArr, i, i2, i3);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newWriteRequest(@Nullable BluetoothGattDescriptor bluetoothGattDescriptor, @Nullable byte[] bArr) {
        return new WriteRequest(Type.WRITE_DESCRIPTOR, bluetoothGattDescriptor, bArr, 0, bArr != null ? bArr.length : 0);
    }

    @NonNull
    @Deprecated
    public static WriteRequest newWriteRequest(@Nullable BluetoothGattDescriptor bluetoothGattDescriptor, @Nullable byte[] bArr, @IntRange(from = 0) int i, @IntRange(from = 0) int i2) {
        return new WriteRequest(Type.WRITE_DESCRIPTOR, bluetoothGattDescriptor, bArr, i, i2);
    }

    public Request(@NonNull Type type, @Nullable BluetoothGattDescriptor bluetoothGattDescriptor) {
        this.type = type;
        this.characteristic = null;
        this.descriptor = bluetoothGattDescriptor;
        this.syncLock = new ConditionVariable(true);
    }
}
