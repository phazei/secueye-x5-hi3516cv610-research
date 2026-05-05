package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemLiveBinding;
import java.util.List;
import tools.OnMultiClickListener;
import tools.ScreenUtil;
import view.MyGlTextureView;

/* JADX INFO: loaded from: classes.dex */
public class LiveHorizontalAdapter extends RecyclerView.Adapter<ViewHolder> {
    int h;
    boolean isTikTok;
    private List<MyGlTextureView> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;
    int w;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    public LiveHorizontalAdapter(Context context, List<MyGlTextureView> list, boolean z, int i, int i2) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.isTikTok = z;
        this.w = i;
        this.h = i2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemLiveBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_live, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<MyGlTextureView> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.layoutItem.addView(this.itemList.get(i));
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-2, -2);
        layoutParams.addRule(15, -1);
        this.itemList.get(i).setLayoutParams(layoutParams);
        setViewLayoutParams(this.itemList.get(i), -1, ScreenUtil.dp2Px(this.layoutInflater.getContext(), 212.0f));
        this.itemList.get(i).requestLayout();
        if (this.isTikTok) {
            setViewLayoutParams(this.itemList.get(i), this.h, this.w);
            setViewLayoutParams(viewHolder.binding.layoutItem, this.h, this.w);
        } else {
            setViewLayoutParams(this.itemList.get(i), this.h / 2, this.w / 2);
            setViewLayoutParams(viewHolder.binding.layoutItem, this.h / 2, this.w / 2);
        }
        viewHolder.binding.layoutItem.setOnClickListener(new OnMultiClickListener() { // from class: adapter.LiveHorizontalAdapter.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (LiveHorizontalAdapter.this.onItemClickListener != null) {
                    LiveHorizontalAdapter.this.onItemClickListener.onItemClick(i);
                }
            }
        });
    }

    public static void setViewLayoutParams(View view2, int i, int i2) {
        ViewGroup.LayoutParams layoutParams = view2.getLayoutParams();
        if (layoutParams.height == i2 && layoutParams.width == i) {
            return;
        }
        layoutParams.width = i;
        layoutParams.height = i2;
        view2.setLayoutParams(layoutParams);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ItemLiveBinding binding;

        public ViewHolder(ItemLiveBinding itemLiveBinding) {
            super(itemLiveBinding.getRoot());
            this.binding = itemLiveBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
