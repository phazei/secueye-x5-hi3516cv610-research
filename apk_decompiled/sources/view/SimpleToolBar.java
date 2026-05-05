package view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class SimpleToolBar extends FrameLayout {
    private final int BACK;
    private final int MENU;
    private final String TAG;
    private final int TITLE;
    private ImageButton backIb;
    private View line;
    private LinearLayout linearLayout;
    private ImageButton menuIb;
    public OnToolBarClickListener onToolBarClickListener;
    private TextView titleTv;

    public interface OnToolBarClickListener {
        void onBackClick(View view2);

        void onMenuClick(View view2);

        void onTitleClick(View view2);
    }

    public SimpleToolBar(@NonNull Context context) {
        super(context);
        this.BACK = 0;
        this.TITLE = 1;
        this.MENU = 2;
        this.TAG = "SimpleToolBar";
        initView();
    }

    public SimpleToolBar(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        this.BACK = 0;
        this.TITLE = 1;
        this.MENU = 2;
        this.TAG = "SimpleToolBar";
        initView();
    }

    public SimpleToolBar(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.BACK = 0;
        this.TITLE = 1;
        this.MENU = 2;
        this.TAG = "SimpleToolBar";
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.simple_toolbar, this);
        this.backIb = (ImageButton) findViewById(R.id.toolbar_back);
        this.menuIb = (ImageButton) findViewById(R.id.toolbar_menu);
        this.titleTv = (TextView) findViewById(R.id.toolbar_title);
        this.linearLayout = (LinearLayout) findViewById(R.id.toolbar_ll);
        this.line = findViewById(R.id.toolbar_line);
    }

    public SimpleToolBar setTitle(String str) {
        TextView textView;
        if (!TextUtils.isEmpty(str) && (textView = this.titleTv) != null) {
            textView.setText(str);
            onClick(this.titleTv, 1);
        }
        return this;
    }

    public SimpleToolBar setBack(boolean z) {
        ImageButton imageButton = this.backIb;
        if (imageButton == null) {
            return this;
        }
        if (z) {
            imageButton.setVisibility(0);
            this.backIb.setImageResource(R.drawable.back_arrow_white);
            onClick(this.backIb, 0);
        } else {
            imageButton.setVisibility(8);
        }
        return this;
    }

    public SimpleToolBar setBack(@DrawableRes int i) {
        ImageButton imageButton = this.backIb;
        if (imageButton == null) {
            return this;
        }
        imageButton.setVisibility(0);
        this.backIb.setImageResource(i);
        onClick(this.backIb, 0);
        return this;
    }

    public SimpleToolBar setMenu(boolean z) {
        ImageButton imageButton = this.menuIb;
        if (imageButton == null) {
            return this;
        }
        if (z) {
            imageButton.setVisibility(0);
            this.menuIb.setImageResource(R.drawable.toolbar_more);
            onClick(this.menuIb, 2);
        } else {
            imageButton.setVisibility(8);
        }
        return this;
    }

    public SimpleToolBar setMenu(@DrawableRes int i) {
        ImageButton imageButton = this.menuIb;
        if (imageButton == null) {
            return this;
        }
        imageButton.setVisibility(0);
        this.menuIb.setImageResource(i);
        onClick(this.menuIb, 2);
        return this;
    }

    public SimpleToolBar setBackGround(@ColorInt int i) {
        LinearLayout linearLayout = this.linearLayout;
        if (linearLayout == null) {
            return this;
        }
        linearLayout.setBackgroundColor(i);
        return this;
    }

    public SimpleToolBar isShowBottomLine(boolean z) {
        if (z) {
            this.line.setVisibility(0);
        } else {
            this.line.setVisibility(4);
        }
        return this;
    }

    public SimpleToolBar setTitleColor(@ColorInt int i) {
        TextView textView = this.titleTv;
        if (textView == null) {
            return this;
        }
        textView.setTextColor(i);
        return this;
    }

    public void onClick(View view2, final int i) {
        if (view2 == null && this.onToolBarClickListener == null) {
            Log.e("SimpleToolBar", "onToolBarClickListener or view is null");
        } else {
            view2.setOnClickListener(new View.OnClickListener() { // from class: view.-$$Lambda$SimpleToolBar$mcoZQeVxsyptoAgAVxxPWGiPp0k
                @Override // android.view.View.OnClickListener
                public final void onClick(View view3) {
                    SimpleToolBar.lambda$onClick$0(this.f$0, i, view3);
                }
            });
        }
    }

    public static /* synthetic */ void lambda$onClick$0(SimpleToolBar simpleToolBar, int i, View view2) {
        switch (i) {
            case 0:
                simpleToolBar.onToolBarClickListener.onBackClick(view2);
                break;
            case 1:
                simpleToolBar.onToolBarClickListener.onTitleClick(view2);
                break;
            case 2:
                simpleToolBar.onToolBarClickListener.onMenuClick(view2);
                break;
        }
    }

    public void setOnToolBarClickListener(OnToolBarClickListener onToolBarClickListener) {
        this.onToolBarClickListener = onToolBarClickListener;
    }
}
