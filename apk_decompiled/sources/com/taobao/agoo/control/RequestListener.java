package com.taobao.agoo.control;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.sdk.android.error.ErrorCode;
import com.google.android.gms.common.internal.ServiceSpecificExtraArgs;
import com.taobao.accs.AccsErrorCode;
import com.taobao.accs.base.AccsAbstractDataListener;
import com.taobao.accs.base.TaoBaseService;
import com.taobao.accs.client.GlobalClientInfo;
import com.taobao.accs.common.Constants;
import com.taobao.accs.utl.ALog;
import com.taobao.accs.utl.JsonUtility;
import com.taobao.accs.utl.UtilityImpl;
import com.taobao.agoo.AgooErrorCode;
import com.taobao.agoo.ICallback;
import com.taobao.agoo.IListAliasCallback;
import com.taobao.agoo.IListAliasCallbackInner;
import com.taobao.agoo.IRegister;
import com.taobao.agoo.LocalStorage;
import com.taobao.agoo.TaobaoConstants;
import com.taobao.agoo.control.data.AliasDO;
import com.taobao.agoo.control.data.BaseDO;
import com.taobao.agoo.control.data.SwitchDO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.android.agoo.common.Config;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes3.dex */
public class RequestListener extends AccsAbstractDataListener {
    private static final String TAG = "RequestListener";
    public static AgooBindCache mAgooBindCache;
    public Map<String, ICallback> mListeners = new HashMap();

    @Override // com.taobao.accs.base.AccsAbstractDataListener, com.taobao.accs.base.AccsDataListener
    public void onBind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsDataListener
    public void onData(String str, String str2, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsAbstractDataListener, com.taobao.accs.base.AccsDataListener
    public void onSendData(String str, String str2, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    @Override // com.taobao.accs.base.AccsAbstractDataListener, com.taobao.accs.base.AccsDataListener
    public void onUnbind(String str, int i, TaoBaseService.ExtraInfo extraInfo) {
    }

    public RequestListener(Context context) {
        if (mAgooBindCache == null) {
            mAgooBindCache = new AgooBindCache(context.getApplicationContext());
        }
    }

    @Override // com.taobao.accs.base.AccsAbstractDataListener, com.taobao.accs.base.AccsDataListenerV2
    public void onResponse(String str, String str2, int i, String str3, byte[] bArr, TaoBaseService.ExtraInfo extraInfo) {
        String string;
        try {
            try {
                if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                    ICallback iCallback = this.mListeners.get(str2);
                    if (i == AccsErrorCode.SUCCESS.getCodeInt()) {
                        String str4 = new String(bArr, "utf-8");
                        ALog.i(TAG, "RequestListener onResponse", Constants.KEY_DATA_ID, str2, ServiceSpecificExtraArgs.CastExtraArgs.LISTENER, iCallback, "json", str4);
                        JSONObject jSONObject = new JSONObject(str4);
                        String string2 = JsonUtility.getString(jSONObject, BaseDO.JSON_ERRORCODE, null);
                        String string3 = JsonUtility.getString(jSONObject, BaseDO.JSON_CMD, null);
                        if (!"success".equals(string2)) {
                            if (iCallback != null) {
                                ErrorCode errorCodeBuild = AgooErrorCode.converAgooServerErrorCode(string2, string3 + "报错").build();
                                iCallback.onFailure(errorCodeBuild.getCode(), errorCodeBuild.getMsg());
                            }
                            if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                                this.mListeners.remove(str2);
                                return;
                            }
                            return;
                        }
                        if ("register".equals(string3)) {
                            String string4 = JsonUtility.getString(jSONObject, "deviceId", null);
                            if (!TextUtils.isEmpty(string4)) {
                                Config.setDeviceToken(GlobalClientInfo.getContext(), string4);
                                mAgooBindCache.onAgooRegister(GlobalClientInfo.getContext().getPackageName());
                                if (iCallback instanceof IRegister) {
                                    UtilityImpl.saveUtdid(Config.PREFERENCES, GlobalClientInfo.getContext());
                                    ((IRegister) iCallback).onSuccess(string4);
                                }
                            } else if (iCallback != null) {
                                ErrorCode errorCodeBuild2 = AgooErrorCode.converAgooServerErrorCode("success", string3 + "成功，但是未返回deviceId").detail(str4).build();
                                iCallback.onFailure(errorCodeBuild2.getCode(), errorCodeBuild2.getMsg());
                            }
                            if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                                this.mListeners.remove(str2);
                                return;
                            }
                            return;
                        }
                        if (AliasDO.JSON_CMD_ADDALIAS.equals(string3) && (string = JsonUtility.getString(jSONObject, "pushAliasToken", null)) != null && iCallback.extra != null) {
                            LocalStorage.saveAliasToken(GlobalClientInfo.getContext(), iCallback.extra, string);
                        }
                        if (AliasDO.JSON_CMD_REMOVEALIAS.equals(string3) && iCallback.extra != null) {
                            LocalStorage.saveAliasToken(GlobalClientInfo.getContext(), iCallback.extra, null);
                        }
                        if (!AliasDO.JSON_CMD_ADDALIAS.equals(string3) && !AliasDO.JSON_CMD_REMOVEALIAS.equals(string3) && !AliasDO.JSON_CMD_REMOVEALLALIAS.equals(string3) && !AliasDO.JSON_CMD_REMOVEALLALIASANDADDALIAS.equals(string3) && !AliasDO.JSON_CMD_RESETAlIASANDBINDCURRENT.equals(string3) && !AliasDO.JSON_CMD_RESETALIASDEVICEONE2ONE.equals(string3)) {
                            if (AliasDO.JSON_CMD_LISTALIAS.equals(string3)) {
                                handleListAlias(jSONObject, (IListAliasCallback) iCallback);
                                if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                                    this.mListeners.remove(str2);
                                    return;
                                }
                                return;
                            }
                            if ((SwitchDO.JSON_CMD_ENABLEPUSH.equals(string3) || SwitchDO.JSON_CMD_DISABLEPUSH.equals(string3)) && iCallback != null) {
                                iCallback.onSuccess();
                            }
                        }
                        if (iCallback != null) {
                            iCallback.onSuccess();
                        }
                        if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                            this.mListeners.remove(str2);
                            return;
                        }
                        return;
                    }
                    if (iCallback != null) {
                        ErrorCode errorCodeBuild3 = AgooErrorCode.converAccsErrorCode(i, str3).build();
                        iCallback.onFailure(errorCodeBuild3.getCode(), errorCodeBuild3.getMsg());
                    }
                }
                if (!TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                    return;
                }
            } catch (Throwable th) {
                ALog.e(TAG, "onResponse", th, new Object[0]);
                if (!TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                    return;
                }
            }
            this.mListeners.remove(str2);
        } catch (Throwable th2) {
            if (TaobaoConstants.SERVICE_ID_DEVICECMD.equals(str)) {
                this.mListeners.remove(str2);
            }
            throw th2;
        }
    }

    private void handleListAlias(JSONObject jSONObject, IListAliasCallback iListAliasCallback) {
        Map<String, String> map = JsonUtility.getMap(jSONObject, AliasDO.JSON_ALIAS_TOKEN_MAP);
        if (map == null) {
            map = new HashMap<>();
        }
        if (iListAliasCallback != null) {
            if (iListAliasCallback instanceof IListAliasCallbackInner) {
                ((IListAliasCallbackInner) iListAliasCallback).onSuccess(map);
            } else {
                iListAliasCallback.onSuccess(new ArrayList(map.keySet()));
            }
        }
    }
}
