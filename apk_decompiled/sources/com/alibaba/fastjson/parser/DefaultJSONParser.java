package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.JSONPathException;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ResolveFieldDeserializer;
import com.alibaba.fastjson.serializer.BeanContext;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/* JADX INFO: loaded from: classes.dex */
public class DefaultJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    private static final Set<Class<?>> primitiveClasses = new HashSet();
    private String[] autoTypeAccept;
    private boolean autoTypeEnable;

    /* JADX INFO: renamed from: config, reason: collision with root package name */
    protected ParserConfig f2824config;
    protected ParseContext context;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private List<ExtraProcessor> extraProcessors;
    private List<ExtraTypeProvider> extraTypeProviders;
    protected FieldTypeResolver fieldTypeResolver;
    public final Object input;
    protected transient BeanContext lastBeanContext;
    public final JSONLexer lexer;
    private int objectKeyLevel;
    public int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    public final SymbolTable symbolTable;

    static {
        primitiveClasses.addAll(Arrays.asList(Boolean.TYPE, Byte.TYPE, Short.TYPE, Integer.TYPE, Long.TYPE, Float.TYPE, Double.TYPE, Boolean.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, BigInteger.class, BigDecimal.class, String.class));
    }

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat(this.dateFormatPattern, this.lexer.getLocale());
            this.dateFormat.setTimeZone(this.lexer.getTimeZone());
        }
        return this.dateFormat;
    }

    public void setDateFormat(String str) {
        this.dateFormatPattern = str;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat) {
        setDateFormat(dateFormat);
    }

    public void setDateFormat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DefaultJSONParser(String str) {
        this(str, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig) {
        this(str, new JSONScanner(str, JSON.DEFAULT_PARSER_FEATURE), parserConfig);
    }

    public DefaultJSONParser(String str, ParserConfig parserConfig, int i) {
        this(str, new JSONScanner(str, i), parserConfig);
    }

    public DefaultJSONParser(char[] cArr, int i, ParserConfig parserConfig, int i2) {
        this(cArr, new JSONScanner(cArr, i, i2), parserConfig);
    }

    public DefaultJSONParser(JSONLexer jSONLexer) {
        this(jSONLexer, ParserConfig.getGlobalInstance());
    }

    public DefaultJSONParser(JSONLexer jSONLexer, ParserConfig parserConfig) {
        this((Object) null, jSONLexer, parserConfig);
    }

    public DefaultJSONParser(Object obj, JSONLexer jSONLexer, ParserConfig parserConfig) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.fieldTypeResolver = null;
        this.objectKeyLevel = 0;
        this.autoTypeAccept = null;
        this.lexer = jSONLexer;
        this.input = obj;
        this.f2824config = parserConfig;
        this.symbolTable = parserConfig.symbolTable;
        char current = jSONLexer.getCurrent();
        if (current == '{') {
            jSONLexer.next();
            ((JSONLexerBase) jSONLexer).token = 12;
        } else if (current == '[') {
            jSONLexer.next();
            ((JSONLexerBase) jSONLexer).token = 14;
        } else {
            jSONLexer.nextToken();
        }
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String getInput() {
        Object obj = this.input;
        if (obj instanceof char[]) {
            return new String((char[]) obj);
        }
        return obj.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:143:0x02ad, code lost:
    
        r3.nextToken(16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:144:0x02b8, code lost:
    
        if (r3.token() != 13) goto L166;
     */
    /* JADX WARN: Code restructure failed: missing block: B:145:0x02ba, code lost:
    
        r3.nextToken(16);
     */
    /* JADX WARN: Code restructure failed: missing block: B:147:0x02c5, code lost:
    
        if ((r18.f2824config.getDeserializer(r6) instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) == false) goto L149;
     */
    /* JADX WARN: Code restructure failed: missing block: B:148:0x02c7, code lost:
    
        r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r19, (java.lang.Class<java.lang.Object>) r6, r18.f2824config);
     */
    /* JADX WARN: Code restructure failed: missing block: B:149:0x02ce, code lost:
    
        r0 = r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:150:0x02cf, code lost:
    
        if (r0 != null) goto L161;
     */
    /* JADX WARN: Code restructure failed: missing block: B:152:0x02d3, code lost:
    
        if (r6 != java.lang.Cloneable.class) goto L154;
     */
    /* JADX WARN: Code restructure failed: missing block: B:153:0x02d5, code lost:
    
        r0 = new java.util.HashMap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:155:0x02e1, code lost:
    
        if ("java.util.Collections$EmptyMap".equals(r5) == false) goto L157;
     */
    /* JADX WARN: Code restructure failed: missing block: B:156:0x02e3, code lost:
    
        r0 = java.util.Collections.emptyMap();
     */
    /* JADX WARN: Code restructure failed: missing block: B:158:0x02ee, code lost:
    
        if ("java.util.Collections$UnmodifiableMap".equals(r5) == false) goto L160;
     */
    /* JADX WARN: Code restructure failed: missing block: B:159:0x02f0, code lost:
    
        r0 = java.util.Collections.unmodifiableMap(new java.util.HashMap());
     */
    /* JADX WARN: Code restructure failed: missing block: B:160:0x02fa, code lost:
    
        r0 = r6.newInstance();
     */
    /* JADX WARN: Code restructure failed: missing block: B:161:0x02fe, code lost:
    
        setContext(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:162:0x0301, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:163:0x0302, code lost:
    
        r0 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:165:0x030a, code lost:
    
        throw new com.alibaba.fastjson.JSONException("create instance error", r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:166:0x030b, code lost:
    
        setResolveStatus(2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:167:0x0311, code lost:
    
        if (r18.context == null) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:168:0x0313, code lost:
    
        if (r20 == null) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:170:0x0317, code lost:
    
        if ((r20 instanceof java.lang.Integer) != false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:172:0x031f, code lost:
    
        if ((r18.context.fieldName instanceof java.lang.Integer) != false) goto L174;
     */
    /* JADX WARN: Code restructure failed: missing block: B:173:0x0321, code lost:
    
        popContext();
     */
    /* JADX WARN: Code restructure failed: missing block: B:175:0x0328, code lost:
    
        if (r19.size() <= 0) goto L179;
     */
    /* JADX WARN: Code restructure failed: missing block: B:176:0x032a, code lost:
    
        r0 = com.alibaba.fastjson.util.TypeUtils.cast((java.lang.Object) r19, (java.lang.Class<java.lang.Object>) r6, r18.f2824config);
        setResolveStatus(0);
        parseObject(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:177:0x0337, code lost:
    
        setContext(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:178:0x033a, code lost:
    
        return r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:179:0x033b, code lost:
    
        r0 = r18.f2824config.getDeserializer(r6);
        r3 = r0.getClass();
     */
    /* JADX WARN: Code restructure failed: missing block: B:180:0x034b, code lost:
    
        if (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class.isAssignableFrom(r3) == false) goto L186;
     */
    /* JADX WARN: Code restructure failed: missing block: B:182:0x034f, code lost:
    
        if (r3 == com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.class) goto L186;
     */
    /* JADX WARN: Code restructure failed: missing block: B:184:0x0353, code lost:
    
        if (r3 == com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer.class) goto L186;
     */
    /* JADX WARN: Code restructure failed: missing block: B:185:0x0355, code lost:
    
        setResolveStatus(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:187:0x035c, code lost:
    
        if ((r0 instanceof com.alibaba.fastjson.parser.deserializer.MapDeserializer) == false) goto L189;
     */
    /* JADX WARN: Code restructure failed: missing block: B:188:0x035e, code lost:
    
        setResolveStatus(0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:189:0x0362, code lost:
    
        r0 = r0.deserialze(r18, r6, r20);
     */
    /* JADX WARN: Code restructure failed: missing block: B:190:0x0366, code lost:
    
        setContext(r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:191:0x0369, code lost:
    
        return r0;
     */
    /* JADX WARN: Removed duplicated region for block: B:256:0x0486 A[Catch: all -> 0x06dc, TryCatch #1 {all -> 0x06dc, blocks: (B:30:0x007f, B:33:0x0092, B:36:0x00aa, B:111:0x0221, B:112:0x0227, B:114:0x0232, B:116:0x023a, B:120:0x0251, B:122:0x025f, B:142:0x02a3, B:143:0x02ad, B:145:0x02ba, B:146:0x02bd, B:148:0x02c7, B:153:0x02d5, B:154:0x02db, B:156:0x02e3, B:157:0x02e8, B:159:0x02f0, B:160:0x02fa, B:164:0x0303, B:165:0x030a, B:166:0x030b, B:169:0x0315, B:171:0x0319, B:173:0x0321, B:174:0x0324, B:176:0x032a, B:179:0x033b, B:185:0x0355, B:189:0x0362, B:186:0x035a, B:188:0x035e, B:123:0x0265, B:126:0x0271, B:130:0x027e, B:132:0x0284, B:136:0x028d, B:139:0x0293, B:198:0x0377, B:256:0x0486, B:258:0x048a, B:260:0x0490, B:262:0x0496, B:263:0x049a, B:268:0x04a4, B:274:0x04b7, B:276:0x04c6, B:278:0x04d1, B:279:0x04d9, B:280:0x04dc, B:290:0x0504, B:292:0x050d, B:294:0x0518, B:297:0x0528, B:298:0x054a, B:285:0x04e8, B:287:0x04f2, B:289:0x0501, B:288:0x04f7, B:301:0x054f, B:303:0x0559, B:305:0x0561, B:306:0x0564, B:308:0x056f, B:309:0x0573, B:311:0x057e, B:314:0x0585, B:317:0x0592, B:318:0x0599, B:321:0x059e, B:323:0x05a3, B:327:0x05af, B:329:0x05b7, B:331:0x05cc, B:335:0x05eb, B:337:0x05f3, B:340:0x05f9, B:342:0x05ff, B:344:0x0607, B:347:0x0619, B:350:0x0621, B:352:0x0625, B:353:0x062c, B:355:0x0631, B:356:0x0634, B:358:0x063c, B:361:0x0646, B:364:0x0650, B:365:0x0658, B:366:0x0660, B:367:0x067a, B:332:0x05d7, B:333:0x05de, B:368:0x067b, B:370:0x068d, B:373:0x0694, B:376:0x06a1, B:377:0x06c3, B:202:0x0381, B:204:0x0389, B:206:0x0393, B:208:0x03a4, B:210:0x03af, B:212:0x03b7, B:214:0x03bb, B:216:0x03c3, B:219:0x03c9, B:221:0x03cd, B:244:0x0436, B:246:0x043e, B:249:0x0447, B:250:0x0461, B:223:0x03d4, B:225:0x03dc, B:227:0x03e0, B:228:0x03e3, B:229:0x03ef, B:232:0x03f8, B:234:0x03fc, B:235:0x03ff, B:237:0x0403, B:238:0x0407, B:239:0x0414, B:241:0x041e, B:243:0x042b, B:251:0x0462, B:252:0x0480, B:39:0x00bc, B:40:0x00de, B:42:0x00e1, B:44:0x00ec, B:46:0x00f0, B:48:0x00f6, B:50:0x00fc, B:51:0x00ff, B:58:0x010e, B:60:0x0116, B:63:0x0128, B:64:0x0142, B:65:0x0143, B:66:0x014a, B:74:0x0159, B:75:0x015f, B:77:0x0166, B:79:0x016f, B:86:0x0181, B:89:0x018a, B:90:0x01a4, B:84:0x017c, B:78:0x016b, B:91:0x01a5, B:92:0x01bf, B:98:0x01c9, B:100:0x01d1, B:103:0x01e2, B:104:0x0204, B:105:0x0205, B:106:0x020c, B:107:0x020d, B:109:0x0217, B:378:0x06c4, B:379:0x06cb, B:380:0x06cc, B:381:0x06d3, B:382:0x06d4, B:383:0x06db), top: B:392:0x007f, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:272:0x04b3  */
    /* JADX WARN: Removed duplicated region for block: B:274:0x04b7 A[Catch: all -> 0x06dc, TryCatch #1 {all -> 0x06dc, blocks: (B:30:0x007f, B:33:0x0092, B:36:0x00aa, B:111:0x0221, B:112:0x0227, B:114:0x0232, B:116:0x023a, B:120:0x0251, B:122:0x025f, B:142:0x02a3, B:143:0x02ad, B:145:0x02ba, B:146:0x02bd, B:148:0x02c7, B:153:0x02d5, B:154:0x02db, B:156:0x02e3, B:157:0x02e8, B:159:0x02f0, B:160:0x02fa, B:164:0x0303, B:165:0x030a, B:166:0x030b, B:169:0x0315, B:171:0x0319, B:173:0x0321, B:174:0x0324, B:176:0x032a, B:179:0x033b, B:185:0x0355, B:189:0x0362, B:186:0x035a, B:188:0x035e, B:123:0x0265, B:126:0x0271, B:130:0x027e, B:132:0x0284, B:136:0x028d, B:139:0x0293, B:198:0x0377, B:256:0x0486, B:258:0x048a, B:260:0x0490, B:262:0x0496, B:263:0x049a, B:268:0x04a4, B:274:0x04b7, B:276:0x04c6, B:278:0x04d1, B:279:0x04d9, B:280:0x04dc, B:290:0x0504, B:292:0x050d, B:294:0x0518, B:297:0x0528, B:298:0x054a, B:285:0x04e8, B:287:0x04f2, B:289:0x0501, B:288:0x04f7, B:301:0x054f, B:303:0x0559, B:305:0x0561, B:306:0x0564, B:308:0x056f, B:309:0x0573, B:311:0x057e, B:314:0x0585, B:317:0x0592, B:318:0x0599, B:321:0x059e, B:323:0x05a3, B:327:0x05af, B:329:0x05b7, B:331:0x05cc, B:335:0x05eb, B:337:0x05f3, B:340:0x05f9, B:342:0x05ff, B:344:0x0607, B:347:0x0619, B:350:0x0621, B:352:0x0625, B:353:0x062c, B:355:0x0631, B:356:0x0634, B:358:0x063c, B:361:0x0646, B:364:0x0650, B:365:0x0658, B:366:0x0660, B:367:0x067a, B:332:0x05d7, B:333:0x05de, B:368:0x067b, B:370:0x068d, B:373:0x0694, B:376:0x06a1, B:377:0x06c3, B:202:0x0381, B:204:0x0389, B:206:0x0393, B:208:0x03a4, B:210:0x03af, B:212:0x03b7, B:214:0x03bb, B:216:0x03c3, B:219:0x03c9, B:221:0x03cd, B:244:0x0436, B:246:0x043e, B:249:0x0447, B:250:0x0461, B:223:0x03d4, B:225:0x03dc, B:227:0x03e0, B:228:0x03e3, B:229:0x03ef, B:232:0x03f8, B:234:0x03fc, B:235:0x03ff, B:237:0x0403, B:238:0x0407, B:239:0x0414, B:241:0x041e, B:243:0x042b, B:251:0x0462, B:252:0x0480, B:39:0x00bc, B:40:0x00de, B:42:0x00e1, B:44:0x00ec, B:46:0x00f0, B:48:0x00f6, B:50:0x00fc, B:51:0x00ff, B:58:0x010e, B:60:0x0116, B:63:0x0128, B:64:0x0142, B:65:0x0143, B:66:0x014a, B:74:0x0159, B:75:0x015f, B:77:0x0166, B:79:0x016f, B:86:0x0181, B:89:0x018a, B:90:0x01a4, B:84:0x017c, B:78:0x016b, B:91:0x01a5, B:92:0x01bf, B:98:0x01c9, B:100:0x01d1, B:103:0x01e2, B:104:0x0204, B:105:0x0205, B:106:0x020c, B:107:0x020d, B:109:0x0217, B:378:0x06c4, B:379:0x06cb, B:380:0x06cc, B:381:0x06d3, B:382:0x06d4, B:383:0x06db), top: B:392:0x007f, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:281:0x04e0  */
    /* JADX WARN: Removed duplicated region for block: B:292:0x050d A[Catch: all -> 0x06dc, TryCatch #1 {all -> 0x06dc, blocks: (B:30:0x007f, B:33:0x0092, B:36:0x00aa, B:111:0x0221, B:112:0x0227, B:114:0x0232, B:116:0x023a, B:120:0x0251, B:122:0x025f, B:142:0x02a3, B:143:0x02ad, B:145:0x02ba, B:146:0x02bd, B:148:0x02c7, B:153:0x02d5, B:154:0x02db, B:156:0x02e3, B:157:0x02e8, B:159:0x02f0, B:160:0x02fa, B:164:0x0303, B:165:0x030a, B:166:0x030b, B:169:0x0315, B:171:0x0319, B:173:0x0321, B:174:0x0324, B:176:0x032a, B:179:0x033b, B:185:0x0355, B:189:0x0362, B:186:0x035a, B:188:0x035e, B:123:0x0265, B:126:0x0271, B:130:0x027e, B:132:0x0284, B:136:0x028d, B:139:0x0293, B:198:0x0377, B:256:0x0486, B:258:0x048a, B:260:0x0490, B:262:0x0496, B:263:0x049a, B:268:0x04a4, B:274:0x04b7, B:276:0x04c6, B:278:0x04d1, B:279:0x04d9, B:280:0x04dc, B:290:0x0504, B:292:0x050d, B:294:0x0518, B:297:0x0528, B:298:0x054a, B:285:0x04e8, B:287:0x04f2, B:289:0x0501, B:288:0x04f7, B:301:0x054f, B:303:0x0559, B:305:0x0561, B:306:0x0564, B:308:0x056f, B:309:0x0573, B:311:0x057e, B:314:0x0585, B:317:0x0592, B:318:0x0599, B:321:0x059e, B:323:0x05a3, B:327:0x05af, B:329:0x05b7, B:331:0x05cc, B:335:0x05eb, B:337:0x05f3, B:340:0x05f9, B:342:0x05ff, B:344:0x0607, B:347:0x0619, B:350:0x0621, B:352:0x0625, B:353:0x062c, B:355:0x0631, B:356:0x0634, B:358:0x063c, B:361:0x0646, B:364:0x0650, B:365:0x0658, B:366:0x0660, B:367:0x067a, B:332:0x05d7, B:333:0x05de, B:368:0x067b, B:370:0x068d, B:373:0x0694, B:376:0x06a1, B:377:0x06c3, B:202:0x0381, B:204:0x0389, B:206:0x0393, B:208:0x03a4, B:210:0x03af, B:212:0x03b7, B:214:0x03bb, B:216:0x03c3, B:219:0x03c9, B:221:0x03cd, B:244:0x0436, B:246:0x043e, B:249:0x0447, B:250:0x0461, B:223:0x03d4, B:225:0x03dc, B:227:0x03e0, B:228:0x03e3, B:229:0x03ef, B:232:0x03f8, B:234:0x03fc, B:235:0x03ff, B:237:0x0403, B:238:0x0407, B:239:0x0414, B:241:0x041e, B:243:0x042b, B:251:0x0462, B:252:0x0480, B:39:0x00bc, B:40:0x00de, B:42:0x00e1, B:44:0x00ec, B:46:0x00f0, B:48:0x00f6, B:50:0x00fc, B:51:0x00ff, B:58:0x010e, B:60:0x0116, B:63:0x0128, B:64:0x0142, B:65:0x0143, B:66:0x014a, B:74:0x0159, B:75:0x015f, B:77:0x0166, B:79:0x016f, B:86:0x0181, B:89:0x018a, B:90:0x01a4, B:84:0x017c, B:78:0x016b, B:91:0x01a5, B:92:0x01bf, B:98:0x01c9, B:100:0x01d1, B:103:0x01e2, B:104:0x0204, B:105:0x0205, B:106:0x020c, B:107:0x020d, B:109:0x0217, B:378:0x06c4, B:379:0x06cb, B:380:0x06cc, B:381:0x06d3, B:382:0x06d4, B:383:0x06db), top: B:392:0x007f, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:345:0x0614  */
    /* JADX WARN: Removed duplicated region for block: B:347:0x0619 A[Catch: all -> 0x06dc, TryCatch #1 {all -> 0x06dc, blocks: (B:30:0x007f, B:33:0x0092, B:36:0x00aa, B:111:0x0221, B:112:0x0227, B:114:0x0232, B:116:0x023a, B:120:0x0251, B:122:0x025f, B:142:0x02a3, B:143:0x02ad, B:145:0x02ba, B:146:0x02bd, B:148:0x02c7, B:153:0x02d5, B:154:0x02db, B:156:0x02e3, B:157:0x02e8, B:159:0x02f0, B:160:0x02fa, B:164:0x0303, B:165:0x030a, B:166:0x030b, B:169:0x0315, B:171:0x0319, B:173:0x0321, B:174:0x0324, B:176:0x032a, B:179:0x033b, B:185:0x0355, B:189:0x0362, B:186:0x035a, B:188:0x035e, B:123:0x0265, B:126:0x0271, B:130:0x027e, B:132:0x0284, B:136:0x028d, B:139:0x0293, B:198:0x0377, B:256:0x0486, B:258:0x048a, B:260:0x0490, B:262:0x0496, B:263:0x049a, B:268:0x04a4, B:274:0x04b7, B:276:0x04c6, B:278:0x04d1, B:279:0x04d9, B:280:0x04dc, B:290:0x0504, B:292:0x050d, B:294:0x0518, B:297:0x0528, B:298:0x054a, B:285:0x04e8, B:287:0x04f2, B:289:0x0501, B:288:0x04f7, B:301:0x054f, B:303:0x0559, B:305:0x0561, B:306:0x0564, B:308:0x056f, B:309:0x0573, B:311:0x057e, B:314:0x0585, B:317:0x0592, B:318:0x0599, B:321:0x059e, B:323:0x05a3, B:327:0x05af, B:329:0x05b7, B:331:0x05cc, B:335:0x05eb, B:337:0x05f3, B:340:0x05f9, B:342:0x05ff, B:344:0x0607, B:347:0x0619, B:350:0x0621, B:352:0x0625, B:353:0x062c, B:355:0x0631, B:356:0x0634, B:358:0x063c, B:361:0x0646, B:364:0x0650, B:365:0x0658, B:366:0x0660, B:367:0x067a, B:332:0x05d7, B:333:0x05de, B:368:0x067b, B:370:0x068d, B:373:0x0694, B:376:0x06a1, B:377:0x06c3, B:202:0x0381, B:204:0x0389, B:206:0x0393, B:208:0x03a4, B:210:0x03af, B:212:0x03b7, B:214:0x03bb, B:216:0x03c3, B:219:0x03c9, B:221:0x03cd, B:244:0x0436, B:246:0x043e, B:249:0x0447, B:250:0x0461, B:223:0x03d4, B:225:0x03dc, B:227:0x03e0, B:228:0x03e3, B:229:0x03ef, B:232:0x03f8, B:234:0x03fc, B:235:0x03ff, B:237:0x0403, B:238:0x0407, B:239:0x0414, B:241:0x041e, B:243:0x042b, B:251:0x0462, B:252:0x0480, B:39:0x00bc, B:40:0x00de, B:42:0x00e1, B:44:0x00ec, B:46:0x00f0, B:48:0x00f6, B:50:0x00fc, B:51:0x00ff, B:58:0x010e, B:60:0x0116, B:63:0x0128, B:64:0x0142, B:65:0x0143, B:66:0x014a, B:74:0x0159, B:75:0x015f, B:77:0x0166, B:79:0x016f, B:86:0x0181, B:89:0x018a, B:90:0x01a4, B:84:0x017c, B:78:0x016b, B:91:0x01a5, B:92:0x01bf, B:98:0x01c9, B:100:0x01d1, B:103:0x01e2, B:104:0x0204, B:105:0x0205, B:106:0x020c, B:107:0x020d, B:109:0x0217, B:378:0x06c4, B:379:0x06cb, B:380:0x06cc, B:381:0x06d3, B:382:0x06d4, B:383:0x06db), top: B:392:0x007f, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:352:0x0625 A[Catch: all -> 0x06dc, TryCatch #1 {all -> 0x06dc, blocks: (B:30:0x007f, B:33:0x0092, B:36:0x00aa, B:111:0x0221, B:112:0x0227, B:114:0x0232, B:116:0x023a, B:120:0x0251, B:122:0x025f, B:142:0x02a3, B:143:0x02ad, B:145:0x02ba, B:146:0x02bd, B:148:0x02c7, B:153:0x02d5, B:154:0x02db, B:156:0x02e3, B:157:0x02e8, B:159:0x02f0, B:160:0x02fa, B:164:0x0303, B:165:0x030a, B:166:0x030b, B:169:0x0315, B:171:0x0319, B:173:0x0321, B:174:0x0324, B:176:0x032a, B:179:0x033b, B:185:0x0355, B:189:0x0362, B:186:0x035a, B:188:0x035e, B:123:0x0265, B:126:0x0271, B:130:0x027e, B:132:0x0284, B:136:0x028d, B:139:0x0293, B:198:0x0377, B:256:0x0486, B:258:0x048a, B:260:0x0490, B:262:0x0496, B:263:0x049a, B:268:0x04a4, B:274:0x04b7, B:276:0x04c6, B:278:0x04d1, B:279:0x04d9, B:280:0x04dc, B:290:0x0504, B:292:0x050d, B:294:0x0518, B:297:0x0528, B:298:0x054a, B:285:0x04e8, B:287:0x04f2, B:289:0x0501, B:288:0x04f7, B:301:0x054f, B:303:0x0559, B:305:0x0561, B:306:0x0564, B:308:0x056f, B:309:0x0573, B:311:0x057e, B:314:0x0585, B:317:0x0592, B:318:0x0599, B:321:0x059e, B:323:0x05a3, B:327:0x05af, B:329:0x05b7, B:331:0x05cc, B:335:0x05eb, B:337:0x05f3, B:340:0x05f9, B:342:0x05ff, B:344:0x0607, B:347:0x0619, B:350:0x0621, B:352:0x0625, B:353:0x062c, B:355:0x0631, B:356:0x0634, B:358:0x063c, B:361:0x0646, B:364:0x0650, B:365:0x0658, B:366:0x0660, B:367:0x067a, B:332:0x05d7, B:333:0x05de, B:368:0x067b, B:370:0x068d, B:373:0x0694, B:376:0x06a1, B:377:0x06c3, B:202:0x0381, B:204:0x0389, B:206:0x0393, B:208:0x03a4, B:210:0x03af, B:212:0x03b7, B:214:0x03bb, B:216:0x03c3, B:219:0x03c9, B:221:0x03cd, B:244:0x0436, B:246:0x043e, B:249:0x0447, B:250:0x0461, B:223:0x03d4, B:225:0x03dc, B:227:0x03e0, B:228:0x03e3, B:229:0x03ef, B:232:0x03f8, B:234:0x03fc, B:235:0x03ff, B:237:0x0403, B:238:0x0407, B:239:0x0414, B:241:0x041e, B:243:0x042b, B:251:0x0462, B:252:0x0480, B:39:0x00bc, B:40:0x00de, B:42:0x00e1, B:44:0x00ec, B:46:0x00f0, B:48:0x00f6, B:50:0x00fc, B:51:0x00ff, B:58:0x010e, B:60:0x0116, B:63:0x0128, B:64:0x0142, B:65:0x0143, B:66:0x014a, B:74:0x0159, B:75:0x015f, B:77:0x0166, B:79:0x016f, B:86:0x0181, B:89:0x018a, B:90:0x01a4, B:84:0x017c, B:78:0x016b, B:91:0x01a5, B:92:0x01bf, B:98:0x01c9, B:100:0x01d1, B:103:0x01e2, B:104:0x0204, B:105:0x0205, B:106:0x020c, B:107:0x020d, B:109:0x0217, B:378:0x06c4, B:379:0x06cb, B:380:0x06cc, B:381:0x06d3, B:382:0x06d4, B:383:0x06db), top: B:392:0x007f, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:355:0x0631 A[Catch: all -> 0x06dc, TryCatch #1 {all -> 0x06dc, blocks: (B:30:0x007f, B:33:0x0092, B:36:0x00aa, B:111:0x0221, B:112:0x0227, B:114:0x0232, B:116:0x023a, B:120:0x0251, B:122:0x025f, B:142:0x02a3, B:143:0x02ad, B:145:0x02ba, B:146:0x02bd, B:148:0x02c7, B:153:0x02d5, B:154:0x02db, B:156:0x02e3, B:157:0x02e8, B:159:0x02f0, B:160:0x02fa, B:164:0x0303, B:165:0x030a, B:166:0x030b, B:169:0x0315, B:171:0x0319, B:173:0x0321, B:174:0x0324, B:176:0x032a, B:179:0x033b, B:185:0x0355, B:189:0x0362, B:186:0x035a, B:188:0x035e, B:123:0x0265, B:126:0x0271, B:130:0x027e, B:132:0x0284, B:136:0x028d, B:139:0x0293, B:198:0x0377, B:256:0x0486, B:258:0x048a, B:260:0x0490, B:262:0x0496, B:263:0x049a, B:268:0x04a4, B:274:0x04b7, B:276:0x04c6, B:278:0x04d1, B:279:0x04d9, B:280:0x04dc, B:290:0x0504, B:292:0x050d, B:294:0x0518, B:297:0x0528, B:298:0x054a, B:285:0x04e8, B:287:0x04f2, B:289:0x0501, B:288:0x04f7, B:301:0x054f, B:303:0x0559, B:305:0x0561, B:306:0x0564, B:308:0x056f, B:309:0x0573, B:311:0x057e, B:314:0x0585, B:317:0x0592, B:318:0x0599, B:321:0x059e, B:323:0x05a3, B:327:0x05af, B:329:0x05b7, B:331:0x05cc, B:335:0x05eb, B:337:0x05f3, B:340:0x05f9, B:342:0x05ff, B:344:0x0607, B:347:0x0619, B:350:0x0621, B:352:0x0625, B:353:0x062c, B:355:0x0631, B:356:0x0634, B:358:0x063c, B:361:0x0646, B:364:0x0650, B:365:0x0658, B:366:0x0660, B:367:0x067a, B:332:0x05d7, B:333:0x05de, B:368:0x067b, B:370:0x068d, B:373:0x0694, B:376:0x06a1, B:377:0x06c3, B:202:0x0381, B:204:0x0389, B:206:0x0393, B:208:0x03a4, B:210:0x03af, B:212:0x03b7, B:214:0x03bb, B:216:0x03c3, B:219:0x03c9, B:221:0x03cd, B:244:0x0436, B:246:0x043e, B:249:0x0447, B:250:0x0461, B:223:0x03d4, B:225:0x03dc, B:227:0x03e0, B:228:0x03e3, B:229:0x03ef, B:232:0x03f8, B:234:0x03fc, B:235:0x03ff, B:237:0x0403, B:238:0x0407, B:239:0x0414, B:241:0x041e, B:243:0x042b, B:251:0x0462, B:252:0x0480, B:39:0x00bc, B:40:0x00de, B:42:0x00e1, B:44:0x00ec, B:46:0x00f0, B:48:0x00f6, B:50:0x00fc, B:51:0x00ff, B:58:0x010e, B:60:0x0116, B:63:0x0128, B:64:0x0142, B:65:0x0143, B:66:0x014a, B:74:0x0159, B:75:0x015f, B:77:0x0166, B:79:0x016f, B:86:0x0181, B:89:0x018a, B:90:0x01a4, B:84:0x017c, B:78:0x016b, B:91:0x01a5, B:92:0x01bf, B:98:0x01c9, B:100:0x01d1, B:103:0x01e2, B:104:0x0204, B:105:0x0205, B:106:0x020c, B:107:0x020d, B:109:0x0217, B:378:0x06c4, B:379:0x06cb, B:380:0x06cc, B:381:0x06d3, B:382:0x06d4, B:383:0x06db), top: B:392:0x007f, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:361:0x0646 A[Catch: all -> 0x06dc, TRY_ENTER, TryCatch #1 {all -> 0x06dc, blocks: (B:30:0x007f, B:33:0x0092, B:36:0x00aa, B:111:0x0221, B:112:0x0227, B:114:0x0232, B:116:0x023a, B:120:0x0251, B:122:0x025f, B:142:0x02a3, B:143:0x02ad, B:145:0x02ba, B:146:0x02bd, B:148:0x02c7, B:153:0x02d5, B:154:0x02db, B:156:0x02e3, B:157:0x02e8, B:159:0x02f0, B:160:0x02fa, B:164:0x0303, B:165:0x030a, B:166:0x030b, B:169:0x0315, B:171:0x0319, B:173:0x0321, B:174:0x0324, B:176:0x032a, B:179:0x033b, B:185:0x0355, B:189:0x0362, B:186:0x035a, B:188:0x035e, B:123:0x0265, B:126:0x0271, B:130:0x027e, B:132:0x0284, B:136:0x028d, B:139:0x0293, B:198:0x0377, B:256:0x0486, B:258:0x048a, B:260:0x0490, B:262:0x0496, B:263:0x049a, B:268:0x04a4, B:274:0x04b7, B:276:0x04c6, B:278:0x04d1, B:279:0x04d9, B:280:0x04dc, B:290:0x0504, B:292:0x050d, B:294:0x0518, B:297:0x0528, B:298:0x054a, B:285:0x04e8, B:287:0x04f2, B:289:0x0501, B:288:0x04f7, B:301:0x054f, B:303:0x0559, B:305:0x0561, B:306:0x0564, B:308:0x056f, B:309:0x0573, B:311:0x057e, B:314:0x0585, B:317:0x0592, B:318:0x0599, B:321:0x059e, B:323:0x05a3, B:327:0x05af, B:329:0x05b7, B:331:0x05cc, B:335:0x05eb, B:337:0x05f3, B:340:0x05f9, B:342:0x05ff, B:344:0x0607, B:347:0x0619, B:350:0x0621, B:352:0x0625, B:353:0x062c, B:355:0x0631, B:356:0x0634, B:358:0x063c, B:361:0x0646, B:364:0x0650, B:365:0x0658, B:366:0x0660, B:367:0x067a, B:332:0x05d7, B:333:0x05de, B:368:0x067b, B:370:0x068d, B:373:0x0694, B:376:0x06a1, B:377:0x06c3, B:202:0x0381, B:204:0x0389, B:206:0x0393, B:208:0x03a4, B:210:0x03af, B:212:0x03b7, B:214:0x03bb, B:216:0x03c3, B:219:0x03c9, B:221:0x03cd, B:244:0x0436, B:246:0x043e, B:249:0x0447, B:250:0x0461, B:223:0x03d4, B:225:0x03dc, B:227:0x03e0, B:228:0x03e3, B:229:0x03ef, B:232:0x03f8, B:234:0x03fc, B:235:0x03ff, B:237:0x0403, B:238:0x0407, B:239:0x0414, B:241:0x041e, B:243:0x042b, B:251:0x0462, B:252:0x0480, B:39:0x00bc, B:40:0x00de, B:42:0x00e1, B:44:0x00ec, B:46:0x00f0, B:48:0x00f6, B:50:0x00fc, B:51:0x00ff, B:58:0x010e, B:60:0x0116, B:63:0x0128, B:64:0x0142, B:65:0x0143, B:66:0x014a, B:74:0x0159, B:75:0x015f, B:77:0x0166, B:79:0x016f, B:86:0x0181, B:89:0x018a, B:90:0x01a4, B:84:0x017c, B:78:0x016b, B:91:0x01a5, B:92:0x01bf, B:98:0x01c9, B:100:0x01d1, B:103:0x01e2, B:104:0x0204, B:105:0x0205, B:106:0x020c, B:107:0x020d, B:109:0x0217, B:378:0x06c4, B:379:0x06cb, B:380:0x06cc, B:381:0x06d3, B:382:0x06d4, B:383:0x06db), top: B:392:0x007f, inners: #0, #2 }] */
    /* JADX WARN: Removed duplicated region for block: B:413:0x0516 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:416:0x063c A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.Object parseObject(java.util.Map r19, java.lang.Object r20) throws java.lang.Throwable {
        /*
            Method dump skipped, instruction units count: 1764
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.util.Map, java.lang.Object):java.lang.Object");
    }

    public ParserConfig getConfig() {
        return this.f2824config;
    }

    public void setConfig(ParserConfig parserConfig) {
        this.f2824config = parserConfig;
    }

    public <T> T parseObject(Class<T> cls) {
        return (T) parseObject(cls, (Object) null);
    }

    public <T> T parseObject(Type type) {
        return (T) parseObject(type, (Object) null);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> T parseObject(Type type, Object obj) {
        int i = this.lexer.token();
        if (i == 8) {
            this.lexer.nextToken();
            return (T) TypeUtils.optionalEmpty(type);
        }
        if (i == 4) {
            if (type == byte[].class) {
                T t = (T) this.lexer.bytesValue();
                this.lexer.nextToken();
                return t;
            }
            if (type == char[].class) {
                String strStringVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return (T) strStringVal.toCharArray();
            }
        }
        ObjectDeserializer deserializer = this.f2824config.getDeserializer(type);
        try {
            if (deserializer.getClass() == JavaBeanDeserializer.class) {
                if (this.lexer.token() != 12 && this.lexer.token() != 14) {
                    throw new JSONException("syntax error,expect start with { or [,but actually start with " + this.lexer.tokenName());
                }
                return (T) ((JavaBeanDeserializer) deserializer).deserialze(this, type, obj, 0);
            }
            return (T) deserializer.deserialze(this, type, obj);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable th) {
            throw new JSONException(th.getMessage(), th);
        }
    }

    public <T> List<T> parseArray(Class<T> cls) {
        ArrayList arrayList = new ArrayList();
        parseArray((Class<?>) cls, (Collection) arrayList);
        return arrayList;
    }

    public void parseArray(Class<?> cls, Collection collection) {
        parseArray((Type) cls, collection);
    }

    public void parseArray(Type type, Collection collection) {
        parseArray(type, collection, null);
    }

    public void parseArray(Type type, Collection collection, Object obj) {
        ObjectDeserializer deserializer;
        int i = this.lexer.token();
        if (i == 21 || i == 22) {
            this.lexer.nextToken();
            i = this.lexer.token();
        }
        if (i != 14) {
            throw new JSONException("field " + obj + " expect '[', but " + JSONToken.name(i) + ", " + this.lexer.info());
        }
        if (Integer.TYPE == type) {
            deserializer = IntegerCodec.instance;
            this.lexer.nextToken(2);
        } else if (String.class == type) {
            deserializer = StringCodec.instance;
            this.lexer.nextToken(4);
        } else {
            deserializer = this.f2824config.getDeserializer(type);
            this.lexer.nextToken(deserializer.getFastMatchToken());
        }
        ParseContext parseContext = this.context;
        setContext(collection, obj);
        int i2 = 0;
        while (true) {
            try {
                if (this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    while (this.lexer.token() == 16) {
                        this.lexer.nextToken();
                    }
                }
                if (this.lexer.token() != 15) {
                    Object objDeserialze = null;
                    if (Integer.TYPE == type) {
                        collection.add(IntegerCodec.instance.deserialze(this, null, null));
                    } else if (String.class == type) {
                        if (this.lexer.token() == 4) {
                            objDeserialze = this.lexer.stringVal();
                            this.lexer.nextToken(16);
                        } else {
                            Object obj2 = parse();
                            if (obj2 != null) {
                                objDeserialze = obj2.toString();
                            }
                        }
                        collection.add(objDeserialze);
                    } else {
                        if (this.lexer.token() == 8) {
                            this.lexer.nextToken();
                        } else {
                            objDeserialze = deserializer.deserialze(this, type, Integer.valueOf(i2));
                        }
                        collection.add(objDeserialze);
                        checkListResolve(collection);
                    }
                    if (this.lexer.token() == 16) {
                        this.lexer.nextToken(deserializer.getFastMatchToken());
                    }
                    i2++;
                } else {
                    setContext(parseContext);
                    this.lexer.nextToken(16);
                    return;
                }
            } catch (Throwable th) {
                setContext(parseContext);
                throw th;
            }
        }
    }

    public Object[] parseArray(Type[] typeArr) {
        Object objCast;
        Class<?> componentType;
        boolean zIsArray;
        Class cls;
        if (this.lexer.token() == 8) {
            this.lexer.nextToken(16);
            return null;
        }
        if (this.lexer.token() != 14) {
            throw new JSONException("syntax error : " + this.lexer.tokenName());
        }
        Object[] objArr = new Object[typeArr.length];
        if (typeArr.length == 0) {
            this.lexer.nextToken(15);
            if (this.lexer.token() != 15) {
                throw new JSONException("syntax error");
            }
            this.lexer.nextToken(16);
            return new Object[0];
        }
        this.lexer.nextToken(2);
        for (int i = 0; i < typeArr.length; i++) {
            if (this.lexer.token() == 8) {
                this.lexer.nextToken(16);
                objCast = null;
            } else {
                Type type = typeArr[i];
                if (type == Integer.TYPE || type == Integer.class) {
                    if (this.lexer.token() == 2) {
                        objCast = Integer.valueOf(this.lexer.intValue());
                        this.lexer.nextToken(16);
                    } else {
                        objCast = TypeUtils.cast(parse(), type, this.f2824config);
                    }
                } else if (type == String.class) {
                    if (this.lexer.token() == 4) {
                        objCast = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        objCast = TypeUtils.cast(parse(), type, this.f2824config);
                    }
                } else {
                    if (i == typeArr.length - 1 && (type instanceof Class) && !(((cls = (Class) type) == byte[].class || cls == char[].class) && this.lexer.token() == 4)) {
                        zIsArray = cls.isArray();
                        componentType = cls.getComponentType();
                    } else {
                        componentType = null;
                        zIsArray = false;
                    }
                    if (zIsArray && this.lexer.token() != 14) {
                        ArrayList arrayList = new ArrayList();
                        ObjectDeserializer deserializer = this.f2824config.getDeserializer(componentType);
                        int fastMatchToken = deserializer.getFastMatchToken();
                        if (this.lexer.token() != 15) {
                            while (true) {
                                arrayList.add(deserializer.deserialze(this, type, null));
                                if (this.lexer.token() != 16) {
                                    break;
                                }
                                this.lexer.nextToken(fastMatchToken);
                            }
                            if (this.lexer.token() != 15) {
                                throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                            }
                        }
                        objCast = TypeUtils.cast(arrayList, type, this.f2824config);
                    } else {
                        objCast = this.f2824config.getDeserializer(type).deserialze(this, type, Integer.valueOf(i));
                    }
                }
            }
            objArr[i] = objCast;
            if (this.lexer.token() == 15) {
                break;
            }
            if (this.lexer.token() != 16) {
                throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
            }
            if (i == typeArr.length - 1) {
                this.lexer.nextToken(15);
            } else {
                this.lexer.nextToken(2);
            }
        }
        if (this.lexer.token() != 15) {
            throw new JSONException("syntax error");
        }
        this.lexer.nextToken(16);
        return objArr;
    }

    public void parseObject(Object obj) {
        Object objDeserialze;
        Class<?> cls = obj.getClass();
        ObjectDeserializer deserializer = this.f2824config.getDeserializer(cls);
        JavaBeanDeserializer javaBeanDeserializer = deserializer instanceof JavaBeanDeserializer ? (JavaBeanDeserializer) deserializer : null;
        if (this.lexer.token() != 12 && this.lexer.token() != 16) {
            throw new JSONException("syntax error, expect {, actual " + this.lexer.tokenName());
        }
        while (true) {
            String strScanSymbol = this.lexer.scanSymbol(this.symbolTable);
            if (strScanSymbol == null) {
                if (this.lexer.token() == 13) {
                    this.lexer.nextToken(16);
                    return;
                } else if (this.lexer.token() != 16 || !this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                }
            }
            FieldDeserializer fieldDeserializer = javaBeanDeserializer != null ? javaBeanDeserializer.getFieldDeserializer(strScanSymbol) : null;
            if (fieldDeserializer == null) {
                if (!this.lexer.isEnabled(Feature.IgnoreNotMatch)) {
                    throw new JSONException("setter not found, class " + cls.getName() + ", property " + strScanSymbol);
                }
                this.lexer.nextTokenWithColon();
                parse();
                if (this.lexer.token() == 13) {
                    this.lexer.nextToken();
                    return;
                }
            } else {
                Class<?> cls2 = fieldDeserializer.fieldInfo.fieldClass;
                Type type = fieldDeserializer.fieldInfo.fieldType;
                if (cls2 == Integer.TYPE) {
                    this.lexer.nextTokenWithColon(2);
                    objDeserialze = IntegerCodec.instance.deserialze(this, type, null);
                } else if (cls2 == String.class) {
                    this.lexer.nextTokenWithColon(4);
                    objDeserialze = StringCodec.deserialze(this);
                } else if (cls2 == Long.TYPE) {
                    this.lexer.nextTokenWithColon(2);
                    objDeserialze = LongCodec.instance.deserialze(this, type, null);
                } else {
                    ObjectDeserializer deserializer2 = this.f2824config.getDeserializer(cls2, type);
                    this.lexer.nextTokenWithColon(deserializer2.getFastMatchToken());
                    objDeserialze = deserializer2.deserialze(this, type, null);
                }
                fieldDeserializer.setValue(obj, objDeserialze);
                if (this.lexer.token() != 16 && this.lexer.token() == 13) {
                    this.lexer.nextToken(16);
                    return;
                }
            }
        }
    }

    public Object parseArrayWithType(Type type) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        if (actualTypeArguments.length != 1) {
            throw new JSONException("not support type " + type);
        }
        Type type2 = actualTypeArguments[0];
        if (type2 instanceof Class) {
            ArrayList arrayList = new ArrayList();
            parseArray((Class<?>) type2, (Collection) arrayList);
            return arrayList;
        }
        if (type2 instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type2;
            Type type3 = wildcardType.getUpperBounds()[0];
            if (Object.class.equals(type3)) {
                if (wildcardType.getLowerBounds().length == 0) {
                    return parse();
                }
                throw new JSONException("not support type : " + type);
            }
            ArrayList arrayList2 = new ArrayList();
            parseArray((Class<?>) type3, (Collection) arrayList2);
            return arrayList2;
        }
        if (type2 instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) type2;
            Type[] bounds = typeVariable.getBounds();
            if (bounds.length != 1) {
                throw new JSONException("not support : " + typeVariable);
            }
            Type type4 = bounds[0];
            if (type4 instanceof Class) {
                ArrayList arrayList3 = new ArrayList();
                parseArray((Class<?>) type4, (Collection) arrayList3);
                return arrayList3;
            }
        }
        if (type2 instanceof ParameterizedType) {
            ArrayList arrayList4 = new ArrayList();
            parseArray((ParameterizedType) type2, arrayList4);
            return arrayList4;
        }
        throw new JSONException("TODO : " + type);
    }

    public void acceptType(String str) {
        JSONLexer jSONLexer = this.lexer;
        jSONLexer.nextTokenWithColon();
        if (jSONLexer.token() != 4) {
            throw new JSONException("type not match error");
        }
        if (str.equals(jSONLexer.stringVal())) {
            jSONLexer.nextToken();
            if (jSONLexer.token() == 16) {
                jSONLexer.nextToken();
                return;
            }
            return;
        }
        throw new JSONException("type not match error");
    }

    public int getResolveStatus() {
        return this.resolveStatus;
    }

    public void setResolveStatus(int i) {
        this.resolveStatus = i;
    }

    public Object getObject(String str) {
        for (int i = 0; i < this.contextArrayIndex; i++) {
            if (str.equals(this.contextArray[i].toString())) {
                return this.contextArray[i].object;
            }
        }
        return null;
    }

    public void checkListResolve(Collection collection) {
        if (this.resolveStatus == 1) {
            if (collection instanceof List) {
                int size = collection.size() - 1;
                ResolveTask lastResolveTask = getLastResolveTask();
                lastResolveTask.fieldDeserializer = new ResolveFieldDeserializer(this, (List) collection, size);
                lastResolveTask.ownerContext = this.context;
                setResolveStatus(0);
                return;
            }
            ResolveTask lastResolveTask2 = getLastResolveTask();
            lastResolveTask2.fieldDeserializer = new ResolveFieldDeserializer(collection);
            lastResolveTask2.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    public void checkMapResolve(Map map, Object obj) {
        if (this.resolveStatus == 1) {
            ResolveFieldDeserializer resolveFieldDeserializer = new ResolveFieldDeserializer(map, obj);
            ResolveTask lastResolveTask = getLastResolveTask();
            lastResolveTask.fieldDeserializer = resolveFieldDeserializer;
            lastResolveTask.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    public Object parseObject(Map map) {
        return parseObject(map, (Object) null);
    }

    public JSONObject parseObject() {
        Object object = parseObject((Map) new JSONObject(this.lexer.isEnabled(Feature.OrderedField)));
        if (object instanceof JSONObject) {
            return (JSONObject) object;
        }
        if (object == null) {
            return null;
        }
        return new JSONObject((Map<String, Object>) object);
    }

    public final void parseArray(Collection collection) {
        parseArray(collection, (Object) null);
    }

    public final void parseArray(Collection collection, Object obj) {
        Number numberDecimalValue;
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == 21 || jSONLexer.token() == 22) {
            jSONLexer.nextToken();
        }
        if (jSONLexer.token() != 14) {
            throw new JSONException("syntax error, expect [, actual " + JSONToken.name(jSONLexer.token()) + ", pos " + jSONLexer.pos() + ", fieldName " + obj);
        }
        jSONLexer.nextToken(4);
        ParseContext parseContext = this.context;
        if (parseContext != null && parseContext.level > 512) {
            throw new JSONException("array level > 512");
        }
        ParseContext parseContext2 = this.context;
        setContext(collection, obj);
        int i = 0;
        while (true) {
            try {
                try {
                    if (jSONLexer.isEnabled(Feature.AllowArbitraryCommas)) {
                        while (jSONLexer.token() == 16) {
                            jSONLexer.nextToken();
                        }
                    }
                    Object object = null;
                    object = null;
                    switch (jSONLexer.token()) {
                        case 2:
                            Number numberIntegerValue = jSONLexer.integerValue();
                            jSONLexer.nextToken(16);
                            object = numberIntegerValue;
                            break;
                        case 3:
                            if (jSONLexer.isEnabled(Feature.UseBigDecimal)) {
                                numberDecimalValue = jSONLexer.decimalValue(true);
                            } else {
                                numberDecimalValue = jSONLexer.decimalValue(false);
                            }
                            jSONLexer.nextToken(16);
                            object = numberDecimalValue;
                            break;
                        case 4:
                            String strStringVal = jSONLexer.stringVal();
                            jSONLexer.nextToken(16);
                            object = strStringVal;
                            if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                                JSONScanner jSONScanner = new JSONScanner(strStringVal);
                                Object time = strStringVal;
                                if (jSONScanner.scanISO8601DateIfMatch()) {
                                    time = jSONScanner.getCalendar().getTime();
                                }
                                jSONScanner.close();
                                object = time;
                            }
                            break;
                        case 6:
                            Boolean bool = Boolean.TRUE;
                            jSONLexer.nextToken(16);
                            object = bool;
                            break;
                        case 7:
                            Boolean bool2 = Boolean.FALSE;
                            jSONLexer.nextToken(16);
                            object = bool2;
                            break;
                        case 8:
                            jSONLexer.nextToken(4);
                            break;
                        case 12:
                            object = parseObject(new JSONObject(jSONLexer.isEnabled(Feature.OrderedField)), Integer.valueOf(i));
                            break;
                        case 14:
                            JSONArray jSONArray = new JSONArray();
                            parseArray(jSONArray, Integer.valueOf(i));
                            object = jSONArray;
                            if (jSONLexer.isEnabled(Feature.UseObjectArray)) {
                                object = jSONArray.toArray();
                            }
                            break;
                        case 15:
                            jSONLexer.nextToken(16);
                            return;
                        case 20:
                            throw new JSONException("unclosed jsonArray");
                        case 23:
                            jSONLexer.nextToken(4);
                            break;
                        default:
                            object = parse();
                            break;
                    }
                    collection.add(object);
                    checkListResolve(collection);
                    if (jSONLexer.token() == 16) {
                        jSONLexer.nextToken(4);
                    }
                    i++;
                } catch (ClassCastException e) {
                    throw new JSONException("unkown error", e);
                }
            } finally {
                setContext(parseContext2);
            }
        }
    }

    public ParseContext getContext() {
        return this.context;
    }

    public ParseContext getOwnerContext() {
        return this.context.parent;
    }

    public List<ResolveTask> getResolveTaskList() {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        return this.resolveTaskList;
    }

    public void addResolveTask(ResolveTask resolveTask) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(resolveTask);
    }

    public ResolveTask getLastResolveTask() {
        return this.resolveTaskList.get(r0.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public FieldTypeResolver getFieldTypeResolver() {
        return this.fieldTypeResolver;
    }

    public void setFieldTypeResolver(FieldTypeResolver fieldTypeResolver) {
        this.fieldTypeResolver = fieldTypeResolver;
    }

    public void setContext(ParseContext parseContext) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return;
        }
        this.context = parseContext;
    }

    public void popContext() {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return;
        }
        this.context = this.context.parent;
        int i = this.contextArrayIndex;
        if (i <= 0) {
            return;
        }
        this.contextArrayIndex = i - 1;
        this.contextArray[this.contextArrayIndex] = null;
    }

    public ParseContext setContext(Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        return setContext(this.context, obj, obj2);
    }

    public ParseContext setContext(ParseContext parseContext, Object obj, Object obj2) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        this.context = new ParseContext(parseContext, obj, obj2);
        addContext(this.context);
        return this.context;
    }

    private void addContext(ParseContext parseContext) {
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        ParseContext[] parseContextArr = this.contextArray;
        if (parseContextArr == null) {
            this.contextArray = new ParseContext[8];
        } else if (i >= parseContextArr.length) {
            ParseContext[] parseContextArr2 = new ParseContext[(parseContextArr.length * 3) / 2];
            System.arraycopy(parseContextArr, 0, parseContextArr2, 0, parseContextArr.length);
            this.contextArray = parseContextArr2;
        }
        this.contextArray[i] = parseContext;
    }

    public Object parse() {
        return parse(null);
    }

    public Object parseKey() {
        if (this.lexer.token() == 18) {
            String strStringVal = this.lexer.stringVal();
            this.lexer.nextToken(16);
            return strStringVal;
        }
        return parse(null);
    }

    public Object parse(Object obj) {
        Map jSONObject;
        JSONLexer jSONLexer = this.lexer;
        switch (jSONLexer.token()) {
            case 2:
                Number numberIntegerValue = jSONLexer.integerValue();
                jSONLexer.nextToken();
                return numberIntegerValue;
            case 3:
                Number numberDecimalValue = jSONLexer.decimalValue(jSONLexer.isEnabled(Feature.UseBigDecimal));
                jSONLexer.nextToken();
                return numberDecimalValue;
            case 4:
                String strStringVal = jSONLexer.stringVal();
                jSONLexer.nextToken(16);
                if (jSONLexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    JSONScanner jSONScanner = new JSONScanner(strStringVal);
                    try {
                        if (jSONScanner.scanISO8601DateIfMatch()) {
                            return jSONScanner.getCalendar().getTime();
                        }
                    } finally {
                        jSONScanner.close();
                    }
                }
                return strStringVal;
            case 5:
            case 10:
            case 11:
            case 13:
            case 15:
            case 16:
            case 17:
            case 19:
            case 24:
            case 25:
            default:
                throw new JSONException("syntax error, " + jSONLexer.info());
            case 6:
                jSONLexer.nextToken();
                return Boolean.TRUE;
            case 7:
                jSONLexer.nextToken();
                return Boolean.FALSE;
            case 8:
                jSONLexer.nextToken();
                return null;
            case 9:
                jSONLexer.nextToken(18);
                if (jSONLexer.token() != 18) {
                    throw new JSONException("syntax error");
                }
                jSONLexer.nextToken(10);
                accept(10);
                long jLongValue = jSONLexer.integerValue().longValue();
                accept(2);
                accept(11);
                return new Date(jLongValue);
            case 12:
                if (isEnabled(Feature.UseNativeJavaObject)) {
                    jSONObject = jSONLexer.isEnabled(Feature.OrderedField) ? new HashMap() : new LinkedHashMap();
                } else {
                    jSONObject = new JSONObject(jSONLexer.isEnabled(Feature.OrderedField));
                }
                return parseObject(jSONObject, obj);
            case 14:
                Collection arrayList = isEnabled(Feature.UseNativeJavaObject) ? new ArrayList() : new JSONArray();
                parseArray(arrayList, obj);
                return jSONLexer.isEnabled(Feature.UseObjectArray) ? arrayList.toArray() : arrayList;
            case 18:
                if ("NaN".equals(jSONLexer.stringVal())) {
                    jSONLexer.nextToken();
                    return null;
                }
                throw new JSONException("syntax error, " + jSONLexer.info());
            case 20:
                if (jSONLexer.isBlankInput()) {
                    return null;
                }
                throw new JSONException("unterminated json string, " + jSONLexer.info());
            case 21:
                jSONLexer.nextToken();
                HashSet hashSet = new HashSet();
                parseArray(hashSet, obj);
                return hashSet;
            case 22:
                jSONLexer.nextToken();
                TreeSet treeSet = new TreeSet();
                parseArray(treeSet, obj);
                return treeSet;
            case 23:
                jSONLexer.nextToken();
                return null;
            case 26:
                byte[] bArrBytesValue = jSONLexer.bytesValue();
                jSONLexer.nextToken();
                return bArrBytesValue;
        }
    }

    public void config(Feature feature, boolean z) {
        this.lexer.config(feature, z);
    }

    public boolean isEnabled(Feature feature) {
        return this.lexer.isEnabled(feature);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public final void accept(int i) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(jSONLexer.token()));
    }

    public final void accept(int i, int i2) {
        JSONLexer jSONLexer = this.lexer;
        if (jSONLexer.token() == i) {
            jSONLexer.nextToken(i2);
        } else {
            throwException(i);
        }
    }

    public void throwException(int i) {
        throw new JSONException("syntax error, expect " + JSONToken.name(i) + ", actual " + JSONToken.name(this.lexer.token()));
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        JSONLexer jSONLexer = this.lexer;
        try {
            if (jSONLexer.isEnabled(Feature.AutoCloseSource) && jSONLexer.token() != 20) {
                throw new JSONException("not close json text, token : " + JSONToken.name(jSONLexer.token()));
            }
        } finally {
            jSONLexer.close();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0022, code lost:
    
        return null;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object resolveReference(java.lang.String r5) {
        /*
            r4 = this;
            com.alibaba.fastjson.parser.ParseContext[] r0 = r4.contextArray
            r1 = 0
            if (r0 != 0) goto L6
            return r1
        L6:
            r0 = 0
        L7:
            com.alibaba.fastjson.parser.ParseContext[] r2 = r4.contextArray
            int r3 = r2.length
            if (r0 >= r3) goto L22
            int r3 = r4.contextArrayIndex
            if (r0 >= r3) goto L22
            r2 = r2[r0]
            java.lang.String r3 = r2.toString()
            boolean r3 = r3.equals(r5)
            if (r3 == 0) goto L1f
            java.lang.Object r5 = r2.object
            return r5
        L1f:
            int r0 = r0 + 1
            goto L7
        L22:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.resolveReference(java.lang.String):java.lang.Object");
    }

    public void handleResovleTask(Object obj) {
        Object objEval;
        List<ResolveTask> list = this.resolveTaskList;
        if (list == null) {
            return;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ResolveTask resolveTask = this.resolveTaskList.get(i);
            String str = resolveTask.referenceValue;
            Object obj2 = resolveTask.ownerContext != null ? resolveTask.ownerContext.object : null;
            if (str.startsWith("$")) {
                objEval = getObject(str);
                if (objEval == null) {
                    try {
                        JSONPath jSONPath = new JSONPath(str, SerializeConfig.getGlobalInstance(), this.f2824config, true);
                        if (jSONPath.isRef()) {
                            objEval = jSONPath.eval(obj);
                        }
                    } catch (JSONPathException unused) {
                    }
                }
            } else {
                objEval = resolveTask.context.object;
            }
            FieldDeserializer fieldDeserializer = resolveTask.fieldDeserializer;
            if (fieldDeserializer != null) {
                if (objEval != null && objEval.getClass() == JSONObject.class && fieldDeserializer.fieldInfo != null && !Map.class.isAssignableFrom(fieldDeserializer.fieldInfo.fieldClass)) {
                    Object obj3 = this.contextArray[0].object;
                    JSONPath jSONPathCompile = JSONPath.compile(str);
                    if (jSONPathCompile.isRef()) {
                        objEval = jSONPathCompile.eval(obj3);
                    }
                }
                if (fieldDeserializer.getOwnerClass() != null && !fieldDeserializer.getOwnerClass().isInstance(obj2) && resolveTask.ownerContext.parent != null) {
                    ParseContext parseContext = resolveTask.ownerContext;
                    while (true) {
                        parseContext = parseContext.parent;
                        if (parseContext != null) {
                            if (fieldDeserializer.getOwnerClass().isInstance(parseContext.object)) {
                                obj2 = parseContext.object;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                fieldDeserializer.setValue(obj2, objEval);
            }
        }
    }

    public static class ResolveTask {
        public final ParseContext context;
        public FieldDeserializer fieldDeserializer;
        public ParseContext ownerContext;
        public final String referenceValue;

        public ResolveTask(ParseContext parseContext, String str) {
            this.context = parseContext;
            this.referenceValue = str;
        }
    }

    public void parseExtra(Object obj, String str) {
        Object object;
        this.lexer.nextTokenWithColon();
        List<ExtraTypeProvider> list = this.extraTypeProviders;
        Type extraType = null;
        if (list != null) {
            Iterator<ExtraTypeProvider> it = list.iterator();
            while (it.hasNext()) {
                extraType = it.next().getExtraType(obj, str);
            }
        }
        if (extraType == null) {
            object = parse();
        } else {
            object = parseObject(extraType);
        }
        if (obj instanceof ExtraProcessable) {
            ((ExtraProcessable) obj).processExtra(str, object);
            return;
        }
        List<ExtraProcessor> list2 = this.extraProcessors;
        if (list2 != null) {
            Iterator<ExtraProcessor> it2 = list2.iterator();
            while (it2.hasNext()) {
                it2.next().processExtra(obj, str, object);
            }
        }
        if (this.resolveStatus == 1) {
            this.resolveStatus = 0;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:85:0x0238, code lost:
    
        return r10;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object parse(com.alibaba.fastjson.parser.deserializer.PropertyProcessable r10, java.lang.Object r11) {
        /*
            Method dump skipped, instruction units count: 619
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parse(com.alibaba.fastjson.parser.deserializer.PropertyProcessable, java.lang.Object):java.lang.Object");
    }
}
