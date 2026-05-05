package aisble.data;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public interface DataSplitter {
    @Nullable
    byte[] chunk(@NonNull byte[] bArr, @IntRange(from = 0) int i, @IntRange(from = 20) int i2);
}
