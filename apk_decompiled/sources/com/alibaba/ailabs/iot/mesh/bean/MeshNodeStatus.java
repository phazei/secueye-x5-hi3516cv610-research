package com.alibaba.ailabs.iot.mesh.bean;

/* JADX INFO: loaded from: classes.dex */
public enum MeshNodeStatus {
    PROVISIONING_INVITE(0),
    PROVISIONING_CAPABILITIES(1),
    PROVISIONING_START(2),
    PROVISIONING_PUBLIC_KEY_SENT(3),
    PROVISIONING_PUBLIC_KEY_RECEIVED(4),
    PROVISIONING_AUTHENTICATION_INPUT_WAITING(5),
    PROVISIONING_AUTHENTICATION_INPUT_ENTERED(6),
    PROVISIONING_INPUT_COMPLETE(7),
    PROVISIONING_CONFIRMATION_SENT(8),
    PROVISIONING_CONFIRMATION_RECEIVED(9),
    PROVISIONING_RANDOM_SENT(10),
    PROVISIONING_RANDOM_RECEIVED(11),
    PROVISIONING_DATA_SENT(12),
    PROVISIONING_COMPLETE(13),
    PROVISIONING_FAILED(14),
    COMPOSITION_DATA_GET_SENT(15),
    COMPOSITION_DATA_STATUS_RECEIVED(16),
    SENDING_BLOCK_ACKNOWLEDGEMENT(17),
    SENDING_APP_KEY_ADD(18),
    BLOCK_ACKNOWLEDGEMENT_RECEIVED(19),
    APP_KEY_STATUS_RECEIVED(20),
    APP_BIND_SENT(21),
    APP_UNBIND_SENT(22),
    APP_BIND_STATUS_RECEIVED(23),
    PUBLISH_ADDRESS_SET_SENT(24),
    PUBLISH_ADDRESS_STATUS_RECEIVED(25),
    SUBSCRIPTION_ADD_SENT(26),
    SUBSCRIPTION_DELETE_SENT(27),
    SUBSCRIPTION_STATUS_RECEIVED(28),
    NODE_RESET_STATUS_RECEIVED(29),
    REQUEST_FAILED(30),
    COMBO_WIFI_CONFIG_STATUS(31);

    public int state;

    MeshNodeStatus(int i) {
        this.state = i;
    }

    public static MeshNodeStatus fromStatusCode(int i) {
        for (MeshNodeStatus meshNodeStatus : values()) {
            if (meshNodeStatus.getState() == i) {
                return meshNodeStatus;
            }
        }
        throw new IllegalStateException("Invalid state");
    }

    public int getState() {
        return this.state;
    }
}
