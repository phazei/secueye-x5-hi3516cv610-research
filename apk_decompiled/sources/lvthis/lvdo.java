package lvthis;

import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public class lvdo {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private Map<String, Object> f8068lvdo = new HashMap(2);

    public lvdo() {
        this.f8068lvdo.put("__time__", Integer.valueOf(new Long(System.currentTimeMillis() / 1000).intValue()));
    }

    public Map<String, Object> lvdo() {
        return this.f8068lvdo;
    }

    public void lvdo(String str, String str2) {
        if (str == null || str.isEmpty()) {
            return;
        }
        if (str2 == null) {
            this.f8068lvdo.put(str, "");
        } else {
            this.f8068lvdo.put(str, str2);
        }
    }
}
