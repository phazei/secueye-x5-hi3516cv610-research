package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.widget.SwitchCompat;
import bean.DeviceInfoBean;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import tools.LogEx;
import view.LongItemView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class CloudSettingsActivity extends CommonActivity {
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private TitleView fl_titlebar;
    private String iotId;
    private boolean isYunServiceEventPlanChecked;
    private LongItemView itemMyCloudStorage;
    private LongItemView itemYunPay;
    LinearLayout layout_main;
    private DeviceInfoBean nvrDevice;
    private SwitchCompat swYunServiceEventPlan;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_cloud_settings;
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.iotId = intent.getStringExtra("iotId");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
        }
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.itemMyCloudStorage = (LongItemView) findViewById(R.id.item_my_cloud_storage);
        this.itemYunPay = (LongItemView) findViewById(R.id.item_yun_pay);
        this.itemMyCloudStorage.addRightView(new SwitchCompat(this));
        this.swYunServiceEventPlan = (SwitchCompat) this.itemMyCloudStorage.getRightView();
        this.swYunServiceEventPlan.setTextOff("");
        this.swYunServiceEventPlan.setTextOn("");
        this.swYunServiceEventPlan.setText("");
        this.swYunServiceEventPlan.setThumbDrawable(null);
        this.swYunServiceEventPlan.setBackgroundResource(R.drawable.sel_switch);
        this.itemMyCloudStorage.setOnClickListener(new AnonymousClass1());
        this.fl_titlebar = (TitleView) findViewById(R.id.titleView);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.CloudSettingsActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                CloudSettingsActivity.this.finish();
            }
        });
        this.swYunServiceEventPlan.setOnClickListener(new View.OnClickListener() { // from class: activity.CloudSettingsActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (!(!CloudSettingsActivity.this.isYunServiceEventPlanChecked)) {
                    CloudSettingsActivity.this.showProgressDialog();
                    CloudSettingsActivity.this.unbindPlanToDevice();
                } else {
                    CloudSettingsActivity.this.showProgressDialog();
                    CloudSettingsActivity.this.triggerRecordPlan();
                }
            }
        });
        this.itemYunPay.setOnClickListener(new View.OnClickListener() { // from class: activity.CloudSettingsActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (CloudSettingsActivity.this.device2 == null) {
                    if (CloudSettingsActivity.this.device1 != null) {
                        Intent intent = new Intent(CloudSettingsActivity.this.getActivity(), (Class<?>) YunDoubleSelectActivity.class);
                        intent.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, CloudSettingsActivity.this.device);
                        intent.putExtra("device1", CloudSettingsActivity.this.device1);
                        intent.putExtra("nvrDevice", CloudSettingsActivity.this.nvrDevice);
                        CloudSettingsActivity.this.startActivity(intent);
                        return;
                    }
                    Intent intent2 = new Intent(CloudSettingsActivity.this.getActivity(), (Class<?>) PayYunServiceActivity2.class);
                    intent2.putExtra("iotId", CloudSettingsActivity.this.iotId);
                    intent2.putExtra(AlinkConstants.KEY_DN, CloudSettingsActivity.this.device.getDeviceName());
                    intent2.putExtra(AlinkConstants.KEY_PK, CloudSettingsActivity.this.device.getProductKey());
                    CloudSettingsActivity.this.startActivity(intent2);
                    return;
                }
                Intent intent3 = new Intent(CloudSettingsActivity.this.getActivity(), (Class<?>) YunThreeSelectActivity.class);
                intent3.putExtra(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, CloudSettingsActivity.this.device);
                intent3.putExtra("device1", CloudSettingsActivity.this.device1);
                intent3.putExtra("device2", CloudSettingsActivity.this.device2);
                intent3.putExtra("nvrDevice", CloudSettingsActivity.this.nvrDevice);
                CloudSettingsActivity.this.startActivity(intent3);
            }
        });
        showProgressDialog();
        getBindYunServicePlan(this.iotId, "", false);
    }

    /* JADX INFO: renamed from: activity.CloudSettingsActivity$1, reason: invalid class name */
    class AnonymousClass1 implements View.OnClickListener {
        AnonymousClass1() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view2) {
            HashMap map = new HashMap();
            map.put("iotId", "8NPmHYJZrRLM7bGngk7F000000");
            map.put(AlinkConstants.KEY_PAGE_NO, "0");
            map.put(AlinkConstants.KEY_PAGE_SIZE, "20");
            map.put("deviceNoOwner", true);
            new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/cloudstorage/order/query").setApiVersion("1.0.6").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudSettingsActivity.1.1
                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onFailure(IoTRequest ioTRequest, Exception exc) {
                    LogEx.d(true, CloudSettingsActivity.this.TAG, exc.getLocalizedMessage());
                    CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.1.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (CloudSettingsActivity.this.getActivity() == null || CloudSettingsActivity.this.getActivity().isFinishing()) {
                                return;
                            }
                            Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.receive_fail, 0).show();
                            CloudSettingsActivity.this.dismissProgressDialog();
                        }
                    });
                }

                @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                    LogEx.e(true, CloudSettingsActivity.this.TAG, "recvYunService:" + ioTResponse.getData() + "");
                    ioTResponse.getCode();
                    ioTResponse.getLocalizedMsg();
                }
            });
        }
    }

    public void triggerRecordPlan() {
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/query").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(new HashMap()).build(), new AnonymousClass5());
    }

    /* JADX INFO: renamed from: activity.CloudSettingsActivity$5, reason: invalid class name */
    class AnonymousClass5 implements IoTCallback {
        AnonymousClass5() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, CloudSettingsActivity.this.TAG, exc.getLocalizedMessage());
            CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.5.1
                @Override // java.lang.Runnable
                public void run() {
                    Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                    CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(false);
                    CloudSettingsActivity.this.dismissProgressDialog();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            LogEx.e(true, CloudSettingsActivity.this.TAG, "triggerRecordPlan:" + ioTResponse.getData() + "");
            int code = ioTResponse.getCode();
            ioTResponse.getLocalizedMsg();
            if (code != 200) {
                CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.5.2
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                        CloudSettingsActivity.this.dismissProgressDialog();
                    }
                });
                return;
            }
            Object data = ioTResponse.getData();
            if (data == null) {
                CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.5.3
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                        CloudSettingsActivity.this.dismissProgressDialog();
                    }
                });
                return;
            }
            JSONObject jSONObject = (JSONObject) data;
            int i = -1;
            try {
                i = jSONObject.getInt(AlinkConstants.KEY_TOTAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i == 0) {
                HashMap map = new HashMap();
                map.put("name", String.valueOf(System.currentTimeMillis()));
                map.put("allDay", 1);
                map.put("timeSectionList", new ArrayList());
                new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/set").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudSettingsActivity.5.4
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                        LogEx.d(true, CloudSettingsActivity.this.TAG, exc.getLocalizedMessage());
                        CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.5.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                                CloudSettingsActivity.this.dismissProgressDialog();
                            }
                        });
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                        LogEx.e(true, CloudSettingsActivity.this.TAG, "triggerRecordPlan:" + ioTResponse2.getData() + "");
                        int code2 = ioTResponse2.getCode();
                        ioTResponse2.getLocalizedMsg();
                        if (code2 != 200) {
                            CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.5.4.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                    CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                                    CloudSettingsActivity.this.dismissProgressDialog();
                                }
                            });
                            return;
                        }
                        Object data2 = ioTResponse2.getData();
                        if (data2 == null) {
                            CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.5.4.3
                                @Override // java.lang.Runnable
                                public void run() {
                                    CloudSettingsActivity.this.dismissProgressDialog();
                                }
                            });
                            return;
                        }
                        try {
                            CloudSettingsActivity.this.bindPlanToDevice(((JSONObject) data2).getString("planId"));
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.5.4.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                    CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                                    CloudSettingsActivity.this.dismissProgressDialog();
                                }
                            });
                        }
                    }
                });
                return;
            }
            try {
                JSONArray jSONArray = jSONObject.getJSONArray("recordPlanList");
                if (jSONArray == null || jSONArray.length() <= 0) {
                    throw new Exception();
                }
                if (jSONArray.length() > 0) {
                    CloudSettingsActivity.this.getBindYunServicePlan(CloudSettingsActivity.this.iotId, jSONArray.getJSONObject(0).getString("planId"), true);
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.5.5
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                        CloudSettingsActivity.this.dismissProgressDialog();
                    }
                });
            }
        }
    }

    public void getBindYunServicePlan(String str, final String str2, final boolean z) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/getbyiotid").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudSettingsActivity.6
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, CloudSettingsActivity.this.TAG, exc.getLocalizedMessage());
                CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        CloudSettingsActivity.this.dismissProgressDialog();
                        if (z) {
                            Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                        }
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, CloudSettingsActivity.this.TAG, "getBindYunServiceEventPlan:" + ioTResponse.getData() + "");
                int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    if (code == 9110 && z) {
                        CloudSettingsActivity.this.bindPlanToDevice(str2);
                    } else {
                        CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.6.2
                            @Override // java.lang.Runnable
                            public void run() {
                                CloudSettingsActivity.this.dismissProgressDialog();
                            }
                        });
                    }
                } else {
                    Object data = ioTResponse.getData();
                    if (data == null) {
                        CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.6.3
                            @Override // java.lang.Runnable
                            public void run() {
                                CloudSettingsActivity.this.dismissProgressDialog();
                            }
                        });
                        return;
                    }
                    try {
                        if (((JSONObject) data).getInt("allDay") == 0) {
                            throw new Exception();
                        }
                        CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.6.4
                            @Override // java.lang.Runnable
                            public void run() {
                                CloudSettingsActivity.this.isYunServiceEventPlanChecked = true;
                                CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(true);
                                CloudSettingsActivity.this.dismissProgressDialog();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (z) {
                            CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.6.5
                                @Override // java.lang.Runnable
                                public void run() {
                                    CloudSettingsActivity.this.isYunServiceEventPlanChecked = false;
                                    CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(false);
                                    CloudSettingsActivity.this.dismissProgressDialog();
                                }
                            });
                        } else {
                            CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.6.6
                                @Override // java.lang.Runnable
                                public void run() {
                                    CloudSettingsActivity.this.isYunServiceEventPlanChecked = false;
                                    CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(false);
                                }
                            });
                        }
                    }
                }
                if (z) {
                    return;
                }
                CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.6.7
                    @Override // java.lang.Runnable
                    public void run() {
                        CloudSettingsActivity.this.dismissProgressDialog();
                    }
                });
            }
        });
    }

    public void bindPlanToDevice(String str) {
        HashMap map = new HashMap();
        map.put("planId", str);
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/bind").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudSettingsActivity.7
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, CloudSettingsActivity.this.TAG, exc.getLocalizedMessage());
                CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.7.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                        CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                        CloudSettingsActivity.this.dismissProgressDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, CloudSettingsActivity.this.TAG, "bindPlanToDevice:" + ioTResponse.getData() + "");
                final int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code == 200) {
                    CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.7.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            CloudSettingsActivity.this.isYunServiceEventPlanChecked = true;
                            CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(true);
                        }
                    });
                }
                CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.7.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (code != 200) {
                            Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                        }
                        CloudSettingsActivity.this.dismissProgressDialog();
                    }
                });
            }
        });
    }

    public void unbindPlanToDevice() {
        HashMap map = new HashMap();
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/plan/unbind").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudSettingsActivity.8
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, CloudSettingsActivity.this.TAG, exc.getLocalizedMessage());
                CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.8.1
                    @Override // java.lang.Runnable
                    public void run() {
                        Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                        CloudSettingsActivity.this.dismissProgressDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, CloudSettingsActivity.this.TAG, "unbindPlanToDevice:" + ioTResponse.getData() + "");
                final int code = ioTResponse.getCode();
                if (code == 200) {
                    CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.8.2
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            CloudSettingsActivity.this.isYunServiceEventPlanChecked = false;
                            CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(false);
                        }
                    });
                }
                ioTResponse.getLocalizedMsg();
                CloudSettingsActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudSettingsActivity.8.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (code != 200) {
                            Toast.makeText(CloudSettingsActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            CloudSettingsActivity.this.swYunServiceEventPlan.setChecked(!CloudSettingsActivity.this.swYunServiceEventPlan.isChecked());
                        }
                        CloudSettingsActivity.this.dismissProgressDialog();
                    }
                });
            }
        });
    }
}
