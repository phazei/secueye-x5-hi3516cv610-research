package aisble;

import aisble.Request;
import aisble.callback.BeforeCallback;
import aisble.callback.FailCallback;
import aisble.callback.InvalidRequestCallback;
import aisble.callback.SuccessCallback;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public class DisconnectRequest extends TimeoutableRequest {
    public DisconnectRequest(@NonNull Request.Type type) {
        super(type);
    }

    @Override // aisble.Request
    @NonNull
    public DisconnectRequest before(@NonNull BeforeCallback beforeCallback) {
        super.before(beforeCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public DisconnectRequest done(@NonNull SuccessCallback successCallback) {
        super.done(successCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public DisconnectRequest fail(@NonNull FailCallback failCallback) {
        super.fail(failCallback);
        return this;
    }

    @Override // aisble.Request
    @NonNull
    public DisconnectRequest invalid(@NonNull InvalidRequestCallback invalidRequestCallback) {
        super.invalid(invalidRequestCallback);
        return this;
    }

    @Override // aisble.TimeoutableRequest
    @NonNull
    public DisconnectRequest timeout(@IntRange(from = 0) long j) {
        super.timeout(j);
        return this;
    }

    @Override // aisble.TimeoutableRequest, aisble.Request
    @NonNull
    public DisconnectRequest setManager(@NonNull BleManager bleManager) {
        super.setManager(bleManager);
        return this;
    }
}
