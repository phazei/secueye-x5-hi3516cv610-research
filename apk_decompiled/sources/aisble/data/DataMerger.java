package aisble.data;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public interface DataMerger {
    boolean merge(@NonNull DataStream dataStream, @Nullable byte[] bArr, @IntRange(from = 0) int i);
}
