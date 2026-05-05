package sdk;

import android.content.Context;
import android.util.Log;
import com.aliyun.alink.linksdk.channel.core.base.AError;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileRequestListener;
import com.aliyun.alink.linksdk.channel.mobile.api.IMobileSubscrbieListener;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileChannel;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectConfig;
import com.aliyun.alink.linksdk.channel.mobile.api.MobileConnectState;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import tools.LogEx;

/* JADX INFO: loaded from: classes4.dex */
public class ChannelManager {
    private String TAG;
    private String allTopicWildcard;
    private String appKey;
    private IMobileConnectListener connectListener;
    private Context context;
    private IMobileDownstreamListener downstreamListener;
    private List<IMobileMsgListener> listenerList;
    private MobileConnectState mMobileConnectState;
    private IMobileSubscrbieListener subscrbieListener;

    public interface IMobileMsgListener {
        void onCommand(String str, String str2);
    }

    private ChannelManager() {
        this.TAG = "ChannelManager";
        this.allTopicWildcard = MqttTopic.MULTI_LEVEL_WILDCARD;
        this.downstreamListener = new IMobileDownstreamListener() { // from class: sdk.ChannelManager.1
            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener
            public boolean shouldHandle(String str) {
                return true;
            }

            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileDownstreamListener
            public void onCommand(String str, String str2) {
                ChannelManager.this.processMsg(str, str2);
            }
        };
        this.connectListener = new IMobileConnectListener() { // from class: sdk.ChannelManager.2
            @Override // com.aliyun.alink.linksdk.channel.mobile.api.IMobileConnectListener
            public void onConnectStateChange(MobileConnectState mobileConnectState) {
                ChannelManager.this.mMobileConnectState = mobileConnectState;
                if (MobileConnectState.CONNECTED == mobileConnectState) {
                    LogEx.e(true, ChannelManager.this.TAG, "通道已连接");
                    ChannelManager.this.notify("dev/state", "CONNECTED");
                    ChannelManager channelManager = ChannelManager.this;
                    channelManager.subscrbie(channelManager.allTopicWildcard, ChannelManager.this.subscrbieListener);
                    return;
                }
                if (MobileConnectState.DISCONNECTED == mobileConnectState) {
                    LogEx.e(true, ChannelManager.this.TAG, "通道已断开");
                    ChannelManager.this.notify("dev/state", "DISCONNECTED");
                    ChannelManager.this.connect();
                } else if (MobileConnectState.CONNECTING == mobileConnectState) {
                    LogEx.e(true, ChannelManager.this.TAG, "通道连接中");
                    ChannelManager.this.notify("dev/state", "CONNECTING");
                } else if (MobileConnectState.CONNECTFAIL == mobileConnectState) {
                    LogEx.e(true, ChannelManager.this.TAG, "通道连接失败");
                    ChannelManager.this.notify("dev/state", "CONNECT_FAIL");
                    ChannelManager.this.connect();
                }
            }
        };
        this.subscrbieListener = new IMobileSubscrbieListener() { // from class: sdk.ChannelManager.3
            @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
            public boolean needUISafety() {
                return false;
            }

            @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
            public void onSuccess(String str) {
                LogEx.e(true, ChannelManager.this.TAG, "订阅成功");
            }

            @Override // com.aliyun.alink.linksdk.channel.core.persistent.IOnSubscribeListener
            public void onFailed(String str, AError aError) {
                LogEx.e(true, ChannelManager.this.TAG, "订阅失败");
                if (MobileConnectState.CONNECTED != ChannelManager.this.mMobileConnectState) {
                    ChannelManager.this.connect();
                } else {
                    ChannelManager channelManager = ChannelManager.this;
                    channelManager.subscrbie(channelManager.allTopicWildcard, ChannelManager.this.subscrbieListener);
                }
            }
        };
    }

    private static class ChannelManagerHolder {
        private static final ChannelManager channelManager = new ChannelManager();

        private ChannelManagerHolder() {
        }
    }

    public static ChannelManager getInstance() {
        return ChannelManagerHolder.channelManager;
    }

    public void init(Context context, String str) {
        this.context = context;
        this.appKey = str;
        connect();
    }

    public void publish(String str, String str2, IMobileRequestListener iMobileRequestListener) {
        MobileChannel.getInstance().ayncSendPublishRequest(str, str2, iMobileRequestListener);
    }

    public void subscrbie(String str, IMobileSubscrbieListener iMobileSubscrbieListener) {
        MobileChannel.getInstance().subscrbie(str, iMobileSubscrbieListener);
    }

    public void unSubscrbie(String str, IMobileSubscrbieListener iMobileSubscrbieListener) {
        MobileChannel.getInstance().unSubscrbie(str, iMobileSubscrbieListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void connect() {
        MobileConnectConfig mobileConnectConfig = new MobileConnectConfig();
        mobileConnectConfig.appkey = this.appKey;
        MobileChannel.getInstance().startConnect(this.context, mobileConnectConfig, this.connectListener);
        MobileChannel.getInstance().registerDownstreamListener(true, this.downstreamListener);
    }

    public void disconnect() {
        MobileChannel.getInstance().unRegisterConnectListener(this.connectListener);
        MobileChannel.getInstance().unRegisterDownstreamListener(this.downstreamListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMsg(String str, String str2) {
        Log.e("fuck", "sssssssssssssssssssssssssssssssssss " + str);
        LogEx.d(true, this.TAG, "收到消息    topic" + str + "      msg:" + str2);
        notify(str, str2);
    }

    public void registerListener(IMobileMsgListener iMobileMsgListener) {
        if (iMobileMsgListener == null) {
            return;
        }
        if (this.listenerList == null) {
            this.listenerList = new LinkedList();
        }
        if (this.listenerList.contains(iMobileMsgListener)) {
            return;
        }
        this.listenerList.add(iMobileMsgListener);
    }

    public void unRegisterListener(IMobileMsgListener iMobileMsgListener) {
        if (iMobileMsgListener == null) {
            return;
        }
        List<IMobileMsgListener> list = this.listenerList;
        if (list == null) {
            this.listenerList = new LinkedList();
        } else if (list.contains(iMobileMsgListener)) {
            this.listenerList.remove(iMobileMsgListener);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void notify(String str, String str2) {
        List<IMobileMsgListener> list;
        if (str == null || str2 == null || (list = this.listenerList) == null || list.size() <= 0) {
            return;
        }
        Iterator<IMobileMsgListener> it = this.listenerList.iterator();
        while (it.hasNext()) {
            it.next().onCommand(str, str2);
        }
    }
}
