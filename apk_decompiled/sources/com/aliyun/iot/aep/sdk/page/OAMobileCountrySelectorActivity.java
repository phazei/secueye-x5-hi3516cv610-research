package com.aliyun.iot.aep.sdk.page;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.model.CountrySort;
import com.alibaba.sdk.android.openaccount.ui.widget.SiderBar;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.framework.R;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.aliyun.iot.link.ui.component.LinkToast;
import com.aliyun.iot.link.ui.component.LoadingCompact;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.eclipse.paho.client.mqttv3.MqttTopic;

/* JADX INFO: loaded from: classes2.dex */
public class OAMobileCountrySelectorActivity extends AppCompatActivity {

    /* JADX INFO: renamed from: a, reason: collision with root package name */
    protected CountrySiderBar f4849a;

    /* JADX INFO: renamed from: b, reason: collision with root package name */
    protected GetCountryNameSort f4850b;

    /* JADX INFO: renamed from: c, reason: collision with root package name */
    private ListView f4851c;

    /* JADX INFO: renamed from: d, reason: collision with root package name */
    private CountryCodeAdapter f4852d;
    private a e = new a();
    private SharedPreferences f;

    public static void start(Activity activity2) {
        activity2.startActivityForResult(new Intent(activity2, (Class<?>) OAMobileCountrySelectorActivity.class), RequestCode.MOBILE_COUNTRY_SELECTOR_REQUEST);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.sdk_framework_ali_sdk_openaccount_mobile_country_selector2);
        this.f = getSharedPreferences("ilop_sp", 0);
        this.f4851c = (ListView) findViewById(R.id.country_list);
        this.f4849a = (CountrySiderBar) findViewById(R.id.country_sidebar);
        this.f4850b = new GetCountryNameSort();
        this.f4851c.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.aliyun.iot.aep.sdk.page.OAMobileCountrySelectorActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                CountrySort countrySort = (CountrySort) OAMobileCountrySelectorActivity.this.f4852d.getItem(i);
                OAMobileCountrySelectorActivity.this.f.edit().putString("phoneCode", countrySort.code).apply();
                Intent intent = new Intent();
                intent.putExtra("countryCode", countrySort.code);
                OAMobileCountrySelectorActivity.this.setResult(-1, intent);
                OAMobileCountrySelectorActivity.this.finish();
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main);
        OATitleBar oATitleBar = (OATitleBar) findViewById(R.id.oat_title);
        oATitleBar.setType(0);
        oATitleBar.setTitle(getString(R.string.account_select_country_code));
        linearLayout.setBackgroundColor(-1);
        oATitleBar.setBackClickListener(new View.OnClickListener() { // from class: com.aliyun.iot.aep.sdk.page.OAMobileCountrySelectorActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                OAMobileCountrySelectorActivity.this.finish();
            }
        });
        this.f4849a.setInterval(0.0f);
        this.f4849a.setOnTouchingLetterChangedListener(new SiderBar.OnTouchingLetterChangedListener() { // from class: com.aliyun.iot.aep.sdk.page.OAMobileCountrySelectorActivity.3
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.SiderBar.OnTouchingLetterChangedListener
            public void onTouchingLetterChanged(String str) {
                int positionForSection;
                if (OAMobileCountrySelectorActivity.this.f4852d == null || (positionForSection = OAMobileCountrySelectorActivity.this.f4852d.getPositionForSection(str.charAt(0))) == -1) {
                    return;
                }
                OAMobileCountrySelectorActivity.this.f4851c.setSelection(positionForSection);
            }
        });
        LoadingCompact.showLoading(this);
        IoTSmart.getCountryList(new IoTSmart.ICountryListGetCallBack() { // from class: com.aliyun.iot.aep.sdk.page.OAMobileCountrySelectorActivity.4
            @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
            public void onSucess(final List<IoTSmart.Country> list) {
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.page.OAMobileCountrySelectorActivity.4.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LoadingCompact.dismissLoading(OAMobileCountrySelectorActivity.this);
                        List list2 = list;
                        if (list2 == null || list2.isEmpty()) {
                            return;
                        }
                        OAMobileCountrySelectorActivity.this.b((List<IoTSmart.Country>) list);
                    }
                });
            }

            @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
            public void onFail(String str, int i, final String str2) {
                ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: com.aliyun.iot.aep.sdk.page.OAMobileCountrySelectorActivity.4.2
                    @Override // java.lang.Runnable
                    public void run() {
                        LoadingCompact.dismissLoading(OAMobileCountrySelectorActivity.this);
                        LinkToast.makeText(OAMobileCountrySelectorActivity.this, str2);
                    }
                });
            }
        });
    }

    class a implements Comparator<CountrySort> {
        a() {
        }

        @Override // java.util.Comparator
        /* JADX INFO: renamed from: a, reason: merged with bridge method [inline-methods] */
        public int compare(CountrySort countrySort, CountrySort countrySort2) {
            if (countrySort.sortLetters.equals("@") || countrySort2.sortLetters.equals(MqttTopic.MULTI_LEVEL_WILDCARD)) {
                return -1;
            }
            if (countrySort.sortLetters.equals(MqttTopic.MULTI_LEVEL_WILDCARD) || countrySort2.sortLetters.equals("@")) {
                return 1;
            }
            return countrySort.sortLetters.compareTo(countrySort2.sortLetters);
        }
    }

    protected List<CountrySort> a(List<CountrySort> list) {
        ArrayList arrayList = new ArrayList();
        try {
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(a("CHN", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(a("FRA", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(a("DEU", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(a("JPN", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(a("KOR", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(a("RUS", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(a("ESP", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(a("GBR", list))));
        } catch (Exception unused) {
        }
        return arrayList;
    }

    private CountrySort a(String str, List<CountrySort> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).englishName.equals(str)) {
                return list.get(i);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void b(List<IoTSmart.Country> list) {
        boolean zEqualsIgnoreCase;
        ArrayList arrayList = new ArrayList();
        try {
            zEqualsIgnoreCase = IoTSmart.getLanguage().equalsIgnoreCase("zh-CN");
        } catch (Exception unused) {
            zEqualsIgnoreCase = false;
        }
        for (int i = 0; i < list.size(); i++) {
            IoTSmart.Country country = list.get(i);
            CountrySort countrySort = new CountrySort();
            countrySort.code = country.code;
            if (zEqualsIgnoreCase) {
                String sortLetterBySortKey = this.f4850b.getSortLetterBySortKey(country.pinyin);
                if (sortLetterBySortKey == null) {
                    sortLetterBySortKey = this.f4850b.getSortLetter(country.areaName);
                }
                countrySort.sortLetters = sortLetterBySortKey;
                countrySort.displayName = country.areaName;
            } else {
                countrySort.sortLetters = this.f4850b.getSortLetterBySortKey(country.areaName);
                countrySort.displayName = country.areaName;
            }
            countrySort.englishName = country.isoCode;
            arrayList.add(countrySort);
        }
        List<CountrySort> listA = a(arrayList);
        Collections.sort(arrayList, this.e);
        arrayList.addAll(0, listA);
        this.f4852d = new CountryCodeAdapter(this, arrayList);
        this.f4851c.setAdapter((ListAdapter) this.f4852d);
    }

    @Override // androidx.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        LoadingCompact.dismissLoading(this);
    }
}
