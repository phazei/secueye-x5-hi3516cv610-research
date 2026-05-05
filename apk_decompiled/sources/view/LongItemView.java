package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class LongItemView extends FrameLayout {
    private FrameLayout flRgihtView;
    private ImageView ivEnter;
    private View line;
    private View mRightView;
    private TextView tvDesc;
    private TextView tvRight;
    private TextView tvTitle;

    public LongItemView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public LongItemView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.ItemView);
        String string = typedArrayObtainStyledAttributes.getString(9);
        String string2 = typedArrayObtainStyledAttributes.getString(0);
        String string3 = typedArrayObtainStyledAttributes.getString(6);
        int resourceId = typedArrayObtainStyledAttributes.getResourceId(1, 0);
        int resourceId2 = typedArrayObtainStyledAttributes.getResourceId(10, 0);
        int resourceId3 = typedArrayObtainStyledAttributes.getResourceId(7, 0);
        boolean z = typedArrayObtainStyledAttributes.getBoolean(3, false);
        boolean z2 = typedArrayObtainStyledAttributes.getBoolean(2, false);
        boolean z3 = typedArrayObtainStyledAttributes.getBoolean(5, true);
        boolean z4 = typedArrayObtainStyledAttributes.getBoolean(8, false);
        typedArrayObtainStyledAttributes.recycle();
        init(context, string, string2, string3, resourceId, resourceId2, resourceId3, z, z2, z3, z4);
    }

    public LongItemView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_item_view_long, (ViewGroup) this, true);
        this.tvTitle = (TextView) findViewById(R.id.tv_text_title);
        this.tvDesc = (TextView) findViewById(R.id.tv_text_desc);
        this.ivEnter = (ImageView) findViewById(R.id.iv_enter);
        this.flRgihtView = (FrameLayout) findViewById(R.id.fl_content_right);
        this.line = findViewById(R.id.line);
    }

    private void init(Context context, String str, String str2, String str3, int i, int i2, int i3, boolean z, boolean z2, boolean z3, boolean z4) {
        LayoutInflater.from(context).inflate(R.layout.layout_item_view_long, (ViewGroup) this, true);
        this.tvTitle = (TextView) findViewById(R.id.tv_text_title);
        this.tvDesc = (TextView) findViewById(R.id.tv_text_desc);
        this.tvRight = (TextView) findViewById(R.id.tv_text_right);
        this.ivEnter = (ImageView) findViewById(R.id.iv_enter);
        this.flRgihtView = (FrameLayout) findViewById(R.id.fl_content_right);
        this.line = findViewById(R.id.line);
        setTitleText(str);
        setDescText(str2);
        setRightText(str3, z4);
        setDescTextColor(i);
        setTitleTextColor(i2);
        setRightTextColor(i3);
        setEnterHide(z);
        setDescHide(z2);
        if (z3) {
            return;
        }
        this.line.setVisibility(8);
    }

    public void setLineShow(boolean z) {
        if (!z) {
            this.line.setVisibility(8);
        } else {
            this.line.setVisibility(0);
        }
    }

    public void setRightTextColor(int i) {
        if (i != 0) {
            this.tvRight.setTextColor(getResources().getColor(i));
        }
    }

    public void setRightTextSize(int i) {
        if (i != 0) {
            this.tvRight.setTextSize(i);
        }
    }

    public void setRightText(String str, boolean z) {
        this.tvRight.setText(str);
        this.tvRight.setSingleLine(z);
    }

    public void setRightText(String str) {
        this.tvRight.setText(str);
    }

    private void setDescHide(boolean z) {
        this.tvDesc.setVisibility(z ? 8 : 0);
    }

    public TextView getTvTitle() {
        return this.tvTitle;
    }

    public void setTvTitle(TextView textView) {
        this.tvTitle = textView;
    }

    public TextView getTvDesc() {
        return this.tvDesc;
    }

    public void setTvDesc(TextView textView) {
        this.tvDesc = textView;
    }

    public ImageView getIvEnter() {
        return this.ivEnter;
    }

    public void setIvEnter(ImageView imageView) {
        this.ivEnter = imageView;
    }

    public void setEnterHide(boolean z) {
        this.ivEnter.setVisibility(z ? 8 : 0);
    }

    public void setDescTextColor(@ColorRes int i) {
        if (i != 0) {
            this.tvDesc.setTextColor(getResources().getColor(i));
        }
    }

    public void setTitleTextColor(@ColorRes int i) {
        if (i != 0) {
            this.tvTitle.setTextColor(getResources().getColor(i));
        }
    }

    public void setTitleText(String str) {
        this.tvTitle.setText(str);
    }

    public void setDescText(String str) {
        this.tvDesc.setText(str);
    }

    public String getTitleText() {
        return this.tvTitle.getText().toString();
    }

    public String getDescText() {
        return this.tvDesc.getText().toString();
    }

    public String getRightText() {
        return this.tvRight.getText().toString();
    }

    public boolean hasRightView() {
        View view2 = this.mRightView;
        return (view2 == null || this.flRgihtView.indexOfChild(view2) == -1) ? false : true;
    }

    public void addRightView(Context context, @LayoutRes int i) {
        View viewInflate = LayoutInflater.from(context).inflate(i, (ViewGroup) this.flRgihtView, true);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewInflate.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        layoutParams.gravity = 16;
        viewInflate.setLayoutParams(layoutParams);
        this.mRightView = viewInflate;
    }

    public void addRightView(View view2) {
        addRightView(view2, (FrameLayout.LayoutParams) null);
    }

    public void addRightView(View view2, FrameLayout.LayoutParams layoutParams) {
        if (layoutParams == null) {
            layoutParams = new FrameLayout.LayoutParams(-2, -2);
        }
        layoutParams.setMargins(0, 0, 0, 0);
        layoutParams.gravity = 16;
        this.flRgihtView.addView(view2, layoutParams);
        this.mRightView = view2;
    }

    public View getRightView() {
        return this.mRightView;
    }
}
