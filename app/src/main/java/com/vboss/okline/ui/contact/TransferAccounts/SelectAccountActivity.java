package com.vboss.okline.ui.contact.TransferAccounts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.CardRepository;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.app.App;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/7 10:51 <br/>
 * Summary  : 选择收款账户界面
 */
public class SelectAccountActivity extends Activity {
    private static final String TAG = "SelectAccountActivity";
    @BindView(R.id.fragment_toolbar)
    FragmentToolbar toolbar;

    @BindView(R.id.select_add_recyclerView)
    RecyclerView select_add_recyclerView;

    @BindView(R.id.select_bind_recyclerView)
    RecyclerView select_bind_recyclerView;

    @BindView(R.id.add_account_button)
    TextView add_account_button;

    CommonAdapter<CardEntity> bindAdapter;
    CommonAdapter<App> addAdapter;

    List<CardEntity> bindList;
    List<App> addList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_account);
        ButterKnife.bind(this);


        initToolBar();
        initAdapter();
        initBindData();
        initAddData();

        add_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectAccountActivity.this,EditAccountActivity.class);
                intent.putExtra("activityTag","add");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initAddData() {
        for (int i = 0; i < 5; i++) {
            addList.add(new App());
        }
        addAdapter.setmDatas(addList);
        addAdapter.notifyDataSetChanged();
    }

    private void initBindData() {
        CardRepository repository = CardRepository.getInstance(this);
        repository.cardList(CardType.BANK_CARD)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<List<CardEntity>>(TAG) {
                    @Override
                    public void onNext(List<CardEntity> cardEntities) {
                        super.onNext(cardEntities);

                        bindAdapter.setmDatas(cardEntities);
                        bindAdapter.notifyDataSetChanged();
                    }
                });

    }


    private void initAdapter() {
        bindList = new ArrayList<>();
        bindAdapter = new CommonAdapter<CardEntity>(SelectAccountActivity.this, R.layout.bind_account_item_layout, bindList) {
            @Override
            public void convert(ViewHolder holder, final CardEntity cardEntity, int position) {
                holder.setText(R.id.bindAccount_cardName,cardEntity.cardName());
                holder.setText(R.id.bindAccount_name,cardEntity.merName());
                if (!TextUtils.isEmpty(cardEntity.imgUrl())) {
                    holder.setImageByUrl(R.id.bindAccount_cardicon,cardEntity.imgUrl());
                }
                String substring = cardEntity.cardNo().substring(0,4)+"****"+cardEntity.cardNo().substring(cardEntity.cardNo().length() - 4);
                holder.setText(R.id.bindAccount_cardNo,substring);
                holder.setOnClickListener(R.id.bind__Account, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i(TAG,"接收方提供银行卡的卡号 ： " + cardEntity.cardNo());
                        TransHelper.getInstance().offerBankInfo(cardEntity.cardNo());
                        finish();
                    }
                });
            }
        };
        select_bind_recyclerView.setLayoutManager(new LinearLayoutManager(SelectAccountActivity.this));
        select_bind_recyclerView.setAdapter(bindAdapter);

        addList = new ArrayList<>();
        addAdapter = new CommonAdapter<App>(SelectAccountActivity.this, R.layout.add_account_item_layout, addList) {
            @Override
            public void convert(final ViewHolder holder, final App app, int position) {
                holder.setOnClickListener(R.id.edit_account, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SelectAccountActivity.this,EditAccountActivity.class);
                        intent.putExtra("activityTag","edit");
                        intent.putExtra("accountName",holder.getText(R.id.addAccount_name));
                        intent.putExtra("accountNo",holder.getText(R.id.addAccount_cardNo));
                        startActivity(intent);
                    }
                });
            }
        };
        select_add_recyclerView.setLayoutManager(new FullyLinearLayoutManager(SelectAccountActivity.this));
        select_add_recyclerView.setNestedScrollingEnabled(false);
        select_add_recyclerView.setAdapter(addAdapter);
    }

    private void initToolBar() {
        toolbar.setActionTitle("选择收款账户");
        toolbar.setActionLogoIcon(R.mipmap.orange_logo);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                // 返回一级界面
                finish();
                //add by yuanshoayu 2017-5-19 : cancle activity Toggle animation
                overridePendingTransition(0,0);
            }
        });

    }
}
