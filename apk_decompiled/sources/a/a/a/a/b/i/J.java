package a.a.a.a.b.i;

import aisble.callback.DataReceivedCallback;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import b.InterfaceC0367a;
import com.alibaba.ailabs.iot.aisbase.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.callback.IConnectCallback;
import com.alibaba.ailabs.iot.mesh.provision.FastProvisionManager;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionConfigCallback;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionTransportCallback;
import com.alibaba.ailabs.iot.mesh.provision.state.AbsFastProvisionState$State;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNodeData;
import meshprovisioner.utils.MeshParserUtils;
import meshprovisioner.utils.SecureUtils;

/* JADX INFO: compiled from: FastProvisionWorker.java */
/* JADX INFO: loaded from: classes.dex */
@RequiresApi(api = 21)
public class J implements FastProvisionStatusCallback {
    public Runnable A;
    public final int B;
    public int C;
    public String D;
    public final DataReceivedCallback E;
    public final Runnable F;

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public String f1354a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Context f1355b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public byte[] f1356c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public byte[] f1357d;
    public String e;
    public byte[] f;
    public byte[] g;
    public byte[] h;
    public UnprovisionedMeshNodeData i;
    public UnprovisionedMeshNode j;
    public BluetoothDevice k;
    public BaseMeshNode l;
    public a.a.a.a.a.a.a.b.a m;
    public List<a.a.a.a.a.a.a.a.a> n;
    public a.a.a.a.b.i.c.a o;
    public FastProvisionConfigCallback p;
    public InterfaceC0367a q;
    public b.p r;
    public b.s s;
    public AbsFastProvisionState$State t;
    public FastProvisionTransportCallback u;
    public Handler v;
    public boolean w;
    public BroadcastReceiver x;
    public String y;
    public Runnable z;

    public J() {
        this.f1354a = FastProvisionManager.TAG;
        this.f = new byte[16];
        this.w = false;
        this.x = null;
        this.z = null;
        this.A = null;
        this.B = 2;
        this.C = 0;
        this.E = new z(this);
        this.F = new w(this);
        this.n = new ArrayList();
    }

    public final void j() {
        if (this.f1355b == null) {
            return;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Utils.ACTION_PROVISIONING_STATE);
        intentFilter.addAction(Utils.ACTION_BIND_STATE);
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this.f1355b);
        x xVar = new x(this);
        this.x = xVar;
        localBroadcastManager.registerReceiver(xVar, intentFilter);
    }

    public void k() {
        if (this.o != null) {
            a.a.a.a.b.m.a.c(this.f1354a, "Release transport layer.");
            this.o.a();
        }
        Runnable runnable = this.A;
        if (runnable != null) {
            this.v.removeCallbacks(runnable);
        }
    }

    public void l() {
        this.v.postDelayed(this.F, 10000L);
    }

    public void m() {
        a.a.a.a.b.i.c.a aVar = this.o;
        if (aVar == null || !(aVar instanceof a.a.a.a.b.i.c.g)) {
            return;
        }
        aVar.a();
    }

    public final void n() {
        Context context = this.f1355b;
        if (context == null || this.x == null) {
            return;
        }
        LocalBroadcastManager.getInstance(context).unregisterReceiver(this.x);
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onAddAppKeyMsgRespReceived(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.c(this.f1354a, "onAddAppKeyMsgRespReceived");
        this.t = AbsFastProvisionState$State.ADD_APP_KEY_RESP_RECEIVED;
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onAddAppKeyMsgSend(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.c(this.f1354a, "onAddAppKeyMsgSend");
        this.t = AbsFastProvisionState$State.ADD_APP_KEY_SEND;
        this.p.advertiseAppKey(provisionedMeshNode, new H(this));
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onBroadcastingRandoms(UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        a.a.a.a.b.m.a.c(this.f1354a, "onBroadcastingRandoms");
        this.t = AbsFastProvisionState$State.BROADCASTING_RANDOMS;
        this.i = unprovisionedMeshNodeData;
        if (unprovisionedMeshNodeData.isFastSupportGatt()) {
            return;
        }
        a(this.f1355b);
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onConfigInfoReceived(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.c(this.f1354a, "onConfigInfoReceived");
        this.t = AbsFastProvisionState$State.CONFIG_INFO_RECEIVED;
        this.p.advertiseAppKey(provisionedMeshNode, new G(this, provisionedMeshNode));
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onProvisionFailed(int i, String str) {
        a.a.a.a.b.m.a.c(this.f1354a, "onProvisionFailed: errorCode = " + i + ", errorMsg = " + str);
        this.t = AbsFastProvisionState$State.PROVISIONING_FAILED;
        b.p pVar = this.r;
        if (pVar != null) {
            pVar.onProvisioningFailed(this.j, i);
        }
        i();
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onReceiveConfirmationFromCloud(UnprovisionedMeshNodeData unprovisionedMeshNodeData, String str) {
        a.a.a.a.b.m.a.c(this.f1354a, "onReceiveConfirmationFromCloud");
        this.e = str;
        this.g = a(str);
        if (this.g == null) {
            a.a.a.a.b.m.a.b(this.f1354a, "failed to generate deviceKey");
            onProvisionFailed(-1, "failed to generate deviceKey");
            return;
        }
        this.t = AbsFastProvisionState$State.CONFIRMATION_CLOUD_RECEIVED;
        a.a.a.a.a.a.b.a.c cVar = new a.a.a.a.a.a.b.a.c(unprovisionedMeshNodeData.getMac(), this.f1356c, this.f1357d);
        if (unprovisionedMeshNodeData.isFastSupportGatt()) {
            a(this.f1355b);
        }
        this.o.a(cVar.a(), new A(this, unprovisionedMeshNodeData));
        int i = this.C;
        if (i >= 2) {
            if (this.i.isFastSupportGatt()) {
                h();
            }
        } else {
            this.C = i + 1;
            Handler handler = this.v;
            B b2 = new B(this, unprovisionedMeshNodeData, str);
            this.z = b2;
            handler.postDelayed(b2, 1500L);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onReceiveDeviceConfirmationFromDevice(UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr) {
        Runnable runnable = this.z;
        if (runnable != null) {
            this.v.removeCallbacks(runnable);
        }
        if (this.t.getState() >= AbsFastProvisionState$State.CONFIRMATION_DEVICE_RECEIVED.getState()) {
            a.a.a.a.b.m.a.b(this.f1354a, "duplicate CONFIRMATION_DEVICE_RECEIVED. skip");
            return;
        }
        this.t = AbsFastProvisionState$State.CONFIRMATION_DEVICE_RECEIVED;
        a.a.a.a.b.m.a.c(this.f1354a, "onReceiveDeviceConfirmationFromDevice: randomA" + ConvertUtils.bytes2HexString(this.f1356c) + ", randomB " + ConvertUtils.bytes2HexString(this.f1357d));
        a.a.a.a.b.m.c.a(this.f1354a, bArr);
        byte[] bArr2 = this.f;
        System.arraycopy(bArr, 3, bArr2, 0, bArr2.length);
        a(unprovisionedMeshNodeData, this.f, this.h, b(this.f1356c, this.f1357d), new C(this));
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onReceiveProvisionInfoRspFromDevice(UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr) {
        this.t = AbsFastProvisionState$State.PROVISION__CONFIG_RESP_RECEIVED;
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onReceiveSendProvisionDataRspFromDevice(UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr) {
        if (this.t.getState() >= AbsFastProvisionState$State.ENCRYPTED_PROVISION_MSG_RSP_RECEIVE.getState()) {
            a.a.a.a.b.m.a.b(this.f1354a, "duplicate ENCRYPTED_PROVISION_MSG_RSP_RECEIVE. skip");
            return;
        }
        this.t = AbsFastProvisionState$State.ENCRYPTED_PROVISION_MSG_RSP_RECEIVE;
        a.a.a.a.b.m.a.c(this.f1354a, "onReceiveSendProvisionDataRspFromDevice: " + ConvertUtils.bytes2HexString(bArr));
        ProvisionedMeshNode provisionedMeshNode = new ProvisionedMeshNode(this.j);
        provisionedMeshNode.setDeviceKey(this.g);
        int i = this.s.i();
        provisionedMeshNode.setUnicastAddress(new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)});
        this.l = provisionedMeshNode;
        if (unprovisionedMeshNodeData.isFastSupportGatt()) {
            this.r.onProvisioningComplete(provisionedMeshNode);
        } else {
            this.p.requestConfigMsg(provisionedMeshNode, new F(this, provisionedMeshNode));
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onReceiveVerifyResultFromCloud(UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        a.a.a.a.b.m.a.c(this.f1354a, "onReceiveVerifyResultFromCloud");
        this.t = AbsFastProvisionState$State.DATA_VERIFY_SUCCESS_FROM_CLOUD;
        byte[] byteArray = MeshParserUtils.toByteArray(this.s.h());
        a.a.a.a.b.m.a.c(this.f1354a, "networkKey: " + ConvertUtils.bytes2HexString(byteArray));
        byte bF = (byte) (this.s.f() & 255);
        int i = this.s.i();
        a.a.a.a.a.a.b.a.d dVar = new a.a.a.a.a.a.b.a.d(unprovisionedMeshNodeData.getMac(), (byte) 0, byteArray, bF, new byte[]{(byte) ((i >> 8) & 255), (byte) (i & 255)}, this.e);
        if (dVar.b()) {
            this.o.a(dVar.a(), new E(this));
        } else {
            onProvisionFailed(-1, "failed to generate encrypted provision data");
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onSendProvisionConfigInfo(UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.t = AbsFastProvisionState$State.PROVISION_CONFIG_SEND;
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onSendProvisionDataToDevice(UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.t = AbsFastProvisionState$State.ENCRYPTED_PROVISION_MSG_SEND;
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onSendRandomAndDeviceConfirmationToCloud(UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.t = AbsFastProvisionState$State.CONFIRMATION_DEVICE_SEND_TO_CLOUD;
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionStatusCallback
    public void onSendRandomToCloud(UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        a.a.a.a.b.m.a.c(this.f1354a, "onSendRandomToCloud");
        this.t = AbsFastProvisionState$State.CONFIRMATION_KEY_AND_RANDOM_SEND_TO_CLOUD;
    }

    public void b(byte[] bArr) {
        if (bArr.length < 3) {
            return;
        }
        byte b2 = bArr[0];
        if (b2 == 1) {
            onReceiveDeviceConfirmationFromDevice(this.i, bArr);
            return;
        }
        if (b2 == 3) {
            onReceiveSendProvisionDataRspFromDevice(this.i, bArr);
            return;
        }
        a.a.a.a.b.m.a.b(this.f1354a, "failed to handle " + ConvertUtils.bytes2HexString(bArr));
    }

    public final byte c() {
        b.s sVar = this.s;
        if (sVar == null) {
            return (byte) 0;
        }
        byte[] byteArray = MeshParserUtils.toByteArray(sVar.h());
        a.a.a.a.b.m.a.c(this.f1354a, "networkKey=" + ConvertUtils.bytes2HexString(byteArray));
        return SecureUtils.calculateK2(byteArray, SecureUtils.K2_MASTER_INPUT).getNid();
    }

    public final void d() {
        this.f1356c = SecureUtils.generateRandomNumber(64);
        this.f1357d = SecureUtils.generateRandomNumberWithSeedByNonaTime(64);
        byte[] bArrHexString2Bytes = ConvertUtils.hexString2Bytes(Integer.toHexString(hashCode()));
        Random random = new Random();
        if (bArrHexString2Bytes != null && bArrHexString2Bytes.length > 0) {
            this.f1356c[random.nextInt(8)] = bArrHexString2Bytes[random.nextInt(bArrHexString2Bytes.length)];
            this.f1357d[random.nextInt(8)] = bArrHexString2Bytes[random.nextInt(bArrHexString2Bytes.length)];
        }
        a(this.f1356c, this.f1357d, new y(this));
    }

    public boolean e() {
        return this.w;
    }

    public a.a.a.a.b.i.c.a f() {
        return this.o;
    }

    public final boolean g() {
        return this.t.getState() <= AbsFastProvisionState$State.PROVISIONING_COMPLETE.getState();
    }

    public final void h() {
        a.a.a.a.b.m.a.d(this.f1354a, "onInvalidConnectionMayHappen()");
        if (this.o != null) {
            a.a.a.a.b.m.a.c(this.f1354a, "Release transport layer.");
            this.o.a();
        }
        onProvisionFailed(-1, "Invalid connection may happen");
    }

    public final void i() {
        this.w = false;
        n();
    }

    public synchronized void a(UnprovisionedMeshNodeData unprovisionedMeshNodeData, BluetoothDevice bluetoothDevice, IConnectCallback iConnectCallback) {
        a.a.a.a.b.m.a.a(this.f1354a, "initTransportLayer supportGatt:" + unprovisionedMeshNodeData.isFastSupportGatt());
        if (a.a.a.a.b.d.a.f1316b || unprovisionedMeshNodeData.isFastSupportGatt()) {
            this.o = new a.a.a.a.b.i.c.r(this.y);
        } else {
            this.o = new a.a.a.a.b.i.c.g();
        }
        this.o.init(this.f1355b);
        this.o.a(bluetoothDevice, iConnectCallback);
        this.i = unprovisionedMeshNodeData;
        this.k = bluetoothDevice;
        d();
    }

    public final byte[] b(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public void b() {
        a.a.a.a.b.i.c.a aVar = this.o;
        if (aVar != null) {
            aVar.b();
        }
    }

    public J(String str) {
        this();
        this.f1354a += str;
        this.y = str;
    }

    public synchronized void a(Context context, InterfaceC0367a interfaceC0367a, b.s sVar, FastProvisionConfigCallback fastProvisionConfigCallback, FastProvisionTransportCallback fastProvisionTransportCallback, b.p pVar) {
        this.f1355b = context.getApplicationContext();
        this.v = new Handler(Looper.getMainLooper());
        a.a.a.a.a.g.c().a(this.f1355b);
        this.q = interfaceC0367a;
        this.s = sVar;
        this.p = fastProvisionConfigCallback;
        this.r = pVar;
        this.u = fastProvisionTransportCallback;
        this.t = AbsFastProvisionState$State.PROVISIONING_COMPLETE;
        this.n.clear();
        this.m = null;
    }

    public void a(UnprovisionedMeshNode unprovisionedMeshNode) {
        this.j = unprovisionedMeshNode;
        this.l = unprovisionedMeshNode;
    }

    public void a(UnprovisionedMeshNodeData unprovisionedMeshNodeData) {
        this.w = true;
        this.C = 0;
        j();
        this.i = unprovisionedMeshNodeData;
        this.v.removeCallbacks(this.F);
        m();
        String str = this.D;
        if (str == null || this.t != AbsFastProvisionState$State.PROVISIONING_COMPLETE) {
            return;
        }
        onReceiveConfirmationFromCloud(unprovisionedMeshNodeData, str);
    }

    public void a(Context context) {
        a.a.a.a.b.m.a.c(this.f1354a, "startScanDeviceAdvertise execute");
        a.a.a.a.b.i.c.a aVar = this.o;
        if (aVar != null) {
            aVar.a(this.E);
        }
    }

    public void a(IActionListener<Boolean> iActionListener) {
        a.a.a.a.b.i.c.a aVar = this.o;
        if (aVar != null) {
            aVar.a(iActionListener);
        }
    }

    public final synchronized void a(byte[] bArr) {
        a.a.a.a.b.m.a.c(this.f1354a, "assembleControlResp " + ConvertUtils.bytes2HexString(bArr));
        if (this.m == null) {
            a.a.a.a.b.m.a.b(this.f1354a, "There is no controlMsg");
            return;
        }
        a.a.a.a.a.a.a.a.a aVarA = a.a.a.a.a.a.a.a.a.a(bArr);
        if (aVarA == null) {
            a.a.a.a.b.m.a.b(this.f1354a, "failed to parse " + ConvertUtils.bytes2HexString(bArr));
            return;
        }
        byte bC = c();
        if (aVarA.d() != bC) {
            a.a.a.a.b.m.a.b(this.f1354a, "network id not equal, abandon. Expect " + ((int) bC) + ", receive " + ((int) this.m.d()));
            return;
        }
        if (this.n.isEmpty()) {
            this.n.add(aVarA);
            a();
        } else {
            a.a.a.a.a.a.a.a.a aVar = this.n.get(0);
            if (aVar.c() != aVarA.c()) {
                a.a.a.a.b.m.a.b(this.f1354a, "clear old cache ...");
                this.n.clear();
                this.n.add(aVarA);
                a();
            } else {
                if (aVar.e() != aVarA.e()) {
                    a.a.a.a.b.m.a.b(this.f1354a, "total package number illegal, expect " + aVar.e() + ", receive " + aVarA.e());
                    return;
                }
                Iterator<a.a.a.a.a.a.a.a.a> it = this.n.iterator();
                while (it.hasNext()) {
                    if (it.next().a() == aVarA.a()) {
                        a.a.a.a.b.m.a.c(this.f1354a, "index duplicate");
                        return;
                    }
                }
                this.n.add(aVarA);
                a();
            }
        }
    }

    public final void a() {
        a.a.a.a.a.a.a.a.a aVar = this.n.get(0);
        a.a.a.a.b.m.a.c(this.f1354a, "checkControlBufferAndSend, expect " + aVar.e() + ", current " + this.n.size());
        Collections.sort(this.n, new I(this));
        if (aVar.e() == this.n.size()) {
            Iterator<a.a.a.a.a.a.a.a.a> it = this.n.iterator();
            int length = 0;
            while (it.hasNext()) {
                byte[] bArrB = it.next().b();
                if (bArrB != null) {
                    length += bArrB.length;
                }
            }
            ByteBuffer byteBufferAllocate = ByteBuffer.allocate(length + 1);
            byteBufferAllocate.put((byte) 0);
            Iterator<a.a.a.a.a.a.a.a.a> it2 = this.n.iterator();
            while (it2.hasNext()) {
                byte[] bArrB2 = it2.next().b();
                if (bArrB2 != null) {
                    byteBufferAllocate.put(bArrB2);
                }
            }
            this.u.onReceiveFastProvisionData(this.l, byteBufferAllocate.array());
        }
    }

    public final byte[] a(String str) {
        try {
            byte[] bArrCalculateSha256 = SecureUtils.calculateSha256((str + "DeviceKey").getBytes("ASCII"));
            if (bArrCalculateSha256 == null || bArrCalculateSha256.length < 16) {
                return null;
            }
            byte[] bArr = new byte[16];
            System.arraycopy(bArrCalculateSha256, 0, bArr, 0, bArr.length);
            return bArr;
        } catch (UnsupportedEncodingException | UnsupportedCharsetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public final void a(byte[] bArr, byte[] bArr2, InterfaceC0367a.b bVar) {
        a.a.a.a.b.m.a.c(this.f1354a, "sendRandomToCloud:");
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr2, 0, bArr3, 0, bArr2.length);
        System.arraycopy(bArr, 0, bArr3, bArr2.length, bArr.length);
        this.h = a(bArr, bArr2);
        byte[] bArr4 = this.h;
        if (bArr4 == null) {
            a.a.a.a.b.m.a.b(this.f1354a, "failed to generate confirmationKey");
            onProvisionFailed(-1, "failed to generate confirmationKey");
            return;
        }
        InterfaceC0367a interfaceC0367a = this.q;
        if (interfaceC0367a != null) {
            interfaceC0367a.generateConfirmationValue(this.i, bArr4, bArr3, bVar);
            onSendRandomToCloud(this.i);
        } else {
            a.a.a.a.b.m.a.b(this.f1354a, "cloudConfirmationProvisioningCallbacks is null");
            onProvisionFailed(-1, "cloudConfirmationProvisioningCallbacks is null");
        }
    }

    public final void a(UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr, byte[] bArr2, byte[] bArr3, InterfaceC0367a.InterfaceC0176a interfaceC0176a) {
        InterfaceC0367a interfaceC0367a = this.q;
        if (interfaceC0367a != null) {
            interfaceC0367a.checkConfirmationValueMatches(this.j, unprovisionedMeshNodeData, bArr, bArr2, bArr3, interfaceC0176a);
        } else {
            a.a.a.a.b.m.a.b(this.f1354a, "cloudConfirmationProvisioningCallbacks is null");
            onProvisionFailed(-1, "cloudConfirmationProvisioningCallbacks is null");
        }
    }

    public final byte[] a(byte[] bArr, byte[] bArr2) {
        try {
            byte[] bytes = (ConvertUtils.bytes2HexString(bArr).toLowerCase() + ConvertUtils.bytes2HexString(bArr2).toLowerCase() + "ConfirmationKey").getBytes("ASCII");
            String str = this.f1354a;
            StringBuilder sb = new StringBuilder();
            sb.append("confirmationBytes: ");
            sb.append(ConvertUtils.bytes2HexString(bytes));
            a.a.a.a.b.m.a.c(str, sb.toString());
            byte[] bArrCalculateSha256 = SecureUtils.calculateSha256(bytes);
            if (bArrCalculateSha256 == null || bArrCalculateSha256.length < 16) {
                return null;
            }
            byte[] bArr3 = new byte[16];
            System.arraycopy(bArrCalculateSha256, 0, bArr3, 0, 16);
            a.a.a.a.b.m.a.c(this.f1354a, "" + ConvertUtils.bytes2HexString(bArr3));
            return bArr3;
        } catch (UnsupportedEncodingException | UnsupportedCharsetException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void a(BaseMeshNode baseMeshNode, byte[] bArr) {
        byte[] bArr2;
        a.a.a.a.b.m.a.c(this.f1354a, "before split package " + ConvertUtils.bytes2HexString(bArr));
        this.l = baseMeshNode;
        if (bArr.length >= 1) {
            bArr2 = new byte[bArr.length - 1];
            System.arraycopy(bArr, 1, bArr2, 0, bArr2.length);
        } else {
            bArr2 = new byte[bArr.length];
            System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
        }
        this.m = new a.a.a.a.a.a.a.b.a(bArr2, c(), new v(this, baseMeshNode, bArr));
        a.a.a.a.a.g.c().b(this.f1355b);
        a.a.a.a.a.g.c().a(this.m);
    }
}
