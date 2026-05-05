package anet.channel.strategy;

import android.content.Context;
import android.text.TextUtils;
import anet.channel.AwcnConfig;
import anet.channel.strategy.c;
import anet.channel.strategy.dispatch.AmdcRuntimeInfo;
import anet.channel.strategy.dispatch.DispatchEvent;
import anet.channel.strategy.dispatch.HttpDispatcher;
import anet.channel.strategy.l;
import anet.channel.util.ALog;
import anet.channel.util.HttpConstant;
import anet.channel.util.HttpUrl;
import anet.channel.util.StringUtils;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import config.Constants;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArraySet;
import org.json.JSONObject;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class g implements IStrategyInstance, HttpDispatcher.IDispatchEventListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    boolean f1890a = false;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    StrategyInfoHolder f1891b = null;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    long f1892c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    CopyOnWriteArraySet<IStrategyListener> f1893d = new CopyOnWriteArraySet<>();
    private IStrategyFilter e = new h(this);

    g() {
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public synchronized void initialize(Context context) {
        if (this.f1890a || context == null) {
            return;
        }
        try {
            ALog.i("awcn.StrategyCenter", "StrategyCenter initialize started.", null, new Object[0]);
            AmdcRuntimeInfo.setContext(context);
            m.a(context);
            HttpDispatcher.getInstance().addListener(this);
            this.f1891b = StrategyInfoHolder.a();
            this.f1890a = true;
            ALog.i("awcn.StrategyCenter", "StrategyCenter initialize finished.", null, new Object[0]);
        } catch (Exception e) {
            ALog.e("awcn.StrategyCenter", "StrategyCenter initialize failed.", null, e, new Object[0]);
        }
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public synchronized void switchEnv() {
        m.a();
        HttpDispatcher.getInstance().switchENV();
        if (this.f1891b != null) {
            this.f1891b.b();
            this.f1891b = StrategyInfoHolder.a();
        }
    }

    @Override // anet.channel.strategy.IStrategyInstance
    @Deprecated
    public String getSchemeByHost(String str) {
        return getSchemeByHost(str, null);
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public String getSchemeByHost(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (a()) {
            return str2;
        }
        String strA = this.f1891b.f1852b.a(str);
        if (strA != null || TextUtils.isEmpty(str2)) {
            str2 = strA;
        }
        if (str2 == null && (str2 = c.a.f1871a.a(str)) == null) {
            str2 = HttpConstant.HTTP;
        }
        ALog.d("awcn.StrategyCenter", "getSchemeByHost", null, "host", str, "scheme", str2);
        return str2;
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public String getCNameByHost(String str) {
        if (a() || TextUtils.isEmpty(str)) {
            return null;
        }
        return this.f1891b.d().getCnameByHost(str);
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public String getFormalizeUrl(String str) {
        HttpUrl httpUrl = HttpUrl.parse(str);
        if (httpUrl == null) {
            ALog.e("awcn.StrategyCenter", "url is invalid.", null, Constants.URL, str);
            return null;
        }
        String strUrlString = httpUrl.urlString();
        try {
            String schemeByHost = getSchemeByHost(httpUrl.host(), httpUrl.scheme());
            if (!schemeByHost.equalsIgnoreCase(httpUrl.scheme())) {
                strUrlString = StringUtils.concatString(schemeByHost, ":", str.substring(str.indexOf("//")));
            }
            if (ALog.isPrintLog(1)) {
                ALog.d("awcn.StrategyCenter", "", null, "raw", StringUtils.simplifyString(str, 128), "ret", StringUtils.simplifyString(strUrlString, 128));
            }
        } catch (Exception e) {
            ALog.e("awcn.StrategyCenter", "getFormalizeUrl failed", null, e, "raw", str);
        }
        return strUrlString;
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public List<IConnStrategy> getConnStrategyListByHost(String str) {
        return getConnStrategyListByHost(str, this.e);
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public List<IConnStrategy> getConnStrategyListByHost(String str, IStrategyFilter iStrategyFilter) {
        if (TextUtils.isEmpty(str) || a()) {
            return Collections.EMPTY_LIST;
        }
        String cnameByHost = this.f1891b.d().getCnameByHost(str);
        if (!TextUtils.isEmpty(cnameByHost)) {
            str = cnameByHost;
        }
        List listQueryByHost = this.f1891b.d().queryByHost(str);
        if (listQueryByHost.isEmpty()) {
            listQueryByHost = this.f1891b.f1853c.a(str);
        }
        if (listQueryByHost.isEmpty() || iStrategyFilter == null) {
            ALog.d("getConnStrategyListByHost", null, "host", str, "result", listQueryByHost);
            return listQueryByHost;
        }
        boolean z = !AwcnConfig.isIpv6Enable() || (AwcnConfig.isIpv6BlackListEnable() && this.f1891b.d().a(str, AwcnConfig.getIpv6BlackListTtl()));
        ListIterator<IConnStrategy> listIterator = listQueryByHost.listIterator();
        while (listIterator.hasNext()) {
            IConnStrategy next = listIterator.next();
            if (!iStrategyFilter.accept(next)) {
                listIterator.remove();
            } else if (z && anet.channel.strategy.utils.c.b(next.getIp())) {
                listIterator.remove();
            }
        }
        if (ALog.isPrintLog(1)) {
            ALog.d("getConnStrategyListByHost", null, "host", str, "result", listQueryByHost);
        }
        return listQueryByHost;
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public void forceRefreshStrategy(String str) {
        if (a() || TextUtils.isEmpty(str)) {
            return;
        }
        ALog.i("awcn.StrategyCenter", "force refresh strategy", null, "host", str);
        this.f1891b.d().a(str, true);
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public void registerListener(IStrategyListener iStrategyListener) {
        ALog.e("awcn.StrategyCenter", "registerListener", null, ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, this.f1893d);
        if (iStrategyListener != null) {
            this.f1893d.add(iStrategyListener);
        }
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public void unregisterListener(IStrategyListener iStrategyListener) {
        ALog.e("awcn.StrategyCenter", "unregisterListener", null, ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, this.f1893d);
        this.f1893d.remove(iStrategyListener);
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public String getUnitByHost(String str) {
        if (a()) {
            return null;
        }
        return this.f1891b.f1852b.b(str);
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public String getClientIp() {
        return a() ? "" : this.f1891b.d().f1860b;
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public void notifyConnEvent(String str, IConnStrategy iConnStrategy, ConnEvent connEvent) {
        if (a() || iConnStrategy == null || !(iConnStrategy instanceof IPConnStrategy)) {
            return;
        }
        IPConnStrategy iPConnStrategy = (IPConnStrategy) iConnStrategy;
        if (iPConnStrategy.f1842b == 1) {
            this.f1891b.f1853c.a(str, iConnStrategy, connEvent);
        } else if (iPConnStrategy.f1842b == 0) {
            this.f1891b.d().a(str, iConnStrategy, connEvent);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean a() {
        if (this.f1891b != null) {
            return false;
        }
        ALog.w("StrategyCenter not initialized", null, "isInitialized", Boolean.valueOf(this.f1890a));
        return true;
    }

    @Override // anet.channel.strategy.dispatch.HttpDispatcher.IDispatchEventListener
    public void onEvent(DispatchEvent dispatchEvent) {
        if (dispatchEvent.eventType != 1 || this.f1891b == null) {
            return;
        }
        ALog.d("awcn.StrategyCenter", "receive amdc event", null, new Object[0]);
        l.d dVarA = l.a((JSONObject) dispatchEvent.extraObject);
        if (dVarA == null) {
            return;
        }
        this.f1891b.a(dVarA);
        saveData();
        Iterator<IStrategyListener> it = this.f1893d.iterator();
        while (it.hasNext()) {
            try {
                it.next().onStrategyUpdated(dVarA);
            } catch (Exception e) {
                ALog.e("awcn.StrategyCenter", "onStrategyUpdated failed", null, e, new Object[0]);
            }
        }
    }

    @Override // anet.channel.strategy.IStrategyInstance
    public synchronized void saveData() {
        ALog.i("awcn.StrategyCenter", "saveData", null, new Object[0]);
        long jCurrentTimeMillis = System.currentTimeMillis();
        if (jCurrentTimeMillis - this.f1892c > 30000) {
            this.f1892c = jCurrentTimeMillis;
            anet.channel.strategy.utils.a.a(new i(this), 500L);
        }
    }
}
