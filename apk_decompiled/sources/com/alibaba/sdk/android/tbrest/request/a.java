package com.alibaba.sdk.android.tbrest.request;

import android.net.SSLCertificateSocketFactory;
import android.os.Build;
import com.alibaba.sdk.android.tbrest.utils.LogUtil;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/* JADX INFO: compiled from: RestSslSocketFactory.java */
/* JADX INFO: loaded from: classes.dex */
public class a extends SSLSocketFactory {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private Method f3186a = null;
    private String j;

    @Override // javax.net.SocketFactory
    public Socket createSocket() throws IOException {
        return null;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i) throws IOException {
        return null;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(String str, int i, InetAddress inetAddress, int i2) throws IOException {
        return null;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i) throws IOException {
        return null;
    }

    @Override // javax.net.SocketFactory
    public Socket createSocket(InetAddress inetAddress, int i, InetAddress inetAddress2, int i2) throws IOException {
        return null;
    }

    public a(String str) {
        this.j = str;
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getDefaultCipherSuites() {
        return new String[0];
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public String[] getSupportedCipherSuites() {
        return new String[0];
    }

    @Override // javax.net.ssl.SSLSocketFactory
    public Socket createSocket(Socket socket, String str, int i, boolean z) throws IOException {
        if (this.j == null) {
            this.j = str;
        }
        LogUtil.d("host" + this.j + RtspHeaders.Values.PORT + i + "autoClose" + z);
        InetAddress inetAddress = socket.getInetAddress();
        if (z) {
            socket.close();
        }
        SSLCertificateSocketFactory sSLCertificateSocketFactory = (SSLCertificateSocketFactory) SSLCertificateSocketFactory.getDefault(0);
        SSLSocket sSLSocket = (SSLSocket) sSLCertificateSocketFactory.createSocket(inetAddress, i);
        sSLSocket.setEnabledProtocols(sSLSocket.getSupportedProtocols());
        if (Build.VERSION.SDK_INT >= 17) {
            sSLCertificateSocketFactory.setHostname(sSLSocket, this.j);
        } else {
            try {
                if (this.f3186a == null) {
                    this.f3186a = sSLSocket.getClass().getMethod("setHostname", String.class);
                    this.f3186a.setAccessible(true);
                }
                this.f3186a.invoke(sSLSocket, this.j);
            } catch (Exception e) {
                LogUtil.w("SNI not useable", e);
            }
        }
        sSLSocket.getSession();
        return sSLSocket;
    }
}
