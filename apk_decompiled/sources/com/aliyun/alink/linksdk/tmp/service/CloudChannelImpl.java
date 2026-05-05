package com.aliyun.alink.linksdk.tmp.service;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IDataDownListener;
import com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel;
import com.aliyun.alink.linksdk.alcs.lpbs.data.PalDeviceInfo;
import com.aliyun.alink.linksdk.channel.gateway.api.GatewayChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceChannel;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener;
import com.aliyun.alink.linksdk.channel.gateway.api.subdevice.SubDeviceInfo;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.tmp.service.DevService;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.aliyun.alink.linksdk.tools.AError;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class CloudChannelImpl implements IThingCloudChannel {
    private static final String TAG = "[Tmp]CloudChannelImpl";
    private IDataDownListener mDataDownListener;
    private PalDeviceInfo mDeviceInfo;
    private ISubDeviceChannel mSubDeviceChannel;

    public interface CloudChannelInitListener {
        void onComplete(boolean z);
    }

    public CloudChannelImpl(PalDeviceInfo palDeviceInfo) {
        this.mDeviceInfo = palDeviceInfo;
    }

    public void initCloudChannel(Map<String, Object> map, final CloudChannelInitListener cloudChannelInitListener) {
        final String str = map != null ? (String) map.get(TmpConstant.KEY_SIGN_METHOD) : null;
        final String str2 = map != null ? (String) map.get("sign") : null;
        final String str3 = map != null ? (String) map.get(TmpConstant.KEY_CLIENT_ID) : null;
        String str4 = this.mDeviceInfo.productModel;
        String str5 = this.mDeviceInfo.deviceId;
        ALog.d(TAG, "initCloudChannel signMethod:" + str + " signValue:" + str2 + " clientId:" + str3 + " subProductKey:" + str4 + " subDeviceName:" + str5 + " listener:" + cloudChannelInitListener);
        if (cloudChannelInitListener == null) {
            ALog.e(TAG, "initCloudChannel listener empty");
            return;
        }
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            if (TextUtils.isEmpty(str4) || TextUtils.isEmpty(str5)) {
                cloudChannelInitListener.onComplete(false);
                return;
            } else {
                DevService.subDeviceAuthenLogin(str4, str5, false, new DevService.ServiceListener() { // from class: com.aliyun.alink.linksdk.tmp.service.CloudChannelImpl.1
                    @Override // com.aliyun.alink.linksdk.tmp.service.DevService.ServiceListener
                    public void onComplete(boolean z, Object obj) {
                        ALog.d(CloudChannelImpl.TAG, "上线请求 isSuccess:" + z);
                        if (z && obj != null) {
                            try {
                                if (obj instanceof Map) {
                                    CloudChannelImpl.this.mSubDeviceChannel = (ISubDeviceChannel) ((Map) obj).get(DevService.BUNDLE_KEY_SUBCHANNEL);
                                }
                            } catch (Exception e) {
                                ALog.e(CloudChannelImpl.TAG, "subDeviceAuthenLogin Exception:" + e.toString());
                            } catch (Throwable th) {
                                ALog.e(CloudChannelImpl.TAG, "subDeviceAuthenLogin Throwable:" + th.toString());
                            }
                        }
                        cloudChannelInitListener.onComplete(z);
                    }
                });
                return;
            }
        }
        GatewayChannel.getInstance().addSubDeviceForILopGlobal(new SubDeviceInfo(str4, str5), new ISubDeviceConnectListener() { // from class: com.aliyun.alink.linksdk.tmp.service.CloudChannelImpl.2
            @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
            public String getSignMethod() {
                return str;
            }

            @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
            public String getSignValue() {
                return str2;
            }

            @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
            public Map<String, Object> getSignExtraData() {
                return new HashMap();
            }

            @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
            public String getClientId() {
                return str3;
            }

            @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
            public void onConnectResult(boolean z, ISubDeviceChannel iSubDeviceChannel, AError aError) {
                StringBuilder sb = new StringBuilder();
                sb.append("添加拓扑关系，onConnectResult， isSucc = ");
                sb.append(z);
                sb.append(" channel:");
                sb.append(iSubDeviceChannel);
                sb.append(", error =");
                sb.append(aError != null ? aError.getMsg() : "");
                ALog.d(CloudChannelImpl.TAG, sb.toString());
                if (z && iSubDeviceChannel != null) {
                    CloudChannelImpl.this.mSubDeviceChannel = iSubDeviceChannel;
                    CloudChannelImpl.this.mSubDeviceChannel.online(new ISubDeviceActionListener() { // from class: com.aliyun.alink.linksdk.tmp.service.CloudChannelImpl.2.1
                        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                        public void onSuccess() {
                            ALog.d(CloudChannelImpl.TAG, "上线请求成功");
                            cloudChannelInitListener.onComplete(true);
                        }

                        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                        public void onFailed(AError aError2) {
                            ALog.d(CloudChannelImpl.TAG, "上线请求失败， error = " + aError2.getMsg());
                            cloudChannelInitListener.onComplete(false);
                        }
                    });
                    cloudChannelInitListener.onComplete(false);
                    return;
                }
                cloudChannelInitListener.onComplete(false);
            }

            @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceConnectListener
            public void onDataPush(String str6, AMessage aMessage) {
                ALog.d(CloudChannelImpl.TAG, "子设备下行： topic = " + str6 + " ,data = " + aMessage);
                if (CloudChannelImpl.this.mDataDownListener == null || aMessage == null || !(aMessage.getData() instanceof byte[])) {
                    return;
                }
                CloudChannelImpl.this.mDataDownListener.onDataDown(str6, (byte[]) aMessage.getData());
            }
        });
    }

    public void deviceLogout() {
        ISubDeviceChannel iSubDeviceChannel = this.mSubDeviceChannel;
        if (iSubDeviceChannel != null) {
            iSubDeviceChannel.offline(new ISubDeviceActionListener() { // from class: com.aliyun.alink.linksdk.tmp.service.CloudChannelImpl.3
                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                public void onSuccess() {
                    ALog.d(CloudChannelImpl.TAG, "mSubDeviceChannel offline onSuccess mDeviceInfo:" + CloudChannelImpl.this.mDeviceInfo);
                }

                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                public void onFailed(AError aError) {
                    ALog.d(CloudChannelImpl.TAG, "mSubDeviceChannel offline onFailed mDeviceInfo:" + CloudChannelImpl.this.mDeviceInfo);
                }
            });
        }
    }

    public void uninitChannel() {
        ISubDeviceChannel iSubDeviceChannel = this.mSubDeviceChannel;
        if (iSubDeviceChannel != null) {
            iSubDeviceChannel.offline(new ISubDeviceActionListener() { // from class: com.aliyun.alink.linksdk.tmp.service.CloudChannelImpl.4
                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                public void onSuccess() {
                    ALog.d(CloudChannelImpl.TAG, "mSubDeviceChannel offline onSuccess mDeviceInfo:" + CloudChannelImpl.this.mDeviceInfo);
                }

                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                public void onFailed(AError aError) {
                    ALog.d(CloudChannelImpl.TAG, "mSubDeviceChannel offline onFailed mDeviceInfo:" + CloudChannelImpl.this.mDeviceInfo);
                }
            });
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel
    public void reportData(final String str, byte[] bArr) {
        try {
            try {
                if (this.mSubDeviceChannel != null) {
                    String str2 = null;
                    try {
                        str2 = new String(bArr, "UTF-8");
                    } catch (Exception e) {
                        ALog.e(TAG, e.toString());
                    }
                    ALog.d(TAG, "reportData topic:" + str + " data:" + str2);
                    this.mSubDeviceChannel.uploadData(str, str2, new ISubDeviceActionListener() { // from class: com.aliyun.alink.linksdk.tmp.service.CloudChannelImpl.5
                        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                        public void onSuccess() {
                            ALog.d(CloudChannelImpl.TAG, "onSuccess topic:" + str);
                        }

                        @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                        public void onFailed(AError aError) {
                            ALog.d(CloudChannelImpl.TAG, "onFailed topic:" + str + " aError:" + aError);
                        }
                    });
                    return;
                }
                ALog.e(TAG, "reportData topic:" + str + " mSubDeviceChannel empty");
            } catch (Throwable th) {
                ALog.e(TAG, "reportData Throwable:" + th.toString());
            }
        } catch (Exception e2) {
            ALog.e(TAG, "reportData Exception:" + e2.toString());
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel
    public void reportData(final String str, Object obj, final IThingCloudChannel.IChannelActionListener iChannelActionListener) {
        if (this.mSubDeviceChannel != null) {
            String strValueOf = null;
            try {
                strValueOf = String.valueOf(obj);
            } catch (Exception e) {
                ALog.e(TAG, e.toString());
            }
            ALog.d(TAG, "reportData topic:" + str + " data:" + strValueOf);
            this.mSubDeviceChannel.uploadData(str, strValueOf, new ISubDeviceActionListener() { // from class: com.aliyun.alink.linksdk.tmp.service.CloudChannelImpl.6
                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                public void onSuccess() {
                    ALog.d(CloudChannelImpl.TAG, "onSuccess topic:" + str);
                    IThingCloudChannel.IChannelActionListener iChannelActionListener2 = iChannelActionListener;
                    if (iChannelActionListener2 != null) {
                        iChannelActionListener2.onSuccess();
                    }
                }

                @Override // com.aliyun.alink.linksdk.channel.gateway.api.subdevice.ISubDeviceActionListener
                public void onFailed(AError aError) {
                    ALog.d(CloudChannelImpl.TAG, "onFailed topic:" + str + " aError:" + aError);
                    if (iChannelActionListener != null) {
                        AError aError2 = new AError();
                        if (aError != null) {
                            aError2.setCode(aError.getCode());
                            aError2.setMsg(aError.getMsg());
                            aError2.setSubCode(aError.getSubCode());
                            aError2.setSubMsg(aError.getSubMsg());
                        }
                        iChannelActionListener.onFailed(aError2);
                    }
                }
            });
            return;
        }
        ALog.e(TAG, "reportData topic:" + str + " mSubDeviceChannel empty");
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel
    public void addDownDataListener(IDataDownListener iDataDownListener) {
        this.mDataDownListener = iDataDownListener;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.cloud.IThingCloudChannel
    public void removeDownDataListener(IDataDownListener iDataDownListener) {
        this.mDataDownListener = null;
    }
}
