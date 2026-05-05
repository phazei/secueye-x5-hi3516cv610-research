package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.viewpager.widget.PagerAdapter;
import com.bumptech.glide.Glide;
import java.util.List;
import uk.co.senab.photoview.PhotoView;

/* JADX INFO: loaded from: classes.dex */
public class PhotoAdapter extends PagerAdapter {
    private List<String> photos;

    @Override // androidx.viewpager.widget.PagerAdapter
    public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getItemPosition(Object obj) {
        return -2;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public boolean isViewFromObject(View view2, Object obj) {
        return view2 == obj;
    }

    public PhotoAdapter(List<String> list) {
        this.photos = list;
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        List<String> list = this.photos;
        if (list == null || list.size() == 0) {
            return 0;
        }
        return this.photos.size();
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public Object instantiateItem(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        PhotoView photoView = new PhotoView(context);
        Glide.with(context).load(this.photos.get(i)).into(photoView);
        viewGroup.addView(photoView);
        photoView.setOnClickListener(new View.OnClickListener() { // from class: adapter.PhotoAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
            }
        });
        return photoView;
    }
}
