package meshprovisioner.states;

import android.content.Context;
import com.alibaba.ailabs.iot.mesh.R;
import meshprovisioner.states.ProvisioningState;

/* JADX INFO: loaded from: classes4.dex */
public class ProvisioningFailedState extends ProvisioningState {
    public int error;
    public final Context mContext;
    public final UnprovisionedMeshNode mUnprovisionedMeshNode;

    /* JADX INFO: renamed from: meshprovisioner.states.ProvisioningFailedState$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode = new int[ProvisioningFailureCode.values().length];

        static {
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.PROHIBITED.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.INVALID_PDU.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.INVALID_FORMAT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.UNEXPECTED_PDU.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.CONFIRMATION_FAILED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.OUT_OF_RESOURCES.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.DECRYPTION_FAILED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.UNEXPECTED_ERROR.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.CANNOT_ASSIGN_ADDRESSES.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.UNKNOWN_ERROR_CODE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }

    public enum ProvisioningFailureCode {
        PROHIBITED(0),
        INVALID_PDU(1),
        INVALID_FORMAT(2),
        UNEXPECTED_PDU(3),
        CONFIRMATION_FAILED(4),
        OUT_OF_RESOURCES(5),
        DECRYPTION_FAILED(6),
        UNEXPECTED_ERROR(7),
        CANNOT_ASSIGN_ADDRESSES(8),
        UNKNOWN_ERROR_CODE(9);

        public final int errorCode;

        ProvisioningFailureCode(int i) {
            this.errorCode = i;
        }

        public static ProvisioningFailureCode fromErrorCode(int i) {
            for (ProvisioningFailureCode provisioningFailureCode : values()) {
                if (provisioningFailureCode.getErrorCode() == i) {
                    return provisioningFailureCode;
                }
            }
            return UNKNOWN_ERROR_CODE;
        }

        public final int getErrorCode() {
            return this.errorCode;
        }
    }

    public ProvisioningFailedState(Context context, UnprovisionedMeshNode unprovisionedMeshNode) {
        this.mContext = context;
        this.mUnprovisionedMeshNode = unprovisionedMeshNode;
    }

    public static String parseProvisioningFailure(Context context, int i) {
        switch (AnonymousClass1.$SwitchMap$meshprovisioner$states$ProvisioningFailedState$ProvisioningFailureCode[ProvisioningFailureCode.fromErrorCode(i).ordinal()]) {
            case 1:
                return context.getString(R.string.error_prohibited);
            case 2:
                return context.getString(R.string.error_invalid_pdu);
            case 3:
                return context.getString(R.string.error_invalid_format);
            case 4:
                return context.getString(R.string.error_prohibited);
            case 5:
                return context.getString(R.string.error_confirmation_failed);
            case 6:
                return context.getString(R.string.error_prohibited);
            case 7:
                return context.getString(R.string.error_decryption_failed);
            case 8:
                return context.getString(R.string.error_unexpected_error);
            case 9:
                return context.getString(R.string.error_cannot_assign_addresses);
            default:
                return context.getString(R.string.error_rfu);
        }
    }

    @Override // meshprovisioner.states.ProvisioningState
    public void executeSend() {
    }

    public int getErrorCode() {
        return this.error;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public ProvisioningState.State getState() {
        return ProvisioningState.State.PROVISINING_FAILED;
    }

    @Override // meshprovisioner.states.ProvisioningState
    public boolean parseData(byte[] bArr) {
        this.error = bArr[2];
        return true;
    }

    public void setErrorCode(ProvisioningFailureCode provisioningFailureCode) {
        this.error = provisioningFailureCode.getErrorCode();
    }
}
