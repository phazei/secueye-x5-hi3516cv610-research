package com.taobao.accs.net;

import com.taobao.accs.data.Message;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class x implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ Message f6412a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ boolean f6413b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ w f6414c;

    x(w wVar, Message message, boolean z) {
        this.f6414c = wVar;
        this.f6412a = message;
        this.f6413b = z;
    }

    @Override // java.lang.Runnable
    public void run() {
        synchronized (this.f6414c.t) {
            this.f6414c.a(this.f6412a);
            if (this.f6414c.t.size() == 0) {
                this.f6414c.t.add(this.f6412a);
            } else {
                Message message = (Message) this.f6414c.t.getFirst();
                if (this.f6412a.a() == 1 || this.f6412a.a() == 0) {
                    this.f6414c.t.addLast(this.f6412a);
                    if (message.a() == 2) {
                        this.f6414c.t.removeFirst();
                    }
                } else if (this.f6412a.a() != 2 || message.a() != 2) {
                    this.f6414c.t.addLast(this.f6412a);
                } else if (!message.f6300d && this.f6412a.f6300d) {
                    this.f6414c.t.removeFirst();
                    this.f6414c.t.addFirst(this.f6412a);
                }
            }
            if (this.f6413b || this.f6414c.s == 3) {
                try {
                    this.f6414c.t.notifyAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
