package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemShareHistoryBinding;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ShareHistoryAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<String> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(String str, int i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public ShareHistoryAdapter(Context context, List<String> list) {
        this.context = context;
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemShareHistoryBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_share_history, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<String> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.tvName.setText(this.itemList.get(i));
        viewHolder.binding.layoutItem.setOnClickListener(new View.OnClickListener() { // from class: adapter.ShareHistoryAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (ShareHistoryAdapter.this.onItemClickListener != null) {
                    ShareHistoryAdapter.this.onItemClickListener.onItemClick((String) ShareHistoryAdapter.this.itemList.get(i), i);
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemShareHistoryBinding binding;

        public ViewHolder(ItemShareHistoryBinding itemShareHistoryBinding) {
            super(itemShareHistoryBinding.getRoot());
            this.binding = itemShareHistoryBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
