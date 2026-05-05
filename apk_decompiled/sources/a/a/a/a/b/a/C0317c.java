package a.a.a.a.b.a;

import com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest;

/* JADX INFO: renamed from: a.a.a.a.b.a.c, reason: case insensitive filesystem */
/* JADX INFO: compiled from: DefaultExecutionDispatcher.java */
/* JADX INFO: loaded from: classes.dex */
/* synthetic */ class C0317c {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final /* synthetic */ int[] f1254a = new int[SIGMeshBizRequest.InteractionModel.values().length];

    static {
        try {
            f1254a[SIGMeshBizRequest.InteractionModel.FIRE_AND_FORGET.ordinal()] = 1;
        } catch (NoSuchFieldError unused) {
        }
        try {
            f1254a[SIGMeshBizRequest.InteractionModel.REQUEST_RESPONSE.ordinal()] = 2;
        } catch (NoSuchFieldError unused2) {
        }
    }
}
