package aisble;

import aisble.Request;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public abstract class SimpleValueRequest<T> extends SimpleRequest {
    public T valueCallback;

    public SimpleValueRequest(@NonNull Request.Type type) {
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
    public SimpleValueRequest<T> with(@NonNull T t) {
        this.valueCallback = t;
        return this;
    }

    public SimpleValueRequest(@NonNull Request.Type type, @Nullable BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        super(type, bluetoothGattCharacteristic);
    }

    public SimpleValueRequest(@NonNull Request.Type type, @Nullable BluetoothGattDescriptor bluetoothGattDescriptor) {
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
}
