package com.aliyun.alink.linksdk.tmp.devicemodel.loader;

import android.content.Context;
import com.aliyun.alink.linksdk.tmp.devicemodel.DeviceModel;
import com.aliyun.alink.linksdk.tmp.utils.LogCat;
import java.util.HashMap;
import java.util.Map;

/* JADX INFO: loaded from: classes2.dex */
public class RootDeviceModelSerializer extends DeviceModelSerializer {
    public static final String MULEXTEND_DEVICEMODELSERIALIZER_ID = "MulExtendSerializer";
    public static final String SINGLEEXTEND_DEVICEMODELSERIALIZER_ID = "SingleExtendSerializer";
    protected Context mContext;
    protected Map<String, String> mModelJsons;
    protected Map<String, DeviceModel> mModelList;
    protected static final String ROOT_DEVICEMODELSERIALIZER_ID = "RootDeviceModelSerializer";
    protected static RootDeviceModelSerializer mInstance = new RootDeviceModelSerializer(ROOT_DEVICEMODELSERIALIZER_ID);

    protected RootDeviceModelSerializer(String str) {
        super(str);
        this.mModelJsons = new HashMap();
        this.mModelList = new HashMap();
        appendSerializer(new SingleExtendSerializer());
        appendSerializer(new MulExtendSerializer());
    }

    public static RootDeviceModelSerializer getInstance() {
        return mInstance;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return this.mContext;
    }

    @Override // com.aliyun.alink.linksdk.tmp.devicemodel.loader.DeviceModelSerializer
    public String serialize(String str, DeviceModel deviceModel) {
        DeviceModelSerializer deviceModelSerializerDispatch = dispatch(str);
        if (deviceModelSerializerDispatch == null) {
            LogCat.e("[Tmp]DeviceModelSerializer", "serialize not find serializer error id:" + str);
            return null;
        }
        return deviceModelSerializerDispatch.serialize(str, deviceModel);
    }

    @Override // com.aliyun.alink.linksdk.tmp.devicemodel.loader.DeviceModelSerializer
    public boolean deserialize(String str, String str2, ILoaderHandler iLoaderHandler) {
        DeviceModelSerializer deviceModelSerializerDispatch = dispatch(str);
        if (deviceModelSerializerDispatch == null) {
            LogCat.e("[Tmp]DeviceModelSerializer", "serialize not find deserializer error id:" + str);
            return false;
        }
        return deviceModelSerializerDispatch.deserialize(str, str2, iLoaderHandler);
    }

    public String insertDeviceModel(String str, String str2) {
        return this.mModelJsons.put(str, str2);
    }

    public String getDeviceModel(String str) {
        return this.mModelJsons.get(str);
    }

    public void insertModelObject(String str, DeviceModel deviceModel) {
        this.mModelList.put(str, deviceModel);
    }

    public void removeModelObject(String str) {
        this.mModelList.remove(str);
    }

    public DeviceModel getModelObject(String str) {
        return this.mModelList.get(str);
    }
}
