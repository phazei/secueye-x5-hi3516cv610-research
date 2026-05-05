package fragment;

import activity.ChooseCameraActivity;
import adapter.MessageAdapter;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import bean.DeviceInfoBean;
import bean.DeviceInfoBeans;
import bean.MessageBean;
import bean.PushRefresh;
import bean.ShareBean;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.seculink.app.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import tools.CallBack;
import tools.DateUtil;
import tools.MessageClient;
import view.MySpinnerView;

/* JADX INFO: loaded from: classes4.dex */
public class MessageFragment extends CommonFragment {
    private int day;
    MessageAdapter messageAdapter;
    private int month;
    private PopupWindow pw;
    RecyclerView recyclerView;
    MySpinnerView spinner_camera;
    MySpinnerView spinner_date;
    MySpinnerView spinner_type;
    SwipeRefreshLayout srl;
    private int year;
    public List<DeviceInfoBean> deviceInfoBeans = new ArrayList();
    private List<String> typeItems = new ArrayList();
    private List<Integer> typeItemValue = new ArrayList();
    List<MessageBean.EventListBean> listBeans = new ArrayList();
    int num = 0;

    interface Callback {
        void finish(int i);
    }

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.msgtab_fragment_layout;
    }

    @Override // fragment.CommonFragment
    protected void initWidget(View view2) {
        super.initWidget(view2);
        this.spinner_camera = (MySpinnerView) view2.findViewById(R.id.spinner_camera);
        this.spinner_type = (MySpinnerView) view2.findViewById(R.id.spinner_type);
        this.spinner_date = (MySpinnerView) view2.findViewById(R.id.spinner_date);
        this.recyclerView = (RecyclerView) view2.findViewById(R.id.recyclerview);
        this.srl = (SwipeRefreshLayout) view2.findViewById(R.id.srl);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.year = DateUtil.getYear();
        this.month = DateUtil.getMonth();
        this.day = DateUtil.getDay();
        this.spinner_type.setOnClickListener(new MySpinnerView.onClickListener() { // from class: fragment.MessageFragment.1
            @Override // view.MySpinnerView.onClickListener
            public void onClick() {
                MessageFragment.this.showPopupWindow();
            }
        });
        this.typeItems.clear();
        this.typeItems.add(getResources().getString(R.string.all_dynamics));
        this.typeItems.add(getResources().getString(R.string.motion_detection));
        this.typeItems.add(getResources().getString(R.string.car));
        this.typeItemValue.clear();
        this.typeItemValue.add(0);
        this.typeItemValue.add(1);
        this.typeItemValue.add(10005);
        this.spinner_type.setData(this.typeItems);
        this.spinner_type.setCanPullList(false);
        this.spinner_date.setOnClickListener(new MySpinnerView.onClickListener() { // from class: fragment.MessageFragment.2
            @Override // view.MySpinnerView.onClickListener
            public void onClick() {
                MessageFragment.this.showTimePicker();
            }
        });
        this.spinner_date.setText(DateUtil.getToday(DateUtil.sdf5));
        this.spinner_camera.setCanPullList(false);
        this.spinner_camera.setOnClickListener(new MySpinnerView.onClickListener() { // from class: fragment.MessageFragment.3
            @Override // view.MySpinnerView.onClickListener
            public void onClick() {
                Intent intent = new Intent(MessageFragment.this.getActivity(), (Class<?>) ChooseCameraActivity.class);
                intent.putStringArrayListExtra("names", (ArrayList) MessageFragment.this.spinner_camera.getData());
                intent.putExtra("pos", MessageFragment.this.spinner_camera.getSelectPos());
                MessageFragment.this.startActivityForResult(intent, 4096);
            }
        });
        this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.recyclerView.setHasFixedSize(true);
        this.recyclerView.setOnTouchListener(new View.OnTouchListener() { // from class: fragment.MessageFragment.4
            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view3, MotionEvent motionEvent) {
                return MessageFragment.this.srl.isRefreshing();
            }
        });
        this.messageAdapter = new MessageAdapter(getContext(), this.listBeans);
        this.recyclerView.setAdapter(this.messageAdapter);
        this.srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: fragment.MessageFragment.5
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                MessageFragment.this.getRefresh();
            }
        });
    }

    @Override // fragment.CommonFragment
    public void initData() {
        getRefresh();
        this.num = 0;
    }

    void refreshAdapter() {
        Log.d(this.TAG, "run: -----" + this.listBeans.size() + "   ");
        Collections.sort(this.listBeans, new Comparator<MessageBean.EventListBean>() { // from class: fragment.MessageFragment.6
            @Override // java.util.Comparator
            public int compare(MessageBean.EventListBean eventListBean, MessageBean.EventListBean eventListBean2) {
                Log.d(MessageFragment.this.TAG, "run: -----" + eventListBean.getEventTime());
                return eventListBean2.getEventTime().compareTo(eventListBean.getEventTime());
            }
        });
        getActivity().runOnUiThread(new Runnable() { // from class: fragment.MessageFragment.7
            @Override // java.lang.Runnable
            public void run() {
                if (MessageFragment.this.srl != null && MessageFragment.this.srl.isRefreshing()) {
                    MessageFragment.this.srl.setRefreshing(false);
                }
                MessageFragment.this.messageAdapter.notifyDataSetChanged();
                Log.d(MessageFragment.this.TAG, "run: notifyDataSetChanged------");
            }
        });
    }

    void getRefresh() {
        int selectPos = this.spinner_camera.getSelectPos() - 1;
        int iIntValue = this.typeItemValue.get(this.spinner_type.getSelectPos()).intValue();
        if (selectPos >= 0) {
            DeviceInfoBean deviceInfoBean = this.deviceInfoBeans.get(selectPos);
            getPushMsg(deviceInfoBean.getIotId(), deviceInfoBean.getNickName() != null ? deviceInfoBean.getNickName() : deviceInfoBean.getProductName(), iIntValue, new Callback() { // from class: fragment.MessageFragment.8
                @Override // fragment.MessageFragment.Callback
                public void finish(int i) {
                    MessageFragment.this.refreshAdapter();
                }
            });
        } else {
            if (iIntValue == 0) {
                getShareMsg();
            }
            getPushMsg(iIntValue, new Callback() { // from class: fragment.MessageFragment.9
                @Override // fragment.MessageFragment.Callback
                public void finish(int i) {
                    MessageFragment.this.num += i;
                    Log.d(MessageFragment.this.TAG, "run:: " + i);
                    if (MessageFragment.this.num == MessageFragment.this.deviceInfoBeans.size()) {
                        MessageFragment.this.refreshAdapter();
                        MessageFragment.this.num = 0;
                    }
                }
            });
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void updateDeviceInfos(DeviceInfoBeans deviceInfoBeans) {
        this.deviceInfoBeans.clear();
        for (DeviceInfoBean deviceInfoBean : deviceInfoBeans.getData()) {
            if (deviceInfoBean.getReceiverStatus() != -1) {
                this.deviceInfoBeans.add(deviceInfoBean);
            }
        }
        List<String> deviceNames = getDeviceNames(this.deviceInfoBeans);
        deviceNames.add(0, getResources().getString(R.string.all_camera));
        this.spinner_camera.setData(deviceNames);
    }

    public void getShareMsg() {
        MessageClient.getInstance().getApi().getMessage(0, 100, "MESSAGE", "share", DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0), DateUtil.getTimeMillins(this.year, this.month, this.day, 23, 59, 59), new CallBack() { // from class: fragment.MessageFragment.10
            @Override // tools.CallBack
            public void compete(IoTResponse ioTResponse, String str, int i) {
                if (ioTResponse.getData() != null) {
                    ShareBean shareBean = (ShareBean) new Gson().fromJson(ioTResponse.getData().toString(), ShareBean.class);
                    for (int i2 = 0; i2 < shareBean.getData().size(); i2++) {
                        MessageBean.EventListBean eventListBean = new MessageBean.EventListBean();
                        eventListBean.setEventDesc(shareBean.getData().get(i2).getBody());
                        eventListBean.setEventTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(shareBean.getData().get(i2).getGmtCreate())));
                        if (eventListBean.getEventPicUrl() != null && !"".equals(eventListBean.getEventPicUrl()) && eventListBean.getEventType() != 2) {
                            MessageFragment.this.listBeans.add(eventListBean);
                        }
                    }
                }
            }
        });
    }

    public List<String> getDeviceNames(List<DeviceInfoBean> list) {
        ArrayList arrayList = new ArrayList();
        Iterator<DeviceInfoBean> it = list.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().getName());
        }
        return arrayList;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showPopupWindow() {
        if (this.pw == null) {
            MySpinnerView mySpinnerView = new MySpinnerView(getActivity());
            this.pw = new PopupWindow(mySpinnerView, this.spinner_type.getWidth(), -2);
            this.pw.setBackgroundDrawable(new BitmapDrawable());
            this.pw.setInputMethodMode(1);
            this.pw.setSoftInputMode(16);
            this.pw.setOutsideTouchable(true);
            this.pw.setFocusable(true);
            this.pw.setAnimationStyle(0);
            mySpinnerView.setOnItemSelectedListener(new MySpinnerView.onItemSelectedListener() { // from class: fragment.MessageFragment.11
                @Override // view.MySpinnerView.onItemSelectedListener
                public void onItemSelected(String str, int i) {
                    MessageFragment.this.spinner_type.setSelectPos(i);
                    MessageFragment.this.getRefresh();
                    MessageFragment.this.pw.dismiss();
                }
            });
        }
        MySpinnerView mySpinnerView2 = (MySpinnerView) this.pw.getContentView();
        mySpinnerView2.setLLRootShow(false);
        mySpinnerView2.setData(this.typeItems);
        mySpinnerView2.setPullListShow(true);
        this.pw.showAsDropDown(this.spinner_type);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 1, 0, 0);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(2025, 10, 11, 23, 59);
        TimePickerView timePickerViewBuild = new TimePickerBuilder(getActivity(), new OnTimeSelectListener() { // from class: fragment.MessageFragment.13
            @Override // com.bigkoo.pickerview.listener.OnTimeSelectListener
            public void onTimeSelect(Date date, View view2) {
                MessageFragment.this.spinner_date.setText(DateUtil.getDateFormat(date, DateUtil.sdf5));
                int[] calendarDate = DateUtil.getCalendarDate(date);
                MessageFragment.this.year = calendarDate[0];
                MessageFragment.this.month = calendarDate[1];
                MessageFragment.this.day = calendarDate[2];
                MessageFragment.this.getRefresh();
            }
        }).setTimeSelectChangeListener(new OnTimeSelectChangeListener() { // from class: fragment.MessageFragment.12
            @Override // com.bigkoo.pickerview.listener.OnTimeSelectChangeListener
            public void onTimeSelectChanged(Date date) {
            }
        }).setRangDate(calendar, calendar2).setType(new boolean[]{true, true, true, false, false, false}).setSubmitText(getResources().getString(R.string.confirm)).setCancelText(getResources().getString(R.string.cancel)).setLabel(getString(R.string.year), getString(R.string.month), getString(R.string.day), getString(R.string.hour), getString(R.string.minute), getString(R.string.second)).setSubmitColor(getResources().getColor(R.color.colorAccent)).setCancelColor(getResources().getColor(R.color.colorAccent)).build();
        Dialog dialog2 = timePickerViewBuild.getDialog();
        if (dialog2 != null) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2, 80);
            layoutParams.leftMargin = 0;
            layoutParams.rightMargin = 0;
            timePickerViewBuild.getDialogContainerLayout().setLayoutParams(layoutParams);
            Window window = dialog2.getWindow();
            if (window != null) {
                window.setWindowAnimations(R.style.picker_view_slide_anim);
                window.setGravity(80);
            }
        }
        timePickerViewBuild.setDate(DateUtil.getCalendar(this.spinner_date.getText() + " 00:00"));
        timePickerViewBuild.show();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 4096 && i2 == -1) {
            this.spinner_camera.setSelectPos(intent.getIntExtra("pos", 0));
            getRefresh();
        }
    }

    public synchronized void getPushMsg(int i, final Callback callback) {
        String productName;
        this.listBeans.clear();
        long timeMillins = DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0);
        long timeMillins2 = DateUtil.getTimeMillins(this.year, this.month, this.day, 23, 59, 59);
        for (int i2 = 0; i2 < this.deviceInfoBeans.size(); i2++) {
            if (this.deviceInfoBeans.get(i2).getNickName() != null) {
                productName = this.deviceInfoBeans.get(i2).getNickName();
            } else {
                productName = this.deviceInfoBeans.get(i2).getProductName();
            }
            MessageClient.getInstance().getApi().getMsg(0, i, timeMillins, timeMillins2, this.deviceInfoBeans.get(i2).getIotId(), productName, 1, new CallBack() { // from class: fragment.MessageFragment.14
                @Override // tools.CallBack
                public void compete(IoTResponse ioTResponse, String str, int i3) {
                    if (ioTResponse.getData() != null) {
                        MessageBean messageBean = (MessageBean) new Gson().fromJson(ioTResponse.getData().toString(), MessageBean.class);
                        for (int i4 = 0; i4 < messageBean.getEventList().size(); i4++) {
                            messageBean.getEventList().get(i4).setEventNickName(str);
                            if (messageBean.getEventList().get(i4).getEventPicUrl() != null && !"".equals(messageBean.getEventList().get(i4).getEventPicUrl()) && messageBean.getEventList().get(i4).getEventType() != 2) {
                                MessageFragment.this.listBeans.add(messageBean.getEventList().get(i4));
                            }
                        }
                        callback.finish(i3);
                    }
                }
            });
        }
    }

    public void getPushMsg(String str, String str2, int i, final Callback callback) {
        this.listBeans.clear();
        MessageClient.getInstance().getApi().getMsg(0, i, DateUtil.getTimeMillins(this.year, this.month, this.day, 0, 0, 0), DateUtil.getTimeMillins(this.year, this.month, this.day, 23, 59, 59), str, str2, 1, new CallBack() { // from class: fragment.MessageFragment.15
            @Override // tools.CallBack
            public void compete(IoTResponse ioTResponse, String str3, int i2) {
                if (ioTResponse.getData() != null) {
                    MessageBean messageBean = (MessageBean) new Gson().fromJson(ioTResponse.getData().toString(), MessageBean.class);
                    for (int i3 = 0; i3 < messageBean.getEventList().size(); i3++) {
                        messageBean.getEventList().get(i3).setEventNickName(str3);
                        if (messageBean.getEventList().get(i3).getEventPicUrl() != null && !"".equals(messageBean.getEventList().get(i3).getEventPicUrl()) && messageBean.getEventList().get(i3).getEventType() != 2) {
                            MessageFragment.this.listBeans.add(messageBean.getEventList().get(i3));
                        }
                    }
                    callback.finish(i2);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void pushRefresh(PushRefresh pushRefresh) {
        getRefresh();
    }
}
