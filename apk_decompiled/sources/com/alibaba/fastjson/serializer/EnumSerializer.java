package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/* JADX INFO: loaded from: classes.dex */
public class EnumSerializer implements ObjectSerializer {
    public static final EnumSerializer instance = new EnumSerializer();
    private final Member member;

    public EnumSerializer() {
        this.member = null;
    }

    public EnumSerializer(Member member) {
        this.member = member;
    }

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        Object objInvoke;
        Member member = this.member;
        if (member == null) {
            jSONSerializer.out.writeEnum((Enum) obj);
            return;
        }
        try {
            if (member instanceof Field) {
                objInvoke = ((Field) member).get(obj);
            } else {
                objInvoke = ((Method) member).invoke(obj, new Object[0]);
            }
            jSONSerializer.write(objInvoke);
        } catch (Exception e) {
            throw new JSONException("getEnumValue error", e);
        }
    }
}
