package com.alibaba.ailabs.iot.mesh.task.bean;

import a.a.a.a.b.j.a.a;
import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.ailabs.iot.mesh.callback.IActionListener;

/* JADX INFO: loaded from: classes.dex */
public class MeshControlDevice implements Parcelable {
    public static final Parcelable.Creator<MeshControlDevice> CREATOR = new a();

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public int f2818a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    public int f2819b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    public int f2820c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    public int f2821d;
    public byte[] e;
    public IActionListener f;
    public boolean g;

    public MeshControlDevice() {
    }

    public void a(int i) {
        this.f2819b = i;
    }

    public int b() {
        return this.f2819b;
    }

    public int c() {
        return this.f2820c;
    }

    public void d(int i) {
        this.f2818a = i;
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public byte[] e() {
        return this.e;
    }

    public int f() {
        return this.f2818a;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.f2818a);
        parcel.writeInt(this.f2819b);
        parcel.writeInt(this.f2820c);
        parcel.writeInt(this.f2821d);
        parcel.writeByteArray(this.e);
        parcel.writeByte(this.g ? (byte) 1 : (byte) 0);
    }

    public MeshControlDevice(Parcel parcel) {
        this.f2818a = parcel.readInt();
        this.f2819b = parcel.readInt();
        this.f2820c = parcel.readInt();
        this.f2821d = parcel.readInt();
        this.e = parcel.createByteArray();
        this.g = parcel.readByte() != 0;
    }

    public void a(byte[] bArr) {
        this.e = bArr;
    }

    public void b(int i) {
        this.f2820c = i;
    }

    public void c(int i) {
        this.f2821d = i;
    }

    public int d() {
        return this.f2821d;
    }

    public void a(boolean z) {
        this.g = z;
    }

    public void a(IActionListener iActionListener) {
        this.f = iActionListener;
    }

    public IActionListener a() {
        return this.f;
    }
}
