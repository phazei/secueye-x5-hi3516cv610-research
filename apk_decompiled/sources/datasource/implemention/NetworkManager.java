package datasource.implemention;

import com.alibaba.ailabs.tg.network.GeniusNetwork;
import java.util.HashMap;

/* JADX INFO: loaded from: classes3.dex */
public class NetworkManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static HashMap<Class, Object> f7862a = new HashMap<>();

    public static <T> T getService(Class<T> cls) {
        Object objCreate = f7862a.get(cls);
        if (objCreate == null) {
            objCreate = new GeniusNetwork.Builder().build().create(cls);
            f7862a.put(cls, objCreate);
        }
        return cls.cast(objCreate);
    }
}
