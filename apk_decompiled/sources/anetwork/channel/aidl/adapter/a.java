package anetwork.channel.aidl.adapter;

import aisble.BleManager;
import android.os.RemoteException;
import anet.channel.util.ALog;
import anetwork.channel.Response;
import anetwork.channel.aidl.ParcelableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a implements Future<Response> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ParcelableFuture f1981a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Response f1982b;

    @Override // java.util.concurrent.Future
    public /* synthetic */ Response get(long j, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        return a(j);
    }

    public a(ParcelableFuture parcelableFuture) {
        this.f1981a = parcelableFuture;
    }

    public a(Response response) {
        this.f1982b = response;
    }

    @Override // java.util.concurrent.Future
    public boolean cancel(boolean z) {
        ParcelableFuture parcelableFuture = this.f1981a;
        if (parcelableFuture == null) {
            return false;
        }
        try {
            return parcelableFuture.cancel(z);
        } catch (RemoteException e) {
            ALog.w("anet.FutureResponse", "[cancel]", null, e, new Object[0]);
            return false;
        }
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        try {
            return this.f1981a.isCancelled();
        } catch (RemoteException e) {
            ALog.w("anet.FutureResponse", "[isCancelled]", null, e, new Object[0]);
            return false;
        }
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        try {
            return this.f1981a.isDone();
        } catch (RemoteException e) {
            ALog.w("anet.FutureResponse", "[isDone]", null, e, new Object[0]);
            return true;
        }
    }

    @Override // java.util.concurrent.Future
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public Response get() throws ExecutionException, InterruptedException {
        Response response = this.f1982b;
        if (response != null) {
            return response;
        }
        ParcelableFuture parcelableFuture = this.f1981a;
        if (parcelableFuture != null) {
            try {
                return parcelableFuture.get(BleManager.CONNECTION_TIMEOUT_THRESHOLD);
            } catch (RemoteException e) {
                ALog.w("anet.FutureResponse", "[get]", null, e, new Object[0]);
            }
        }
        return null;
    }

    public Response a(long j) throws ExecutionException, InterruptedException, TimeoutException {
        Response response = this.f1982b;
        if (response != null) {
            return response;
        }
        ParcelableFuture parcelableFuture = this.f1981a;
        if (parcelableFuture != null) {
            try {
                return parcelableFuture.get(j);
            } catch (RemoteException e) {
                ALog.w("anet.FutureResponse", "[get(long timeout, TimeUnit unit)]", null, e, new Object[0]);
            }
        }
        return null;
    }
}
