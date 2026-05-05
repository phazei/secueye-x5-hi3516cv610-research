package aisble;

import aisble.callback.DataReceivedCallback;
import aisble.callback.ReadProgressCallback;
import aisble.data.Data;
import aisble.data.DataFilter;
import aisble.data.DataMerger;
import aisble.data.DataStream;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public class ValueChangedCallback {
    public DataStream buffer;
    public int count = 0;
    public DataMerger dataMerger;
    public DataFilter filter;
    public ReadProgressCallback progressCallback;
    public DataReceivedCallback valueCallback;

    @NonNull
    public ValueChangedCallback filter(@NonNull DataFilter dataFilter) {
        this.filter = dataFilter;
        return this;
    }

    public ValueChangedCallback free() {
        this.valueCallback = null;
        this.dataMerger = null;
        this.progressCallback = null;
        this.buffer = null;
        return this;
    }

    public boolean matches(byte[] bArr) {
        DataFilter dataFilter = this.filter;
        return dataFilter == null || dataFilter.filter(bArr);
    }

    @NonNull
    public ValueChangedCallback merge(@NonNull DataMerger dataMerger) {
        this.dataMerger = dataMerger;
        this.progressCallback = null;
        return this;
    }

    public void notifyValueChanged(@NonNull BluetoothDevice bluetoothDevice, @Nullable byte[] bArr) {
        DataReceivedCallback dataReceivedCallback = this.valueCallback;
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

    @NonNull
    public ValueChangedCallback with(@NonNull DataReceivedCallback dataReceivedCallback) {
        this.valueCallback = dataReceivedCallback;
        return this;
    }

    @NonNull
    public ValueChangedCallback merge(@NonNull DataMerger dataMerger, @NonNull ReadProgressCallback readProgressCallback) {
        this.dataMerger = dataMerger;
        this.progressCallback = readProgressCallback;
        return this;
    }
}
