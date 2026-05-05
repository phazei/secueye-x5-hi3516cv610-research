package com.alibaba.sdk.android.pluto.meta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ModuleInfoBuilder {
    private List<BeanInfo> beanInfos = new ArrayList();
    private String moduleName;
    private Map<String, String> moduleProperties;

    public ModuleInfoBuilder(String str) {
        this.moduleName = str;
    }

    public ModuleInfo build() {
        ModuleInfo moduleInfo = new ModuleInfo();
        moduleInfo.name = this.moduleName;
        moduleInfo.beanInfos = Collections.unmodifiableList(this.beanInfos);
        moduleInfo.properties = this.moduleProperties;
        return moduleInfo;
    }

    public void addBeanInfo(Class<?> cls, Class<?> cls2, String str, Map<String, String> map) {
        BeanInfo beanInfo = new BeanInfo();
        beanInfo.types = new Class[]{cls};
        beanInfo.implType = cls2;
        beanInfo.properties = map;
        beanInfo.initMethod = str;
        this.beanInfos.add(beanInfo);
    }

    public void addBeanInfo(Class<?> cls, Class<?> cls2) {
        addBeanInfo(cls, cls2, (String) null, (Map<String, String>) null);
    }

    public void addBeanInfo(Class<?>[] clsArr, Class<?> cls, String str, Map<String, String> map) {
        BeanInfo beanInfo = new BeanInfo();
        beanInfo.types = clsArr;
        beanInfo.implType = cls;
        beanInfo.properties = map;
        beanInfo.initMethod = str;
        this.beanInfos.add(beanInfo);
    }

    public void addBeanInfo(Class<?>[] clsArr, Class<?> cls) {
        addBeanInfo(clsArr, cls, (String) null, (Map<String, String>) null);
    }

    public void addBeanInfo(Class<?> cls, Object obj, String str, Map<String, String> map) {
        BeanInfo beanInfo = new BeanInfo();
        beanInfo.types = new Class[]{cls};
        beanInfo.instance = obj;
        beanInfo.properties = map;
        beanInfo.initMethod = str;
        this.beanInfos.add(beanInfo);
    }

    public void addBeanInfo(Class<?> cls, Object obj) {
        addBeanInfo(new Class[]{cls}, obj, (String) null, (Map<String, String>) null);
    }

    public void addBeanInfo(Class<?>[] clsArr, Object obj, String str, Map<String, String> map) {
        BeanInfo beanInfo = new BeanInfo();
        beanInfo.types = clsArr;
        beanInfo.instance = obj;
        beanInfo.properties = map;
        beanInfo.initMethod = str;
        this.beanInfos.add(beanInfo);
    }

    public void addBeanInfo(Class<?>[] clsArr, Object obj) {
        addBeanInfo(clsArr, obj, (String) null, (Map<String, String>) null);
    }

    public void setModuleProperties(Map<String, String> map) {
        this.moduleProperties = map;
    }
}
