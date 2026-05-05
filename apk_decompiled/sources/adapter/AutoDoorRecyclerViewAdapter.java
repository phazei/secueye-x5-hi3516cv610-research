package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.seculink.app.R;
import com.seculink.app.databinding.AutodoorRecyclerviewItemBinding;
import sdk.IPCManager;
import tools.SharePreferenceManager;
import view.DialogView;

/* JADX INFO: loaded from: classes.dex */
public class AutoDoorRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context context;
    int controllerSize;
    String iotId;

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return 4;
    }

    public AutoDoorRecyclerViewAdapter(String str, Context context, int i) {
        this.iotId = str;
        this.context = context;
        this.controllerSize = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(AutodoorRecyclerviewItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        String autoName = SharePreferenceManager.getInstance().getAutoName(this.iotId, i);
        TextView textView = viewHolder.binding.name;
        if (autoName == null || autoName.equals("")) {
            autoName = this.context.getString(R.string.remote_control) + (i + 1);
        }
        textView.setText(autoName);
        viewHolder.binding.reset.setOnClickListener(new View.OnClickListener() { // from class: adapter.AutoDoorRecyclerViewAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                new DialogView.Builder(AutoDoorRecyclerViewAdapter.this.context).setContent(AutoDoorRecyclerViewAdapter.this.context.getString(R.string.device_reset_tips)).setNegativeClickListener(AutoDoorRecyclerViewAdapter.this.context.getString(R.string.cancel), new DialogView.OnNegativeClickListener() { // from class: adapter.AutoDoorRecyclerViewAdapter.1.2
                    @Override // view.DialogView.OnNegativeClickListener
                    public void onNegativeClick(DialogView dialogView) {
                        dialogView.dismiss();
                    }
                }).setPositiveClickListener(AutoDoorRecyclerViewAdapter.this.context.getString(R.string.confirm), new DialogView.OnPositiveClickListener() { // from class: adapter.AutoDoorRecyclerViewAdapter.1.1
                    @Override // view.DialogView.OnPositiveClickListener
                    public void onPositiveClick(DialogView dialogView) {
                        AutoDoorRecyclerViewAdapter.this.delete(i);
                        dialogView.dismiss();
                    }
                }).build().show();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void delete(int i) {
        for (int i2 = 0; i2 < this.controllerSize; i2++) {
            IPCManager.getInstance().getDevice(this.iotId).deleteController((this.controllerSize * i) + i2, new IPanelCallback() { // from class: adapter.AutoDoorRecyclerViewAdapter.2
                @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
                public void onComplete(boolean z, @Nullable Object obj) {
                }
            });
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AutodoorRecyclerviewItemBinding binding;

        public ViewHolder(@NonNull AutodoorRecyclerviewItemBinding autodoorRecyclerviewItemBinding) {
            super(autodoorRecyclerviewItemBinding.getRoot());
            this.binding = autodoorRecyclerviewItemBinding;
        }
    }
}
