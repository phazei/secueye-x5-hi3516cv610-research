package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.seculink.app.R;
import com.seculink.app.databinding.ItemFourLiveBinding;
import java.util.List;
import tools.OnMultiClickListener;
import tools.ScreenUtil;
import view.MyGlTextureView;

/* JADX INFO: loaded from: classes.dex */
public class LiveFourAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final int TYPE_HORIZONTAL = 0;
    private static final int TYPE_VERTICAL = 1;
    private int h;
    private boolean isTikTok;
    private List<MyGlTextureView> itemList;
    private LayoutInflater layoutInflater;
    private OnItemClickListener onItemClickListener;
    private int w;

    public interface OnItemClickListener {
        void onItemClick(int i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return (i == 0 || i == 1) ? 0 : 1;
    }

    public LiveFourAdapter(Context context, List<MyGlTextureView> list, int i, int i2, boolean z) {
        this.itemList = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.w = i;
        this.h = i2;
        this.isTikTok = z;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder((ItemFourLiveBinding) DataBindingUtil.inflate(LayoutInflater.from(this.layoutInflater.getContext()), R.layout.item_four_live, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, @SuppressLint({"RecyclerView"}) final int i) {
        List<MyGlTextureView> list = this.itemList;
        if (list == null || list.size() == 0) {
            return;
        }
        viewHolder.binding.layoutItem.addView(this.itemList.get(i));
        if (i == 0 || i == 1) {
            MyGlTextureView myGlTextureView = this.itemList.get(i);
            int i2 = this.w;
            setViewLayoutParams(myGlTextureView, i2 / 2, ((i2 / 2) / 16) * 9);
        } else {
            setViewLayoutParams(this.itemList.get(i), -1, ScreenUtil.dp2Px(this.layoutInflater.getContext(), 212.0f));
        }
        this.itemList.get(i).requestLayout();
        if (i == 0 || i == 1) {
            if (this.isTikTok) {
                setViewLayoutParams(this.itemList.get(i), this.h / 2, ScreenUtil.dp2Px(this.layoutInflater.getContext(), 11.0f));
            } else {
                MyGlTextureView myGlTextureView2 = this.itemList.get(i);
                int i3 = this.w;
                setViewLayoutParams(myGlTextureView2, i3 / 2, ((i3 / 2) / 16) * 9);
            }
        } else {
            setViewLayoutParams(this.itemList.get(i), -1, ScreenUtil.dp2Px(this.layoutInflater.getContext(), 212.0f));
        }
        viewHolder.binding.layoutItem.setOnClickListener(new OnMultiClickListener() { // from class: adapter.LiveFourAdapter.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                if (LiveFourAdapter.this.onItemClickListener != null) {
                    LiveFourAdapter.this.onItemClickListener.onItemClick(i);
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
        public ItemFourLiveBinding binding;

        public ViewHolder(ItemFourLiveBinding itemFourLiveBinding) {
            super(itemFourLiveBinding.getRoot());
            this.binding = itemFourLiveBinding;
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
