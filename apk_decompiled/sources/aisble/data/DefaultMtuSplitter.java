package aisble.data;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* JADX INFO: loaded from: classes.dex */
public final class DefaultMtuSplitter implements DataSplitter {
    @Override // aisble.data.DataSplitter
    @Nullable
    public byte[] chunk(@NonNull byte[] bArr, @IntRange(from = 0) int i, @IntRange(from = 20) int i2) {
        int i3 = i * i2;
        int iMin = Math.min(i2, bArr.length - i3);
        if (iMin <= 0) {
            return null;
        }
        byte[] bArr2 = new byte[iMin];
        System.arraycopy(bArr, i3, bArr2, 0, iMin);
        return bArr2;
    }
}
