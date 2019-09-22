package com.vboss.okline.ui.contact.TransferAccounts;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vboss.okline.R;
import com.vboss.okline.base.helper.ToolbarSearchHelper;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : yuan shaoyu
 * Email : yuer@okline.cn
 * Date : 2017/5/19 15:23
 * Desc :
 */
public class SearchBankActivity extends AppCompatActivity {

    @BindView(R.id.search_result_title)
    LinearLayout search_result_title;

    @BindView(R.id.search_bank_num)
    TextView search_bank_num;

    @BindView(R.id.search_bank_recyclerView)
    RecyclerView recyclerView;

    ToolbarSearchHelper searchHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bank);
        ButterKnife.bind(this);

        initSearchBar();
    }

    private void initSearchBar() {
        searchHelper  = new ToolbarSearchHelper(this);
        searchHelper.setSearchHintText(getString(R.string.edittext_hint));
        searchHelper.enableHomeAsUp(true);
        searchHelper.setNavigationListener(new ToolbarSearchHelper.OnNavigationClickListener() {
            @Override
            public void onNavigationClick() {
                hideSoftInput(searchHelper.getSearchText());
                finish();
                //add by yuanshoayu 2017-5-23 : cancle activity Toggle animation
                overridePendingTransition(0,0);
            }
        });
        searchHelper.setOnActionSearchListener(new ToolbarSearchHelper.OnActionSearchListener() {
            @Override
            public void onSearch(String key) {
                if (TextUtils.isEmpty(key)) {
                    Utils.customToast(SearchBankActivity.this, getResources().getString(R.string.card_query_key_empty),
                            Toast.LENGTH_SHORT).show();
                    return;
                }else {

                }
                hideSoftInput(searchHelper.getSearchText());
            }
        });
    }
    /**
     * hide input keyboard
     *
     * @param searchText editText
     */
    private void hideSoftInput(EditText searchText) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchText.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchHelper.CancelInput(this);
    }
}
