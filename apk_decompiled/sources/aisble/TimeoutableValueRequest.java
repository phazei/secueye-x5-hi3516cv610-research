package aisble;

import aisble.Request;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public abstract class TimeoutableValueRequest<T> extends TimeoutableRequest {
    public T valueCallback;

    public TimeoutableValueRequest(@NonNull Request.Type type) {
        super(type);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NonNull
    public <E extends T> E await(@NonNull E e) {
        Request.assertNotMainThread();
        T t = this.valueCallback;
        try {
            with(e).await();
            return e;
        } finally {
            this.valueCallback = t;
        }
    }

    @NonNull
    public TimeoutableValueRequest<T> with(@NonNull T t) {
        this.valueCallback = t;
        return this;
    }

    public TimeoutableValueRequest(@NonNull Request.Type type, @Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
    }

    @Override // aisble.TimeoutableRequest
    @NonNull
    public TimeoutableValueRequest<T> timeout(long j) {
        super.timeout(j);
        return this;
    }

    public TimeoutableValueRequest(@NonNull Request.Type type, @Nullable BluetoothGattDescriptor bluetoothGattDescriptor) {
        super(type, bluetoothGattDescriptor);
    }

    @NonNull
    public <E extends T> E await(@NonNull Class<E> cls) {
        Request.assertNotMainThread();
        try {
            return (E) await(cls.newInstance());
        } catch (IllegalAccessException unused) {
            throw new IllegalArgumentException("Couldn't instantiate " + cls.getCanonicalName() + " class. Is the default constructor accessible?");
        } catch (InstantiationException unused2) {
            throw new IllegalArgumentException("Couldn't instantiate " + cls.getCanonicalName() + " class. Does it have a default constructor with no arguments?");
        }
    }

    @NonNull
    @Deprecated
    public <E extends T> E await(@NonNull Class<E> cls, @IntRange(from = 0) long j) {
        return (E) timeout(j).await((Class) cls);
    }

    @NonNull
    @Deprecated
    public <E extends T> E await(@NonNull E e, @IntRange(from = 0) long j) {
        return (E) timeout(j).await(e);
    }
}
