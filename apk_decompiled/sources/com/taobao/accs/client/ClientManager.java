package com.taobao.accs.client;

import android.content.Context;
import com.taobao.accs.utl.ALog;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class ClientManager {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Context f6286a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ConcurrentMap<String, Integer> f6287b = new ConcurrentHashMap();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private String f6288c = "ClientManager_";

    public ClientManager(Context context, String str, String str2, String str3) {
        if (context == null) {
            throw new RuntimeException("Context is null!!");
        }
        this.f6288c += str;
        this.f6286a = context.getApplicationContext();
    }

    public void onAppBind(String str) {
        Integer num = this.f6287b.get(str);
        if (num == null || num.intValue() != 2) {
            this.f6287b.put(str, 2);
        }
    }

    public void onAppUnbind(String str) {
        Integer num = this.f6287b.get(str);
        if (num == null || num.intValue() != 4) {
            this.f6287b.put(str, 4);
        }
    }

    public void onAppBinding(String str) {
        Integer num = this.f6287b.get(str);
        if (num == null || num.intValue() != 1) {
            this.f6287b.put(str, 1);
        }
    }

    public void onAppUnbinding(String str) {
        Integer num = this.f6287b.get(str);
        if (num == null || num.intValue() != 3) {
            this.f6287b.put(str, 3);
        }
    }

    public boolean isAppBinded(String str) {
        Integer num = this.f6287b.get(str);
        ALog.i(this.f6288c, "isAppBinded", "appStatus", num, "mBindStatus", this.f6287b);
        return num != null && num.intValue() == 2;
    }

    public boolean isAppUnbinded(String str) {
        Integer num = this.f6287b.get(str);
        return num != null && num.intValue() == 4;
    }

    public boolean isAppBinding(String str) {
        Integer num = this.f6287b.get(str);
        return num != null && num.intValue() == 1;
    }

    public boolean isAppUnbinding(String str) {
        Integer num = this.f6287b.get(str);
        return num != null && num.intValue() == 3;
    }

    public void clearClients() {
        try {
            this.f6287b.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
