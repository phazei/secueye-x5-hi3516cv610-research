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
import androidx.annotation.Nullable;
import com.seculink.app.R;
import tools.DensityUtil;

/* JADX INFO: loaded from: classes5.dex */
public class TabView extends FrameLayout {
    private int picColor;
    private int picId;
    private int picPadding;
    private ImageView tabPic;
    private TextView tabText;
    private String text;
    private int textColor;

    /* JADX INFO: renamed from: view, reason: collision with root package name */
    private View f8111view;

    private void init(Context context) {
    }

    public TabView(Context context) {
        super(context);
        init(context);
    }

    public TabView(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TabView);
        this.picId = typedArrayObtainStyledAttributes.getResourceId(0, 0);
        this.text = typedArrayObtainStyledAttributes.getString(3);
        this.textColor = typedArrayObtainStyledAttributes.getResourceId(4, 0);
        this.picColor = typedArrayObtainStyledAttributes.getResourceId(1, 0);
        this.picPadding = typedArrayObtainStyledAttributes.getInteger(2, 0);
        typedArrayObtainStyledAttributes.recycle();
        init(context, this.picId, this.text, this.textColor, this.picColor, this.picPadding);
    }

    private void init(Context context, int i, String str, int i2, int i3, int i4) {
        this.f8111view = LayoutInflater.from(context).inflate(R.layout.layout_tab_view, (ViewGroup) this, false);
        addView(this.f8111view);
        this.tabText = (TextView) findViewById(R.id.tv_tab_text);
        this.tabPic = (ImageView) findViewById(R.id.iv_tab_pic);
        setTabText(str);
        setTabTextColor(i2);
        setTabPicColor(i3);
        setTabPic(i);
        setTabPicPadding(i4);
    }

    public void setGravity(int i) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.f8111view.getLayoutParams();
        layoutParams.gravity = i;
        this.f8111view.setLayoutParams(layoutParams);
    }

    private void setTabPicPadding(int i) {
        this.tabText.setPadding(DensityUtil.dip2px(getContext(), i), 0, 0, 0);
    }

    private void setTabPicColor(@ColorRes int i) {
        this.tabPic.setColorFilter(i);
    }

    public TabView(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context);
    }

    public void setTabPic(int i) {
        this.tabPic.setImageResource(i);
    }

    public void setTabTextColor(@ColorRes int i) {
        this.tabText.setTextColor(getResources().getColor(i));
    }

    public void setTabText(String str) {
        this.tabText.setText(str);
    }

    public int getPicId() {
        return this.picId;
    }

    public void setPicId(int i) {
        this.picId = i;
    }

    public int getTextColor() {
        return this.textColor;
    }

    public void setTextColor(int i) {
        this.textColor = i;
    }

    public TextView getTabText() {
        return this.tabText;
    }

    public ImageView getTabPic() {
        return this.tabPic;
    }

    public void setTabPic(ImageView imageView) {
        this.tabPic = imageView;
    }
}
