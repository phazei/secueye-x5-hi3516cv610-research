package com.aliyun.alink.linksdk.alcs.coap.resources;

import com.aliyun.alink.linksdk.alcs.coap.AlcsCoAPConstant;
import com.aliyun.alink.linksdk.alcs.coap.IAlcsCoAPResHandler;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Semaphore;

/* JADX INFO: loaded from: classes2.dex */
public class AlcsCoAPResource implements Resource {
    private static final String TAG = "AlcsCoAPResource";
    private final ResourceAttributes attributes;
    private int cType;
    private ConcurrentHashMap<String, Resource> children;
    private int expTime;
    protected IAlcsCoAPResHandler handler;
    private int maxAge;
    private String name;
    private int needAuth;
    private boolean observable;
    private AlcsCoAPConstant.Type observeType;
    private Resource parent;
    private String path;
    private int permission;
    private boolean visible;

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public boolean isCachable() {
        return true;
    }

    public AlcsCoAPResource(String str) {
        this(str, true);
    }

    public AlcsCoAPResource(String str, boolean z) {
        this.observeType = null;
        this.name = str;
        this.path = "";
        this.visible = z;
        this.attributes = new ResourceAttributes();
        this.children = new ConcurrentHashMap<>();
    }

    public IAlcsCoAPResHandler getHandler() {
        return this.handler;
    }

    public void setHandler(IAlcsCoAPResHandler iAlcsCoAPResHandler) {
        this.handler = iAlcsCoAPResHandler;
    }

    public int getPermission() {
        return this.permission;
    }

    public void setPermission(int i) {
        this.permission = i;
    }

    public int getcType() {
        return this.cType;
    }

    public void setcType(int i) {
        this.cType = i;
    }

    public int getMaxAge() {
        return this.maxAge;
    }

    public void setMaxAge(int i) {
        this.maxAge = i;
    }

    public int getExpTime() {
        return this.expTime;
    }

    public void setExpTime(int i) {
        this.expTime = i;
    }

    public int getNeedAuth() {
        return this.needAuth;
    }

    public void setNeedAuth(int i) {
        this.needAuth = i;
    }

    public synchronized AlcsCoAPResource addChild(AlcsCoAPResource alcsCoAPResource) {
        add(alcsCoAPResource);
        return this;
    }

    public synchronized Resource add(Resource... resourceArr) {
        for (Resource resource : resourceArr) {
            add(resource);
        }
        return this;
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public synchronized void add(Resource resource) {
        if (resource.getName() == null) {
            ALog.e(TAG, "Child must have a name");
            return;
        }
        if (resource.getParent() != null) {
            resource.getParent().delete(resource);
        }
        this.children.put(resource.getName(), resource);
        resource.setParent(this);
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public synchronized boolean delete(Resource resource) {
        if (delete(resource.getName()) != resource) {
            return false;
        }
        resource.setParent(null);
        resource.setPath(null);
        return true;
    }

    public synchronized Resource delete(String str) {
        return this.children.remove(str);
    }

    public synchronized void delete() {
        Resource parent = getParent();
        if (parent != null) {
            parent.delete(this);
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public Resource getParent() {
        return this.parent;
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public void setParent(Resource resource) {
        this.parent = resource;
        if (resource != null) {
            this.path = resource.getPath() + resource.getName() + "/";
        }
        adjustChildrenPath();
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public Resource getChild(String str) {
        return this.children.get(str);
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public ResourceAttributes getAttributes() {
        return this.attributes;
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public String getName() {
        return this.name;
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public String getPath() {
        return this.path;
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public String getURI() {
        return getPath() + getName();
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public synchronized void setPath(String str) {
        String str2 = this.path;
        this.path = str;
        adjustChildrenPath();
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public synchronized void setName(String str) {
        if (str == null) {
            ALog.e(TAG, "name NullPointerException");
            return;
        }
        String str2 = this.name;
        Resource parent = getParent();
        if (parent != null) {
            synchronized (parent) {
                parent.delete(this);
                this.name = str;
                parent.add(this);
            }
        } else {
            this.name = str;
        }
        adjustChildrenPath();
    }

    private void adjustChildrenPath() {
        String str = this.path + this.name + "/";
        Iterator<Resource> it = this.children.values().iterator();
        while (it.hasNext()) {
            it.next().setPath(str);
        }
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean z) {
        this.visible = z;
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public boolean isObservable() {
        return this.observable;
    }

    public void setObservable(boolean z) {
        this.observable = z;
    }

    public void setObserveType(AlcsCoAPConstant.Type type) {
        if (type == AlcsCoAPConstant.Type.ACK || type == AlcsCoAPConstant.Type.RST) {
            throw new IllegalArgumentException("Only CON and NON notifications are allowed or null for no changes by the framework");
        }
        this.observeType = type;
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public Collection<Resource> getChildren() {
        return this.children.values();
    }

    @Override // com.aliyun.alink.linksdk.alcs.coap.resources.Resource
    public ExecutorService getExecutor() {
        Resource resource = this.parent;
        if (resource != null) {
            return resource.getExecutor();
        }
        return null;
    }

    public void execute(Runnable runnable) {
        ExecutorService executor = getExecutor();
        if (executor == null) {
            runnable.run();
        } else {
            executor.execute(runnable);
        }
    }

    public void executeAndWait(final Runnable runnable) throws InterruptedException {
        final Semaphore semaphore = new Semaphore(0);
        execute(new Runnable() { // from class: com.aliyun.alink.linksdk.alcs.coap.resources.AlcsCoAPResource.1
            @Override // java.lang.Runnable
            public void run() {
                runnable.run();
                semaphore.release();
            }
        });
        semaphore.acquire();
    }
}
