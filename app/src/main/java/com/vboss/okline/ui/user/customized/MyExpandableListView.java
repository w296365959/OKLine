package com.vboss.okline.ui.user.customized;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.ui.user.Utils;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/4/22
 * Summary : 在这里描述Class的主要功能
 */

public class MyExpandableListView extends ExpandableListView implements AbsListView.OnScrollListener {

    public static final int PAGE_COUNT = 15;
    public static final int ON_LOAD_ERROR = -1;
    private final Context context;
    private boolean showNoMoreData;

    public void setLoadModeEnabled(boolean loadModeEnabled) {
        this.loadModeEnabled = loadModeEnabled;
    }

    private boolean loadModeEnabled = true;

    public void setLoadingMoreAvailable(boolean loadingMoreAvailable) {
        isLoadingMoreAvailable = loadingMoreAvailable;
    }

    private boolean isLoadingMoreAvailable;
    private boolean loadingOnGoing;

    public void setPage() {
            this.page = 1;
    }

    private int page = 1;
    private LoadingDialog loadingDialog;
    private static final String TAG = "MyExpandableListView";

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private FragmentManager fragmentManager;

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    private LoadMoreListener loadMoreListener;

    public MyExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOnScrollListener(this);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        switch (scrollState) {
            case SCROLL_STATE_IDLE:
                if (showNoMoreData) {
                    showNoMoreData = false;
                    Utils.customToast(context, context.getString(R.string.no_more_data), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (visibleItemCount + firstVisibleItem == totalItemCount) {
            View lastVisibleItemView = getChildAt(totalItemCount - firstVisibleItem - 1);
            if (lastVisibleItemView != null && lastVisibleItemView.getBottom() == view.getHeight()) {
                if (loadModeEnabled) {
                    // 滑动到了底部
                    if (!isLoadingMoreAvailable && totalItemCount >= PAGE_COUNT) {
                        showNoMoreData = true;
                    }
                    if (isLoadingMoreAvailable && !loadingOnGoing) {
                        loadingOnGoing = true;
                        //开始加载
                        loadingDialog = LoadingDialog.getInstance();
                        loadingDialog.show(fragmentManager, LoadingDialog.class.getName());
                        if (loadMoreListener != null) {
                            ++page;
                            loadMoreListener.startLoading(page, PAGE_COUNT);
                        }
                    }
                }
            }
        }
    }

    public void endLoading(int resultSize) {
        if (resultSize > ON_LOAD_ERROR) {
            loadingOnGoing = false;
            setLoadingMoreAvailable(resultSize == PAGE_COUNT);
            setSelection(PAGE_COUNT*(page -1));
        }
        if (loadingDialog != null) {
            loadingDialog.onFinished(null, 0);
        }
    }

    public interface LoadMoreListener {
        void startLoading(int page, int pageCount);
    }
}
