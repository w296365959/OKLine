package com.vboss.okline.ui.express.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.model.ExpressModel;
import com.vboss.okline.ui.express.ExpressActivity;
import com.vboss.okline.ui.express.ExpressContact;
import com.vboss.okline.ui.express.MyRvItemClickListener;
import com.vboss.okline.ui.express.adapter.HorizontalExpressCompanyRecyclerViewAdapter;
import com.vboss.okline.ui.express.adapter.HorizontalRecyclerViewAdapter;
import com.vboss.okline.ui.express.presenter.ExpressPresenter;
import com.vboss.okline.ui.express.presenter.OnTheWayPressenter;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.vboss.okline.R.id.express_send_message_kd_tv;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/6 15:56 </br>
 * Summary:快递状态 在路上
 */

public class OnTheWayFragment extends Fragment implements ExpressContact.ExpressView {


    private static final String TAG = OnTheWayFragment.class.getSimpleName();
    @BindView(R.id.express_company_rv)
    RecyclerView expressCompanyRv;
    @BindView(R.id.express_null_company_tv)
    TextView expressNullCompanyTv;
    @BindView(R.id.send_message_textView)
    TextView sendMessageTextView;
    @BindView(express_send_message_kd_tv)
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
    @BindView(R.id.send_user_and_company_fl)
    FrameLayout sendUserAndCompanyFl;
    @BindView(R.id.express_msg_accept_ll)
    LinearLayout expressMsgAcceptLl;
    @BindView(R.id.express_send_message_title_rl)
    RelativeLayout expressSendMessageTitleRl;
    @BindView(R.id.express_accept_message_title_rl)
    RelativeLayout expressAcceptMessageTitleRl;
    @BindView(R.id.express_onTheWay_scrollview)
    ScrollView expressOnTheWayScrollview;
    private ExpressContact.ExpressPresenter expressPresenter;
    private ExpressContact.OnTheWayPresenter onTheWayPresenter;
    private List<ExpressModel> expressModelList;
    private List<ExpressModel.Company> companiesList;
    private boolean companyFlag;
    private HorizontalRecyclerViewAdapter horizontalRecyclerViewAdapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.showLog("log","ontheway==onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_express_ontheway, container, false);
        unbinder = ButterKnife.bind(this, view);
        Utils.showLog("log","ontheway==onCreateView");
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Utils.showLog("log","ontheway==onViewCreated");
        initView();

    }


    @Override
    public void onResume() {
        super.onResume();
        Utils.showLog("log","ontheway==onResume");
        //显示底部局域栏
     //   ((ExpressActivity)getActivity()).hideBottomBar(false,"下一步");

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if ((ExpressActivity) getActivity() == null) {
            return;
        }


        if (isVisibleToUser) {//界面可见时
            Utils.showLog("log","ontheway==setUserVisibleHint==isVisibleToUser");
            if((ExpressActivity) getActivity()==null){
                return;
            }
            //显示底部局域栏
            ((ExpressActivity) getActivity()).hideBottomBar(false, "下一步");
        }else{
            Utils.showLog("log","ontheway==setUserVisibleHint==isUnVisibleToUser");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.showLog("log","ontheway==onPause");
    }


    private void initView() {
        onTheWayPresenter = new OnTheWayPressenter(getActivity(), this);
        expressPresenter = new ExpressPresenter(getActivity(), this);


        //快递列表 默认在路上 横向recyclerView
        LinearLayoutManager lm = new LinearLayoutManager(getActivity());
        lm.setOrientation(LinearLayoutManager.HORIZONTAL);
        expressCompanyRv.setLayoutManager(lm);
        //网络请求 得到快递列表expressModelList,然后初始化viewpager 走updateExpressList方法
        expressPresenter.getExpressLists(ExpressModel.STATE_UNDERWAY);

        //请求网络得到 可选择的快递公司集合,在setExpressCompany方法里回调得到list再设置adapter
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        expressOnTheWayEditKdRv.setLayoutManager(llm);
        expressPresenter.getCompanyLists();
        //隐藏选择快递公司
        companyFlag = false;
        hideSelectCompany();
        //初始化赋值
        //发件人信息
        String sendPhone = UserRepository.getInstance(getActivity()).getUser().getPhone();
        String sendImgUrl = UserRepository.getInstance(getActivity()).getUser().getAvatar();
        String sendName = UserRepository.getInstance(getActivity()).getUser().getRealName();
        if (sendImgUrl != null) {//头像地址
            expressEditEditSendUserImg.setImageURI(Uri.parse(sendImgUrl));
        }
        Utils.showLog(TAG, sendImgUrl);
        expressEditSendNameTv.setText(sendName);
        expressEditSendNumberTv.setText(sendPhone);
        //发货地址
        //  expressEditSendAddressTv.setText(((ExpressActivity)getActivity()).area+((ExpressActivity)getActivity()).detialLocation);

        // 收件人信息
        String ImgUrl = ((ExpressActivity) getActivity()).acceptImgUrl;
        if (ImgUrl != null) {
            expressAcceptUserEditImg.setImageURI(ImgUrl);
        }
        expressAcceptNameEditTv.setText(((ExpressActivity) getActivity()).acceptName);
        expressAcceptNumberEditTv.setText(((ExpressActivity) getActivity()).acceptPhone);
        expressAcceptAddressEditTv.setText(((ExpressActivity) getActivity()).area + ((ExpressActivity) getActivity()).detialLocation);



    }


    /**
     * 隐藏或显示快递列表
     * 显示或隐藏暂无快递
     *
     * @param b Ture隐藏快递列表
     */

    private void hideCompanyRv(boolean b) {
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


    @Override
    public void setMyExpressList(List<ExpressModel> list) {
        //快递列表
        expressModelList = list;
        boolean b = expressModelList == null || expressModelList.size() <= 0;
        hideCompanyRv(b);
        horizontalRecyclerViewAdapter = new HorizontalRecyclerViewAdapter(getActivity(), expressModelList);
        expressCompanyRv.setAdapter(horizontalRecyclerViewAdapter);
        horizontalRecyclerViewAdapter.setOnItemClickListener(new MyRvItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                //快递列表 条目点击事件
                ExpressModel expressModel = expressModelList.get(postion);
                //初始隐藏布局
                acceptflag = false;
                sendflag = false;
                hideSendExpressMessage(sendflag);
                hideAcceptExpressMessage(sendflag);
                //隐藏创建快递编辑布局
                expressOnTheWaySendMessageEditLl.setVisibility(View.GONE);
                //显示快递物流记录布局
                expressOnTheWayMessageRecordLl.setVisibility(View.VISIBLE);
                //对在路上的布局 内容赋值
                setExpressDetialText(expressModel);
                //隐藏地步栏
                ((ExpressActivity) getActivity()).hideBottomBar(true, "下一步");

            }


        });

    }

    /**
     * 在路上
     * 设置快递记录布局界面,初始化赋值
     *
     * @param expressModel 快递详情
     */
    private void setExpressDetialText(ExpressModel expressModel) {
        //快递单号id
        expressOddNumbersTv.setText(expressModel.getExpressId());
        //快递公司
        expressAddressNameTv.setText(expressModel.getExpressName() + "快递");
        //设置物流信息
        setExpressLogistics();

        //隐藏货物信息上无用布局 选择快递公司和用户信息
        expressOnTheWayKdLl.setVisibility(View.GONE);

        //发货人头像
        // expressSendUserImg.setImageURI(); 可以根据ol号去请求
        //

        //发货人姓名
        expressSendNameTv.setText(expressModel.getsName());
        //发货人电话
        expressSendNumberTv.setText(expressModel.getsPhone());

        //货物名称
        expressNameOfGoodsTv.setText(expressModel.getGoodsName());
        //货物重量
        expressWeightOfGoodsTv.setText(expressModel.getWeight());
        //发货地址
        expressSendAddressTv.setText(expressModel.getsAddress());
        //隐藏添加修改地址
        itemExpressUpdateAddressLl.setVisibility(View.GONE);


        //收货人头像
        //expressAcceptUserImg.setImageURI();

        //收货人姓名
        expressAcceptNameTv.setText(expressModel.getdName());
        //收货人电话
        expressAcceptNumberTv.setText(expressModel.getdPhone());
        //收货人地址
        expressAcceptAddressTv.setText(expressModel.getdAddress());
        //隐藏添加修改地址
        expressUpdateAcceptAddressTv.setVisibility(View.GONE);
        //收货信息已确认图标
        expressAcceptMessageComfirmedIv.setVisibility(View.VISIBLE);

    }

    private boolean acceptflag = false;
    private boolean sendflag = false;

    /**
     * 发快递信息  false表示当前是隐藏
     *
     * @param sendflag
     */
    private void hideSendExpressMessage(boolean sendflag) {
        arrowRecordDateTitleIv.setImageResource(sendflag ? R.mipmap.upwards : R.mipmap.downwards);
        expressSendMessageLl.setVisibility(sendflag ? View.VISIBLE : View.GONE);
        ToastUtil.show(getActivity(), "发货");
    }


    /**
     * 收快递信息
     *
     * @param acceptflag false的当前为隐藏
     */
    private void hideAcceptExpressMessage(boolean acceptflag) {
        arrowAcceptDateTitleIv.setImageResource(acceptflag ? R.mipmap.upwards : R.mipmap.downwards);
        expressMsgAcceptLl.setVisibility(acceptflag ? View.VISIBLE : View.GONE);
        ToastUtil.show(getActivity(), "收货");
    }

    /**
     * 设置物流信息
     */
    private void setExpressLogistics() {
        // expressCompanyRv.setAdapter();
        //显示 隐藏 expressNullCompanyTv
    }


    @Override
    public void setExpressCompany(List<ExpressModel.Company> list) {
        //快递公司列表
        companiesList = list;
        HorizontalExpressCompanyRecyclerViewAdapter adapter = new HorizontalExpressCompanyRecyclerViewAdapter(getActivity(), companiesList);
        expressOnTheWayEditKdRv.setAdapter(adapter);
        adapter.setOnItemClickListener(new MyRvItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                String companyNmae = companiesList.get(postion).getName();
                ((ExpressActivity) getActivity()).expressNo = companiesList.get(postion).getNumber();
                ToastUtil.show(getActivity(), "选择" + companyNmae + "快递");

                expressSendMessageKdTv.setText(companyNmae+"快递");
                //点击后 隐藏选择快递公司
                hideSelectCompany();

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({express_send_message_kd_tv, R.id.express_send_message_kd_iv, R.id.express_edit_update_send_address_tv,
            R.id.item_express_edit_update_address_ll, R.id.express_send_message_title_rl, R.id.express_accept_message_title_rl})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case express_send_message_kd_tv:
                //快递公司选择
                hideSelectCompany();
                break;
            case R.id.express_send_message_kd_iv:
                //快递公司选择
                hideSelectCompany();
                break;
            case R.id.express_edit_update_send_address_tv:
                //express_onTheWay_edit_kd_rv
                // 添加/修改地址 界面跳转
                ((ExpressActivity) getActivity()).editAddress();
                break;
            case R.id.express_send_message_title_rl:
                //点击切换显示发快递信息,false隐藏
                sendflag = !sendflag;
                hideSendExpressMessage(sendflag);
                break;
            case R.id.express_accept_message_title_rl:
                //点击切换显示收快递信息,false隐藏
                acceptflag = !acceptflag;
                hideAcceptExpressMessage(acceptflag);
                break;
        }
    }

    /**
     * 隐藏显示 可选择的快递公司
     * true表示要显示
     */
    @Override
    public void hideSelectCompany() {
        if (companyFlag) {
            //隐藏变显示
            expressOnTheWayEditKdLl.setVisibility(View.VISIBLE);
            //箭头变向上
            expressSendMessageKdIv.setImageResource(R.drawable.express_arrow_up);
            companyFlag = !companyFlag;
        } else {
            expressOnTheWayEditKdLl.setVisibility(View.GONE);
            expressSendMessageKdIv.setImageResource(R.drawable.express_arrow_down);
            companyFlag = !companyFlag;
        }
    }


}
