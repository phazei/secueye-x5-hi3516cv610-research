package bean;

/* JADX INFO: loaded from: classes.dex */
public class TouchViewModel {
    private int bgResId;
    private int directionPicResId;
    private int mRoundBgPadding;
    private int mRoundBgRadius;
    private float mWholePadHeight;
    private float mWholePadWid;
    private float mWholeViewHeight;
    private float mWholeViewWid;
    private int touchBmpResId;
    private int mTouchBallRadius = 100;
    private boolean showDirectionPic = false;
    private PadStyle mPadStyle = PadStyle.FIXED;
    private PadLocationType mPadLocationType = PadLocationType.LEFT_BOT;

    public TouchViewModel(int i, int i2) {
        this.bgResId = i;
        this.touchBmpResId = i2;
    }

    public void setWholeViewSize(float f, float f2) {
        this.mWholeViewWid = f;
        this.mWholeViewHeight = f2;
    }

    public void setPadSize(float f, float f2) {
        this.mWholePadWid = f;
        this.mWholePadHeight = f2;
    }

    public void setContentSize(int i, int i2) {
        this.mRoundBgRadius = i;
        this.mTouchBallRadius = i2;
    }

    public int getTouchBallRadius() {
        return this.mTouchBallRadius;
    }

    public void setStyle(PadStyle padStyle, PadLocationType padLocationType) {
        this.mPadStyle = padStyle;
        this.mPadLocationType = padLocationType;
    }

    public void setTouchBallRadius(int i) {
        this.mTouchBallRadius = i;
    }

    public boolean isShowDirectionPic() {
        return this.showDirectionPic;
    }

    public int getDirectionPicResId() {
        return this.directionPicResId;
    }

    public void setDirectionPicResId(int i) {
        this.directionPicResId = i;
        this.showDirectionPic = true;
    }

    public int getRoundBgPadding() {
        return this.mRoundBgPadding;
    }

    public void setRoundBgPadding(int i) {
        this.mRoundBgPadding = i;
    }

    public int getBgResId() {
        return this.bgResId;
    }

    public void setBgResId(int i) {
        this.bgResId = i;
    }

    public int getTouchBmpResId() {
        return this.touchBmpResId;
    }

    public float getWholeViewWid() {
        return this.mWholeViewWid;
    }

    public float getWholeViewHeight() {
        return this.mWholeViewHeight;
    }

    public PadStyle getPadStyle() {
        return this.mPadStyle;
    }

    public PadLocationType getPadLocationType() {
        return this.mPadLocationType;
    }

    public float getWholePadHeight() {
        return this.mWholePadHeight;
    }

    public float getWholePadWid() {
        return this.mWholePadWid;
    }

    public int getRoundBgRadius() {
        return this.mRoundBgRadius;
    }
}
