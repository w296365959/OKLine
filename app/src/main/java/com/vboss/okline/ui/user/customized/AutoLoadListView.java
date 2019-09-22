package com.vboss.okline.ui.user.customized;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.ui.user.Utils;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/22
 * Summary : 动态加载的自定义ListView
 */

public class AutoLoadListView extends ListView implements AbsListView.OnScrollListener {

    public static final int PAGE_COUNT = 15;
    public static final int ON_LOADING_ERROR = -1;
    private final Context context;
    private LoadingDialog loadingDialog;
    private View header;
    private ImageView iv_loading_header;
    private TextView tv_last_loading_header;
    private int headerMeasuredHeight;
    private RotateAnimation animation;
    private int downY;
    public static final int STATE_NORMAL = 0;
    public static final int STATE_RELEASE = 2;
    public static final int STATE_REFRESHING = 3;
    private int refreshState = STATE_NORMAL;
    private TextView tv_refresh_header;

    public void setPage() {
        this.page = 1;
    }

    private int page = 1;
    private boolean loadingOnGoing;
    private FragmentManager fragmentManager;
    private AutoLoadListener listener;
    private boolean isLoadingMoreAvailable = false;
    private static final String TAG = "AutoLoadListView";

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }


    public void setListener(AutoLoadListener listener) {
        this.listener = listener;
    }

    public void setLoadingMoreAvailable(boolean loadingMoreAvailable) {
        isLoadingMoreAvailable = loadingMoreAvailable;
    }


    public AutoLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        measure(0,0);
        this.context = context;
        setOnScrollListener(this);

        initHeaderView();
        initAnimation();
    }

    private void initAnimation() {
        animation = new RotateAnimation(0, 360, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(800);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setFillAfter(true);
    }

    private void initHeaderView() {
        header = View.inflate(getContext(), R.layout.view_pull_refresh_fheader, null);
        iv_loading_header = (ImageView) header.findViewById(R.id.iv_loading_header);
        tv_last_loading_header = (TextView) header.findViewById(R.id.tv_last_loading_header);
        tv_refresh_header = (TextView) header.findViewById(R.id.tv_refresh_header);
        header.measure(0,0);
        headerMeasuredHeight = header.getMeasuredHeight();
        header.setPadding(0,-headerMeasuredHeight,0,0);
        addHeaderView(header);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (refreshState == STATE_REFRESHING) {
                    break;
                }
                int deltaY = (int) (ev.getY() - downY);
                int paddingTop = deltaY - headerMeasuredHeight;
                if (paddingTop > -headerMeasuredHeight && getFirstVisiblePosition() == 0) {
                    int v = (int) (headerMeasuredHeight * 0.4);
                    header.setPadding(0,paddingTop< v ?paddingTop:v,0,0);
                    if (paddingTop >= 0 && refreshState == STATE_NORMAL) {
                        refreshState = STATE_RELEASE;
                        swithHeaderState();
                    } else if (paddingTop < 0 && refreshState == STATE_RELEASE){
                        refreshState = STATE_NORMAL;
                        swithHeaderState();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                switch (refreshState) {
                    case STATE_NORMAL:
                        header.setPadding(0,-headerMeasuredHeight,0,0);
                        break;
                    case STATE_RELEASE:
                        Utils.showLog(TAG,"完全显示header");
                        header.setPadding(0,0,0,0);
                        refreshState = STATE_REFRESHING;
                        swithHeaderState();
                        if (listener != null) {
                            listener.onPullRefresh();
                        }
                        break;
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    public void endPullRefresh(){
        Utils.showLog(TAG,"AutoLoadListView.endPullRefresh");
        refreshState = STATE_NORMAL;
        swithHeaderState();
        Utils.showLog(TAG,"隐藏header");
        header.setPadding(0,-headerMeasuredHeight,0,0);
        setLastRefreshTime(System.currentTimeMillis());
    }

    private void swithHeaderState() {
        switch (refreshState) {
            case STATE_REFRESHING:
                tv_refresh_header.setText("正在刷新...");
                iv_loading_header.setVisibility(VISIBLE);
                iv_loading_header.startAnimation(animation);
                break;
            case STATE_RELEASE:
                tv_refresh_header.setText("松开手指开始刷新");
                break;
            case STATE_NORMAL:
                tv_refresh_header.setText("下拉刷新");
                break;
        }
    }

    public void setLastRefreshTime(long current) {
        String text = "最后加载时间：" + Utils.transformatTime(current, 1);
        tv_last_loading_header.setText(text);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }


    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (visibleItemCount + firstVisibleItem == totalItemCount) {
            View lastVisibleItemView = getChildAt(totalItemCount - firstVisibleItem - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == view.getHeight()) {
                if (isLoadingMoreAvailable && !loadingOnGoing) {
                    loadingOnGoing = true;
                    loadingDialog = LoadingDialog.getInstance();
                    loadingDialog.show(fragmentManager,LoadingDialog.class.getName());
                    ++page;
                    listener.startLoading(page,PAGE_COUNT);
                }
                if (!isLoadingMoreAvailable && !loadingOnGoing && totalItemCount >= PAGE_COUNT){
                    Utils.showLog(TAG,"totalItemCount:"+totalItemCount +"  PAGE_COUNT:"+PAGE_COUNT);
                    Utils.customToast(context, context.getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void endLoading(int loadedCount){
        loadingOnGoing = false;
        if (loadedCount > ON_LOADING_ERROR) {
            setLoadingMoreAvailable(loadedCount == PAGE_COUNT);
            setSelection(PAGE_COUNT*(page-1));
        }
        if (loadingDialog != null) {
            loadingDialog.onFinished(null,0);
        }
    }

    public interface AutoLoadListener{
        void startLoading(int page,int pageCount);
        void onPullRefresh();
    }
}
