package aisble;

import aisble.Request;
import aisble.callback.FailCallback;
import aisble.callback.SuccessCallback;
import aisble.exception.BluetoothDisabledException;
import aisble.exception.DeviceDisconnectedException;
import aisble.exception.InvalidRequestException;
import aisble.exception.RequestFailedException;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public class SimpleRequest extends Request {
    public SimpleRequest(@NonNull Request.Type type) {
        super(type);
    }

    public final void await() {
        Request.assertNotMainThread();
        SuccessCallback successCallback = this.successCallback;
        FailCallback failCallback = this.failCallback;
        try {
            this.syncLock.close();
            Request.RequestCallback requestCallback = new Request.RequestCallback();
            done(requestCallback).fail(requestCallback).invalid(requestCallback).enqueue();
            this.syncLock.block();
            if (requestCallback.isSuccess()) {
                return;
            }
            if (requestCallback.status == -1) {
                throw new DeviceDisconnectedException();
            }
            if (requestCallback.status == -100) {
                throw new BluetoothDisabledException();
            }
            if (requestCallback.status != -1000000) {
                throw new RequestFailedException(this, requestCallback.status);
            }
            throw new InvalidRequestException(this);
        } finally {
            this.successCallback = successCallback;
            this.failCallback = failCallback;
        }
    }

    public SimpleRequest(@NonNull Request.Type type, @Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
    }

    public SimpleRequest(@NonNull Request.Type type, @Nullable BluetoothGattDescriptor bluetoothGattDescriptor) {
        super(type, bluetoothGattDescriptor);
    }
}
