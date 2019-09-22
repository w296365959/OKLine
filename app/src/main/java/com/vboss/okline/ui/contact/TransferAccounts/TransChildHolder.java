package com.vboss.okline.ui.contact.TransferAccounts;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.TextUtils;

import butterknife.ButterKnife;


/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/4 10:51 <br/>
 * Summary  :
 */
public class TransChildHolder extends ChildViewHolder<TransLog> {
    private TextView trans_record_date;
    private TextView trans_record_state;
    private SimpleDraweeView trans_otherSide_img;
    private SimpleDraweeView trans_otherSide_cardImg;
    private TextView trans_otherSide_cardName;
    private TextView trans_otherSide_cardNo;
    private TextView trans_money;
    private ImageView trans_to_left;
    private SimpleDraweeView trans_my_cardImg;
    private TextView trans_my_cardName;
    private TextView trans_my_cardNo;
    private SimpleDraweeView trans_my_personImg;
    RelativeLayout cardRecord_item;

    public TransChildHolder(@NonNull View itemView) {
        super(itemView);
        trans_record_date = ButterKnife.findById(itemView, R.id.trans_record_date);
        trans_record_state = ButterKnife.findById(itemView, R.id.trans_record_state);
        trans_otherSide_img = ButterKnife.findById(itemView, R.id.trans_otherSide_img);
        trans_otherSide_cardImg = ButterKnife.findById(itemView, R.id.trans_otherSide_cardImg);
        trans_otherSide_cardName = ButterKnife.findById(itemView, R.id.trans_otherSide_cardName);
        trans_otherSide_cardNo = ButterKnife.findById(itemView, R.id.trans_otherSide_cardNo);
        trans_money = ButterKnife.findById(itemView, R.id.trans_money);
        trans_to_left = ButterKnife.findById(itemView, R.id.trans_to_left);
        trans_my_cardImg = ButterKnife.findById(itemView, R.id.trans_my_cardImg);
        trans_my_cardName = ButterKnife.findById(itemView, R.id.trans_my_cardName);
        trans_my_cardNo = ButterKnife.findById(itemView, R.id.trans_my_cardNo);
        trans_my_personImg = ButterKnife.findById(itemView, R.id.trans_my_personImg);
        cardRecord_item = ButterKnife.findById(itemView,R.id.cardRecord_item);

    }

    public void bind(TransLog cardLog) {
        trans_record_date.setText(cardLog.getTransTime());
        trans_record_state.setText(cardLog.getTransState());
        trans_otherSide_img.setImageURI(cardLog.getOtherPersonImg());
        trans_otherSide_cardImg.setImageURI(cardLog.getOtherCardImgUrl());
        trans_otherSide_cardName.setText(cardLog.getOtherCardName());
        trans_otherSide_cardNo.setText(cardLog.getOtherCardNo());
        trans_money.setText(cardLog.getTransMoney());
        // TODO: 2017/5/4 判断转账方，确定箭头方向 trans_to_left
        trans_my_cardImg.setImageURI(cardLog.getMyCardImgUrl());
        trans_my_cardName.setText(cardLog.getMyCardName());
        trans_my_cardNo.setText(cardLog.getMyCardNo());
        trans_my_personImg.setImageURI(cardLog.getMyImg());

        // TODO: 2017/5/10  根据转账方判断跳转到哪个转账记录界面
        cardRecord_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),TransRecordActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }
}
