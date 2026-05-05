package view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.seculink.app.R;
import java.util.List;

/* JADX INFO: loaded from: classes5.dex */
public class MySpinnerView extends LinearLayout {
    private boolean canPullList;
    private LinearLayout ll_root;
    private LinearLayout ll_title;
    private MyAdapter myAdapter;
    private onClickListener onClickListener;
    private onItemSelectedListener onItemSelectedListener;
    private MyRecyclerView recyclerView;
    private int selectPos;
    private TextView tv_name;

    public interface onClickListener {
        void onClick();
    }

    public interface onItemSelectedListener {
        void onItemSelected(String str, int i);
    }

    public MySpinnerView(Context context) {
        super(context);
        this.selectPos = 0;
        this.canPullList = true;
        initView();
    }

    public MySpinnerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.selectPos = 0;
        this.canPullList = true;
        initView();
    }

    public MySpinnerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.selectPos = 0;
        this.canPullList = true;
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.item_spinner, (ViewGroup) this, true);
        this.tv_name = (TextView) findViewById(R.id.tv_name);
        this.ll_root = (LinearLayout) findViewById(R.id.ll_root);
        this.ll_title = (LinearLayout) findViewById(R.id.ll_title);
        this.recyclerView = (MyRecyclerView) findViewById(R.id.myrecyclerview);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.myAdapter = new MyAdapter(R.layout.item_spinner_each);
        this.myAdapter.bindToRecyclerView(this.recyclerView);
        this.myAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: view.MySpinnerView.1
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view2, int i) {
                MySpinnerView.this.selectPos = i;
                String str = MySpinnerView.this.myAdapter.getData().get(i);
                MySpinnerView.this.tv_name.setText(str);
                if (MySpinnerView.this.onItemSelectedListener != null) {
                    MySpinnerView.this.onItemSelectedListener.onItemSelected(str, i);
                }
                MySpinnerView.this.recyclerView.setVisibility(8);
            }
        });
        this.ll_root.setOnClickListener(new View.OnClickListener() { // from class: view.MySpinnerView.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (MySpinnerView.this.canPullList && MySpinnerView.this.myAdapter.getData().size() > 0) {
                    MySpinnerView.this.recyclerView.setVisibility(MySpinnerView.this.recyclerView.getVisibility() == 8 ? 0 : 8);
                }
                if (MySpinnerView.this.onClickListener != null) {
                    MySpinnerView.this.onClickListener.onClick();
                }
            }
        });
    }

    public void setSelectPos(int i) {
        if (i >= this.myAdapter.getData().size()) {
            return;
        }
        this.selectPos = i;
        this.tv_name.setText(this.myAdapter.getData().get(i));
    }

    public void setLLRootShow(boolean z) {
        this.ll_title.setVisibility(z ? 0 : 8);
    }

    public void setPullListShow(boolean z) {
        if (!this.canPullList || this.myAdapter.getData().size() <= 0) {
            return;
        }
        this.recyclerView.setVisibility(z ? 0 : 8);
    }

    public void setText(String str) {
        this.tv_name.setText(str);
    }

    public String getText() {
        return this.tv_name.getText().toString();
    }

    public void setCanPullList(boolean z) {
        this.canPullList = z;
    }

    public int getSelectPos() {
        return this.selectPos;
    }

    public List<String> getData() {
        return this.myAdapter.getData();
    }

    public void setData(List<String> list) {
        this.myAdapter.replaceData(list);
        if (list.size() <= 0) {
            this.recyclerView.setVisibility(8);
        } else {
            this.recyclerView.setVisibility(8);
            this.tv_name.setText(list.get(0));
        }
    }

    public void notifyDataSetChanged() {
        this.myAdapter.notifyDataSetChanged();
    }

    class MyAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public MyAdapter(int i) {
            super(i);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.chad.library.adapter.base.BaseQuickAdapter
        public void convert(BaseViewHolder baseViewHolder, String str) {
            ((TextView) baseViewHolder.getView(R.id.tv_name)).setText(str);
        }
    }

    public void setOnItemSelectedListener(onItemSelectedListener onitemselectedlistener) {
        this.onItemSelectedListener = onitemselectedlistener;
    }

    public void setOnClickListener(onClickListener onclicklistener) {
        this.onClickListener = onclicklistener;
    }
}
