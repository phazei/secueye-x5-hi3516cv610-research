package view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.seculink.app.R;
import tools.ScreenTools;

/* JADX INFO: loaded from: classes5.dex */
public class ALoadView2 extends FrameLayout {
    private Drawable errorBackground;
    private Drawable errorIcon;
    private int errorIconSize;
    private String errorTipText;
    private int errorTipTextColor;
    private int errorTipTextSize;
    private ImageView iconIV;
    private View.OnClickListener listener;
    private Drawable loadBackground;
    private LinearLayout loadViewLL;
    private Drawable loadingIcon;
    private int loadingIconSize;
    private String loadingTipText;
    private int loadingTipTextColor;
    private int loadingTipTextSize;
    private ObjectAnimator mRotation;
    private Drawable retryBackground;
    private TextView retryTV;
    private String retryText;
    private int retryTextColor;
    private int retryTextSize;
    private TextView tipTV;

    public ALoadView2(Context context) {
        super(context);
        this.loadBackground = null;
        this.loadingIcon = null;
        this.loadingIconSize = 0;
        this.loadingTipText = null;
        this.loadingTipTextSize = 0;
        this.loadingTipTextColor = -6634561;
        this.errorBackground = null;
        this.errorIcon = null;
        this.errorIconSize = 0;
        this.errorTipText = null;
        this.errorTipTextSize = 0;
        this.errorTipTextColor = -6634561;
        this.retryBackground = null;
        this.retryText = null;
        this.retryTextSize = 0;
        this.retryTextColor = -16726094;
        initView(context, null);
    }

    public ALoadView2(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ALoadView2(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.loadBackground = null;
        this.loadingIcon = null;
        this.loadingIconSize = 0;
        this.loadingTipText = null;
        this.loadingTipTextSize = 0;
        this.loadingTipTextColor = -6634561;
        this.errorBackground = null;
        this.errorIcon = null;
        this.errorIconSize = 0;
        this.errorTipText = null;
        this.errorTipTextSize = 0;
        this.errorTipTextColor = -6634561;
        this.retryBackground = null;
        this.retryText = null;
        this.retryTextSize = 0;
        this.retryTextColor = -16726094;
        initView(context, attributeSet);
    }

    public void initView(Context context, AttributeSet attributeSet) {
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ALoadView2);
        this.loadBackground = typedArrayObtainStyledAttributes.getDrawable(6);
        if (this.loadBackground == null) {
            this.loadBackground = new ColorDrawable(0);
        }
        this.loadingIcon = typedArrayObtainStyledAttributes.getDrawable(7);
        if (this.loadingIcon == null) {
            this.loadingIcon = context.getResources().getDrawable(R.drawable.aloadview2_loading);
        }
        this.loadingIconSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(8, (int) ScreenTools.convertDp2Px(context, 29.0f));
        this.loadingTipText = typedArrayObtainStyledAttributes.getString(9);
        this.loadingTipTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(11, (int) ScreenTools.convertDp2Px(context, 16.0f));
        this.loadingTipTextColor = typedArrayObtainStyledAttributes.getColor(10, -6634561);
        this.errorBackground = typedArrayObtainStyledAttributes.getDrawable(0);
        if (this.errorBackground == null) {
            this.errorBackground = new ColorDrawable(0);
        }
        this.errorIcon = typedArrayObtainStyledAttributes.getDrawable(1);
        if (this.errorIcon == null) {
            this.errorIcon = context.getResources().getDrawable(R.drawable.aloadview2_loadingerror_light);
        }
        this.errorIconSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(2, (int) ScreenTools.convertDp2Px(context, 44.0f));
        this.errorTipText = typedArrayObtainStyledAttributes.getString(3);
        if (TextUtils.isEmpty(this.errorTipText)) {
            this.errorTipText = context.getString(R.string.aloadview2_loadingerror);
        }
        this.errorTipTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(5, (int) ScreenTools.convertDp2Px(context, 16.0f));
        this.errorTipTextColor = typedArrayObtainStyledAttributes.getColor(4, -6634561);
        this.retryBackground = typedArrayObtainStyledAttributes.getDrawable(12);
        if (this.retryBackground == null) {
            this.retryBackground = context.getResources().getDrawable(R.drawable.aloadview2_retry_background_green);
        }
        this.retryText = typedArrayObtainStyledAttributes.getString(13);
        this.retryTextSize = typedArrayObtainStyledAttributes.getDimensionPixelSize(15, (int) ScreenTools.convertDp2Px(context, 14.0f));
        this.retryTextColor = typedArrayObtainStyledAttributes.getColor(14, -16726094);
        typedArrayObtainStyledAttributes.recycle();
        setClickable(true);
        LayoutInflater.from(getContext()).inflate(R.layout.aloadview2, (ViewGroup) this, true);
        this.loadViewLL = (LinearLayout) findViewById(R.id.aloadview2_linearlayout_root);
        this.iconIV = (ImageView) findViewById(R.id.aloadview2_imageview_icon);
        this.tipTV = (TextView) findViewById(R.id.aloadview2_textview_tip);
        this.retryTV = (TextView) findViewById(R.id.aloadview2_textview_retry);
    }

    public void showLoading() {
        if (getVisibility() == 0) {
            hide();
        }
        setVisibility(0);
        this.loadViewLL.setBackground(this.loadBackground);
        this.iconIV.setBackground(this.loadingIcon);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.iconIV.getLayoutParams();
        int i = this.loadingIconSize;
        marginLayoutParams.width = i;
        marginLayoutParams.height = i;
        this.iconIV.setLayoutParams(marginLayoutParams);
        if (TextUtils.isEmpty(this.loadingTipText)) {
            this.tipTV.setVisibility(8);
        } else {
            this.tipTV.setVisibility(0);
            this.tipTV.setTextColor(this.loadingTipTextColor);
            this.tipTV.setTextSize(0, this.loadingTipTextSize);
            this.tipTV.setText(this.loadingTipText);
        }
        this.retryTV.setVisibility(8);
        startAnimator();
    }

    public void showError(View.OnClickListener onClickListener) {
        if (getVisibility() == 0) {
            hide();
        }
        setVisibility(0);
        this.loadViewLL.setBackground(this.errorBackground);
        this.iconIV.setRotation(0.0f);
        this.iconIV.setBackground(this.errorIcon);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.iconIV.getLayoutParams();
        int i = this.errorIconSize;
        marginLayoutParams.width = i;
        marginLayoutParams.height = i;
        this.iconIV.setLayoutParams(marginLayoutParams);
        if (TextUtils.isEmpty(this.errorTipText)) {
            this.tipTV.setVisibility(8);
        } else {
            this.tipTV.setVisibility(0);
            this.tipTV.setText(this.errorTipText);
            this.tipTV.setTextSize(0, this.errorTipTextSize);
            this.tipTV.setTextColor(this.errorTipTextColor);
        }
        if (onClickListener == null) {
            this.retryTV.setVisibility(8);
            return;
        }
        this.retryTV.setVisibility(0);
        this.retryTV.setBackground(this.retryBackground);
        this.retryTV.setTextColor(this.retryTextColor);
        this.retryTV.setTextSize(0, this.retryTextSize);
        this.retryTV.setText(TextUtils.isEmpty(this.retryText) ? getResources().getString(R.string.aloadview2_clickreload) : this.retryText);
        this.retryTV.setOnClickListener(onClickListener);
    }

    public void startAnimator() {
        try {
            if (this.iconIV.getBackground() != null && (this.iconIV.getBackground() instanceof AnimationDrawable)) {
                ((AnimationDrawable) this.iconIV.getBackground()).start();
            } else {
                this.mRotation = ObjectAnimator.ofFloat(this.iconIV, "rotation", 0.0f, 360.0f);
                this.mRotation.setInterpolator(new LinearInterpolator());
                this.mRotation.setDuration(1000L);
                this.mRotation.setRepeatCount(-1);
                this.mRotation.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        ObjectAnimator objectAnimator = this.mRotation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.mRotation = null;
        }
        this.iconIV.clearAnimation();
        this.listener = null;
        setVisibility(8);
    }
}
