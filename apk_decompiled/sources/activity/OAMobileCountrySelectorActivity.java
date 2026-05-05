package activity;

import activity.OAMobileCountrySelectorActivity;
import adapter.CountryCodeAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.alibaba.sdk.android.openaccount.ui.RequestCode;
import com.alibaba.sdk.android.openaccount.ui.model.CountrySort;
import com.alibaba.sdk.android.openaccount.ui.widget.SiderBar;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.aliyun.iot.link.ui.component.LoadingCompact;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import tools.GetCountryNameSort;
import tools.LanguageUtil;
import view.OASiderBar;

/* JADX INFO: loaded from: classes.dex */
public class OAMobileCountrySelectorActivity extends CommonActivity {
    public static final String TAG = "CountrySelector";

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    private CountryCodeAdapter f1579adapter;
    protected GetCountryNameSort countryChangeUtil;
    private CountryComparator countryComparator = new CountryComparator();
    private ListView mCountryListView;
    protected OASiderBar mSiderBar;
    private SharedPreferences sp;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.ali_sdk_openaccount_mobile_country_selector2;
    }

    public static void start(Activity activity2) {
        activity2.startActivityForResult(new Intent(activity2, (Class<?>) OAMobileCountrySelectorActivity.class), RequestCode.MOBILE_COUNTRY_SELECTOR_REQUEST);
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.sp = getSharedPreferences("ilop_sp", 0);
        this.mCountryListView = (ListView) findViewById(R.id.country_list);
        this.mSiderBar = (OASiderBar) findViewById(R.id.country_sidebar);
        this.countryChangeUtil = new GetCountryNameSort();
        this.mCountryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: activity.-$$Lambda$OAMobileCountrySelectorActivity$szEcdbGmafc6ZeJ1rCTMomOXZ48
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view2, int i, long j) {
                OAMobileCountrySelectorActivity.lambda$initWidget$0(this.f$0, adapterView, view2, i, j);
            }
        });
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.main);
        setEdgeToEdge(linearLayout);
        findViewById(R.id.fl_titlebar).setOnClickListener(new View.OnClickListener() { // from class: activity.OAMobileCountrySelectorActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                OAMobileCountrySelectorActivity.this.finish();
            }
        });
        linearLayout.setBackgroundColor(-1);
        this.mSiderBar.setInterval(0.0f);
        this.mSiderBar.setOnTouchingLetterChangedListener(new SiderBar.OnTouchingLetterChangedListener() { // from class: activity.-$$Lambda$OAMobileCountrySelectorActivity$QwxLj1I0qHoMAAhPJZEra01hOVU
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.SiderBar.OnTouchingLetterChangedListener
            public final void onTouchingLetterChanged(String str) {
                OAMobileCountrySelectorActivity.lambda$initWidget$1(this.f$0, str);
            }
        });
        LoadingCompact.showLoading(this);
        IoTSmart.getCountryList(new AnonymousClass2());
    }

    public static /* synthetic */ void lambda$initWidget$0(OAMobileCountrySelectorActivity oAMobileCountrySelectorActivity, AdapterView adapterView, View view2, int i, long j) {
        CountrySort countrySort = (CountrySort) oAMobileCountrySelectorActivity.f1579adapter.getItem(i);
        oAMobileCountrySelectorActivity.sp.edit().putString("phoneCode", countrySort.code).apply();
        Intent intent = new Intent();
        intent.putExtra("countryCode", countrySort.code);
        oAMobileCountrySelectorActivity.setResult(-1, intent);
        oAMobileCountrySelectorActivity.finish();
    }

    public static /* synthetic */ void lambda$initWidget$1(OAMobileCountrySelectorActivity oAMobileCountrySelectorActivity, String str) {
        int positionForSection;
        CountryCodeAdapter countryCodeAdapter = oAMobileCountrySelectorActivity.f1579adapter;
        if (countryCodeAdapter == null || (positionForSection = countryCodeAdapter.getPositionForSection(str.charAt(0))) == -1) {
            return;
        }
        oAMobileCountrySelectorActivity.mCountryListView.setSelection(positionForSection);
    }

    /* JADX INFO: renamed from: activity.OAMobileCountrySelectorActivity$2, reason: invalid class name */
    class AnonymousClass2 implements IoTSmart.ICountryListGetCallBack {
        AnonymousClass2() {
        }

        @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
        public void onSucess(final List<IoTSmart.Country> list) {
            ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: activity.-$$Lambda$OAMobileCountrySelectorActivity$2$Obirwx-uy-nNe8rh5S57ttEnW_Y
                @Override // java.lang.Runnable
                public final void run() {
                    OAMobileCountrySelectorActivity.AnonymousClass2.lambda$onSucess$0(this.f$0, list);
                }
            });
        }

        public static /* synthetic */ void lambda$onSucess$0(AnonymousClass2 anonymousClass2, List list) {
            LoadingCompact.dismissLoading(OAMobileCountrySelectorActivity.this);
            if (list != null && !list.isEmpty()) {
                OAMobileCountrySelectorActivity.this.asyncCountryList(list);
                return;
            }
            ALog.d(OAMobileCountrySelectorActivity.TAG, "load country list failed");
            LoadingCompact.dismissLoading(OAMobileCountrySelectorActivity.this);
            Toast.makeText(OAMobileCountrySelectorActivity.this, "Failed to load list data from remote", 0).show();
        }

        @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
        public void onFail(String str, int i, String str2) {
            ALog.d(OAMobileCountrySelectorActivity.TAG, "load country list failed");
            LoadingCompact.dismissLoading(OAMobileCountrySelectorActivity.this);
            Toast.makeText(OAMobileCountrySelectorActivity.this, str2, 0).show();
        }
    }

    @Override // android.app.Activity, android.view.ContextThemeWrapper, android.content.ContextWrapper
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(LanguageUtil.attachBaseContext(context));
    }

    private class CountryComparator implements Comparator<CountrySort> {
        CountryComparator() {
        }

        @Override // java.util.Comparator
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

    protected List<CountrySort> getHot(List<CountrySort> list) {
        ArrayList arrayList = new ArrayList();
        try {
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(getCountryForCode("CHN", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(getCountryForCode("FRA", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(getCountryForCode("DEU", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(getCountryForCode("JPN", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(getCountryForCode("KOR", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(getCountryForCode("RUS", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(getCountryForCode("ESP", list))));
            arrayList.add(CountrySort.getCountryHot((CountrySort) Objects.requireNonNull(getCountryForCode("GBR", list))));
        } catch (Exception unused) {
        }
        return arrayList;
    }

    private CountrySort getCountryForCode(String str, List<CountrySort> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).englishName.equals(str)) {
                return list.get(i);
            }
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void asyncCountryList(List<IoTSmart.Country> list) {
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
                String sortLetterBySortKey = this.countryChangeUtil.getSortLetterBySortKey(country.pinyin);
                if (sortLetterBySortKey == null) {
                    sortLetterBySortKey = this.countryChangeUtil.getSortLetter(country.areaName);
                }
                countrySort.sortLetters = sortLetterBySortKey;
                countrySort.displayName = country.areaName;
            } else {
                countrySort.sortLetters = this.countryChangeUtil.getSortLetterBySortKey(country.areaName);
                countrySort.displayName = country.areaName;
            }
            countrySort.englishName = country.isoCode;
            arrayList.add(countrySort);
        }
        List<CountrySort> hot = getHot(arrayList);
        Collections.sort(arrayList, this.countryComparator);
        arrayList.addAll(0, hot);
        this.f1579adapter = new CountryCodeAdapter(this, arrayList);
        this.mCountryListView.setAdapter((ListAdapter) this.f1579adapter);
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        LoadingCompact.dismissLoading(this);
    }
}
