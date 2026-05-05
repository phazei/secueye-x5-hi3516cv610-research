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
public class ConfigModelPublicationStatus extends ConfigMessageState {
    public static final int CONFIG_MODEL_APP_BIND_STATUS_VENDOR_MODEL_PDU_LENGTH = 16;
    public static final int CONFIG_MODEL_PUBLICATION_STATUS_SIG_MODEL_PDU_LENGTH = 14;
    public static final String TAG = ConfigModelAppStatus.class.getSimpleName();
    public byte[] appKeyIndex;
    public int credentialFlag;
    public byte[] elementAddress;
    public boolean isSuccessful;
    public byte[] modelIdentifier;
    public byte[] publishAddress;
    public int publishPeriod;
    public int publishRetransmitCount;
    public int publishRetransmitIntervalSteps;
    public int publishTtl;
    public int status;
    public String statusMessage;

    /* JADX INFO: renamed from: meshprovisioner.configuration.ConfigModelPublicationStatus$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus = new int[PublicationStatus.values().length];

        static {
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.INVALID_ADDRESS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.INVALID_MODEL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.INVALID_APPKEY_INDEX.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.INVALID_NETKEY_INDEX.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.INSUFFICIENT_RESOURCES.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.KEY_INDEX_ALREADY_STORED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.INVALID_PUBLISH_PARAMETERS.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.NOT_A_SUBSCRIBE_MODEL.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.STORAGE_FAILURE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.FEATURE_NOT_SUPPORTED.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.CANNOT_UPDATE.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.CANNOT_REMOVE.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.CANNOT_BIND.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.TEMPORARILY_UNABLE_TO_CHANGE_STATE.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.CANNOT_SET.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.UNSPECIFIED_ERROR.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.INVALID_BINDING.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.RFU.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
        }
    }

    public enum PublicationStatus {
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

        PublicationStatus(int i) {
            this.statusCode = i;
        }

        public static PublicationStatus fromStatusCode(int i) {
            for (PublicationStatus publicationStatus : values()) {
                if (publicationStatus.getStatusCode() == i) {
                    return publicationStatus;
                }
            }
            throw new IllegalArgumentException("Enum not found in AppKeyStatus");
        }

        public final int getStatusCode() {
            return this.statusCode;
        }
    }

    public ConfigModelPublicationStatus(Context context, ProvisionedMeshNode provisionedMeshNode, InterfaceC0369c interfaceC0369c) {
        super(context, provisionedMeshNode, interfaceC0369c);
    }

    private int getElementAddressInt() {
        return ByteBuffer.wrap(this.elementAddress).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    private int getModelIdentifierInt() {
        byte[] bArr = this.modelIdentifier;
        return bArr.length == 2 ? ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort() : ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getInt();
    }

    private void parseStatus(int i) {
        if (AnonymousClass1.$SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.fromStatusCode(i).ordinal()] != 1) {
            return;
        }
        this.isSuccessful = true;
    }

    public static String parseStatusMessage(Context context, int i) {
        switch (AnonymousClass1.$SwitchMap$meshprovisioner$configuration$ConfigModelPublicationStatus$PublicationStatus[PublicationStatus.fromStatusCode(i).ordinal()]) {
            case 1:
                return context.getString(R.string.publish_address_status_success);
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

    public int getCredentialFlag() {
        return this.credentialFlag;
    }

    public byte[] getElementAddress() {
        return this.elementAddress;
    }

    public byte[] getModelIdentifier() {
        return this.modelIdentifier;
    }

    public byte[] getPublicationAppKeyIndex() {
        return this.appKeyIndex;
    }

    public byte[] getPublishAddress() {
        return this.publishAddress;
    }

    public int getPublishAddressInt() {
        return ByteBuffer.wrap(this.publishAddress).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public int getPublishPeriod() {
        return this.publishPeriod;
    }

    public int getPublishRetransmitCount() {
        return this.publishRetransmitCount;
    }

    public int getPublishRetransmitIntervalSteps() {
        return this.publishRetransmitIntervalSteps;
    }

    public int getPublishTtl() {
        return this.publishTtl;
    }

    @Override // meshprovisioner.configuration.ConfigMessageState, meshprovisioner.configuration.MeshMessageState
    public MeshMessageState.MessageState getState() {
        return MeshMessageState.MessageState.CONFIG_MODEL_PUBLICATION_STATUS_STATE;
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
            if (MeshParserUtils.getOpCode(bArrU, ((bArrU[0] >> 7) & 1) + 1) == -32743) {
                a.a(TAG, "Received model publication status");
                this.status = bArrU[2];
                this.elementAddress = new byte[]{bArrU[4], bArrU[3]};
                this.publishAddress = new byte[]{bArrU[6], bArrU[5]};
                this.appKeyIndex = new byte[]{(byte) (bArrU[8] & 15), bArrU[7]};
                this.credentialFlag = (bArrU[8] & 240) >> 4;
                this.publishTtl = bArrU[9];
                this.publishPeriod = bArrU[10];
                this.publishRetransmitCount = bArrU[11] >> 5;
                this.publishRetransmitIntervalSteps = bArrU[11] & 31;
                if (bArrU.length == 14) {
                    this.modelIdentifier = new byte[]{bArrU[13], bArrU[12]};
                } else {
                    this.modelIdentifier = new byte[]{bArrU[13], bArrU[12], bArrU[15], bArrU[14]};
                }
                this.statusMessage = parseStatusMessage(this.mContext, this.status);
                parseStatus(this.status);
                a.a(TAG, "Status: " + this.status);
                a.a(TAG, "Status message: " + this.statusMessage);
                a.a(TAG, "Element Address: " + MeshParserUtils.bytesToHex(this.elementAddress, false));
                a.a(TAG, "Publish Address: " + MeshParserUtils.bytesToHex(this.publishAddress, false));
                a.a(TAG, "App key index: " + MeshParserUtils.bytesToHex(this.appKeyIndex, false));
                a.a(TAG, "Credential Flag: " + this.credentialFlag);
                a.a(TAG, "Publish TTL: " + this.publishTtl);
                a.a(TAG, "Publish Period: " + this.publishPeriod);
                a.a(TAG, "Publish Retransmit Count: " + this.publishRetransmitCount);
                a.a(TAG, "Publish Publish Interval Steps: " + this.publishRetransmitIntervalSteps);
                a.a(TAG, "Model Identifier: " + getModelIdentifierInt());
                if (this.isSuccessful) {
                    this.mProvisionedMeshNode.getElements().get(Integer.valueOf(getElementAddressInt())).getMeshModels().get(Integer.valueOf(getModelIdentifierInt())).setPublicationStatus(this);
                }
                this.mMeshStatusCallbacks.onPublicationStatusReceived(this.mProvisionedMeshNode, this.isSuccessful, this.status, this.elementAddress, this.publishAddress, getModelIdentifierInt());
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
        a.a(TAG, "Sending acknowledgement: " + MeshParserUtils.bytesToHex(bVarCreateSegmentBlockAcknowledgementMessage.m().get(0), false));
        this.mInternalTransportCallbacks.sendPdu(this.mProvisionedMeshNode, bVarCreateSegmentBlockAcknowledgementMessage.m().get(0));
        this.mMeshStatusCallbacks.onBlockAcknowledgementSent(this.mProvisionedMeshNode);
    }
}
