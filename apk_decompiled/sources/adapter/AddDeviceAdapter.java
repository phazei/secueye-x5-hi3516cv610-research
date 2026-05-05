package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bean.AddDeviceModel;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemAddDeviceBinding;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class AddDeviceAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context context;
    private List<AddDeviceModel> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(AddDeviceModel addDeviceModel, int i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public AddDeviceAdapter(Context context, List<AddDeviceModel> list) {
        this.context = context;
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemAddDeviceBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_add_device, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<AddDeviceModel> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.setModel(this.itemList.get(i));
        Glide.with(this.context).load(Integer.valueOf(this.itemList.get(i).imgSrc)).into(viewHolder.binding.ivType);
        viewHolder.binding.layoutItem.setOnClickListener(new View.OnClickListener() { // from class: adapter.AddDeviceAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (AddDeviceAdapter.this.onItemClickListener != null) {
                    AddDeviceAdapter.this.onItemClickListener.onItemClick((AddDeviceModel) AddDeviceAdapter.this.itemList.get(i), i);
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemAddDeviceBinding binding;

        public ViewHolder(ItemAddDeviceBinding itemAddDeviceBinding) {
            super(itemAddDeviceBinding.getRoot());
            this.binding = itemAddDeviceBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
