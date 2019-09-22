package com.vboss.okline.ui.express.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.model.ExpressModel;
import com.vboss.okline.ui.express.ExpressActivity;
import com.vboss.okline.ui.express.ExpressContact;
import com.vboss.okline.ui.express.MyRvItemClickListener;
import com.vboss.okline.ui.express.adapter.HorizontalExpressCompanyRecyclerViewAdapter;
import com.vboss.okline.ui.express.adapter.HorizontalRecyclerViewAdapter;
import com.vboss.okline.ui.express.presenter.ExpressPresenter;
import com.vboss.okline.ui.express.presenter.ToBeConfirmPressenter;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/6 15:58 </br>
 * Summary:快递状态 待确认
 */

public class ToBeConfirmFragment extends Fragment implements ExpressContact.ExpressView {
    @BindView(R.id.express_company_rv)
    RecyclerView expressCompanyRv;
    @BindView(R.id.express_null_company_tv)
    TextView expressNullCompanyTv;
    @BindView(R.id.send_message_textView)
    TextView sendMessageTextView;
    @BindView(R.id.express_send_message_kd_tv)
    TextView expressSendMessageKdTv;
    @BindView(R.id.express_send_message_kd_iv)
    ImageView expressSendMessageKdIv;
    @BindView(R.id.express_to_be_confirmed_new_textView)
    TextView expressToBeConfirmedNewTextView;
    @BindView(R.id.express_onTheWay_kd_rv)
    RecyclerView expressOnTheWayKdRv;
    @BindView(R.id.express_onTheWay_kd_ll)
    LinearLayout expressOnTheWayKdLl;
    @BindView(R.id.express_send_user_img)
    SimpleDraweeView expressSendUserImg;
    @BindView(R.id.express_send_name_tv)
    TextView expressSendNameTv;
    @BindView(R.id.express_send_number_tv)
    TextView expressSendNumberTv;
    @BindView(R.id.express_send_message_user_ll)
    LinearLayout expressSendMessageUserLl;
    @BindView(R.id.express_name_of_goods_tv)
    TextView expressNameOfGoodsTv;
    @BindView(R.id.express_weight_of_goods_tv)
    TextView expressWeightOfGoodsTv;
    @BindView(R.id.express_cargo_message_ll)
    LinearLayout expressCargoMessageLl;
    @BindView(R.id.express_send_address_tv)
    TextView expressSendAddressTv;
    @BindView(R.id.express_update_send_address_tv)
    TextView expressUpdateSendAddressTv;
    @BindView(R.id.item_express_update_address_ll)
    LinearLayout itemExpressUpdateAddressLl;
    @BindView(R.id.express_send_message_ll)
    LinearLayout expressSendMessageLl;
    @BindView(R.id.accept_message_textView)
    TextView acceptMessageTextView;
    @BindView(R.id.express_accept_user_img)
    SimpleDraweeView expressAcceptUserImg;
    @BindView(R.id.express_accept_name_tv)
    TextView expressAcceptNameTv;
    @BindView(R.id.express_accept_number_tv)
    TextView expressAcceptNumberTv;
    @BindView(R.id.express_accept_address_tv)
    TextView expressAcceptAddressTv;
    @BindView(R.id.express_update_accept_address_tv)
    TextView expressUpdateAcceptAddressTv;
    @BindView(R.id.express_other_confirmed_address_tv)
    TextView expressOtherConfirmedAddressTv;
    @BindView(R.id.express_accept_message_comfirmed_iv)
    ImageView expressAcceptMessageComfirmedIv;
    @BindView(R.id.express_message_view)
    View expressMessageView;

    Unbinder unbinder;
    @BindView(R.id.express_msg_accept_ll)
    LinearLayout expressMsgAcceptLl;
    private ExpressContact.ExpressPresenter expressPresenter;
    private ExpressContact.ToBeConfirmPresenter toBeConfirmPresenter;
    private List<ExpressModel> expressModelList;
    private List<ExpressModel.Company> companiesList;
    private HorizontalRecyclerViewAdapter horizontalRecyclerViewAdapter;
    private int expressState = 1;//快递状态
    private int expressType = 1;//快递类型，1我发出，2我收到
    private boolean companyFlag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.showLog("log", "tobec==oncreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_express_tobeconfirm, container, false);

        unbinder = ButterKnife.bind(this, view);
        Utils.showLog("log", "tobec==onCreateView");
        return view;


    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.showLog("log", "tobec==onViewCreated");
        initView();
    }

    private void initView() {
        toBeConfirmPresenter = new ToBeConfirmPressenter(getActivity(), this);
        expressPresenter = new ExpressPresenter(getActivity(), this);


        //快递列表 默认在路上 横向recyclerView
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        expressCompanyRv.setLayoutManager(lm);
        //网络请求 得到快递列表expressModelList,然后初始化viewpager 走setMyExpressList方法
        expressPresenter.getExpressLists(ExpressModel.STATE_UNCONFIRMED);

        //网络请求
        //请求网络得到 可选择的快递公司集合,在setExpressCompany方法里回调得到list再设置adapter
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        expressOnTheWayKdRv.setLayoutManager(llm);
        expressPresenter.getCompanyLists();
        //隐藏选择快递公司
        companyFlag = false;
        hideSelectCompany();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setMyExpressList(List<ExpressModel> list) {
        //快递列表
        expressModelList = list;
        Boolean b = expressModelList == null || expressModelList.size() <= 0;
        hideExpressListRv(b);
        horizontalRecyclerViewAdapter = new HorizontalRecyclerViewAdapter(getActivity(), expressModelList);
        //默认选中第一条item
        horizontalRecyclerViewAdapter.vector.set(0, true);
        expressCompanyRv.setAdapter(horizontalRecyclerViewAdapter);
        horizontalRecyclerViewAdapter.setOnItemClickListener(new MyRvItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                //快递列表 条目点击事件
                ExpressModel expressModel = expressModelList.get(postion);
                //设置快递信息数据
                setExpressMessageData(expressModel);

            }


        });
        ExpressModel expressModel = expressModelList.get(0);

        //快递状态，默认显示第一条
        expressState = expressModel.getState();
        //快递类型，1我发出，2我收到
        expressType = expressModel.getExpressType();
        //根据类型 显示相应布局
        displayLayout(expressType, expressState);
        //初始设置
        setExpressMessageData(expressModel);

    }

    /**
     * 给控件赋值（设置数据）
     *
     * @param expressModel
     */
    private void setExpressMessageData(ExpressModel expressModel) {
        //发件人信息
        expressSendMessageKdTv.setText(expressModel.getExpressName() + "快递");
        //头像要从我的名片 接口取获取
        // expressSendUserImg.setImageURI(Uri.parse(sendImgUrl));
        expressSendNameTv.setText(expressModel.getsName());
        expressSendNumberTv.setText(expressModel.getsPhone());
        expressNameOfGoodsTv.setText(expressModel.getGoodsName());
        expressWeightOfGoodsTv.setText(expressModel.getWeight());
        expressSendAddressTv.setText(expressModel.getsAddress());

        //收货信息
        expressAcceptNameTv.setText(expressModel.getdName());
        expressAcceptNumberTv.setText(expressModel.getdPhone());
        expressAcceptAddressTv.setText(expressModel.getdAddress());
    }



    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((ExpressActivity) getActivity() == null) {
            return;
        }
        if (isVisibleToUser) {
            //结合Viewpager时真正Fragment的onResume
            Utils.showLog("log", "tobec==setUserVisibleHint");
        /*    //隐藏选择快递公司
            companyFlag = false;
            hideSelectCompany();*/

            if (expressType == 1) {//我发出
                //隐藏底部栏
                ((ExpressActivity) getActivity()).hideBottomBar(true, "下一步");
                expressMessageView.setVisibility(View.GONE);
                if (expressState == 4) {
                    //显示底部栏
                    ((ExpressActivity) getActivity()).hideBottomBar(false, "通知快递");
                    expressMessageView.setVisibility(View.VISIBLE);
                }
            } else {//我接收
                //显示底部栏
                ((ExpressActivity) getActivity()).hideBottomBar(false, "下一步");
                //显示底部间隔view
                expressMessageView.setVisibility(View.VISIBLE);
                if (expressState == 4) {
                    //隐藏底部栏
                    ((ExpressActivity) getActivity()).hideBottomBar(true, "通知快递");
                    //隐藏底部间隔view
                    expressMessageView.setVisibility(View.GONE);
                }
            }
        } else {
            //相当于Fragment的onPause
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.showLog("log", "tobec==onResume");

    }

    /**
     * 判断显示哪个布局
     *
     * @param expressType  快递类型，1我发出，2我收到
     * @param expressState 快递状态 1待确认，4已确认
     */
    private void displayLayout(int expressType, int expressState) {

        //显示发快递信息布局
        expressSendMessageLl.setVisibility(View.VISIBLE);
        //显示 收快递信息布局
        expressMsgAcceptLl.setVisibility(View.VISIBLE);
        if (expressType == 1) {//我发出
            //隐藏  你有新快递的text提醒
            expressToBeConfirmedNewTextView.setVisibility(View.GONE);

            //隐藏 发快递的添加修改地址的布局
            itemExpressUpdateAddressLl.setVisibility(View.GONE);

            //隐藏 收快递的添加修改地址控件
            expressUpdateAcceptAddressTv.setVisibility(View.GONE);
            //显示 “待对方确认收货地址”字
            expressOtherConfirmedAddressTv.setVisibility(View.VISIBLE);
            //隐藏 确认收货地址的 图片
            expressAcceptMessageComfirmedIv.setVisibility(View.GONE);
         /*   //隐藏底部栏
            ((ExpressActivity) getActivity()).hideBottomBar(true, "下一步");
            expressMessageView.setVisibility(View.GONE);*/

            if (expressState == 4) {//已确认

                //  显示 确认收货地址的 图片
                expressAcceptMessageComfirmedIv.setVisibility(View.VISIBLE);
                //显示 “待对方确认收货地址”字
                expressOtherConfirmedAddressTv.setVisibility(View.GONE);
             /*   //显示底部栏
                ((ExpressActivity) getActivity()).hideBottomBar(false, "通知快递");
                expressMessageView.setVisibility(View.VISIBLE);*/
            }
        } else {
            //显示 你有新快递的text提醒
            expressToBeConfirmedNewTextView.setVisibility(View.VISIBLE);


            //隐藏 发快递的添加修改地址的布局
            itemExpressUpdateAddressLl.setVisibility(View.GONE);

            //显示 收快递的添加修改地址控件
            expressUpdateAcceptAddressTv.setVisibility(View.VISIBLE);
            //隐藏 “待对方确认收货地址”字
            expressOtherConfirmedAddressTv.setVisibility(View.GONE);
            //隐藏 确认收货地址的 图片
            expressAcceptMessageComfirmedIv.setVisibility(View.GONE);
         /*   //显示底部栏
            ((ExpressActivity) getActivity()).hideBottomBar(false, "下一步");
            //显示底部间隔view
            expressMessageView.setVisibility(View.VISIBLE);*/

            if (expressState == 4) {//收件方 ，已确认

                //隐藏 收快递的添加修改地址控件
                expressUpdateAcceptAddressTv.setVisibility(View.GONE);
                //显示 确认收货地址的 图片
                expressAcceptMessageComfirmedIv.setVisibility(View.VISIBLE);
         /*       //隐藏底部栏
                ((ExpressActivity) getActivity()).hideBottomBar(true, "通知快递");
                //隐藏底部间隔view
                expressMessageView.setVisibility(View.GONE);*/


            }
        }


    }

    @Override
    public void setExpressCompany(List<ExpressModel.Company> list) {
        //快递公司列表
        companiesList = list;
        HorizontalExpressCompanyRecyclerViewAdapter adapter = new HorizontalExpressCompanyRecyclerViewAdapter(getActivity(), companiesList);
        expressOnTheWayKdRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyRvItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                String companyNmae = companiesList.get(postion).getName();
                ((ExpressActivity) getActivity()).expressNo = companiesList.get(postion).getNumber();
                ToastUtil.show(getActivity(), "选择" + companyNmae + "快递");

                expressSendMessageKdTv.setText(companyNmae+"快递");
                hideSelectCompany();

            }
        });
    }

    @Override
    public void hideSelectCompany() {
        if (companyFlag) {
            //隐藏变显示
            expressOnTheWayKdLl.setVisibility(View.VISIBLE);
            //箭头变向上
            expressSendMessageKdIv.setImageResource(R.drawable.express_arrow_up);
        //todo 图片没显示
            companyFlag = !companyFlag;
        } else {
            expressOnTheWayKdLl.setVisibility(View.GONE);
            expressSendMessageKdIv.setImageResource(R.drawable.express_arrow_down);

            companyFlag = !companyFlag;
        }
    }

    /**
     * 隐藏或显示快递列表
     * 显示或隐藏暂无快递
     *
     * @param b Ture隐藏快递列表
     */

    private void hideExpressListRv(Boolean b) {
        if (b) {
            //隐藏快递列表
            expressCompanyRv.setVisibility(View.GONE);
            //显示暂无快递
            expressNullCompanyTv.setVisibility(View.VISIBLE);
        } else {
            //隐藏暂无快递
            expressSendMessageKdIv.setVisibility(View.GONE);
            //显示快递列表
            expressCompanyRv.setVisibility(View.VISIBLE);
        }

    }


    @OnClick({R.id.express_send_message_kd_tv, R.id.express_send_message_kd_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.express_send_message_kd_tv:
                //快递公司选择
                hideSelectCompany();
                break;
            case R.id.express_send_message_kd_iv:
                //快递公司选择
                hideSelectCompany();
                break;
        }
    }

}
