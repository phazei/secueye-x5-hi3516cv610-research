package com.aliyun.alink.linksdk.channel.core.itls;

import android.text.TextUtils;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.aliyun.alink.linksdk.channel.core.persistent.mqtt.MqttConfigure;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttClientPersistence;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPingSender;
import org.eclipse.paho.client.mqttv3.internal.ExceptionHelper;
import org.eclipse.paho.client.mqttv3.internal.NetworkModule;
import org.eclipse.paho.client.mqttv3.internal.SSLNetworkModule;
import org.eclipse.paho.client.mqttv3.internal.TCPNetworkModule;
import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import org.eclipse.paho.client.mqttv3.internal.websocket.WebSocketNetworkModule;
import org.eclipse.paho.client.mqttv3.internal.websocket.WebSocketSecureNetworkModule;

/* JADX INFO: compiled from: IoTMqttAsyncClient.java */
/* JADX INFO: loaded from: classes2.dex */
public class e extends MqttAsyncClient {
    public e(String str, String str2, MqttClientPersistence mqttClientPersistence) {
        super(str, str2, mqttClientPersistence);
    }

    public final NetworkModule a(String str, MqttConnectOptions mqttConnectOptions) throws MqttException {
        SSLSocketFactoryFactory sSLSocketFactoryFactory;
        String[] enabledCipherSuites;
        SocketFactory socketFactory;
        SSLSocketFactoryFactory sSLSocketFactoryFactory2;
        String[] enabledCipherSuites2;
        com.aliyun.alink.linksdk.channel.core.utils.a.a("IoTMqttAsyncClient", "createNetworkModule 115 address=" + str + ", client=" + this);
        SocketFactory socketFactory2 = mqttConnectOptions.getSocketFactory();
        int iValidateURI = MqttConnectOptions.validateURI(str);
        try {
            URI uri = new URI(str);
            if (uri.getHost() == null && str.contains(OpenAccountUIConstants.UNDER_LINE)) {
                try {
                    Field declaredField = URI.class.getDeclaredField("host");
                    declaredField.setAccessible(true);
                    declaredField.set(uri, a(str.substring(uri.getScheme().length() + 3)));
                } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
                    throw ExceptionHelper.createMqttException(e.getCause());
                }
            }
            String host = uri.getHost();
            int port = uri.getPort();
            if (iValidateURI == 0) {
                if (port == -1) {
                    port = 1883;
                }
                if (socketFactory2 == null) {
                    socketFactory2 = SocketFactory.getDefault();
                } else if (socketFactory2 instanceof SSLSocketFactory) {
                    throw ExceptionHelper.createMqttException(32105);
                }
                TCPNetworkModule tCPNetworkModule = new TCPNetworkModule(socketFactory2, host, port, getClientId());
                tCPNetworkModule.setConnectTimeout(mqttConnectOptions.getConnectionTimeout());
                return tCPNetworkModule;
            }
            if (iValidateURI != 1) {
                if (iValidateURI == 3) {
                    int i = port == -1 ? 80 : port;
                    if (socketFactory2 == null) {
                        socketFactory = SocketFactory.getDefault();
                    } else {
                        if (socketFactory2 instanceof SSLSocketFactory) {
                            throw ExceptionHelper.createMqttException(32105);
                        }
                        socketFactory = socketFactory2;
                    }
                    WebSocketNetworkModule webSocketNetworkModule = new WebSocketNetworkModule(socketFactory, str, host, i, getClientId());
                    webSocketNetworkModule.setConnectTimeout(mqttConnectOptions.getConnectionTimeout());
                    return webSocketNetworkModule;
                }
                if (iValidateURI != 4) {
                    return null;
                }
                int i2 = port == -1 ? 443 : port;
                if (socketFactory2 == null) {
                    SSLSocketFactoryFactory sSLSocketFactoryFactory3 = new SSLSocketFactoryFactory();
                    Properties sSLProperties = mqttConnectOptions.getSSLProperties();
                    if (sSLProperties != null) {
                        sSLSocketFactoryFactory3.initialize(sSLProperties, null);
                    }
                    sSLSocketFactoryFactory2 = sSLSocketFactoryFactory3;
                    socketFactory2 = sSLSocketFactoryFactory3.createSocketFactory(null);
                } else {
                    if (!(socketFactory2 instanceof SSLSocketFactory)) {
                        throw ExceptionHelper.createMqttException(32105);
                    }
                    sSLSocketFactoryFactory2 = null;
                }
                WebSocketSecureNetworkModule webSocketSecureNetworkModule = new WebSocketSecureNetworkModule((SSLSocketFactory) socketFactory2, str, host, i2, getClientId());
                webSocketSecureNetworkModule.setSSLhandshakeTimeout(mqttConnectOptions.getConnectionTimeout());
                if (sSLSocketFactoryFactory2 != null && (enabledCipherSuites2 = sSLSocketFactoryFactory2.getEnabledCipherSuites(null)) != null) {
                    webSocketSecureNetworkModule.setEnabledCiphers(enabledCipherSuites2);
                }
                return webSocketSecureNetworkModule;
            }
            com.aliyun.alink.linksdk.channel.core.utils.a.c("IoTMqttAsyncClient", "createNetworkModule channel(2-TLS,8-ITLS)=" + MqttConfigure.SECURE_MODE + ",host=" + host + ", port=" + port);
            if (port == -1) {
                port = 1883;
            }
            if (MqttConfigure.SECURE_MODE == 8) {
                a aVar = new a();
                aVar.f4089c = MqttConfigure.productKey;
                aVar.f4090d = MqttConfigure.productSecret;
                if (TextUtils.isEmpty(host)) {
                    aVar.f4087a = aVar.f4089c + aVar.f4087a;
                } else {
                    aVar.f4087a = host;
                }
                if (aVar.f4088b != -1) {
                    aVar.f4088b = port;
                }
                return new c(aVar);
            }
            if (port == -1) {
                port = 8883;
            }
            if (socketFactory2 == null) {
                sSLSocketFactoryFactory = new SSLSocketFactoryFactory();
                Properties sSLProperties2 = mqttConnectOptions.getSSLProperties();
                if (sSLProperties2 != null) {
                    sSLSocketFactoryFactory.initialize(sSLProperties2, null);
                }
                socketFactory2 = sSLSocketFactoryFactory.createSocketFactory(null);
            } else {
                if (!(socketFactory2 instanceof SSLSocketFactory)) {
                    throw ExceptionHelper.createMqttException(32105);
                }
                sSLSocketFactoryFactory = null;
            }
            SSLNetworkModule sSLNetworkModule = new SSLNetworkModule((SSLSocketFactory) socketFactory2, host, port, getClientId());
            sSLNetworkModule.setSSLhandshakeTimeout(mqttConnectOptions.getConnectionTimeout());
            sSLNetworkModule.setSSLHostnameVerifier(mqttConnectOptions.getSSLHostnameVerifier());
            if (sSLSocketFactoryFactory != null && (enabledCipherSuites = sSLSocketFactoryFactory.getEnabledCipherSuites(null)) != null) {
                sSLNetworkModule.setEnabledCiphers(enabledCipherSuites);
            }
            return sSLNetworkModule;
        } catch (URISyntaxException e2) {
            throw new IllegalArgumentException("Malformed URI: " + str + ", " + e2.getMessage());
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttAsyncClient, org.eclipse.paho.client.mqttv3.IMqttAsyncClient
    public IMqttToken connect(MqttConnectOptions mqttConnectOptions, Object obj, IMqttActionListener iMqttActionListener) throws MqttException {
        com.aliyun.alink.linksdk.channel.core.utils.a.c("IoTMqttAsyncClient", "mqtt-paho connect start, userContext = [" + obj + "], callback = [" + iMqttActionListener + "], [ clientId = " + getClientId() + "]");
        try {
            return super.connect(mqttConnectOptions, obj, iMqttActionListener);
        } catch (MqttException e) {
            throw e;
        } catch (Exception e2) {
            throw new MqttException(53301, e2);
        }
    }

    @Override // org.eclipse.paho.client.mqttv3.MqttAsyncClient
    public NetworkModule[] createNetworkModules(String str, MqttConnectOptions mqttConnectOptions) {
        String[] serverURIs = mqttConnectOptions.getServerURIs();
        if (serverURIs == null || serverURIs.length == 0) {
            serverURIs = new String[]{str};
        }
        NetworkModule[] networkModuleArr = new NetworkModule[serverURIs.length];
        for (int i = 0; i < serverURIs.length; i++) {
            networkModuleArr[i] = a(serverURIs[i], mqttConnectOptions);
        }
        return networkModuleArr;
    }

    public e(String str, String str2, MqttClientPersistence mqttClientPersistence, MqttPingSender mqttPingSender) {
        super(str, str2, mqttClientPersistence, mqttPingSender);
    }

    public final String a(String str) {
        int iIndexOf = str.indexOf(58);
        if (iIndexOf == -1) {
            iIndexOf = str.indexOf(47);
        }
        if (iIndexOf == -1) {
            iIndexOf = str.length();
        }
        return str.substring(0, iIndexOf);
    }
}
