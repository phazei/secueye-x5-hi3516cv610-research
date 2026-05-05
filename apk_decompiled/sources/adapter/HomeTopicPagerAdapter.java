package adapter;

import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public class HomeTopicPagerAdapter extends PagerAdapter {
    private ArrayList<RecyclerView> mList;

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(@NonNull View view2, @NonNull Object obj) {
        return view2 == obj;
    }

    public HomeTopicPagerAdapter(ArrayList<RecyclerView> arrayList) {
        this.mList = arrayList;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        if (this.mList.isEmpty()) {
            return 0;
        }
        return this.mList.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
        viewGroup.removeView(this.mList.get(i));
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        viewGroup.addView(this.mList.get(i));
        return this.mList.get(i);
    }
}
