package com.alibaba.ailabs.iot.bluetoothlesdk;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.LinkedList;

/* JADX INFO: compiled from: ControlMessageQueue.java */
/* JADX INFO: loaded from: classes.dex */
public class b {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    @NonNull
    private final LinkedList<ControlMessage> f2747a = new LinkedList<>();

    @NonNull
    public b a(@NonNull ControlMessage controlMessage) {
        if (controlMessage.enqueued) {
            throw new IllegalStateException("Request already enqueued");
        }
        this.f2747a.add(controlMessage);
        controlMessage.enqueued = true;
        return this;
    }

    @NonNull
    public b b(@NonNull ControlMessage controlMessage) {
        if (controlMessage.enqueued) {
            throw new IllegalStateException("Request already enqueued");
        }
        this.f2747a.addFirst(controlMessage);
        controlMessage.enqueued = true;
        return this;
    }

    @Nullable
    ControlMessage a() {
        try {
            return this.f2747a.remove();
        } catch (Exception unused) {
            return null;
        }
    }

    boolean b() {
        return !this.f2747a.isEmpty();
    }
}
