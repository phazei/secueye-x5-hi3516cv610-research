package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bean.BluetoothDeviceModel;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemBleBinding;
import com.xiaomi.mipush.sdk.Constants;
import java.util.List;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class ScanBleAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<BluetoothDeviceModel> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onConnectClick(BluetoothDeviceModel bluetoothDeviceModel, int i);

        void onDisconnectClick(BluetoothDeviceModel bluetoothDeviceModel, int i);
    }

    public ScanBleAdapter(Context context, List<BluetoothDeviceModel> list) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemBleBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_ble, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<BluetoothDeviceModel> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.tvName.setText(this.itemList.get(i).getName().replace("ipc_", ""));
        viewHolder.binding.tvAddress.setText(this.itemList.get(i).getAddress());
        if (this.itemList.get(i).getName().split(Constants.ACCEPT_TIME_SEPARATOR_SERVER)[r0.length - 1].length() == 6) {
            viewHolder.binding.tvState.setText(this.layoutInflater.getContext().getString(R.string.ble_not_connect));
        } else {
            viewHolder.binding.tvState.setText(this.layoutInflater.getContext().getString(R.string.ble_not_connect));
        }
        viewHolder.binding.layoutItem.setOnClickListener(new OnMultiClickListener() { // from class: adapter.ScanBleAdapter.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (ScanBleAdapter.this.onItemClickListener != null) {
                    ScanBleAdapter.this.onItemClickListener.onConnectClick((BluetoothDeviceModel) ScanBleAdapter.this.itemList.get(i), i);
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemBleBinding binding;

        public ViewHolder(ItemBleBinding itemBleBinding) {
            super(itemBleBinding.getRoot());
            this.binding = itemBleBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
