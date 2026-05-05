package activity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import config.Constants;
import java.util.HashMap;
import sdk.ChannelManager;
import sdk.IPCManager;
import tools.MyCallback;
import tools.SettingsCtrl;
import tools.SharePreferenceManager;
import view.ItemView;
import view.SelectorDialogFragment;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class RecordSettingActivity extends CommonActivity implements View.OnClickListener {
    TitleView fl_titlebar;
    private String iotId;
    ItemView itemQualityMode;
    ItemView itemRecordMode;
    LinearLayout layout_main;
    private SelectorDialogFragment qualityDialogFragment;
    private int recordMode;
    private String[] recordModeArr;
    private String[] recordModeArrDesc;
    private String[] recordModeArrValue;
    private SelectorDialogFragment recordModeDialogFragment;
    private int recordQuality;
    private String[] recordQualityArr;
    private Handler uiHandler = new Handler();
    private SharePreferenceManager.OnCallSetListener mSPModifyListener = new SharePreferenceManager.OnCallSetListener() { // from class: activity.RecordSettingActivity.4
        @Override // tools.SharePreferenceManager.OnCallSetListener
        public void onCallSet(final String str, final String str2) {
            RecordSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.RecordSettingActivity.4.1
                @Override // java.lang.Runnable
                public void run() {
                    String str3 = str2;
                    if (str3 == null || str3.trim().equals("")) {
                        return;
                    }
                    if (str2.equals(RecordSettingActivity.this.getString(R.string.storage_record_mode_key))) {
                        RecordSettingActivity.this.recordMode = SharePreferenceManager.getInstance().getStorageRecordMode(str);
                        RecordSettingActivity.this.itemRecordMode.setRightText(RecordSettingActivity.this.recordModeArr[RecordSettingActivity.this.recordMode - 1]);
                    } else if (str2.equals(RecordSettingActivity.this.getResources().getString(R.string.storage_record_quality))) {
                        RecordSettingActivity.this.recordQuality = SharePreferenceManager.getInstance().getRecordQuality(str);
                        RecordSettingActivity.this.itemQualityMode.setRightText(RecordSettingActivity.this.recordQualityArr[RecordSettingActivity.this.recordQuality]);
                    }
                }
            });
        }
    };
    private ChannelManager.IMobileMsgListener iMobileMsgListener = new ChannelManager.IMobileMsgListener() { // from class: activity.RecordSettingActivity.8
        @Override // sdk.ChannelManager.IMobileMsgListener
        public void onCommand(String str, String str2) {
            Log.e(RecordSettingActivity.this.TAG, "ChannelManager.IMobileMsgListener    topic:" + str + "     msg:" + str2);
            if (str.equals("/thing/properties")) {
                SettingsCtrl.getInstance().getProperties(RecordSettingActivity.this.iotId, new MyCallback() { // from class: activity.RecordSettingActivity.8.1
                    @Override // tools.MyCallback
                    public void onComplete(boolean z) {
                    }
                });
            }
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_record_setting;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.itemRecordMode = (ItemView) findViewById(R.id.item_record_modes);
        this.itemRecordMode.setOnClickListener(this);
        this.itemQualityMode = (ItemView) findViewById(R.id.item_record_quality);
        this.itemQualityMode.setOnClickListener(this);
        this.iotId = getIntent().getStringExtra("iotId");
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.RecordSettingActivity.1
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                RecordSettingActivity.this.finish();
            }
        });
        this.recordModeArr = new String[]{getResources().getString(R.string.alarm_record), getResources().getString(R.string.day_record)};
        this.recordModeArrValue = new String[]{"1", "2"};
        this.recordModeArrDesc = new String[]{getResources().getString(R.string.record_desc1), getResources().getString(R.string.record_desc)};
        this.recordModeDialogFragment = new SelectorDialogFragment(getString(R.string.record_mode), this.recordModeArr);
        this.recordModeDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.RecordSettingActivity.2
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                RecordSettingActivity recordSettingActivity = RecordSettingActivity.this;
                recordSettingActivity.updateDevParam(recordSettingActivity.getString(R.string.storage_record_mode_key), RecordSettingActivity.this.recordModeArrValue[i]);
            }
        });
        this.recordQualityArr = new String[]{getResources().getString(R.string.video_L), getResources().getString(R.string.video_H)};
        this.qualityDialogFragment = new SelectorDialogFragment(getString(R.string.record_video_quality), this.recordQualityArr);
        this.qualityDialogFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: activity.RecordSettingActivity.3
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateDevParam(String str, final Object obj) {
        HashMap map = new HashMap();
        if (str.equals(getString(R.string.storage_record_mode_key))) {
            map.put(Constants.STORAGE_RECORD_MODE_MODEL_NAME, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.RecordSettingActivity.5
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            RecordSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.RecordSettingActivity.5.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(RecordSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                            return;
                        }
                        SharePreferenceManager.getInstance().setStorageRecordMode(RecordSettingActivity.this.iotId, Integer.parseInt(obj.toString()));
                        SharePreferenceManager.getInstance().getStorageRecordMode(RecordSettingActivity.this.iotId);
                        RecordSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.RecordSettingActivity.5.2
                            @Override // java.lang.Runnable
                            public void run() {
                                RecordSettingActivity.this.itemRecordMode.setDescText(RecordSettingActivity.this.recordModeArrDesc[Integer.parseInt(obj.toString()) - 1]);
                                Toast.makeText(RecordSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            });
        } else if (str.equals(getString(R.string.storage_record_quality))) {
            map.put(Constants.StorageRecordQuality, Integer.valueOf(Integer.parseInt(obj.toString())));
            IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.RecordSettingActivity.6
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, Object obj2) {
                    if (!z || obj2 == null || "".equals(String.valueOf(obj2))) {
                        return;
                    }
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj2));
                    if (object.containsKey("code")) {
                        if (object.getInteger("code").intValue() != 200) {
                            RecordSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.RecordSettingActivity.6.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(RecordSettingActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                                }
                            });
                        } else {
                            SharePreferenceManager.getInstance().setRecordQuality(RecordSettingActivity.this.iotId, Integer.parseInt(obj.toString()));
                            RecordSettingActivity.this.uiHandler.post(new Runnable() { // from class: activity.RecordSettingActivity.6.2
                                @Override // java.lang.Runnable
                                public void run() {
                                    Toast.makeText(RecordSettingActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                                }
                            });
                        }
                    }
                }
            });
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.item_record_modes) {
            this.recordModeDialogFragment.showAllowingStateLoss(getSupportFragmentManager(), "");
        } else if (view2.getId() == R.id.item_record_quality) {
            this.qualityDialogFragment.showAllowingStateLoss(getSupportFragmentManager(), "");
        }
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        SharePreferenceManager.getInstance().registerOnCallSetListener(this.mSPModifyListener);
        SettingsCtrl.getInstance().getProperties(this.iotId, new MyCallback() { // from class: activity.RecordSettingActivity.7
            @Override // tools.MyCallback
            public void onComplete(boolean z) {
            }
        });
        ChannelManager.getInstance().registerListener(this.iMobileMsgListener);
        this.recordMode = SharePreferenceManager.getInstance().getStorageRecordMode(this.iotId);
        this.itemRecordMode.setRightText(this.recordModeArr[this.recordMode - 1]);
        this.itemRecordMode.setDescText(this.recordModeArrDesc[this.recordMode - 1]);
        this.recordQuality = SharePreferenceManager.getInstance().getRecordQuality(this.iotId);
        this.itemQualityMode.setRightText(this.recordQualityArr[this.recordQuality]);
    }
}
