package com.alibaba.sdk.android.openaccount.ui.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.sdk.android.openaccount.rpc.model.RpcResponse;
import com.alibaba.sdk.android.openaccount.task.TaskWithDialog;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.alibaba.sdk.android.openaccount.ui.model.CountryComparator;
import com.alibaba.sdk.android.openaccount.ui.model.CountrySort;
import com.alibaba.sdk.android.openaccount.ui.model.GetCountryNameSort;
import com.alibaba.sdk.android.openaccount.ui.util.LocaleUtils;
import com.alibaba.sdk.android.openaccount.ui.widget.CountrySortAdapter;
import com.alibaba.sdk.android.openaccount.ui.widget.SiderBar;
import com.alibaba.sdk.android.openaccount.util.RpcUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: loaded from: classes.dex */
public class MobileCountrySelectorActivity extends ActivityTemplate {
    public static final int COUNTRY_CODE_REQUEST = 12;
    protected static final String COUNTRY_NUM_LIST_KEY = "countryNumList";
    protected static final String MAX_COUNTRY_VERSION_KEY = "maxCountryVersion";
    protected static final String PREF_FILE_NAME = "openaccount_country_list";
    protected GetCountryNameSort countryChangeUtil;
    protected CountryComparator countryComparator;
    protected TextView mCountryDialog;
    protected List<CountrySort> mCountryList = null;
    protected CountrySortAdapter mCountryListAdapter = null;
    protected ListView mCountryListView;
    protected EditText mSearchBox;
    protected SiderBar mSiderBar;

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate
    protected String getLayoutName() {
        return "ali_sdk_openaccount_mobile_country_selector";
    }

    @Override // com.alibaba.sdk.android.openaccount.ui.ui.ActivityTemplate, com.alibaba.sdk.android.openaccount.ui.ui.BaseAppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mSearchBox = (EditText) findViewById("search_box");
        this.mCountryListView = (ListView) findViewById("country_list");
        this.mCountryDialog = (TextView) findViewById("country_dialog");
        this.mSiderBar = (SiderBar) findViewById("country_sidebar");
        this.mSiderBar.setTextView(this.mCountryDialog);
        this.countryComparator = new CountryComparator();
        this.countryChangeUtil = new GetCountryNameSort();
        this.mCountryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.MobileCountrySelectorActivity.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
                Intent intent = new Intent();
                intent.putExtra("countryCode", ((CountrySort) MobileCountrySelectorActivity.this.mCountryListAdapter.getItem(i)).code);
                MobileCountrySelectorActivity.this.setResult(-1, intent);
                MobileCountrySelectorActivity.this.finish();
            }
        });
        this.mSearchBox.addTextChangedListener(new TextWatcher() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.MobileCountrySelectorActivity.2
            @Override // android.text.TextWatcher
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override // android.text.TextWatcher
            public void afterTextChanged(Editable editable) {
                if (MobileCountrySelectorActivity.this.mCountryList == null) {
                    return;
                }
                String string = MobileCountrySelectorActivity.this.mSearchBox.getText().toString();
                if (string.length() > 0) {
                    MobileCountrySelectorActivity.this.mCountryListAdapter.updateListView((ArrayList) MobileCountrySelectorActivity.this.countryChangeUtil.search(string, MobileCountrySelectorActivity.this.mCountryList));
                } else {
                    MobileCountrySelectorActivity.this.mCountryListAdapter.updateListView(MobileCountrySelectorActivity.this.mCountryList);
                }
                MobileCountrySelectorActivity.this.mCountryListView.setSelection(0);
            }
        });
        this.mSiderBar.setOnTouchingLetterChangedListener(new SiderBar.OnTouchingLetterChangedListener() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.MobileCountrySelectorActivity.3
            @Override // com.alibaba.sdk.android.openaccount.ui.widget.SiderBar.OnTouchingLetterChangedListener
            public void onTouchingLetterChanged(String str) {
                int positionForSection;
                if (MobileCountrySelectorActivity.this.mCountryListAdapter == null || (positionForSection = MobileCountrySelectorActivity.this.mCountryListAdapter.getPositionForSection(str.charAt(0))) == -1) {
                    return;
                }
                MobileCountrySelectorActivity.this.mCountryListView.setSelection(positionForSection);
            }
        });
        getCountryList();
        useCustomAttrs(this, this.attrs);
    }

    protected AsyncTask<Void, Void, List<CountrySort>> getCountryList() {
        return new GetCountryTask(this).execute(new Void[0]);
    }

    protected List<CountrySort> readArray(JSONArray jSONArray) {
        try {
            int length = jSONArray.length();
            if (length <= 0) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (int i = 0; i < length; i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                if (jSONObject != null) {
                    CountrySort countrySort = new CountrySort();
                    countrySort.name = jSONObject.optString("areaName");
                    countrySort.code = jSONObject.optString("code");
                    countrySort.englishName = jSONObject.optString("areaEnglishName");
                    countrySort.tranditionalName = jSONObject.optString("areaChineseTranditionalName");
                    countrySort.pinyin = jSONObject.optString("pinyin");
                    countrySort.sortWeightKey = jSONObject.optString("sortWeightKey");
                    countrySort.checkKey = jSONObject.optString("checkKey");
                    countrySort.domain = jSONObject.optString("domainAbbreviation");
                    countrySort.version = jSONObject.optInt("version");
                    String currentLocale = LocaleUtils.getCurrentLocale();
                    if (LocaleUtils.isZHLocale(currentLocale)) {
                        String sortLetterBySortKey = this.countryChangeUtil.getSortLetterBySortKey(countrySort.pinyin);
                        if (sortLetterBySortKey == null) {
                            sortLetterBySortKey = this.countryChangeUtil.getSortLetter(countrySort.name);
                        }
                        countrySort.sortLetters = sortLetterBySortKey;
                        if (LocaleUtils.isUseTraditionChinese(currentLocale)) {
                            countrySort.displayName = countrySort.tranditionalName;
                        } else {
                            countrySort.displayName = countrySort.name;
                        }
                    } else {
                        String sortLetterBySortKey2 = this.countryChangeUtil.getSortLetterBySortKey(countrySort.englishName);
                        if (sortLetterBySortKey2 == null) {
                            sortLetterBySortKey2 = this.countryChangeUtil.getSortLetter(countrySort.name);
                        }
                        countrySort.sortLetters = sortLetterBySortKey2;
                        countrySort.displayName = countrySort.englishName;
                    }
                    arrayList.add(countrySort);
                }
            }
            this.mCountryList = arrayList;
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    protected class GetCountryTask extends TaskWithDialog<Void, Void, List<CountrySort>> {
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        protected void doWhenException(Throwable th) {
        }

        public GetCountryTask(Activity activity2) {
            super(activity2);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.alibaba.sdk.android.openaccount.task.AbsAsyncTask
        public List<CountrySort> asyncExecute(Void... voidArr) {
            String string;
            JSONArray jSONArray;
            final JSONArray jSONArray2;
            boolean z = false;
            SharedPreferences sharedPreferences = MobileCountrySelectorActivity.this.getSharedPreferences(MobileCountrySelectorActivity.PREF_FILE_NAME, 0);
            final List<CountrySort> array = null;
            String string2 = sharedPreferences.getString(MobileCountrySelectorActivity.COUNTRY_NUM_LIST_KEY, null);
            if (TextUtils.isEmpty(string2)) {
                string = null;
                jSONArray = null;
            } else {
                string = sharedPreferences.getString(MobileCountrySelectorActivity.MAX_COUNTRY_VERSION_KEY, null);
                try {
                    jSONArray = new JSONArray(string2);
                } catch (Exception e) {
                    AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, "fail to parse local cached country list, will use server side", e);
                    string = null;
                    jSONArray = null;
                }
            }
            HashMap map = new HashMap();
            map.put("queryString", "q=test&rows=100");
            map.put("version", string);
            RpcResponse rpcResponsePureInvokeWithRiskControlInfo = RpcUtils.pureInvokeWithRiskControlInfo("searchCountryRequest", map, "searchcountry");
            if (rpcResponsePureInvokeWithRiskControlInfo == null || rpcResponsePureInvokeWithRiskControlInfo.arrayData == null) {
                jSONArray2 = null;
            } else {
                try {
                    if (rpcResponsePureInvokeWithRiskControlInfo.code == 1) {
                        jSONArray2 = rpcResponsePureInvokeWithRiskControlInfo.arrayData;
                        if (jSONArray2 != null) {
                            try {
                                array = MobileCountrySelectorActivity.this.readArray(jSONArray2);
                                z = true;
                            } catch (Exception e2) {
                                e = e2;
                                AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, "fail to parse the server side response", e);
                            }
                        }
                    } else {
                        jSONArray2 = null;
                    }
                } catch (Exception e3) {
                    e = e3;
                    jSONArray2 = null;
                }
            }
            if (array == null && jSONArray != null) {
                try {
                    array = MobileCountrySelectorActivity.this.readArray(jSONArray);
                } catch (Exception e4) {
                    AliSDKLogger.e(OpenAccountUIConstants.LOG_TAG, "fail to parse the local country list", e4);
                }
            }
            if (z) {
                this.executorService.postTask(new Runnable() { // from class: com.alibaba.sdk.android.openaccount.ui.ui.MobileCountrySelectorActivity.GetCountryTask.1
                    @Override // java.lang.Runnable
                    public void run() {
                        int i = 0;
                        SharedPreferences.Editor editorEdit = MobileCountrySelectorActivity.this.getSharedPreferences(MobileCountrySelectorActivity.PREF_FILE_NAME, 0).edit();
                        editorEdit.putString(MobileCountrySelectorActivity.COUNTRY_NUM_LIST_KEY, jSONArray2.toString());
                        for (CountrySort countrySort : array) {
                            if (countrySort.version > i) {
                                i = countrySort.version;
                            }
                        }
                        editorEdit.putString(MobileCountrySelectorActivity.MAX_COUNTRY_VERSION_KEY, String.valueOf(i));
                        editorEdit.apply();
                    }
                });
            }
            return array;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<CountrySort> list) {
            if (list == null || list.size() <= 0) {
                return;
            }
            MobileCountrySelectorActivity mobileCountrySelectorActivity = MobileCountrySelectorActivity.this;
            List<CountrySort> hot5 = mobileCountrySelectorActivity.getHot5(mobileCountrySelectorActivity.mCountryList);
            Collections.sort(MobileCountrySelectorActivity.this.mCountryList, MobileCountrySelectorActivity.this.countryComparator);
            list.addAll(0, hot5);
            MobileCountrySelectorActivity mobileCountrySelectorActivity2 = MobileCountrySelectorActivity.this;
            mobileCountrySelectorActivity2.mCountryListAdapter = new CountrySortAdapter(mobileCountrySelectorActivity2, list);
            MobileCountrySelectorActivity.this.mCountryListView.setAdapter((ListAdapter) MobileCountrySelectorActivity.this.mCountryListAdapter);
        }
    }

    protected List<CountrySort> getHot5(List<CountrySort> list) {
        ArrayList arrayList = new ArrayList();
        if (list.size() >= 5) {
            for (int i = 0; i < 5; i++) {
                arrayList.add(CountrySort.getCountryHot(list.get(i)));
            }
        }
        return arrayList;
    }
}
