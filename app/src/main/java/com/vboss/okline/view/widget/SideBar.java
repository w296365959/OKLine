package com.vboss.okline.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.vboss.okline.utils.StringUtils;

import java.util.Arrays;
import java.util.List;

import timber.log.Timber;


/**
 * Created by admin on 15-8-14.
 */
public class SideBar extends View {
    private static final String TAG = "SideBar";
    // 26个字母
    public static String[] b = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#" };
    // 触摸事件
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    private int choose = -1;// 选中
    private Paint paint = new Paint();

    private TextView mTextDialog;

    private List<String> mIndexDatas = Arrays.asList(b);
    private List<String> mSourceDatas;

    public SideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SideBar(Context context) {
        super(context);
    }

    public SideBar setmSourceDatas(List<String> mSourceDatas) {
        mIndexDatas = mSourceDatas;
        Timber.tag(TAG).i("mIndexDatas size : " + mIndexDatas.size());
        initSourceDatas();//对数据源进行初始化
        return this;
    }

    private void initSourceDatas() {
        if (null == mSourceDatas || mSourceDatas.isEmpty()) {
            return;
        }
    }

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
        int height = getHeight();// 获取对应高度
        int width = getWidth(); // 获取对应宽度
//        int singleHeight = height / b.length;// 获取每一个字母的高度
        int singleHeight = height / mIndexDatas.size();// 获取每一个字母的高度

        for (int i = 0; i < mIndexDatas.size(); i++) {
            paint.setColor(Color.parseColor("#555555"));
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(StringUtils.dpToPx(10,getResources()));
            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#208ee5"));
                paint.setFakeBoldText(true);
            }
            // x坐标等于中间-字符串宽度的一半.
//            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float xPos = width / 2 - paint.measureText(mIndexDatas.get(i)) / 2;
            float yPos = singleHeight * i + singleHeight;
//            canvas.drawText(b[i], xPos, yPos, paint);
            canvas.drawText(mIndexDatas.get(i), xPos, yPos, paint);
            paint.reset();// 重置画笔
            Timber.tag(TAG).i("onDraw");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) getLayoutParams();
        linearParams.height = StringUtils.dpToPx(10,getResources()) * mIndexDatas.size();
        setLayoutParams(linearParams);
        Timber.tag(TAG).i("onMeasure : "+linearParams.height);
    }

    /**
     * 测量宽高
     * type=0 测量宽度， type=1 测量高度
     */
//    private int measureWH(int measureSpec, int type){
//        int model = MeasureSpec.getMode(measureSpec);//获得当前空间值的一个模式
//        int size = MeasureSpec.getSize(measureSpec);//获得当前空间值的推荐值
//
//        switch (model){
//            case MeasureSpec.EXACTLY://当你的控件设置了一个精确的值或者为match_parent时, 为这种模式
//                Log.d("xxx","EXACTLY");
//                //size = (int) paint.measureText(labels[0]);
//                return size;
//            case MeasureSpec.AT_MOST://当你的控件设置为wrap_content时，为这种模式
//                if(type == 0){
//                    Log.d("xxx","AT_MOST");
//                    //测量宽度
//                    size = (int) paint.measureText(labels[0]);
//                    return size;
//                } else {
//                    //测量高度
//                    return size;
//                }
//            case MeasureSpec.UNSPECIFIED://尽可能的多
//                break;
//        }
//        return 0;
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        final int c = (int) (y / getHeight() * mIndexDatas.size());// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundDrawable(new ColorDrawable(0x00000000));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
                //setBackgroundResource(R.drawable.sidebar_background);
                if (oldChoose != c) {
                    if (c >= 0 && c < mIndexDatas.size()) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(mIndexDatas.get(c));
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(mIndexDatas.get(c));
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }


    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     *
     */
    public interface OnTouchingLetterChangedListener {
         void onTouchingLetterChanged(String s);
    }

}
