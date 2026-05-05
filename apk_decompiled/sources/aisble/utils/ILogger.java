package aisble.utils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;

/* JADX INFO: loaded from: classes.dex */
public interface ILogger {
    void log(int i, @StringRes int i2, @Nullable Object... objArr);

    void log(int i, @NonNull String str);
}
