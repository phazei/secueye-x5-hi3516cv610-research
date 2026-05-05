package aisble;

import aisble.Request;
import aisble.callback.BeforeCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.RssiCallback;
import aisble.callback.SuccessCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public final class ReadRssiRequest extends SimpleValueRequest<RssiCallback> implements Operation {
    public ReadRssiRequest(@NonNull Request.Type type) {
        super(type);
    }

    public void notifyRssiRead(@NonNull BluetoothDevice bluetoothDevice, @IntRange(from = -128, to = 20) int i) {
        T t = this.valueCallback;
        if (t != 0) {
            ((RssiCallback) t).onRssiRead(bluetoothDevice, i);
        }
    }

    @Override // aisble.Request
    @NonNull
    public ReadRssiRequest before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ReadRssiRequest done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ReadRssiRequest fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ReadRssiRequest invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ReadRssiRequest setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }

    @Override // aisble.SimpleValueRequest
    @NonNull
    public ReadRssiRequest with(@NonNull RssiCallback rssiCallback) {
        super.with(rssiCallback);
        return this;
    }
}
