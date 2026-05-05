package com.taobao.accs.net;

import com.taobao.accs.utl.UtilityImpl;
import org.android.spdy.AccsSSLCallback;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
class aa implements AccsSSLCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    final /* synthetic */ w f6362a;

    aa(w wVar) {
        this.f6362a = wVar;
    }

    @Override // org.android.spdy.AccsSSLCallback
    public byte[] getSSLPublicKey(int i, byte[] bArr) {
        return UtilityImpl.a(this.f6362a.m);
    }
}
