package com.aliyun.iot.aep.sdk.apiclient;

import android.util.Log;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import okhttp3.Call;
import okhttp3.Connection;
import okhttp3.EventListener;
import okhttp3.Handshake;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

/* JADX INFO: compiled from: HttpsEventListener.java */
/* JADX INFO: loaded from: classes2.dex */
public class a extends EventListener {
    @Override // okhttp3.EventListener
    public void callEnd(Call call) {
        super.callEnd(call);
        Log.i("HttpsEventListener", "callEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void callFailed(Call call, IOException iOException) {
        super.callFailed(call, iOException);
        Log.e("HttpsEventListener", "callFailed: url:" + call.request().url() + "  exception:" + iOException.getMessage());
    }

    @Override // okhttp3.EventListener
    public void callStart(Call call) {
        super.callStart(call);
        Log.i("HttpsEventListener", "callStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        Log.i("HttpsEventListener", "connectEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException iOException) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, iOException);
        Log.e("HttpsEventListener", "connectFailed: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        Log.i("HttpsEventListener", "connectStart: url:" + call.request().url() + " host:" + inetSocketAddress.getHostName());
    }

    @Override // okhttp3.EventListener
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
        Log.i("HttpsEventListener", "connectionAcquired: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void connectionReleased(Call call, Connection connection) {
        super.connectionReleased(call, connection);
        Log.i("HttpsEventListener", "connectionReleased: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void dnsEnd(Call call, String str, List<InetAddress> list) {
        super.dnsEnd(call, str, list);
        Log.i("HttpsEventListener", "dnsEnd: url:" + call.request().url() + "  domainName:" + str + "  " + list);
    }

    @Override // okhttp3.EventListener
    public void dnsStart(Call call, String str) {
        super.dnsStart(call, str);
        Log.i("HttpsEventListener", "dnsStart: url:" + call.request().url() + "   domainName:" + str);
    }

    @Override // okhttp3.EventListener
    public void requestBodyEnd(Call call, long j) {
        super.requestBodyEnd(call, j);
        Log.i("HttpsEventListener", "requestBodyEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        Log.i("HttpsEventListener", "requestBodyStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        Log.i("HttpsEventListener", "requestHeadersEnd: url:" + request.url());
    }

    @Override // okhttp3.EventListener
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        Log.i("HttpsEventListener", "requestHeadersStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void responseBodyEnd(Call call, long j) {
        super.responseBodyEnd(call, j);
        Log.i("HttpsEventListener", "responseBodyEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        Log.i("HttpsEventListener", "responseBodyStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        Log.i("HttpsEventListener", "responseHeadersEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        Log.i("HttpsEventListener", "responseHeadersStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void secureConnectEnd(Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        Log.i("HttpsEventListener", "secureConnectEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        Log.i("HttpsEventListener", "secureConnectStart: url:" + call.request().url());
    }
}
