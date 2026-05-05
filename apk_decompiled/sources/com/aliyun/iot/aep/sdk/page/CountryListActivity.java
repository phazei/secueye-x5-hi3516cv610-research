package com.aliyun.iot.aep.sdk.page;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.openaccount.ui.widget.SiderBar;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.framework.R;
import com.aliyun.iot.aep.sdk.framework.config.GlobalConfig;
import com.aliyun.iot.aep.sdk.page.LocateHandler;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.aliyun.iot.link.ui.component.LinkToast;
import com.aliyun.iot.link.ui.component.LoadingCompact;
import com.hjq.permissions.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/* JADX INFO: loaded from: classes2.dex */
public class CountryListActivity extends AppCompatActivity implements View.OnClickListener, LocateHandler.OnLocationListener {
    public static final String TAG = "CountryListActivity";

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    private BaseAdapter f4807a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    private ListView f4808b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private TextView f4809c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private CountrySiderBar f4810d;
    private e e;
    private IoTSmart.Country g;
    private WindowManager i;
    private View j;
    private TextView k;
    private LocateTask l;
    private Button m;
    private ImageView n;
    private HashMap<String, Integer> o;
    private String[] p;
    private ArrayList<IoTSmart.Country> f = new ArrayList<>();
    private boolean h = false;
    private Handler q = new Handler();
    private Handler r = new Handler();

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.sdk_framework_country_list);
        OATitleBar oATitleBar = (OATitleBar) findViewById(R.id.country_title);
        this.m = (Button) findViewById(R.id.btn_select_country);
        this.f4808b = (ListView) findViewById(R.id.country_list);
        this.f4810d = (CountrySiderBar) findViewById(R.id.countryLetterListView);
        oATitleBar.setType(0);
        oATitleBar.setTitle(getString(R.string.region_header_title));
        this.m.setVisibility(0);
        this.m.setOnClickListener(this);
        oATitleBar.setBackgroundColor(-1);
        this.f4808b.setBackgroundColor(-1);
        oATitleBar.setBackClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.page.CountryListActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                CountryListActivity.this.finish();
            }
        });
        this.g = IoTSmart.getCountry();
        LoadingCompact.showLoading(this);
        IoTSmart.getCountryList(new IoTSmart.ICountryListGetCallBack() { // from class: com.aliyun.iot.aep.sdk.page.CountryListActivity.2
            @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
            public void onSucess(final List<IoTSmart.Country> list) {
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.page.CountryListActivity.2.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LoadingCompact.dismissLoading(CountryListActivity.this);
                        List list2 = list;
                        if (list2 != null) {
                            String jSONString = JSON.toJSONString(list2);
                            int i = 0;
                            while (i <= jSONString.length() / 600) {
                                int i2 = i * 600;
                                i++;
                                int length = i * 600;
                                if (length > jSONString.length()) {
                                    length = jSONString.length();
                                }
                                ALog.d(CountryListActivity.TAG, jSONString.substring(i2, length));
                            }
                            CountryListActivity.this.a((List<IoTSmart.Country>) list);
                        }
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
            public void onFail(String str, int i, final String str2) {
                ALog.d(CountryListActivity.TAG, str + i + str2);
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.page.CountryListActivity.2.2
                    @Override // java.lang.Runnable
                    public void run() {
                        LoadingCompact.showLoading(CountryListActivity.this);
                        LinkToast.makeText(CountryListActivity.this, str2);
                    }
                });
            }
        });
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        LoadingCompact.dismissLoading(this);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(List<IoTSmart.Country> list) {
        if (list == null) {
            return;
        }
        this.f.addAll(list);
        Collections.sort(this.f, new Comparator<IoTSmart.Country>() { // from class: com.aliyun.iot.aep.sdk.page.CountryListActivity.3
            @Override // java.util.Comparator
            /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
            public int compare(IoTSmart.Country country, IoTSmart.Country country2) {
                return b.b(country).compareTo(b.b(country2));
            }
        });
        this.f4810d.setInterval(0.0f);
        this.f4810d.setOnTouchingLetterChangedListener(new c());
        this.o = new HashMap<>();
        this.e = new e();
        b();
        this.f4807a = new d(this, this.f);
        this.f4808b.setAdapter((ListAdapter) this.f4807a);
        this.f4808b.setOnItemClickListener(new a());
        a();
    }

    private void a() {
        if (ContextCompat.checkSelfPermission(this, Permission.ACCESS_COARSE_LOCATION) != 0) {
            ActivityCompat.requestPermissions(this, new String[]{Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION}, 1);
        }
        if (!LocationUtil.isLocationEnabled(this)) {
            LocationUtil.remindStartLocateService(this);
        }
        c();
    }

    @Override // com.aliyun.iot.aep.sdk.page.LocateHandler.OnLocationListener
    public void onContinuedLocate(String str) {
        this.k.setText(str);
    }

    @Override // com.aliyun.iot.aep.sdk.page.LocateHandler.OnLocationListener
    @SuppressLint({"SetTextI18n"})
    public void onFailLocate() {
        View view2 = this.j;
        if (view2 != null) {
            this.f4808b.removeHeaderView(view2);
        }
        this.j = View.inflate(this, R.layout.sdk_framework_header_view_location_fail, null);
        LineTextView lineTextView = (LineTextView) this.j.findViewById(R.id.tv_location_again);
        lineTextView.setText(getString(R.string.location_failed) + getString(R.string.location_failed_again));
        ((LinearLayout) this.j.findViewById(R.id.ll_header_fail_back)).setBackgroundColor(getResources().getColor(android.R.color.white));
        ((TextView) this.j.findViewById(R.id.tv_location_fail)).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        lineTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.j.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.page.CountryListActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                if (ContextCompat.checkSelfPermission(CountryListActivity.this, Permission.ACCESS_COARSE_LOCATION) != 0) {
                    ActivityCompat.requestPermissions(CountryListActivity.this, new String[]{Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION}, 1);
                }
                if (!LocationUtil.isLocationEnabled(CountryListActivity.this)) {
                    LocationUtil.remindStartLocateService(CountryListActivity.this);
                }
                CountryListActivity.this.c();
            }
        });
        this.f4808b.addHeaderView(this.j);
    }

    @Override // com.aliyun.iot.aep.sdk.page.LocateHandler.OnLocationListener
    public void onSuccessLocate(final IoTSmart.Country country) {
        View view2 = this.j;
        if (view2 != null) {
            this.f4808b.removeHeaderView(view2);
        }
        this.j = View.inflate(this, R.layout.sdk_framework_header_view, null);
        TextView textView = (TextView) this.j.findViewById(R.id.ilop_pagestart_default_country_name);
        this.n = (ImageView) this.j.findViewById(R.id.code);
        textView.setText(country.areaName);
        this.j.setOnClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.page.CountryListActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                CountryListActivity.this.n.setVisibility(0);
                CountryListActivity.this.g = country;
                if (CountryListActivity.this.f4807a != null) {
                    CountryListActivity.this.f4807a.notifyDataSetChanged();
                }
            }
        });
        this.f4808b.addHeaderView(this.j);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.btn_select_country) {
            this.m.setEnabled(false);
            if (this.g != null) {
                if (GlobalConfig.getInstance().getCountrySelectCallBack() != null) {
                    GlobalConfig.getInstance().getCountrySelectCallBack().onCountrySelect(this.g);
                }
                this.q.postDelayed(new Runnable() { // from class: com.aliyun.iot.aep.sdk.page.CountryListActivity.6
                    @Override // java.lang.Runnable
                    public void run() {
                        CountryListActivity.this.finish();
                    }
                }, 500L);
            }
            this.q.postDelayed(new Runnable() { // from class: com.aliyun.iot.aep.sdk.page.CountryListActivity.7
                @Override // java.lang.Runnable
                public void run() {
                    CountryListActivity.this.m.setEnabled(true);
                }
            }, 300L);
        }
    }

    class a implements AdapterView.OnItemClickListener {
        a() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
            if (i <= 0) {
                return;
            }
            IoTSmart.Country country = (IoTSmart.Country) CountryListActivity.this.f4808b.getAdapter().getItem(i);
            if (CountryListActivity.this.n != null && !Objects.equals(CountryListActivity.this.g, country)) {
                CountryListActivity.this.n.setVisibility(8);
            }
            CountryListActivity.this.g = country;
            CountryListActivity.this.f4807a.notifyDataSetChanged();
        }
    }

    class d extends BaseAdapter {

        /* JADX INFO: renamed from: b, reason: collision with root package name */
        private LayoutInflater f4826b;

        /* JADX INFO: renamed from: c, reason: collision with root package name */
        private List<IoTSmart.Country> f4827c = new ArrayList();

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            a();
            super.notifyDataSetChanged();
        }

        void a() {
            if (CountryListActivity.this.o == null) {
                CountryListActivity.this.o = new HashMap();
            }
            CountryListActivity.this.o.clear();
            CountryListActivity.this.p = new String[this.f4827c.size()];
            for (int i = 0; i < this.f4827c.size(); i++) {
                if (this.f4827c.get(i) != null) {
                    String strA = b.a(this.f4827c.get(i));
                    int i2 = i - 1;
                    if (!(i2 >= 0 ? b.a(this.f4827c.get(i2)) : " ").equals(strA)) {
                        CountryListActivity.this.o.put(strA, Integer.valueOf(i));
                        CountryListActivity.this.p[i] = strA;
                    }
                }
            }
        }

        d(Context context, List<IoTSmart.Country> list) {
            this.f4826b = LayoutInflater.from(context);
            if (list != null) {
                this.f4827c.addAll(list);
            }
            a();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            return this.f4827c.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.f4827c.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view2, ViewGroup viewGroup) {
            a aVar;
            if (view2 == null) {
                view2 = this.f4826b.inflate(R.layout.sdk_framework_list_item, (ViewGroup) null);
                aVar = new a();
                aVar.f4828a = (TextView) view2.findViewById(R.id.alpha);
                aVar.f4829b = (TextView) view2.findViewById(R.id.name);
                aVar.f4830c = (ImageView) view2.findViewById(R.id.code);
                aVar.f4831d = view2.findViewById(R.id.view_item_top_line);
                aVar.e = (RelativeLayout) view2.findViewById(R.id.rl_item);
                view2.setTag(aVar);
            } else {
                aVar = (a) view2.getTag();
            }
            aVar.e.setBackgroundColor(-1);
            aVar.f4828a.setBackgroundColor(Color.parseColor("#FFF6F6F6"));
            aVar.f4828a.setTextColor(Color.parseColor("#FF999999"));
            aVar.f4829b.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            aVar.f4831d.setBackgroundColor(Color.parseColor("#FFF5F5F5"));
            IoTSmart.Country country = this.f4827c.get(i);
            if (country == null) {
                return view2;
            }
            aVar.f4829b.setText(country.areaName);
            if (CountryListActivity.this.g != null && country.domainAbbreviation.equals(CountryListActivity.this.g.domainAbbreviation)) {
                aVar.f4830c.setVisibility(0);
            } else {
                aVar.f4830c.setVisibility(8);
            }
            String strA = b.a(country);
            int i2 = i - 1;
            if (!(i2 >= 0 ? b.a(this.f4827c.get(i2)) : " ").equals(strA)) {
                aVar.f4828a.setVisibility(0);
                aVar.f4828a.setText(strA);
                aVar.f4831d.setVisibility(8);
            } else {
                aVar.f4828a.setVisibility(8);
                aVar.f4831d.setVisibility(0);
            }
            return view2;
        }

        class a {

            /* JADX INFO: renamed from: a, reason: collision with root package name */
            TextView f4828a;

            /* JADX INFO: renamed from: b, reason: collision with root package name */
            TextView f4829b;

            /* JADX INFO: renamed from: c, reason: collision with root package name */
            ImageView f4830c;

            /* JADX INFO: renamed from: d, reason: collision with root package name */
            View f4831d;
            RelativeLayout e;

            private a() {
            }
        }
    }

    private void b() {
        this.f4809c = (TextView) LayoutInflater.from(this).inflate(R.layout.sdk_framework_overlay, (ViewGroup) null);
        this.f4809c.setVisibility(4);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2, 24, -3);
        this.i = (WindowManager) getSystemService("window");
        WindowManager windowManager = this.i;
        if (windowManager != null) {
            windowManager.addView(this.f4809c, layoutParams);
        }
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        TextView textView;
        WindowManager windowManager = this.i;
        if (windowManager != null && (textView = this.f4809c) != null) {
            windowManager.removeViewImmediate(textView);
        }
        Handler handler = this.r;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        LocateTask locateTask = this.l;
        if (locateTask != null) {
            locateTask.stopLocation();
            this.l = null;
        }
        LocationUtil.cancelLocating();
        super.onDestroy();
        this.q.removeCallbacksAndMessages(null);
    }

    class c implements SiderBar.OnTouchingLetterChangedListener {
        private c() {
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.widget.SiderBar.OnTouchingLetterChangedListener
        public void onTouchingLetterChanged(String str) {
            Integer num;
            if (CountryListActivity.this.o.get(str) == null || (num = (Integer) CountryListActivity.this.o.get(str)) == null) {
                return;
            }
            CountryListActivity.this.f4808b.setSelection(num.intValue() + 1);
            CountryListActivity.this.f4809c.setText(CountryListActivity.this.p[num.intValue()]);
            CountryListActivity.this.f4809c.setVisibility(0);
            CountryListActivity.this.r.removeCallbacks(CountryListActivity.this.e);
            CountryListActivity.this.r.postDelayed(CountryListActivity.this.e, 700L);
        }
    }

    class e implements Runnable {
        private e() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CountryListActivity.this.f4809c.setVisibility(8);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void c() {
        View view2 = this.j;
        if (view2 != null) {
            this.f4808b.removeHeaderView(view2);
        }
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(-1, -2);
        this.j = View.inflate(this, R.layout.sdk_framework_header_view, null);
        this.k = (TextView) this.j.findViewById(R.id.ilop_pagestart_default_country_name);
        this.k.setTextColor(Color.parseColor("#FF333333"));
        this.k.setText(R.string.locating);
        this.j.setLayoutParams(layoutParams);
        this.f4808b.addHeaderView(this.j);
        LocationUtil.requestLocation(this);
        ArrayList<IoTSmart.Country> arrayList = this.f;
        if (arrayList != null) {
            this.l = new LocateTask(this, arrayList, this);
            this.l.startLocation();
        }
    }

    static class b {
        private static boolean a() {
            try {
                return IoTSmart.getLanguage().equalsIgnoreCase("zh-CN");
            } catch (Exception unused) {
                return false;
            }
        }

        static String a(@NonNull IoTSmart.Country country) {
            try {
                if (a()) {
                    return country.pinyin.substring(0, 1);
                }
                return country.areaName.substring(0, 1);
            } catch (IndexOutOfBoundsException | NullPointerException unused) {
                return "";
            }
        }

        static String b(@NonNull IoTSmart.Country country) {
            try {
                if (a()) {
                    return country.pinyin;
                }
                return country.areaName;
            } catch (NullPointerException unused) {
                return "";
            }
        }
    }
}
