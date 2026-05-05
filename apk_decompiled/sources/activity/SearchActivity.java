package activity;

import adapter.SearchAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import bean.SearchModel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback;
import com.haibin.calendarview.CalendarModel;
import com.haibin.calendarview.CalendarView;
import com.seculink.app.R;
import com.seculink.app.databinding.ActivitySearchBinding;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import sdk.IPCManager;
import tools.DateUtil;
import tools.OnMultiClickListener;

/* JADX INFO: loaded from: classes.dex */
public class SearchActivity extends CommonActivity {
    ActivitySearchBinding binding;
    private int curBeginTime;
    private int curEndTime;
    private int day;
    LinearLayout layout_main;
    private int month;
    List<SearchModel> searchModel;
    private int year;
    String iotId = "";
    private int type = 0;

    @Override // activity.CommonActivity
    protected int getContentLayoutId() {
        return R.layout.activity_search;
    }

    @Override // activity.CommonActivity
    protected void initWidget(Bundle bundle) {
        super.initWidget(bundle);
        this.binding = (ActivitySearchBinding) DataBindingUtil.setContentView(this, R.layout.activity_search);
        setEdgeToEdge(this.layout_main);
        this.iotId = getIntent().getStringExtra("iotId");
        this.binding.leftImg.setOnClickListener(new OnMultiClickListener() { // from class: activity.SearchActivity.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                SearchActivity.this.finish();
            }
        });
        this.binding.editSearch.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: activity.SearchActivity.2
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                SearchActivity.this.binding.editSearch.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                SearchActivity.this.binding.editSearch.requestFocus();
            }
        });
        this.binding.tvSearch.setOnClickListener(new OnMultiClickListener() { // from class: activity.SearchActivity.3
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                String string = SearchActivity.this.binding.editSearch.getText().toString();
                if (!string.isEmpty()) {
                    SearchActivity.this.SearchData(string);
                } else {
                    SearchActivity.this.showToast("请输入内容");
                }
            }
        });
        initDate();
        isToday();
        this.binding.tvStart.setText(this.year + "年" + this.month + "月" + this.day + "日");
        this.binding.tvEnd.setText(this.year + "年" + this.month + "月" + this.day + "日");
        this.binding.layoutStart.setOnClickListener(new OnMultiClickListener() { // from class: activity.SearchActivity.4
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                SearchActivity.this.type = 1;
                if (SearchActivity.this.binding.layoutDate.getVisibility() == 8) {
                    SearchActivity.this.binding.layoutDate.setVisibility(0);
                } else {
                    SearchActivity.this.binding.layoutDate.setVisibility(8);
                }
            }
        });
        this.binding.layoutEnd.setOnClickListener(new OnMultiClickListener() { // from class: activity.SearchActivity.5
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                SearchActivity.this.type = 2;
                if (SearchActivity.this.binding.layoutDate.getVisibility() == 8) {
                    SearchActivity.this.binding.layoutDate.setVisibility(0);
                } else {
                    SearchActivity.this.binding.layoutDate.setVisibility(8);
                }
            }
        });
        this.binding.calendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() { // from class: activity.SearchActivity.6
            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarOutOfRange(CalendarModel calendarModel) {
            }

            @Override // com.haibin.calendarview.CalendarView.OnCalendarSelectListener
            public void onCalendarSelect(CalendarModel calendarModel, boolean z) {
                if (z) {
                    SearchActivity.this.binding.layoutDate.setVisibility(8);
                    if (SearchActivity.this.type == 1) {
                        SearchActivity.this.binding.tvStart.setText(calendarModel.getYear() + "年" + calendarModel.getMonth() + "月" + calendarModel.getDay() + "日");
                        SearchActivity.this.curBeginTime = (int) (DateUtil.getTimeMillins(calendarModel.getYear(), calendarModel.getMonth(), calendarModel.getDay(), 0, 0, 0) / 1000);
                        return;
                    }
                    SearchActivity.this.binding.tvEnd.setText(calendarModel.getYear() + "年" + calendarModel.getMonth() + "月" + calendarModel.getDay() + "日");
                    SearchActivity.this.curEndTime = (int) (DateUtil.getTimeMillins(calendarModel.getYear(), calendarModel.getMonth(), calendarModel.getDay(), 23, 59, 59) / 1000);
                }
            }
        });
        this.binding.tvToday.setOnClickListener(new OnMultiClickListener() { // from class: activity.SearchActivity.7
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                SearchActivity.this.isToday();
                SearchActivity.this.binding.tvStart.setText(SearchActivity.this.year + "年" + SearchActivity.this.month + "月" + SearchActivity.this.day + "日");
                SearchActivity.this.binding.tvEnd.setText(SearchActivity.this.year + "年" + SearchActivity.this.month + "月" + SearchActivity.this.day + "日");
                SearchActivity.this.setTextColor(0);
            }
        });
        this.binding.tvMonth.setOnClickListener(new OnMultiClickListener() { // from class: activity.SearchActivity.8
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                Intent intent = new Intent();
                intent.putExtra("result", 1715662212);
                SearchActivity.this.setResult(-1, intent);
                SearchActivity.this.finish();
            }
        });
        this.binding.tvYear.setOnClickListener(new OnMultiClickListener() { // from class: activity.SearchActivity.9
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view2) {
                List<String> nearlyMonthDates = SearchActivity.getNearlyMonthDates();
                String str = nearlyMonthDates.get(0);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
                SearchActivity.this.isToday();
                try {
                    int[] calendarDate = DateUtil.getCalendarDate(simpleDateFormat.parse(str));
                    SearchActivity.this.year = calendarDate[0];
                    SearchActivity.this.month = calendarDate[1];
                    SearchActivity.this.day = calendarDate[2];
                    long[] stEtTime = SearchActivity.this.getStEtTime();
                    SearchActivity.this.curEndTime = (int) (stEtTime[1] / 1000);
                    SearchActivity.this.binding.tvEnd.setText(nearlyMonthDates.get(nearlyMonthDates.size() - 1));
                    SearchActivity.this.binding.tvStart.setText(str);
                    SearchActivity.this.setTextColor(2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /* JADX INFO: renamed from: activity.SearchActivity$10, reason: invalid class name */
    class AnonymousClass10 implements IPanelCallback {
        AnonymousClass10() {
        }

        @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
        public void onComplete(final boolean z, final Object obj) {
            Log.d(SearchActivity.this.TAG, "onComplete: ------" + z + "    " + obj.toString());
            new Handler().post(new Runnable() { // from class: activity.SearchActivity.10.1
                @Override // java.lang.Runnable
                public void run() {
                    if (z) {
                        Object obj2 = obj;
                        if (obj2 == null || String.valueOf(obj2).equals("")) {
                            return;
                        }
                        try {
                            JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                            JSONObject jSONObject = object.getJSONObject("data");
                            if (object.getInteger("code").intValue() == 200) {
                                JSONArray jSONArray = jSONObject.getJSONArray("data");
                                SearchActivity.this.searchModel = JSON.parseArray(jSONArray.toString(), SearchModel.class);
                                if (SearchActivity.this.searchModel.size() != 0) {
                                    SearchActivity.this.binding.tvNoData.setVisibility(8);
                                    SearchActivity.this.binding.rvList.setVisibility(0);
                                    SearchActivity.this.binding.rvList.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                                    SearchAdapter searchAdapter = new SearchAdapter(SearchActivity.this, SearchActivity.this.searchModel);
                                    SearchActivity.this.binding.rvList.setAdapter(searchAdapter);
                                    searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() { // from class: activity.SearchActivity.10.1.1
                                        @Override // adapter.SearchAdapter.OnItemClickListener
                                        public void onItemClick(int i, SearchModel searchModel) {
                                            Intent intent = new Intent();
                                            Log.e("回传数据0", searchModel.TimeStamp + "");
                                            intent.putExtra("return_key", searchModel.TimeStamp);
                                            SearchActivity.this.setResult(-1, intent);
                                            SearchActivity.this.finish();
                                        }
                                    });
                                } else {
                                    SearchActivity.this.showToast("未找到该记录");
                                    SearchActivity.this.binding.tvNoData.setVisibility(0);
                                    SearchActivity.this.binding.rvList.setVisibility(8);
                                }
                            } else {
                                SearchActivity.this.showToast("未找到该记录");
                                SearchActivity.this.binding.tvNoData.setVisibility(0);
                                SearchActivity.this.binding.rvList.setVisibility(8);
                            }
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            return;
                        }
                    }
                    SearchActivity.this.showToast("未找到该记录");
                    SearchActivity.this.binding.tvNoData.setVisibility(0);
                    SearchActivity.this.binding.rvList.setVisibility(8);
                }
            });
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void SearchData(String str) {
        IPCManager.getInstance().getDevice(this.iotId).setQueryLocalEvent(str, 6, this.curBeginTime, this.curEndTime, new AnonymousClass10());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setTextColor(int i) {
        this.binding.tvToday.setTextColor(i == 0 ? getColor(R.color.colorAccent) : getColor(R.color.color_B5BFC6));
        this.binding.tvMonth.setTextColor(i == 1 ? getColor(R.color.colorAccent) : getColor(R.color.color_B5BFC6));
        this.binding.tvYear.setTextColor(i == 2 ? getColor(R.color.colorAccent) : getColor(R.color.color_B5BFC6));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void isToday() {
        this.year = DateUtil.getYear();
        this.month = DateUtil.getMonth();
        this.day = DateUtil.getDay();
        long[] stEtTime = getStEtTime();
        this.curBeginTime = (int) (stEtTime[0] / 1000);
        this.curEndTime = (int) (stEtTime[1] / 1000);
    }

    public static List<String> getNearlyWeekDates() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String str = simpleDateFormat.format(new Date());
        calendar.add(5, -7);
        return getBetweenDates(simpleDateFormat.format(calendar.getTime()), str, false);
    }

    public static List<String> getNearlyMonthDates() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        String str = simpleDateFormat.format(new Date());
        calendar.add(2, -1);
        return getBetweenDates(simpleDateFormat.format(calendar.getTime()), str, false);
    }

    public static List<String> getBetweenDates(String str, String str2, boolean z) {
        ArrayList arrayList = new ArrayList();
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
            Date date = new SimpleDateFormat("yyyy年MM月dd日").parse(str);
            Date date2 = new SimpleDateFormat("yyyy年MM月dd日").parse(str2);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (z) {
                arrayList.add(simpleDateFormat.format(date));
            }
            while (calendar.getTime().before(date2)) {
                calendar.add(5, 1);
                String str3 = new SimpleDateFormat("yyyy年MM月dd日").format(calendar.getTime());
                arrayList.add(str3);
                System.out.println(str3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long[] getStEtTime() {
        return new long[]{DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0), DateUtil.getTimeMillins(this.year, this.month, this.day, 23, 59, 59)};
    }

    private void initDate() {
        IPCManager.getInstance().getDevice(this.iotId).QueryRecordTimeList(new IPanelCallback() { // from class: activity.SearchActivity.11
            @Override // com.aliyun.alink.linksdk.tmp.device.panel.listener.IPanelCallback
            public void onComplete(boolean z, Object obj) {
                if (z) {
                    Log.d(SearchActivity.this.TAG, "onComplete: " + z);
                    ArrayList arrayList = new ArrayList();
                    JSONObject object = JSONObject.parseObject(String.valueOf(obj));
                    if (object.containsKey("code") && object.get("code").equals(200)) {
                        for (int i = 0; i < object.getJSONObject("data").getJSONArray("DateList").size(); i++) {
                            arrayList.add(object.getJSONObject("data").getJSONArray("DateList").getJSONObject(i).getInteger("Date"));
                        }
                    }
                    SearchActivity.this.drawCircle(arrayList);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void drawCircle(List<Integer> list) {
        int i;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        HashMap map = new HashMap();
        for (int i2 = 0; i2 < list.size(); i2++) {
            String str = simpleDateFormat.format(new Date(Long.valueOf(list.get(i2) + "000").longValue()));
            int i3 = Integer.parseInt(str.substring(0, 4));
            int i4 = Integer.parseInt(str.substring(5, 7));
            String strSubstring = str.substring(8, 10);
            if (strSubstring.substring(0, 1).equals("0")) {
                i = Integer.parseInt(strSubstring.substring(1, 2));
            } else {
                i = Integer.parseInt(strSubstring);
            }
            int i5 = i;
            map.put(getSchemeCalendar(i3, i4, i5, -12526811, "").toString(), getSchemeCalendar(i3, i4, i5, -12526811, ""));
        }
        this.binding.calendarView.setSchemeDate(map);
    }

    private CalendarModel getSchemeCalendar(int i, int i2, int i3, int i4, String str) {
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.setYear(i);
        calendarModel.setMonth(i2);
        calendarModel.setDay(i3);
        return calendarModel;
    }
}
