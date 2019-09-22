package com.vboss.okline.ui.express.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.model.ExpressModel;
import com.vboss.okline.ui.express.ExpressActivity;
import com.vboss.okline.ui.express.ExpressContact;
import com.vboss.okline.ui.express.adapter.HorizontalRecyclerViewAdapter;
import com.vboss.okline.ui.express.presenter.CompletedPressenter;
import com.vboss.okline.ui.express.presenter.ExpressPresenter;
import com.vboss.okline.ui.user.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/6 18:16 </br>
 * Summary: 快递状态 已完成
 */

public class CompletedFragment extends Fragment implements ExpressContact.ExpressView {

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
    @BindView(R.id.express_edit_edit_send_user_img)
    SimpleDraweeView expressEditEditSendUserImg;
    @BindView(R.id.express_edit_send_name_tv)
    TextView expressEditSendNameTv;
    @BindView(R.id.express_edit_send_number_tv)
    TextView expressEditSendNumberTv;
    @BindView(R.id.express_edit_send_message_user_ll)
    LinearLayout expressEditSendMessageUserLl;
    @BindView(R.id.express_onTheWay_edit_kd_rv)
    RecyclerView expressOnTheWayEditKdRv;
    @BindView(R.id.express_onTheWay_edit_kd_ll)
    LinearLayout expressOnTheWayEditKdLl;
    @BindView(R.id.express_name_of_goods_et)
    EditText expressNameOfGoodsEt;
    @BindView(R.id.express_weight_of_goods_et)
    EditText expressWeightOfGoodsEt;
    @BindView(R.id.express_cargo_message_edit_ll)
    LinearLayout expressCargoMessageEditLl;
    @BindView(R.id.express_edit_send_address_tv)
    TextView expressEditSendAddressTv;
    @BindView(R.id.express_edit_update_send_address_tv)
    TextView expressEditUpdateSendAddressTv;
    @BindView(R.id.item_express_edit_update_address_ll)
    LinearLayout itemExpressEditUpdateAddressLl;
    @BindView(R.id.express_send_message_edit_ll)
    LinearLayout expressSendMessageEditLl;
    @BindView(R.id.accept_message_textView)
    TextView acceptMessageTextView;
    @BindView(R.id.express_accept_user_edit_img)
    SimpleDraweeView expressAcceptUserEditImg;
    @BindView(R.id.express_accept_name_edit_tv)
    TextView expressAcceptNameEditTv;
    @BindView(R.id.express_accept_number_edit_tv)
    TextView expressAcceptNumberEditTv;
    @BindView(R.id.express_accept_address_edit_tv)
    TextView expressAcceptAddressEditTv;
    @BindView(R.id.express_message_view)
    View expressMessageView;
    @BindView(R.id.express_onTheWay_send_message_edit_ll)
    LinearLayout expressOnTheWaySendMessageEditLl;
    @BindView(R.id.express_odd_numbers_tv)
    TextView expressOddNumbersTv;
    @BindView(R.id.express_address_name_tv)
    TextView expressAddressNameTv;
    @BindView(R.id.express_record_rv)
    RecyclerView expressRecordRv;
    @BindView(R.id.express_null_record_tv)
    TextView expressNullRecordTv;
    @BindView(R.id.text_record_date_title)
    TextView textRecordDateTitle;
    @BindView(R.id.arrow_record_date_title_iv)
    ImageView arrowRecordDateTitleIv;
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
    @BindView(R.id.text_accept_date_title)
    TextView textAcceptDateTitle;
    @BindView(R.id.arrow_accept_date_title_iv)
    ImageView arrowAcceptDateTitleIv;
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
    @BindView(R.id.express_onTheWay_message_record_ll)
    LinearLayout expressOnTheWayMessageRecordLl;
    Unbinder unbinder;
    private ExpressContact.ExpressPresenter expressPresenter;
    private ExpressContact.CompletedPresenter completedPresenter;
    private List<ExpressModel> expressModelList;
    private List<ExpressModel.Company> companiesList;
    private boolean companyFlag;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_express_ontheway, container, false);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.showLog("log","comp==onViewCreated");
        initView();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if((ExpressActivity) getActivity()==null){
            return;
        }
        if (isVisibleToUser) {//界面可见时
            Utils.showLog("log","comp==setUserVisibleHint==isVisibleToUser");
            //显示底部局域栏
            ((ExpressActivity) getActivity()).hideBottomBar(false, "下一步");

        }else{
            Utils.showLog("log","comp==setUserVisibleHint==isUnVisibleToUser");
        }
    }

    private void initView() {
        completedPresenter = new CompletedPressenter(getActivity(),this);
        expressPresenter = new ExpressPresenter(getActivity(), this);


        //快递列表 默认在路上 横向recyclerView
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        expressCompanyRv.setLayoutManager(lm);
        //网络请求 得到快递列表expressModelList,然后初始化viewpager 走updateExpressList方法
        expressPresenter.getExpressLists(ExpressModel.STATE_UNDERWAY);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void setMyExpressList(List<ExpressModel> list) {
        expressModelList = list;
        Boolean b = expressModelList == null || expressModelList.size() <= 0;
        hideCompanyRv(b);
        expressCompanyRv.setAdapter(new HorizontalRecyclerViewAdapter(getActivity(), expressModelList));

    }

    @Override
    public void setExpressCompany(List<ExpressModel.Company> list) {

    }

    @Override
    public void hideSelectCompany() {
        if (companyFlag) {
            //隐藏变显示
            expressOnTheWayKdLl.setVisibility(View.VISIBLE);
            //箭头变向上
            expressSendMessageKdIv.setImageResource(R.drawable.express_arrow_up);
            companyFlag = !companyFlag;
        } else {
            expressOnTheWayKdLl.setVisibility(View.GONE);
            expressSendMessageKdIv.setImageResource(R.drawable.express_arrow_down);
            companyFlag = !companyFlag;
        }
    }
    @OnClick({R.id.express_send_message_kd_tv, R.id.express_send_message_kd_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.express_send_message_kd_tv:
                //隐藏，显示 选择快递公司
                hideSelectCompany();
                break;
            case R.id.express_send_message_kd_iv:
                //隐藏，显示 选择快递公司
                hideSelectCompany();
                break;
        }
    }
    /**
     * 隐藏或显示快递列表
     * 显示或隐藏暂无快递
     *
     * @param b Ture隐藏快递列表
     */

    private void hideCompanyRv(Boolean b) {
        if (b) {
            //隐藏快递列表
            expressCompanyRv.setVisibility(View.GONE);
            //显示暂无快递
            expressNullCompanyTv.setVisibility(View.VISIBLE);
        } else {
            //隐藏暂无快递
            expressNullCompanyTv.setVisibility(View.GONE);
            //显示快递列表
            expressCompanyRv.setVisibility(View.VISIBLE);
        }

    }
}
