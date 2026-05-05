package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bean.FeedBackReplyList;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemFeedbackReplyBinding;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class FeedbackReplyAdapter extends RecyclerView.Adapter<ViewHolder> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private List<FeedBackReplyList> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int i, FeedBackReplyList feedBackReplyList);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public FeedbackReplyAdapter(Context context, List<FeedBackReplyList> list) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemFeedbackReplyBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_feedback_reply, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) int i) {
        List<FeedBackReplyList> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.setModel(this.itemList.get(i));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) viewHolder.binding.layoutItem.getLayoutParams();
        if (this.itemList.get(i).type == 1) {
            viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getResources().getString(R.string.feedback_content));
            layoutParams.addRule(11, -1);
        } else {
            viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getResources().getString(R.string.service_response));
            layoutParams.addRule(9, -1);
        }
        viewHolder.binding.layoutItem.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) viewHolder.binding.tvTime.getLayoutParams();
        if (this.itemList.get(i).type == 1) {
            layoutParams2.addRule(11, -1);
        } else {
            layoutParams2.addRule(9, -1);
        }
        viewHolder.binding.tvTime.setLayoutParams(layoutParams2);
        viewHolder.binding.tvTime.setText(this.dateFormat.format(new Date(this.itemList.get(i).gmtCreate)));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemFeedbackReplyBinding binding;

        public ViewHolder(ItemFeedbackReplyBinding itemFeedbackReplyBinding) {
            super(itemFeedbackReplyBinding.getRoot());
            this.binding = itemFeedbackReplyBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
