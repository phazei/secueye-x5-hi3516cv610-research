package bean;

import android.os.Bundle;
import android.view.View;
import com.seculink.app.R;

/* JADX INFO: loaded from: classes.dex */
public class TabInfoBean {
    int backgroundRes;
    Bundle bundle;
    Class fragmentClass;
    String tabTag;
    View tabView;

    /* JADX INFO: Access modifiers changed from: private */
    public void setTabTag(String str) {
        this.tabTag = str;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTabView(View view2) {
        this.tabView = view2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFragmentClass(Class cls) {
        this.fragmentClass = cls;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setBackgroundRes(int i) {
        this.backgroundRes = i;
    }

    public String getTabTag() {
        return this.tabTag;
    }

    public View getTabView() {
        return this.tabView;
    }

    public Class getFragmentClass() {
        return this.fragmentClass;
    }

    public Bundle getBundle() {
        return this.bundle;
    }

    public static class Builder {
        int backgroundRes;
        Bundle bundle;
        Class fragmentClass;
        private TabInfoBean tabInfoBean;
        String tabTag;
        View tabView;

        public Builder(String str, View view2, Class cls) {
            this(str, view2, cls, null);
        }

        public Builder(String str, View view2, Class cls, Bundle bundle) {
            this.backgroundRes = -1;
            this.tabTag = str;
            this.tabView = view2;
            this.fragmentClass = cls;
            this.bundle = bundle;
        }

        public Builder setTabTag(String str) {
            this.tabTag = str;
            return this;
        }

        public Builder setTabView(View view2) {
            this.tabView = view2;
            return this;
        }

        public Builder setFragmentClass(Class cls) {
            this.fragmentClass = cls;
            return this;
        }

        public Builder setBundle(Bundle bundle) {
            this.bundle = bundle;
            return this;
        }

        public Builder setBackgroundRes(int i) {
            this.backgroundRes = i;
            return this;
        }

        public TabInfoBean build() {
            this.tabInfoBean = new TabInfoBean();
            this.tabInfoBean.setTabTag(this.tabTag);
            this.tabInfoBean.setBundle(this.bundle);
            this.tabInfoBean.setTabView(this.tabView);
            this.tabInfoBean.setFragmentClass(this.fragmentClass);
            if (this.backgroundRes == -1) {
                this.backgroundRes = R.drawable.selector_tab_background;
                this.tabInfoBean.setBackgroundRes(this.backgroundRes);
            }
            return this.tabInfoBean;
        }
    }
}
