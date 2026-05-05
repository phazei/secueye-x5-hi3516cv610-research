package com.aliyun.iot.aep.sdk.page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.alibaba.sdk.android.openaccount.ui.model.CountrySort;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;
import com.aliyun.iot.aep.sdk.framework.R;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* JADX INFO: loaded from: classes2.dex */
public class CountryCodeAdapter extends BaseAdapter {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private List<CountrySort> f4801a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private Context f4802b;

    public static class ViewHolder {

        /* JADX INFO: renamed from: a, reason: collision with root package name */
        LinearLayout f4803a;

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        TextView f4804b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        TextView f4805c;

        /* JADX INFO: renamed from: d, reason: collision with root package name */
        TextView f4806d;
        View e;
    }

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public CountryCodeAdapter(Context context, List<CountrySort> list) {
        this.f4802b = context;
        if (list == null) {
            this.f4801a = new ArrayList();
        } else {
            this.f4801a = list;
        }
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.f4801a.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.f4801a.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view2, ViewGroup viewGroup) {
        View viewInflate;
        ViewHolder viewHolder;
        CountrySort countrySort = this.f4801a.get(i);
        if (view2 == null) {
            viewHolder = new ViewHolder();
            viewInflate = LayoutInflater.from(this.f4802b).inflate(R.layout.sdk_framework_ali_sdk_openaccount_mobile_country_item2, (ViewGroup) null);
            viewHolder.f4803a = (LinearLayout) viewInflate.findViewById(ResourceUtils.getRId(this.f4802b, "country_catalog"));
            viewHolder.f4804b = (TextView) viewInflate.findViewById(ResourceUtils.getRId(this.f4802b, "country_catalog_text"));
            viewHolder.f4805c = (TextView) viewInflate.findViewById(ResourceUtils.getRId(this.f4802b, "country_name"));
            viewHolder.f4806d = (TextView) viewInflate.findViewById(ResourceUtils.getRId(this.f4802b, "country_code"));
            viewHolder.e = viewInflate.findViewById(ResourceUtils.getRId(this.f4802b, "country_code_hint"));
            viewInflate.setTag(viewHolder);
        } else {
            viewInflate = view2;
            viewHolder = (ViewHolder) view2.getTag();
        }
        if (i == getPositionForSection(a(i))) {
            viewHolder.f4803a.setVisibility(0);
            viewHolder.e.setVisibility(4);
            if (WebSocketServerHandshaker.SUB_PROTOCOL_WILDCARD.equals(countrySort.sortLetters)) {
                viewHolder.f4804b.setText(R.string.account_hot_country);
            } else {
                viewHolder.f4804b.setText(countrySort.sortLetters);
            }
        } else {
            viewHolder.e.setVisibility(0);
            viewHolder.f4803a.setVisibility(8);
        }
        viewHolder.f4805c.setText(this.f4801a.get(i).displayName);
        viewHolder.f4806d.setText(this.f4801a.get(i).code);
        return viewInflate;
    }

    private int a(int i) {
        return this.f4801a.get(i).sortLetters.charAt(0);
    }

    public int getPositionForSection(int i) {
        if (i == 42) {
            return 0;
        }
        for (int i2 = 0; i2 < getCount(); i2++) {
            if (this.f4801a.get(i2).sortLetters.toUpperCase(Locale.CHINESE).charAt(0) == i) {
                return i2;
            }
        }
        return -1;
    }
}
