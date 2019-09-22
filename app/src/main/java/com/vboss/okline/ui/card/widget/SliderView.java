package com.vboss.okline.ui.card.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import com.vboss.okline.R;

public class SliderView extends LinearLayout {
    private static final String TAG = "SlideView";
    private static final int TAN = 2;
    private int mHolderWidth = 120;
    private float mLastX = 0;
    private float mLastY = 0;
    private Context mContext;
    private LinearLayout mViewContent;
    private Scroller mScroller;
    public static final int MODE_SINGLE = 0x001;
    public static final int MODE_DOUBLE = 0x002;
    public static final int MODE_NONE = 0x003;
    private boolean sliderEnabled = false;

    public SliderView(Context context, Resources resources) {
        super(context);
    }

    public SliderView(Context context) {
        super(context);
        initView(R.layout.slide_double_merge);
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(R.layout.slide_double_merge);
    }

    private void initView(int resId) {
        setOrientation(LinearLayout.HORIZONTAL);
        mContext = getContext();
        mScroller = new Scroller(mContext);
        View.inflate(mContext, resId, this);
        mViewContent = (LinearLayout) findViewById(R.id.view_content);
        mHolderWidth = Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mHolderWidth, getResources()
                        .getDisplayMetrics()));
    }

    public void setContentView(View view) {
        mViewContent.addView(view);
    }

    public void shrink() {
        int offset = getScrollX();
        if (offset == 0) {
            return;
        }
        scrollTo(0, 0);
    }

    public void reset() {
        int offset = getScrollX();
        if (offset == 0) {
            return;
        }
        smoothScrollTo(0, 0);
    }

    public boolean open() {
        int offset = getScrollX();
        if (offset == 0) {
            return false;
        }
        return true;
    }

    public void adjust(boolean left) {
        int offset = getScrollX();
        if (offset == 0) {
            return;
        }
        if (offset < 20) {
            this.smoothScrollTo(0, 0);
        } else if (offset < mHolderWidth - 20) {
            if (left) {
                this.smoothScrollTo(mHolderWidth, 0);
            } else {
                this.smoothScrollTo(0, 0);
            }
        } else {
            this.smoothScrollTo(mHolderWidth, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (sliderEnabled) {
                    float x = event.getX();
                    float y = event.getY();
                    float deltaX = x - mLastX;
                    float delatY = y - mLastY;
                    mLastX = x;
                    mLastY = y;
                    if (Math.abs(deltaX) < Math.abs(delatY) * TAN) {
                        break;
                    }
                    if (deltaX != 0) {
                        float newScrollX = getScrollX() - deltaX;
                        if (newScrollX < 0) {
                            newScrollX = 0;
                        } else if (newScrollX > mHolderWidth) {
                            newScrollX = mHolderWidth;
                        }
                        this.scrollTo((int) newScrollX, 0);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void smoothScrollTo(int destX, int destY) {
        int scrollX = getScrollX();
        int delta = destX - scrollX;
        mScroller.startScroll(scrollX, 0, delta, 0, Math.abs(delta) * 3);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }

    public void setSliderEnabled(boolean sliderEnabled) {
        this.sliderEnabled = sliderEnabled;
    }
}
