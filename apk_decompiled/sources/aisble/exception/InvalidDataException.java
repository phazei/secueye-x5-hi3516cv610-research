package aisble.exception;

import aisble.callback.profile.ProfileReadResponse;
import androidx.annotation.NonNull;

/* JADX INFO: loaded from: classes.dex */
public final class InvalidDataException extends Exception {
    public final ProfileReadResponse response;

    public InvalidDataException(@NonNull ProfileReadResponse profileReadResponse) {
        this.response = profileReadResponse;
    }

    public ProfileReadResponse getResponse() {
        return this.response;
    }
}
