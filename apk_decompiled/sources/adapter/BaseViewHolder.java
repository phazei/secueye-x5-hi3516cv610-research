package adapter;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseViewHolder<T> extends RecyclerView.ViewHolder {
    public void onBind(T t, int i) {
    }

    public BaseViewHolder(View view2) {
        super(view2);
    }
}
