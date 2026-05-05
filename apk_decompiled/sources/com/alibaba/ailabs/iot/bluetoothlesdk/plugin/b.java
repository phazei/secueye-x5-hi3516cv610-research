package com.alibaba.ailabs.iot.bluetoothlesdk.plugin;

import aisble.callback.DataSentCallback;
import aisble.callback.FailCallback;
import aisble.data.Data;
import android.bluetooth.BluetoothDevice;
import androidx.annotation.NonNull;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.util.Arrays;

/* JADX INFO: compiled from: GattBLEInfrastructurePluginV2.java */
/* JADX INFO: loaded from: classes.dex */
public class b extends a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static final String f2783a = "b";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private byte f2784b = 1;

    @Override // com.alibaba.ailabs.iot.bluetoothlesdk.plugin.a
    protected void a(final IActionListener iActionListener, byte b2, byte[] bArr, byte b3) {
        byte[] bArrA = a(bArr, true);
        LogUtils.d(f2783a, "Bus Payload:" + ConvertUtils.bytes2HexString(bArrA));
        AISCommand aISCommandSendCommandWithCallback = sendCommandWithCallback(b2, bArrA, new DataSentCallback() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.plugin.b.1
            @Override // aisble.callback.DataSentCallback
            public void onDataSent(@NonNull BluetoothDevice bluetoothDevice, @NonNull Data data) {
            }
        }, new FailCallback() { // from class: com.alibaba.ailabs.iot.bluetoothlesdk.plugin.b.2
            @Override // aisble.callback.FailCallback
            public void onRequestFailed(@NonNull BluetoothDevice bluetoothDevice, int i) {
                iActionListener.onFailure(i, "");
            }
        });
        if (aISCommandSendCommandWithCallback == null || b3 == 0) {
            return;
        }
        a(AISCommand.getMessageSpec(b3, aISCommandSendCommandWithCallback.getHeader().getMsgID()), iActionListener);
    }

    @Override // com.alibaba.ailabs.iot.bluetoothlesdk.plugin.a
    protected byte[] a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        return Arrays.copyOfRange(bArr, 1, bArr.length);
    }

    private byte[] a(byte[] bArr, boolean z) {
        byte[] bArr2 = new byte[bArr == null ? 1 : bArr.length + 1];
        bArr2[0] = 0;
        if (bArr != null) {
            System.arraycopy(bArr, 0, bArr2, 1, bArr.length);
        }
        bArr2[0] = this.f2784b;
        if (!z) {
            bArr2[0] = (byte) (bArr2[0] | Constants.CMD_TYPE.CMD_DEV_LOG_NOTIFY);
        }
        return bArr2;
    }
}
