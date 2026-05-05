package bluetooth.bind;

/* JADX INFO: loaded from: classes.dex */
public interface OnBindDeviceCompletedListener {
    void onFailed(int i, String str, String str2);

    void onFailed(Exception exc);

    void onSuccess(String str);
}
