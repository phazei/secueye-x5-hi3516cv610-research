package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemDialogListBinding;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DiaLogListAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<String> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;
    private int position;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public DiaLogListAdapter(Context context, List<String> list, int i) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.position = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemDialogListBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_dialog_list, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        viewHolder.binding.tvText.setText(this.itemList.get(i));
        if (this.position != i) {
            viewHolder.binding.tvText.setTextColor(this.layoutInflater.getContext().getColor(R.color.colorAccent));
        } else {
            viewHolder.binding.tvText.setTextColor(this.layoutInflater.getContext().getColor(R.color.color_ff0000));
        }
        viewHolder.binding.layoutItem.setOnClickListener(new View.OnClickListener() { // from class: adapter.DiaLogListAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (DiaLogListAdapter.this.onItemClickListener != null) {
                    DiaLogListAdapter.this.onItemClickListener.onItemClick(i);
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemDialogListBinding binding;

        public ViewHolder(@NonNull ItemDialogListBinding itemDialogListBinding) {
            super(itemDialogListBinding.getRoot());
            this.binding = itemDialogListBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
