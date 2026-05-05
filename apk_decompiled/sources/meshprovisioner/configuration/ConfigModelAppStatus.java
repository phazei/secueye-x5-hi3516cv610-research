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
import meshprovisioner.utils.AddressUtils;
import meshprovisioner.utils.MeshParserUtils;

/* JADX INFO: loaded from: classes4.dex */
public final class ConfigModelAppStatus extends ConfigMessageState {
    public static final int CONFIG_MODEL_APP_BIND_STATUS_SIG_MODEL = 9;
    public static final int CONFIG_MODEL_APP_BIND_STATUS_VENDOR_MODEL = 11;
    public static final String TAG = "ConfigModelAppStatus";
    public byte[] appKeyIndex;
    public byte[] elementAddress;
    public boolean isSuccessful;
    public byte[] modelIdentifier;
    public final int previiousMessageType;
    public int status;
    public String statusMessage;

    /* JADX INFO: renamed from: meshprovisioner.configuration.ConfigModelAppStatus$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses = new int[AppKeyBindStatuses.values().length];

        static {
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.INVALID_ADDRESS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.INVALID_MODEL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.INVALID_APPKEY_INDEX.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.INVALID_NETKEY_INDEX.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.INSUFFICIENT_RESOURCES.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.KEY_INDEX_ALREADY_STORED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.INVALID_PUBLISH_PARAMETERS.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.NOT_A_SUBSCRIBE_MODEL.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.STORAGE_FAILURE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.FEATURE_NOT_SUPPORTED.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.CANNOT_UPDATE.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.CANNOT_REMOVE.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.CANNOT_BIND.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.TEMPORARILY_UNABLE_TO_CHANGE_STATE.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.CANNOT_SET.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.UNSPECIFIED_ERROR.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.INVALID_BINDING.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.RFU.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
        }
    }

    public enum AppKeyBindStatuses {
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

        AppKeyBindStatuses(int i) {
            this.statusCode = i;
        }

        public static AppKeyBindStatuses fromStatusCode(int i) {
            for (AppKeyBindStatuses appKeyBindStatuses : values()) {
                if (appKeyBindStatuses.getStatusCode() == i) {
                    return appKeyBindStatuses;
                }
            }
            throw new IllegalArgumentException("Enum not found in AppBindStatus");
        }

        public final int getStatusCode() {
            return this.statusCode;
        }
    }

    public ConfigModelAppStatus(Context context, ProvisionedMeshNode provisionedMeshNode, int i, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
        this.messageType = i;
        this.previiousMessageType = i;
    }

    private void parseStatus(int i) {
        if (AnonymousClass1.$SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.fromStatusCode(i).ordinal()] != 1) {
            return;
        }
        this.isSuccessful = true;
    }

    public static String parseStatusMessage(Context context, int i) {
        switch (AnonymousClass1.$SwitchMap$meshprovisioner$configuration$ConfigModelAppStatus$AppKeyBindStatuses[AppKeyBindStatuses.fromStatusCode(i).ordinal()]) {
            case 1:
                return context.getString(R.string.app_key_bind_status_success);
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

    public int getAppKeyIndexInt() {
        return ByteBuffer.wrap(this.appKeyIndex).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public byte[] getElementAddress() {
        return this.elementAddress;
    }

    public int getElementAddressInt() {
        return ByteBuffer.wrap(this.elementAddress).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public byte[] getModelIdentifier() {
        return this.modelIdentifier;
    }

    public int getModelIdentifierInt() {
        byte[] bArr = this.modelIdentifier;
        return bArr.length == 2 ? ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort() : ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    @Override // meshprovisioner.configuration.ConfigMessageState, meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.CONFIG_MODEL_APP_STATUS_STATE;
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
            if (MeshParserUtils.getOpCode(bArrU, ((bArrU[0] >> 7) & 1) + 1) == -32706) {
                a.a(TAG, "Received app key bind status");
                ByteBuffer byteBufferOrder = ByteBuffer.wrap(bArrU).order(ByteOrder.LITTLE_ENDIAN);
                byteBufferOrder.position(2);
                this.status = byteBufferOrder.get();
                this.elementAddress = new byte[]{bArrU[4], bArrU[3]};
                this.appKeyIndex = new byte[]{(byte) (bArrU[6] & 15), bArrU[5]};
                if (bArrU.length == 9) {
                    this.modelIdentifier = new byte[]{bArrU[8], bArrU[7]};
                } else {
                    this.modelIdentifier = new byte[]{bArrU[8], bArrU[7], bArrU[10], bArrU[9]};
                }
                this.statusMessage = parseStatusMessage(this.mContext, this.status);
                parseStatus(this.status);
                a.a(TAG, "Status: " + this.isSuccessful);
                if (this.previiousMessageType == 32829) {
                    if (this.isSuccessful) {
                        this.mProvisionedMeshNode.setAppKeyBindStatus(this);
                        this.statusMessage = "App key was successfully bound";
                        a.a(TAG, "Status message: " + this.statusMessage);
                    }
                } else if (this.isSuccessful) {
                    this.mProvisionedMeshNode.setAppKeyUnbindStatus(this);
                    this.statusMessage = "App key was successfully unbound";
                    a.a(TAG, "Status message: " + this.statusMessage);
                }
                a.a(TAG, "App key index: " + MeshParserUtils.bytesToHex(this.appKeyIndex, false));
                a.a(TAG, "Model Identifier: " + MeshParserUtils.bytesToHex(this.modelIdentifier, false));
                this.mMeshStatusCallbacks.onAppKeyBindStatusReceived(this.mProvisionedMeshNode, this.isSuccessful, this.status, AddressUtils.getUnicastAddressInt(this.elementAddress), getAppKeyIndexInt(), getModelIdentifierInt());
                this.mInternalTransportCallbacks.a(this.mProvisionedMeshNode);
                return true;
            }
            a.a(TAG, "Unknown pdu received!");
            this.mMeshStatusCallbacks.onUnknownPduReceived(this.mProvisionedMeshNode);
        } else {
            parseControlMessage((b) pdu, this.mPayloads.size());
        }
        return false;
    }

    @Override // b.f.f
    public void sendSegmentAcknowledgementMessage(b bVar) {
        b bVarCreateSegmentBlockAcknowledgementMessage = this.mMeshTransport.createSegmentBlockAcknowledgementMessage(bVar);
        a.a(TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }
}
