package b;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseIntArray;
import androidx.annotation.NonNull;
import b.InterfaceC0367a;
import com.alibaba.ailabs.tg.utils.ToastUtils;
import java.nio.ByteBuffer;
import meshprovisioner.configuration.ProvisionedMeshNode;
import meshprovisioner.states.GenieProvisioningStartState;
import meshprovisioner.states.ProvisioningCapabilities;
import meshprovisioner.states.ProvisioningCapabilitiesState;
import meshprovisioner.states.ProvisioningCompleteState;
import meshprovisioner.states.ProvisioningConfirmationState;
import meshprovisioner.states.ProvisioningDataState;
import meshprovisioner.states.ProvisioningFailedState;
import meshprovisioner.states.ProvisioningInviteState;
import meshprovisioner.states.ProvisioningPublicKeyState;
import meshprovisioner.states.ProvisioningRandomConfirmationState;
import meshprovisioner.states.ProvisioningState;
import meshprovisioner.states.UnprovisionedMeshNode;
import meshprovisioner.states.UnprovisionedMeshNodeData;
import meshprovisioner.utils.ParseOutputOOBActions;
import meshprovisioner.utils.ParseProvisioningAlgorithm;
import meshprovisioner.utils.UnprovisionedMeshNodeUtil;

/* JADX INFO: compiled from: MeshProvisioningHandler.java */
/* JADX INFO: loaded from: classes.dex */
public class o implements InterfaceC0367a.InterfaceC0176a {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public static final String f2199a = "o";

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public final InterfaceC0370d f2200b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public final Context f2201c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public p f2202d;
    public int e;
    public int f;
    public int g;
    public int h;
    public int i;
    public int j;
    public int k;
    public int l;
    public int m;
    public ProvisioningState n;
    public boolean o;
    public boolean p;
    public InterfaceC0368b q;
    public InterfaceC0367a r;
    public Handler s = new Handler(Looper.getMainLooper());
    public boolean t = false;
    public SparseIntArray u = new SparseIntArray();

    public o(Context context, InterfaceC0370d interfaceC0370d, InterfaceC0368b interfaceC0368b, InterfaceC0367a interfaceC0367a) {
        this.f2201c = context;
        this.f2200b = interfaceC0370d;
        this.q = interfaceC0368b;
        this.r = interfaceC0367a;
    }

    public void a(InterfaceC0367a interfaceC0367a) {
        this.r = interfaceC0367a;
    }

    public void b(UnprovisionedMeshNode unprovisionedMeshNode, byte[] bArr) {
        if (this.t) {
        }
        switch (n.f2198a[this.n.getState().ordinal()]) {
            case 2:
                if (!c(bArr)) {
                    c(unprovisionedMeshNode, bArr);
                } else {
                    e(unprovisionedMeshNode, bArr);
                }
                break;
            case 4:
                if (!c(bArr)) {
                    c(unprovisionedMeshNode, bArr);
                } else {
                    a(unprovisionedMeshNode, bArr);
                }
                break;
            case 6:
                if (!c(bArr)) {
                    c(unprovisionedMeshNode, bArr);
                } else if (a(bArr)) {
                    f(unprovisionedMeshNode);
                }
                break;
            case 7:
                if (!c(bArr)) {
                    c(unprovisionedMeshNode, bArr);
                } else {
                    b(bArr);
                }
                break;
            case 8:
            case 9:
            case 10:
                c(unprovisionedMeshNode, bArr);
                break;
        }
    }

    public final void c(UnprovisionedMeshNode unprovisionedMeshNode, byte[] bArr) {
        if (bArr.length >= 2 && bArr[0] == 3 && bArr[1] == ProvisioningState.State.PROVISINING_COMPLETE.getState()) {
            this.n = new ProvisioningCompleteState(unprovisionedMeshNode);
            this.o = false;
            this.p = false;
            ProvisionedMeshNode provisionedMeshNode = new ProvisionedMeshNode(unprovisionedMeshNode);
            this.q.c(provisionedMeshNode);
            this.f2202d.onProvisioningComplete(provisionedMeshNode);
            return;
        }
        if (bArr.length >= 2 && bArr[0] == 3 && bArr[1] < this.n.getState().ordinal()) {
            a.a.a.a.b.m.a.d(f2199a, "Received data that did not meet expectations: " + Integer.toHexString(bArr[1] & 255));
            return;
        }
        this.o = false;
        this.p = false;
        ProvisioningFailedState provisioningFailedState = new ProvisioningFailedState(this.f2201c, unprovisionedMeshNode);
        this.n = provisioningFailedState;
        if (bArr.length > 2 && provisioningFailedState.parseData(bArr)) {
            unprovisionedMeshNode.setIsProvisioned(false);
            this.f2202d.onProvisioningFailed(unprovisionedMeshNode, provisioningFailedState.getErrorCode());
        } else {
            unprovisionedMeshNode.setIsProvisioned(false);
            provisioningFailedState.setErrorCode(ProvisioningFailedState.ProvisioningFailureCode.UNEXPECTED_ERROR);
            this.f2202d.onProvisioningFailed(unprovisionedMeshNode, provisioningFailedState.getErrorCode());
        }
    }

    public void d(UnprovisionedMeshNode unprovisionedMeshNode, byte[] bArr) {
        if (this.n == null) {
            return;
        }
        c(unprovisionedMeshNode, bArr);
    }

    public final boolean e(UnprovisionedMeshNode unprovisionedMeshNode, byte[] bArr) {
        ProvisioningCapabilitiesState provisioningCapabilitiesState = new ProvisioningCapabilitiesState(unprovisionedMeshNode, this.f2202d);
        this.n = provisioningCapabilitiesState;
        provisioningCapabilitiesState.parseData(bArr);
        return true;
    }

    public final void f(UnprovisionedMeshNode unprovisionedMeshNode) {
        ProvisioningRandomConfirmationState provisioningRandomConfirmationState = new ProvisioningRandomConfirmationState(this, unprovisionedMeshNode, this.f2200b, this.f2202d);
        this.n = provisioningRandomConfirmationState;
        provisioningRandomConfirmationState.executeSend();
    }

    public void g(@NonNull UnprovisionedMeshNode unprovisionedMeshNode) {
        this.t = false;
        this.e = 5;
        e(unprovisionedMeshNode);
    }

    @Override // b.InterfaceC0367a.InterfaceC0176a
    public void a(UnprovisionedMeshNode unprovisionedMeshNode, boolean z) {
        if (unprovisionedMeshNode == null || !z) {
            return;
        }
        c(unprovisionedMeshNode);
    }

    public void a(UnprovisionedMeshNode unprovisionedMeshNode) {
        if (this.t) {
        }
        switch (n.f2198a[this.n.getState().ordinal()]) {
            case 1:
                this.n = new ProvisioningCapabilitiesState(unprovisionedMeshNode, this.f2202d);
                break;
            case 3:
            case 4:
                b(unprovisionedMeshNode);
                break;
        }
    }

    public final void d(UnprovisionedMeshNode unprovisionedMeshNode) {
        this.o = false;
        this.p = false;
        this.e = 5;
        ProvisioningInviteState provisioningInviteState = new ProvisioningInviteState(unprovisionedMeshNode, this.e, this.f2200b, this.f2202d);
        this.n = provisioningInviteState;
        provisioningInviteState.executeSend();
    }

    public final void e(UnprovisionedMeshNode unprovisionedMeshNode) {
        ProvisioningCapabilities capabilities = ((ProvisioningCapabilitiesState) this.n).getCapabilities();
        this.f = capabilities.getNumberOfElements();
        this.g = capabilities.getSupportedAlgorithm();
        this.h = capabilities.getPublicKeyType();
        this.i = capabilities.getStaticOOBType();
        this.j = capabilities.getOutputOOBSize();
        this.k = capabilities.getOutputOOBAction();
        this.l = capabilities.getInputOOBSize();
        this.m = capabilities.getInputOOBAction();
        new GenieProvisioningStartState(unprovisionedMeshNode, this.f2200b, this.f2202d).executeSend();
        this.n = new ProvisioningPublicKeyState(unprovisionedMeshNode, this.f2200b, this.f2202d);
    }

    public void a(@NonNull String str, String str2, @NonNull String str3, int i, int i2, int i3, int i4, int i5, byte[] bArr, byte[] bArr2, UnprovisionedMeshNodeData unprovisionedMeshNodeData, a.a.a.a.b.i.J j) {
        this.t = false;
        this.u.clear();
        UnprovisionedMeshNode unprovisionedMeshNodeBuildUnprovisionedMeshNode = UnprovisionedMeshNodeUtil.buildUnprovisionedMeshNode(this.f2201c, str, str2, str3, i, i2, i3, i4, i5, bArr, bArr2, this.r);
        unprovisionedMeshNodeBuildUnprovisionedMeshNode.setSupportFastProvision(unprovisionedMeshNodeData.isFastProvisionMesh());
        unprovisionedMeshNodeBuildUnprovisionedMeshNode.setSupportFastGattProvision(unprovisionedMeshNodeData.isFastSupportGatt());
        unprovisionedMeshNodeBuildUnprovisionedMeshNode.setSupportAutomaticallyGenerateShareAppKey(unprovisionedMeshNodeData.isSupportAutomaticallyGenerateShareAppKey());
        if (unprovisionedMeshNodeBuildUnprovisionedMeshNode.supportFastProvision) {
            a.a.a.a.b.m.a.c("InexpensiveMesh", "identify: try fast provision");
            if (Build.VERSION.SDK_INT < 21) {
                a.a.a.a.b.m.a.b(f2199a, "Device is version is not support fast Provision");
                ToastUtils.showLong("Device not supported");
                return;
            } else {
                if (j != null) {
                    j.a(unprovisionedMeshNodeBuildUnprovisionedMeshNode);
                    j.a(unprovisionedMeshNodeData);
                    return;
                }
                return;
            }
        }
        d(unprovisionedMeshNodeBuildUnprovisionedMeshNode);
    }

    public final void b(UnprovisionedMeshNode unprovisionedMeshNode) {
        if (this.o) {
            return;
        }
        ProvisioningState provisioningState = this.n;
        if (provisioningState instanceof ProvisioningPublicKeyState) {
            this.o = true;
            provisioningState.executeSend();
        } else {
            ProvisioningPublicKeyState provisioningPublicKeyState = new ProvisioningPublicKeyState(unprovisionedMeshNode, this.f2200b, this.f2202d);
            this.n = provisioningPublicKeyState;
            this.o = true;
            provisioningPublicKeyState.executeSend();
        }
    }

    public final void c(UnprovisionedMeshNode unprovisionedMeshNode) {
        ProvisioningDataState provisioningDataState = new ProvisioningDataState(this, unprovisionedMeshNode, this.f2200b, this.f2202d);
        this.n = provisioningDataState;
        provisioningDataState.executeSend();
    }

    public final void a(UnprovisionedMeshNode unprovisionedMeshNode, byte[] bArr) {
        ProvisioningState provisioningState = this.n;
        if (provisioningState instanceof ProvisioningPublicKeyState) {
            this.p = ((ProvisioningPublicKeyState) provisioningState).parseData(bArr);
            if (!this.p) {
                ProvisioningFailedState provisioningFailedState = new ProvisioningFailedState(this.f2201c, unprovisionedMeshNode);
                this.n = provisioningFailedState;
                unprovisionedMeshNode.setIsProvisioned(false);
                provisioningFailedState.setErrorCode(ProvisioningFailedState.ProvisioningFailureCode.INVALID_PDU);
                this.f2202d.onProvisioningFailed(unprovisionedMeshNode, provisioningFailedState.getErrorCode());
            }
            if (this.o && this.p) {
                this.n = new ProvisioningConfirmationState(this, unprovisionedMeshNode, this.f2200b, this.f2202d);
                if (this.k == 0 && this.m == 0) {
                    a("");
                } else {
                    this.f2202d.onProvisioningAuthenticationInputRequested(unprovisionedMeshNode);
                }
            }
        }
    }

    public final boolean c(byte[] bArr) {
        return bArr[0] == 3 && bArr[1] == this.n.getState().ordinal();
    }

    public final boolean b(byte[] bArr) {
        String str = f2199a;
        StringBuilder sb = new StringBuilder();
        sb.append("isMainThread: ");
        sb.append(Looper.getMainLooper() == Looper.myLooper());
        a.a.a.a.b.m.a.a(str, sb.toString());
        return ((ProvisioningRandomConfirmationState) this.n).parseData(bArr);
    }

    public void c() {
        this.t = true;
    }

    public final byte[] b() {
        byte[] bArr = new byte[5];
        bArr[0] = ParseProvisioningAlgorithm.getAlgorithmValue(this.g);
        bArr[1] = 0;
        short sSelectOutputActionsFromBitMask = (byte) ParseOutputOOBActions.selectOutputActionsFromBitMask(this.k);
        if (this.i != 0) {
            bArr[2] = 1;
            bArr[3] = 0;
            bArr[4] = 0;
        } else if (sSelectOutputActionsFromBitMask != 0) {
            bArr[2] = 2;
            bArr[3] = (byte) ParseOutputOOBActions.getOuputOOBActionValue(sSelectOutputActionsFromBitMask);
            bArr[4] = (byte) this.j;
        } else {
            bArr[2] = 0;
            bArr[3] = 0;
            bArr[4] = 0;
        }
        return bArr;
    }

    public void a(String str) {
        if (str != null) {
            ProvisioningConfirmationState provisioningConfirmationState = (ProvisioningConfirmationState) this.n;
            provisioningConfirmationState.setPin(str);
            provisioningConfirmationState.executeSend();
        }
    }

    public final boolean a(byte[] bArr) {
        return ((ProvisioningConfirmationState) this.n).parseData(bArr);
    }

    public final byte[] a(byte[] bArr, byte[] bArr2) {
        byte[] bArr3 = {(byte) this.e};
        byte[] bArrA = a();
        byte[] bArrB = b();
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(bArr3.length + bArrA.length + bArrB.length + bArr.length + bArr2.length);
        byteBufferAllocate.put(bArr3);
        byteBufferAllocate.put(bArrA);
        byteBufferAllocate.put(bArrB);
        byteBufferAllocate.put(bArr);
        byteBufferAllocate.put(bArr2);
        return byteBufferAllocate.array();
    }

    public final byte[] a() {
        int i = this.g;
        int i2 = this.k;
        int i3 = this.m;
        return new byte[]{(byte) this.f, (byte) ((i >> 8) & 255), (byte) (i & 255), (byte) this.h, (byte) this.i, (byte) this.j, (byte) ((i2 >> 8) & 255), (byte) (i2 & 255), (byte) this.l, (byte) ((i3 >> 8) & 255), (byte) (i3 & 255)};
    }

    public void a(p pVar) {
        this.f2202d = pVar;
    }
}
