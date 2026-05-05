package adapter;

import android.view.View;
import bean.TabInfoBean;
import fragment.MyFragmentTabLayout;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: loaded from: classes.dex */
public class DefaultFragmentTabAdapter implements MyFragmentTabLayout.FragmentTabLayoutAdapter {
    private List<Integer> drawables;
    private List<String> fragmentTag;
    private List<Class> fragmentclass;

    @Override // fragment.MyFragmentTabLayout.FragmentTabLayoutAdapter
    public View createView(int i) {
        return null;
    }

    @Override // fragment.MyFragmentTabLayout.FragmentTabLayoutAdapter
    public void onClick(int i) {
    }

    public DefaultFragmentTabAdapter(List<Class> list, List<String> list2, List<Integer> list3) {
        this.fragmentclass = new ArrayList();
        this.fragmentTag = new ArrayList();
        this.drawables = new ArrayList();
        this.fragmentclass = list;
        this.fragmentTag = list2;
        this.drawables = list3;
    }

    @Override // fragment.MyFragmentTabLayout.FragmentTabLayoutAdapter
    public int getCount() {
        return this.fragmentTag.size();
    }

    @Override // fragment.MyFragmentTabLayout.FragmentTabLayoutAdapter
    public TabInfoBean getTabInfo(int i) {
        return new TabInfoBean.Builder(this.fragmentTag.get(i), createView(i), this.fragmentclass.get(i)).build();
    }
}
