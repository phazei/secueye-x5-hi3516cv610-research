package lvchar;

import anet.channel.util.HttpConstant;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import lvcase.lvint;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/* JADX INFO: loaded from: classes4.dex */
public class lvif {

    /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
    private volatile URI f7983lvdo;

    /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
    private lvgoto.lvdo f7984lvfor;

    /* JADX INFO: renamed from: lvif, reason: collision with root package name */
    private OkHttpClient f7985lvif;

    static /* synthetic */ class lvdo {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        static final /* synthetic */ int[] f7986lvdo;

        static {
            int[] iArr = new int[lvlong.lvdo.values().length];
            f7986lvdo = iArr;
            try {
                iArr[lvlong.lvdo.POST.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                f7986lvdo[lvlong.lvdo.PUT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                f7986lvdo[lvlong.lvdo.GET.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                f7986lvdo[lvlong.lvdo.HEAD.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                f7986lvdo[lvlong.lvdo.DELETE.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    /* JADX INFO: renamed from: lvchar.lvif$lvif, reason: collision with other inner class name */
    class C0304lvif extends RequestBody {

        /* JADX INFO: renamed from: lvdo, reason: collision with root package name */
        private byte[] f7987lvdo;

        /* JADX INFO: renamed from: lvfor, reason: collision with root package name */
        private InputStream f7988lvfor;

        /* JADX INFO: renamed from: lvif, reason: collision with root package name */
        private File f7989lvif;

        /* JADX INFO: renamed from: lvint, reason: collision with root package name */
        private String f7990lvint;

        /* JADX INFO: renamed from: lvnew, reason: collision with root package name */
        private long f7991lvnew;

        public C0304lvif(File file, String str) {
            this.f7989lvif = file;
            this.f7990lvint = str;
            this.f7991lvnew = file.length();
        }

        public C0304lvif(InputStream inputStream, long j, String str) {
            this.f7988lvfor = inputStream;
            this.f7990lvint = str;
            this.f7991lvnew = j;
        }

        public C0304lvif(byte[] bArr, String str) {
            this.f7987lvdo = bArr;
            this.f7990lvint = str;
            this.f7991lvnew = bArr.length;
        }

        @Override // okhttp3.RequestBody
        public long contentLength() throws IOException {
            return this.f7991lvnew;
        }

        @Override // okhttp3.RequestBody
        public MediaType contentType() {
            return MediaType.parse(this.f7990lvint);
        }

        @Override // okhttp3.RequestBody
        public void writeTo(BufferedSink bufferedSink) throws IOException {
            Source source;
            File file = this.f7989lvif;
            if (file != null) {
                source = Okio.source(file);
            } else {
                byte[] bArr = this.f7987lvdo;
                if (bArr != null) {
                    source = Okio.source(new ByteArrayInputStream(bArr));
                } else {
                    InputStream inputStream = this.f7988lvfor;
                    source = inputStream != null ? Okio.source(inputStream) : null;
                }
            }
            long j = 0;
            while (true) {
                long j2 = this.f7991lvnew;
                if (j >= j2) {
                    break;
                }
                long j3 = source.read(bufferedSink.buffer(), Math.min(j2 - j, 2048L));
                if (j3 == -1) {
                    break;
                }
                j += j3;
                bufferedSink.flush();
            }
            if (source != null) {
                source.close();
            }
        }
    }

    public lvif(URI uri, lvgoto.lvdo lvdoVar, OkHttpClient okHttpClient) {
        this.f7983lvdo = uri;
        this.f7984lvfor = lvdoVar;
        this.f7985lvif = okHttpClient;
    }

    private lvbreak.lvdo lvdo(lvchar.lvdo lvdoVar) {
        Request.Builder builderMethod;
        String string;
        C0304lvif c0304lvif;
        lvbreak.lvdo lvdoVar2 = new lvbreak.lvdo();
        try {
            lvint.lvdo("[call] - ");
            Request.Builder builderUrl = new Request.Builder().url(lvdoVar.f7978lvfor);
            for (String str : lvdoVar.lvdo().keySet()) {
                builderUrl = builderUrl.addHeader(str, lvdoVar.lvdo().get(str));
            }
            String str2 = lvdoVar.lvdo().get("Content-Type");
            switch (lvdo.f7986lvdo[lvdoVar.lvif().ordinal()]) {
                case 1:
                case 2:
                    lvcatch.lvfor.lvdo(str2 != null, "Content type can't be null when upload!");
                    if (lvdoVar.lvint() != null) {
                        string = lvdoVar.lvif().toString();
                        c0304lvif = new C0304lvif(lvdoVar.lvint(), str2);
                    } else if (lvdoVar.lvnew() != null) {
                        string = lvdoVar.lvif().toString();
                        c0304lvif = new C0304lvif(new File(lvdoVar.lvnew()), str2);
                    } else if (lvdoVar.lvtry() == null) {
                        builderMethod = builderUrl.method(lvdoVar.lvif().toString(), RequestBody.create((MediaType) null, new byte[0]));
                        builderUrl = builderMethod;
                    } else {
                        string = lvdoVar.lvif().toString();
                        c0304lvif = new C0304lvif(lvdoVar.lvtry(), lvdoVar.lvfor(), str2);
                    }
                    builderMethod = builderUrl.method(string, c0304lvif);
                    builderUrl = builderMethod;
                    break;
                case 3:
                    builderMethod = builderUrl.get();
                    builderUrl = builderMethod;
                    break;
                case 4:
                    builderMethod = builderUrl.head();
                    builderUrl = builderMethod;
                    break;
                case 5:
                    builderMethod = builderUrl.delete();
                    builderUrl = builderMethod;
                    break;
            }
            Request requestBuild = builderUrl.build();
            Response responseExecute = this.f7985lvif.newCall(requestBuild).execute();
            Map<String, List<String>> multimap = responseExecute.headers().toMultimap();
            StringBuilder sb = new StringBuilder();
            sb.append("response:---------------------\n");
            sb.append("response code : " + responseExecute.code() + " for url : " + requestBuild.url() + SdkConstant.CLOUDAPI_LF);
            for (String str3 : multimap.keySet()) {
                sb.append("responseHeader [" + str3 + "]: ");
                StringBuilder sb2 = new StringBuilder();
                sb2.append(multimap.get(str3).get(0));
                sb2.append(SdkConstant.CLOUDAPI_LF);
                sb.append(sb2.toString());
            }
            lvint.lvdo(sb.toString());
            try {
                lvcatch.lvif.lvdo(lvcatch.lvif.lvdo(responseExecute.header("Date")).getTime());
            } catch (Exception unused) {
            }
            lvdoVar2.lvdo(new lvcase.lvfor(responseExecute.code(), "", ""));
            lvdoVar2.lvdo(responseExecute);
            responseExecute.close();
            return lvdoVar2;
        } catch (Exception e) {
            lvint.lvif("Encounter local execpiton: " + e.toString());
            if (lvint.lvif()) {
                e.printStackTrace();
            }
            lvdoVar2.lvdo(new lvcase.lvfor(-1, e.getMessage(), e.getCause(), ""));
            return lvdoVar2;
        }
    }

    private void lvdo(lvvoid.lvdo lvdoVar, lvchar.lvdo lvdoVar2) throws Throwable {
        if (lvdoVar == null || lvdoVar2 == null) {
            throw new lvcase.lvfor(500, "postLogRequest or requestMessage when buildheaders is not null", null, "");
        }
        lvthis.lvif lvifVar = lvdoVar.f8090lvfor;
        String str = lvdoVar.f8091lvif;
        String str2 = lvdoVar.f8089lvdo;
        String str3 = lvdoVar.f8092lvint;
        String str4 = str2 + "." + this.f7983lvdo.getHost();
        Map<String, String> map = lvdoVar2.f7977lvdo;
        map.put("x-log-apiversion", "0.6.0");
        map.put("x-log-signaturemethod", "hmac-sha1");
        map.put("x-log-compresstype", "deflate");
        map.put("Content-Type", str3);
        map.put("Date", lvcatch.lvfor.lvdo());
        map.put("Host", str4);
        try {
            byte[] bytes = lvifVar.lvdo().getBytes();
            byte[] bArrLvdo = lvcatch.lvfor.lvdo(bytes);
            lvdoVar2.lvdo(bArrLvdo);
            map.put("Content-MD5", lvcatch.lvfor.lvif(bArrLvdo));
            map.put("Content-Length", String.valueOf(bArrLvdo.length));
            map.put("x-log-bodyrawsize", String.valueOf(bytes.length));
            StringBuilder sb = new StringBuilder("POST\n");
            sb.append(map.get("Content-MD5") + SdkConstant.CLOUDAPI_LF);
            sb.append(map.get("Content-Type") + SdkConstant.CLOUDAPI_LF);
            sb.append(map.get("Date") + SdkConstant.CLOUDAPI_LF);
            lvgoto.lvdo lvdoVar3 = this.f7984lvfor;
            lvgoto.lvif lvifVarLvdo = lvdoVar3 instanceof lvgoto.lvint ? ((lvgoto.lvint) lvdoVar3).lvdo() : null;
            String strLvdo = lvifVarLvdo == null ? "" : lvifVarLvdo.lvdo();
            if (strLvdo != null && strLvdo != "") {
                map.put("x-acs-security-token", strLvdo);
                sb.append("x-acs-security-token:" + strLvdo + SdkConstant.CLOUDAPI_LF);
            }
            sb.append("x-log-apiversion:0.6.0\n");
            sb.append("x-log-bodyrawsize:" + map.get("x-log-bodyrawsize") + SdkConstant.CLOUDAPI_LF);
            sb.append("x-log-compresstype:deflate\n");
            sb.append("x-log-signaturemethod:hmac-sha1\n");
            sb.append("/logstores/" + str + "/shards/lb");
            String string = sb.toString();
            String strLvdo2 = lvcatch.lvfor.lvdo(lvifVarLvdo.lvif(), lvifVarLvdo.lvfor(), string);
            lvint.lvif("signed content: " + string + "   \n ---------   signature: " + strLvdo2, false);
            map.put("Authorization", strLvdo2);
            map.put("User-Agent", lvcatch.lvint.lvif());
        } catch (Exception unused) {
            throw new lvcase.lvfor(500, "postLogRequest or requestMessage is not null", null, "");
        }
    }

    private void lvif(lvvoid.lvdo lvdoVar, lvchar.lvdo lvdoVar2) throws lvcase.lvfor {
        if (lvdoVar == null || lvdoVar2 == null) {
            throw new lvcase.lvfor(500, "postLogRequest or requestMessage when buildUrl is not null", null, "");
        }
        String str = lvdoVar.f8091lvif;
        String str2 = lvdoVar.f8089lvdo;
        lvdoVar2.f7978lvfor = this.f7983lvdo.getScheme() + HttpConstant.SCHEME_SPLIT + (str2 + "." + this.f7983lvdo.getHost()) + "/logstores/" + str + "/shards/lb";
        lvdoVar2.f7979lvif = lvlong.lvdo.POST;
    }

    public lvbreak.lvdo lvdo(lvvoid.lvdo lvdoVar) throws Throwable {
        lvchar.lvdo lvdoVar2 = new lvchar.lvdo();
        try {
            lvif(lvdoVar, lvdoVar2);
            lvdo(lvdoVar, lvdoVar2);
        } catch (lvcase.lvfor e) {
            e.printStackTrace();
        }
        return lvdo(lvdoVar2);
    }
}
