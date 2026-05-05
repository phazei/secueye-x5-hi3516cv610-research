package view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes5.dex */
public class EditViewIpc extends FrameLayout {
    private int height;
    private TextView tvCheckAll;
    private TextView tvDelete;
    private TextView tvInverse;

    public interface OnClickListener {
        void OnCheckAllClick(View view2);

        void OnDeleteClick(View view2);

        void OnInverseClick(View view2);
    }

    public EditViewIpc(Context context) {
        super(context);
        initView(context);
    }

    public EditViewIpc(Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        initView(context);
    }

    public EditViewIpc(Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.layout_edit_view_ipc, (ViewGroup) this, true);
        this.tvCheckAll = (TextView) findViewById(R.id.tv_check_all);
        this.tvInverse = (TextView) findViewById(R.id.tv_inverse);
        this.tvDelete = (TextView) findViewById(R.id.tv_delete);
    }

    @Override // android.view.View
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        this.height = getHeight();
    }

    public void addOnClickListener(final OnClickListener onClickListener) {
        this.tvCheckAll.setOnClickListener(new View.OnClickListener() { // from class: view.EditViewIpc.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                onClickListener.OnCheckAllClick(view2);
            }
        });
        this.tvInverse.setOnClickListener(new View.OnClickListener() { // from class: view.EditViewIpc.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                onClickListener.OnInverseClick(view2);
            }
        });
        this.tvDelete.setOnClickListener(new View.OnClickListener() { // from class: view.EditViewIpc.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                onClickListener.OnDeleteClick(view2);
            }
        });
    }

    public void setTvCheckAllDrawable(boolean z) {
        this.tvCheckAll.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, getResources().getDrawable(!z ? R.drawable.ic_sel_all : R.drawable.ic_check), (Drawable) null, (Drawable) null);
        this.tvCheckAll.setTextColor(!z ? Color.parseColor("#aeb3c0") : getResources().getColor(R.color.color_white));
    }

    public void setTvDeleteDrawable(boolean z) {
        this.tvDelete.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, getResources().getDrawable(!z ? R.drawable.ic_delete2 : R.drawable.ic_delete3), (Drawable) null, (Drawable) null);
        this.tvDelete.setTextColor(!z ? Color.parseColor("#aeb3c0") : getResources().getColor(R.color.color_white));
        this.tvDelete.setEnabled(z);
    }

    public void show() {
        ObjectAnimator.ofFloat(this, "translationY", 0.0f, -this.height).setDuration(300L).start();
    }

    public void hide() {
        ObjectAnimator.ofFloat(this, "translationY", -this.height, 0.0f).setDuration(300L).start();
    }
}
