package com.alibaba.sdk.android.openaccount.rpc.cloudapi;

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

/* JADX INFO: loaded from: classes.dex */
class HttpsEventListener extends EventListener {
    private static final String TAG = "HttpsEventListenerOA";

    @Override // okhttp3.EventListener
    public void callStart(Call call) {
        super.callStart(call);
        Log.i(TAG, "callStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void dnsStart(Call call, String str) {
        super.dnsStart(call, str);
        Log.i(TAG, "dnsStart: url:" + call.request().url() + "   domainName:" + str);
    }

    @Override // okhttp3.EventListener
    public void dnsEnd(Call call, String str, List<InetAddress> list) {
        super.dnsEnd(call, str, list);
        Log.i(TAG, "dnsEnd: url:" + call.request().url() + "  domainName:" + str + "  " + list);
    }

    @Override // okhttp3.EventListener
    public void connectStart(Call call, InetSocketAddress inetSocketAddress, Proxy proxy) {
        super.connectStart(call, inetSocketAddress, proxy);
        Log.i(TAG, "connectStart: url:" + call.request().url() + " host:" + inetSocketAddress.getHostName());
    }

    @Override // okhttp3.EventListener
    public void secureConnectStart(Call call) {
        super.secureConnectStart(call);
        Log.i(TAG, "secureConnectStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void secureConnectEnd(Call call, Handshake handshake) {
        super.secureConnectEnd(call, handshake);
        Log.i(TAG, "secureConnectEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void connectEnd(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol) {
        super.connectEnd(call, inetSocketAddress, proxy, protocol);
        Log.i(TAG, "connectEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void connectFailed(Call call, InetSocketAddress inetSocketAddress, Proxy proxy, Protocol protocol, IOException iOException) {
        super.connectFailed(call, inetSocketAddress, proxy, protocol, iOException);
        Log.e(TAG, "connectFailed: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void connectionAcquired(Call call, Connection connection) {
        super.connectionAcquired(call, connection);
        Log.i(TAG, "connectionAcquired: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void connectionReleased(Call call, Connection connection) {
        super.connectionReleased(call, connection);
        Log.i(TAG, "connectionReleased: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void requestHeadersStart(Call call) {
        super.requestHeadersStart(call);
        Log.i(TAG, "requestHeadersStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void requestHeadersEnd(Call call, Request request) {
        super.requestHeadersEnd(call, request);
        Log.i(TAG, "requestHeadersEnd: url:" + request.url());
    }

    @Override // okhttp3.EventListener
    public void requestBodyStart(Call call) {
        super.requestBodyStart(call);
        Log.i(TAG, "requestBodyStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void requestBodyEnd(Call call, long j) {
        super.requestBodyEnd(call, j);
        Log.i(TAG, "requestBodyEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void responseHeadersStart(Call call) {
        super.responseHeadersStart(call);
        Log.i(TAG, "responseHeadersStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void responseHeadersEnd(Call call, Response response) {
        super.responseHeadersEnd(call, response);
        Log.i(TAG, "responseHeadersEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void responseBodyStart(Call call) {
        super.responseBodyStart(call);
        Log.i(TAG, "responseBodyStart: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void responseBodyEnd(Call call, long j) {
        super.responseBodyEnd(call, j);
        Log.i(TAG, "responseBodyEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void callEnd(Call call) {
        super.callEnd(call);
        Log.i(TAG, "callEnd: url:" + call.request().url());
    }

    @Override // okhttp3.EventListener
    public void callFailed(Call call, IOException iOException) {
        super.callFailed(call, iOException);
        Log.e(TAG, "callFailed: url:" + call.request().url() + "  exception:" + iOException.getMessage());
    }
}
