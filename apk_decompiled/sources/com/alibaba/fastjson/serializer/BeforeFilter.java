package com.alibaba.fastjson.serializer;

import io.netty.util.internal.StringUtil;

/* JADX INFO: loaded from: classes.dex */
public abstract class BeforeFilter implements SerializeFilter {
    private static final ThreadLocal<JSONSerializer> serializerLocal = new ThreadLocal<>();
    private static final ThreadLocal<Character> seperatorLocal = new ThreadLocal<>();
    private static final Character COMMA = Character.valueOf(StringUtil.COMMA);

    public abstract void writeBefore(Object obj);

    final char writeBefore(JSONSerializer jSONSerializer, Object obj, char c2) {
        JSONSerializer jSONSerializer2 = serializerLocal.get();
        serializerLocal.set(jSONSerializer);
        seperatorLocal.set(Character.valueOf(c2));
        writeBefore(obj);
        serializerLocal.set(jSONSerializer2);
        return seperatorLocal.get().charValue();
    }

    protected final void writeKeyValue(String str, Object obj) {
        JSONSerializer jSONSerializer = serializerLocal.get();
        char cCharValue = seperatorLocal.get().charValue();
        boolean zContainsKey = jSONSerializer.references.containsKey(obj);
        jSONSerializer.writeKeyValue(cCharValue, str, obj);
        if (!zContainsKey) {
            jSONSerializer.references.remove(obj);
        }
        if (cCharValue != ',') {
            seperatorLocal.set(COMMA);
        }
    }
}
