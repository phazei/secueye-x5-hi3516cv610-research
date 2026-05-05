package fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTabHost;
import bean.TabInfoBean;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes4.dex */
public class MyFragmentTabLayout extends FragmentTabHost {
    private Drawable dividerDrawable;
    private FragmentTabHost fragmentTabHost;
    private FragmentTabLayoutAdapter fragmentTabLayoutAdapter;

    public interface FragmentTabLayoutAdapter {
        View createView(int i);

        int getCount();

        TabInfoBean getTabInfo(int i);

        void onClick(int i);
    }

    private void readAttrs(Context context, AttributeSet attributeSet) {
    }

    public MyFragmentTabLayout(Context context) {
        super(context);
        init();
    }

    public MyFragmentTabLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        readAttrs(context, attributeSet);
        init();
    }

    private void init() {
        this.fragmentTabHost = (FragmentTabHost) LayoutInflater.from(getContext()).inflate(R.layout.myfragment_tab_layout, (ViewGroup) this, true).findViewById(android.R.id.tabhost);
        this.dividerDrawable = null;
    }

    public void setDividerDrawable(Drawable drawable) {
        this.dividerDrawable = drawable;
    }

    public MyFragmentTabLayout init(FragmentManager fragmentManager) {
        this.fragmentTabHost.setup(getContext(), fragmentManager, android.R.id.tabcontent);
        return this;
    }

    public MyFragmentTabLayout setFragmentTabLayoutAdapter(FragmentTabLayoutAdapter fragmentTabLayoutAdapter) {
        this.fragmentTabLayoutAdapter = fragmentTabLayoutAdapter;
        return this;
    }

    public MyFragmentTabLayout creat() {
        if (this.fragmentTabLayoutAdapter == null) {
            return null;
        }
        for (int i = 0; i < this.fragmentTabLayoutAdapter.getCount(); i++) {
            TabInfoBean tabInfo = this.fragmentTabLayoutAdapter.getTabInfo(i);
            this.fragmentTabHost.addTab(this.fragmentTabHost.newTabSpec(tabInfo.getTabTag()).setIndicator(tabInfo.getTabView()), tabInfo.getFragmentClass(), tabInfo.getBundle());
            this.fragmentTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() { // from class: fragment.MyFragmentTabLayout.1
                @Override // android.widget.TabHost.OnTabChangeListener
                public void onTabChanged(String str) {
                    MyFragmentTabLayout.this.fragmentTabLayoutAdapter.onClick(MyFragmentTabLayout.this.fragmentTabHost.getCurrentTab());
                }
            });
        }
        return this;
    }

    public FragmentTabHost getFragmentTabHost() {
        return this.fragmentTabHost;
    }

    public void setFragmentTabHost(FragmentTabHost fragmentTabHost) {
        this.fragmentTabHost = fragmentTabHost;
    }
}
