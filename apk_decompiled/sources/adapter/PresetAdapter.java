package adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import bean.PresetBean;
import com.alibaba.sdk.android.openaccount.ui.OpenAccountUIConstants;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.seculink.app.R;
import com.xiaomi.mipush.sdk.Constants;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import tools.BitmapUtil;
import tools.SpUtil;
import tools.Utils;
import view.GlideRoundTransform;

/* JADX INFO: loaded from: classes.dex */
public class PresetAdapter extends RecyclerView.Adapter<PresetAdapterHolder> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private List<Integer> intList;
    private Context mContext;
    private List<PresetBean> mData;
    private OnItemClickListener onItemClickListener;
    private int recycleHeight;

    public interface OnItemClickListener {
        void onItemClick(PresetBean presetBean, int i);

        void onItemDeleteClick(PresetBean presetBean, int i);
    }

    public List<Integer> getIntList() {
        return this.intList;
    }

    public PresetAdapter(Context context, List<PresetBean> list, List<Integer> list2) {
        this.mContext = context;
        this.mData = list;
        this.intList = list2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    @NonNull
    public PresetAdapterHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new PresetAdapterHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_preset_item, viewGroup, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(@NonNull final PresetAdapterHolder presetAdapterHolder, int i) {
        ImageView imageView;
        int i2;
        final PresetBean presetBean = this.mData.get(i);
        LinearLayout linearLayout = presetAdapterHolder.ll;
        LinearLayout linearLayout2 = presetAdapterHolder.ll1;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) linearLayout.getLayoutParams();
        layoutParams.height = 150;
        layoutParams.width = 266;
        linearLayout.setLayoutParams(layoutParams);
        linearLayout2.setLayoutParams(layoutParams);
        ImageView imageView2 = presetAdapterHolder.imageView1;
        ImageView imageView3 = presetAdapterHolder.imageView;
        RequestOptions requestOptionsTransform = new RequestOptions().transform(new GlideRoundTransform(this.mContext, 5));
        File[] fileArr = new File[4];
        Context context = this.mContext;
        StringBuilder sb = new StringBuilder();
        sb.append(Utils.getDevSnapKey(OpenAccountUIConstants.UNDER_LINE + presetBean.getIotId()));
        sb.append(OpenAccountUIConstants.UNDER_LINE);
        List<String> listHasPrefix = SpUtil.hasPrefix(context, sb.toString());
        StringBuilder sb2 = null;
        if (listHasPrefix.size() <= 0) {
            if (presetBean.getString() != null && !"".equals(presetBean.getString())) {
                sb2 = new StringBuilder(presetBean.getString());
            } else {
                sb2 = new StringBuilder();
            }
        } else {
            for (int i3 = 0; i3 < listHasPrefix.size(); i3++) {
                try {
                    fileArr[Integer.parseInt(listHasPrefix.get(i3).replace(".png", "").split(Constants.ACCEPT_TIME_SEPARATOR_SERVER)[1])] = new File(listHasPrefix.get(i3));
                    sb2.append(listHasPrefix.get(i3));
                    sb2.append(OpenAccountUIConstants.UNDER_LINE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ImageView imageView4 = presetAdapterHolder.add_preset_ipc;
        TextView textView = presetAdapterHolder.caretaker_top;
        TextView textView2 = presetAdapterHolder.caretakerText;
        textView2.setText("");
        textView.setVisibility(8);
        ImageView imageView5 = presetAdapterHolder.check_image;
        if ("".contentEquals(sb2)) {
            imageView4.setVisibility(0);
            if (i == 8) {
                textView2.setText(R.string.caretaker_ipc);
            }
            textView.setVisibility(8);
        } else {
            textView.setVisibility(0);
            textView.setText((i + 1) + "");
            if (i == 8) {
                textView.setText(R.string.caretaker_ipc);
            }
            imageView4.setVisibility(8);
        }
        if (sb2.length() <= 0 || !sb2.toString().toString().equals(imageView2.getTag())) {
            if (listHasPrefix.size() <= 0) {
                Glide.with(this.mContext).load(sb2.toString().toString()).apply(requestOptionsTransform.error(R.drawable.bg_white_rect_5dp).dontAnimate()).into(imageView3);
            } else {
                Bitmap bitmapCompoundBitmap = BitmapUtil.compoundBitmap(fileArr, 4);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmapCompoundBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                Glide.with(this.mContext).load(byteArrayOutputStream.toByteArray()).apply(requestOptionsTransform.error(R.drawable.bg_white_rect_5dp).dontAnimate()).into(imageView3);
            }
        }
        imageView2.setTag(sb2.toString().toString());
        imageView3.setOnClickListener(new View.OnClickListener() { // from class: adapter.PresetAdapter.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (presetBean.isEdit()) {
                    if (presetBean.isCanDelete()) {
                        if (presetAdapterHolder.getAdapterPosition() == 8) {
                            PresetAdapter.this.onItemClickListener.onItemDeleteClick(presetBean, 57);
                            return;
                        } else {
                            PresetAdapter.this.onItemClickListener.onItemDeleteClick(presetBean, presetAdapterHolder.getAdapterPosition());
                            return;
                        }
                    }
                    return;
                }
                if (presetAdapterHolder.getAdapterPosition() == 8) {
                    PresetAdapter.this.onItemClickListener.onItemClick(presetBean, 57);
                } else {
                    PresetAdapter.this.onItemClickListener.onItemClick(presetBean, presetAdapterHolder.getAdapterPosition());
                }
            }
        });
        if (presetBean.isEdit()) {
            textView2.setText("");
            i2 = 8;
            imageView4.setVisibility(8);
            textView.setVisibility(8);
            linearLayout2.setVisibility(0);
            if (presetBean.isCanDelete()) {
                if (presetBean.isSelected()) {
                    imageView = imageView5;
                    imageView.setVisibility(0);
                } else {
                    imageView = imageView5;
                    imageView.setVisibility(8);
                }
            } else {
                imageView = imageView5;
                imageView.setVisibility(8);
            }
        } else {
            imageView = imageView5;
            i2 = 8;
            linearLayout2.setVisibility(8);
        }
        if (!presetBean.isOwner()) {
            textView2.setText("");
            imageView4.setVisibility(i2);
            if (presetBean.isCanDelete()) {
                if (presetBean.isEdit()) {
                    if (presetBean.isSelected()) {
                        imageView.setVisibility(0);
                    } else {
                        imageView.setVisibility(i2);
                    }
                    textView.setVisibility(i2);
                } else {
                    imageView.setVisibility(i2);
                    textView.setVisibility(0);
                }
                textView.setText((i + 1) + "");
                if (i == 8) {
                    textView.setText(R.string.caretaker_ipc);
                }
                Glide.with(this.mContext).load(Integer.valueOf(R.drawable.pre_picture_ipc)).into(imageView3);
            }
        } else if (presetBean.isCanDelete()) {
            imageView4.setVisibility(8);
            textView2.setText("");
            if (presetBean.isEdit()) {
                if (presetBean.isSelected()) {
                    imageView.setVisibility(0);
                } else {
                    imageView.setVisibility(8);
                }
                textView.setVisibility(8);
            } else {
                imageView.setVisibility(8);
                textView.setVisibility(0);
            }
            textView.setText((i + 1) + "");
            if (i == 8) {
                textView.setText(R.string.caretaker_ipc);
            }
            Glide.with(this.mContext).load(sb2.toString().toString()).apply(requestOptionsTransform.error(R.drawable.pre_picture_ipc).dontAnimate()).into(imageView3);
        } else {
            imageView4.setVisibility(0);
        }
        if (presetBean.isEdit()) {
            return;
        }
        imageView.setVisibility(8);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mData.size();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class PresetAdapterHolder extends RecyclerView.ViewHolder {
        private ImageView add_preset_ipc;
        private TextView caretakerText;
        private TextView caretaker_top;
        private ImageView check_image;
        public ImageView imageView;
        public ImageView imageView1;
        public LinearLayout ll;
        public LinearLayout ll1;

        public PresetAdapterHolder(View view2) {
            super(view2);
            this.imageView = (ImageView) view2.findViewById(R.id.imageView);
            this.imageView1 = (ImageView) view2.findViewById(R.id.position_text);
            this.ll = (LinearLayout) view2.findViewById(R.id.ll);
            this.ll1 = (LinearLayout) view2.findViewById(R.id.ll1);
            this.add_preset_ipc = (ImageView) view2.findViewById(R.id.add_preset_ipc);
            this.caretakerText = (TextView) view2.findViewById(R.id.caretaker);
            this.caretaker_top = (TextView) view2.findViewById(R.id.caretaker_top);
            this.check_image = (ImageView) view2.findViewById(R.id.check_image);
        }
    }

    public void update(int i) {
        this.recycleHeight = i;
        notifyAll();
    }

    public List<PresetBean> getmData() {
        return this.mData;
    }

    public void setmData(List<PresetBean> list, List<Integer> list2) {
        this.mData = list;
        this.intList = list2;
    }
}
