package com.aliyun.iot.link.ui.component.simpleLoadview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.aliyun.iot.link.ui.component.R;

/* JADX INFO: loaded from: classes2.dex */
public class LinkSimpleLoadView extends FrameLayout {
    private static final String TAG = "WHloadView";
    private View mBottomBlanSpaceView;
    private String mDefaultTipText;
    private View mDeleTopBar;
    private ImageView mIconScreen;
    private LinearLayout mInfoArea;
    private Drawable mInfoAreaLoadingBg;
    private float mInfoAreaLoadingHeight;
    private float mInfoAreaLoadingSize;
    private Drawable mInfoAreaTipBg;
    private float mInfoAreaTipSize;
    private Drawable mLoadTipIcon;
    private float mLoadTipIconSize;
    private float mLoadViewLoacation;
    private LinearLayout mLoadViewRoot;
    private Drawable mLoadingIcon;
    private int mLoadingIconRoateDuration;
    private float mLoadingIconSize;
    private ObjectAnimator mRotation;
    private TextView mTip;
    private float mTip2IconSpace;
    private int mTipColor;
    private float mTipTextSize;
    private float mTipViewLoacation;
    private View mUpperBlanSpaceView;

    public LinkSimpleLoadView(Context context) {
        super(context);
        init((AttributeSet) null);
    }

    public LinkSimpleLoadView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet);
    }

    public LinkSimpleLoadView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet);
    }

    public void init(AttributeSet attributeSet) {
        addView(LayoutInflater.from(getContext()).inflate(R.layout.link_view_simpleloadview, (ViewGroup) null, false));
        this.mLoadViewRoot = (LinearLayout) findViewById(R.id.simpleloadview_root_linearlayout);
        this.mUpperBlanSpaceView = findViewById(R.id.simpleloadview_upper_blanspace_view);
        this.mBottomBlanSpaceView = findViewById(R.id.simpleloadview_bottom_blanspace_view);
        this.mDeleTopBar = findViewById(R.id.simpleloadview_topbar_dele_view);
        this.mInfoArea = (LinearLayout) findViewById(R.id.simpleloadview_infoarea_linearlayout);
        this.mIconScreen = (ImageView) findViewById(R.id.simpleloadview_icon_imageview);
        this.mTip = (TextView) findViewById(R.id.simpleloadview_loadtip_textview);
        if (attributeSet != null) {
            TypedArray typedArrayObtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.LinkSimpleLoadView);
            applyStyle(typedArrayObtainStyledAttributes);
            typedArrayObtainStyledAttributes.recycle();
        }
    }

    public void setLoadViewLoacation(float f) {
        this.mLoadViewLoacation = f;
    }

    public void setTipViewLoacation(float f) {
        this.mTipViewLoacation = f;
    }

    public void applyStyle(TypedArray typedArray) {
        this.mDefaultTipText = typedArray.getString(R.styleable.LinkSimpleLoadView_slv_tip_text);
        this.mTipColor = typedArray.getColor(R.styleable.LinkSimpleLoadView_slv_tip_color, -3355444);
        this.mTipTextSize = typedArray.getDimension(R.styleable.LinkSimpleLoadView_slv_tip_textsize, convertdp2px(14.0f));
        this.mLoadTipIcon = typedArray.getDrawable(R.styleable.LinkSimpleLoadView_slv_loadtip_icon);
        this.mLoadTipIconSize = typedArray.getDimension(R.styleable.LinkSimpleLoadView_slv_loadtip_icon_size, convertdp2px(45.0f));
        this.mLoadingIcon = typedArray.getDrawable(R.styleable.LinkSimpleLoadView_slv_loading_icon);
        this.mLoadingIconSize = typedArray.getDimension(R.styleable.LinkSimpleLoadView_slv_loading_icon_size, convertdp2px(94.0f));
        this.mLoadingIconRoateDuration = typedArray.getInteger(R.styleable.LinkSimpleLoadView_slv_loadtip_icon_roate_duration, 500);
        this.mInfoAreaTipSize = typedArray.getLayoutDimension(R.styleable.LinkSimpleLoadView_slv_infoarea_tip_size, convertdp2px(96.0f));
        this.mInfoAreaLoadingSize = typedArray.getLayoutDimension(R.styleable.LinkSimpleLoadView_slv_infoarea_loading_size, -2);
        this.mInfoAreaLoadingHeight = typedArray.getLayoutDimension(R.styleable.LinkSimpleLoadView_slv_infoarea_loading_height, -2);
        this.mInfoAreaLoadingBg = typedArray.getDrawable(R.styleable.LinkSimpleLoadView_slv_infoarea_loading_background);
        this.mInfoAreaTipBg = typedArray.getDrawable(R.styleable.LinkSimpleLoadView_slv_infoarea_tip_background);
        this.mTip2IconSpace = typedArray.getDimension(R.styleable.LinkSimpleLoadView_slv_tip2icon_space, convertdp2px(10.0f));
        this.mLoadViewLoacation = typedArray.getFloat(R.styleable.LinkSimpleLoadView_slv_loadview_location, 1.0f);
        this.mTipViewLoacation = typedArray.getFloat(R.styleable.LinkSimpleLoadView_slv_tipview_location, 1.0f);
        if (!TextUtils.isEmpty(this.mDefaultTipText)) {
            this.mTip.setText(this.mDefaultTipText);
        }
        this.mTip.setTextColor(this.mTipColor);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mTip.getLayoutParams();
        marginLayoutParams.setMargins(0, (int) this.mTip2IconSpace, 0, 0);
        this.mTip.setLayoutParams(marginLayoutParams);
    }

    public void setLoadViewRootBgColor(int i) {
        LinearLayout linearLayout = this.mLoadViewRoot;
        if (linearLayout == null) {
            return;
        }
        linearLayout.setBackgroundColor(i);
    }

    public void setLoadViewRootBgDrawbleRes(int i) {
        LinearLayout linearLayout = this.mLoadViewRoot;
        if (linearLayout == null || i <= 0) {
            return;
        }
        linearLayout.setBackgroundColor(-1);
    }

    public void setLoadViewlocationOnV(float f, float f2) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1, 0);
        layoutParams.weight = f;
        this.mUpperBlanSpaceView.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(-1, 0);
        layoutParams2.weight = f2;
        this.mBottomBlanSpaceView.setLayoutParams(layoutParams2);
    }

    public void setLoadingDrawable(Drawable drawable) {
        this.mLoadingIcon = drawable;
    }

    public void setLoadErrorDrawable(Drawable drawable) {
        this.mLoadTipIcon = drawable;
    }

    public void showTip(String str) {
        showTip(str, this.mLoadTipIcon);
    }

    public void showTip(String str, Drawable drawable) {
        setVisibility(0);
        setClickable(true);
        this.mLoadViewRoot.setVisibility(0);
        ObjectAnimator objectAnimator = this.mRotation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        if (this.mTipViewLoacation < 0.0f) {
            this.mTipViewLoacation = 1.0f;
        }
        float f = this.mTipViewLoacation;
        if (f < 1.0f) {
            setLoadViewlocationOnV(f * 10.0f, 10.0f);
        } else {
            setLoadViewlocationOnV(f, 1.0f);
        }
        float f2 = this.mInfoAreaTipSize;
        this.mInfoArea.setLayoutParams(new LinearLayout.LayoutParams((int) f2, (int) f2));
        this.mInfoArea.setBackgroundDrawable(this.mInfoAreaTipBg);
        this.mIconScreen.clearAnimation();
        this.mIconScreen.setRotation(0.0f);
        this.mIconScreen.setVisibility(0);
        float f3 = this.mLoadTipIconSize;
        this.mIconScreen.setLayoutParams(new LinearLayout.LayoutParams((int) f3, (int) f3));
        this.mIconScreen.setBackgroundDrawable(drawable);
        if (TextUtils.isEmpty(str)) {
            this.mTip.setText("");
            this.mTip.setVisibility(8);
        } else {
            this.mTip.setText(str);
            this.mTip.setVisibility(0);
        }
    }

    public void showError(String str) {
        showTip(str);
    }

    public void showError(String str, Drawable drawable) {
        showTip(str, drawable);
    }

    public void showLoading(String str) {
        showLoading(str, this.mLoadingIcon);
    }

    public void showLoading(String str, Drawable drawable) {
        setClickable(true);
        setVisibility(0);
        this.mLoadViewRoot.setVisibility(0);
        this.mIconScreen.setVisibility(0);
        if (this.mLoadViewLoacation < 0.0f) {
            this.mLoadViewLoacation = 1.0f;
        }
        float f = this.mLoadViewLoacation;
        if (f < 1.0f) {
            setLoadViewlocationOnV(f * 10.0f, 10.0f);
        } else {
            setLoadViewlocationOnV(f, 1.0f);
        }
        this.mInfoArea.setLayoutParams(new LinearLayout.LayoutParams((int) this.mInfoAreaLoadingSize, (int) this.mInfoAreaLoadingHeight));
        this.mInfoArea.setBackgroundDrawable(this.mInfoAreaLoadingBg);
        this.mIconScreen.setClickable(false);
        float f2 = this.mLoadingIconSize;
        this.mIconScreen.setLayoutParams(new LinearLayout.LayoutParams((int) f2, (int) f2));
        this.mIconScreen.setBackgroundDrawable(drawable);
        if (TextUtils.isEmpty(str)) {
            this.mTip.setText("");
            this.mTip.setVisibility(8);
        } else {
            this.mTip.setText(str);
            this.mTip.setVisibility(0);
        }
        startAnimator();
    }

    public void setTopbarClickableArea(View view2, View.OnClickListener onClickListener) {
        view2.measure(0, 0);
        int measuredHeight = view2.getMeasuredHeight();
        int measuredWidth = view2.getMeasuredWidth();
        try {
            ViewGroup.LayoutParams layoutParams = this.mDeleTopBar.getLayoutParams();
            if (measuredHeight <= 0) {
                measuredHeight = 0;
            }
            layoutParams.height = measuredHeight;
            this.mDeleTopBar.getLayoutParams().width = measuredWidth > 0 ? measuredWidth / 2 : getContext().getResources().getDisplayMetrics().widthPixels;
            this.mDeleTopBar.requestLayout();
            this.mDeleTopBar.setOnClickListener(onClickListener);
        } catch (Exception e) {
            this.mDeleTopBar.getLayoutParams().height = 0;
            this.mDeleTopBar.requestLayout();
            this.mDeleTopBar.setOnClickListener(onClickListener);
            Log.e(TAG, "fail to get the height of topbar," + e);
            e.printStackTrace();
        }
    }

    public void startAnimator() {
        ImageView imageView = this.mIconScreen;
        if (imageView == null) {
            return;
        }
        try {
            if (imageView.getBackground() == null) {
                return;
            }
            if (this.mIconScreen.getBackground() instanceof AnimationDrawable) {
                ((AnimationDrawable) this.mIconScreen.getBackground()).start();
                return;
            }
            if (this.mRotation == null) {
                this.mRotation = ObjectAnimator.ofFloat(this.mIconScreen, (Property<ImageView, Float>) View.ROTATION, 0.0f, 360.0f);
                this.mRotation.setDuration(this.mLoadingIconRoateDuration);
                this.mRotation.setInterpolator(new LinearInterpolator());
                this.mRotation.setRepeatCount(-1);
            } else {
                this.mRotation.cancel();
            }
            this.mRotation.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hide() {
        ObjectAnimator objectAnimator = this.mRotation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        setVisibility(8);
    }

    public int convertdp2px(float f) {
        return (int) ((f * getResources().getDisplayMetrics().density) + 0.5f);
    }

    public float convertpx2dp(int i) {
        return i / getResources().getDisplayMetrics().density;
    }
}
