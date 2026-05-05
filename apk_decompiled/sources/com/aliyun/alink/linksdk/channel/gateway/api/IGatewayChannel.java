package com.aliyun.alink.linksdk.channel.gateway.api;

import android.content.Context;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceRemoveListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.SubDeviceInfo;
import com.aliyun.alink.linksdk.cmp.connect.channel.PersistentConnectConfig;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public interface IGatewayChannel {
    void addSubDevice(SubDeviceInfo subDeviceInfo, ISubDeviceConnectListener iSubDeviceConnectListener);

    void addSubDeviceForILopGlobal(SubDeviceInfo subDeviceInfo, ISubDeviceConnectListener iSubDeviceConnectListener);

    void asyncSendRequest(String str, String str2, Map<String, Object> map, Object obj, IGatewayRequestListener iGatewayRequestListener);

    void ayncSendPublishRequest(String str, String str2, Map<String, Object> map, Object obj, IGatewayRequestListener iGatewayRequestListener);

    void destroyConnect();

    void deviceListUpload(List<SubDeviceInfo> list, IConnectSendListener iConnectSendListener);

    GatewayConnectState getGatewayConnectState();

    PersistentConnectConfig getPersistentConnectConfig();

    ISubDeviceChannel getSubDeviceChannel(String str);

    void getSubDevices(IConnectSendListener iConnectSendListener);

    void registerDownstreamListener(boolean z, IGatewayDownstreamListener iGatewayDownstreamListener);

    void removeSubDevice(SubDeviceInfo subDeviceInfo, ISubDeviceRemoveListener iSubDeviceRemoveListener);

    void startConnect(Context context, PersistentConnectConfig persistentConnectConfig, IGatewayConnectListener iGatewayConnectListener);

    void startConnectReuseMobileChannel(Context context, IGatewayConnectListener iGatewayConnectListener);

    @Deprecated
    void subDevicUnregister(List<SubDeviceInfo> list, IConnectSendListener iConnectSendListener);

    void subDeviceRegister(ARequest aRequest, IConnectSendListener iConnectSendListener);

    void subDeviceRegister(List<SubDeviceInfo> list, IConnectSendListener iConnectSendListener);

    void subscribe(String str, IGatewaySubscribeListener iGatewaySubscribeListener);

    void unRegisterDownstreamListener(IGatewayDownstreamListener iGatewayDownstreamListener);

    void unSubscribe(String str, IGatewaySubscribeListener iGatewaySubscribeListener);
}
