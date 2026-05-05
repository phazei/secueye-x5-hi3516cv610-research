package com.aliyun.alink.linksdk.tmp.devicemodel.loader;

import android.text.TextUtils;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.devicemodel.Event;
import com.aliyun.alink.linksdk.tmp.utils.GsonUtils;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import com.google.gson.reflect.TypeToken;
import java.util.LinkedList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public abstract class DeviceModelSerializer {
    protected static final String TAG = "[Tmp]DeviceModelSerializer";
    protected DeviceModelSerializer mChild;
    protected String mId;

    public abstract boolean deserialize(String str, String str2, ILoaderHandler iLoaderHandler);

    public abstract String serialize(String str, DeviceModel deviceModel);

    public DeviceModelSerializer(String str) {
        this.mId = str;
    }

    public void appendSerializer(DeviceModelSerializer deviceModelSerializer) {
        DeviceModelSerializer deviceModelSerializer2 = this.mChild;
        if (deviceModelSerializer2 == null) {
            this.mChild = deviceModelSerializer;
        } else {
            deviceModelSerializer2.appendSerializer(deviceModelSerializer);
        }
    }

    public DeviceModel deserializeSync(String str, String str2) {
        return deserializeInner(str2);
    }

    protected DeviceModel deserializeInner(String str) {
        return (DeviceModel) GsonUtils.fromJson(str, new TypeToken<DeviceModel>() { // from class: com.aliyun.alink.linksdk.tmp.devicemodel.loader.DeviceModelSerializer.1
        }.getType());
    }

    protected String serializeInner(DeviceModel deviceModel) {
        return GsonUtils.toJson(deviceModel);
    }

    protected DeviceModelSerializer dispatch(String str) {
        if (TextUtils.isEmpty(this.mId)) {
            LogCat.e(TAG, "dispatch empty id error");
        } else if (this.mId.equalsIgnoreCase(str)) {
            return this;
        }
        DeviceModelSerializer deviceModelSerializer = this.mChild;
        if (deviceModelSerializer != null) {
            return deviceModelSerializer.dispatch(str);
        }
        return null;
    }

    public static void addChildModel(DeviceModel deviceModel, DeviceModel deviceModel2) {
        if (deviceModel == null || deviceModel2 == null) {
            return;
        }
        addList(deviceModel.getProperties(), deviceModel2.getProperties());
        addList(deviceModel.getServices(), deviceModel2.getServices());
        addList(deviceModel.getEvents(), deviceModel2.getEvents());
    }

    protected static void expandEvent(String str, DeviceModel deviceModel) {
        if (deviceModel == null) {
            LogCat.d(TAG, "expandEvent model empty");
            return;
        }
        if (deviceModel.getEvents() == null || deviceModel.getEvents().isEmpty()) {
            LogCat.d(TAG, "expandEvent event empty");
            return;
        }
        for (int i = 0; i < deviceModel.getEvents().size(); i++) {
            Event event2 = deviceModel.getEvents().get(i);
            if (event2 != null) {
                event2.setName(expand(str, event2.getIdentifier()));
            }
        }
    }

    protected static void addList(List list, List list2) {
        if (list2 == null || list2.isEmpty()) {
            return;
        }
        if (list == null) {
            list = new LinkedList();
        }
        list.addAll(list2);
    }

    public static String expand(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str2;
        }
        return str + "." + str2;
    }

    public static String froamtUrl(String str, String str2) {
        return str + "/" + str2 + ".json";
    }

    /* JADX WARN: Can't wrap try/catch for region: R(12:0|2|(8:65|3|4|80|5|6|78|7)|(6:8|68|9|(1:11)(1:83)|47|48)|12|72|13|76|17|47|48|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x0040, code lost:
    
        r9 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0041, code lost:
    
        r9.printStackTrace();
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0048, code lost:
    
        r9 = e;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:63:0x006b A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0075 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v12 */
    /* JADX WARN: Type inference failed for: r0v2 */
    /* JADX WARN: Type inference failed for: r0v3, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v9 */
    /* JADX WARN: Type inference failed for: r3v1 */
    /* JADX WARN: Type inference failed for: r3v3, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r3v6 */
    /* JADX WARN: Type inference failed for: r3v7, types: [java.io.ByteArrayOutputStream] */
    /* JADX WARN: Type inference failed for: r9v1 */
    /* JADX WARN: Type inference failed for: r9v12 */
    /* JADX WARN: Type inference failed for: r9v16 */
    /* JADX WARN: Type inference failed for: r9v3 */
    /* JADX WARN: Type inference failed for: r9v5, types: [java.net.HttpURLConnection] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.lang.String requestDeviceModel(java.lang.String r8, java.lang.String r9) throws java.lang.Throwable {
        /*
            r0 = 0
            java.net.URL r1 = new java.net.URL     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L62
            java.lang.String r8 = froamtUrl(r8, r9)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L62
            r1.<init>(r8)     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L62
            java.net.URLConnection r8 = r1.openConnection()     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L62
            java.net.HttpURLConnection r8 = (java.net.HttpURLConnection) r8     // Catch: java.lang.Throwable -> L5e java.lang.Exception -> L62
            r9 = 10000(0x2710, float:1.4013E-41)
            r8.setConnectTimeout(r9)     // Catch: java.lang.Throwable -> L55 java.lang.Exception -> L5b
            r9 = 5000(0x1388, float:7.006E-42)
            r8.setReadTimeout(r9)     // Catch: java.lang.Throwable -> L55 java.lang.Exception -> L5b
            java.io.BufferedInputStream r9 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L55 java.lang.Exception -> L5b
            java.io.InputStream r1 = r8.getInputStream()     // Catch: java.lang.Throwable -> L55 java.lang.Exception -> L5b
            r9.<init>(r1)     // Catch: java.lang.Throwable -> L55 java.lang.Exception -> L5b
            r1 = 1024(0x400, float:1.435E-42)
            byte[] r2 = new byte[r1]     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L52
            java.io.ByteArrayOutputStream r3 = new java.io.ByteArrayOutputStream     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L52
            r3.<init>()     // Catch: java.lang.Throwable -> L4c java.lang.Exception -> L52
        L2c:
            r4 = 0
            int r5 = r9.read(r2, r4, r1)     // Catch: java.lang.Exception -> L4a java.lang.Throwable -> L81
            r6 = -1
            if (r5 == r6) goto L38
            r3.write(r2, r4, r5)     // Catch: java.lang.Exception -> L4a java.lang.Throwable -> L81
            goto L2c
        L38:
            java.lang.String r0 = r3.toString()     // Catch: java.lang.Exception -> L4a java.lang.Throwable -> L81
            r9.close()     // Catch: java.lang.Exception -> L40
            goto L44
        L40:
            r9 = move-exception
            r9.printStackTrace()
        L44:
            r3.close()     // Catch: java.lang.Exception -> L48
            goto L7d
        L48:
            r9 = move-exception
            goto L7a
        L4a:
            r1 = move-exception
            goto L66
        L4c:
            r1 = move-exception
            r3 = r0
            r0 = r9
            r9 = r8
            r8 = r1
            goto L86
        L52:
            r1 = move-exception
            r3 = r0
            goto L66
        L55:
            r9 = move-exception
            r3 = r0
            r7 = r9
            r9 = r8
            r8 = r7
            goto L86
        L5b:
            r1 = move-exception
            r9 = r0
            goto L65
        L5e:
            r8 = move-exception
            r9 = r0
            r3 = r9
            goto L86
        L62:
            r1 = move-exception
            r8 = r0
            r9 = r8
        L65:
            r3 = r9
        L66:
            r1.printStackTrace()     // Catch: java.lang.Throwable -> L81
            if (r9 == 0) goto L73
            r9.close()     // Catch: java.lang.Exception -> L6f
            goto L73
        L6f:
            r9 = move-exception
            r9.printStackTrace()
        L73:
            if (r3 == 0) goto L7d
            r3.close()     // Catch: java.lang.Exception -> L79
            goto L7d
        L79:
            r9 = move-exception
        L7a:
            r9.printStackTrace()
        L7d:
            r8.disconnect()
            return r0
        L81:
            r0 = move-exception
            r7 = r9
            r9 = r8
            r8 = r0
            r0 = r7
        L86:
            if (r0 == 0) goto L90
            r0.close()     // Catch: java.lang.Exception -> L8c
            goto L90
        L8c:
            r0 = move-exception
            r0.printStackTrace()
        L90:
            if (r3 == 0) goto L9a
            r3.close()     // Catch: java.lang.Exception -> L96
            goto L9a
        L96:
            r0 = move-exception
            r0.printStackTrace()
        L9a:
            r9.disconnect()
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.aliyun.alink.linksdk.tmp.devicemodel.loader.DeviceModelSerializer.requestDeviceModel(java.lang.String, java.lang.String):java.lang.String");
    }
}
