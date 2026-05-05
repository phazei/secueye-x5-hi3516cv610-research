package anet.channel.e;

import android.content.SharedPreferences;
import anet.channel.entity.ConnType;
import anet.channel.status.NetworkStatusHelper;
import anet.channel.strategy.IStrategyListener;
import anet.channel.strategy.l;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
final class c implements IStrategyListener {
    c() {
    }

    @Override // anet.channel.strategy.IStrategyListener
    public void onStrategyUpdated(l.d dVar) {
        if (dVar == null || dVar.f1912b == null) {
            return;
        }
        for (int i = 0; i < dVar.f1912b.length; i++) {
            String str = dVar.f1912b[i].f1905a;
            l.a[] aVarArr = dVar.f1912b[i].h;
            if (aVarArr != null && aVarArr.length > 0) {
                for (l.a aVar : aVarArr) {
                    String str2 = aVar.f1902b;
                    if (ConnType.HTTP3.equals(str2) || ConnType.HTTP3_PLAIN.equals(str2)) {
                        if (!str.equals(a.f1723b)) {
                            String unused = a.f1723b = str;
                            SharedPreferences.Editor editorEdit = a.f.edit();
                            editorEdit.putString("http3_detector_host", a.f1723b);
                            editorEdit.apply();
                        }
                        a.a(NetworkStatusHelper.getStatus());
                        return;
                    }
                }
            }
        }
    }
}
