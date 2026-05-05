package com.aliyun.alink.linksdk.cmp.api;

import android.content.Context;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.alcs.AlcsServerConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.apigw.ApiGatewayConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnectConfig;
import com.aliyun.alink.linksdk.cmp.connect.hubapi.HubApiClientConnectConfig;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectInfo;
import com.aliyun.alink.linksdk.cmp.core.base.AConnectOption;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResource;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectDiscovery;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectPublishResourceListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectResourceRegister;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectRrpcListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IResourceRequestListener;
import com.aliyun.alink.linksdk.cmp.manager.connect.IRegisterConnectListener;

/* JADX INFO: loaded from: classes2.dex */
public interface IConnectSDK {
    void destoryConnect(String str);

    IConnectDiscovery getAlcsDiscovery();

    String getAlcsDiscoveryConnectId();

    IConnectResourceRegister getAlcsResourceRegister();

    String getAlcsServerConnectId();

    String getApiGatewayConnectId();

    IConnectDiscovery getConnectDiscovery(String str);

    AConnectInfo getConnectInfo(String str);

    IConnectResourceRegister getConnectResourceRegister(String str);

    ConnectState getConnectState(String str);

    String getHubApiClientConnectId();

    String getPersistentConnectId();

    void init(Context context);

    boolean isConnectRegisted(String str);

    void publishResource(AResource aResource, IConnectPublishResourceListener iConnectPublishResourceListener);

    void registerAlcsConnect(Context context, String str, AlcsConnectConfig alcsConnectConfig, IRegisterConnectListener iRegisterConnectListener);

    void registerAlcsServerConnect(Context context, AlcsServerConnectConfig alcsServerConnectConfig, IRegisterConnectListener iRegisterConnectListener);

    void registerApiGatewayConnect(Context context, ApiGatewayConnectConfig apiGatewayConnectConfig, IRegisterConnectListener iRegisterConnectListener);

    void registerHubApiClientConnect(Context context, HubApiClientConnectConfig hubApiClientConnectConfig, IRegisterConnectListener iRegisterConnectListener);

    void registerNofityListener(String str, IConnectNotifyListener iConnectNotifyListener);

    void registerPersistentConnect(Context context, PersistentConnectConfig persistentConnectConfig, IRegisterConnectListener iRegisterConnectListener);

    void registerResource(AResource aResource, IResourceRequestListener iResourceRequestListener);

    void send(ARequest aRequest, IConnectSendListener iConnectSendListener);

    void send(String str, ARequest aRequest, IConnectSendListener iConnectSendListener);

    void subscribe(String str, ARequest aRequest, IConnectSubscribeListener iConnectSubscribeListener);

    @Deprecated
    void subscribeRrpc(String str, ARequest aRequest, IConnectRrpcListener iConnectRrpcListener);

    void unregisterConnect(String str);

    void unregisterNofityListener(IConnectNotifyListener iConnectNotifyListener);

    void unsubscribe(String str, ARequest aRequest, IConnectUnscribeListener iConnectUnscribeListener);

    void updateConnectOption(String str, AConnectOption aConnectOption);
}
