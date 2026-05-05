package fragment;

import activity.CloudDownloadActivity;
import activity.RecordVideoActivity;
import adapter.CloudPictureAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bean.CloudInfo;
import bean.CloudRecordBean;
import bean.CloudVideo;
import bean.EventInfo;
import bean.NetWorkEvent;
import bean.PicInfoByIds;
import bean.RefreshPicture;
import com.alibaba.fastjson.JSON;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClient;
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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.gson.Gson;
import com.haibin.calendarview.CalendarModel;
import com.haibin.calendarview.CalendarView;
import com.hjq.permissions.Permission;
import com.seculink.app.R;
import com.xiaomi.mipush.sdk.Constants;
import config.AppConfig;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import jp.wasabeef.glide.transformations.BuildConfig;
import kt.CloudButton;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tools.DateUtil;
import tools.LogEx;
import tools.ScreenUtil;
import tools.SharePreferenceManager;
import tools.SpUtil;
import tools.Utils;
import view.ButtonRipperView;
import view.SelectorDialogFragment;
import view.TimeRulerView;
import view.TitleView;

/* JADX INFO: loaded from: classes4.dex */
public class YunFragment extends CommonFragment implements View.OnClickListener {
    private static final String TAG = "playback";

    /* JADX INFO: renamed from: activity, reason: collision with root package name */
    private RecordVideoActivity f7880activity;
    private Animation alphaAnimation;
    private LinearLayout bottomView;
    private List<Integer> cPaints;
    private CalendarView calendarView;
    private ImageView capture_btn;
    private SeekBar cardSeekBar;
    private RelativeLayout card_video_rl;
    private List<CloudRecordBean> catRecordBean;
    private ImageView circle_record_image;
    private LinearLayout circle_record_ll;
    private TextView circle_record_text;
    private CloudButton cloudButton;
    private CloudInfo cloudInfo;
    private CloudVideo cloudVideoBean;
    private long countTime;
    private int curBeginTime;
    private int curBeginTimeHead;
    private int curEndTime;
    private int day;
    private List<CloudRecordBean> dogRecordBean;
    private TextView durationTv;
    private ButtonRipperView enlarge_scale_ll;
    private List<Integer> etList;
    private List<EventInfo.Info> eventList;
    private SimpleExoPlayerView exoPlayerView;
    private Button exoZoomBtn;
    private List<CloudRecordBean> finalCloudRecordBeanList;
    private LinearLayout fl_title;
    private TitleView fl_titlebar;
    private FrameLayout fllayout;
    private LinearLayout full_after_ten_seconds_ll;
    private LinearLayout full_before_ten_seconds_ll;
    ImageView full_camera;
    LinearLayout full_icon;
    ImageView full_sound;
    private TextView full_speed;
    private LinearLayout full_speed_ll;
    private LinearLayout full_speed_ll1;
    ImageView full_stop;
    private boolean isCalendarShow;
    private boolean isFloat;
    private boolean isRequestNone;
    private boolean isSeeked;
    private ImageView iv;
    private ImageView ivPlayAfter;
    private ImageView ivPlayBefore;
    private LinearLayout layout_control;
    private ImageButton listenerBtn;
    private View llSeekTimebar;
    private ButtonRipperView ll_capture;
    private LinearLayout ll_date;
    private LinearLayout ll_double_eye_record;
    private LinearLayout ll_download;
    private LinearLayout ll_listener;
    private LinearLayout ll_md;
    private LinearLayout ll_play;
    private LinearLayout ll_root;
    private LinearLayout ll_spinner_speed;
    private LinearLayout ll_title;
    private LinearLayout ll_zoom;
    private CloudPictureAdapter mCloudPictureAdapter;
    private RecyclerView mRecyclerView;
    private int month;
    private List<CloudRecordBean> moveRecordBean;
    private ButtonRipperView narrow_scale_ll;
    private List<CloudRecordBean> peopleRecordBean;
    private ImageButton playBtn;
    private ImageButton playButton;
    private TextView playInfoTv;
    private ButtonRipperView play_back_off_ll;
    private ButtonRipperView play_fast_forward_ll;
    private ExoHlsPlayer player;
    private LinearLayout rightView;
    private View rlPlayView;
    private ImageView shot_record_image;
    private LinearLayout shot_record_ll;
    private TextView shot_record_text;
    private ImageButton speedButton;
    private SelectorDialogFragment speedFragment;
    private ImageView speed_btn;
    private List<Integer> stList;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    private TimeRulerView timeRulerView;
    private TextView timeTextView;
    private LinearLayout time_rule_view_ll1;
    private LinearLayout time_rule_view_ll2;
    private LinearLayout time_rule_view_ll3;
    ScheduledFuture<?> timelineUpdateHandle;
    private Timer timer;
    private String title;
    private TextView tvRecordTime;
    private TextView tv_curdate;
    private TextView tv_day;
    private TextView tv_month;
    private TextView tv_times;
    private Handler uiHandler;
    ScheduledFuture<?> updatePlayInfoHandle;
    private ProgressBar videoBufferingProgressBar;
    private View view_line;
    private int year;
    private List<CloudInfo> videoInfoList = new ArrayList();
    final SimpleDateFormat timeLineFormatter = new SimpleDateFormat("mm:ss");
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private boolean speakerSwitch = false;
    private final int querySize = 500;
    MutableLiveData<Boolean> isPause = new MutableLiveData<>();
    private String[] speeds = {"0.5x", "1x", "2x", "4x", "8X", "16X"};
    private int speedPosition = 1;
    private boolean isToday = true;
    private int currentDoubleId = 0;
    private int newTime = 0;
    private boolean isRecordingMp4 = false;
    private File file = null;
    private int count = 0;
    final Runnable timelineUpdateTask = new Runnable() { // from class: fragment.YunFragment.35
        @Override // java.lang.Runnable
        public void run() {
            YunFragment.this.f7880activity.runOnUiThread(new Runnable() { // from class: fragment.YunFragment.35.1
                @Override // java.lang.Runnable
                public void run() {
                    if (YunFragment.this.getActivity() == null || YunFragment.this.getActivity().isFinishing() || YunFragment.this.player.getPlayState() != 3) {
                        return;
                    }
                    YunFragment.this.updateTimeline();
                }
            });
        }
    };
    final Runnable updatePlayInfoTimerTask = new Runnable() { // from class: fragment.YunFragment.36
        @Override // java.lang.Runnable
        public void run() {
            YunFragment.this.f7880activity.runOnUiThread(new Runnable() { // from class: fragment.YunFragment.36.1
                @Override // java.lang.Runnable
                public void run() {
                    if (YunFragment.this.getActivity() == null || YunFragment.this.getActivity().isFinishing()) {
                        return;
                    }
                    YunFragment.this.updatePlayInfo();
                }
            });
        }
    };

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.fragment_yun;
    }

    static /* synthetic */ int access$3908(YunFragment yunFragment) {
        int i = yunFragment.count;
        yunFragment.count = i + 1;
        return i;
    }

    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.f7880activity = (RecordVideoActivity) context;
    }

    public static void verifyStoragePermissions(Activity activity2) {
        try {
            if (ActivityCompat.checkSelfPermission(activity2, Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                ActivityCompat.requestPermissions(activity2, new String[]{Permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void snapshot() {
        if (this.player.getPlayState() != 3) {
            Toast.makeText(this.f7880activity, R.string.only_play_snap, 0).show();
            return;
        }
        Bitmap bitmap = ((TextureView) this.exoPlayerView.getVideoSurfaceView()).getBitmap();
        if (bitmap == null) {
            showToast(getResources().getString(R.string.no_snap));
        } else {
            scanFile(bitmap);
            showToast(getResources().getString(R.string.camera_check));
        }
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        if (configuration.orientation == 2) {
            setFullScreen();
        } else {
            backFullScreen();
        }
        super.onConfigurationChanged(configuration);
    }

    public void setFullScreen() {
        this.calendarView.setVisibility(8);
        ExoHlsPlayer exoHlsPlayer = this.player;
        if (exoHlsPlayer != null) {
            exoHlsPlayer.setVideoScalingMode(1);
        }
        this.exoPlayerView.setResizeMode(3);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.card_video_rl.getLayoutParams();
        layoutParams.height = -1;
        this.card_video_rl.setLayoutParams(layoutParams);
        this.isFloat = false;
        setFloatBarState();
        this.fl_title.setVisibility(8);
        this.tv_times.setVisibility(8);
        this.view_line.setVisibility(8);
        this.llSeekTimebar.setVisibility(8);
        this.ll_date.setVisibility(8);
        this.fl_titlebar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        this.fl_titlebar.setTitleTextColor(R.color.color_white);
        this.fl_titlebar.setTitleBackTextColor(R.color.color_white);
        this.tv_times.setTextColor(getResources().getColor(R.color.color_white));
        this.fl_titlebar.setLeftImgId(R.drawable.ic_back2);
        this.fl_titlebar.setTitleTextVisible(0);
        if (Build.VERSION.SDK_INT >= 19) {
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.fl_titlebar.getLayoutParams();
            layoutParams2.height = ScreenUtil.dp2Px(this.f7880activity, 44.0f);
            this.fl_titlebar.setTitleRlPadding(0);
            this.fl_titlebar.setLayoutParams(layoutParams2);
        }
        if (this.ll_title.getParent() != null) {
            ((ViewGroup) this.ll_title.getParent()).removeView(this.ll_title);
        }
        this.card_video_rl.addView(this.ll_title, 2);
        RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.tvRecordTime.getLayoutParams();
        layoutParams3.removeRule(12);
        layoutParams3.addRule(3, R.id.ll_title);
        this.tvRecordTime.setLayoutParams(layoutParams3);
        this.bottomView.setVisibility(8);
        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) this.playInfoTv.getLayoutParams();
        layoutParams4.removeRule(12);
        layoutParams4.leftMargin = 0;
        layoutParams4.bottomMargin = 0;
        layoutParams4.addRule(10);
        layoutParams4.addRule(11);
        layoutParams4.topMargin = ScreenUtil.dp2Px(getActivity(), 10.0f);
        layoutParams4.rightMargin = ScreenUtil.dp2Px(getActivity(), 20.0f);
        this.playInfoTv.setLayoutParams(layoutParams4);
        if (this.timeRulerView.getParent() != null) {
            ((ViewGroup) this.timeRulerView.getParent()).removeView(this.timeRulerView);
        }
        this.time_rule_view_ll2.addView(this.timeRulerView);
        if (this.full_speed.getText().toString().equals("1X")) {
            this.full_speed.setBackgroundResource(R.drawable.speed_unselected_full);
        } else {
            this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
        }
    }

    public void backFullScreen() {
        this.calendarView.setVisibility(0);
        ExoHlsPlayer exoHlsPlayer = this.player;
        if (exoHlsPlayer != null) {
            exoHlsPlayer.setVideoScalingMode(2);
        }
        this.exoPlayerView.setResizeMode(3);
        for (int i = 0; i < this.rightView.getChildCount(); i++) {
            View childAt = this.rightView.getChildAt(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
            layoutParams.rightMargin = 0;
            if (childAt.getId() == R.id.ll_capture) {
                childAt.setPadding(ScreenUtil.dp2Px(getActivity(), 20.0f), 0, 0, 0);
            } else if (childAt.getId() == R.id.ll_listener) {
                childAt.setPadding(0, 0, ScreenUtil.dp2Px(getActivity(), 10.0f), 0);
            }
            childAt.setLayoutParams(layoutParams);
        }
        float f = (ScreenUtil.getDisplayMetrics(getActivity())[1] * 1.0f) / ScreenUtil.getDisplayMetrics(getActivity())[0];
        if (f < 1.85d || f >= 2.0f) {
            int i2 = (f > 2.0f ? 1 : (f == 2.0f ? 0 : -1));
        }
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.card_video_rl.getLayoutParams();
        layoutParams2.height = getResources().getDimensionPixelSize(R.dimen.dimen_200);
        this.card_video_rl.setLayoutParams(layoutParams2);
        this.isFloat = false;
        setFloatBarState();
        this.tv_times.setVisibility(0);
        this.view_line.setVisibility(0);
        this.llSeekTimebar.setVisibility(0);
        this.fl_titlebar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        this.fl_titlebar.setTitleTextColor(R.color.color_black);
        this.fl_titlebar.setTitleBackTextColor(R.color.colorAccent);
        this.tv_times.setTextColor(getResources().getColor(R.color.colors_aeb3c0));
        this.fl_titlebar.setLeftImgId(R.drawable.ic_back);
        this.fl_titlebar.setTitleTextVisible(4);
        if (Build.VERSION.SDK_INT >= 19) {
            LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) this.fl_titlebar.getLayoutParams();
            layoutParams3.height = ScreenUtil.dp2Px(this.f7880activity, 69.0f);
            this.fl_titlebar.setTitleRlPadding(getResources().getDimensionPixelSize(R.dimen.dp_30));
            this.fl_titlebar.setLayoutParams(layoutParams3);
        }
        if (this.ll_title.getParent() != null) {
            ((ViewGroup) this.ll_title.getParent()).removeView(this.ll_title);
        }
        this.ll_root.addView(this.ll_title, 0);
        RelativeLayout.LayoutParams layoutParams4 = (RelativeLayout.LayoutParams) this.tvRecordTime.getLayoutParams();
        layoutParams4.removeRule(3);
        layoutParams4.addRule(12);
        this.tvRecordTime.setLayoutParams(layoutParams4);
        this.bottomView.setVisibility(0);
        RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams) this.playInfoTv.getLayoutParams();
        layoutParams5.addRule(12);
        layoutParams5.leftMargin = ScreenUtil.dp2Px(getActivity(), 30.0f);
        layoutParams5.bottomMargin = ScreenUtil.dp2Px(getActivity(), 9.0f);
        layoutParams5.removeRule(10);
        layoutParams5.removeRule(11);
        layoutParams5.topMargin = 0;
        layoutParams5.rightMargin = 0;
        this.playInfoTv.setLayoutParams(layoutParams5);
        if (this.timeRulerView.getParent() != null) {
            ((ViewGroup) this.timeRulerView.getParent()).removeView(this.timeRulerView);
        }
        this.time_rule_view_ll1.addView(this.timeRulerView);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFloatBarState() {
        this.playInfoTv.setVisibility((this.isFloat && this.player.getPlayState() == 3) ? 0 : 8);
        if (getResources().getConfiguration().orientation == 2) {
            this.rightView.setVisibility(this.isFloat ? 0 : 8);
            if (this.full_speed.getText().toString().equals("1X")) {
                this.full_speed.setBackgroundResource(R.drawable.speed_unselected_full);
            } else {
                this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
            }
            initColor();
            this.full_speed_ll.setVisibility(8);
            this.full_icon.setVisibility(this.isFloat ? 0 : 8);
            this.time_rule_view_ll3.setVisibility(this.isFloat ? 0 : 8);
            return;
        }
        initColor();
        if (this.full_speed.getText().toString().equals("1X")) {
            this.full_speed.setBackgroundResource(R.drawable.speed_unselected_full);
        } else {
            this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
        }
        this.full_speed_ll.setVisibility(8);
        this.full_icon.setVisibility(8);
        this.rightView.setVisibility(8);
        this.time_rule_view_ll3.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long[] getStEtTime() {
        this.curBeginTimeHead = (int) (DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0) / 1000);
        return new long[]{DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0) - (DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 30, 0) - DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0)), DateUtil.getTimeMillins(this.year, this.month, this.day, 23, 59, 59)};
    }

    private long[] getEventStEtTime() {
        this.curBeginTimeHead = (int) (DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0) / 1000);
        return new long[]{DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0), DateUtil.getTimeMillins(this.year, this.month, this.day, 23, 59, 59)};
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.take_picture_ll) {
            ImageView imageView = this.capture_btn;
            if (imageView != null) {
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.video_camera));
            }
            snapshot();
            return;
        }
        if (view2.getId() == R.id.play_btn || view2.getId() == R.id.ll_play) {
            if (this.isPause.getValue().booleanValue()) {
                this.isPause.postValue(false);
                return;
            } else {
                this.isPause.postValue(true);
                return;
            }
        }
        if (view2.getId() == R.id.ll_listener || view2.getId() == R.id.listener_btn) {
            this.speakerSwitch = !this.speakerSwitch;
            this.player.setVolume(this.speakerSwitch ? 1.0f : 0.0f);
            setListen(this.speakerSwitch);
            return;
        }
        if (view2.getId() == R.id.ll_zoom || view2.getId() == R.id.exo_zoom_tbtn) {
            if (this.f7880activity.getRequestedOrientation() == 1) {
                this.f7880activity.setRequestedOrientation(0);
                return;
            } else {
                this.f7880activity.setRequestedOrientation(8);
                return;
            }
        }
        if (view2.getId() == R.id.ll_download) {
            Intent intent = new Intent(getActivity(), (Class<?>) CloudDownloadActivity.class);
            intent.putExtra("iotId", this.f7880activity.iotId);
            startActivity(intent);
            return;
        }
        if (view2.getId() == R.id.play_back_off_ll || view2.getId() == R.id.full_before_ten_seconds_ll) {
            int currentTime = this.timeRulerView.getCurrentTime() - 10;
            int i = currentTime % 3600;
            if (DateUtil.getTimeMillins(currentTime / 3600, i / 60, i % 60) <= 0) {
                seekPlayerPos(0);
                return;
            } else {
                seekPlayerPos(currentTime);
                return;
            }
        }
        if (view2.getId() == R.id.play_fast_forward_ll || view2.getId() == R.id.full_after_ten_seconds_ll) {
            seekPlayerPos(this.timeRulerView.getCurrentTime() + 10);
            return;
        }
        if (view2.getId() == R.id.enlarge_scale_ll) {
            this.timeRulerView.setBigScale();
            return;
        }
        if (view2.getId() == R.id.narrow_scale_ll) {
            this.timeRulerView.setSmallScale();
            return;
        }
        if (view2.getId() == R.id.full_speed) {
            if (this.full_speed_ll.getVisibility() != 0) {
                this.full_speed_ll.setVisibility(0);
                this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
                this.full_speed_ll1.setBackgroundResource(R.drawable.bg_button_blue_radius_20dp);
                String string = this.full_speed.getText().toString();
                byte b2 = -1;
                int iHashCode = string.hashCode();
                if (iHashCode != 1607) {
                    if (iHashCode != 1638) {
                        if (iHashCode != 1700) {
                            if (iHashCode != 1824) {
                                if (iHashCode != 48851) {
                                    if (iHashCode == 1475905 && string.equals("0.5X")) {
                                        b2 = 0;
                                    }
                                } else if (string.equals("16X")) {
                                    b2 = 5;
                                }
                            } else if (string.equals("8X")) {
                                b2 = 4;
                            }
                        } else if (string.equals("4X")) {
                            b2 = 3;
                        }
                    } else if (string.equals("2X")) {
                        b2 = 2;
                    }
                } else if (string.equals("1X")) {
                    b2 = 1;
                }
                switch (b2) {
                    case 0:
                        this.text6.setTextColor(Color.parseColor("#FA5555"));
                        break;
                    case 1:
                        this.text5.setTextColor(Color.parseColor("#FA5555"));
                        break;
                    case 2:
                        this.text4.setTextColor(Color.parseColor("#FA5555"));
                        break;
                    case 3:
                        this.text3.setTextColor(Color.parseColor("#FA5555"));
                        break;
                    case 4:
                        this.text2.setTextColor(Color.parseColor("#FA5555"));
                        break;
                    case 5:
                        this.text1.setTextColor(Color.parseColor("#FA5555"));
                        break;
                }
                return;
            }
            this.full_speed_ll.setVisibility(8);
            if (this.full_speed.getText().toString().equals("1X")) {
                this.full_speed.setBackgroundResource(R.drawable.speed_unselected_full);
            } else {
                this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
            }
            initColor();
            return;
        }
        if (view2.getId() == R.id.text1) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text1.getText());
            this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
            initColor();
            this.speedButton.setImageResource(R.drawable.speed_sixteen);
            this.speedPosition = 5;
            this.player.setPlaybackSpeed(16.0f);
            return;
        }
        if (view2.getId() == R.id.text2) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text2.getText());
            this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
            initColor();
            this.speedButton.setImageResource(R.drawable.speed_eight);
            this.speedPosition = 4;
            this.player.setPlaybackSpeed(8.0f);
            return;
        }
        if (view2.getId() == R.id.text3) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text3.getText());
            this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
            initColor();
            this.speedButton.setImageResource(R.drawable.speed_four);
            this.speedPosition = 3;
            this.player.setPlaybackSpeed(4.0f);
            return;
        }
        if (view2.getId() == R.id.text4) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text4.getText());
            this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
            initColor();
            this.speedButton.setImageResource(R.drawable.speed_two);
            this.speedPosition = 2;
            this.player.setPlaybackSpeed(2.0f);
            return;
        }
        if (view2.getId() == R.id.text5) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text5.getText());
            this.full_speed.setBackgroundResource(R.drawable.speed_unselected_full);
            initColor();
            this.speedButton.setImageResource(R.drawable.speed_one);
            this.speedPosition = 1;
            this.player.setPlaybackSpeed(1.0f);
            return;
        }
        if (view2.getId() == R.id.text6) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text6.getText());
            this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
            initColor();
            this.speedPosition = 0;
            this.speedButton.setImageResource(R.drawable.speed_half_five);
            this.player.setPlaybackSpeed(0.5f);
            return;
        }
        if (view2.getId() == R.id.circle_record_ll) {
            switch2ShotMachine(0);
        } else if (view2.getId() == R.id.shot_record_ll) {
            switch2ShotMachine(1);
        }
    }

    @Override // fragment.CommonFragment
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    protected void initWidget(View view2) {
        super.initWidget(view2);
        this.isPause.postValue(false);
        this.isPause.observe(this, new Observer<Boolean>() { // from class: fragment.YunFragment.1
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Boolean bool) {
                if (bool.booleanValue()) {
                    YunFragment.this.player.pause();
                    YunFragment.this.full_stop.setBackgroundResource(R.drawable.play_record_full);
                    YunFragment.this.playButton.setImageDrawable(YunFragment.this.getResources().getDrawable(R.drawable.play_record));
                } else {
                    YunFragment.this.player.start();
                    YunFragment.this.full_stop.setBackgroundResource(R.drawable.stop_play_record_full);
                    YunFragment.this.playButton.setImageDrawable(YunFragment.this.getResources().getDrawable(R.drawable.stop_play_record));
                }
            }
        });
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.timeTextView = (TextView) view2.findViewById(R.id.time_text);
        this.videoBufferingProgressBar = (ProgressBar) view2.findViewById(R.id.card_video_buffering_bar);
        this.fllayout = (FrameLayout) view2.findViewById(R.id.fllayout);
        this.tvRecordTime = (TextView) view2.findViewById(R.id.tv_record_time);
        this.uiHandler = new Handler(Looper.getMainLooper());
        this.view_line = view2.findViewById(R.id.view_line);
        this.ll_listener = (LinearLayout) view2.findViewById(R.id.ll_listener);
        this.ll_title = (LinearLayout) view2.findViewById(R.id.ll_title);
        this.ll_md = (LinearLayout) view2.findViewById(R.id.ll_md);
        this.tv_times = (TextView) view2.findViewById(R.id.tv_times);
        this.playInfoTv = (TextView) view2.findViewById(R.id.player_info_tv2);
        this.mRecyclerView = (RecyclerView) view2.findViewById(R.id.recyclerview);
        this.ivPlayBefore = (ImageView) view2.findViewById(R.id.iv_b_10);
        this.ivPlayAfter = (ImageView) view2.findViewById(R.id.iv_a_10);
        this.playBtn = (ImageButton) view2.findViewById(R.id.video_play_ibtn);
        this.cardSeekBar = (SeekBar) view2.findViewById(R.id.card_player_seek_bar);
        this.durationTv = (TextView) view2.findViewById(R.id.card_duration_tv);
        this.calendarView = (CalendarView) view2.findViewById(R.id.calendarView_);
        this.ll_date = (LinearLayout) view2.findViewById(R.id.ll_date);
        this.tv_month = (TextView) view2.findViewById(R.id.tv_month);
        this.tv_day = (TextView) view2.findViewById(R.id.tv_day);
        this.listenerBtn = (ImageButton) view2.findViewById(R.id.listener_btn);
        this.exoZoomBtn = (Button) view2.findViewById(R.id.exo_zoom_tbtn);
        this.llSeekTimebar = view2.findViewById(R.id.llSeekTimebar);
        this.rlPlayView = view2.findViewById(R.id.rlPlayView);
        this.rightView = (LinearLayout) view2.findViewById(R.id.rightView);
        this.bottomView = (LinearLayout) view2.findViewById(R.id.bottomView);
        this.iv = (ImageView) view2.findViewById(R.id.iv);
        this.tv_curdate = (TextView) view2.findViewById(R.id.tv_curdate);
        this.ll_capture = (ButtonRipperView) view2.findViewById(R.id.take_picture_ll);
        this.ll_zoom = (LinearLayout) view2.findViewById(R.id.ll_zoom);
        this.ll_root = (LinearLayout) view2.findViewById(R.id.ll_root);
        this.card_video_rl = (RelativeLayout) view2.findViewById(R.id.card_video_rl);
        this.fl_titlebar = (TitleView) view2.findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setLineViewId(0);
        this.fl_title = (LinearLayout) view2.findViewById(R.id.fl_title);
        this.full_speed_ll1 = (LinearLayout) view2.findViewById(R.id.full_speed_ll1);
        this.full_speed = (TextView) view2.findViewById(R.id.full_speed);
        this.full_speed.setOnClickListener(this);
        this.shot_record_text = (TextView) view2.findViewById(R.id.shot_record_text);
        this.circle_record_text = (TextView) view2.findViewById(R.id.circle_record_text);
        this.circle_record_image = (ImageView) view2.findViewById(R.id.circle_record_image);
        this.shot_record_image = (ImageView) view2.findViewById(R.id.shot_record_image);
        this.text1 = (TextView) view2.findViewById(R.id.text1);
        this.text2 = (TextView) view2.findViewById(R.id.text2);
        this.text3 = (TextView) view2.findViewById(R.id.text3);
        this.text4 = (TextView) view2.findViewById(R.id.text4);
        this.text5 = (TextView) view2.findViewById(R.id.text5);
        this.text6 = (TextView) view2.findViewById(R.id.text6);
        this.ll_double_eye_record = (LinearLayout) view2.findViewById(R.id.ll_double_eye_record);
        this.circle_record_ll = (LinearLayout) view2.findViewById(R.id.circle_record_ll);
        this.layout_control = (LinearLayout) view2.findViewById(R.id.layout_control);
        this.shot_record_ll = (LinearLayout) view2.findViewById(R.id.shot_record_ll);
        if (this.f7880activity.iotId2 != null && !"".equals(this.f7880activity.iotId2)) {
            this.ll_double_eye_record.setVisibility(0);
        } else {
            this.ll_double_eye_record.setVisibility(8);
        }
        this.shot_record_ll.setOnClickListener(this);
        this.circle_record_ll.setOnClickListener(this);
        this.layout_control.setOnClickListener(this);
        this.text1.setOnClickListener(this);
        this.text2.setOnClickListener(this);
        this.text3.setOnClickListener(this);
        this.text4.setOnClickListener(this);
        this.text5.setOnClickListener(this);
        this.text6.setOnClickListener(this);
        this.full_speed_ll = (LinearLayout) view2.findViewById(R.id.full_speed_ll);
        this.timeRulerView = (TimeRulerView) view2.findViewById(R.id.time_rule_view);
        this.full_camera = (ImageView) view2.findViewById(R.id.full_camera);
        this.full_sound = (ImageView) view2.findViewById(R.id.full_sound);
        this.full_stop = (ImageView) view2.findViewById(R.id.full_stop);
        this.time_rule_view_ll1 = (LinearLayout) view2.findViewById(R.id.time_rule_view_ll1);
        this.time_rule_view_ll2 = (LinearLayout) view2.findViewById(R.id.time_rule_view_ll2);
        this.time_rule_view_ll3 = (LinearLayout) view2.findViewById(R.id.time_rule_view_ll3);
        this.full_camera.setOnClickListener(new View.OnClickListener() { // from class: fragment.YunFragment.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                YunFragment.this.ll_capture.performClick();
            }
        });
        this.full_sound.setOnClickListener(new View.OnClickListener() { // from class: fragment.YunFragment.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                YunFragment.this.ll_listener.performClick();
            }
        });
        this.full_stop.setOnClickListener(new View.OnClickListener() { // from class: fragment.YunFragment.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                YunFragment.this.playButton.performClick();
            }
        });
        this.full_before_ten_seconds_ll = (LinearLayout) view2.findViewById(R.id.full_before_ten_seconds_ll);
        this.full_after_ten_seconds_ll = (LinearLayout) view2.findViewById(R.id.full_after_ten_seconds_ll);
        this.full_before_ten_seconds_ll.setOnClickListener(this);
        this.full_after_ten_seconds_ll.setOnClickListener(this);
        this.full_before_ten_seconds_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.YunFragment.5
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                return false;
            }
        });
        this.full_after_ten_seconds_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.YunFragment.6
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                return false;
            }
        });
        this.ll_spinner_speed = (LinearLayout) view2.findViewById(R.id.ll_spinner_speed);
        this.play_fast_forward_ll = (ButtonRipperView) view2.findViewById(R.id.play_fast_forward_ll);
        this.ll_play = (LinearLayout) view2.findViewById(R.id.ll_play);
        this.full_icon = (LinearLayout) view2.findViewById(R.id.full_icon);
        this.playButton = (ImageButton) view2.findViewById(R.id.play_btn);
        this.speedButton = (ImageButton) view2.findViewById(R.id.speed_btn);
        this.speedButton.setOnClickListener(new View.OnClickListener() { // from class: fragment.YunFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                YunFragment.this.speedFragment.showAllowingStateLoss(YunFragment.this.getFragmentManager(), "", YunFragment.this.speedPosition);
            }
        });
        this.ll_play.setOnClickListener(this);
        this.playButton.setOnClickListener(this);
        this.ll_spinner_speed.setOnClickListener(this);
        this.play_back_off_ll = (ButtonRipperView) view2.findViewById(R.id.play_back_off_ll);
        this.play_back_off_ll.setOnClickListener(this);
        this.play_back_off_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.YunFragment.8
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    YunFragment.this.play_back_off_ll.setImageResource(R.drawable.back_ten_second_select);
                    YunFragment.this.play_back_off_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() == 1) {
                    YunFragment.this.play_back_off_ll.setImageResource(R.drawable.back_ten_second);
                    YunFragment.this.play_back_off_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                    return false;
                }
                if (motionEvent.getAction() != 3) {
                    return false;
                }
                YunFragment.this.play_back_off_ll.setImageResource(R.drawable.back_ten_second);
                YunFragment.this.play_back_off_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.play_fast_forward_ll.setOnClickListener(this);
        this.play_fast_forward_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.YunFragment.9
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    YunFragment.this.play_fast_forward_ll.setImageResource(R.drawable.speed_ten_second_select);
                    YunFragment.this.play_fast_forward_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() == 1) {
                    YunFragment.this.play_fast_forward_ll.setImageResource(R.drawable.speed_ten_second);
                    YunFragment.this.play_fast_forward_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                    return false;
                }
                if (motionEvent.getAction() != 3) {
                    return false;
                }
                YunFragment.this.play_fast_forward_ll.setImageResource(R.drawable.speed_ten_second);
                YunFragment.this.play_fast_forward_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.enlarge_scale_ll = (ButtonRipperView) view2.findViewById(R.id.enlarge_scale_ll);
        this.enlarge_scale_ll.setOnClickListener(this);
        this.enlarge_scale_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.YunFragment.10
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    YunFragment.this.enlarge_scale_ll.setImageResource(R.drawable.amplification_record_select);
                    YunFragment.this.enlarge_scale_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() == 1) {
                    YunFragment.this.enlarge_scale_ll.setImageResource(R.drawable.amplification_record);
                    YunFragment.this.enlarge_scale_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                    return false;
                }
                if (motionEvent.getAction() != 3) {
                    return false;
                }
                YunFragment.this.enlarge_scale_ll.setImageResource(R.drawable.amplification_record);
                YunFragment.this.enlarge_scale_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.narrow_scale_ll = (ButtonRipperView) view2.findViewById(R.id.narrow_scale_ll);
        this.narrow_scale_ll.setOnClickListener(this);
        this.narrow_scale_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.YunFragment.11
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    YunFragment.this.narrow_scale_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colorAccent));
                    YunFragment.this.narrow_scale_ll.setImageResource(R.drawable.reduce_record_select);
                    return false;
                }
                if (motionEvent.getAction() == 1) {
                    YunFragment.this.narrow_scale_ll.setImageResource(R.drawable.reduce_record);
                    YunFragment.this.narrow_scale_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                    return false;
                }
                if (motionEvent.getAction() != 3) {
                    return false;
                }
                YunFragment.this.narrow_scale_ll.setImageResource(R.drawable.reduce_record);
                YunFragment.this.narrow_scale_ll.setTextColor(YunFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.cloudButton = (CloudButton) view2.findViewById(R.id.cloud_button);
        this.cloudButton.setListener(new CloudButton.ChangeIndex() { // from class: fragment.YunFragment.12
            @Override // kt.CloudButton.ChangeIndex
            public void change(int i) {
                switch (i) {
                    case 0:
                        if (YunFragment.this.finalCloudRecordBeanList == null) {
                            YunFragment.this.mCloudPictureAdapter.setNewData(null);
                        } else {
                            YunFragment.this.mCloudPictureAdapter.replaceData(YunFragment.this.finalCloudRecordBeanList);
                        }
                        break;
                    case 1:
                        if (YunFragment.this.moveRecordBean == null) {
                            YunFragment.this.mCloudPictureAdapter.setNewData(null);
                        } else {
                            YunFragment.this.mCloudPictureAdapter.replaceData(YunFragment.this.moveRecordBean);
                        }
                        break;
                    case 3:
                        if (YunFragment.this.peopleRecordBean == null) {
                            YunFragment.this.mCloudPictureAdapter.setNewData(null);
                        } else {
                            YunFragment.this.mCloudPictureAdapter.replaceData(YunFragment.this.peopleRecordBean);
                        }
                        break;
                    case 11500:
                        if (YunFragment.this.catRecordBean == null) {
                            YunFragment.this.mCloudPictureAdapter.setNewData(null);
                        } else {
                            YunFragment.this.mCloudPictureAdapter.replaceData(YunFragment.this.catRecordBean);
                        }
                        break;
                    case 11501:
                        if (YunFragment.this.dogRecordBean == null) {
                            YunFragment.this.mCloudPictureAdapter.setNewData(null);
                        } else {
                            YunFragment.this.mCloudPictureAdapter.replaceData(YunFragment.this.dogRecordBean);
                        }
                        break;
                }
                YunFragment.this.mCloudPictureAdapter.setCurrent(i);
            }
        });
        this.timeRulerView.setOnTimeChangedListener(new TimeRulerView.OnTimeChangedListener() { // from class: fragment.YunFragment.13
            @Override // view.TimeRulerView.OnTimeChangedListener
            public void onTimeChanged(int i) {
            }

            @Override // view.TimeRulerView.OnTimeChangedListener
            public void onTimeSelected(int i) {
                boolean z = false;
                int size = 0;
                while (true) {
                    if (size >= YunFragment.this.videoInfoList.size()) {
                        size = 0;
                        break;
                    } else {
                        if (i <= DateUtil.getHMSTimeSecs(((CloudInfo) YunFragment.this.videoInfoList.get(size)).endTime)) {
                            z = true;
                            break;
                        }
                        size++;
                    }
                }
                if (YunFragment.this.cloudInfo == null) {
                    return;
                }
                if (!z) {
                    size = YunFragment.this.videoInfoList.size() - 1;
                }
                if (size > 0) {
                    CloudInfo cloudInfo = (CloudInfo) YunFragment.this.videoInfoList.get(size);
                    if (cloudInfo != YunFragment.this.cloudInfo || YunFragment.this.player.getPlayState() != 3) {
                        YunFragment.this.isSeeked = true;
                        YunFragment.this.playVideo(cloudInfo);
                    } else {
                        YunFragment.this.seekPlayerPos(i);
                    }
                }
                YunFragment.this.seekPlayerPos(i);
                YunFragment.this.full_speed_ll.setVisibility(8);
            }

            @Override // view.TimeRulerView.OnTimeChangedListener
            public void seekError() {
                YunFragment.this.player.pause();
                YunFragment.this.dismissBuffering();
                YunFragment.this.dismissPlayButton();
                YunFragment.this.cancelUpdateTimeline();
            }
        });
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: fragment.YunFragment.14
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view3) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view3) {
                YunFragment.this.f7880activity.onBackPressed();
            }
        });
        this.title = this.f7880activity.getIntent().getStringExtra("title");
        this.fl_titlebar.setTitleText(this.title);
        this.fl_titlebar.setTitleTextVisible(4);
        this.alphaAnimation = AnimationUtils.loadAnimation(this.f7880activity, R.anim.alpha_loop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(1);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);
        this.mRecyclerView.setItemAnimator(null);
        this.mCloudPictureAdapter = new CloudPictureAdapter(R.layout.item_cloud_picture);
        this.mCloudPictureAdapter.bindToRecyclerView(this.mRecyclerView);
        this.mCloudPictureAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: fragment.YunFragment.15
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view3, int i) {
                if (YunFragment.this.cloudInfo == null) {
                    return;
                }
                int iMax = Math.max(0, (DateUtil.getHMSTimeMillins(Long.parseLong(YunFragment.this.mCloudPictureAdapter.getData().get(i).picCreateTime)) / 1000) - 5);
                for (CloudInfo cloudInfo : YunFragment.this.videoInfoList) {
                    int hMSTimeSecs = DateUtil.getHMSTimeSecs(cloudInfo.actualBeginTime);
                    int hMSTimeSecs2 = DateUtil.getHMSTimeSecs(cloudInfo.actualEndTime);
                    if (hMSTimeSecs <= iMax && iMax <= hMSTimeSecs2) {
                        if (cloudInfo != YunFragment.this.cloudInfo) {
                            YunFragment.this.isSeeked = true;
                            YunFragment.this.timeRulerView.setCurrentTime(iMax);
                            YunFragment.this.playVideo(cloudInfo);
                        } else if (YunFragment.this.player.getPlayState() != 3) {
                            YunFragment.this.isSeeked = true;
                            YunFragment.this.timeRulerView.setCurrentTime(iMax);
                            YunFragment.this.playVideo(cloudInfo);
                        } else {
                            YunFragment.this.seekPlayerPos(iMax);
                        }
                    }
                }
            }
        });
        this.ll_capture.setOnClickListener(this);
        this.listenerBtn.setOnClickListener(this);
        this.ll_listener.setOnClickListener(this);
        this.exoZoomBtn.setOnClickListener(this);
        this.ll_zoom.setOnClickListener(this);
        this.exoZoomBtn.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.YunFragment.16
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    YunFragment.this.exoZoomBtn.setBackgroundResource(R.drawable.expand_record_selected);
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                YunFragment.this.exoZoomBtn.setBackgroundResource(R.drawable.expand_record);
                return false;
            }
        });
        this.ll_zoom.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.YunFragment.17
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    YunFragment.this.exoZoomBtn.setBackgroundResource(R.drawable.expand_record_selected);
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                YunFragment.this.exoZoomBtn.setBackgroundResource(R.drawable.expand_record);
                return false;
            }
        });
        this.ll_capture.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.YunFragment.18
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    YunFragment.this.ll_capture.setImageResource(R.drawable.take_picture_record_select);
                    YunFragment.this.ll_capture.setTextColor(YunFragment.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                YunFragment.this.ll_capture.setImageResource(R.drawable.take_picture_record);
                YunFragment.this.ll_capture.setTextColor(YunFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.ivPlayBefore.setOnClickListener(new View.OnClickListener() { // from class: fragment.YunFragment.19
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
            }
        });
        this.ivPlayAfter.setOnClickListener(new View.OnClickListener() { // from class: fragment.YunFragment.20
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
            }
        });
        this.playBtn.setOnClickListener(new View.OnClickListener() { // from class: fragment.YunFragment.21
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                YunFragment.this.dismissPlayButton();
                YunFragment yunFragment = YunFragment.this;
                yunFragment.playVideo(yunFragment.cloudInfo);
            }
        });
        this.calendarView.setSelectSingleMode();
        this.calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() { // from class: fragment.YunFragment.22
            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarOutOfRange(CalendarModel calendarModel) {
                YunFragment yunFragment = YunFragment.this;
                yunFragment.showToast(yunFragment.getString(R.string.start_below_current_date));
            }

            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarSelect(CalendarModel calendarModel, boolean z) {
                if (z) {
                    YunFragment.this.tv_month.setText(String.format("%02d", Integer.valueOf(calendarModel.getMonth())));
                    YunFragment.this.tv_day.setText(String.format("%02d", Integer.valueOf(calendarModel.getDay())));
                    YunFragment.this.year = calendarModel.getYear();
                    YunFragment.this.month = calendarModel.getMonth();
                    YunFragment.this.day = calendarModel.getDay();
                    long[] stEtTime = YunFragment.this.getStEtTime();
                    long[] stEtTime2 = YunFragment.this.getStEtTime();
                    YunFragment.this.curBeginTime = (int) (stEtTime[0] / 1000);
                    YunFragment.this.curEndTime = (int) (stEtTime[1] / 1000);
                    YunFragment.this.tv_curdate.setTextColor(YunFragment.this.getResources().getColor(R.color.colorAccent));
                    YunFragment.this.resetData();
                    YunFragment.this.isRequestNone = true;
                    YunFragment.this.isSeeked = false;
                    YunFragment.this.isToday = false;
                    YunFragment yunFragment = YunFragment.this;
                    yunFragment.getCloudRecordList(yunFragment.curBeginTime, YunFragment.this.curEndTime, true, false);
                    YunFragment.this.mCloudPictureAdapter.setNewData(null);
                    if (YunFragment.this.dogRecordBean != null) {
                        YunFragment.this.dogRecordBean.clear();
                    }
                    if (YunFragment.this.finalCloudRecordBeanList != null) {
                        YunFragment.this.finalCloudRecordBeanList.clear();
                    }
                    if (YunFragment.this.catRecordBean != null) {
                        YunFragment.this.catRecordBean.clear();
                    }
                    if (YunFragment.this.peopleRecordBean != null) {
                        YunFragment.this.peopleRecordBean.clear();
                    }
                    if (YunFragment.this.moveRecordBean != null) {
                        YunFragment.this.moveRecordBean.clear();
                    }
                    YunFragment.this.count = 0;
                    YunFragment.this.cloudButton.setCurrentIndex(0);
                    YunFragment yunFragment2 = YunFragment.this;
                    yunFragment2.getEventRecordList(yunFragment2.f7880activity.iotId, stEtTime2[0], stEtTime2[1], 0, 0, 100);
                    YunFragment.this.ll_date.setVisibility(8);
                    YunFragment.this.isCalendarShow = false;
                    YunFragment.this.mRecyclerView.setVisibility(0);
                }
            }
        });
        this.calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() { // from class: fragment.YunFragment.23
            @Override // com.haibin.calendarview.CalendarView.OnMonthChangeListener
            public void onMonthChange(int i, int i2) {
                if (YunFragment.this.isAdded()) {
                    YunFragment.this.tv_curdate.setText(i + Constants.ACCEPT_TIME_SEPARATOR_SERVER + String.format("%02d", Integer.valueOf(i2)));
                    if (YunFragment.this.year != i || YunFragment.this.month != i2) {
                        YunFragment.this.tv_curdate.setTextColor(YunFragment.this.getResources().getColor(R.color.color_black));
                    } else {
                        YunFragment.this.tv_curdate.setTextColor(YunFragment.this.getResources().getColor(R.color.colorAccent));
                    }
                }
            }
        });
        this.cardSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: fragment.YunFragment.24
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (YunFragment.this.player.getDuration() > 0) {
                    int duration = (int) ((((long) i) * YunFragment.this.player.getDuration()) / ((long) YunFragment.this.cardSeekBar.getMax()));
                    YunFragment.this.durationTv.setText(YunFragment.this.timeLineFormatter.format(Integer.valueOf(duration)) + "/" + YunFragment.this.timeLineFormatter.format(Long.valueOf(YunFragment.this.player.getDuration())));
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = (int) ((((long) seekBar.getProgress()) * YunFragment.this.player.getDuration()) / ((long) YunFragment.this.cardSeekBar.getMax()));
                if (YunFragment.this.player.getPlayState() == 3 || YunFragment.this.player.getPlayState() == 2) {
                    YunFragment.this.player.seekTo(progress);
                    YunFragment.this.updateTimeline();
                } else {
                    YunFragment yunFragment = YunFragment.this;
                    yunFragment.playVideo(yunFragment.cloudInfo);
                }
            }
        });
        this.exoPlayerView = (SimpleExoPlayerView) view2.findViewById(R.id.card_player_surface_view);
        this.fllayout.setClickable(true);
        this.fllayout.setOnClickListener(new View.OnClickListener() { // from class: fragment.YunFragment.25
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                YunFragment.this.isFloat = !r2.isFloat;
                YunFragment.this.setFloatBarState();
            }
        });
        this.player = new ExoHlsPlayer(getActivity());
        this.player.setVideoScalingMode(2);
        this.exoPlayerView.setPlayer(this.player.getExoPlayer());
        this.exoPlayerView.requestFocus();
        this.exoPlayerView.setResizeMode(3);
        this.player.setVolume(this.speakerSwitch ? 1.0f : 0.0f);
        this.player.setCirclePlay(false);
        setListen(this.speakerSwitch);
        this.player.setOnCompletionListener(new OnCompletionListener() { // from class: fragment.YunFragment.26
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnCompletionListener
            public void onCompletion() {
            }
        });
        this.player.setOnErrorListener(new OnErrorListener() { // from class: fragment.YunFragment.27
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
            public void onError(final PlayerException playerException) {
                YunFragment.this.f7880activity.runOnUiThread(new Runnable() { // from class: fragment.YunFragment.27.1
                    @Override // java.lang.Runnable
                    @SuppressLint({"StringFormatInvalid"})
                    public void run() {
                        if (YunFragment.this.getActivity() == null || YunFragment.this.getActivity().isFinishing()) {
                            return;
                        }
                        YunFragment.this.dismissBuffering();
                        YunFragment.this.cancelUpdateTimeline();
                        switch (playerException.getCode()) {
                            case 6:
                                switch (playerException.getSubCode()) {
                                    case 1005:
                                        YunFragment.this.showToast(YunFragment.this.getString(R.string.connect_failed, Integer.valueOf(playerException.getSubCode())));
                                        break;
                                    case 1006:
                                        YunFragment.this.showToast(YunFragment.this.getString(R.string.connect_failed, Integer.valueOf(playerException.getSubCode())));
                                        break;
                                    case 1007:
                                        YunFragment.this.showToast(YunFragment.this.getString(R.string.connect_failed, Integer.valueOf(playerException.getSubCode())));
                                        break;
                                    case 1008:
                                        YunFragment.this.showToast(YunFragment.this.getString(R.string.connect_failed, Integer.valueOf(playerException.getSubCode())));
                                        break;
                                    case 1009:
                                        YunFragment.this.showToast(YunFragment.this.getString(R.string.connect_failed, Integer.valueOf(playerException.getSubCode())));
                                        break;
                                }
                                break;
                            case 7:
                                if (playerException.getSubCode() == 1000) {
                                    YunFragment.this.showToast(YunFragment.this.getString(R.string.play_failed_retry, Integer.valueOf(playerException.getSubCode())));
                                }
                                break;
                            case 8:
                                if (playerException.getSubCode() == 1100) {
                                    YunFragment.this.showToast(YunFragment.this.getString(R.string.play_failed_retry, Integer.valueOf(playerException.getSubCode())));
                                }
                                break;
                        }
                        YunFragment.this.showPlayButton();
                    }
                });
            }
        });
        this.player.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: fragment.YunFragment.28
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                int iIndexOf;
                boolean z = false;
                switch (i) {
                    case 1:
                        Log.d(YunFragment.TAG, "onPlayerStateChange: puppet:PlayerState.STATE_IDLE");
                        break;
                    case 2:
                        Log.d(YunFragment.TAG, "onPlayerStateChange: puppet:PlayerState.STATE_BUFFERING");
                        if (YunFragment.this.isRequestNone || YunFragment.this.videoInfoList.size() > 0) {
                            YunFragment.this.dismissPlayButton();
                            YunFragment.this.showBuffering();
                        }
                        break;
                    case 3:
                        Log.d(YunFragment.TAG, "onPlayerStateChange: puppet:PlayerState.STATE_READY");
                        if (YunFragment.this.isSeeked) {
                            YunFragment.this.isSeeked = false;
                            if (YunFragment.this.cloudInfo != null) {
                                if (YunFragment.this.isToday) {
                                    YunFragment.this.seekPlayerPos(YunFragment.this.timeRulerView.getCurrentTime());
                                } else {
                                    YunFragment.this.seekPlayerPos(0);
                                }
                            }
                        }
                        YunFragment.this.dismissBuffering();
                        YunFragment.this.showPlayInfo();
                        YunFragment.this.updateTimeline();
                        break;
                    case 4:
                        Log.d(YunFragment.TAG, "onPlayerStateChange: puppet:PlayerState.STATE_ENDED");
                        if (YunFragment.this.cloudInfo != null && (iIndexOf = YunFragment.this.videoInfoList.indexOf(YunFragment.this.cloudInfo)) > -1) {
                            CloudInfo cloudInfo = null;
                            int i2 = iIndexOf + 1;
                            int i3 = i2;
                            while (true) {
                                if (i3 < YunFragment.this.videoInfoList.size()) {
                                    CloudInfo cloudInfo2 = (CloudInfo) YunFragment.this.videoInfoList.get(i3);
                                    boolean z2 = YunFragment.this.cloudInfo.actualEndTime > cloudInfo2.beginTime;
                                    if (cloudInfo2.actualEndTime > YunFragment.this.cloudInfo.actualEndTime) {
                                        cloudInfo = cloudInfo2;
                                        z = z2;
                                    } else {
                                        i3++;
                                    }
                                }
                            }
                            if (iIndexOf < YunFragment.this.videoInfoList.size() - 1) {
                                if (cloudInfo == null) {
                                    cloudInfo = (CloudInfo) YunFragment.this.videoInfoList.get(i2);
                                }
                                if (z) {
                                    YunFragment.this.isSeeked = true;
                                }
                                YunFragment.this.playVideo(cloudInfo);
                            }
                        }
                        break;
                }
            }
        });
        this.ll_md.setOnClickListener(new View.OnClickListener() { // from class: fragment.YunFragment.29
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                YunFragment.this.isCalendarShow = !r4.isCalendarShow;
                YunFragment.this.ll_date.setVisibility(YunFragment.this.ll_date.getVisibility() == 0 ? 8 : 0);
                if (YunFragment.this.isCalendarShow) {
                    YunFragment.this.mRecyclerView.setVisibility(8);
                    YunFragment.this.layout_control.setVisibility(8);
                } else {
                    YunFragment.this.mRecyclerView.setVisibility(0);
                    YunFragment.this.layout_control.setVisibility(0);
                }
            }
        });
        this.ll_download = (LinearLayout) view2.findViewById(R.id.ll_download);
        this.ll_download.setOnClickListener(this);
        this.year = DateUtil.getYear();
        this.month = DateUtil.getMonth();
        this.day = DateUtil.getDay();
        this.tv_month.setText(String.format("%02d", Integer.valueOf(this.month)));
        this.tv_day.setText(String.format("%02d", Integer.valueOf(this.day)));
        this.tv_curdate.setText(this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + String.format("%02d", Integer.valueOf(this.month)));
        this.calendarView.setRange(2000, 1, 1, this.year, this.month, this.day);
        long[] stEtTime = getStEtTime();
        long[] eventStEtTime = getEventStEtTime();
        this.curBeginTime = (int) (stEtTime[0] / 1000);
        this.curEndTime = (int) (stEtTime[1] / 1000);
        setFloatBarState();
        float f = (ScreenUtil.getDisplayMetrics(getActivity())[1] * 1.0f) / ScreenUtil.getDisplayMetrics(getActivity())[0];
        if (f < 1.85d || f >= 2.0f) {
            int i = (f > 2.0f ? 1 : (f == 2.0f ? 0 : -1));
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.card_video_rl.getLayoutParams();
        layoutParams.height = (int) ((ScreenUtil.getDisplayMetrics(getActivity())[0] * 9.0f) / 16.0f);
        this.card_video_rl.setLayoutParams(layoutParams);
        this.isRequestNone = true;
        this.isToday = true;
        Log.e("播放", "开始查询云回放");
        getCloudRecordList(this.curBeginTime, this.curEndTime, true, true);
        getEventRecordList(this.f7880activity.iotId, eventStEtTime[0], eventStEtTime[1], 0, 0, 100);
        getMonthList(this.f7880activity.iotId, this.year, this.month);
        this.speedFragment = new SelectorDialogFragment(getString(R.string.multiple_play), this.speeds);
        this.speedFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: fragment.YunFragment.30
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i2) {
                switch (i2) {
                    case 0:
                        YunFragment.this.full_speed.setText("0.5X");
                        YunFragment.this.player.setPlaybackSpeed(0.5f);
                        YunFragment.this.speedButton.setImageResource(R.drawable.speed_half_five);
                        YunFragment.this.speedPosition = 0;
                        break;
                    case 1:
                        YunFragment.this.full_speed.setText("1X");
                        YunFragment.this.player.setPlaybackSpeed(1.0f);
                        YunFragment.this.speedButton.setImageResource(R.drawable.speed_one);
                        YunFragment.this.speedPosition = 1;
                        break;
                    case 2:
                        YunFragment.this.full_speed.setText("2X");
                        YunFragment.this.player.setPlaybackSpeed(2.0f);
                        YunFragment.this.speedButton.setImageResource(R.drawable.speed_two);
                        YunFragment.this.speedPosition = 2;
                        break;
                    case 3:
                        YunFragment.this.full_speed.setText("4X");
                        YunFragment.this.player.setPlaybackSpeed(4.0f);
                        YunFragment.this.speedButton.setImageResource(R.drawable.speed_four);
                        YunFragment.this.speedPosition = 3;
                        break;
                    case 4:
                        YunFragment.this.full_speed.setText("8X");
                        YunFragment.this.player.setPlaybackSpeed(8.0f);
                        YunFragment.this.speedButton.setImageResource(R.drawable.speed_eight);
                        YunFragment.this.speedPosition = 4;
                        break;
                    case 5:
                        YunFragment.this.full_speed.setText("16X");
                        YunFragment.this.player.setPlaybackSpeed(16.0f);
                        YunFragment.this.speedButton.setImageResource(R.drawable.speed_sixteen);
                        YunFragment.this.speedPosition = 5;
                        break;
                }
            }
        });
    }

    private void getMonthList(String str, final int i, final int i2) {
        HashMap map = new HashMap();
        String str2 = new DecimalFormat("00").format(i2);
        map.put("iotId", str);
        map.put(SpUtil.MONTH, i + "" + str2);
        map.put("timeZone", 8);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/monthrecord/query").setApiVersion(BuildConfig.VERSION_NAME).setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.YunFragment.31
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                int code = ioTResponse.getCode();
                Log.e(YunFragment.TAG, "shareDevice onResponse: code: " + code);
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
                YunFragment.this.drawCircle(i, i2, arrayList);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawCircle(int i, int i2, List<Integer> list) {
        HashMap map = new HashMap();
        for (int i3 = 0; i3 < list.size(); i3++) {
            map.put(getSchemeCalendar(i, i2, list.get(i3).intValue(), -12526811, "").toString(), getSchemeCalendar(i, i2, list.get(i3).intValue(), -12526811, ""));
        }
        this.calendarView.setSchemeDate(map);
    }

    private CalendarModel getSchemeCalendar(int i, int i2, int i3, int i4, String str) {
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.setYear(i);
        calendarModel.setMonth(i2);
        calendarModel.setDay(i3);
        return calendarModel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlayButton() {
        this.videoBufferingProgressBar.setVisibility(8);
        this.playBtn.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPlayButton() {
        this.playBtn.setVisibility(8);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netEventChange(NetWorkEvent netWorkEvent) {
        if (this.player == null) {
            return;
        }
        this.isSeeked = false;
        playVideo(this.cloudInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getEventRecordList(final String str, final long j, long j2, int i, int i2, int i3) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("beginTime", Long.valueOf(j));
        map.put(AUserTrack.UTKEY_END_TIME, Long.valueOf(j2));
        map.put("eventType", Integer.valueOf(i));
        map.put("pageStart", Integer.valueOf(i2));
        map.put(AlinkConstants.KEY_PAGE_SIZE, Integer.valueOf(i3));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/event/query").setApiVersion("2.1.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.YunFragment.32
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                LogEx.d(true, YunFragment.TAG, exc.getLocalizedMessage());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                LogEx.e(true, YunFragment.TAG, "getEventRecordList:" + ioTResponse.getData() + "");
                int code = ioTResponse.getCode();
                ioTResponse.getLocalizedMsg();
                if (code != 200) {
                    return;
                }
                try {
                    EventInfo eventInfo = (EventInfo) JSON.parseObject(ioTResponse.getData().toString(), EventInfo.class);
                    YunFragment.this.uiHandler.post(new Runnable() { // from class: fragment.YunFragment.32.1
                        @Override // java.lang.Runnable
                        public void run() {
                            if (YunFragment.this.f7880activity == null || YunFragment.this.f7880activity.isFinishing() || !YunFragment.this.isAdded()) {
                                return;
                            }
                            YunFragment.this.mCloudPictureAdapter.setNewData(null);
                        }
                    });
                    if (eventInfo.eventList.size() <= 0 || !YunFragment.this.isAdded()) {
                        return;
                    }
                    if (YunFragment.this.eventList == null) {
                        YunFragment.this.eventList = new ArrayList();
                    }
                    YunFragment.this.eventList.addAll(eventInfo.eventList);
                    YunFragment.this.requestPicture(str, eventInfo.eventList, null, j);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void requestPicture(final String str, final List<EventInfo.Info> list, List<CloudRecordBean> list2, final long j) {
        if (list2 == null) {
            list2 = new ArrayList<>();
        }
        final List<EventInfo.Info> listSubList = list.subList(0, list.size() < 20 ? list.size() : 20);
        ArrayList arrayList = new ArrayList();
        for (EventInfo.Info info : listSubList) {
            if (!TextUtils.isEmpty(info.eventPicId)) {
                arrayList.add(info.eventPicId);
            }
        }
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("pictureIdList", arrayList);
        map.put("type", 0);
        IoTRequest ioTRequestBuild = new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/picture/querybyids").setApiVersion("2.1.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build();
        IoTAPIClient client = new IoTAPIClientFactory().getClient();
        if (this.finalCloudRecordBeanList == null) {
            this.finalCloudRecordBeanList = list2;
        }
        if (this.moveRecordBean == null) {
            this.moveRecordBean = new ArrayList();
        }
        if (this.peopleRecordBean == null) {
            this.peopleRecordBean = new ArrayList();
        }
        if (this.catRecordBean == null) {
            this.catRecordBean = new ArrayList();
        }
        if (this.dogRecordBean == null) {
            this.dogRecordBean = new ArrayList();
        }
        client.send(ioTRequestBuild, new IoTCallback() { // from class: fragment.YunFragment.33
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() == 200) {
                    if (YunFragment.this.count == 4 && list.size() >= 20) {
                        YunFragment.this.count = 0;
                        List list3 = list;
                        YunFragment.this.getEventRecordList(str, j, ((EventInfo.Info) list3.get(list3.size() - 1)).eventTime, 0, 0, 100);
                    }
                    try {
                        LogEx.e(true, YunFragment.TAG, "getEventRecordList:getPicDetail:" + ioTResponse.getData());
                        final PicInfoByIds picInfoByIds = (PicInfoByIds) JSON.parseObject(ioTResponse.getData().toString(), PicInfoByIds.class);
                        YunFragment.this.uiHandler.post(new Runnable() { // from class: fragment.YunFragment.33.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (YunFragment.this.f7880activity == null || YunFragment.this.f7880activity.isFinishing() || !YunFragment.this.isAdded()) {
                                    return;
                                }
                                CloudInfo cloudInfo = YunFragment.this.videoInfoList.size() > 0 ? (CloudInfo) YunFragment.this.videoInfoList.get(YunFragment.this.videoInfoList.size() - 1) : null;
                                for (PicInfoByIds.Picture picture : picInfoByIds.pictureList) {
                                    if (!AppConfig.isChina) {
                                        picture.pictureTime = DateUtil.chinaToTimeZome(picture.pictureTime);
                                    }
                                    CloudRecordBean cloudRecordBean = new CloudRecordBean();
                                    Iterator it = listSubList.iterator();
                                    while (true) {
                                        if (it.hasNext()) {
                                            EventInfo.Info info2 = (EventInfo.Info) it.next();
                                            if (!TextUtils.isEmpty(info2.eventPicId) && info2.eventPicId.equals(picture.pictureId)) {
                                                if (info2.intelligentTypeList == null) {
                                                    int i = info2.eventType;
                                                    if (i != 1) {
                                                        switch (i) {
                                                            case 10005:
                                                                cloudRecordBean.desc = YunFragment.this.f7880activity.getString(R.string.car);
                                                                break;
                                                            case 10006:
                                                                cloudRecordBean.desc = YunFragment.this.f7880activity.getString(R.string.awaken);
                                                                break;
                                                            default:
                                                                cloudRecordBean.desc = YunFragment.this.f7880activity.getString(R.string.other_alarm);
                                                                break;
                                                        }
                                                    } else {
                                                        cloudRecordBean.desc = YunFragment.this.f7880activity.getString(R.string.motion_detect_alarm);
                                                    }
                                                } else {
                                                    int i2 = info2.eventType;
                                                    if (i2 != 1) {
                                                        switch (i2) {
                                                            case 10005:
                                                                cloudRecordBean.desc = YunFragment.this.f7880activity.getString(R.string.car);
                                                                break;
                                                            case 10006:
                                                                cloudRecordBean.desc = YunFragment.this.f7880activity.getString(R.string.awaken);
                                                                break;
                                                            default:
                                                                cloudRecordBean.desc = YunFragment.this.f7880activity.getString(R.string.other_alarm);
                                                                break;
                                                        }
                                                    } else {
                                                        cloudRecordBean.desc = YunFragment.this.f7880activity.getString(R.string.motion_detect_alarm);
                                                    }
                                                }
                                                cloudRecordBean.eventType = info2.eventType;
                                                cloudRecordBean.eventId = info2.eventId;
                                                cloudRecordBean.tagList = info2.tagList;
                                            }
                                        }
                                    }
                                    cloudRecordBean.picCreateTime = String.valueOf(DateUtil.getDateTime(picture.pictureTime, DateUtil.sdf2));
                                    cloudRecordBean.iotId = str;
                                    cloudRecordBean.thumbUrl = picture.thumbUrl;
                                    if (cloudInfo != null) {
                                        if (((long) cloudInfo.endTime) * 1000 >= Long.parseLong(cloudRecordBean.picCreateTime) && YunFragment.this.verifyTimeRect(cloudInfo)) {
                                            cloudRecordBean.isShow = true;
                                        } else {
                                            cloudRecordBean.isShow = false;
                                        }
                                    }
                                    if (cloudRecordBean.desc.equals(YunFragment.this.f7880activity.getString(R.string.motion_detect_alarm))) {
                                        YunFragment.this.moveRecordBean.add(cloudRecordBean);
                                    } else if (cloudRecordBean.desc.equals("人形侦测告警")) {
                                        YunFragment.this.peopleRecordBean.add(cloudRecordBean);
                                    } else if (cloudRecordBean.desc.equals("猫咪侦测告警")) {
                                        YunFragment.this.catRecordBean.add(cloudRecordBean);
                                    } else if (cloudRecordBean.desc.equals("狗狗侦测告警")) {
                                        YunFragment.this.dogRecordBean.add(cloudRecordBean);
                                    } else {
                                        YunFragment.this.moveRecordBean.add(cloudRecordBean);
                                    }
                                    if (cloudRecordBean.eventType != 2) {
                                        YunFragment.this.finalCloudRecordBeanList.add(cloudRecordBean);
                                    }
                                }
                                YunFragment.this.mCloudPictureAdapter.replaceData(YunFragment.this.finalCloudRecordBeanList);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                YunFragment.this.uiHandler.post(new Runnable() { // from class: fragment.YunFragment.33.2
                    @Override // java.lang.Runnable
                    public void run() {
                        if (YunFragment.this.f7880activity == null || YunFragment.this.f7880activity.isFinishing() || list.size() <= listSubList.size()) {
                            return;
                        }
                        YunFragment.access$3908(YunFragment.this);
                        YunFragment.this.requestPicture(str, list.subList(listSubList.size(), list.size()), YunFragment.this.finalCloudRecordBeanList, j);
                    }
                });
            }
        });
    }

    private void setListen(boolean z) {
        int i = this.f7880activity.getResources().getConfiguration().orientation;
        int i2 = R.drawable.record_sound_origion;
        int i3 = R.drawable.record_sound;
        if (i == 2) {
            ImageView imageView = this.full_sound;
            if (z) {
                i3 = R.drawable.silence_record;
            }
            imageView.setBackgroundResource(i3);
            ImageButton imageButton = this.listenerBtn;
            if (z) {
                i2 = R.drawable.silence_record_origion;
            }
            imageButton.setImageResource(i2);
            return;
        }
        ImageView imageView2 = this.full_sound;
        if (!z) {
            i3 = R.drawable.silence_record;
        }
        imageView2.setBackgroundResource(i3);
        ImageButton imageButton2 = this.listenerBtn;
        if (!z) {
            i2 = R.drawable.silence_record_origion;
        }
        imageButton2.setImageResource(i2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showToast(final String str) {
        this.f7880activity.runOnUiThread(new Runnable() { // from class: fragment.YunFragment.34
            @Override // java.lang.Runnable
            public void run() {
                if (YunFragment.this.getActivity() == null || YunFragment.this.getActivity().isFinishing()) {
                    return;
                }
                Toast.makeText(YunFragment.this.f7880activity, str, 0).show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void cancelUpdateTimeline() {
        ScheduledFuture<?> scheduledFuture = this.timelineUpdateHandle;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.timelineUpdateHandle = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateTimeline() {
        if (this.timelineUpdateHandle == null) {
            this.timelineUpdateHandle = this.scheduledExecutorService.scheduleAtFixedRate(this.timelineUpdateTask, 100L, 100L, TimeUnit.MILLISECONDS);
        }
        ExoHlsPlayer exoHlsPlayer = this.player;
        if (exoHlsPlayer != null) {
            long duration = exoHlsPlayer.getDuration();
            if (duration <= 0) {
                this.durationTv.setText("-/-");
                this.cardSeekBar.setProgress(0);
                this.timelineUpdateHandle.cancel(true);
                this.timelineUpdateHandle = null;
                return;
            }
            this.cardSeekBar.setProgress((int) ((this.player.getCurrentPosition() * ((long) this.cardSeekBar.getMax())) / duration));
            this.durationTv.setText(this.timeLineFormatter.format(Long.valueOf(this.player.getCurrentPosition())) + "/" + this.timeLineFormatter.format(Long.valueOf(duration)));
            long currentPosition = this.player.getCurrentPosition();
            if (currentPosition >= duration) {
                stopVideo();
            } else if (this.isSeeked) {
                return;
            } else {
                seekTimeBarPos(currentPosition);
            }
            this.tv_times.setText(DateUtil.getCompleteTime(getActivity(), this.year, this.month, this.day, (int) currentPosition));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlayInfo() {
        if (this.isFloat) {
            this.playInfoTv.setVisibility(0);
        }
        updatePlayInfo();
    }

    private void dismissPlayInfo() {
        this.playInfoTv.setVisibility(8);
        ScheduledFuture<?> scheduledFuture = this.updatePlayInfoHandle;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            this.updatePlayInfoHandle = null;
        }
    }

    public void updatePlayInfo() {
        if (this.updatePlayInfoHandle == null) {
            this.updatePlayInfoHandle = this.scheduledExecutorService.scheduleAtFixedRate(this.updatePlayInfoTimerTask, 1L, 1L, TimeUnit.SECONDS);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.player.start();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void playVideo(CloudInfo cloudInfo) {
        if (cloudInfo == null) {
            return;
        }
        stopVideo();
        initPlayVideo(cloudInfo);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.player.pause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        dismissPlayInfo();
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.tvRecordTime.setText("");
        this.tvRecordTime.setVisibility(8);
    }

    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        cancelUpdateTimeline();
        stopVideo();
        this.player.release();
        this.uiHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

    private void initPlayVideo(CloudInfo cloudInfo) {
        cancelUpdateTimeline();
        stopVideo();
        this.player.setDataSourceByIPCRecordFileName(this.f7880activity.iotId, cloudInfo.fileName);
        this.player.setOnPreparedListener(new OnPreparedListener() { // from class: fragment.YunFragment.37
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                YunFragment.this.player.start();
            }
        });
        this.player.prepare();
        keepScreenLight();
        this.cloudInfo = cloudInfo;
        if (this.cloudInfo.isBeginTimeLastDay) {
            this.isSeeked = true;
        }
    }

    private void stopVideo() {
        this.player.stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBuffering() {
        this.f7880activity.runOnUiThread(new Runnable() { // from class: fragment.YunFragment.38
            @Override // java.lang.Runnable
            public void run() {
                if (YunFragment.this.getActivity() == null || YunFragment.this.getActivity().isFinishing()) {
                    return;
                }
                YunFragment.this.videoBufferingProgressBar.setVisibility(0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissBuffering() {
        this.f7880activity.runOnUiThread(new Runnable() { // from class: fragment.YunFragment.39
            @Override // java.lang.Runnable
            public void run() {
                if (YunFragment.this.getActivity() == null || YunFragment.this.getActivity().isFinishing()) {
                    return;
                }
                YunFragment.this.videoBufferingProgressBar.setVisibility(8);
            }
        });
    }

    public void getCloudRecordList(final int i, final int i2, final boolean z, final boolean z2) {
        HashMap map = new HashMap();
        map.put("iotId", this.f7880activity.iotId);
        map.put("beginTime", Integer.valueOf(i));
        map.put(AUserTrack.UTKEY_END_TIME, Integer.valueOf(i2));
        map.put(AlinkConstants.KEY_PAGE_SIZE, 500);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/record/query").setApiVersion("2.1.3").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.YunFragment.40
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                ALog.e(YunFragment.TAG, exc.getLocalizedMessage());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
                ALog.e(YunFragment.TAG, ioTResponse.getData() + "");
                if (ioTResponse.getCode() != 200) {
                    return;
                }
                YunFragment.this.cloudVideoBean = (CloudVideo) new Gson().fromJson(ioTResponse.getData().toString(), CloudVideo.class);
                if (YunFragment.this.f7880activity.isFirst) {
                    if (YunFragment.this.cloudVideoBean.getRecordFileList().size() == 0) {
                        if (SharePreferenceManager.getInstance().getStorageStatus(YunFragment.this.f7880activity.iotId) == 0) {
                            YunFragment.this.uiHandler.post(new Runnable() { // from class: fragment.YunFragment.40.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    if (YunFragment.this.f7880activity == null || YunFragment.this.f7880activity.isFinishing() || !YunFragment.this.isAdded()) {
                                        return;
                                    }
                                    YunFragment.this.initCardVideoListView(i, i2, ioTResponse, z, z2);
                                }
                            });
                        }
                    } else {
                        YunFragment.this.uiHandler.post(new Runnable() { // from class: fragment.YunFragment.40.2
                            @Override // java.lang.Runnable
                            public void run() {
                                if (YunFragment.this.f7880activity == null || YunFragment.this.f7880activity.isFinishing() || !YunFragment.this.isAdded()) {
                                    return;
                                }
                                YunFragment.this.initCardVideoListView(i, i2, ioTResponse, z, z2);
                            }
                        });
                    }
                    YunFragment.this.f7880activity.isFirst = false;
                    return;
                }
                YunFragment.this.uiHandler.post(new Runnable() { // from class: fragment.YunFragment.40.3
                    @Override // java.lang.Runnable
                    public void run() {
                        if (YunFragment.this.f7880activity == null || YunFragment.this.f7880activity.isFinishing() || !YunFragment.this.isAdded()) {
                            return;
                        }
                        YunFragment.this.initCardVideoListView(i, i2, ioTResponse, z, z2);
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean verifyTimeRect(CloudInfo cloudInfo) {
        return cloudInfo.endTime >= this.curBeginTimeHead && this.curEndTime >= cloudInfo.beginTime;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initCardVideoListView(int i, int i2, IoTResponse ioTResponse, boolean z, boolean z2) {
        CloudInfo cloudInfo;
        RecordVideoActivity recordVideoActivity = this.f7880activity;
        if (recordVideoActivity == null || recordVideoActivity.isFinishing()) {
            return;
        }
        try {
            Object data = ioTResponse.getData();
            if (data != null) {
                JSONArray jSONArray = ((JSONObject) data).getJSONArray("recordFileList");
                if (jSONArray != null && jSONArray.length() > 0) {
                    if (this.videoInfoList == null) {
                        this.videoInfoList = new ArrayList();
                    }
                    int i3 = 0;
                    for (int i4 = 0; i4 < jSONArray.length(); i4++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i4);
                        CloudInfo cloudInfo2 = new CloudInfo();
                        cloudInfo2.iotId = this.f7880activity.iotId;
                        cloudInfo2.fileName = jSONObject.getString("fileName");
                        try {
                            cloudInfo2.fileSize = jSONObject.getInt("fileSize");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        cloudInfo2.recordType = jSONObject.getInt("recordType");
                        cloudInfo2.beginTime = (int) (DateUtil.getDateTime(jSONObject.getString("beginTime"), DateUtil.sdf2) / 1000);
                        cloudInfo2.endTime = (int) (DateUtil.getDateTime(jSONObject.getString(AUserTrack.UTKEY_END_TIME), DateUtil.sdf2) / 1000);
                        if (!this.videoInfoList.contains(cloudInfo2) && verifyTimeRect(cloudInfo2)) {
                            this.videoInfoList.add(cloudInfo2);
                            i3++;
                        }
                    }
                    if (i3 == 500 && this.videoInfoList.size() > 0 && (cloudInfo = this.videoInfoList.get(this.videoInfoList.size() - 1)) != null) {
                        getCloudRecordList(this.curBeginTime, cloudInfo.endTime, true, z2);
                    }
                    Collections.sort(this.videoInfoList);
                    List<CloudRecordBean> data2 = this.mCloudPictureAdapter.getData();
                    if (data2.size() > 0) {
                        CloudInfo cloudInfo3 = this.videoInfoList.size() > 0 ? this.videoInfoList.get(this.videoInfoList.size() - 1) : null;
                        if (cloudInfo3 != null) {
                            for (CloudRecordBean cloudRecordBean : data2) {
                                if (this.curEndTime >= Long.parseLong(cloudRecordBean.picCreateTime) && verifyTimeRect(cloudInfo3)) {
                                    cloudRecordBean.isShow = true;
                                } else {
                                    cloudRecordBean.isShow = false;
                                }
                            }
                            this.mCloudPictureAdapter.replaceData(data2);
                        }
                    }
                    initVideo();
                    if (z && this.videoInfoList.size() > 0) {
                        if (DateUtil.getYear() == this.year && DateUtil.getMonth() == this.month && DateUtil.getDay() == this.day) {
                            playVideo(this.videoInfoList.get(this.videoInfoList.size() - 1));
                        } else {
                            playVideo(this.videoInfoList.get(0));
                        }
                    }
                } else if (z) {
                    showToast(this.f7880activity.getResources().getString(R.string.date_no_record));
                    dismissBuffering();
                    dismissPlayButton();
                    dismissPlayInfo();
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        this.isRequestNone = false;
    }

    private void initVideo() {
        List<Integer> list = this.stList;
        if (list == null) {
            this.stList = new ArrayList();
        } else {
            list.clear();
        }
        List<Integer> list2 = this.etList;
        if (list2 == null) {
            this.etList = new ArrayList();
        } else {
            list2.clear();
        }
        List<Integer> list3 = this.cPaints;
        if (list3 == null) {
            this.cPaints = new ArrayList();
        } else {
            list3.clear();
        }
        for (int i = 0; i < this.videoInfoList.size(); i++) {
            CloudInfo cloudInfo = this.videoInfoList.get(i);
            int i2 = cloudInfo.beginTime;
            int i3 = this.curBeginTimeHead;
            if (i2 <= i3) {
                cloudInfo.isBeginTimeLastDay = true;
                cloudInfo.actualBeginTime = Math.max(i3, cloudInfo.beginTime);
                this.stList.add(Integer.valueOf(DateUtil.getHMSTimeSecs(this.curBeginTimeHead)));
            } else {
                cloudInfo.actualBeginTime = cloudInfo.beginTime;
                this.stList.add(Integer.valueOf(DateUtil.getHMSTimeSecs(cloudInfo.beginTime)));
            }
            int i4 = cloudInfo.endTime;
            int i5 = this.curEndTime;
            if (i4 > i5) {
                cloudInfo.isEndTimeAfterDay = true;
                cloudInfo.actualEndTime = Math.min(i5, cloudInfo.endTime);
                this.etList.add(Integer.valueOf(DateUtil.getHMSTimeSecs(this.curEndTime)));
            } else {
                cloudInfo.actualEndTime = cloudInfo.endTime;
                this.etList.add(Integer.valueOf(DateUtil.getHMSTimeSecs(cloudInfo.endTime)));
            }
            if (cloudInfo.recordType == 1) {
                this.cPaints.add(1);
            } else {
                this.cPaints.add(0);
            }
        }
        ArrayList arrayList = new ArrayList();
        if (this.stList != null) {
            for (int i6 = 0; i6 < this.stList.size(); i6++) {
                TimeRulerView.TimePart timePart = new TimeRulerView.TimePart();
                timePart.startTime = this.stList.get(i6).intValue();
                timePart.endTime = this.etList.get(i6).intValue() + 10;
                timePart.timeColor = this.cPaints.get(i6).intValue();
                arrayList.add(timePart);
            }
        }
        this.timeRulerView.setTimePartListAndBackCenter(arrayList);
    }

    public void resetData() {
        List<Integer> list = this.stList;
        if (list != null) {
            list.clear();
        }
        List<Integer> list2 = this.etList;
        if (list2 != null) {
            list2.clear();
        }
        List<Integer> list3 = this.cPaints;
        if (list3 != null) {
            list3.clear();
        }
        List<CloudInfo> list4 = this.videoInfoList;
        if (list4 != null) {
            list4.clear();
        }
        ArrayList arrayList = new ArrayList();
        if (this.stList != null) {
            for (int i = 0; i < this.stList.size(); i++) {
                TimeRulerView.TimePart timePart = new TimeRulerView.TimePart();
                timePart.startTime = this.stList.get(i).intValue();
                timePart.endTime = this.etList.get(i).intValue() + 10;
                timePart.timeColor = this.cPaints.get(i).intValue();
                arrayList.add(timePart);
            }
        }
        this.timeRulerView.setTimePartListAndBackCenter(arrayList);
        cancelUpdateTimeline();
        stopVideo();
    }

    public void seekPlayerPos(int i) {
        CloudInfo cloudInfo = this.cloudInfo;
        if (cloudInfo == null) {
            return;
        }
        int i2 = cloudInfo.actualBeginTime;
        int i3 = this.cloudInfo.actualEndTime;
        long j = i2;
        if (i < DateUtil.getHMSTimeSecs(j)) {
            i = DateUtil.getHMSTimeSecs(j);
        }
        long j2 = i3;
        if (i > DateUtil.getHMSTimeSecs(j2)) {
            i = DateUtil.getHMSTimeSecs(j2);
        }
        int i4 = this.cloudInfo.actualBeginTime - this.cloudInfo.beginTime;
        if (this.cloudInfo.endTime - this.cloudInfo.beginTime > 0) {
            long jMax = Math.max(0L, (this.player.getDuration() * ((long) ((i - DateUtil.getHMSTimeSecs(j)) + i4))) / ((long) (this.cloudInfo.endTime - this.cloudInfo.beginTime)));
            if (this.player.getPlayState() == 3 || this.player.getPlayState() == 2) {
                this.player.seekTo(jMax);
            } else {
                playVideo(this.cloudInfo);
            }
        }
    }

    public void seekTimeBarPos(long j) {
        int i = this.cloudInfo.actualBeginTime;
        int i2 = this.cloudInfo.actualEndTime;
        int hMSTimeSecs = DateUtil.getHMSTimeSecs(i) + Math.max(0, ((int) ((((long) (this.cloudInfo.endTime - this.cloudInfo.beginTime)) * j) / this.player.getDuration())) - (this.cloudInfo.actualBeginTime - this.cloudInfo.beginTime));
        if (this.cloudInfo.isEndTimeAfterDay && hMSTimeSecs > DateUtil.getHMSTimeSecs(i2)) {
            stopVideo();
            dismissBuffering();
            dismissPlayButton();
            cancelUpdateTimeline();
            return;
        }
        this.timeRulerView.setCurrentTime(hMSTimeSecs);
        this.timeTextView.setText(TimeRulerView.formatTimeHHmmss(hMSTimeSecs));
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
        File file = new File(getFilesPath(getContext()) + "/photo/");
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

    public void initColor() {
        this.full_speed_ll1.setBackgroundResource(0);
        this.text1.setTextColor(getResources().getColor(R.color.color_white));
        this.text2.setTextColor(getResources().getColor(R.color.color_white));
        this.text3.setTextColor(getResources().getColor(R.color.color_white));
        this.text4.setTextColor(getResources().getColor(R.color.color_white));
        this.text5.setTextColor(getResources().getColor(R.color.color_white));
        this.text6.setTextColor(getResources().getColor(R.color.color_white));
    }

    private void keepScreenLight() {
        this.f7880activity.getWindow().addFlags(128);
    }

    public void goToNextDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        int year = this.calendarView.getSelectedCalendar().getYear();
        int month = this.calendarView.getSelectedCalendar().getMonth();
        calendar.set(1, year);
        calendar.set(2, month - 1);
        if (this.calendarView.getSelectedCalendar().getDay() + 1 > calendar.getActualMaximum(5)) {
            if (this.calendarView.getSelectedCalendar().getMonth() >= 12) {
                CalendarView calendarView = this.calendarView;
                calendarView.scrollToCalendarAndUpdate(calendarView.getSelectedCalendar().getYear() + 1, 1, 1);
                return;
            } else {
                CalendarView calendarView2 = this.calendarView;
                calendarView2.scrollToCalendarAndUpdate(calendarView2.getSelectedCalendar().getYear(), this.calendarView.getSelectedCalendar().getMonth() + 1, 1);
                return;
            }
        }
        CalendarView calendarView3 = this.calendarView;
        calendarView3.scrollToCalendarAndUpdate(calendarView3.getSelectedCalendar().getYear(), this.calendarView.getSelectedCalendar().getMonth(), this.calendarView.getSelectedCalendar().getDay() + 1);
    }

    public void goToLastDay() {
        if (this.calendarView.getSelectedCalendar().getDay() - 1 < 1) {
            if (this.calendarView.getSelectedCalendar().getMonth() <= 1) {
                Calendar calendar = Calendar.getInstance();
                calendar.clear();
                int year = this.calendarView.getSelectedCalendar().getYear();
                this.calendarView.getSelectedCalendar().getMonth();
                calendar.set(1, year - 1);
                calendar.set(2, 11);
                int actualMaximum = calendar.getActualMaximum(5);
                CalendarView calendarView = this.calendarView;
                calendarView.scrollToCalendarAndUpdate(calendarView.getSelectedCalendar().getYear() - 1, 12, actualMaximum);
                return;
            }
            Calendar calendar2 = Calendar.getInstance();
            calendar2.clear();
            int year2 = this.calendarView.getSelectedCalendar().getYear();
            int month = this.calendarView.getSelectedCalendar().getMonth();
            calendar2.set(1, year2);
            calendar2.set(2, (month - 1) - 1);
            int actualMaximum2 = calendar2.getActualMaximum(5);
            CalendarView calendarView2 = this.calendarView;
            calendarView2.scrollToCalendarAndUpdate(calendarView2.getSelectedCalendar().getYear(), this.calendarView.getSelectedCalendar().getMonth() - 1, actualMaximum2);
            return;
        }
        CalendarView calendarView3 = this.calendarView;
        calendarView3.scrollToCalendarAndUpdate(calendarView3.getSelectedCalendar().getYear(), this.calendarView.getSelectedCalendar().getMonth(), this.calendarView.getSelectedCalendar().getDay() - 1);
    }

    public void showNvr() {
        this.ll_double_eye_record.setVisibility(0);
    }

    public void switch2ShotMachine(int i) {
        this.newTime = this.timeRulerView.getCurrentTime();
        if (this.currentDoubleId != i) {
            if (i == 1) {
                RecordVideoActivity recordVideoActivity = this.f7880activity;
                recordVideoActivity.iotId = recordVideoActivity.iotId2;
                this.circle_record_image.setImageResource(R.drawable.ball_playback_false);
                this.shot_record_image.setImageResource(R.drawable.gun_playback_true);
                this.shot_record_text.setTextColor(getResources().getColor(R.color.colorAccent));
                this.circle_record_text.setTextColor(getResources().getColor(R.color.color_99000000));
            } else if (i == 0) {
                RecordVideoActivity recordVideoActivity2 = this.f7880activity;
                recordVideoActivity2.iotId = recordVideoActivity2.iotId1;
                this.circle_record_image.setImageResource(R.drawable.ball_playback_true);
                this.shot_record_image.setImageResource(R.drawable.gun_playback_false);
                this.shot_record_text.setTextColor(getResources().getColor(R.color.color_99000000));
                this.circle_record_text.setTextColor(getResources().getColor(R.color.colorAccent));
            }
            this.currentDoubleId = i;
            long[] stEtTime = getStEtTime();
            long[] stEtTime2 = getStEtTime();
            this.curBeginTime = (int) (stEtTime[0] / 1000);
            this.curEndTime = (int) (stEtTime[1] / 1000);
            resetData();
            this.isRequestNone = true;
            this.isSeeked = false;
            this.isToday = false;
            getCloudRecordList(this.curBeginTime, this.curEndTime, true, false);
            this.mCloudPictureAdapter.setNewData(null);
            List<CloudRecordBean> list = this.dogRecordBean;
            if (list != null) {
                list.clear();
            }
            List<CloudRecordBean> list2 = this.finalCloudRecordBeanList;
            if (list2 != null) {
                list2.clear();
            }
            List<CloudRecordBean> list3 = this.catRecordBean;
            if (list3 != null) {
                list3.clear();
            }
            List<CloudRecordBean> list4 = this.peopleRecordBean;
            if (list4 != null) {
                list4.clear();
            }
            List<CloudRecordBean> list5 = this.moveRecordBean;
            if (list5 != null) {
                list5.clear();
            }
            this.count = 0;
            this.cloudButton.setCurrentIndex(0);
            getEventRecordList(this.f7880activity.iotId, stEtTime2[0], stEtTime2[1], 0, 0, 100);
            getMonthList(this.f7880activity.iotId, this.year, this.month);
            this.isCalendarShow = false;
            this.mRecyclerView.setVisibility(0);
        }
    }
}
