package a.a.a.a.b.a;

import android.content.Context;
import android.util.Pair;
import meshprovisioner.configuration.bean.CfgMsgModelSubscriptionStatus;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class u implements I<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Context f1281a;

    public u(Context context) {
        this.f1281a = context;
    }

    @Override // a.a.a.a.b.a.I
    public Pair<Integer, ?> parseResponse(Object obj) {
        if (obj instanceof CfgMsgModelSubscriptionStatus) {
            return CfgMsgModelSubscriptionStatus.parseStatus(this.f1281a, ((CfgMsgModelSubscriptionStatus) obj).getStatus());
        }
        if (obj instanceof byte[]) {
            return CfgMsgModelSubscriptionStatus.parseStatus(this.f1281a, ((byte[]) obj)[0]);
        }
        return null;
    }
}
