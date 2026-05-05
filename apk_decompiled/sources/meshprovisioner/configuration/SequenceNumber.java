package meshprovisioner.configuration;

import a.a.a.a.b.m.a;
import a.a.a.a.b.m.g;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.util.Random;

/* JADX INFO: loaded from: classes4.dex */
public final class SequenceNumber {
    public static final String PREFS_MESH_DATA = "MESH_NETWORK_DATA";
    public static final String TAG = "MESH_STORAGE";
    public static SequenceNumber mInstance;
    public Context mContext;
    public volatile Integer mSequenceNumber = null;
    public String mCurrentUserId = null;
    public String mUUID = null;
    public String mUUIDCacheKey = null;
    public String mSequenceNumberCacheKey = null;

    public static String calcHmac(byte[] bArr) {
        return Base64.encodeToString(bArr, 2);
    }

    private String generateUUID() {
        a.a(TAG, "generateUUID");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int iCurrentTimeMillis = (int) (System.currentTimeMillis() / 1000);
        int iNextInt = new Random().nextInt();
        byte[] bArrA = g.a(iCurrentTimeMillis);
        byte[] bArrA2 = g.a(iNextInt);
        byteArrayOutputStream.write(bArrA, 0, 4);
        byteArrayOutputStream.write(bArrA2, 0, 4);
        byteArrayOutputStream.write(3);
        byteArrayOutputStream.write(0);
        byteArrayOutputStream.write(g.a(g.a(String.valueOf(new Random().nextInt()))), 0, 4);
        byteArrayOutputStream.write(g.a(g.a(calcHmac(byteArrayOutputStream.toByteArray()))));
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), 2);
    }

    public static SequenceNumber getInstance() {
        if (mInstance == null) {
            synchronized (SequenceNumber.class) {
                if (mInstance == null) {
                    mInstance = new SequenceNumber();
                }
            }
        }
        return mInstance;
    }

    private String getSequenceNumberCacheKey() {
        return this.mSequenceNumberCacheKey;
    }

    private String getUUIDCacheKey() {
        return this.mUUIDCacheKey;
    }

    private void initUUIDAndSequenceNumber() {
        this.mSequenceNumber = 0;
        this.mUUID = null;
        if (readFromSP()) {
            a.a(TAG, "Read from SP successfully");
            return;
        }
        if (readFromSettings()) {
            a.a(TAG, "Read from Settings successfully");
            writeToSP(this.mSequenceNumber.intValue(), this.mUUID);
            return;
        }
        this.mSequenceNumber = 0;
        try {
            this.mUUID = generateUUID();
        } catch (Exception e) {
            a.b(TAG, e.toString());
        }
        persistentUUIDAndSequenceNumber(this.mSequenceNumber.intValue(), this.mUUID);
        a.a(TAG, "SEQ Number: " + this.mSequenceNumber + ", UUID: " + this.mUUID);
    }

    private void persistentSequenceNumber(int i) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(PREFS_MESH_DATA, 0);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putInt(getSequenceNumberCacheKey(), i);
            editorEdit.apply();
        }
    }

    private void persistentUUIDAndSequenceNumber(int i, String str) {
        writeToSP(i, str);
        writeToSettings(i, str);
    }

    private boolean readFromSP() {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(PREFS_MESH_DATA, 0);
        if (sharedPreferences == null) {
            return false;
        }
        this.mUUID = sharedPreferences.getString(getUUIDCacheKey(), null);
        this.mSequenceNumber = Integer.valueOf(sharedPreferences.getInt(getSequenceNumberCacheKey(), 0));
        return !TextUtils.isEmpty(this.mUUID);
    }

    private boolean readFromSettings() {
        return false;
    }

    private void writeToSP(int i, String str) {
        SharedPreferences sharedPreferences = this.mContext.getSharedPreferences(PREFS_MESH_DATA, 0);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editorEdit = sharedPreferences.edit();
            editorEdit.putInt(getSequenceNumberCacheKey(), i);
            editorEdit.putString(getUUIDCacheKey(), str);
            editorEdit.apply();
        }
    }

    private void writeToSettings(int i, String str) {
    }

    public int getSequenceNumber() {
        return this.mSequenceNumber.intValue();
    }

    public String getUUID() {
        return this.mUUID;
    }

    public int incrementAndStore() {
        if (this.mSequenceNumber == null) {
            initUUIDAndSequenceNumber();
        }
        Integer num = this.mSequenceNumber;
        this.mSequenceNumber = Integer.valueOf(this.mSequenceNumber.intValue() + 1);
        persistentSequenceNumber(this.mSequenceNumber.intValue());
        return this.mSequenceNumber.intValue();
    }

    public void init(Context context, String str) {
        a.a(TAG, "Init with user ID: " + str);
        this.mCurrentUserId = str;
        this.mContext = context;
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(this.mCurrentUserId);
            sb.append("_UUID");
            this.mUUIDCacheKey = calcHmac(sb.toString().getBytes());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(this.mCurrentUserId);
            sb2.append("_MESH_SEQUENCE_NUMBER");
            this.mSequenceNumberCacheKey = calcHmac(sb2.toString().getBytes());
        } catch (Exception unused) {
            this.mUUIDCacheKey = this.mCurrentUserId + "_UUID";
            this.mSequenceNumberCacheKey = this.mCurrentUserId + "_MESH_SEQUENCE_NUMBER";
        }
        initUUIDAndSequenceNumber();
    }

    private int getSequenceNumber(byte[] bArr) {
        return (bArr[2] & 255) | ((bArr[0] & 255) << 16) | ((bArr[1] & 255) << 8);
    }

    public int incrementAndStore(byte[] bArr) {
        int sequenceNumber = getSequenceNumber(bArr) + 1;
        this.mSequenceNumber = Integer.valueOf(sequenceNumber);
        persistentSequenceNumber(this.mSequenceNumber.intValue());
        return sequenceNumber;
    }
}
