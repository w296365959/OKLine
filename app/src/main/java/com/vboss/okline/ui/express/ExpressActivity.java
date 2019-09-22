package com.vboss.okline.ui.express;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.model.ExpressModel;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.express.adapter.expressNoViewPagerAdapter;
import com.vboss.okline.ui.express.fragment.CompletedFragment;
import com.vboss.okline.ui.express.fragment.OnTheWayFragment;
import com.vboss.okline.ui.express.fragment.ToBeConfirmFragment;
import com.vboss.okline.ui.express.presenter.ExpressPresenter;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.view.widget.ExpressNoViewPager;
import com.vboss.okline.view.widget.OKCardView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.vboss.okline.ui.express.ExpressChooseAddressActivity.KEY_LOCATION_ADDRESS;
import static com.vboss.okline.ui.express.ExpressChooseAddressActivity.KEY_LOCATION_AREA;
import static com.vboss.okline.ui.express.ExpressChooseAddressActivity.KEY_LOCATION_MOBILE;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/3 10:19 </br>
 * Summary: 快递信息
 */

public class ExpressActivity extends BaseActivity implements ExpressContact.ExpressView {
    private static final String TAG = ExpressActivity.class.getSimpleName();
    public static final String KEY_OF_REAL_NAME = "key_of_realname";
    public static final String KEY_OF_PHONE = "key_of_phone";
    public static final String KEY_OF_AVATAR = "key_of_avatar";
    public static final String KEY_OF_AREA = "key_of_area";
    public static final String KEY_OF_DETAIL_LOCATION = "key_of_detail_location";

    @BindView(R.id.action_back)
    ImageButton actionBack;
    @BindView(R.id.iv_ocard_state)
    LogoView ivOcardState;
    @BindView(R.id.okcard_view)
    OKCardView okcardView;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.sdv_logo)
    SimpleDraweeView sdvLogo;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_menu_button)
    ImageButton actionMenuButton;
    @BindView(R.id.action_menu_right_rl)
    RelativeLayout actionMenuRightRl;
    @BindView(R.id.express_on_the_way_tv)
    TextView expressOnTheWayTv;
    @BindView(R.id.express_to_be_confirmed_tv)
    TextView expressToBeConfirmedTv;
    @BindView(R.id.express_completed_tv)
    TextView expressCompletedTv;
    @BindView(R.id.express_noViewPager)
    ExpressNoViewPager expressNoViewPager;
    @BindView(R.id.express_next_tv)
    TextView expressNextTv;
    @BindView(R.id.activity_express_bottom_rl)
    RelativeLayout activityExpressBottomRl;


    private ExpressPresenter expressPresenter;
    private List<ExpressModel> expressModelList;
    private List<ExpressModel.Company> companiesList;
    private List<String> companyNameList = new ArrayList<>();
    private List<Fragment> fragments = new ArrayList<>();

    public String acceptName;
    public String acceptPhone;
    public String acceptImgUrl;
    public String area;//对方地址
    public String detialLocation;
    //对方 欧乐号
    private  String friendOlNo;
    //快递公司编号
    public int  expressNo;
    //快递公司编号
    private String sAddress;
    //收件人地址
    private String dAddress;
    //物品名称
    private String goodsName;
    // 物品重量
    private String weight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_message);
        ButterKnife.bind(this);
        hideTitleBar(true);
        Utils.showLog("log","expressActivity=onCreated");
        init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.showLog("log","expressActivity==onResume");
    }

    private void init() {
        //创建ExpressPresenter
        expressPresenter = new ExpressPresenter(this, this);
        //设置viewpager adapter
        fragments.add(new OnTheWayFragment());
        fragments.add(new ToBeConfirmFragment());
        fragments.add(new CompletedFragment());
        expressNoViewPager.setAdapter(new expressNoViewPagerAdapter(getSupportFragmentManager(), fragments));


        //默认 在路上
        expressOnTheWayTv.setSelected(true);
        //待确认
        expressToBeConfirmedTv.setSelected(false);
        //已完成
        expressCompletedTv.setSelected(false);


        //网络请求 得到快递集合expressModelList,然后初始化viewpager 走updateExpressList方法
        expressPresenter.getExpressLists(ExpressModel.STATE_UNDERWAY);

/*
        //请求网络得到 快递公司集合,
        companyNameList = getCompanyNameList();*/

        //对方信息
        acceptName = getIntent().getStringExtra(KEY_OF_REAL_NAME);
        acceptPhone = getIntent().getStringExtra(KEY_OF_PHONE);
        acceptImgUrl = getIntent().getStringExtra(KEY_OF_AVATAR);
        area = getIntent().getStringExtra(KEY_OF_AREA);
        detialLocation = getIntent().getStringExtra(KEY_OF_DETAIL_LOCATION);

    }


    public void hideTitleBar(boolean b) {
        if (b) {
            //隐藏右上角图标
            actionMenuButton.setVisibility(View.INVISIBLE);
        } else {
            //显示
            actionMenuButton.setVisibility(View.VISIBLE);
        }

    }

    @OnClick({R.id.action_back, R.id.action_menu_right_rl, R.id.express_on_the_way_tv, R.id.express_to_be_confirmed_tv, R.id.express_completed_tv, R.id.express_next_tv})
    public void onViewClicked(View view) {
        int item = 0;
        int currentItemi=expressNoViewPager.getCurrentItem();
        switch (view.getId()) {
            case R.id.action_back:
                //左上角返回键
                finish();
                break;
            case R.id.action_menu_right_rl:
                //右上角图标
                break;
            case R.id.express_on_the_way_tv:
                //在路上
                expressPresenter.setSelectedView(expressOnTheWayTv, true);
                expressPresenter.setSelectedView(expressToBeConfirmedTv, false);
                expressPresenter.setSelectedView(expressCompletedTv, false);

                hideBottomBar(true, "下一步");
                item = 0;
                selectedFragment(item);

                break;
            case R.id.express_to_be_confirmed_tv:
                //待确认
                hideTitleBar(false);
                expressPresenter.setSelectedView(expressOnTheWayTv, false);
                expressPresenter.setSelectedView(expressToBeConfirmedTv, true);
                expressPresenter.setSelectedView(expressCompletedTv, false);
                item = 1;
                selectedFragment(item);
                break;
            case R.id.express_completed_tv:
                //已完成
                hideTitleBar(false);
                expressPresenter.setSelectedView(expressOnTheWayTv, false);
                expressPresenter.setSelectedView(expressToBeConfirmedTv, false);
                expressPresenter.setSelectedView(expressCompletedTv, true);
                item = 2;
                selectedFragment(item);
                break;
            case R.id.express_next_tv:
                //下一步
                if (item != 3) {
                    nextStep(item);
                }
                break;
        }
    }

    /**
     * 选择viewpager的当前Fragment页面
     *
     * @param i
     */
    public void selectedFragment(int i) {
        expressNoViewPager.setCurrentItem(i);
        selectedState();
    }


    /**
     * Added by wangshuai 2017-07-06
     * <p>
     * start activity edit address
     * intent data contains detail address,area,phone,type
     *
     * @see ExpressChooseAddressActivity
     */
    public void editAddress() {

        String mobile = UserRepository.getInstance(this).getUser().getPhone();

        Intent chooseIntent = new Intent(this, ExpressChooseAddressActivity.class);
        chooseIntent.putExtra(KEY_LOCATION_MOBILE, mobile);
        chooseIntent.putExtra(KEY_LOCATION_AREA, "");
        chooseIntent.putExtra(KEY_LOCATION_ADDRESS, "");
        startActivity(chooseIntent);
    }

    /**
     * 下一步
     */
    public void nextStep(int item) {

        switch (item) {
            case 0:

                //发快递信息:
                //跳转到 待确认 页面
                selectedFragment(1);
                //底部栏隐藏
                hideBottomBar(true, "下一步");
                //对方欧乐号
                friendOlNo=UserRepository.getInstance(this).getUser().getOlNo();
                //创建新快递
                expressPresenter.sendExpress(friendOlNo,expressNo+"",sAddress,sAddress,goodsName,weight);
                break;
            case 1:

                break;
            case 2:

                break;
            default:
                break;
        }

    }

    /**
     * 状态选择, 在路上,待确认,已完成
     */
    private void selectedState() {
       int item= expressNoViewPager.getCurrentItem();
        //在路上
        expressOnTheWayTv.setSelected(item == 0 ? true : false);
        //待确认
        expressToBeConfirmedTv.setSelected(item == 1 ? true : false);
        //已完成
        expressCompletedTv.setSelected(item == 2 ? true : false);
    }

    /**
     * @param b       true表示隐藏底部栏
     * @param content 要显示的内容
     */
    public void hideBottomBar(boolean b, String content) {
        if (b) {
          /*  if(activityExpressBottomRl==null){
                return;
            }*/
            //底部栏隐藏
            activityExpressBottomRl.setVisibility(View.GONE);
        } else {
          /*  if(activityExpressBottomRl==null||expressNextTv==null){
                return;
            }*/
            //底部栏隐藏
            activityExpressBottomRl.setVisibility(View.VISIBLE);
            //设置右下角显示textview的内容
            expressNextTv.setText(content);
        }
    }


    @Override
    public void setMyExpressList(List<ExpressModel> list) {
        //快递列表
        expressModelList = list;

    }

    @Override
    public void setExpressCompany(List<ExpressModel.Company> list) {
        companiesList = list;
    }

    @Override
    public void hideSelectCompany() {

    }


  /*  *//**
     * 得到快递公司集合companiesList
     *
     * @return
     *//*
    public List<String> getCompanyNameList() {
        //请求网络,得到companiesList
        expressPresenter.getCompanyLists();
        if (companiesList != null) {
            for (ExpressModel.Company company : companiesList) {
                companyNameList.add(company.getName());
            }
            return companyNameList;
        }
        return null;
    }*/


    /**
     * Added by wangshuai 2017-07-06
     * <p>
     * accept address and show address
     *
     * @param address String
     */
    @Subscribe(tags = {@Tag(EventToken.EDIT_EXPRESS)})
    @SuppressWarnings("unused")
    public void onAcceptAddress(String address) {
        Timber.tag(TAG).i("address %s", address);
    }


}
