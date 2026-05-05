package meshprovisioner.configuration;

import android.os.Parcel;
import android.os.Parcelable;
import com.alibaba.fastjson.annotation.JSONField;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes4.dex */
public abstract class MeshModel implements Parcelable {
    public static final int DEFAULT_MODEL_ID = -1;
    public int credentialFlag;
    public List<Integer> mBoundAppKeyIndexes;
    public Map<Integer, String> mBoundAppKeys;
    public int mModelId;

    @JSONField(deserialize = false, serialize = false)
    public List<byte[]> mSubscriptionAddress;
    public int publicationResolution;
    public int publicationSteps;
    public byte[] publishAddress;
    public byte[] publishAppKeyIndex;
    public int publishPeriod;
    public int publishRetransmitCount;
    public int publishRetransmitIntervalSteps;
    public int publishTtl;

    public MeshModel() {
        this.mBoundAppKeyIndexes = new ArrayList();
        this.mBoundAppKeys = new LinkedHashMap();
        this.publishTtl = 255;
        this.publishPeriod = 0;
        this.publishRetransmitCount = 1;
        this.publishRetransmitIntervalSteps = 1;
        this.mSubscriptionAddress = new ArrayList();
        this.publicationSteps = 0;
        this.publicationResolution = 0;
        this.mModelId = -1;
    }

    private boolean checkIfAlreadySubscribed(byte[] bArr) {
        Iterator<byte[]> it = this.mSubscriptionAddress.iterator();
        while (it.hasNext()) {
            if (Arrays.equals(it.next(), bArr)) {
                return true;
            }
        }
        return false;
    }

    private int getIndex(byte[] bArr) {
        Iterator<byte[]> it = this.mSubscriptionAddress.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (Arrays.equals(it.next(), bArr)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void sortAppKeys(HashMap<Integer, String> map) {
        ArrayList arrayList = new ArrayList(map.keySet());
        Collections.sort(arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            int iIntValue = ((Integer) it.next()).intValue();
            this.mBoundAppKeys.put(Integer.valueOf(iIntValue), map.get(Integer.valueOf(iIntValue)));
        }
    }

    public String getBoundAppKey(int i) {
        return this.mBoundAppKeys.get(Integer.valueOf(i));
    }

    public List<Integer> getBoundAppKeyIndexes() {
        return this.mBoundAppKeyIndexes;
    }

    public Map<Integer, String> getBoundAppkeys() {
        return this.mBoundAppKeys;
    }

    public int getCredentialFlag() {
        return this.credentialFlag;
    }

    public abstract int getModelId();

    public abstract String getModelName();

    public int getPublicationResolution() {
        return this.publicationResolution;
    }

    public int getPublicationSteps() {
        return this.publicationSteps;
    }

    public byte[] getPublishAddress() {
        return this.publishAddress;
    }

    public byte[] getPublishAppKeyIndex() {
        return this.publishAppKeyIndex;
    }

    public Integer getPublishAppKeyIndexInt() {
        byte[] bArr = this.publishAppKeyIndex;
        if (bArr != null) {
            return Integer.valueOf(ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).getShort());
        }
        return null;
    }

    public int getPublishRetransmitCount() {
        return this.publishRetransmitCount;
    }

    public int getPublishRetransmitIntervalSteps() {
        return this.publishRetransmitIntervalSteps;
    }

    public int getPublishTtl() {
        return this.publishTtl & 255;
    }

    @JSONField(serialize = false)
    public List<byte[]> getSubscriptionAddresses() {
        return this.mSubscriptionAddress;
    }

    public final void parcelMeshModel(Parcel parcel, int i) {
        parcel.writeInt(this.mModelId);
        parcel.writeList(this.mBoundAppKeyIndexes);
        parcel.writeMap(this.mBoundAppKeys);
        parcel.writeByteArray(this.publishAddress);
        parcel.writeByteArray(this.publishAppKeyIndex);
        parcel.writeInt(this.credentialFlag);
        parcel.writeInt(this.publishTtl);
        parcel.writeInt(this.publishPeriod);
        parcel.writeInt(this.publishRetransmitIntervalSteps);
        parcel.writeList(this.mSubscriptionAddress);
    }

    public void removeBoundAppKey(int i, String str) {
        if (this.mBoundAppKeyIndexes.contains(Integer.valueOf(i))) {
            this.mBoundAppKeyIndexes.remove(this.mBoundAppKeyIndexes.indexOf(Integer.valueOf(i)));
        }
        this.mBoundAppKeys.remove(Integer.valueOf(i));
    }

    public void removeSubscriptionAddress(byte[] bArr) {
        int index;
        if (bArr == null || (index = getIndex(bArr)) <= -1) {
            return;
        }
        this.mSubscriptionAddress.remove(index);
    }

    public void setBoundAppKey(int i, String str) {
        if (!this.mBoundAppKeyIndexes.contains(Integer.valueOf(i))) {
            this.mBoundAppKeyIndexes.add(Integer.valueOf(i));
        }
        this.mBoundAppKeys.put(Integer.valueOf(i), str);
    }

    public void setCredentialFlag(int i) {
        this.credentialFlag = i;
    }

    public void setModelId(int i) {
        this.mModelId = i;
    }

    public void setPublicationResolution(int i) {
        this.publicationResolution = i;
    }

    public void setPublicationStatus(ConfigModelPublicationStatus configModelPublicationStatus) {
        this.publishAddress = configModelPublicationStatus.getPublishAddress();
        this.publishAppKeyIndex = configModelPublicationStatus.getPublicationAppKeyIndex();
        this.credentialFlag = configModelPublicationStatus.getCredentialFlag();
        this.publishTtl = configModelPublicationStatus.getPublishTtl();
        this.publishPeriod = configModelPublicationStatus.getPublishPeriod();
        int i = this.publishPeriod;
        this.publicationSteps = i >> 6;
        this.publicationResolution = i & 3;
        this.publishRetransmitCount = configModelPublicationStatus.getPublishRetransmitCount();
        this.publishRetransmitIntervalSteps = configModelPublicationStatus.getPublishRetransmitIntervalSteps();
    }

    public void setPublicationSteps(int i) {
        this.publicationSteps = i;
    }

    public void setPublishAddress(byte[] bArr) {
        this.publishAddress = bArr;
    }

    public void setPublishAppKeyIndex(byte[] bArr) {
        this.publishAppKeyIndex = bArr;
    }

    public void setPublishPeriod(int i) {
        this.publishPeriod = i;
    }

    public void setPublishRetransmitCount(int i) {
        this.publishRetransmitCount = i;
    }

    public void setPublishRetransmitIntervalSteps(int i) {
        this.publishRetransmitIntervalSteps = i;
    }

    public void setPublishTtl(int i) {
        this.publishTtl = i;
    }

    public void setSubscriptionAddress(List<byte[]> list) {
        this.mSubscriptionAddress = list;
    }

    public void setPublicationStatus(byte[] bArr) {
        if (bArr == null || checkIfAlreadySubscribed(bArr)) {
            return;
        }
        this.mSubscriptionAddress.add(bArr);
    }

    public MeshModel(int i) {
        this.mBoundAppKeyIndexes = new ArrayList();
        this.mBoundAppKeys = new LinkedHashMap();
        this.publishTtl = 255;
        this.publishPeriod = 0;
        this.publishRetransmitCount = 1;
        this.publishRetransmitIntervalSteps = 1;
        this.mSubscriptionAddress = new ArrayList();
        this.publicationSteps = 0;
        this.publicationResolution = 0;
        this.mModelId = i;
    }

    public MeshModel(Parcel parcel) {
        this.mBoundAppKeyIndexes = new ArrayList();
        this.mBoundAppKeys = new LinkedHashMap();
        this.publishTtl = 255;
        this.publishPeriod = 0;
        this.publishRetransmitCount = 1;
        this.publishRetransmitIntervalSteps = 1;
        this.mSubscriptionAddress = new ArrayList();
        this.publicationSteps = 0;
        this.publicationResolution = 0;
        int i = parcel.readInt();
        if (i >= -32768 && i <= 32767) {
            this.mModelId = (short) i;
        } else {
            this.mModelId = i;
        }
        parcel.readList(this.mBoundAppKeyIndexes, Integer.class.getClassLoader());
        sortAppKeys(parcel.readHashMap(String.class.getClassLoader()));
        this.publishAddress = parcel.createByteArray();
        this.publishAppKeyIndex = parcel.createByteArray();
        this.credentialFlag = parcel.readInt();
        this.publishTtl = parcel.readInt();
        this.publishPeriod = parcel.readInt();
        int i2 = this.publishPeriod;
        this.publicationSteps = i2 >> 6;
        this.publicationResolution = i2 & 3;
        this.publishRetransmitIntervalSteps = parcel.readInt();
        parcel.readList(this.mSubscriptionAddress, byte[].class.getClassLoader());
    }
}
