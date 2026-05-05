package aisble;

import aisble.Request;
import aisble.callback.BeforeCallback;
import aisble.callback.DataSentCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.SuccessCallback;
import aisble.callback.WriteProgressCallback;
import aisble.data.Data;
import aisble.data.DataSplitter;
import aisble.data.DefaultMtuSplitter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public final class WriteRequest extends SimpleValueRequest<DataSentCallback> implements Operation {
    public static final DataSplitter MTU_SPLITTER = new DefaultMtuSplitter();
    public boolean complete;
    public int count;
    public byte[] currentChunk;
    public final byte[] data;
    public DataSplitter dataSplitter;
    public Handler handler;
    public byte[] nextChunk;
    public WriteProgressCallback progressCallback;
    public Runnable timeoutRunnable;
    public final int writeType;

    public WriteRequest(@NonNull Request.Type type) {
        this(type, null);
    }

    public static byte[] copy(@Nullable byte[] bArr, @IntRange(from = 0) int i, @IntRange(from = 0) int i2) {
        if (bArr == null || i > bArr.length) {
            return null;
        }
        int iMin = Math.min(bArr.length - i, i2);
        byte[] bArr2 = new byte[iMin];
        System.arraycopy(bArr, i, bArr2, 0, iMin);
        return bArr2;
    }

    public void forceSplit() {
        if (this.dataSplitter == null) {
            split();
        }
    }

    public byte[] getData(@IntRange(from = 23, to = 517) int i) {
        if (this.dataSplitter == null || this.data == null) {
            this.complete = true;
            byte[] bArr = this.data;
            this.currentChunk = bArr;
            return bArr;
        }
        int i2 = this.writeType != 4 ? i - 3 : i - 12;
        byte[] bArrChunk = this.nextChunk;
        if (bArrChunk == null) {
            bArrChunk = this.dataSplitter.chunk(this.data, this.count, i2);
        }
        if (bArrChunk != null) {
            this.nextChunk = this.dataSplitter.chunk(this.data, this.count + 1, i2);
        }
        if (this.nextChunk == null) {
            this.complete = true;
        }
        this.currentChunk = bArrChunk;
        return bArrChunk;
    }

    public int getWriteType() {
        return this.writeType;
    }

    public boolean hasMore() {
        return !this.complete;
    }

    public boolean notifyPacketSent(@NonNull BluetoothDevice bluetoothDevice, @Nullable byte[] bArr) {
        T t;
        WriteProgressCallback writeProgressCallback = this.progressCallback;
        if (writeProgressCallback != null) {
            writeProgressCallback.onPacketSent(bluetoothDevice, bArr, this.count);
        }
        this.count++;
        if (this.complete && (t = this.valueCallback) != 0) {
            ((DataSentCallback) t).onDataSent(bluetoothDevice, new Data(this.data));
        }
        return Arrays.equals(bArr, this.currentChunk);
    }

    public void onDelivered(@NonNull final BluetoothGatt bluetoothGatt) {
        Handler handler = this.handler;
        Runnable runnable = new Runnable() { // from class: aisble.WriteRequest.1
            @Override // java.lang.Runnable
            public void run() {
                WriteRequest.this.notifyFail(bluetoothGatt.getDevice(), -5);
            }
        };
        this.timeoutRunnable = runnable;
        handler.postDelayed(runnable, 1000L);
    }

    public void onWriteFinished() {
        Runnable runnable = this.timeoutRunnable;
        if (runnable != null) {
            this.handler.removeCallbacks(runnable);
        }
    }

    @NonNull
    public WriteRequest split(@NonNull DataSplitter dataSplitter) {
        this.dataSplitter = dataSplitter;
        this.progressCallback = null;
        return this;
    }

    public WriteRequest(@NonNull Request.Type type, @Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
        this.complete = false;
        this.handler = new Handler(Looper.getMainLooper());
        this.timeoutRunnable = null;
        this.data = null;
        this.writeType = 0;
        this.complete = true;
    }

    @Override // aisble.Request
    @NonNull
    public WriteRequest before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public WriteRequest done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public WriteRequest fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public WriteRequest invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public WriteRequest setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }

    @Override // aisble.SimpleValueRequest
    @NonNull
    public WriteRequest with(@NonNull DataSentCallback dataSentCallback) {
        super.with(dataSentCallback);
        return this;
    }

    @NonNull
    public WriteRequest split(@NonNull DataSplitter dataSplitter, @NonNull WriteProgressCallback writeProgressCallback) {
        this.dataSplitter = dataSplitter;
        this.progressCallback = writeProgressCallback;
        return this;
    }

    @NonNull
    public WriteRequest split() {
        this.dataSplitter = MTU_SPLITTER;
        this.progressCallback = null;
        return this;
    }

    @NonNull
    public WriteRequest split(@NonNull WriteProgressCallback writeProgressCallback) {
        this.dataSplitter = MTU_SPLITTER;
        this.progressCallback = writeProgressCallback;
        return this;
    }

    public WriteRequest(@NonNull Request.Type type, @Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic, @Nullable byte[] bArr, @IntRange(from = 0) int i, @IntRange(from = 0) int i2, int i3) {
        super(type, bluetoothGattCharacteristic);
        this.count = 0;
        this.complete = false;
        this.handler = new Handler(Looper.getMainLooper());
        this.timeoutRunnable = null;
        this.data = copy(bArr, i, i2);
        this.writeType = i3;
    }

    public WriteRequest(@NonNull Request.Type type, @Nullable BluetoothGattDescriptor bluetoothGattDescriptor, @Nullable byte[] bArr, @IntRange(from = 0) int i, @IntRange(from = 0) int i2) {
        super(type, bluetoothGattDescriptor);
        this.count = 0;
        this.complete = false;
        this.handler = new Handler(Looper.getMainLooper());
        this.timeoutRunnable = null;
        this.data = copy(bArr, i, i2);
        this.writeType = 2;
    }
}
