package adapter;

import activity.PictureTestActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import bean.PicInfo;
import com.seculink.app.R;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import tools.ImageCache;

/* JADX INFO: loaded from: classes.dex */
public class PicTestAdapter extends android.widget.BaseAdapter implements PictureTestActivity.onViewScrollListener {
    private int ImgEnd;
    private int ImgStart;
    private List<PicInfo> data;
    private GridView gridView;
    private LayoutInflater inflater;
    private boolean isState = false;
    private ArrayList<Boolean> selectedLst = new ArrayList<>();
    private Set<ImgAsyncTask> taskSet = new HashSet();
    private boolean isFirstIn = true;

    @Override // android.widget.Adapter
    public long getItemId(int i) {
        return i;
    }

    public PicTestAdapter(Context context, List<PicInfo> list, GridView gridView) {
        this.inflater = LayoutInflater.from(context);
        this.data = list;
        this.gridView = gridView;
    }

    public void setSelectedItems(ArrayList<Boolean> arrayList) {
        this.selectedLst = arrayList;
    }

    public void setData(List<PicInfo> list) {
        this.data = list;
    }

    public void setIsState(boolean z) {
        this.isState = z;
        notifyDataSetChanged();
    }

    @Override // android.widget.Adapter
    public int getCount() {
        return this.data.size();
    }

    @Override // android.widget.Adapter
    public Object getItem(int i) {
        return this.data.get(i);
    }

    @Override // android.widget.Adapter
    public View getView(int i, View view2, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view2 == null) {
            view2 = this.inflater.inflate(R.layout.pic_picture_item, (ViewGroup) null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) view2.findViewById(R.id.pic_iv);
            viewHolder.textView = (TextView) view2.findViewById(R.id.title_tv);
            viewHolder.checkBox = (CheckBox) view2.findViewById(R.id.choose_cb);
            view2.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view2.getTag();
        }
        viewHolder.checkBox.setOnCheckedChangeListener(null);
        viewHolder.imageView.setTag(this.data.get(i).getThumbUrl());
        viewHolder.checkBox.setVisibility(this.isState ? 0 : 8);
        if (this.selectedLst.size() != 0) {
            viewHolder.checkBox.setChecked(this.selectedLst.get(i).booleanValue());
        }
        viewHolder.textView.setText(changeTime(this.data.get(i).getPicCreateTime().longValue()));
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() { // from class: adapter.PicTestAdapter.1
            @Override // android.widget.CompoundButton.OnCheckedChangeListener
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
            }
        });
        showImage(i, viewHolder.imageView, this.data.get(i).getThumbUrl());
        return view2;
    }

    private class ViewHolder {
        CheckBox checkBox;
        ImageView imageView;
        TextView textView;

        private ViewHolder() {
        }
    }

    private void showImage(int i, ImageView imageView, String str) {
        Bitmap image = ImageCache.getInstance().getImage(str);
        if (image != null) {
            imageView.setImageBitmap(image);
            return;
        }
        imageView.setImageResource(R.drawable.errors_no_pic);
        ImgAsyncTask imgAsyncTask = new ImgAsyncTask(str, i);
        imgAsyncTask.execute(str);
        this.taskSet.add(imgAsyncTask);
    }

    private String changeTime(long j) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA).format(new Date(j));
    }

    public void LoadImageByAsyncTask(int i, int i2) {
        while (i < i2) {
            String thumbUrl = this.data.get(i).getThumbUrl();
            Bitmap image = ImageCache.getInstance().getImage(thumbUrl);
            if (image != null) {
                ((ImageView) this.gridView.findViewWithTag(thumbUrl)).setImageBitmap(image);
            } else {
                ImgAsyncTask imgAsyncTask = new ImgAsyncTask(thumbUrl, i);
                imgAsyncTask.execute(thumbUrl);
                this.taskSet.add(imgAsyncTask);
            }
            i++;
        }
    }

    private class ImgAsyncTask extends AsyncTask<String, Void, Bitmap> {
        int position;
        String url;

        public ImgAsyncTask(String str, int i) {
            this.url = str;
            this.position = i;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public Bitmap doInBackground(String... strArr) throws Throwable {
            Bitmap bitmapDownloadImage = PicTestAdapter.this.downloadImage(this.url);
            if (bitmapDownloadImage != null) {
                ImageCache.getInstance().addImgIntoCache(this.url, bitmapDownloadImage);
            }
            return bitmapDownloadImage;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView = (ImageView) PicTestAdapter.this.gridView.findViewWithTag(this.url);
            if (imageView != null && bitmap != null) {
                imageView.setImageBitmap(bitmap);
            }
            PicTestAdapter.this.taskSet.remove(this);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:21:0x0045  */
    /* JADX WARN: Type inference failed for: r5v0, types: [java.lang.String] */
    /* JADX WARN: Type inference failed for: r5v2 */
    /* JADX WARN: Type inference failed for: r5v4, types: [java.net.HttpURLConnection] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public android.graphics.Bitmap downloadImage(java.lang.String r5) throws java.lang.Throwable {
        /*
            r4 = this;
            r0 = 0
            java.net.URL r1 = new java.net.URL     // Catch: java.lang.Throwable -> L34 java.lang.Exception -> L39
            r1.<init>(r5)     // Catch: java.lang.Throwable -> L34 java.lang.Exception -> L39
            java.net.URLConnection r5 = r1.openConnection()     // Catch: java.lang.Throwable -> L34 java.lang.Exception -> L39
            java.net.HttpURLConnection r5 = (java.net.HttpURLConnection) r5     // Catch: java.lang.Throwable -> L34 java.lang.Exception -> L39
            java.lang.String r1 = "GET"
            r5.setRequestMethod(r1)     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L42
            r1 = 5000(0x1388, float:7.006E-42)
            r5.setConnectTimeout(r1)     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L42
            r5.setReadTimeout(r1)     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L42
            r5.connect()     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L42
            int r1 = r5.getResponseCode()     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L42
            r2 = 200(0xc8, float:2.8E-43)
            if (r1 != r2) goto L2c
            java.io.InputStream r1 = r5.getInputStream()     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L42
            android.graphics.Bitmap r0 = android.graphics.BitmapFactory.decodeStream(r1)     // Catch: java.lang.Exception -> L32 java.lang.Throwable -> L42
        L2c:
            if (r5 == 0) goto L41
        L2e:
            r5.disconnect()
            goto L41
        L32:
            r1 = move-exception
            goto L3b
        L34:
            r5 = move-exception
            r3 = r0
            r0 = r5
            r5 = r3
            goto L43
        L39:
            r1 = move-exception
            r5 = r0
        L3b:
            r1.printStackTrace()     // Catch: java.lang.Throwable -> L42
            if (r5 == 0) goto L41
            goto L2e
        L41:
            return r0
        L42:
            r0 = move-exception
        L43:
            if (r5 == 0) goto L48
            r5.disconnect()
        L48:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: adapter.PicTestAdapter.downloadImage(java.lang.String):android.graphics.Bitmap");
    }

    public void cancelAllTask() {
        Set<ImgAsyncTask> set = this.taskSet;
        if (set != null) {
            Iterator<ImgAsyncTask> it = set.iterator();
            while (it.hasNext()) {
                it.next().cancel(false);
            }
        }
    }

    @Override // activity.PictureTestActivity.onViewScrollListener
    public void onScrollStateChanged(AbsListView absListView, int i) {
        if (i == 0) {
            LoadImageByAsyncTask(this.ImgStart, this.ImgEnd);
        } else {
            cancelAllTask();
        }
    }

    @Override // activity.PictureTestActivity.onViewScrollListener
    public void onScroll(AbsListView absListView, int i, int i2, int i3) {
        this.ImgStart = i;
        int i4 = this.ImgStart;
        this.ImgEnd = i4 + i2;
        if (!this.isFirstIn || i2 <= 0) {
            return;
        }
        LoadImageByAsyncTask(i4, this.ImgEnd);
        this.isFirstIn = false;
    }
}
