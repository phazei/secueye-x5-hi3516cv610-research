package com.alibaba.sdk.android.pluto.runtime.impl;

import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.pluto.PlutoConstants;
import com.alibaba.sdk.android.pluto.runtime.BeanRegistration;
import com.alibaba.sdk.android.pluto.runtime.BeanRegistry;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

/* JADX INFO: loaded from: classes.dex */
public class ProxyEnabledBeanRegistryDelegator implements BeanRegistry {
    private BeanRegistry delegator;

    public ProxyEnabledBeanRegistryDelegator(BeanRegistry beanRegistry) {
        this.delegator = beanRegistry;
    }

    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public void recycle() {
        this.delegator.recycle();
    }

    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public BeanRegistration registerBean(Class<?>[] clsArr, Object obj, Map<String, String> map) {
        return this.delegator.registerBean(clsArr, obj, map);
    }

    public <T> T getBean(final Class<T> cls, final Map<String, String> map, boolean z) {
        T t = (T) this.delegator.getBean(cls, map);
        if (t != null) {
            return t;
        }
        if (!z && cls.isInterface()) {
            return cls.cast(Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{cls}, new InvocationHandler() { // from class: com.alibaba.sdk.android.pluto.runtime.impl.ProxyEnabledBeanRegistryDelegator.1
                @Override // java.lang.reflect.InvocationHandler
                public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
                    Object bean2 = ProxyEnabledBeanRegistryDelegator.this.delegator.getBean(cls, map);
                    if (bean2 == null) {
                        Object[] objArr2 = new Object[2];
                        objArr2[0] = cls.getName();
                        Map map2 = map;
                        objArr2[1] = map2 != null ? map2.toString() : "";
                        AliSDKLogger.log(PlutoConstants.PLUTO_LOG_TAG, MessageUtils.createMessage(17, objArr2));
                        return null;
                    }
                    return method.invoke(bean2, objArr);
                }
            }));
        }
        return null;
    }

    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public <T> T getBean(Class<T> cls, Map<String, String> map) {
        return (T) getBean(cls, map, false);
    }

    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public <T> T[] getBeans(Class<T> cls, Map<String, String> map) {
        return (T[]) this.delegator.getBeans(cls, map);
    }

    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public Object unregisterBean(BeanRegistration beanRegistration) {
        return this.delegator.unregisterBean(beanRegistration);
    }
}
