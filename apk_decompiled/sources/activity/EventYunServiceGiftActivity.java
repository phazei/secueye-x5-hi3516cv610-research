package activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.seculink.app.R;
import config.APIConstants;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import tools.DateUtil;
import tools.LogEx;
import tools.OnMultiClickListener;
import tools.SystemUtil;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class EventYunServiceGiftActivity extends CommonActivity {
    TextView action_get;
    TextView check_order;
    private int consumed;
    private String endTime;
    private int expired;
    private String iotId;
    ImageView iv_banner;
    ImageView iv_meal;
    ImageView iv_no_meal;
    LinearLayout layout_main;
    LinearLayout mealGet;
    LinearLayout noMealGet;
    private String startTime;
    TextView tv_end_time;
    TitleView tv_title;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_yunservice_give;
    }

    @Override // activity.CommonActivity
    protected boolean initArgs(Intent intent) {
        this.consumed = getIntent().getIntExtra("consumed", 1);
        this.expired = getIntent().getIntExtra("expired", 1);
        this.startTime = getIntent().getStringExtra(AUserTrack.UTKEY_START_TIME);
        this.endTime = getIntent().getStringExtra(AUserTrack.UTKEY_END_TIME);
        this.iotId = getIntent().getStringExtra("iotId");
        return super.initArgs(intent);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.mealGet = (LinearLayout) findViewById(R.id.meal_get);
        this.noMealGet = (LinearLayout) findViewById(R.id.no_meal_get);
        this.tv_end_time = (TextView) findViewById(R.id.tv_end_time);
        this.tv_title = (TitleView) findViewById(R.id.fl_titlebar);
        this.action_get = (TextView) findViewById(R.id.action_get);
        this.check_order = (TextView) findViewById(R.id.check_order);
        this.iv_banner = (ImageView) findViewById(R.id.iv_banner);
        this.iv_meal = (ImageView) findViewById(R.id.iv_meal);
        this.iv_no_meal = (ImageView) findViewById(R.id.iv_no_meal);
        this.action_get.setOnClickListener(new OnMultiClickListener() { // from class: activity.EventYunServiceGiftActivity.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                EventYunServiceGiftActivity eventYunServiceGiftActivity = EventYunServiceGiftActivity.this;
                eventYunServiceGiftActivity.recvYunService(eventYunServiceGiftActivity.iotId);
            }
        });
        this.tv_title.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.EventYunServiceGiftActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                EventYunServiceGiftActivity.this.finish();
            }
        });
        updateYunServiceMeal();
        this.check_order.setOnClickListener(new View.OnClickListener() { // from class: activity.EventYunServiceGiftActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                Intent intent = new Intent(EventYunServiceGiftActivity.this.getActivity(), (Class<?>) PayYunServiceActivity.class);
                intent.putExtra("iotId", EventYunServiceGiftActivity.this.iotId);
                EventYunServiceGiftActivity.this.startActivity(intent);
            }
        });
        if (SystemUtil.isZhJianTi()) {
            this.iv_banner.setImageResource(R.drawable.banner);
            this.iv_meal.setImageResource(R.drawable.iv_get_meal);
            this.iv_no_meal.setImageResource(R.drawable.iv_no_get_meal);
        } else {
            this.iv_banner.setImageResource(R.drawable.banner_en);
            this.iv_meal.setImageResource(R.drawable.iv_get_meal_en);
            this.iv_no_meal.setImageResource(R.drawable.iv_no_get_meal_en);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateYunServiceMeal() {
        if (this.consumed == 0) {
            this.mealGet.setVisibility(8);
            this.noMealGet.setVisibility(0);
        } else {
            this.mealGet.setVisibility(0);
            this.noMealGet.setVisibility(8);
            this.tv_end_time.setText(getResources().getString(R.string.date_to, DateUtil.getTimeDes(Long.parseLong(this.endTime), DateUtil.sdf5)));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void recvYunService(String str) {
        showProgressDialog();
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/cloudstorage/presented/consume").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.EventYunServiceGiftActivity.4
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, EventYunServiceGiftActivity.this.TAG, exc.getLocalizedMessage());
                EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        if (EventYunServiceGiftActivity.this.getActivity() == null || EventYunServiceGiftActivity.this.getActivity().isFinishing()) {
                            return;
                        }
                        Toast.makeText(EventYunServiceGiftActivity.this.getActivity(), R.string.receive_fail, 0).show();
                        EventYunServiceGiftActivity.this.dismissProgressDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, EventYunServiceGiftActivity.this.TAG, "recvYunService:" + ioTResponse.getData() + "");
                int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.4.2
                        @Override // java.lang.Runnable
                        public void run() {
                            if (EventYunServiceGiftActivity.this.getActivity() == null || EventYunServiceGiftActivity.this.getActivity().isFinishing()) {
                                return;
                            }
                            Toast.makeText(EventYunServiceGiftActivity.this.getActivity(), R.string.receive_fail, 0).show();
                            EventYunServiceGiftActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                }
                try {
                    JSONObject object = JSONObject.parseObject(ioTResponse.getData().toString());
                    EventYunServiceGiftActivity.this.consumed = object.getIntValue("consumed");
                    EventYunServiceGiftActivity.this.expired = object.getIntValue("expired");
                    EventYunServiceGiftActivity.this.startTime = object.getString(AUserTrack.UTKEY_START_TIME);
                    EventYunServiceGiftActivity.this.endTime = object.getString(AUserTrack.UTKEY_END_TIME);
                    EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.4.3
                        @Override // java.lang.Runnable
                        public void run() {
                            if (EventYunServiceGiftActivity.this.getActivity() == null || EventYunServiceGiftActivity.this.getActivity().isFinishing()) {
                                return;
                            }
                            EventYunServiceGiftActivity.this.updateYunServiceMeal();
                            if (EventYunServiceGiftActivity.this.consumed == 1) {
                                Toast.makeText(EventYunServiceGiftActivity.this.getActivity(), R.string.receive_success, 0).show();
                                EventYunServiceGiftActivity.this.triggerRecordPlan();
                            } else {
                                EventYunServiceGiftActivity.this.dismissProgressDialog();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.4.4
                        @Override // java.lang.Runnable
                        public void run() {
                            if (EventYunServiceGiftActivity.this.getActivity() == null || EventYunServiceGiftActivity.this.getActivity().isFinishing()) {
                                return;
                            }
                            Toast.makeText(EventYunServiceGiftActivity.this.getActivity(), R.string.receive_fail, 0).show();
                            EventYunServiceGiftActivity.this.dismissProgressDialog();
                        }
                    });
                }
            }
        });
    }

    public void triggerRecordPlan() {
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath(APIConstants.API_PATH_RECORD_PLAN_QUERY).setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(new HashMap()).build(), new AnonymousClass5());
    }

    /* JADX INFO: renamed from: activity.EventYunServiceGiftActivity$5, reason: invalid class name */
    class AnonymousClass5 implements IoTCallback {
        AnonymousClass5() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, EventYunServiceGiftActivity.this.TAG, exc.getLocalizedMessage());
            EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.5.1
                @Override // java.lang.Runnable
                public void run() {
                    EventYunServiceGiftActivity.this.dismissProgressDialog();
                }
            });
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
            LogEx.e(true, EventYunServiceGiftActivity.this.TAG, "triggerRecordPlan:" + ioTResponse.getData() + "");
            int code = ioTResponse.getCode();
            ioTResponse.getLocalizedMsg();
            if (code != 200) {
                EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.5.2
                    @Override // java.lang.Runnable
                    public void run() {
                        EventYunServiceGiftActivity.this.dismissProgressDialog();
                    }
                });
                return;
            }
            Object data = ioTResponse.getData();
            if (data == null) {
                EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.5.3
                    @Override // java.lang.Runnable
                    public void run() {
                        EventYunServiceGiftActivity.this.dismissProgressDialog();
                    }
                });
                return;
            }
            org.json.JSONObject jSONObject = (org.json.JSONObject) data;
            int i = -1;
            try {
                i = jSONObject.getInt(AlinkConstants.KEY_TOTAL);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (i == 0) {
                HashMap map = new HashMap();
                map.put("name", String.valueOf(System.currentTimeMillis()));
                map.put("recordDuration", 30);
                ArrayList arrayList = new ArrayList();
                arrayList.add(1);
                map.put("eventTypeList", arrayList);
                map.put("allDay", 1);
                map.put("timeSectionList", new ArrayList());
                new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath(APIConstants.API_PATH_RECORD_PLAN_SET).setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.EventYunServiceGiftActivity.5.4
                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onFailure(IoTRequest ioTRequest2, Exception exc) {
                        LogEx.d(true, EventYunServiceGiftActivity.this.TAG, exc.getLocalizedMessage());
                        EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.5.4.1
                            @Override // java.lang.Runnable
                            public void run() {
                                EventYunServiceGiftActivity.this.dismissProgressDialog();
                            }
                        });
                    }

                    @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
                    public void onResponse(IoTRequest ioTRequest2, IoTResponse ioTResponse2) {
                        LogEx.e(true, EventYunServiceGiftActivity.this.TAG, "triggerRecordPlan:" + ioTResponse2.getData() + "");
                        int code2 = ioTResponse2.getCode();
                        ioTResponse2.getLocalizedMsg();
                        if (code2 != 200) {
                            EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.5.4.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    EventYunServiceGiftActivity.this.dismissProgressDialog();
                                }
                            });
                            return;
                        }
                        Object data2 = ioTResponse2.getData();
                        if (data2 == null) {
                            EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.5.4.3
                                @Override // java.lang.Runnable
                                public void run() {
                                    EventYunServiceGiftActivity.this.dismissProgressDialog();
                                }
                            });
                            return;
                        }
                        try {
                            EventYunServiceGiftActivity.this.bindPlanToDevice(((org.json.JSONObject) data2).getString("planId"));
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.5.4.4
                                @Override // java.lang.Runnable
                                public void run() {
                                    EventYunServiceGiftActivity.this.dismissProgressDialog();
                                }
                            });
                        }
                    }
                });
                return;
            }
            try {
                JSONArray jSONArray = jSONObject.getJSONArray("eventRecordPlanList");
                if (jSONArray.length() > 0) {
                    EventYunServiceGiftActivity.this.getBindYunServicePlan(EventYunServiceGiftActivity.this.iotId, jSONArray.getJSONObject(0).getString("planId"));
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.5.5
                    @Override // java.lang.Runnable
                    public void run() {
                        EventYunServiceGiftActivity.this.dismissProgressDialog();
                    }
                });
            }
        }
    }

    public void getBindYunServicePlan(String str, final String str2) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/eventrecord/plan/getbyiotid").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.EventYunServiceGiftActivity.6
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, EventYunServiceGiftActivity.this.TAG, exc.getLocalizedMessage());
                EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.6.1
                    @Override // java.lang.Runnable
                    public void run() {
                        EventYunServiceGiftActivity.this.dismissProgressDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, EventYunServiceGiftActivity.this.TAG, "getBindYunServiceEventPlan:" + ioTResponse.getData() + "");
                int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    if (code == 9116) {
                        EventYunServiceGiftActivity.this.bindPlanToDevice(str2);
                        return;
                    } else {
                        EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.6.2
                            @Override // java.lang.Runnable
                            public void run() {
                                EventYunServiceGiftActivity.this.dismissProgressDialog();
                            }
                        });
                        return;
                    }
                }
                Object data = ioTResponse.getData();
                if (data == null) {
                    EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.6.3
                        @Override // java.lang.Runnable
                        public void run() {
                            EventYunServiceGiftActivity.this.dismissProgressDialog();
                        }
                    });
                    return;
                }
                try {
                    if (((org.json.JSONObject) data).getInt("allDay") == 0) {
                        throw new Exception();
                    }
                    EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.6.4
                        @Override // java.lang.Runnable
                        public void run() {
                            EventYunServiceGiftActivity.this.dismissProgressDialog();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                    EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.6.5
                        @Override // java.lang.Runnable
                        public void run() {
                            EventYunServiceGiftActivity.this.dismissProgressDialog();
                        }
                    });
                }
            }
        });
    }

    public void bindPlanToDevice(String str) {
        HashMap map = new HashMap();
        map.put("planId", str);
        map.put("iotId", this.iotId);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/eventrecord/plan/device/bind").setApiVersion("2.0.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.EventYunServiceGiftActivity.7
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, EventYunServiceGiftActivity.this.TAG, exc.getLocalizedMessage());
                EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.7.1
                    @Override // java.lang.Runnable
                    public void run() {
                        EventYunServiceGiftActivity.this.dismissProgressDialog();
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, EventYunServiceGiftActivity.this.TAG, "bindPlanToDevice:" + ioTResponse.getData() + "");
                ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                EventYunServiceGiftActivity.this.runOnUiThread(new Runnable() { // from class: activity.EventYunServiceGiftActivity.7.2
                    @Override // java.lang.Runnable
                    public void run() {
                        EventYunServiceGiftActivity.this.dismissProgressDialog();
                    }
                });
            }
        });
    }
}
