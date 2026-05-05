package a.a.a.a.b.a;

import android.util.Pair;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import java.util.Map;

/* JADX INFO: Access modifiers changed from: package-private */
/* JADX INFO: compiled from: SIGMeshBizRequestGenerator.java */
/* JADX INFO: loaded from: classes.dex */
public class m implements I<byte[]> {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final /* synthetic */ int f1270a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final /* synthetic */ Map f1271b;

    public m(int i, Map map) {
        this.f1270a = i;
        this.f1271b = map;
    }

    @Override // a.a.a.a.b.a.I
    /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
    public Pair<Integer, Object> parseResponse(byte[] bArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("parseResponse() called with: result = [");
        sb.append(bArr == null ? TmpConstant.GROUP_ROLE_UNKNOWN : Integer.valueOf(bArr.length));
        sb.append("], setLen=");
        sb.append(this.f1270a);
        a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", sb.toString());
        if (bArr == null || bArr.length < 4) {
            return new Pair<>(-14, "Invalid length");
        }
        byte b2 = bArr[0];
        if (bArr.length != this.f1270a) {
            a.a.a.a.b.m.a.d("SIGMeshBizRequestGenerator", "scene bind failed.");
            byte b3 = bArr[bArr.length - 1];
            return new Pair<>(-53, b3 != 1 ? b3 != 2 ? "unknown error" : "Invalid Scene Number" : "Invalid request");
        }
        short s = (short) ((bArr[2] << 8) | bArr[1]);
        a.a.a.a.b.m.a.a("SIGMeshBizRequestGenerator", "Request, attributeSceneIdMap: " + this.f1271b + ", reply, sceneNumber: " + ((int) ((short) (bArr[bArr.length - 2] | (bArr[bArr.length - 1] << 8)))) + ", replyAttrType: " + ((int) s));
        return new Pair<>(0, true);
    }
}
