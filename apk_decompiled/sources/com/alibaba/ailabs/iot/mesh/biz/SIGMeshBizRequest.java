package com.alibaba.ailabs.iot.mesh.biz;

import a.a.a.a.b.G;
import a.a.a.a.b.a.C0315a;
import a.a.a.a.b.a.I;
import a.a.a.a.b.a.RunnableC0322h;
import a.a.a.a.b.a.RunnableC0323i;
import aisble.Operation;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import b.C0378l;
import b.u;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;
import com.alibaba.ailabs.iot.mesh.utils.Utils;
import java.util.concurrent.atomic.AtomicInteger;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes.dex */
public class SIGMeshBizRequest implements Operation {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public final Type f2802a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public Mode f2803b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public IActionListener f2804c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public b f2805d;
    public boolean e;
    public ProvisionedMeshNode f;
    public byte[] g;
    public I<?> h;
    public AtomicInteger i;
    public byte[] j;
    public boolean k;
    public Runnable l;
    public Handler m;
    public int n;
    public int o;
    public String p;
    public C0378l q;
    public Runnable r;

    public enum InteractionModel {
        REQUEST_RESPONSE,
        FIRE_AND_FORGET,
        UNKNOWN
    }

    public enum Mode {
        UNICAST,
        MULTICAST
    }

    public enum NetworkParameter {
        DEVICE_NUMBER_1(1, 5, 4, 8, 10, 30, 205, 1, 20),
        DEVICE_NUMBER_21(1, 5, 6, 12, 10, 30, 120, 21, 50),
        DEVICE_NUMBER_51(1, 5, 8, 16, 10, 30, 120, 51, 100),
        DEVICE_NUMBER_101(1, 5, 10, 20, 10, 45, 120, 101, 150),
        DEVICE_NUMBER_151(1, 5, 12, 28, 20, 60, 120, 151, Integer.MAX_VALUE);

        public int adv_duration;
        public int boot_interval;
        public int cr_enable;
        public int device_count;
        public int group_delay_max;
        public int group_delay_min;
        public int max_device_count;
        public int min_device_count;
        public int per_interval;
        public int send_ttl;

        NetworkParameter(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9) {
            this.cr_enable = i;
            this.send_ttl = i2;
            this.group_delay_min = i3;
            this.group_delay_max = i4;
            this.boot_interval = i5;
            this.per_interval = i6;
            this.adv_duration = i7;
            this.min_device_count = i8;
            this.max_device_count = i9;
        }

        /* JADX WARN: Removed duplicated region for block: B:18:0x0026  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static synchronized boolean changeNetworkParameter(com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.NetworkParameter r4, boolean r5) {
            /*
                java.lang.Class<com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest$NetworkParameter> r0 = com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.NetworkParameter.class
                monitor-enter(r0)
                r1 = 0
                if (r4 != 0) goto L8
                monitor-exit(r0)
                return r1
            L8:
                r2 = 1
                if (r5 == 0) goto L12
                int r5 = r4.device_count     // Catch: java.lang.Throwable -> L29
                int r5 = r5 + r2
                r4.setDevice_count(r5)     // Catch: java.lang.Throwable -> L29
                goto L1c
            L12:
                int r5 = r4.min_device_count     // Catch: java.lang.Throwable -> L29
                if (r5 <= r2) goto L1c
                int r5 = r4.device_count     // Catch: java.lang.Throwable -> L29
                int r5 = r5 - r2
                r4.setDevice_count(r5)     // Catch: java.lang.Throwable -> L29
            L1c:
                int r5 = r4.device_count     // Catch: java.lang.Throwable -> L29
                int r3 = r4.min_device_count     // Catch: java.lang.Throwable -> L29
                if (r5 < r3) goto L26
                int r4 = r4.max_device_count     // Catch: java.lang.Throwable -> L29
                if (r5 <= r4) goto L27
            L26:
                r1 = r2
            L27:
                monitor-exit(r0)
                return r1
            L29:
                r4 = move-exception
                monitor-exit(r0)
                throw r4
            */
            throw new UnsupportedOperationException("Method not decompiled: com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest.NetworkParameter.changeNetworkParameter(com.alibaba.ailabs.iot.mesh.biz.SIGMeshBizRequest$NetworkParameter, boolean):boolean");
        }

        public static NetworkParameter getNetworkParameter(int i) {
            if (i == 0) {
                return null;
            }
            NetworkParameter networkParameter = i < 21 ? DEVICE_NUMBER_1 : i < 52 ? DEVICE_NUMBER_21 : i < 102 ? DEVICE_NUMBER_51 : i < 152 ? DEVICE_NUMBER_101 : DEVICE_NUMBER_151;
            networkParameter.setDevice_count(i);
            return networkParameter;
        }

        public byte[] getParameter() {
            return new byte[]{(byte) this.cr_enable, (byte) this.send_ttl, (byte) this.group_delay_min, (byte) this.group_delay_max, (byte) this.boot_interval, (byte) this.per_interval, (byte) this.adv_duration};
        }

        public void setDevice_count(int i) {
            this.device_count = i;
        }

        public void setSend_ttl(int i) {
            this.send_ttl = i;
        }
    }

    public enum Type {
        CONFIG_MODEL_SUBSCRIPTION(-32741, InteractionModel.REQUEST_RESPONSE, -1, true),
        SCENE_SETUP_STORE(33350, InteractionModel.REQUEST_RESPONSE, -32187, true),
        SCENE_SETUP_DELETE(33438, InteractionModel.REQUEST_RESPONSE, -32187, true),
        SCENE_RECALL_UNACKNOWLEDGED(33347, InteractionModel.FIRE_AND_FORGET, -1, true),
        SCENE_RECALL(33346, InteractionModel.REQUEST_RESPONSE, 94, true),
        VENDOR_ATTRIBUTE_SET_UNACKNOWLEDGED(13805569, InteractionModel.FIRE_AND_FORGET, -1, true),
        VENDOR_ATTRIBUTE_SET(13740033, InteractionModel.REQUEST_RESPONSE, 13871105, true),
        VENDOR_DELEGATE_PROTOCOL(81, InteractionModel.FIRE_AND_FORGET, -1, false),
        COMMON_FIRE_AND_FORGET(0, InteractionModel.FIRE_AND_FORGET, -1, true),
        COMMON_REQUEST_RESPONSE(0, InteractionModel.REQUEST_RESPONSE, -1, true),
        COMMON_DEVICE_REST_NODE(32841, InteractionModel.FIRE_AND_FORGET, -1, true),
        UPDATE_MESH_PARAMETER(80, InteractionModel.FIRE_AND_FORGET, -1, false);

        public boolean access;
        public int expectedOpcode;
        public InteractionModel interactionModel;
        public int opcode;

        Type(int i, InteractionModel interactionModel, int i2, boolean z) {
            this.opcode = i;
            this.interactionModel = interactionModel;
            this.expectedOpcode = i2;
            this.access = z;
        }

        public int getExpectedOpcode() {
            return this.expectedOpcode;
        }

        public InteractionModel getInteractionModel() {
            return this.interactionModel;
        }

        public int getOpcode() {
            return this.opcode;
        }

        public boolean isAccess() {
            return this.access;
        }

        public void setExpectedOpcode(int i) {
            this.expectedOpcode = i;
        }

        public void setOpcode(int i) {
            this.opcode = i;
        }
    }

    public static final class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public Type f2806a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        public ProvisionedMeshNode f2807b;
        public IActionListener f;
        public String i;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        public boolean f2808c = true;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        public I<?> f2809d = null;
        public b e = null;
        public byte[] g = null;
        public Mode h = Mode.UNICAST;
        public int j = -1;

        public a a(Type type) {
            this.f2806a = type;
            return this;
        }

        public a a(byte[] bArr) {
            if (bArr == null || bArr.length != 2) {
                throw new IllegalArgumentException("Illegal address");
            }
            this.g = bArr;
            if (AddressUtils.isValidGroupAddress(bArr)) {
                this.h = Mode.MULTICAST;
            } else if (AddressUtils.isValidUnicastAddress(bArr)) {
                this.h = Mode.UNICAST;
            }
            return this;
        }

        public a a(ProvisionedMeshNode provisionedMeshNode) {
            this.f2807b = provisionedMeshNode;
            return this;
        }

        public a a(boolean z) {
            this.f2808c = z;
            return this;
        }

        public a a(I<?> i) {
            this.f2809d = i;
            return this;
        }

        public a a(b bVar) {
            this.e = bVar;
            return this;
        }

        public a a(IActionListener<?> iActionListener) {
            this.f = iActionListener;
            return this;
        }

        public a a(String str) {
            this.i = str;
            return this;
        }

        public a a(int i) {
            this.j = i;
            return this;
        }

        public SIGMeshBizRequest a() {
            SIGMeshBizRequest sIGMeshBizRequest = new SIGMeshBizRequest(this.f2806a, this.h, this.f2807b, null);
            sIGMeshBizRequest.e = this.f2808c;
            I<?> i = this.f2809d;
            if (i != null) {
                sIGMeshBizRequest.h = i;
            }
            b bVar = this.e;
            if (bVar != null) {
                sIGMeshBizRequest.f2805d = bVar;
            }
            IActionListener iActionListener = this.f;
            if (iActionListener != null) {
                sIGMeshBizRequest.f2804c = iActionListener;
            }
            byte[] bArr = this.g;
            if (bArr != null) {
                sIGMeshBizRequest.g = bArr;
            }
            if (!TextUtils.isEmpty(this.i)) {
                sIGMeshBizRequest.p = this.i;
            }
            int i2 = this.j;
            if (i2 != -1) {
                sIGMeshBizRequest.i = new AtomicInteger(i2);
            }
            return sIGMeshBizRequest;
        }
    }

    public interface b {
        byte[] getEncodedParameters();
    }

    public /* synthetic */ SIGMeshBizRequest(Type type, Mode mode, ProvisionedMeshNode provisionedMeshNode, RunnableC0322h runnableC0322h) {
        this(type, mode, provisionedMeshNode);
    }

    public void b() {
        Runnable runnable = this.l;
        if (runnable != null) {
            this.m.removeCallbacks(runnable);
        }
    }

    public byte[] c() {
        b bVar = this.f2805d;
        if (bVar != null) {
            return bVar.getEncodedParameters();
        }
        return null;
    }

    public byte[] d() {
        return this.j;
    }

    public int e() {
        return this.o;
    }

    public int f() {
        return this.n;
    }

    public I g() {
        return this.h;
    }

    public u.a h() {
        u uVarD = G.a().d();
        ProvisionedMeshNode provisionedMeshNode = this.f;
        if (provisionedMeshNode == null) {
            return uVarD.d();
        }
        u.a aVarH = uVarD.h(provisionedMeshNode.getNetworkKey());
        if (aVarH != null) {
            return aVarH;
        }
        a.a.a.a.b.m.a.d("SIGMeshBizRequest", String.format("Can't find Subnet via netKey(%s)", MeshParserUtils.bytesToHex(this.f.getNetworkKey(), false)));
        return uVarD.d();
    }

    public C0378l i() {
        return this.q;
    }

    public byte[] j() {
        return this.g;
    }

    public String k() {
        ProvisionedMeshNode provisionedMeshNode = this.f;
        return provisionedMeshNode != null ? Utils.deviceId2Mac(provisionedMeshNode.getDevId()) : "";
    }

    public Type l() {
        return this.f2802a;
    }

    public IActionListener m() {
        return this.f2804c;
    }

    public boolean n() {
        return this.k;
    }

    public void o() {
        this.i.decrementAndGet();
    }

    public void p() {
        this.i.getAndDecrement();
    }

    public void q() {
        Runnable runnable = this.r;
        if (runnable != null) {
            runnable.run();
        }
    }

    public SIGMeshBizRequest(@NonNull Type type, @NonNull Mode mode) {
        this.e = true;
        this.i = new AtomicInteger(3);
        this.j = null;
        this.k = false;
        this.l = null;
        this.m = new Handler(Looper.getMainLooper());
        this.r = null;
        this.f2802a = type;
        this.f2803b = mode;
        this.n = type.opcode;
        this.o = type.expectedOpcode;
    }

    public SIGMeshBizRequest a(IActionListener iActionListener) {
        this.f2804c = iActionListener;
        return this;
    }

    public boolean a() {
        return this.i.get() > 0;
    }

    public void a(byte[] bArr) {
        this.j = bArr;
    }

    public static C0315a a(String str, String str2, int i, int i2, int i3, int i4) {
        return new C0315a(Type.CONFIG_MODEL_SUBSCRIPTION, str, str2, i, i2, i3, i4);
    }

    public void a(int i, Runnable runnable) {
        this.l = new RunnableC0323i(this, runnable);
        this.m.postDelayed(this.l, i);
    }

    public SIGMeshBizRequest(@NonNull Type type, @NonNull Mode mode, @NonNull ProvisionedMeshNode provisionedMeshNode) {
        this(type, mode);
        this.f = provisionedMeshNode;
    }

    public void a(C0378l c0378l) {
        this.q = c0378l;
    }

    public void a(Runnable runnable) {
        this.r = runnable;
    }
}
