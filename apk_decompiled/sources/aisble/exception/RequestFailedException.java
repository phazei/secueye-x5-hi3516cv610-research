package aisble.exception;

import aisble.Request;

/* JADX INFO: loaded from: classes.dex */
public final class RequestFailedException extends Exception {
    public final Request request;
    public final int status;

    public RequestFailedException(Request request, int i) {
        super("Request failed with status " + i);
        this.request = request;
        this.status = i;
    }

    public Request getRequest() {
        return this.request;
    }

    public int getStatus() {
        return this.status;
    }
}
