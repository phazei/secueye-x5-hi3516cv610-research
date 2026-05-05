package lvthis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: loaded from: classes4.dex */
public class lvif {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private List<lvdo> f8069lvdo = new ArrayList();

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private String f8071lvif = "";

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private String f8070lvfor = "";

    public String lvdo() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("__source__", (Object) this.f8070lvfor);
        jSONObject.put("__topic__", (Object) this.f8071lvif);
        JSONArray jSONArray = new JSONArray();
        Iterator<lvdo> it = this.f8069lvdo.iterator();
        while (it.hasNext()) {
            jSONArray.add(new JSONObject(it.next().lvdo()));
        }
        jSONObject.put("__logs__", (Object) jSONArray);
        return jSONObject.toJSONString();
    }

    public void lvdo(lvdo lvdoVar) {
        this.f8069lvdo.add(lvdoVar);
    }
}
