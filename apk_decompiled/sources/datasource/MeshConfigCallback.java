package datasource;

/* JADX INFO: loaded from: classes3.dex */
public interface MeshConfigCallback<T> {
    void onFailure(String str, String str2);

    void onSuccess(T t);
}
