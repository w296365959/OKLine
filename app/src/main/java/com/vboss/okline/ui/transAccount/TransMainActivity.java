package com.vboss.okline.ui.transAccount;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.ui.app.App;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class TransMainActivity extends Activity {

    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;
    @BindView(R.id.trans_record_listView)
    RecyclerView trans_record_listView;
    CommonAdapter<App> adapter;
    List<App> transRunningList;
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.trans_next)
    TextView trans_next;
    @BindView(R.id.trans_confirm)
    TextView trans_confirm;
    @BindView(R.id.trans_money)
    LineEditText trans_money;
    @BindView(R.id.trans_noInfo)
    TextView trans_noInfo;
    @BindView(R.id.trans_state_running)
    TextView trans_state_running;
    @BindView(R.id.trans_state_running1)
    TextView trans_state_running1;
    @BindView(R.id.trans_state_complete1)
    TextView trans_state_complete1;
    @BindView(R.id.trans_state_complete)
    TextView trans_state_complete;
    @BindView(R.id.trans_running_num)
    TextView trans_running_num;
    @BindView(R.id.trans_complete_num)
    TextView trans_complete_num;
    @BindView(R.id.trans_running_button)
    LinearLayout trans_running_button;
    @BindView(R.id.trans_complete_button)
    LinearLayout trans_complete_button;

    private int[] tabTitles = new int[]{
            R.string.trans_tab_title1, R.string.trans_tab_title2
    };

    private int[] tabColors = new int[]{
            R.color.black,R.color.transAccount_tab
    };

    private static final String TAB_BANKTRANS = "tab_bankTrans";
    private static final String TAB_QUICK = "tab_quick";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans_main);
        ButterKnife.bind(this);

        initToolBar();
        initTab();
        onViewClick(trans_running_button);
        transRunningList = new ArrayList<>();
        for (int i = 0; i <5 ; i++) {
            App app = new App();
            app.setAppName("杨欣");
            app.setAppId(i);
            transRunningList.add(app);
        }
        //horizontal_layout();
        adapter = new CommonAdapter<App>(this, R.layout.trans_record_item_layout,transRunningList) {
            @Override
            public void convert(final ViewHolder holder, App app, int position) {
//                holder.setOnClickListener(R.id.trans_list_item, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        holder.setBackgroundRes(R.id.trans_list_item,R.drawable.trans_listitem_check_bg);
//                    }
//                });
            }
        };
        LinearLayoutManager lm = new LinearLayoutManager(this);
        //横向recyclerView
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        trans_record_listView.setLayoutManager(lm);
        trans_record_listView.setAdapter(adapter);

    }
    /**
     * create tab View
     *
     * @param position 位置
     * @return tabView   tab View
     */
    private View createTabView(int position) {
        View tabView = LayoutInflater.from(this).inflate(R.layout.layout_trans_tab, null);
        TextView trans_tab = (TextView) tabView.findViewById(R.id.trans_tab);
        trans_tab.setText(getResources().getString(tabTitles[position]));
        trans_tab.setTextColor(getResources().getColor(tabColors[position]));
        return tabView;
    }
    private void initTab() {
        mTabLayout.addTab(mTabLayout.newTab().setTag(TAB_BANKTRANS).setCustomView(createTabView(0)));//setText("银行卡转账"));
        mTabLayout.addTab(mTabLayout.newTab().setTag(TAB_QUICK).setCustomView(createTabView(1)));//.setText("银联快钱"));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView().findViewById(R.id.trans_tab);
                text.setTextColor(ActivityCompat.getColor(TransMainActivity.this,R.color.black));
                String tag = (String) tab.getTag();
                switch (tag){
                    case TAB_BANKTRANS:

                        break;
                    case TAB_QUICK:
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView text = (TextView) tab.getCustomView().findViewById(R.id.trans_tab);
                text.setTextColor(ActivityCompat.getColor(TransMainActivity.this,R.color.transAccount_tab));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @OnClick({R.id.trans_running_button,R.id.trans_complete_button,R.id.trans_next})
    public void onViewClick(View view){
        switch (view.getId()){
            case R.id.trans_running_button:
                trans_running_button.setSelected(true);
                trans_state_running.setSelected(true);
                trans_state_running1.setSelected(true);
                trans_running_num.setSelected(true);
                trans_complete_button.setSelected(false);
                trans_state_complete.setSelected(false);
                trans_state_complete1.setSelected(false);
                trans_complete_num.setSelected(false);
                break;
            case R.id.trans_complete_button:
                trans_running_button.setSelected(false);
                trans_state_running.setSelected(false);
                trans_state_running1.setSelected(false);
                trans_running_num.setSelected(false);
                trans_complete_button.setSelected(true);
                trans_state_complete.setSelected(true);
                trans_state_complete1.setSelected(true);
                trans_complete_num.setSelected(true);
                break;
            case R.id.trans_next:

                break;
            default:
                break;
        }
    }
    private void initToolBar() {
        toolbar.setActionTitle("转账");
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                // 返回一级界面
                finish();
            }
        });
    }
}
