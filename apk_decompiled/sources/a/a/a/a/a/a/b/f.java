package a.a.a.a.a.a.b;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* JADX INFO: compiled from: MobileTripleValueManager.java */
/* JADX INFO: loaded from: classes.dex */
public class f {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public List<e> f1164a = null;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public e f1165b = null;

    /* JADX INFO: compiled from: MobileTripleValueManager.java */
    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final f f1166a = new f();
    }

    public static f b() {
        return a.f1166a;
    }

    public void a() {
        this.f1165b = null;
    }

    public boolean a(Context context, e eVar) {
        boolean z = false;
        if (context == null || eVar == null || !eVar.a()) {
            a.a.a.a.a.a.a.a.a("MobileTripleValueManager", "saveTripleValue(), params error");
            return false;
        }
        if (TextUtils.isEmpty(eVar.f1160a)) {
            a.a.a.a.a.a.a.a.a("MobileTripleValueManager", "saveTripleValue(), host is empty");
            return false;
        }
        this.f1165b = eVar;
        this.f1164a = a(context);
        if (this.f1164a == null) {
            this.f1164a = new ArrayList();
        }
        Iterator<e> it = this.f1164a.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            e next = it.next();
            if (eVar.f1160a.equals(next.f1160a)) {
                next.f1161b = eVar.f1161b;
                next.f1162c = eVar.f1162c;
                next.f1163d = eVar.f1163d;
                z = true;
                break;
            }
        }
        if (!z) {
            this.f1164a.add(eVar);
        }
        JSONArray jSONArray = new JSONArray();
        for (e eVar2 : this.f1164a) {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(AlinkConstants.KEY_PK, (Object) eVar2.f1161b);
            jSONObject.put(AlinkConstants.KEY_DN, (Object) eVar2.f1162c);
            jSONObject.put("ds", (Object) eVar2.f1163d);
            jSONObject.put("host", (Object) eVar2.f1160a);
            jSONArray.add(jSONObject);
        }
        return a.a.a.a.a.a.c.b.a(context, "LINKSDK_CHANNNEL_MOBILE_TRIPLES", jSONArray.toJSONString());
    }

    public e b(Context context) {
        e eVar = this.f1165b;
        if (eVar != null && eVar.a()) {
            return this.f1165b;
        }
        if (context == null) {
            a.a.a.a.a.a.a.a.b("MobileTripleValueManager", "getTripleValue(), context is empty");
            return null;
        }
        if (this.f1164a == null) {
            this.f1164a = a(context);
        }
        this.f1165b = a(b.b(), this.f1164a);
        return this.f1165b;
    }

    public final List<e> a(Context context) {
        if (context == null) {
            a.a.a.a.a.a.a.a.a("MobileTripleValueManager", "getSaveMobileTriple(), params error");
            return null;
        }
        String strA = a.a.a.a.a.a.c.b.a(context, "LINKSDK_CHANNNEL_MOBILE_TRIPLES");
        if (TextUtils.isEmpty(strA)) {
            a.a.a.a.a.a.a.a.a("MobileTripleValueManager", "getSaveMobileTriple(), empty data");
            return null;
        }
        try {
            JSONArray array = JSONArray.parseArray(strA);
            if (array == null) {
                return null;
            }
            a.a.a.a.a.a.a.a.a("MobileTripleValueManager", "getSaveMobileTriple(), data = " + array.toJSONString());
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < array.size(); i++) {
                JSONObject jSONObject = array.getJSONObject(i);
                e eVar = new e(jSONObject.getString(AlinkConstants.KEY_PK), jSONObject.getString(AlinkConstants.KEY_DN), jSONObject.getString("ds"));
                eVar.f1160a = jSONObject.getString("host");
                arrayList.add(eVar);
            }
            return arrayList;
        } catch (Exception unused) {
            a.a.a.a.a.a.a.a.a("MobileTripleValueManager", "getSaveMobileTriple(), params error");
            return null;
        }
    }

    public final e a(String str, List<e> list) {
        if (!TextUtils.isEmpty(str) && list != null && list.size() != 0) {
            for (e eVar : list) {
                if (str.equals(eVar.f1160a) && eVar.a()) {
                    a.a.a.a.a.a.a.a.a("MobileTripleValueManager", "getTripleByHost,get!");
                    return eVar;
                }
            }
        }
        return null;
    }
}
