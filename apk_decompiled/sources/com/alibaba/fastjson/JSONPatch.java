package com.alibaba.fastjson;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;

/* JADX INFO: loaded from: classes.dex */
public class JSONPatch {

    @JSONType(orders = {"op", "from", "path", "value"})
    public static class Operation {
        public String from;
        public String path;

        @JSONField(name = "op")
        public OperationType type;
        public Object value;
    }

    public enum OperationType {
        add,
        remove,
        replace,
        move,
        copy,
        test
    }

    public static String apply(String str, String str2) {
        return JSON.toJSONString(apply(JSON.parse(str, Feature.OrderedField), str2));
    }

    public static Object apply(Object obj, String str) {
        Operation[] operationArr;
        if (isObject(str)) {
            operationArr = new Operation[]{(Operation) JSON.parseObject(str, Operation.class)};
        } else {
            operationArr = (Operation[]) JSON.parseObject(str, Operation[].class);
        }
        for (Operation operation : operationArr) {
            JSONPath jSONPathCompile = JSONPath.compile(operation.path);
            switch (operation.type) {
                case add:
                    jSONPathCompile.patchAdd(obj, operation.value, false);
                    break;
                case replace:
                    jSONPathCompile.patchAdd(obj, operation.value, true);
                    break;
                case remove:
                    jSONPathCompile.remove(obj);
                    break;
                case copy:
                case move:
                    JSONPath jSONPathCompile2 = JSONPath.compile(operation.from);
                    Object objEval = jSONPathCompile2.eval(obj);
                    if (operation.type == OperationType.move && !jSONPathCompile2.remove(obj)) {
                        throw new JSONException("json patch move error : " + operation.from + " -> " + operation.path);
                    }
                    jSONPathCompile.set(obj, objEval);
                    break;
                    break;
                case test:
                    Object objEval2 = jSONPathCompile.eval(obj);
                    if (objEval2 == null) {
                        return Boolean.valueOf(operation.value == null);
                    }
                    return Boolean.valueOf(objEval2.equals(operation.value));
            }
        }
        return obj;
    }

    private static boolean isObject(String str) {
        if (str == null) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            char cCharAt = str.charAt(i);
            if (!JSONScanner.isWhitespace(cCharAt)) {
                return cCharAt == '{';
            }
        }
        return false;
    }
}
