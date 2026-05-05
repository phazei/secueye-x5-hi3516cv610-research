package fragment;

import activity.LocalPhotoDetailsActivity;
import adapter.PictureAdapter;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import bean.FileBean;
import bean.RefreshPicture;
import com.aliyun.alink.linksdk.alcs.coap.resources.LinkFormat;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.exoplayer2.util.MimeTypes;
import com.seculink.app.BuildConfig;
import com.seculink.app.R;
import dialog.BaseDialog;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import tools.DateUtil;
import tools.FileUtil;
import tools.OnMultiClickListener;
import tools.ScreenUtil;
import tools.Utils;
import view.EditView;
import view.GridItemDecoration;

/* JADX INFO: loaded from: classes4.dex */
public class PictureFragment extends CommonFragment {
    private boolean checkAll;
    LinearLayout edit;
    EditView editView;
    private Handler handler = new Handler();
    private int hasSelectedNum = 0;
    ImageButton ib_cancel;
    private boolean isEidt;
    private boolean isMediaStoreDeleteSucceed;
    private GridLayoutManager layoutManager;
    private PictureAdapter mAdapter;
    RecyclerView recycler;
    private ExecutorService service;
    SwipeRefreshLayout srl;
    TextView tv_title;

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.picture_fragment;
    }

    static /* synthetic */ int access$208(PictureFragment pictureFragment) {
        int i = pictureFragment.hasSelectedNum;
        pictureFragment.hasSelectedNum = i + 1;
        return i;
    }

    static /* synthetic */ int access$210(PictureFragment pictureFragment) {
        int i = pictureFragment.hasSelectedNum;
        pictureFragment.hasSelectedNum = i - 1;
        return i;
    }

    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ExecutorService executorService = this.service;
        if (executorService != null) {
            executorService.shutdown();
            this.service = null;
        }
    }

    @Override // fragment.CommonFragment
    protected void initWidget(View view2) {
        super.initWidget(view2);
        this.recycler = (RecyclerView) view2.findViewById(R.id.recycler);
        this.srl = (SwipeRefreshLayout) view2.findViewById(R.id.srl);
        this.edit = (LinearLayout) view2.findViewById(R.id.edit);
        this.editView = (EditView) view2.findViewById(R.id.editView);
        this.ib_cancel = (ImageButton) view2.findViewById(R.id.ib_cancel);
        this.tv_title = (TextView) view2.findViewById(R.id.tv_title);
        this.service = Executors.newSingleThreadExecutor();
        this.ib_cancel.setOnClickListener(new OnMultiClickListener() { // from class: fragment.PictureFragment.1
            @Override // tools.OnMultiClickListener
            public void onMultiClick(View view3) {
                PictureFragment.this.ib_cancel.setVisibility(8);
                PictureFragment.this.isEidt = false;
                PictureFragment.this.srl.post(new Runnable() { // from class: fragment.PictureFragment.1.1
                    @Override // java.lang.Runnable
                    public void run() {
                        PictureFragment.this.srl.setEnabled(true);
                    }
                });
                PictureFragment.this.editView.setVisibility(8);
                if (PictureFragment.this.mAdapter.getData().size() > 0) {
                    for (T t : PictureFragment.this.mAdapter.getData()) {
                        if (t.getItemType() == 1) {
                            t.setEdit(PictureFragment.this.isEidt);
                            t.setSelected(false);
                        }
                    }
                    PictureFragment.this.mAdapter.notifyDataSetChanged();
                }
                PictureFragment.this.tv_title.setText(R.string.picture);
            }
        });
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        this.layoutManager = new GridLayoutManager(getActivity(), 4);
        this.recycler.setLayoutManager(this.layoutManager);
        int iDp2Px = ScreenUtil.dp2Px(getActivity(), 2.0f);
        this.recycler.addItemDecoration(new GridItemDecoration(iDp2Px, iDp2Px));
        this.mAdapter = new PictureAdapter();
        this.mAdapter.bindToRecyclerView(this.recycler);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= 24) {
            builder.detectFileUriExposure();
        }
        this.recycler.setItemAnimator(null);
        this.mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() { // from class: fragment.PictureFragment.2
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemClickListener
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view3, int i) {
                Uri uriForFile;
                FileBean fileBean = (FileBean) PictureFragment.this.mAdapter.getItem(i);
                if (fileBean.getItemType() == 1) {
                    if (PictureFragment.this.isEidt) {
                        ((FileBean) PictureFragment.this.mAdapter.getData().get(i)).setSelected(!((FileBean) PictureFragment.this.mAdapter.getData().get(i)).isSelected());
                        PictureFragment.this.mAdapter.notifyItemChanged(i);
                        if (((FileBean) PictureFragment.this.mAdapter.getData().get(i)).isSelected()) {
                            PictureFragment.access$208(PictureFragment.this);
                        } else {
                            PictureFragment.access$210(PictureFragment.this);
                        }
                        PictureFragment.this.tv_title.setText(PictureFragment.this.getResources().getString(R.string.have_choose_items, Integer.valueOf(PictureFragment.this.hasSelectedNum)));
                        PictureFragment.this.checkAll = false;
                        PictureFragment.this.editView.setTvCheckAllDrawable(false);
                        if (PictureFragment.this.hasSelectedNum > 0) {
                            PictureFragment.this.editView.setTvDeleteDrawable(true);
                            return;
                        } else {
                            PictureFragment.this.editView.setTvDeleteDrawable(false);
                            return;
                        }
                    }
                    if (fileBean.getName().endsWith(".mp4")) {
                        Intent intent = new Intent("android.intent.action.VIEW");
                        if (Build.VERSION.SDK_INT >= 24) {
                            uriForFile = FileProvider.getUriForFile(PictureFragment.this.getActivity(), BuildConfig.APPLICATION_ID.concat(".provider"), new File(fileBean.getUrl()));
                            intent.addFlags(1);
                        } else {
                            uriForFile = Uri.parse("file://" + fileBean.getUrl());
                        }
                        intent.setDataAndType(uriForFile, MimeTypes.VIDEO_MP4);
                        PictureFragment.this.startActivity(intent);
                        return;
                    }
                    ArrayList arrayList = new ArrayList();
                    for (T t : PictureFragment.this.mAdapter.getData()) {
                        if (t.getMediaType() == 1) {
                            arrayList.add(t.getUrl());
                        }
                    }
                    int iIndexOf = arrayList.indexOf(fileBean.getUrl());
                    Intent intent2 = new Intent(PictureFragment.this.getActivity(), (Class<?>) LocalPhotoDetailsActivity.class);
                    intent2.putExtra("photos", arrayList);
                    intent2.putExtra("pos", iIndexOf);
                    intent2.putExtra("isFromPicture", true);
                    intent2.putExtra("isHttpUrl", false);
                    PictureFragment.this.startActivity(intent2);
                }
            }
        });
        this.mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() { // from class: fragment.PictureFragment.3
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.chad.library.adapter.base.BaseQuickAdapter.OnItemChildClickListener
            public void onItemChildClick(BaseQuickAdapter baseQuickAdapter, View view3, int i) {
                if (view3.getId() == R.id.bt_select && ((FileBean) PictureFragment.this.mAdapter.getItem(i)).getItemType() == 1) {
                    ((FileBean) PictureFragment.this.mAdapter.getData().get(i)).setSelected(!((FileBean) PictureFragment.this.mAdapter.getData().get(i)).isSelected());
                    PictureFragment.this.mAdapter.notifyItemChanged(i);
                    if (((FileBean) PictureFragment.this.mAdapter.getData().get(i)).isSelected()) {
                        PictureFragment.access$208(PictureFragment.this);
                    } else {
                        PictureFragment.access$210(PictureFragment.this);
                    }
                    PictureFragment.this.tv_title.setText(PictureFragment.this.getResources().getString(R.string.have_choose_items, Integer.valueOf(PictureFragment.this.hasSelectedNum)));
                    PictureFragment.this.checkAll = false;
                    PictureFragment.this.editView.setTvCheckAllDrawable(false);
                    if (PictureFragment.this.hasSelectedNum > 0) {
                        PictureFragment.this.editView.setTvDeleteDrawable(true);
                    } else {
                        PictureFragment.this.editView.setTvDeleteDrawable(false);
                    }
                }
            }
        });
        this.layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() { // from class: fragment.PictureFragment.4
            @Override // androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
            public int getSpanSize(int i) {
                if (PictureFragment.this.mAdapter.getItemViewType(i) == 1) {
                    return 1;
                }
                return PictureFragment.this.layoutManager.getSpanCount();
            }
        });
        this.edit.setOnClickListener(new View.OnClickListener() { // from class: fragment.PictureFragment.5
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                if (PictureFragment.this.isEidt) {
                    return;
                }
                PictureFragment.this.isEidt = true;
                PictureFragment.this.srl.setEnabled(false);
                PictureFragment.this.ib_cancel.setVisibility(0);
                PictureFragment.this.hasSelectedNum = 0;
                PictureFragment.this.checkAll = false;
                PictureFragment.this.editView.setTvCheckAllDrawable(false);
                PictureFragment.this.editView.setTvDeleteDrawable(false);
                PictureFragment.this.tv_title.setText(PictureFragment.this.getResources().getString(R.string.have_choose_items, 0));
                if (PictureFragment.this.mAdapter.getData().size() > 0) {
                    PictureFragment.this.editView.setVisibility(0);
                    for (T t : PictureFragment.this.mAdapter.getData()) {
                        if (t.getItemType() == 1) {
                            t.setEdit(PictureFragment.this.isEidt);
                            t.setSelected(false);
                        }
                    }
                    PictureFragment.this.mAdapter.notifyDataSetChanged();
                }
            }
        });
        this.editView.addOnClickListener(new AnonymousClass6());
        getPicturelist();
    }

    /* JADX INFO: renamed from: fragment.PictureFragment$6, reason: invalid class name */
    class AnonymousClass6 implements EditView.OnClickListener {
        @Override // view.EditView.OnClickListener
        public void OnInverseClick(View view2) {
        }

        AnonymousClass6() {
        }

        @Override // view.EditView.OnClickListener
        public void OnCheckAllClick(View view2) {
            PictureFragment.this.hasSelectedNum = 0;
            if (!PictureFragment.this.checkAll) {
                PictureFragment.this.checkAll = true;
                for (T t : PictureFragment.this.mAdapter.getData()) {
                    if (t.getItemType() == 1) {
                        t.setSelected(true);
                        PictureFragment.access$208(PictureFragment.this);
                    }
                }
                PictureFragment.this.editView.setTvCheckAllDrawable(true);
                PictureFragment.this.editView.setTvDeleteDrawable(true);
            } else {
                PictureFragment.this.checkAll = false;
                for (T t2 : PictureFragment.this.mAdapter.getData()) {
                    if (t2.getItemType() == 1) {
                        t2.setSelected(false);
                    }
                }
                PictureFragment.this.editView.setTvCheckAllDrawable(false);
                PictureFragment.this.editView.setTvDeleteDrawable(false);
            }
            PictureFragment.this.mAdapter.notifyDataSetChanged();
            PictureFragment.this.tv_title.setText(PictureFragment.this.getResources().getString(R.string.have_choose_items, Integer.valueOf(PictureFragment.this.hasSelectedNum)));
        }

        /* JADX INFO: renamed from: fragment.PictureFragment$6$1, reason: invalid class name */
        class AnonymousClass1 implements View.OnClickListener {
            AnonymousClass1() {
            }

            /* JADX INFO: renamed from: fragment.PictureFragment$6$1$1, reason: invalid class name and collision with other inner class name */
            class RunnableC02901 implements Runnable {
                RunnableC02901() {
                }

                @Override // java.lang.Runnable
                public void run() {
                    final int i = PictureFragment.this.hasSelectedNum;
                    int size = PictureFragment.this.mAdapter.getData().size() - 1;
                    while (size >= 0) {
                        FileBean fileBean = (FileBean) PictureFragment.this.mAdapter.getData().get(size);
                        if (fileBean.isSelected() && FileUtil.delete(fileBean.getUrl())) {
                            PictureFragment.access$210(PictureFragment.this);
                            PictureFragment.this.mAdapter.getData().remove(fileBean);
                            if (fileBean.getMediaType() == 1) {
                                fileBean.parent.photos.remove(fileBean);
                            } else {
                                fileBean.parent.videos.remove(fileBean);
                            }
                            if (fileBean.parent != null && fileBean.parent.photos.size() == 0 && fileBean.parent.videos.size() == 0) {
                                PictureFragment.this.mAdapter.getData().remove(fileBean.parent);
                                size--;
                            }
                        }
                        size--;
                    }
                    PictureFragment.this.handler.post(new Runnable() { // from class: fragment.PictureFragment.6.1.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            PictureFragment.this.mAdapter.notifyDataSetChanged();
                            if (PictureFragment.this.mAdapter.getData().size() <= 0) {
                                PictureFragment.this.isEidt = false;
                                PictureFragment.this.editView.setVisibility(8);
                                PictureFragment.this.ib_cancel.setVisibility(8);
                                PictureFragment.this.edit.setVisibility(8);
                                PictureFragment.this.srl.post(new Runnable() { // from class: fragment.PictureFragment.6.1.1.1.1
                                    @Override // java.lang.Runnable
                                    public void run() {
                                        PictureFragment.this.srl.setEnabled(true);
                                    }
                                });
                                PictureFragment.this.tv_title.setText(R.string.picture);
                            } else {
                                PictureFragment.this.tv_title.setText(PictureFragment.this.getResources().getString(R.string.have_choose_items, Integer.valueOf(PictureFragment.this.hasSelectedNum)));
                            }
                            if (PictureFragment.this.hasSelectedNum == 0) {
                                PictureFragment.this.checkAll = false;
                                PictureFragment.this.editView.setTvCheckAllDrawable(false);
                                PictureFragment.this.editView.setTvDeleteDrawable(false);
                                Toast.makeText(PictureFragment.this.getActivity(), R.string.delete_success, 1).show();
                            } else {
                                if (PictureFragment.this.hasSelectedNum < i) {
                                    PictureFragment.this.checkAll = false;
                                    PictureFragment.this.editView.setTvCheckAllDrawable(false);
                                }
                                Toast.makeText(PictureFragment.this.getActivity(), R.string.delete_fail, 1).show();
                            }
                            PictureFragment.this.dismissProgressDialog();
                        }
                    });
                }
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                PictureFragment.this.showProgressDialog(R.string.deleting);
                PictureFragment.this.service.execute(new RunnableC02901());
            }
        }

        @Override // view.EditView.OnClickListener
        public void OnDeleteClick(View view2) {
            new BaseDialog.Builder().view(R.layout.dialog_delete_camera).content(PictureFragment.this.getString(R.string.sure_cancel)).leftBtnText(PictureFragment.this.getString(R.string.sure_delete)).rightBtnText(PictureFragment.this.getString(R.string.cancel)).clickLeft(new AnonymousClass1()).canCancel(false).create().show(PictureFragment.this.getActivity().getSupportFragmentManager(), "");
        }

        @Override // view.EditView.OnClickListener
        public void onDownloadClick(View view2) {
            if (PictureFragment.this.hasSelectedNum <= 0) {
                Toast.makeText(PictureFragment.this.getActivity(), R.string.have_no_choose, 0).show();
            } else {
                PictureFragment.this.showProgressDialog(R.string.downloading);
                PictureFragment.this.service.execute(new Runnable() { // from class: fragment.PictureFragment.6.2
                    @Override // java.lang.Runnable
                    public void run() {
                        OutputStream outputStreamOpenOutputStream;
                        PictureFragment.this.isMediaStoreDeleteSucceed = true;
                        for (T t : PictureFragment.this.mAdapter.getData()) {
                            if (t.isSelected()) {
                                if (t.getMediaType() == 1) {
                                    FileInputStream fileInputStream = null;
                                    try {
                                        fileInputStream = new FileInputStream(t.getUrl());
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Bitmap bitmapDecodeStream = BitmapFactory.decodeStream(fileInputStream);
                                    PictureFragment.this.saveBitmap(bitmapDecodeStream, System.currentTimeMillis() + ".jpg");
                                } else if (t.getMediaType() == 2) {
                                    try {
                                        FileInputStream fileInputStream2 = new FileInputStream(new File(t.getUrl()));
                                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), System.currentTimeMillis() + ".mp4");
                                        ContentResolver contentResolver = PictureFragment.this.getActivity().getContentResolver();
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("title", file.getName());
                                        contentValues.put("_display_name", file.getName());
                                        contentValues.put("relative_path", "Movies/");
                                        contentValues.put("datetaken", Long.valueOf(t.getCreateTime()));
                                        contentValues.put("date_added", Long.valueOf(t.getCreateTime() / 1000));
                                        contentValues.put("date_modified", Long.valueOf(System.currentTimeMillis() / 1000));
                                        contentValues.put("_size", Long.valueOf(file.length()));
                                        contentValues.put("mime_type", MimeTypes.VIDEO_MP4);
                                        Uri uriInsert = contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, contentValues);
                                        if (uriInsert != null && (outputStreamOpenOutputStream = PictureFragment.this.getContext().getContentResolver().openOutputStream(uriInsert)) != null) {
                                            byte[] bArr = new byte[1024];
                                            while (true) {
                                                int i = fileInputStream2.read(bArr, 0, bArr.length);
                                                if (i == -1) {
                                                    break;
                                                } else {
                                                    outputStreamOpenOutputStream.write(bArr, 0, i);
                                                }
                                            }
                                            outputStreamOpenOutputStream.flush();
                                            fileInputStream2.close();
                                            outputStreamOpenOutputStream.close();
                                        }
                                        PictureFragment.this.getActivity().getApplication().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", uriInsert));
                                    } catch (IOException e2) {
                                        e2.printStackTrace();
                                        PictureFragment.this.isMediaStoreDeleteSucceed = false;
                                    }
                                }
                            }
                        }
                        PictureFragment.this.handler.post(new Runnable() { // from class: fragment.PictureFragment.6.2.1
                            @Override // java.lang.Runnable
                            public void run() {
                                if (PictureFragment.this.isMediaStoreDeleteSucceed) {
                                    Toast.makeText(PictureFragment.this.getActivity(), R.string.download_success, 0).show();
                                } else {
                                    Toast.makeText(PictureFragment.this.getActivity(), R.string.download_failed, 0).show();
                                }
                                PictureFragment.this.dismissProgressDialog();
                            }
                        });
                    }
                });
            }
        }
    }

    private Bitmap getImageThumbnail(String str, int i, int i2) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        options.inJustDecodeBounds = false;
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        int i5 = i4 / i;
        int i6 = i3 / i2;
        if (i5 < i6) {
            i6 = i5;
        }
        if (i6 <= 0) {
            i6 = 1;
        }
        options.inSampleSize = i6;
        return ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(str, options), i, (int) (i / ((i4 * 1.0f) / i3)), 2);
    }

    private Bitmap getVideoThumbnail(String str, int i, int i2) {
        Bitmap bitmapCreateVideoThumbnail = ThumbnailUtils.createVideoThumbnail(str, 1);
        System.out.println("w" + bitmapCreateVideoThumbnail.getWidth());
        System.out.println(LinkFormat.HOST + bitmapCreateVideoThumbnail.getHeight());
        return ThumbnailUtils.extractThumbnail(bitmapCreateVideoThumbnail, i, i2, 2);
    }

    @Override // fragment.CommonFragment
    protected void onFirstInitData() {
        super.onFirstInitData();
        this.srl.measure(0, 0);
        this.srl.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        this.srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { // from class: fragment.PictureFragment.7
            @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
            public void onRefresh() {
                PictureFragment.this.getPicturelist();
            }
        });
        getPicturelist();
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i, @NonNull String[] strArr, @NonNull int[] iArr) {
        if (i == 4374 && iArr.length > 0 && iArr[0] == 0) {
            getPicturelist();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void PictureUpdate(RefreshPicture refreshPicture) {
        getPicturelist();
    }

    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View viewOnCreateView = super.onCreateView(layoutInflater, viewGroup, bundle);
        this.isEidt = false;
        this.editView.setVisibility(8);
        this.ib_cancel.setVisibility(8);
        this.hasSelectedNum = 0;
        this.checkAll = false;
        this.editView.setTvCheckAllDrawable(false);
        this.editView.setTvDeleteDrawable(false);
        this.tv_title.setText(R.string.picture);
        this.srl.post(new Runnable() { // from class: fragment.PictureFragment.8
            @Override // java.lang.Runnable
            public void run() {
                PictureFragment.this.srl.setEnabled(true);
            }
        });
        if (this.mAdapter.getData().size() > 0) {
            for (T t : this.mAdapter.getData()) {
                if (t.getItemType() == 1) {
                    t.setEdit(this.isEidt);
                    t.setSelected(false);
                }
            }
            this.mAdapter.notifyDataSetChanged();
        }
        return viewOnCreateView;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Type inference failed for: r0v1, types: [fragment.PictureFragment$9] */
    public void getPicturelist() {
        this.srl.setRefreshing(true);
        new Thread() { // from class: fragment.PictureFragment.9
            @Override // java.lang.Thread, java.lang.Runnable
            public void run() {
                StringBuilder sb = new StringBuilder();
                PictureFragment pictureFragment = PictureFragment.this;
                sb.append(pictureFragment.getFilesPath(pictureFragment.getContext()));
                sb.append("/photo/");
                String string = sb.toString();
                final ArrayList arrayList = new ArrayList();
                File file = new File(string);
                StringBuilder sb2 = new StringBuilder();
                PictureFragment pictureFragment2 = PictureFragment.this;
                sb2.append(pictureFragment2.getFilesPath(pictureFragment2.getContext()));
                sb2.append("/video/");
                File file2 = new File(sb2.toString());
                arrayList.clear();
                if (file.exists()) {
                    for (File file3 : file.listFiles()) {
                        if (file3.getName().endsWith(".jpg")) {
                            Log.e("run: 照片", file3.getName() + "");
                            FileBean fileBean = new FileBean();
                            fileBean.setItemType(1);
                            fileBean.setMediaType(1);
                            fileBean.setName(file3.getName());
                            fileBean.setUrl(file3.getPath());
                            long j = Long.parseLong(file3.getName().substring(0, file3.getName().indexOf(".jpg")));
                            fileBean.setCreateTime(j);
                            int[] calendarDate = DateUtil.getCalendarDate(new Date(j));
                            fileBean.setYear(calendarDate[0]);
                            fileBean.setMonth(calendarDate[1]);
                            fileBean.setDay(calendarDate[2]);
                            fileBean.setSize(file3.length());
                            arrayList.add(fileBean);
                        }
                    }
                }
                if (file2.exists()) {
                    for (File file4 : file2.listFiles()) {
                        if (file4.getName().endsWith(".mp4")) {
                            Log.e("run: 视频", file4.getName() + "");
                            FileBean fileBean2 = new FileBean();
                            fileBean2.setItemType(1);
                            fileBean2.setMediaType(2);
                            fileBean2.setName(file4.getName());
                            fileBean2.setUrl(file4.getPath());
                            String strSubstring = file4.getName().substring(0, file4.getName().indexOf(".mp4"));
                            if (PictureFragment.isNumeric(strSubstring)) {
                                long j2 = Long.parseLong(strSubstring);
                                fileBean2.setCreateTime(j2);
                                int[] calendarDate2 = DateUtil.getCalendarDate(new Date(j2));
                                fileBean2.setYear(calendarDate2[0]);
                                fileBean2.setMonth(calendarDate2[1]);
                                fileBean2.setDay(calendarDate2[2]);
                                fileBean2.setSize(file4.length());
                                arrayList.add(fileBean2);
                            }
                        }
                    }
                }
                Collections.sort(arrayList, new Comparator<FileBean>() { // from class: fragment.PictureFragment.9.1
                    @Override // java.util.Comparator
                    public int compare(FileBean fileBean3, FileBean fileBean4) {
                        return Long.compare(fileBean3.getCreateTime(), fileBean4.getCreateTime()) * (-1);
                    }
                });
                FileBean fileBean3 = null;
                int i = 0;
                while (i < arrayList.size()) {
                    FileBean fileBean4 = (FileBean) arrayList.get(i);
                    if (i == 0) {
                        fileBean3 = new FileBean();
                        fileBean3.setItemType(0);
                        fileBean3.setYear(fileBean4.getYear());
                        fileBean3.setMonth(fileBean4.getMonth());
                        fileBean3.setDay(fileBean4.getDay());
                        fileBean3.setCreateTime(fileBean4.getCreateTime());
                        arrayList.add(i, fileBean3);
                        i++;
                        if (fileBean4.getMediaType() == 1) {
                            fileBean3.photos.add(fileBean4);
                        } else if (fileBean4.getMediaType() == 2) {
                            fileBean3.videos.add(fileBean4);
                        }
                        fileBean4.parent = fileBean3;
                    } else {
                        FileBean fileBean5 = (FileBean) arrayList.get(i - 1);
                        if (fileBean4.getYear() != fileBean5.getYear() || fileBean4.getMonth() != fileBean5.getMonth() || fileBean4.getDay() != fileBean5.getDay()) {
                            fileBean3 = new FileBean();
                            fileBean3.setItemType(0);
                            fileBean3.setYear(fileBean4.getYear());
                            fileBean3.setMonth(fileBean4.getMonth());
                            fileBean3.setDay(fileBean4.getDay());
                            fileBean3.setCreateTime(fileBean4.getCreateTime());
                            arrayList.add(i, fileBean3);
                            i++;
                        }
                        if (fileBean4.getMediaType() == 1) {
                            fileBean3.photos.add(fileBean4);
                        } else if (fileBean4.getMediaType() == 2) {
                            fileBean3.videos.add(fileBean4);
                        }
                        fileBean4.parent = fileBean3;
                    }
                    i++;
                }
                PictureFragment.this.handler.post(new Runnable() { // from class: fragment.PictureFragment.9.2
                    @Override // java.lang.Runnable
                    public void run() {
                        PictureFragment.this.mAdapter.replaceData(arrayList);
                        if (PictureFragment.this.srl != null && PictureFragment.this.srl.isRefreshing()) {
                            PictureFragment.this.srl.setRefreshing(false);
                        }
                        if (PictureFragment.this.mAdapter.getData().size() > 0) {
                            PictureFragment.this.edit.setVisibility(0);
                        } else {
                            PictureFragment.this.edit.setVisibility(8);
                        }
                    }
                });
            }
        }.start();
    }

    public static boolean isNumeric(String str) {
        char cCharAt;
        int length = str.length();
        do {
            length--;
            if (length < 0) {
                return true;
            }
            cCharAt = str.charAt(length);
            if (cCharAt < '0') {
                return false;
            }
        } while (cCharAt <= '9');
        return false;
    }

    public String getFilesPath(Context context) {
        String path;
        if ("mounted".equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            path = context.getExternalFilesDir(null).getPath();
        } else {
            path = context.getFilesDir().getPath();
        }
        return path + "//" + Utils.getUserPhone();
    }

    public boolean saveBitmap(Bitmap bitmap, String str) {
        String str2;
        String str3 = Build.BRAND;
        if (str3.equals("xiaomi")) {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + str;
        } else if (str3.equalsIgnoreCase("Huawei")) {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/Camera/" + str;
        } else {
            str2 = Environment.getExternalStorageDirectory().getPath() + "/DCIM/" + str;
        }
        if (Build.VERSION.SDK_INT >= 29) {
            saveSignImage(str, bitmap);
            return true;
        }
        Log.v("saveBitmap brand", "" + str3);
        File file = new File(str2);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream)) {
                fileOutputStream.flush();
                fileOutputStream.close();
                if (Build.VERSION.SDK_INT >= 29) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("_data", file.getAbsolutePath());
                    contentValues.put("mime_type", "image/jpeg");
                    getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                } else {
                    MediaStore.Images.Media.insertImage(getContext().getContentResolver(), file.getAbsolutePath(), str, (String) null);
                }
            }
            getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.parse("file://" + str2)));
            return true;
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", "FileNotFoundException:" + e.getMessage().toString());
            e.printStackTrace();
            return false;
        } catch (IOException e2) {
            Log.e("IOException", "IOException:" + e2.getMessage().toString());
            e2.printStackTrace();
            return false;
        } catch (Exception e3) {
            Log.e("IOException", "IOException:" + e3.getMessage().toString());
            e3.printStackTrace();
            return false;
        }
    }

    public void saveSignImage(String str, Bitmap bitmap) {
        OutputStream outputStreamOpenOutputStream;
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("_display_name", str);
            if (Build.VERSION.SDK_INT >= 29) {
                contentValues.put("relative_path", "DCIM/");
            } else {
                contentValues.put("_data", Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath());
            }
            contentValues.put("mime_type", "image/JPEG");
            Uri uriInsert = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (uriInsert == null || (outputStreamOpenOutputStream = getContext().getContentResolver().openOutputStream(uriInsert)) == null) {
                return;
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStreamOpenOutputStream);
            outputStreamOpenOutputStream.flush();
            outputStreamOpenOutputStream.close();
        } catch (Exception unused) {
        }
    }

    public void saveVideo(String str) {
        File file = new File(str);
        getContext().sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", getContext().getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, getVideoContentValues(getContext(), file, System.currentTimeMillis()))));
    }

    public static ContentValues getVideoContentValues(Context context, File file, long j) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", file.getName());
        contentValues.put("_display_name", file.getName());
        contentValues.put("mime_type", MimeTypes.VIDEO_MP4);
        contentValues.put("datetaken", Long.valueOf(j));
        contentValues.put("date_modified", Long.valueOf(j));
        contentValues.put("date_added", Long.valueOf(j));
        contentValues.put("_data", file.getAbsolutePath());
        contentValues.put("_size", Long.valueOf(file.length()));
        return contentValues;
    }
}
