package activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import bean.CrossLineDetectBean;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.seculink.app.R;
import config.Constants;
import java.util.HashMap;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import tools.SpUtil;
import tools.Utils;
import view.ItemView;
import view.LineView;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class SetTheBoundaryActivity extends CommonActivity {
    private ItemView alarmDirectionItem;
    Button direction;
    Button edit;
    TitleView fl_titlebar;
    String iotId;
    boolean isSwitch1;
    ConstraintLayout layout_main;
    LineView lineView;
    private ItemView resetItem;
    private Button saveButton;
    private Handler uiHandler;
    boolean isEdit = false;
    int selectPosition = 0;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_set_the_boundary;
    }

    @Override // activity.CommonActivity, activity.SwipeBackActivity2, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        setSwipeBackEnable(false);
        this.uiHandler = new Handler(Looper.getMainLooper());
        this.iotId = getIntent().getStringExtra("iotId");
        Glide.with((FragmentActivity) this).asBitmap().load(SpUtil.getString(this, Utils.getDevSnapKey(this.iotId), "")).into(this.lineView.getPic());
        this.lineView.setIotId(this.iotId);
        this.edit.setOnClickListener(new View.OnClickListener() { // from class: activity.SetTheBoundaryActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SetTheBoundaryActivity.this.lineView.hide();
                SetTheBoundaryActivity.this.lineView.display();
            }
        });
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.fl_titlebar = (TitleView) findViewById(R.id.titleView);
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.SetTheBoundaryActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                SetTheBoundaryActivity.this.finish();
            }
        });
        this.lineView = (LineView) findViewById(R.id.line_view_item);
        this.edit = (Button) findViewById(R.id.edit);
        this.saveButton = (Button) findViewById(R.id.save);
        this.direction = (Button) findViewById(R.id.direction);
        this.alarmDirectionItem = (ItemView) findViewById(R.id.alarm_direction_item);
        this.resetItem = (ItemView) findViewById(R.id.reset_item);
        this.alarmDirectionItem.setOnClickListener(new View.OnClickListener() { // from class: activity.SetTheBoundaryActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SetTheBoundaryActivity.this.lineView.changeDirection();
            }
        });
        this.resetItem.setOnClickListener(new View.OnClickListener() { // from class: activity.SetTheBoundaryActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SetTheBoundaryActivity.this.lineView.hide();
                SetTheBoundaryActivity.this.lineView.display();
            }
        });
        this.direction.setOnClickListener(new View.OnClickListener() { // from class: activity.SetTheBoundaryActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SetTheBoundaryActivity.this.lineView.changeDirection();
            }
        });
        this.saveButton.setOnClickListener(new View.OnClickListener() { // from class: activity.SetTheBoundaryActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                SetTheBoundaryActivity.this.setCrossLine();
            }
        });
    }

    protected void setCrossLine() {
        HashMap map = new HashMap();
        final CrossLineDetectBean crossLineDetectBean = new CrossLineDetectBean();
        crossLineDetectBean.setEnable(1);
        crossLineDetectBean.setStartX((int) ((this.lineView.getEp().x / 1024.0f) * 640.0f));
        crossLineDetectBean.setStartY((int) ((this.lineView.getEp().y / 576.0f) * 360.0f));
        crossLineDetectBean.setEndX((int) ((this.lineView.getSp().x / 1024.0f) * 640.0f));
        crossLineDetectBean.setEndY((int) ((this.lineView.getSp().y / 576.0f) * 360.0f));
        crossLineDetectBean.setDirection(0);
        map.put(Constants.Cross_Line_Detect, crossLineDetectBean);
        IPCManager.getInstance().getDevice(this.iotId).setProperties(map, new IPanelCallback() { // from class: activity.SetTheBoundaryActivity.7
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (!z) {
                    SetTheBoundaryActivity.this.uiHandler.post(new Runnable() { // from class: activity.SetTheBoundaryActivity.7.3
                        @Override // java.lang.Runnable
                        public void run() {
                            Toast.makeText(SetTheBoundaryActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                        }
                    });
                    return;
                }
                SetTheBoundaryActivity.this.lineView.SaveBitmap(SetTheBoundaryActivity.this.iotId);
                if (obj == null || "".equals(String.valueOf(obj))) {
                    return;
                }
                JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                if (object.containsKey("code")) {
                    if (object.getInteger("code").intValue() != 200) {
                        SetTheBoundaryActivity.this.uiHandler.post(new Runnable() { // from class: activity.SetTheBoundaryActivity.7.1
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(SetTheBoundaryActivity.this.getActivity(), R.string.mofify_failed, 0).show();
                            }
                        });
                    } else {
                        SharePreferenceManager.getInstance().setCrossLineDetect(SetTheBoundaryActivity.this.iotId, new Gson().toJson(crossLineDetectBean));
                        SetTheBoundaryActivity.this.uiHandler.post(new Runnable() { // from class: activity.SetTheBoundaryActivity.7.2
                            @Override // java.lang.Runnable
                            public void run() {
                                Toast.makeText(SetTheBoundaryActivity.this.getActivity(), R.string.mofify_succeed, 0).show();
                            }
                        });
                    }
                }
            }
        });
    }
}
