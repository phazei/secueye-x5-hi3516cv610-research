package com.alibaba.sdk.android.pluto.runtime;

import android.content.Context;
import android.util.Log;
import com.alibaba.sdk.android.openaccount.OpenAccountSDK;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.model.ResultCode;
import com.alibaba.sdk.android.pluto.Pluto;
import com.alibaba.sdk.android.pluto.PlutoConstants;
import com.alibaba.sdk.android.pluto.runtime.BeanMetadataManager;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class BeanLoaderManager {
    private static final String TAG = "BeanLoaderManager";
    private Pluto pluto;

    public BeanLoaderManager(Pluto pluto) {
        this.pluto = pluto;
    }

    public List<ResultCode> load(BeanMetadataManager.BeanRuntimeInfo[] beanRuntimeInfoArr) {
        Object objNewInstance;
        Object objInvoke;
        Constructor<?> constructor;
        if (beanRuntimeInfoArr == null || beanRuntimeInfoArr.length == 0) {
            return Collections.emptyList();
        }
        ArrayList arrayList = new ArrayList();
        for (BeanMetadataManager.BeanRuntimeInfo beanRuntimeInfo : beanRuntimeInfoArr) {
            if (beanRuntimeInfo.beanInfo.instance == null) {
                Object[] objArr = null;
                try {
                    beanRuntimeInfo.beanInfo.implType.getDeclaredField("INSTANCE").get(null);
                } catch (Exception unused) {
                    Log.i(PlutoConstants.PLUTO_LOG_TAG, "No instance field detect for bean " + beanRuntimeInfo.name);
                }
                try {
                    try {
                        constructor = beanRuntimeInfo.beanInfo.implType.getConstructor(new Class[0]);
                    } catch (NoSuchMethodException unused2) {
                        constructor = beanRuntimeInfo.beanInfo.implType.getConstructor(Context.class);
                        objArr = new Object[]{this.pluto.getAndroidContext()};
                    }
                    try {
                        objNewInstance = constructor.newInstance(objArr);
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                } catch (NoSuchMethodException unused3) {
                    throw new IllegalStateException("No support constructor method found");
                }
            } else {
                objNewInstance = beanRuntimeInfo.beanInfo.instance;
            }
            this.pluto.getBeanInjectManagaer().inject(objNewInstance, beanRuntimeInfo.injectFields);
            if (beanRuntimeInfo.initMethod != null) {
                try {
                    beanRuntimeInfo.initMethod.setAccessible(true);
                    if (beanRuntimeInfo.initMethod.getParameterTypes().length == 0) {
                        objInvoke = beanRuntimeInfo.initMethod.invoke(objNewInstance, new Object[0]);
                    } else {
                        objInvoke = beanRuntimeInfo.initMethod.invoke(objNewInstance, OpenAccountSDK.getAndroidContext());
                    }
                    if (objInvoke instanceof ResultCode) {
                        ResultCode resultCode = (ResultCode) objInvoke;
                        if (!resultCode.isSuccess()) {
                            arrayList.add(resultCode);
                        }
                    }
                } catch (Exception e2) {
                    Log.e(PlutoConstants.PLUTO_LOG_TAG, "fail to invoke the init method", e2);
                    arrayList.add(ResultCode.create(MessageConstants.GENERIC_SYSTEM_ERROR, e2.getMessage()));
                }
            }
            this.pluto.registerBean(beanRuntimeInfo.beanInfo.types, objNewInstance, beanRuntimeInfo.beanInfo.properties);
        }
        return arrayList;
    }
}
