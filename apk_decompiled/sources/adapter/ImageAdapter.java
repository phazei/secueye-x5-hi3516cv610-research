package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bean.PresetPositionBean;
import com.bumptech.glide.Glide;
import com.seculink.app.R;
import java.util.HashSet;
import java.util.List;
import tools.DensityUtil;

/* JADX INFO: loaded from: classes.dex */
public class ImageAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context context;
    private List<PresetPositionBean.bitmapBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private HashSet<String> set;
    private final int mCountLimit = 8;
    boolean isEdit = false;

    public interface OnItemClickListener {
        void onItemLongClick(View view2, int i);

        void onTakePhotoClick(View view2, int i);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public ImageAdapter(List<PresetPositionBean.bitmapBean> list, Context context, HashSet<String> hashSet) {
        this.mData = list;
        this.context = context;
        this.set = hashSet;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.iamge_recycler_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        if (this.isEdit) {
            myViewHolder.imageView1.setVisibility(0);
        } else {
            myViewHolder.imageView1.setVisibility(8);
        }
        Glide.with(this.context).load(stringToBitmap(this.mData.get(i).getBitmap())).into(myViewHolder.imageView);
        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() { // from class: adapter.ImageAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ImageAdapter.this.mOnItemClickListener.onTakePhotoClick(view2, ((PresetPositionBean.bitmapBean) ImageAdapter.this.mData.get(i)).getPresetPositionNum());
            }
        });
        myViewHolder.imageView.setOnLongClickListener(new View.OnLongClickListener() { // from class: adapter.ImageAdapter.2
            @Override // android.view.View.OnLongClickListener
            public boolean onLongClick(View view2) {
                ImageAdapter.this.mOnItemClickListener.onItemLongClick(view2, myViewHolder.getAdapterPosition());
                return true;
            }
        });
        myViewHolder.textView.setVisibility(0);
        myViewHolder.textView.setText(this.mData.get(i).getPresetPositionNum() + "");
        myViewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        myViewHolder.imageView1.setOnClickListener(new View.OnClickListener() { // from class: adapter.ImageAdapter.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ImageAdapter.this.set.remove(((PresetPositionBean.bitmapBean) ImageAdapter.this.mData.get(myViewHolder.getAdapterPosition())).getPresetPositionNum() + "");
                ImageAdapter.this.mData.remove(myViewHolder.getAdapterPosition());
                ImageAdapter.this.notifyItemRemoved(i);
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<PresetPositionBean.bitmapBean> list = this.mData;
        if (list != null && list.size() >= 8) {
            return 8;
        }
        List<PresetPositionBean.bitmapBean> list2 = this.mData;
        if (list2 == null) {
            return 1;
        }
        return list2.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private ImageView imageView1;
        private TextView textView;

        private MyViewHolder(View view2) {
            super(view2);
            this.imageView = (ImageView) view2.findViewById(R.id.image_view);
            int iDip2px = (ImageAdapter.this.context.getResources().getDisplayMetrics().widthPixels - DensityUtil.dip2px(ImageAdapter.this.context, 90.0f)) / 3;
            ViewGroup.LayoutParams layoutParams = this.imageView.getLayoutParams();
            layoutParams.width = iDip2px;
            layoutParams.height = -2;
            this.imageView.setLayoutParams(layoutParams);
            this.imageView.setMaxWidth(iDip2px);
            this.imageView.setMaxHeight(iDip2px);
            this.imageView1 = (ImageView) view2.findViewById(R.id.image_view_delete);
            this.textView = (TextView) view2.findViewById(R.id.image_view_text);
            this.imageView1.setImageResource(R.drawable.ic_delete);
        }
    }

    public void setEdit() {
        this.isEdit = !this.isEdit;
        notifyDataSetChanged();
    }

    public Bitmap stringToBitmap(String str) {
        try {
            byte[] bArrDecode = Base64.decode(str, 0);
            return BitmapFactory.decodeByteArray(bArrDecode, 0, bArrDecode.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void itemInserted() {
        notifyItemInserted(getItemCount());
    }

    public void itemChanged(int i) {
        notifyItemChanged(i);
    }
}
