package aisble;

import aisble.Request;
import aisble.callback.BeforeCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.SuccessCallback;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public final class SleepRequest extends SimpleRequest implements Operation {
    public long delay;

    public SleepRequest(@NonNull Request.Type type, @IntRange(from = 0) long j) {
        super(type);
        this.delay = j;
    }

    public long getDelay() {
        return this.delay;
    }

    @Override // aisble.Request
    @NonNull
    public SleepRequest before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public SleepRequest done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public SleepRequest fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public SleepRequest invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public SleepRequest setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }
}
