package com.alibaba.ailabs.iot.mesh.provision.state;

/* JADX INFO: loaded from: classes.dex */
public abstract class FastProvisioningState {

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
        PROVISINING_FAILED(9),
        PROVISIONING_ADDAPPKEY(10);

        public int state;

        State(int i) {
            this.state = i;
        }

        public int getState() {
            return this.state;
        }
    }

    public abstract void a();

    public abstract boolean a(byte[] bArr);

    public abstract State b();
}
