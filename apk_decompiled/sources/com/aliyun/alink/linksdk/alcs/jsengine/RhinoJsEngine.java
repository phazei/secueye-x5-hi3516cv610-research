package com.aliyun.alink.linksdk.alcs.jsengine;

import com.aliyun.alink.linksdk.alcs.lpbs.component.jsengine.IJSEngine;
import com.aliyun.alink.linksdk.alcs.lpbs.utils.TextHelper;
import com.aliyun.alink.linksdk.tools.ALog;
import java.util.Arrays;
import org.json.JSONObject;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeJavaArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.json.JsonParser;

/* JADX INFO: loaded from: classes2.dex */
public class RhinoJsEngine implements IJSEngine {
    private static final String TAG = "[AlcsLPBS]RhinoJsEngine";

    private RhinoJsEngine() {
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.jsengine.IJSEngine
    public String rawDataToProtocol(String str, byte[] bArr) {
        ALog.d(TAG, "rawDataToProtocol  params:" + TextHelper.byte2hex(bArr));
        ALog.d(TAG, "rawDataToProtocol  params:" + Arrays.toString(bArr));
        try {
            Context contextEnter = Context.enter();
            contextEnter.setOptimizationLevel(-1);
            contextEnter.setLanguageVersion(200);
            ScriptableObject scriptableObjectInitStandardObjects = contextEnter.initStandardObjects();
            str = bArr != null ? new String(bArr, "UTF-8") : null;
            NativeObject nativeObject = (NativeObject) callJsFunction(contextEnter, scriptableObjectInitStandardObjects, str, IJSEngine.RAWDATA_TO_PROTOCAL_FUNCNAME, new Object[]{new NativeJavaArray(scriptableObjectInitStandardObjects, bArr)});
            if (nativeObject != null) {
                str = new JSONObject(nativeObject).toString();
            }
        } catch (Exception e) {
            ALog.e(TAG, "Exception:" + e.toString());
        } catch (Throwable th) {
            ALog.e(TAG, "Throwable:" + th.toString());
        }
        ALog.d(TAG, "rawDataToProtocol  result:" + str);
        Context.exit();
        return str;
    }

    @Override // com.aliyun.alink.linksdk.alcs.lpbs.component.jsengine.IJSEngine
    public byte[] protocolToRawData(String str, String str2) {
        ALog.d(TAG, "protocolToRawData  params:" + str2);
        try {
            Context contextEnter = Context.enter();
            contextEnter.setOptimizationLevel(-1);
            contextEnter.setLanguageVersion(200);
            ScriptableObject scriptableObjectInitStandardObjects = contextEnter.initStandardObjects();
            bytes = str2 != null ? str2.getBytes() : null;
            NativeArray nativeArray = (NativeArray) callJsFunction(contextEnter, scriptableObjectInitStandardObjects, str, IJSEngine.PROTOCAL_TO_RAWDATA_FUNCNAME, new Object[]{new JsonParser(contextEnter, scriptableObjectInitStandardObjects).parseValue(str2)});
            if (nativeArray != null) {
                bytes = new byte[nativeArray.size()];
                int size = nativeArray.size();
                for (int i = 0; i < size; i++) {
                    bytes[i] = (byte) ((Integer) nativeArray.get(i)).intValue();
                }
            }
        } catch (Exception e) {
            ALog.e(TAG, "Exception:" + e.toString());
        } catch (Throwable th) {
            ALog.e(TAG, "Throwable:" + th.toString());
        }
        Context.exit();
        return bytes;
    }

    public Object callJsFunction(Context context, ScriptableObject scriptableObject, String str, String str2, Object[] objArr) {
        context.evaluateString(scriptableObject, str, null, 1, null);
        return ((Function) scriptableObject.get(str2, scriptableObject)).call(context, scriptableObject, scriptableObject, objArr);
    }

    private static class InstanceHolder {
        private static RhinoJsEngine mInstance = new RhinoJsEngine();

        private InstanceHolder() {
        }
    }

    public static RhinoJsEngine getInstance() {
        return InstanceHolder.mInstance;
    }
}
