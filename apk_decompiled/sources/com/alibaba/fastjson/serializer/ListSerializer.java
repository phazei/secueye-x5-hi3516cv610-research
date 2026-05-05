package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.util.TypeUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import io.netty.util.internal.StringUtil;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public final class ListSerializer implements ObjectSerializer {
    public static final ListSerializer instance = new ListSerializer();

    @Override // com.alibaba.fastjson.serializer.ObjectSerializer
    public final void write(JSONSerializer jSONSerializer, Object obj, Object obj2, Type type, int i) throws IOException {
        char c2;
        int i2;
        Object obj3;
        boolean z;
        char c3;
        boolean z2 = jSONSerializer.out.isEnabled(SerializerFeature.WriteClassName) || SerializerFeature.isEnabled(i, SerializerFeature.WriteClassName);
        SerializeWriter serializeWriter = jSONSerializer.out;
        Type collectionItemType = z2 ? TypeUtils.getCollectionItemType(type) : null;
        if (obj == null) {
            serializeWriter.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        List list = (List) obj;
        if (list.size() == 0) {
            serializeWriter.append((CharSequence) "[]");
            return;
        }
        SerialContext serialContext = jSONSerializer.context;
        jSONSerializer.setContext(serialContext, obj, obj2, 0);
        try {
            boolean zIsEnabled = serializeWriter.isEnabled(SerializerFeature.PrettyFormat);
            char c4 = StringUtil.COMMA;
            char c5 = ']';
            if (zIsEnabled) {
                serializeWriter.append('[');
                jSONSerializer.incrementIndent();
                int i3 = 0;
                for (Object obj4 : list) {
                    if (i3 != 0) {
                        serializeWriter.append(c4);
                    }
                    jSONSerializer.println();
                    if (obj4 != null) {
                        if (jSONSerializer.containsReference(obj4)) {
                            jSONSerializer.writeReference(obj4);
                            c3 = c5;
                        } else {
                            ObjectSerializer objectWriter = jSONSerializer.getObjectWriter(obj4.getClass());
                            c3 = c5;
                            jSONSerializer.context = new SerialContext(serialContext, obj, obj2, 0, 0);
                            objectWriter.write(jSONSerializer, obj4, Integer.valueOf(i3), collectionItemType, i);
                        }
                    } else {
                        c3 = c5;
                        jSONSerializer.out.writeNull();
                    }
                    i3++;
                    c5 = c3;
                    c4 = StringUtil.COMMA;
                }
                jSONSerializer.decrementIdent();
                jSONSerializer.println();
                serializeWriter.append(c5);
                return;
            }
            char c6 = ']';
            serializeWriter.append('[');
            int size = list.size();
            int i4 = 0;
            while (i4 < size) {
                Object obj5 = list.get(i4);
                if (i4 != 0) {
                    c2 = StringUtil.COMMA;
                    serializeWriter.append(StringUtil.COMMA);
                } else {
                    c2 = StringUtil.COMMA;
                }
                if (obj5 == null) {
                    serializeWriter.append((CharSequence) TmpConstant.GROUP_ROLE_UNKNOWN);
                    i2 = i4;
                    z = z2;
                } else {
                    Class<?> cls = obj5.getClass();
                    if (cls == Integer.class) {
                        serializeWriter.writeInt(((Integer) obj5).intValue());
                        i2 = i4;
                        z = z2;
                    } else if (cls == Long.class) {
                        long jLongValue = ((Long) obj5).longValue();
                        if (z2) {
                            serializeWriter.writeLong(jLongValue);
                            serializeWriter.write(76);
                        } else {
                            serializeWriter.writeLong(jLongValue);
                        }
                        i2 = i4;
                        z = z2;
                    } else if ((SerializerFeature.DisableCircularReferenceDetect.mask & i) != 0) {
                        i2 = i4;
                        jSONSerializer.getObjectWriter(obj5.getClass()).write(jSONSerializer, obj5, Integer.valueOf(i4), collectionItemType, i);
                        z = z2;
                    } else {
                        i2 = i4;
                        if (serializeWriter.disableCircularReferenceDetect) {
                            obj3 = obj5;
                            z = z2;
                        } else {
                            obj3 = obj5;
                            z = z2;
                            jSONSerializer.context = new SerialContext(serialContext, obj, obj2, 0, 0);
                        }
                        if (jSONSerializer.containsReference(obj3)) {
                            jSONSerializer.writeReference(obj3);
                        } else {
                            ObjectSerializer objectWriter2 = jSONSerializer.getObjectWriter(obj3.getClass());
                            if ((SerializerFeature.WriteClassName.mask & i) != 0 && (objectWriter2 instanceof JavaBeanSerializer)) {
                                ((JavaBeanSerializer) objectWriter2).writeNoneASM(jSONSerializer, obj3, Integer.valueOf(i2), collectionItemType, i);
                            } else {
                                objectWriter2.write(jSONSerializer, obj3, Integer.valueOf(i2), collectionItemType, i);
                            }
                        }
                    }
                }
                i4 = i2 + 1;
                z2 = z;
                c6 = ']';
            }
            serializeWriter.append(c6);
        } finally {
            jSONSerializer.context = serialContext;
        }
    }
}
