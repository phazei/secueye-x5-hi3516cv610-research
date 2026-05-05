package com.alibaba.ailabs.iot.mesh;

import a.a.a.a.b.A;
import a.a.a.a.b.B;
import a.a.a.a.b.C;
import a.a.a.a.b.C0327b;
import a.a.a.a.b.C0330e;
import a.a.a.a.b.C0331f;
import a.a.a.a.b.C0332g;
import a.a.a.a.b.C0333h;
import a.a.a.a.b.C0334i;
import a.a.a.a.b.C0351j;
import a.a.a.a.b.C0355n;
import a.a.a.a.b.C0357p;
import a.a.a.a.b.C0358q;
import a.a.a.a.b.C0359s;
import a.a.a.a.b.C0362v;
import a.a.a.a.b.C0363w;
import a.a.a.a.b.D;
import a.a.a.a.b.E;
import a.a.a.a.b.F;
import a.a.a.a.b.G;
import a.a.a.a.b.RunnableC0352k;
import a.a.a.a.b.RunnableC0353l;
import a.a.a.a.b.RunnableC0354m;
import a.a.a.a.b.RunnableC0356o;
import a.a.a.a.b.RunnableC0360t;
import a.a.a.a.b.RunnableC0361u;
import a.a.a.a.b.RunnableC0364x;
import a.a.a.a.b.RunnableC0365y;
import a.a.a.a.b.RunnableC0366z;
import a.a.a.a.b.i.C0335a;
import a.a.a.a.b.i.C0336b;
import a.a.a.a.b.i.J;
import a.a.a.a.b.i.u;
import a.a.a.a.b.l.c;
import a.a.a.a.b.na;
import a.a.a.a.b.oa;
import a.a.a.a.b.r;
import aisble.BleManager;
import aisscanner.BluetoothLeScannerCompat;
import aisscanner.ScanCallback;
import aisscanner.ScanFilter;
import aisscanner.ScanRecord;
import aisscanner.ScanSettings;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.SparseArray;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import b.C0378l;
import b.InterfaceC0367a;
import b.InterfaceC0379m;
import b.K;
import b.e.i;
import b.p;
import b.q;
import b.s;
import b.u;
import com.alibaba.ailabs.iot.aisbase.scanner.BLEScannerProxy;
import com.alibaba.ailabs.iot.mesh.MeshService;
import com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice;
import com.alibaba.ailabs.iot.mesh.bean.MeshNodeStatus;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManager;
import com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.contant.MeshUtConst$MeshErrorEnum;
import com.alibaba.ailabs.iot.mesh.delegate.OnReadyToBindHandler;
import com.alibaba.ailabs.iot.mesh.managers.MeshDeviceInfoManager;
import com.alibaba.ailabs.iot.mesh.provision.WiFiConfigReplyParser;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionConfigCallback;
import com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionTransportCallback;
import com.alibaba.ailabs.iot.mesh.ut.UtError;
import com.alibaba.ailabs.iot.mesh.utils.AliMeshUUIDParserUtil;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import com.alibaba.ailabs.tg.utils.ConvertUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.taobao.accs.net.b;
import datasource.bean.AddPublish;
import datasource.bean.BindModel;
import datasource.bean.ConfigurationData;
import datasource.bean.DeviceStatus;
import datasource.bean.IotDevice;
import datasource.bean.ProvisionAppKey;
import datasource.bean.ProvisionInfo;
import datasource.bean.ProvisionNetKey;
import datasource.bean.SigmeshKey;
import datasource.bean.SubscribeGroupAddr;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import meshprovisioner.BaseMeshNode;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNodeData;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes.dex */
public class DeviceProvisioningWorker implements BleMeshManagerCallbacks, p, InterfaceC0379m, InterfaceC0367a, FastProvisionConfigCallback, FastProvisionTransportCallback, q {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final Semaphore f2788a = new Semaphore(f());
    public MeshService.OnDisconnectListener A;
    public OnReadyToBindHandler C;
    public q D;
    public a E;
    public ExtendedBluetoothDevice G;
    public boolean H;
    public boolean J;
    public boolean O;
    public J P;
    public Runnable S;
    public C0336b U;
    public OnProvisionFinishedListener W;
    public Map<String, Object> Y;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public Context f2790c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public BleMeshManager f2791d;
    public C0378l e;
    public boolean f;
    public s m;
    public BluetoothDevice q;
    public ScanRecord r;
    public Handler s;
    public SparseArray<SigmeshKey> t;
    public List<BindModel> u;
    public List<Integer> v;
    public List<Integer> w;
    public List<SubscribeGroupAddr> x;
    public List<AddPublish> y;
    public UnprovisionedMeshNodeData z;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public String f2789b = DeviceProvisioningWorker.class.getSimpleName();
    public boolean g = false;
    public boolean h = false;
    public BaseMeshNode i = null;
    public boolean j = false;
    public boolean k = false;
    public boolean l = false;
    public ConcurrentLinkedQueue<Integer> n = new ConcurrentLinkedQueue<>();
    public ConcurrentLinkedQueue<String> o = new ConcurrentLinkedQueue<>();
    public boolean p = false;
    public boolean B = false;
    public int F = 0;
    public List<String> I = new ArrayList();
    public SparseArray<Integer> K = new SparseArray<>();
    public SparseArray<Byte> L = new SparseArray<>();
    public boolean M = true;
    public boolean N = false;
    public CountDownLatch Q = null;
    public AtomicBoolean R = new AtomicBoolean(false);
    public volatile boolean T = false;
    public boolean V = true;
    public u X = null;
    public Runnable Z = null;
    public final int aa = b.ACCS_RECEIVE_TIMEOUT;
    public WiFiConfigReplyParser ba = null;
    public final Runnable ca = new RunnableC0356o(this);
    public final Runnable da = new RunnableC0365y(this);
    public final Runnable ea = new RunnableC0366z(this);
    public final ScanCallback fa = new A(this);

    public interface OnProvisionFinishedListener {
        boolean OnProvisionFinished(BluetoothDevice bluetoothDevice, boolean z);
    }

    private class a implements Runnable {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public String f2792a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public int f2793b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public ProvisionedMeshNode f2794c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public boolean f2795d;

        public a(String str, int i, ProvisionedMeshNode provisionedMeshNode, boolean z) {
            this.f2792a = str;
            this.f2793b = i;
            this.f2794c = provisionedMeshNode;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (DeviceProvisioningWorker.this.F >= 2) {
                if (this.f2795d) {
                    DeviceProvisioningWorker.this.onAppKeyStatusReceived(this.f2794c, true, 0, 0, this.f2793b);
                    return;
                }
                return;
            }
            a.a.a.a.b.m.a.c(DeviceProvisioningWorker.this.f2789b, "retry to add app key: appKeyIndex = " + this.f2793b + ", mAppKey = " + this.f2792a);
            DeviceProvisioningWorker.this.e.a(this.f2794c, this.f2793b, this.f2792a);
            DeviceProvisioningWorker deviceProvisioningWorker = DeviceProvisioningWorker.this;
            deviceProvisioningWorker.F = deviceProvisioningWorker.F + 1;
            this.f2795d = DeviceProvisioningWorker.this.F >= 2;
            DeviceProvisioningWorker.this.s.postDelayed(this, 500L);
            a.a.a.a.b.m.a.a(DeviceProvisioningWorker.this.f2789b, "addAppKey");
        }
    }

    public DeviceProvisioningWorker(Context context, q qVar, SparseArray<SigmeshKey> sparseArray, byte[] bArr, OnReadyToBindHandler onReadyToBindHandler, C0336b c0336b) {
        this.C = null;
        this.P = null;
        this.f2790c = context;
        this.D = qVar;
        this.t = sparseArray;
        this.C = onReadyToBindHandler;
        this.f2789b += (hashCode() % 1000000);
        this.u = new ArrayList();
        this.s = new Handler(Looper.getMainLooper());
        this.f2791d = new BleMeshManager(this.f2790c, String.valueOf(hashCode() % 1000000));
        this.f2791d.setGattCallbacks(this);
        this.e = new C0378l(this.f2790c);
        this.e.a((InterfaceC0379m) this);
        this.e.a((p) this);
        this.e.a((InterfaceC0367a) this);
        this.e.h(bArr);
        this.e.a((q) this);
        this.m = this.e.d();
        this.U = c0336b;
        if (Build.VERSION.SDK_INT >= 21) {
            this.P = new J(String.valueOf(hashCode() % 1000000));
            this.P.a(this.f2790c, this, this.m, this, this, this);
        }
        this.K.put(13936641, 13959592);
        this.K.put(13543425, 13435304);
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionConfigCallback
    public void advertiseAppKey(ProvisionedMeshNode provisionedMeshNode, IActionListener<Boolean> iActionListener) {
        b(MeshNodeStatus.COMPOSITION_DATA_STATUS_RECEIVED.getState());
        if (this.p) {
            this.p = false;
            this.s.postDelayed(new RunnableC0352k(this, provisionedMeshNode), 500L);
        }
    }

    @Override // b.InterfaceC0367a
    public void checkConfirmationValueMatches(UnprovisionedMeshNode unprovisionedMeshNode, UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr, byte[] bArr2, byte[] bArr3, InterfaceC0367a.InterfaceC0176a interfaceC0176a) {
        a.a.a.a.b.m.a.a(this.f2789b, "checkConfirmationValueMatches");
        this.i = unprovisionedMeshNode;
        this.z = unprovisionedMeshNodeData;
        String deviceMac = unprovisionedMeshNodeData.getDeviceMac();
        int productId = unprovisionedMeshNodeData.getProductId();
        String lowerCase = MeshParserUtils.bytesToHex(bArr3, false).toLowerCase();
        String strBytesToHex = MeshParserUtils.bytesToHex(bArr2, false);
        String lowerCase2 = MeshParserUtils.bytesToHex(bArr, false).toLowerCase();
        String strBytesToHex2 = unprovisionedMeshNodeData.getDeviceUuid() != null ? MeshParserUtils.bytesToHex(unprovisionedMeshNodeData.getDeviceUuid(), false) : "";
        na.a().a(deviceMac, strBytesToHex, lowerCase, lowerCase2, productId + "", strBytesToHex2, new C0334i(this, interfaceC0176a));
    }

    @Override // b.InterfaceC0367a
    public void generateConfirmationValue(UnprovisionedMeshNodeData unprovisionedMeshNodeData, byte[] bArr, byte[] bArr2, InterfaceC0367a.b bVar) {
        a.a.a.a.b.m.a.a(this.f2789b, "generateConfirmationValue");
        this.z = unprovisionedMeshNodeData;
        String deviceMac = unprovisionedMeshNodeData.getDeviceMac();
        String strBytesToHex = MeshParserUtils.bytesToHex(bArr2, false);
        na.a().a(deviceMac, strBytesToHex.toLowerCase(), MeshParserUtils.bytesToHex(bArr, false), unprovisionedMeshNodeData.getProductId() + "", this.z.getDeviceUuid() != null ? MeshParserUtils.bytesToHex(this.z.getDeviceUuid(), false) : "", new C0333h(this, bVar, unprovisionedMeshNodeData));
    }

    @Override // b.q
    public ProvisionedMeshNode getMeshNode(byte[] bArr, byte[] bArr2) {
        return (ProvisionedMeshNode) G.a().d().a(bArr, bArr2);
    }

    @Override // b.InterfaceC0379m
    public int getMtu() {
        int mtuSize = a.a.a.a.b.d.a.f1317c ? 35 : this.f2791d.getMtuSize();
        a.a.a.a.b.m.a.a(this.f2789b, "getMtu, MtuSize: " + mtuSize);
        return mtuSize;
    }

    @Override // b.q
    public void onAppKeyAddSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "ProvisionedMeshNode, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.SENDING_APP_KEY_ADD.getState());
    }

    @Override // b.q
    public void onAppKeyBindSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onAppKeyBindSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.APP_BIND_SENT.getState());
    }

    @Override // b.q
    public void onAppKeyBindStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, int i, int i2, int i3, int i4) {
        a.a.a.a.b.m.a.a(this.f2789b, "onAppKeyBindStatusReceived, ProvisionedMeshNode" + provisionedMeshNode.getNodeName() + ", status:" + i);
        if (!z) {
            a(MeshUtConst$MeshErrorEnum.APPKEY_BIND_ERROR, "appKey bind status: " + i);
            return;
        }
        if (!this.o.isEmpty()) {
            a(provisionedMeshNode);
            return;
        }
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_CONFIGURATION_STATE, MeshNodeStatus.APP_BIND_STATUS_RECEIVED.getState());
        intent.putExtra(Utils.EXTRA_IS_SUCCESS, z);
        intent.putExtra(Utils.EXTRA_STATUS, i);
        intent.putExtra(Utils.EXTRA_ELEMENT_ADDRESS, i2);
        intent.putExtra("EXTRA_APP_KEY_INDEX", i3);
        intent.putExtra(Utils.EXTRA_MODEL_ID, i4);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
        this.s.postDelayed(new RunnableC0353l(this, provisionedMeshNode, i2, i3), 500L);
    }

    @Override // b.q
    public void onAppKeyStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, int i, int i2, int i3) {
        a.a.a.a.b.m.a.a(this.f2789b, "onAppKeyStatusReceived, ProvisionedMeshNode" + provisionedMeshNode.getNodeName() + ", status: " + i);
        this.s.removeCallbacks(this.E);
        this.E = null;
        this.F = 0;
        if (!z) {
            a(MeshUtConst$MeshErrorEnum.APPKEY_ADD_ERROR, "appKey add status: " + i);
            return;
        }
        SigmeshKey sigmeshKey = this.t.get(i2);
        if (sigmeshKey != null && sigmeshKey.getProvisionAppKeys() != null) {
            Iterator<ProvisionAppKey> it = sigmeshKey.getProvisionAppKeys().iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                ProvisionAppKey next = it.next();
                if (next.getAppKeyIndex() == i3) {
                    provisionedMeshNode.setAddedAppKey(i3, next.getAppKey());
                    break;
                }
            }
        }
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_CONFIGURATION_STATE, MeshNodeStatus.APP_KEY_STATUS_RECEIVED.getState());
        intent.putExtra(Utils.EXTRA_STATUS, i);
        intent.putExtra(Utils.EXTRA_IS_SUCCESS, z);
        intent.putExtra("EXTRA_APP_KEY_INDEX", i2);
        intent.putExtra("EXTRA_APP_KEY_INDEX", i3);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
        a.a.a.a.b.m.a.a(this.f2789b, "size of appKey Queue: " + this.o.size());
        if (this.u.size() == 0) {
            if (this.o.isEmpty()) {
                a(provisionedMeshNode.getUnicastAddress(), (DeviceStatus) null);
                return;
            } else {
                a(provisionedMeshNode);
                return;
            }
        }
        BindModel bindModel = this.u.get(0);
        if (bindModel == null || bindModel.getModelElementAddr() == null) {
            return;
        }
        Integer modelElementAddr = bindModel.getModelElementAddr();
        this.w = bindModel.getModelIds();
        if (modelElementAddr == null || this.w == null) {
            return;
        }
        a(provisionedMeshNode, AddressUtils.getUnicastAddressBytes(modelElementAddr.intValue()), i3, this.w);
    }

    @Override // b.q
    public void onAppKeyUnbindSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onAppKeyUnbindSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.APP_UNBIND_SENT.getState());
    }

    @Override // aisble.BleManagerCallbacks
    public void onBatteryValueReceived(BluetoothDevice bluetoothDevice, int i) {
        a.a.a.a.b.m.a.a(this.f2789b, "onBatteryValueReceived...");
    }

    @Override // b.q
    public void onBlockAcknowledgementReceived(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onBlockAcknowledgementReceived, ProvisionedMeshNode: " + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.BLOCK_ACKNOWLEDGEMENT_RECEIVED.getState());
    }

    @Override // b.q
    public void onBlockAcknowledgementSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onBlockAcknowledgementSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.SENDING_BLOCK_ACKNOWLEDGEMENT.getState());
    }

    @Override // aisble.BleManagerCallbacks
    public void onBonded(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(this.f2789b, "onBonded...");
    }

    @Override // aisble.BleManagerCallbacks
    public void onBondingFailed(BluetoothDevice bluetoothDevice) {
    }

    @Override // aisble.BleManagerCallbacks
    public void onBondingRequired(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(this.f2789b, "onBondingRequired...");
    }

    @Override // b.q
    public void onCommonMessageStatusReceived(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, String str, byte[] bArr2, a.a.a.a.b.h.a aVar) {
        int i = Integer.parseInt(str, 16);
        byte[] opCodeBytes = Utils.getOpCodeBytes(i);
        String strConvertDevIdToIotId = MeshDeviceInfoManager.getInstance().convertDevIdToIotId(provisionedMeshNode.getDevId());
        a.a.a.a.b.m.a.a(this.f2789b, String.format("Common message status received from %s, opcode: %s, parameters: %s", strConvertDevIdToIotId, str, ConvertUtils.bytes2HexString(bArr2)));
        TgMeshManager.getInstance().notifyMeshMessage(bArr, str, bArr2, provisionedMeshNode.getNetworkKey(), provisionedMeshNode.getSequenceNumber(), strConvertDevIdToIotId);
        a(bArr, str, bArr2);
        Integer num = this.K.get(i);
        if (num != null) {
            Byte b2 = this.L.get(Utils.byteArray2Int(bArr));
            Byte bValueOf = null;
            if (bArr2 != null && bArr2.length > 0) {
                bValueOf = Byte.valueOf(bArr2[0]);
            }
            if (bValueOf != null) {
                if (b2 != null && b2.byteValue() == bValueOf.byteValue()) {
                    return;
                }
                this.L.put(Utils.byteArray2Int(bArr), bValueOf);
                byte[] opCodeBytes2 = Utils.getOpCodeBytes(num.intValue());
                byte[] bArr3 = {bArr2[0]};
                String str2 = this.m.c().get(0);
                a.a.a.a.b.m.a.c(this.f2789b, "Ack: opcode(" + ConvertUtils.bytes2HexString(opCodeBytes2) + "), parameter(" + ConvertUtils.bytes2HexString(bArr3) + ")");
                if (this.M) {
                    Map<Integer, String> addedAppKeys = provisionedMeshNode.getAddedAppKeys();
                    if (addedAppKeys != null) {
                        int iIntValue = addedAppKeys.keySet().iterator().next().intValue();
                        String str3 = addedAppKeys.get(Integer.valueOf(iIntValue));
                        K kE = G.a().d().h(provisionedMeshNode.getNetworkKey()).e();
                        if (kE != null) {
                            kE.g().a(provisionedMeshNode, true, str3, bArr, false, iIntValue, Utils.byteArray2Int(opCodeBytes2), bArr3);
                        } else {
                            a.a.a.a.b.m.a.b(this.f2789b, "subnets is null");
                            this.e.a(provisionedMeshNode, true, str3, bArr, false, iIntValue, Utils.byteArray2Int(opCodeBytes2), bArr3);
                        }
                    }
                } else {
                    this.e.a(provisionedMeshNode, true, str2, bArr, false, 0, Utils.byteArray2Int(opCodeBytes2), bArr3);
                }
            }
        }
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("opcode", (Object) Utils.bytes2HexString(opCodeBytes));
        jSONObject.put("parameters", (Object) MeshParserUtils.bytesToHex(bArr2, false));
        String jSONString = jSONObject.toJSONString();
        int unicastAddressInt = AddressUtils.getUnicastAddressInt(bArr);
        jSONObject.put("srcAddr", (Object) Integer.valueOf(unicastAddressInt));
        Intent intent = new Intent(Utils.ACTION_COMMON_MESSAGE_STATUS_RECEIVED);
        intent.putExtra(Utils.EXTRA_STATUS, jSONObject.toJSONString());
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
        DeviceStatus deviceStatus = new DeviceStatus();
        deviceStatus.setUserId(na.a().b());
        String strC = na.a().c();
        deviceStatus.setUuid(strC);
        deviceStatus.setUnicastAddress(unicastAddressInt);
        deviceStatus.setStatus(jSONString);
        deviceStatus.setIotId(strConvertDevIdToIotId);
        c.a(unicastAddressInt, 2, false);
        na.a().a(strC, Collections.singletonList(deviceStatus), new C0355n(this, unicastAddressInt));
    }

    @Override // b.q
    public void onCompositionDataStatusReceived(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onCompositionDataStatusReceived, ProvisionedMeshNode: " + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.COMPOSITION_DATA_STATUS_RECEIVED.getState());
        if (this.p) {
            this.p = false;
            String strPoll = this.o.poll();
            if (strPoll == null) {
                a.a.a.a.b.m.a.d(this.f2789b, "Empty appKey queue");
                return;
            }
            Integer numPoll = this.n.poll();
            Integer num = numPoll == null ? 0 : numPoll;
            a.a.a.a.b.m.a.c(this.f2789b, "try to add app key: appKeyIndex = " + num + ", mAppKey = " + strPoll);
            Handler handler = this.s;
            a aVar = new a(strPoll, num.intValue(), provisionedMeshNode, false);
            this.E = aVar;
            handler.postDelayed(aVar, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
            this.e.a(provisionedMeshNode, num.intValue(), strPoll);
            a.a.a.a.b.m.a.a(this.f2789b, "addAppKey");
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks
    public void onDataReceived(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        String str = this.f2789b;
        StringBuilder sb = new StringBuilder();
        sb.append("onDataReceived, device: ");
        sb.append(bluetoothDevice.getName());
        sb.append(", mac: ");
        sb.append(bluetoothDevice.getAddress());
        sb.append(", mtu: ");
        sb.append(i);
        sb.append(", pdu length: ");
        sb.append(bArr == null ? 0 : bArr.length);
        a.a.a.a.b.m.a.a(str, sb.toString());
        BaseMeshNode baseMeshNode = this.i;
        if (baseMeshNode == null) {
            a.a.a.a.b.m.a.d(this.f2789b, "provision mesh node is null");
        } else {
            this.e.a(baseMeshNode, i, bArr, (a.a.a.a.b.h.a) null);
        }
    }

    @Override // com.alibaba.ailabs.iot.mesh.ble.BleMeshManagerCallbacks
    public void onDataSent(BluetoothDevice bluetoothDevice, int i, byte[] bArr) {
        String str = this.f2789b;
        StringBuilder sb = new StringBuilder();
        sb.append("onDataSent, device: ");
        sb.append(bluetoothDevice.getName());
        sb.append(", mac: ");
        sb.append(bluetoothDevice.getAddress());
        sb.append(", mtu: ");
        sb.append(i);
        sb.append(", pdu length: ");
        sb.append(bArr == null ? 0 : bArr.length);
        a.a.a.a.b.m.a.a(str, sb.toString());
        BaseMeshNode baseMeshNode = this.i;
        if (baseMeshNode == null) {
            a.a.a.a.b.m.a.d(this.f2789b, "provision mesh node is null");
        } else {
            this.e.a(baseMeshNode, i, bArr);
        }
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceConnected(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(this.f2789b, "onDeviceConnected to device: " + bluetoothDevice.getName());
        if (this.H) {
            return;
        }
        this.f2791d.refreshGattCacheImmediately();
        o();
        this.q = bluetoothDevice;
        this.f = true;
        if (this.O) {
            a(true, (String) null);
        }
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceConnecting(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(this.f2789b, "onDeviceConnecting...");
        b(this.f2790c.getString(R.string.state_connecting));
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceDisconnected(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(this.f2789b, "onDeviceDisconnected...");
        j();
        a(false, UtError.MESH_DISCONNECT.getMsg());
        b(this.f2790c.getString(R.string.state_disconnected));
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceDisconnecting(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(this.f2789b, "onDeviceDisconnecting...");
        this.f = false;
        b(this.f2790c.getString(R.string.state_disconnecting));
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceNotSupported(BluetoothDevice bluetoothDevice) {
        String str;
        a.a.a.a.b.m.a.b(this.f2789b, "onDeviceNotSupported...");
        if (this.g) {
            return;
        }
        Intent intent = new Intent(Utils.ACTION_PROVISIONING_STATE);
        intent.putExtra(Utils.EXTRA_PROVISIONING_STATE, MeshNodeStatus.REQUEST_FAILED.getState());
        MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.DEVICE_NOT_SUPPORT_ERROR;
        intent.putExtra(Utils.EXTRA_REQUEST_FAIL_MSG, meshUtConst$MeshErrorEnum.getErrorMsg());
        if (this.z == null) {
            str = "";
        } else {
            str = this.z.getProductId() + "";
        }
        a.a.a.a.b.m.b.a("ALSMesh", "ble", str, false, this.r.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID)), "", 0L, meshUtConst$MeshErrorEnum.getErrorCode(), meshUtConst$MeshErrorEnum.getErrorMsg());
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    @Override // aisble.BleManagerCallbacks
    public void onDeviceReady(BluetoothDevice bluetoothDevice) {
        BaseMeshNode baseMeshNode;
        a.a.a.a.b.m.a.a(this.f2789b, "onDeviceReady...");
        if (this.B) {
            a.a.a.a.b.m.a.d(this.f2789b, "onDeviceReady, But User terminated the process");
            return;
        }
        Intent intent = new Intent(Utils.ACTION_ON_DEVICE_READY);
        intent.putExtra(Utils.EXTRA_DATA, true);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
        if (this.O) {
            return;
        }
        if (!this.f2791d.isProvisioningComplete()) {
            a.a.a.a.b.m.a.a(this.f2789b, "Provisioning Not Complete");
            this.k = true;
            this.g = false;
            this.h = false;
            i();
            return;
        }
        if (this.h || (baseMeshNode = this.i) == null) {
            a.a.a.a.b.m.a.a(this.f2789b, "Configuration Not Complete");
            return;
        }
        baseMeshNode.setBluetoothDeviceAddress(bluetoothDevice.getAddress());
        BaseMeshNode baseMeshNode2 = this.i;
        if (baseMeshNode2 == null || baseMeshNode2.getDeviceKey() == null || this.i.getUnicastAddress() == null) {
            return;
        }
        this.j = true;
        this.p = true;
        a.a.a.a.b.m.a.a(this.f2789b, "getCompositionData");
        b("getCompositionData");
        onCompositionDataStatusReceived((ProvisionedMeshNode) this.i);
    }

    @Override // aisble.BleManagerCallbacks
    public void onError(BluetoothDevice bluetoothDevice, String str, int i) {
        a.a.a.a.b.m.a.b(this.f2789b, "onError: " + str + ", provision finished ? " + this.T);
        if (this.T || BleManager.BleManagerGattCallback.ERROR_WRITE_CHARACTERISTIC.equals(str) || BleManager.BleManagerGattCallback.ERROR_WRITE_DESCRIPTOR.equalsIgnoreCase(str)) {
            return;
        }
        a(MeshUtConst$MeshErrorEnum.CALLBACK_ERROR, str);
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionTransportCallback
    public void onFastProvisionDataSend(BaseMeshNode baseMeshNode, byte[] bArr) {
        a.a.a.a.b.m.a.c(this.f2789b, "onFastProvisionDataSend: " + ConvertUtils.bytes2HexString(bArr));
        this.e.a(baseMeshNode, 18, bArr);
    }

    @Override // b.q
    public void onGenericLevelGetSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onGenericLevelGetSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
    }

    @Override // b.q
    public void onGenericLevelSetUnacknowledgedSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onGenericLevelSetUnacknowledgedSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
    }

    @Override // b.q
    public void onGenericLevelStatusReceived(ProvisionedMeshNode provisionedMeshNode, int i, int i2, int i3, int i4) {
        a.a.a.a.b.m.a.a(this.f2789b, "onGenericLevelStatusReceived, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        Intent intent = new Intent(Utils.ACTION_GENERIC_LEVEL_STATE);
        intent.putExtra(Utils.EXTRA_GENERIC_PRESENT_STATE, i);
        intent.putExtra(Utils.EXTRA_GENERIC_TARGET_STATE, i2);
        intent.putExtra(Utils.EXTRA_GENERIC_TRANSITION_STEPS, i3);
        intent.putExtra(Utils.EXTRA_GENERIC_TRANSITION_RES, i4);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    @Override // b.q
    public void onGenericOnOffGetSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onGenericOnOffGetSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
    }

    @Override // b.q
    public void onGenericOnOffSetUnacknowledgedSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onGenericOnOffSetUnacknowledgedSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        Intent intent = new Intent(Utils.ACTION_GENERIC_STATE);
        intent.putExtra(Utils.EXTRA_GENERIC_ON_OFF_SET_UNACK, "");
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    @Override // b.q
    public void onGenericOnOffStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, Boolean bool, int i, int i2) {
        a.a.a.a.b.m.a.a(this.f2789b, "onGenericOnOffStatusReceived, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        Intent intent = new Intent(Utils.ACTION_GENERIC_ON_OFF_STATE);
        intent.putExtra(Utils.EXTRA_GENERIC_PRESENT_STATE, z);
        intent.putExtra(Utils.EXTRA_GENERIC_TARGET_STATE, bool);
        intent.putExtra(Utils.EXTRA_GENERIC_TRANSITION_STEPS, i);
        intent.putExtra(Utils.EXTRA_GENERIC_TRANSITION_RES, i2);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    @Override // b.q
    public void onGetCompositionDataSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onGetCompositionDataSent, ProvisionedMeshNode: " + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.COMPOSITION_DATA_GET_SENT.getState());
    }

    @Override // aisble.BleManagerCallbacks
    public void onLinkLossOccurred(BluetoothDevice bluetoothDevice) {
        J j;
        a.a.a.a.b.m.a.a(this.f2789b, "onLinklossOccur...");
        this.f2791d.close();
        if (Build.VERSION.SDK_INT >= 21 && (j = this.P) != null) {
            j.a(new F(this));
            this.P.b();
        }
        a(false, UtError.MESH_LINK_LOSS_OCCURRED.getMsg());
        this.H = false;
        this.g = false;
        this.h = false;
        b(this.f2790c.getString(R.string.state_linkloss_occur));
        j();
    }

    @Override // b.q
    public void onMeshNodeResetSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onMeshNodeResetSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_DATA_NODE_RESET, "");
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    @Override // b.q
    public void onMeshNodeResetStatusReceived(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onMeshNodeResetStatusReceived, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.NODE_RESET_STATUS_RECEIVED.getState());
    }

    @Override // b.p
    public void onProvisioningAuthenticationInputRequested(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningAuthenticationInputRequested, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_AUTHENTICATION_INPUT_WAITING.getState());
    }

    @Override // b.p
    public void onProvisioningCapabilitiesReceived(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningCapabilitiesReceived, meshNode: " + unprovisionedMeshNode.getNodeName());
        c();
        this.g = false;
        this.h = false;
        this.e.a(unprovisionedMeshNode);
        a.a.a.a.b.m.a.a(this.f2789b, "startProvisioning");
        c(MeshNodeStatus.PROVISIONING_CAPABILITIES.getState());
    }

    @Override // b.p
    public void onProvisioningComplete(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningComplete, provisionedMeshNode: " + provisionedMeshNode.getNodeName());
        if (this.B) {
            a.a.a.a.b.m.a.d(this.f2789b, "onProvisioningComplete, But user terminated the process");
            return;
        }
        a.a.a.a.b.m.a.a(this.f2789b, "provision complete device isSupportGatt:" + provisionedMeshNode.getSupportFastGattProvision());
        provisionedMeshNode.setDevId(MeshParserUtils.bytesToHex(this.z.getDeviceUuid(), false).toLowerCase());
        G.a().d().a(provisionedMeshNode, true, true);
        this.i = provisionedMeshNode;
        n();
        a(provisionedMeshNode, new C0331f(this));
    }

    @Override // b.p
    public void onProvisioningConfirmationReceived(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningConfirmationReceived, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_CONFIRMATION_RECEIVED.getState());
        c();
    }

    @Override // b.p
    public void onProvisioningConfirmationSent(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningConfirmationSent, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_CONFIRMATION_SENT.getState());
        a(2000);
    }

    @Override // b.p
    public void onProvisioningDataSent(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningDataSent, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_DATA_SENT.getState());
    }

    @Override // b.p
    public void onProvisioningFailed(UnprovisionedMeshNode unprovisionedMeshNode, int i) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningFailed, meshNode: " + unprovisionedMeshNode.getNodeName());
        if (this.T) {
            return;
        }
        this.g = false;
        a(MeshUtConst$MeshErrorEnum.ILLEGAL_PROVISION_DATA_RECEIVED, "inner error code: " + i);
    }

    @Override // b.p
    public void onProvisioningInputCompleteSent(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningInputCompleteSent, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_INPUT_COMPLETE.getState());
    }

    @Override // b.p
    public void onProvisioningInviteSent(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningInviteSent, meshNode: " + unprovisionedMeshNode.getNodeName());
        this.i = unprovisionedMeshNode;
        c(MeshNodeStatus.PROVISIONING_INVITE.getState());
        a(2000);
    }

    @Override // b.p
    public void onProvisioningPublicKeyReceived(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningPublicKeyReceived, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_PUBLIC_KEY_RECEIVED.getState());
    }

    @Override // b.p
    public void onProvisioningPublicKeySent(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningPublicKeySent, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_PUBLIC_KEY_SENT.getState());
    }

    @Override // b.p
    public void onProvisioningRandomReceived(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningRandomReceived, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_RANDOM_RECEIVED.getState());
    }

    @Override // b.p
    public void onProvisioningRandomSent(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningRandomSent, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_RANDOM_SENT.getState());
    }

    @Override // b.p
    public void onProvisioningStartSent(UnprovisionedMeshNode unprovisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisioningStartSent, meshNode: " + unprovisionedMeshNode.getNodeName());
        c(MeshNodeStatus.PROVISIONING_START.getState());
    }

    @Override // b.q
    public void onPublicationSetSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onPublicationSetSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.PUBLISH_ADDRESS_SET_SENT.getState());
    }

    @Override // b.q
    public void onPublicationStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, int i, byte[] bArr, byte[] bArr2, int i2) {
        a.a.a.a.b.m.a.a(this.f2789b, "onPublicationStatusReceived, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_CONFIGURATION_STATE, MeshNodeStatus.PUBLISH_ADDRESS_STATUS_RECEIVED.getState());
        intent.putExtra(Utils.EXTRA_IS_SUCCESS, z);
        intent.putExtra(Utils.EXTRA_STATUS, i);
        intent.putExtra(Utils.EXTRA_ELEMENT_ADDRESS, bArr);
        intent.putExtra(Utils.EXTRA_PUBLISH_ADDRESS, bArr2);
        intent.putExtra(Utils.EXTRA_MODEL_ID, i2);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
        this.s.postDelayed(new RunnableC0354m(this, provisionedMeshNode, bArr), 500L);
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionTransportCallback
    public void onReceiveFastProvisionData(BaseMeshNode baseMeshNode, byte[] bArr) {
        a.a.a.a.b.m.a.c(this.f2789b, "onReceiveFastProvisionData " + ConvertUtils.bytes2HexString(bArr));
        BaseMeshNode baseMeshNode2 = this.i;
        if (baseMeshNode2 == null) {
            return;
        }
        baseMeshNode2.setIsProvisioned(true);
        if (this.i == null) {
            return;
        }
        this.H = false;
        this.f = true;
        this.e.a(baseMeshNode, 18, bArr, (a.a.a.a.b.h.a) null);
    }

    @Override // aisble.BleManagerCallbacks
    public void onServicesDiscovered(@NonNull BluetoothDevice bluetoothDevice, boolean z) {
        a.a.a.a.b.m.a.a(this.f2789b, "onServicesDiscovered...");
        b(this.f2790c.getString(R.string.state_initializing));
        if (this.H) {
            this.H = false;
        } else if (this.O) {
            d();
        } else if (this.B) {
            a.a.a.a.b.m.a.d(this.f2789b, "onServicesDiscovered, But User terminated the process");
        }
    }

    @Override // b.q
    public void onSubscriptionAddSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onSubscriptionAddSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.SUBSCRIPTION_ADD_SENT.getState());
    }

    @Override // b.q
    public void onSubscriptionDeleteSent(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onSubscriptionDeleteSent, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        b(MeshNodeStatus.SUBSCRIPTION_DELETE_SENT.getState());
    }

    @Override // b.q
    public void onSubscriptionStatusReceived(ProvisionedMeshNode provisionedMeshNode, boolean z, int i, byte[] bArr, byte[] bArr2, int i2) {
        a.a.a.a.b.m.a.a(this.f2789b, "onSubscriptionStatusReceived, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        if (!this.g || this.h) {
            return;
        }
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_CONFIGURATION_STATE, MeshNodeStatus.SUBSCRIPTION_STATUS_RECEIVED.getState());
        intent.putExtra(Utils.EXTRA_IS_SUCCESS, z);
        intent.putExtra(Utils.EXTRA_STATUS, i);
        intent.putExtra(Utils.EXTRA_ELEMENT_ADDRESS, bArr);
        intent.putExtra(Utils.EXTRA_PUBLISH_ADDRESS, bArr2);
        intent.putExtra(Utils.EXTRA_MODEL_ID, i2);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
        List<SubscribeGroupAddr> list = this.x;
        if (list == null || list.size() <= 0) {
            a(provisionedMeshNode.getUnicastAddress(), (DeviceStatus) null);
            return;
        }
        SubscribeGroupAddr subscribeGroupAddrRemove = this.x.remove(0);
        if (subscribeGroupAddrRemove == null || subscribeGroupAddrRemove.getGroupAddr() == null || subscribeGroupAddrRemove.getModelId() == null) {
            return;
        }
        Integer groupAddr = subscribeGroupAddrRemove.getGroupAddr();
        this.e.a(provisionedMeshNode, bArr, new byte[]{(byte) ((groupAddr.intValue() >> 8) & 255), (byte) (groupAddr.intValue() & 255)}, subscribeGroupAddrRemove.getModelId().intValue());
    }

    @Override // b.q
    public void onTransactionFailed(ProvisionedMeshNode provisionedMeshNode, int i, boolean z) {
        a.a.a.a.b.m.a.a(this.f2789b, "onTransactionFailed, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        Intent intent = new Intent(Utils.ACTION_TRANSACTION_STATE);
        intent.putExtra(Utils.EXTRA_ELEMENT_ADDRESS, i);
        intent.putExtra(Utils.EXTRA_DATA, z);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    @Override // b.q
    public void onUnknownPduReceived(ProvisionedMeshNode provisionedMeshNode) {
        a.a.a.a.b.m.a.a(this.f2789b, "onUnknownPduReceived, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
    }

    @Override // b.q
    public void onVendorModelMessageStatusReceived(ProvisionedMeshNode provisionedMeshNode, byte[] bArr) {
        a.a.a.a.b.m.a.a(this.f2789b, "onVendorModelMessageStatusReceived, ProvisionedMeshNode" + provisionedMeshNode.getNodeName());
        Intent intent = new Intent(Utils.ACTION_VENDOR_MODEL_MESSAGE_STATE);
        intent.putExtra(Utils.EXTRA_DATA, bArr);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionConfigCallback
    public void requestConfigMsg(ProvisionedMeshNode provisionedMeshNode, IActionListener<Boolean> iActionListener) {
        provisionedMeshNode.setDevId(MeshParserUtils.bytesToHex(this.z.getDeviceUuid(), false));
        G.a().d().a(provisionedMeshNode, true, true);
        this.i = provisionedMeshNode;
        a(provisionedMeshNode, new C0351j(this, iActionListener));
    }

    @Override // com.alibaba.ailabs.iot.mesh.provision.callback.FastProvisionConfigCallback
    public void requestProvisionMsg(ScanRecord scanRecord) {
        a.a.a.a.b.m.a.a(this.f2789b, "requestProvisionMsg...");
        b(this.f2790c.getString(R.string.state_initializing));
        if (this.H) {
            this.H = false;
            return;
        }
        if (this.O) {
            d();
            return;
        }
        byte[] serviceData = this.r.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID));
        UnprovisionedMeshNodeData unprovisionedMeshNodeData = new UnprovisionedMeshNodeData(serviceData);
        a(serviceData, unprovisionedMeshNodeData, unprovisionedMeshNodeData.getDeviceMac(), unprovisionedMeshNodeData.getProductId() + "");
        this.g = false;
        this.h = false;
    }

    @Override // b.InterfaceC0379m
    @RequiresApi(api = 18)
    public void sendPdu(BaseMeshNode baseMeshNode, byte[] bArr) {
        J j;
        String str = this.f2789b;
        StringBuilder sb = new StringBuilder();
        sb.append("sendPdu, meshNode: ");
        sb.append(baseMeshNode.getNodeName());
        sb.append(", mac: ");
        sb.append(baseMeshNode.getBluetoothDeviceAddress());
        sb.append(", pdu length: ");
        sb.append(bArr == null ? 0 : bArr.length);
        a.a.a.a.b.m.a.a(str, sb.toString());
        if (!baseMeshNode.getSupportFastProvision() || Build.VERSION.SDK_INT < 21) {
            this.f2791d.sendPdu(bArr);
            c.a(baseMeshNode.getUnicastAddressInt(), "0");
            return;
        }
        if (this.f2791d.getConnectState() == 2) {
            this.f2791d.sendPdu(bArr);
        } else if (a.a.a.a.b.d.a.f1315a && (j = this.P) != null) {
            j.a(baseMeshNode, bArr);
        }
        c.a(baseMeshNode.getUnicastAddressInt(), "1");
    }

    @Override // aisble.BleManagerCallbacks
    public boolean shouldEnableBatteryLevelNotifications(BluetoothDevice bluetoothDevice) {
        a.a.a.a.b.m.a.a(this.f2789b, "shouldEnableBatteryLevelNotifications...");
        return false;
    }

    public static int f() {
        if ("SM-N9760".equals(Build.MODEL) || "SM-G975U1".equals(Build.MODEL) || "SM-A6060".equals(Build.MODEL)) {
            return 3;
        }
        return "Pixel 5".equals(Build.MODEL) ? 2 : 1;
    }

    public final int g() {
        return "VOG-AL00".equals(Build.MODEL) ? 3000 : 7000;
    }

    public String h() {
        ExtendedBluetoothDevice extendedBluetoothDevice;
        UnprovisionedMeshNodeData unprovisionedMeshNodeData = this.z;
        String lowerCase = (unprovisionedMeshNodeData == null || TextUtils.isEmpty(unprovisionedMeshNodeData.getDeviceMac())) ? null : this.z.getDeviceMac().toLowerCase();
        return (!TextUtils.isEmpty(lowerCase) || (extendedBluetoothDevice = this.G) == null || extendedBluetoothDevice.getAddress() == null) ? lowerCase : this.G.getAddress().toLowerCase();
    }

    public final void i() {
        a.a.a.a.b.m.a.a(this.f2789b, "handle device ready event in provisioning step, provisioning info ready flag: " + this.l);
        if (this.l) {
            a.a.a.a.b.m.a.a(this.f2789b, "identifyNode after device(GATT connected or adv) is ready");
            new Thread(new E(this)).start();
        }
    }

    public final void j() {
        BleMeshManager bleMeshManager = this.f2791d;
        if (!(bleMeshManager == null ? this.g : bleMeshManager.isProvisioningComplete())) {
            this.f = false;
        } else if (this.H) {
            Intent intent = new Intent(Utils.ACTION_IS_RECONNECTING);
            intent.putExtra(Utils.EXTRA_DATA, true);
            LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
            a.a.a.a.b.m.a.a(this.f2789b, "deviceTobeProvision:" + this.G);
            b(this.G, true);
        } else if (this.h) {
            this.f = false;
        }
        MeshService.OnDisconnectListener onDisconnectListener = this.A;
        if (onDisconnectListener != null) {
            onDisconnectListener.onDisconnected();
            this.A = null;
        }
    }

    public final boolean k() {
        ScanRecord scanRecord = this.r;
        if (scanRecord != null) {
            return AliMeshUUIDParserUtil.isComboMesh(scanRecord.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID)));
        }
        Map<String, Object> map = this.Y;
        return map != null && map.containsKey("ssid");
    }

    public final boolean l() {
        ExtendedBluetoothDevice extendedBluetoothDevice = this.G;
        if (extendedBluetoothDevice == null || this.U == null) {
            return false;
        }
        return extendedBluetoothDevice.getAddress().equalsIgnoreCase(this.U.a());
    }

    public final void m() {
        BaseMeshNode baseMeshNode;
        J j;
        this.T = true;
        G.a().d().a((ProvisionedMeshNode) this.i, true, true);
        if (k()) {
            a.a.a.a.b.m.a.a(this.f2789b, "Detect current is combo device");
            b((ProvisionedMeshNode) this.i);
            return;
        }
        u.a aVarD = G.a().d().d();
        if (aVarD == null) {
            p();
            return;
        }
        K kE = aVarD.e();
        if (kE == null) {
            kE = new K(this.f2790c, aVarD, this.D);
            aVarD.a(kE);
        }
        boolean zA = false;
        if (this.G.getAddress().equalsIgnoreCase(this.U.a())) {
            a.a.a.a.b.m.a.c(this.f2789b, "Hit last task, attach connection info to subnetsBiz");
            zA = kE.a(this.f2791d, (ProvisionedMeshNode) this.i, this.P);
        }
        if (zA) {
            this.N = true;
            BLEScannerProxy.getInstance().unlock();
        } else {
            p();
        }
        this.f2791d = new BleMeshManager(this.f2790c);
        if (this.N || (baseMeshNode = this.i) == null || !baseMeshNode.getSupportFastGattProvision() || Build.VERSION.SDK_INT < 21 || (j = this.P) == null) {
            return;
        }
        j.a(new C0359s(this));
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x006d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void n() {
        /*
            r5 = this;
            java.lang.String r0 = r5.f2789b
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "prepareToReconnect blemesh is:"
            r1.append(r2)
            com.alibaba.ailabs.iot.mesh.ble.BleMeshManager r2 = r5.f2791d
            r3 = 0
            r4 = 1
            if (r2 == 0) goto L14
            r2 = r4
            goto L15
        L14:
            r2 = r3
        L15:
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            a.a.a.a.b.m.a.a(r0, r1)
            r5.H = r4
            com.alibaba.ailabs.iot.mesh.ble.BleMeshManager r0 = r5.f2791d
            r0.setProvisioningComplete(r4)
            meshprovisioner.BaseMeshNode r0 = r5.i
            if (r0 == 0) goto L86
            boolean r0 = r0.getSupportFastGattProvision()
            if (r0 == 0) goto L86
            java.lang.String r0 = r5.f2789b
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "deviceTobeProvision:"
            r1.append(r2)
            com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice r2 = r5.G
            r1.append(r2)
            java.lang.String r1 = r1.toString()
            a.a.a.a.b.m.a.a(r0, r1)
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 21
            r2 = 0
            if (r0 < r1) goto L6d
            a.a.a.a.b.i.J r0 = r5.P
            if (r0 == 0) goto L6d
            r0.b()
            a.a.a.a.b.i.J r0 = r5.P
            a.a.a.a.b.i.c.a r0 = r0.f()
            boolean r0 = r0 instanceof a.a.a.a.b.i.c.r
            if (r0 == 0) goto L6d
            a.a.a.a.b.i.J r0 = r5.P
            a.a.a.a.b.i.c.a r0 = r0.f()
            a.a.a.a.b.i.c.r r0 = (a.a.a.a.b.i.c.r) r0
            com.alibaba.ailabs.iot.mesh.ble.BleMeshManager r0 = r0.d()
            goto L6e
        L6d:
            r0 = r2
        L6e:
            if (r0 == 0) goto L7e
            r0.setProvisioningComplete(r4)
            r0.setGattCallbacks(r5)
            r5.f2791d = r0
            com.alibaba.ailabs.iot.mesh.ble.BleMeshManager r0 = r5.f2791d
            r0.discoveryServices(r3)
            goto L83
        L7e:
            com.alibaba.ailabs.iot.mesh.bean.ExtendedBluetoothDevice r0 = r5.G
            r5.b(r0, r4)
        L83:
            r5.A = r2
            goto L95
        L86:
            meshprovisioner.BaseMeshNode r0 = r5.i
            if (r0 == 0) goto L95
            int r0 = android.os.Build.VERSION.SDK_INT
            r1 = 18
            if (r0 < r1) goto L95
            com.alibaba.ailabs.iot.mesh.ble.BleMeshManager r0 = r5.f2791d
            r0.discoveryServices(r3)
        L95:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ailabs.iot.mesh.DeviceProvisioningWorker.n():void");
    }

    public final void o() {
        if (this.V && this.R.get()) {
            ExtendedBluetoothDevice extendedBluetoothDevice = this.G;
            if (extendedBluetoothDevice != null && extendedBluetoothDevice.getDevice() != null && this.z.isSupportLargeScaleMeshNetwork()) {
                C0327b.b().a(this.G.getDevice(), true);
            }
            a.a.a.a.b.m.a.c(this.f2789b, "Thread(" + Thread.currentThread().getName() + ") release global connection semaphore");
            f2788a.release();
            this.R.set(false);
        }
    }

    public final void p() {
        a.a.a.a.b.m.a.c(this.f2789b, "Reset...");
        this.n.clear();
        this.o.clear();
        this.O = false;
        if (!this.N) {
            if (this.f2791d.getConnectState() == 2) {
                a.a.a.a.b.m.a.a(this.f2789b, "mBleMeshManager.getConnectState() == STATE_CONNECTED");
                this.f2791d.disconnectImmediately();
            } else {
                this.f2791d.disconnect().enqueue();
                this.f2791d.close();
            }
            if (this.P != null) {
                a.a.a.a.b.m.a.a(this.f2789b, "mFastProvisionWorker.reset()");
                this.P.k();
            }
            a.a.a.a.b.i.u uVar = this.X;
            if (uVar != null) {
                uVar.c();
            }
        }
        this.s.removeCallbacks(this.da);
        this.s.removeCallbacks(this.E);
        this.H = false;
        this.g = false;
        this.h = false;
        this.e.g();
        C0327b.b().b(this.f2791d);
        if (l()) {
            BLEScannerProxy.getInstance().unlock();
        }
    }

    public final void q() {
        a.a.a.a.b.m.a.a(this.f2789b, "startScan...");
        this.J = true;
        ScanSettings scanSettingsBuild = new ScanSettings.Builder().setScanMode(1).setReportDelay(0L).setUseHardwareFilteringIfSupported(true).build();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new ScanFilter.Builder().setServiceUuid(new ParcelUuid(BleMeshManager.MESH_PROXY_UUID)).build());
        if (Utils.isBleEnabled()) {
            try {
                BluetoothLeScannerCompat.getScanner().startScan(arrayList, scanSettingsBuild, this.fa);
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.s.postDelayed(this.ea, 60000L);
        }
    }

    public void r() {
        a.a.a.a.b.m.a.c(this.f2789b, "stop() called " + this.O);
        if (this.O) {
            return;
        }
        this.s.removeCallbacks(this.ca);
        if (this.G != null && !this.g) {
            this.e.g();
        }
        this.B = true;
        if (this.f) {
            e();
        }
    }

    public final void s() {
        a.a.a.a.b.m.a.a(this.f2789b, "stopScan...");
        this.s.removeCallbacks(this.ea);
        if (Utils.isBleEnabled()) {
            BluetoothLeScannerCompat.getScanner().stopScan(this.fa);
        }
        this.J = false;
    }

    public final void d() {
        BaseMeshNode baseMeshNode = this.i;
        if (baseMeshNode == null || !baseMeshNode.getSupportFastProvision()) {
            byte[] bArr = {0};
            SigmeshKey sigmeshKey = this.t.get(0);
            if (sigmeshKey == null || sigmeshKey.getProvisionNetKey() == null) {
                return;
            }
            UnprovisionedMeshNode unprovisionedMeshNode = new UnprovisionedMeshNode();
            unprovisionedMeshNode.setIvIndex(ByteBuffer.allocate(4).putInt(this.m.f()).array());
            unprovisionedMeshNode.setNetworkKey(MeshParserUtils.toByteArray(sigmeshKey.getProvisionNetKey().getNetKey()));
            unprovisionedMeshNode.setConfigurationSrc(this.e.b());
            ProvisionedMeshNode provisionedMeshNode = new ProvisionedMeshNode(unprovisionedMeshNode);
            int size = this.m.c().size();
            for (int i = 0; i < size; i++) {
                provisionedMeshNode.setAddedAppKey(i, this.m.c().get(i));
            }
            provisionedMeshNode.setIsProvisioned(true);
            provisionedMeshNode.setConfigured(true);
            this.e.a(provisionedMeshNode, 0, bArr);
            this.s.postDelayed(new r(this, provisionedMeshNode), 500L);
        }
    }

    public final void e() {
        s();
        p();
        a.a.a.a.b.m.a.a(this.f2789b, "disconnect");
    }

    public final void c(String str) {
        Intent intent = new Intent(Utils.ACTION_PROVISIONED_NODE_FOUND);
        intent.putExtra(Utils.EXTRA_DATA, str);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    public final void b(ExtendedBluetoothDevice extendedBluetoothDevice) {
        a.a.a.a.b.m.a.a(this.f2789b, "onProvisionedDeviceFound...");
        b(extendedBluetoothDevice, true);
    }

    public final void b(ExtendedBluetoothDevice extendedBluetoothDevice, boolean z) {
        J j;
        G.a().d().g();
        if (!z) {
            a();
        }
        if (!z) {
            this.s.removeCallbacks(this.ca);
            this.i = null;
        }
        if (extendedBluetoothDevice == null) {
            a.a.a.a.b.m.a.b(this.f2789b, "device is null");
            return;
        }
        a.a.a.a.b.m.a.a(this.f2789b, "Start for device: " + extendedBluetoothDevice.getAddress());
        if (this.z.isSupportLargeScaleMeshNetwork()) {
            C0327b.b().a(extendedBluetoothDevice.getDevice());
        }
        a.a.a.a.b.m.a.a(this.f2789b, "connect to device: " + extendedBluetoothDevice.getAddress() + ", isProvisioned：" + z);
        boolean z2 = false;
        if (!z) {
            this.G = extendedBluetoothDevice;
            this.B = false;
            this.H = false;
            this.g = false;
            this.h = false;
            this.l = false;
            this.k = false;
            this.j = false;
            this.n.clear();
            this.o.clear();
        }
        this.f2791d.setProvisioningComplete(z);
        ScanRecord scanRecord = this.r;
        if (scanRecord != null) {
            byte[] serviceData = scanRecord.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID));
            a.a.a.a.b.m.a.a(this.f2789b, "service data: " + Utils.bytes2HexString(serviceData));
            UnprovisionedMeshNodeData unprovisionedMeshNodeData = new UnprovisionedMeshNodeData(serviceData);
            String deviceMac = unprovisionedMeshNodeData.getDeviceMac();
            String str = unprovisionedMeshNodeData.getProductId() + "";
            if (!z && !unprovisionedMeshNodeData.isSupportFastProvisioningV2()) {
                a(serviceData, unprovisionedMeshNodeData, deviceMac, str);
            }
            a.a.a.a.b.m.a.c(this.f2789b, ConvertUtils.bytes2HexString(serviceData));
            boolean zIsFastProvisionMesh = unprovisionedMeshNodeData.isFastProvisionMesh();
            a.a.a.a.b.m.a.c(this.f2789b, "unprovisionedMeshNodeData.isFastProvisionMesh: " + unprovisionedMeshNodeData.isFastProvisionMesh() + ", isProvisioned: " + z);
            if (!z && unprovisionedMeshNodeData.isSupportFastProvisioningV2()) {
                a(extendedBluetoothDevice.getConfigurationInfo());
            } else if (!z && unprovisionedMeshNodeData.isFastProvisionMesh()) {
                this.p = true;
                this.O = false;
                if (Build.VERSION.SDK_INT >= 21 && (j = this.P) != null) {
                    j.a(this.f2790c, this, this.m, this, this, this);
                    this.P.a(unprovisionedMeshNodeData, extendedBluetoothDevice.getDevice(), new B(this));
                }
            } else {
                if (!z) {
                    BleMeshManager bleMeshManager = this.f2791d;
                    if (bleMeshManager != null) {
                        bleMeshManager.disconnect().enqueue();
                    }
                    this.f2791d = new BleMeshManager(this.f2790c.getApplicationContext(), String.valueOf(hashCode() % 1000000));
                }
                this.f2791d.setGattCallbacks(this);
                this.f2791d.connect(extendedBluetoothDevice.getDevice()).retry(5, 1000).enqueue();
                if (z && !this.H) {
                    z2 = true;
                }
                this.O = z2;
                a.a.a.a.b.m.a.a(this.f2789b, "mConnectToMeshNetwork: " + this.O);
            }
            z2 = zIsFastProvisionMesh;
        } else {
            a.a.a.a.b.m.a.c(this.f2789b, "mScannerRecord is null");
            this.f2791d.connect(extendedBluetoothDevice.getDevice()).retry(5, 1000).enqueue();
            this.O = z && !this.H;
        }
        if (z) {
            return;
        }
        this.s.postDelayed(this.ca, oa.a(z2));
    }

    public final void c(int i) {
        Intent intent = new Intent(Utils.ACTION_PROVISIONING_STATE);
        intent.putExtra(Utils.EXTRA_PROVISIONING_STATE, i);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    public final void c() {
        Runnable runnable = this.S;
        if (runnable == null) {
            return;
        }
        this.s.removeCallbacks(runnable);
        this.S = null;
    }

    public final boolean a(byte[] bArr) {
        for (Map.Entry<Integer, ProvisionedMeshNode> entry : this.e.c().entrySet()) {
            if (this.e != null && entry.getValue().getIdentityKey() != null && this.e.b(entry.getValue(), bArr)) {
                return true;
            }
        }
        return false;
    }

    public final boolean a(String str) {
        List<String> list = this.I;
        if (list == null || list.size() == 0) {
            return true;
        }
        return this.I.contains(str.toUpperCase());
    }

    public void a(OnProvisionFinishedListener onProvisionFinishedListener) {
        this.W = onProvisionFinishedListener;
    }

    public void a(ExtendedBluetoothDevice extendedBluetoothDevice, boolean z, Map<String, Object> map) {
        this.Y = map;
        a(extendedBluetoothDevice, z);
    }

    public void a(ExtendedBluetoothDevice extendedBluetoothDevice, boolean z) {
        a.a.a.a.b.m.a.a(this.f2789b, "startProvisioning() called with: device = [" + extendedBluetoothDevice + "], isProvisioned = [" + z + "]");
        if (extendedBluetoothDevice == null) {
            return;
        }
        CountDownLatch countDownLatch = this.Q;
        if (countDownLatch != null) {
            countDownLatch.notify();
            this.Q = null;
        }
        this.Q = new CountDownLatch(1);
        a(extendedBluetoothDevice);
        b(extendedBluetoothDevice, z);
        try {
            this.Q.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        a.a.a.a.b.m.a.c(this.f2789b, "execution complete");
    }

    public final void a(ExtendedBluetoothDevice extendedBluetoothDevice) {
        this.r = extendedBluetoothDevice.getScanRecord();
        this.q = extendedBluetoothDevice.getDevice();
        ScanRecord scanRecord = this.r;
        if (scanRecord != null) {
            this.z = new UnprovisionedMeshNodeData(scanRecord.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID)));
            this.V = !this.z.isFastProvisionMesh() || this.z.isFastSupportGatt();
        }
    }

    public final void a(ConfigurationData configurationData) {
        if (configurationData == null) {
            a(MeshUtConst$MeshErrorEnum.INVALID_PROVISIONING_PARAMETER, "Configuration info can not be null for fastProvisioningV2");
            return;
        }
        if (configurationData.getConfigResultMap() != null && configurationData.getConfigResultMap().getSigmeshKeys() != null && configurationData.getConfigResultMap().getSigmeshKeys().size() != 0 && configurationData.getConfigResultMap().getSigmeshKeys().get(0).getProvisionNetKey() != null && configurationData.getConfigResultMap().getSigmeshKeys().get(0).getProvisionAppKeys() != null && configurationData.getConfigResultMap().getSigmeshKeys().get(0).getProvisionAppKeys().size() != 0 && configurationData.getConfigResultMap().getSigmeshKeys().get(0).getProvisionAppKeys().get(0) != null) {
            this.X = new a.a.a.a.b.i.u(this.f2790c, String.valueOf(hashCode() % 1000000));
            this.X.a(this.z, this.q, new C(this), new D(this));
            ProvisionInfo provisionInfo = new ProvisionInfo();
            provisionInfo.setPrimaryUnicastAddress((Integer) configurationData.getPrimaryUnicastAddress());
            provisionInfo.setServerConfirmation(configurationData.getServerConfirmation());
            ProvisionNetKey provisionNetKey = configurationData.getConfigResultMap().getSigmeshKeys().get(0).getProvisionNetKey();
            ArrayList arrayList = new ArrayList();
            arrayList.add(Integer.valueOf(provisionNetKey.getNetKeyIndex()));
            ArrayList arrayList2 = new ArrayList();
            arrayList2.add(provisionNetKey.getNetKey());
            provisionInfo.setNetKeyIndexes(arrayList);
            provisionInfo.setNetKeys(arrayList2);
            List<ProvisionAppKey> provisionAppKeys = configurationData.getConfigResultMap().getSigmeshKeys().get(0).getProvisionAppKeys();
            ArrayList arrayList3 = new ArrayList();
            ArrayList arrayList4 = new ArrayList(2);
            for (ProvisionAppKey provisionAppKey : provisionAppKeys) {
                arrayList4.add(provisionAppKey.getAppKey());
                arrayList3.add(Integer.valueOf(provisionAppKey.getAppKeyIndex()));
            }
            provisionInfo.setAppKeyIndexes(arrayList3);
            provisionInfo.setAppKeys(arrayList4);
            this.X.a(provisionInfo);
            return;
        }
        a(MeshUtConst$MeshErrorEnum.INVALID_PROVISIONING_PARAMETER, "netKey and appKey can not be null for fastProvisioningV2");
    }

    public final void b(String str) {
        Intent intent = new Intent(Utils.ACTION_CONNECTION_STATE);
        intent.putExtra(Utils.EXTRA_DATA, str);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    public final void b(int i) {
        Intent intent = new Intent(Utils.ACTION_CONFIGURATION_STATE);
        intent.putExtra(Utils.EXTRA_CONFIGURATION_STATE, i);
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    public final void b(ProvisionedMeshNode provisionedMeshNode) {
        Map<String, Object> map = this.Y;
        if (map == null || !map.containsKey("ssid")) {
            return;
        }
        a.a.a.a.b.m.a.c(this.f2789b, "WiFi config start, mesh channel connected: " + this.f2791d.isConnected());
        this.e.a(provisionedMeshNode, provisionedMeshNode.getAddedAppKeys().get(0), provisionedMeshNode.getUnicastAddress(), 13740033, 13871105, C0335a.a((String) this.Y.get("ssid"), (String) this.Y.get("password"), this.Y.containsKey(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_BSSID) ? (String) this.Y.get(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_BSSID) : null, this.Y.get(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_REGION_INDEX) != null ? ((Byte) this.Y.get(TgMeshManager.KEY_PROVISION_COMBO_MESH_WIFI_REGION_INDEX)).byteValue() : (byte) 0), new C0362v(this));
        this.Y = null;
    }

    public final void a(byte[] bArr, UnprovisionedMeshNodeData unprovisionedMeshNodeData, String str, String str2) {
        a.a.a.a.b.m.a.c(this.f2789b, "requestProvisionInfo with productId " + str2);
        na.a().c(str, str2, unprovisionedMeshNodeData.getDeviceUuid() != null ? MeshParserUtils.bytesToHex(unprovisionedMeshNodeData.getDeviceUuid(), false).toLowerCase() : "", new C0330e(this, unprovisionedMeshNodeData, bArr));
    }

    public final void a(ProvisionedMeshNode provisionedMeshNode, IActionListener<Boolean> iActionListener) {
        a.a.a.a.b.m.a.c(this.f2789b, "requestConfirmation, fire provisionComplete request");
        UnprovisionedMeshNodeData unprovisionedMeshNodeData = this.z;
        if (unprovisionedMeshNodeData != null) {
            na.a().a(unprovisionedMeshNodeData.getDeviceMac(), MeshParserUtils.bytesToHex(provisionedMeshNode.getDeviceKey(), false), this.z.getProductId() + "", (this.z.getDeviceUuid() != null ? MeshParserUtils.bytesToHex(this.z.getDeviceUuid(), false).toLowerCase() : "").toLowerCase(), new C0332g(this, iActionListener));
        }
    }

    public final void b() {
        Runnable runnable = this.Z;
        if (runnable != null) {
            this.s.removeCallbacks(runnable);
            this.Z = null;
        }
    }

    public final void a(ProvisionedMeshNode provisionedMeshNode) {
        String strPoll = this.o.poll();
        if (strPoll != null) {
            Integer numPoll = this.n.poll();
            Integer num = numPoll == null ? 0 : numPoll;
            Handler handler = this.s;
            a aVar = new a(strPoll, num.intValue(), provisionedMeshNode, true);
            this.E = aVar;
            handler.postDelayed(aVar, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS);
            this.e.a(provisionedMeshNode, num.intValue(), strPoll);
            a.a.a.a.b.m.a.c(this.f2789b, "try to add app key: appKeyIndex = " + num + ", mAppKey = " + strPoll);
        }
    }

    public final void a(ProvisionedMeshNode provisionedMeshNode, byte[] bArr, int i, List<Integer> list) {
        Integer numRemove;
        if (list.size() == 0 || (numRemove = list.remove(0)) == null) {
            return;
        }
        this.e.a(provisionedMeshNode, bArr, numRemove.intValue(), i);
        a.a.a.a.b.m.a.a(this.f2789b, "bindAppKey");
    }

    public final void a(byte[] bArr, DeviceStatus deviceStatus) {
        a.a.a.a.b.m.a.a(this.f2789b, "getInfoByAuthInfo");
        if (bArr == null) {
            a.a.a.a.b.m.a.b(this.f2789b, "getInfoByAuthInfo: unicast address is null");
            return;
        }
        this.h = true;
        m();
        ExtendedBluetoothDevice extendedBluetoothDevice = this.G;
        if (extendedBluetoothDevice != null) {
            extendedBluetoothDevice.getPk();
        }
        UnprovisionedMeshNodeData unprovisionedMeshNodeData = new UnprovisionedMeshNodeData(this.r.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID)));
        String deviceMac = unprovisionedMeshNodeData.getDeviceMac();
        String str = unprovisionedMeshNodeData.getProductId() + "";
        String strBytesToHex = MeshParserUtils.bytesToHex(unprovisionedMeshNodeData.getDeviceUuid(), false);
        int unicastAddressInt = AddressUtils.getUnicastAddressInt(bArr);
        IotDevice iotDevice = new IotDevice();
        iotDevice.setDevId(strBytesToHex.toLowerCase());
        iotDevice.setPlatform("SIGMESH");
        iotDevice.setSource("app");
        iotDevice.setMac(deviceMac);
        iotDevice.setProductKey(str);
        iotDevice.setUnicastAddress(unicastAddressInt);
        iotDevice.setUserId(na.a().b());
        iotDevice.setUuid(na.a().c());
        JSONObject jSONObject = null;
        if (deviceStatus != null) {
            jSONObject = new JSONObject();
            jSONObject.put("deviceStatus", (Object) JSONObject.toJSONString(deviceStatus));
        }
        Map<String, Object> map = this.Y;
        if (map != null && !TextUtils.isEmpty(String.valueOf(map.get("familyId")))) {
            if (jSONObject == null) {
                jSONObject = new JSONObject();
            }
            jSONObject.put("familyId", (Object) String.valueOf(this.Y.get("familyId")));
        }
        if (jSONObject != null) {
            iotDevice.setExtensions(jSONObject.toJSONString());
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(iotDevice);
        String jSONString = JSON.toJSONString(arrayList);
        i.c().f(this.m.h(), bArr);
        a.a.a.a.b.m.a.a(this.f2789b, "Use delegate to handle bind logic args:" + jSONString);
        if (a.a.a.a.b.d.a.f1315a) {
            na.a().b("iot", "bindBLEDevice", jSONString, new C0357p(this, iotDevice, jSONString));
            return;
        }
        OnReadyToBindHandler onReadyToBindHandler = this.C;
        if (onReadyToBindHandler != null) {
            onReadyToBindHandler.onReadyToBind(jSONString, new C0358q(this, iotDevice));
        }
    }

    public final void a(MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum, String str) {
        String str2;
        this.T = true;
        o();
        Intent intent = new Intent(Utils.ACTION_PROVISIONING_STATE);
        intent.putExtra(Utils.EXTRA_PROVISIONING_STATE, MeshNodeStatus.PROVISIONING_FAILED.getState());
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("device_mac_address", (Object) h());
        jSONObject.put(TmpConstant.SERVICE_DESC, (Object) str);
        intent.putExtra(Utils.EXTRA_PROVISIONING_FAIL_MSG, jSONObject.toJSONString());
        if (this.z == null) {
            str2 = "";
        } else {
            str2 = this.z.getProductId() + "";
        }
        a.a.a.a.b.m.b.a("ALSMesh", "ble", str2, false, this.r.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID)), "", 0L, meshUtConst$MeshErrorEnum.getErrorCode(), meshUtConst$MeshErrorEnum.getErrorMsg());
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
        this.s.removeCallbacks(this.ca);
        CountDownLatch countDownLatch = this.Q;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
        p();
    }

    public final void a(boolean z, String str) {
        Intent intent = new Intent(Utils.ACTION_IS_CONNECTED);
        intent.putExtra(Utils.EXTRA_DATA, z);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        intent.putExtra(Utils.EXTRA_CONNECT_FAIL_MSG, str);
    }

    public final void a(int i, String str) {
        Intent intent = new Intent(Utils.ACTION_BIND_STATE);
        intent.putExtra(Utils.EXTRA_BIND_CODE, i);
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra(Utils.EXTRA_BIND_STATE_MSG, str);
        }
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
        if (i == 1) {
            byte[] serviceData = this.r.getServiceData(new ParcelUuid(BleMeshManager.MESH_PROVISIONING_UUID));
            a.a.a.a.b.m.b.a("ALSMesh", "ble", String.valueOf(AliMeshUUIDParserUtil.extractPIDFromUUID(serviceData)), false, serviceData, "", 0L);
        } else {
            MeshUtConst$MeshErrorEnum meshUtConst$MeshErrorEnum = MeshUtConst$MeshErrorEnum.BIND_API_FAILED;
            a(meshUtConst$MeshErrorEnum, meshUtConst$MeshErrorEnum.getErrorMsg());
        }
        CountDownLatch countDownLatch = this.Q;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    public final void a() {
        if (this.V) {
            a.a.a.a.b.m.a.c(this.f2789b, Thread.currentThread().getName() + " acquire global connection semaphore");
            if (this.R.get()) {
                return;
            }
            try {
                f2788a.acquire();
                this.R.set(true);
                String str = this.f2789b;
                StringBuilder sb = new StringBuilder();
                sb.append(Thread.currentThread().getName());
                sb.append(" global connection semaphore acquired");
                a.a.a.a.b.m.a.c(str, sb.toString());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.s.postDelayed(new RunnableC0360t(this), g());
        }
    }

    public final void a(int i) {
        Handler handler = this.s;
        RunnableC0361u runnableC0361u = new RunnableC0361u(this);
        this.S = runnableC0361u;
        handler.postDelayed(runnableC0361u, i);
    }

    public final void a(byte[] bArr, String str, byte[] bArr2) {
        if (!Arrays.equals(bArr, this.i.getUnicastAddress()) || !"d4a801".equalsIgnoreCase(str) || bArr2 == null || bArr2.length < 6) {
            return;
        }
        byte b2 = bArr2[0];
        if (Arrays.equals(new byte[]{bArr2[2], bArr2[1]}, new byte[]{-16, 6})) {
            if (this.ba == null) {
                this.ba = new WiFiConfigReplyParser(new C0363w(this));
            }
            byte[] bArr3 = new byte[bArr2.length - 3];
            System.arraycopy(bArr2, 3, bArr3, 0, bArr3.length);
            this.ba.a(bArr3);
        }
    }

    public final void a(boolean z, int i, int i2, String str) {
        b();
        m();
        if (z) {
            a.a.a.a.b.m.a.c(this.f2789b, "on successful to config Wi-Fi info");
        } else {
            a.a.a.a.b.m.a.b(this.f2789b, "on failed to config Wi-Fi info, error code: " + i + " , " + str);
        }
        Intent intent = new Intent(Utils.ACTION_PROVISIONING_STATE);
        intent.putExtra(Utils.EXTRA_PROVISIONING_STATE, MeshNodeStatus.COMBO_WIFI_CONFIG_STATUS.getState());
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("device_mac_address", (Object) h());
        jSONObject.put("isSuccess", (Object) Boolean.valueOf(z));
        if (!z) {
            jSONObject.put("errorCode", (Object) Integer.valueOf(i));
            jSONObject.put("subErrorCode", (Object) Integer.valueOf(i2));
            jSONObject.put("errorMessage", (Object) str);
        }
        intent.putExtra(Utils.EXTRA_PROVISIONING_FAIL_MSG, jSONObject.toJSONString());
        LocalBroadcastManager.getInstance(this.f2790c).sendBroadcast(intent);
    }

    public final void a(byte b2) {
        a.a.a.a.b.m.a.c(this.f2789b, String.format("D3 ack recevied, code: %02X", Byte.valueOf(b2)));
        if (b2 == 1) {
            Handler handler = this.s;
            RunnableC0364x runnableC0364x = new RunnableC0364x(this);
            this.Z = runnableC0364x;
            handler.postDelayed(runnableC0364x, 40000L);
            return;
        }
        a(false, -71, (int) b2, "");
    }

    public final void a(IotDevice iotDevice, String str) {
        UnprovisionedMeshNodeData unprovisionedMeshNodeData;
        J j;
        a.a.a.a.b.m.a.a(this.f2789b, "bindDevice request success");
        OnReadyToBindHandler onReadyToBindHandler = this.C;
        if (onReadyToBindHandler != null) {
            onReadyToBindHandler.onReadyToBind(str, null);
        }
        BaseMeshNode baseMeshNode = this.i;
        if (baseMeshNode != null && !baseMeshNode.getSupportFastProvision()) {
            this.O = true;
            a(true, (String) null);
        }
        this.s.removeCallbacks(this.ca);
        a(1, JSON.toJSONString(iotDevice));
        if (Build.VERSION.SDK_INT >= 21 && (j = this.P) != null) {
            j.l();
        }
        if (this.i == null || (unprovisionedMeshNodeData = this.z) == null) {
            return;
        }
        try {
            a.a.a.a.b.e.a.a(String.valueOf(this.z.getProductId()), MeshParserUtils.bytesToHex(unprovisionedMeshNodeData.getDeviceUuid(), false), this.z.getDeviceMac(), this.i.getUnicastAddressInt(), MeshParserUtils.bytesToHex(this.i.getDeviceKey(), false));
        } catch (Exception unused) {
        }
    }
}
