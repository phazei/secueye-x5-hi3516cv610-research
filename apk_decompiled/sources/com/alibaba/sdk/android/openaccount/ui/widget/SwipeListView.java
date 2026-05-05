package com.alibaba.sdk.android.openaccount.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import com.alibaba.sdk.android.openaccount.trace.AliSDKLogger;
import com.alibaba.sdk.android.openaccount.util.ResourceUtils;

/* JADX INFO: loaded from: classes.dex */
public class SwipeListView extends ListView {
    private static final String TAG = "oa_ui_swipe";
    private View mCurrentItemView;
    private ScrollLinearLayout mCurrentScrollView;
    private int mDeleteButtonWidth;
    private View mDeleteView;
    private float mFirstX;
    private float mFirstY;
    private boolean mIsGiveupTouchEvent;
    private Boolean mIsHorizontal;
    private boolean mIsShown;
    private boolean mIsStatusMarked;
    private AtomListListener mListener;
    private STATUS mStatus;
    private OnStatusChangeListener mStatusListener;
    private boolean mSupportQuickMark;
    private boolean mSupportSwipeDelete;

    public interface AtomListListener {
        void deleteItem(int i);
    }

    public enum DRAG_STATUS {
        MOVING,
        FINISH
    }

    public interface OnStatusChangeListener {
        void onStatusChange(View view2, int i, READ_STATUS read_status, DRAG_STATUS drag_status);
    }

    public enum READ_STATUS {
        UNREAD,
        READ
    }

    private enum STATUS {
        DRAGGING,
        SHOW,
        IDLE
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SwipeListView(Context context) {
        super(context);
        this.mDeleteButtonWidth = 80;
        this.mIsShown = false;
        this.mSupportSwipeDelete = true;
        this.mSupportQuickMark = false;
        this.mIsStatusMarked = true;
        this.mStatusListener = null;
        this.mIsGiveupTouchEvent = false;
        this.mStatus = STATUS.IDLE;
        this.mListener = (AtomListListener) context;
        this.mDeleteButtonWidth = context.getResources().getDimensionPixelSize(ResourceUtils.getIdentifier(context, "dimen", "ali_sdk_openaccount_swipe_delete_button_width"));
    }

    /* JADX WARN: Multi-variable type inference failed */
    public SwipeListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDeleteButtonWidth = 80;
        this.mIsShown = false;
        this.mSupportSwipeDelete = true;
        this.mSupportQuickMark = false;
        this.mIsStatusMarked = true;
        this.mStatusListener = null;
        this.mIsGiveupTouchEvent = false;
        this.mStatus = STATUS.IDLE;
        this.mListener = (AtomListListener) context;
        this.mDeleteButtonWidth = context.getResources().getDimensionPixelSize(ResourceUtils.getIdentifier(context, "dimen", "ali_sdk_openaccount_swipe_delete_button_width"));
    }

    public SwipeListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDeleteButtonWidth = 80;
        this.mIsShown = false;
        this.mSupportSwipeDelete = true;
        this.mSupportQuickMark = false;
        this.mIsStatusMarked = true;
        this.mStatusListener = null;
        this.mIsGiveupTouchEvent = false;
        this.mStatus = STATUS.IDLE;
        this.mDeleteButtonWidth = context.getResources().getDimensionPixelSize(ResourceUtils.getIdentifier(context, "dimen", "ali_sdk_openaccount_swipe_delete_button_width"));
    }

    private boolean judgeScrollDirection(float f, float f2) {
        boolean z = false;
        if (Math.abs(f) > 30.0f) {
            if (Math.abs(f) > Math.abs(f2) * 2.0f) {
                this.mIsHorizontal = true;
            } else {
                this.mIsHorizontal = false;
            }
            z = true;
        }
        if (AliSDKLogger.isDebugEnabled()) {
            AliSDKLogger.d(TAG, "judgeScrollDirection, mIsHorizontal=" + this.mIsHorizontal);
        }
        return z;
    }

    @Override // android.widget.AbsListView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (!this.mSupportSwipeDelete) {
            return super.onTouchEvent(motionEvent);
        }
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                this.mIsStatusMarked = false;
                this.mFirstX = x;
                this.mFirstY = y;
                this.mIsHorizontal = null;
                final int iPointToPosition = pointToPosition((int) x, (int) y);
                if (AliSDKLogger.isDebugEnabled()) {
                    AliSDKLogger.d(TAG, "onTouchEvent, ACTION_DOWN, motionPosition=" + iPointToPosition);
                }
                if (iPointToPosition >= 0) {
                    View childAt = getChildAt(iPointToPosition - getFirstVisiblePosition());
                    if (this.mIsShown) {
                        if (childAt != this.mCurrentItemView) {
                            this.mIsGiveupTouchEvent = true;
                            hiddenDeleteButton(false, true);
                            return true;
                        }
                        childAt.setPressed(false);
                    } else {
                        this.mCurrentItemView = childAt;
                        this.mDeleteView = this.mCurrentItemView.findViewById(ResourceUtils.getRId("alisdk_openaccount_id_item_delete_bt"));
                        this.mDeleteView.setVisibility(4);
                        this.mDeleteView.setOnClickListener(new View.OnClickListener() { // from class: com.alibaba.sdk.android.openaccount.ui.widget.SwipeListView.1
                            @Override // android.view.View.OnClickListener
                            public void onClick(View view2) {
                                if (AliSDKLogger.isDebugEnabled()) {
                                    AliSDKLogger.d(SwipeListView.TAG, "Delete button clicked.");
                                }
                                SwipeListView.this.mListener.deleteItem(iPointToPosition);
                            }
                        });
                        this.mCurrentScrollView = (ScrollLinearLayout) this.mCurrentItemView.findViewById(ResourceUtils.getRId("alisdk_openaccount_id_item_swipe_left"));
                    }
                } else if (this.mIsShown) {
                    hiddenDeleteButton(false, false);
                }
                this.mIsGiveupTouchEvent = false;
                if (AliSDKLogger.isDebugEnabled()) {
                    AliSDKLogger.d(TAG, "onTouchEvent, ACTION_DOWN");
                }
                break;
            case 1:
                Log.d(TAG, "onTouchEvent, ACTION_UP, mIsHorizontal=" + this.mIsHorizontal);
                if (this.mIsGiveupTouchEvent) {
                    MotionEvent motionEventObtain = MotionEvent.obtain(motionEvent);
                    motionEventObtain.setAction(3);
                    super.onTouchEvent(motionEventObtain);
                    motionEventObtain.recycle();
                    return true;
                }
                Boolean bool = this.mIsHorizontal;
                if (bool != null && bool.booleanValue()) {
                    float f = this.mFirstX - x;
                    if (this.mIsShown) {
                        f += this.mDeleteButtonWidth;
                    }
                    if (f > this.mDeleteButtonWidth / 2) {
                        if (AliSDKLogger.isDebugEnabled()) {
                            AliSDKLogger.d(TAG, "onTouchEvent, ACTION_UP, showDeleteButton");
                        }
                        showDeleteButton(this.mCurrentItemView);
                    } else {
                        if (AliSDKLogger.isDebugEnabled()) {
                            AliSDKLogger.d(TAG, "onTouchEvent, ACTION_UP, hiddenDeleteButton");
                        }
                        hiddenDeleteButton(false, true);
                    }
                    MotionEvent motionEventObtain2 = MotionEvent.obtain(motionEvent);
                    motionEventObtain2.setAction(3);
                    super.onTouchEvent(motionEventObtain2);
                    motionEventObtain2.recycle();
                    return true;
                }
                break;
            case 2:
                if (AliSDKLogger.isDebugEnabled()) {
                    AliSDKLogger.d(TAG, "onTouchEvent, ACTION_MOVE");
                }
                if (this.mIsGiveupTouchEvent) {
                    return true;
                }
                if (this.mCurrentItemView != null) {
                    float f2 = x - this.mFirstX;
                    float f3 = y - this.mFirstY;
                    if (this.mIsHorizontal == null && !judgeScrollDirection(f2, f3)) {
                        if (this.mIsShown) {
                            if (AliSDKLogger.isDebugEnabled()) {
                                AliSDKLogger.d(TAG, "onTouchEvent, ACTION_MOVE but break, mIsHorizontal=" + this.mIsHorizontal + " return true");
                            }
                            return true;
                        }
                        if (AliSDKLogger.isDebugEnabled()) {
                            AliSDKLogger.d(TAG, "onTouchEvent, ACTION_MOVE but break, mIsHorizontal=" + this.mIsHorizontal + " return false");
                        }
                    } else {
                        if (AliSDKLogger.isDebugEnabled()) {
                            AliSDKLogger.d(TAG, "onTouchEvent, ACTION_MOVE mIsHorizontal=" + this.mIsHorizontal);
                        }
                        if (this.mIsHorizontal.booleanValue()) {
                            setLongClickable(false);
                            setPressed(false);
                            Log.d("account", "1mCurrentItemView:" + this.mCurrentItemView);
                            this.mCurrentItemView.setPressed(false);
                            if (f2 <= 0.0f && this.mIsStatusMarked) {
                                this.mIsStatusMarked = false;
                            }
                            if (f2 < 0.0f || this.mIsShown) {
                                if (AliSDKLogger.isDebugEnabled()) {
                                    AliSDKLogger.d(TAG, "onTouchEvent, ACTION_MOVE, dx=" + f2);
                                }
                                if (this.mIsShown) {
                                    f2 -= this.mDeleteButtonWidth;
                                    if (AliSDKLogger.isDebugEnabled()) {
                                        AliSDKLogger.d(TAG, "onTouchEvent, ACTION_MOVE, dx=" + f2);
                                    }
                                }
                                if (f2 < 0.0f) {
                                    Log.d("account", "mDeleteView.setVisibility");
                                    this.mDeleteView.setVisibility(0);
                                    this.mCurrentScrollView.scrollTo((int) (-f2), 0);
                                    this.mStatus = STATUS.DRAGGING;
                                } else {
                                    Log.d("account", "mDeleteView.setVisibility INVISIBLE");
                                    this.mDeleteView.setVisibility(4);
                                    if (this.mSupportQuickMark) {
                                        this.mCurrentScrollView.scrollTo((int) (-f2), 0);
                                        this.mStatus = STATUS.DRAGGING;
                                    } else {
                                        hiddenDeleteButton(false, false);
                                    }
                                }
                                if (AliSDKLogger.isDebugEnabled()) {
                                    AliSDKLogger.d(TAG, "onTouchEvent, ACTION_MOVE");
                                }
                            } else if (f2 > 0.0f && this.mSupportQuickMark) {
                                if (AliSDKLogger.isDebugEnabled()) {
                                    AliSDKLogger.d(TAG, "onTouchEvent, mSupportQuickMark  mDeleteView invisible ACTION_MOVE, dx=" + f2);
                                }
                                this.mDeleteView.setVisibility(4);
                                if (f2 > this.mDeleteButtonWidth && !this.mIsStatusMarked) {
                                    this.mIsStatusMarked = true;
                                }
                                this.mCurrentScrollView.stopScroll();
                                float f4 = -f2;
                                this.mCurrentScrollView.scrollTo((int) f4, 0);
                                this.mStatus = STATUS.DRAGGING;
                                if (AliSDKLogger.isDebugEnabled()) {
                                    AliSDKLogger.d(TAG, "onTouchEvent, ACTION_MOVE, scroll to " + f4);
                                }
                            }
                            MotionEvent motionEventObtain3 = MotionEvent.obtain(motionEvent);
                            motionEventObtain3.setAction(3);
                            super.onTouchEvent(motionEventObtain3);
                            motionEventObtain3.recycle();
                            return true;
                        }
                        if (this.mIsShown) {
                            if (AliSDKLogger.isDebugEnabled()) {
                                AliSDKLogger.d(TAG, "onTouchEvent, ACTION_MOVE, hiddenDeleteButton");
                            }
                            hiddenDeleteButton(false, false);
                            this.mIsGiveupTouchEvent = true;
                            MotionEvent motionEventObtain4 = MotionEvent.obtain(motionEvent);
                            motionEventObtain4.setAction(3);
                            super.onTouchEvent(motionEventObtain4);
                            motionEventObtain4.recycle();
                            return true;
                        }
                        this.mCurrentItemView = null;
                        Log.d("account", "mCurrentItemView=" + ((Object) null));
                    }
                }
                break;
            case 3:
                if (AliSDKLogger.isDebugEnabled()) {
                    AliSDKLogger.d(TAG, "onTouchEvent, ACTION_CANCEL");
                }
                break;
        }
        return super.onTouchEvent(motionEvent);
    }

    private void showDeleteButton(View view2) {
        ScrollLinearLayout scrollLinearLayout = this.mCurrentScrollView;
        if (scrollLinearLayout == null) {
            if (AliSDKLogger.isDebugEnabled()) {
                AliSDKLogger.d(TAG, "showDeleteButton nothing");
                return;
            }
            return;
        }
        scrollLinearLayout.smoothScrollTo(scrollLinearLayout.getScrollX(), 0, this.mDeleteButtonWidth, 0);
        this.mIsShown = true;
        this.mStatus = STATUS.SHOW;
        this.mDeleteView.setClickable(true);
        if (AliSDKLogger.isDebugEnabled()) {
            AliSDKLogger.d(TAG, "showDeleteButton viewl");
        }
    }

    public void clearState() {
        this.mCurrentItemView = null;
    }

    public void hiddenDeleteButton(boolean z, boolean z2) {
        ScrollLinearLayout scrollLinearLayout = this.mCurrentScrollView;
        if (scrollLinearLayout == null) {
            if (AliSDKLogger.isDebugEnabled()) {
                AliSDKLogger.d(TAG, "hiddenDeleteButton viewl=nothing");
                return;
            }
            return;
        }
        scrollLinearLayout.stopScroll();
        if (z) {
            this.mCurrentScrollView.scrollTo(0, 0);
        } else {
            ScrollLinearLayout scrollLinearLayout2 = this.mCurrentScrollView;
            scrollLinearLayout2.smoothScrollTo(scrollLinearLayout2.getScrollX(), 0, 0, 0);
        }
        this.mIsShown = false;
        this.mStatus = STATUS.IDLE;
        this.mIsStatusMarked = false;
        this.mCurrentItemView = null;
        this.mDeleteView.setClickable(false);
        setLongClickable(true);
    }

    public int getRightViewWidth() {
        return this.mDeleteButtonWidth;
    }

    public void setRightViewWidth(int i) {
        this.mDeleteButtonWidth = i;
    }

    public void deleteItem(View view2) {
        hiddenDeleteButton(true, true);
    }

    public void setSupportSwipeDelete(boolean z) {
        this.mSupportSwipeDelete = z;
    }

    public boolean deleteButtonShown() {
        return this.mIsShown || this.mStatus != STATUS.IDLE;
    }

    public void setOnStatusChangeListener(OnStatusChangeListener onStatusChangeListener) {
        this.mStatusListener = onStatusChangeListener;
    }
}
