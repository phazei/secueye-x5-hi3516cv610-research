package lvbreak;

import lvchar.lvfor;
import okhttp3.Response;

/* JADX INFO: loaded from: classes4.dex */
public class lvdo extends lvfor {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    lvcase.lvfor f7940lvdo;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    Response f7941lvif;

    public lvdo lvdo(lvcase.lvfor lvforVar) {
        this.f7940lvdo = lvforVar;
        return this;
    }

    public lvdo lvdo(Response response) {
        this.f7941lvif = response;
        return this;
    }

    public lvcase.lvfor lvdo() {
        return this.f7940lvdo;
    }
}
