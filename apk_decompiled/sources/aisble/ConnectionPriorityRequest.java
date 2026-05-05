package aisble;

import aisble.Request;
import aisble.callback.BeforeCallback;
import aisble.callback.ConnectionPriorityCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.SuccessCallback;
import android.bluetooth.BluetoothDevice;
import android.support.v4.media.MediaDescriptionCompat;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import view.CustomFinderView;

/* JADX INFO: loaded from: classes.dex */
public final class ConnectionPriorityRequest extends SimpleValueRequest<ConnectionPriorityCallback> implements Operation {
    public static final int CONNECTION_PRIORITY_BALANCED = 0;
    public static final int CONNECTION_PRIORITY_HIGH = 1;
    public static final int CONNECTION_PRIORITY_LOW_POWER = 2;
    public final int value;

    public ConnectionPriorityRequest(@NonNull Request.Type type, int i) {
        super(type);
        this.value = (i < 0 || i > 2) ? 0 : i;
    }

    public int getRequiredPriority() {
        return this.value;
    }

    @RequiresApi(api = 26)
    public void notifyConnectionPriorityChanged(@NonNull BluetoothDevice bluetoothDevice, @IntRange(from = MediaDescriptionCompat.BT_FOLDER_TYPE_YEARS, to = 3200) int i, @IntRange(from = 0, to = 499) int i2, @IntRange(from = CustomFinderView.CUSTOME_ANIMATION_DELAY, to = 3200) int i3) {
        T t = this.valueCallback;
        if (t != 0) {
            ((ConnectionPriorityCallback) t).onConnectionUpdated(bluetoothDevice, i, i2, i3);
        }
    }

    @Override // aisble.Request
    @NonNull
    public ConnectionPriorityRequest before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ConnectionPriorityRequest done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ConnectionPriorityRequest fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ConnectionPriorityRequest invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ConnectionPriorityRequest setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }

    @Override // aisble.SimpleValueRequest
    @NonNull
    @RequiresApi(26)
    public ConnectionPriorityRequest with(@NonNull ConnectionPriorityCallback connectionPriorityCallback) {
        super.with(connectionPriorityCallback);
        return this;
    }

    @Override // aisble.SimpleValueRequest
    @NonNull
    @RequiresApi(26)
    public <E extends ConnectionPriorityCallback> E await(@NonNull Class<E> cls) {
        return (E) super.await((Class) cls);
    }

    @Override // aisble.SimpleValueRequest
    @NonNull
    @RequiresApi(26)
    public <E extends ConnectionPriorityCallback> E await(@NonNull E e) {
        return (E) super.await(e);
    }
}
