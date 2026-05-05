package anetwork.channel.aidl.adapter;

import android.os.RemoteException;
import anetwork.channel.IBodyHandler;
import anetwork.channel.aidl.ParcelableBodyHandler;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class ParcelableBodyHandlerWrapper extends ParcelableBodyHandler.Stub {
    private static final String TAG = "anet.ParcelableBodyHandlerWrapper";
    private IBodyHandler handler;

    public ParcelableBodyHandlerWrapper(IBodyHandler iBodyHandler) {
        this.handler = iBodyHandler;
    }

    @Override // anetwork.channel.aidl.ParcelableBodyHandler
    public int read(byte[] bArr) throws RemoteException {
        IBodyHandler iBodyHandler = this.handler;
        if (iBodyHandler != null) {
            return iBodyHandler.read(bArr);
        }
        return 0;
    }

    @Override // anetwork.channel.aidl.ParcelableBodyHandler
    public boolean isCompleted() throws RemoteException {
        IBodyHandler iBodyHandler = this.handler;
        if (iBodyHandler != null) {
            return iBodyHandler.isCompleted();
        }
        return true;
    }

    public String toString() {
        return super.toString() + " handle:" + this.handler;
    }
}
