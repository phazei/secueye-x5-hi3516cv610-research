package kt;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.alibaba.sdk.android.oss.common.RequestParameters;
import com.aliyun.alink.business.devicecenter.base.AlinkConstants;
import com.seculink.app.R;
import java.util.ArrayList;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

/* JADX INFO: compiled from: HelpAdapter.kt */
/* JADX INFO: loaded from: classes4.dex */
@Metadata(bv = {1, 0, 3}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001:\u0002\u0016\u0017B%\u0012\u0016\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006\u0012\u0006\u0010\u0007\u001a\u00020\b¢\u0006\u0002\u0010\tJ\b\u0010\f\u001a\u00020\rH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00022\u0006\u0010\u0011\u001a\u00020\rH\u0016J\u0018\u0010\u0012\u001a\u00020\u00022\u0006\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\rH\u0016R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004¢\u0006\u0002\n\u0000R!\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006¢\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000b¨\u0006\u0018"}, d2 = {"Lkt/HelpAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lkt/HelpAdapter$ViewHolder;", AlinkConstants.KEY_LIST, "Ljava/util/ArrayList;", "", "Lkotlin/collections/ArrayList;", "imageViewClick", "Lkt/HelpAdapter$ImageViewClick;", "(Ljava/util/ArrayList;Lkt/HelpAdapter$ImageViewClick;)V", "getList", "()Ljava/util/ArrayList;", "getItemCount", "", "onBindViewHolder", "", "viewHolder", RequestParameters.POSITION, "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "p1", "ImageViewClick", "ViewHolder", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
public final class HelpAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final ImageViewClick imageViewClick;

    @NotNull
    private final ArrayList<String> list;

    /* JADX INFO: compiled from: HelpAdapter.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0000\bf\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H&¨\u0006\u0006"}, d2 = {"Lkt/HelpAdapter$ImageViewClick;", "", "onClick", "", "string", "", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
    public interface ImageViewClick {
        void onClick(@NotNull String string);
    }

    public HelpAdapter(@NotNull ArrayList<String> list, @NotNull ImageViewClick imageViewClick) {
        Intrinsics.checkParameterIsNotNull(list, "list");
        Intrinsics.checkParameterIsNotNull(imageViewClick, "imageViewClick");
        this.list = list;
        this.imageViewClick = imageViewClick;
    }

    @NotNull
    public final ArrayList<String> getList() {
        return this.list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NotNull
    public ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int p1) {
        Intrinsics.checkParameterIsNotNull(parent, "parent");
        View viewInflate = View.inflate(parent.getContext(), R.layout.recycler_adapter_help_item, null);
        Intrinsics.checkExpressionValueIsNotNull(viewInflate, "View.inflate(parent.cont…_adapter_help_item, null)");
        return new ViewHolder(viewInflate);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NotNull ViewHolder viewHolder, int position) {
        Intrinsics.checkParameterIsNotNull(viewHolder, "viewHolder");
        View view2 = viewHolder.itemView;
        Intrinsics.checkExpressionValueIsNotNull(view2, "viewHolder.itemView");
        TextView textView = (TextView) view2.findViewById(R.id.text1);
        Intrinsics.checkExpressionValueIsNotNull(textView, "viewHolder.itemView.text1");
        textView.setText(this.list.get(position));
        View view3 = viewHolder.itemView;
        Intrinsics.checkExpressionValueIsNotNull(view3, "viewHolder.itemView");
        ((ImageView) view3.findViewById(R.id.imageView)).setOnClickListener(new View.OnClickListener() { // from class: kt.HelpAdapter.onBindViewHolder.1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view4) {
                HelpAdapter.this.imageViewClick.onClick("https://www.bilibili.com/video/BV1ki4y157Lh");
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.list.size();
    }

    /* JADX INFO: compiled from: HelpAdapter.kt */
    @Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003¢\u0006\u0002\u0010\u0004¨\u0006\u0005"}, d2 = {"Lkt/HelpAdapter$ViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Landroid/view/View;)V", "secueye_googleRelease"}, k = 1, mv = {1, 1, 15})
    public static final class ViewHolder extends RecyclerView.ViewHolder {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ViewHolder(@NotNull View view2) {
            super(view2);
            Intrinsics.checkParameterIsNotNull(view2, "view");
        }
    }
}
