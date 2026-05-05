package com.alibaba.ailabs.iot.aisbase.plugin;

import com.alibaba.ailabs.iot.aisbase.AESUtil;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher;
import com.alibaba.ailabs.iot.aisbase.exception.UnsupportedLayerException;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class PluginBase implements IPlugin {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2619a = "PluginBase";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public AESUtil f2620b;
    public byte[] mAesKey;
    public ITransmissionLayer mTransmissionLayer;
    public boolean mEnableAesEncryption = false;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public BluetoothDeviceWrapper f2621c = null;

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void enableAESEncryption(byte[] bArr) {
        this.mEnableAesEncryption = bArr != null;
        if (this.mEnableAesEncryption) {
            this.f2620b = AESUtil.getInstance();
            this.f2620b.setKey(bArr);
        }
        this.mAesKey = bArr;
        if (this.mTransmissionLayer == null) {
            return;
        }
        for (String str : getChannelUUIDs()) {
            CommandResponseDispatcher commandResponseDispatcher = this.mTransmissionLayer.getCommandResponseDispatcher(str);
            if (commandResponseDispatcher != null) {
                commandResponseDispatcher.enableAESEncryption(bArr);
            }
        }
    }

    public byte[] encryptPayload(byte[] bArr) {
        return (!this.mEnableAesEncryption || bArr == null) ? bArr : this.f2620b.encrypt(AESUtil.PKCS7PADDING_CIPHER_ALGORITHM, bArr);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public BluetoothDeviceWrapper getBluetoothDeviceWrapper() {
        return this.f2621c;
    }

    public abstract String[] getChannelUUIDs();

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void init(ITransmissionLayer iTransmissionLayer) throws UnsupportedLayerException {
        this.mTransmissionLayer = iTransmissionLayer;
    }

    @Override // com.alibaba.ailabs.iot.aisbase.plugin.IPlugin
    public void setBluetoothDeviceWrapper(BluetoothDeviceWrapper bluetoothDeviceWrapper) {
        this.f2621c = bluetoothDeviceWrapper;
    }

    public List<AISCommand> splitDataToCommands(int i, int i2, byte b2, byte[] bArr, boolean z) {
        int i3;
        int i4;
        byte[] bArr2;
        byte[] bArr3;
        byte[] bArrEncryptPayload = z ? encryptPayload(bArr) : bArr;
        ArrayList arrayList = new ArrayList();
        int i5 = 0;
        int i6 = i2;
        if (i6 > 16) {
            i6 = 0;
        }
        if (bArrEncryptPayload == null) {
            AISCommand aISCommand = new AISCommand(this.mTransmissionLayer.generateMessageID(), b2, (byte) 0, (byte) i6, (byte) 0, null);
            aISCommand.setEnableEncrypt(this.mEnableAesEncryption);
            arrayList.add(aISCommand);
            return arrayList;
        }
        int length = bArrEncryptPayload.length;
        int mtu = this.mTransmissionLayer.getMtu();
        LogUtils.d(f2619a, "transmission mtu: " + mtu);
        int i7 = mtu + (-7);
        int length2 = bArrEncryptPayload.length / i7;
        if (bArrEncryptPayload.length % i7 == 0) {
            length2--;
        }
        if (i6 != 0) {
            i3 = length2;
            i4 = 0;
            while (i6 <= i) {
                int iMin = Math.min(bArrEncryptPayload.length - i4, i7);
                if (iMin != 0) {
                    byte[] bArr4 = new byte[iMin];
                    System.arraycopy(bArrEncryptPayload, i4, bArr4, 0, iMin);
                    i4 += iMin;
                    bArr3 = bArr4;
                } else {
                    bArr3 = null;
                }
                AISCommand aISCommand2 = new AISCommand(this.mTransmissionLayer.generateMessageID(), b2, (byte) i, (byte) (i6 % (i + 1)), (byte) iMin, bArr3);
                aISCommand2.setEnableEncrypt(this.mEnableAesEncryption);
                arrayList.add(aISCommand2);
                i3--;
                i6++;
            }
        } else {
            i3 = length2;
            i4 = 0;
        }
        if (i4 < bArrEncryptPayload.length) {
            int i8 = 0;
            while (i8 <= i3) {
                int iMin2 = Math.min(bArrEncryptPayload.length - i4, i7);
                if (iMin2 != 0) {
                    byte[] bArr5 = new byte[iMin2];
                    System.arraycopy(bArrEncryptPayload, i4, bArr5, i5, iMin2);
                    i4 += iMin2;
                    bArr2 = bArr5;
                } else {
                    bArr2 = null;
                }
                AISCommand aISCommand3 = new AISCommand(this.mTransmissionLayer.generateMessageID(), b2, (byte) (((i8 / 16) + 1) * 16 > i3 ? i3 % 16 : 15), (byte) (i8 % 16), (byte) iMin2, bArr2);
                aISCommand3.setEnableEncrypt(this.mEnableAesEncryption);
                arrayList.add(aISCommand3);
                i8++;
                i5 = 0;
            }
        }
        return arrayList;
    }

    public List<AISCommand> splitFirmwareBinToFixedQuantityAISCommands(int i, int i2, byte b2, byte[] bArr, boolean z) {
        byte[] bArr2;
        byte[] bArrEncryptPayload = z ? encryptPayload(bArr) : bArr;
        ArrayList arrayList = new ArrayList();
        boolean z2 = false;
        int i3 = i2;
        if (i3 > i) {
            i3 = 0;
        }
        if (bArrEncryptPayload == null) {
            AISCommand aISCommand = new AISCommand(this.mTransmissionLayer.generateMessageID(), b2, (byte) 0, (byte) i3, (byte) 0, null);
            if (z && this.mEnableAesEncryption) {
                z2 = true;
            }
            aISCommand.setEnableEncrypt(z2);
            arrayList.add(aISCommand);
            return arrayList;
        }
        int length = bArrEncryptPayload.length;
        int mtu = this.mTransmissionLayer.getMtu();
        LogUtils.d(f2619a, "transmission mtu: " + mtu);
        int i4 = 0;
        while (i3 <= i) {
            int iMin = Math.min(bArrEncryptPayload.length - i4, mtu - 7);
            int i5 = i4 + iMin;
            if (i5 > length) {
                break;
            }
            if (iMin != 0) {
                byte[] bArr3 = new byte[iMin];
                System.arraycopy(bArrEncryptPayload, i4, bArr3, 0, iMin);
                bArr2 = bArr3;
                i4 = i5;
            } else {
                bArr2 = null;
            }
            AISCommand aISCommand2 = new AISCommand(this.mTransmissionLayer.generateMessageID(), b2, (byte) i, (byte) (i3 % (i + 1)), (byte) iMin, bArr2);
            aISCommand2.setEnableEncrypt(z && this.mEnableAesEncryption);
            arrayList.add(aISCommand2);
            i3++;
        }
        return arrayList;
    }

    public List<AISCommand> splitDataToCommands(byte b2, byte[] bArr, boolean z) {
        byte[] bArr2;
        byte[] bArrEncryptPayload = z ? encryptPayload(bArr) : bArr;
        ArrayList arrayList = new ArrayList();
        int i = 1;
        if (bArrEncryptPayload == null) {
            AISCommand aISCommand = new AISCommand(this.mTransmissionLayer.generateMessageID(), b2, (byte) 0, (byte) 0, (byte) 0, null);
            aISCommand.setEnableEncrypt(z && this.mEnableAesEncryption);
            arrayList.add(aISCommand);
            return arrayList;
        }
        int length = bArrEncryptPayload.length;
        int mtu = this.mTransmissionLayer.getMtu();
        LogUtils.d(f2619a, "transmission mtu: " + mtu);
        int i2 = mtu + (-7);
        int length2 = bArrEncryptPayload.length / i2;
        if (bArrEncryptPayload.length % i2 == 0) {
            length2--;
        }
        int i3 = 0;
        int i4 = 0;
        while (i3 <= length2) {
            int iMin = Math.min(bArrEncryptPayload.length - i4, i2);
            if (iMin != 0) {
                byte[] bArr3 = new byte[iMin];
                System.arraycopy(bArrEncryptPayload, i4, bArr3, 0, iMin);
                i4 += iMin;
                bArr2 = bArr3;
            } else {
                bArr2 = null;
            }
            AISCommand aISCommand2 = new AISCommand(this.mTransmissionLayer.generateMessageID(), b2, (byte) (((i3 / 16) + i) * 16 > length2 ? length2 % 16 : 15), (byte) (i3 % 16), (byte) iMin, bArr2);
            aISCommand2.setEnableEncrypt(z && this.mEnableAesEncryption);
            arrayList.add(aISCommand2);
            i3++;
            i = 1;
        }
        return arrayList;
    }
}
