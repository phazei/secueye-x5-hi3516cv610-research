package com.alibaba.ailabs.iot.aisbase.plugin.ota;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.SparseArray;
import com.alibaba.ailabs.iot.aisbase.Aa;
import com.alibaba.ailabs.iot.aisbase.Ba;
import com.alibaba.ailabs.iot.aisbase.C0418aa;
import com.alibaba.ailabs.iot.aisbase.C0420ba;
import com.alibaba.ailabs.iot.aisbase.C0422ca;
import com.alibaba.ailabs.iot.aisbase.C0430ga;
import com.alibaba.ailabs.iot.aisbase.C0432ha;
import com.alibaba.ailabs.iot.aisbase.C0434ia;
import com.alibaba.ailabs.iot.aisbase.C0436ja;
import com.alibaba.ailabs.iot.aisbase.C0438ka;
import com.alibaba.ailabs.iot.aisbase.C0440la;
import com.alibaba.ailabs.iot.aisbase.C0444na;
import com.alibaba.ailabs.iot.aisbase.C0446oa;
import com.alibaba.ailabs.iot.aisbase.C0450qa;
import com.alibaba.ailabs.iot.aisbase.C0452ra;
import com.alibaba.ailabs.iot.aisbase.C0456ta;
import com.alibaba.ailabs.iot.aisbase.C0460va;
import com.alibaba.ailabs.iot.aisbase.C0462wa;
import com.alibaba.ailabs.iot.aisbase.C0464xa;
import com.alibaba.ailabs.iot.aisbase.Ca;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.aisbase.RequestManage;
import com.alibaba.ailabs.iot.aisbase.RunnableC0426ea;
import com.alibaba.ailabs.iot.aisbase.RunnableC0442ma;
import com.alibaba.ailabs.iot.aisbase.RunnableC0448pa;
import com.alibaba.ailabs.iot.aisbase.RunnableC0458ua;
import com.alibaba.ailabs.iot.aisbase.Utils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ICastEventListener;
import com.alibaba.ailabs.iot.aisbase.callback.OtaActionListener;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.channel.LayerState;
import com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher;
import com.alibaba.ailabs.iot.aisbase.plugin.PluginBase;
import com.alibaba.ailabs.iot.aisbase.plugin.auth.IAuthPlugin;
import com.alibaba.ailabs.iot.aisbase.plugin.ota.IOTAPlugin;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceSubtype;
import com.alibaba.ailabs.iot.aisbase.spec.BluetoothDeviceWrapper;
import com.alibaba.ailabs.iot.aisbase.utils.DownloadManagerUtils;
import com.alibaba.ailabs.iot.aisbase.ya;
import com.alibaba.ailabs.iot.aisbase.za;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.google.firebase.appindexing.Indexable;
import datasource.implemention.data.DeviceVersionInfo;
import datasource.implemention.data.UpdateDeviceVersionRespData;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class OTAPluginProxy implements CommandResponseDispatcher.OnCommandReceivedListener, ICastEventListener {
    public IOTAPlugin.IFirmwareDownloadListener A;
    public Context M;
    public BroadcastReceiver Q;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public IOTAPlugin.IOTAActionListener f2635c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public Handler f2636d;
    public List<AISCommand> e;
    public byte[] h;
    public byte[] i;
    public byte[] j;
    public int k;
    public byte[] l;
    public byte m;
    public byte n;
    public ITransmissionLayer t;
    public PluginBase u;
    public BluetoothDevice v;
    public DownloadManagerUtils z;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final String f2633a = OTAPluginProxy.class.getSimpleName();

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public IOTAPlugin.OTAState f2634b = IOTAPlugin.OTAState.IDLE;
    public SparseArray<IActionListener> f = new SparseArray<>();
    public SparseArray<Runnable> g = new SparseArray<>();
    public int o = 0;
    public int p = 0;
    public int q = 0;
    public int r = 0;
    public List<Byte> s = new ArrayList();
    public String w = null;
    public String x = null;
    public long y = -1;
    public a B = null;
    public DeviceVersionInfo C = null;
    public int D = 1;
    public boolean E = false;
    public boolean F = false;
    public int G = 0;
    public final int H = 6;
    public int I = 0;
    public SparseArray<AISCommand> J = new SparseArray<>();
    public String K = null;
    public boolean L = false;
    public Runnable N = new RunnableC0442ma(this);
    public Runnable O = new RunnableC0458ua(this);
    public Runnable P = null;

    public OTAPluginProxy(CommandResponseDispatcher commandResponseDispatcher, ITransmissionLayer iTransmissionLayer, PluginBase pluginBase) {
        commandResponseDispatcher.subscribeMultiCommandReceivedListener(new byte[]{33, Constants.CMD_TYPE.CMD_GET_FIRMWARE_VERSION_RESEX, 35, 36, Constants.CMD_TYPE.CMD_OTA_VERIFY_RES, 15}, this);
        this.t = iTransmissionLayer;
        this.u = pluginBase;
        this.f2636d = new Handler(Looper.getMainLooper());
        this.t.registerCastEventListener(this);
        this.v = this.t.getBluetoothDevice();
    }

    public void checkNewVersion(String str, String str2, String str3, IActionListener<DeviceVersionInfo> iActionListener) {
        LogUtils.d(this.f2633a, String.format("check new version, productId: %s, macAddress: %s, appVersion: %s", str, str2, str3));
        if (!a()) {
            getFirmwareVersionCommand(new C0432ha(this));
        }
        if (TextUtils.isEmpty(this.w)) {
            a(str, str2, new C0434ia(this, str3, iActionListener));
        } else {
            b(this.w, str3, iActionListener);
        }
    }

    public void enableReportOtaProgress(boolean z) {
        this.L = z;
    }

    public void getFirmwareVersionCommand(IActionListener iActionListener) {
        AISCommand aISCommandSendCommandWithCallback = this.u.sendCommandWithCallback((byte) 32, new byte[]{0}, new C0460va(this), new C0462wa(this, iActionListener));
        if (aISCommandSendCommandWithCallback == null) {
            return;
        }
        b(AISCommand.getMessageSpec((byte) 33, aISCommandSendCommandWithCallback.getHeader().getMsgID()), iActionListener, aISCommandSendCommandWithCallback);
    }

    public void getFirmwareVersionCommandV1(IActionListener iActionListener) {
        AISCommand aISCommandSendCommandWithCallback = this.u.sendCommandWithCallback((byte) 32, new byte[]{0}, new C0464xa(this), new ya(this, iActionListener));
        if (aISCommandSendCommandWithCallback == null) {
            return;
        }
        b(AISCommand.getMessageSpec(Constants.CMD_TYPE.CMD_GET_FIRMWARE_VERSION_RESEX, aISCommandSendCommandWithCallback.getHeader().getMsgID()), iActionListener, aISCommandSendCommandWithCallback);
    }

    public void onBluetoothConnectionStateChanged(int i) {
        LogUtils.d(this.f2633a, "onBluetoothConnectionStateChanged: " + i);
        if (i != 0) {
            if (i == 2) {
                LogUtils.i(this.f2633a, "device connected, remove reconnect task");
                Runnable runnable = this.P;
                if (runnable != null) {
                    this.f2636d.removeCallbacks(runnable);
                }
                i();
                this.G = 0;
                this.v = this.t.getBluetoothDevice();
                BluetoothDeviceWrapper bluetoothDeviceWrapper = this.u.getBluetoothDeviceWrapper();
                if (bluetoothDeviceWrapper == null || !bluetoothDeviceWrapper.isIsSafetyMode()) {
                    e();
                    return;
                }
                return;
            }
            return;
        }
        IOTAPlugin.OTAState oTAState = this.f2634b;
        if (oTAState != IOTAPlugin.OTAState.IDLE && oTAState != IOTAPlugin.OTAState.FINISH && oTAState != IOTAPlugin.OTAState.VERIFY_SUCCESS && oTAState != IOTAPlugin.OTAState.WAIT_RECONNECT && oTAState != IOTAPlugin.OTAState.ERROR) {
            b(6, "link loss in OTA process");
        }
        IOTAPlugin.OTAState oTAState2 = this.f2634b;
        if (oTAState2 == IOTAPlugin.OTAState.VERIFY_SUCCESS || oTAState2 == IOTAPlugin.OTAState.WAIT_RECONNECT) {
            if (this.f2634b == IOTAPlugin.OTAState.VERIFY_SUCCESS) {
                a(IOTAPlugin.OTAState.WAIT_RECONNECT);
            } else {
                this.G++;
            }
            if (this.G >= 6) {
                if (this.f2634b != IOTAPlugin.OTAState.VERIFY_SUCCESS) {
                    b(6, "link loss in OTA process");
                    return;
                }
                IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2635c;
                if (iOTAActionListener != null) {
                    iOTAActionListener.onSuccess("");
                    return;
                }
                return;
            }
            if (this.v == null) {
                this.v = this.t.getBluetoothDevice();
            }
            if (this.v == null) {
                b(6, "Maybe the user manually turned off Bluetooth");
                return;
            }
            Handler handler = this.f2636d;
            RunnableC0426ea runnableC0426ea = new RunnableC0426ea(this);
            this.P = runnableC0426ea;
            handler.postDelayed(runnableC0426ea, this.G == 0 ? b() : r1 * 500);
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.callback.ICastEventListener
    public void onCast(String str) {
        LogUtils.d(this.f2633a, "Handle cast message: " + str);
        if (str.equals(IAuthPlugin.EVENT_AUTH_SUCCESS) || str.equals(IAuthPlugin.EVENT_AUTH_FAILED)) {
            e();
        }
    }

    @Override // com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher.OnCommandReceivedListener
    @SuppressLint({"DefaultLocale"})
    public void onCommandReceived(byte b2, byte b3, byte[] bArr) {
        LogUtils.d(this.f2633a, String.format("Received whole command, command type: %d, payload: %s", Byte.valueOf(b2), ConvertUtils.bytes2HexString(bArr)));
        if (b2 == 15) {
            b(7, "Device reply illegal command");
            return;
        }
        if (b2 == 33) {
            if (bArr == null || bArr.length < 5) {
                return;
            }
            this.f2636d.removeCallbacks(this.N);
            int iByteArray2IntByLittleEndian = Utils.byteArray2IntByLittleEndian(Arrays.copyOfRange(bArr, 1, bArr.length));
            int messageSpec = AISCommand.getMessageSpec(b2, b3);
            IActionListener iActionListener = this.f.get(messageSpec);
            if (iActionListener != null) {
                iActionListener.onSuccess(Integer.valueOf(iByteArray2IntByLittleEndian));
            }
            a(messageSpec);
            return;
        }
        if (b2 == 38) {
            if (bArr == null || bArr.length < 1) {
                return;
            }
            this.f2636d.removeCallbacks(this.N);
            if (bArr[0] != 1) {
                b(3, "Verify firmware failed");
                return;
            }
            a(IOTAPlugin.OTAState.VERIFY_SUCCESS);
            if (this.t.getConnectionState() == LayerState.CONNECTED) {
                this.t.disconnectDevice(null);
            }
            c();
            return;
        }
        if (b2 == 42) {
            if (bArr == null || bArr.length < 2) {
                return;
            }
            this.f2636d.removeCallbacks(this.N);
            int messageSpec2 = AISCommand.getMessageSpec(b2, b3);
            IActionListener iActionListener2 = this.f.get(messageSpec2);
            if (iActionListener2 == null) {
                messageSpec2 = AISCommand.getMessageSpec((byte) 33, b3);
                iActionListener2 = this.f.get(messageSpec2);
            }
            a(messageSpec2);
            try {
                String str = new String(Arrays.copyOfRange(bArr, 1, bArr.length), "UTF-8");
                if (iActionListener2 != null) {
                    iActionListener2.onSuccess(str);
                    return;
                }
                return;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }
        }
        if (b2 == 35) {
            if (bArr == null || bArr.length < 5) {
                return;
            }
            this.f2636d.removeCallbacks(this.N);
            if (bArr[0] != 1) {
                b(2, "Device does not allow upgrade");
                return;
            }
            int iByteArray2IntByLittleEndian2 = Utils.byteArray2IntByLittleEndian(Arrays.copyOfRange(bArr, 1, 5));
            if (bArr.length >= 6) {
                this.E = true;
                this.I = bArr[5];
            }
            LogUtils.d(this.f2633a, "Allow ota, fast ota model: " + this.E);
            byte[] bArr2 = this.h;
            if (bArr2 == null) {
                b(-201, "Null exception");
                return;
            }
            if (iByteArray2IntByLittleEndian2 == bArr2.length) {
                sendOTAFinishAndRequestVerifyCommand();
                return;
            }
            if (iByteArray2IntByLittleEndian2 > bArr2.length) {
                iByteArray2IntByLittleEndian2 = 0;
            }
            byte[] bArr3 = this.h;
            byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr3, iByteArray2IntByLittleEndian2, bArr3.length);
            if (!this.E && this.I == 0) {
                this.I = 15;
            }
            this.e = this.u.splitFirmwareBinToFixedQuantityAISCommands(this.I, 0, Constants.CMD_TYPE.CMD_OTA, bArrCopyOfRange, false);
            this.o = 0;
            a(IOTAPlugin.OTAState.OTA_PROGRESS);
            this.f2636d.postDelayed(this.O, 1200000L);
            f();
            return;
        }
        if (b2 != 36) {
            LogUtils.w(this.f2633a, "Unknown command type: " + ((int) b2));
            return;
        }
        if (this.F) {
            LogUtils.w(this.f2633a, "In send loop");
            return;
        }
        if (bArr == null || bArr.length < 5) {
            return;
        }
        this.q = (bArr[0] & 240) >> 4;
        this.r = bArr[0] & 15;
        int iByteArray2IntByLittleEndian3 = Utils.byteArray2IntByLittleEndian(Arrays.copyOfRange(bArr, 1, bArr.length));
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2635c;
        if (iOTAActionListener != null) {
            iOTAActionListener.onProgress(this.k, iByteArray2IntByLittleEndian3);
        }
        LogUtils.v(this.f2633a, String.format("Device received %d bytes, SDK send %d bytes", Integer.valueOf(iByteArray2IntByLittleEndian3), Integer.valueOf(this.p)));
        byte[] bArr4 = this.h;
        if (bArr4 == null) {
            b(-201, "Null exception");
            return;
        }
        if (this.f2634b != IOTAPlugin.OTAState.OTA_PROGRESS) {
            return;
        }
        if (iByteArray2IntByLittleEndian3 != this.p && iByteArray2IntByLittleEndian3 < bArr4.length) {
            LogUtils.w(this.f2633a, String.format("Packet loss in OTA progress, received package size: %d, next send package size: %d", Integer.valueOf(iByteArray2IntByLittleEndian3), Integer.valueOf(this.p)));
            byte[] bArr5 = this.h;
            this.e = this.u.splitFirmwareBinToFixedQuantityAISCommands(this.q, this.r + 1, Constants.CMD_TYPE.CMD_OTA, Arrays.copyOfRange(bArr5, iByteArray2IntByLittleEndian3, bArr5.length), false);
            this.o = 0;
            this.p = iByteArray2IntByLittleEndian3;
        }
        if (iByteArray2IntByLittleEndian3 == this.h.length) {
            sendOTAFinishAndRequestVerifyCommand();
            return;
        }
        if (this.o == this.e.size()) {
            LogUtils.d(this.f2633a, "Conduct a new round of data transmission");
            byte[] bArr6 = this.h;
            this.e = this.u.splitFirmwareBinToFixedQuantityAISCommands(this.q, 0, Constants.CMD_TYPE.CMD_OTA, Arrays.copyOfRange(bArr6, iByteArray2IntByLittleEndian3, bArr6.length), false);
            this.o = 0;
        }
        f();
    }

    public void sendOTAFinishAndRequestVerifyCommand() {
        this.f2636d.removeCallbacks(this.O);
        a(IOTAPlugin.OTAState.REQUEST_VERIFY);
        this.u.sendCommandWithCallback(Constants.CMD_TYPE.CMD_OTA_REQUEST_VERIFY, new byte[]{1}, new Ba(this), new C0418aa(this));
        this.f2636d.postDelayed(this.N, 10000L);
    }

    public void sendOTARequestCommand(byte b2, byte[] bArr, byte[] bArr2, byte[] bArr3, byte b3) {
        int length = bArr.length + 2 + bArr2.length + bArr3.length;
        byte[] bArr4 = new byte[length];
        bArr4[0] = b2;
        System.arraycopy(bArr, 0, bArr4, 1, bArr.length);
        System.arraycopy(bArr2, 0, bArr4, bArr.length + 1, bArr2.length);
        System.arraycopy(bArr3, 0, bArr4, bArr.length + 1 + bArr2.length, bArr3.length);
        bArr4[length - 1] = b3;
        LogUtils.d(this.f2633a, "request ota: " + ConvertUtils.bytes2HexString(bArr4));
        a(IOTAPlugin.OTAState.REQUEST);
        this.u.sendCommandWithCallback((byte) 34, bArr4, new za(this), new Aa(this));
        this.f2636d.postDelayed(this.N, 10000L);
    }

    public void setOnOTAActionListener(IOTAPlugin.IOTAActionListener iOTAActionListener) {
        this.f2635c = iOTAActionListener;
    }

    public void setSendCompleteFrameWithNoAckFlag(boolean z) {
        this.E = z;
    }

    public void startDownloadFirmware(Context context, DeviceVersionInfo deviceVersionInfo, String str, IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener) {
        this.M = context;
        this.A = iFirmwareDownloadListener;
        if (this.y != -1) {
            a(-400, "There is currently a download task with id" + this.y);
            return;
        }
        if (this.z == null) {
            this.z = DownloadManagerUtils.getInstance(context);
        }
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener2 = this.A;
        if (iFirmwareDownloadListener2 != null) {
            iFirmwareDownloadListener2.onDownloadStart();
        }
        if (this.L) {
            ReportProgressUtil.reportOtaProgress(this.K, deviceVersionInfo.getModel().getVersion(), this.w, this.x, ReportProgressUtil.TAG_START, ReportProgressUtil.CODE_OK, "");
        }
        this.y = this.z.downloadFile(deviceVersionInfo.getModel().getOtaUrl(), deviceVersionInfo.getModel().getMd5(), str, new C0444na(this, deviceVersionInfo));
        long j = this.y;
        if (j < 0) {
            a(-401, "no write permission or insufficient disk");
            return;
        }
        if (j > 0) {
            g();
        }
        if (this.y == 0) {
            this.y = -1L;
        }
    }

    public void startDownloadIlopFirmware(Context context, DeviceVersionInfo.DeviceInfoModel deviceInfoModel, String str, IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener) {
        this.M = context;
        this.A = iFirmwareDownloadListener;
        if (this.y != -1) {
            a(-400, "There is currently a download task with id" + this.y);
            return;
        }
        if (this.z == null) {
            this.z = DownloadManagerUtils.getInstance(context);
        }
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener2 = this.A;
        if (iFirmwareDownloadListener2 != null) {
            iFirmwareDownloadListener2.onDownloadStart();
        }
        if (this.L) {
            ReportProgressUtil.reportOtaProgress(this.K, deviceInfoModel.getVersion(), this.w, this.x, ReportProgressUtil.TAG_START, ReportProgressUtil.CODE_OK, "");
        }
        this.y = this.z.downloadFile(deviceInfoModel.getOtaUrl(), deviceInfoModel.getMd5(), str, new C0446oa(this, deviceInfoModel));
        long j = this.y;
        if (j < 0) {
            a(-401, "no write permission or insufficient disk");
            return;
        }
        if (j > 0) {
            g();
        }
        if (this.y == 0) {
            this.y = -1L;
        }
    }

    public void startOTA(byte[] bArr, byte[] bArr2, byte[] bArr3, byte b2, byte[] bArr4, byte b3, IOTAPlugin.IOTAActionListener iOTAActionListener) {
        this.F = false;
        this.p = 0;
        this.G = 0;
        this.f2635c = iOTAActionListener;
        IOTAPlugin.OTAState oTAState = this.f2634b;
        if (oTAState != IOTAPlugin.OTAState.IDLE && oTAState != IOTAPlugin.OTAState.FINISH && oTAState != IOTAPlugin.OTAState.ERROR) {
            b(4, "Waiting for the current upgrade to complete");
        }
        this.h = bArr;
        this.i = bArr2;
        this.j = bArr3;
        this.m = b2;
        this.l = bArr4;
        this.n = b3;
        this.k = Utils.byteArray2IntByLittleEndian(bArr3);
        sendOTARequestCommand(this.m, this.i, this.j, this.l, this.n);
    }

    public void stopDownloadFirmware() {
        DownloadManagerUtils downloadManagerUtils;
        long j = this.y;
        if (j != -1 || (downloadManagerUtils = this.z) == null) {
            return;
        }
        downloadManagerUtils.cancelDownload(j);
    }

    public void stopOTA() {
        IOTAPlugin.OTAState oTAState = this.f2634b;
        if (oTAState == IOTAPlugin.OTAState.IDLE || oTAState == IOTAPlugin.OTAState.FINISH) {
            return;
        }
        b(8, "User terminated the OTA process");
        this.f2634b = IOTAPlugin.OTAState.IDLE;
        this.f2636d.removeCallbacks(this.N);
        this.f2636d.removeCallbacks(this.P);
    }

    public void updateDeviceVersion(String str, String str2, IActionListener<UpdateDeviceVersionRespData> iActionListener) {
        RequestManage.getInstance().updateDeviceVersion(str, str2, new C0440la(this, iActionListener));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public class a extends Thread {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public boolean f2637a;

        public a() {
            this.f2637a = true;
        }

        public void a() {
            this.f2637a = false;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            if (OTAPluginProxy.this.z != null) {
                while (this.f2637a) {
                    int iValidDownload = OTAPluginProxy.this.z.validDownload(OTAPluginProxy.this.y);
                    if (iValidDownload != 0) {
                        OTAPluginProxy.this.a(iValidDownload, "Download failed");
                        OTAPluginProxy.this.y = -1L;
                        return;
                    }
                    DownloadManagerUtils.DownloadTaskDetails downloadDetails = OTAPluginProxy.this.z.getDownloadDetails(OTAPluginProxy.this.y);
                    if (downloadDetails == null) {
                        this.f2637a = false;
                        return;
                    }
                    if (OTAPluginProxy.this.A != null && downloadDetails.totalSize > 0) {
                        OTAPluginProxy.this.f2636d.post(new Ca(this, downloadDetails));
                    }
                    try {
                        Thread.sleep(200L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public /* synthetic */ a(OTAPluginProxy oTAPluginProxy, RunnableC0442ma runnableC0442ma) {
            this();
        }
    }

    public final boolean d() {
        BluetoothDeviceWrapper bluetoothDeviceWrapper = this.u.getBluetoothDeviceWrapper();
        return bluetoothDeviceWrapper != null && bluetoothDeviceWrapper.getSubtype() == BluetoothDeviceSubtype.GMA;
    }

    public final void e() {
        LogUtils.d(this.f2633a, "recheckVersion...");
        if (this.f2634b == IOTAPlugin.OTAState.WAIT_RECONNECT) {
            a(IOTAPlugin.OTAState.WAIT_RECHECK_VERSION);
            getFirmwareVersionCommand(new C0430ga(this));
        }
    }

    public final void f() {
        IOTAPlugin.OTAState oTAState = this.f2634b;
        if (oTAState == IOTAPlugin.OTAState.ERROR || oTAState == IOTAPlugin.OTAState.IDLE) {
            LogUtils.w(this.f2633a, "Current OTA state is ERROR, ignore");
            this.F = false;
            return;
        }
        if (this.o == this.e.size()) {
            this.F = false;
            return;
        }
        LogUtils.v(this.f2633a, "Start send pdu(" + this.o + ")");
        this.F = true;
        byte[] bytes = this.e.get(this.o).getBytes();
        this.D = 1;
        this.u.sendRawDataWithCallback(bytes, new C0420ba(this), new C0422ca(this));
    }

    public final void g() {
        h();
        this.B = new a(this, null);
        this.B.start();
    }

    public final void h() {
        a aVar = this.B;
        if (aVar != null) {
            aVar.a();
            try {
                this.B.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public final void i() {
        Context context;
        LogUtils.d(this.f2633a, String.format("unregisterBluetoothReceiver called, receiver: %s, context: %s", this.Q, this.M));
        BroadcastReceiver broadcastReceiver = this.Q;
        if (broadcastReceiver == null || (context = this.M) == null) {
            return;
        }
        context.unregisterReceiver(broadcastReceiver);
    }

    public final void c() {
        if (d() && this.Q == null) {
            LogUtils.i(this.f2633a, "Register system bluetoothA2dp receiver");
            this.Q = new C0456ta(this);
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.bluetooth.adapter.action.STATE_CHANGED");
            intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
            Context context = this.M;
            if (context != null) {
                context.registerReceiver(this.Q, intentFilter);
            }
        }
    }

    public final void b(int i, String str) {
        LogUtils.e(this.f2633a, "ota error (" + i + ":" + str + ")");
        this.F = false;
        this.f2636d.removeCallbacks(this.N);
        this.f2636d.removeCallbacks(this.O);
        i();
        this.f2634b = IOTAPlugin.OTAState.ERROR;
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2635c;
        if (iOTAActionListener != null) {
            iOTAActionListener.onFailed(i, str);
        }
    }

    public final void a(IOTAPlugin.OTAState oTAState) {
        this.f2634b = oTAState;
        IOTAPlugin.IOTAActionListener iOTAActionListener = this.f2635c;
        if (iOTAActionListener != null) {
            iOTAActionListener.onStateChanged(this.f2634b);
        }
    }

    public final void b(String str, String str2, IActionListener<DeviceVersionInfo> iActionListener) {
        LogUtils.d(this.f2633a, String.format("Query ota info, uuid: %s, appVersion: %s", str, str2));
        RequestManage.getInstance().queryOtaInfo(str, str2, new C0438ka(this, iActionListener));
    }

    public final void a(String str, String str2, IActionListener<String> iActionListener) {
        RequestManage.getInstance().getDeviceUUIDViaProductId(str, str2, new C0436ja(this, iActionListener));
    }

    public final void a(int i, IActionListener iActionListener, AISCommand aISCommand) {
        LogUtils.d(this.f2633a, "ReTransmission command, Key: " + i + ", Command type: " + Utils.byte2String(aISCommand.getHeader().getCommandType(), true));
        this.u.sendRawDataWithCallback(aISCommand.getBytes(), new C0450qa(this), new C0452ra(this, iActionListener));
        b(i, iActionListener, (AISCommand) null);
    }

    public final void b(int i, IActionListener iActionListener, AISCommand aISCommand) {
        LogUtils.d(this.f2633a, "Save listener and set timeout task for key : " + i);
        this.f.put(i, iActionListener);
        if (aISCommand != null) {
            this.J.put(i, aISCommand);
        }
        RunnableC0448pa runnableC0448pa = new RunnableC0448pa(this, i, iActionListener);
        this.g.put(i, runnableC0448pa);
        this.f2636d.postDelayed(runnableC0448pa, 3000L);
    }

    public void startOTA(String str, IOTAPlugin.IOTAActionListener iOTAActionListener) {
        IOTAPlugin.IOTAActionListener otaActionListener;
        if (this.L) {
            DeviceVersionInfo deviceVersionInfo = this.C;
            otaActionListener = new OtaActionListener(iOTAActionListener, this.u.getBluetoothDeviceWrapper(), this.w, this.x, this.K, deviceVersionInfo == null ? "" : deviceVersionInfo.getModel().getVersion());
        } else {
            otaActionListener = iOTAActionListener;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(str));
            int iAvailable = fileInputStream.available();
            byte[] bArr = new byte[iAvailable];
            fileInputStream.read(bArr);
            fileInputStream.close();
            byte[] bArrInt2ByteArrayByLittleEndian = {-1, -1, -1, -1};
            if (this.C != null) {
                bArrInt2ByteArrayByLittleEndian = Utils.int2ByteArrayByLittleEndian(Utils.adapterToAisVersion(this.C.getModel().getVersion()));
                String str2 = this.f2633a;
                StringBuilder sb = new StringBuilder();
                sb.append("firmware version: ");
                sb.append(ConvertUtils.bytes2HexString(bArrInt2ByteArrayByLittleEndian));
                LogUtils.d(str2, sb.toString());
            }
            startOTA(bArr, bArrInt2ByteArrayByLittleEndian, Utils.int2ByteArrayByLittleEndian(iAvailable), (byte) 0, Arrays.copyOfRange(Utils.int2ByteArrayByLittleEndian(Utils.genCrc16CCITT(bArr, 0, bArr.length)), 0, 2), (byte) 0, otaActionListener);
        } catch (IOException e) {
            LogUtils.e(this.f2633a, e.toString());
            if (otaActionListener != null) {
                otaActionListener.onFailed(-200, "Failed to open firmware file");
            }
        }
    }

    public final void a(int i) {
        LogUtils.d(this.f2633a, "Remove listener and cancel timeout task for key : " + i);
        this.f.remove(i);
        this.f2636d.removeCallbacks(this.g.get(i));
    }

    public final void a(int i, String str) {
        IOTAPlugin.IFirmwareDownloadListener iFirmwareDownloadListener = this.A;
        if (iFirmwareDownloadListener != null) {
            iFirmwareDownloadListener.onFailed(i, str);
        }
        if (this.L) {
            DeviceVersionInfo deviceVersionInfo = this.C;
            String version = deviceVersionInfo == null ? "" : deviceVersionInfo.getModel().getVersion();
            ReportProgressUtil.reportOtaProgress(this.K, version, this.w, this.x, "FINISH", ReportProgressUtil.CODE_ERR, str + OpenAccountUIConstants.UNDER_LINE + i);
        }
    }

    public final int b() {
        BluetoothDeviceWrapper bluetoothDeviceWrapper = this.u.getBluetoothDeviceWrapper();
        if (bluetoothDeviceWrapper == null || bluetoothDeviceWrapper.getSubtype() != BluetoothDeviceSubtype.GMA) {
            return 8000;
        }
        return Indexable.MAX_STRING_LENGTH;
    }

    public final boolean a() {
        IOTAPlugin.OTAState oTAState = this.f2634b;
        return (oTAState == IOTAPlugin.OTAState.IDLE || oTAState == IOTAPlugin.OTAState.FINISH || oTAState == IOTAPlugin.OTAState.ERROR) ? false : true;
    }
}
