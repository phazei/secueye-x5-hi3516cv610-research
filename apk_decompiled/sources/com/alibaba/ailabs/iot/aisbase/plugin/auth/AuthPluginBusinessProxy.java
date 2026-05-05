package com.alibaba.ailabs.iot.aisbase.plugin.auth;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import com.alibaba.ailabs.iot.aisbase.Constants;
import com.alibaba.ailabs.iot.aisbase.F;
import com.alibaba.ailabs.iot.aisbase.G;
import com.alibaba.ailabs.iot.aisbase.H;
import com.alibaba.ailabs.iot.aisbase.J;
import com.alibaba.ailabs.iot.aisbase.K;
import com.alibaba.ailabs.iot.aisbase.L;
import com.alibaba.ailabs.iot.aisbase.M;
import com.alibaba.ailabs.iot.aisbase.N;
import com.alibaba.ailabs.iot.aisbase.O;
import com.alibaba.ailabs.iot.aisbase.RequestManage;
import com.alibaba.ailabs.iot.aisbase.UTLogUtils;
import com.alibaba.ailabs.iot.aisbase.Utils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.IDetailActionListener;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.contant.GmaLogConst;
import com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.util.concurrent.atomic.AtomicBoolean;

/* JADX INFO: loaded from: classes.dex */
public class AuthPluginBusinessProxy implements CommandResponseDispatcher.OnCommandReceivedListener {
    public static final int MAX_RETRY_COUNT = 3;
    public static final String TAG = GmaLogConst.GMA_CONNECT_AUTH + AuthPluginBusinessProxy.class.getSimpleName();
    public static AtomicBoolean isAuthAndBind = new AtomicBoolean(false);
    public int mAuthAndCheckCipherRetries;
    public Runnable mAuthAndCheckCipherTimeoutTask;
    public Runnable mGetAuthRandomTimeoutTask;
    public IPlugin mRealPlugin;
    public ITransmissionLayer mTransmissionLayer;
    public int retries;
    public SparseArray<IActionListener> mListeners = new SparseArray<>();
    public SparseArray<Runnable> mTimeoutTask = new SparseArray<>();
    public SparseArray<AISCommand> mReTransmissionArray = new SparseArray<>();
    public Handler mHandler = new Handler(Looper.getMainLooper());
    public String mDeviceAddress = "";
    public String mProductId = "";
    public boolean mIsBLEDevice = false;

    public AuthPluginBusinessProxy(CommandResponseDispatcher commandResponseDispatcher, ITransmissionLayer iTransmissionLayer) {
        this.retries = 3;
        this.mAuthAndCheckCipherRetries = 3;
        commandResponseDispatcher.setCommandReassembleByFrameSeq(Constants.CMD_TYPE.CMD_AUDIO_UPSTREAM, true);
        commandResponseDispatcher.setCommandReassembleByFrameSeq((byte) 4, true);
        commandResponseDispatcher.subscribeMultiCommandReceivedListener(new byte[]{16, 17, 18, 19}, this);
        this.mTransmissionLayer = iTransmissionLayer;
        this.retries = 3;
        this.mAuthAndCheckCipherRetries = 3;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void authCheckAndGetBleKey(IPlugin iPlugin, byte[] bArr, IActionListener<byte[]> iActionListener) {
        if (this.mAuthAndCheckCipherTimeoutTask == null) {
            this.mAuthAndCheckCipherTimeoutTask = new H(this, iPlugin, bArr, iActionListener);
            this.mHandler.postDelayed(this.mAuthAndCheckCipherTimeoutTask, 3000L);
        }
        UTLogUtils.updateBusInfo("gma_auth", UTLogUtils.buildDeviceInfo(iPlugin.getBluetoothDeviceWrapper()), UTLogUtils.buildAuthBusInfo("auth_and_check_cipher", this.mProductId, this.mDeviceAddress, 0, ""));
        RequestManage.getInstance().authCheckAndGetBleKey(this.mProductId, this.mDeviceAddress, ConvertUtils.bytes2HexString(bArr).toLowerCase(), this.mIsBLEDevice, new J(this, iActionListener, iPlugin));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canRetryAuthAndCheckCipher() {
        int i = this.mAuthAndCheckCipherRetries;
        if (i <= 0) {
            return false;
        }
        this.mAuthAndCheckCipherRetries = i - 1;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean canRetryGetAuthRandom() {
        int i = this.retries;
        if (i <= 0) {
            return false;
        }
        this.retries = i - 1;
        return true;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelAuthAndCheckCipherTimeoutTask() {
        Runnable runnable = this.mAuthAndCheckCipherTimeoutTask;
        if (runnable == null) {
            return;
        }
        this.mHandler.removeCallbacks(runnable);
        this.mAuthAndCheckCipherTimeoutTask = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelGetAuthRandomTimeoutTask() {
        Runnable runnable = this.mGetAuthRandomTimeoutTask;
        if (runnable == null) {
            return;
        }
        this.mHandler.removeCallbacks(runnable);
        this.mGetAuthRandomTimeoutTask = null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deliveryRandomId(IPlugin iPlugin, IActionListener<byte[]> iActionListener, String str) {
        byte[] bArrHexString2Bytes = ConvertUtils.hexString2Bytes(str);
        UTLogUtils.updateBusInfo("gma_auth", UTLogUtils.buildDeviceInfo(iPlugin.getBluetoothDeviceWrapper()), UTLogUtils.buildAuthBusInfo("delivery_random", this.mProductId, this.mDeviceAddress, 0, ""));
        realSendCommand(iPlugin, new G(this, iActionListener, iPlugin), (byte) 16, bArrHexString2Bytes, (byte) 17);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void reTransmissionCommand(int i, IActionListener iActionListener, AISCommand aISCommand) {
        LogUtils.d(TAG, "ReTransmission command, Key: " + i + ", Command type: " + Utils.byte2String(aISCommand.getHeader().getCommandType(), true));
        this.mRealPlugin.sendRawDataWithCallback(aISCommand.getBytes(), new N(this), new O(this, iActionListener));
        saveListenerAndSetTimeoutTask(i, iActionListener, null);
    }

    private void realSendCommand(IPlugin iPlugin, IActionListener iActionListener, byte b2, byte[] bArr, byte b3) {
        AISCommand aISCommandSendCommandWithCallback = iPlugin.sendCommandWithCallback(b2, bArr, new K(this), new L(this, iActionListener));
        if (aISCommandSendCommandWithCallback == null || b3 == 0) {
            return;
        }
        saveListenerAndSetTimeoutTask(AISCommand.getMessageSpec(b3, aISCommandSendCommandWithCallback.getHeader().getMsgID()), iActionListener, aISCommandSendCommandWithCallback);
    }

    private void removeListenerAndCancelTimeoutTask(int i) {
        this.mListeners.remove(i);
        this.mHandler.removeCallbacks(this.mTimeoutTask.get(i));
    }

    private void saveListenerAndSetTimeoutTask(int i, IActionListener iActionListener, AISCommand aISCommand) {
        this.mListeners.put(i, iActionListener);
        if (aISCommand != null) {
            this.mReTransmissionArray.put(i, aISCommand);
        }
        this.mListeners.put(i, iActionListener);
        M m = new M(this, i, iActionListener);
        this.mTimeoutTask.put(i, m);
        this.mHandler.postDelayed(m, 3000L);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendVerifyResult(IPlugin iPlugin, IActionListener iActionListener, boolean z) {
        realSendCommand(iPlugin, iActionListener, (byte) 18, z ? new byte[]{0} : new byte[]{1}, (byte) 19);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher.OnCommandReceivedListener
    public void onCommandReceived(byte b2, byte b3, byte[] bArr) {
        LogUtils.d(TAG, "onCommandReceived, cmdType " + ((int) b2) + " payload: " + ConvertUtils.bytes2HexString(bArr));
        int messageSpec = AISCommand.getMessageSpec(b2, b3);
        IActionListener iActionListener = this.mListeners.get(messageSpec);
        LogUtils.d(TAG, "onCommandReceived, listener: " + iActionListener);
        if (b2 == 17) {
            removeListenerAndCancelTimeoutTask(messageSpec);
            if (bArr != null && bArr.length > 0) {
                if (iActionListener != null) {
                    iActionListener.onSuccess(bArr);
                    return;
                }
                return;
            } else {
                if (iActionListener != null) {
                    iActionListener.onFailure(-206, "Illegal reply: " + ConvertUtils.bytes2HexString(bArr));
                    return;
                }
                return;
            }
        }
        if (b2 != 19) {
            return;
        }
        removeListenerAndCancelTimeoutTask(messageSpec);
        if (bArr != null && bArr.length > 0) {
            if (iActionListener != null) {
                iActionListener.onSuccess(bArr);
            }
        } else if (iActionListener != null) {
            iActionListener.onFailure(-206, "Illegal reply: " + ConvertUtils.bytes2HexString(bArr));
        }
    }

    public void setIsBLEDevice(boolean z) {
        this.mIsBLEDevice = z;
    }

    public void startAuth(IPlugin iPlugin, byte[] bArr, String str, IActionListener<byte[]> iActionListener) {
        this.mRealPlugin = iPlugin;
        int iByteArray2Int = Utils.byteArray2Int(bArr);
        this.mProductId = String.valueOf(iByteArray2Int);
        LogUtils.d(TAG, String.format("Start auth, productId: %s, macAddress: %s", String.valueOf(iByteArray2Int), str));
        UTLogUtils.updateBusInfo("gma_auth", UTLogUtils.buildDeviceInfo(iPlugin.getBluetoothDeviceWrapper()), UTLogUtils.buildAuthBusInfo("start", this.mProductId, str, 0, ""));
        this.mDeviceAddress = str;
        if (iActionListener instanceof IDetailActionListener) {
            ((IDetailActionListener) iActionListener).onState(0, "start get random id", null);
        }
        isAuthAndBind.set(false);
        RequestManage.getInstance().getAuthRandomId(this.mProductId, str, this.mIsBLEDevice, new F(this, iPlugin, iActionListener, bArr, str));
    }
}
