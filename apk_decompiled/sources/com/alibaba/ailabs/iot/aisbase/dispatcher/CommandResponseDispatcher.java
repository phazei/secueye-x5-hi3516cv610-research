package com.alibaba.ailabs.iot.aisbase.dispatcher;

import aisble.callback.profile.ProfileDataCallback;
import aisble.data.Data;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.util.Log;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.AESUtil;
import com.alibaba.ailabs.iot.aisbase.contant.GmaLogConst;
import com.alibaba.ailabs.iot.aisbase.exception.IllegalCommandException;
import com.alibaba.ailabs.iot.aisbase.exception.IncompletePayloadException;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class CommandResponseDispatcher implements ProfileDataCallback {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2568a = "CommandResponseDispatcher";
    public byte e;
    public OnCommandReceivedListener g;
    public byte[] h;
    public byte[] j;
    public AESUtil l;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public SparseArray<byte[]> f2569b = new SparseArray<>();

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Set<Byte> f2570c = new HashSet();

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public boolean f2571d = false;
    public SparseArray<OnCommandReceivedListener> f = new SparseArray<>();
    public int i = -1;
    public boolean k = false;

    public interface OnCommandReceivedListener {
        void onCommandReceived(byte b2, byte b3, byte[] bArr);
    }

    @SuppressLint({"DefaultLocale", "LongLogTag"})
    public final void a(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data, boolean z) {
        byte[] value;
        if (this.h != null) {
            int length = data.getValue().length;
            byte[] bArr = this.h;
            value = new byte[length + bArr.length];
            System.arraycopy(bArr, 0, value, 0, bArr.length);
            if (data.getValue() != null) {
                System.arraycopy(data.getValue(), 0, value, this.h.length, data.getValue().length);
            }
            this.h = null;
        } else {
            value = data.getValue();
        }
        try {
            try {
                AISCommand fromByte = AISCommand.parseFromByte(value);
                if (this.i == -1) {
                    this.i = fromByte.getHeader().getMsgID();
                } else {
                    byte msgID = fromByte.getHeader().getMsgID();
                    if (this.i != 15 || msgID != 0) {
                        if (this.i == 15 && msgID != 0) {
                            LogUtils.w(f2568a, String.format("Packet loss: %d: %d", Integer.valueOf(this.i), Byte.valueOf(msgID)));
                        } else if (this.i + 1 != msgID) {
                            LogUtils.w(f2568a, String.format("Packet loss: %d: %d", Integer.valueOf(this.i), Byte.valueOf(msgID)));
                        }
                    }
                    this.i = msgID;
                }
                byte totalFrame = fromByte.getHeader().getTotalFrame();
                byte frameSeq = fromByte.getHeader().getFrameSeq();
                byte commandType = fromByte.getHeader().getCommandType();
                this.f2571d = true;
                this.e = commandType;
                if (totalFrame == 0 && frameSeq == 0) {
                    this.f2569b.put(commandType, fromByte.getPayload());
                } else {
                    byte[] payload = fromByte.getPayload();
                    if (payload != null) {
                        byte[] bArr2 = this.f2569b.get(commandType);
                        if (bArr2 == null) {
                            this.f2569b.put(commandType, payload);
                        } else {
                            byte[] bArr3 = new byte[payload.length + bArr2.length];
                            System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
                            System.arraycopy(payload, 0, bArr3, bArr2.length, payload.length);
                            this.f2569b.put(commandType, bArr3);
                        }
                    }
                }
                boolean zContains = this.f2570c.contains(Byte.valueOf(commandType));
                if (totalFrame == frameSeq || zContains) {
                    this.f2571d = false;
                    byte[] bArrDecryptPayload = this.f2569b.get(commandType);
                    if (fromByte.getHeader().getEncryption() == 1) {
                        bArrDecryptPayload = decryptPayload(bArrDecryptPayload);
                    }
                    if (this.g != null) {
                        this.g.onCommandReceived(commandType, fromByte.getHeader().getMsgID(), bArrDecryptPayload);
                    }
                    LogUtils.w(f2568a, String.format("cmdType: %d", Integer.valueOf(commandType)));
                    for (int i = 0; i < this.f.size(); i++) {
                        String str = f2568a;
                        StringBuilder sb = new StringBuilder();
                        sb.append("subscribe=");
                        sb.append(this.f.valueAt(i));
                        LogUtils.w(str, String.format(sb.toString(), new Object[0]));
                    }
                    OnCommandReceivedListener onCommandReceivedListener = this.f.get(commandType);
                    if (onCommandReceivedListener != null) {
                        onCommandReceivedListener.onCommandReceived(commandType, fromByte.getHeader().getMsgID(), bArrDecryptPayload);
                    }
                    this.f2569b.remove(commandType);
                }
                if (fromByte.getHeader().getPayloadLength() < value.length - 4) {
                    a(bluetoothDevice, new Data(Arrays.copyOfRange(value, fromByte.getHeader().getPayloadLength() + 4, value.length)), true);
                }
            } catch (IncompletePayloadException e) {
                Log.e(GmaLogConst.GMA_CONNECT_AUTH, e.toString());
                this.h = value;
            }
        } catch (IllegalCommandException e2) {
            LogUtils.e(f2568a, e2.toString());
            this.f2571d = false;
        }
    }

    public byte[] decryptPayload(byte[] bArr) {
        if (!this.k || bArr == null) {
            return bArr;
        }
        if (this.l == null) {
            this.l = AESUtil.getInstance();
            this.l.setKey(this.j);
        }
        return this.l.decrypt(bArr);
    }

    public void enableAESEncryption(byte[] bArr) {
        LogUtils.d(f2568a, "enableAESEncryption: " + ConvertUtils.bytes2HexString(bArr));
        this.k = bArr != null;
        this.j = bArr;
        if (bArr != null) {
            this.l = AESUtil.getInstance();
            this.l.setKey(bArr);
        }
    }

    @Override // aisble.callback.DataReceivedCallback
    public void onDataReceived(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
        a(bluetoothDevice, data, false);
    }

    @Override // aisble.callback.profile.ProfileDataCallback
    public void onInvalidDataReceived(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
    }

    public void reset() {
        LogUtils.d(f2568a, "Reset...");
        this.h = null;
        this.i = -1;
    }

    public void setCommandReassembleByFrameSeq(byte b2, boolean z) {
        if (z) {
            this.f2570c.add(Byte.valueOf(b2));
        } else {
            this.f2570c.remove(Byte.valueOf(b2));
        }
    }

    public void subscribeMultiCommandReceivedListener(byte[] bArr, OnCommandReceivedListener onCommandReceivedListener) {
        for (byte b2 : bArr) {
            this.f.put(b2, onCommandReceivedListener);
        }
    }

    public void unsubscribeMultiCommandReceivedListener(byte[] bArr) {
        for (byte b2 : bArr) {
            this.f.remove(b2);
        }
    }
}
