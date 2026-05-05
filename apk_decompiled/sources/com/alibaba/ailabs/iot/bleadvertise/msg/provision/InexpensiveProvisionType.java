package com.alibaba.ailabs.iot.bleadvertise.msg.provision;

/* JADX INFO: loaded from: classes.dex */
public enum InexpensiveProvisionType {
    PROVISION_RANDOM((byte) 0),
    PROVISION_CONFIRMATION((byte) 1),
    PROVISION_DATA((byte) 2),
    PROVISION_COMPLETE((byte) 3),
    PROVISION_CONFIG_MSG((byte) 4),
    PROVISION_CONFIG_ACK((byte) 5),
    PROVISIONING_ADD_APPKEY((byte) 9);

    public byte type;

    InexpensiveProvisionType(byte b2) {
        this.type = b2;
    }

    public byte getType() {
        return this.type;
    }
}
