package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bean.CloudPayModel;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemPayBinding;
import java.util.List;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class YunPayAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<CloudPayModel> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public YunPayAdapter(Context context, List<CloudPayModel> list) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemPayBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_pay, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<CloudPayModel> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.tvName.setText(this.itemList.get(i).comboName == null ? "全天云存储" : this.itemList.get(i).comboName);
        viewHolder.binding.tvOrder.setText(this.itemList.get(i).orderNum);
        viewHolder.binding.tvTime.setText(this.itemList.get(i).timeNum + this.itemList.get(i).timeUnit);
        viewHolder.binding.tvTypeText.setText(this.itemList.get(i).status);
        if (this.itemList.get(i).status.equals("使用中")) {
            viewHolder.binding.ivType.setImageResource(R.drawable.green_circle);
        } else if (this.itemList.get(i).status.equals("已过期")) {
            viewHolder.binding.ivType.setImageResource(R.drawable.red_circle);
        } else if (this.itemList.get(i).status.equals("待使用")) {
            viewHolder.binding.ivType.setImageResource(R.drawable.bg_blue_oval);
        }
        viewHolder.binding.layoutItem.setOnClickListener(new OnMultiClickListener() { // from class: adapter.YunPayAdapter.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (YunPayAdapter.this.onItemClickListener != null) {
                    YunPayAdapter.this.onItemClickListener.onItemClick(i);
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemPayBinding binding;

        public ViewHolder(ItemPayBinding itemPayBinding) {
            super(itemPayBinding.getRoot());
            this.binding = itemPayBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
