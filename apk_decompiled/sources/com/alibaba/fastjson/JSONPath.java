package com.alibaba.fastjson;

import anetwork.channel.util.RequestConstant;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.FieldSerializer;
import com.alibaba.fastjson.serializer.JavaBeanSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.util.IOUtils;
import com.alibaba.fastjson.util.TypeUtils;
import com.aliyun.alink.linksdk.tmp.utils.TmpConstant;
import com.xiaomi.mipush.sdk.Constants;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

/* JADX INFO: loaded from: classes.dex */
public class JSONPath implements JSONAware {
    static final long LENGTH = -1580386065683472715L;
    static final long SIZE = 5614464919154503228L;
    private static ConcurrentMap<String, JSONPath> pathCache = new ConcurrentHashMap(128, 0.75f, 1);
    private boolean hasRefSegment;
    private boolean ignoreNullValue;
    private ParserConfig parserConfig;
    private final String path;
    private Segment[] segments;
    private SerializeConfig serializeConfig;

    interface Filter {
        boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3);
    }

    enum Operator {
        EQ,
        NE,
        GT,
        GE,
        LT,
        LE,
        LIKE,
        NOT_LIKE,
        RLIKE,
        NOT_RLIKE,
        IN,
        NOT_IN,
        BETWEEN,
        NOT_BETWEEN,
        And,
        Or,
        REG_MATCH
    }

    interface Segment {
        Object eval(JSONPath jSONPath, Object obj, Object obj2);

        void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context);
    }

    public JSONPath(String str) {
        this(str, SerializeConfig.getGlobalInstance(), ParserConfig.getGlobalInstance(), true);
    }

    public JSONPath(String str, boolean z) {
        this(str, SerializeConfig.getGlobalInstance(), ParserConfig.getGlobalInstance(), z);
    }

    public JSONPath(String str, SerializeConfig serializeConfig, ParserConfig parserConfig, boolean z) {
        if (str == null || str.length() == 0) {
            throw new JSONPathException("json-path can not be null or empty");
        }
        this.path = str;
        this.serializeConfig = serializeConfig;
        this.parserConfig = parserConfig;
        this.ignoreNullValue = z;
    }

    protected void init() {
        if (this.segments != null) {
            return;
        }
        if (WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD.equals(this.path)) {
            this.segments = new Segment[]{WildCardSegment.instance};
            return;
        }
        JSONPathParser jSONPathParser = new JSONPathParser(this.path);
        this.segments = jSONPathParser.explain();
        this.hasRefSegment = jSONPathParser.hasRefSegment;
    }

    public boolean isRef() {
        try {
            init();
            for (int i = 0; i < this.segments.length; i++) {
                Class<?> cls = this.segments[i].getClass();
                if (cls != ArrayAccessSegment.class && cls != PropertySegment.class) {
                    return false;
                }
            }
            return true;
        } catch (JSONPathException unused) {
            return false;
        }
    }

    public Object eval(Object obj) {
        if (obj == null) {
            return null;
        }
        init();
        int i = 0;
        Object objEval = obj;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i >= segmentArr.length) {
                return objEval;
            }
            objEval = segmentArr[i].eval(this, obj, objEval);
            i++;
        }
    }

    public <T> T eval(Object obj, Type type, ParserConfig parserConfig) {
        return (T) TypeUtils.cast(eval(obj), type, parserConfig);
    }

    public <T> T eval(Object obj, Type type) {
        return (T) eval(obj, type, ParserConfig.getGlobalInstance());
    }

    public Object extract(DefaultJSONParser defaultJSONParser) {
        boolean z;
        if (defaultJSONParser == null) {
            return null;
        }
        init();
        if (this.hasRefSegment) {
            return eval(defaultJSONParser.parse());
        }
        Segment[] segmentArr = this.segments;
        if (segmentArr.length == 0) {
            return defaultJSONParser.parse();
        }
        Segment segment = segmentArr[segmentArr.length - 1];
        if ((segment instanceof TypeSegment) || (segment instanceof FloorSegment) || (segment instanceof MultiIndexSegment)) {
            return eval(defaultJSONParser.parse());
        }
        Context context = null;
        int i = 0;
        while (true) {
            Segment[] segmentArr2 = this.segments;
            if (i < segmentArr2.length) {
                Segment segment2 = segmentArr2[i];
                boolean z2 = i == segmentArr2.length - 1;
                if (context != null && context.object != null) {
                    context.object = segment2.eval(this, null, context.object);
                } else {
                    if (z2) {
                        z = true;
                    } else {
                        Segment segment3 = this.segments[i + 1];
                        if ((segment2 instanceof PropertySegment) && ((PropertySegment) segment2).deep && ((segment3 instanceof ArrayAccessSegment) || (segment3 instanceof MultiIndexSegment) || (segment3 instanceof MultiPropertySegment) || (segment3 instanceof SizeSegment) || (segment3 instanceof PropertySegment) || (segment3 instanceof FilterSegment))) {
                            z = true;
                        } else {
                            z = ((segment3 instanceof ArrayAccessSegment) && ((ArrayAccessSegment) segment3).index < 0) || (segment3 instanceof FilterSegment) || (segment2 instanceof WildCardSegment) || (segment2 instanceof MultiIndexSegment);
                        }
                    }
                    Context context2 = new Context(context, z);
                    segment2.extract(this, defaultJSONParser, context2);
                    context = context2;
                }
                i++;
            } else {
                return context.object;
            }
        }
    }

    private static class Context {
        final boolean eval;
        Object object;
        final Context parent;

        public Context(Context context, boolean z) {
            this.parent = context;
            this.eval = z;
        }
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        init();
        Object obj2 = obj;
        int i = 0;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i >= segmentArr.length) {
                return true;
            }
            Object objEval = segmentArr[i].eval(this, obj, obj2);
            if (objEval == null) {
                return false;
            }
            if (objEval == Collections.EMPTY_LIST && (obj2 instanceof List)) {
                return ((List) obj2).contains(objEval);
            }
            i++;
            obj2 = objEval;
        }
    }

    public boolean containsValue(Object obj, Object obj2) {
        Object objEval = eval(obj);
        if (objEval == obj2) {
            return true;
        }
        if (objEval == null) {
            return false;
        }
        if (objEval instanceof Iterable) {
            Iterator it = ((Iterable) objEval).iterator();
            while (it.hasNext()) {
                if (eq(it.next(), obj2)) {
                    return true;
                }
            }
            return false;
        }
        return eq(objEval, obj2);
    }

    public int size(Object obj) {
        if (obj == null) {
            return -1;
        }
        init();
        int i = 0;
        Object objEval = obj;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i < segmentArr.length) {
                objEval = segmentArr[i].eval(this, obj, objEval);
                i++;
            } else {
                return evalSize(objEval);
            }
        }
    }

    public Set<?> keySet(Object obj) {
        if (obj == null) {
            return null;
        }
        init();
        int i = 0;
        Object objEval = obj;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i < segmentArr.length) {
                objEval = segmentArr[i].eval(this, obj, objEval);
                i++;
            } else {
                return evalKeySet(objEval);
            }
        }
    }

    public void patchAdd(Object obj, Object obj2, boolean z) {
        if (obj == null) {
            return;
        }
        init();
        Object obj3 = obj;
        Object obj4 = null;
        int i = 0;
        while (true) {
            Segment[] segmentArr = this.segments;
            if (i >= segmentArr.length) {
                break;
            }
            Segment segment = segmentArr[i];
            Object objEval = segment.eval(this, obj, obj3);
            if (objEval == null && i != this.segments.length - 1 && (segment instanceof PropertySegment)) {
                objEval = new JSONObject();
                ((PropertySegment) segment).setValue(this, obj3, objEval);
            }
            i++;
            obj4 = obj3;
            obj3 = objEval;
        }
        if (!z && (obj3 instanceof Collection)) {
            ((Collection) obj3).add(obj2);
            return;
        }
        if (obj3 != null && !z) {
            Class<?> cls = obj3.getClass();
            if (cls.isArray()) {
                int length = Array.getLength(obj3);
                Object objNewInstance = Array.newInstance(cls.getComponentType(), length + 1);
                System.arraycopy(obj3, 0, objNewInstance, 0, length);
                Array.set(objNewInstance, length, obj2);
                obj2 = objNewInstance;
            } else if (!Map.class.isAssignableFrom(cls)) {
                throw new JSONException("unsupported array put operation. " + cls);
            }
        }
        Segment segment2 = this.segments[r7.length - 1];
        if (segment2 instanceof PropertySegment) {
            ((PropertySegment) segment2).setValue(this, obj4, obj2);
        } else {
            if (segment2 instanceof ArrayAccessSegment) {
                ((ArrayAccessSegment) segment2).setValue(this, obj4, obj2);
                return;
            }
            throw new UnsupportedOperationException();
        }
    }

    public void arrayAdd(Object obj, Object... objArr) {
        if (objArr == null || objArr.length == 0 || obj == null) {
            return;
        }
        init();
        int i = 0;
        Object objEval = obj;
        Object obj2 = null;
        int i2 = 0;
        while (true) {
            if (i2 >= this.segments.length) {
                break;
            }
            if (i2 == r4.length - 1) {
                obj2 = objEval;
            }
            objEval = this.segments[i2].eval(this, obj, objEval);
            i2++;
        }
        if (objEval == null) {
            throw new JSONPathException("value not found in path " + this.path);
        }
        if (objEval instanceof Collection) {
            Collection collection = (Collection) objEval;
            int length = objArr.length;
            while (i < length) {
                collection.add(objArr[i]);
                i++;
            }
            return;
        }
        Class<?> cls = objEval.getClass();
        if (cls.isArray()) {
            int length2 = Array.getLength(objEval);
            Object objNewInstance = Array.newInstance(cls.getComponentType(), objArr.length + length2);
            System.arraycopy(objEval, 0, objNewInstance, 0, length2);
            while (i < objArr.length) {
                Array.set(objNewInstance, length2 + i, objArr[i]);
                i++;
            }
            Segment segment = this.segments[r8.length - 1];
            if (segment instanceof PropertySegment) {
                ((PropertySegment) segment).setValue(this, obj2, objNewInstance);
                return;
            } else {
                if (segment instanceof ArrayAccessSegment) {
                    ((ArrayAccessSegment) segment).setValue(this, obj2, objNewInstance);
                    return;
                }
                throw new UnsupportedOperationException();
            }
        }
        throw new JSONException("unsupported array put operation. " + cls);
    }

    /* JADX WARN: Code restructure failed: missing block: B:46:0x008f, code lost:
    
        if (r1 != null) goto L48;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0091, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x0094, code lost:
    
        if ((r2 instanceof com.alibaba.fastjson.JSONPath.PropertySegment) == false) goto L67;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x0096, code lost:
    
        r2 = (com.alibaba.fastjson.JSONPath.PropertySegment) r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x009a, code lost:
    
        if ((r1 instanceof java.util.Collection) == false) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x009c, code lost:
    
        r12 = r11.segments;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x009f, code lost:
    
        if (r12.length <= 1) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x00a1, code lost:
    
        r12 = r12[r12.length - 2];
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x00a8, code lost:
    
        if ((r12 instanceof com.alibaba.fastjson.JSONPath.RangeSegment) != false) goto L58;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x00ac, code lost:
    
        if ((r12 instanceof com.alibaba.fastjson.JSONPath.MultiIndexSegment) == false) goto L65;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x00ae, code lost:
    
        r12 = ((java.util.Collection) r1).iterator();
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x00b8, code lost:
    
        if (r12.hasNext() == false) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x00c2, code lost:
    
        if (r2.remove(r11, r12.next()) == false) goto L96;
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x00c4, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x00c6, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x00cb, code lost:
    
        return r2.remove(r11, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:0x00ce, code lost:
    
        if ((r2 instanceof com.alibaba.fastjson.JSONPath.ArrayAccessSegment) == false) goto L71;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:0x00d6, code lost:
    
        return ((com.alibaba.fastjson.JSONPath.ArrayAccessSegment) r2).remove(r11, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:72:0x00d9, code lost:
    
        if ((r2 instanceof com.alibaba.fastjson.JSONPath.FilterSegment) == false) goto L75;
     */
    /* JADX WARN: Code restructure failed: missing block: B:74:0x00e1, code lost:
    
        return ((com.alibaba.fastjson.JSONPath.FilterSegment) r2).remove(r11, r12, r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:76:0x00e7, code lost:
    
        throw new java.lang.UnsupportedOperationException();
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean remove(java.lang.Object r12) {
        /*
            Method dump skipped, instruction units count: 232
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.remove(java.lang.Object):boolean");
    }

    public boolean set(Object obj, Object obj2) {
        return set(obj, obj2, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x004f  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x005b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public boolean set(java.lang.Object r9, java.lang.Object r10, boolean r11) {
        /*
            r8 = this;
            r11 = 0
            if (r9 != 0) goto L4
            return r11
        L4:
            r8.init()
            r0 = 0
            r2 = r9
            r1 = r11
            r3 = r0
        Lb:
            com.alibaba.fastjson.JSONPath$Segment[] r4 = r8.segments
            int r5 = r4.length
            r6 = 1
            if (r1 >= r5) goto L86
            r3 = r4[r1]
            java.lang.Object r4 = r3.eval(r8, r9, r2)
            if (r4 != 0) goto L81
            com.alibaba.fastjson.JSONPath$Segment[] r4 = r8.segments
            int r5 = r4.length
            int r5 = r5 - r6
            if (r1 >= r5) goto L24
            int r5 = r1 + 1
            r4 = r4[r5]
            goto L25
        L24:
            r4 = r0
        L25:
            boolean r5 = r4 instanceof com.alibaba.fastjson.JSONPath.PropertySegment
            if (r5 == 0) goto L61
            boolean r4 = r3 instanceof com.alibaba.fastjson.JSONPath.PropertySegment
            if (r4 == 0) goto L4b
            r4 = r3
            com.alibaba.fastjson.JSONPath$PropertySegment r4 = (com.alibaba.fastjson.JSONPath.PropertySegment) r4
            java.lang.String r4 = com.alibaba.fastjson.JSONPath.PropertySegment.access$400(r4)
            java.lang.Class r5 = r2.getClass()
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r5 = r8.getJavaBeanDeserializer(r5)
            if (r5 == 0) goto L4b
            com.alibaba.fastjson.parser.deserializer.FieldDeserializer r4 = r5.getFieldDeserializer(r4)
            com.alibaba.fastjson.util.FieldInfo r4 = r4.fieldInfo
            java.lang.Class<?> r4 = r4.fieldClass
            com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer r5 = r8.getJavaBeanDeserializer(r4)
            goto L4d
        L4b:
            r4 = r0
            r5 = r4
        L4d:
            if (r5 == 0) goto L5b
            com.alibaba.fastjson.util.JavaBeanInfo r7 = r5.beanInfo
            java.lang.reflect.Constructor<?> r7 = r7.defaultConstructor
            if (r7 == 0) goto L5a
            java.lang.Object r4 = r5.createInstance(r0, r4)
            goto L6c
        L5a:
            return r11
        L5b:
            com.alibaba.fastjson.JSONObject r4 = new com.alibaba.fastjson.JSONObject
            r4.<init>()
            goto L6c
        L61:
            boolean r4 = r4 instanceof com.alibaba.fastjson.JSONPath.ArrayAccessSegment
            if (r4 == 0) goto L6b
            com.alibaba.fastjson.JSONArray r4 = new com.alibaba.fastjson.JSONArray
            r4.<init>()
            goto L6c
        L6b:
            r4 = r0
        L6c:
            if (r4 == 0) goto L87
            boolean r5 = r3 instanceof com.alibaba.fastjson.JSONPath.PropertySegment
            if (r5 == 0) goto L78
            com.alibaba.fastjson.JSONPath$PropertySegment r3 = (com.alibaba.fastjson.JSONPath.PropertySegment) r3
            r3.setValue(r8, r2, r4)
            goto L81
        L78:
            boolean r5 = r3 instanceof com.alibaba.fastjson.JSONPath.ArrayAccessSegment
            if (r5 == 0) goto L87
            com.alibaba.fastjson.JSONPath$ArrayAccessSegment r3 = (com.alibaba.fastjson.JSONPath.ArrayAccessSegment) r3
            r3.setValue(r8, r2, r4)
        L81:
            int r1 = r1 + 1
            r3 = r2
            r2 = r4
            goto Lb
        L86:
            r2 = r3
        L87:
            if (r2 != 0) goto L8a
            return r11
        L8a:
            com.alibaba.fastjson.JSONPath$Segment[] r9 = r8.segments
            int r11 = r9.length
            int r11 = r11 - r6
            r9 = r9[r11]
            boolean r11 = r9 instanceof com.alibaba.fastjson.JSONPath.PropertySegment
            if (r11 == 0) goto L9a
            com.alibaba.fastjson.JSONPath$PropertySegment r9 = (com.alibaba.fastjson.JSONPath.PropertySegment) r9
            r9.setValue(r8, r2, r10)
            return r6
        L9a:
            boolean r11 = r9 instanceof com.alibaba.fastjson.JSONPath.ArrayAccessSegment
            if (r11 == 0) goto La5
            com.alibaba.fastjson.JSONPath$ArrayAccessSegment r9 = (com.alibaba.fastjson.JSONPath.ArrayAccessSegment) r9
            boolean r9 = r9.setValue(r8, r2, r10)
            return r9
        La5:
            java.lang.UnsupportedOperationException r9 = new java.lang.UnsupportedOperationException
            r9.<init>()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.set(java.lang.Object, java.lang.Object, boolean):boolean");
    }

    public static Object eval(Object obj, String str) {
        return compile(str).eval(obj);
    }

    public static Object eval(Object obj, String str, boolean z) {
        return compile(str, z).eval(obj);
    }

    public static int size(Object obj, String str) {
        JSONPath jSONPathCompile = compile(str);
        return jSONPathCompile.evalSize(jSONPathCompile.eval(obj));
    }

    public static Set<?> keySet(Object obj, String str) {
        JSONPath jSONPathCompile = compile(str);
        return jSONPathCompile.evalKeySet(jSONPathCompile.eval(obj));
    }

    public static boolean contains(Object obj, String str) {
        if (obj == null) {
            return false;
        }
        return compile(str).contains(obj);
    }

    public static boolean containsValue(Object obj, String str, Object obj2) {
        return compile(str).containsValue(obj, obj2);
    }

    public static void arrayAdd(Object obj, String str, Object... objArr) {
        compile(str).arrayAdd(obj, objArr);
    }

    public static boolean set(Object obj, String str, Object obj2) {
        return compile(str).set(obj, obj2);
    }

    public static boolean remove(Object obj, String str) {
        return compile(str).remove(obj);
    }

    public static JSONPath compile(String str) {
        if (str == null) {
            throw new JSONPathException("jsonpath can not be null");
        }
        JSONPath jSONPath = pathCache.get(str);
        if (jSONPath != null) {
            return jSONPath;
        }
        JSONPath jSONPath2 = new JSONPath(str);
        if (pathCache.size() >= 1024) {
            return jSONPath2;
        }
        pathCache.putIfAbsent(str, jSONPath2);
        return pathCache.get(str);
    }

    public static JSONPath compile(String str, boolean z) {
        if (str == null) {
            throw new JSONPathException("jsonpath can not be null");
        }
        JSONPath jSONPath = pathCache.get(str);
        if (jSONPath != null) {
            return jSONPath;
        }
        JSONPath jSONPath2 = new JSONPath(str, z);
        if (pathCache.size() >= 1024) {
            return jSONPath2;
        }
        pathCache.putIfAbsent(str, jSONPath2);
        return pathCache.get(str);
    }

    public static Object read(String str, String str2) {
        return compile(str2).eval(JSON.parse(str));
    }

    public static <T> T read(String str, String str2, Type type, ParserConfig parserConfig) {
        return (T) compile(str2).eval(JSON.parse(str), type, parserConfig);
    }

    public static <T> T read(String str, String str2, Type type) {
        return (T) read(str, str2, type, null);
    }

    public static Object extract(String str, String str2, ParserConfig parserConfig, int i, Feature... featureArr) {
        DefaultJSONParser defaultJSONParser = new DefaultJSONParser(str, parserConfig, i | Feature.OrderedField.mask);
        Object objExtract = compile(str2).extract(defaultJSONParser);
        defaultJSONParser.lexer.close();
        return objExtract;
    }

    public static Object extract(String str, String str2) {
        return extract(str, str2, ParserConfig.global, JSON.DEFAULT_PARSER_FEATURE, new Feature[0]);
    }

    public static Map<String, Object> paths(Object obj) {
        return paths(obj, SerializeConfig.globalInstance);
    }

    public static Map<String, Object> paths(Object obj, SerializeConfig serializeConfig) {
        IdentityHashMap identityHashMap = new IdentityHashMap();
        HashMap map = new HashMap();
        paths(identityHashMap, map, "/", obj, serializeConfig);
        return map;
    }

    private static void paths(Map<Object, String> map, Map<String, Object> map2, String str, Object obj, SerializeConfig serializeConfig) {
        StringBuilder sb;
        StringBuilder sb2;
        StringBuilder sb3;
        StringBuilder sb4;
        if (obj == null) {
            return;
        }
        int i = 0;
        if (map.put(obj, str) != null) {
            Class<?> cls = obj.getClass();
            if (!(cls == String.class || cls == Boolean.class || cls == Character.class || cls == UUID.class || cls.isEnum() || (obj instanceof Number) || (obj instanceof Date))) {
                return;
            }
        }
        map2.put(str, obj);
        if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object key = entry.getKey();
                if (key instanceof String) {
                    if (str.equals("/")) {
                        sb4 = new StringBuilder();
                    } else {
                        sb4 = new StringBuilder();
                        sb4.append(str);
                    }
                    sb4.append("/");
                    sb4.append(key);
                    paths(map, map2, sb4.toString(), entry.getValue(), serializeConfig);
                }
            }
            return;
        }
        if (obj instanceof Collection) {
            for (Object obj2 : (Collection) obj) {
                if (str.equals("/")) {
                    sb3 = new StringBuilder();
                } else {
                    sb3 = new StringBuilder();
                    sb3.append(str);
                }
                sb3.append("/");
                sb3.append(i);
                paths(map, map2, sb3.toString(), obj2, serializeConfig);
                i++;
            }
            return;
        }
        Class<?> cls2 = obj.getClass();
        if (cls2.isArray()) {
            int length = Array.getLength(obj);
            while (i < length) {
                Object obj3 = Array.get(obj, i);
                if (str.equals("/")) {
                    sb2 = new StringBuilder();
                } else {
                    sb2 = new StringBuilder();
                    sb2.append(str);
                }
                sb2.append("/");
                sb2.append(i);
                paths(map, map2, sb2.toString(), obj3, serializeConfig);
                i++;
            }
            return;
        }
        if (ParserConfig.isPrimitive2(cls2) || cls2.isEnum()) {
            return;
        }
        ObjectSerializer objectWriter = serializeConfig.getObjectWriter(cls2);
        if (objectWriter instanceof JavaBeanSerializer) {
            try {
                for (Map.Entry<String, Object> entry2 : ((JavaBeanSerializer) objectWriter).getFieldValuesMap(obj).entrySet()) {
                    String key2 = entry2.getKey();
                    if (key2 instanceof String) {
                        if (str.equals("/")) {
                            sb = new StringBuilder();
                            sb.append("/");
                            sb.append(key2);
                        } else {
                            sb = new StringBuilder();
                            sb.append(str);
                            sb.append("/");
                            sb.append(key2);
                        }
                        paths(map, map2, sb.toString(), entry2.getValue(), serializeConfig);
                    }
                }
            } catch (Exception e) {
                throw new JSONException("toJSON error", e);
            }
        }
    }

    public String getPath() {
        return this.path;
    }

    static class JSONPathParser {
        private char ch;
        private boolean hasRefSegment;
        private int level;
        private final String path;
        private int pos;
        private static final String strArrayRegex = "'\\s*,\\s*'";
        private static final Pattern strArrayPatternx = Pattern.compile(strArrayRegex);

        static boolean isDigitFirst(char c2) {
            return c2 == '-' || c2 == '+' || (c2 >= '0' && c2 <= '9');
        }

        public JSONPathParser(String str) {
            this.path = str;
            next();
        }

        void next() {
            String str = this.path;
            int i = this.pos;
            this.pos = i + 1;
            this.ch = str.charAt(i);
        }

        char getNextChar() {
            return this.path.charAt(this.pos);
        }

        boolean isEOF() {
            return this.pos >= this.path.length();
        }

        Segment readSegement() {
            boolean z;
            char c2;
            if (this.level == 0 && this.path.length() == 1) {
                if (isDigitFirst(this.ch)) {
                    return new ArrayAccessSegment(this.ch - '0');
                }
                char c3 = this.ch;
                if ((c3 >= 'a' && c3 <= 'z') || ((c2 = this.ch) >= 'A' && c2 <= 'Z')) {
                    return new PropertySegment(Character.toString(this.ch), false);
                }
            }
            while (!isEOF()) {
                skipWhitespace();
                char c4 = this.ch;
                if (c4 != '$') {
                    if (c4 != '.' && c4 != '/') {
                        if (c4 == '[') {
                            return parseArrayAccess(true);
                        }
                        if (this.level == 0) {
                            return new PropertySegment(readName(), false);
                        }
                        if (c4 == '?') {
                            return new FilterSegment((Filter) parseArrayAccessFilter(false));
                        }
                        throw new JSONPathException("not support jsonpath : " + this.path);
                    }
                    char c5 = this.ch;
                    next();
                    if (c5 == '.' && this.ch == '.') {
                        next();
                        int length = this.path.length();
                        int i = this.pos;
                        if (length > i + 3 && this.ch == '[' && this.path.charAt(i) == '*' && this.path.charAt(this.pos + 1) == ']' && this.path.charAt(this.pos + 2) == '.') {
                            next();
                            next();
                            next();
                            next();
                        }
                        z = true;
                    } else {
                        z = false;
                    }
                    char c6 = this.ch;
                    if (c6 == '*' || (z && c6 == '[')) {
                        boolean z2 = this.ch == '[';
                        if (!isEOF()) {
                            next();
                        }
                        if (!z) {
                            return WildCardSegment.instance;
                        }
                        if (z2) {
                            return WildCardSegment.instance_deep_objectOnly;
                        }
                        return WildCardSegment.instance_deep;
                    }
                    if (isDigitFirst(this.ch)) {
                        return parseArrayAccess(false);
                    }
                    String name = readName();
                    if (this.ch == '(') {
                        next();
                        if (this.ch == ')') {
                            if (!isEOF()) {
                                next();
                            }
                            if ("size".equals(name) || "length".equals(name)) {
                                return SizeSegment.instance;
                            }
                            if ("max".equals(name)) {
                                return MaxSegment.instance;
                            }
                            if ("min".equals(name)) {
                                return MinSegment.instance;
                            }
                            if ("keySet".equals(name)) {
                                return KeySetSegment.instance;
                            }
                            if ("type".equals(name)) {
                                return TypeSegment.instance;
                            }
                            if ("floor".equals(name)) {
                                return FloorSegment.instance;
                            }
                            throw new JSONPathException("not support jsonpath : " + this.path);
                        }
                        throw new JSONPathException("not support jsonpath : " + this.path);
                    }
                    return new PropertySegment(name, z);
                }
                next();
                skipWhitespace();
                if (this.ch == '?') {
                    return new FilterSegment((Filter) parseArrayAccessFilter(false));
                }
            }
            return null;
        }

        public final void skipWhitespace() {
            while (true) {
                char c2 = this.ch;
                if (c2 > ' ') {
                    return;
                }
                if (c2 != ' ' && c2 != '\r' && c2 != '\n' && c2 != '\t' && c2 != '\f' && c2 != '\b') {
                    return;
                } else {
                    next();
                }
            }
        }

        Segment parseArrayAccess(boolean z) {
            Object arrayAccessFilter = parseArrayAccessFilter(z);
            if (arrayAccessFilter instanceof Segment) {
                return (Segment) arrayAccessFilter;
            }
            return new FilterSegment((Filter) arrayAccessFilter);
        }

        Object parseArrayAccessFilter(boolean z) {
            boolean z2;
            int i;
            boolean z3;
            char c2;
            char c3;
            char c4;
            char c5;
            char c6;
            char c7;
            char c8;
            char c9;
            char c10;
            char c11;
            Filter nullSegement;
            char c12;
            char c13;
            String str;
            String[] strArr;
            String str2;
            Filter matchSegement;
            Operator operator;
            char c14;
            Filter valueSegment;
            char c15;
            char c16;
            Filter valueSegment2;
            char c17;
            char c18;
            Filter notNullSegement;
            char c19;
            char c20;
            Filter doubleOpSegement;
            char c21;
            char c22;
            char c23;
            char c24;
            char c25;
            int i2;
            String strReplaceAll;
            int i3;
            char cCharAt;
            if (z) {
                accept('[');
            }
            int i4 = 0;
            if (this.ch == '?') {
                next();
                accept('(');
                int i5 = 1;
                while (this.ch == '(') {
                    next();
                    i5++;
                }
                i = i5;
                z2 = true;
            } else {
                z2 = false;
                i = 0;
            }
            skipWhitespace();
            char c26 = '\\';
            if (z2 || IOUtils.firstIdentifier(this.ch) || Character.isJavaIdentifierStart(this.ch) || (c24 = this.ch) == '\\' || c24 == '@') {
                if (this.ch == '@') {
                    next();
                    accept('.');
                }
                String name = readName();
                skipWhitespace();
                char c27 = '|';
                if (z2 && this.ch == ')') {
                    next();
                    Filter notNullSegement2 = new NotNullSegement(name, false);
                    while (true) {
                        c23 = this.ch;
                        if (c23 != ' ') {
                            break;
                        }
                        next();
                    }
                    if (c23 == '&' || c23 == '|') {
                        notNullSegement2 = filterRest(notNullSegement2);
                    }
                    if (z) {
                        accept(']');
                    }
                    return notNullSegement2;
                }
                if (z && this.ch == ']') {
                    if (isEOF() && name.equals("last")) {
                        return new MultiIndexSegment(new int[]{-1});
                    }
                    next();
                    Filter notNullSegement3 = new NotNullSegement(name, false);
                    while (true) {
                        c22 = this.ch;
                        if (c22 != ' ') {
                            break;
                        }
                        next();
                    }
                    if (c22 == '&' || c22 == '|') {
                        notNullSegement3 = filterRest(notNullSegement3);
                    }
                    accept(')');
                    if (z2) {
                        accept(')');
                    }
                    if (z) {
                        accept(']');
                    }
                    return notNullSegement3;
                }
                skipWhitespace();
                if (this.ch == '(') {
                    next();
                    accept(')');
                    skipWhitespace();
                    z3 = true;
                } else {
                    z3 = false;
                }
                Operator op = readOp();
                skipWhitespace();
                if (op == Operator.BETWEEN || op == Operator.NOT_BETWEEN) {
                    boolean z4 = op == Operator.NOT_BETWEEN;
                    Object value = readValue();
                    if (!"and".equalsIgnoreCase(readName())) {
                        throw new JSONPathException(this.path);
                    }
                    Object value2 = readValue();
                    if (value == null || value2 == null) {
                        throw new JSONPathException(this.path);
                    }
                    if (JSONPath.isInt(value.getClass()) && JSONPath.isInt(value2.getClass())) {
                        return new IntBetweenSegement(name, z3, TypeUtils.longExtractValue((Number) value), TypeUtils.longExtractValue((Number) value2), z4);
                    }
                    throw new JSONPathException(this.path);
                }
                if (op == Operator.IN || op == Operator.NOT_IN) {
                    boolean z5 = op == Operator.NOT_IN;
                    accept('(');
                    JSONArray jSONArray = new JSONArray();
                    jSONArray.add(readValue());
                    while (true) {
                        skipWhitespace();
                        if (this.ch != ',') {
                            break;
                        }
                        next();
                        jSONArray.add(readValue());
                        c27 = c27;
                    }
                    boolean z6 = true;
                    boolean z7 = true;
                    boolean z8 = true;
                    for (Object obj : jSONArray) {
                        if (obj != null) {
                            Class<?> cls = obj.getClass();
                            if (z6 && cls != Byte.class && cls != Short.class && cls != Integer.class && cls != Long.class) {
                                z6 = false;
                                z8 = false;
                            }
                            if (z7 && cls != String.class) {
                                z7 = false;
                            }
                        } else if (z6) {
                            z6 = false;
                        }
                    }
                    if (jSONArray.size() == 1 && jSONArray.get(0) == null) {
                        if (z5) {
                            nullSegement = new NotNullSegement(name, z3);
                        } else {
                            nullSegement = new NullSegement(name, z3);
                        }
                        while (true) {
                            c12 = this.ch;
                            if (c12 != ' ') {
                                break;
                            }
                            next();
                        }
                        if (c12 == '&' || c12 == '|') {
                            nullSegement = filterRest(nullSegement);
                            c13 = ')';
                        } else {
                            c13 = ')';
                        }
                        accept(c13);
                        if (z2) {
                            accept(c13);
                        }
                        if (z) {
                            accept(']');
                        }
                        return nullSegement;
                    }
                    if (z6) {
                        if (jSONArray.size() == 1) {
                            Filter intOpSegement = new IntOpSegement(name, z3, TypeUtils.longExtractValue((Number) jSONArray.get(0)), z5 ? Operator.NE : Operator.EQ);
                            while (true) {
                                c10 = this.ch;
                                if (c10 != ' ') {
                                    break;
                                }
                                next();
                            }
                            if (c10 == '&' || c10 == '|') {
                                intOpSegement = filterRest(intOpSegement);
                                c11 = ')';
                            } else {
                                c11 = ')';
                            }
                            accept(c11);
                            if (z2) {
                                accept(c11);
                            }
                            if (z) {
                                accept(']');
                            }
                            return intOpSegement;
                        }
                        long[] jArr = new long[jSONArray.size()];
                        while (i4 < jArr.length) {
                            jArr[i4] = TypeUtils.longExtractValue((Number) jSONArray.get(i4));
                            i4++;
                        }
                        Filter intInSegement = new IntInSegement(name, z3, jArr, z5);
                        while (true) {
                            c8 = this.ch;
                            if (c8 != ' ') {
                                break;
                            }
                            next();
                        }
                        if (c8 == '&' || c8 == '|') {
                            intInSegement = filterRest(intInSegement);
                            c9 = ')';
                        } else {
                            c9 = ')';
                        }
                        accept(c9);
                        if (z2) {
                            accept(c9);
                        }
                        if (z) {
                            accept(']');
                        }
                        return intInSegement;
                    }
                    if (!z7) {
                        if (z8) {
                            Long[] lArr = new Long[jSONArray.size()];
                            while (i4 < lArr.length) {
                                Number number = (Number) jSONArray.get(i4);
                                if (number != null) {
                                    lArr[i4] = Long.valueOf(TypeUtils.longExtractValue(number));
                                }
                                i4++;
                            }
                            Filter intObjInSegement = new IntObjInSegement(name, z3, lArr, z5);
                            while (true) {
                                c2 = this.ch;
                                if (c2 != ' ') {
                                    break;
                                }
                                next();
                            }
                            if (c2 == '&' || c2 == '|') {
                                intObjInSegement = filterRest(intObjInSegement);
                                c3 = ')';
                            } else {
                                c3 = ')';
                            }
                            accept(c3);
                            if (z2) {
                                accept(c3);
                            }
                            if (z) {
                                accept(']');
                            }
                            return intObjInSegement;
                        }
                        throw new UnsupportedOperationException();
                    }
                    if (jSONArray.size() == 1) {
                        Filter stringOpSegement = new StringOpSegement(name, z3, (String) jSONArray.get(0), z5 ? Operator.NE : Operator.EQ);
                        while (true) {
                            c6 = this.ch;
                            if (c6 != ' ') {
                                break;
                            }
                            next();
                        }
                        if (c6 == '&' || c6 == '|') {
                            stringOpSegement = filterRest(stringOpSegement);
                            c7 = ')';
                        } else {
                            c7 = ')';
                        }
                        accept(c7);
                        if (z2) {
                            accept(c7);
                        }
                        if (z) {
                            accept(']');
                        }
                        return stringOpSegement;
                    }
                    String[] strArr2 = new String[jSONArray.size()];
                    jSONArray.toArray(strArr2);
                    Filter stringInSegement = new StringInSegement(name, z3, strArr2, z5);
                    while (true) {
                        c4 = this.ch;
                        if (c4 != ' ') {
                            break;
                        }
                        next();
                    }
                    if (c4 == '&' || c4 == '|') {
                        stringInSegement = filterRest(stringInSegement);
                        c5 = ')';
                    } else {
                        c5 = ')';
                    }
                    accept(c5);
                    if (z2) {
                        accept(c5);
                    }
                    if (z) {
                        accept(']');
                    }
                    return stringInSegement;
                }
                char c28 = this.ch;
                if (c28 == '\'' || c28 == '\"') {
                    String string = readString();
                    if (op == Operator.RLIKE) {
                        matchSegement = new RlikeSegement(name, z3, string, false);
                    } else if (op == Operator.NOT_RLIKE) {
                        matchSegement = new RlikeSegement(name, z3, string, true);
                    } else if (op == Operator.LIKE || op == Operator.NOT_LIKE) {
                        while (string.indexOf("%%") != -1) {
                            string = string.replaceAll("%%", "%");
                        }
                        boolean z9 = op == Operator.NOT_LIKE;
                        int iIndexOf = string.indexOf(37);
                        if (iIndexOf == -1) {
                            if (op == Operator.LIKE) {
                                operator = Operator.EQ;
                            } else {
                                operator = Operator.NE;
                            }
                            matchSegement = new StringOpSegement(name, z3, string, operator);
                        } else {
                            String[] strArrSplit = string.split("%");
                            if (iIndexOf == 0) {
                                if (string.charAt(string.length() - 1) == '%') {
                                    String[] strArr3 = new String[strArrSplit.length - 1];
                                    System.arraycopy(strArrSplit, 1, strArr3, 0, strArr3.length);
                                    strArr = strArr3;
                                    str = null;
                                    str2 = null;
                                } else {
                                    String str3 = strArrSplit[strArrSplit.length - 1];
                                    if (strArrSplit.length > 2) {
                                        String[] strArr4 = new String[strArrSplit.length - 2];
                                        System.arraycopy(strArrSplit, 1, strArr4, 0, strArr4.length);
                                        str2 = str3;
                                        strArr = strArr4;
                                        str = null;
                                    } else {
                                        str2 = str3;
                                        str = null;
                                        strArr = null;
                                    }
                                }
                            } else if (string.charAt(string.length() - 1) == '%') {
                                if (strArrSplit.length == 1) {
                                    str = strArrSplit[0];
                                    str2 = null;
                                    strArr = null;
                                } else {
                                    strArr = strArrSplit;
                                    str = null;
                                    str2 = null;
                                }
                            } else if (strArrSplit.length == 1) {
                                str = strArrSplit[0];
                                str2 = null;
                                strArr = null;
                            } else if (strArrSplit.length == 2) {
                                str = strArrSplit[0];
                                str2 = strArrSplit[1];
                                strArr = null;
                            } else {
                                String str4 = strArrSplit[0];
                                String str5 = strArrSplit[strArrSplit.length - 1];
                                String[] strArr5 = new String[strArrSplit.length - 2];
                                System.arraycopy(strArrSplit, 1, strArr5, 0, strArr5.length);
                                str = str4;
                                strArr = strArr5;
                                str2 = str5;
                            }
                            matchSegement = new MatchSegement(name, z3, str, str2, strArr, z9);
                        }
                    } else {
                        matchSegement = new StringOpSegement(name, z3, string, op);
                    }
                    while (true) {
                        c14 = this.ch;
                        if (c14 != ' ') {
                            break;
                        }
                        next();
                    }
                    if (c14 == '&' || c14 == '|') {
                        matchSegement = filterRest(matchSegement);
                    }
                    if (z2) {
                        accept(')');
                    }
                    if (z) {
                        accept(']');
                    }
                    return matchSegement;
                }
                if (isDigitFirst(c28)) {
                    long longValue = readLongValue();
                    double doubleValue = this.ch == '.' ? readDoubleValue(longValue) : 0.0d;
                    if (doubleValue == 0.0d) {
                        doubleOpSegement = new IntOpSegement(name, z3, longValue, op);
                    } else {
                        doubleOpSegement = new DoubleOpSegement(name, z3, doubleValue, op);
                    }
                    while (true) {
                        c21 = this.ch;
                        if (c21 != ' ') {
                            break;
                        }
                        next();
                    }
                    if (i > 1 && c21 == ')') {
                        next();
                    }
                    char c29 = this.ch;
                    if (c29 == '&' || c29 == '|') {
                        doubleOpSegement = filterRest(doubleOpSegement);
                    }
                    if (z2) {
                        accept(')');
                    }
                    if (z) {
                        accept(']');
                    }
                    return doubleOpSegement;
                }
                char c30 = this.ch;
                if (c30 == '$') {
                    RefOpSegement refOpSegement = new RefOpSegement(name, z3, readSegement(), op);
                    this.hasRefSegment = true;
                    while (this.ch == ' ') {
                        next();
                    }
                    if (z2) {
                        accept(')');
                    }
                    if (z) {
                        accept(']');
                    }
                    return refOpSegement;
                }
                if (c30 == '/') {
                    StringBuilder sb = new StringBuilder();
                    while (true) {
                        next();
                        char c31 = this.ch;
                        if (c31 == '/') {
                            break;
                        }
                        if (c31 == '\\') {
                            next();
                            sb.append(this.ch);
                        } else {
                            sb.append(c31);
                        }
                    }
                    next();
                    if (this.ch == 'i') {
                        next();
                        i4 = 2;
                    }
                    RegMatchSegement regMatchSegement = new RegMatchSegement(name, z3, Pattern.compile(sb.toString(), i4), op);
                    if (z2) {
                        accept(')');
                    }
                    if (z) {
                        accept(']');
                    }
                    return regMatchSegement;
                }
                if (c30 == 'n') {
                    if (TmpConstant.GROUP_ROLE_UNKNOWN.equals(readName())) {
                        if (op == Operator.EQ) {
                            notNullSegement = new NullSegement(name, z3);
                        } else {
                            notNullSegement = op == Operator.NE ? new NotNullSegement(name, z3) : null;
                        }
                        if (notNullSegement != null) {
                            while (true) {
                                c20 = this.ch;
                                if (c20 != ' ') {
                                    break;
                                }
                                next();
                            }
                            if (c20 == '&' || c20 == '|') {
                                notNullSegement = filterRest(notNullSegement);
                            }
                        }
                        if (z2) {
                            accept(')');
                            c19 = ']';
                        } else {
                            c19 = ']';
                        }
                        accept(c19);
                        if (notNullSegement != null) {
                            return notNullSegement;
                        }
                        throw new UnsupportedOperationException();
                    }
                } else if (c30 == 't') {
                    if ("true".equals(readName())) {
                        if (op == Operator.EQ) {
                            valueSegment2 = new ValueSegment(name, z3, Boolean.TRUE, true);
                        } else {
                            valueSegment2 = op == Operator.NE ? new ValueSegment(name, z3, Boolean.TRUE, false) : null;
                        }
                        if (valueSegment2 != null) {
                            while (true) {
                                c18 = this.ch;
                                if (c18 != ' ') {
                                    break;
                                }
                                next();
                            }
                            if (c18 == '&' || c18 == '|') {
                                valueSegment2 = filterRest(valueSegment2);
                            }
                        }
                        if (z2) {
                            accept(')');
                            c17 = ']';
                        } else {
                            c17 = ']';
                        }
                        accept(c17);
                        if (valueSegment2 != null) {
                            return valueSegment2;
                        }
                        throw new UnsupportedOperationException();
                    }
                } else if (c30 == 'f' && RequestConstant.FALSE.equals(readName())) {
                    if (op == Operator.EQ) {
                        valueSegment = new ValueSegment(name, z3, Boolean.FALSE, true);
                    } else {
                        valueSegment = op == Operator.NE ? new ValueSegment(name, z3, Boolean.FALSE, false) : null;
                    }
                    if (valueSegment != null) {
                        while (true) {
                            c16 = this.ch;
                            if (c16 != ' ') {
                                break;
                            }
                            next();
                        }
                        if (c16 == '&' || c16 == '|') {
                            valueSegment = filterRest(valueSegment);
                        }
                    }
                    if (z2) {
                        accept(')');
                        c15 = ']';
                    } else {
                        c15 = ']';
                    }
                    accept(c15);
                    if (valueSegment != null) {
                        return valueSegment;
                    }
                    throw new UnsupportedOperationException();
                }
                throw new UnsupportedOperationException();
            }
            int i6 = this.pos - 1;
            while (true) {
                char c32 = this.ch;
                if (c32 == ']' || c32 == '/' || isEOF() || !(this.ch != '.' || z2 || z2 || c24 == '\'')) {
                    break;
                }
                if (this.ch == '\\') {
                    next();
                }
                next();
            }
            if (z || (c25 = this.ch) == '/' || c25 == '.') {
                i2 = this.pos - 1;
            } else {
                i2 = this.pos;
            }
            String strSubstring = this.path.substring(i6, i2);
            if (strSubstring.indexOf(92) != 0) {
                StringBuilder sb2 = new StringBuilder(strSubstring.length());
                int i7 = 0;
                while (i7 < strSubstring.length()) {
                    char cCharAt2 = strSubstring.charAt(i7);
                    if (cCharAt2 == c26 && i7 < strSubstring.length() - 1 && ((cCharAt = strSubstring.charAt((i3 = i7 + 1))) == '@' || cCharAt2 == c26 || cCharAt2 == '\"')) {
                        sb2.append(cCharAt);
                    } else {
                        sb2.append(cCharAt2);
                        i3 = i7;
                    }
                    i7 = i3 + 1;
                    c26 = '\\';
                }
                strSubstring = sb2.toString();
            }
            if (strSubstring.indexOf("\\.") != -1) {
                if (c24 == '\'' && strSubstring.length() > 2 && strSubstring.charAt(strSubstring.length() - 1) == c24) {
                    strReplaceAll = strSubstring.substring(1, strSubstring.length() - 1);
                } else {
                    strReplaceAll = strSubstring.replaceAll("\\\\\\.", "\\.");
                    if (strReplaceAll.indexOf("\\-") != -1) {
                        strReplaceAll = strReplaceAll.replaceAll("\\\\-", Constants.ACCEPT_TIME_SEPARATOR_SERVER);
                    }
                }
                if (z2) {
                    accept(')');
                }
                return new PropertySegment(strReplaceAll, false);
            }
            Segment segmentBuildArraySegement = buildArraySegement(strSubstring);
            if (z && !isEOF()) {
                accept(']');
            }
            return segmentBuildArraySegement;
        }

        Filter filterRest(Filter filter) {
            boolean z = true;
            boolean z2 = this.ch == '&';
            if ((this.ch != '&' || getNextChar() != '&') && (this.ch != '|' || getNextChar() != '|')) {
                return filter;
            }
            next();
            next();
            if (this.ch == '(') {
                next();
            } else {
                z = false;
            }
            while (this.ch == ' ') {
                next();
            }
            FilterGroup filterGroup = new FilterGroup(filter, (Filter) parseArrayAccessFilter(false), z2);
            if (z && this.ch == ')') {
                next();
            }
            return filterGroup;
        }

        protected long readLongValue() {
            int i = this.pos - 1;
            char c2 = this.ch;
            if (c2 == '+' || c2 == '-') {
                next();
            }
            while (true) {
                char c3 = this.ch;
                if (c3 < '0' || c3 > '9') {
                    break;
                }
                next();
            }
            return Long.parseLong(this.path.substring(i, this.pos - 1));
        }

        protected double readDoubleValue(long j) {
            int i = this.pos - 1;
            next();
            while (true) {
                char c2 = this.ch;
                if (c2 < '0' || c2 > '9') {
                    break;
                }
                next();
            }
            return Double.parseDouble(this.path.substring(i, this.pos - 1)) + j;
        }

        protected Object readValue() {
            skipWhitespace();
            if (isDigitFirst(this.ch)) {
                return Long.valueOf(readLongValue());
            }
            char c2 = this.ch;
            if (c2 == '\"' || c2 == '\'') {
                return readString();
            }
            if (c2 == 'n') {
                if (TmpConstant.GROUP_ROLE_UNKNOWN.equals(readName())) {
                    return null;
                }
                throw new JSONPathException(this.path);
            }
            throw new UnsupportedOperationException();
        }

        protected Operator readOp() {
            Operator operator;
            char c2 = this.ch;
            if (c2 == '=') {
                next();
                char c3 = this.ch;
                if (c3 == '~') {
                    next();
                    operator = Operator.REG_MATCH;
                } else if (c3 == '=') {
                    next();
                    operator = Operator.EQ;
                } else {
                    operator = Operator.EQ;
                }
            } else if (c2 == '!') {
                next();
                accept('=');
                operator = Operator.NE;
            } else if (c2 == '<') {
                next();
                if (this.ch == '=') {
                    next();
                    operator = Operator.LE;
                } else {
                    operator = Operator.LT;
                }
            } else if (c2 == '>') {
                next();
                if (this.ch == '=') {
                    next();
                    operator = Operator.GE;
                } else {
                    operator = Operator.GT;
                }
            } else {
                operator = null;
            }
            if (operator != null) {
                return operator;
            }
            String name = readName();
            if ("not".equalsIgnoreCase(name)) {
                skipWhitespace();
                String name2 = readName();
                if ("like".equalsIgnoreCase(name2)) {
                    return Operator.NOT_LIKE;
                }
                if ("rlike".equalsIgnoreCase(name2)) {
                    return Operator.NOT_RLIKE;
                }
                if ("in".equalsIgnoreCase(name2)) {
                    return Operator.NOT_IN;
                }
                if ("between".equalsIgnoreCase(name2)) {
                    return Operator.NOT_BETWEEN;
                }
                throw new UnsupportedOperationException();
            }
            if ("nin".equalsIgnoreCase(name)) {
                return Operator.NOT_IN;
            }
            if ("like".equalsIgnoreCase(name)) {
                return Operator.LIKE;
            }
            if ("rlike".equalsIgnoreCase(name)) {
                return Operator.RLIKE;
            }
            if ("in".equalsIgnoreCase(name)) {
                return Operator.IN;
            }
            if ("between".equalsIgnoreCase(name)) {
                return Operator.BETWEEN;
            }
            throw new UnsupportedOperationException();
        }

        String readName() {
            skipWhitespace();
            char c2 = this.ch;
            if (c2 != '\\' && !Character.isJavaIdentifierStart(c2)) {
                throw new JSONPathException("illeal jsonpath syntax. " + this.path);
            }
            StringBuilder sb = new StringBuilder();
            while (!isEOF()) {
                char c3 = this.ch;
                if (c3 == '\\') {
                    next();
                    sb.append(this.ch);
                    if (isEOF()) {
                        return sb.toString();
                    }
                    next();
                } else {
                    if (!Character.isJavaIdentifierPart(c3)) {
                        break;
                    }
                    sb.append(this.ch);
                    next();
                }
            }
            if (isEOF() && Character.isJavaIdentifierPart(this.ch)) {
                sb.append(this.ch);
            }
            return sb.toString();
        }

        String readString() {
            char c2 = this.ch;
            next();
            int i = this.pos - 1;
            while (this.ch != c2 && !isEOF()) {
                next();
            }
            String strSubstring = this.path.substring(i, isEOF() ? this.pos : this.pos - 1);
            accept(c2);
            return strSubstring;
        }

        void accept(char c2) {
            if (this.ch == ' ') {
                next();
            }
            if (this.ch != c2) {
                throw new JSONPathException("expect '" + c2 + ", but '" + this.ch + "'");
            }
            if (isEOF()) {
                return;
            }
            next();
        }

        public Segment[] explain() {
            String str = this.path;
            if (str == null || str.length() == 0) {
                throw new IllegalArgumentException();
            }
            Segment[] segmentArr = new Segment[8];
            while (true) {
                Segment segement = readSegement();
                if (segement == null) {
                    break;
                }
                if (segement instanceof PropertySegment) {
                    PropertySegment propertySegment = (PropertySegment) segement;
                    if (propertySegment.deep || !propertySegment.propertyName.equals(WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD)) {
                    }
                }
                int i = this.level;
                if (i == segmentArr.length) {
                    Segment[] segmentArr2 = new Segment[(i * 3) / 2];
                    System.arraycopy(segmentArr, 0, segmentArr2, 0, i);
                    segmentArr = segmentArr2;
                }
                int i2 = this.level;
                this.level = i2 + 1;
                segmentArr[i2] = segement;
            }
            int i3 = this.level;
            if (i3 == segmentArr.length) {
                return segmentArr;
            }
            Segment[] segmentArr3 = new Segment[i3];
            System.arraycopy(segmentArr, 0, segmentArr3, 0, i3);
            return segmentArr3;
        }

        Segment buildArraySegement(String str) {
            int length = str.length();
            char cCharAt = str.charAt(0);
            int i = length - 1;
            char cCharAt2 = str.charAt(i);
            int iIndexOf = str.indexOf(44);
            if (str.length() > 2 && cCharAt == '\'' && cCharAt2 == '\'') {
                String strSubstring = str.substring(1, i);
                if (iIndexOf == -1 || !strArrayPatternx.matcher(str).find()) {
                    return new PropertySegment(strSubstring, false);
                }
                return new MultiPropertySegment(strSubstring.split(strArrayRegex));
            }
            int iIndexOf2 = str.indexOf(58);
            if (iIndexOf == -1 && iIndexOf2 == -1) {
                if (TypeUtils.isNumber(str)) {
                    try {
                        return new ArrayAccessSegment(Integer.parseInt(str));
                    } catch (NumberFormatException unused) {
                        return new PropertySegment(str, false);
                    }
                }
                if (str.charAt(0) == '\"' && str.charAt(str.length() - 1) == '\"') {
                    str = str.substring(1, str.length() - 1);
                }
                return new PropertySegment(str, false);
            }
            if (iIndexOf != -1) {
                String[] strArrSplit = str.split(",");
                int[] iArr = new int[strArrSplit.length];
                for (int i2 = 0; i2 < strArrSplit.length; i2++) {
                    iArr[i2] = Integer.parseInt(strArrSplit[i2]);
                }
                return new MultiIndexSegment(iArr);
            }
            if (iIndexOf2 != -1) {
                String[] strArrSplit2 = str.split(":");
                int[] iArr2 = new int[strArrSplit2.length];
                for (int i3 = 0; i3 < strArrSplit2.length; i3++) {
                    String str2 = strArrSplit2[i3];
                    if (str2.length() != 0) {
                        iArr2[i3] = Integer.parseInt(str2);
                    } else if (i3 == 0) {
                        iArr2[i3] = 0;
                    } else {
                        throw new UnsupportedOperationException();
                    }
                }
                int i4 = iArr2[0];
                int i5 = iArr2.length > 1 ? iArr2[1] : -1;
                int i6 = iArr2.length == 3 ? iArr2[2] : 1;
                if (i5 < 0 || i5 >= i4) {
                    if (i6 <= 0) {
                        throw new UnsupportedOperationException("step must greater than zero : " + i6);
                    }
                    return new RangeSegment(i4, i5, i6);
                }
                throw new UnsupportedOperationException("end must greater than or equals start. start " + i4 + ",  end " + i5);
            }
            throw new UnsupportedOperationException();
        }
    }

    static class SizeSegment implements Segment {
        public static final SizeSegment instance = new SizeSegment();

        SizeSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Integer eval(JSONPath jSONPath, Object obj, Object obj2) {
            return Integer.valueOf(jSONPath.evalSize(obj2));
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            context.object = Integer.valueOf(jSONPath.evalSize(defaultJSONParser.parse()));
        }
    }

    static class TypeSegment implements Segment {
        public static final TypeSegment instance = new TypeSegment();

        TypeSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public String eval(JSONPath jSONPath, Object obj, Object obj2) {
            return obj2 == null ? TmpConstant.GROUP_ROLE_UNKNOWN : obj2 instanceof Collection ? TmpConstant.TYPE_VALUE_ARRAY : obj2 instanceof Number ? "number" : obj2 instanceof Boolean ? "boolean" : ((obj2 instanceof String) || (obj2 instanceof UUID) || (obj2 instanceof Enum)) ? "string" : "object";
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static class FloorSegment implements Segment {
        public static final FloorSegment instance = new FloorSegment();

        FloorSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (obj2 instanceof JSONArray) {
                JSONArray jSONArray = (JSONArray) ((JSONArray) obj2).clone();
                for (int i = 0; i < jSONArray.size(); i++) {
                    Object obj3 = jSONArray.get(i);
                    Object objFloor = floor(obj3);
                    if (objFloor != obj3) {
                        jSONArray.set(i, objFloor);
                    }
                }
                return jSONArray;
            }
            return floor(obj2);
        }

        private static Object floor(Object obj) {
            if (obj == null) {
                return null;
            }
            if (obj instanceof Float) {
                return Double.valueOf(Math.floor(((Float) obj).floatValue()));
            }
            if (obj instanceof Double) {
                return Double.valueOf(Math.floor(((Double) obj).doubleValue()));
            }
            if (obj instanceof BigDecimal) {
                return ((BigDecimal) obj).setScale(0, RoundingMode.FLOOR);
            }
            if ((obj instanceof Byte) || (obj instanceof Short) || (obj instanceof Integer) || (obj instanceof Long) || (obj instanceof BigInteger)) {
                return obj;
            }
            throw new UnsupportedOperationException();
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static class MaxSegment implements Segment {
        public static final MaxSegment instance = new MaxSegment();

        MaxSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (obj2 instanceof Collection) {
                Object obj3 = null;
                for (Object obj4 : (Collection) obj2) {
                    if (obj4 != null && (obj3 == null || JSONPath.compare(obj3, obj4) < 0)) {
                        obj3 = obj4;
                    }
                }
                return obj3;
            }
            throw new UnsupportedOperationException();
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static class MinSegment implements Segment {
        public static final MinSegment instance = new MinSegment();

        MinSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (obj2 instanceof Collection) {
                Object obj3 = null;
                for (Object obj4 : (Collection) obj2) {
                    if (obj4 != null && (obj3 == null || JSONPath.compare(obj3, obj4) > 0)) {
                        obj3 = obj4;
                    }
                }
                return obj3;
            }
            throw new UnsupportedOperationException();
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:70:0x0176  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    static int compare(java.lang.Object r3, java.lang.Object r4) {
        /*
            Method dump skipped, instruction units count: 382
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.JSONPath.compare(java.lang.Object, java.lang.Object):int");
    }

    static class KeySetSegment implements Segment {
        public static final KeySetSegment instance = new KeySetSegment();

        KeySetSegment() {
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.evalKeySet(obj2);
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static class PropertySegment implements Segment {
        private final boolean deep;
        private final String propertyName;
        private final long propertyNameHash;

        public PropertySegment(String str, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.deep = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (this.deep) {
                ArrayList arrayList = new ArrayList();
                jSONPath.deepScan(obj2, this.propertyName, arrayList);
                return arrayList;
            }
            return jSONPath.getPropertyValue(obj2, this.propertyName, this.propertyNameHash);
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            Object objIntegerValue;
            Object objIntegerValue2;
            JSONArray jSONArray;
            Object objIntegerValue3;
            JSONLexerBase jSONLexerBase = (JSONLexerBase) defaultJSONParser.lexer;
            if (this.deep && context.object == null) {
                context.object = new JSONArray();
            }
            if (jSONLexerBase.token() == 14) {
                if (WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD.equals(this.propertyName)) {
                    return;
                }
                jSONLexerBase.nextToken();
                if (this.deep) {
                    jSONArray = (JSONArray) context.object;
                } else {
                    jSONArray = new JSONArray();
                }
                while (true) {
                    int i = jSONLexerBase.token();
                    if (i == 12) {
                        boolean z = this.deep;
                        if (z) {
                            extract(jSONPath, defaultJSONParser, context);
                        } else {
                            int iSeekObjectToField = jSONLexerBase.seekObjectToField(this.propertyNameHash, z);
                            if (iSeekObjectToField == 3) {
                                int i2 = jSONLexerBase.token();
                                if (i2 == 2) {
                                    objIntegerValue3 = jSONLexerBase.integerValue();
                                    jSONLexerBase.nextToken();
                                } else if (i2 == 4) {
                                    objIntegerValue3 = jSONLexerBase.stringVal();
                                    jSONLexerBase.nextToken();
                                } else {
                                    objIntegerValue3 = defaultJSONParser.parse();
                                }
                                jSONArray.add(objIntegerValue3);
                                if (jSONLexerBase.token() == 13) {
                                    jSONLexerBase.nextToken();
                                } else {
                                    jSONLexerBase.skipObject(false);
                                }
                            } else if (iSeekObjectToField == -1) {
                                continue;
                            } else {
                                if (this.deep) {
                                    throw new UnsupportedOperationException(jSONLexerBase.info());
                                }
                                jSONLexerBase.skipObject(false);
                            }
                        }
                    } else if (i == 14) {
                        if (this.deep) {
                            extract(jSONPath, defaultJSONParser, context);
                        } else {
                            jSONLexerBase.skipObject(false);
                        }
                    } else {
                        switch (i) {
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                            case 8:
                                jSONLexerBase.nextToken();
                                break;
                        }
                    }
                    if (jSONLexerBase.token() == 15) {
                        jSONLexerBase.nextToken();
                        if (this.deep || jSONArray.size() <= 0) {
                            return;
                        }
                        context.object = jSONArray;
                        return;
                    }
                    if (jSONLexerBase.token() == 16) {
                        jSONLexerBase.nextToken();
                    } else {
                        throw new JSONException("illegal json : " + jSONLexerBase.info());
                    }
                }
            } else {
                boolean z2 = this.deep;
                if (!z2) {
                    if (jSONLexerBase.seekObjectToField(this.propertyNameHash, z2) == 3 && context.eval) {
                        switch (jSONLexerBase.token()) {
                            case 2:
                                objIntegerValue2 = jSONLexerBase.integerValue();
                                jSONLexerBase.nextToken(16);
                                break;
                            case 3:
                                objIntegerValue2 = jSONLexerBase.decimalValue();
                                jSONLexerBase.nextToken(16);
                                break;
                            case 4:
                                objIntegerValue2 = jSONLexerBase.stringVal();
                                jSONLexerBase.nextToken(16);
                                break;
                            default:
                                objIntegerValue2 = defaultJSONParser.parse();
                                break;
                        }
                        if (context.eval) {
                            context.object = objIntegerValue2;
                            return;
                        }
                        return;
                    }
                    return;
                }
                while (true) {
                    int iSeekObjectToField2 = jSONLexerBase.seekObjectToField(this.propertyNameHash, this.deep);
                    if (iSeekObjectToField2 == -1) {
                        return;
                    }
                    if (iSeekObjectToField2 == 3) {
                        if (context.eval) {
                            switch (jSONLexerBase.token()) {
                                case 2:
                                    objIntegerValue = jSONLexerBase.integerValue();
                                    jSONLexerBase.nextToken(16);
                                    break;
                                case 3:
                                    objIntegerValue = jSONLexerBase.decimalValue();
                                    jSONLexerBase.nextToken(16);
                                    break;
                                case 4:
                                    objIntegerValue = jSONLexerBase.stringVal();
                                    jSONLexerBase.nextToken(16);
                                    break;
                                default:
                                    objIntegerValue = defaultJSONParser.parse();
                                    break;
                            }
                            if (context.eval) {
                                if (context.object instanceof List) {
                                    List list = (List) context.object;
                                    if (list.size() == 0 && (objIntegerValue instanceof List)) {
                                        context.object = objIntegerValue;
                                    } else {
                                        list.add(objIntegerValue);
                                    }
                                } else {
                                    context.object = objIntegerValue;
                                }
                            }
                        }
                    } else if (iSeekObjectToField2 == 1 || iSeekObjectToField2 == 2) {
                        extract(jSONPath, defaultJSONParser, context);
                    }
                }
            }
        }

        public void setValue(JSONPath jSONPath, Object obj, Object obj2) {
            if (this.deep) {
                jSONPath.deepSet(obj, this.propertyName, this.propertyNameHash, obj2);
            } else {
                jSONPath.setPropertyValue(obj, this.propertyName, this.propertyNameHash, obj2);
            }
        }

        public boolean remove(JSONPath jSONPath, Object obj) {
            return jSONPath.removePropertyValue(obj, this.propertyName, this.deep);
        }
    }

    static class MultiPropertySegment implements Segment {
        private final String[] propertyNames;
        private final long[] propertyNamesHash;

        public MultiPropertySegment(String[] strArr) {
            this.propertyNames = strArr;
            this.propertyNamesHash = new long[strArr.length];
            int i = 0;
            while (true) {
                long[] jArr = this.propertyNamesHash;
                if (i >= jArr.length) {
                    return;
                }
                jArr[i] = TypeUtils.fnv1a_64(strArr[i]);
                i++;
            }
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            ArrayList arrayList = new ArrayList(this.propertyNames.length);
            int i = 0;
            while (true) {
                String[] strArr = this.propertyNames;
                if (i >= strArr.length) {
                    return arrayList;
                }
                arrayList.add(jSONPath.getPropertyValue(obj2, strArr[i], this.propertyNamesHash[i]));
                i++;
            }
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            JSONArray jSONArray;
            Object objIntegerValue;
            JSONLexerBase jSONLexerBase = (JSONLexerBase) defaultJSONParser.lexer;
            if (context.object == null) {
                jSONArray = new JSONArray();
                context.object = jSONArray;
            } else {
                jSONArray = (JSONArray) context.object;
            }
            for (int size = jSONArray.size(); size < this.propertyNamesHash.length; size++) {
                jSONArray.add(null);
            }
            do {
                int iSeekObjectToField = jSONLexerBase.seekObjectToField(this.propertyNamesHash);
                if (jSONLexerBase.matchStat != 3) {
                    return;
                }
                switch (jSONLexerBase.token()) {
                    case 2:
                        objIntegerValue = jSONLexerBase.integerValue();
                        jSONLexerBase.nextToken(16);
                        break;
                    case 3:
                        objIntegerValue = jSONLexerBase.decimalValue();
                        jSONLexerBase.nextToken(16);
                        break;
                    case 4:
                        objIntegerValue = jSONLexerBase.stringVal();
                        jSONLexerBase.nextToken(16);
                        break;
                    default:
                        objIntegerValue = defaultJSONParser.parse();
                        break;
                }
                jSONArray.set(iSeekObjectToField, objIntegerValue);
            } while (jSONLexerBase.token() == 16);
        }
    }

    static class WildCardSegment implements Segment {
        public static final WildCardSegment instance = new WildCardSegment(false, false);
        public static final WildCardSegment instance_deep = new WildCardSegment(true, false);
        public static final WildCardSegment instance_deep_objectOnly = new WildCardSegment(true, true);
        private boolean deep;
        private boolean objectOnly;

        private WildCardSegment(boolean z, boolean z2) {
            this.deep = z;
            this.objectOnly = z2;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (!this.deep) {
                return jSONPath.getPropertyValues(obj2);
            }
            ArrayList arrayList = new ArrayList();
            jSONPath.deepGetPropertyValues(obj2, arrayList);
            return arrayList;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            if (context.eval) {
                Object obj = defaultJSONParser.parse();
                if (this.deep) {
                    ArrayList arrayList = new ArrayList();
                    if (this.objectOnly) {
                        jSONPath.deepGetObjects(obj, arrayList);
                    } else {
                        jSONPath.deepGetPropertyValues(obj, arrayList);
                    }
                    context.object = arrayList;
                    return;
                }
                if (obj instanceof JSONObject) {
                    Collection<?> collectionValues = ((JSONObject) obj).values();
                    JSONArray jSONArray = new JSONArray(collectionValues.size());
                    jSONArray.addAll(collectionValues);
                    context.object = jSONArray;
                    return;
                }
                if (obj instanceof JSONArray) {
                    context.object = obj;
                    return;
                }
            }
            throw new JSONException("TODO");
        }
    }

    static class ArrayAccessSegment implements Segment {
        private final int index;

        public ArrayAccessSegment(int i) {
            this.index = i;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.getArrayItem(obj2, this.index);
        }

        public boolean setValue(JSONPath jSONPath, Object obj, Object obj2) {
            return jSONPath.setArrayItem(jSONPath, obj, this.index, obj2);
        }

        public boolean remove(JSONPath jSONPath, Object obj) {
            return jSONPath.removeArrayItem(jSONPath, obj, this.index);
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            if (((JSONLexerBase) defaultJSONParser.lexer).seekArrayToItem(this.index) && context.eval) {
                context.object = defaultJSONParser.parse();
            }
        }
    }

    static class MultiIndexSegment implements Segment {
        private final int[] indexes;

        public MultiIndexSegment(int[] iArr) {
            this.indexes = iArr;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            JSONArray jSONArray = new JSONArray(this.indexes.length);
            int i = 0;
            while (true) {
                int[] iArr = this.indexes;
                if (i >= iArr.length) {
                    return jSONArray;
                }
                jSONArray.add(jSONPath.getArrayItem(obj2, iArr[i]));
                i++;
            }
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            if (context.eval) {
                Object obj = defaultJSONParser.parse();
                if (obj instanceof List) {
                    int[] iArr = this.indexes;
                    int[] iArr2 = new int[iArr.length];
                    System.arraycopy(iArr, 0, iArr2, 0, iArr2.length);
                    List list = (List) obj;
                    if (iArr2[0] >= 0) {
                        for (int size = list.size() - 1; size >= 0; size--) {
                            if (Arrays.binarySearch(iArr2, size) < 0) {
                                list.remove(size);
                            }
                        }
                        context.object = list;
                        return;
                    }
                }
            }
            throw new UnsupportedOperationException();
        }
    }

    static class RangeSegment implements Segment {
        private final int end;
        private final int start;
        private final int step;

        public RangeSegment(int i, int i2, int i3) {
            this.start = i;
            this.end = i2;
            this.step = i3;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            int iIntValue = SizeSegment.instance.eval(jSONPath, obj, obj2).intValue();
            int i = this.start;
            if (i < 0) {
                i += iIntValue;
            }
            int i2 = this.end;
            if (i2 < 0) {
                i2 += iIntValue;
            }
            int i3 = ((i2 - i) / this.step) + 1;
            if (i3 == -1) {
                return null;
            }
            ArrayList arrayList = new ArrayList(i3);
            while (i <= i2 && i < iIntValue) {
                arrayList.add(jSONPath.getArrayItem(obj2, i));
                i += this.step;
            }
            return arrayList;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            throw new UnsupportedOperationException();
        }
    }

    static class NotNullSegement extends PropertyFilter {
        public NotNullSegement(String str, boolean z) {
            super(str, z);
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            return jSONPath.getPropertyValue(obj3, this.propertyName, this.propertyNameHash) != null;
        }
    }

    static class NullSegement extends PropertyFilter {
        public NullSegement(String str, boolean z) {
            super(str, z);
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            return get(jSONPath, obj, obj3) == null;
        }
    }

    static class ValueSegment extends PropertyFilter {
        private boolean eq;
        private final Object value;

        public ValueSegment(String str, boolean z, Object obj, boolean z2) {
            super(str, z);
            this.eq = true;
            if (obj == null) {
                throw new IllegalArgumentException("value is null");
            }
            this.value = obj;
            this.eq = z2;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            boolean zEquals = this.value.equals(get(jSONPath, obj, obj3));
            return !this.eq ? !zEquals : zEquals;
        }
    }

    static class IntInSegement extends PropertyFilter {
        private final boolean not;
        private final long[] values;

        public IntInSegement(String str, boolean z, long[] jArr, boolean z2) {
            super(str, z);
            this.values = jArr;
            this.not = z2;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            if (obj4 == null) {
                return false;
            }
            if (obj4 instanceof Number) {
                long jLongExtractValue = TypeUtils.longExtractValue((Number) obj4);
                for (long j : this.values) {
                    if (j == jLongExtractValue) {
                        return !this.not;
                    }
                }
            }
            return this.not;
        }
    }

    static class IntBetweenSegement extends PropertyFilter {
        private final long endValue;
        private final boolean not;
        private final long startValue;

        public IntBetweenSegement(String str, boolean z, long j, long j2, boolean z2) {
            super(str, z);
            this.startValue = j;
            this.endValue = j2;
            this.not = z2;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            if (obj4 == null) {
                return false;
            }
            if (obj4 instanceof Number) {
                long jLongExtractValue = TypeUtils.longExtractValue((Number) obj4);
                if (jLongExtractValue >= this.startValue && jLongExtractValue <= this.endValue) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }

    static class IntObjInSegement extends PropertyFilter {
        private final boolean not;
        private final Long[] values;

        public IntObjInSegement(String str, boolean z, Long[] lArr, boolean z2) {
            super(str, z);
            this.values = lArr;
            this.not = z2;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            int i = 0;
            if (obj4 == null) {
                Long[] lArr = this.values;
                int length = lArr.length;
                while (i < length) {
                    if (lArr[i] == null) {
                        return !this.not;
                    }
                    i++;
                }
                return this.not;
            }
            if (obj4 instanceof Number) {
                long jLongExtractValue = TypeUtils.longExtractValue((Number) obj4);
                Long[] lArr2 = this.values;
                int length2 = lArr2.length;
                while (i < length2) {
                    Long l = lArr2[i];
                    if (l != null && l.longValue() == jLongExtractValue) {
                        return !this.not;
                    }
                    i++;
                }
            }
            return this.not;
        }
    }

    static class StringInSegement extends PropertyFilter {
        private final boolean not;
        private final String[] values;

        public StringInSegement(String str, boolean z, String[] strArr, boolean z2) {
            super(str, z);
            this.values = strArr;
            this.not = z2;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            for (String str : this.values) {
                if (str == obj4) {
                    return !this.not;
                }
                if (str != null && str.equals(obj4)) {
                    return !this.not;
                }
            }
            return this.not;
        }
    }

    static class IntOpSegement extends PropertyFilter {
        private final Operator op;
        private final long value;
        private BigDecimal valueDecimal;
        private Double valueDouble;
        private Float valueFloat;

        public IntOpSegement(String str, boolean z, long j, Operator operator) {
            super(str, z);
            this.value = j;
            this.op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            if (obj4 == null || !(obj4 instanceof Number)) {
                return false;
            }
            if (obj4 instanceof BigDecimal) {
                if (this.valueDecimal == null) {
                    this.valueDecimal = BigDecimal.valueOf(this.value);
                }
                int iCompareTo = this.valueDecimal.compareTo((BigDecimal) obj4);
                switch (this.op) {
                    case EQ:
                        if (iCompareTo == 0) {
                        }
                        break;
                    case NE:
                        if (iCompareTo != 0) {
                        }
                        break;
                    case GE:
                        if (iCompareTo <= 0) {
                        }
                        break;
                    case GT:
                        if (iCompareTo < 0) {
                        }
                        break;
                    case LE:
                        if (iCompareTo >= 0) {
                        }
                        break;
                    case LT:
                        if (iCompareTo > 0) {
                        }
                        break;
                }
                return false;
            }
            if (obj4 instanceof Float) {
                if (this.valueFloat == null) {
                    this.valueFloat = Float.valueOf(this.value);
                }
                int iCompareTo2 = this.valueFloat.compareTo((Float) obj4);
                switch (this.op) {
                    case EQ:
                        if (iCompareTo2 == 0) {
                        }
                        break;
                    case NE:
                        if (iCompareTo2 != 0) {
                        }
                        break;
                    case GE:
                        if (iCompareTo2 <= 0) {
                        }
                        break;
                    case GT:
                        if (iCompareTo2 < 0) {
                        }
                        break;
                    case LE:
                        if (iCompareTo2 >= 0) {
                        }
                        break;
                    case LT:
                        if (iCompareTo2 > 0) {
                        }
                        break;
                }
                return false;
            }
            if (obj4 instanceof Double) {
                if (this.valueDouble == null) {
                    this.valueDouble = Double.valueOf(this.value);
                }
                int iCompareTo3 = this.valueDouble.compareTo((Double) obj4);
                switch (this.op) {
                    case EQ:
                        if (iCompareTo3 == 0) {
                        }
                        break;
                    case NE:
                        if (iCompareTo3 != 0) {
                        }
                        break;
                    case GE:
                        if (iCompareTo3 <= 0) {
                        }
                        break;
                    case GT:
                        if (iCompareTo3 < 0) {
                        }
                        break;
                    case LE:
                        if (iCompareTo3 >= 0) {
                        }
                        break;
                    case LT:
                        if (iCompareTo3 > 0) {
                        }
                        break;
                }
                return false;
            }
            long jLongExtractValue = TypeUtils.longExtractValue((Number) obj4);
            switch (this.op) {
                case EQ:
                    if (jLongExtractValue == this.value) {
                    }
                    break;
                case NE:
                    if (jLongExtractValue != this.value) {
                    }
                    break;
                case GE:
                    if (jLongExtractValue >= this.value) {
                    }
                    break;
                case GT:
                    if (jLongExtractValue > this.value) {
                    }
                    break;
                case LE:
                    if (jLongExtractValue <= this.value) {
                    }
                    break;
                case LT:
                    if (jLongExtractValue < this.value) {
                    }
                    break;
            }
            return false;
        }
    }

    static abstract class PropertyFilter implements Filter {
        static long TYPE = TypeUtils.fnv1a_64("type");
        protected final boolean function;
        protected Segment functionExpr;
        protected final String propertyName;
        protected final long propertyNameHash;

        protected PropertyFilter(String str, boolean z) {
            this.propertyName = str;
            this.propertyNameHash = TypeUtils.fnv1a_64(str);
            this.function = z;
            if (z) {
                long j = this.propertyNameHash;
                if (j == TYPE) {
                    this.functionExpr = TypeSegment.instance;
                } else {
                    if (j == JSONPath.SIZE) {
                        this.functionExpr = SizeSegment.instance;
                        return;
                    }
                    throw new JSONPathException("unsupported funciton : " + str);
                }
            }
        }

        protected Object get(JSONPath jSONPath, Object obj, Object obj2) {
            Segment segment = this.functionExpr;
            if (segment != null) {
                return segment.eval(jSONPath, obj, obj2);
            }
            return jSONPath.getPropertyValue(obj2, this.propertyName, this.propertyNameHash);
        }
    }

    static class DoubleOpSegement extends PropertyFilter {
        private final Operator op;
        private final double value;

        public DoubleOpSegement(String str, boolean z, double d2, Operator operator) {
            super(str, z);
            this.value = d2;
            this.op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            if (obj4 == null || !(obj4 instanceof Number)) {
                return false;
            }
            double dDoubleValue = ((Number) obj4).doubleValue();
            switch (this.op) {
                case EQ:
                    if (dDoubleValue == this.value) {
                    }
                    break;
                case NE:
                    if (dDoubleValue != this.value) {
                    }
                    break;
                case GE:
                    if (dDoubleValue >= this.value) {
                    }
                    break;
                case GT:
                    if (dDoubleValue > this.value) {
                    }
                    break;
                case LE:
                    if (dDoubleValue <= this.value) {
                    }
                    break;
                case LT:
                    if (dDoubleValue < this.value) {
                    }
                    break;
            }
            return false;
        }
    }

    static class RefOpSegement extends PropertyFilter {
        private final Operator op;
        private final Segment refSgement;

        public RefOpSegement(String str, boolean z, Segment segment, Operator operator) {
            super(str, z);
            this.refSgement = segment;
            this.op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            if (obj4 == null || !(obj4 instanceof Number)) {
                return false;
            }
            Object objEval = this.refSgement.eval(jSONPath, obj, obj);
            if ((objEval instanceof Integer) || (objEval instanceof Long) || (objEval instanceof Short) || (objEval instanceof Byte)) {
                long jLongExtractValue = TypeUtils.longExtractValue((Number) objEval);
                if ((obj4 instanceof Integer) || (obj4 instanceof Long) || (obj4 instanceof Short) || (obj4 instanceof Byte)) {
                    long jLongExtractValue2 = TypeUtils.longExtractValue((Number) obj4);
                    switch (this.op) {
                        case EQ:
                            return jLongExtractValue2 == jLongExtractValue;
                        case NE:
                            return jLongExtractValue2 != jLongExtractValue;
                        case GE:
                            return jLongExtractValue2 >= jLongExtractValue;
                        case GT:
                            return jLongExtractValue2 > jLongExtractValue;
                        case LE:
                            return jLongExtractValue2 <= jLongExtractValue;
                        case LT:
                            return jLongExtractValue2 < jLongExtractValue;
                    }
                }
                if (obj4 instanceof BigDecimal) {
                    int iCompareTo = BigDecimal.valueOf(jLongExtractValue).compareTo((BigDecimal) obj4);
                    switch (this.op) {
                        case EQ:
                            return iCompareTo == 0;
                        case NE:
                            return iCompareTo != 0;
                        case GE:
                            return iCompareTo <= 0;
                        case GT:
                            return iCompareTo < 0;
                        case LE:
                            return iCompareTo >= 0;
                        case LT:
                            return iCompareTo > 0;
                        default:
                            return false;
                    }
                }
            }
            throw new UnsupportedOperationException();
        }
    }

    static class MatchSegement extends PropertyFilter {
        private final String[] containsValues;
        private final String endsWithValue;
        private final int minLength;
        private final boolean not;
        private final String startsWithValue;

        public MatchSegement(String str, boolean z, String str2, String str3, String[] strArr, boolean z2) {
            super(str, z);
            this.startsWithValue = str2;
            this.endsWithValue = str3;
            this.containsValues = strArr;
            this.not = z2;
            int length = str2 != null ? str2.length() + 0 : 0;
            length = str3 != null ? length + str3.length() : length;
            if (strArr != null) {
                for (String str4 : strArr) {
                    length += str4.length();
                }
            }
            this.minLength = length;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            int length;
            Object obj4 = get(jSONPath, obj, obj3);
            if (obj4 == null) {
                return false;
            }
            String string = obj4.toString();
            if (string.length() < this.minLength) {
                return this.not;
            }
            String str = this.startsWithValue;
            if (str == null) {
                length = 0;
            } else {
                if (!string.startsWith(str)) {
                    return this.not;
                }
                length = this.startsWithValue.length() + 0;
            }
            String[] strArr = this.containsValues;
            if (strArr != null) {
                for (String str2 : strArr) {
                    int iIndexOf = string.indexOf(str2, length);
                    if (iIndexOf == -1) {
                        return this.not;
                    }
                    length = iIndexOf + str2.length();
                }
            }
            String str3 = this.endsWithValue;
            if (str3 != null && !string.endsWith(str3)) {
                return this.not;
            }
            return !this.not;
        }
    }

    static class RlikeSegement extends PropertyFilter {
        private final boolean not;
        private final Pattern pattern;

        public RlikeSegement(String str, boolean z, String str2, boolean z2) {
            super(str, z);
            this.pattern = Pattern.compile(str2);
            this.not = z2;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            if (obj4 == null) {
                return false;
            }
            boolean zMatches = this.pattern.matcher(obj4.toString()).matches();
            return this.not ? !zMatches : zMatches;
        }
    }

    static class StringOpSegement extends PropertyFilter {
        private final Operator op;
        private final String value;

        public StringOpSegement(String str, boolean z, String str2, Operator operator) {
            super(str, z);
            this.value = str2;
            this.op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            if (this.op == Operator.EQ) {
                return this.value.equals(obj4);
            }
            if (this.op == Operator.NE) {
                return !this.value.equals(obj4);
            }
            if (obj4 == null) {
                return false;
            }
            int iCompareTo = this.value.compareTo(obj4.toString());
            return this.op == Operator.GE ? iCompareTo <= 0 : this.op == Operator.GT ? iCompareTo < 0 : this.op == Operator.LE ? iCompareTo >= 0 : this.op == Operator.LT && iCompareTo > 0;
        }
    }

    static class RegMatchSegement extends PropertyFilter {
        private final Operator op;
        private final Pattern pattern;

        public RegMatchSegement(String str, boolean z, Pattern pattern, Operator operator) {
            super(str, z);
            this.pattern = pattern;
            this.op = operator;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            Object obj4 = get(jSONPath, obj, obj3);
            if (obj4 == null) {
                return false;
            }
            return this.pattern.matcher(obj4.toString()).matches();
        }
    }

    public static class FilterSegment implements Segment {
        private final Filter filter;

        public FilterSegment(Filter filter) {
            this.filter = filter;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public Object eval(JSONPath jSONPath, Object obj, Object obj2) {
            if (obj2 == null) {
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            if (obj2 instanceof Iterable) {
                for (Object obj3 : (Iterable) obj2) {
                    if (this.filter.apply(jSONPath, obj, obj2, obj3)) {
                        jSONArray.add(obj3);
                    }
                }
                return jSONArray;
            }
            if (this.filter.apply(jSONPath, obj, obj2, obj2)) {
                return obj2;
            }
            return null;
        }

        @Override // com.alibaba.fastjson.JSONPath.Segment
        public void extract(JSONPath jSONPath, DefaultJSONParser defaultJSONParser, Context context) {
            Object obj = defaultJSONParser.parse();
            context.object = eval(jSONPath, obj, obj);
        }

        public boolean remove(JSONPath jSONPath, Object obj, Object obj2) {
            if (obj2 == null || !(obj2 instanceof Iterable)) {
                return false;
            }
            Iterator it = ((Iterable) obj2).iterator();
            while (it.hasNext()) {
                if (this.filter.apply(jSONPath, obj, obj2, it.next())) {
                    it.remove();
                }
            }
            return true;
        }
    }

    static class FilterGroup implements Filter {
        private boolean and;
        private List<Filter> fitlers = new ArrayList(2);

        public FilterGroup(Filter filter, Filter filter2, boolean z) {
            this.fitlers.add(filter);
            this.fitlers.add(filter2);
            this.and = z;
        }

        @Override // com.alibaba.fastjson.JSONPath.Filter
        public boolean apply(JSONPath jSONPath, Object obj, Object obj2, Object obj3) {
            if (this.and) {
                Iterator<Filter> it = this.fitlers.iterator();
                while (it.hasNext()) {
                    if (!it.next().apply(jSONPath, obj, obj2, obj3)) {
                        return false;
                    }
                }
                return true;
            }
            Iterator<Filter> it2 = this.fitlers.iterator();
            while (it2.hasNext()) {
                if (it2.next().apply(jSONPath, obj, obj2, obj3)) {
                    return true;
                }
            }
            return false;
        }
    }

    protected Object getArrayItem(Object obj, int i) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof List) {
            List list = (List) obj;
            if (i >= 0) {
                if (i < list.size()) {
                    return list.get(i);
                }
                return null;
            }
            if (Math.abs(i) <= list.size()) {
                return list.get(list.size() + i);
            }
            return null;
        }
        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            if (i >= 0) {
                if (i < length) {
                    return Array.get(obj, i);
                }
                return null;
            }
            if (Math.abs(i) <= length) {
                return Array.get(obj, length + i);
            }
            return null;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            Object obj2 = map.get(Integer.valueOf(i));
            return obj2 == null ? map.get(Integer.toString(i)) : obj2;
        }
        if (!(obj instanceof Collection)) {
            if (i == 0) {
                return obj;
            }
            throw new UnsupportedOperationException();
        }
        int i2 = 0;
        for (Object obj3 : (Collection) obj) {
            if (i2 == i) {
                return obj3;
            }
            i2++;
        }
        return null;
    }

    public boolean setArrayItem(JSONPath jSONPath, Object obj, int i, Object obj2) {
        if (obj instanceof List) {
            List list = (List) obj;
            if (i >= 0) {
                list.set(i, obj2);
            } else {
                list.set(list.size() + i, obj2);
            }
            return true;
        }
        Class<?> cls = obj.getClass();
        if (cls.isArray()) {
            int length = Array.getLength(obj);
            if (i >= 0) {
                if (i < length) {
                    Array.set(obj, i, obj2);
                }
            } else if (Math.abs(i) <= length) {
                Array.set(obj, length + i, obj2);
            }
            return true;
        }
        throw new JSONPathException("unsupported set operation." + cls);
    }

    public boolean removeArrayItem(JSONPath jSONPath, Object obj, int i) {
        if (obj instanceof List) {
            List list = (List) obj;
            if (i >= 0) {
                if (i >= list.size()) {
                    return false;
                }
                list.remove(i);
                return true;
            }
            int size = list.size() + i;
            if (size < 0) {
                return false;
            }
            list.remove(size);
            return true;
        }
        throw new JSONPathException("unsupported set operation." + obj.getClass());
    }

    protected Collection<Object> getPropertyValues(Object obj) {
        if (obj == null) {
            return null;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer != null) {
            try {
                return javaBeanSerializer.getFieldValues(obj);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path, e);
            }
        }
        if (obj instanceof Map) {
            return ((Map) obj).values();
        }
        if (obj instanceof Collection) {
            return (Collection) obj;
        }
        throw new UnsupportedOperationException();
    }

    protected void deepGetObjects(Object obj, List<Object> list) {
        Collection fieldValues;
        Class<?> cls = obj.getClass();
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(cls);
        if (javaBeanSerializer != null) {
            try {
                fieldValues = javaBeanSerializer.getFieldValues(obj);
                list.add(obj);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path, e);
            }
        } else if (obj instanceof Map) {
            list.add(obj);
            fieldValues = ((Map) obj).values();
        } else {
            fieldValues = obj instanceof Collection ? (Collection) obj : null;
        }
        if (fieldValues != null) {
            for (Object obj2 : fieldValues) {
                if (obj2 != null && !ParserConfig.isPrimitive2(obj2.getClass())) {
                    deepGetObjects(obj2, list);
                }
            }
            return;
        }
        throw new UnsupportedOperationException(cls.getName());
    }

    protected void deepGetPropertyValues(Object obj, List<Object> list) {
        Collection fieldValues;
        Class<?> cls = obj.getClass();
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(cls);
        if (javaBeanSerializer != null) {
            try {
                fieldValues = javaBeanSerializer.getFieldValues(obj);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path, e);
            }
        } else if (obj instanceof Map) {
            fieldValues = ((Map) obj).values();
        } else {
            fieldValues = obj instanceof Collection ? (Collection) obj : null;
        }
        if (fieldValues != null) {
            for (Object obj2 : fieldValues) {
                if (obj2 == null || ParserConfig.isPrimitive2(obj2.getClass())) {
                    list.add(obj2);
                } else {
                    deepGetPropertyValues(obj2, list);
                }
            }
            return;
        }
        throw new UnsupportedOperationException(cls.getName());
    }

    static boolean eq(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        if (obj.getClass() == obj2.getClass()) {
            return obj.equals(obj2);
        }
        if (obj instanceof Number) {
            if (obj2 instanceof Number) {
                return eqNotNull((Number) obj, (Number) obj2);
            }
            return false;
        }
        return obj.equals(obj2);
    }

    static boolean eqNotNull(Number number, Number number2) {
        Class<?> cls = number.getClass();
        boolean zIsInt = isInt(cls);
        Class<?> cls2 = number2.getClass();
        boolean zIsInt2 = isInt(cls2);
        if (number instanceof BigDecimal) {
            BigDecimal bigDecimal = (BigDecimal) number;
            if (zIsInt2) {
                return bigDecimal.equals(BigDecimal.valueOf(TypeUtils.longExtractValue(number2)));
            }
        }
        if (zIsInt) {
            if (zIsInt2) {
                return number.longValue() == number2.longValue();
            }
            if (number2 instanceof BigInteger) {
                return BigInteger.valueOf(number.longValue()).equals((BigInteger) number);
            }
        }
        if (zIsInt2 && (number instanceof BigInteger)) {
            return ((BigInteger) number).equals(BigInteger.valueOf(TypeUtils.longExtractValue(number2)));
        }
        boolean zIsDouble = isDouble(cls);
        boolean zIsDouble2 = isDouble(cls2);
        return ((zIsDouble && zIsDouble2) || ((zIsDouble && zIsInt2) || (zIsDouble2 && zIsInt))) && number.doubleValue() == number2.doubleValue();
    }

    protected static boolean isDouble(Class<?> cls) {
        return cls == Float.class || cls == Double.class;
    }

    protected static boolean isInt(Class<?> cls) {
        return cls == Byte.class || cls == Short.class || cls == Integer.class || cls == Long.class;
    }

    protected Object getPropertyValue(Object obj, String str, long j) {
        Object obj2;
        JSONArray jSONArray = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            try {
                obj2 = (JSONObject) JSON.parse((String) obj, this.parserConfig);
            } catch (Exception unused) {
                obj2 = obj;
            }
        } else {
            obj2 = obj;
        }
        if (obj2 instanceof Map) {
            Map map = (Map) obj2;
            Object obj3 = map.get(str);
            return obj3 == null ? (SIZE == j || LENGTH == j) ? Integer.valueOf(map.size()) : obj3 : obj3;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj2.getClass());
        if (javaBeanSerializer != null) {
            try {
                return javaBeanSerializer.getFieldValue(obj2, str, j, false);
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str, e);
            }
        }
        int i = 0;
        if (obj2 instanceof List) {
            List list = (List) obj2;
            if (SIZE == j || LENGTH == j) {
                return Integer.valueOf(list.size());
            }
            while (i < list.size()) {
                Object obj4 = list.get(i);
                if (obj4 == list) {
                    if (jSONArray == null) {
                        jSONArray = new JSONArray(list.size());
                    }
                    jSONArray.add(obj4);
                } else {
                    Object propertyValue = getPropertyValue(obj4, str, j);
                    if (propertyValue instanceof Collection) {
                        Collection collection = (Collection) propertyValue;
                        if (jSONArray == null) {
                            jSONArray = new JSONArray(list.size());
                        }
                        jSONArray.addAll(collection);
                    } else if (propertyValue != null || !this.ignoreNullValue) {
                        if (jSONArray == null) {
                            jSONArray = new JSONArray(list.size());
                        }
                        jSONArray.add(propertyValue);
                    }
                }
                i++;
            }
            return jSONArray == null ? Collections.emptyList() : jSONArray;
        }
        if (obj2 instanceof Object[]) {
            Object[] objArr = (Object[]) obj2;
            if (SIZE == j || LENGTH == j) {
                return Integer.valueOf(objArr.length);
            }
            JSONArray jSONArray2 = new JSONArray(objArr.length);
            while (i < objArr.length) {
                Object[] objArr2 = objArr[i];
                if (objArr2 == objArr) {
                    jSONArray2.add(objArr2);
                } else {
                    Object propertyValue2 = getPropertyValue(objArr2, str, j);
                    if (propertyValue2 instanceof Collection) {
                        jSONArray2.addAll((Collection) propertyValue2);
                    } else if (propertyValue2 != null || !this.ignoreNullValue) {
                        jSONArray2.add(propertyValue2);
                    }
                }
                i++;
            }
            return jSONArray2;
        }
        if (obj2 instanceof Enum) {
            Enum r9 = (Enum) obj2;
            if (-4270347329889690746L == j) {
                return r9.name();
            }
            if (-1014497654951707614L == j) {
                return Integer.valueOf(r9.ordinal());
            }
        }
        if (obj2 instanceof Calendar) {
            Calendar calendar = (Calendar) obj2;
            if (8963398325558730460L == j) {
                return Integer.valueOf(calendar.get(1));
            }
            if (-811277319855450459L == j) {
                return Integer.valueOf(calendar.get(2));
            }
            if (-3851359326990528739L == j) {
                return Integer.valueOf(calendar.get(5));
            }
            if (4647432019745535567L == j) {
                return Integer.valueOf(calendar.get(11));
            }
            if (6607618197526598121L == j) {
                return Integer.valueOf(calendar.get(12));
            }
            if (-6586085717218287427L == j) {
                return Integer.valueOf(calendar.get(13));
            }
        }
        return null;
    }

    protected void deepScan(Object obj, String str, List<Object> list) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Map) {
            for (Map.Entry entry : ((Map) obj).entrySet()) {
                Object value = entry.getValue();
                if (str.equals(entry.getKey())) {
                    if (value instanceof Collection) {
                        list.addAll((Collection) value);
                    } else {
                        list.add(value);
                    }
                } else if (value != null && !ParserConfig.isPrimitive2(value.getClass())) {
                    deepScan(value, str, list);
                }
            }
            return;
        }
        if (obj instanceof Collection) {
            for (Object obj2 : (Collection) obj) {
                if (!ParserConfig.isPrimitive2(obj2.getClass())) {
                    deepScan(obj2, str, list);
                }
            }
            return;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer != null) {
            try {
                FieldSerializer fieldSerializer = javaBeanSerializer.getFieldSerializer(str);
                if (fieldSerializer != null) {
                    try {
                        try {
                            list.add(fieldSerializer.getPropertyValueDirect(obj));
                            return;
                        } catch (InvocationTargetException e) {
                            throw new JSONException("getFieldValue error." + str, e);
                        }
                    } catch (IllegalAccessException e2) {
                        throw new JSONException("getFieldValue error." + str, e2);
                    }
                }
                Iterator<Object> it = javaBeanSerializer.getFieldValues(obj).iterator();
                while (it.hasNext()) {
                    deepScan(it.next(), str, list);
                }
                return;
            } catch (Exception e3) {
                throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str, e3);
            }
        }
        if (obj instanceof List) {
            List list2 = (List) obj;
            for (int i = 0; i < list2.size(); i++) {
                deepScan(list2.get(i), str, list);
            }
        }
    }

    protected void deepSet(Object obj, String str, long j, Object obj2) {
        if (obj == null) {
            return;
        }
        if (obj instanceof Map) {
            Map map = (Map) obj;
            if (map.containsKey(str)) {
                map.get(str);
                map.put(str, obj2);
                return;
            } else {
                Iterator it = map.values().iterator();
                while (it.hasNext()) {
                    deepSet(it.next(), str, j, obj2);
                }
                return;
            }
        }
        Class<?> cls = obj.getClass();
        JavaBeanDeserializer javaBeanDeserializer = getJavaBeanDeserializer(cls);
        if (javaBeanDeserializer != null) {
            try {
                FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(str);
                if (fieldDeserializer != null) {
                    fieldDeserializer.setValue(obj, obj2);
                    return;
                }
                Iterator<Object> it2 = getJavaBeanSerializer(cls).getObjectFieldValues(obj).iterator();
                while (it2.hasNext()) {
                    deepSet(it2.next(), str, j, obj2);
                }
                return;
            } catch (Exception e) {
                throw new JSONPathException("jsonpath error, path " + this.path + ", segement " + str, e);
            }
        }
        if (obj instanceof List) {
            List list = (List) obj;
            for (int i = 0; i < list.size(); i++) {
                deepSet(list.get(i), str, j, obj2);
            }
        }
    }

    protected boolean setPropertyValue(Object obj, String str, long j, Object obj2) {
        if (obj instanceof Map) {
            ((Map) obj).put(str, obj2);
            return true;
        }
        if (obj instanceof List) {
            for (Object obj3 : (List) obj) {
                if (obj3 != null) {
                    setPropertyValue(obj3, str, j, obj2);
                }
            }
            return true;
        }
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer(obj.getClass());
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (javaBeanDeserializer != null) {
            FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(j);
            if (fieldDeserializer == null) {
                return false;
            }
            if (obj2 != null && obj2.getClass() != fieldDeserializer.fieldInfo.fieldClass) {
                obj2 = TypeUtils.cast(obj2, fieldDeserializer.fieldInfo.fieldType, this.parserConfig);
            }
            fieldDeserializer.setValue(obj, obj2);
            return true;
        }
        throw new UnsupportedOperationException();
    }

    protected boolean removePropertyValue(Object obj, String str, boolean z) {
        if (obj instanceof Map) {
            Map map = (Map) obj;
            z = map.remove(str) != null;
            if (z) {
                Iterator it = map.values().iterator();
                while (it.hasNext()) {
                    removePropertyValue(it.next(), str, z);
                }
            }
            return z;
        }
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer(obj.getClass());
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (javaBeanDeserializer == null) {
            if (z) {
                return false;
            }
            throw new UnsupportedOperationException();
        }
        FieldDeserializer fieldDeserializer = javaBeanDeserializer.getFieldDeserializer(str);
        if (fieldDeserializer != null) {
            fieldDeserializer.setValue(obj, (String) null);
        } else {
            z = false;
        }
        if (z) {
            for (Object obj2 : getPropertyValues(obj)) {
                if (obj2 != null) {
                    removePropertyValue(obj2, str, z);
                }
            }
        }
        return z;
    }

    protected JavaBeanSerializer getJavaBeanSerializer(Class<?> cls) {
        ObjectSerializer objectWriter = this.serializeConfig.getObjectWriter(cls);
        if (objectWriter instanceof JavaBeanSerializer) {
            return (JavaBeanSerializer) objectWriter;
        }
        return null;
    }

    protected JavaBeanDeserializer getJavaBeanDeserializer(Class<?> cls) {
        ObjectDeserializer deserializer = this.parserConfig.getDeserializer(cls);
        if (deserializer instanceof JavaBeanDeserializer) {
            return (JavaBeanDeserializer) deserializer;
        }
        return null;
    }

    int evalSize(Object obj) {
        if (obj == null) {
            return -1;
        }
        if (obj instanceof Collection) {
            return ((Collection) obj).size();
        }
        if (obj instanceof Object[]) {
            return ((Object[]) obj).length;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj);
        }
        if (obj instanceof Map) {
            int i = 0;
            Iterator it = ((Map) obj).values().iterator();
            while (it.hasNext()) {
                if (it.next() != null) {
                    i++;
                }
            }
            return i;
        }
        JavaBeanSerializer javaBeanSerializer = getJavaBeanSerializer(obj.getClass());
        if (javaBeanSerializer == null) {
            return -1;
        }
        try {
            return javaBeanSerializer.getSize(obj);
        } catch (Exception e) {
            throw new JSONPathException("evalSize error : " + this.path, e);
        }
    }

    Set<?> evalKeySet(Object obj) {
        JavaBeanSerializer javaBeanSerializer;
        if (obj == null) {
            return null;
        }
        if (obj instanceof Map) {
            return ((Map) obj).keySet();
        }
        if ((obj instanceof Collection) || (obj instanceof Object[]) || obj.getClass().isArray() || (javaBeanSerializer = getJavaBeanSerializer(obj.getClass())) == null) {
            return null;
        }
        try {
            return javaBeanSerializer.getFieldNames(obj);
        } catch (Exception e) {
            throw new JSONPathException("evalKeySet error : " + this.path, e);
        }
    }

    @Override // com.alibaba.fastjson.JSONAware
    public String toJSONString() {
        return JSON.toJSONString(this.path);
    }

    public static Object reserveToArray(Object obj, String... strArr) {
        JSONArray jSONArray = new JSONArray();
        if (strArr == null || strArr.length == 0) {
            return jSONArray;
        }
        for (String str : strArr) {
            JSONPath jSONPathCompile = compile(str);
            jSONPathCompile.init();
            jSONArray.add(jSONPathCompile.eval(obj));
        }
        return jSONArray;
    }

    public static Object reserveToObject(Object obj, String... strArr) {
        Object objEval;
        if (strArr == null || strArr.length == 0) {
            return obj;
        }
        JSONObject jSONObject = new JSONObject(true);
        for (String str : strArr) {
            JSONPath jSONPathCompile = compile(str);
            jSONPathCompile.init();
            Segment[] segmentArr = jSONPathCompile.segments;
            if ((segmentArr[segmentArr.length - 1] instanceof PropertySegment) && (objEval = jSONPathCompile.eval(obj)) != null) {
                jSONPathCompile.set(jSONObject, objEval);
            }
        }
        return jSONObject;
    }
}
