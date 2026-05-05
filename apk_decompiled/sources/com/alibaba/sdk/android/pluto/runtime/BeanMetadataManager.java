package com.alibaba.sdk.android.pluto.runtime;

import android.content.Context;
import android.util.Log;
import com.alibaba.cloudapi.sdk.constant.SdkConstant;
import com.alibaba.sdk.android.pluto.PlutoConstants;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import com.alibaba.sdk.android.pluto.annotation.BeanProperty;
import com.alibaba.sdk.android.pluto.annotation.Qualifier;
import com.alibaba.sdk.android.pluto.meta.BeanInfo;
import com.alibaba.sdk.android.pluto.meta.ModuleInfo;
import com.alibaba.sdk.android.pluto.util.SortUtils;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/* JADX INFO: loaded from: classes.dex */
public class BeanMetadataManager {
    private String[] systemPackagePrefixes = {"android.", "java."};

    public static class BeanRuntimeInfo extends SortUtils.SortInfo {
        public BeanInfo beanInfo;
        public Method initMethod;
        public List<Field> injectFields;
    }

    protected boolean isSystemClass(Class<?> cls) {
        if (cls == null || cls == Object.class) {
            return true;
        }
        String name = cls.getName();
        for (String str : this.systemPackagePrefixes) {
            if (name.startsWith(str)) {
                return true;
            }
        }
        return false;
    }

    public void setSystemPackagePrefixes(String[] strArr) {
        if (strArr == null) {
            return;
        }
        String[] strArr2 = new String[strArr.length];
        System.arraycopy(strArr, 0, strArr2, 0, strArr.length);
        this.systemPackagePrefixes = strArr2;
    }

    protected BeanRuntimeInfo parse(BeanInfo beanInfo) {
        BeanRuntimeInfo beanRuntimeInfo = new BeanRuntimeInfo();
        beanRuntimeInfo.beanInfo = beanInfo;
        if (beanInfo.implType == null && beanInfo.instance == null) {
            throw new IllegalStateException("implType and instance could not be null at the same time for bean " + beanInfo);
        }
        Class<?> cls = beanInfo.implType == null ? beanInfo.instance.getClass() : beanInfo.implType;
        beanRuntimeInfo.injectFields = getInjectionFields(cls);
        if (beanInfo.initMethod != null) {
            try {
                try {
                    beanRuntimeInfo.initMethod = cls.getMethod(beanInfo.initMethod, Context.class);
                } catch (Exception unused) {
                    beanRuntimeInfo.initMethod = cls.getMethod(beanInfo.initMethod, new Class[0]);
                }
            } catch (Exception e) {
                throw new IllegalStateException("invalid init method for bean " + beanInfo, e);
            }
        }
        return beanRuntimeInfo;
    }

    public List<Field> getInjectionFields(Class<?> cls) {
        ArrayList arrayList = new ArrayList(3);
        while (!isSystemClass(cls)) {
            for (Field field : cls.getDeclaredFields()) {
                if (field.isAnnotationPresent(Autowired.class)) {
                    arrayList.add(field);
                }
            }
            cls = cls.getSuperclass();
        }
        return arrayList;
    }

    public BeanRuntimeInfo[] buildBeanRuntimeInfos(Set<ModuleInfo> set) {
        HashMap map;
        HashMap map2;
        boolean z;
        ArrayList<BeanRuntimeInfo> arrayList = new ArrayList();
        HashMap map3 = new HashMap();
        int i = 0;
        for (ModuleInfo moduleInfo : set) {
            if (moduleInfo.beanInfos != null) {
                Map<String, String> mapSingletonMap = Collections.singletonMap(AUserTrack.UTKEY_MODULE, moduleInfo.name);
                for (BeanInfo beanInfo : moduleInfo.beanInfos) {
                    if (beanInfo.properties == null) {
                        beanInfo.properties = mapSingletonMap;
                    } else {
                        beanInfo.properties = new HashMap(beanInfo.properties);
                        beanInfo.properties.put(AUserTrack.UTKEY_MODULE, moduleInfo.name);
                    }
                    BeanRuntimeInfo beanRuntimeInfo = parse(beanInfo);
                    String simpleName = (beanInfo.implType == null ? beanInfo.instance.getClass() : beanInfo.implType).getSimpleName();
                    StringBuilder sb = new StringBuilder();
                    sb.append(simpleName);
                    sb.append(".");
                    int i2 = i + 1;
                    sb.append(i);
                    beanRuntimeInfo.name = sb.toString();
                    arrayList.add(beanRuntimeInfo);
                    for (Class<?> cls : beanInfo.types) {
                        List arrayList2 = (List) map3.get(cls);
                        if (arrayList2 == null) {
                            arrayList2 = new ArrayList(2);
                            map3.put(cls, arrayList2);
                        }
                        arrayList2.add(beanRuntimeInfo);
                    }
                    i = i2;
                }
            }
        }
        for (BeanRuntimeInfo beanRuntimeInfo2 : arrayList) {
            if (beanRuntimeInfo2.injectFields == null) {
                beanRuntimeInfo2.after = Collections.emptyList();
            } else {
                beanRuntimeInfo2.after = new ArrayList(5);
            }
            for (Field field : beanRuntimeInfo2.injectFields) {
                Autowired autowired = (Autowired) field.getAnnotation(Autowired.class);
                List<BeanRuntimeInfo> list = (List) map3.get(field.getType());
                if (list == null) {
                    String str = "fail to find matched bean for " + field.getDeclaringClass().getName() + "." + field.getName();
                    Log.e(PlutoConstants.PLUTO_LOG_TAG, str);
                    throw new IllegalStateException(str);
                }
                Qualifier qualifier = (Qualifier) field.getAnnotation(Qualifier.class);
                if (qualifier == null || qualifier.filters() == null || qualifier.filters().length == 0) {
                    map = map3;
                    if (list.size() > 1) {
                        String str2 = "more then one matched bean for " + field.getDeclaringClass().getName() + "." + field.getName() + " matched beans " + getBeanImplDescriptionInfo(list);
                        Log.e(PlutoConstants.PLUTO_LOG_TAG, str2);
                        throw new IllegalStateException(str2);
                    }
                    beanRuntimeInfo2.after.add(list.get(0).name);
                } else {
                    BeanProperty[] beanPropertyArrFilters = qualifier.filters();
                    ArrayList arrayList3 = new ArrayList(list.size());
                    for (BeanRuntimeInfo beanRuntimeInfo3 : list) {
                        int length = beanPropertyArrFilters.length;
                        int i3 = 0;
                        while (i3 < length) {
                            BeanProperty beanProperty = beanPropertyArrFilters[i3];
                            map2 = map3;
                            String str3 = beanRuntimeInfo3.beanInfo.properties.get(beanProperty.key());
                            if (str3 == null || !str3.equals(beanProperty.value())) {
                                z = false;
                                break;
                            }
                            i3++;
                            map3 = map2;
                        }
                        map2 = map3;
                        z = true;
                        if (z) {
                            arrayList3.add(beanRuntimeInfo3);
                        }
                        map3 = map2;
                    }
                    map = map3;
                    if (arrayList3.size() == 1) {
                        beanRuntimeInfo2.after.add(((BeanRuntimeInfo) arrayList3.get(0)).name);
                    } else {
                        if (arrayList3.size() == 0 && autowired.required()) {
                            String str4 = "fail to find matched bean for " + field.getDeclaringClass().getName() + "." + field.getName();
                            Log.e(PlutoConstants.PLUTO_LOG_TAG, str4);
                            throw new IllegalStateException(str4);
                        }
                        if (arrayList3.size() > 1) {
                            String str5 = "more then one matched bean for " + field.getDeclaringClass().getName() + "." + field.getName() + " matched beans " + getBeanImplDescriptionInfo(list);
                            Log.e(PlutoConstants.PLUTO_LOG_TAG, str5);
                            throw new IllegalStateException(str5);
                        }
                    }
                }
                map3 = map;
            }
        }
        BeanRuntimeInfo[] beanRuntimeInfoArr = (BeanRuntimeInfo[]) arrayList.toArray(new BeanRuntimeInfo[0]);
        SortUtils.sorts(beanRuntimeInfoArr);
        return beanRuntimeInfoArr;
    }

    private String getBeanImplDescriptionInfo(List<BeanRuntimeInfo> list) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (BeanRuntimeInfo beanRuntimeInfo : list) {
            sb.append(i);
            sb.append(" : ");
            if (beanRuntimeInfo.beanInfo.implType != null) {
                sb.append(beanRuntimeInfo.beanInfo.implType);
            } else if (beanRuntimeInfo.beanInfo.instance != null) {
                sb.append(beanRuntimeInfo.beanInfo.instance);
            } else {
                sb.append("null implType and instance");
            }
            sb.append(SdkConstant.CLOUDAPI_LF);
            i++;
        }
        return sb.toString();
    }
}
