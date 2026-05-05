package com.aliyun.alink.linksdk.alcs.coap.resources;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

/* JADX INFO: loaded from: classes2.dex */
public interface Resource {
    void add(Resource resource);

    boolean delete(Resource resource);

    ResourceAttributes getAttributes();

    Resource getChild(String str);

    Collection<Resource> getChildren();

    ExecutorService getExecutor();

    String getName();

    Resource getParent();

    String getPath();

    String getURI();

    boolean isCachable();

    boolean isObservable();

    boolean isVisible();

    void setName(String str);

    void setParent(Resource resource);

    void setPath(String str);
}
