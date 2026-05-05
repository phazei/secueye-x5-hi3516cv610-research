package com.taobao.accs.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Messenger;
import com.taobao.accs.messenger.MessengerInnerHandler;
import com.taobao.accs.utl.ALog;
import com.vivo.push.PushClientConstants;
import java.io.Serializable;
import java.util.Map;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes3.dex */
public abstract class TaoBaseService extends Service implements AccsDataListenerV2 {
    private static final String TAG = "TaoBaseService";
    private AccsAbstractDataListener mDefaultDataListener = new AccsAbstractDataListener() { // from class: com.taobao.accs.base.TaoBaseService.1
        @Override // com.taobao.accs.base.AccsAbstractDataListener, com.taobao.accs.base.AccsDataListener
        public void onBind(String str, int i, ExtraInfo extraInfo) {
        }

        @Override // com.taobao.accs.base.AccsDataListener
        public void onData(String str, String str2, String str3, byte[] bArr, ExtraInfo extraInfo) {
        }

        @Override // com.taobao.accs.base.AccsAbstractDataListener, com.taobao.accs.base.AccsDataListener
        public void onResponse(String str, String str2, int i, byte[] bArr, ExtraInfo extraInfo) {
        }

        @Override // com.taobao.accs.base.AccsAbstractDataListener, com.taobao.accs.base.AccsDataListener
        public void onSendData(String str, String str2, int i, ExtraInfo extraInfo) {
        }

        @Override // com.taobao.accs.base.AccsAbstractDataListener, com.taobao.accs.base.AccsDataListener
        public void onUnbind(String str, int i, ExtraInfo extraInfo) {
        }
    };
    MessengerInnerHandler innerHandler = new MessengerInnerHandler(TAG, this);
    private final Messenger messenger = new Messenger(this.innerHandler);

    /* JADX INFO: compiled from: Taobao */
    public static class ExtraInfo implements Serializable {
        public static final String EXT_HEADER = "ext_header";
        public int connType;
        public Map<ExtHeaderType, String> extHeader;
        public String fromHost;
        public String fromPackage;
        public Map<Integer, String> oriExtHeader;
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onBind(String str, int i, ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onResponse(String str, String str2, int i, byte[] bArr, ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onSendData(String str, String str2, int i, ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onUnbind(String str, int i, ExtraInfo extraInfo) {
    }

    /* JADX INFO: compiled from: Taobao */
    public enum ExtHeaderType {
        TYPE_BUSINESS,
        TYPE_SID,
        TYPE_USERID,
        TYPE_COOKIE,
        TYPE_TAG,
        TYPE_STATUS,
        TYPE_DELAY,
        TYPE_EXPIRE,
        TYPE_LOCATION,
        TYPE_UNIT,
        TYPE_NEED_BUSINESS_ACK;

        public static ExtHeaderType valueOf(int i) {
            switch (i) {
                case 0:
                    return TYPE_BUSINESS;
                case 1:
                    return TYPE_SID;
                case 2:
                    return TYPE_USERID;
                case 3:
                    return TYPE_COOKIE;
                case 4:
                    return TYPE_TAG;
                case 5:
                    return TYPE_STATUS;
                case 6:
                    return TYPE_DELAY;
                case 7:
                    return TYPE_EXPIRE;
                case 8:
                    return TYPE_LOCATION;
                case 9:
                    return TYPE_UNIT;
                case 10:
                    return TYPE_NEED_BUSINESS_ACK;
                default:
                    return null;
            }
        }
    }

    /* JADX INFO: compiled from: Taobao */
    public static class ConnectInfo implements Serializable {
        private static final long serialVersionUID = 8974674111758240362L;
        public boolean connected;
        public int errorCode;
        public String errordetail;
        public String host;
        public boolean isCenterHost;
        public boolean isInapp;

        public ConnectInfo(String str, boolean z, boolean z2) {
            this.host = str;
            this.isInapp = z;
            this.isCenterHost = z2;
        }

        public ConnectInfo(String str, boolean z, boolean z2, int i, String str2) {
            this.host = str;
            this.isInapp = z;
            this.isCenterHost = z2;
            this.errorCode = i;
            this.errordetail = str2;
        }

        public String toString() {
            return "ConnectInfo{host='" + this.host + "', isInapp=" + this.isInapp + ", isCenterHost=" + this.isCenterHost + ", connected=" + this.connected + ", errorCode=" + this.errorCode + ", errorDetail='" + this.errordetail + "'}";
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.messenger.getBinder();
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onAntiBrush(boolean z, ExtraInfo extraInfo) {
        this.mDefaultDataListener.onAntiBrush(z, extraInfo);
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onDisconnected(ConnectInfo connectInfo) {
        this.mDefaultDataListener.onDisconnected(connectInfo);
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onConnected(ConnectInfo connectInfo) {
        this.mDefaultDataListener.onConnected(connectInfo);
    }

    @Override // android.app.Service
    public void onCreate() {
        super.onCreate();
    }

    @Override // android.app.Service
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        if (ALog.isPrintLog(ALog.Level.D)) {
            ALog.d(TAG, "onStartCommand", PushClientConstants.TAG_CLASS_NAME, getClass().getSimpleName());
        }
        return AccsAbstractDataListener.onReceiveData(this, intent, this);
    }

    @Override // com.taobao.accs.base.AccsDataListenerV2
    public void onBind(String str, int i, String str2, ExtraInfo extraInfo) {
        onBind(str, i, extraInfo);
    }

    @Override // com.taobao.accs.base.AccsDataListenerV2
    public void onUnbind(String str, int i, String str2, ExtraInfo extraInfo) {
        onUnbind(str, i, extraInfo);
    }

    @Override // com.taobao.accs.base.AccsDataListenerV2
    public void onSendData(String str, String str2, int i, String str3, ExtraInfo extraInfo) {
        onSendData(str, str2, i, extraInfo);
    }

    @Override // com.taobao.accs.base.AccsDataListenerV2
    public void onResponse(String str, String str2, int i, String str3, byte[] bArr, ExtraInfo extraInfo) {
        onResponse(str, str2, i, bArr, extraInfo);
    }
}
