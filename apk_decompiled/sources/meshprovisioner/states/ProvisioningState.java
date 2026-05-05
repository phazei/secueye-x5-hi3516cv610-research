package meshprovisioner.states;

/* JADX INFO: loaded from: classes4.dex */
public abstract class ProvisioningState {
    public static final byte TYPE_PROVISIONING_CAPABILITIES = 1;
    public static final byte TYPE_PROVISIONING_COMPLETE = 8;
    public static final byte TYPE_PROVISIONING_CONFIRMATION = 5;
    public static final byte TYPE_PROVISIONING_DATA = 7;
    public static final byte TYPE_PROVISIONING_INPUT_COMPLETE = 4;
    public static final byte TYPE_PROVISIONING_INVITE = 0;
    public static final byte TYPE_PROVISIONING_PUBLIC_KEY = 3;
    public static final byte TYPE_PROVISIONING_RANDOM_CONFIRMATION = 6;
    public static final byte TYPE_PROVISIONING_START = 2;

    public enum State {
        PROVISIONING_INVITE(0),
        PROVISIONING_CAPABILITIES(1),
        PROVISIONING_START(2),
        PROVISIONING_PUBLIC_KEY(3),
        PROVISINING_INPUT_COMPLETE(4),
        PROVISIONING_CONFIRMATION(5),
        PROVISINING_RANDOM(6),
        PROVISINING_DATA(7),
        PROVISINING_COMPLETE(8),
        PROVISINING_FAILED(9);

        public int state;

        State(int i) {
            this.state = i;
        }

        public int getState() {
            return this.state;
        }
    }

    public abstract void executeSend();

    public abstract State getState();

    public abstract boolean parseData(byte[] bArr);
}
