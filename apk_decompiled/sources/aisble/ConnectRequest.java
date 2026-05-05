package aisble;

import aisble.Request;
import aisble.callback.BeforeCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.SuccessCallback;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public class ConnectRequest extends TimeoutableRequest {

    @IntRange(from = 0)
    public int attempt;
    public boolean autoConnect;

    @IntRange(from = 0)
    public int delay;

    @NonNull
    public BluetoothDevice device;
    public int preferredPhy;

    @IntRange(from = 0)
    public int retries;

    public ConnectRequest(@NonNull Request.Type type, @NonNull BluetoothDevice bluetoothDevice) {
        super(type);
        this.attempt = 0;
        this.retries = 0;
        this.delay = 0;
        this.autoConnect = false;
        this.device = bluetoothDevice;
        this.preferredPhy = 1;
    }

    public boolean canRetry() {
        int i = this.retries;
        if (i <= 0) {
            return false;
        }
        this.retries = i - 1;
        return true;
    }

    @NonNull
    public BluetoothDevice getDevice() {
        return this.device;
    }

    public int getPreferredPhy() {
        return this.preferredPhy;
    }

    @IntRange(from = 0)
    public int getRetryDelay() {
        return this.delay;
    }

    public boolean isFirstAttempt() {
        int i = this.attempt;
        this.attempt = i + 1;
        return i == 0;
    }

    public ConnectRequest retry(@IntRange(from = 0) int i) {
        this.retries = i;
        this.delay = 0;
        return this;
    }

    public boolean shouldAutoConnect() {
        return this.autoConnect;
    }

    public ConnectRequest useAutoConnect(boolean z) {
        this.autoConnect = z;
        return this;
    }

    public ConnectRequest usePreferredPhy(int i) {
        this.preferredPhy = i;
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ConnectRequest before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ConnectRequest done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ConnectRequest fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ConnectRequest invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.TimeoutableRequest
    @NonNull
    public ConnectRequest timeout(long j) {
        super.timeout(j);
        return this;
    }

    public ConnectRequest retry(@IntRange(from = 0) int i, @IntRange(from = 0) int i2) {
        this.retries = i;
        this.delay = i2;
        return this;
    }

    @Override // aisble.TimeoutableRequest, aisble.Request
    @NonNull
    public ConnectRequest setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }
}
