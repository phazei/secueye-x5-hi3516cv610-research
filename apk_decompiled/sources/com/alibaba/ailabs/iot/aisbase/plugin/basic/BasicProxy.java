package com.alibaba.ailabs.iot.aisbase.plugin.basic;

import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import com.alibaba.ailabs.iot.aisbase.P;
import com.alibaba.ailabs.iot.aisbase.Q;
import com.alibaba.ailabs.iot.aisbase.S;
import com.alibaba.ailabs.iot.aisbase.T;
import com.alibaba.ailabs.iot.aisbase.U;
import com.alibaba.ailabs.iot.aisbase.Utils;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ICommandActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ICommandSendListener;
import com.alibaba.ailabs.iot.aisbase.channel.ITransmissionLayer;
import com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher;
import com.alibaba.ailabs.iot.aisbase.plugin.PluginBase;
import com.alibaba.ailabs.iot.aisbase.spec.AISCommand;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;

/* JADX INFO: loaded from: classes.dex */
public class BasicProxy implements CommandResponseDispatcher.OnCommandReceivedListener {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2622a = "BasicProxy";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public ICommandActionListener f2623b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public ITransmissionLayer f2624c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public PluginBase f2625d;
    public SparseArray<IActionListener> e = new SparseArray<>();
    public SparseArray<Runnable> f = new SparseArray<>();
    public Handler g = new Handler(Looper.getMainLooper());

    public BasicProxy(CommandResponseDispatcher commandResponseDispatcher, ITransmissionLayer iTransmissionLayer, PluginBase pluginBase) {
        this.f2624c = iTransmissionLayer;
        commandResponseDispatcher.subscribeMultiCommandReceivedListener(new byte[]{1, 3, 15, 8}, this);
        this.f2625d = pluginBase;
    }

    public void getManufacturerSpecificData(IActionListener<byte[]> iActionListener) {
        a(iActionListener, (byte) 7, null, (byte) 8);
    }

    @Override // com.alibaba.ailabs.iot.aisbase.dispatcher.CommandResponseDispatcher.OnCommandReceivedListener
    public void onCommandReceived(byte b2, byte b3, byte[] bArr) {
        ICommandActionListener iCommandActionListener;
        if (b2 == 1) {
            ICommandActionListener iCommandActionListener2 = this.f2623b;
            if (iCommandActionListener2 != null) {
                iCommandActionListener2.onNotify(bArr);
                return;
            }
            return;
        }
        if (b2 == 3) {
            ICommandActionListener iCommandActionListener3 = this.f2623b;
            if (iCommandActionListener3 != null) {
                iCommandActionListener3.onResponse(bArr);
                return;
            }
            return;
        }
        if (b2 == 8) {
            this.f2624c.forwardStream(bArr);
        } else if (b2 == 15 && (iCommandActionListener = this.f2623b) != null) {
            iCommandActionListener.onError();
        }
    }

    public void sendCommand(byte[] bArr, ICommandSendListener iCommandSendListener) {
        this.f2625d.sendCommandWithCallback((byte) 2, bArr, new P(this, iCommandSendListener), new Q(this, iCommandSendListener));
    }

    public void setCommandActionListener(ICommandActionListener iCommandActionListener) {
        this.f2623b = iCommandActionListener;
    }

    public final void a(IActionListener iActionListener, byte b2, byte[] bArr, byte b3) {
        LogUtils.d(f2622a, "Send command(" + Utils.byte2String(b2, true) + "), payload(" + ConvertUtils.bytes2HexString(bArr) + ")");
        AISCommand aISCommandSendCommandWithCallback = this.f2625d.sendCommandWithCallback(b2, bArr, new S(this), new T(this, iActionListener));
        if (aISCommandSendCommandWithCallback == null || b3 == 0) {
            return;
        }
        a(AISCommand.getMessageSpec(b3, aISCommandSendCommandWithCallback.getHeader().getMsgID()), iActionListener);
    }

    public final void a(int i, IActionListener iActionListener) {
        LogUtils.d(f2622a, "Set timeout timer for key: " + i);
        this.e.put(i, iActionListener);
        U u = new U(this, i, iActionListener);
        this.f.put(i, u);
        this.g.postDelayed(u, 3000L);
    }
}
