package com.alibaba.ailabs.iot.aisbase.spec;

import android.annotation.SuppressLint;
import android.util.Log;
import com.alibaba.ailabs.iot.aisbase.Utils;
import com.alibaba.ailabs.iot.aisbase.contant.GmaLogConst;
import com.alibaba.ailabs.iot.aisbase.exception.IllegalCommandException;
import com.alibaba.ailabs.iot.aisbase.exception.IncompletePayloadException;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.ailabs.tg.utils.LogUtils;
import java.util.Arrays;

/* JADX INFO: loaded from: classes.dex */
public class AISCommand {
    public static final int HEADER_LENGTH = 4;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public AISCommandHeader f2661a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public byte[] f2662b;

    public static class AISCommandHeader {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public byte f2663a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public byte f2664b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public byte f2665c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public byte f2666d;
        public byte e;
        public byte f;
        public int g;

        public AISCommandHeader(byte b2, byte b3, byte b4, byte b5, byte b6) {
            this.f2663a = b2;
            this.f2664b = (byte) 0;
            this.f2665c = (byte) 0;
            this.f2666d = b3;
            this.e = b4;
            this.f = b5;
            this.g = b6;
        }

        public byte[] getBytes() {
            return new byte[]{(byte) (this.f2663a | (this.f2664b << 4) | (this.f2665c << 5)), this.f2666d, (byte) ((this.e << 4) | this.f), (byte) this.g};
        }

        public byte getCommandType() {
            return this.f2666d;
        }

        public byte getEncryption() {
            return this.f2664b;
        }

        public byte getFrameSeq() {
            return this.f;
        }

        public byte getMsgID() {
            return this.f2663a;
        }

        public int getPayloadLength() {
            return this.g;
        }

        public byte getTotalFrame() {
            return this.e;
        }

        public byte getVersion() {
            return this.f2665c;
        }

        public void setCommandType(byte b2) {
            this.f2666d = b2;
        }

        public void setEncryption(byte b2) {
            this.f2664b = b2;
        }

        public void setFrameSeq(byte b2) {
            this.f = b2;
        }

        public void setPayloadLength(byte b2) {
            this.g = b2;
        }

        public void setTotalFrame(byte b2) {
            this.e = b2;
        }

        public void setVersion(byte b2) {
            this.f2665c = b2;
        }

        public void setsgID(byte b2) {
            this.f2663a = b2;
        }

        @SuppressLint({"DefaultLocale"})
        public String toString() {
            return String.format("Command(%s), flag(version: %d, encrypt: %d, message id: %d, total frame: %d, current frame sequence: %d,frame length: %d)", Utils.byte2String(this.f2666d, true), Byte.valueOf(this.f2665c), Byte.valueOf(this.f2664b), Byte.valueOf(this.f2663a), Byte.valueOf(this.e), Byte.valueOf(this.f), Integer.valueOf(this.g));
        }

        public static AISCommandHeader a(byte[] bArr) {
            AISCommandHeader aISCommandHeader = new AISCommandHeader();
            aISCommandHeader.f2663a = (byte) (bArr[0] & 15);
            aISCommandHeader.f2664b = (byte) ((bArr[0] & 16) >> 4);
            aISCommandHeader.f2665c = (byte) ((bArr[0] & 224) >> 5);
            aISCommandHeader.f2666d = bArr[1];
            aISCommandHeader.e = (byte) ((bArr[2] & 240) >> 4);
            aISCommandHeader.f = (byte) (bArr[2] & 15);
            aISCommandHeader.g = bArr[3] & 255;
            return aISCommandHeader;
        }

        public AISCommandHeader() {
        }
    }

    public AISCommand() {
    }

    public static int getMessageSpec(byte b2, byte b3) {
        return b2 | 0;
    }

    public static int getMessageSpec(byte b2, byte b3, byte b4) {
        return b2 | 0 | (b3 << 8);
    }

    @SuppressLint({"LongLogTag"})
    public static AISCommand parseFromByte(byte[] bArr) throws IllegalCommandException, IncompletePayloadException {
        if (bArr == null) {
            throw new IllegalCommandException();
        }
        Log.i(GmaLogConst.GMA_CONNECT_AUTH, "parseFromByte: " + ConvertUtils.bytes2HexString(bArr));
        if (bArr.length < 4) {
            throw new IncompletePayloadException("Header not complete", 0, 0);
        }
        AISCommand aISCommand = new AISCommand();
        aISCommand.f2661a = AISCommandHeader.a(Arrays.copyOfRange(bArr, 0, 4));
        if (aISCommand.f2661a.g == 0) {
            aISCommand.f2662b = null;
        } else if (aISCommand.f2661a.g > 0) {
            if (bArr.length < aISCommand.f2661a.g + 4) {
                Log.e(GmaLogConst.GMA_CONNECT_AUTH, "***********receive error while processing with data: " + ConvertUtils.bytes2HexString(bArr));
                throw new IncompletePayloadException(String.format("Need %d payload, only %d in the payload in current packet", Integer.valueOf(aISCommand.f2661a.g), Integer.valueOf(bArr.length - 4)), aISCommand.f2661a.g, bArr.length - 4);
            }
            aISCommand.f2662b = Arrays.copyOfRange(bArr, 4, aISCommand.f2661a.g + 4);
        }
        return aISCommand;
    }

    public byte[] getBytes() {
        byte[] bytes = this.f2661a.getBytes();
        byte[] bArr = this.f2662b;
        byte[] bArr2 = new byte[(bArr == null ? 0 : bArr.length) + 4];
        System.arraycopy(bytes, 0, bArr2, 0, 4);
        byte[] bArr3 = this.f2662b;
        if (bArr3 != null) {
            System.arraycopy(bArr3, 0, bArr2, 4, bArr3.length);
        }
        return bArr2;
    }

    public AISCommandHeader getHeader() {
        return this.f2661a;
    }

    public byte[] getPayload() {
        return this.f2662b;
    }

    public AISCommand setEnableEncrypt(boolean z) {
        LogUtils.d("AISCommand", "set enable encrypt: " + z);
        AISCommandHeader aISCommandHeader = this.f2661a;
        if (aISCommandHeader != null) {
            aISCommandHeader.setEncryption(z ? (byte) 1 : (byte) 0);
        }
        return this;
    }

    public void setHeader(AISCommandHeader aISCommandHeader) {
        this.f2661a = aISCommandHeader;
    }

    public void setPayload(byte[] bArr) {
        this.f2662b = bArr;
    }

    public AISCommand(byte b2, byte b3, byte b4, byte b5, byte b6, byte[] bArr) {
        this.f2661a = new AISCommandHeader(b2, b3, b4, b5, b6);
        this.f2662b = bArr;
    }
}
