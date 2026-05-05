package aisble;

import aisble.Request;
import aisble.callback.BeforeCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.PhyCallback;
import aisble.callback.SuccessCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public final class PhyRequest extends SimpleValueRequest<PhyCallback> implements Operation {
    public static final int PHY_LE_1M_MASK = 1;
    public static final int PHY_LE_2M_MASK = 2;
    public static final int PHY_LE_CODED_MASK = 4;
    public static final int PHY_OPTION_NO_PREFERRED = 0;
    public static final int PHY_OPTION_S2 = 1;
    public static final int PHY_OPTION_S8 = 2;
    public final int phyOptions;
    public final int rxPhy;
    public final int txPhy;

    public PhyRequest(@NonNull Request.Type type) {
        super(type);
        this.txPhy = 0;
        this.rxPhy = 0;
        this.phyOptions = 0;
    }

    public int getPreferredPhyOptions() {
        return this.phyOptions;
    }

    public int getPreferredRxPhy() {
        return this.rxPhy;
    }

    public int getPreferredTxPhy() {
        return this.txPhy;
    }

    public void notifyLegacyPhy(@NonNull BluetoothDevice bluetoothDevice) {
        T t = this.valueCallback;
        if (t != 0) {
            ((PhyCallback) t).onPhyChanged(bluetoothDevice, 1, 1);
        }
    }

    public void notifyPhyChanged(@NonNull BluetoothDevice bluetoothDevice, int i, int i2) {
        T t = this.valueCallback;
        if (t != 0) {
            ((PhyCallback) t).onPhyChanged(bluetoothDevice, i, i2);
        }
    }

    @Override // aisble.Request
    @NonNull
    public PhyRequest before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public PhyRequest done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public PhyRequest fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public PhyRequest invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public PhyRequest setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }

    @Override // aisble.SimpleValueRequest
    @NonNull
    public PhyRequest with(@NonNull PhyCallback phyCallback) {
        super.with(phyCallback);
        return this;
    }

    public PhyRequest(@NonNull Request.Type type, int i, int i2, int i3) {
        super(type);
        i = (i & (-8)) > 0 ? 1 : i;
        i2 = (i2 & (-8)) > 0 ? 1 : i2;
        i3 = (i3 < 0 || i3 > 2) ? 0 : i3;
        this.txPhy = i;
        this.rxPhy = i2;
        this.phyOptions = i3;
    }
}
