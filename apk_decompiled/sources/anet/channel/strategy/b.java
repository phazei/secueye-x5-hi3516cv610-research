package anet.channel.strategy;

import anet.channel.util.ALog;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.taobao.accs.common.Constants;
import java.net.InetAddress;
import java.util.Collections;
import java.util.LinkedList;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class b implements Runnable {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ String f1866a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    final /* synthetic */ Object f1867b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    final /* synthetic */ a f1868c;

    b(a aVar, String str, Object obj) {
        this.f1868c = aVar;
        this.f1866a = str;
        this.f1867b = obj;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            try {
                String hostAddress = InetAddress.getByName(this.f1866a).getHostAddress();
                LinkedList linkedList = new LinkedList();
                ConnProtocol connProtocol = StrategyTemplate.getInstance().getConnProtocol(this.f1866a);
                if (connProtocol != null) {
                    linkedList.add(IPConnStrategy.a(hostAddress, !this.f1868c.a(connProtocol) ? 80 : Constants.PORT, connProtocol, 0, 0, 1, 45000));
                }
                linkedList.add(IPConnStrategy.a(hostAddress, 80, ConnProtocol.HTTP, 0, 0, 0, 0));
                linkedList.add(IPConnStrategy.a(hostAddress, Constants.PORT, ConnProtocol.HTTPS, 0, 0, 0, 0));
                this.f1868c.f1864a.put(this.f1866a, linkedList);
                if (ALog.isPrintLog(1)) {
                    ALog.d("awcn.LocalDnsStrategyTable", "resolve ip by local dns", null, "host", this.f1866a, "ip", hostAddress, AlinkConstants.KEY_LIST, linkedList);
                }
                synchronized (this.f1868c.f1865b) {
                    this.f1868c.f1865b.remove(this.f1866a);
                }
                synchronized (this.f1867b) {
                    this.f1867b.notifyAll();
                }
            } catch (Exception unused) {
                if (ALog.isPrintLog(1)) {
                    ALog.d("awcn.LocalDnsStrategyTable", "resolve ip by local dns failed", null, "host", this.f1866a);
                }
                this.f1868c.f1864a.put(this.f1866a, Collections.EMPTY_LIST);
                synchronized (this.f1868c.f1865b) {
                    this.f1868c.f1865b.remove(this.f1866a);
                    synchronized (this.f1867b) {
                        this.f1867b.notifyAll();
                    }
                }
            }
        } catch (Throwable th) {
            synchronized (this.f1868c.f1865b) {
                this.f1868c.f1865b.remove(this.f1866a);
                synchronized (this.f1867b) {
                    this.f1867b.notifyAll();
                    throw th;
                }
            }
        }
    }
}
