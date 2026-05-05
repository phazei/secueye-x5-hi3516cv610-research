package activity;

import adapter.CloudImgAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import bean.CloudVideo;
import bean.DeviceInfoBean;
import bean.RefreshPicture;
import com.alibaba.sdk.android.openaccount.ut.UTConstants;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iotx.linkvisual.media.video.PlayerException;
import com.aliyun.iotx.linkvisual.media.video.listener.OnCompletionListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener;
import com.aliyun.iotx.linkvisual.media.video.player.ExoHlsPlayer;
import com.google.gson.Gson;
import com.haibin.calendarview.CalendarModel;
import com.haibin.calendarview.CalendarView;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivityCloudStorageBinding;
import com.smarx.notchlib.NotchScreenManager;
import com.xiaomi.mipush.sdk.Constants;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import jp.wasabeef.glide.transformations.BuildConfig;
import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import tools.DateUtil;
import tools.DensityUtil;
import tools.OnMultiClickListener;
import tools.SpUtil;
import tools.StatusBarUtil;
import tools.Utils;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
@RequiresApi(api = 3)
public class CloudStorageActivity extends CommonActivity {
    private static final int RECORD_UPDATE_TIME = 102;
    private static final int UPDATE_TIME = 101;
    private ActivityCloudStorageBinding binding;
    private CloudImgAdapter cloudImgAdapter;
    private CloudVideo cloudVideoBean;
    private int curBeginTime;
    private int curEndTime;
    private int day;
    private DeviceInfoBean device;
    private DeviceInfoBean device1;
    private DeviceInfoBean device2;
    private DeviceInfoBean device3;
    private int month;
    private DeviceInfoBean nvrDevice;
    private ExoHlsPlayer player;
    private CalendarModel selectCalendar;
    private CloudVideo.RecordFileListBean selectFile;
    private String selectIotId;
    private int year;
    private boolean isRecord = false;
    private boolean isPlay = false;
    private int recordTime = 0;
    private float speed = 1.0f;
    private Handler uiHandler = new Handler(new Handler.Callback() { // from class: activity.CloudStorageActivity.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case 101:
                    if (CloudStorageActivity.this.isPlay) {
                        CloudStorageActivity.this.updateTimeline();
                    }
                    break;
                case 102:
                    CloudStorageActivity.access$208(CloudStorageActivity.this);
                    break;
            }
            return false;
        }
    });
    private int selectPosition = 0;
    final SimpleDateFormat timeLineFormatter = new SimpleDateFormat("mm:ss");
    private boolean isSound = false;
    private boolean isHorizontal = false;
    CloudImgAdapter.OnItemClickListener adapterListener = new CloudImgAdapter.OnItemClickListener() { // from class: activity.CloudStorageActivity.27
        @Override // adapter.CloudImgAdapter.OnItemClickListener
        public void onItemClick(int i, CloudVideo.RecordFileListBean recordFileListBean) {
            int i2 = 0;
            while (i2 < CloudStorageActivity.this.cloudVideoBean.getRecordFileList().size()) {
                CloudStorageActivity.this.cloudVideoBean.getRecordFileList().get(i2).setIsSelected(i2 == i);
                i2++;
            }
            CloudStorageActivity.this.cloudImgAdapter.notifyDataSetChanged();
            CloudStorageActivity.this.selectPosition = i;
            CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
            cloudStorageActivity.initPlayVideo(cloudStorageActivity.cloudVideoBean.getRecordFileList().get(CloudStorageActivity.this.selectPosition));
        }
    };

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_cloud_storage;
    }

    static /* synthetic */ int access$208(CloudStorageActivity cloudStorageActivity) {
        int i = cloudStorageActivity.recordTime;
        cloudStorageActivity.recordTime = i + 1;
        return i;
    }

    static /* synthetic */ int access$2608(CloudStorageActivity cloudStorageActivity) {
        int i = cloudStorageActivity.selectPosition;
        cloudStorageActivity.selectPosition = i + 1;
        return i;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivityCloudStorageBinding) DataBindingUtil.setContentView(this, R.layout.activity_cloud_storage);
        setEdgeToEdge(this.binding.landscapePlayer);
        NotchScreenManager.getInstance().setDisplayInNotch(this);
        StatusBarUtil.setTranslucentStatus(getActivity());
        StatusBarUtil.setLightStatusBar(getActivity(), true);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.device2 = (DeviceInfoBean) extras.getSerializable("device2");
            this.device3 = (DeviceInfoBean) extras.getSerializable("device3");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            if (this.device1 != null && this.nvrDevice != null) {
                this.binding.layoutBottom.setVisibility(0);
            }
            if (this.device1 != null && this.nvrDevice != null && this.device2 != null) {
                this.binding.tvPlaybackGun.setText(R.string.playback_gun1);
                this.binding.layoutBottom.setVisibility(0);
                this.binding.layoutBullet2.setVisibility(0);
            }
            if (this.device1 != null && this.nvrDevice != null && this.device2 != null && this.device3 != null) {
                this.binding.tvPlaybackGun.setText(R.string.playback_gun1);
                this.binding.layoutBottom.setVisibility(0);
                this.binding.layoutBullet2.setVisibility(0);
                this.binding.layoutBullet3.setVisibility(0);
            }
        }
        this.binding.tvTitle.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.CloudStorageActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                CloudStorageActivity.this.finish();
            }

            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
                Intent intent = new Intent(CloudStorageActivity.this.getActivity(), (Class<?>) CloudDownloadActivity.class);
                intent.putExtra("iotId", CloudStorageActivity.this.selectIotId);
                CloudStorageActivity.this.startActivity(intent);
            }
        });
        this.binding.seekBar.bringToFront();
        this.binding.portraitPlayer.setOnClickListener(new View.OnClickListener() { // from class: activity.CloudStorageActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                CloudStorageActivity.this.binding.portraitPlayer.setSelected(!CloudStorageActivity.this.binding.portraitPlayer.isSelected());
                CloudStorageActivity.this.binding.layoutControl.setVisibility(CloudStorageActivity.this.binding.portraitPlayer.isSelected() ? 0 : 8);
                CloudStorageActivity.this.binding.layoutTime.setVisibility(CloudStorageActivity.this.binding.portraitPlayer.isSelected() ? 0 : 8);
                if (CloudStorageActivity.this.isHorizontal) {
                    CloudStorageActivity.this.binding.ivShadowTop.setVisibility(CloudStorageActivity.this.binding.portraitPlayer.isSelected() ? 0 : 8);
                    CloudStorageActivity.this.binding.ivShadowBottom.setVisibility(CloudStorageActivity.this.binding.portraitPlayer.isSelected() ? 0 : 8);
                    CloudStorageActivity.this.binding.ivBack.setVisibility(CloudStorageActivity.this.binding.portraitPlayer.isSelected() ? 0 : 8);
                }
            }
        });
        this.binding.ivFull.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (CloudStorageActivity.this.getRequestedOrientation() == 1) {
                    CloudStorageActivity.this.setRequestedOrientation(0);
                } else {
                    CloudStorageActivity.this.setRequestedOrientation(8);
                }
            }
        });
        this.binding.tvDate.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                CloudStorageActivity.this.binding.layoutCalendarView.setVisibility(0);
                CloudStorageActivity.this.binding.layoutCalendarView.bringToFront();
            }
        });
        this.binding.calendarView.setSelectSingleMode();
        this.binding.calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() { // from class: activity.CloudStorageActivity.6
            @Override // com.haibin.calendarview.CalendarView.OnMonthChangeListener
            public void onMonthChange(int i, int i2) {
                Resources resources;
                int i3;
                CloudStorageActivity.this.binding.tvCurdate.setText(i + Constants.ACCEPT_TIME_SEPARATOR_SERVER + String.format("%02d", Integer.valueOf(i2)));
                TextView textView = CloudStorageActivity.this.binding.tvCurdate;
                if (CloudStorageActivity.this.year == i && CloudStorageActivity.this.month == i2) {
                    resources = CloudStorageActivity.this.getResources();
                    i3 = R.color.colorAccent;
                } else {
                    resources = CloudStorageActivity.this.getResources();
                    i3 = R.color.color_black;
                }
                textView.setTextColor(resources.getColor(i3));
            }
        });
        this.binding.calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() { // from class: activity.CloudStorageActivity.7
            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarOutOfRange(CalendarModel calendarModel) {
                CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                cloudStorageActivity.showToast(cloudStorageActivity.getString(R.string.start_below_current_date));
            }

            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarSelect(CalendarModel calendarModel, boolean z) {
                if (z) {
                    CloudStorageActivity.this.selectCalendar = calendarModel;
                }
            }
        });
        this.binding.tvCancel.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.8
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                CloudStorageActivity.this.selectCalendar = null;
                CloudStorageActivity.this.binding.layoutCalendarView.setVisibility(8);
            }
        });
        this.binding.tvSuccess.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.9
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                CloudStorageActivity.this.binding.layoutCalendarView.setVisibility(8);
                if (CloudStorageActivity.this.selectCalendar != null) {
                    CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                    cloudStorageActivity.year = cloudStorageActivity.selectCalendar.getYear();
                    CloudStorageActivity cloudStorageActivity2 = CloudStorageActivity.this;
                    cloudStorageActivity2.month = cloudStorageActivity2.selectCalendar.getMonth();
                    CloudStorageActivity cloudStorageActivity3 = CloudStorageActivity.this;
                    cloudStorageActivity3.day = cloudStorageActivity3.selectCalendar.getDay();
                    CloudStorageActivity.this.binding.tvDate.setText(CloudStorageActivity.this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.month + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.day);
                    long[] stEtTime = CloudStorageActivity.this.getStEtTime();
                    CloudStorageActivity.this.curBeginTime = (int) (stEtTime[0] / 1000);
                    CloudStorageActivity.this.curEndTime = (int) (stEtTime[1] / 1000);
                    CloudStorageActivity cloudStorageActivity4 = CloudStorageActivity.this;
                    cloudStorageActivity4.initPlayData(cloudStorageActivity4.selectIotId);
                    return;
                }
                CloudStorageActivity cloudStorageActivity5 = CloudStorageActivity.this;
                cloudStorageActivity5.showToast(cloudStorageActivity5.getResources().getString(R.string.not_selected_date));
            }
        });
        this.binding.layoutPtz.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.10
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (CloudStorageActivity.this.selectIotId.equals(CloudStorageActivity.this.device.getIotId())) {
                    return;
                }
                CloudStorageActivity.this.year = DateUtil.getYear();
                CloudStorageActivity.this.month = DateUtil.getMonth();
                CloudStorageActivity.this.day = DateUtil.getDay();
                CloudStorageActivity.this.binding.tvDate.setText(CloudStorageActivity.this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.month + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.day);
                CloudStorageActivity.this.binding.calendarView.setRange(2000, 1, 1, CloudStorageActivity.this.year, CloudStorageActivity.this.month, CloudStorageActivity.this.day);
                CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                cloudStorageActivity.initPlayData(cloudStorageActivity.device.getIotId());
            }
        });
        this.binding.layoutBullet.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.11
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (CloudStorageActivity.this.selectIotId.equals(CloudStorageActivity.this.device1.getIotId())) {
                    return;
                }
                CloudStorageActivity.this.year = DateUtil.getYear();
                CloudStorageActivity.this.month = DateUtil.getMonth();
                CloudStorageActivity.this.day = DateUtil.getDay();
                CloudStorageActivity.this.binding.tvDate.setText(CloudStorageActivity.this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.month + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.day);
                CloudStorageActivity.this.binding.calendarView.setRange(2000, 1, 1, CloudStorageActivity.this.year, CloudStorageActivity.this.month, CloudStorageActivity.this.day);
                CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                cloudStorageActivity.initPlayData(cloudStorageActivity.device1.getIotId());
            }
        });
        this.binding.layoutBullet2.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.12
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (CloudStorageActivity.this.selectIotId.equals(CloudStorageActivity.this.device2.getIotId())) {
                    return;
                }
                CloudStorageActivity.this.year = DateUtil.getYear();
                CloudStorageActivity.this.month = DateUtil.getMonth();
                CloudStorageActivity.this.day = DateUtil.getDay();
                CloudStorageActivity.this.binding.tvDate.setText(CloudStorageActivity.this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.month + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.day);
                CloudStorageActivity.this.binding.calendarView.setRange(2000, 1, 1, CloudStorageActivity.this.year, CloudStorageActivity.this.month, CloudStorageActivity.this.day);
                CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                cloudStorageActivity.initPlayData(cloudStorageActivity.device2.getIotId());
            }
        });
        this.binding.layoutBullet3.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.13
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (CloudStorageActivity.this.selectIotId.equals(CloudStorageActivity.this.device3.getIotId())) {
                    return;
                }
                CloudStorageActivity.this.year = DateUtil.getYear();
                CloudStorageActivity.this.month = DateUtil.getMonth();
                CloudStorageActivity.this.day = DateUtil.getDay();
                CloudStorageActivity.this.binding.tvDate.setText(CloudStorageActivity.this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.month + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.day);
                CloudStorageActivity.this.binding.calendarView.setRange(2000, 1, 1, CloudStorageActivity.this.year, CloudStorageActivity.this.month, CloudStorageActivity.this.day);
                CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                cloudStorageActivity.initPlayData(cloudStorageActivity.device3.getIotId());
            }
        });
        this.binding.ivSound.setOnClickListener(new View.OnClickListener() { // from class: activity.CloudStorageActivity.14
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                CloudStorageActivity.this.isSound = !r2.isSound;
                CloudStorageActivity.this.binding.ivSound.setImageResource(CloudStorageActivity.this.isSound ? R.drawable.icon_sound_open : R.drawable.icon_sound_close);
                CloudStorageActivity.this.player.setVolume(CloudStorageActivity.this.isSound ? 1.0f : 0.0f);
            }
        });
        this.binding.layoutShot.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.15
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                CloudStorageActivity.this.snapshot();
            }
        });
        this.binding.tvSpeed.setOnClickListener(new View.OnClickListener() { // from class: activity.CloudStorageActivity.16
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (CloudStorageActivity.this.speed == 1.0f) {
                    CloudStorageActivity.this.speed = 2.0f;
                    CloudStorageActivity.this.binding.tvSpeed.setText("2X");
                    CloudStorageActivity.this.player.setPlaybackSpeed(CloudStorageActivity.this.speed);
                    return;
                }
                if (CloudStorageActivity.this.speed == 2.0f) {
                    CloudStorageActivity.this.speed = 4.0f;
                    CloudStorageActivity.this.binding.tvSpeed.setText("4X");
                    CloudStorageActivity.this.player.setPlaybackSpeed(CloudStorageActivity.this.speed);
                } else if (CloudStorageActivity.this.speed == 4.0f) {
                    CloudStorageActivity.this.speed = 0.5f;
                    CloudStorageActivity.this.binding.tvSpeed.setText("0.5X");
                    CloudStorageActivity.this.player.setPlaybackSpeed(CloudStorageActivity.this.speed);
                } else if (CloudStorageActivity.this.speed == 0.5d) {
                    CloudStorageActivity.this.speed = 1.0f;
                    CloudStorageActivity.this.binding.tvSpeed.setText("1X");
                    CloudStorageActivity.this.player.setPlaybackSpeed(CloudStorageActivity.this.speed);
                }
            }
        });
        this.binding.ivLeft.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.17
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getDay() - 1 < 1) {
                    if (CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getMonth() <= 1) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.clear();
                        int year = CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getYear();
                        CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getMonth();
                        calendar.set(1, year - 1);
                        calendar.set(2, 11);
                        CloudStorageActivity.this.binding.calendarView.scrollToCalendarAndUpdate(CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getYear() - 1, 12, calendar.getActualMaximum(5));
                    } else {
                        Calendar calendar2 = Calendar.getInstance();
                        calendar2.clear();
                        int year2 = CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getYear();
                        int month = CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getMonth();
                        calendar2.set(1, year2);
                        calendar2.set(2, (month - 1) - 1);
                        CloudStorageActivity.this.binding.calendarView.scrollToCalendarAndUpdate(CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getYear(), CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getMonth() - 1, calendar2.getActualMaximum(5));
                    }
                } else {
                    CloudStorageActivity.this.binding.calendarView.scrollToCalendarAndUpdate(CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getYear(), CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getMonth(), CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getDay() - 1);
                }
                if (CloudStorageActivity.this.selectCalendar != null) {
                    CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                    cloudStorageActivity.year = cloudStorageActivity.selectCalendar.getYear();
                    CloudStorageActivity cloudStorageActivity2 = CloudStorageActivity.this;
                    cloudStorageActivity2.month = cloudStorageActivity2.selectCalendar.getMonth();
                    CloudStorageActivity cloudStorageActivity3 = CloudStorageActivity.this;
                    cloudStorageActivity3.day = cloudStorageActivity3.selectCalendar.getDay();
                    CloudStorageActivity.this.binding.tvDate.setText(CloudStorageActivity.this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.month + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.day);
                    long[] stEtTime = CloudStorageActivity.this.getStEtTime();
                    CloudStorageActivity.this.curBeginTime = (int) (stEtTime[0] / 1000);
                    CloudStorageActivity.this.curEndTime = (int) (stEtTime[1] / 1000);
                    CloudStorageActivity cloudStorageActivity4 = CloudStorageActivity.this;
                    cloudStorageActivity4.initPlayData(cloudStorageActivity4.selectIotId);
                    return;
                }
                CloudStorageActivity cloudStorageActivity5 = CloudStorageActivity.this;
                cloudStorageActivity5.showToast(cloudStorageActivity5.getResources().getString(R.string.not_selected_date));
            }
        });
        this.binding.ivRight.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.18
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                int year = CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getYear();
                int month = CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getMonth();
                calendar.set(1, year);
                calendar.set(2, month - 1);
                if (CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getDay() + 1 > calendar.getActualMaximum(5)) {
                    if (CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getMonth() >= 12) {
                        CloudStorageActivity.this.binding.calendarView.scrollToCalendarAndUpdate(CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getYear() + 1, 1, 1);
                    } else {
                        CloudStorageActivity.this.binding.calendarView.scrollToCalendarAndUpdate(CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getYear(), CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getMonth() + 1, 1);
                    }
                } else {
                    CloudStorageActivity.this.binding.calendarView.scrollToCalendarAndUpdate(CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getYear(), CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getMonth(), CloudStorageActivity.this.binding.calendarView.getSelectedCalendar().getDay() + 1);
                }
                if (CloudStorageActivity.this.selectCalendar != null) {
                    CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                    cloudStorageActivity.year = cloudStorageActivity.selectCalendar.getYear();
                    CloudStorageActivity cloudStorageActivity2 = CloudStorageActivity.this;
                    cloudStorageActivity2.month = cloudStorageActivity2.selectCalendar.getMonth();
                    CloudStorageActivity cloudStorageActivity3 = CloudStorageActivity.this;
                    cloudStorageActivity3.day = cloudStorageActivity3.selectCalendar.getDay();
                    CloudStorageActivity.this.binding.tvDate.setText(CloudStorageActivity.this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.month + Constants.ACCEPT_TIME_SEPARATOR_SERVER + CloudStorageActivity.this.day);
                    long[] stEtTime = CloudStorageActivity.this.getStEtTime();
                    CloudStorageActivity.this.curBeginTime = (int) (stEtTime[0] / 1000);
                    CloudStorageActivity.this.curEndTime = (int) (stEtTime[1] / 1000);
                    CloudStorageActivity cloudStorageActivity4 = CloudStorageActivity.this;
                    cloudStorageActivity4.initPlayData(cloudStorageActivity4.selectIotId);
                    return;
                }
                CloudStorageActivity cloudStorageActivity5 = CloudStorageActivity.this;
                cloudStorageActivity5.showToast(cloudStorageActivity5.getResources().getString(R.string.not_selected_date));
            }
        });
        this.binding.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: activity.CloudStorageActivity.19
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (CloudStorageActivity.this.selectFile == null) {
                    return;
                }
                int progress = (int) ((((long) seekBar.getProgress()) * CloudStorageActivity.this.player.getDuration()) / ((long) CloudStorageActivity.this.binding.seekBar.getMax()));
                if (CloudStorageActivity.this.player.getPlayState() == 3 || CloudStorageActivity.this.player.getPlayState() == 2) {
                    CloudStorageActivity.this.player.seekTo(progress);
                    CloudStorageActivity.this.updateTimeline();
                } else {
                    CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                    cloudStorageActivity.initPlayVideo(cloudStorageActivity.selectFile);
                }
            }
        });
        this.binding.ivRefund.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.20
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                long currentPosition = CloudStorageActivity.this.player.getCurrentPosition() - 9500;
                if (currentPosition > 0 && currentPosition < CloudStorageActivity.this.player.getDuration()) {
                    CloudStorageActivity.this.seekPlayerPos(currentPosition);
                } else {
                    CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                    cloudStorageActivity.showToast(cloudStorageActivity.getResources().getString(R.string.out_of_range));
                }
            }
        });
        this.binding.ivAdvance.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.21
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                long currentPosition = CloudStorageActivity.this.player.getCurrentPosition() + 9500;
                if (currentPosition > 0 && currentPosition < CloudStorageActivity.this.player.getDuration()) {
                    CloudStorageActivity.this.seekPlayerPos(currentPosition);
                } else {
                    CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                    cloudStorageActivity.showToast(cloudStorageActivity.getResources().getString(R.string.out_of_range));
                }
            }
        });
        this.binding.ivPlayState.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.22
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (!CloudStorageActivity.this.isPlay || CloudStorageActivity.this.player == null) {
                    CloudStorageActivity.this.player.start();
                    CloudStorageActivity.this.isPlay = true;
                    CloudStorageActivity.this.updateTimeline();
                } else {
                    CloudStorageActivity.this.player.pause();
                    CloudStorageActivity.this.isPlay = false;
                }
                CloudStorageActivity.this.binding.ivPlayState.setSelected(CloudStorageActivity.this.isPlay);
            }
        });
        this.binding.ivBack.setOnClickListener(new OnMultiClickListener() { // from class: activity.CloudStorageActivity.23
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (CloudStorageActivity.this.getRequestedOrientation() == 0) {
                    CloudStorageActivity.this.setRequestedOrientation(1);
                } else {
                    CloudStorageActivity.this.setRequestedOrientation(9);
                }
            }
        });
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        if (configuration.orientation == 2) {
            this.isHorizontal = true;
            setFullScreen();
        } else {
            this.isHorizontal = false;
            backFullScreen();
        }
        super.onConfigurationChanged(configuration);
        setSwipeBackEnable(!this.isLand);
    }

    @Override // activity.CommonActivity, androidx.activity.ComponentActivity, android.app.Activity
    @SuppressLint({"SourceLockedOrientationActivity"})
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

    /* JADX INFO: Access modifiers changed from: private */
    public void snapshot() {
        if (this.player.getPlayState() != 3) {
            Toast.makeText(this, R.string.only_play_snap, 0).show();
            return;
        }
        Bitmap bitmap = ((TextureView) this.binding.play.getVideoSurfaceView()).getBitmap();
        if (bitmap == null) {
            showToast(getResources().getString(R.string.no_snap));
        } else {
            scanFile(bitmap);
            showToast(getResources().getString(R.string.camera_check));
        }
    }

    public String getFilesPath(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = context.getExternalFilesDir(null).getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "//" + Utils.getUserPhone();
    }

    public void scanFile(Bitmap bitmap) {
        File file = new File(getFilesPath(this) + "/photo/");
        if (file.exists() || file.mkdirs()) {
            Bitmap bitmapCreateScaledBitmap = Bitmap.createScaledBitmap(bitmap, 2880, 1620, true);
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(new File(file, System.currentTimeMillis() + ".jpg"));
                bitmapCreateScaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                EventBus.getDefault().post(new RefreshPicture());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    public void setFullScreen() {
        ExoHlsPlayer exoHlsPlayer = this.player;
        if (exoHlsPlayer != null) {
            exoHlsPlayer.setVideoScalingMode(1);
        }
        this.binding.tvTitle.setVisibility(8);
        this.binding.layoutFunction.setVisibility(8);
        this.binding.ivFull.setVisibility(8);
        this.binding.portraitPlayer.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.binding.layoutTime.getLayoutParams();
        layoutParams.addRule(12);
        layoutParams.setMargins(0, 0, 0, DensityUtil.dip2px(this, 70.0f));
        this.binding.layoutTime.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.binding.layoutBottomControl.getLayoutParams();
        layoutParams2.addRule(12);
        layoutParams2.setMargins(DensityUtil.dip2px(this, 40.0f), 0, DensityUtil.dip2px(this, 40.0f), DensityUtil.dip2px(this, 30.0f));
        this.binding.layoutBottomControl.setLayoutParams(layoutParams2);
        if (this.binding.portraitPlayer.isSelected()) {
            this.binding.ivShadowTop.setVisibility(0);
            this.binding.ivShadowBottom.setVisibility(0);
            this.binding.ivBack.setVisibility(0);
        }
        this.binding.tvStartTime.setVisibility(0);
        this.binding.tvEndTime.setVisibility(0);
        getWindow().setFlags(1024, 1024);
    }

    public void backFullScreen() {
        ExoHlsPlayer exoHlsPlayer = this.player;
        if (exoHlsPlayer != null) {
            exoHlsPlayer.setVideoScalingMode(2);
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.binding.portraitPlayer.getLayoutParams();
        layoutParams.height = getResources().getDimensionPixelSize(R.dimen.dimen_240);
        this.binding.portraitPlayer.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.binding.layoutTime.getLayoutParams();
        layoutParams2.addRule(12);
        layoutParams2.setMargins(0, 0, 0, 0);
        this.binding.layoutTime.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.binding.layoutBottomControl.getLayoutParams();
        layoutParams3.addRule(12);
        layoutParams3.setMargins(DensityUtil.dip2px(this, 15.0f), 0, DensityUtil.dip2px(this, 15.0f), DensityUtil.dip2px(this, 15.0f));
        this.binding.layoutBottomControl.setLayoutParams(layoutParams3);
        getWindow().setFlags(1024, 1024);
        this.binding.tvTitle.setVisibility(0);
        this.binding.ivFull.setVisibility(0);
        this.binding.layoutFunction.setVisibility(0);
        if (this.binding.portraitPlayer.isSelected()) {
            this.binding.ivShadowTop.setVisibility(8);
            this.binding.ivShadowBottom.setVisibility(8);
            this.binding.ivBack.setVisibility(8);
        }
        this.binding.tvStartTime.setVisibility(8);
        this.binding.tvEndTime.setVisibility(8);
        getWindow().clearFlags(1024);
    }

    @Override // activity.CommonActivity
    protected void initData() {
        super.initData();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.device = (DeviceInfoBean) extras.getSerializable(UTConstants.E_SDK_CONNECT_DEVICE_ACTION);
            this.device1 = (DeviceInfoBean) extras.getSerializable("device1");
            this.nvrDevice = (DeviceInfoBean) extras.getSerializable("nvrDevice");
            if (this.device != null) {
                this.year = DateUtil.getYear();
                this.month = DateUtil.getMonth();
                this.day = DateUtil.getDay();
                this.binding.tvDate.setText(this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + this.month + Constants.ACCEPT_TIME_SEPARATOR_SERVER + this.day);
                this.binding.calendarView.setRange(2000, 1, 1, this.year, this.month, this.day);
                initPlayData(this.device.getIotId());
            }
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.player.pause();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        this.player.start();
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.player.setVolume(0.0f);
        this.player.stop();
        this.player.release();
        this.player = null;
        this.uiHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPlayData(String str) {
        this.selectIotId = str;
        this.isPlay = false;
        this.binding.ivPlayState.setSelected(this.isPlay);
        if (this.binding.layoutBottom.getVisibility() == 0) {
            this.binding.layoutPtz.setSelected(this.selectIotId.equals(this.device.getIotId()));
            if (this.device1 != null) {
                this.binding.layoutBullet.setSelected(this.selectIotId.equals(this.device1.getIotId()));
            }
            if (this.device2 != null) {
                this.binding.layoutBullet2.setSelected(this.selectIotId.equals(this.device2.getIotId()));
            }
            if (this.device3 != null) {
                this.binding.layoutBullet3.setSelected(this.selectIotId.equals(this.device3.getIotId()));
            }
        }
        long[] stEtTime = getStEtTime();
        this.curBeginTime = (int) (stEtTime[0] / 1000);
        this.curEndTime = (int) (stEtTime[1] / 1000);
        initPlay();
        clearData();
        getCloudRecordList(str, this.curBeginTime, this.curEndTime, true, true);
        getMonthList(this.selectIotId, this.year, this.month);
    }

    private void clearData() {
        CloudVideo cloudVideo = this.cloudVideoBean;
        if (cloudVideo != null) {
            cloudVideo.getRecordFileList().clear();
        }
        CloudImgAdapter cloudImgAdapter = this.cloudImgAdapter;
        if (cloudImgAdapter != null) {
            cloudImgAdapter.notifyDataSetChanged();
        }
        this.selectFile = null;
        this.player.stop();
        this.isPlay = false;
        this.binding.ivPlayState.setSelected(this.isPlay);
        this.binding.seekBar.setProgress(0);
        this.binding.layoutNoData.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPlayVideo(CloudVideo.RecordFileListBean recordFileListBean) {
        ExoHlsPlayer exoHlsPlayer;
        if (recordFileListBean == null || (exoHlsPlayer = this.player) == null) {
            return;
        }
        exoHlsPlayer.stop();
        this.player.setDataSourceByIPCRecordFileName(this.selectIotId, recordFileListBean.getFileName());
        this.player.setOnPreparedListener(new OnPreparedListener() { // from class: activity.CloudStorageActivity.24
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                CloudStorageActivity.this.player.start();
                CloudStorageActivity.this.isPlay = true;
                CloudStorageActivity.this.binding.ivPlayState.setSelected(CloudStorageActivity.this.isPlay);
                CloudStorageActivity.this.updateTimeline();
                Log.e("播放数据开始", "开始" + CloudStorageActivity.this.binding.seekBar.getMin() + "  结束" + CloudStorageActivity.this.binding.seekBar.getMax());
            }
        });
        this.player.prepare();
        keepScreenLight();
        this.selectFile = recordFileListBean;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTimeline() {
        if (this.isPlay) {
            Message messageObtain = Message.obtain();
            messageObtain.what = 101;
            this.uiHandler.sendMessageDelayed(messageObtain, 1000L);
            ExoHlsPlayer exoHlsPlayer = this.player;
            if (exoHlsPlayer != null) {
                long duration = exoHlsPlayer.getDuration();
                if (duration <= 0) {
                    this.binding.seekBar.setProgress(0);
                    return;
                }
                this.binding.seekBar.setProgress((int) ((this.player.getCurrentPosition() * ((long) this.binding.seekBar.getMax())) / duration));
                this.binding.tvStartTime.setText(this.timeLineFormatter.format(Long.valueOf(this.player.getCurrentPosition())));
                this.binding.tvEndTime.setText(this.timeLineFormatter.format(Long.valueOf(this.player.getDuration())));
                if (this.player.getCurrentPosition() >= duration) {
                    this.player.stop();
                }
            }
        }
    }

    private void keepScreenLight() {
        getWindow().addFlags(128);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long[] getStEtTime() {
        return new long[]{DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0) - (DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0) - DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0)), DateUtil.getTimeMillins(this.year, this.month, this.day, 23, 59, 59)};
    }

    public void getCloudRecordList(String str, int i, int i2, boolean z, boolean z2) {
        Log.e("查询日期", "开始" + i + "  至" + i2);
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("beginTime", Integer.valueOf(i));
        map.put(AUserTrack.UTKEY_END_TIME, Integer.valueOf(i2));
        map.put(AlinkConstants.KEY_PAGE_SIZE, 500);
        map.put("needSnapshot", true);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/query").setApiVersion("2.1.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudStorageActivity.25
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.e(CloudStorageActivity.this.TAG, exc.getLocalizedMessage());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                ALog.e(CloudStorageActivity.this.TAG, ioTResponse.getData() + "");
                if (ioTResponse.getCode() != 200) {
                    CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                    cloudStorageActivity.showToast(cloudStorageActivity.getResources().getString(R.string.date_no_record));
                    return;
                }
                CloudStorageActivity.this.cloudVideoBean = (CloudVideo) new Gson().fromJson(ioTResponse.getData().toString(), CloudVideo.class);
                if (CloudStorageActivity.this.cloudVideoBean.getRecordFileList().size() != 0) {
                    CloudStorageActivity.this.uiHandler.post(new Runnable() { // from class: activity.CloudStorageActivity.25.1
                        @Override // java.lang.Runnable
                        public void run() {
                            CloudStorageActivity.this.binding.layoutNoData.setVisibility(8);
                            CloudStorageActivity.this.binding.layoutCalendarView.setVisibility(8);
                            CloudStorageActivity.this.binding.rvList.setVisibility(0);
                            if (CloudStorageActivity.this.cloudVideoBean == null || CloudStorageActivity.this.cloudVideoBean.getRecordFileList() == null || CloudStorageActivity.this.cloudVideoBean.getRecordFileList().size() == 0) {
                                return;
                            }
                            CloudStorageActivity.this.cloudImgAdapter = new CloudImgAdapter(CloudStorageActivity.this, CloudStorageActivity.this.cloudVideoBean.getRecordFileList());
                            RecyclerView.ItemAnimator itemAnimator = CloudStorageActivity.this.binding.rvList.getItemAnimator();
                            if (itemAnimator instanceof SimpleItemAnimator) {
                                ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
                            }
                            CloudStorageActivity.this.binding.rvList.getItemAnimator().setChangeDuration(0L);
                            CloudStorageActivity.this.cloudImgAdapter.setHasStableIds(true);
                            CloudStorageActivity.this.binding.rvList.setLayoutManager(new LinearLayoutManager(CloudStorageActivity.this));
                            CloudStorageActivity.this.binding.rvList.setAdapter(CloudStorageActivity.this.cloudImgAdapter);
                            CloudStorageActivity.this.cloudImgAdapter.setOnItemClickListener(CloudStorageActivity.this.adapterListener);
                            CloudStorageActivity.this.cloudVideoBean.getRecordFileList().get(0).setIsSelected(true);
                            CloudStorageActivity.this.cloudImgAdapter.notifyDataSetChanged();
                            CloudStorageActivity.this.selectPosition = 0;
                            CloudStorageActivity.this.initPlayVideo(CloudStorageActivity.this.cloudVideoBean.getRecordFileList().get(CloudStorageActivity.this.selectPosition));
                        }
                    });
                    return;
                }
                CloudStorageActivity.this.binding.layoutNoData.setVisibility(0);
                CloudStorageActivity.this.binding.layoutCalendarView.setVisibility(8);
                CloudStorageActivity.this.binding.rvList.setVisibility(8);
            }
        });
    }

    private void getMonthList(String str, final int i, final int i2) {
        HashMap map = new HashMap();
        String str2 = new DecimalFormat("00").format(i2);
        map.put("iotId", str);
        map.put(SpUtil.MONTH, i + "" + str2);
        map.put("timeZone", 8);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/monthrecord/query").setApiVersion(BuildConfig.VERSION_NAME).setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: activity.CloudStorageActivity.26
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                Log.e(CloudStorageActivity.this.TAG, "shareDevice onResponse: code: " + code);
                ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    return;
                }
                ArrayList arrayList = new ArrayList();
                String string = null;
                try {
                    string = ((JSONObject) ioTResponse.getData()).get("recordFlags").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i3 = 0; i3 < string.length(); i3++) {
                    if (string.charAt(i3) == '1') {
                        arrayList.add(Integer.valueOf(i3 + 1));
                    }
                }
                CloudStorageActivity.this.drawCircle(i, i2, arrayList);
            }
        });
    }

    private CalendarModel getSchemeCalendar(int i, int i2, int i3, int i4, String str) {
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.setYear(i);
        calendarModel.setMonth(i2);
        calendarModel.setDay(i3);
        return calendarModel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawCircle(int i, int i2, List<Integer> list) {
        HashMap map = new HashMap();
        for (int i3 = 0; i3 < list.size(); i3++) {
            map.put(getSchemeCalendar(i, i2, list.get(i3).intValue(), -12526811, "").toString(), getSchemeCalendar(i, i2, list.get(i3).intValue(), -12526811, ""));
        }
        this.binding.calendarView.setSchemeDate(map);
    }

    public void seekPlayerPos(long j) {
        if (this.selectFile == null) {
            return;
        }
        this.player.seekTo(j);
    }

    private void initPlay() {
        this.player = new ExoHlsPlayer(this);
        this.player.setVideoScalingMode(2);
        this.binding.play.setPlayer(this.player.getExoPlayer());
        this.binding.play.requestFocus();
        this.speed = 1.0f;
        this.binding.tvSpeed.setText("1X");
        this.player.setPlaybackSpeed(this.speed);
        this.binding.play.setResizeMode(3);
        this.player.setVolume(this.isSound ? 1.0f : 0.0f);
        this.binding.ivSound.setImageResource(this.isSound ? R.drawable.icon_sound_open : R.drawable.icon_sound_close);
        this.player.setCirclePlay(false);
        this.player.setOnCompletionListener(new OnCompletionListener() { // from class: activity.CloudStorageActivity.28
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnCompletionListener
            public void onCompletion() {
            }
        });
        this.player.setOnErrorListener(new OnErrorListener() { // from class: activity.CloudStorageActivity.29
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
            public void onError(final PlayerException playerException) {
                CloudStorageActivity.this.runOnUiThread(new Runnable() { // from class: activity.CloudStorageActivity.29.1
                    @Override // java.lang.Runnable
                    @SuppressLint({"StringFormatInvalid"})
                    public void run() {
                        if (CloudStorageActivity.this.getActivity() == null || CloudStorageActivity.this.getActivity().isFinishing()) {
                            return;
                        }
                        switch (playerException.getCode()) {
                            case 6:
                                switch (playerException.getSubCode()) {
                                    case 1005:
                                        CloudStorageActivity.this.showToast(CloudStorageActivity.this.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                        break;
                                    case 1006:
                                        CloudStorageActivity.this.showToast(CloudStorageActivity.this.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                        break;
                                    case 1007:
                                        CloudStorageActivity.this.showToast(CloudStorageActivity.this.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                        break;
                                    case 1008:
                                        CloudStorageActivity.this.showToast(CloudStorageActivity.this.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                        break;
                                    case 1009:
                                        CloudStorageActivity.this.showToast(CloudStorageActivity.this.getString(R.string.connect_failed, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                        break;
                                }
                                break;
                            case 7:
                                if (playerException.getSubCode() == 1000) {
                                    CloudStorageActivity.this.showToast(CloudStorageActivity.this.getString(R.string.play_failed_retry, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                }
                                break;
                            case 8:
                                if (playerException.getSubCode() == 1100) {
                                    CloudStorageActivity.this.showToast(CloudStorageActivity.this.getString(R.string.play_failed_retry, new Object[]{Integer.valueOf(playerException.getSubCode())}));
                                    break;
                                }
                                break;
                        }
                    }
                });
            }
        });
        this.player.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: activity.CloudStorageActivity.30
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        Log.d(CloudStorageActivity.this.TAG, "onPlayerStateChange: puppet:PlayerState.STATE_IDLE");
                        break;
                    case 2:
                        Log.d(CloudStorageActivity.this.TAG, "onPlayerStateChange: puppet:PlayerState.STATE_BUFFERING");
                        break;
                    case 3:
                        Log.d(CloudStorageActivity.this.TAG, "onPlayerStateChange: puppet:PlayerState.STATE_READY");
                        if (CloudStorageActivity.this.selectFile != null) {
                            CloudStorageActivity.this.updateTimeline();
                            break;
                        }
                        break;
                    case 4:
                        Log.e("播放", "结束");
                        CloudStorageActivity.this.isPlay = false;
                        CloudStorageActivity.this.binding.ivPlayState.setSelected(CloudStorageActivity.this.isPlay);
                        if (CloudStorageActivity.this.selectPosition + 1 < CloudStorageActivity.this.cloudVideoBean.getRecordFileList().size()) {
                            CloudStorageActivity.access$2608(CloudStorageActivity.this);
                            int i2 = 0;
                            while (i2 < CloudStorageActivity.this.cloudVideoBean.getRecordFileList().size()) {
                                CloudStorageActivity.this.cloudVideoBean.getRecordFileList().get(i2).setIsSelected(i2 == CloudStorageActivity.this.selectPosition);
                                i2++;
                            }
                            CloudStorageActivity.this.cloudImgAdapter.notifyDataSetChanged();
                            CloudStorageActivity.this.binding.rvList.smoothScrollToPosition(CloudStorageActivity.this.selectPosition);
                            CloudStorageActivity cloudStorageActivity = CloudStorageActivity.this;
                            cloudStorageActivity.initPlayVideo(cloudStorageActivity.cloudVideoBean.getRecordFileList().get(CloudStorageActivity.this.selectPosition));
                        }
                        break;
                }
            }
        });
    }
}
