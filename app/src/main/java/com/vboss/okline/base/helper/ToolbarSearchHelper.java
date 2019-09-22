package com.vboss.okline.base.helper;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.view.widget.ClearEditText;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/28 9:00 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class ToolbarSearchHelper {
    private static final String TAG = ToolbarSearchHelper.class.getSimpleName();
    private Toolbar mToolbar;
    private ActionBar mActionBar;
    private TextView mActionBarSearch;
    private ClearEditText editText;

    private Context mContext;

    public ToolbarSearchHelper(AppCompatActivity activity) {
        mContext = activity.getApplicationContext();
        mToolbar = (Toolbar) activity.findViewById(R.id.my_search_toolbar);
        if (mToolbar == null) {
            throw new IllegalStateException("Fail to find Toolbar ");
        }
        mToolbar.setTitle("");
        mActionBarSearch = (TextView) mToolbar.findViewById(R.id.tv_search);
        editText = (ClearEditText) mToolbar.findViewById(R.id.edit_search);

        activity.setSupportActionBar(mToolbar);
        mActionBar = activity.getSupportActionBar();

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
     * 控制返回键显示与否
     *
     * @param enable 传入true则显示返回键，传入false不显示
     */
    public void enableHomeAsUp(boolean enable) {
        mActionBar.setDisplayHomeAsUpEnabled(enable);
        mActionBar.setDisplayShowHomeEnabled(enable);
    }

    public void setOnActionSearchListener(final OnActionSearchListener listener) {
        mActionBarSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener != null) {
                    listener.onSearch(editText.getText().toString().trim());
                    //editText.setText("");
                }
            }
        });
    }


    /**
     * 获取editText
     * @return editText
     */
    public EditText getSearchText() {
        if (editText!=null){
            return editText;
        }
        return new EditText(mContext);
    }

    /**
     * 设置搜索提示
     *
     * @param hintText 搜索提示
     */
    public void setSearchHintText(String hintText) {
        editText.setHint(hintText);
    }

    public interface OnActionSearchListener {
        void onSearch(String key);
    }

    public interface OnNavigationClickListener {
        void onNavigationClick();
    }

    /**
     * 关闭虚拟键盘  Added by 2017-04-5 yuan shaoyu
     */
    public void CancelInput(AppCompatActivity activity){
        InputMethodManager imm =  (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
