package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.sdk.android.openaccount.ui.model.CountrySort;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.seculink.app.R;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes.dex */
public class CountryCodeAdapter extends android.widget.BaseAdapter {
    private Context mContext;
    private List<CountrySort> mList;

    public static class ViewHolder {
        View country_code_hint;
        TextView country_name;
        TextView country_number;
        LinearLayout country_sortName;
        TextView country_sortName_text;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public CountryCodeAdapter(Context context, List<CountrySort> list) {
        this.mContext = context;
        if (list == null) {
            this.mList = new ArrayList();
        } else {
            this.mList = list;
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.mList.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.mList.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view2, ViewGroup viewGroup) {
        View viewInflate;
        ViewHolder viewHolder;
        CountrySort countrySort = this.mList.get(i);
        if (view2 == null) {
            viewHolder = new ViewHolder();
            viewInflate = LayoutInflater.from(this.mContext).inflate(R.layout.ali_sdk_openaccount_mobile_country_item2, (ViewGroup) null);
            viewHolder.country_sortName = (LinearLayout) viewInflate.findViewById(ResourceUtils.getRId(this.mContext, "country_catalog"));
            viewHolder.country_sortName_text = (TextView) viewInflate.findViewById(ResourceUtils.getRId(this.mContext, "country_catalog_text"));
            viewHolder.country_name = (TextView) viewInflate.findViewById(ResourceUtils.getRId(this.mContext, "country_name"));
            viewHolder.country_number = (TextView) viewInflate.findViewById(ResourceUtils.getRId(this.mContext, "country_code"));
            viewHolder.country_code_hint = viewInflate.findViewById(ResourceUtils.getRId(this.mContext, "country_code_hint"));
            viewInflate.setTag(viewHolder);
        } else {
            viewInflate = view2;
            viewHolder = (ViewHolder) view2.getTag();
        }
        if (i == getPositionForSection(getSectionForPosition(i))) {
            viewHolder.country_sortName.setVisibility(0);
            viewHolder.country_code_hint.setVisibility(4);
            if (WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD.equals(countrySort.sortLetters)) {
                viewHolder.country_sortName_text.setText(R.string.account_hot_country);
            } else {
                viewHolder.country_sortName_text.setText(countrySort.sortLetters);
            }
        } else {
            viewHolder.country_code_hint.setVisibility(0);
            viewHolder.country_sortName.setVisibility(8);
        }
        viewHolder.country_name.setText(this.mList.get(i).displayName);
        viewHolder.country_number.setText(this.mList.get(i).code);
        return viewInflate;
    }

    private int getSectionForPosition(int i) {
        return this.mList.get(i).sortLetters.charAt(0);
    }

    public int getPositionForSection(int i) {
        if (i == 42) {
            return 0;
        }
        for (int i2 = 0; i2 < getCount(); i2++) {
            if (this.mList.get(i2).sortLetters.toUpperCase(Locale.CHINESE).charAt(0) == i) {
                return i2;
            }
        }
        return -1;
    }
}
