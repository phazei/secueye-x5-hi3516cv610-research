package com.alibaba.sdk.android.openaccount.ui.ui;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import com.alibaba.sdk.android.openaccount.device.DeviceManager;
import com.alibaba.sdk.android.openaccount.message.MessageConstants;
import com.alibaba.sdk.android.openaccount.message.MessageUtils;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.CustomWidget;
import com.alibaba.sdk.android.openaccount.ui.LayoutMapping;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.callback.AccountDeviceCallback;
import com.alibaba.sdk.android.openaccount.ui.impl.OpenAccountUIServiceImpl;
import com.alibaba.sdk.android.openaccount.ui.model.AccountDevice;
import com.alibaba.sdk.android.openaccount.ui.widget.SwipeListView;
import com.alibaba.sdk.android.openaccount.util.CommonUtils;
import com.alibaba.sdk.android.openaccount.util.NetworkUtils;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import com.alibaba.sdk.android.pluto.annotation.Autowired;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class AccountDeviceActivity extends ActivityTemplate implements SwipeListView.AtomListListener {
    protected DeviceListAdapter deviceListAdapter;

    @Autowired
    protected DeviceManager deviceManager;
    protected ListView listView;
    protected Switch securitySwitchBtn;

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_device_manager";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setViewVariable();
        useCustomAttrs(this, this.attrs);
        if (NetworkUtils.isNetworkAvaiable(this)) {
            new GetAccountDeviceTask(this).execute(new Void[0]);
        } else {
            CommonUtils.toastNetworkError();
        }
    }

    @TargetApi(14)
    public void setViewVariable() {
        this.securitySwitchBtn = (Switch) findViewById("protect_switch");
        this.listView = (ListView) findViewById("device_list");
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.widget.SwipeListView.AtomListListener
    public void deleteItem(int i) {
        AccountDevice accountDevice = (AccountDevice) this.deviceListAdapter.getItem(i);
        if (accountDevice != null && accountDevice.isCurrentDevice) {
            CommonUtils.toast("ali_sdk_openaccount_text_device_not_del_current");
        } else if (NetworkUtils.isNetworkAvaiable(this)) {
            new DelelteDeviceTask(this, accountDevice, i).execute(new Void[0]);
        } else {
            CommonUtils.toastNetworkError();
        }
    }

    public void finishWithoutCallback() {
        super.finish();
    }

    protected class GetAccountDeviceTask extends TaskWithDialog<Void, Void, List<AccountDevice>> {
        public GetAccountDeviceTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public List<AccountDevice> asyncExecute(Void... voidArr) {
            RpcResponse rpcResponsePureInvokeWithRiskControlInfo = RpcUtils.pureInvokeWithRiskControlInfo("getAccountDeviceList", new HashMap(), "getaccountdevicelist");
            if (rpcResponsePureInvokeWithRiskControlInfo == null || rpcResponsePureInvokeWithRiskControlInfo.code != 1 || rpcResponsePureInvokeWithRiskControlInfo.arrayData == null) {
                StringBuilder sb = new StringBuilder();
                sb.append("fail to get device list ");
                sb.append(rpcResponsePureInvokeWithRiskControlInfo != null ? rpcResponsePureInvokeWithRiskControlInfo.toString() : " null response");
                AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, sb.toString());
                if (rpcResponsePureInvokeWithRiskControlInfo == null) {
                    CommonUtils.onFailure(OpenAccountUIServiceImpl._accountDeviceCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
                } else {
                    CommonUtils.onFailure(OpenAccountUIServiceImpl._accountDeviceCallback, rpcResponsePureInvokeWithRiskControlInfo.code, rpcResponsePureInvokeWithRiskControlInfo.message);
                }
                return Collections.emptyList();
            }
            try {
                JSONArray jSONArray = rpcResponsePureInvokeWithRiskControlInfo.arrayData;
                int length = jSONArray.length();
                if (length == 0) {
                    AliSDKLogger.w(OpenAccountUIConstants.LOG_TAG, " no device returned, something wrong happened ?");
                    CommonUtils.onFailure(OpenAccountUIServiceImpl._accountDeviceCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
                    return Collections.emptyList();
                }
                ArrayList arrayList = new ArrayList();
                String sdkDeviceId = AccountDeviceActivity.this.deviceManager.getSdkDeviceId();
                int i = 0;
                for (int i2 = 0; i2 < length; i2++) {
                    JSONObject jSONObject = jSONArray.getJSONObject(i2);
                    if (jSONObject != null) {
                        AccountDevice accountDevice = new AccountDevice();
                        accountDevice.gmtCreate = jSONObject.optString("loginTime");
                        accountDevice.deviceId = jSONObject.optString("deviceId");
                        if (!TextUtils.isEmpty(sdkDeviceId) && sdkDeviceId.equals(accountDevice.deviceId)) {
                            accountDevice.isCurrentDevice = true;
                            i = i2;
                        }
                        accountDevice.deviceName = jSONObject.optString("deviceModel");
                        accountDevice.id = Long.valueOf(jSONObject.optLong("id"));
                        arrayList.add(i2, accountDevice);
                    }
                }
                if (i != 0) {
                    AccountDevice accountDevice2 = (AccountDevice) arrayList.get(i);
                    arrayList.remove(i);
                    arrayList.add(0, accountDevice2);
                }
                this.executorService.postUITask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.AccountDeviceActivity.GetAccountDeviceTask.1
                    @Override // java.lang.Runnable
                    public void run() {
                        try {
                            AccountDeviceCallback accountDeviceCallback = OpenAccountUIServiceImpl._accountDeviceCallback;
                            if (accountDeviceCallback != null) {
                                accountDeviceCallback.onSuccess();
                            }
                        } catch (Exception unused) {
                        }
                    }
                });
                return arrayList;
            } catch (JSONException e) {
                AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, "fail to parse the response device list", e);
                CommonUtils.onFailure(OpenAccountUIServiceImpl._accountDeviceCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, e.getMessage()));
                return null;
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<AccountDevice> list) {
            if (list == null || list.size() <= 0) {
                return;
            }
            AccountDeviceActivity accountDeviceActivity = AccountDeviceActivity.this;
            accountDeviceActivity.deviceListAdapter = accountDeviceActivity.new DeviceListAdapter(accountDeviceActivity, list);
            AccountDeviceActivity.this.listView.setAdapter((ListAdapter) AccountDeviceActivity.this.deviceListAdapter);
        }

        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        protected void doWhenException(Throwable th) {
            AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, "fail to get account device list", th);
            CommonUtils.onFailure(OpenAccountUIServiceImpl._accountDeviceCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, th.getMessage()));
        }
    }

    protected class DelelteDeviceTask extends TaskWithDialog<Void, Void, Boolean> {
        private AccountDevice accountDevice;
        private int position;

        public DelelteDeviceTask(Activity activity2, AccountDevice accountDevice, int i) {
            super(activity2);
            this.accountDevice = accountDevice;
            this.position = i;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public Boolean asyncExecute(Void... voidArr) {
            HashMap map = new HashMap();
            map.put("deviceId", this.accountDevice.deviceId);
            RpcResponse rpcResponsePureInvokeWithRiskControlInfo = RpcUtils.pureInvokeWithRiskControlInfo("deleteDeviceRequst", map, "deletedevice");
            if (rpcResponsePureInvokeWithRiskControlInfo == null || rpcResponsePureInvokeWithRiskControlInfo.code != 1) {
                if (rpcResponsePureInvokeWithRiskControlInfo == null) {
                    CommonUtils.onFailure(OpenAccountUIServiceImpl._accountDeviceCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, new Object[0]));
                } else {
                    CommonUtils.onFailure(OpenAccountUIServiceImpl._accountDeviceCallback, rpcResponsePureInvokeWithRiskControlInfo.code, rpcResponsePureInvokeWithRiskControlInfo.message);
                }
                return false;
            }
            return true;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            if (bool != null && bool.booleanValue()) {
                AccountDeviceActivity.this.deviceListAdapter.removeItem(this.position);
                AccountDeviceActivity.this.listView.setAdapter((ListAdapter) AccountDeviceActivity.this.deviceListAdapter);
            }
        }

        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        protected void doWhenException(Throwable th) {
            AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, "fail to delete account device list", th);
            CommonUtils.onFailure(OpenAccountUIServiceImpl._accountDeviceCallback, MessageUtils.createMessage(MessageConstants.GENERIC_SYSTEM_ERROR, th.getMessage()));
        }
    }

    public class DeviceListAdapter extends BaseAdapter implements CustomWidget {
        private List<AccountDevice> accountDeviceList;
        private Context context;
        private String currentDeviceId;

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return 0L;
        }

        public DeviceListAdapter(Context context, List<AccountDevice> list) {
            this.accountDeviceList = list;
            this.context = context;
            this.currentDeviceId = AccountDeviceActivity.this.deviceManager.getSdkDeviceId();
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.CustomWidget
        public int getLayoutId(Context context) {
            if (LayoutMapping.hasCustomLayout(getClass())) {
                return LayoutMapping.get(getClass()).intValue();
            }
            return ResourceUtils.getRLayout(context, "ali_sdk_openaccount_device_item");
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.accountDeviceList.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.accountDeviceList.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view2, ViewGroup viewGroup) {
            if (view2 == null) {
                view2 = LayoutInflater.from(this.context).inflate(getLayoutId(this.context), (ViewGroup) null);
            }
            TextView textView = (TextView) view2.findViewById(ResourceUtils.getRId(this.context, "alisdk_openaccount_id_device_name"));
            TextView textView2 = (TextView) view2.findViewById(ResourceUtils.getRId(this.context, "alisdk_openaccount_id_gmt_create"));
            AccountDevice accountDevice = this.accountDeviceList.get(i);
            if (!TextUtils.isEmpty(accountDevice.deviceName) && accountDevice.deviceName.length() > 20) {
                textView.setText(accountDevice.deviceName.substring(0, 20));
            } else {
                textView.setText(accountDevice.deviceName);
            }
            textView2.setText(accountDevice.gmtCreate);
            TextView textView3 = (TextView) view2.findViewById(ResourceUtils.getRId(this.context, "alisdk_openaccount_id_current_device"));
            if (accountDevice.isCurrentDevice) {
                textView3.setVisibility(0);
            } else {
                textView3.setVisibility(8);
            }
            return view2;
        }

        public void removeItem(int i) {
            this.accountDeviceList.remove(i);
        }
    }
}
