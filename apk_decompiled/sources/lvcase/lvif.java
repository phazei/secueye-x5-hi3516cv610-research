package lvcase;

import java.net.URI;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import okhttp3.OkHttpClient;

/* JADX INFO: loaded from: classes4.dex */
public class lvif {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private String f7967lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private URI f7968lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private String f7969lvif;

    /* JADX INFO: renamed from: lvint, reason: collision with root package name */
    OkHttpClient f7970lvint;

    /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
    private lvchar.lvif f7971lvnew;

    class lvdo implements HostnameVerifier {
        lvdo() {
        }

        @Override // javax.net.ssl.HostnameVerifier
        public boolean verify(String str, SSLSession sSLSession) {
            return HttpsURLConnection.getDefaultHostnameVerifier().verify(lvif.this.f7968lvfor.getHost(), sSLSession);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x0045 A[Catch: URISyntaxException -> 0x00f6, TryCatch #0 {URISyntaxException -> 0x00f6, blocks: (B:3:0x0003, B:5:0x000d, B:7:0x0017, B:15:0x0051, B:12:0x003a, B:14:0x0045, B:16:0x0054, B:8:0x0021, B:10:0x002b, B:26:0x00ee, B:27:0x00f5), top: B:30:0x0003 }] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0054 A[Catch: URISyntaxException -> 0x00f6, TRY_LEAVE, TryCatch #0 {URISyntaxException -> 0x00f6, blocks: (B:3:0x0003, B:5:0x000d, B:7:0x0017, B:15:0x0051, B:12:0x003a, B:14:0x0045, B:16:0x0054, B:8:0x0021, B:10:0x002b, B:26:0x00ee, B:27:0x00f5), top: B:30:0x0003 }] */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:14:0x0045 -> B:15:0x0051). Please report as a decompilation issue!!! */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public lvif(java.lang.String r8, lvgoto.lvdo r9, lvcase.lvdo r10) {
        /*
            Method dump skipped, instruction units count: 254
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: lvcase.lvif.<init>(java.lang.String, lvgoto.lvdo, lvcase.lvdo):void");
    }

    public lvbreak.lvdo lvdo(lvvoid.lvdo lvdoVar) {
        return this.f7971lvnew.lvdo(lvdoVar);
    }
}
