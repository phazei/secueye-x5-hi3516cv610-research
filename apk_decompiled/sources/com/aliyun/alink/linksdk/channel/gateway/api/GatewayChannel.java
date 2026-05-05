package com.aliyun.alink.linksdk.channel.gateway.api;

import android.content.Context;
import com.aliyun.alink.linksdk.channel.gateway.a.a;
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
public class GatewayChannel implements IGatewayChannel {
    public static final String DID_SEPARATOR = "-&&-";
    public static final String METHOD_PRESET_SUBDEV_REGITER = "thing.proxy.provisioning.product_register";
    public static final String TOPIC_PRESET_SUBDEV_REGITER = "/thing/proxy/provisioning/product_register";
    private a gatewayChannelImpl;

    private GatewayChannel() {
        this.gatewayChannelImpl = null;
        this.gatewayChannelImpl = new a();
    }

    public void setNeedAutoLoginSubdevice(boolean z) {
        this.gatewayChannelImpl.a(z);
    }

    private static class InstanceHolder {
        private static final IGatewayChannel sInstance = new GatewayChannel();

        private InstanceHolder() {
        }
    }

    public static IGatewayChannel getInstance() {
        return InstanceHolder.sInstance;
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void startConnect(Context context, PersistentConnectConfig persistentConnectConfig, IGatewayConnectListener iGatewayConnectListener) {
        this.gatewayChannelImpl.startConnect(context, persistentConnectConfig, iGatewayConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void startConnectReuseMobileChannel(Context context, IGatewayConnectListener iGatewayConnectListener) {
        this.gatewayChannelImpl.startConnectReuseMobileChannel(context, iGatewayConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public GatewayConnectState getGatewayConnectState() {
        return this.gatewayChannelImpl.getGatewayConnectState();
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void subDeviceRegister(List<SubDeviceInfo> list, IConnectSendListener iConnectSendListener) {
        this.gatewayChannelImpl.subDeviceRegister(list, iConnectSendListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void subDeviceRegister(ARequest aRequest, IConnectSendListener iConnectSendListener) {
        this.gatewayChannelImpl.subDeviceRegister(aRequest, iConnectSendListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void subDevicUnregister(List<SubDeviceInfo> list, IConnectSendListener iConnectSendListener) {
        this.gatewayChannelImpl.subDevicUnregister(list, iConnectSendListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void addSubDevice(SubDeviceInfo subDeviceInfo, ISubDeviceConnectListener iSubDeviceConnectListener) {
        this.gatewayChannelImpl.addSubDevice(subDeviceInfo, iSubDeviceConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void addSubDeviceForILopGlobal(SubDeviceInfo subDeviceInfo, ISubDeviceConnectListener iSubDeviceConnectListener) {
        this.gatewayChannelImpl.addSubDeviceForILopGlobal(subDeviceInfo, iSubDeviceConnectListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void removeSubDevice(SubDeviceInfo subDeviceInfo, ISubDeviceRemoveListener iSubDeviceRemoveListener) {
        this.gatewayChannelImpl.removeSubDevice(subDeviceInfo, iSubDeviceRemoveListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void getSubDevices(IConnectSendListener iConnectSendListener) {
        this.gatewayChannelImpl.getSubDevices(iConnectSendListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void deviceListUpload(List<SubDeviceInfo> list, IConnectSendListener iConnectSendListener) {
        this.gatewayChannelImpl.deviceListUpload(list, iConnectSendListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void asyncSendRequest(String str, String str2, Map<String, Object> map, Object obj, IGatewayRequestListener iGatewayRequestListener) {
        this.gatewayChannelImpl.asyncSendRequest(str, str2, map, obj, iGatewayRequestListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void ayncSendPublishRequest(String str, String str2, Map<String, Object> map, Object obj, IGatewayRequestListener iGatewayRequestListener) {
        this.gatewayChannelImpl.ayncSendPublishRequest(str, str2, map, obj, iGatewayRequestListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void subscribe(String str, IGatewaySubscribeListener iGatewaySubscribeListener) {
        this.gatewayChannelImpl.subscribe(str, iGatewaySubscribeListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void unSubscribe(String str, IGatewaySubscribeListener iGatewaySubscribeListener) {
        this.gatewayChannelImpl.unSubscribe(str, iGatewaySubscribeListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void registerDownstreamListener(boolean z, IGatewayDownstreamListener iGatewayDownstreamListener) {
        this.gatewayChannelImpl.registerDownstreamListener(z, iGatewayDownstreamListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void unRegisterDownstreamListener(IGatewayDownstreamListener iGatewayDownstreamListener) {
        this.gatewayChannelImpl.unRegisterDownstreamListener(iGatewayDownstreamListener);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public ISubDeviceChannel getSubDeviceChannel(String str) {
        return this.gatewayChannelImpl.getSubDeviceChannel(str);
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public void destroyConnect() {
        a aVar = this.gatewayChannelImpl;
        if (aVar != null) {
            aVar.destroyConnect();
        }
    }

    @Override // com.aliyun.alink.linksdk.channel.gateway.api.IGatewayChannel
    public PersistentConnectConfig getPersistentConnectConfig() {
        a aVar = this.gatewayChannelImpl;
        if (aVar != null) {
            return aVar.getPersistentConnectConfig();
        }
        return null;
    }
}
