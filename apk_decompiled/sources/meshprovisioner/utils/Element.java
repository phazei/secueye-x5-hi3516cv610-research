package meshprovisioner.utils;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.core.view.MotionEventCompat;
import com.alibaba.fastjson.annotation.JSONField;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import meshprovisioner.configuration.MeshModel;

/* JADX INFO: loaded from: classes4.dex */
public class Element implements Parcelable {
    public static final Parcelable.Creator<Element> CREATOR = new Parcelable.Creator<Element>() { // from class: meshprovisioner.utils.Element.1
        @Override // android.os.Parcelable.Creator
        public Element createFromParcel(Parcel parcel) {
            return new Element(parcel);
        }

        @Override // android.os.Parcelable.Creator
        public Element[] newArray(int i) {
            return new Element[i];
        }
    };
    public byte[] elementAddress;
    public Set<Integer> flatSubscribedGroupAddress;
    public int locationDescriptor;
    public Map<Integer, MeshModel> meshModels;
    public int sigModelCount;
    public int vendorModelCount;

    public Element() {
        this.flatSubscribedGroupAddress = new HashSet();
        this.meshModels = new LinkedHashMap();
    }

    private void sortModels(HashMap<Integer, MeshModel> map) {
        ArrayList arrayList = new ArrayList(map.keySet());
        Collections.sort(arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            int iIntValue = ((Integer) it.next()).intValue();
            this.meshModels.put(Integer.valueOf(iIntValue), map.get(Integer.valueOf(iIntValue)));
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public byte[] getElementAddress() {
        return this.elementAddress;
    }

    @JSONField(deserialize = false)
    public int getElementAddressInt() {
        return ByteBuffer.wrap(this.elementAddress).order(ByteOrder.BIG_ENDIAN).getShort();
    }

    public Set<Integer> getFlatSubscribedGroupAddress() {
        return this.flatSubscribedGroupAddress;
    }

    public int getLocationDescriptor() {
        return this.locationDescriptor;
    }

    @JSONField(deserialize = false)
    public Map<Integer, MeshModel> getMeshModels() {
        return Collections.unmodifiableMap(this.meshModels);
    }

    public int getSigModelCount() {
        return this.sigModelCount;
    }

    public int getVendorModelCount() {
        return this.vendorModelCount;
    }

    public void setElementAddress(byte[] bArr) {
        this.elementAddress = bArr;
    }

    public void setLocationDescriptor(int i) {
        this.locationDescriptor = i;
    }

    public void setMeshModels(Map<Integer, MeshModel> map) {
        this.meshModels = map;
    }

    public void setSigModelCount(int i) {
        this.sigModelCount = i;
    }

    public void setVendorModelCount(int i) {
        this.vendorModelCount = i;
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByteArray(this.elementAddress);
        parcel.writeInt(this.locationDescriptor);
        parcel.writeInt(this.sigModelCount);
        parcel.writeInt(this.vendorModelCount);
        parcel.writeMap(this.meshModels);
    }

    public Element(byte[] bArr, int i, int i2, int i3, Map<Integer, MeshModel> map) {
        this.flatSubscribedGroupAddress = new HashSet();
        this.elementAddress = bArr;
        this.locationDescriptor = i;
        this.sigModelCount = i2;
        this.vendorModelCount = i3;
        this.meshModels = map;
        for (MeshModel meshModel : map.values()) {
            if (meshModel != null) {
                for (byte[] bArr2 : meshModel.getSubscriptionAddresses()) {
                    if (bArr2 != null && bArr2.length > 1) {
                        this.flatSubscribedGroupAddress.add(Integer.valueOf((bArr2[1] & 255) | ((bArr2[0] << 8) & MotionEventCompat.ACTION_POINTER_INDEX_MASK)));
                    }
                }
            }
        }
    }

    public Element(Parcel parcel) {
        this.flatSubscribedGroupAddress = new HashSet();
        this.elementAddress = parcel.createByteArray();
        this.locationDescriptor = parcel.readInt();
        this.sigModelCount = parcel.readInt();
        this.vendorModelCount = parcel.readInt();
        this.meshModels = new LinkedHashMap();
        sortModels(parcel.readHashMap(MeshModel.class.getClassLoader()));
    }
}
