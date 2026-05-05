package aisble;

import aisble.Request;
import aisble.callback.BeforeCallback;
import aisble.callback.DataReceivedCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.ReadProgressCallback;
import aisble.callback.SuccessCallback;
import aisble.callback.profile.ProfileReadResponse;
import aisble.data.Data;
import aisble.data.DataFilter;
import aisble.data.DataMerger;
import aisble.data.DataStream;
import aisble.exception.InvalidDataException;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public final class ReadRequest extends SimpleValueRequest<DataReceivedCallback> implements Operation {
    public DataStream buffer;
    public int count;
    public DataMerger dataMerger;
    public DataFilter filter;
    public ReadProgressCallback progressCallback;

    public ReadRequest(@NonNull Request.Type type) {
        super(type);
        this.count = 0;
    }

    @NonNull
    public <E extends ProfileReadResponse> E awaitValid(@NonNull Class<E> cls) throws InvalidDataException {
        E e = (E) await((Class) cls);
        if (e.isValid()) {
            return e;
        }
        throw new InvalidDataException(e);
    }

    @NonNull
    public ReadRequest filter(@NonNull DataFilter dataFilter) {
        this.filter = dataFilter;
        return this;
    }

    public boolean hasMore() {
        return this.count > 0;
    }

    public boolean matches(byte[] bArr) {
        DataFilter dataFilter = this.filter;
        return dataFilter == null || dataFilter.filter(bArr);
    }

    @NonNull
    public ReadRequest merge(@NonNull DataMerger dataMerger) {
        this.dataMerger = dataMerger;
        this.progressCallback = null;
        return this;
    }

    public void notifyValueChanged(@NonNull BluetoothDevice bluetoothDevice, @Nullable byte[] bArr) {
        DataReceivedCallback dataReceivedCallback = (DataReceivedCallback) this.valueCallback;
        if (dataReceivedCallback == null) {
            return;
        }
        if (this.dataMerger == null) {
            dataReceivedCallback.onDataReceived(bluetoothDevice, new Data(bArr));
            return;
        }
        ReadProgressCallback readProgressCallback = this.progressCallback;
        if (readProgressCallback != null) {
            readProgressCallback.onPacketReceived(bluetoothDevice, bArr, this.count);
        }
        if (this.buffer == null) {
            this.buffer = new DataStream();
        }
        DataMerger dataMerger = this.dataMerger;
        DataStream dataStream = this.buffer;
        int i = this.count;
        this.count = i + 1;
        if (dataMerger.merge(dataStream, bArr, i)) {
            dataReceivedCallback.onDataReceived(bluetoothDevice, this.buffer.toData());
            this.buffer = null;
            this.count = 0;
        }
    }

    @Override // aisble.Request
    @NonNull
    public ReadRequest before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ReadRequest done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ReadRequest fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ReadRequest invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public ReadRequest setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }

    @Override // aisble.SimpleValueRequest
    @NonNull
    public ReadRequest with(@NonNull DataReceivedCallback dataReceivedCallback) {
        super.with(dataReceivedCallback);
        return this;
    }

    public ReadRequest(@NonNull Request.Type type, @Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
    }

    @NonNull
    public ReadRequest merge(@NonNull DataMerger dataMerger, @NonNull ReadProgressCallback readProgressCallback) {
        this.dataMerger = dataMerger;
        this.progressCallback = readProgressCallback;
        return this;
    }

    @NonNull
    public <E extends ProfileReadResponse> E awaitValid(@NonNull E e) throws InvalidDataException {
        await(e);
        if (e.isValid()) {
            return e;
        }
        throw new InvalidDataException(e);
    }

    public ReadRequest(@NonNull Request.Type type, @Nullable BluetoothGattDescriptor bluetoothGattDescriptor) {
        super(type, bluetoothGattDescriptor);
        this.count = 0;
    }
}
