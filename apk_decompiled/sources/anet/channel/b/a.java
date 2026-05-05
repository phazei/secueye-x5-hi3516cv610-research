package anet.channel.b;

import anet.channel.util.ALog;
import anet.channel.util.StringUtils;
import anetwork.channel.cache.Cache;
import com.taobao.alivfssdk.cache.AVFSCache;
import com.taobao.alivfssdk.cache.AVFSCacheConfig;
import com.taobao.alivfssdk.cache.AVFSCacheManager;
import com.taobao.alivfssdk.cache.IAVFSCache;

/* JADX INFO: compiled from: Taobao */
/* JADX INFO: loaded from: classes.dex */
public class a implements Cache {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private static boolean f1668a = true;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private static Object f1669b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private static Object f1670c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private static Object f1671d;

    static {
        try {
            Class.forName("com.taobao.alivfssdk.cache.AVFSCacheManager");
            f1669b = new b();
            f1670c = new c();
            f1671d = new d();
        } catch (ClassNotFoundException unused) {
            f1668a = false;
            ALog.w("anet.AVFSCacheImpl", "no alivfs sdk!", null, new Object[0]);
        }
    }

    public void a() {
        AVFSCache aVFSCacheCacheForModule;
        if (f1668a && (aVFSCacheCacheForModule = AVFSCacheManager.getInstance().cacheForModule("networksdk.httpcache")) != null) {
            AVFSCacheConfig aVFSCacheConfig = new AVFSCacheConfig();
            aVFSCacheConfig.limitSize = 5242880L;
            aVFSCacheConfig.fileMemMaxSize = 1048576L;
            aVFSCacheCacheForModule.moduleConfig(aVFSCacheConfig);
        }
    }

    @Override // anetwork.channel.cache.Cache
    public Cache.Entry get(String str) {
        if (!f1668a) {
            return null;
        }
        try {
            IAVFSCache iAVFSCacheB = b();
            if (iAVFSCacheB != null) {
                return (Cache.Entry) iAVFSCacheB.objectForKey(StringUtils.md5ToHex(str));
            }
        } catch (Exception e) {
            ALog.e("anet.AVFSCacheImpl", "get cache failed", null, e, new Object[0]);
        }
        return null;
    }

    @Override // anetwork.channel.cache.Cache
    public void put(String str, Cache.Entry entry) {
        if (f1668a) {
            try {
                IAVFSCache iAVFSCacheB = b();
                if (iAVFSCacheB != null) {
                    iAVFSCacheB.setObjectForKey(StringUtils.md5ToHex(str), entry, (IAVFSCache.OnObjectSetCallback) f1669b);
                }
            } catch (Exception e) {
                ALog.e("anet.AVFSCacheImpl", "put cache failed", null, e, new Object[0]);
            }
        }
    }

    @Override // anetwork.channel.cache.Cache
    public void remove(String str) {
        if (f1668a) {
            try {
                IAVFSCache iAVFSCacheB = b();
                if (iAVFSCacheB != null) {
                    iAVFSCacheB.removeObjectForKey(StringUtils.md5ToHex(str), (IAVFSCache.OnObjectRemoveCallback) f1670c);
                }
            } catch (Exception e) {
                ALog.e("anet.AVFSCacheImpl", "remove cache failed", null, e, new Object[0]);
            }
        }
    }

    @Override // anetwork.channel.cache.Cache
    public void clear() {
        if (f1668a) {
            try {
                IAVFSCache iAVFSCacheB = b();
                if (iAVFSCacheB != null) {
                    iAVFSCacheB.removeAllObject((IAVFSCache.OnAllObjectRemoveCallback) f1671d);
                }
            } catch (Exception e) {
                ALog.e("anet.AVFSCacheImpl", "clear cache failed", null, e, new Object[0]);
            }
        }
    }

    private IAVFSCache b() {
        AVFSCache aVFSCacheCacheForModule = AVFSCacheManager.getInstance().cacheForModule("networksdk.httpcache");
        if (aVFSCacheCacheForModule != null) {
            return aVFSCacheCacheForModule.getFileCache();
        }
        return null;
    }
}
