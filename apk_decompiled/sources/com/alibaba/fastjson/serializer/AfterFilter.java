package com.alibaba.fastjson.serializer;

import io.netty.util.internal.StringUtil;

/* JADX INFO: loaded from: classes.dex */
public abstract class AfterFilter implements SerializeFilter {
    private static final ThreadLocal<JSONSerializer> serializerLocal = new ThreadLocal<>();
    private static final ThreadLocal<Character> seperatorLocal = new ThreadLocal<>();
    private static final Character COMMA = Character.valueOf(StringUtil.COMMA);

    public abstract void writeAfter(Object obj);

    final char writeAfter(JSONSerializer jSONSerializer, Object obj, char c2) {
        JSONSerializer jSONSerializer2 = serializerLocal.get();
        serializerLocal.set(jSONSerializer);
        seperatorLocal.set(Character.valueOf(c2));
        writeAfter(obj);
        serializerLocal.set(jSONSerializer2);
        return seperatorLocal.get().charValue();
    }

    protected final void writeKeyValue(String str, Object obj) {
        JSONSerializer jSONSerializer = serializerLocal.get();
        char cCharValue = seperatorLocal.get().charValue();
        boolean zContainsReference = jSONSerializer.containsReference(obj);
        jSONSerializer.writeKeyValue(cCharValue, str, obj);
        if (!zContainsReference && jSONSerializer.references != null) {
            jSONSerializer.references.remove(obj);
        }
        if (cCharValue != ',') {
            seperatorLocal.set(COMMA);
        }
    }
}
