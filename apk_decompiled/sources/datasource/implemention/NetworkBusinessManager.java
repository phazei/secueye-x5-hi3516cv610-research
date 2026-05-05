package datasource.implemention;

import com.alibaba.ailabs.tg.network.GeniusNetwork;
import java.util.HashMap;

/* JADX INFO: loaded from: classes3.dex */
public class NetworkBusinessManager {
    public static HashMap<Class, Object> sCache = new HashMap<>();

    public static <T> T getService(Class<T> cls) {
        Object objCreate = sCache.get(cls);
        if (objCreate == null) {
            objCreate = new GeniusNetwork.Builder().build().create(cls);
            sCache.put(cls, objCreate);
        }
        return cls.cast(objCreate);
    }
}
