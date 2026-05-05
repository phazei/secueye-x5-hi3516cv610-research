package com.aliyun.alink.linksdk.alcs.coap;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.option.BlockOption;
import com.aliyun.alink.linksdk.alcs.coap.option.OptionSet;
import com.aliyun.alink.linksdk.tools.ALog;
import java.net.InetAddress;

/* JADX INFO: loaded from: classes2.dex */
public abstract class AlcsCoAPMessage {
    public static final int MAX_MID = 65535;
    public static final int NONE = -1;
    private static final String TAG = "AlcsCoAPMessage";
    private volatile boolean acknowledged;
    private volatile byte[] bytes;
    private volatile boolean canceled;
    private InetAddress destination;
    private int destinationPort;
    private volatile boolean duplicate;
    private int multicast;
    private OptionSet options;
    private byte[] payload;
    private volatile boolean rejected;
    private volatile Throwable sendError;
    private volatile boolean sent;
    private InetAddress source;
    private int sourcePort;
    private volatile boolean timedOut;
    private volatile long timestamp;
    private AlcsCoAPConstant.Type type;
    private volatile int mid = -1;
    private volatile byte[] token = null;

    public abstract int getRawCode();

    protected AlcsCoAPMessage() {
    }

    public AlcsCoAPMessage(AlcsCoAPConstant.Type type) {
        this.type = type;
    }

    public AlcsCoAPConstant.Type getType() {
        return this.type;
    }

    public int getRawType() {
        AlcsCoAPConstant.Type type = this.type;
        if (type != null) {
            return type.value;
        }
        return 0;
    }

    public int isMulticast() {
        return this.multicast;
    }

    public void setMulticast(int i) {
        this.multicast = i;
    }

    public AlcsCoAPMessage setType(AlcsCoAPConstant.Type type) {
        this.type = type;
        return this;
    }

    public void setType(int i) {
        this.type = AlcsCoAPConstant.Type.valueOf(i);
    }

    public boolean isConfirmable() {
        return getType() == AlcsCoAPConstant.Type.CON;
    }

    public AlcsCoAPMessage setConfirmable(boolean z) {
        setType(z ? AlcsCoAPConstant.Type.CON : AlcsCoAPConstant.Type.NON);
        return this;
    }

    public int getMID() {
        return this.mid;
    }

    public boolean hasMID() {
        return this.mid != -1;
    }

    public AlcsCoAPMessage setMID(int i) {
        if (i > 65535 || i < -1) {
            ALog.e(TAG, "The MID must be an unsigned 16-bit number but was " + i);
            return this;
        }
        this.mid = i;
        return this;
    }

    public void removeMID() {
        setMID(-1);
    }

    public boolean hasEmptyToken() {
        return this.token == null || this.token.length == 0;
    }

    public byte[] getToken() {
        return this.token;
    }

    public String getTokenString() {
        return Utils.toHexString(getToken());
    }

    public void setToken(byte[] bArr) {
        if (bArr != null && bArr.length > 8) {
            ALog.e(TAG, "Token length must be between 0 and 8 inclusive");
        } else {
            this.token = bArr;
        }
    }

    public void setToken(String str) {
        if (TextUtils.isEmpty(str) || str.length() > 8) {
            return;
        }
        this.token = str.getBytes();
    }

    public OptionSet getOptions() {
        if (this.options == null) {
            this.options = new OptionSet();
        }
        return this.options;
    }

    public AlcsCoAPMessage setOptions(OptionSet optionSet) {
        this.options = new OptionSet(optionSet);
        return this;
    }

    public int getPayloadSize() {
        byte[] bArr = this.payload;
        if (bArr == null) {
            return 0;
        }
        return bArr.length;
    }

    public byte[] getPayload() {
        return this.payload;
    }

    public String getPayloadString() {
        byte[] bArr = this.payload;
        return bArr == null ? "" : new String(bArr, AlcsCoAPConstant.UTF8_CHARSET);
    }

    public AlcsCoAPMessage setPayload(String str) {
        if (str == null) {
            this.payload = null;
        } else {
            setPayload(str.getBytes(AlcsCoAPConstant.UTF8_CHARSET));
        }
        return this;
    }

    public void setPayload(byte[] bArr) {
        this.payload = bArr;
    }

    public InetAddress getDestination() {
        return this.destination;
    }

    public AlcsCoAPMessage setDestination(InetAddress inetAddress) {
        this.destination = inetAddress;
        return this;
    }

    public int getDestinationPort() {
        return this.destinationPort;
    }

    public AlcsCoAPMessage setDestinationPort(int i) {
        this.destinationPort = i;
        return this;
    }

    public InetAddress getSource() {
        return this.source;
    }

    public void setSource(InetAddress inetAddress) {
        this.source = inetAddress;
    }

    public int getSourcePort() {
        return this.sourcePort;
    }

    public void setSourcePort(int i) {
        this.sourcePort = i;
    }

    public boolean isAcknowledged() {
        return this.acknowledged;
    }

    public void setAcknowledged(boolean z) {
        this.acknowledged = z;
    }

    public boolean isRejected() {
        return this.rejected;
    }

    public void setRejected(boolean z) {
        this.rejected = z;
    }

    public boolean isTimedOut() {
        return this.timedOut;
    }

    public void setTimedOut(boolean z) {
        this.timedOut = z;
    }

    public boolean isCanceled() {
        return this.canceled;
    }

    public void setCanceled(boolean z) {
        this.canceled = z;
    }

    public boolean isSent() {
        return this.sent;
    }

    public void setSent(boolean z) {
        this.sent = z;
    }

    public Throwable getSendError() {
        return this.sendError;
    }

    public void setSendError(Throwable th) {
        this.sendError = th;
    }

    public boolean isDuplicate() {
        return this.duplicate;
    }

    public void setDuplicate(boolean z) {
        this.duplicate = z;
    }

    public byte[] getBytes() {
        return this.bytes;
    }

    public void setBytes(byte[] bArr) {
        this.bytes = bArr;
    }

    public boolean hasBlock(BlockOption blockOption) {
        return getPayloadSize() > 0 && blockOption.getOffset() < getPayloadSize();
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
    }

    public void cancel() {
        setCanceled(true);
    }
}
