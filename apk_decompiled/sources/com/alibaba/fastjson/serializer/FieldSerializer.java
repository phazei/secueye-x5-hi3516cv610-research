package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/* JADX INFO: loaded from: classes.dex */
public class FieldSerializer implements Comparable<FieldSerializer> {
    protected boolean browserCompatible;
    protected boolean disableCircularReferenceDetect;
    private final String double_quoted_fieldPrefix;
    protected int features;
    protected BeanContext fieldContext;
    public final FieldInfo fieldInfo;
    private String format;
    protected boolean persistenceXToMany;
    private RuntimeSerializerInfo runtimeInfo;
    protected boolean serializeUsing = false;
    private String single_quoted_fieldPrefix;
    private String un_quoted_fieldPrefix;
    protected boolean writeEnumUsingName;
    protected boolean writeEnumUsingToString;
    protected final boolean writeNull;

    public FieldSerializer(Class<?> cls, FieldInfo fieldInfo) {
        boolean z;
        JSONType jSONType;
        this.writeEnumUsingToString = false;
        this.writeEnumUsingName = false;
        this.disableCircularReferenceDetect = false;
        this.persistenceXToMany = false;
        this.fieldInfo = fieldInfo;
        this.fieldContext = new BeanContext(cls, fieldInfo);
        if (cls != null && (jSONType = (JSONType) TypeUtils.getAnnotation(cls, JSONType.class)) != null) {
            for (SerializerFeature serializerFeature : jSONType.serialzeFeatures()) {
                if (serializerFeature == SerializerFeature.WriteEnumUsingToString) {
                    this.writeEnumUsingToString = true;
                } else if (serializerFeature == SerializerFeature.WriteEnumUsingName) {
                    this.writeEnumUsingName = true;
                } else if (serializerFeature == SerializerFeature.DisableCircularReferenceDetect) {
                    this.disableCircularReferenceDetect = true;
                } else if (serializerFeature == SerializerFeature.BrowserCompatible) {
                    this.features |= SerializerFeature.BrowserCompatible.mask;
                    this.browserCompatible = true;
                } else if (serializerFeature == SerializerFeature.WriteMapNullValue) {
                    this.features |= SerializerFeature.WriteMapNullValue.mask;
                }
            }
        }
        fieldInfo.setAccessible();
        this.double_quoted_fieldPrefix = '\"' + fieldInfo.name + "\":";
        JSONField annotation = fieldInfo.getAnnotation();
        if (annotation != null) {
            SerializerFeature[] serializerFeatureArrSerialzeFeatures = annotation.serialzeFeatures();
            int length = serializerFeatureArrSerialzeFeatures.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = false;
                    break;
                } else {
                    if ((serializerFeatureArrSerialzeFeatures[i].getMask() & SerializerFeature.WRITE_MAP_NULL_FEATURES) != 0) {
                        z = true;
                        break;
                    }
                    i++;
                }
            }
            this.format = annotation.format();
            if (this.format.trim().length() == 0) {
                this.format = null;
            }
            for (SerializerFeature serializerFeature2 : annotation.serialzeFeatures()) {
                if (serializerFeature2 == SerializerFeature.WriteEnumUsingToString) {
                    this.writeEnumUsingToString = true;
                } else if (serializerFeature2 == SerializerFeature.WriteEnumUsingName) {
                    this.writeEnumUsingName = true;
                } else if (serializerFeature2 == SerializerFeature.DisableCircularReferenceDetect) {
                    this.disableCircularReferenceDetect = true;
                } else if (serializerFeature2 == SerializerFeature.BrowserCompatible) {
                    this.browserCompatible = true;
                }
            }
            this.features = SerializerFeature.of(annotation.serialzeFeatures()) | this.features;
        } else {
            z = false;
        }
        this.writeNull = z;
        this.persistenceXToMany = TypeUtils.isAnnotationPresentOneToMany(fieldInfo.method) || TypeUtils.isAnnotationPresentManyToMany(fieldInfo.method);
    }

    public void writePrefix(JSONSerializer jSONSerializer) throws IOException {
        SerializeWriter serializeWriter = jSONSerializer.out;
        if (serializeWriter.quoteFieldNames) {
            if (SerializerFeature.isEnabled(serializeWriter.features, this.fieldInfo.serialzeFeatures, SerializerFeature.UseSingleQuotes)) {
                if (this.single_quoted_fieldPrefix == null) {
                    this.single_quoted_fieldPrefix = '\'' + this.fieldInfo.name + "':";
                }
                serializeWriter.write(this.single_quoted_fieldPrefix);
                return;
            }
            serializeWriter.write(this.double_quoted_fieldPrefix);
            return;
        }
        if (this.un_quoted_fieldPrefix == null) {
            this.un_quoted_fieldPrefix = this.fieldInfo.name + ":";
        }
        serializeWriter.write(this.un_quoted_fieldPrefix);
    }

    public Object getPropertyValueDirect(Object obj) throws IllegalAccessException, InvocationTargetException {
        Object obj2 = this.fieldInfo.get(obj);
        if (!this.persistenceXToMany || TypeUtils.isHibernateInitialized(obj2)) {
            return obj2;
        }
        return null;
    }

    public Object getPropertyValue(Object obj) throws IllegalAccessException, InvocationTargetException {
        Object obj2 = this.fieldInfo.get(obj);
        if (this.format == null || obj2 == null || !(this.fieldInfo.fieldClass == Date.class || this.fieldInfo.fieldClass == java.sql.Date.class)) {
            return obj2;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(this.format, JSON.defaultLocale);
        simpleDateFormat.setTimeZone(JSON.defaultTimeZone);
        return simpleDateFormat.format(obj2);
    }

    @Override // java.lang.Comparable
    public int compareTo(FieldSerializer fieldSerializer) {
        return this.fieldInfo.compareTo(fieldSerializer.fieldInfo);
    }

    public void writeValue(JSONSerializer jSONSerializer, Object obj) throws Exception {
        ObjectSerializer objectWriter;
        Class<?> cls;
        if (this.runtimeInfo == null) {
            if (obj == null) {
                cls = this.fieldInfo.fieldClass;
                if (cls == Byte.TYPE) {
                    cls = Byte.class;
                } else if (cls == Short.TYPE) {
                    cls = Short.class;
                } else if (cls == Integer.TYPE) {
                    cls = Integer.class;
                } else if (cls == Long.TYPE) {
                    cls = Long.class;
                } else if (cls == Float.TYPE) {
                    cls = Float.class;
                } else if (cls == Double.TYPE) {
                    cls = Double.class;
                } else if (cls == Boolean.TYPE) {
                    cls = Boolean.class;
                }
            } else {
                cls = obj.getClass();
            }
            ObjectSerializer objectWriter2 = null;
            JSONField annotation = this.fieldInfo.getAnnotation();
            if (annotation != null && annotation.serializeUsing() != Void.class) {
                objectWriter2 = (ObjectSerializer) annotation.serializeUsing().newInstance();
                this.serializeUsing = true;
            } else {
                if (this.format != null) {
                    if (cls == Double.TYPE || cls == Double.class) {
                        objectWriter2 = new DoubleSerializer(this.format);
                    } else if (cls == Float.TYPE || cls == Float.class) {
                        objectWriter2 = new FloatCodec(this.format);
                    }
                }
                if (objectWriter2 == null) {
                    objectWriter2 = jSONSerializer.getObjectWriter(cls);
                }
            }
            this.runtimeInfo = new RuntimeSerializerInfo(objectWriter2, cls);
        }
        RuntimeSerializerInfo runtimeSerializerInfo = this.runtimeInfo;
        int i = (this.disableCircularReferenceDetect ? this.fieldInfo.serialzeFeatures | SerializerFeature.DisableCircularReferenceDetect.mask : this.fieldInfo.serialzeFeatures) | this.features;
        if (obj == null) {
            SerializeWriter serializeWriter = jSONSerializer.out;
            if (this.fieldInfo.fieldClass == Object.class && serializeWriter.isEnabled(SerializerFeature.WRITE_MAP_NULL_FEATURES)) {
                serializeWriter.writeNull();
                return;
            }
            Class<?> cls2 = runtimeSerializerInfo.runtimeFieldClass;
            if (Number.class.isAssignableFrom(cls2)) {
                serializeWriter.writeNull(this.features, SerializerFeature.WriteNullNumberAsZero.mask);
                return;
            }
            if (String.class == cls2) {
                serializeWriter.writeNull(this.features, SerializerFeature.WriteNullStringAsEmpty.mask);
                return;
            }
            if (Boolean.class == cls2) {
                serializeWriter.writeNull(this.features, SerializerFeature.WriteNullBooleanAsFalse.mask);
                return;
            }
            if (Collection.class.isAssignableFrom(cls2) || cls2.isArray()) {
                serializeWriter.writeNull(this.features, SerializerFeature.WriteNullListAsEmpty.mask);
                return;
            }
            ObjectSerializer objectSerializer = runtimeSerializerInfo.fieldSerializer;
            if (serializeWriter.isEnabled(SerializerFeature.WRITE_MAP_NULL_FEATURES) && (objectSerializer instanceof JavaBeanSerializer)) {
                serializeWriter.writeNull();
                return;
            } else {
                objectSerializer.write(jSONSerializer, null, this.fieldInfo.name, this.fieldInfo.fieldType, i);
                return;
            }
        }
        if (this.fieldInfo.isEnum) {
            if (this.writeEnumUsingName) {
                jSONSerializer.out.writeString(((Enum) obj).name());
                return;
            } else if (this.writeEnumUsingToString) {
                jSONSerializer.out.writeString(((Enum) obj).toString());
                return;
            }
        }
        Class<?> cls3 = obj.getClass();
        if (cls3 == runtimeSerializerInfo.runtimeFieldClass || this.serializeUsing) {
            objectWriter = runtimeSerializerInfo.fieldSerializer;
        } else {
            objectWriter = jSONSerializer.getObjectWriter(cls3);
        }
        String str = this.format;
        if (str != null && !(objectWriter instanceof DoubleSerializer) && !(objectWriter instanceof FloatCodec)) {
            if (objectWriter instanceof ContextObjectSerializer) {
                ((ContextObjectSerializer) objectWriter).write(jSONSerializer, obj, this.fieldContext);
                return;
            } else {
                jSONSerializer.writeWithFormat(obj, str);
                return;
            }
        }
        if (this.fieldInfo.unwrapped) {
            if (objectWriter instanceof JavaBeanSerializer) {
                ((JavaBeanSerializer) objectWriter).write(jSONSerializer, obj, this.fieldInfo.name, this.fieldInfo.fieldType, i, true);
                return;
            } else if (objectWriter instanceof MapSerializer) {
                ((MapSerializer) objectWriter).write(jSONSerializer, obj, this.fieldInfo.name, this.fieldInfo.fieldType, i, true);
                return;
            }
        }
        if ((this.features & SerializerFeature.WriteClassName.mask) != 0 && cls3 != this.fieldInfo.fieldClass && (objectWriter instanceof JavaBeanSerializer)) {
            ((JavaBeanSerializer) objectWriter).write(jSONSerializer, obj, this.fieldInfo.name, this.fieldInfo.fieldType, i, false);
            return;
        }
        if (this.browserCompatible && (this.fieldInfo.fieldClass == Long.TYPE || this.fieldInfo.fieldClass == Long.class)) {
            long jLongValue = ((Long) obj).longValue();
            if (jLongValue > 9007199254740991L || jLongValue < -9007199254740991L) {
                jSONSerializer.getWriter().writeString(Long.toString(jLongValue));
                return;
            }
        }
        objectWriter.write(jSONSerializer, obj, this.fieldInfo.name, this.fieldInfo.fieldType, i);
    }

    static class RuntimeSerializerInfo {
        final ObjectSerializer fieldSerializer;
        final Class<?> runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer objectSerializer, Class<?> cls) {
            this.fieldSerializer = objectSerializer;
            this.runtimeFieldClass = cls;
        }
    }
}
