package anetwork.channel.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface ParcelableBodyHandler extends IInterface {
    boolean isCompleted() throws RemoteException;

    int read(byte[] bArr) throws RemoteException;

    /* JADX INFO: compiled from: Taobao */
    public static abstract class Stub extends Binder implements ParcelableBodyHandler {
        private static final String DESCRIPTOR = "anetwork.channel.aidl.ParcelableBodyHandler";
        static final int TRANSACTION_isCompleted = 2;
        static final int TRANSACTION_read = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ParcelableBodyHandler asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof ParcelableBodyHandler)) {
                return (ParcelableBodyHandler) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i == 1598968902) {
                parcel2.writeString(DESCRIPTOR);
                return true;
            }
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    byte[] bArrCreateByteArray = parcel.createByteArray();
                    int i3 = read(bArrCreateByteArray);
                    parcel2.writeNoException();
                    parcel2.writeInt(i3);
                    parcel2.writeByteArray(bArrCreateByteArray);
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    boolean zIsCompleted = isCompleted();
                    parcel2.writeNoException();
                    parcel2.writeInt(zIsCompleted ? 1 : 0);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        /* JADX INFO: compiled from: Taobao */
        private static class Proxy implements ParcelableBodyHandler {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private IBinder f1975a;

            Proxy(IBinder iBinder) {
                this.f1975a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f1975a;
            }

            @Override // anetwork.channel.aidl.ParcelableBodyHandler
            public int read(byte[] bArr) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeByteArray(bArr);
                    this.f1975a.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    int i = parcelObtain2.readInt();
                    parcelObtain2.readByteArray(bArr);
                    return i;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }

            @Override // anetwork.channel.aidl.ParcelableBodyHandler
            public boolean isCompleted() throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.f1975a.transact(2, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return parcelObtain2.readInt() != 0;
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
