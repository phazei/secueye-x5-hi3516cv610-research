package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import android.content.Context;
import b.InterfaceC0369c;
import b.d.b;
import b.d.c;
import com.alibaba.ailabs.iot.mesh.R;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import meshprovisioner.configuration.MeshMessageState;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public class ConfigAppKeyStatus extends ConfigMessageState {
    public static final String TAG = "ConfigAppKeyStatus";
    public String appKey;
    public byte[] appKeyIndex;
    public boolean isSuccessful;
    public byte[] netKeyIndex;
    public int status;
    public String statusMessage;

    /* JADX INFO: renamed from: meshprovisioner.configuration.ConfigAppKeyStatus$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses = new int[AppKeyStatuses.values().length];

        static {
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.INVALID_ADDRESS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.INVALID_MODEL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.INVALID_APPKEY_INDEX.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.INVALID_NETKEY_INDEX.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.INSUFFICIENT_RESOURCES.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.KEY_INDEX_ALREADY_STORED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.INVALID_PUBLISH_PARAMETERS.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.NOT_A_SUBSCRIBE_MODEL.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.STORAGE_FAILURE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.FEATURE_NOT_SUPPORTED.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.CANNOT_UPDATE.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.CANNOT_REMOVE.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.CANNOT_BIND.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.TEMPORARILY_UNABLE_TO_CHANGE_STATE.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.CANNOT_SET.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.UNSPECIFIED_ERROR.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.INVALID_BINDING.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.RFU.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
        }
    }

    public enum AppKeyStatuses {
        SUCCESS(0),
        INVALID_ADDRESS(1),
        INVALID_MODEL(2),
        INVALID_APPKEY_INDEX(3),
        INVALID_NETKEY_INDEX(4),
        INSUFFICIENT_RESOURCES(5),
        KEY_INDEX_ALREADY_STORED(6),
        INVALID_PUBLISH_PARAMETERS(7),
        NOT_A_SUBSCRIBE_MODEL(8),
        STORAGE_FAILURE(9),
        FEATURE_NOT_SUPPORTED(10),
        CANNOT_UPDATE(11),
        CANNOT_REMOVE(12),
        CANNOT_BIND(13),
        TEMPORARILY_UNABLE_TO_CHANGE_STATE(14),
        CANNOT_SET(15),
        UNSPECIFIED_ERROR(16),
        INVALID_BINDING(17),
        RFU(18);

        public final int statusCode;

        AppKeyStatuses(int i) {
            this.statusCode = i;
        }

        public static AppKeyStatuses fromStatusCode(int i) {
            for (AppKeyStatuses appKeyStatuses : values()) {
                if (appKeyStatuses.getStatusCode() == i) {
                    return appKeyStatuses;
                }
            }
            throw new IllegalArgumentException("Enum not found in AppKeyStatus");
        }

        public final int getStatusCode() {
            return this.statusCode;
        }
    }

    public ConfigAppKeyStatus(Context context, ProvisionedMeshNode provisionedMeshNode, byte[] bArr, String str, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.appKey = str;
    }

    private void parseConfigAppKeyStatus(byte[] bArr, int i) {
        this.status = bArr[i];
        parseStatus(this.status);
        this.statusMessage = parseStatusMessage(this.mContext, this.status);
        this.netKeyIndex = new byte[]{(byte) (bArr[4] & 15), bArr[3]};
        this.appKeyIndex = new byte[]{(byte) ((bArr[5] & 240) >> 4), (byte) (((bArr[4] & 240) >> 4) | (bArr[5] << 4))};
        a.a(TAG, "Status: " + this.status);
        a.a(TAG, "Status message: " + this.statusMessage);
        a.a(TAG, "Net key index: " + MeshParserUtils.bytesToHex(this.netKeyIndex, false));
        a.a(TAG, "App key index: " + MeshParserUtils.bytesToHex(this.appKeyIndex, false));
    }

    private void parseStatus(int i) {
        if (AnonymousClass1.$SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.fromStatusCode(i).ordinal()] != 1) {
            return;
        }
        this.isSuccessful = true;
    }

    public static String parseStatusMessage(Context context, int i) {
        switch (AnonymousClass1.$SwitchMap$meshprovisioner$configuration$ConfigAppKeyStatus$AppKeyStatuses[AppKeyStatuses.fromStatusCode(i).ordinal()]) {
            case 1:
                return context.getString(R.string.status_success);
            case 2:
                return context.getString(R.string.status_invalid_address);
            case 3:
                return context.getString(R.string.status_invalid_model);
            case 4:
                return context.getString(R.string.status_invalid_appkey_index);
            case 5:
                return context.getString(R.string.status_invalid_netkey_index);
            case 6:
                return context.getString(R.string.status_insufficient_resources);
            case 7:
                return context.getString(R.string.status_key_index_already_stored);
            case 8:
                return context.getString(R.string.status_invalid_publish_parameters);
            case 9:
                return context.getString(R.string.status_not_a_subscribe_model);
            case 10:
                return context.getString(R.string.status_storage_failure);
            case 11:
                return context.getString(R.string.status_feature_not_supported);
            case 12:
                return context.getString(R.string.status_cannot_update);
            case 13:
                return context.getString(R.string.status_cannot_remove);
            case 14:
                return context.getString(R.string.status_cannot_bind);
            case 15:
                return context.getString(R.string.status_temporarily_unable_to_change_state);
            case 16:
                return context.getString(R.string.status_cannot_set);
            case 17:
                return context.getString(R.string.status_unspecified_error);
            case 18:
                return context.getString(R.string.status_success_invalid_binding);
            default:
                return context.getString(R.string.app_key_status_rfu);
        }
    }

    @Override // meshprovisioner.configuration.ConfigMessageState, meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.APP_KEY_STATUS_STATE;
    }

    public int getStatus() {
        return this.status;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    @Override // meshprovisioner.configuration.MeshMessageState
    public final boolean parseMeshPdu(byte[] bArr) {
        c pdu = this.mMeshTransport.parsePdu(this.mSrc, bArr);
        if (pdu == null) {
            a.a(TAG, "Message reassembly may not be complete yet");
        } else if (pdu instanceof b.d.a) {
            byte[] bArrU = ((b.d.a) pdu).u();
            if (MeshParserUtils.getOpCode(bArrU, ((bArrU[0] >> 7) & 1) + 1) == -32765) {
                a.a(TAG, "Received config app key status");
                parseConfigAppKeyStatus(bArrU, 2);
                if (this.isSuccessful) {
                    this.mProvisionedMeshNode.setAddedAppKey(ByteBuffer.wrap(this.appKeyIndex).order(ByteOrder.BIG_ENDIAN).getShort(), this.appKey);
                }
                this.mMeshStatusCallbacks.onAppKeyStatusReceived(this.mProvisionedMeshNode, this.isSuccessful, this.status, ByteBuffer.wrap(this.netKeyIndex).order(ByteOrder.BIG_ENDIAN).getShort(), ByteBuffer.wrap(this.appKeyIndex).order(ByteOrder.BIG_ENDIAN).getShort());
                this.mInternalTransportCallbacks.a(this.mProvisionedMeshNode);
                return true;
            }
            this.mMeshStatusCallbacks.onUnknownPduReceived(this.mProvisionedMeshNode);
        } else {
            parseControlMessage((b) pdu, this.mPayloads.size());
        }
        return false;
    }

    @Override // b.f.f
    public void sendSegmentAcknowledgementMessage(b bVar) {
        b bVarCreateSegmentBlockAcknowledgementMessage = this.mMeshTransport.createSegmentBlockAcknowledgementMessage(bVar);
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        a.a(TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }
}
