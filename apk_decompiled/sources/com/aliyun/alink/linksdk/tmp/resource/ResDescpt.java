package com.aliyun.alink.linksdk.tmp.resource;

import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes2.dex */
public class ResDescpt {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected String f4406a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected List<a> f4407b = new ArrayList();

    public enum ResElementType {
        PROPERTY,
        SERVICE,
        EVENT,
        DISCOVERY,
        ALCS
    }

    public ResDescpt(String str) {
        this.f4406a = str;
    }

    public void a(a aVar) {
        this.f4407b.add(aVar);
    }

    public List<a> a() {
        return this.f4407b;
    }

    public static class a {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        protected String f4408a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        protected ResElementType f4409b;

        public a(String str, ResElementType resElementType) {
            this.f4408a = str;
            this.f4409b = resElementType;
        }
    }
}
