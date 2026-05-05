package anetwork.channel.aidl;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import anetwork.channel.aidl.RemoteNetwork;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public interface IRemoteNetworkGetter extends IInterface {
    RemoteNetwork get(int i) throws RemoteException;

    /* JADX INFO: compiled from: Taobao */
    public static abstract class Stub extends Binder implements IRemoteNetworkGetter {
        private static final String DESCRIPTOR = "anetwork.channel.aidl.IRemoteNetworkGetter";
        static final int TRANSACTION_get = 1;

        @Override // android.os.IInterface
        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteNetworkGetter asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface iInterfaceQueryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (iInterfaceQueryLocalInterface != null && (iInterfaceQueryLocalInterface instanceof IRemoteNetworkGetter)) {
                return (IRemoteNetworkGetter) iInterfaceQueryLocalInterface;
            }
            return new Proxy(iBinder);
        }

        @Override // android.os.Binder
        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1) {
                if (i == 1598968902) {
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                }
                return super.onTransact(i, parcel, parcel2, i2);
            }
            parcel.enforceInterface(DESCRIPTOR);
            RemoteNetwork remoteNetwork = get(parcel.readInt());
            parcel2.writeNoException();
            parcel2.writeStrongBinder(remoteNetwork != null ? remoteNetwork.asBinder() : null);
            return true;
        }

        /* JADX INFO: compiled from: Taobao */
        private static class Proxy implements IRemoteNetworkGetter {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            private IBinder f1966a;

            Proxy(IBinder iBinder) {
                this.f1966a = iBinder;
            }

            @Override // android.os.IInterface
            public IBinder asBinder() {
                return this.f1966a;
            }

            @Override // anetwork.channel.aidl.IRemoteNetworkGetter
            public RemoteNetwork get(int i) throws RemoteException {
                Parcel parcelObtain = Parcel.obtain();
                Parcel parcelObtain2 = Parcel.obtain();
                try {
                    parcelObtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    parcelObtain.writeInt(i);
                    this.f1966a.transact(1, parcelObtain, parcelObtain2, 0);
                    parcelObtain2.readException();
                    return RemoteNetwork.Stub.asInterface(parcelObtain2.readStrongBinder());
                } finally {
                    parcelObtain2.recycle();
                    parcelObtain.recycle();
                }
            }
        }
    }
}
