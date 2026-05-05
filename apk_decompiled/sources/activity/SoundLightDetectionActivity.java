package activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.seculink.app.R;
import tools.ScreenUtil;
import view.ItemView;

/* JADX INFO: loaded from: classes.dex */
public class SoundLightDetectionActivity extends CommonActivity implements View.OnClickListener {
    ItemView itemAlarmTime;
    ItemView itemAllDayAlert;
    ItemView itemCustomAlert;
    ItemView itemSmallAlert;
    ItemView itemTimingAlert;
    ItemView itemWarningLight;
    ConstraintLayout layout_main;
    Switch swAllDayAlert;
    Switch swCustomAlert;
    Switch swSmallAlert;
    Switch swTimingAlert;
    Switch swWarningLight;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_sound_light_detection;
    }

    public static void start(Context context) {
        context.startActivity(new Intent(context, (Class<?>) SoundLightDetectionActivity.class));
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.layout_main = (ConstraintLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        findViewById(R.id.ib_back).setOnClickListener(this);
        this.itemSmallAlert = (ItemView) findViewById(R.id.item_smart_alert);
        this.itemSmallAlert.addRightView(new Switch(this));
        this.swSmallAlert = (Switch) this.itemSmallAlert.getRightView();
        this.swSmallAlert.setTextOff("");
        this.swSmallAlert.setTextOn("");
        this.swSmallAlert.setText("");
        this.swSmallAlert.setBackgroundResource(R.drawable.sel_switch);
        this.itemSmallAlert.getTvTitle().setCompoundDrawablePadding(ScreenUtil.dp2Px(this, 4.0f));
        this.itemSmallAlert.getTvTitle().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.iv_circle), (Drawable) null, (Drawable) null, (Drawable) null);
        this.itemAllDayAlert = (ItemView) findViewById(R.id.item_all_day_alert);
        this.itemAllDayAlert.addRightView(new Switch(this));
        this.swAllDayAlert = (Switch) this.itemAllDayAlert.getRightView();
        this.swAllDayAlert.setTextOff("");
        this.swAllDayAlert.setTextOn("");
        this.swAllDayAlert.setText("");
        this.swAllDayAlert.setBackgroundResource(R.drawable.sel_switch);
        this.itemAllDayAlert.getTvTitle().setCompoundDrawablePadding(ScreenUtil.dp2Px(this, 4.0f));
        this.itemAllDayAlert.getTvTitle().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.iv_circle), (Drawable) null, (Drawable) null, (Drawable) null);
        this.itemTimingAlert = (ItemView) findViewById(R.id.item_timing_alert);
        this.itemTimingAlert.addRightView(new Switch(this));
        this.swTimingAlert = (Switch) this.itemTimingAlert.getRightView();
        this.swTimingAlert.setTextOff("");
        this.swTimingAlert.setTextOn("");
        this.swTimingAlert.setText("");
        this.swTimingAlert.setBackgroundResource(R.drawable.sel_switch);
        this.itemTimingAlert.getTvTitle().setCompoundDrawablePadding(ScreenUtil.dp2Px(this, 4.0f));
        this.itemTimingAlert.getTvTitle().setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.iv_circle), (Drawable) null, (Drawable) null, (Drawable) null);
        this.itemAlarmTime = (ItemView) findViewById(R.id.item_alarm_time);
        this.itemWarningLight = (ItemView) findViewById(R.id.item_warning_light);
        this.itemWarningLight.addRightView(new Switch(this));
        this.swWarningLight = (Switch) this.itemWarningLight.getRightView();
        this.swWarningLight.setTextOff("");
        this.swWarningLight.setTextOn("");
        this.swWarningLight.setText("");
        this.swWarningLight.setBackgroundResource(R.drawable.sel_switch);
        this.itemCustomAlert = (ItemView) findViewById(R.id.item_custom_alert);
        this.itemCustomAlert.addRightView(new Switch(this));
        this.swCustomAlert = (Switch) this.itemCustomAlert.getRightView();
        this.swCustomAlert.setTextOff("");
        this.swCustomAlert.setTextOn("");
        this.swCustomAlert.setText("");
        this.swCustomAlert.setBackgroundResource(R.drawable.sel_switch);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.ib_back) {
            finish();
        }
    }
}
