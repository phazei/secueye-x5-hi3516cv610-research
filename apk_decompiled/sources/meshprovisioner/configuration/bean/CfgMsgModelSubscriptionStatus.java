package meshprovisioner.configuration.bean;

import android.content.Context;
import android.util.Pair;
import com.alibaba.ailabs.iot.mesh.R;
import meshprovisioner.configuration.ConfigModelSubscriptionStatus;

/* JADX INFO: loaded from: classes4.dex */
public class CfgMsgModelSubscriptionStatus {
    public byte[] elementAddress;
    public boolean isSuccessful;
    public byte[] mSubscriptionAddress;
    public byte[] modelIdentifier;
    public int status;
    public String statusMessage;

    /* JADX INFO: renamed from: meshprovisioner.configuration.bean.CfgMsgModelSubscriptionStatus$1, reason: invalid class name */
    static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus = new int[ConfigModelSubscriptionStatus.SubscriptionStatus.values().length];

        static {
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.SUCCESS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.INVALID_ADDRESS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.INVALID_MODEL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.INVALID_APPKEY_INDEX.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.INVALID_NETKEY_INDEX.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.INSUFFICIENT_RESOURCES.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.KEY_INDEX_ALREADY_STORED.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.INVALID_PUBLISH_PARAMETERS.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.NOT_A_SUBSCRIBE_MODEL.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.STORAGE_FAILURE.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.FEATURE_NOT_SUPPORTED.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.CANNOT_UPDATE.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.CANNOT_REMOVE.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.CANNOT_BIND.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.TEMPORARILY_UNABLE_TO_CHANGE_STATE.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.CANNOT_SET.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.UNSPECIFIED_ERROR.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.INVALID_BINDING.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.RFU.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
        }
    }

    public CfgMsgModelSubscriptionStatus(int i, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        this.status = i;
        this.elementAddress = bArr;
        this.mSubscriptionAddress = bArr2;
        this.mSubscriptionAddress = bArr3;
        this.isSuccessful = i == 0;
    }

    public static Pair<Integer, Object> parseStatus(Context context, int i) {
        switch (AnonymousClass1.$SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.fromStatusCode(i).ordinal()]) {
            case 1:
                return new Pair<>(0, true);
            case 2:
                return new Pair<>(-60, context.getString(R.string.status_invalid_address));
            case 3:
                return new Pair<>(-61, context.getString(R.string.status_invalid_model));
            case 4:
                return new Pair<>(-62, context.getString(R.string.status_invalid_appkey_index));
            case 5:
                return new Pair<>(-63, context.getString(R.string.status_invalid_netkey_index));
            case 6:
                return new Pair<>(-64, context.getString(R.string.status_insufficient_resources));
            case 7:
                return new Pair<>(-65, context.getString(R.string.status_key_index_already_stored));
            case 8:
                return new Pair<>(-66, context.getString(R.string.status_invalid_publish_parameters));
            case 9:
                return new Pair<>(-67, context.getString(R.string.status_not_a_subscribe_model));
            case 10:
                return new Pair<>(-68, context.getString(R.string.status_storage_failure));
            case 11:
                return new Pair<>(-69, context.getString(R.string.status_feature_not_supported));
            case 12:
                return new Pair<>(-70, context.getString(R.string.status_cannot_update));
            case 13:
                return new Pair<>(-71, context.getString(R.string.status_cannot_remove));
            case 14:
                return new Pair<>(-72, context.getString(R.string.status_cannot_bind));
            case 15:
                return new Pair<>(-73, context.getString(R.string.status_temporarily_unable_to_change_state));
            case 16:
                return new Pair<>(-74, context.getString(R.string.status_cannot_set));
            case 17:
                return new Pair<>(-75, context.getString(R.string.status_unspecified_error));
            case 18:
                return new Pair<>(-76, context.getString(R.string.status_success_invalid_binding));
            default:
                return new Pair<>(-77, context.getString(R.string.app_key_status_rfu));
        }
    }

    public static String parseStatusMessage(Context context, int i) {
        switch (AnonymousClass1.$SwitchMap$meshprovisioner$configuration$ConfigModelSubscriptionStatus$SubscriptionStatus[ConfigModelSubscriptionStatus.SubscriptionStatus.fromStatusCode(i).ordinal()]) {
            case 1:
                return context.getString(R.string.model_subscription_success);
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

    public byte[] getElementAddress() {
        return this.elementAddress;
    }

    public byte[] getModelIdentifier() {
        return this.modelIdentifier;
    }

    public int getStatus() {
        return this.status;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public byte[] getSubscriptionAddress() {
        return this.mSubscriptionAddress;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    public void setElementAddress(byte[] bArr) {
        this.elementAddress = bArr;
    }

    public void setModelIdentifier(byte[] bArr) {
        this.modelIdentifier = bArr;
    }

    public void setStatus(int i) {
        this.status = i;
    }

    public void setStatusMessage(String str) {
        this.statusMessage = str;
    }

    public void setSubscriptionAddress(byte[] bArr) {
        this.mSubscriptionAddress = bArr;
    }

    public void setSuccessful(boolean z) {
        this.isSuccessful = z;
    }
}
