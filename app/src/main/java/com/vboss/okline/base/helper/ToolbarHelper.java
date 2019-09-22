package com.vboss.okline.base.helper;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/28 9:00 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class ToolbarHelper {
    private static final String TAG = ToolbarHelper.class.getSimpleName();
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView mActionBarTitle;
    private ImageButton mActionMenu;
    private SimpleDraweeView iv_logo;

    private Context mContext;

    public ToolbarHelper(AppCompatActivity activity) {
        mContext = activity.getApplicationContext();
        mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Fail to find Toolbar ");
        }
        mToolbar.setTitle("");
        mActionBarTitle = (TextView) mToolbar.findViewById(R.id.action_title);
        mActionMenu = (ImageButton) mToolbar.findViewById(R.id.action_menu_button);
        iv_logo = (SimpleDraweeView) mToolbar.findViewById(R.id.sdv_logo);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();

    }

    /**
     * 设置actionBar logo 图标
     *
     * @param resId
     */
    public void setActionBarLogo(int resId) {
        Uri uri = Uri.parse("res//" + mContext.getPackageName() + "/" + resId);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(uri)
                .build();
        iv_logo.setController(controller);
    }

    /**
     * 设置NavigationIcon
     *
     * @param resId navigation icon
     */
    public void setNavigationIcon(int resId) {
        mToolbar.setNavigationIcon(resId);
    }

    /**
     * 设置 NavigationOnClickListener
     *
     * @param listener OnNavigationClickListener
     */
    public void setNavigationListener(final OnNavigationClickListener listener) {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNavigationClick();
                }
            }
        });
    }

    /**
     * 显示标题栏
     *
     * @param title      标题
     * @param enableHome 是否显示返回键
     */
    public void showToolbar(int title, boolean enableHome) {
        showToolbar(title, 0, 0, enableHome);
    }


    /**
     * 显示标题栏
     *
     * @param title        标题
     * @param toolbarColor 标题栏颜色
     * @param enableHome   是否显示返回键
     */
    public void showToolbar(int title, int toolbarColor, boolean enableHome) {
        showToolbar(title, 0, toolbarColor, enableHome);
    }

    /**
     * 显示标题栏
     *
     * @param title        标题
     * @param textColor    标题字体颜色
     * @param toolbarColor 标题栏颜色
     * @param enableHome   是否显示返回键
     */
    public void showToolbar(int title, int textColor, int toolbarColor, boolean enableHome) {
        if (!mActionBar.isShowing()) {
            mActionBar.show();
        }
        updateToolbar(title, textColor, toolbarColor);
        enableHomeAsUp(enableHome);
    }

    /**
     * 隐藏标题栏
     */
    public void hideToolBar() {
        if (mActionBar.isShowing()) {
            mActionBar.hide();
        }
    }

    /**
     * 控制返回键显示与否
     *
     * @param enable 传入true则显示返回键，传入false不显示
     */
    public void enableHomeAsUp(boolean enable) {
        mActionBar.setDisplayHomeAsUpEnabled(enable);
        mActionBar.setDisplayShowHomeEnabled(enable);
    }

    public void updateToolbar(String title) {
        updateToolbar(title, 0, 0);
    }

    /**
     * 更新标题
     *
     * @param title
     */
    public void updateToolbar(int title) {
        updateToolbar(title, 0);
    }

    /**
     * 更新标题及标题栏背景色
     *
     * @param title        标题
     * @param toolbarColor 标题栏背景色
     */
    public void updateToolbar(int title, int toolbarColor) {
        updateToolbar(title, 0, toolbarColor);
    }

    /**
     * 更新标题、标题字体颜色及标题栏背景色
     *
     * @param title        标题
     * @param titleColor   标题字体颜色
     * @param toolbarColor 标题栏背景色
     */
    public void updateToolbar(int title, int titleColor, int toolbarColor) {
        updateToolbar(mContext.getString(title), titleColor, toolbarColor);
    }

    /**
     * 更新标题栏
     *
     * @param title        标题
     * @param titleColor   标题字体颜色
     * @param toolbarColor 标题栏颜色
     */
    public void updateToolbar(String title, int titleColor, int toolbarColor) {
        mActionBarTitle.setText(title);
        Resources resources = mContext.getResources();
        if (titleColor != 0) {
            mActionBarTitle.setTextColor(resources.getColor(titleColor));
        }
        if (toolbarColor != 0) {
            mToolbar.setBackgroundColor(resources.getColor(toolbarColor));
        }
    }

    /**
     * 显示ActionBar 右侧的搜索Icon
     *
     * @param enable true为显示
     */
    public void showMenuButton(boolean enable) {
        if (enable) {
            mActionMenu.setVisibility(View.VISIBLE);
        } else {
            mActionMenu.setVisibility(View.GONE);
        }
    }

    /**
     * 显示ActionBar 右侧的搜索Icon
     *
     * @param resId  button icon
     * @param enable true 为显示
     */
    public void showMenuButton(int resId, boolean enable) {
        Log.i("ToolbarHelper", "showMenuButton: resID:"+resId +" enable:" + enable);
        mActionMenu.setImageResource(resId);
        if (enable) {
            mActionMenu.setVisibility(View.VISIBLE);
        } else {
            mActionMenu.setVisibility(View.GONE);
        }
    }

    /**
     * 设置ActionBar 右侧按钮是否可被点击
     *
     * @param clickable 是否可被点击
     */
    public void setMenuButtonClickable(boolean clickable) {
        if (clickable) {
            mActionMenu.setClickable(true);
        } else {
            mActionMenu.setClickable(false);
        }
    }

    /**
     * 设置 menu button click listener
     *
     * @param listener click listener
     */
    public void setMenuButtonClickListener(final OnMenuButtonClickListener listener) {
        mActionMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onMenuClick();
                }
            }
        });
    }

    public interface OnMenuButtonClickListener {
        void onMenuClick();
    }

    public interface OnNavigationClickListener {
        void onNavigationClick();
    }
}
