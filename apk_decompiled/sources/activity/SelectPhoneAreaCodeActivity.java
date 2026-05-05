package activity;

import adapter.SelectPhoneAreaCodeAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import bean.AreaCodeModel;
import com.bigkoo.quicksidebar.QuickSideBarTipsView;
import com.bigkoo.quicksidebar.QuickSideBarView;
import com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener;
import com.seculink.app.R;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import tools.Utils;
import view.TitleView;

/* JADX INFO: loaded from: classes.dex */
public class SelectPhoneAreaCodeActivity extends CommonActivity implements OnQuickSideBarTouchListener {
    public static final int REQUEST_CODE_AREA_CODE = 4369;

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    private SelectPhoneAreaCodeAdapter f1584adapter;
    private List<AreaCodeModel> datalist;
    TitleView fl_titlebar;
    private LinearLayoutManager layoutManager;
    RelativeLayout layout_main;
    QuickSideBarTipsView quickSideBarTipsView;
    QuickSideBarView quickSideBarView;
    private List<String> sections = new ArrayList();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_select_phone_area_code;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.quickSideBarView = (QuickSideBarView) findViewById(R.id.quickSideBarView);
        this.quickSideBarView.setOnQuickSideBarTouchListener(this);
        this.layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.quickSideBarTipsView = (QuickSideBarTipsView) findViewById(R.id.quickSideBarTipsView);
        this.fl_titlebar = (TitleView) findViewById(R.id.fl_titlebar);
        this.datalist = Utils.readAreaCodeList(this);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        this.layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(this.layoutManager);
        sortList(this.datalist);
        this.f1584adapter = new SelectPhoneAreaCodeAdapter();
        this.f1584adapter.setDataList(this.datalist);
        this.f1584adapter.setStickHeaderColor(R.color.colorAccent);
        recyclerView.setAdapter(this.f1584adapter);
        recyclerView.addItemDecoration(new StickyRecyclerHeadersDecoration(this.f1584adapter));
        this.f1584adapter.setOnItemClickListener(new SelectPhoneAreaCodeAdapter.OnItemClickListener() { // from class: activity.SelectPhoneAreaCodeActivity.1
            @Override // adapter.SelectPhoneAreaCodeAdapter.OnItemClickListener
            public void onItemClick(AreaCodeModel areaCodeModel) {
                Intent intent = new Intent();
                intent.putExtra("areacode", areaCodeModel);
                SelectPhoneAreaCodeActivity.this.setResult(-1, intent);
                SelectPhoneAreaCodeActivity.this.finish();
            }
        });
        this.fl_titlebar.setOnViewClick(new TitleView.OnViewClick() { // from class: activity.SelectPhoneAreaCodeActivity.2
            @Override // view.TitleView.OnViewClick
            public void OnRightClick(View view2) {
            }

            @Override // view.TitleView.OnViewClick
            public void OnLeftClick(View view2) {
                SelectPhoneAreaCodeActivity.this.finish();
            }
        });
    }

    private void sortList(List<AreaCodeModel> list) {
        Collections.sort(list, new Comparator<AreaCodeModel>() { // from class: activity.SelectPhoneAreaCodeActivity.3
            @Override // java.util.Comparator
            public int compare(AreaCodeModel areaCodeModel, AreaCodeModel areaCodeModel2) {
                return Utils.getFirstPinYin(areaCodeModel.getName()).compareTo(Utils.getFirstPinYin(areaCodeModel2.getName()));
            }
        });
        this.sections.clear();
        Iterator<AreaCodeModel> it = list.iterator();
        while (it.hasNext()) {
            String firstPinYin = Utils.getFirstPinYin(it.next().getName());
            if (!this.sections.contains(firstPinYin)) {
                this.sections.add(firstPinYin);
            }
        }
        this.quickSideBarView.setLetters(this.sections);
    }

    @Override // com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener
    public void onLetterChanged(String str, int i, float f) {
        this.quickSideBarTipsView.setText(str, i, f);
        this.layoutManager.scrollToPositionWithOffset(index(str), 0);
    }

    private int index(String str) {
        for (int i = 0; i < this.datalist.size(); i++) {
            if (TextUtils.equals(str, Utils.getFirstPinYin(this.datalist.get(i).getName()))) {
                return i;
            }
        }
        return 0;
    }

    @Override // com.bigkoo.quicksidebar.listener.OnQuickSideBarTouchListener
    public void onLetterTouching(boolean z) {
        this.quickSideBarTipsView.setVisibility(z ? 0 : 8);
    }
}
