package activity;

import activity.CountryListActivity;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.openaccount.ui.widget.SiderBar;
import com.aliyun.alink.linksdk.tools.ALog;
import com.aliyun.iot.aep.sdk.IoTSmart;
import com.aliyun.iot.aep.sdk.framework.AApplication;
import com.aliyun.iot.aep.sdk.login.ILoginCallback;
import com.aliyun.iot.aep.sdk.login.ILogoutCallback;
import com.aliyun.iot.aep.sdk.login.LoginBusiness;
import com.aliyun.iot.aep.sdk.threadpool.ThreadPool;
import com.aliyun.iot.link.ui.component.LinkToast;
import com.aliyun.iot.link.ui.component.LoadingCompact;
import com.google.android.material.button.MaterialButton;
import com.hjq.permissions.Permission;
import com.huawei.hms.push.AttributionReporter;
import com.seculink.app.R;
import config.AppConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import sdk.DemoApplication;
import sdk.SDKInitHelper;
import tools.LocateHandler;
import tools.LocateTask;
import tools.LocationUtil;
import tools.ScreenUtils;
import tools.SharePreferenceManager;
import view.LineTextView;
import view.OASiderBar;

/* JADX INFO: loaded from: classes.dex */
public class CountryListActivity extends CommonActivity implements LocateHandler.OnLocationListener, View.OnClickListener {
    private static final int ACCESS_COARSE_LOCATION1 = 1;
    public static final String TAG = "sinyuk";
    public static final String URL = "app://aliyun.iot.aep.demo/page/country/select";

    /* JADX INFO: renamed from: adapter, reason: collision with root package name */
    private BaseAdapter f1567adapter;
    private HashMap<String, Integer> alphaIndexer;
    private MaterialButton btnSelectCountry;
    private TextView defaultCountryName;
    private View headerView;
    LinearLayout layout_main;
    private LocateTask locateTask;
    private ListView mCountryList;
    private IoTSmart.Country mSelectedCountry;
    private ImageView mSelectedView;
    private int mSuspensionHeight;
    private TextView overlay;
    private OverlayThread overlayThread;
    private String[] sections;
    private SharedPreferences sharedPreferences;
    private OASiderBar siderBar;
    private WindowManager windowManager;
    private ArrayList<IoTSmart.Country> mCountries = new ArrayList<>();
    private boolean isChinaFormer = false;
    private int mCurrentPosition = 0;
    private IoTSmart.Country mLocatedCountry = null;
    private Handler handler = new Handler();

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.country_list;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        findViewById(R.id.fl_titlebar).setOnClickListener(new View.OnClickListener() { // from class: activity.CountryListActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                CountryListActivity.this.finish();
            }
        });
        this.layout_main = (LinearLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.btnSelectCountry = (MaterialButton) findViewById(R.id.btn_select_country);
        this.mCountryList = (ListView) findViewById(R.id.country_list);
        this.siderBar = (OASiderBar) findViewById(R.id.countryLetterListView);
        this.btnSelectCountry.setVisibility(0);
        this.btnSelectCountry.setOnClickListener(this);
        this.mCountryList.setBackgroundColor(-1);
        this.mSelectedCountry = IoTSmart.getCountry();
        LoadingCompact.showLoading(this);
        IoTSmart.getCountryList(new IoTSmart.ICountryListGetCallBack() { // from class: activity.CountryListActivity.2
            @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
            public void onSucess(List<IoTSmart.Country> list) {
                int i = 0;
                if (list != null) {
                    String jSONString = JSON.toJSONString(list);
                    while (i <= jSONString.length() / 600) {
                        int i2 = i * 600;
                        i++;
                        int length = i * 600;
                        if (length > jSONString.length()) {
                            length = jSONString.length();
                        }
                        ALog.d(CountryListActivity.TAG, jSONString.substring(i2, length));
                    }
                    CountryListActivity.this.onCountryListLoaded(list);
                    LoadingCompact.dismissLoading(CountryListActivity.this);
                    return;
                }
                ALog.d(CountryListActivity.TAG, "load country list failed");
                LoadingCompact.dismissLoading(CountryListActivity.this);
                Toast.makeText(CountryListActivity.this, "Failed to load list data from remote", 0).show();
            }

            @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountryListGetCallBack
            public void onFail(String str, int i, String str2) {
                ALog.d(CountryListActivity.TAG, "load country list failed");
                LoadingCompact.dismissLoading(CountryListActivity.this);
                Toast.makeText(CountryListActivity.this, str2, 0).show();
            }
        });
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onStop() {
        super.onStop();
        LoadingCompact.dismissLoading(this);
    }

    private void getHotCountries() {
        ArrayList arrayList = new ArrayList();
        ArrayList<IoTSmart.Country> arrayList2 = new ArrayList<>();
        arrayList.add("CHN");
        arrayList.add("DEU");
        arrayList.add("FRA");
        arrayList.add("IND");
        arrayList.add("USA");
        arrayList.add("VNM");
        arrayList.add("BRA");
        arrayList.add("RUS");
        arrayList.add("GBR");
        for (int i = 0; i < arrayList.size(); i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= this.mCountries.size()) {
                    break;
                }
                if (this.mCountries.get(i2).isoCode.equals(arrayList.get(i))) {
                    arrayList2.add(this.mCountries.get(i2));
                    break;
                }
                i2++;
            }
        }
        arrayList2.addAll(this.mCountries);
        this.mCountries = arrayList2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onCountryListLoaded(List<IoTSmart.Country> list) {
        IoTSmart.Country country;
        if (isDestroyed() || isFinishing() || list == null) {
            return;
        }
        this.mCountries.clear();
        this.mCountries.addAll(list);
        Collections.sort(this.mCountries, new Comparator() { // from class: activity.-$$Lambda$CountryListActivity$oNVuK_a_38snaQmy9FlptVgpTx4
            @Override // java.util.Comparator
            public final int compare(Object obj, Object obj2) {
                return CountryListActivity.CountryExt.getComparatorFiled((IoTSmart.Country) obj).compareTo(CountryListActivity.CountryExt.getComparatorFiled((IoTSmart.Country) obj2));
            }
        });
        this.siderBar.setInterval(0.0f);
        this.siderBar.setOnTouchingLetterChangedListener(new LetterListViewListener());
        this.alphaIndexer = new HashMap<>();
        this.overlayThread = new OverlayThread();
        try {
            initOverlay();
        } catch (WindowManager.BadTokenException unused) {
        }
        getHotCountries();
        this.f1567adapter = new ListAdapter(this, this.mCountries);
        this.mCountryList.setAdapter((android.widget.ListAdapter) this.f1567adapter);
        this.mCountryList.setOnItemClickListener(new CityListOnItemClick());
        initHeaderView();
        int i = -1;
        int i2 = 0;
        while (true) {
            if (i2 < this.mCountries.size()) {
                IoTSmart.Country country2 = this.mCountries.get(i2);
                if (country2 != null && (country = this.mSelectedCountry) != null && Objects.equals(country.domainAbbreviation, country2.domainAbbreviation)) {
                    i = i2;
                    break;
                }
                i2++;
            } else {
                break;
            }
        }
        if (i >= 0) {
            this.mCountryList.setSelection(i);
        }
    }

    private void initHeaderView() {
        if (ContextCompat.checkSelfPermission(this, Permission.ACCESS_COARSE_LOCATION) != 0 || ContextCompat.checkSelfPermission(this, Permission.ACCESS_FINE_LOCATION) != 0) {
            this.sharedPreferences = getSharedPreferences(AttributionReporter.SYSTEM_PERMISSION, 0);
            if (!this.sharedPreferences.getBoolean("refuse", false)) {
                TextView textView = new TextView(this);
                textView.setText(getResources().getString(R.string.location_permission));
                textView.setTextSize(20.0f);
                textView.setGravity(17);
                textView.setPadding(0, 50, 0, 0);
                textView.setTextColor(getResources().getColor(R.color.color_black));
                AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setCustomTitle(textView).setMessage(getResources().getString(R.string.location_permission_tips)).setPositiveButton(getResources().getString(R.string.allow), new DialogInterface.OnClickListener() { // from class: activity.CountryListActivity.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(CountryListActivity.this, new String[]{Permission.ACCESS_COARSE_LOCATION, Permission.ACCESS_FINE_LOCATION}, 3);
                    }
                }).setNegativeButton(getResources().getString(R.string.refuse), new DialogInterface.OnClickListener() { // from class: activity.CountryListActivity.3
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        CountryListActivity.this.sharedPreferences.edit().putBoolean("refuse", true).apply();
                        dialogInterface.dismiss();
                    }
                }).create();
                alertDialogCreate.show();
                WindowManager.LayoutParams attributes = alertDialogCreate.getWindow().getAttributes();
                attributes.width = (ScreenUtils.getScreenWidth(this) * 9) / 10;
                attributes.gravity = 17;
                alertDialogCreate.getWindow().setAttributes(attributes);
                alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
                alertDialogCreate.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
                Button button = alertDialogCreate.getButton(-1);
                Button button2 = alertDialogCreate.getButton(-2);
                button2.setTextSize(18.0f);
                button.setTextSize(18.0f);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
                layoutParams.weight = 10.0f;
                button.setLayoutParams(layoutParams);
                button2.setLayoutParams(layoutParams);
            }
        } else {
            LocationUtil.requestLocation(this);
            ArrayList<IoTSmart.Country> arrayList = this.mCountries;
            if (arrayList != null) {
                this.locateTask = new LocateTask(this, arrayList, this);
                this.locateTask.startLocation();
            }
        }
        startLocation();
    }

    @Override // tools.LocateHandler.OnLocationListener
    public void onContinuedLocate(String str) {
        if (isFinishing()) {
            return;
        }
        this.defaultCountryName.setText(str);
    }

    @Override // tools.LocateHandler.OnLocationListener
    @SuppressLint({"SetTextI18n"})
    public void onFailLocate() {
        if (isFinishing()) {
            return;
        }
        this.mLocatedCountry = null;
        View view2 = this.headerView;
        if (view2 != null) {
            this.mCountryList.removeHeaderView(view2);
        }
        this.headerView = View.inflate(this, R.layout.header_view_location_fail, null);
        LineTextView lineTextView = (LineTextView) this.headerView.findViewById(R.id.tv_location_again);
        lineTextView.setText(getString(R.string.location_failed) + getString(R.string.location_failed_again));
        ((LinearLayout) this.headerView.findViewById(R.id.ll_header_fail_back)).setBackgroundColor(getResources().getColor(android.R.color.white));
        ((TextView) this.headerView.findViewById(R.id.tv_location_fail)).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        lineTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
        this.headerView.setOnClickListener(new View.OnClickListener() { // from class: activity.-$$Lambda$CountryListActivity$NFMgbHR9PZDPI3ccynCkvbd50xM
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                CountryListActivity.lambda$onFailLocate$1(this.f$0, view3);
            }
        });
        this.mCountryList.addHeaderView(this.headerView);
    }

    public static /* synthetic */ void lambda$onFailLocate$1(CountryListActivity countryListActivity, View view2) {
        if (ContextCompat.checkSelfPermission(countryListActivity, Permission.ACCESS_COARSE_LOCATION) != 0 || ContextCompat.checkSelfPermission(countryListActivity, Permission.ACCESS_FINE_LOCATION) != 0) {
            ActivityCompat.requestPermissions(countryListActivity, new String[]{Permission.ACCESS_FINE_LOCATION, Permission.ACCESS_COARSE_LOCATION}, 1);
        }
        countryListActivity.startLocation();
    }

    @Override // tools.LocateHandler.OnLocationListener
    public void onSuccessLocate(final IoTSmart.Country country) {
        if (isFinishing()) {
            return;
        }
        LocationUtil.cancelLocating();
        this.mLocatedCountry = country;
        View view2 = this.headerView;
        if (view2 != null) {
            this.mCountryList.removeHeaderView(view2);
        }
        this.headerView = View.inflate(this, R.layout.header_view, null);
        TextView textView = (TextView) this.headerView.findViewById(R.id.ilop_pagestart_default_country_name);
        this.mSelectedView = (ImageView) this.headerView.findViewById(R.id.code);
        textView.setText(country.areaName);
        this.headerView.setOnClickListener(new View.OnClickListener() { // from class: activity.-$$Lambda$CountryListActivity$hbRD4PXNoLeVTAxpwzj-kVKNy4I
            @Override // android.view.View.OnClickListener
            public final void onClick(View view3) {
                CountryListActivity.lambda$onSuccessLocate$2(this.f$0, country, view3);
            }
        });
        this.mCountryList.addHeaderView(this.headerView);
    }

    public static /* synthetic */ void lambda$onSuccessLocate$2(CountryListActivity countryListActivity, IoTSmart.Country country, View view2) {
        countryListActivity.mSelectedView.setVisibility(0);
        countryListActivity.mSelectedCountry = country;
        BaseAdapter baseAdapter = countryListActivity.f1567adapter;
        if (baseAdapter != null) {
            baseAdapter.notifyDataSetChanged();
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view2) {
        if (view2.getId() == R.id.btn_select_country) {
            this.btnSelectCountry.setEnabled(false);
            showTips();
            this.btnSelectCountry.setEnabled(true);
        }
    }

    private void showTips() {
        TextView textView = new TextView(this);
        textView.setText(R.string.tip);
        textView.setTextSize(20.0f);
        textView.setGravity(17);
        textView.setPadding(0, 50, 0, 0);
        textView.setTextColor(getResources().getColor(R.color.color_black));
        AlertDialog alertDialogCreate = new AlertDialog.Builder(this).setCustomTitle(textView).setMessage(R.string.country_text2).setPositiveButton(R.string.confirm, new AnonymousClass6()).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() { // from class: activity.CountryListActivity.5
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();
        alertDialogCreate.show();
        WindowManager.LayoutParams attributes = alertDialogCreate.getWindow().getAttributes();
        attributes.width = (ScreenUtils.getScreenWidth(this) * 9) / 10;
        attributes.gravity = 17;
        alertDialogCreate.getWindow().setAttributes(attributes);
        alertDialogCreate.getButton(-1).setTextColor(Color.parseColor("#2c99fd"));
        alertDialogCreate.getButton(-2).setTextColor(ViewCompat.MEASURED_STATE_MASK);
        Button button = alertDialogCreate.getButton(-1);
        Button button2 = alertDialogCreate.getButton(-2);
        button2.setTextSize(18.0f);
        button.setTextSize(18.0f);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) button.getLayoutParams();
        layoutParams.weight = 10.0f;
        button.setLayoutParams(layoutParams);
        button2.setLayoutParams(layoutParams);
    }

    /* JADX INFO: renamed from: activity.CountryListActivity$6, reason: invalid class name */
    class AnonymousClass6 implements DialogInterface.OnClickListener {
        AnonymousClass6() {
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            if (CountryListActivity.this.mSelectedCountry != null) {
                LocationUtil.cancelLocating();
                IoTSmart.setCountry(CountryListActivity.this.mSelectedCountry, new IoTSmart.ICountrySetCallBack() { // from class: activity.-$$Lambda$CountryListActivity$6$TlQsXxnrQrAKOxSY7pV_y1WY85U
                    @Override // com.aliyun.iot.aep.sdk.IoTSmart.ICountrySetCallBack
                    public final void onCountrySet(boolean z) {
                        CountryListActivity.AnonymousClass6.lambda$onClick$0(this.f$0, z);
                    }
                });
            } else {
                Toast.makeText(CountryListActivity.this.getActivity(), R.string.select_use_area, 0).show();
            }
        }

        public static /* synthetic */ void lambda$onClick$0(AnonymousClass6 anonymousClass6, boolean z) {
            if (z) {
                CountryListActivity.this.killProcess();
            } else {
                SDKInitHelper.init(AApplication.getInstance());
                CountryListActivity.this.startLogin();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void killProcess() {
        restartApp();
    }

    private void restartApp() {
        if (getIntent().getBooleanExtra("needLogin", false)) {
            showProgressDialog();
            ((DemoApplication) DemoApplication.getInstance()).stopPushAndDisconnect();
            LoginBusiness.logout(new ILogoutCallback() { // from class: activity.CountryListActivity.7
                @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
                public void onLogoutSuccess() {
                    CountryListActivity.this.dismissProgressDialog();
                    SharePreferenceManager.getInstance().clear();
                    Intent intent = new Intent(CountryListActivity.this.getActivity(), (Class<?>) StartActivity.class);
                    intent.addFlags(268468224);
                    CountryListActivity.this.startActivity(intent);
                    System.gc();
                    Process.killProcess(Process.myPid());
                }

                @Override // com.aliyun.iot.aep.sdk.login.ILogoutCallback
                public void onLogoutFailed(int i, String str) {
                    CountryListActivity.this.dismissProgressDialog();
                    SharePreferenceManager.getInstance().clear();
                    Intent intent = new Intent(CountryListActivity.this.getActivity(), (Class<?>) StartActivity.class);
                    intent.addFlags(268468224);
                    CountryListActivity.this.startActivity(intent);
                    System.gc();
                    Process.killProcess(Process.myPid());
                }
            });
        } else {
            Intent intent = new Intent(getActivity(), (Class<?>) StartActivity.class);
            intent.addFlags(268468224);
            startActivity(intent);
            System.gc();
            Process.killProcess(Process.myPid());
        }
    }

    class CityListOnItemClick implements AdapterView.OnItemClickListener {
        CityListOnItemClick() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
            if (i <= 0) {
                return;
            }
            IoTSmart.Country country = (IoTSmart.Country) CountryListActivity.this.mCountryList.getAdapter().getItem(i);
            CountryListActivity.this.mSelectedCountry = country;
            if (CountryListActivity.this.mSelectedView != null) {
                if (Objects.equals(CountryListActivity.this.mLocatedCountry, country)) {
                    CountryListActivity.this.mSelectedView.setVisibility(0);
                } else {
                    CountryListActivity.this.mSelectedView.setVisibility(8);
                }
            }
            CountryListActivity.this.f1567adapter.notifyDataSetChanged();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void startLogin() {
        Intent intent = new Intent();
        intent.putExtra("countryCode", "1");
        setResult(-1, intent);
        if (getIntent().getStringExtra("type").equals("1")) {
            finish();
        } else {
            LoginBusiness.login(new ILoginCallback() { // from class: activity.CountryListActivity.8
                @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                public void onLoginSuccess() {
                    CountryListActivity.this.startActivity(new Intent(CountryListActivity.this, (Class<?>) MainActivity.class));
                    CountryListActivity.this.overridePendingTransition(0, 0);
                    CountryListActivity.this.finish();
                }

                @Override // com.aliyun.iot.aep.sdk.login.ILoginCallback
                public void onLoginFailed(int i, String str) {
                    LinkToast.makeText(CountryListActivity.this.getApplicationContext(), str).show();
                }
            });
        }
    }

    private class ListAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private List<IoTSmart.Country> list = new ArrayList();

        @Override // android.widget.Adapter
        public long getItemId(int i) {
            return i;
        }

        @Override // android.widget.BaseAdapter
        public void notifyDataSetChanged() {
            updateIndex();
            super.notifyDataSetChanged();
        }

        void updateIndex() {
            if (CountryListActivity.this.alphaIndexer == null) {
                CountryListActivity.this.alphaIndexer = new HashMap();
            }
            CountryListActivity.this.alphaIndexer.clear();
            CountryListActivity.this.sections = new String[this.list.size()];
            for (int i = 0; i < this.list.size(); i++) {
                if (this.list.get(i) != null) {
                    String nameSort = CountryExt.getNameSort(this.list.get(i));
                    int i2 = i - 1;
                    if (!(i2 >= 0 ? CountryExt.getNameSort(this.list.get(i2)) : " ").equals(nameSort)) {
                        CountryListActivity.this.alphaIndexer.put(nameSort, Integer.valueOf(i));
                        CountryListActivity.this.sections[i] = nameSort;
                    }
                }
            }
        }

        ListAdapter(Context context, List<IoTSmart.Country> list) {
            this.inflater = LayoutInflater.from(context);
            if (list != null) {
                this.list.addAll(list);
            }
            updateIndex();
        }

        @Override // android.widget.Adapter
        public int getCount() {
            if (AppConfig.isChina) {
                return 1;
            }
            return this.list.size();
        }

        @Override // android.widget.Adapter
        public Object getItem(int i) {
            return this.list.get(i);
        }

        @Override // android.widget.Adapter
        public View getView(int i, View view2, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            if (view2 == null) {
                view2 = this.inflater.inflate(R.layout.list_item, (ViewGroup) null);
                viewHolder = new ViewHolder();
                viewHolder.alpha = (TextView) view2.findViewById(R.id.alpha);
                viewHolder.name = (TextView) view2.findViewById(R.id.name);
                viewHolder.code = (ImageView) view2.findViewById(R.id.code);
                viewHolder.view_item_top_line = view2.findViewById(R.id.view_item_top_line);
                viewHolder.rl_item = (RelativeLayout) view2.findViewById(R.id.rl_item);
                view2.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view2.getTag();
            }
            viewHolder.rl_item.setBackgroundColor(-1);
            viewHolder.alpha.setBackgroundColor(Color.parseColor("#FFF6F6F6"));
            viewHolder.alpha.setTextColor(Color.parseColor("#FF999999"));
            viewHolder.name.setTextColor(ViewCompat.MEASURED_STATE_MASK);
            viewHolder.view_item_top_line.setBackgroundColor(Color.parseColor("#FFF5F5F5"));
            IoTSmart.Country country = this.list.get(i);
            if (country == null) {
                return view2;
            }
            viewHolder.name.setText(country.areaName);
            if (CountryListActivity.this.mSelectedCountry != null && country.domainAbbreviation.equals(CountryListActivity.this.mSelectedCountry.domainAbbreviation)) {
                viewHolder.code.setVisibility(0);
            } else {
                viewHolder.code.setVisibility(8);
            }
            String nameSort = CountryExt.getNameSort(country);
            int i2 = i - 1;
            String nameSort2 = i2 >= 0 ? CountryExt.getNameSort(this.list.get(i2)) : " ";
            if (i > 8) {
                if (!nameSort2.equals(nameSort)) {
                    viewHolder.alpha.setVisibility(0);
                    viewHolder.alpha.setText(nameSort);
                    viewHolder.view_item_top_line.setVisibility(8);
                } else {
                    viewHolder.alpha.setVisibility(8);
                    viewHolder.view_item_top_line.setVisibility(0);
                }
            } else if (i == 0) {
                viewHolder.alpha.setVisibility(0);
                viewHolder.alpha.setText(R.string.popular_areas);
            } else {
                viewHolder.alpha.setVisibility(8);
            }
            return view2;
        }

        private class ViewHolder {
            TextView alpha;
            ImageView code;
            TextView name;
            RelativeLayout rl_item;
            View view_item_top_line;

            private ViewHolder() {
            }
        }
    }

    private void initOverlay() {
        this.overlay = (TextView) LayoutInflater.from(this).inflate(R.layout.overlay, (ViewGroup) null);
        this.overlay.setVisibility(4);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2, 24, -3);
        this.windowManager = (WindowManager) getSystemService("window");
        WindowManager windowManager = this.windowManager;
        if (windowManager != null) {
            windowManager.addView(this.overlay, layoutParams);
        }
    }

    @Override // activity.CommonActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onDestroy() {
        TextView textView;
        super.onDestroy();
        WindowManager windowManager = this.windowManager;
        if (windowManager != null && (textView = this.overlay) != null) {
            windowManager.removeViewImmediate(textView);
        }
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        LocateTask locateTask = this.locateTask;
        if (locateTask != null) {
            locateTask.stopLocation();
            this.locateTask = null;
        }
        LocationUtil.cancelLocating();
        LoadingCompact.dismissLoading(this);
    }

    private class LetterListViewListener implements SiderBar.OnTouchingLetterChangedListener {
        private LetterListViewListener() {
        }

        @Override // com.alibaba.sdk.android.openaccount.ui.widget.SiderBar.OnTouchingLetterChangedListener
        public void onTouchingLetterChanged(String str) {
            Integer num;
            if (CountryListActivity.this.alphaIndexer.get(str) == null || (num = (Integer) CountryListActivity.this.alphaIndexer.get(str)) == null) {
                return;
            }
            CountryListActivity.this.mCountryList.setSelection(num.intValue() + 1);
            CountryListActivity.this.overlay.setText(CountryListActivity.this.sections[num.intValue()]);
            CountryListActivity.this.overlay.setVisibility(0);
            CountryListActivity.this.handler.removeCallbacks(CountryListActivity.this.overlayThread);
            CountryListActivity.this.handler.postDelayed(CountryListActivity.this.overlayThread, 700L);
        }
    }

    private class OverlayThread implements Runnable {
        private OverlayThread() {
        }

        @Override // java.lang.Runnable
        public void run() {
            CountryListActivity.this.overlay.setVisibility(8);
        }
    }

    private void startLocation() {
        View view2 = this.headerView;
        if (view2 != null) {
            this.mCountryList.removeHeaderView(view2);
        }
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(-1, -2);
        this.headerView = View.inflate(this, R.layout.header_view, null);
        this.defaultCountryName = (TextView) this.headerView.findViewById(R.id.ilop_pagestart_default_country_name);
        this.defaultCountryName.setTextColor(Color.parseColor("#FF333333"));
        this.defaultCountryName.setText(R.string.locating);
        this.headerView.setLayoutParams(layoutParams);
        this.mCountryList.addHeaderView(this.headerView);
    }

    static class CountryExt {
        CountryExt() {
        }

        private static boolean isChina() {
            try {
                return IoTSmart.getLanguage().equalsIgnoreCase("zh-CN");
            } catch (Exception unused) {
                return false;
            }
        }

        static String getNameSort(@NonNull IoTSmart.Country country) {
            try {
                if (isChina()) {
                    return country.pinyin.substring(0, 1);
                }
                return country.areaName.substring(0, 1);
            } catch (IndexOutOfBoundsException | NullPointerException unused) {
                return "";
            }
        }

        static String getComparatorFiled(@NonNull IoTSmart.Country country) {
            try {
                if (isChina()) {
                    return country.pinyin;
                }
                return country.areaName;
            } catch (NullPointerException unused) {
                return "";
            }
        }
    }

    public static void loadCountriesFromLocal(final Context context, final IoTSmart.ICountryListGetCallBack iCountryListGetCallBack) {
        ThreadPool.DefaultThreadPool.getInstance().submit(new Runnable() { // from class: activity.-$$Lambda$CountryListActivity$dzo1YlieLY_5Gosf09Uds_75Yj4
            @Override // java.lang.Runnable
            public final void run() {
                CountryListActivity.lambda$loadCountriesFromLocal$5(context, iCountryListGetCallBack);
            }
        });
    }

    static /* synthetic */ void lambda$loadCountriesFromLocal$5(Context context, final IoTSmart.ICountryListGetCallBack iCountryListGetCallBack) {
        try {
            final List array = JSON.parseArray(getJson("country_list.json", context.getApplicationContext()), IoTSmart.Country.class);
            ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: activity.-$$Lambda$CountryListActivity$iGbN4aryKIm0GoAuDY-MIifI2gE
                @Override // java.lang.Runnable
                public final void run() {
                    iCountryListGetCallBack.onSucess(array);
                }
            });
        } catch (Exception e) {
            ThreadPool.MainThreadHandler.getInstance().post(new Runnable() { // from class: activity.-$$Lambda$CountryListActivity$PMniF-LIi1TUWi1pAc6eWPx0ULk
                @Override // java.lang.Runnable
                public final void run() {
                    iCountryListGetCallBack.onFail("server", -1, e.getMessage());
                }
            });
        }
    }

    private static String getJson(String str, Context context) throws Throwable {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            try {
                try {
                    BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(context.getAssets().open(str)));
                    while (true) {
                        try {
                            String line = bufferedReader2.readLine();
                            if (line == null) {
                                break;
                            }
                            sb.append(line);
                        } catch (IOException e) {
                            e = e;
                            bufferedReader = bufferedReader2;
                            e.printStackTrace();
                            if (bufferedReader != null) {
                                bufferedReader.close();
                            }
                            return sb.toString();
                        } catch (Throwable th) {
                            th = th;
                            bufferedReader = bufferedReader2;
                            if (bufferedReader != null) {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            throw th;
                        }
                    }
                    bufferedReader2.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            } catch (IOException e4) {
                e = e4;
            }
            return sb.toString();
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity, androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        if (1 == i || i == 3) {
            boolean z = true;
            for (int i2 = 0; i2 < iArr.length; i2++) {
                if (iArr[i2] == -1) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, strArr[i2])) {
                        Log.e("权限", "被拒绝的权限" + strArr[i2]);
                        this.sharedPreferences.edit().putBoolean("refuse", true).apply();
                    } else if (i == 1) {
                        LocationUtil.remindStartLocateService(this);
                    }
                    z = false;
                }
            }
            if (z) {
                LocationUtil.requestLocation(this);
                ArrayList<IoTSmart.Country> arrayList = this.mCountries;
                if (arrayList != null) {
                    this.locateTask = new LocateTask(this, arrayList, this);
                    this.locateTask.startLocation();
                }
            }
        }
    }
}
