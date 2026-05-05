package meshprovisioner.control;

/* JADX INFO: loaded from: classes4.dex */
public abstract class TransportControlMessage {

    public enum TransportControlMessageState {
        LOWER_TRANSPORT_BLOCK_ACKNOWLEDGEMENT(0);

        public int state;

        TransportControlMessageState(int i) {
            this.state = i;
        }

        public int getState() {
            return this.state;
        }
    }

    public abstract TransportControlMessageState a();
}
