package anet.channel;

import anet.channel.util.HttpConstant;
import anetwork.channel.cache.CachePrediction;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
class k implements CachePrediction {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ j f1770a;

    k(j jVar) {
        this.f1770a = jVar;
    }

    @Override // anetwork.channel.cache.CachePrediction
    public boolean handleCache(String str, Map<String, String> map) {
        return "weex".equals(map.get(HttpConstant.F_REFER));
    }
}
