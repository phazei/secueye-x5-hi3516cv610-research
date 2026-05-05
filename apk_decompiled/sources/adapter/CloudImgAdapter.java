package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import bean.CloudVideo;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemCloudImgBinding;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import tools.OnMultiClickListener;
import view.GlideRoundTransform;

/* JADX INFO: loaded from: classes.dex */
public class CloudImgAdapter extends RecyclerView.Adapter<ViewHolder> {
    private List<CloudVideo.RecordFileListBean> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    final SimpleDateFormat timeLineFormatter = new SimpleDateFormat("mm:ss");

    public interface OnItemClickListener {
        void onItemClick(int i, CloudVideo.RecordFileListBean recordFileListBean);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return i;
    }

    public CloudImgAdapter(Context context, List<CloudVideo.RecordFileListBean> list) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemCloudImgBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_cloud_img, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<CloudVideo.RecordFileListBean> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.layoutItem.setSelected(false);
        Glide.with(this.layoutInflater.getContext()).load(Integer.valueOf(R.drawable.icon_playing)).load((Object) viewHolder.binding.ivPlaying);
        viewHolder.binding.ivPlaying.setVisibility(8);
        viewHolder.binding.top.setVisibility(0);
        viewHolder.binding.bottom.setVisibility(0);
        viewHolder.binding.setModel(this.itemList.get(i));
        viewHolder.binding.layoutItem.setSelected(this.itemList.get(i).getIsSelected());
        viewHolder.binding.ivPlaying.setVisibility(this.itemList.get(i).getIsSelected() ? 0 : 8);
        String beginTime = this.itemList.get(i).getBeginTime();
        String endTime = this.itemList.get(i).getEndTime();
        try {
            Date date = this.sdf.parse(beginTime);
            Date date2 = this.sdf.parse(endTime);
            viewHolder.binding.tvTimeSize.setText("" + this.timeLineFormatter.format(Long.valueOf(date2.getTime() - date.getTime())));
            viewHolder.binding.tvTimeSize.bringToFront();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (i == 0) {
            viewHolder.binding.top.setVisibility(8);
        }
        if (i == this.itemList.size() - 1) {
            viewHolder.binding.bottom.setVisibility(8);
        }
        RequestOptions requestOptionsError = new RequestOptions().transform(new GlideRoundTransform(this.layoutInflater.getContext(), 4)).diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.mipmap.default_snap);
        requestOptionsError.transforms(new RoundedCorners(25));
        Glide.with(this.layoutInflater.getContext()).load(this.itemList.get(i).getSnapshotUrl()).apply(requestOptionsError).into(new SimpleTarget<Drawable>() { // from class: adapter.CloudImgAdapter.1
            @Override // com.bumptech.glide.request.target.Target
            public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
                onResourceReady((Drawable) obj, (Transition<? super Drawable>) transition);
            }

            public void onResourceReady(Drawable drawable, Transition<? super Drawable> transition) {
                if (drawable != null) {
                    viewHolder.binding.ivImg.setImageDrawable(drawable);
                }
            }
        });
        if (this.itemList.get(i).getIntelligentTypeList() == null) {
            int recordType = this.itemList.get(i).getRecordType();
            if (recordType == 1) {
                viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getString(R.string.motion_detect_alarm));
            } else {
                switch (recordType) {
                    case 10005:
                        viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getString(R.string.car));
                        break;
                    case 10006:
                        viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getString(R.string.awaken));
                        break;
                    default:
                        viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getString(R.string.other_alarm));
                        break;
                }
            }
        } else {
            int recordType2 = this.itemList.get(i).getRecordType();
            if (recordType2 == 1) {
                viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getString(R.string.motion_detect_alarm));
            } else {
                switch (recordType2) {
                    case 10005:
                        viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getString(R.string.car));
                        break;
                    case 10006:
                        viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getString(R.string.awaken));
                        break;
                    default:
                        viewHolder.binding.tvType.setText(this.layoutInflater.getContext().getString(R.string.other_alarm));
                        break;
                }
            }
        }
        viewHolder.binding.layoutItem.setOnClickListener(new OnMultiClickListener() { // from class: adapter.CloudImgAdapter.2
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (CloudImgAdapter.this.onItemClickListener != null) {
                    CloudImgAdapter.this.onItemClickListener.onItemClick(i, (CloudVideo.RecordFileListBean) CloudImgAdapter.this.itemList.get(i));
                }
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemCloudImgBinding binding;

        public ViewHolder(ItemCloudImgBinding itemCloudImgBinding) {
            super(itemCloudImgBinding.getRoot());
            this.binding = itemCloudImgBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
