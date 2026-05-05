package adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bean.CloudVideo;
import com.bumptech.glide.Glide;
import com.seculink.app.databinding.CloudDownloadRecyclerviewItemBinding;
import java.text.DecimalFormat;

/* JADX INFO: loaded from: classes.dex */
public class CloudDownloadAdapter extends RecyclerView.Adapter<ViewHolder> {
    private CloudVideo cloudVideoBean;
    private OnItemClickListener itemClickListener;

    public interface OnItemClickListener {
        void onClick(int i, Button button);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(CloudDownloadRecyclerviewItemBinding.inflate(LayoutInflater.from(viewGroup.getContext()), null, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @SuppressLint({"SetTextI18n"})
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.binding.beginTime.setText(this.cloudVideoBean.getRecordFileList().get(i).getBeginTime());
        viewHolder.binding.endTime.setText(this.cloudVideoBean.getRecordFileList().get(i).getEndTime());
        String str = new DecimalFormat("#0.00").format((this.cloudVideoBean.getRecordFileList().get(i).getFileSize() / 1024.0f) / 1024.0f);
        viewHolder.binding.fileSize.setText(str + " MB");
        Glide.with(viewHolder.binding.getRoot()).load(this.cloudVideoBean.getRecordFileList().get(i).getSnapshotUrl()).into(viewHolder.binding.imageView);
        if (this.itemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() { // from class: adapter.CloudDownloadAdapter.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view2) {
                    CloudDownloadAdapter.this.itemClickListener.onClick(i, viewHolder.binding.btSelect);
                }
            });
        }
        viewHolder.binding.btSelect.setVisibility(this.cloudVideoBean.getRecordFileList().get(i).getIsSelected() ? 0 : 8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        CloudVideo cloudVideo = this.cloudVideoBean;
        if (cloudVideo != null) {
            return cloudVideo.getRecordFileList().size();
        }
        return 0;
    }

    public void setDate(CloudVideo cloudVideo) {
        this.cloudVideoBean = cloudVideo;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CloudDownloadRecyclerviewItemBinding binding;

        public ViewHolder(@NonNull CloudDownloadRecyclerviewItemBinding cloudDownloadRecyclerviewItemBinding) {
            super(cloudDownloadRecyclerviewItemBinding.getRoot());
            this.binding = cloudDownloadRecyclerviewItemBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.itemClickListener = onItemClickListener;
    }
}
