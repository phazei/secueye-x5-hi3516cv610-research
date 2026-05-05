package aisble;

import aisble.Request;
import aisble.callback.BeforeCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.SuccessCallback;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.LinkedList;
import java.util.Queue;

/* JADX INFO: loaded from: classes.dex */
public class RequestQueue extends SimpleRequest {

    @NonNull
    public final Queue<Request> requests;

    public RequestQueue() {
        super(Request.Type.SET);
        this.requests = new LinkedList();
    }

    /* JADX WARN: Multi-variable type inference failed */
    @NonNull
    public RequestQueue add(@NonNull Operation operation) {
        if (!(operation instanceof Request)) {
            throw new IllegalArgumentException("Operation does not extend Request");
        }
        Request request = (Request) operation;
        if (request.enqueued) {
            throw new IllegalStateException("Request already enqueued");
        }
        this.requests.add(request);
        request.enqueued = true;
        return this;
    }

    public void cancelQueue() {
        this.requests.clear();
    }

    @Nullable
    public Request getNext() {
        try {
            return this.requests.remove();
        } catch (Exception unused) {
            return null;
        }
    }

    public boolean hasMore() {
        return !this.requests.isEmpty();
    }

    public boolean isEmpty() {
        return this.requests.isEmpty();
    }

    @IntRange(from = 0)
    public int size() {
        return this.requests.size();
    }

    @Override // aisble.Request
    @NonNull
    public RequestQueue before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public RequestQueue done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public RequestQueue fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public RequestQueue invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public RequestQueue setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }
}
