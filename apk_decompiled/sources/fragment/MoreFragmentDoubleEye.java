package fragment;

import adapter.GridSpaceItemDecoration;
import adapter.HomeTopicPagerAdapter;
import adapter.TopicAdapterDoubleEye;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import bean.TopicBean;
import com.seculink.app.R;
import java.util.ArrayList;
import java.util.List;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.DummyPagerTitleView;
import tools.SharePreferenceManager;

/* JADX INFO: loaded from: classes4.dex */
public class MoreFragmentDoubleEye extends CommonFragment implements TopicAdapterDoubleEye.OnItemClickListener {
    private ImageView back;
    private int currentInfrarred;
    private int faceDetectionAbility;
    private String[] infrarredMode;
    private boolean isDetecting;
    private boolean isOwner;
    boolean kuaXianSwitch;
    private boolean lightVisible;
    private MyBackListener listener;
    private ArrayList<RecyclerView> mList;
    private FragmentContextChangeListener mListener;
    private boolean netVisible;
    boolean quYuSwitch;
    private boolean shopVisible;
    private boolean smartDoorVisible;
    private int supportMotionDetect;
    private String switchText;

    /* JADX INFO: renamed from: view, reason: collision with root package name */
    private View f7878view;
    private ArrayList<TopicBean> mTopicData = new ArrayList<>();
    private String iotId = "";
    private int selectShowMode = 0;
    private List<String> showList = new ArrayList();

    public interface FragmentContextChangeListener {
        void CloudBackPlay();

        void CustomerService();

        void Floodlight();

        void GPSLocate();

        void Locate();

        void SDplayBack();

        void doubleSameWindow(int i);

        void fourPicture(TopicBean topicBean, RecyclerView.Adapter adapter2, int i);

        void humanoidTracking(TopicBean topicBean, RecyclerView.Adapter adapter2, int i, int i2, boolean z);

        void lightMode();

        void mall();

        void netWorkSwitch();

        void preset();

        void share();

        void smart();

        void zoom();
    }

    public interface MyBackListener {
        void backOut();
    }

    @Override // fragment.CommonFragment
    protected int getContentLayoutId() {
        return R.layout.more_ipc_double_layout;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // fragment.CommonFragment, androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        this.listener = (MyBackListener) context;
        this.mListener = (FragmentContextChangeListener) context;
        super.onAttach(context);
    }

    @Override // fragment.CommonFragment
    protected void initWidget(View view2) {
        super.initWidget(view2);
        this.f7878view = view2;
        this.infrarredMode = getResources().getStringArray(R.array.InfrarredMode);
        Bundle arguments = getArguments();
        this.showList.add(getResources().getString(R.string.show_mode1));
        this.showList.add(getResources().getString(R.string.show_mode2));
        this.showList.add(getResources().getString(R.string.show_mode3));
        if (arguments != null) {
            this.iotId = arguments.getString("iotId");
            this.isDetecting = arguments.getBoolean("isDetecting");
            this.lightVisible = arguments.getBoolean("lightVisible");
            this.shopVisible = arguments.getBoolean("shopVisible");
            this.smartDoorVisible = arguments.getBoolean("smartDoorVisible");
            this.netVisible = arguments.getBoolean("netVisible");
            this.faceDetectionAbility = arguments.getInt("faceDetectionAbility");
            this.supportMotionDetect = arguments.getInt("supportMotionDetect");
            this.isOwner = arguments.getBoolean("isOwner");
            if (this.lightVisible) {
                this.currentInfrarred = arguments.getInt("currentInfrarred", this.currentInfrarred);
            }
            if (this.netVisible) {
                this.switchText = arguments.getString("switchText", this.switchText);
            }
        }
        initTopicData(this.isDetecting, this.lightVisible, this.netVisible, this.currentInfrarred, this.switchText, this.supportMotionDetect, this.shopVisible, this.smartDoorVisible, this.selectShowMode);
        initTypeViewPager(view2, 2, 3);
        this.back = (ImageView) view2.findViewById(R.id.back);
        this.back.setOnClickListener(new View.OnClickListener() { // from class: fragment.MoreFragmentDoubleEye.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view3) {
                MoreFragmentDoubleEye.this.listener.backOut();
            }
        });
    }

    private void initTopicData(boolean z, boolean z2, boolean z3, int i, String str, int i2, boolean z4, boolean z5, int i3) {
        this.mTopicData.clear();
        if (this.isOwner) {
            this.mTopicData.add(new TopicBean(R.drawable.share_ipc, getResources().getString(R.string.share)));
        }
        if (SharePreferenceManager.getInstance().getEventRecord(this.iotId) == 1) {
            this.mTopicData.add(new TopicBean(R.drawable.icon_card_back_false, getResources().getString(R.string.sd_playback)));
            this.mTopicData.add(new TopicBean(R.drawable.icon_cloud_back_false, getResources().getString(R.string.cloud_playback)));
        } else {
            this.mTopicData.add(new TopicBean(R.drawable.video_back, getString(R.string.video_back), false));
        }
        this.selectShowMode = i3;
        this.mTopicData.add(new TopicBean(R.drawable.gunball_false, this.showList.get(i3), false));
        if (z) {
            if (i2 == 0) {
                this.mTopicData.add(new TopicBean(R.drawable.humanoid_hracking_ipc_light, getResources().getString(R.string.track), true));
            } else {
                this.mTopicData.add(new TopicBean(R.drawable.humanoid_hracking_ipc_light, getResources().getString(R.string.track), true));
            }
        } else if (i2 == 0) {
            this.mTopicData.add(new TopicBean(R.drawable.humanoid_hracking_ipc, getResources().getString(R.string.track), false));
        } else {
            this.mTopicData.add(new TopicBean(R.drawable.humanoid_hracking_ipc, getResources().getString(R.string.track), false));
        }
        this.mTopicData.add(new TopicBean(R.drawable.zoom_out, getResources().getString(R.string.zoom)));
        if (z4) {
            this.mTopicData.add(new TopicBean(R.drawable.mall_ipc, getResources().getString(R.string.mall_ipc)));
        }
        if (SharePreferenceManager.getInstance().getFloodlightSwitchShow(this.iotId) == 1) {
            if (SharePreferenceManager.getInstance().getFloodlightSwitch(this.iotId) == 1) {
                this.mTopicData.add(new TopicBean(R.drawable.icon_on_full, getResources().getString(R.string.floodlight), true));
            } else {
                this.mTopicData.add(new TopicBean(R.drawable.icon_off_full, getResources().getString(R.string.floodlight), false));
            }
        }
        if (SharePreferenceManager.getInstance().getMapShow(this.iotId) == 1) {
            this.mTopicData.add(new TopicBean(R.drawable.icon_more_loaction, getResources().getString(R.string.locate)));
        }
        if (SharePreferenceManager.getInstance().getLocationAbility(this.iotId) != -1) {
            StringBuilder sbReverse = new StringBuilder(Integer.toBinaryString(SharePreferenceManager.getInstance().getLocationAbility(this.iotId))).reverse();
            for (int i4 = 0; i4 < sbReverse.length(); i4++) {
                if (sbReverse.charAt(i4) - '0' == 1 && i4 == 1) {
                    this.mTopicData.add(new TopicBean(R.drawable.icon_more_gps, getResources().getString(R.string.map_gps)));
                }
            }
        }
    }

    private void initTypeViewPager(View view2, int i, int i2) {
        ViewPager viewPager = (ViewPager) view2.findViewById(R.id.topicViewPager);
        MagicIndicator magicIndicator = (MagicIndicator) view2.findViewById(R.id.topicIndicator);
        int i3 = i * i2;
        final int size = this.mTopicData.size() / i3;
        if (this.mTopicData.size() % i3 > 0) {
            size++;
        }
        this.mList = new ArrayList<>();
        int i4 = 0;
        while (i4 < size) {
            RecyclerView recyclerView = new RecyclerView(getContext());
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), i2));
            recyclerView.addItemDecoration(new GridSpaceItemDecoration(i2, 1, 1));
            int i5 = i4 * i3;
            i4++;
            int size2 = i4 * i3;
            if (size2 > this.mTopicData.size()) {
                size2 = this.mTopicData.size();
            }
            TopicAdapterDoubleEye topicAdapterDoubleEye = new TopicAdapterDoubleEye(getContext(), new ArrayList(this.mTopicData.subList(i5, size2)));
            topicAdapterDoubleEye.setOnItemClickListener(this);
            recyclerView.setAdapter(topicAdapterDoubleEye);
            this.mList.add(recyclerView);
        }
        viewPager.setAdapter(new HomeTopicPagerAdapter(this.mList));
        viewPager.setOffscreenPageLimit(size - 1);
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() { // from class: fragment.MoreFragmentDoubleEye.2
            @Override // net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
            public int getCount() {
                return size;
            }

            @Override // net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
            public IPagerTitleView getTitleView(Context context, int i6) {
                return new DummyPagerTitleView(context);
            }

            @Override // net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator linePagerIndicator = new LinePagerIndicator(context);
                linePagerIndicator.setMode(2);
                linePagerIndicator.setLineHeight(UIUtil.dip2px(context, 3.0d));
                linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 66 / size));
                linePagerIndicator.setRoundRadius(UIUtil.dip2px(context, 3.0d));
                linePagerIndicator.setStartInterpolator(new AccelerateInterpolator());
                linePagerIndicator.setEndInterpolator(new DecelerateInterpolator(3.0f));
                linePagerIndicator.setColors(Integer.valueOf(ContextCompat.getColor(context, R.color.colorAccent)));
                return linePagerIndicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);
    }

    @Override // adapter.TopicAdapterDoubleEye.OnItemClickListener
    public void onTopicItemClick(TopicBean topicBean, final int i) {
        if (topicBean.getIcon() == R.drawable.icon_cloud_back_false) {
            this.mListener.CloudBackPlay();
            return;
        }
        if (topicBean.getIcon() == R.drawable.icon_card_back_false || topicBean.getIcon() == R.drawable.video_back) {
            this.mListener.SDplayBack();
            return;
        }
        if (topicBean.getIcon() == R.drawable.light_ipc) {
            this.mListener.lightMode();
            return;
        }
        if (topicBean.getTitle().equals(getResources().getString(R.string.track)) || topicBean.getTitle().equals(getResources().getString(R.string.mobile_tracking))) {
            int i2 = 1;
            this.kuaXianSwitch = SharePreferenceManager.getInstance().getCrossLineEnable(this.iotId).intValue() != 0;
            this.quYuSwitch = SharePreferenceManager.getInstance().getAreaDetectEnable(this.iotId).intValue() != 0;
            if (this.faceDetectionAbility == 1) {
                if (SharePreferenceManager.getInstance().getTlrClRgn(this.iotId).intValue() == 1) {
                    if (!this.isDetecting) {
                        if (this.quYuSwitch) {
                            i2 = this.kuaXianSwitch ? 7 : 5;
                        } else if (this.kuaXianSwitch) {
                            i2 = 3;
                        }
                    } else if (this.quYuSwitch) {
                        i2 = this.kuaXianSwitch ? 6 : 4;
                    } else {
                        i2 = this.kuaXianSwitch ? 2 : 0;
                    }
                } else if (!this.isDetecting) {
                    i2 = 0;
                }
                if (this.isDetecting) {
                    this.mListener.humanoidTracking(topicBean, this.mList.get(0).getAdapter(), i, i2, true);
                    return;
                } else {
                    this.mListener.humanoidTracking(topicBean, this.mList.get(0).getAdapter(), i, i2, true);
                    return;
                }
            }
            if (this.isDetecting) {
                this.mListener.humanoidTracking(topicBean, this.mList.get(0).getAdapter(), i, 0, false);
                return;
            } else {
                this.mListener.humanoidTracking(topicBean, this.mList.get(0).getAdapter(), i, 1, false);
                return;
            }
        }
        if (topicBean.getTitle().equals(getResources().getString(R.string.four_pictures))) {
            this.mListener.fourPicture(topicBean, this.mList.get(0).getAdapter(), i);
            return;
        }
        if (topicBean.getTitle().equals(getResources().getString(R.string.mall_ipc))) {
            this.mListener.mall();
            return;
        }
        if (topicBean.getIcon() == R.drawable.fourg_ipc) {
            this.mListener.netWorkSwitch();
            return;
        }
        if (topicBean.getTitle().equals(getResources().getString(R.string.preset_ipc))) {
            this.mListener.preset();
            return;
        }
        if (topicBean.getTitle().equals(getResources().getString(R.string.zoom))) {
            this.mListener.zoom();
            return;
        }
        if (topicBean.getTitle().equals(getResources().getString(R.string.customer_service))) {
            this.mListener.CustomerService();
            return;
        }
        if (topicBean.getTitle().equals(getResources().getString(R.string.share))) {
            this.mListener.share();
            return;
        }
        if (topicBean.getTitle().equals(getResources().getString(R.string.show_mode1)) || topicBean.getTitle().equals(getResources().getString(R.string.show_mode2)) || topicBean.getTitle().equals(getResources().getString(R.string.show_mode3))) {
            new Handler().postDelayed(new Runnable() { // from class: fragment.MoreFragmentDoubleEye.3
                @Override // java.lang.Runnable
                public void run() {
                    MoreFragmentDoubleEye.this.mListener.doubleSameWindow(i);
                }
            }, 300L);
            return;
        }
        if (topicBean.getTitle().equals(getResources().getString(R.string.floodlight))) {
            this.mListener.Floodlight();
        } else if (topicBean.getTitle().equals(getResources().getString(R.string.locate))) {
            this.mListener.Locate();
        } else if (topicBean.getTitle().equals(getResources().getString(R.string.map_gps))) {
            this.mListener.GPSLocate();
        }
    }

    public static int dp2px(Context context, float f) {
        return (int) TypedValue.applyDimension(1, f, context.getResources().getDisplayMetrics());
    }

    public void updateData(boolean z, boolean z2, boolean z3, int i, String str, boolean z4, boolean z5, boolean z6, int i2, int i3) {
        if (isAdded()) {
            initTopicData(z, z2, z3, i, str, i2, z4, z5, i3);
            initTypeViewPager(this.f7878view, 2, 3);
        }
    }

    public boolean isDetecting() {
        return this.isDetecting;
    }

    public void setDetecting(boolean z) {
        this.isDetecting = z;
    }

    public void hideBack() {
        this.back.setVisibility(8);
    }

    public void showBack() {
        this.back.setVisibility(0);
    }
}
