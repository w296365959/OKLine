package com.vboss.okline.ui.contact.TransferAccounts;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.TransferProtocol;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/3 15:51 <br/>
 * Summary  : 转账管理界面
 */
public class TransferManagerFragment extends Fragment {
    private static final String TAG = "TransferManagerFragment";

    @BindView(R.id.trans_recyclerView)
    RecyclerView recyclerView;
//    @BindView(R.id.trans_ptrFrameLayout)
//    PtrClassicFrameLayout ptrFrameLayout;

    @BindView(R.id.transter_accounts_people)
    SimpleDraweeView transter_accounts_img;

    @BindView(R.id.transter_accounts_peopleName)
    TextView transter_accounts_peopleName;

    @BindView(R.id.transter_accounts_remark)
    TextView transter_accounts_remark;

    @BindView(R.id.no_trans_record)
    LinearLayout no_trans_record;

    private TransDateAdapter adapter;
    MainActivity activity;
    private String phone;
    private String olNo;
    private String name;
    private String avatar;
    private String remark;
    private int index = 1;
    private static final int PAGE_SIZE = 15;

    CommonAdapter<TransferProtocol> transRecordAdapter;
    List<TransferProtocol> transRecordList;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transfer_manager, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //initPtrFrameLayout();
        //initRecyclerView();
        //获取联系人信息
        Bundle bundle = getArguments();
        phone = bundle.getString("phone");
        name = bundle.getString("name");
        olNo = bundle.getString("olNo");
        avatar = bundle.getString("avatar");
        remark = bundle.getString("remark");

        //设置头像
        if (StringUtils.isNullString(avatar)) {
            transter_accounts_img.setImageResource(R.mipmap.default_avatar);
        } else {
            transter_accounts_img.setImageURI(Uri.parse(avatar));
        }

        transter_accounts_peopleName.setText(name);
        transter_accounts_remark.setText(remark);

        initAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        UserRepository userRepository = UserRepository.getInstance(getActivity());
        Log.i(TAG,"friendOlNo :" +olNo);
//        userRepository.getTransferHistory(olNo)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DefaultSubscribe<List<TransferProtocol>>(TAG) {
//                    @Override
//                    public void onNext(List<TransferProtocol> transferProtocols) {
//                        super.onNext(transferProtocols);
//                        //transRecordList.addAll(transferProtocols);
//                        Log.i(TAG, "转账卡片记录列表 transferProtocols.size :" + transferProtocols.size());
//                        transRecordAdapter.setmDatas(transferProtocols);
//                        transRecordAdapter.notifyDataSetChanged();
//
//                        //没有记录的时候
//                        if (transferProtocols.size() == 0) {
//                            no_trans_record.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(View.GONE);
//                        }else {
//                            no_trans_record.setVisibility(View.GONE);
//                            recyclerView.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
    }

    private void initAdapter() {
        transRecordList = new ArrayList<>();
        transRecordAdapter = new CommonAdapter<TransferProtocol>(getActivity(),R.layout.trans_record_date_child,transRecordList) {
            @Override
            public void convert(ViewHolder holder, TransferProtocol transferProtocol, int position) {
                if (transferProtocol.getPayObj() == 0) {
                    //己方作为转账对象
                    holder.setVisible(R.id.trans_my_imgLayout,true);
                    holder.setVisible(R.id.trans_otherSide_imgLayout,false);
                    holder.setImageByUrl(R.id.trans_my_personImg, Uri.parse(avatar).toString());
                    holder.setImageDrawable(R.id.trans_to_left, ActivityCompat.getDrawable(getActivity(),R.mipmap.trans_to_left));
                }else {
                    holder.setVisible(R.id.trans_my_imgLayout,false);
                    holder.setVisible(R.id.trans_otherSide_imgLayout,true);
                    if (!TextUtils.isEmpty(transferProtocol.getfImgUrl())) {
                        holder.setImageByUrl(R.id.trans_otherSide_img,transferProtocol.getfImgUrl());
                    }
                    holder.setImageDrawable(R.id.trans_to_left, ActivityCompat.getDrawable(getActivity(),R.mipmap.trans_to_right));
                }
                if (!TextUtils.isEmpty(transferProtocol.getfImgUrl())) {
                    holder.setImageByUrl(R.id.trans_otherSide_cardImg,transferProtocol.getfImgUrl());
                }
                if (!TextUtils.isEmpty(transferProtocol.getImgUrl())){
                    holder.setImageByUrl(R.id.trans_my_cardImg,transferProtocol.getImgUrl());
                }

                holder.setText(R.id.trans_otherSide_cardName,transferProtocol.getfCardName());
                holder.setText(R.id.trans_my_cardName,transferProtocol.getCardName());
                holder.setText(R.id.trans_otherSide_cardNo,transferProtocol.getfCardNo());
                holder.setText(R.id.trans_my_cardNo,transferProtocol.getCardNo());
                holder.setText(R.id.trans_money,String.valueOf(transferProtocol.getAmount()));

                // TODO: 2017/5/11  接收方已选择收款账户的时候点击跳转到支付通道界面，接收方收到邀请时跳转到选择银行卡的界面
                holder.setOnClickListener(R.id.cardRecord_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(),SelectAccountActivity.class);
                        v.getContext().startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(transRecordAdapter);
    }


//    private void initPtrFrameLayout() {
//        ptrFrameLayout.setLastUpdateTimeRelateObject(this);
//        ptrFrameLayout.setPtrHandler(new PtrDefaultHandler2() {
//
//            @Override
//            public void onLoadMoreBegin(PtrFrameLayout frame) {
//                loadMore();
//            }
//
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                refresh();
//            }
//
//            @Override
//            public boolean checkCanDoLoadMore(PtrFrameLayout frame, View content, View footer) {
//                return super.checkCanDoLoadMore(frame, recyclerView, footer);
//            }
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return super.checkCanDoRefresh(frame, recyclerView, header);
//            }
//        });
//        ptrFrameLayout.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ptrFrameLayout.autoRefresh();
//            }
//        }, 100);
//        ptrFrameLayout.setLoadingMinTime(2000);
//        ptrFrameLayout.setResistanceFooter(1.0f);
//        ptrFrameLayout.setDurationToCloseFooter(0); // footer will hide immediately when completed
//        ptrFrameLayout.setForceBackWhenComplete(true);
//        ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
//    }
//
//    private void initRecyclerView() {
//        adapter = new TransDateAdapter(getContext(), new ArrayList<TransDateObj>());
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//    }
//    private void refresh() {
//        loadParent(true);
//    }

    @OnClick({R.id.transManager_phone, R.id.transManager_chat, R.id.transManager_email,
            R.id.transManager_delivery, R.id.to_transfer,R.id.transter_accounts_people})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.transter_accounts_people:
                //返回上一界面
                activity.removeSecondFragment();
                break;
            case R.id.transManager_phone:
                //打电话
                ContactsUtils.safeCall(phone, activity);
                break;
            case R.id.transManager_chat:
                //聊天
                if (!StringUtils.isNullString(olNo)) {
                    ContactsUtils.personalChat(activity, name,remark, olNo, phone,avatar);
                } else {
                    ContactsUtils.normalChat(phone,activity);
                }
                break;
            case R.id.transManager_email:
                //发邮件
                break;
            case R.id.transManager_delivery:
                //物流
                break;
            case R.id.to_transfer:
                //给他转账
//                Intent intent = new Intent(getActivity(),TransPaymentActivity.class);
//                intent.putExtra("olNo",olNo);
//                startActivity(intent);
                Intent intent = new Intent(getActivity(),SelectAccountActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
//    private void loadParent(final boolean refresh) {
//        if (refresh) {
//            index = 1;
//        }
//        RecordRepository repository = RecordRepository.getInstance(getContext());
//        repository.getTransDateList(index, PAGE_SIZE)
//                //转换数据类型
//                .map(new Func1<List<String>, List<TransDateObj>>() {
//                    @Override
//                    public List<TransDateObj> call(List<String> strings) {
//                        List<TransDateObj> dateList = new ArrayList<>();
//                        for (String title : strings) {
//                            TransDateObj dateObj = new TransDateObj(title);
//                            dateList.add(dateObj);
//                        }
//                        return dateList;
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DefaultSubscribe<List<TransDateObj>>(TAG) {
//                    @Override
//                    public void onNext(List<TransDateObj> list) {
//                        ptrFrameLayout.refreshComplete();
//                        if (refresh) {
//                            adapter.refresh(list);
//                        } else {
//                            adapter.addDatas(list);
//                        }
//                        if (list.size() >= PAGE_SIZE) {
//                            ptrFrameLayout.setMode(PtrFrameLayout.Mode.BOTH);
//                            index++;
//                        } else {
//                            ptrFrameLayout.setMode(PtrFrameLayout.Mode.REFRESH);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        super.onError(throwable);
//                        ptrFrameLayout.refreshComplete();
//                        ToastUtil.show(getContext(), throwable.getMessage());
//                    }
//                });
//    }
//
//    private void loadMore() {
//        loadParent(false);
//    }
//
//    public void setDate(String date) {
//        List<TransDateObj> dateObjs = new ArrayList<>();
//        dateObjs.add(new TransDateObj(date));
//        adapter.refresh(dateObjs, true);
//    }

    public static Fragment newInstance(String phone, String olNo, String name,String remark,  String avatar) {
        TransferManagerFragment instance = new TransferManagerFragment();
        Bundle args = new Bundle();
        args.putString("phone", phone);
        args.putString("olNo", olNo);
        args.putString("name", name);
        args.putString("avatar", avatar);
        args.putString("remark", remark);
        instance.setArguments(args);
        return instance;
    }
}
