package com.alibaba.sdk.android.pluto.runtime.impl;

import com.alibaba.sdk.android.openaccount.trace.ActionTraceLogger;
import com.alibaba.sdk.android.openaccount.trace.TraceLoggerManager;
import com.alibaba.sdk.android.pluto.runtime.BeanRegistration;
import com.alibaba.sdk.android.pluto.runtime.BeanRegistry;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* JADX INFO: loaded from: classes.dex */
public class DefaultBeanRegistry implements BeanRegistry {
    private Map<Class<?>, List<ServiceEntry>> typeServiceEntries = new HashMap();
    private Map<BeanRegistration, ServiceEntry> registrationServiceEntries = new HashMap();
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public BeanRegistration registerBean(Class<?>[] clsArr, Object obj, Map<String, String> map) {
        if (clsArr == null || clsArr.length == 0 || obj == null) {
            throw new IllegalArgumentException("service types and instance must not be null");
        }
        ActionTraceLogger actionTraceLoggerBegin = TraceLoggerManager.INSTANCE.action("registerBean." + obj.getClass().getName()).begin();
        ServiceEntry serviceEntry = new ServiceEntry();
        serviceEntry.instance = obj;
        serviceEntry.types = clsArr;
        serviceEntry.properties = Collections.synchronizedMap(new HashMap());
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    serviceEntry.properties.put(entry.getKey(), entry.getValue());
                }
            }
        }
        this.lock.writeLock().lock();
        try {
            for (Class<?> cls : clsArr) {
                List<ServiceEntry> arrayList = this.typeServiceEntries.get(cls);
                if (arrayList == null) {
                    arrayList = new ArrayList<>(2);
                    this.typeServiceEntries.put(cls, arrayList);
                }
                arrayList.add(serviceEntry);
            }
            InternalServiceRegistration internalServiceRegistration = new InternalServiceRegistration(this, serviceEntry.properties);
            this.registrationServiceEntries.put(internalServiceRegistration, serviceEntry);
            return internalServiceRegistration;
        } finally {
            this.lock.writeLock().unlock();
            actionTraceLoggerBegin.done();
        }
    }

    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public <T> T getBean(Class<T> cls, Map<String, String> map) {
        T tCast;
        this.lock.readLock().lock();
        try {
            List<ServiceEntry> list = this.typeServiceEntries.get(cls);
            if (list != null && list.size() != 0) {
                if (map == null || map.size() == 0) {
                    tCast = cls.cast(list.get(0).instance);
                } else {
                    for (ServiceEntry serviceEntry : list) {
                        boolean z = true;
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            String str = serviceEntry.properties.get(entry.getKey());
                            if (str == null || !str.equals(entry.getValue())) {
                                z = false;
                                break;
                            }
                        }
                        if (z) {
                            tCast = cls.cast(serviceEntry.instance);
                        }
                    }
                }
                return tCast;
            }
            return null;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public <T> T[] getBeans(Class<T> cls, Map<String, String> map) {
        T[] tArr;
        boolean z;
        this.lock.readLock().lock();
        try {
            List<ServiceEntry> list = this.typeServiceEntries.get(cls);
            if (list == null || list.size() == 0) {
                tArr = (T[]) ((Object[]) Array.newInstance((Class<?>) cls, 0));
            } else {
                if (map != null && map.size() != 0) {
                    ArrayList arrayList = new ArrayList(list.size());
                    for (ServiceEntry serviceEntry : list) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            String str = serviceEntry.properties.get(entry.getKey());
                            if (str == null || !str.equals(entry.getValue())) {
                                z = false;
                                break;
                            }
                        }
                        z = true;
                        if (z) {
                            arrayList.add(cls.cast(serviceEntry.instance));
                        }
                    }
                    tArr = (T[]) arrayList.toArray((Object[]) Array.newInstance((Class<?>) cls, arrayList.size()));
                }
                T[] tArr2 = (T[]) ((Object[]) Array.newInstance((Class<?>) cls, list.size()));
                int size = list.size();
                for (int i = 0; i < size; i++) {
                    tArr2[i] = cls.cast(list.get(i).instance);
                }
                return tArr2;
            }
            return tArr;
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public void recycle() {
        this.lock.writeLock().lock();
        try {
            this.registrationServiceEntries.clear();
            this.typeServiceEntries.clear();
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistry
    public Object unregisterBean(BeanRegistration beanRegistration) {
        if (beanRegistration == null) {
            return null;
        }
        this.lock.writeLock().lock();
        try {
            ServiceEntry serviceEntryRemove = this.registrationServiceEntries.remove(beanRegistration);
            if (serviceEntryRemove == null) {
                return null;
            }
            if (serviceEntryRemove.types != null) {
                for (Class<?> cls : serviceEntryRemove.types) {
                    List<ServiceEntry> list = this.typeServiceEntries.get(cls);
                    Iterator<ServiceEntry> it = list.iterator();
                    while (true) {
                        if (!it.hasNext()) {
                            break;
                        }
                        if (it.next() == serviceEntryRemove) {
                            it.remove();
                            break;
                        }
                    }
                    if (list.size() == 0) {
                        this.typeServiceEntries.remove(cls);
                    }
                }
            }
            return serviceEntryRemove.instance;
        } finally {
            this.lock.writeLock().unlock();
        }
    }

    static class ServiceEntry {
        public Object instance;
        public Map<String, String> properties;
        public Class<?>[] types;

        ServiceEntry() {
        }
    }

    static class InternalServiceRegistration implements BeanRegistration {
        private Map<String, String> properties;
        private BeanRegistry serviceRegistry;
        private final String uuid = UUID.randomUUID().toString();

        public InternalServiceRegistration(BeanRegistry beanRegistry, Map<String, String> map) {
            this.serviceRegistry = beanRegistry;
            this.properties = map;
        }

        @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistration
        public void setProperties(Map<String, String> map) {
            if (map == null) {
                return;
            }
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    this.properties.put(entry.getKey(), entry.getValue());
                }
            }
        }

        @Override // com.alibaba.sdk.android.pluto.runtime.BeanRegistration
        public void unregister() {
            this.serviceRegistry.unregisterBean(this);
        }

        public int hashCode() {
            String str = this.uuid;
            return 31 + (str == null ? 0 : str.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj != null && getClass() == obj.getClass()) {
                return this.uuid.equals(((InternalServiceRegistration) obj).uuid);
            }
            return false;
        }
    }
}
