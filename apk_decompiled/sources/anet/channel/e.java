package anet.channel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class e {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private final Map<SessionRequest, List<Session>> f1718a = new HashMap();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private final ReentrantReadWriteLock f1719b = new ReentrantReadWriteLock();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private final ReentrantReadWriteLock.ReadLock f1720c = this.f1719b.readLock();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private final ReentrantReadWriteLock.WriteLock f1721d = this.f1719b.writeLock();

    e() {
    }

    public void a(SessionRequest sessionRequest, Session session) {
        if (sessionRequest == null || sessionRequest.a() == null || session == null) {
            return;
        }
        this.f1721d.lock();
        try {
            List<Session> arrayList = this.f1718a.get(sessionRequest);
            if (arrayList == null) {
                arrayList = new ArrayList<>();
                this.f1718a.put(sessionRequest, arrayList);
            }
            if (arrayList.indexOf(session) != -1) {
                return;
            }
            arrayList.add(session);
            Collections.sort(arrayList);
        } finally {
            this.f1721d.unlock();
        }
    }

    public void b(SessionRequest sessionRequest, Session session) {
        this.f1721d.lock();
        try {
            List<Session> list = this.f1718a.get(sessionRequest);
            if (list == null) {
                return;
            }
            list.remove(session);
            if (list.size() == 0) {
                this.f1718a.remove(sessionRequest);
            }
        } finally {
            this.f1721d.unlock();
        }
    }

    public List<Session> a(SessionRequest sessionRequest) {
        this.f1720c.lock();
        try {
            List<Session> list = this.f1718a.get(sessionRequest);
            if (list != null) {
                return new ArrayList(list);
            }
            return Collections.EMPTY_LIST;
        } finally {
            this.f1720c.unlock();
        }
    }

    public Session a(SessionRequest sessionRequest, int i) {
        this.f1720c.lock();
        try {
            List<Session> list = this.f1718a.get(sessionRequest);
            Session session = null;
            if (list != null && !list.isEmpty()) {
                for (Session session2 : list) {
                    if (session2 != null && session2.isAvailable() && (i == anet.channel.entity.c.f1743c || session2.j.getType() == i)) {
                        session = session2;
                        break;
                    }
                }
                return session;
            }
            return null;
        } finally {
            this.f1720c.unlock();
        }
    }

    public List<SessionRequest> a() {
        List<SessionRequest> list = Collections.EMPTY_LIST;
        this.f1720c.lock();
        try {
            return this.f1718a.isEmpty() ? list : new ArrayList(this.f1718a.keySet());
        } finally {
            this.f1720c.unlock();
        }
    }

    public boolean c(SessionRequest sessionRequest, Session session) {
        this.f1720c.lock();
        try {
            List<Session> list = this.f1718a.get(sessionRequest);
            if (list == null) {
                return false;
            }
            return list.indexOf(session) != -1;
        } finally {
            this.f1720c.unlock();
        }
    }
}
