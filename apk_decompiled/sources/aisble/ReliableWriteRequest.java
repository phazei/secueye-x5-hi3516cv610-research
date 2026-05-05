package aisble;

import aisble.callback.BeforeCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.SuccessCallback;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public final class ReliableWriteRequest extends RequestQueue {
    public boolean cancelled;
    public boolean closed;
    public boolean initialized;

    public void abort() {
        cancelQueue();
    }

    @Override // aisble.RequestQueue
    public void cancelQueue() {
        this.cancelled = true;
        super.cancelQueue();
    }

    @Override // aisble.RequestQueue
    public Request getNext() {
        if (!this.initialized) {
            this.initialized = true;
            return Request.newBeginReliableWriteRequest();
        }
        if (!super.isEmpty()) {
            return super.getNext();
        }
        this.closed = true;
        return this.cancelled ? Request.newAbortReliableWriteRequest() : Request.newExecuteReliableWriteRequest();
    }

    @Override // aisble.RequestQueue
    public boolean hasMore() {
        return !this.initialized ? super.hasMore() : !this.closed;
    }

    @Override // aisble.RequestQueue
    public int size() {
        int size = super.size();
        if (!this.initialized) {
            size++;
        }
        return !this.closed ? size + 1 : size;
    }

    @Override // aisble.RequestQueue
    @NonNull
    public ReliableWriteRequest add(@NonNull Operation operation) {
        super.add(operation);
        if (operation instanceof WriteRequest) {
            ((WriteRequest) operation).forceSplit();
        }
        return this;
    }

    @Override // aisble.RequestQueue, aisble.Request
    @NonNull
    public ReliableWriteRequest before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.RequestQueue, aisble.Request
    @NonNull
    public ReliableWriteRequest done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.RequestQueue, aisble.Request
    @NonNull
    public ReliableWriteRequest fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.RequestQueue, aisble.Request
    @NonNull
    public ReliableWriteRequest invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.RequestQueue, aisble.Request
    @NonNull
    public ReliableWriteRequest setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }
}
