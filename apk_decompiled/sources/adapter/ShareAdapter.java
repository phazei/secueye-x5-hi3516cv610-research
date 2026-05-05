package adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import bean.SharePersonBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class ShareAdapter extends BaseQuickAdapter<SharePersonBean, com.chad.library.adapter.base.BaseViewHolder> {
    private Context context;
    private boolean isOpen;
    private boolean mFlag;
    private DeleteShareOnClick onClick;
    private int openPosition;
    private final List<String> sharePersonBeans;

    public interface DeleteShareOnClick {
        void update(int i);
    }

    public ShareAdapter(int i, Context context, DeleteShareOnClick deleteShareOnClick) {
        super(i);
        this.openPosition = -1;
        this.isOpen = false;
        this.sharePersonBeans = new ArrayList();
        this.mFlag = false;
        this.context = context;
        this.onClick = deleteShareOnClick;
    }

    private void initData() {
        this.sharePersonBeans.add("基础设置权限");
        this.sharePersonBeans.add("设置报警权限");
        this.sharePersonBeans.add("设备存储权限");
        this.sharePersonBeans.add("设备网络权限");
        this.sharePersonBeans.add("系统权限");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.chad.library.adapter.base.BaseQuickAdapter
    public void convert(com.chad.library.adapter.base.BaseViewHolder baseViewHolder, SharePersonBean sharePersonBean) {
        TextView textView = (TextView) baseViewHolder.getView(R.id.tv_share_number);
        final int adapterPosition = baseViewHolder.getAdapterPosition();
        textView.setText(sharePersonBean.identityAlias + "");
        Button button = (Button) baseViewHolder.getView(R.id.expand_share_permission_btn);
        ((TextView) baseViewHolder.getView(R.id.text_permission)).setOnClickListener(new View.OnClickListener() { // from class: adapter.ShareAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ShareAdapter.this.mFlag = !r3.mFlag;
                ShareAdapter shareAdapter = ShareAdapter.this;
                shareAdapter.setOpenList(adapterPosition, shareAdapter.mFlag);
            }
        });
        button.setOnClickListener(new View.OnClickListener() { // from class: adapter.ShareAdapter.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                ShareAdapter.this.mFlag = !r3.mFlag;
                ShareAdapter shareAdapter = ShareAdapter.this;
                shareAdapter.setOpenList(adapterPosition, shareAdapter.mFlag);
            }
        });
        if (adapterPosition == this.openPosition) {
            if (!this.isOpen) {
                baseViewHolder.setGone(R.id.cancel_share, false);
                button.setBackground(this.context.getResources().getDrawable(R.drawable.controller_down));
            } else {
                baseViewHolder.setGone(R.id.cancel_share, true);
                ((Button) baseViewHolder.getView(R.id.cancel_share)).setOnClickListener(new View.OnClickListener() { // from class: adapter.ShareAdapter.3
                    @Override // android.view.View.OnClickListener
                    public void onClick(View view2) {
                        ShareAdapter.this.onClick.update(adapterPosition);
                    }
                });
                button.setBackground(this.context.getResources().getDrawable(R.drawable.controller_up));
            }
        }
    }

    public void setOpenList(int i, boolean z) {
        this.openPosition = i;
        this.isOpen = z;
        notifyDataSetChanged();
    }
}
