package activity;

import adapter.PicTestAdapter;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import bean.DevPictureFile;
import bean.PicInfo;
import com.alibaba.fastjson.JSON;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback;
import com.aliyun.iot.aep.sdk.apiclient.callback.IoTResponse;
import com.aliyun.iot.aep.sdk.apiclient.request.IoTRequest;
import com.seculink.app.R;
import dialog.PictureDialog;
import enums.PicRequestTypeEnums;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import sdk.IPCManager;

/* JADX INFO: loaded from: classes.dex */
public class PictureTestActivity extends AppCompatActivity {
    private Button cancelBtn;
    private Button clearBtn;
    private Button delBtn;
    private RelativeLayout headerRl;
    private String iotId;
    private boolean isChooseState;
    RelativeLayout layout_main;
    private Toast mToast;
    private GridView picGv;
    private ArrayList<PicInfo> picInfoLst;
    private PicTestAdapter picTestAdapter;
    private Button refreshBtn;
    private Button selectAllBtn;
    private ArrayList<Boolean> selectItems;
    private TextView titleTv;
    private String TAG = getClass().getSimpleName();
    private int index = 0;
    private int requestNum = 9;
    private boolean haveMoreData = true;
    private long endTime = System.currentTimeMillis();
    private AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() { // from class: activity.PictureTestActivity.7
        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> adapterView, View view2, int i, long j) {
            if (!PictureTestActivity.this.isChooseState) {
                PictureDialog.getInstance().openDialog(PictureTestActivity.this, ((PicInfo) PictureTestActivity.this.picInfoLst.get(i)).getPicUrl());
                return;
            }
            CheckBox checkBox = (CheckBox) view2.findViewById(R.id.choose_cb);
            if (checkBox.isChecked()) {
                checkBox.setChecked(false);
                PictureTestActivity.this.selectItems.set(i, false);
            } else {
                checkBox.setChecked(true);
                PictureTestActivity.this.selectItems.set(i, true);
            }
            PictureTestActivity.this.picTestAdapter.setSelectedItems(PictureTestActivity.this.selectItems);
            PictureTestActivity.this.picTestAdapter.notifyDataSetChanged();
            PictureTestActivity.this.setSelectNum();
        }
    };
    private AdapterView.OnItemLongClickListener itemLongClickListener = new AdapterView.OnItemLongClickListener() { // from class: activity.PictureTestActivity.8
        @Override // android.widget.AdapterView.OnItemLongClickListener
        public boolean onItemLongClick(AdapterView<?> adapterView, View view2, int i, long j) {
            if (!PictureTestActivity.this.isChooseState) {
                PictureTestActivity.this.selectItems = new ArrayList();
                for (int i2 = 0; i2 < PictureTestActivity.this.picInfoLst.size(); i2++) {
                    PictureTestActivity.this.selectItems.add(false);
                }
                ((CheckBox) view2.findViewById(R.id.choose_cb)).setChecked(true);
                PictureTestActivity.this.selectItems.set(i, true);
                PictureTestActivity.this.setChooseState(true);
                PictureTestActivity.this.picTestAdapter.setIsState(true);
                PictureTestActivity.this.setSelectNum();
                PictureTestActivity.this.picTestAdapter.setSelectedItems(PictureTestActivity.this.selectItems);
                PictureTestActivity.this.refreshGV();
            }
            return true;
        }
    };
    private List<onViewScrollListener> listenerList = new LinkedList();
    private boolean canLoadData = false;
    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() { // from class: activity.PictureTestActivity.9
        @Override // android.widget.AbsListView.OnScrollListener
        public void onScrollStateChanged(AbsListView absListView, int i) {
            if (PictureTestActivity.this.listenerList != null && PictureTestActivity.this.listenerList.size() > 0) {
                Iterator it = PictureTestActivity.this.listenerList.iterator();
                while (it.hasNext()) {
                    ((onViewScrollListener) it.next()).onScrollStateChanged(absListView, i);
                }
            }
            if (PictureTestActivity.this.isChooseState || i != 0) {
                return;
            }
            absListView.getChildAt(absListView.getChildCount() - 1);
            if (absListView.canScrollVertically(1)) {
                return;
            }
            if (PictureTestActivity.this.canLoadData) {
                if (PictureTestActivity.this.haveMoreData) {
                    synchronized (PictureTestActivity.class) {
                        PictureTestActivity.this.getPictureLst(0L, PictureTestActivity.this.endTime, PictureTestActivity.this.index, PictureTestActivity.this.requestNum, 0);
                        PictureTestActivity.this.canLoadData = false;
                    }
                    return;
                }
                PictureTestActivity.this.showToast("已经没有更多数据了！");
                return;
            }
            if (PictureTestActivity.this.haveMoreData) {
                PictureTestActivity.this.showToast("已经拖动至底部，再次拖动即可翻页");
                PictureTestActivity.this.canLoadData = true;
            } else {
                PictureTestActivity.this.showToast("已经没有更多数据了！");
            }
        }

        @Override // android.widget.AbsListView.OnScrollListener
        public void onScroll(AbsListView absListView, int i, int i2, int i3) {
            if (PictureTestActivity.this.listenerList == null || PictureTestActivity.this.listenerList.size() <= 0) {
                return;
            }
            Iterator it = PictureTestActivity.this.listenerList.iterator();
            while (it.hasNext()) {
                ((onViewScrollListener) it.next()).onScroll(absListView, i, i2, i3);
            }
        }
    };
    private boolean isLoading = false;

    public interface onViewScrollListener {
        void onScroll(AbsListView absListView, int i, int i2, int i3);

        void onScrollStateChanged(AbsListView absListView, int i);
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_picture_test);
        this.layout_main = (RelativeLayout) findViewById(R.id.layout_main);
        setEdgeToEdge(this.layout_main);
        this.picInfoLst = new ArrayList<>();
        this.selectItems = new ArrayList<>();
        initData();
        initView();
    }

    public void setEdgeToEdge(View view2) {
        ViewCompat.setOnApplyWindowInsetsListener(view2, new OnApplyWindowInsetsListener() { // from class: activity.-$$Lambda$PictureTestActivity$5Xfw7A0ZaxHzQ-LU5ZOGNH2hcCg
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view3, WindowInsetsCompat windowInsetsCompat) {
                return PictureTestActivity.lambda$setEdgeToEdge$0(view3, windowInsetsCompat);
            }
        });
    }

    static /* synthetic */ WindowInsetsCompat lambda$setEdgeToEdge$0(View view2, WindowInsetsCompat windowInsetsCompat) {
        Insets insets = windowInsetsCompat.getInsets(WindowInsetsCompat.Type.systemBars());
        view2.setPadding(insets.left, view2.getPaddingTop(), insets.right, insets.bottom);
        return WindowInsetsCompat.CONSUMED;
    }

    private void initData() {
        this.iotId = getIntent().getStringExtra("iotId");
        this.endTime = System.currentTimeMillis();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    protected void onResume() {
        super.onResume();
        getPictureLst(0L, this.endTime, this.index, this.requestNum, 0);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshGV() {
        runOnUiThread(new Runnable() { // from class: activity.PictureTestActivity.1
            @Override // java.lang.Runnable
            public void run() {
                PictureTestActivity.this.picTestAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initView() {
        this.titleTv = (TextView) findViewById(R.id.title_tv);
        this.cancelBtn = (Button) findViewById(R.id.bt_cancel);
        this.delBtn = (Button) findViewById(R.id.bt_del);
        this.selectAllBtn = (Button) findViewById(R.id.bt_select_all);
        this.clearBtn = (Button) findViewById(R.id.bt_clear);
        this.refreshBtn = (Button) findViewById(R.id.bt_refresh);
        this.cancelBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.PictureTestActivity.2
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PictureTestActivity.this.quitChooseState();
            }
        });
        this.delBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.PictureTestActivity.3
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PictureTestActivity.this.delSelections();
            }
        });
        this.selectAllBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.PictureTestActivity.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PictureTestActivity.this.setSelectAll(true);
            }
        });
        this.clearBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.PictureTestActivity.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PictureTestActivity.this.setSelectAll(false);
            }
        });
        this.refreshBtn.setOnClickListener(new View.OnClickListener() { // from class: activity.PictureTestActivity.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PictureTestActivity.this.refreshPicData();
            }
        });
        this.headerRl = (RelativeLayout) findViewById(R.id.rl_pic_test_header);
        this.picGv = (GridView) findViewById(R.id.gv_pic);
        this.picTestAdapter = new PicTestAdapter(this, this.picInfoLst, this.picGv);
        this.picGv.setAdapter((ListAdapter) this.picTestAdapter);
        this.picGv.setOnItemClickListener(this.itemClickListener);
        this.picGv.setOnItemLongClickListener(this.itemLongClickListener);
        this.picGv.setOnScrollListener(this.scrollListener);
        registerViewScrollListener(this.picTestAdapter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void quitChooseState() {
        this.selectItems.clear();
        this.picTestAdapter.setIsState(false);
        setChooseState(false);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void refreshPicData() {
        if (this.picInfoLst == null) {
            this.picInfoLst = new ArrayList<>();
        }
        this.picInfoLst.clear();
        this.index = 0;
        this.haveMoreData = true;
        getPictureLst(0L, this.endTime, 0, this.requestNum, 0);
    }

    public void registerViewScrollListener(onViewScrollListener onviewscrolllistener) {
        if (this.listenerList == null) {
            this.listenerList = new LinkedList();
        }
        if (this.listenerList.contains(onviewscrolllistener)) {
            return;
        }
        this.listenerList.add(onviewscrolllistener);
    }

    public void unregisterViewScrollListener(onViewScrollListener onviewscrolllistener) {
        List<onViewScrollListener> list = this.listenerList;
        if (list == null) {
            this.listenerList = new LinkedList();
        } else if (list.contains(onviewscrolllistener)) {
            this.listenerList.remove(onviewscrolllistener);
        }
    }

    public void showToast(String str) {
        Toast toast = this.mToast;
        if (toast == null) {
            this.mToast = Toast.makeText(getApplicationContext(), str, 1);
        } else {
            toast.setText(str);
        }
        this.mToast.show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setChooseState(boolean z) {
        this.isChooseState = z;
        if (z) {
            this.refreshBtn.setVisibility(8);
            this.selectAllBtn.setVisibility(0);
            this.cancelBtn.setVisibility(0);
            setSelectNum();
            this.delBtn.setVisibility(0);
            return;
        }
        this.titleTv.setText("查看图片");
        this.refreshBtn.setVisibility(0);
        this.selectAllBtn.setVisibility(8);
        this.clearBtn.setVisibility(8);
        this.cancelBtn.setVisibility(8);
        this.delBtn.setVisibility(8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSelectNum() {
        Iterator<Boolean> it = this.selectItems.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (it.next().booleanValue()) {
                i++;
            }
        }
        this.titleTv.setText("已选择" + i + "项");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setSelectAll(boolean z) {
        for (int i = 0; i < this.selectItems.size(); i++) {
            this.selectItems.set(i, Boolean.valueOf(z));
        }
        this.picTestAdapter.setSelectedItems(this.selectItems);
        refreshGV();
        setSelectNum();
        this.selectAllBtn.setVisibility(z ? 8 : 0);
        this.clearBtn.setVisibility(z ? 0 : 8);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void delSelections() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!this.selectItems.contains(true)) {
            builder.setTitle("提示").setMessage("当前未选中项目").setPositiveButton("确认", (DialogInterface.OnClickListener) null).create().show();
            return;
        }
        builder.setTitle("提示");
        builder.setMessage("确认删除所选项目？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() { // from class: activity.PictureTestActivity.10
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                LinkedList linkedList = new LinkedList();
                for (int i2 = 0; i2 < PictureTestActivity.this.picInfoLst.size(); i2++) {
                    if (((Boolean) PictureTestActivity.this.selectItems.get(i2)).booleanValue()) {
                        PictureTestActivity pictureTestActivity = PictureTestActivity.this;
                        if (!pictureTestActivity.hasSamePic(linkedList, (PicInfo) pictureTestActivity.picInfoLst.get(i2))) {
                            linkedList.add(PictureTestActivity.this.picInfoLst.get(i2));
                        }
                    }
                }
                if (linkedList.size() < 1) {
                    return;
                }
                if (linkedList.size() == 1) {
                    PictureTestActivity.this.deletePic(((PicInfo) linkedList.get(0)).getPicId(), ((PicInfo) linkedList.get(0)).getPicCreateTime().longValue());
                } else {
                    PictureTestActivity.this.deletePicBatch(linkedList);
                }
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.cancel), (DialogInterface.OnClickListener) null);
        builder.create().show();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean hasSamePic(List<PicInfo> list, PicInfo picInfo) {
        return list.contains(picInfo);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void onDeletePicSuccess() {
        for (int i = 0; i < this.picInfoLst.size(); i++) {
            if (this.selectItems.get(i).booleanValue()) {
                this.picInfoLst.set(i, null);
            }
        }
        if (this.index < 0) {
            this.index = 0;
        }
        while (this.picInfoLst.contains(null)) {
            this.picInfoLst.remove((Object) null);
        }
        this.selectItems = new ArrayList<>();
        for (int i2 = 0; i2 < this.picInfoLst.size(); i2++) {
            this.selectItems.add(false);
        }
        Log.e(this.TAG, "onDeletePicSuccess    picInfoLst.size():" + this.picInfoLst.size());
        this.picTestAdapter.setSelectedItems(this.selectItems);
        this.picTestAdapter.setData(this.picInfoLst);
        refreshGV();
        if (this.picInfoLst.size() == 0) {
            this.picTestAdapter.setIsState(false);
            setChooseState(false);
            if (this.haveMoreData) {
                refreshPicData();
                return;
            }
            return;
        }
        runOnUiThread(new Runnable() { // from class: activity.PictureTestActivity.11
            @Override // java.lang.Runnable
            public void run() {
                PictureTestActivity.this.setSelectNum();
            }
        });
    }

    @Override // androidx.appcompat.app.AppCompatActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 4) {
            if (this.isChooseState) {
                this.titleTv.setText("已选择1项");
                quitChooseState();
                return true;
            }
            finish();
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void getPictureLst(long j, long j2, int i, final int i2, int i3) {
        Log.d(this.TAG, "getPictureLst  startTime:" + j + "  endTime:" + j2 + "  pageStart:" + i + "  pageSize:" + i2);
        if (this.isLoading) {
            return;
        }
        this.isLoading = true;
        IPCManager.getInstance().getDevice(this.iotId).queryDevPictureFileList(j, j2, i, i2, PicRequestTypeEnums.ALL.getCode(), i3, new IoTCallback() { // from class: activity.PictureTestActivity.12
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                PictureTestActivity.this.isLoading = false;
                Log.d(PictureTestActivity.this.TAG, "queryDevPictureFileList ToC onFailure:" + exc.toString());
                PictureTestActivity.this.showToast("获取照片列表失败，e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                PictureTestActivity.this.isLoading = false;
                Log.d(PictureTestActivity.this.TAG, "queryDevPictureFileList ToC onResponse:" + ioTResponse.getCode() + "  ID:" + ioTResponse.getId() + "   Data:" + ioTResponse.getData());
                if (ioTResponse.getCode() == 200) {
                    if (ioTResponse.getData() != null && !"".equals(ioTResponse.getData().toString().trim())) {
                        List array = JSON.parseArray(ioTResponse.getData().toString(), PicInfo.class);
                        if (array == null || array.size() <= 0) {
                            return;
                        }
                        if (PictureTestActivity.this.picInfoLst == null) {
                            PictureTestActivity.this.picInfoLst = new ArrayList();
                        }
                        for (int i4 = 0; i4 < array.size(); i4++) {
                            PictureTestActivity pictureTestActivity = PictureTestActivity.this;
                            if (!pictureTestActivity.hasSamePic(pictureTestActivity.picInfoLst, (PicInfo) array.get(i4))) {
                                PictureTestActivity.this.picInfoLst.add(array.get(i4));
                            }
                        }
                        PictureTestActivity.this.index++;
                        if (array.size() < i2) {
                            PictureTestActivity.this.haveMoreData = false;
                        }
                        PictureTestActivity.this.refreshGV();
                        return;
                    }
                    PictureTestActivity.this.showToast("获取照片列表成功：data为null");
                    return;
                }
                PictureTestActivity.this.showToast("获取照片列表成功，code:" + ioTResponse.getCode() + "   data:" + ioTResponse.getData());
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deletePic(String str, long j) {
        IPCManager.getInstance().getDevice(this.iotId).deleteDevPictureFile(str, j, new IoTCallback() { // from class: activity.PictureTestActivity.13
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.e(PictureTestActivity.this.TAG, "deletePic ToC onFailure:" + exc.toString());
                PictureTestActivity.this.showToast("删除照片失败，e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Log.d(PictureTestActivity.this.TAG, "deletePic ToC onResponse:" + ioTResponse.getCode() + "  ID:" + ioTResponse.getId() + "   Data:" + ioTResponse.getData());
                PictureTestActivity.this.processDeletePicResponse(ioTResponse);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processDeletePicResponse(IoTResponse ioTResponse) {
        if (ioTResponse.getCode() == 200) {
            if (ioTResponse.getData() != null && !"".equals(ioTResponse.getData().toString().trim())) {
                if (((Boolean) ioTResponse.getData()).booleanValue()) {
                    showToast("删除照片成功");
                    runOnUiThread(new Runnable() { // from class: activity.PictureTestActivity.14
                        @Override // java.lang.Runnable
                        public void run() {
                            PictureTestActivity.this.onDeletePicSuccess();
                        }
                    });
                    return;
                } else {
                    showToast("删除照片失败");
                    return;
                }
            }
            showToast("删除照片成功：data为null");
            return;
        }
        showToast("删除照片成功，code:" + ioTResponse.getCode() + "   data:" + ioTResponse.getData());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void deletePicBatch(List<PicInfo> list) {
        LinkedList linkedList = new LinkedList();
        for (PicInfo picInfo : list) {
            DevPictureFile devPictureFile = new DevPictureFile();
            devPictureFile.setIotId(picInfo.getIotId());
            devPictureFile.setPicCreateTime(picInfo.getPicCreateTime());
            devPictureFile.setPicId(picInfo.getPicId());
            linkedList.add(devPictureFile);
        }
        IPCManager.getInstance().batchDeleteDevPictureFile(linkedList, new IoTCallback() { // from class: activity.PictureTestActivity.15
            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onFailure(IoTRequest ioTRequest, Exception exc) {
                Log.e(PictureTestActivity.this.TAG, "batchDeleteDevPictureFile ToC onFailure:" + exc.toString());
                PictureTestActivity.this.showToast("批量删除照片失败，e:" + exc.toString());
            }

            @Override // com.aliyun.iot.aep.sdk.apiclient.callback.IoTCallback
            public void onResponse(IoTRequest ioTRequest, IoTResponse ioTResponse) {
                Log.d(PictureTestActivity.this.TAG, "batchDeleteDevPictureFile ToC onResponse:" + ioTResponse.getCode() + "  ID:" + ioTResponse.getId() + "   msg:" + ioTResponse.getLocalizedMsg());
                if (ioTResponse.getCode() == 200) {
                    if (ioTResponse.getData() != null && !"".equals(ioTResponse.getData().toString().trim())) {
                        if (((Boolean) ioTResponse.getData()).booleanValue()) {
                            PictureTestActivity.this.showToast("批量删除照片成功");
                            PictureTestActivity.this.runOnUiThread(new Runnable() { // from class: activity.PictureTestActivity.15.1
                                @Override // java.lang.Runnable
                                public void run() {
                                    PictureTestActivity.this.onDeletePicSuccess();
                                }
                            });
                            return;
                        } else {
                            PictureTestActivity.this.showToast("批量删除照片失败");
                            return;
                        }
                    }
                    PictureTestActivity.this.showToast("批量删除照片失败：data为null");
                    return;
                }
                String localizedMsg = ioTResponse.getLocalizedMsg();
                String message = ioTResponse.getMessage();
                PictureTestActivity.this.showToast("批量删除照片失败，code:" + ioTResponse.getCode() + "   msg:" + localizedMsg + "  " + message);
            }
        });
    }
}
