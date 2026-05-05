package com.alibaba.ailabs.iot.mesh.bean;

import android.os.Parcel;
import android.os.Parcelable;

/* JADX INFO: loaded from: classes.dex */
public class MeshAccessPayload implements Parcelable {
    public static final Parcelable.Creator<MeshAccessPayload> CREATOR = new Parcelable.Creator<MeshAccessPayload>() { // from class: com.alibaba.ailabs.iot.mesh.bean.MeshAccessPayload.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MeshAccessPayload createFromParcel(Parcel parcel) {
            return new MeshAccessPayload(parcel);
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // android.os.Parcelable.Creator
        public MeshAccessPayload[] newArray(int i) {
            return new MeshAccessPayload[i];
        }
    };
    public String iotId;
    public byte[] netKey;
    public String opCode;
    public byte[] parameters;
    public int sequenceNo;
    public String translatedTSLDesc;

    public MeshAccessPayload() {
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public String getIotId() {
        return this.iotId;
    }

    public byte[] getNetKey() {
        return this.netKey;
    }

    public String getOpCode() {
        return this.opCode;
    }

    public byte[] getParameters() {
        return this.parameters;
    }

    public int getSequenceNo() {
        return this.sequenceNo;
    }

    public String getTranslatedTSLDesc() {
        return this.translatedTSLDesc;
    }

    public void setIotId(String str) {
        this.iotId = str;
    }

    public void setNetKey(byte[] bArr) {
        this.netKey = bArr;
    }

    public void setOpCode(String str) {
        this.opCode = str;
    }

    public void setParameters(byte[] bArr) {
        this.parameters = bArr;
    }

    public void setSequenceNo(int i) {
        this.sequenceNo = i;
    }

    public void setTranslatedTSLDesc(String str) {
        this.translatedTSLDesc = str;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.opCode);
        parcel.writeByteArray(this.netKey);
        parcel.writeByteArray(this.parameters);
        parcel.writeString(this.translatedTSLDesc);
        parcel.writeInt(this.sequenceNo);
        parcel.writeString(this.iotId);
    }

    public MeshAccessPayload(Parcel parcel) {
        this.opCode = parcel.readString();
        this.netKey = parcel.createByteArray();
        this.parameters = parcel.createByteArray();
        this.translatedTSLDesc = parcel.readString();
        this.sequenceNo = parcel.readInt();
        this.iotId = parcel.readString();
    }
}
