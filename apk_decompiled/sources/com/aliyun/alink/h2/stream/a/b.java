package com.aliyun.alink.h2.stream.a;

import android.util.Log;
import com.aliyun.alink.h2.api.CompletableListener;
import com.aliyun.alink.h2.api.StreamWriteContext;
import com.aliyun.alink.h2.connection.Connection;
import com.aliyun.alink.h2.entity.Http2Response;
import com.aliyun.alink.h2.stream.entity.StreamData;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http2.Http2Stream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.android.agoo.common.AgooConstants;

/* JADX INFO: compiled from: FileManager.java */
/* JADX INFO: loaded from: classes2.dex */
public class b {

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private int f3906c = 0;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private AtomicBoolean f3907d = new AtomicBoolean(false);
    private final Object e = new Object();
    private CompletableListener<Http2Response> f = null;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    com.aliyun.alink.h2.stream.b.a f3904a = new com.aliyun.alink.h2.stream.b.a() { // from class: com.aliyun.alink.h2.stream.a.b.1
        @Override // com.aliyun.alink.h2.api.Http2StreamListener
        public void onStreamError(Connection connection, Http2Stream http2Stream, IOException iOException) {
            Log.d("FileManager", "onStreamError() called with: connection = [" + connection + "], http2Stream = [" + http2Stream + "], e = [" + iOException + "]");
            b.this.f3906c = 0;
            if (b.this.f3907d.compareAndSet(false, true) && b.this.f != null) {
                com.aliyun.alink.h2.stream.c.a.d("FileManager", "uploadFile onStreamError " + iOException);
                b.this.f.completeExceptionally(iOException);
            }
            synchronized (b.this.e) {
                b.this.e.notify();
                com.aliyun.alink.h2.stream.c.a.a("FileManager", AgooConstants.MESSAGE_NOTIFICATION);
            }
        }

        @Override // com.aliyun.alink.h2.stream.b.a
        public void a(Connection connection, Http2Stream http2Stream, StreamData streamData) {
            Log.d("FileManager", "onStreamDataReceived() called with: connection = [" + connection + "], stream = [" + http2Stream + "], streamData = [" + streamData + "]");
            b.this.f3907d.set(true);
            synchronized (b.this.e) {
                b.this.e.notify();
                com.aliyun.alink.h2.stream.c.a.a("FileManager", AgooConstants.MESSAGE_NOTIFICATION);
            }
            c cVar = new c(new Http2Response(streamData.getHeaders(), streamData.readAllData()));
            if (HttpResponseStatus.OK.equals(cVar.getStatus())) {
                if (b.this.f != null) {
                    b.this.f.complete(cVar);
                }
                com.aliyun.alink.h2.stream.c.a.b("FileManager", "upload file stream success, streamId: " + cVar.a());
                return;
            }
            com.aliyun.alink.h2.stream.c.a.d("FileManager", "upload file stream failed, response=" + cVar);
            if (b.this.f != null) {
                b.this.f.completeExceptionally(new IllegalStateException("upload file failed " + cVar));
            }
        }
    };

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    CompletableListener<StreamWriteContext> f3905b = new CompletableListener<StreamWriteContext>() { // from class: com.aliyun.alink.h2.stream.a.b.2
        @Override // com.aliyun.alink.h2.api.CompletableListener
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public void complete(StreamWriteContext streamWriteContext) {
            com.aliyun.alink.h2.stream.c.a.a("FileManager", "complete() called with: streamWriteContext = [" + streamWriteContext + "]");
            if (streamWriteContext != null && b.this.f3906c == 0 && streamWriteContext.getStream() != null && streamWriteContext.getStream().id() != 0) {
                b.this.f3906c = streamWriteContext.getStream().id();
            }
            synchronized (b.this.e) {
                b.this.e.notify();
                com.aliyun.alink.h2.stream.c.a.a("FileManager", AgooConstants.MESSAGE_NOTIFICATION);
            }
        }

        @Override // com.aliyun.alink.h2.api.CompletableListener
        public void completeExceptionally(Throwable th) {
            if (b.this.f3907d.compareAndSet(false, true) && b.this.f != null) {
                com.aliyun.alink.h2.stream.c.a.d("FileManager", "sendFile completeExceptionally throwable=" + th);
                b.this.f.completeExceptionally(th);
            }
            b.this.f3906c = 0;
            synchronized (b.this.e) {
                b.this.e.notify();
                com.aliyun.alink.h2.stream.c.a.a("FileManager", AgooConstants.MESSAGE_NOTIFICATION);
            }
            com.aliyun.alink.h2.stream.c.a.d("FileManager", "completeExceptionally() called with: throwable = [" + th + "]");
        }
    };

    /* JADX WARN: Removed duplicated region for block: B:109:0x0204 A[Catch: all -> 0x01e1, TRY_LEAVE, TryCatch #0 {all -> 0x01e1, blocks: (B:8:0x0019, B:107:0x01e8, B:109:0x0204, B:115:0x0217, B:117:0x0233, B:123:0x0246, B:125:0x0262), top: B:152:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:112:0x0212 A[Catch: Throwable -> 0x0274, Exception -> 0x0279, IOException -> 0x027e, TRY_LEAVE, TryCatch #16 {IOException -> 0x027e, Exception -> 0x0279, Throwable -> 0x0274, blocks: (B:90:0x01bb, B:110:0x0207, B:112:0x0212, B:118:0x0236, B:120:0x0241, B:126:0x0265, B:128:0x0270), top: B:162:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x0233 A[Catch: all -> 0x01e1, TRY_LEAVE, TryCatch #0 {all -> 0x01e1, blocks: (B:8:0x0019, B:107:0x01e8, B:109:0x0204, B:115:0x0217, B:117:0x0233, B:123:0x0246, B:125:0x0262), top: B:152:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:120:0x0241 A[Catch: Throwable -> 0x0274, Exception -> 0x0279, IOException -> 0x027e, TRY_LEAVE, TryCatch #16 {IOException -> 0x027e, Exception -> 0x0279, Throwable -> 0x0274, blocks: (B:90:0x01bb, B:110:0x0207, B:112:0x0212, B:118:0x0236, B:120:0x0241, B:126:0x0265, B:128:0x0270), top: B:162:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:125:0x0262 A[Catch: all -> 0x01e1, TRY_LEAVE, TryCatch #0 {all -> 0x01e1, blocks: (B:8:0x0019, B:107:0x01e8, B:109:0x0204, B:115:0x0217, B:117:0x0233, B:123:0x0246, B:125:0x0262), top: B:152:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0270 A[Catch: Throwable -> 0x0274, Exception -> 0x0279, IOException -> 0x027e, TRY_LEAVE, TryCatch #16 {IOException -> 0x027e, Exception -> 0x0279, Throwable -> 0x0274, blocks: (B:90:0x01bb, B:110:0x0207, B:112:0x0212, B:118:0x0236, B:120:0x0241, B:126:0x0265, B:128:0x0270), top: B:162:0x0019 }] */
    /* JADX WARN: Removed duplicated region for block: B:139:0x028e A[Catch: Throwable -> 0x0292, Exception -> 0x0297, IOException -> 0x029c, TRY_LEAVE, TryCatch #15 {IOException -> 0x029c, Exception -> 0x0297, Throwable -> 0x0292, blocks: (B:137:0x0283, B:139:0x028e), top: B:164:0x0283 }] */
    /* JADX WARN: Removed duplicated region for block: B:183:? A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void a(com.aliyun.alink.h2.api.IotHttp2Client r20, java.lang.String r21, java.lang.String r22, com.aliyun.alink.h2.entity.Http2Request r23, com.aliyun.alink.h2.api.CompletableListener<com.aliyun.alink.h2.entity.Http2Response> r24) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 694
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.h2.stream.a.b.a(com.aliyun.alink.h2.api.IotHttp2Client, java.lang.String, java.lang.String, com.aliyun.alink.h2.entity.Http2Request, com.aliyun.alink.h2.api.CompletableListener):void");
    }
}
