package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bean.FeedbackBean;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemFeedbackBinding;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class FeedbackAdapter extends RecyclerView.Adapter<ViewHolder> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<FeedbackBean> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int i, FeedbackBean feedbackBean);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public FeedbackAdapter(Context context, List<FeedbackBean> list) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemFeedbackBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_feedback, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<FeedbackBean> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.setModel(this.itemList.get(i));
        String[] stringArray = this.layoutInflater.getContext().getResources().getStringArray(R.array.feedback_type);
        if (this.itemList.get(i).type > 0 && this.itemList.get(i).type - 1 < stringArray.length) {
            viewHolder.binding.tvType.setText(stringArray[this.itemList.get(i).type - 1]);
        }
        viewHolder.binding.tvTime.setText(this.dateFormat.format(new Date(this.itemList.get(i).gmtCreate)));
        viewHolder.binding.layoutItem.setOnClickListener(new OnMultiClickListener() { // from class: adapter.FeedbackAdapter.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (FeedbackAdapter.this.onItemClickListener != null) {
                    FeedbackAdapter.this.onItemClickListener.onItemClick(i, (FeedbackBean) FeedbackAdapter.this.itemList.get(i));
                }
            }
        });
        viewHolder.binding.line.setVisibility(this.itemList.size() + (-1) == i ? 8 : 0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemFeedbackBinding binding;

        public ViewHolder(ItemFeedbackBinding itemFeedbackBinding) {
            super(itemFeedbackBinding.getRoot());
            this.binding = itemFeedbackBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
