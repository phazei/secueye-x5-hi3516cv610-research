package view;

import android.content.Context;
import android.widget.RelativeLayout;
import bean.PadLocationType;
import bean.PadStyle;
import bean.TouchViewModel;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class DefaultController implements IJoystickController {
    private RelativeLayout containerView;
    private Context ctx;
    private TouchView leftControlTouchView;
    private PadStyle padStyle;
    private TouchView rightControlTouchView;

    public DefaultController(Context context, RelativeLayout relativeLayout) {
        this(context, relativeLayout, PadStyle.FIXED);
    }

    public DefaultController(Context context, RelativeLayout relativeLayout, PadStyle padStyle) {
        this.padStyle = PadStyle.FIXED;
        this.ctx = context;
        this.containerView = relativeLayout;
        this.padStyle = padStyle;
    }

    @Override // view.IJoystickController
    public void createViews() {
        createLeftControlTouchView();
        this.containerView.addView(this.leftControlTouchView);
        createRightControlTouchView();
        this.containerView.addView(this.rightControlTouchView);
    }

    @Override // view.IJoystickController
    public void showViews(boolean z) {
        this.leftControlTouchView.clearAnimation();
        this.leftControlTouchView.setVisibility(0);
        this.rightControlTouchView.clearAnimation();
        this.rightControlTouchView.setVisibility(0);
    }

    private void createLeftControlTouchView() {
        TouchViewModel touchViewModel = new TouchViewModel(R.drawable.ui_pic_joystick_left_pad, R.drawable.ui_pic_joystick_control_ball);
        touchViewModel.setWholeViewSize(this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_wid), this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_height));
        touchViewModel.setPadSize(this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_pad_size), this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_pad_size));
        int dimensionPixelSize = this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_round_bg_radius);
        touchViewModel.setContentSize(dimensionPixelSize, (int) (((double) dimensionPixelSize) / 3.5d));
        touchViewModel.setStyle(this.padStyle, PadLocationType.LEFT_BOT);
        touchViewModel.setRoundBgPadding(this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_circle_bg_padding));
        this.leftControlTouchView = new TouchView(this.ctx);
        this.leftControlTouchView.init(touchViewModel);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_wid), this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_height));
        layoutParams.addRule(12);
        layoutParams.addRule(9);
        this.leftControlTouchView.setLayoutParams(layoutParams);
    }

    private void createRightControlTouchView() {
        TouchViewModel touchViewModel = new TouchViewModel(R.drawable.ui_pic_joystick_right_pad, R.drawable.ui_pic_joystick_control_ball);
        touchViewModel.setWholeViewSize(this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_wid), this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_height));
        touchViewModel.setPadSize(this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_pad_size), this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_pad_size));
        int dimensionPixelSize = this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_round_bg_radius);
        touchViewModel.setContentSize(dimensionPixelSize, (int) (((double) dimensionPixelSize) / 3.5d));
        touchViewModel.setDirectionPicResId(R.drawable.ui_pic_joystick_arrow);
        touchViewModel.setStyle(this.padStyle, PadLocationType.RIGHT_BOT);
        touchViewModel.setRoundBgPadding(this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_circle_bg_padding));
        this.rightControlTouchView = new TouchView(this.ctx);
        this.rightControlTouchView.init(touchViewModel);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_wid), this.ctx.getResources().getDimensionPixelSize(R.dimen.ui_joystick_whole_field_height));
        layoutParams.addRule(12);
        layoutParams.addRule(11);
        this.rightControlTouchView.setLayoutParams(layoutParams);
    }

    public void setLeftTouchViewListener(JoystickTouchViewListener joystickTouchViewListener) {
        TouchView touchView = this.leftControlTouchView;
        if (touchView != null) {
            touchView.setListener(joystickTouchViewListener);
        }
    }

    public void setRightTouchViewListener(JoystickTouchViewListener joystickTouchViewListener) {
        TouchView touchView = this.rightControlTouchView;
        if (touchView != null) {
            touchView.setListener(joystickTouchViewListener);
        }
    }

    public void setPadStyle(PadStyle padStyle) {
        this.padStyle = padStyle;
        this.leftControlTouchView.setPadStyle(padStyle);
        this.rightControlTouchView.setPadStyle(padStyle);
    }

    public PadStyle getPadStyle() {
        return this.padStyle;
    }
}
