package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public abstract class BaseRecycleViewAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {
    protected Context context;
    protected LayoutInflater inflater;
    protected List<T> mDatas = new ArrayList();

    public BaseRecycleViewAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void addData(int i, T t) {
        this.mDatas.add(i, t);
        notifyItemInserted(i);
    }

    public void setData(int i, T t) {
        this.mDatas.set(i, t);
        notifyItemChanged(i);
    }

    public void setDatas(List<T> list) {
        this.mDatas.clear();
        notifyDataSetChanged();
        addDatas(list);
    }

    public void addDatas(List<T> list) {
        this.mDatas.addAll(list);
        notifyItemRangeInserted(0, list.size());
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDatas.size();
    }
}
