package com.taobao.accs.client;

import android.text.TextUtils;
import com.taobao.accs.IAppReceiver;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public class a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private ConcurrentHashMap<String, HashSet<IAppReceiver>> f6293a;

    /* JADX INFO: renamed from: com.taobao.accs.client.a$a, reason: collision with other inner class name */
    /* JADX INFO: compiled from: Taobao */
    private static class C0270a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        private static final a f6294a = new a();

        private C0270a() {
        }
    }

    public static a a() {
        return C0270a.f6294a;
    }

    private a() {
        this.f6293a = new ConcurrentHashMap<>(2);
    }

    public void a(String str, IAppReceiver iAppReceiver) {
        if (iAppReceiver != null) {
            HashSet<IAppReceiver> hashSet = this.f6293a.get(str);
            if (hashSet == null) {
                hashSet = new HashSet<>();
                this.f6293a.put(str, hashSet);
            }
            if (hashSet.contains(iAppReceiver)) {
                return;
            }
            hashSet.add(iAppReceiver);
        }
    }

    public ArrayList<IAppReceiver> a(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        HashSet<IAppReceiver> hashSet = this.f6293a.get(str);
        if (hashSet == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(hashSet);
    }

    public ArrayList<IAppReceiver> b() {
        HashSet hashSet = new HashSet();
        Iterator<HashSet<IAppReceiver>> it = this.f6293a.values().iterator();
        while (it.hasNext()) {
            hashSet.addAll(it.next());
        }
        return new ArrayList<>(hashSet);
    }

    public void b(String str) {
        try {
            this.f6293a.remove(str);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
