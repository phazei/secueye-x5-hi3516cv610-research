package com.alibaba.ailabs.iot.mesh.contant;

/* JADX INFO: loaded from: classes.dex */
public enum MeshUtConst$MeshErrorEnum {
    NO_INITIALIZED_ERROR(5001, "Not initialized..."),
    NULL_PROVISION_INFO_ERROR(5002, "get_provisioninfo_request : provisionInfo is null"),
    CALLBACK_ERROR(5003, "gma callback error"),
    TIMEOUT_ERROR(5004, "timeout"),
    GET_PROVISION_4_MASTER_REQUEST_ERROR(5005, "get_provisioninfo4master_request_failed"),
    GET_PROVISION_REQUEST_ERROR(5006, "get_provisioninfo_request_failed"),
    DEVICE_NOT_SUPPORT_ERROR(5007, "Device not supported"),
    PROVISION_COMPLETE_REQUEST_ERROR(5008, "provisionComplete_request_failed"),
    PROVISION_CONFIRM_REQUEST_ERROR(5009, "provisionConfirm_request_failed"),
    PROVISION_AUTH_REQUEST_ERROR(5010, "provisionAuth_request_failed"),
    MESH_MANAGER_NOT_INITIALIZED_ERROR(5011, "mesh manager not initialized"),
    MESH_MANAGER_NOT_BIND_SERVICE_ERROR(5012, "TgMeshManager has not bond to service"),
    LOCATION_NOT_ENABLED_ERROR(5013, "Location not enabled"),
    BLUETOOTH_NOT_ENABLED_ERROR(5014, "Bluetooth disabled"),
    APPKEY_ADD_ERROR(5015, "appKey add status error"),
    APPKEY_BIND_ERROR(5016, "appKey bind status error"),
    CREATE_TINYMESH_CHANNEL_ERROR(5017, "Failed to establish connection to tinyMesh device"),
    ILLEGAL_PROVISION_DATA_RECEIVED(5018, "Illegal data received in provision progress"),
    INVALID_PROVISIONING_PARAMETER(5019, "invalid provisioning parameter"),
    GATT_CONNECT_TIMEOUT(5020, "gatt connection was not established successfully within 7 seconds"),
    BIND_API_FAILED(5021, "bind device failed");

    public int errorCode;
    public String errorMsg;

    MeshUtConst$MeshErrorEnum(int i, String str) {
        this.errorCode = i;
        this.errorMsg = str;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMsg() {
        return this.errorMsg;
    }
}
