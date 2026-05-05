package com.aliyun.alink.business.devicecenter.plugin;

import android.content.Context;
import com.aliyun.alink.business.devicecenter.api.add.LinkType;
import com.aliyun.alink.business.devicecenter.config.IConfigStrategy;
import com.aliyun.alink.business.devicecenter.log.ALog;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

/* JADX INFO: loaded from: classes.dex */
public class ProvisionPlugin implements IProvisionPlugin {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    public ConcurrentHashMap<LinkType, Class<? extends IConfigStrategy>> f3654a;

    private static class SingletonHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        public static final ProvisionPlugin f3655a = new ProvisionPlugin();
    }

    public static ProvisionPlugin getInstance() {
        return SingletonHolder.f3655a;
    }

    public IConfigStrategy createStrategy(Context context, LinkType linkType) {
        ALog.d("ProvisionPlugin", "createStrategy() called with: context = [" + context + "], linkType = [" + linkType + "]");
        if (linkType == null) {
            throw new IllegalArgumentException("linkType empty.");
        }
        if (!this.f3654a.containsKey(linkType)) {
            ALog.w("ProvisionPlugin", "provision strategy not exist.");
            return null;
        }
        Class<? extends IConfigStrategy> cls = this.f3654a.get(linkType);
        if (cls == null) {
            ALog.w("ProvisionPlugin", "strategyHashMap classType=null, linkType=" + linkType);
            return null;
        }
        try {
            return cls.getConstructor(Context.class).newInstance(context);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            ALog.w("ProvisionPlugin", "createStrategy IllegalAccessException=" + e);
            return null;
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            ALog.w("ProvisionPlugin", "createStrategy InstantiationException=" + e2);
            return null;
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
            ALog.w("ProvisionPlugin", "createStrategy NoSuchMethodException=" + e3);
            return null;
        } catch (InvocationTargetException e4) {
            e4.printStackTrace();
            ALog.w("ProvisionPlugin", "createStrategy InvocationTargetException=" + e4);
            return null;
        } catch (Throwable th) {
            th.printStackTrace();
            ALog.w("ProvisionPlugin", "createStrategy exception=" + th);
            return null;
        }
    }

    public Class<? extends IConfigStrategy> getStrategyType(LinkType linkType) {
        ALog.d("ProvisionPlugin", "getStrategyType() called with: linkType = [" + linkType + "]");
        if (linkType == null) {
            return null;
        }
        return this.f3654a.get(linkType);
    }

    public boolean isStrategyExist(LinkType linkType) {
        if (linkType == null) {
            return false;
        }
        return this.f3654a.containsKey(linkType);
    }

    @Override // com.aliyun.alink.business.devicecenter.plugin.IProvisionPlugin
    public void registerProvisionStrategy(LinkType linkType, Class<? extends IConfigStrategy> cls) {
        if (linkType == null) {
            ALog.w("ProvisionPlugin", "registerProvisionStrategy fail, linkType=null");
            throw new IllegalArgumentException("linkType empty.");
        }
        if (cls != null) {
            if (this.f3654a.containsKey(linkType)) {
                return;
            }
            this.f3654a.put(linkType, cls);
        } else {
            ALog.w("ProvisionPlugin", "registerProvisionStrategy fail, linkType=" + linkType);
            throw new IllegalArgumentException("strategy class type empty.");
        }
    }

    @Override // com.aliyun.alink.business.devicecenter.plugin.IProvisionPlugin
    public void unregisterProvisionStrategy(LinkType linkType) {
        ALog.d("ProvisionPlugin", "unregisterProvisionStrategy() called with: linkType = [" + linkType + "]");
        if (linkType != null && this.f3654a.containsKey(linkType)) {
            this.f3654a.remove(linkType);
        }
    }

    public ProvisionPlugin() {
        this.f3654a = null;
        this.f3654a = new ConcurrentHashMap<>();
    }
}
