package activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import bean.StorageEvent;
import com.seculink.app.R;
import com.smarx.notchlib.NotchScreenManager;
import fragment.StorageFragment;
import fragment.YunFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import tools.DateUtil;
import tools.SharePreferenceManager;

/* JADX INFO: loaded from: classes.dex */
public class RecordVideoActivity extends CommonActivity {
    private static final int SENSOR_LANDSCAPE = 1;
    private static final int SENSOR_PORTRAIT = 0;
    private static final int SENSOR_REVERSE_LANDSCAPE = 3;
    private static final int SENSOR_REVERSE_PORTRAIT = 2;
    public String appKey;
    private View decorView;
    private FragmentManager fragmentManager;
    public boolean isFirst;
    private int lastOrientation;
    LinearLayout layout_main;
    private OrientationEventListener mOrientationEventListener;
    private RadioGroup radioGroup;
    private RadioButton rb_cloud;
    private RadioButton rb_storage;
    private StorageFragment storageFragment;
    private RelativeLayout title;
    private int uiVisibility;
    private YunFragment yunFragment;
    private int pos = -1;
    public String iotId = "";
    public String iotId1 = "";
    public String iotId2 = "";
    public String iotId3 = "";
    public String iotId4 = "";

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_recordvideo;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        NotchScreenManager.getInstance().setDisplayInNotch(this);
        this.isFirst = true;
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.decorView = getWindow().getDecorView();
        this.uiVisibility = this.decorView.getSystemUiVisibility();
        this.radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        this.rb_cloud = (RadioButton) findViewById(R.id.rb_cloud);
        this.rb_storage = (RadioButton) findViewById(R.id.rb_storage);
        this.title = (RelativeLayout) findViewById(R.id.title);
        this.iotId = getIntent().getStringExtra("iotId");
        this.iotId1 = getIntent().getStringExtra("iotId");
        this.iotId2 = getIntent().getStringExtra("iotId2");
        this.iotId3 = getIntent().getStringExtra("iotId3");
        this.iotId4 = getIntent().getStringExtra("iotId4");
        this.appKey = getIntent().getStringExtra("appKey");
        if (SharePreferenceManager.getInstance().getEventRecord(this.iotId) == 1) {
            this.rb_cloud.setVisibility(8);
        }
        this.rb_cloud.setOnClickListener(new View.OnClickListener() { // from class: activity.RecordVideoActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                RecordVideoActivity.this.replaceFragment(0);
            }
        });
        this.rb_storage.setOnClickListener(new View.OnClickListener() { // from class: activity.RecordVideoActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                RecordVideoActivity.this.replaceFragment(1);
            }
        });
        this.fragmentManager = getSupportFragmentManager();
        this.yunFragment = new YunFragment();
        this.storageFragment = new StorageFragment();
        String str = this.iotId2;
        if (str == null || !"".equals(str)) {
            replaceFragment(1);
        } else {
            replaceFragment(1);
        }
        if (EventBus.getDefault().isRegistered(this)) {
            return;
        }
        EventBus.getDefault().register(this);
    }

    @Override // activity.CommonActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (getResources().getConfiguration().orientation == 2) {
            if (getRequestedOrientation() == 0) {
                setRequestedOrientation(1);
                return;
            } else {
                setRequestedOrientation(9);
                return;
            }
        }
        super.onBackPressed();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        if (configuration.orientation == 2) {
            this.radioGroup.setVisibility(8);
            hideSystemUI();
        } else {
            this.radioGroup.setVisibility(0);
            showSystemUI();
        }
        super.onConfigurationChanged(configuration);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1111 && i2 == -1) {
            final int intExtra = intent.getIntExtra("return_key", 0);
            Log.e("回传数据1", intExtra + "");
            new Handler().postDelayed(new Runnable() { // from class: activity.RecordVideoActivity.3
                @Override // java.lang.Runnable
                public void run() {
                    RecordVideoActivity.this.storageFragment.seek2(Math.max(0, DateUtil.getHMSTimeMillins(intExtra * 1000)));
                }
            }, 1000L);
        }
    }

    public void getActivityData() {
        Intent intent = new Intent(this, (Class<?>) SearchActivity.class);
        intent.putExtra("iotId", this.iotId);
        startActivityForResult(intent, 1111);
    }

    private void showSystemUI() {
        this.decorView.setSystemUiVisibility(this.uiVisibility);
        getWindow().clearFlags(1024);
    }

    private void hideSystemUI() {
        this.decorView.setSystemUiVisibility(3846);
        getWindow().setFlags(1024, 1024);
    }

    public void setState() {
        if (this.pos == 0) {
            this.rb_cloud.setBackgroundResource(R.drawable.bg_tab_l_selected);
            this.rb_cloud.setTextColor(getResources().getColor(R.color.color_white));
            this.rb_storage.setBackgroundResource(R.drawable.bg_tab_r_unselected);
            this.rb_storage.setTextColor(getResources().getColor(R.color.colorAccent));
            return;
        }
        this.rb_cloud.setBackgroundResource(R.drawable.bg_tab_l_unselected);
        this.rb_cloud.setTextColor(getResources().getColor(R.color.colorAccent));
        this.rb_storage.setBackgroundResource(R.drawable.bg_tab_r_selected);
        this.rb_storage.setTextColor(getResources().getColor(R.color.color_white));
    }

    public void replaceFragment(int i) {
        if (this.pos == i) {
            return;
        }
        FragmentTransaction fragmentTransactionBeginTransaction = this.fragmentManager.beginTransaction();
        if (i == 0) {
            this.yunFragment = new YunFragment();
            fragmentTransactionBeginTransaction.replace(R.id.rl_frags, this.yunFragment);
        } else {
            this.storageFragment = new StorageFragment();
            fragmentTransactionBeginTransaction.replace(R.id.rl_frags, this.storageFragment);
        }
        fragmentTransactionBeginTransaction.commitAllowingStateLoss();
        this.pos = i;
        setState();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        registerSensorEvent();
        Log.d(this.TAG, "onResume: ===========" + SharePreferenceManager.getInstance().getStorageStatus(this.iotId));
    }

    public boolean isSensorOpen() {
        try {
            return Settings.System.getInt(getContentResolver(), "accelerometer_rotation") == 1;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.mOrientationEventListener.disable();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        this.isFirst = false;
        EventBus.getDefault().unregister(this);
    }

    public void registerSensorEvent() {
        this.mOrientationEventListener = new OrientationEventListener(this, 3) { // from class: activity.RecordVideoActivity.4
            @Override // android.view.OrientationEventListener
            public void onOrientationChanged(int i) {
                if (RecordVideoActivity.this.isSensorOpen()) {
                    if (i >= 245 && i < 295) {
                        if (RecordVideoActivity.this.lastOrientation != 1) {
                            RecordVideoActivity.this.setRequestedOrientation(0);
                            RecordVideoActivity.this.lastOrientation = 1;
                            return;
                        }
                        return;
                    }
                    if (i >= 65 && i < 115) {
                        if (RecordVideoActivity.this.lastOrientation != 3) {
                            RecordVideoActivity.this.setRequestedOrientation(8);
                            RecordVideoActivity.this.lastOrientation = 3;
                            return;
                        }
                        return;
                    }
                    if (i < 155 || i >= 205) {
                        if ((i >= 335 || i < 25) && RecordVideoActivity.this.lastOrientation != 0) {
                            RecordVideoActivity.this.setRequestedOrientation(1);
                            RecordVideoActivity.this.lastOrientation = 0;
                            return;
                        }
                        return;
                    }
                    if (RecordVideoActivity.this.lastOrientation != 2) {
                        RecordVideoActivity.this.setRequestedOrientation(9);
                        RecordVideoActivity.this.lastOrientation = 2;
                    }
                }
            }
        };
        this.mOrientationEventListener.enable();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void PictureUpdate(StorageEvent storageEvent) {
        replaceFragment(1);
    }
}
