package com.alibaba.ailabs.iot.aisbase.plugin.basic;

import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ICommandActionListener;
import com.alibaba.ailabs.iot.aisbase.callback.ICommandSendListener;
import com.alibaba.ailabs.iot.aisbase.plugin.IPlugin;

/* JADX INFO: loaded from: classes.dex */
public interface IBasicPlugin extends IPlugin {
    void getManufacturerSpecificData(IActionListener<byte[]> iActionListener);

    void sendCommand(byte[] bArr, ICommandSendListener iCommandSendListener);

    void setCommandActionListener(ICommandActionListener iCommandActionListener);
}
