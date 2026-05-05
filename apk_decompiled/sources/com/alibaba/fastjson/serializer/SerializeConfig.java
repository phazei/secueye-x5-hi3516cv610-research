package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONAware;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONStreamAware;
import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.spi.Module;
import com.alibaba.fastjson.support.moneta.MonetaCodec;
import com.alibaba.fastjson.support.springfox.SwaggerJsonSerializer;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.ServiceLoader;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;
import javax.xml.datatype.XMLGregorianCalendar;
import org.w3c.dom.Node;

/* JADX INFO: loaded from: classes.dex */
public class SerializeConfig {
    private boolean asm;
    private ASMSerializerFactory asmFactory;
    private long[] denyClasses;
    private final boolean fieldBased;
    private final IdentityHashMap<Type, IdentityHashMap<Type, ObjectSerializer>> mixInSerializers;
    private List<Module> modules;
    public PropertyNamingStrategy propertyNamingStrategy;
    private final IdentityHashMap<Type, ObjectSerializer> serializers;
    protected String typeKey;
    public static final SerializeConfig globalInstance = new SerializeConfig();
    private static boolean awtError = false;
    private static boolean jdk8Error = false;
    private static boolean oracleJdbcError = false;
    private static boolean springfoxError = false;
    private static boolean guavaError = false;
    private static boolean jodaError = false;

    public String getTypeKey() {
        return this.typeKey;
    }

    public void setTypeKey(String str) {
        this.typeKey = str;
    }

    private final JavaBeanSerializer createASMSerializer(SerializeBeanInfo serializeBeanInfo) throws Exception {
        JavaBeanSerializer javaBeanSerializerCreateJavaBeanSerializer = this.asmFactory.createJavaBeanSerializer(serializeBeanInfo);
        for (int i = 0; i < javaBeanSerializerCreateJavaBeanSerializer.sortedGetters.length; i++) {
            Class<?> cls = javaBeanSerializerCreateJavaBeanSerializer.sortedGetters[i].fieldInfo.fieldClass;
            if (cls.isEnum() && !(getObjectWriter(cls) instanceof EnumSerializer)) {
                javaBeanSerializerCreateJavaBeanSerializer.writeDirect = false;
            }
        }
        return javaBeanSerializerCreateJavaBeanSerializer;
    }

    public final ObjectSerializer createJavaBeanSerializer(Class<?> cls) {
        String name = cls.getName();
        if (Arrays.binarySearch(this.denyClasses, TypeUtils.fnv1a_64(name)) >= 0) {
            throw new JSONException("not support class : " + name);
        }
        SerializeBeanInfo serializeBeanInfoBuildBeanInfo = TypeUtils.buildBeanInfo(cls, null, this.propertyNamingStrategy, this.fieldBased);
        if (serializeBeanInfoBuildBeanInfo.fields.length == 0 && Iterable.class.isAssignableFrom(cls)) {
            return MiscCodec.instance;
        }
        return createJavaBeanSerializer(serializeBeanInfoBuildBeanInfo);
    }

    public ObjectSerializer createJavaBeanSerializer(SerializeBeanInfo serializeBeanInfo) {
        Method method;
        JSONType jSONType = serializeBeanInfo.jsonType;
        boolean z = false;
        boolean z2 = this.asm && !this.fieldBased;
        if (jSONType != null) {
            Class<?> clsSerializer = jSONType.serializer();
            if (clsSerializer != Void.class) {
                try {
                    Object objNewInstance = clsSerializer.newInstance();
                    if (objNewInstance instanceof ObjectSerializer) {
                        return (ObjectSerializer) objNewInstance;
                    }
                } catch (Throwable unused) {
                }
            }
            if (!jSONType.asm()) {
                z2 = false;
            }
            if (z2) {
                for (SerializerFeature serializerFeature : jSONType.serialzeFeatures()) {
                    if (SerializerFeature.WriteNonStringValueAsString == serializerFeature || SerializerFeature.WriteEnumUsingToString == serializerFeature || SerializerFeature.NotWriteDefaultValue == serializerFeature || SerializerFeature.BrowserCompatible == serializerFeature) {
                        z2 = false;
                        break;
                    }
                }
            }
            if (z2 && jSONType.serialzeFilters().length != 0) {
                z2 = false;
            }
        }
        Class<?> cls = serializeBeanInfo.beanType;
        if (!Modifier.isPublic(serializeBeanInfo.beanType.getModifiers())) {
            return new JavaBeanSerializer(serializeBeanInfo);
        }
        if ((z2 && this.asmFactory.classLoader.isExternalClass(cls)) || cls == Serializable.class || cls == Object.class) {
            z2 = false;
        }
        if (z2 && !ASMUtils.checkName(cls.getSimpleName())) {
            z2 = false;
        }
        if (z2 && serializeBeanInfo.beanType.isInterface()) {
            z2 = false;
        }
        if (z2) {
            FieldInfo[] fieldInfoArr = serializeBeanInfo.fields;
            int length = fieldInfoArr.length;
            boolean z3 = z2;
            int i = 0;
            while (true) {
                if (i >= length) {
                    z = z3;
                    break;
                }
                FieldInfo fieldInfo = fieldInfoArr[i];
                Field field = fieldInfo.field;
                if ((field != null && !field.getType().equals(fieldInfo.fieldClass)) || (((method = fieldInfo.method) != null && !method.getReturnType().equals(fieldInfo.fieldClass)) || (fieldInfo.fieldClass.isEnum() && get(fieldInfo.fieldClass) != EnumSerializer.instance))) {
                    break;
                }
                JSONField annotation = fieldInfo.getAnnotation();
                if (annotation != null) {
                    String str = annotation.format();
                    if ((str.length() != 0 && (fieldInfo.fieldClass != String.class || !"trim".equals(str))) || !ASMUtils.checkName(annotation.name()) || annotation.jsonDirect() || annotation.serializeUsing() != Void.class || annotation.unwrapped()) {
                        break;
                    }
                    for (SerializerFeature serializerFeature2 : annotation.serialzeFeatures()) {
                        if (SerializerFeature.WriteNonStringValueAsString == serializerFeature2 || SerializerFeature.WriteEnumUsingToString == serializerFeature2 || SerializerFeature.NotWriteDefaultValue == serializerFeature2 || SerializerFeature.BrowserCompatible == serializerFeature2 || SerializerFeature.WriteClassName == serializerFeature2) {
                            z3 = false;
                            break;
                        }
                    }
                    if (TypeUtils.isAnnotationPresentOneToMany(method) || TypeUtils.isAnnotationPresentManyToMany(method) || (annotation.defaultValue() != null && !"".equals(annotation.defaultValue()))) {
                        break;
                    }
                }
                i++;
            }
        } else {
            z = z2;
        }
        if (z) {
            try {
                JavaBeanSerializer javaBeanSerializerCreateASMSerializer = createASMSerializer(serializeBeanInfo);
                if (javaBeanSerializerCreateASMSerializer != null) {
                    return javaBeanSerializerCreateASMSerializer;
                }
            } catch (ClassCastException | ClassFormatError | ClassNotFoundException unused2) {
            } catch (OutOfMemoryError e) {
                if (e.getMessage().indexOf("Metaspace") != -1) {
                    throw e;
                }
            } catch (Throwable th) {
                throw new JSONException("create asm serializer error, verson 1.2.76, class " + cls, th);
            }
        }
        return new JavaBeanSerializer(serializeBeanInfo);
    }

    public boolean isAsmEnable() {
        return this.asm;
    }

    public void setAsmEnable(boolean z) {
        if (ASMUtils.IS_ANDROID) {
            return;
        }
        this.asm = z;
    }

    public static SerializeConfig getGlobalInstance() {
        return globalInstance;
    }

    public SerializeConfig() {
        this(8192);
    }

    public SerializeConfig(boolean z) {
        this(8192, z);
    }

    public SerializeConfig(int i) {
        this(i, false);
    }

    public SerializeConfig(int i, boolean z) {
        this.asm = !ASMUtils.IS_ANDROID;
        this.typeKey = JSON.DEFAULT_TYPE_KEY;
        this.denyClasses = new long[]{4165360493669296979L, 4446674157046724083L};
        this.modules = new ArrayList();
        this.fieldBased = z;
        this.serializers = new IdentityHashMap<>(i);
        this.mixInSerializers = new IdentityHashMap<>(16);
        try {
            if (this.asm) {
                this.asmFactory = new ASMSerializerFactory();
            }
        } catch (Throwable unused) {
            this.asm = false;
        }
        initSerializers();
    }

    private void initSerializers() {
        put(Boolean.class, (ObjectSerializer) BooleanCodec.instance);
        put(Character.class, (ObjectSerializer) CharacterCodec.instance);
        put(Byte.class, (ObjectSerializer) IntegerCodec.instance);
        put(Short.class, (ObjectSerializer) IntegerCodec.instance);
        put(Integer.class, (ObjectSerializer) IntegerCodec.instance);
        put(Long.class, (ObjectSerializer) LongCodec.instance);
        put(Float.class, (ObjectSerializer) FloatCodec.instance);
        put(Double.class, (ObjectSerializer) DoubleSerializer.instance);
        put(BigDecimal.class, (ObjectSerializer) BigDecimalCodec.instance);
        put(BigInteger.class, (ObjectSerializer) BigIntegerCodec.instance);
        put(String.class, (ObjectSerializer) StringCodec.instance);
        put(byte[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(short[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(int[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(long[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(float[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(double[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(boolean[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(char[].class, (ObjectSerializer) PrimitiveArraySerializer.instance);
        put(Object[].class, (ObjectSerializer) ObjectArrayCodec.instance);
        put(Class.class, (ObjectSerializer) MiscCodec.instance);
        put(SimpleDateFormat.class, (ObjectSerializer) MiscCodec.instance);
        put(Currency.class, (ObjectSerializer) new MiscCodec());
        put(TimeZone.class, (ObjectSerializer) MiscCodec.instance);
        put(InetAddress.class, (ObjectSerializer) MiscCodec.instance);
        put(Inet4Address.class, (ObjectSerializer) MiscCodec.instance);
        put(Inet6Address.class, (ObjectSerializer) MiscCodec.instance);
        put(InetSocketAddress.class, (ObjectSerializer) MiscCodec.instance);
        put(File.class, (ObjectSerializer) MiscCodec.instance);
        put(Appendable.class, (ObjectSerializer) AppendableSerializer.instance);
        put(StringBuffer.class, (ObjectSerializer) AppendableSerializer.instance);
        put(StringBuilder.class, (ObjectSerializer) AppendableSerializer.instance);
        put(Charset.class, (ObjectSerializer) ToStringSerializer.instance);
        put(Pattern.class, (ObjectSerializer) ToStringSerializer.instance);
        put(Locale.class, (ObjectSerializer) ToStringSerializer.instance);
        put(URI.class, (ObjectSerializer) ToStringSerializer.instance);
        put(URL.class, (ObjectSerializer) ToStringSerializer.instance);
        put(UUID.class, (ObjectSerializer) ToStringSerializer.instance);
        put(AtomicBoolean.class, (ObjectSerializer) AtomicCodec.instance);
        put(AtomicInteger.class, (ObjectSerializer) AtomicCodec.instance);
        put(AtomicLong.class, (ObjectSerializer) AtomicCodec.instance);
        put(AtomicReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put(AtomicIntegerArray.class, (ObjectSerializer) AtomicCodec.instance);
        put(AtomicLongArray.class, (ObjectSerializer) AtomicCodec.instance);
        put(WeakReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put(SoftReference.class, (ObjectSerializer) ReferenceCodec.instance);
        put(LinkedList.class, (ObjectSerializer) CollectionCodec.instance);
    }

    public void addFilter(Class<?> cls, SerializeFilter serializeFilter) {
        Object objectWriter = getObjectWriter(cls);
        if (objectWriter instanceof SerializeFilterable) {
            SerializeFilterable serializeFilterable = (SerializeFilterable) objectWriter;
            if (this != globalInstance && serializeFilterable == MapSerializer.instance) {
                MapSerializer mapSerializer = new MapSerializer();
                put((Type) cls, (ObjectSerializer) mapSerializer);
                mapSerializer.addFilter(serializeFilter);
                return;
            }
            serializeFilterable.addFilter(serializeFilter);
        }
    }

    public void config(Class<?> cls, SerializerFeature serializerFeature, boolean z) throws NoSuchMethodException {
        ObjectSerializer objectWriter = getObjectWriter(cls, false);
        if (objectWriter == null) {
            SerializeBeanInfo serializeBeanInfoBuildBeanInfo = TypeUtils.buildBeanInfo(cls, null, this.propertyNamingStrategy);
            if (z) {
                serializeBeanInfoBuildBeanInfo.features = serializerFeature.mask | serializeBeanInfoBuildBeanInfo.features;
            } else {
                serializeBeanInfoBuildBeanInfo.features = (~serializerFeature.mask) & serializeBeanInfoBuildBeanInfo.features;
            }
            put((Type) cls, createJavaBeanSerializer(serializeBeanInfoBuildBeanInfo));
            return;
        }
        if (objectWriter instanceof JavaBeanSerializer) {
            SerializeBeanInfo serializeBeanInfo = ((JavaBeanSerializer) objectWriter).beanInfo;
            int i = serializeBeanInfo.features;
            if (z) {
                serializeBeanInfo.features = serializerFeature.mask | serializeBeanInfo.features;
            } else {
                serializeBeanInfo.features = (~serializerFeature.mask) & serializeBeanInfo.features;
            }
            if (i == serializeBeanInfo.features || objectWriter.getClass() == JavaBeanSerializer.class) {
                return;
            }
            put((Type) cls, createJavaBeanSerializer(serializeBeanInfo));
        }
    }

    public ObjectSerializer getObjectWriter(Class<?> cls) {
        return getObjectWriter(cls, true);
    }

    public ObjectSerializer getObjectWriter(Class<?> cls, boolean z) throws NoSuchMethodException {
        JSONType jSONType;
        ClassLoader classLoader;
        ObjectSerializer objectSerializer = get(cls);
        if (objectSerializer != null) {
            return objectSerializer;
        }
        try {
            for (Object obj : ServiceLoader.load(AutowiredObjectSerializer.class, Thread.currentThread().getContextClassLoader())) {
                if (obj instanceof AutowiredObjectSerializer) {
                    AutowiredObjectSerializer autowiredObjectSerializer = (AutowiredObjectSerializer) obj;
                    Iterator<Type> it = autowiredObjectSerializer.getAutowiredFor().iterator();
                    while (it.hasNext()) {
                        put(it.next(), (ObjectSerializer) autowiredObjectSerializer);
                    }
                }
            }
        } catch (ClassCastException unused) {
        }
        ObjectSerializer objectSerializerCreateJavaBeanSerializer = get(cls);
        if (objectSerializerCreateJavaBeanSerializer == null && (classLoader = JSON.class.getClassLoader()) != Thread.currentThread().getContextClassLoader()) {
            try {
                for (Object obj2 : ServiceLoader.load(AutowiredObjectSerializer.class, classLoader)) {
                    if (obj2 instanceof AutowiredObjectSerializer) {
                        AutowiredObjectSerializer autowiredObjectSerializer2 = (AutowiredObjectSerializer) obj2;
                        Iterator<Type> it2 = autowiredObjectSerializer2.getAutowiredFor().iterator();
                        while (it2.hasNext()) {
                            put(it2.next(), (ObjectSerializer) autowiredObjectSerializer2);
                        }
                    }
                }
            } catch (ClassCastException unused2) {
            }
            objectSerializerCreateJavaBeanSerializer = get(cls);
        }
        Iterator<Module> it3 = this.modules.iterator();
        while (it3.hasNext()) {
            objectSerializerCreateJavaBeanSerializer = it3.next().createSerializer(this, cls);
            if (objectSerializerCreateJavaBeanSerializer != null) {
                put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                return objectSerializerCreateJavaBeanSerializer;
            }
        }
        if (objectSerializerCreateJavaBeanSerializer != null) {
            return objectSerializerCreateJavaBeanSerializer;
        }
        String name = cls.getName();
        if (Map.class.isAssignableFrom(cls)) {
            objectSerializerCreateJavaBeanSerializer = MapSerializer.instance;
            put((Type) cls, objectSerializerCreateJavaBeanSerializer);
        } else if (List.class.isAssignableFrom(cls)) {
            objectSerializerCreateJavaBeanSerializer = ListSerializer.instance;
            put((Type) cls, objectSerializerCreateJavaBeanSerializer);
        } else if (Collection.class.isAssignableFrom(cls)) {
            objectSerializerCreateJavaBeanSerializer = CollectionCodec.instance;
            put((Type) cls, objectSerializerCreateJavaBeanSerializer);
        } else if (Date.class.isAssignableFrom(cls)) {
            objectSerializerCreateJavaBeanSerializer = DateCodec.instance;
            put((Type) cls, objectSerializerCreateJavaBeanSerializer);
        } else if (JSONAware.class.isAssignableFrom(cls)) {
            objectSerializerCreateJavaBeanSerializer = JSONAwareSerializer.instance;
            put((Type) cls, objectSerializerCreateJavaBeanSerializer);
        } else if (JSONSerializable.class.isAssignableFrom(cls)) {
            objectSerializerCreateJavaBeanSerializer = JSONSerializableSerializer.instance;
            put((Type) cls, objectSerializerCreateJavaBeanSerializer);
        } else if (JSONStreamAware.class.isAssignableFrom(cls)) {
            objectSerializerCreateJavaBeanSerializer = MiscCodec.instance;
            put((Type) cls, objectSerializerCreateJavaBeanSerializer);
        } else {
            Class<?> cls2 = null;
            enumValueField = null;
            enumValueField = null;
            Member enumValueField = null;
            if (cls.isEnum()) {
                Class cls3 = (Class) JSON.getMixInAnnotations(cls);
                if (cls3 != null) {
                    jSONType = (JSONType) TypeUtils.getAnnotation((Class<?>) cls3, JSONType.class);
                } else {
                    jSONType = (JSONType) TypeUtils.getAnnotation(cls, JSONType.class);
                }
                if (jSONType == null || !jSONType.serializeEnumAsJavaBean()) {
                    if (cls3 != null) {
                        Member enumValueField2 = getEnumValueField(cls3);
                        if (enumValueField2 != null) {
                            try {
                                if (enumValueField2 instanceof Method) {
                                    Method method = (Method) enumValueField2;
                                    enumValueField = cls.getMethod(method.getName(), method.getParameterTypes());
                                }
                            } catch (Exception unused3) {
                            }
                        }
                    } else {
                        enumValueField = getEnumValueField(cls);
                    }
                    if (enumValueField != null) {
                        objectSerializerCreateJavaBeanSerializer = new EnumSerializer(enumValueField);
                        put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                    } else {
                        objectSerializerCreateJavaBeanSerializer = getEnumSerializer();
                        put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                    }
                } else {
                    objectSerializerCreateJavaBeanSerializer = createJavaBeanSerializer(cls);
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                }
            } else {
                Class<? super Object> superclass = cls.getSuperclass();
                if (superclass != null && superclass.isEnum()) {
                    JSONType jSONType2 = (JSONType) TypeUtils.getAnnotation(superclass, JSONType.class);
                    if (jSONType2 != null && jSONType2.serializeEnumAsJavaBean()) {
                        objectSerializerCreateJavaBeanSerializer = createJavaBeanSerializer(cls);
                        put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                    } else {
                        objectSerializerCreateJavaBeanSerializer = getEnumSerializer();
                        put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                    }
                } else if (cls.isArray()) {
                    Class<?> componentType = cls.getComponentType();
                    ObjectSerializer arraySerializer = new ArraySerializer(componentType, getObjectWriter(componentType));
                    put((Type) cls, arraySerializer);
                    objectSerializerCreateJavaBeanSerializer = arraySerializer;
                } else if (Throwable.class.isAssignableFrom(cls)) {
                    SerializeBeanInfo serializeBeanInfoBuildBeanInfo = TypeUtils.buildBeanInfo(cls, null, this.propertyNamingStrategy);
                    serializeBeanInfoBuildBeanInfo.features |= SerializerFeature.WriteClassName.mask;
                    ObjectSerializer javaBeanSerializer = new JavaBeanSerializer(serializeBeanInfoBuildBeanInfo);
                    put((Type) cls, javaBeanSerializer);
                    objectSerializerCreateJavaBeanSerializer = javaBeanSerializer;
                } else if (TimeZone.class.isAssignableFrom(cls) || Map.Entry.class.isAssignableFrom(cls)) {
                    objectSerializerCreateJavaBeanSerializer = MiscCodec.instance;
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                } else if (Appendable.class.isAssignableFrom(cls)) {
                    objectSerializerCreateJavaBeanSerializer = AppendableSerializer.instance;
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                } else if (Charset.class.isAssignableFrom(cls)) {
                    objectSerializerCreateJavaBeanSerializer = ToStringSerializer.instance;
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                } else if (Enumeration.class.isAssignableFrom(cls)) {
                    objectSerializerCreateJavaBeanSerializer = EnumerationSerializer.instance;
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                } else if (Calendar.class.isAssignableFrom(cls) || XMLGregorianCalendar.class.isAssignableFrom(cls)) {
                    objectSerializerCreateJavaBeanSerializer = CalendarCodec.instance;
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                } else if (TypeUtils.isClob(cls)) {
                    objectSerializerCreateJavaBeanSerializer = ClobSerializer.instance;
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                } else if (TypeUtils.isPath(cls)) {
                    objectSerializerCreateJavaBeanSerializer = ToStringSerializer.instance;
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                } else if (Iterator.class.isAssignableFrom(cls)) {
                    objectSerializerCreateJavaBeanSerializer = MiscCodec.instance;
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                } else if (Node.class.isAssignableFrom(cls)) {
                    objectSerializerCreateJavaBeanSerializer = MiscCodec.instance;
                    put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                } else {
                    int i = 0;
                    if (name.startsWith("java.awt.") && AwtCodec.support(cls) && !awtError) {
                        try {
                            for (String str : new String[]{"java.awt.Color", "java.awt.Font", "java.awt.Point", "java.awt.Rectangle"}) {
                                if (str.equals(name)) {
                                    Type cls4 = Class.forName(str);
                                    objectSerializerCreateJavaBeanSerializer = AwtCodec.instance;
                                    put(cls4, objectSerializerCreateJavaBeanSerializer);
                                    return objectSerializerCreateJavaBeanSerializer;
                                }
                            }
                        } catch (Throwable unused4) {
                            awtError = true;
                        }
                    }
                    if (!jdk8Error && (name.startsWith("java.time.") || name.startsWith("java.util.Optional") || name.equals("java.util.concurrent.atomic.LongAdder") || name.equals("java.util.concurrent.atomic.DoubleAdder"))) {
                        try {
                            for (String str2 : new String[]{"java.time.LocalDateTime", "java.time.LocalDate", "java.time.LocalTime", "java.time.ZonedDateTime", "java.time.OffsetDateTime", "java.time.OffsetTime", "java.time.ZoneOffset", "java.time.ZoneRegion", "java.time.Period", "java.time.Duration", "java.time.Instant"}) {
                                if (str2.equals(name)) {
                                    Type cls5 = Class.forName(str2);
                                    ObjectSerializer objectSerializer2 = Jdk8DateCodec.instance;
                                    put(cls5, objectSerializer2);
                                    return objectSerializer2;
                                }
                            }
                            for (String str3 : new String[]{"java.util.Optional", "java.util.OptionalDouble", "java.util.OptionalInt", "java.util.OptionalLong"}) {
                                if (str3.equals(name)) {
                                    Type cls6 = Class.forName(str3);
                                    ObjectSerializer objectSerializer3 = OptionalCodec.instance;
                                    put(cls6, objectSerializer3);
                                    return objectSerializer3;
                                }
                            }
                            for (String str4 : new String[]{"java.util.concurrent.atomic.LongAdder", "java.util.concurrent.atomic.DoubleAdder"}) {
                                if (str4.equals(name)) {
                                    Type cls7 = Class.forName(str4);
                                    ObjectSerializer objectSerializer4 = AdderSerializer.instance;
                                    put(cls7, objectSerializer4);
                                    return objectSerializer4;
                                }
                            }
                        } catch (Throwable unused5) {
                            jdk8Error = true;
                        }
                    }
                    if (!oracleJdbcError && name.startsWith("oracle.sql.")) {
                        try {
                            for (String str5 : new String[]{"oracle.sql.DATE", "oracle.sql.TIMESTAMP"}) {
                                if (str5.equals(name)) {
                                    Type cls8 = Class.forName(str5);
                                    objectSerializerCreateJavaBeanSerializer = DateCodec.instance;
                                    put(cls8, objectSerializerCreateJavaBeanSerializer);
                                    return objectSerializerCreateJavaBeanSerializer;
                                }
                            }
                        } catch (Throwable unused6) {
                            oracleJdbcError = true;
                        }
                    }
                    if (!springfoxError && name.equals("springfox.documentation.spring.web.json.Json")) {
                        try {
                            Type cls9 = Class.forName("springfox.documentation.spring.web.json.Json");
                            objectSerializerCreateJavaBeanSerializer = SwaggerJsonSerializer.instance;
                            put(cls9, objectSerializerCreateJavaBeanSerializer);
                            return objectSerializerCreateJavaBeanSerializer;
                        } catch (ClassNotFoundException unused7) {
                            springfoxError = true;
                        }
                    }
                    if (!guavaError && name.startsWith("com.google.common.collect.")) {
                        try {
                            for (String str6 : new String[]{"com.google.common.collect.HashMultimap", "com.google.common.collect.LinkedListMultimap", "com.google.common.collect.LinkedHashMultimap", "com.google.common.collect.ArrayListMultimap", "com.google.common.collect.TreeMultimap"}) {
                                if (str6.equals(name)) {
                                    Type cls10 = Class.forName(str6);
                                    objectSerializerCreateJavaBeanSerializer = GuavaCodec.instance;
                                    put(cls10, objectSerializerCreateJavaBeanSerializer);
                                    return objectSerializerCreateJavaBeanSerializer;
                                }
                            }
                        } catch (ClassNotFoundException unused8) {
                            guavaError = true;
                        }
                    }
                    if (name.equals("net.sf.json.JSONNull")) {
                        ObjectSerializer objectSerializer5 = MiscCodec.instance;
                        put((Type) cls, objectSerializer5);
                        return objectSerializer5;
                    }
                    if (name.equals("org.json.JSONObject")) {
                        ObjectSerializer objectSerializer6 = JSONObjectCodec.instance;
                        put((Type) cls, objectSerializer6);
                        return objectSerializer6;
                    }
                    if (!jodaError && name.startsWith("org.joda.")) {
                        try {
                            for (String str7 : new String[]{"org.joda.time.LocalDate", "org.joda.time.LocalDateTime", "org.joda.time.LocalTime", "org.joda.time.Instant", "org.joda.time.DateTime", "org.joda.time.Period", "org.joda.time.Duration", "org.joda.time.DateTimeZone", "org.joda.time.UTCDateTimeZone", "org.joda.time.tz.CachedDateTimeZone", "org.joda.time.tz.FixedDateTimeZone"}) {
                                if (str7.equals(name)) {
                                    Type cls11 = Class.forName(str7);
                                    objectSerializerCreateJavaBeanSerializer = JodaCodec.instance;
                                    put(cls11, objectSerializerCreateJavaBeanSerializer);
                                    return objectSerializerCreateJavaBeanSerializer;
                                }
                            }
                        } catch (ClassNotFoundException unused9) {
                            jodaError = true;
                        }
                    }
                    if ("java.nio.HeapByteBuffer".equals(name)) {
                        ObjectSerializer objectSerializer7 = ByteBufferCodec.instance;
                        put((Type) cls, objectSerializer7);
                        return objectSerializer7;
                    }
                    if ("org.javamoney.moneta.Money".equals(name)) {
                        ObjectSerializer objectSerializer8 = MonetaCodec.instance;
                        put((Type) cls, objectSerializer8);
                        return objectSerializer8;
                    }
                    if ("com.google.protobuf.Descriptors$FieldDescriptor".equals(name)) {
                        ObjectSerializer objectSerializer9 = ToStringSerializer.instance;
                        put((Type) cls, objectSerializer9);
                        return objectSerializer9;
                    }
                    Class<?>[] interfaces = cls.getInterfaces();
                    if (interfaces.length == 1 && interfaces[0].isAnnotation()) {
                        put((Type) cls, AnnotationSerializer.instance);
                        return AnnotationSerializer.instance;
                    }
                    if (TypeUtils.isProxy(cls)) {
                        ObjectSerializer objectWriter = getObjectWriter(cls.getSuperclass());
                        put((Type) cls, objectWriter);
                        return objectWriter;
                    }
                    if (Proxy.isProxyClass(cls)) {
                        if (interfaces.length == 2) {
                            cls2 = interfaces[1];
                        } else {
                            int length = interfaces.length;
                            Class<?> cls12 = null;
                            while (true) {
                                if (i >= length) {
                                    cls2 = cls12;
                                    break;
                                }
                                Class<?> cls13 = interfaces[i];
                                if (!cls13.getName().startsWith("org.springframework.aop.")) {
                                    if (cls12 != null) {
                                        break;
                                    }
                                    cls12 = cls13;
                                }
                                i++;
                            }
                        }
                        if (cls2 != null) {
                            ObjectSerializer objectWriter2 = getObjectWriter(cls2);
                            put((Type) cls, objectWriter2);
                            return objectWriter2;
                        }
                    }
                    if (z) {
                        objectSerializerCreateJavaBeanSerializer = createJavaBeanSerializer(cls);
                        put((Type) cls, objectSerializerCreateJavaBeanSerializer);
                    }
                }
            }
        }
        return objectSerializerCreateJavaBeanSerializer == null ? get(cls) : objectSerializerCreateJavaBeanSerializer;
    }

    private static Member getEnumValueField(Class cls) {
        Field field = null;
        for (Method method : cls.getMethods()) {
            if (method.getReturnType() != Void.class && ((JSONField) method.getAnnotation(JSONField.class)) != null) {
                if (field != null) {
                    return null;
                }
                field = method;
            }
        }
        for (Field field2 : cls.getFields()) {
            if (((JSONField) field2.getAnnotation(JSONField.class)) != null) {
                if (field != null) {
                    return null;
                }
                field = field2;
            }
        }
        return field;
    }

    protected ObjectSerializer getEnumSerializer() {
        return EnumSerializer.instance;
    }

    public final ObjectSerializer get(Type type) {
        Type mixInAnnotations = JSON.getMixInAnnotations(type);
        if (mixInAnnotations == null) {
            return this.serializers.get(type);
        }
        IdentityHashMap<Type, ObjectSerializer> identityHashMap = this.mixInSerializers.get(type);
        if (identityHashMap == null) {
            return null;
        }
        return identityHashMap.get(mixInAnnotations);
    }

    public boolean put(Object obj, Object obj2) {
        return put((Type) obj, (ObjectSerializer) obj2);
    }

    public boolean put(Type type, ObjectSerializer objectSerializer) {
        Type mixInAnnotations = JSON.getMixInAnnotations(type);
        if (mixInAnnotations != null) {
            IdentityHashMap<Type, ObjectSerializer> identityHashMap = this.mixInSerializers.get(type);
            if (identityHashMap == null) {
                identityHashMap = new IdentityHashMap<>(4);
                this.mixInSerializers.put(type, identityHashMap);
            }
            return identityHashMap.put(mixInAnnotations, objectSerializer);
        }
        return this.serializers.put(type, objectSerializer);
    }

    public void configEnumAsJavaBean(Class<? extends Enum>... clsArr) {
        for (Class<? extends Enum> cls : clsArr) {
            put((Type) cls, createJavaBeanSerializer(cls));
        }
    }

    public void setPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
        this.propertyNamingStrategy = propertyNamingStrategy;
    }

    public void clearSerializers() {
        this.serializers.clear();
        initSerializers();
    }

    public void register(Module module) {
        this.modules.add(module);
    }
}
