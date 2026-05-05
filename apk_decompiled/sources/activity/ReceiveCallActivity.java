package activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import bean.DeviceInfoBean;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.iotx.linkvisual.media.LinkVisualMedia;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.seculink.app.R;
import tools.MediaUtil;

/* JADX INFO: loaded from: classes.dex */
public class ReceiveCallActivity extends CommonActivity {
    private String DeviceName;
    private String ProductKey;
    private DeviceInfoBean device;
    private boolean isOwner;
    private TextView nickNameTextView;
    private ImageView noReceiveImage;
    private ImageView receiveImage;
    private String title;
    private Vibrator vibrator;
    private String wakeUpData;
    public String iotId = "";
    public String appKey = "";

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.receive_activity;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        hideStatusBar();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onPause() {
        super.onPause();
        this.vibrator.cancel();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        long[] jArr = {1000, AdaptiveTrackSelection.DEFAULT_MIN_TIME_BETWEEN_BUFFER_REEVALUTATION_MS};
        this.vibrator = (Vibrator) getSystemService("vibrator");
        this.vibrator.vibrate(jArr, 0);
        MediaUtil.playRing(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            DeviceInfoBean deviceInfoBean = this.device;
            if (deviceInfoBean != null) {
                this.iotId = deviceInfoBean.getIotId();
                this.title = this.device.getName();
                this.isOwner = this.device.getOwned() == 1;
                this.DeviceName = this.device.getDeviceName();
                this.ProductKey = this.device.getProductKey();
                this.wakeUpData = this.device.getWakeUpData();
            }
        }
        this.receiveImage = (ImageView) findViewById(R.id.receive_call);
        this.noReceiveImage = (ImageView) findViewById(R.id.no_receive_call);
        this.nickNameTextView = (TextView) findViewById(R.id.tv_name);
        this.receiveImage.setOnClickListener(new View.OnClickListener() { // from class: activity.ReceiveCallActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                LinkVisualMedia.getInstance().preConnectByIotId(ReceiveCallActivity.this.iotId);
                ReceiveCallActivity.this.finish();
                Intent intent = new Intent(ReceiveCallActivity.this, (Class<?>) IPCameraActivity.class);
                intent.setFlags(335544320);
                Bundle bundle = new Bundle();
                bundle.putSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION, ReceiveCallActivity.this.device);
                intent.putExtras(bundle);
                intent.putExtra(AlinkConstants.KEY_LIST, ReceiveCallActivity.this.getIntent().getStringArrayExtra(AlinkConstants.KEY_LIST));
                ReceiveCallActivity.this.startActivity(intent);
            }
        });
        this.noReceiveImage.setOnClickListener(new View.OnClickListener() { // from class: activity.ReceiveCallActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ReceiveCallActivity.this.finish();
            }
        });
        this.nickNameTextView.setText(this.title + getResources().getString(R.string.alarm_event_occurring));
    }

    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.clearFlags(67108864);
            window.getDecorView().setSystemUiVisibility(1284);
            window.addFlags(Integer.MIN_VALUE);
            window.setStatusBarColor(0);
        }
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        MediaUtil.stopRing();
    }
}
