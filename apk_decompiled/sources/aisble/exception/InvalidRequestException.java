package aisble.exception;

import aisble.Request;

/* JADX INFO: loaded from: classes.dex */
public final class InvalidRequestException extends Exception {
    public final Request request;

    public InvalidRequestException(Request request) {
        super("Invalid request");
        this.request = request;
    }

    public Request getRequest() {
        return this.request;
    }
}
