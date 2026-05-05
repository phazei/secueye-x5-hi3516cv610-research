package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import com.seculink.app.R;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes5.dex */
public class TitleView extends RelativeLayout {
    int background_color;
    private Context context;
    int layout;
    private ImageView leftImg;
    private RelativeLayout leftRl;
    int left_img;
    private View lineView;
    private ImageView rightImg;
    private ImageView rightImg2;
    private LinearLayout rightLl;
    private TextView rightText;
    String right_icon_text;
    int right_img;
    int right_img2;
    String right_text;
    private RelativeLayout titleRl;
    private TextView titleTv;
    String title_name;
    private TextView tv_back_tip;
    private TextView tv_right_text;

    public interface OnViewClick {
        void OnLeftClick(View view2);

        void OnRightClick(View view2);
    }

    public TitleView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.context = context;
        TypedArray typedArrayObtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.TitleView);
        this.title_name = typedArrayObtainStyledAttributes.getString(8);
        this.left_img = typedArrayObtainStyledAttributes.getResourceId(3, 0);
        this.right_img = typedArrayObtainStyledAttributes.getResourceId(5, 0);
        this.right_img2 = typedArrayObtainStyledAttributes.getResourceId(6, 0);
        this.right_text = typedArrayObtainStyledAttributes.getString(7);
        this.right_icon_text = typedArrayObtainStyledAttributes.getString(4);
        this.background_color = typedArrayObtainStyledAttributes.getResourceId(0, 0);
        this.layout = typedArrayObtainStyledAttributes.getResourceId(2, 0);
        typedArrayObtainStyledAttributes.recycle();
        initView(context, this.title_name, this.left_img, this.right_img, this.right_img2, this.right_text, this.right_icon_text, this.background_color, this.layout);
    }

    private void initView(Context context, String str, int i, int i2, int i3, String str2, String str3, int i4, int i5) {
        LayoutInflater layoutInflaterFrom = LayoutInflater.from(context);
        if (i5 == 0) {
            i5 = R.layout.layout_title;
        }
        layoutInflaterFrom.inflate(i5, (ViewGroup) this, true);
        this.titleRl = (RelativeLayout) findViewById(R.id.title_rl);
        this.leftRl = (RelativeLayout) findViewById(R.id.left_rl);
        this.leftImg = (ImageView) findViewById(R.id.left_img);
        this.titleTv = (TextView) findViewById(R.id.title_tv);
        this.rightLl = (LinearLayout) findViewById(R.id.right_ll);
        this.rightImg = (ImageView) findViewById(R.id.right_img);
        this.rightImg2 = (ImageView) findViewById(R.id.right_img2);
        this.rightText = (TextView) findViewById(R.id.right_text);
        this.lineView = findViewById(R.id.line_view);
        this.tv_back_tip = (TextView) findViewById(R.id.tv_back_tip);
        this.tv_right_text = (TextView) findViewById(R.id.tv_right_text);
        setBackground(i4);
        setTitleText(str);
        setLeftImgId(i);
        setRightImgId(i2);
        setRightImg2Id(i3);
        setRightText(str2);
        setRightIconText(str3);
    }

    public void setRightLlGone(boolean z) {
        this.rightLl.setVisibility(z ? 8 : 0);
    }

    public void setOnViewClick(final OnViewClick onViewClick) {
        this.leftRl.setOnClickListener(new View.OnClickListener() { // from class: view.TitleView.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                onViewClick.OnLeftClick(view2);
            }
        });
        this.rightLl.setOnClickListener(new OnMultiClickListener() { // from class: view.TitleView.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                onViewClick.OnRightClick(view2);
            }
        });
        this.rightText.setOnClickListener(new OnMultiClickListener() { // from class: view.TitleView.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                onViewClick.OnRightClick(view2);
            }
        });
        this.rightImg.setOnClickListener(new OnMultiClickListener() { // from class: view.TitleView.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                onViewClick.OnRightClick(view2);
            }
        });
        this.rightImg2.setOnClickListener(new OnMultiClickListener() { // from class: view.TitleView.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                onViewClick.OnRightClick(view2);
            }
        });
    }

    public void setBackground(@ColorRes int i) {
        if (i != 0) {
            this.titleRl.setBackgroundColor(ContextCompat.getColor(this.context, i));
        }
    }

    public void setTBackgroundDrawable(Drawable drawable) {
        this.titleRl.setBackground(drawable);
    }

    public void setTitleRlPadding(int i) {
        this.titleRl.setPadding(0, i, 0, 0);
    }

    public void setTitleText(String str) {
        this.titleTv.setText(str);
    }

    public void setTitleTextVisible(int i) {
        this.titleTv.setVisibility(i);
    }

    public void setRightIconText(String str) {
        this.tv_right_text.setText(str);
    }

    public String getTitleText() {
        return this.titleTv.getText().toString();
    }

    public void setTitleStyle() {
        this.titleTv.setTypeface(Typeface.defaultFromStyle(1));
    }

    public void setTitleTextColor(int i) {
        this.titleTv.setTextColor(ContextCompat.getColor(this.context, i));
    }

    public void setTitleBackTextColor(int i) {
        this.tv_back_tip.setTextColor(ContextCompat.getColor(this.context, i));
    }

    public void setTitleBackText(String str) {
        this.tv_back_tip.setText(str);
    }

    public void setRightText(String str) {
        if (TextUtils.isEmpty(str)) {
            this.rightText.setVisibility(8);
            return;
        }
        this.rightImg.setVisibility(8);
        this.rightText.setVisibility(0);
        this.rightText.setText(str);
    }

    public void setLeftImgId(@DrawableRes int i) {
        if (i == 0) {
            this.leftImg.setVisibility(8);
        } else {
            this.leftImg.setImageResource(i);
            this.leftImg.setVisibility(0);
        }
    }

    public void setLineViewId(@ColorRes int i) {
        if (i == 0) {
            this.lineView.setVisibility(8);
        } else {
            this.lineView.setBackgroundColor(getResources().getColor(i));
            this.lineView.setVisibility(0);
        }
    }

    public void setRightImgId(@DrawableRes int i) {
        if (i == 0) {
            this.rightImg.setVisibility(8);
        } else {
            this.rightImg.setImageResource(i);
            this.rightImg.setVisibility(0);
        }
    }

    public void setRightImg2Id(@DrawableRes int i) {
        if (i == 0) {
            this.rightImg2.setVisibility(8);
        } else {
            this.rightImg2.setImageResource(i);
            this.rightImg2.setVisibility(0);
        }
    }

    public void setRightSettingDisplay(boolean z) {
        if (z) {
            this.rightImg2.setVisibility(0);
            this.tv_right_text.setVisibility(0);
        } else {
            this.rightImg2.setVisibility(8);
            this.tv_right_text.setVisibility(8);
        }
    }
}
