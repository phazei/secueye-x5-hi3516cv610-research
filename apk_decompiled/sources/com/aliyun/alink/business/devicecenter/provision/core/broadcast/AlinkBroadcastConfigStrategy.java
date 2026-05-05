package com.aliyun.alink.business.devicecenter.provision.core.broadcast;

import android.content.Context;
import android.text.TextUtils;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkHelper;
import com.aliyun.alink.business.devicecenter.base.DCErrorCode;
import com.aliyun.alink.business.devicecenter.config.BaseProvisionStrategy;
import com.aliyun.alink.business.devicecenter.config.IConfigCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigExtraCallback;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.annotation.ConfigStrategy;
import com.aliyun.alink.business.devicecenter.config.model.DCAlibabaConfigParams;
import com.aliyun.alink.business.devicecenter.config.model.DCConfigParams;
import com.aliyun.alink.business.devicecenter.config.phoneap.AlinkAESHelper;
import com.aliyun.alink.business.devicecenter.log.ALog;
import com.aliyun.alink.business.devicecenter.provision.core.C0480n;
import com.aliyun.alink.business.devicecenter.provision.core.RunnableC0482p;
import com.aliyun.alink.business.devicecenter.provision.core.RunnableC0483q;
import com.aliyun.alink.business.devicecenter.provision.core.RunnableC0484r;
import com.aliyun.alink.business.devicecenter.provision.core.RunnableC0485s;
import com.aliyun.alink.business.devicecenter.provision.core.RunnableC0486t;
import com.aliyun.alink.business.devicecenter.provision.core.Z;
import com.aliyun.alink.business.devicecenter.track.DCUserTrack;
import com.aliyun.alink.business.devicecenter.utils.AlinkWifiSolutionUtils;
import com.aliyun.alink.business.devicecenter.utils.StringUtils;
import com.aliyun.alink.business.devicecenter.utils.ThreadPool;
import com.aliyun.alink.business.devicecenter.utils.WiFiUtils;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.gms.common.ConnectionResult;
import io.netty.handler.codec.http2.Http2CodecUtil;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.codec.digest.MessageDigestAlgorithms;

/* JADX INFO: loaded from: classes2.dex */
@ConfigStrategy(linkType = LinkType.ALI_BROADCAST)
public class AlinkBroadcastConfigStrategy extends BaseProvisionStrategy implements IConfigStrategy {
    public static String TAG = "AlinkBroadcastConfigStrategy";
    public long INTERVAL_UDP_Loop;
    public long INTERVAL_UDP_Loop_LEVEL1;
    public long INTERVAL_UDP_Loop_LEVEL2;
    public long INTERVAL_UDP_Packet;
    public long INTERVAL_UDP_Packet_LEVEL0;
    public long INTERVAL_UDP_Packet_LEVEL1;
    public long INTERVAL_UDP_Packet_LEVEL2;
    public long INTERVAL_UDP_SENDING;
    public long INTERVAL_UDP_SENDING_LEVEL1;
    public long INTERVAL_UDP_SENDING_LEVEL2;
    public final String MULTICAST_UDP_Addr_Prefix;
    public final int START_FLAG1;
    public final int START_FLAG2;
    public final int START_FLAG3;
    public final String UDP_Addr;
    public int UDP_LEVEL;
    public final int UDP_PORT;
    public InetAddress address;
    public InetAddress addressMulticast;
    public String appRandom;
    public byte[] buffer;
    public String cloudRandom;
    public AtomicInteger delayBroadcastTimeAI;
    public boolean enableMulticastSend;
    public boolean enableSendRegion;
    public String getTokenBssid;
    public boolean getTokenWithBssid;
    public boolean isChinessSSID;
    public AtomicBoolean isProvisioningAB;
    public Context mContext;
    public Z mP2PProvision;
    public byte[] packetMulticastByteArray;
    public int port;
    public int portIndex;
    public String securityAesKey;
    public int sendBssidByteLen;
    public DatagramPacket sendPacketMulticast;
    public byte[] send_data;
    public DatagramPacket send_packet;
    public Thread startMulProvisionThread;
    public Thread startP2PThread;
    public Thread startProvisionThread;
    public int testFlagByte;
    public int tokenByteLen;
    public DatagramSocket udpSocket;
    public MulticastSocket udpSocketMulticast;
    public AtomicBoolean useAppTokenAB;

    public AlinkBroadcastConfigStrategy() {
        this.START_FLAG1 = 1248;
        this.START_FLAG2 = 1248;
        this.START_FLAG3 = 1248;
        this.UDP_PORT = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS;
        this.MULTICAST_UDP_Addr_Prefix = "239.";
        this.UDP_Addr = "255.255.255.255";
        this.INTERVAL_UDP_SENDING_LEVEL1 = 10000L;
        this.INTERVAL_UDP_SENDING_LEVEL2 = 10000L;
        this.INTERVAL_UDP_Packet_LEVEL0 = 40L;
        this.INTERVAL_UDP_Packet_LEVEL1 = 20L;
        this.INTERVAL_UDP_Packet_LEVEL2 = 100L;
        this.INTERVAL_UDP_Loop_LEVEL1 = 200L;
        this.INTERVAL_UDP_Loop_LEVEL2 = 0L;
        this.isProvisioningAB = new AtomicBoolean(false);
        this.enableMulticastSend = true;
        this.enableSendRegion = false;
        this.delayBroadcastTimeAI = new AtomicInteger(3000);
        this.address = null;
        this.send_packet = null;
        this.addressMulticast = null;
        this.sendPacketMulticast = null;
        this.mContext = null;
        this.send_data = new byte[128];
        this.buffer = new byte[ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED];
        this.packetMulticastByteArray = null;
        this.portIndex = 0;
        this.isChinessSSID = false;
        this.securityAesKey = null;
        this.startProvisionThread = null;
        this.startMulProvisionThread = null;
        this.startP2PThread = null;
        this.mP2PProvision = null;
        this.tokenByteLen = 3;
        this.cloudRandom = null;
        this.sendBssidByteLen = 3;
        this.getTokenWithBssid = true;
        this.getTokenBssid = null;
        this.testFlagByte = -1;
        this.appRandom = null;
        this.useAppTokenAB = new AtomicBoolean(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void UDP_SEND(int i) throws InterruptedException {
        try {
            this.portIndex = (this.portIndex + 1) % 10000;
            this.send_packet = new DatagramPacket(this.buffer, i, this.address, this.port + this.portIndex);
            this.udpSocket.send(this.send_packet);
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(TAG, "UDP_SEND(),send data fail");
        }
        Thread.sleep(this.INTERVAL_UDP_Packet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void closeSocket(DatagramSocket datagramSocket) {
        if (datagramSocket != null) {
            try {
                if (datagramSocket.isClosed()) {
                    return;
                }
                datagramSocket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private byte getFlagByte(boolean z) {
        byte bByteValue = (byte) (((byte) (new Byte((byte) 0).byteValue() | 8)) | 6);
        if (z) {
            bByteValue = (byte) (bByteValue | 1);
        }
        if (this.isChinessSSID) {
            bByteValue = (byte) (bByteValue | 32);
        }
        if (!this.enableSendRegion) {
            return bByteValue;
        }
        byte b2 = (byte) (bByteValue & 39);
        int i = this.testFlagByte;
        return (byte) (((byte) (b2 | ((i & 3) << 3))) | ((i & 12) << 4));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String getToken(String str, String str2) {
        String lastBssid = this.getTokenWithBssid ? StringUtils.getLastBssid(this.getTokenBssid, this.sendBssidByteLen) : "";
        String mDData = null;
        if (lastBssid == null) {
            ALog.w(TAG, "getTokenWithBssid=true, but get last bssid failed. no need check token.");
            return null;
        }
        if (str == null) {
            str = "";
        }
        if (str2 == null) {
            str2 = "";
        }
        String str3 = lastBssid + str + StringUtils.bytesToHexString(str2.getBytes());
        try {
            mDData = StringUtils.getMDData(StringUtils.hexStringTobytes(str3), MessageDigestAlgorithms.SHA_256);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        ALog.d(TAG, "enc=" + str3 + ", token=" + mDData);
        return mDData;
    }

    private boolean isSSidDataASCII(byte[] bArr) {
        if (bArr != null && bArr.length != 0) {
            for (byte b2 : bArr) {
                int i = b2 & 255;
                if (i < 32 || i > 126) {
                    return false;
                }
            }
        }
        return true;
    }

    private byte[] packSSID(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return null;
        }
        int i = 0;
        if (isSSidDataASCII(bArr)) {
            this.isChinessSSID = false;
            byte[] bArr2 = new byte[bArr.length];
            while (i < bArr.length) {
                bArr2[i] = (byte) ((bArr[i] & 255) - 32);
                i++;
            }
            return bArr2;
        }
        this.isChinessSSID = true;
        byte[] bArr3 = new byte[(bArr.length * 8) + 7];
        int i2 = 0;
        for (int i3 = 0; i3 < bArr.length; i3++) {
            int i4 = i2 + 1;
            bArr3[i2] = (byte) (bArr[i3] & 1);
            int i5 = i4 + 1;
            bArr3[i4] = (byte) ((bArr[i3] >> 1) & 1);
            int i6 = i5 + 1;
            bArr3[i5] = (byte) ((bArr[i3] >> 2) & 1);
            int i7 = i6 + 1;
            bArr3[i6] = (byte) ((bArr[i3] >> 3) & 1);
            int i8 = i7 + 1;
            bArr3[i7] = (byte) ((bArr[i3] >> 4) & 1);
            int i9 = i8 + 1;
            bArr3[i8] = (byte) ((bArr[i3] >> 5) & 1);
            int i10 = i9 + 1;
            bArr3[i9] = (byte) ((bArr[i3] >> 6) & 1);
            i2 = i10 + 1;
            bArr3[i10] = (byte) ((bArr[i3] >> 7) & 1);
        }
        int length = ((bArr.length * 8) + 5) / 6;
        byte[] bArr4 = new byte[length];
        while (i < length) {
            int i11 = i * 6;
            bArr4[i] = (byte) ((bArr3[i11 + 5] << 5) | bArr3[i11] | (bArr3[i11 + 1] << 1) | (bArr3[i11 + 2] << 2) | (bArr3[i11 + 3] << 3) | (bArr3[i11 + 4] << 4));
            i++;
        }
        return bArr4;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void packetData(String str, String str2) {
        byte[] bytes;
        int i;
        byte[] bArr = new byte[65];
        byte[] bArr2 = new byte[65];
        try {
            if (TextUtils.isEmpty(str2)) {
                bytes = str2.getBytes("UTF-8");
                for (int i2 = 0; i2 < bytes.length; i2++) {
                    bytes[i2] = (byte) ((bytes[i2] & 255) - 32);
                }
            } else {
                byte[] bArrEncrypt128CFB = AlinkAESHelper.encrypt128CFB(str2, this.securityAesKey);
                ALog.d(TAG, "packetData(), pwd encrypted data = ");
                AlinkWifiSolutionUtils.printByteArray(bArrEncrypt128CFB);
                bytes = AlinkWifiSolutionUtils.eightBitsToSixBits(bArrEncrypt128CFB);
                ALog.d(TAG, "packetData(), pwd encrypted 8->6 data = ");
                AlinkWifiSolutionUtils.printByteArray(bytes);
            }
            if (str == null || str.length() <= 0) {
                this.send_data[0] = (byte) (bytes.length + 5);
                this.send_data[1] = getFlagByte(false);
                this.send_data[2] = (byte) bytes.length;
                i = 3;
                int i3 = 0;
                while (i3 < bytes.length) {
                    this.send_data[i] = bytes[i3];
                    i3++;
                    i++;
                }
            } else {
                byte[] bytes2 = str.getBytes("UTF-8");
                String str3 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("ssid len = ");
                sb.append(bytes2.length);
                ALog.d(str3, sb.toString());
                byte[] bArrPackSSID = packSSID(bytes2);
                String str4 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("packed ssid len = ");
                sb2.append(bArrPackSSID.length);
                ALog.d(str4, sb2.toString());
                if (bArrPackSSID.length + bytes.length > 121) {
                    ALog.e(TAG, "SSID and passwd too long");
                    stopConfig();
                    return;
                }
                String str5 = TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("dataLen = ");
                sb3.append(bArrPackSSID.length + 6 + bytes.length);
                ALog.d(str5, sb3.toString());
                this.send_data[0] = (byte) (bArrPackSSID.length + 6 + bytes.length);
                this.send_data[1] = getFlagByte(true);
                this.send_data[2] = (byte) bArrPackSSID.length;
                i = 4;
                this.send_data[3] = (byte) bytes.length;
                int i4 = 0;
                while (i4 < bArrPackSSID.length) {
                    this.send_data[i] = bArrPackSSID[i4];
                    i4++;
                    i++;
                }
                int i5 = 0;
                while (i5 < bytes.length) {
                    this.send_data[i] = bytes[i5];
                    i5++;
                    i++;
                }
            }
            short s = 0;
            for (int i6 = 0; i6 < i; i6++) {
                s = (short) (s + (this.send_data[i6] & 255));
            }
            int i7 = i + 1;
            this.send_data[i] = (byte) ((s >> 6) & 63);
            int i8 = i7 + 1;
            this.send_data[i7] = (byte) (s & 63);
            int i9 = i8 - 2;
            if ((this.send_data[i9] & 255) == 0) {
                this.send_data[i9] = 1;
            }
            int i10 = i8 - 1;
            if ((this.send_data[i10] & 255) == 0) {
                this.send_data[i10] = 1;
            }
            String str6 = TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("send data i= ");
            sb4.append(i8);
            sb4.append(", sendDataLen=");
            sb4.append(this.send_data.length);
            ALog.d(str6, sb4.toString());
            String str7 = TAG;
            StringBuilder sb5 = new StringBuilder();
            sb5.append("send data = ");
            sb5.append(AlinkWifiSolutionUtils.bytesToHexString(this.send_data));
            ALog.i(str7, sb5.toString());
            AlinkWifiSolutionUtils.printByteArray(this.send_data);
        } catch (Exception e) {
            ALog.w(TAG, "packetData(),error = " + e.toString());
            e.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void packetMulticastData(String str, String str2) {
        int length;
        byte[] bArrHexStringTobytes;
        try {
            byte[] bytes = str.getBytes("UTF-8");
            int length2 = TextUtils.isEmpty(str2) ? 0 : str2.length() + 1;
            byte[] bArrEncrypt128CFB = null;
            if (length2 > 0 && (bArrEncrypt128CFB = AlinkAESHelper.encrypt128CFB(str2, this.securityAesKey)) == null) {
                throw new IllegalStateException("psw encrypt failed.");
            }
            this.getTokenBssid = WiFiUtils.getCurrentApBssid();
            byte[] bssidHexByteArray = StringUtils.getBssidHexByteArray(this.getTokenBssid);
            this.getTokenWithBssid = StringUtils.isValidBssid(this.getTokenBssid) && this.sendBssidByteLen > 0 && this.sendBssidByteLen < 7 && bssidHexByteArray != null && bssidHexByteArray.length > 0;
            int i = this.getTokenWithBssid ? this.sendBssidByteLen + 1 : 0;
            int length3 = bytes.length + 1;
            int i2 = 2;
            if (TextUtils.isEmpty(this.cloudRandom) || this.useAppTokenAB.get()) {
                this.useAppTokenAB.set(true);
                length = this.tokenByteLen > 0 ? this.tokenByteLen + 1 : 0;
            } else {
                this.useAppTokenAB.set(false);
                length = (this.cloudRandom.length() / 2) + 1;
            }
            int i3 = length2 + 2 + length + length3 + i + 1;
            this.packetMulticastByteArray = new byte[i3];
            this.packetMulticastByteArray[0] = (byte) (i3 & 255);
            byte b2 = length2 > 0 ? (byte) (-63) : (byte) -64;
            if (length > 0) {
                b2 = (byte) (b2 | 2);
            }
            if (length3 > 0) {
                b2 = (byte) (b2 | 4);
            }
            this.packetMulticastByteArray[1] = b2;
            if (length2 > 0) {
                this.packetMulticastByteArray[2] = (byte) (bArrEncrypt128CFB.length & 255);
                int i4 = 0;
                i2 = 3;
                while (i4 < bArrEncrypt128CFB.length) {
                    this.packetMulticastByteArray[i2] = (byte) (bArrEncrypt128CFB[i4] & 255);
                    i4++;
                    i2++;
                }
            }
            if (length > 0) {
                if (this.useAppTokenAB.get()) {
                    this.appRandom = StringUtils.getHexString(6);
                    bArrHexStringTobytes = StringUtils.hexStringTobytes(this.appRandom);
                } else {
                    bArrHexStringTobytes = StringUtils.hexStringTobytes(this.cloudRandom);
                }
                this.packetMulticastByteArray[i2] = (byte) (bArrHexStringTobytes.length & 255);
                int i5 = 0;
                i2++;
                while (i5 < bArrHexStringTobytes.length) {
                    this.packetMulticastByteArray[i2] = (byte) (bArrHexStringTobytes[i5] & 255);
                    i5++;
                    i2++;
                }
            }
            int i6 = i2 + 1;
            this.packetMulticastByteArray[i2] = (byte) (bytes.length & 255);
            int i7 = 0;
            while (i7 < bytes.length) {
                this.packetMulticastByteArray[i6] = (byte) (bytes[i7] & 255);
                i7++;
                i6++;
            }
            if (this.getTokenWithBssid) {
                this.packetMulticastByteArray[i6] = (byte) (this.sendBssidByteLen & 255);
                int length4 = bssidHexByteArray.length - this.sendBssidByteLen;
                i6++;
                while (length4 < bssidHexByteArray.length) {
                    this.packetMulticastByteArray[i6] = bssidHexByteArray[length4];
                    length4++;
                    i6++;
                }
            }
            short s = 0;
            for (int i8 = 0; i8 < i6; i8++) {
                s = (short) (s + (this.packetMulticastByteArray[i8] & 255));
            }
            this.packetMulticastByteArray[i6] = (byte) (s & Http2CodecUtil.MAX_UNSIGNED_BYTE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void provisioning(IConfigCallback iConfigCallback) {
        DCErrorCode dCErrorCode = this.provisionErrorInfo;
        if (dCErrorCode != null) {
            dCErrorCode.setSubcode(DCErrorCode.SUBCODE_PT_NO_CONNECTAP_NOTIFY_AND_CHECK_TOKEN_FAIL).setMsg("noConnectApOrCheckTokenSuccess");
        }
        new Thread(new RunnableC0483q(this, iConfigCallback)).start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void send() {
        if (this.isProvisioningAB.get() || this.provisionHasStopped.get()) {
            return;
        }
        this.isProvisioningAB.set(true);
        this.startProvisionThread = new Thread(new RunnableC0484r(this));
        if (this.enableMulticastSend) {
            this.startMulProvisionThread = new Thread(new RunnableC0485s(this));
            this.startMulProvisionThread.start();
        }
        this.startProvisionThread.start();
        if (this.provisionHasStopped.get() || this.mConfigParams == null) {
            return;
        }
        this.startP2PThread = new Thread(new RunnableC0486t(this));
        this.startP2PThread.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void sendMulticastUdpPacket(InetAddress inetAddress, int i) throws InterruptedException {
        try {
            this.portIndex = (this.portIndex + 1) % 10000;
            if (this.udpSocketMulticast != null) {
                this.sendPacketMulticast = new DatagramPacket(this.buffer, i, inetAddress, this.port + this.portIndex);
                this.udpSocketMulticast.send(this.sendPacketMulticast);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ALog.w(TAG, "UDP_SEND(), send multicast data fail");
        }
        Thread.sleep(this.INTERVAL_UDP_Packet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSendLevel(int i) {
        if (i == 0) {
            this.UDP_LEVEL = 0;
            this.INTERVAL_UDP_Loop = this.INTERVAL_UDP_Loop_LEVEL1;
            this.INTERVAL_UDP_Packet = this.INTERVAL_UDP_Packet_LEVEL0;
            this.INTERVAL_UDP_SENDING = this.INTERVAL_UDP_SENDING_LEVEL1;
            return;
        }
        if (i == 1) {
            this.UDP_LEVEL = 1;
            this.INTERVAL_UDP_Loop = this.INTERVAL_UDP_Loop_LEVEL1;
            this.INTERVAL_UDP_Packet = this.INTERVAL_UDP_Packet_LEVEL1;
            this.INTERVAL_UDP_SENDING = this.INTERVAL_UDP_SENDING_LEVEL1;
            return;
        }
        if (i != 2) {
            this.UDP_LEVEL = 2;
            this.INTERVAL_UDP_Loop = this.INTERVAL_UDP_Loop_LEVEL2;
            this.INTERVAL_UDP_Packet = this.INTERVAL_UDP_Packet_LEVEL2;
            this.INTERVAL_UDP_SENDING = this.INTERVAL_UDP_SENDING_LEVEL2;
            return;
        }
        this.UDP_LEVEL = 2;
        this.INTERVAL_UDP_Loop = this.INTERVAL_UDP_Loop_LEVEL2;
        this.INTERVAL_UDP_Packet = this.INTERVAL_UDP_Packet_LEVEL2;
        this.INTERVAL_UDP_SENDING = this.INTERVAL_UDP_SENDING_LEVEL2;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void continueConfig(Map map) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void doExtraPrepareWork(IConfigExtraCallback iConfigExtraCallback) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public String getProvisionType() {
        return LinkType.ALI_BROADCAST.getName();
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean hasExtraPrepareWork() {
        return false;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean isSupport() {
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public boolean needWiFiSsidPwd() {
        return true;
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void preConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) {
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void startConfig(IConfigCallback iConfigCallback, DCConfigParams dCConfigParams) throws Exception {
        ALog.d(TAG, "startConfig() called with: callback = [" + iConfigCallback + "], configParams = [" + dCConfigParams + "]");
        this.mConfigCallback = iConfigCallback;
        if (!(dCConfigParams instanceof DCAlibabaConfigParams)) {
            this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PARAM_ERROR_MSG, DCErrorCode.PF_PARAMS_ERROR).setMsg("configParams error:").setSubcode(DCErrorCode.SUBCODE_PE_PROVISION_PARAMS_ERROR);
            provisionResultCallback(null);
            return;
        }
        DCUserTrack.addTrackData(AlinkConstants.KEY_PROVISION_STARTED, "true");
        this.provisionErrorInfo = new DCErrorCode(DCErrorCode.PROVISION_TIMEOUT_MSG, DCErrorCode.PF_PROVISION_TIMEOUT);
        DCAlibabaConfigParams dCAlibabaConfigParams = (DCAlibabaConfigParams) dCConfigParams;
        this.mConfigParams = dCAlibabaConfigParams;
        this.provisionHasStopped.set(false);
        this.appRandom = null;
        this.cloudRandom = null;
        this.useAppTokenAB.set(false);
        if (AlinkHelper.isBatchBroadcast(this.mConfigParams)) {
            setCallbackOnce(false);
            stopBackupCheck();
        } else {
            startProvisionTimer();
        }
        addProvisionOverListener(new C0480n(this));
        if (TextUtils.isEmpty(this.mConfigParams.password) || !TextUtils.isEmpty(this.mConfigParams.productEncryptKey)) {
            this.securityAesKey = dCAlibabaConfigParams.productEncryptKey;
            provisioning(iConfigCallback);
            return;
        }
        this.securityAesKey = null;
        DCErrorCode dCErrorCode = this.provisionErrorInfo;
        if (dCErrorCode != null) {
            dCErrorCode.setSubcode(DCErrorCode.SUBCODE_PT_GET_CIPHER_TIMEOUT).setMsg("getCipherTimeout");
        }
        ThreadPool.submit(new RunnableC0482p(this, iConfigCallback));
    }

    @Override // com.aliyun.alink.business.devicecenter.config.IConfigStrategy
    public void stopConfig() {
        ALog.d(TAG, "stopProvison()");
        this.provisionHasStopped.set(true);
        cancelRequest(this.retryTransitoryClient);
        removeProvisionOverListener();
        this.provisionErrorInfo = null;
        stopProvisionTimer();
        this.appRandom = null;
        this.cloudRandom = null;
        try {
            if (this.isProvisioningAB.compareAndSet(true, false)) {
                if (this.startProvisionThread != null) {
                    this.startProvisionThread.interrupt();
                }
                if (this.startMulProvisionThread != null) {
                    this.startMulProvisionThread.interrupt();
                }
                if (this.startP2PThread != null) {
                    this.startP2PThread.interrupt();
                }
            }
        } catch (Exception unused) {
        }
        Z z = this.mP2PProvision;
        if (z != null) {
            z.g();
            this.mP2PProvision = null;
        }
        this.useAppTokenAB.set(false);
        stopBackupCheck();
    }

    public AlinkBroadcastConfigStrategy(Context context) {
        this.START_FLAG1 = 1248;
        this.START_FLAG2 = 1248;
        this.START_FLAG3 = 1248;
        this.UDP_PORT = DefaultLoadControl.DEFAULT_MAX_BUFFER_MS;
        this.MULTICAST_UDP_Addr_Prefix = "239.";
        this.UDP_Addr = "255.255.255.255";
        this.INTERVAL_UDP_SENDING_LEVEL1 = 10000L;
        this.INTERVAL_UDP_SENDING_LEVEL2 = 10000L;
        this.INTERVAL_UDP_Packet_LEVEL0 = 40L;
        this.INTERVAL_UDP_Packet_LEVEL1 = 20L;
        this.INTERVAL_UDP_Packet_LEVEL2 = 100L;
        this.INTERVAL_UDP_Loop_LEVEL1 = 200L;
        this.INTERVAL_UDP_Loop_LEVEL2 = 0L;
        this.isProvisioningAB = new AtomicBoolean(false);
        this.enableMulticastSend = true;
        this.enableSendRegion = false;
        this.delayBroadcastTimeAI = new AtomicInteger(3000);
        this.address = null;
        this.send_packet = null;
        this.addressMulticast = null;
        this.sendPacketMulticast = null;
        this.mContext = null;
        this.send_data = new byte[128];
        this.buffer = new byte[ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED];
        this.packetMulticastByteArray = null;
        this.portIndex = 0;
        this.isChinessSSID = false;
        this.securityAesKey = null;
        this.startProvisionThread = null;
        this.startMulProvisionThread = null;
        this.startP2PThread = null;
        this.mP2PProvision = null;
        this.tokenByteLen = 3;
        this.cloudRandom = null;
        this.sendBssidByteLen = 3;
        this.getTokenWithBssid = true;
        this.getTokenBssid = null;
        this.testFlagByte = -1;
        this.appRandom = null;
        this.useAppTokenAB = new AtomicBoolean(false);
        this.mContext = context;
        this.mP2PProvision = new Z(context);
    }
}
