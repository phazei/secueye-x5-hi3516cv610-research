package a.a.a.a.b.a;

import android.content.Context;
import android.util.Pair;
import meshprovisioner.configuration.bean.SceneRegisterStatus;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class x implements I<Object> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ Context f1284a;

    public x(Context context) {
        this.f1284a = context;
    }

    @Override // a.a.a.a.b.a.I
    public Pair<Integer, ?> parseResponse(Object obj) {
        return obj instanceof SceneRegisterStatus ? ((SceneRegisterStatus) obj).parseStatus(this.f1284a) : new Pair<>(-30, "internal error");
    }
}
