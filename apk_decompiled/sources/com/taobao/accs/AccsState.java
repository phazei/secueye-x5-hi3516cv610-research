package com.taobao.accs;

import android.os.SystemClock;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class AccsState {
    public static final String ALL = "all";
    public static final String BIND_APP_FROM_CACHE = "bfc";
    public static final String CONNECTION_CHANGE = "cc";
    public static final String LAST_MSG_RECEIVE_TIME = "lmrt";
    public static final String LAST_MSG_SEND_TIME = "lmst";
    public static final String RECENT_ERRORS = "re";
    public static final String SDK_VERSION = "sv";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private HashMap<String, c> f6264a = new HashMap<>();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private long f6265b = -1;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private long f6266c = -1;

    /* JADX INFO: compiled from: Taobao */
    private static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final AccsState f6267a = new AccsState();

        private a() {
        }
    }

    public static AccsState getInstance() {
        return a.f6267a;
    }

    protected AccsState() {
    }

    public synchronized void a(String str, Object obj) {
        a("all").a(str, obj, b());
    }

    public synchronized void b(String str, Object obj) {
        a("all").b(str, obj, b());
    }

    public synchronized String getState() {
        if (!a(this.f6264a)) {
            return "{}";
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("t", this.f6266c);
            for (Map.Entry entry : new ArrayList(this.f6264a.entrySet())) {
                jSONObject.put((String) entry.getKey(), ((c) entry.getValue()).a());
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return jSONObject.toString();
    }

    public synchronized String getStateByKey(String str) {
        if (!a(this.f6264a, str)) {
            return "{}";
        }
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("t", this.f6266c);
            for (Map.Entry entry : new ArrayList(this.f6264a.entrySet())) {
                if (((c) entry.getValue()).a(str)) {
                    jSONObject.put((String) entry.getKey(), ((c) entry.getValue()).b(str));
                }
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        return jSONObject.toString();
    }

    public synchronized void a(String str, String str2, Object obj) {
        a(str).a(str2, obj, b());
    }

    public synchronized void b(String str, String str2, Object obj) {
        a(str).b(str2, obj, b());
    }

    private c a(String str) {
        c cVar = this.f6264a.get(str);
        if (cVar != null) {
            return cVar;
        }
        c cVar2 = new c();
        this.f6264a.put(str, cVar2);
        return cVar2;
    }

    private void a() {
        if (this.f6266c < 0 || this.f6265b < 0) {
            this.f6266c = System.currentTimeMillis();
            this.f6265b = SystemClock.elapsedRealtime();
        }
    }

    private long b() {
        a();
        return SystemClock.elapsedRealtime() - this.f6265b;
    }

    private boolean a(HashMap<String, c> map) {
        return map.size() > 0;
    }

    private boolean a(HashMap<String, c> map, String str) {
        Iterator it = new ArrayList(map.values()).iterator();
        while (it.hasNext()) {
            if (((c) it.next()).a(str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: compiled from: Taobao */
    private static class c {
        public static final int MAX_HISTORY = 5;

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private HashMap<String, b> f6271a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private HashMap<String, ArrayList<b>> f6272b;

        private c() {
            this.f6271a = new HashMap<>();
            this.f6272b = new HashMap<>();
        }

        public void a(String str, Object obj, long j) {
            this.f6271a.put(str, new b(j, str, a(obj)));
        }

        public void b(String str, Object obj, long j) {
            ArrayList<b> arrayList = this.f6272b.get(str);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.f6272b.put(str, arrayList);
            }
            arrayList.add(new b(j, str, a(obj)));
            while (arrayList.size() > 5) {
                arrayList.remove(0);
            }
        }

        private static String a(Object obj) {
            return obj == null ? TmpConstant.GROUP_ROLE_UNKNOWN : obj.toString();
        }

        public boolean a(String str) {
            return this.f6271a.keySet().contains(str) || this.f6272b.keySet().contains(str);
        }

        public JSONArray b(String str) {
            JSONArray jSONArray = new JSONArray();
            b bVar = this.f6271a.get(str);
            if (bVar != null) {
                jSONArray.put(bVar.a());
            }
            ArrayList<b> arrayList = this.f6272b.get(str);
            if (arrayList != null) {
                Iterator<b> it = arrayList.iterator();
                while (it.hasNext()) {
                    jSONArray.put(it.next().a());
                }
            }
            return jSONArray;
        }

        public JSONArray a() {
            JSONArray jSONArray = new JSONArray();
            Iterator it = new ArrayList(this.f6271a.values()).iterator();
            while (it.hasNext()) {
                jSONArray.put(((b) it.next()).a());
            }
            ArrayList arrayList = new ArrayList();
            Iterator<ArrayList<b>> it2 = this.f6272b.values().iterator();
            while (it2.hasNext()) {
                arrayList.addAll(it2.next());
            }
            Iterator it3 = arrayList.iterator();
            while (it3.hasNext()) {
                jSONArray.put(((b) it3.next()).a());
            }
            return jSONArray;
        }
    }

    /* JADX INFO: compiled from: Taobao */
    private static class b {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public long f6268a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public String f6269b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public String f6270c;

        public b(long j, String str, String str2) {
            this.f6268a = j;
            this.f6269b = str;
            this.f6270c = str2;
        }

        public JSONArray a() {
            JSONArray jSONArray = new JSONArray();
            jSONArray.put(this.f6268a);
            jSONArray.put(this.f6269b);
            jSONArray.put(this.f6270c);
            return jSONArray;
        }
    }
}
