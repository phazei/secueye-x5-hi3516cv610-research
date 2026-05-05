package fragment;

import activity.RecordVideoActivity;
import adapter.TFPictureAdapter;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import bean.EventInfo;
import bean.EventModel;
import bean.NetWorkEvent;
import bean.RefreshPicture;
import bean.VideoInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.aliyun.alink.linksdk.tools.ut.AUserTrack;
import com.aliyun.iot.aep.sdk.apiclient.IoTAPIClientFactory;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.emuns.Scheme;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequestBuilder;
import com.aliyun.iotx.linkvisual.media.video.PlayerException;
import com.aliyun.iotx.linkvisual.media.video.beans.PlayInfo;
import com.aliyun.iotx.linkvisual.media.video.listener.OnCompletionListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener;
import com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener;
import com.aliyun.iotx.linkvisual.media.video.player.VodPlayer;
import com.chad.library.adapter.base.BaseQuickAdapter;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import sdk.IPCManager;
import tools.DateUtil;
import tools.LogEx;
import tools.MediaStoreUtil;
import tools.OnMultiClickListener;
import tools.ScreenUtil;
import tools.SharePreferenceManager;
import tools.Utils;
import view.ButtonRipperView;
import view.MyGlTextureView;
import view.SelectorDialogFragment;
import view.TimeRulerView;
import view.TitleView;

/* JADX INFO: loaded from: classes4.dex */
public class StorageFragment extends CommonFragment implements View.OnClickListener {
    private static String[] PERMISSIONS_STORAGE = {Permission.READ_EXTERNAL_STORAGE, Permission.WRITE_EXTERNAL_STORAGE};
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String TAG = "playback";

    /* JADX INFO: renamed from: activity, reason: collision with root package name */
    private RecordVideoActivity f7879activity;
    private Animation alphaAnimation;
    private LinearLayout bottomView;
    private TextView bt_cancel;
    private TextView bt_query;
    private List<Integer> cPaints;
    private CalendarView calendarView;
    private SeekBar cardSeekBar;
    private RelativeLayout card_video_rl;
    private ImageView circle_record_image;
    private LinearLayout circle_record_ll;
    private TextView circle_record_text;
    private long countTime;
    private int curBeginTime;
    private int curEndTime;
    private int day;
    private TextView durationTv;
    private EditText edit_query;
    private ButtonRipperView enlarge_scale_ll;
    private List<Integer> etList;
    private List<EventInfo.Info> eventList;
    private Button exoZoomBtn;
    private List<EventModel> finalCloudRecordBeanList;
    private TitleView fl_titlebar;
    private ImageView full_after_ten_seconds;
    private LinearLayout full_after_ten_seconds_ll;
    private ImageView full_before_ten_seconds;
    private LinearLayout full_before_ten_seconds_ll;
    ImageView full_camera;
    LinearLayout full_icon;
    private LinearLayout full_screen_zoom;
    ImageView full_sound;
    private TextView full_speed;
    private LinearLayout full_speed_ll;
    private LinearLayout full_speed_ll1;
    ImageView full_stop;
    ImageView full_video;
    private boolean isCalendarShow;
    private boolean isFloat;
    private boolean isRequestNone;
    private ImageView iv;
    private ImageView iv_search;
    private LinearLayout layout_control;
    private RelativeLayout layout_rv_list;
    RelativeLayout layout_search;
    private ImageButton listenerBtn;
    private LinearLayout llSeekTimebar;
    private LinearLayout ll_date;
    private LinearLayout ll_double_eye_record;
    private LinearLayout ll_listener;
    private LinearLayout ll_md;
    private LinearLayout ll_play;
    private LinearLayout ll_root;
    private LinearLayout ll_spinner_speed;
    private LinearLayout ll_title;
    private LinearLayout ll_zoom;
    private TFPictureAdapter mCloudPictureAdapter;
    private RecyclerView mRecyclerView;
    private int month;
    private ImageView mv_zoom_add;
    private ImageView mv_zoom_minus;
    private ButtonRipperView narrow_scale_ll;
    private Timer onTouchTimer;
    int phoneTimeZone;
    private ImageButton playBtn;
    private ImageButton playButton;
    private TextView playInfoTv;
    private ButtonRipperView play_back_off_ll;
    private ButtonRipperView play_fast_forward_ll;
    private MyGlTextureView playerGLSurfaceView;
    private Handler playerHandler;
    private ButtonRipperView record_ll;
    private LinearLayout rightView;
    private ImageView shot_record_image;
    private LinearLayout shot_record_l2;
    private LinearLayout shot_record_l3;
    private LinearLayout shot_record_ll;
    private TextView shot_record_text;
    private TextView shot_record_text2;
    private ImageButton speedButton;
    private SelectorDialogFragment speedFragment;
    private List<Integer> stList;
    private int storageStatusValues;
    private int storageStatusValues1;
    private ButtonRipperView take_picture_ll;
    private TextView text1;
    private TextView text2;
    private TextView text3;
    private TextView text4;
    private TextView text5;
    private TextView text6;
    private TextView tf_text;
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
    private Handler uiHandler;
    ScheduledFuture<?> updatePlayInfoHandle;
    private ProgressBar videoBufferingProgressBar;
    private View view_line;
    private VodPlayer vodPlayer;
    private int year;
    private List<VideoInfo> videoInfoList = new ArrayList();
    final SimpleDateFormat timeLineFormatter = new SimpleDateFormat("mm:ss");
    ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(2);
    private boolean speakerSwitch = true;
    List<EventModel> deviceInfoBeans = new ArrayList();
    List<EventModel> queryLocalEventList = new ArrayList();
    MutableLiveData<Boolean> isPause = new MutableLiveData<>();
    private int zoomLevel = 0;
    private int count = 0;
    private int speedPosition = 1;
    private long pauseLong = 0;
    private int currentDoubleId = 0;
    private boolean isRecordingMp4 = false;
    private File file = null;
    private String[] speeds = {"0.5x", "1x", "2x", "4x"};
    private int newTime = 0;
    private boolean havePermission = false;
    long differenceTimeZone = 0;
    long deviceTimeZone = 0;
    private int pageStart = 0;
    private Handler mHandler = new Handler();
    final Runnable timelineUpdateTask = new Runnable() { // from class: fragment.StorageFragment.41
        @Override // java.lang.Runnable
        public void run() {
            StorageFragment.this.f7879activity.runOnUiThread(new Runnable() { // from class: fragment.StorageFragment.41.1
                @Override // java.lang.Runnable
                public void run() {
                    if (StorageFragment.this.getActivity() == null || StorageFragment.this.getActivity().isFinishing() || StorageFragment.this.vodPlayer.getPlayState() != 3) {
                        return;
                    }
                    StorageFragment.this.updateTimeline();
                }
            });
        }
    };
    final Runnable updatePlayInfoTimerTask = new Runnable() { // from class: fragment.StorageFragment.42
        @Override // java.lang.Runnable
        public void run() {
            StorageFragment.this.f7879activity.runOnUiThread(new Runnable() { // from class: fragment.StorageFragment.42.1
                @Override // java.lang.Runnable
                public void run() {
                    if (StorageFragment.this.getActivity() == null || StorageFragment.this.getActivity().isFinishing()) {
                        return;
                    }
                    StorageFragment.this.updatePlayInfo();
                }
            });
        }
    };

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.fragment_storage;
    }

    static /* synthetic */ int access$4508(StorageFragment storageFragment) {
        int i = storageFragment.pageStart;
        storageFragment.pageStart = i + 1;
        return i;
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                startActivity(new Intent("android.settings.MANAGE_ALL_FILES_ACCESS_PERMISSION"));
                return;
            } else {
                this.havePermission = true;
                Log.i("swyLog", "Android 11以上，当前已有权限");
                return;
            }
        }
        if (Build.VERSION.SDK_INT > 23) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Permission.WRITE_EXTERNAL_STORAGE) != 0) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE, 1);
                return;
            } else {
                this.havePermission = true;
                Log.i("swyLog", "Android 6.0以上，11以下，当前已有权限");
                return;
            }
        }
        this.havePermission = true;
        Log.i("swyLog", "Android 6.0以下，已获取权限");
    }

    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        this.f7879activity = (RecordVideoActivity) context;
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

    private void startOrStopRecordingMp4() {
        checkPermission();
        long j = 0;
        if (this.isRecordingMp4 || !this.havePermission) {
            if (this.countTime <= 3) {
                showToast(getResources().getString(R.string.record_time_short));
                return;
            }
            if (SharePreferenceManager.getInstance().getSmartP(this.f7879activity.iotId) == 1 && this.countTime <= 16) {
                showToast(getResources().getString(R.string.record_time_short));
                return;
            }
            if (this.vodPlayer.stopRecordingContent()) {
                EventBus.getDefault().post(new RefreshPicture());
                showToast(getResources().getString(R.string.record_check));
                try {
                    j = Long.parseLong(this.file.getName().substring(0, this.file.getName().indexOf(".")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    MediaStoreUtil.addVideoFileToMediaStore((Context) Objects.requireNonNull(getActivity()), this.file, j);
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
            } else {
                showToast(getResources().getString(R.string.save_fail));
            }
            this.isRecordingMp4 = false;
            setRecordState();
            Timer timer = this.timer;
            if (timer != null) {
                timer.cancel();
                this.timer = null;
            }
            this.tvRecordTime.setText("");
            this.tvRecordTime.setVisibility(8);
            return;
        }
        File file = new File(getFilesPath(getContext()) + "/video/");
        if (file.exists() || file.mkdirs()) {
            this.file = new File(file, System.currentTimeMillis() + ".mp4");
            try {
                if (this.vodPlayer.startRecordingContent(this.file)) {
                    this.isRecordingMp4 = true;
                    setRecordState();
                    if (SharePreferenceManager.getInstance().getSmartP(this.f7879activity.iotId) == 1) {
                        int currentTime = this.timeRulerView.getCurrentTime() - 16;
                        int i = currentTime / 3600;
                        int i2 = (currentTime % 3600) / 60;
                        int i3 = (currentTime % 3600) % 60;
                        if (DateUtil.getTimeMillins(i, i2, i3) <= 0) {
                            seek2(0L);
                        } else {
                            seek2(DateUtil.getTimeMillins(i, i2, i3));
                        }
                        showToast(getString(R.string.pre_recording));
                    }
                    if (this.timer != null) {
                        this.timer.cancel();
                        this.timer = null;
                    }
                    this.countTime = 0L;
                    this.tvRecordTime.setVisibility(0);
                    this.tvRecordTime.setText(getResources().getString(R.string.recording, "00:00:00"));
                    this.timer = new Timer();
                    this.timer.schedule(new TimerTask() { // from class: fragment.StorageFragment.1
                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            if (StorageFragment.this.timer != null) {
                                StorageFragment.this.mHandler.post(new Runnable() { // from class: fragment.StorageFragment.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        String strValueOf;
                                        String strValueOf2;
                                        String strValueOf3;
                                        if (StorageFragment.this.getActivity() == null || StorageFragment.this.getActivity().isFinishing()) {
                                            return;
                                        }
                                        StorageFragment.this.countTime++;
                                        int i4 = (int) (StorageFragment.this.countTime / 3600);
                                        int i5 = (int) ((StorageFragment.this.countTime % 3600) / 60);
                                        int i6 = (int) ((StorageFragment.this.countTime % 3600) % 60);
                                        StringBuilder sb = new StringBuilder();
                                        if (i4 > 9) {
                                            strValueOf = String.valueOf(i4);
                                        } else {
                                            strValueOf = "0" + i4;
                                        }
                                        sb.append(strValueOf);
                                        sb.append(":");
                                        if (i5 > 9) {
                                            strValueOf2 = String.valueOf(i5);
                                        } else {
                                            strValueOf2 = "0" + i5;
                                        }
                                        sb.append(strValueOf2);
                                        sb.append(":");
                                        if (i6 > 9) {
                                            strValueOf3 = String.valueOf(i6);
                                        } else {
                                            strValueOf3 = "0" + i6;
                                        }
                                        sb.append(strValueOf3);
                                        StorageFragment.this.tvRecordTime.setText(StorageFragment.this.getResources().getString(R.string.recording, sb.toString()));
                                    }
                                });
                            }
                        }
                    }, 50L, 1000L);
                    return;
                }
                showToast(getResources().getString(R.string.record_fail));
            } catch (Exception e3) {
                e3.printStackTrace();
                showToast(this.file.getAbsolutePath() + getResources().getString(R.string.not_write));
            }
        }
    }

    private void snapshot() {
        if (this.vodPlayer.getPlayState() != 3) {
            Toast.makeText(this.f7879activity, R.string.only_play_snap, 0).show();
            return;
        }
        Bitmap bitmapSnapShot = this.vodPlayer.snapShot();
        if (bitmapSnapShot == null) {
            showToast(getResources().getString(R.string.no_snap));
        } else {
            scanFile(bitmapSnapShot);
            showToast(getResources().getString(R.string.camera_check));
        }
    }

    @SuppressLint({"ResourceAsColor"})
    private void setRecordState() {
        int i = this.f7879activity.getResources().getConfiguration().orientation;
        int i2 = R.drawable.play_back_video_select;
        int i3 = R.drawable.play_back_video_select_full;
        if (i == 2) {
            ImageView imageView = this.full_video;
            if (!this.isRecordingMp4) {
                i3 = R.drawable.play_back_video_gray_full;
            }
            imageView.setBackgroundResource(i3);
            ButtonRipperView buttonRipperView = this.record_ll;
            if (!this.isRecordingMp4) {
                i2 = R.drawable.play_back_video_gray;
            }
            buttonRipperView.setImageResource(i2);
            this.record_ll.setTextColor(this.isRecordingMp4 ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.colors_aeb3c0));
            return;
        }
        ImageView imageView2 = this.full_video;
        if (!this.isRecordingMp4) {
            i3 = R.drawable.play_back_video_gray_full;
        }
        imageView2.setBackgroundResource(i3);
        ButtonRipperView buttonRipperView2 = this.record_ll;
        if (!this.isRecordingMp4) {
            i2 = R.drawable.play_back_video_gray;
        }
        buttonRipperView2.setImageResource(i2);
        this.record_ll.setTextColor(this.isRecordingMp4 ? getResources().getColor(R.color.colorAccent) : getResources().getColor(R.color.colors_aeb3c0));
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        if (configuration.orientation == 2) {
            VodPlayer vodPlayer = this.vodPlayer;
            if (vodPlayer != null) {
                vodPlayer.setVideoScalingMode(1);
            }
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.card_video_rl.getLayoutParams();
            layoutParams.height = -1;
            this.card_video_rl.setLayoutParams(layoutParams);
            this.isFloat = false;
            setFloatBarState();
            this.view_line.setVisibility(8);
            this.layout_rv_list.setVisibility(8);
            this.llSeekTimebar.setVisibility(8);
            this.ll_date.setVisibility(8);
            if (this.ll_date.getVisibility() == 8) {
                this.layout_control.setVisibility(0);
            }
            this.fl_titlebar.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            this.fl_titlebar.setTitleTextColor(R.color.color_white);
            this.fl_titlebar.setTitleBackTextColor(R.color.color_white);
            this.fl_titlebar.setLeftImgId(R.drawable.ic_back2);
            this.fl_titlebar.setTitleTextVisible(0);
            if (Build.VERSION.SDK_INT >= 19) {
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.fl_titlebar.getLayoutParams();
                layoutParams2.height = ScreenUtil.dp2Px(this.f7879activity, 44.0f);
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
        } else {
            VodPlayer vodPlayer2 = this.vodPlayer;
            if (vodPlayer2 != null) {
                vodPlayer2.setVideoScalingMode(1);
            }
            for (int i = 0; i < this.rightView.getChildCount(); i++) {
                View childAt = this.rightView.getChildAt(i);
                LinearLayout.LayoutParams layoutParams5 = (LinearLayout.LayoutParams) childAt.getLayoutParams();
                layoutParams5.rightMargin = 0;
                if (childAt.getId() == R.id.ll_play) {
                    childAt.setPadding(ScreenUtil.dp2Px(getActivity(), 20.0f), 0, 0, 0);
                } else if (childAt.getId() == R.id.ll_spinner_speed) {
                    childAt.setPadding(ScreenUtil.dp2Px(getActivity(), 10.0f), 0, 0, 0);
                } else if (childAt.getId() == R.id.ll_listener) {
                    childAt.setPadding(0, 0, ScreenUtil.dp2Px(getActivity(), 10.0f), 0);
                }
                childAt.setLayoutParams(layoutParams5);
            }
            float f = (ScreenUtil.getDisplayMetrics(getActivity())[1] * 1.0f) / ScreenUtil.getDisplayMetrics(getActivity())[0];
            if (f < 1.85d || f >= 2.0f) {
                int i2 = (f > 2.0f ? 1 : (f == 2.0f ? 0 : -1));
            }
            LinearLayout.LayoutParams layoutParams6 = (LinearLayout.LayoutParams) this.card_video_rl.getLayoutParams();
            layoutParams6.height = getResources().getDimensionPixelSize(R.dimen.dimen_200);
            this.card_video_rl.setLayoutParams(layoutParams6);
            this.isFloat = false;
            setFloatBarState();
            this.view_line.setVisibility(0);
            this.llSeekTimebar.setVisibility(0);
            this.fl_titlebar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            this.fl_titlebar.setTitleTextColor(R.color.color_black);
            this.fl_titlebar.setTitleBackTextColor(R.color.colorAccent);
            this.fl_titlebar.setLeftImgId(R.drawable.ic_back);
            this.fl_titlebar.setTitleTextVisible(4);
            if (Build.VERSION.SDK_INT >= 19) {
                LinearLayout.LayoutParams layoutParams7 = (LinearLayout.LayoutParams) this.fl_titlebar.getLayoutParams();
                layoutParams7.height = ScreenUtil.dp2Px(this.f7879activity, 84.0f);
                this.fl_titlebar.setTitleRlPadding(getResources().getDimensionPixelSize(R.dimen.dp_30));
                this.fl_titlebar.setLayoutParams(layoutParams7);
            }
            if (this.ll_title.getParent() != null) {
                ((ViewGroup) this.ll_title.getParent()).removeView(this.ll_title);
            }
            this.ll_root.addView(this.ll_title, 0);
            RelativeLayout.LayoutParams layoutParams8 = (RelativeLayout.LayoutParams) this.tvRecordTime.getLayoutParams();
            layoutParams8.removeRule(3);
            layoutParams8.addRule(12);
            this.tvRecordTime.setLayoutParams(layoutParams8);
            if (this.isCalendarShow) {
                this.ll_date.setVisibility(0);
            } else {
                this.layout_rv_list.setVisibility(0);
            }
            this.bottomView.setVisibility(0);
            RelativeLayout.LayoutParams layoutParams9 = (RelativeLayout.LayoutParams) this.playInfoTv.getLayoutParams();
            layoutParams9.addRule(12);
            layoutParams9.leftMargin = ScreenUtil.dp2Px(getActivity(), 30.0f);
            layoutParams9.bottomMargin = ScreenUtil.dp2Px(getActivity(), 9.0f);
            layoutParams9.removeRule(10);
            layoutParams9.removeRule(11);
            layoutParams9.topMargin = 0;
            layoutParams9.rightMargin = 0;
            this.playInfoTv.setLayoutParams(layoutParams9);
            if (this.timeRulerView.getParent() != null) {
                ((ViewGroup) this.timeRulerView.getParent()).removeView(this.timeRulerView);
            }
            this.time_rule_view_ll1.addView(this.timeRulerView);
        }
        super.onConfigurationChanged(configuration);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFloatBarState() {
        this.playInfoTv.setVisibility((this.isFloat && this.vodPlayer.getPlayState() == 3) ? 0 : 8);
        if (getResources().getConfiguration().orientation == 2) {
            this.rightView.setVisibility(this.isFloat ? 0 : 8);
            if (this.full_speed.getText().toString().equals("1X")) {
                this.full_speed.setBackgroundResource(R.drawable.speed_unselected_full);
            } else {
                this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
            }
            initColor();
            this.full_screen_zoom.setVisibility(this.isFloat ? 0 : 8);
            this.time_rule_view_ll3.setVisibility(this.isFloat ? 0 : 8);
            this.full_icon.setVisibility(this.isFloat ? 0 : 8);
            this.full_speed_ll.setVisibility(8);
            this.fl_titlebar.setVisibility(this.isFloat ? 0 : 8);
            return;
        }
        if (this.full_speed.getText().toString().equals("1X")) {
            this.full_speed.setBackgroundResource(R.drawable.speed_unselected_full);
        } else {
            this.full_speed.setBackgroundResource(R.drawable.speed_selected_full);
        }
        initColor();
        this.full_screen_zoom.setVisibility(8);
        this.full_icon.setVisibility(8);
        this.full_speed_ll.setVisibility(8);
        this.time_rule_view_ll3.setVisibility(8);
        this.rightView.setVisibility(8);
        this.fl_titlebar.setVisibility(0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long[] getStEtTime() {
        long timeMillins = DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0);
        long timeMillins2 = DateUtil.getTimeMillins(this.year, this.month, this.day, 23, 59, 59);
        long j = this.differenceTimeZone;
        long[] jArr = {timeMillins + j, timeMillins2 + j};
        Log.e("时区差", "" + this.differenceTimeZone);
        Log.e("时区时间差startTime", "" + jArr[0]);
        Log.e("时区时间差endTime", "" + jArr[1]);
        return jArr;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.take_picture_ll) {
            snapshot();
            return;
        }
        if (view2.getId() == R.id.record_ll) {
            startOrStopRecordingMp4();
            return;
        }
        if (view2.getId() == R.id.ll_listener || view2.getId() == R.id.listener_btn) {
            this.speakerSwitch = !this.speakerSwitch;
            this.vodPlayer.setVolume(this.speakerSwitch ? 1.0f : 0.0f);
            setListen(this.speakerSwitch);
            return;
        }
        if (view2.getId() == R.id.ll_zoom || view2.getId() == R.id.exo_zoom_tbtn) {
            if (this.f7879activity.getRequestedOrientation() == 1) {
                this.f7879activity.setRequestedOrientation(0);
                return;
            } else {
                this.f7879activity.setRequestedOrientation(8);
                return;
            }
        }
        if (view2.getId() == R.id.enlarge_scale_ll) {
            this.timeRulerView.setBigScale();
            return;
        }
        if (view2.getId() == R.id.narrow_scale_ll) {
            this.timeRulerView.setSmallScale();
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
        if (view2.getId() == R.id.play_back_off_ll || view2.getId() == R.id.full_before_ten_seconds_ll) {
            int currentTime = this.timeRulerView.getCurrentTime() - 10;
            int i = currentTime / 3600;
            int i2 = currentTime % 3600;
            int i3 = i2 / 60;
            int i4 = i2 % 60;
            if (DateUtil.getTimeMillins(i, i3, i4) <= 0) {
                seek2(0L);
                return;
            } else {
                seek2(DateUtil.getTimeMillins(i, i3, i4));
                return;
            }
        }
        if (view2.getId() == R.id.play_fast_forward_ll || view2.getId() == R.id.full_after_ten_seconds_ll) {
            int currentTime2 = this.timeRulerView.getCurrentTime() + 10;
            int i5 = currentTime2 / 3600;
            int i6 = currentTime2 % 3600;
            seek2(DateUtil.getTimeMillins(i5, i6 / 60, i6 % 60));
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
                            if (iHashCode == 1475905 && string.equals("0.5X")) {
                                b2 = 0;
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
        if (view2.getId() == R.id.text3) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text3.getText());
            this.full_speed.setBackgroundResource(R.drawable.bg_blue_oval);
            initColor();
            this.speedButton.setImageResource(R.drawable.speed_four);
            this.speedPosition = 3;
            this.vodPlayer.setPlaybackSpeed(4.0f);
            return;
        }
        if (view2.getId() == R.id.text4) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text4.getText());
            this.full_speed.setBackgroundResource(R.drawable.bg_blue_oval);
            initColor();
            this.speedButton.setImageResource(R.drawable.speed_two);
            this.speedPosition = 2;
            this.vodPlayer.setPlaybackSpeed(2.0f);
            return;
        }
        if (view2.getId() == R.id.text5) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text5.getText());
            this.full_speed.setBackgroundResource(R.drawable.speed_unselected_full);
            initColor();
            this.speedButton.setImageResource(R.drawable.speed_one);
            this.speedPosition = 1;
            this.vodPlayer.setPlaybackSpeed(1.0f);
            return;
        }
        if (view2.getId() == R.id.text6) {
            this.full_speed_ll.setVisibility(8);
            this.full_speed.setText(this.text6.getText());
            this.full_speed.setBackgroundResource(R.drawable.bg_blue_oval);
            initColor();
            this.speedButton.setImageResource(R.drawable.speed_half_five);
            this.speedPosition = 0;
            this.vodPlayer.setPlaybackSpeed(0.5f);
            return;
        }
        if (view2.getId() == R.id.iv_search) {
            RelativeLayout relativeLayout = this.layout_search;
            relativeLayout.setVisibility(relativeLayout.getVisibility() != 8 ? 8 : 0);
            if (this.layout_search.getVisibility() == 0) {
                this.iv_search.setVisibility(8);
                return;
            }
            return;
        }
        if (view2.getId() == R.id.bt_cancel) {
            this.layout_search.setVisibility(8);
            this.iv_search.setVisibility(0);
            this.mCloudPictureAdapter.setNewData(null);
            this.mCloudPictureAdapter.setNewData(this.deviceInfoBeans);
            return;
        }
        if (view2.getId() == R.id.bt_query) {
            ((InputMethodManager) getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.edit_query.getWindowToken(), 0);
            String string2 = this.edit_query.getText().toString();
            if (string2.isEmpty()) {
                showToast("请输入内容");
                return;
            }
            this.layout_search.setVisibility(8);
            this.iv_search.setVisibility(0);
            IPCManager.getInstance().getDevice(this.f7879activity.iotId).setQueryLocalEvent(string2, 6, this.curBeginTime, this.curEndTime, new IPanelCallback() { // from class: fragment.StorageFragment.2
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(final boolean z, final Object obj) {
                    Log.d(StorageFragment.TAG, "onComplete: ------" + z + "    " + obj.toString());
                    new Handler().post(new Runnable() { // from class: fragment.StorageFragment.2.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Object obj2;
                            if (!z || (obj2 = obj) == null || String.valueOf(obj2).equals("")) {
                                return;
                            }
                            try {
                                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                                JSONObject jSONObject = object.getJSONObject("data");
                                if (object.getInteger("code").intValue() != 200) {
                                    StorageFragment.this.showToast("未找到该记录");
                                    return;
                                }
                                StorageFragment.this.queryLocalEventList.clear();
                                JSONArray jSONArray = jSONObject.getJSONArray("data");
                                if (jSONArray.size() == 0) {
                                    StorageFragment.this.showToast("未找到该记录");
                                    return;
                                }
                                if (jSONArray == null || jSONArray.size() <= 0) {
                                    return;
                                }
                                for (int i7 = 0; i7 < jSONArray.size(); i7++) {
                                    JSONObject jSONObject2 = jSONArray.getJSONObject(i7);
                                    EventModel eventModel = new EventModel();
                                    long jLongValue = jSONObject2.getLong("TimeStamp").longValue();
                                    jSONObject2.getLong("TimeStamp").longValue();
                                    eventModel.eventDesc = jSONObject2.getString("Message");
                                    eventModel.eventTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(jLongValue * 1000));
                                    if (!StorageFragment.this.queryLocalEventList.contains(eventModel)) {
                                        StorageFragment.this.queryLocalEventList.add(0, eventModel);
                                    }
                                }
                                StorageFragment.this.mCloudPictureAdapter.setNewData(null);
                                StorageFragment.this.mCloudPictureAdapter.setNewData(StorageFragment.this.queryLocalEventList);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });
        }
    }

    @Override // fragment.CommonFragment
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    protected void initWidget(View view2) {
        super.initWidget(view2);
        this.isPause.postValue(false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.videoBufferingProgressBar = (ProgressBar) view2.findViewById(R.id.card_video_buffering_bar);
        this.full_camera = (ImageView) view2.findViewById(R.id.full_camera);
        this.full_sound = (ImageView) view2.findViewById(R.id.full_sound);
        this.full_stop = (ImageView) view2.findViewById(R.id.full_stop);
        this.full_video = (ImageView) view2.findViewById(R.id.full_video);
        this.shot_record_text = (TextView) view2.findViewById(R.id.shot_record_text);
        this.shot_record_text2 = (TextView) view2.findViewById(R.id.shot_record_text2);
        this.ll_double_eye_record = (LinearLayout) view2.findViewById(R.id.ll_double_eye_record);
        this.circle_record_ll = (LinearLayout) view2.findViewById(R.id.circle_record_ll);
        this.shot_record_ll = (LinearLayout) view2.findViewById(R.id.shot_record_ll);
        this.shot_record_l2 = (LinearLayout) view2.findViewById(R.id.shot_record_l2);
        this.shot_record_l3 = (LinearLayout) view2.findViewById(R.id.shot_record_l3);
        this.circle_record_image = (ImageView) view2.findViewById(R.id.circle_record_image);
        this.shot_record_image = (ImageView) view2.findViewById(R.id.shot_record_image);
        this.edit_query = (EditText) view2.findViewById(R.id.edit_query);
        this.bt_query = (TextView) view2.findViewById(R.id.bt_query);
        this.bt_query.setOnClickListener(this);
        this.bt_cancel = (TextView) view2.findViewById(R.id.bt_cancel);
        this.bt_cancel.setOnClickListener(this);
        this.circle_record_ll.setOnClickListener(new OnMultiClickListener() { // from class: fragment.StorageFragment.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view3) {
                StorageFragment.this.switch2ShotMachine(0);
            }
        });
        this.shot_record_ll.setOnClickListener(new OnMultiClickListener() { // from class: fragment.StorageFragment.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view3) {
                StorageFragment.this.switch2ShotMachine(1);
            }
        });
        this.shot_record_l2.setOnClickListener(new OnMultiClickListener() { // from class: fragment.StorageFragment.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view3) {
                StorageFragment.this.switch2ShotMachine(2);
            }
        });
        this.shot_record_l3.setOnClickListener(new OnMultiClickListener() { // from class: fragment.StorageFragment.6
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view3) {
                StorageFragment.this.switch2ShotMachine(3);
            }
        });
        if (this.f7879activity.iotId2 != null && !"".equals(this.f7879activity.iotId2)) {
            this.circle_record_ll.setSelected(true);
            this.ll_double_eye_record.setVisibility(0);
        } else {
            this.ll_double_eye_record.setVisibility(8);
        }
        if (this.f7879activity.iotId2 != null && !"".equals(this.f7879activity.iotId2) && this.f7879activity.iotId3 != null && !"".equals(this.f7879activity.iotId3)) {
            this.ll_double_eye_record.setVisibility(0);
            this.shot_record_l2.setVisibility(0);
        }
        if (this.f7879activity.iotId2 != null && !"".equals(this.f7879activity.iotId2) && this.f7879activity.iotId3 != null && !"".equals(this.f7879activity.iotId3) && this.f7879activity.iotId4 != null && !"".equals(this.f7879activity.iotId4)) {
            this.ll_double_eye_record.setVisibility(0);
            this.shot_record_l3.setVisibility(0);
        }
        this.time_rule_view_ll1 = (LinearLayout) view2.findViewById(R.id.time_rule_view_ll1);
        this.time_rule_view_ll2 = (LinearLayout) view2.findViewById(R.id.time_rule_view_ll2);
        this.time_rule_view_ll3 = (LinearLayout) view2.findViewById(R.id.time_rule_view_ll3);
        this.full_camera.setOnClickListener(new View.OnClickListener() { // from class: fragment.StorageFragment.7
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                StorageFragment.this.take_picture_ll.performClick();
            }
        });
        this.full_sound.setOnClickListener(new View.OnClickListener() { // from class: fragment.StorageFragment.8
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                StorageFragment.this.ll_listener.performClick();
            }
        });
        this.full_stop.setOnClickListener(new View.OnClickListener() { // from class: fragment.StorageFragment.9
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                StorageFragment.this.playButton.performClick();
            }
        });
        this.full_video.setOnClickListener(new View.OnClickListener() { // from class: fragment.StorageFragment.10
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                StorageFragment.this.record_ll.performClick();
            }
        });
        this.tvRecordTime = (TextView) view2.findViewById(R.id.tv_record_time);
        this.uiHandler = new Handler(Looper.getMainLooper());
        this.playerHandler = new Handler(Looper.getMainLooper());
        this.view_line = view2.findViewById(R.id.view_line);
        this.ll_listener = (LinearLayout) view2.findViewById(R.id.ll_listener);
        this.ll_title = (LinearLayout) view2.findViewById(R.id.ll_title);
        this.layout_control = (LinearLayout) view2.findViewById(R.id.layout_control);
        this.ll_md = (LinearLayout) view2.findViewById(R.id.ll_md);
        this.playInfoTv = (TextView) view2.findViewById(R.id.player_info_tv2);
        this.mRecyclerView = (RecyclerView) view2.findViewById(R.id.recyclerview);
        this.layout_rv_list = (RelativeLayout) view2.findViewById(R.id.layout_rv_list);
        this.playBtn = (ImageButton) view2.findViewById(R.id.video_play_ibtn);
        this.tf_text = (TextView) view2.findViewById(R.id.tf_text);
        this.cardSeekBar = (SeekBar) view2.findViewById(R.id.card_player_seek_bar);
        this.durationTv = (TextView) view2.findViewById(R.id.card_duration_tv);
        this.timeRulerView = (TimeRulerView) view2.findViewById(R.id.time_rule_view);
        this.calendarView = (CalendarView) view2.findViewById(R.id.calendarView);
        this.ll_date = (LinearLayout) view2.findViewById(R.id.ll_date);
        this.tv_month = (TextView) view2.findViewById(R.id.tv_month);
        this.tv_day = (TextView) view2.findViewById(R.id.tv_day);
        this.listenerBtn = (ImageButton) view2.findViewById(R.id.listener_btn);
        this.exoZoomBtn = (Button) view2.findViewById(R.id.exo_zoom_tbtn);
        this.llSeekTimebar = (LinearLayout) view2.findViewById(R.id.llSeekTimebar);
        this.rightView = (LinearLayout) view2.findViewById(R.id.rightView);
        this.bottomView = (LinearLayout) view2.findViewById(R.id.bottomView);
        this.iv = (ImageView) view2.findViewById(R.id.iv);
        this.tv_curdate = (TextView) view2.findViewById(R.id.tv_curdate);
        this.ll_play = (LinearLayout) view2.findViewById(R.id.ll_play);
        this.ll_spinner_speed = (LinearLayout) view2.findViewById(R.id.ll_spinner_speed);
        this.ll_zoom = (LinearLayout) view2.findViewById(R.id.ll_zoom);
        this.full_speed_ll1 = (LinearLayout) view2.findViewById(R.id.full_speed_ll1);
        this.full_icon = (LinearLayout) view2.findViewById(R.id.full_icon);
        this.ll_root = (LinearLayout) view2.findViewById(R.id.ll_root);
        this.card_video_rl = (RelativeLayout) view2.findViewById(R.id.card_video_rl);
        this.fl_titlebar = (TitleView) view2.findViewById(R.id.fl_titlebar);
        this.fl_titlebar.setLineViewId(0);
        this.full_screen_zoom = (LinearLayout) view2.findViewById(R.id.full_screen_zoom);
        this.mv_zoom_add = (ImageView) view2.findViewById(R.id.mv_zoom_add);
        this.mv_zoom_minus = (ImageView) view2.findViewById(R.id.mv_zoom_minus);
        this.full_before_ten_seconds = (ImageView) view2.findViewById(R.id.full_before_ten_seconds);
        this.full_after_ten_seconds = (ImageView) view2.findViewById(R.id.full_after_ten_seconds);
        this.full_before_ten_seconds_ll = (LinearLayout) view2.findViewById(R.id.full_before_ten_seconds_ll);
        this.full_after_ten_seconds_ll = (LinearLayout) view2.findViewById(R.id.full_after_ten_seconds_ll);
        this.playButton = (ImageButton) view2.findViewById(R.id.play_btn);
        this.timeTextView = (TextView) view2.findViewById(R.id.time_text);
        this.playButton.setOnClickListener(this);
        this.take_picture_ll = (ButtonRipperView) view2.findViewById(R.id.take_picture_ll);
        this.take_picture_ll.setOnClickListener(this);
        this.take_picture_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.11
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    StorageFragment.this.take_picture_ll.setImageResource(R.drawable.take_picture_record_select);
                    StorageFragment.this.take_picture_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                StorageFragment.this.take_picture_ll.setImageResource(R.drawable.take_picture_record);
                StorageFragment.this.take_picture_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.record_ll = (ButtonRipperView) view2.findViewById(R.id.record_ll);
        this.record_ll.setOnClickListener(this);
        if (!AppConfig.isChina) {
            this.record_ll.setVisibility(8);
            this.full_video.setVisibility(8);
        }
        this.layout_search = (RelativeLayout) view2.findViewById(R.id.layout_search);
        this.iv_search = (ImageView) view2.findViewById(R.id.iv_search);
        this.iv_search.setOnClickListener(this);
        Log.e("事件查询", "" + SharePreferenceManager.getInstance().getEventSearch(this.f7879activity.iotId));
        if (SharePreferenceManager.getInstance().getEventSearch(this.f7879activity.iotId) == 1) {
            this.iv_search.setVisibility(0);
        }
        this.play_back_off_ll = (ButtonRipperView) view2.findViewById(R.id.play_back_off_ll);
        this.play_back_off_ll.setOnClickListener(this);
        this.play_back_off_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.12
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    StorageFragment.this.play_back_off_ll.setImageResource(R.drawable.back_ten_second_select);
                    StorageFragment.this.play_back_off_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                StorageFragment.this.play_back_off_ll.setImageResource(R.drawable.back_ten_second);
                StorageFragment.this.play_back_off_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.play_fast_forward_ll = (ButtonRipperView) view2.findViewById(R.id.play_fast_forward_ll);
        this.play_fast_forward_ll.setOnClickListener(this);
        this.play_fast_forward_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.13
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    StorageFragment.this.play_fast_forward_ll.setImageResource(R.drawable.speed_ten_second_select);
                    StorageFragment.this.play_fast_forward_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                StorageFragment.this.play_fast_forward_ll.setImageResource(R.drawable.speed_ten_second);
                StorageFragment.this.play_fast_forward_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.enlarge_scale_ll = (ButtonRipperView) view2.findViewById(R.id.enlarge_scale_ll);
        this.enlarge_scale_ll.setOnClickListener(this);
        this.enlarge_scale_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.14
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    StorageFragment.this.enlarge_scale_ll.setImageResource(R.drawable.amplification_record_select);
                    StorageFragment.this.enlarge_scale_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colorAccent));
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                StorageFragment.this.enlarge_scale_ll.setImageResource(R.drawable.amplification_record);
                StorageFragment.this.enlarge_scale_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.narrow_scale_ll = (ButtonRipperView) view2.findViewById(R.id.narrow_scale_ll);
        this.narrow_scale_ll.setOnClickListener(this);
        this.narrow_scale_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.15
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    StorageFragment.this.narrow_scale_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colorAccent));
                    StorageFragment.this.narrow_scale_ll.setImageResource(R.drawable.reduce_record_select);
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                StorageFragment.this.narrow_scale_ll.setImageResource(R.drawable.reduce_record);
                StorageFragment.this.narrow_scale_ll.setTextColor(StorageFragment.this.getResources().getColor(R.color.colors_aeb3c0));
                return false;
            }
        });
        this.speedButton = (ImageButton) view2.findViewById(R.id.speed_btn);
        this.speedButton.setOnClickListener(new View.OnClickListener() { // from class: fragment.StorageFragment.16
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                StorageFragment.this.speedFragment.showAllowingStateLoss(StorageFragment.this.getFragmentManager(), "", StorageFragment.this.speedPosition);
            }
        });
        this.full_speed = (TextView) view2.findViewById(R.id.full_speed);
        this.circle_record_text = (TextView) view2.findViewById(R.id.circle_record_text);
        this.full_speed.setOnClickListener(this);
        this.text3 = (TextView) view2.findViewById(R.id.text3);
        this.text4 = (TextView) view2.findViewById(R.id.text4);
        this.text5 = (TextView) view2.findViewById(R.id.text5);
        this.text6 = (TextView) view2.findViewById(R.id.text6);
        this.text3.setOnClickListener(this);
        this.text4.setOnClickListener(this);
        this.text5.setOnClickListener(this);
        this.text6.setOnClickListener(this);
        this.full_speed_ll = (LinearLayout) view2.findViewById(R.id.full_speed_ll);
        this.speedFragment = new SelectorDialogFragment(getString(R.string.multiple_play), this.speeds);
        this.speedFragment.setOnItemClickListener(new SelectorDialogFragment.OnItemClickListener() { // from class: fragment.StorageFragment.17
            @Override // view.SelectorDialogFragment.OnItemClickListener
            public void onItemClick(int i) {
                switch (i) {
                    case 0:
                        StorageFragment.this.full_speed.setText("0.5X");
                        StorageFragment.this.vodPlayer.setPlaybackSpeed(0.5f);
                        StorageFragment.this.speedButton.setImageResource(R.drawable.speed_half_five);
                        StorageFragment.this.speedPosition = 0;
                        break;
                    case 1:
                        StorageFragment.this.full_speed.setText("1X");
                        StorageFragment.this.vodPlayer.setPlaybackSpeed(1.0f);
                        StorageFragment.this.speedButton.setImageResource(R.drawable.speed_one);
                        StorageFragment.this.speedPosition = 1;
                        break;
                    case 2:
                        StorageFragment.this.full_speed.setText("2X");
                        StorageFragment.this.vodPlayer.setPlaybackSpeed(2.0f);
                        StorageFragment.this.speedButton.setImageResource(R.drawable.speed_two);
                        StorageFragment.this.speedPosition = 2;
                        break;
                    case 3:
                        StorageFragment.this.full_speed.setText("4X");
                        StorageFragment.this.vodPlayer.setPlaybackSpeed(4.0f);
                        StorageFragment.this.speedButton.setImageResource(R.drawable.speed_four);
                        StorageFragment.this.speedPosition = 3;
                        break;
                }
            }
        });
        this.mv_zoom_add.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.18
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    StorageFragment.this.mv_zoom_add.setImageResource(R.drawable.full_screen_add_select);
                    if (StorageFragment.this.onTouchTimer == null) {
                        StorageFragment.this.onTouchTimer = new Timer();
                        StorageFragment.this.onTouchTimer.schedule(new TimerTask() { // from class: fragment.StorageFragment.18.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                StorageFragment.this.playerGLSurfaceView.addZoom();
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    StorageFragment.this.mv_zoom_add.setImageResource(R.drawable.full_screen_add);
                    if (StorageFragment.this.onTouchTimer != null) {
                        StorageFragment.this.onTouchTimer.cancel();
                        StorageFragment.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.mv_zoom_minus.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.19
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    StorageFragment.this.mv_zoom_minus.setImageResource(R.drawable.full_screen_delete_select);
                    if (StorageFragment.this.onTouchTimer == null) {
                        StorageFragment.this.onTouchTimer = new Timer();
                        StorageFragment.this.onTouchTimer.schedule(new TimerTask() { // from class: fragment.StorageFragment.19.1
                            @Override // java.util.TimerTask, java.lang.Runnable
                            public void run() {
                                StorageFragment.this.playerGLSurfaceView.reduceZoom();
                            }
                        }, 0L, 50L);
                    }
                } else if (motionEvent.getAction() == 1) {
                    StorageFragment.this.mv_zoom_minus.setImageResource(R.drawable.full_screen_delete);
                    if (StorageFragment.this.onTouchTimer != null) {
                        StorageFragment.this.onTouchTimer.cancel();
                        StorageFragment.this.onTouchTimer = null;
                    }
                }
                return true;
            }
        });
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: fragment.StorageFragment.20
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view3) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view3) {
                StorageFragment.this.f7879activity.onBackPressed();
            }
        });
        this.title = this.f7879activity.getIntent().getStringExtra("title");
        this.fl_titlebar.setTitleText(this.title);
        this.fl_titlebar.setTitleTextVisible(4);
        this.alphaAnimation = AnimationUtils.loadAnimation(this.f7879activity, R.anim.alpha_loop);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(1);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);
        this.mRecyclerView.setItemAnimator(null);
        this.mCloudPictureAdapter = new TFPictureAdapter(R.layout.item_cloud_picture);
        this.mCloudPictureAdapter.bindToRecyclerView(this.mRecyclerView);
        this.mCloudPictureAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: fragment.StorageFragment.21
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view3, int i) {
                int iMax;
                StorageFragment.this.vodPlayer.start();
                EventModel eventModel = StorageFragment.this.mCloudPictureAdapter.getData().get(i);
                if (SharePreferenceManager.getInstance().getLowPower(StorageFragment.this.f7879activity.iotId) == 1 && eventModel != null && eventModel.eventData != null && !eventModel.eventData.equals("")) {
                    iMax = Math.max(0, DateUtil.getHMSTimeMillins(Long.parseLong(eventModel.eventData) * 1000));
                } else {
                    iMax = Math.max(0, DateUtil.getHMSTimeMillins(DateUtil.getDateTime(eventModel.eventTime, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"))));
                }
                StorageFragment.this.seek2(iMax);
                Log.e("seek时间", "" + iMax);
            }
        });
        this.timeRulerView.setOnTimeChangedListener(new TimeRulerView.OnTimeChangedListener() { // from class: fragment.StorageFragment.22
            @Override // view.TimeRulerView.OnTimeChangedListener
            public void onTimeChanged(int i) {
                Log.e("当前时间", "" + i);
            }

            @Override // view.TimeRulerView.OnTimeChangedListener
            public void onTimeSelected(int i) {
                Log.e("当前时间onTimeSelected", "" + i);
                StorageFragment.this.vodPlayer.start();
                int i2 = i / 3600;
                int i3 = i % 3600;
                StorageFragment.this.seek2(DateUtil.getTimeMillins(i2, i3 / 60, i3 % 60));
                StorageFragment.this.speedButton.setImageResource(R.drawable.speed_one);
                StorageFragment.this.speedPosition = 1;
                StorageFragment.this.full_speed.setText("1X");
                StorageFragment.this.full_speed_ll.setVisibility(8);
                if (SharePreferenceManager.getInstance().getDisplayReplay(StorageFragment.this.f7879activity.iotId) == 1) {
                    StorageFragment.this.vodPlayer.setPlaybackSpeed(1.0f);
                }
            }

            @Override // view.TimeRulerView.OnTimeChangedListener
            public void seekError() {
                StorageFragment.this.vodPlayer.pause();
                StorageFragment.this.dismissBuffering();
                StorageFragment.this.dismissPlayButton();
                StorageFragment.this.cancelUpdateTimeline();
            }
        });
        this.ll_play.setOnClickListener(this);
        this.ll_spinner_speed.setOnClickListener(this);
        this.listenerBtn.setOnClickListener(this);
        this.ll_listener.setOnClickListener(this);
        this.exoZoomBtn.setOnClickListener(this);
        this.ll_zoom.setOnClickListener(this);
        this.exoZoomBtn.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.23
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    StorageFragment.this.exoZoomBtn.setBackgroundResource(R.drawable.expand_record_selected);
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                StorageFragment.this.exoZoomBtn.setBackgroundResource(R.drawable.expand_record);
                return false;
            }
        });
        this.ll_zoom.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.24
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 0) {
                    StorageFragment.this.exoZoomBtn.setBackgroundResource(R.drawable.expand_record_selected);
                    return false;
                }
                if (motionEvent.getAction() != 1) {
                    return false;
                }
                StorageFragment.this.exoZoomBtn.setBackgroundResource(R.drawable.expand_record);
                return false;
            }
        });
        this.isPause.observe(this, new Observer<Boolean>() { // from class: fragment.StorageFragment.25
            @Override // androidx.lifecycle.Observer
            public void onChanged(@Nullable Boolean bool) {
                if (bool.booleanValue()) {
                    StorageFragment.this.vodPlayer.pause();
                    StorageFragment.this.full_stop.setBackgroundResource(R.drawable.play_record_full);
                    StorageFragment.this.playButton.setImageDrawable(StorageFragment.this.getResources().getDrawable(R.drawable.play_record));
                } else {
                    StorageFragment.this.vodPlayer.start();
                    StorageFragment.this.full_stop.setBackgroundResource(R.drawable.stop_play_record_full);
                    StorageFragment.this.playButton.setImageDrawable(StorageFragment.this.getResources().getDrawable(R.drawable.stop_play_record));
                }
            }
        });
        this.playBtn.setOnClickListener(new View.OnClickListener() { // from class: fragment.StorageFragment.26
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                if (StorageFragment.this.currentDoubleId == 0) {
                    if (StorageFragment.this.storageStatusValues != 0) {
                        StorageFragment.this.dismissPlayButton();
                        StorageFragment.this.replayVideo(0L);
                        return;
                    }
                    return;
                }
                if (StorageFragment.this.currentDoubleId != 1 || StorageFragment.this.storageStatusValues1 == 0) {
                    return;
                }
                StorageFragment.this.dismissPlayButton();
                StorageFragment.this.replayVideo(0L);
            }
        });
        this.calendarView.setSelectSingleMode();
        this.calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() { // from class: fragment.StorageFragment.27
            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarOutOfRange(CalendarModel calendarModel) {
                StorageFragment storageFragment = StorageFragment.this;
                storageFragment.showToast(storageFragment.getString(R.string.start_below_current_date));
            }

            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarSelect(CalendarModel calendarModel, boolean z) {
                if (z) {
                    if (StorageFragment.this.currentDoubleId == 0) {
                        if (StorageFragment.this.storageStatusValues == 0) {
                            return;
                        }
                    } else if (StorageFragment.this.currentDoubleId == 1 && StorageFragment.this.storageStatusValues == 1) {
                        return;
                    }
                    StorageFragment.this.tv_month.setText(String.format("%02d", Integer.valueOf(calendarModel.getMonth())));
                    StorageFragment.this.tv_day.setText(String.format("%02d", Integer.valueOf(calendarModel.getDay())));
                    StorageFragment.this.year = calendarModel.getYear();
                    StorageFragment.this.month = calendarModel.getMonth();
                    StorageFragment.this.day = calendarModel.getDay();
                    long[] stEtTime = StorageFragment.this.getStEtTime();
                    StorageFragment.this.curBeginTime = (int) (stEtTime[0] / 1000);
                    StorageFragment.this.curEndTime = (int) (stEtTime[1] / 1000);
                    StorageFragment.this.tv_curdate.setTextColor(StorageFragment.this.getResources().getColor(R.color.colorAccent));
                    StorageFragment.this.resetData();
                    StorageFragment.this.isRequestNone = true;
                    if (StorageFragment.this.finalCloudRecordBeanList != null) {
                        StorageFragment.this.finalCloudRecordBeanList.clear();
                    }
                    StorageFragment.this.count = 0;
                    StorageFragment storageFragment = StorageFragment.this;
                    storageFragment.getCardRecordList(storageFragment.curBeginTime, StorageFragment.this.curEndTime, false);
                    StorageFragment.this.pageStart = 0;
                    StorageFragment.this.deviceInfoBeans.clear();
                    StorageFragment storageFragment2 = StorageFragment.this;
                    storageFragment2.getEventRecordList(storageFragment2.f7879activity.iotId, stEtTime[0], stEtTime[1], 0, StorageFragment.this.pageStart, 100);
                    StorageFragment.this.ll_date.setVisibility(8);
                    if (StorageFragment.this.ll_date.getVisibility() == 8) {
                        StorageFragment.this.layout_control.setVisibility(0);
                    }
                    StorageFragment.this.isCalendarShow = false;
                    StorageFragment.this.layout_rv_list.setVisibility(0);
                }
            }
        });
        IPCManager.getInstance().getDevice(this.f7879activity.iotId).QueryRecordTimeList(new IPanelCallback() { // from class: fragment.StorageFragment.28
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    Log.d(StorageFragment.TAG, "onComplete: " + z);
                    ArrayList arrayList = new ArrayList();
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code") && object.get("code").equals(200)) {
                        for (int i = 0; i < object.getJSONObject("data").getJSONArray("DateList").size(); i++) {
                            arrayList.add(object.getJSONObject("data").getJSONArray("DateList").getJSONObject(i).getInteger("Date"));
                        }
                    }
                    StorageFragment.this.drawCircle(arrayList);
                }
            }
        });
        this.calendarView.setOnMonthChangeListener(new CalendarView.OnMonthChangeListener() { // from class: fragment.StorageFragment.29
            @Override // com.haibin.calendarview.CalendarView.OnMonthChangeListener
            public void onMonthChange(int i, int i2) {
                if (StorageFragment.this.isAdded()) {
                    StorageFragment.this.tv_curdate.setText(i + Constants.ACCEPT_TIME_SEPARATOR_SERVER + String.format("%02d", Integer.valueOf(i2)));
                    if (StorageFragment.this.year != i || StorageFragment.this.month != i2) {
                        StorageFragment.this.tv_curdate.setTextColor(StorageFragment.this.getResources().getColor(R.color.color_black));
                    } else {
                        StorageFragment.this.tv_curdate.setTextColor(StorageFragment.this.getResources().getColor(R.color.colorAccent));
                    }
                }
            }
        });
        this.cardSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class: fragment.StorageFragment.30
            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (StorageFragment.this.vodPlayer.getDuration() > 0) {
                    int duration = (int) ((((long) i) * StorageFragment.this.vodPlayer.getDuration()) / ((long) StorageFragment.this.cardSeekBar.getMax()));
                    StorageFragment.this.durationTv.setText(StorageFragment.this.timeLineFormatter.format(Integer.valueOf(duration)) + "/" + StorageFragment.this.timeLineFormatter.format(Long.valueOf(StorageFragment.this.vodPlayer.getDuration())));
                }
            }

            @Override // android.widget.SeekBar.OnSeekBarChangeListener
            public void onStopTrackingTouch(SeekBar seekBar) {
                StorageFragment.this.seek2((int) ((((long) seekBar.getProgress()) * StorageFragment.this.vodPlayer.getDuration()) / ((long) StorageFragment.this.cardSeekBar.getMax())));
                StorageFragment.this.updateTimeline();
            }
        });
        this.playerGLSurfaceView = (MyGlTextureView) view2.findViewById(R.id.card_player_surface_view);
        this.playerGLSurfaceView.setScaleMode(true);
        this.playerGLSurfaceView.setOnClickListener(new View.OnClickListener() { // from class: fragment.StorageFragment.31
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                StorageFragment.this.isFloat = !r2.isFloat;
                StorageFragment.this.setFloatBarState();
            }
        });
        this.vodPlayer = new VodPlayer();
        this.vodPlayer.setVideoScalingMode(1);
        this.vodPlayer.setTextureView(this.playerGLSurfaceView);
        this.vodPlayer.setVolume(this.speakerSwitch ? 1.0f : 0.0f);
        setListen(this.speakerSwitch);
        this.vodPlayer.setOnCompletionListener(new OnCompletionListener() { // from class: fragment.StorageFragment.32
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnCompletionListener
            public void onCompletion() {
                StorageFragment.this.cancelUpdateTimeline();
                int currentTime = StorageFragment.this.timeRulerView.getCurrentTime();
                int i = currentTime / 3600;
                int i2 = currentTime % 3600;
                int i3 = i2 / 60;
                int i4 = i2 % 60;
                if (DateUtil.getTimeMillins(i, i3, i4) <= 0) {
                    StorageFragment.this.seek2(0L);
                } else {
                    StorageFragment.this.seek2(DateUtil.getTimeMillins(i, i3, i4));
                }
            }
        });
        this.vodPlayer.setOnErrorListener(new OnErrorListener() { // from class: fragment.StorageFragment.33
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnErrorListener
            public void onError(final PlayerException playerException) {
                StorageFragment.this.f7879activity.runOnUiThread(new Runnable() { // from class: fragment.StorageFragment.33.1
                    @Override // java.lang.Runnable
                    @SuppressLint({"StringFormatInvalid"})
                    public void run() {
                        if (StorageFragment.this.getActivity() == null || StorageFragment.this.getActivity().isFinishing()) {
                            return;
                        }
                        StorageFragment.this.dismissBuffering();
                        StorageFragment.this.cancelUpdateTimeline();
                        Log.e("播放器日志", playerException.getCode() + "  getSubCode=" + playerException.getSubCode());
                        switch (playerException.getCode()) {
                            case 6:
                                switch (playerException.getSubCode()) {
                                    case 1005:
                                        StorageFragment.this.showToast(StorageFragment.this.getString(R.string.connect_failed, Integer.valueOf(playerException.getSubCode())));
                                        break;
                                    case 1006:
                                        StorageFragment.this.showToast(StorageFragment.this.getString(R.string.connect_failed, Integer.valueOf(playerException.getSubCode())));
                                        break;
                                    case 1008:
                                        StorageFragment.this.showToast(StorageFragment.this.getString(R.string.connect_failed, Integer.valueOf(playerException.getSubCode())));
                                        break;
                                    case 1009:
                                        StorageFragment.this.showToast(StorageFragment.this.getString(R.string.connect_failed, Integer.valueOf(playerException.getSubCode())));
                                        break;
                                }
                                break;
                            case 7:
                                if (playerException.getSubCode() == 1000) {
                                    StorageFragment.this.showToast(StorageFragment.this.getString(R.string.play_failed_retry, Integer.valueOf(playerException.getSubCode())));
                                }
                                break;
                            case 8:
                                if (playerException.getSubCode() == 1100) {
                                    StorageFragment.this.showToast(StorageFragment.this.getString(R.string.play_failed_retry, Integer.valueOf(playerException.getSubCode())));
                                }
                                break;
                        }
                        if (SharePreferenceManager.getInstance().getStorageStatus(StorageFragment.this.f7879activity.iotId) != 0) {
                            StorageFragment.this.showPlayButton();
                        }
                    }
                });
            }
        });
        this.vodPlayer.setOnPlayerStateChangedListener(new OnPlayerStateChangedListener() { // from class: fragment.StorageFragment.34
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPlayerStateChangedListener
            public void onPlayerStateChange(int i) {
                switch (i) {
                    case 1:
                        Log.d(StorageFragment.TAG, "puppet:STATE_IDLE");
                        break;
                    case 2:
                        if (StorageFragment.this.isRequestNone || StorageFragment.this.videoInfoList.size() > 0) {
                            StorageFragment.this.dismissPlayButton();
                            StorageFragment.this.showBuffering();
                            Log.d(StorageFragment.TAG, "puppet:STATE_BUFFERING");
                        }
                        break;
                    case 3:
                        StorageFragment.this.dismissBuffering();
                        StorageFragment.this.showPlayInfo();
                        StorageFragment.this.updateTimeline();
                        Log.d(StorageFragment.TAG, "puppet:STATE_READY");
                        StorageFragment.this.playerGLSurfaceView.setVisibility(0);
                        StorageFragment.this.iv.setVisibility(8);
                        break;
                    case 4:
                        Log.d(StorageFragment.TAG, "puppet:STATE_ENDED");
                        StorageFragment.this.stopRecording();
                        break;
                }
            }
        });
        this.ll_md.setOnClickListener(new View.OnClickListener() { // from class: fragment.StorageFragment.35
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                StorageFragment.this.isCalendarShow = !r4.isCalendarShow;
                StorageFragment.this.ll_date.setVisibility(StorageFragment.this.ll_date.getVisibility() == 0 ? 8 : 0);
                if (StorageFragment.this.isCalendarShow) {
                    StorageFragment.this.layout_rv_list.setVisibility(8);
                    StorageFragment.this.layout_control.setVisibility(8);
                } else {
                    StorageFragment.this.layout_rv_list.setVisibility(0);
                    StorageFragment.this.layout_control.setVisibility(0);
                }
            }
        });
        this.year = DateUtil.getYear();
        this.month = DateUtil.getMonth();
        this.day = DateUtil.getDay();
        this.tv_month.setText(String.format("%02d", Integer.valueOf(this.month)));
        this.tv_day.setText(String.format("%02d", Integer.valueOf(this.day)));
        this.tv_curdate.setText(this.year + Constants.ACCEPT_TIME_SEPARATOR_SERVER + String.format("%02d", Integer.valueOf(this.month)));
        this.calendarView.setRange(2000, 1, 1, this.year, this.month, this.day);
        this.phoneTimeZone = TimeZone.getDefault().getRawOffset();
        if (!"".equals(SharePreferenceManager.getInstance().getDeviceTZ(this.f7879activity.iotId))) {
            String strReplace = SharePreferenceManager.getInstance().getDeviceTZ(this.f7879activity.iotId).replace("--", Constants.ACCEPT_TIME_SEPARATOR_SERVER);
            Log.e("时区-设备", "" + strReplace);
            Log.e("时区-手机", "" + this.phoneTimeZone);
            if (this.phoneTimeZone != 0 && strReplace.length() >= 9) {
                String[] strArrSplit = strReplace.substring(4, 9).split(":");
                this.deviceTimeZone = ((Integer.parseInt(strArrSplit[0]) * 60) + Integer.parseInt(strArrSplit[1])) * 60 * 1000;
                Log.e("时区时间戳", "" + this.deviceTimeZone);
                if (MqttTopic.SINGLE_LEVEL_WILDCARD.equals(String.valueOf(strReplace.charAt(3)))) {
                    this.differenceTimeZone = ((long) this.phoneTimeZone) - this.deviceTimeZone;
                } else {
                    this.differenceTimeZone = ((long) this.phoneTimeZone) - (-this.deviceTimeZone);
                }
            }
        }
        long[] stEtTime = getStEtTime();
        this.curBeginTime = (int) (stEtTime[0] / 1000);
        this.curEndTime = (int) (stEtTime[1] / 1000);
        setFloatBarState();
        float f = (ScreenUtil.getDisplayMetrics(getActivity())[1] * 1.0f) / ScreenUtil.getDisplayMetrics(getActivity())[0];
        if (f < 1.85d || f >= 2.0f) {
            int i = (f > 2.0f ? 1 : (f == 2.0f ? 0 : -1));
        }
        if (this.f7879activity.iotId1 != null && !"".equals(this.f7879activity.iotId1)) {
            RecordVideoActivity recordVideoActivity = this.f7879activity;
            recordVideoActivity.iotId = recordVideoActivity.iotId1;
        }
        this.storageStatusValues = SharePreferenceManager.getInstance().getStorageStatus(this.f7879activity.iotId);
        Log.e("卡状态0", this.f7879activity.iotId + "   " + this.storageStatusValues);
        int i2 = this.storageStatusValues;
        this.storageStatusValues1 = i2;
        if (i2 == 0) {
            showToast(getResources().getString(R.string.device_not_use_sd));
            this.tf_text.setVisibility(0);
        } else {
            this.isRequestNone = true;
            getCardRecordList(this.curBeginTime, this.curEndTime, true);
            getEventRecordList(this.f7879activity.iotId, stEtTime[0], stEtTime[1], 0, this.pageStart, 100);
        }
        this.full_before_ten_seconds_ll.setOnClickListener(this);
        this.full_before_ten_seconds_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.36
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                return false;
            }
        });
        this.full_after_ten_seconds_ll.setOnClickListener(this);
        this.full_after_ten_seconds_ll.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.StorageFragment.37
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                return false;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlayButton() {
        this.videoBufferingProgressBar.setVisibility(8);
        this.playBtn.setVisibility(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void netEventChange(NetWorkEvent netWorkEvent) {
        int i = this.currentDoubleId;
        if (i == 0) {
            if (this.storageStatusValues != 0) {
                replayVideo(0L);
            }
        } else {
            if (i != 1 || this.storageStatusValues1 == 0) {
                return;
            }
            replayVideo(0L);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPlayButton() {
        this.playBtn.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getEventRecordList(String str, long j, long j2, int i, int i2, int i3) {
        HashMap map = new HashMap();
        map.put("iotId", str);
        map.put("beginTime", Long.valueOf(j));
        map.put(AUserTrack.UTKEY_END_TIME, Long.valueOf(j2));
        map.put("eventType", Integer.valueOf(i));
        map.put("pageStart", Integer.valueOf(i2));
        map.put(AlinkConstants.KEY_PAGE_SIZE, Integer.valueOf(i3));
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/event/query").setApiVersion("2.1.2").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new AnonymousClass38());
    }

    /* JADX INFO: renamed from: fragment.StorageFragment$38, reason: invalid class name */
    class AnonymousClass38 implements IoTCallback {
        AnonymousClass38() {
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onFailure(IoTRequest ioTRequest, Exception exc) {
            LogEx.d(true, StorageFragment.TAG, exc.getLocalizedMessage());
        }

        @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
        public void onResponse(IoTRequest ioTRequest, final IoTResponse ioTResponse) {
            LogEx.e(true, StorageFragment.TAG, "getEventRecordList:" + ioTResponse.getData() + "");
            final int code = ioTResponse.getCode();
            ioTResponse.getLocalizedMsg();
            StorageFragment.this.uiHandler.post(new Runnable() { // from class: fragment.StorageFragment.38.1
                @Override // java.lang.Runnable
                public void run() {
                    if (code != 200) {
                        return;
                    }
                    try {
                        List array = JSON.parseArray(((org.json.JSONObject) ioTResponse.getData()).getString("eventList"), EventModel.class);
                        if (StorageFragment.this.f7879activity == null || StorageFragment.this.f7879activity.isFinishing() || !StorageFragment.this.isAdded()) {
                            return;
                        }
                        ListIterator listIterator = array.listIterator();
                        while (listIterator.hasNext()) {
                            EventModel eventModel = (EventModel) listIterator.next();
                            if (!AppConfig.isChina) {
                                long dateTime = DateUtil.getDateTime(eventModel.eventTimeUTC, new SimpleDateFormat(DateUtil.DATE_FORMAT));
                                Log.e("设备报警图片时间", "" + dateTime + "          " + eventModel.eventTime);
                                eventModel.eventTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(StorageFragment.this.deviceTimeZone + dateTime));
                                Log.e("设备报警图片时间", "" + dateTime + "     修改   " + eventModel.eventTime);
                            }
                            if (eventModel.eventType != 2 && !eventModel.eventPicUrl.equals("")) {
                                StorageFragment.this.deviceInfoBeans.add(eventModel);
                            } else {
                                listIterator.remove();
                            }
                        }
                        boolean z = ((org.json.JSONObject) ioTResponse.getData()).getBoolean("nextValid");
                        Log.e("下一页是否有效", "" + z);
                        if (z) {
                            StorageFragment.access$4508(StorageFragment.this);
                            long[] stEtTime = StorageFragment.this.getStEtTime();
                            StorageFragment.this.getEventRecordList(StorageFragment.this.f7879activity.iotId, stEtTime[0], stEtTime[1], 0, StorageFragment.this.pageStart, 100);
                        } else if (StorageFragment.this.deviceInfoBeans.size() != 0) {
                            StorageFragment.this.mCloudPictureAdapter.setNewData(null);
                            StorageFragment.this.mCloudPictureAdapter.setNewData(StorageFragment.this.deviceInfoBeans);
                        } else {
                            IPCManager.getInstance().getDevice(StorageFragment.this.f7879activity.iotId).queryCardRecordList(StorageFragment.this.curBeginTime, StorageFragment.this.curEndTime, 0, 0, new IPanelCallback() { // from class: fragment.StorageFragment.38.1.1
                                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                                public void onComplete(boolean z2, Object obj) {
                                    JSONArray jSONArray;
                                    if (!z2 || obj == null || String.valueOf(obj).equals("")) {
                                        return;
                                    }
                                    String string = obj.toString();
                                    if (TextUtils.isEmpty(string)) {
                                        return;
                                    }
                                    try {
                                        JSONObject jSONObject = JSON.parseObject(string).getJSONObject("data");
                                        if (jSONObject == null || (jSONArray = jSONObject.getJSONArray("TimeList")) == null || jSONArray.size() <= 0) {
                                            return;
                                        }
                                        for (int i = 0; i < jSONArray.size(); i++) {
                                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                                            EventModel eventModel2 = new EventModel();
                                            long jLongValue = jSONObject2.getLong("BeginTime").longValue();
                                            eventModel2.eventFileTime = jSONObject2.getLong("EndTime").longValue() - jLongValue;
                                            eventModel2.eventTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(jLongValue * 1000));
                                            if (!StorageFragment.this.deviceInfoBeans.contains(eventModel2) && jSONObject2.getInteger("Type").intValue() == 1) {
                                                StorageFragment.this.deviceInfoBeans.add(0, eventModel2);
                                            }
                                        }
                                        StorageFragment.this.mCloudPictureAdapter.setNewData(null);
                                        StorageFragment.this.mCloudPictureAdapter.setNewData(StorageFragment.this.deviceInfoBeans);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public void requestPicture() {
        ArrayList arrayList = new ArrayList();
        arrayList.add("My9BTHpPT3RDdURCTF93OHFralNlaUZHYXNiSTZsQ0wtdEJxRUc4bklfOExvLzBkNmQxMmZlZDY2MDQxNDlhODhlOTBmMjM3ZjlkNzMyXzE3MTU4MjcxMjg0Nzg=");
        HashMap map = new HashMap();
        map.put("iotId", this.f7879activity.iotId);
        map.put("pictureIdList", arrayList);
        map.put("type", 0);
        new IoTAPIClientFactory().getClient().send(new IoTRequestBuilder().setScheme(Scheme.HTTPS).setPath("/vision/customer/picture/querybyids").setApiVersion("2.1.0").setAuthType(AlinkConstants.KEY_IOT_AUTH).setParams(map).build(), new IoTCallback() { // from class: fragment.StorageFragment.39
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                if (ioTResponse.getCode() != 200) {
                    return;
                }
                try {
                    LogEx.e(true, StorageFragment.TAG, "getEventRecordList:getPicDetail:" + ioTResponse.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setListen(boolean z) {
        int i = this.f7879activity.getResources().getConfiguration().orientation;
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
        this.f7879activity.runOnUiThread(new Runnable() { // from class: fragment.StorageFragment.40
            @Override // java.lang.Runnable
            public void run() {
                if (StorageFragment.this.getActivity() == null || StorageFragment.this.getActivity().isFinishing()) {
                    return;
                }
                Toast.makeText(StorageFragment.this.f7879activity, str, 0).show();
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
            this.timelineUpdateHandle = this.scheduledExecutorService.scheduleAtFixedRate(this.timelineUpdateTask, 1000L, 1000L, TimeUnit.MILLISECONDS);
        }
        VodPlayer vodPlayer = this.vodPlayer;
        if (vodPlayer != null) {
            long duration = vodPlayer.getDuration();
            if (duration <= 0) {
                this.durationTv.setText("-/-");
                this.cardSeekBar.setProgress(0);
                return;
            }
            this.cardSeekBar.setProgress((int) ((this.vodPlayer.getCurrentPosition() * ((long) this.cardSeekBar.getMax())) / duration));
            this.durationTv.setText(this.timeLineFormatter.format(Long.valueOf(this.vodPlayer.getCurrentPosition())) + "/" + this.timeLineFormatter.format(Long.valueOf(duration)));
            long currentPosition = this.vodPlayer.getCurrentPosition();
            Log.e("TimeRuler  vodPlayer", "" + currentPosition);
            if (currentPosition >= duration) {
                stopVideo();
                return;
            }
            if (this.videoInfoList.size() > 0) {
                this.timeRulerView.setCurrentTime(DateUtil.getHMSTimeSecs(Integer.parseInt(this.videoInfoList.get(0).beginTime)) + ((int) (currentPosition / 1000)));
            }
            int i = (int) (currentPosition / 1000);
            this.timeRulerView.setCurrentTime(i);
            this.timeTextView.setText(TimeRulerView.formatTimeHHmmss(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPlayInfo() {
        if (this.isFloat) {
            this.playInfoTv.setVisibility(0);
        }
        updatePlayInfo();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissPlayInfo() {
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
        PlayInfo currentPlayInfo = this.vodPlayer.getCurrentPlayInfo();
        this.playInfoTv.setText(((currentPlayInfo.bitRate / 1024) / 8) + "KB/S");
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.vodPlayer.start();
        long j = this.pauseLong;
        if (j > 0) {
            replayVideo(j);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void replayVideo(final long j) {
        this.vodPlayer.stop();
        this.playerHandler.removeCallbacksAndMessages(null);
        this.playerHandler.post(new Runnable() { // from class: fragment.StorageFragment.43
            @Override // java.lang.Runnable
            public void run() {
                if (StorageFragment.this.f7879activity == null || StorageFragment.this.f7879activity.isFinishing()) {
                    return;
                }
                StorageFragment storageFragment = StorageFragment.this;
                storageFragment.initPlayVideo(storageFragment.curBeginTime, StorageFragment.this.curEndTime, true, 0, j);
            }
        });
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        this.pauseLong = this.vodPlayer.getCurrentPosition();
        this.vodPlayer.pause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        stopRecording();
        int i = this.currentDoubleId;
        if (i == 0) {
            if (this.storageStatusValues != 0) {
                this.vodPlayer.stop();
            }
        } else if (i == 1 && this.storageStatusValues1 != 0) {
            this.vodPlayer.stop();
        }
        dismissPlayInfo();
        Timer timer = this.timer;
        if (timer != null) {
            timer.cancel();
            this.timer = null;
        }
        this.tvRecordTime.setText("");
        this.tvRecordTime.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void stopRecording() {
        if (this.isRecordingMp4) {
            VodPlayer vodPlayer = this.vodPlayer;
            if (vodPlayer != null && vodPlayer.stopRecordingContent()) {
                if (this.countTime <= 3) {
                    File file = this.file;
                    if (file != null && file.exists()) {
                        this.file.delete();
                    }
                } else {
                    EventBus.getDefault().post(new RefreshPicture());
                    long j = 0;
                    try {
                        j = Long.parseLong(this.file.getName().substring(0, this.file.getName().indexOf(".")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        MediaStoreUtil.addVideoFileToMediaStore(getActivity(), this.file, j);
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
            }
            this.isRecordingMp4 = false;
            setRecordState();
            Timer timer = this.timer;
            if (timer != null) {
                timer.cancel();
                this.timer = null;
            }
            this.tvRecordTime.setText("");
            this.tvRecordTime.setVisibility(8);
        }
    }

    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        cancelUpdateTimeline();
        stopVideo();
        this.vodPlayer.release();
        this.uiHandler.removeCallbacksAndMessages(null);
        this.playerHandler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initPlayVideo(int i, int i2, boolean z, int i3, long j) {
        cancelUpdateTimeline();
        stopVideo();
        Log.e("TimeRuler 开始播放", "beginTime= " + i + "      endTime= " + i2);
        this.vodPlayer.setDataSourceByIPCRecordTime(this.f7879activity.iotId, i, i2, j);
        this.vodPlayer.setOnPreparedListener(new OnPreparedListener() { // from class: fragment.StorageFragment.44
            @Override // com.aliyun.iotx.linkvisual.media.video.listener.OnPreparedListener
            public void onPrepared() {
                StorageFragment.this.vodPlayer.start();
                if (StorageFragment.this.newTime > 0) {
                    new Handler().postDelayed(new Runnable() { // from class: fragment.StorageFragment.44.1
                        @Override // java.lang.Runnable
                        public void run() {
                            StorageFragment.this.seek2(DateUtil.getTimeMillins(StorageFragment.this.newTime / 3600, (StorageFragment.this.newTime % 3600) / 60, (StorageFragment.this.newTime % 3600) % 60));
                            StorageFragment.this.updateTimeline();
                        }
                    }, 100L);
                }
            }
        });
        this.vodPlayer.prepare();
        keepScreenLight();
    }

    private void stopVideo() {
        this.vodPlayer.stop();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showBuffering() {
        this.f7879activity.runOnUiThread(new Runnable() { // from class: fragment.StorageFragment.45
            @Override // java.lang.Runnable
            public void run() {
                if (StorageFragment.this.getActivity() == null || StorageFragment.this.getActivity().isFinishing()) {
                    return;
                }
                StorageFragment.this.videoBufferingProgressBar.setVisibility(0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dismissBuffering() {
        this.f7879activity.runOnUiThread(new Runnable() { // from class: fragment.StorageFragment.46
            @Override // java.lang.Runnable
            public void run() {
                if (StorageFragment.this.getActivity() == null || StorageFragment.this.getActivity().isFinishing()) {
                    return;
                }
                StorageFragment.this.videoBufferingProgressBar.setVisibility(8);
            }
        });
    }

    public void getCardRecordList(final int i, final int i2, boolean z) {
        IPCManager.getInstance().getDevice(this.f7879activity.iotId).queryCardRecordList(i, i2, 0, 0, new IPanelCallback() { // from class: fragment.StorageFragment.47
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z2, final Object obj) {
                if (!z2 || obj == null || String.valueOf(obj).equals("")) {
                    return;
                }
                StorageFragment.this.uiHandler.post(new Runnable() { // from class: fragment.StorageFragment.47.1
                    @Override // java.lang.Runnable
                    public void run() {
                        JSONObject jSONObject;
                        if (StorageFragment.this.f7879activity == null || StorageFragment.this.f7879activity.isFinishing() || !StorageFragment.this.isAdded() || StorageFragment.this.f7879activity == null || StorageFragment.this.f7879activity.isFinishing() || i != StorageFragment.this.curBeginTime || i2 != StorageFragment.this.curEndTime) {
                            return;
                        }
                        String string = obj.toString();
                        if (TextUtils.isEmpty(string)) {
                            return;
                        }
                        try {
                            jSONObject = JSON.parseObject(string).getJSONObject("data");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (jSONObject == null) {
                            return;
                        }
                        JSONArray jSONArray = jSONObject.getJSONArray("TimeList");
                        if (jSONArray == null || jSONArray.size() <= 0) {
                            StorageFragment.this.showToast(StorageFragment.this.f7879activity.getResources().getString(R.string.date_no_record));
                            StorageFragment.this.dismissBuffering();
                            StorageFragment.this.dismissPlayButton();
                            StorageFragment.this.dismissPlayInfo();
                        } else {
                            if (StorageFragment.this.videoInfoList == null) {
                                StorageFragment.this.videoInfoList = new ArrayList();
                            } else {
                                StorageFragment.this.videoInfoList.clear();
                            }
                            for (int i3 = 0; i3 < jSONArray.size(); i3++) {
                                JSONObject jSONObject2 = jSONArray.getJSONObject(i3);
                                VideoInfo videoInfo = new VideoInfo();
                                videoInfo.iotId = StorageFragment.this.f7879activity.iotId;
                                videoInfo.recordType = jSONObject2.getInteger("Type").intValue();
                                videoInfo.beginTime = jSONObject2.getString("BeginTime");
                                videoInfo.endTime = jSONObject2.getString("EndTime");
                                if (!StorageFragment.this.videoInfoList.contains(videoInfo) && StorageFragment.this.verifyTimeRect(videoInfo)) {
                                    StorageFragment.this.videoInfoList.add(videoInfo);
                                }
                            }
                            Collections.sort(StorageFragment.this.videoInfoList);
                            List<EventModel> data = StorageFragment.this.mCloudPictureAdapter.getData();
                            if (data.size() > 0) {
                                if ((StorageFragment.this.videoInfoList.size() > 0 ? (VideoInfo) StorageFragment.this.videoInfoList.get(StorageFragment.this.videoInfoList.size() - 1) : null) != null) {
                                    StorageFragment.this.mCloudPictureAdapter.replaceData(data);
                                }
                            }
                            StorageFragment.this.initVideo();
                            if (StorageFragment.this.videoInfoList.size() > 0) {
                                if (DateUtil.getYear() != StorageFragment.this.year || DateUtil.getMonth() != StorageFragment.this.month || DateUtil.getDay() != StorageFragment.this.day) {
                                    StorageFragment.this.replayVideo(0L);
                                } else if (StorageFragment.this.videoInfoList.size() > 1) {
                                    StorageFragment.this.replayVideo((Long.parseLong(((VideoInfo) StorageFragment.this.videoInfoList.get(StorageFragment.this.videoInfoList.size() - 2)).beginTime) - ((long) StorageFragment.this.curBeginTime)) * 1000);
                                } else if (StorageFragment.this.videoInfoList.size() == 1) {
                                    StorageFragment.this.replayVideo((Long.parseLong(((VideoInfo) StorageFragment.this.videoInfoList.get(StorageFragment.this.videoInfoList.size() - 1)).beginTime) - ((long) StorageFragment.this.curBeginTime)) * 1000);
                                }
                            }
                        }
                        StorageFragment.this.isRequestNone = false;
                    }
                });
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean verifyTimeRect(VideoInfo videoInfo) {
        return Integer.parseInt(videoInfo.endTime) >= this.curBeginTime && this.curEndTime >= Integer.parseInt(videoInfo.beginTime);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initVideo() {
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
        Log.e("时间尺视频", "" + JSON.toJSONString(this.videoInfoList));
        for (int i = 0; i < this.videoInfoList.size(); i++) {
            VideoInfo videoInfo = this.videoInfoList.get(i);
            int i2 = Integer.parseInt(videoInfo.beginTime);
            int i3 = this.curBeginTime;
            if (i2 < i3) {
                videoInfo.beginTime = String.valueOf(i3);
            }
            this.stList.add(Integer.valueOf(DateUtil.getHMSTimeSecs(Integer.parseInt(videoInfo.beginTime))));
            int i4 = Integer.parseInt(videoInfo.endTime);
            int i5 = this.curEndTime;
            if (i4 > i5) {
                videoInfo.endTime = String.valueOf(i5);
            }
            this.etList.add(Integer.valueOf(DateUtil.getHMSTimeSecs(Integer.parseInt(videoInfo.endTime))));
            if (videoInfo.recordType == 1) {
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
        Log.e("时间尺", "" + JSON.toJSONString(arrayList));
        this.timeRulerView.setTimePartListAndBackCenter(arrayList);
    }

    private CalendarModel getSchemeCalendar(int i, int i2, int i3, int i4, String str) {
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.setYear(i);
        calendarModel.setMonth(i2);
        calendarModel.setDay(i3);
        return calendarModel;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawCircle(List<Integer> list) {
        int i;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        HashMap map = new HashMap();
        for (int i2 = 0; i2 < list.size(); i2++) {
            String str = simpleDateFormat.format(new Date(Long.valueOf(list.get(i2) + "000").longValue()));
            int i3 = Integer.parseInt(str.substring(0, 4));
            int i4 = Integer.parseInt(str.substring(5, 7));
            String strSubstring = str.substring(8, 10);
            if (strSubstring.substring(0, 1).equals("0")) {
                i = Integer.parseInt(strSubstring.substring(1, 2));
            } else {
                i = Integer.parseInt(strSubstring);
            }
            int i5 = i;
            map.put(getSchemeCalendar(i3, i4, i5, -12526811, "").toString(), getSchemeCalendar(i3, i4, i5, -12526811, ""));
        }
        this.calendarView.setSchemeDate(map);
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
        List<VideoInfo> list4 = this.videoInfoList;
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
            }
        }
        this.timeRulerView.setTimePartListAndBackCenter(arrayList);
        cancelUpdateTimeline();
        stopVideo();
        this.mCloudPictureAdapter.setNewData(null);
        this.deviceInfoBeans.clear();
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
        this.text3.setTextColor(getResources().getColor(R.color.color_white));
        this.text4.setTextColor(getResources().getColor(R.color.color_white));
        this.text5.setTextColor(getResources().getColor(R.color.color_white));
        this.text6.setTextColor(getResources().getColor(R.color.color_white));
    }

    private void keepScreenLight() {
        this.f7879activity.getWindow().addFlags(128);
    }

    public void seek2(long j) {
        Log.e("seek时间", "" + j);
        if (this.vodPlayer.getPlayState() == 3 || this.vodPlayer.getPlayState() == 2) {
            this.vodPlayer.seekTo(j);
        } else {
            replayVideo(j);
        }
    }

    public void switch2ShotMachine(int i) {
        this.newTime = this.timeRulerView.getCurrentTime();
        if (this.currentDoubleId != i) {
            if (i == 3) {
                RecordVideoActivity recordVideoActivity = this.f7879activity;
                recordVideoActivity.iotId = recordVideoActivity.iotId4;
                this.shot_record_l3.setSelected(true);
                this.shot_record_l2.setSelected(false);
                this.shot_record_ll.setSelected(false);
                this.circle_record_ll.setSelected(false);
                Log.e("卡状态", this.f7879activity.iotId + "   " + this.storageStatusValues1);
                if (this.storageStatusValues1 == 0) {
                    showToast(getResources().getString(R.string.device_not_use_sd));
                    this.tf_text.setVisibility(0);
                    return;
                }
            }
            if (i == 2) {
                RecordVideoActivity recordVideoActivity2 = this.f7879activity;
                recordVideoActivity2.iotId = recordVideoActivity2.iotId3;
                this.shot_record_l3.setSelected(false);
                this.shot_record_l2.setSelected(true);
                this.shot_record_ll.setSelected(false);
                this.circle_record_ll.setSelected(false);
                Log.e("卡状态", this.f7879activity.iotId + "   " + this.storageStatusValues1);
                if (this.storageStatusValues1 == 0) {
                    showToast(getResources().getString(R.string.device_not_use_sd));
                    this.tf_text.setVisibility(0);
                    return;
                }
            } else if (i == 1) {
                RecordVideoActivity recordVideoActivity3 = this.f7879activity;
                recordVideoActivity3.iotId = recordVideoActivity3.iotId2;
                this.shot_record_l3.setSelected(false);
                this.shot_record_l2.setSelected(false);
                this.shot_record_ll.setSelected(true);
                this.circle_record_ll.setSelected(false);
                Log.e("卡状态", this.f7879activity.iotId + "   " + this.storageStatusValues1);
                if (this.storageStatusValues1 == 0) {
                    showToast(getResources().getString(R.string.device_not_use_sd));
                    this.tf_text.setVisibility(0);
                    return;
                }
            } else if (i == 0) {
                RecordVideoActivity recordVideoActivity4 = this.f7879activity;
                recordVideoActivity4.iotId = recordVideoActivity4.iotId1;
                this.shot_record_l3.setSelected(false);
                this.shot_record_l2.setSelected(false);
                this.shot_record_ll.setSelected(false);
                this.circle_record_ll.setSelected(true);
                Log.e("卡状态", this.f7879activity.iotId + "   " + this.storageStatusValues);
                if (this.storageStatusValues == 0) {
                    showToast(getResources().getString(R.string.device_not_use_sd));
                    this.tf_text.setVisibility(0);
                    return;
                }
            }
            this.currentDoubleId = i;
            long[] stEtTime = getStEtTime();
            this.curBeginTime = (int) (stEtTime[0] / 1000);
            this.curEndTime = (int) (stEtTime[1] / 1000);
            this.tv_curdate.setTextColor(getResources().getColor(R.color.colorAccent));
            resetData();
            this.isRequestNone = true;
            List<EventModel> list = this.finalCloudRecordBeanList;
            if (list != null) {
                list.clear();
            }
            this.count = 0;
            getCardRecordList(this.curBeginTime, this.curEndTime, false);
            this.pageStart = 0;
            getEventRecordList(this.f7879activity.iotId, stEtTime[0], stEtTime[1], 0, this.pageStart, 100);
            this.ll_date.setVisibility(8);
            if (this.ll_date.getVisibility() == 8) {
                this.layout_control.setVisibility(0);
            }
            this.isCalendarShow = false;
            this.layout_rv_list.setVisibility(0);
        }
    }
}
