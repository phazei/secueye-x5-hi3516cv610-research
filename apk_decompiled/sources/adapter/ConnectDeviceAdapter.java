package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bean.ConnectDeviceInfo;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemConnectDeviceBinding;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ConnectDeviceAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<ConnectDeviceInfo> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int i, ConnectDeviceInfo connectDeviceInfo);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public ConnectDeviceAdapter(Context context, List<ConnectDeviceInfo> list) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemConnectDeviceBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_connect_device, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) int i) {
        List<ConnectDeviceInfo> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.setModel(this.itemList.get(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemConnectDeviceBinding binding;

        public ViewHolder(ItemConnectDeviceBinding itemConnectDeviceBinding) {
            super(itemConnectDeviceBinding.getRoot());
            this.binding = itemConnectDeviceBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
