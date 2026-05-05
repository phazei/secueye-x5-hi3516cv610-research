package com.alibaba.ailabs.iot.mesh.provision.state;

/* JADX INFO: loaded from: classes.dex */
public enum AbsFastProvisionState$State {
    PROVISION_CONFIG_SEND(0),
    PROVISION__CONFIG_RESP_RECEIVED(1),
    CONFIRMATION_KEY_AND_RANDOM_SEND_TO_CLOUD(2),
    CONFIRMATION_CLOUD_RECEIVED(3),
    BROADCASTING_RANDOMS(4),
    CONFIRMATION_DEVICE_RECEIVED(5),
    CONFIRMATION_DEVICE_SEND_TO_CLOUD(6),
    DATA_VERIFY_SUCCESS_FROM_CLOUD(7),
    ENCRYPTED_PROVISION_MSG_SEND(8),
    ENCRYPTED_PROVISION_MSG_RSP_RECEIVE(9),
    CONTROL_MSG_SEND(10),
    CONTROL_MSG_RESP_RECEIVE(11),
    CONFIG_INFO_RECEIVED(12),
    ADD_APP_KEY_SEND(13),
    ADD_APP_KEY_RESP_RECEIVED(14),
    PROVISIONING_COMPLETE(15),
    PROVISIONING_FAILED(16);

    public int state;

    AbsFastProvisionState$State(int i) {
        this.state = i;
    }

    public int getState() {
        return this.state;
    }
}
