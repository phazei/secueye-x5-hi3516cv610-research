package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import b.InterfaceC0369c;
import b.InterfaceC0370d;
import b.d.b;
import b.d.c;
import b.f.f;
import b.q;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import meshprovisioner.control.TransportControlMessage;
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public abstract class MeshMessageState implements f {
    public static final String TAG = "MeshMessageState";
    public boolean isIncompleteTimerExpired;
    public int mAppKeyIndex;
    public final Context mContext;
    public InterfaceC0370d mInternalTransportCallbacks;
    public MeshModel mMeshModel;
    public q mMeshStatusCallbacks;
    public final MeshTransport mMeshTransport;
    public final ProvisionedMeshNode mProvisionedMeshNode;
    public final byte[] mSrc;
    public final InterfaceC0369c meshMessageHandlerCallbacks;
    public c message;
    public int messageType;
    public final Map<Integer, byte[]> mPayloads = new HashMap();
    public final List<Integer> mRetransmitPayloads = new ArrayList();
    public int fastProvisionSendCallbackCount = 0;
    public Handler mMainThreadHandler = new Handler(Looper.getMainLooper());

    /* JADX INFO: renamed from: meshprovisioner.configuration.MeshMessageState$2, reason: invalid class name */
    static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$meshprovisioner$control$TransportControlMessage$TransportControlMessageState = new int[TransportControlMessage.TransportControlMessageState.values().length];

        static {
            try {
                $SwitchMap$meshprovisioner$control$TransportControlMessage$TransportControlMessageState[TransportControlMessage.TransportControlMessageState.LOWER_TRANSPORT_BLOCK_ACKNOWLEDGEMENT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public enum MessageState {
        COMPOSITION_DATA_GET_STATE(32776),
        COMPOSITION_DATA_STATUS_STATE(2),
        APP_KEY_ADD_STATE(0),
        APP_KEY_STATUS_STATE(-32765),
        CONFIG_MODEL_APP_BIND_STATE(32829),
        CONFIG_MODEL_APP_UNBIND_STATE(32831),
        CONFIG_MODEL_APP_STATUS_STATE(-32706),
        CONFIG_MODEL_PUBLICATION_SET_STATE(3),
        CONFIG_MODEL_PUBLICATION_STATUS_STATE(-32743),
        CONFIG_MODEL_SUBSCRIPTION_ADD_STATE(32795),
        CONFIG_MODEL_SUBSCRIPTION_DELETE_STATE(32796),
        CONFIG_MODEL_SUBSCRIPTION_STATUS_STATE(-32737),
        CONFIG_NODE_RESET_STATE(32841),
        CONFIG_NODE_RESET_STATUS_STATE(-32694),
        GENERIC_ON_OFF_GET_STATE(33281),
        GENERIC_ON_OFF_SET_STATE(33282),
        GENERIC_ON_OFF_SET_UNACKNOWLEDGED_STATE(33283),
        GENERIC_ON_OFF_STATUS_STATE(-32252),
        GENERIC_LEVEL_GET_STATE(33285),
        GENERIC_LEVEL_SET_STATE(33286),
        GENERIC_LEVEL_SET_UNACKNOWLEDGED_STATE(33287),
        GENERIC_LEVEL_STATUS_STATE(-32248);

        public int state;

        MessageState(int i) {
            this.state = i;
        }

        public int getState() {
            return this.state;
        }
    }

    public MeshMessageState(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c) {
        this.mContext = context;
        this.mProvisionedMeshNode = provisionedMeshNode;
        this.meshMessageHandlerCallbacks = interfaceC0369c;
        this.mSrc = this.mProvisionedMeshNode.getConfigurationSrc();
        this.mMeshTransport = new MeshTransport(context, provisionedMeshNode);
        this.mMeshTransport.setLowerTransportLayerCallbacks(this);
    }

    public void executeResend() {
        if (this.mPayloads.isEmpty() || this.mRetransmitPayloads.isEmpty()) {
            return;
        }
        for (int i = 0; i < this.mRetransmitPayloads.size(); i++) {
            int iIntValue = this.mRetransmitPayloads.get(i).intValue();
            if (this.mPayloads.containsKey(Integer.valueOf(iIntValue))) {
                byte[] bArr = this.mPayloads.get(Integer.valueOf(iIntValue));
                a.a(TAG, "Resending segment " + iIntValue + " : " + MeshParserUtils.bytesToHex(bArr, false));
                this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, this.mMeshTransport.createRetransmitMeshMessage(this.message, iIntValue).m().get(Integer.valueOf(iIntValue)));
            }
        }
    }

    public void executeSend() {
        a.a(TAG, ", payloadSize = " + this.mPayloads.size());
        if (this.mPayloads.isEmpty()) {
            return;
        }
        if (a.a.a.a.b.d.a.f1317c) {
            new Thread(new Runnable() { // from class: meshprovisioner.configuration.MeshMessageState.1
                @Override // java.lang.Runnable
                public void run() {
                    for (int i = 0; i < MeshMessageState.this.mPayloads.size(); i++) {
                        try {
                            Thread.sleep(150L);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        final byte[] bArr = MeshMessageState.this.mPayloads.get(Integer.valueOf(i));
                        MeshMessageState.this.mMainThreadHandler.post(new Runnable() { // from class: meshprovisioner.configuration.MeshMessageState.1.1
                            @Override // java.lang.Runnable
                            public void run() {
                                MeshMessageState meshMessageState = MeshMessageState.this;
                                meshMessageState.mInternalTransportCallbacks.sendPdu(meshMessageState.mProvisionedMeshNode, bArr);
                            }
                        });
                    }
                }
            }).start();
            return;
        }
        for (int i = 0; i < this.mPayloads.size(); i++) {
            this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, this.mPayloads.get(Integer.valueOf(i)));
        }
    }

    public int getAppKeyIndex() {
        return this.mAppKeyIndex;
    }

    public int getFastProvisionSendCallbackCount() {
        return this.fastProvisionSendCallbackCount;
    }

    public MeshModel getMeshModel() {
        return this.mMeshModel;
    }

    public ProvisionedMeshNode getMeshNode() {
        return this.mProvisionedMeshNode;
    }

    public abstract MessageState getState();

    public void increaseFastProvisionSendCallbackCount() {
        this.fastProvisionSendCallbackCount++;
    }

    public boolean isFastProvisionSegmentsAllSend() {
        a.a(TAG, "fastProvisionSendCallbackCount = " + this.fastProvisionSendCallbackCount + ", payloadSize = " + this.mPayloads.size());
        return this.fastProvisionSendCallbackCount >= this.mPayloads.size();
    }

    public boolean isIncompleteTimerExpired() {
        return this.isIncompleteTimerExpired;
    }

    public final boolean isRetransmissionRequired(byte[] bArr) {
        parseMeshPdu(bArr);
        return !this.mRetransmitPayloads.isEmpty();
    }

    public boolean isSegmented() {
        return this.mPayloads.size() > 1;
    }

    @Override // b.f.f
    public void onIncompleteTimerExpired() {
        a.a(TAG, "Incomplete timer has expired, all segments were not received!");
        this.isIncompleteTimerExpired = true;
        InterfaceC0369c interfaceC0369c = this.meshMessageHandlerCallbacks;
        if (interfaceC0369c != null) {
            byte[] bArr = this.mSrc;
            interfaceC0369c.a(this.mProvisionedMeshNode, bArr, true);
            if (this.mMeshStatusCallbacks != null) {
                this.mMeshStatusCallbacks.onTransactionFailed(this.mProvisionedMeshNode, AddressUtils.getUnicastAddressInt(bArr), true);
            }
        }
    }

    public final void parseControlMessage(b bVar, int i) {
        TransportControlMessage transportControlMessageU = bVar.u();
        if (transportControlMessageU == null) {
            return;
        }
        if (AnonymousClass2.$SwitchMap$meshprovisioner$control$TransportControlMessage$TransportControlMessageState[transportControlMessageU.a().ordinal()] != 1) {
            a.a(TAG, "Unexpected control message received, ignoring message");
            this.mMeshStatusCallbacks.onUnknownPduReceived(this.mProvisionedMeshNode);
            return;
        }
        a.a(TAG, "Acknowledgement payload: " + MeshParserUtils.bytesToHex(bVar.v(), false));
        this.mRetransmitPayloads.clear();
        this.mRetransmitPayloads.addAll(b.a.a.a(bVar.v(), i));
        this.mMeshStatusCallbacks.onBlockAcknowledgementReceived(this.mProvisionedMeshNode);
    }

    public boolean parseMeshPdu(byte[] bArr) {
        return false;
    }

    public void setStatusCallbacks(q qVar) {
        this.mMeshStatusCallbacks = qVar;
    }

    public void setTransportCallbacks(InterfaceC0370d interfaceC0370d) {
        this.mInternalTransportCallbacks = interfaceC0370d;
    }
}
