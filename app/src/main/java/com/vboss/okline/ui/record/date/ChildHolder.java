package com.vboss.okline.ui.record.date;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.user.DateUtils;
import com.vboss.okline.utils.Formater;
import com.vboss.okline.utils.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/2 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class ChildHolder extends ChildViewHolder<CardLog> {
    @BindView(R.id.date_view)
    TextView dateView;
    @BindView(R.id.time_view)
    TextView timeView;
    @BindView(R.id.icon_date)
    SimpleDraweeView iconDate;
    @BindView(R.id.name_card_date)
    TextView nameCardDate;
    @BindView(R.id.name_institution_date)
    TextView nameInstitutionDate;
    @BindView(R.id.amout_delta_institution_date)
    TextView amoutDeltaInstitutionDate;
    @BindView(R.id.points_delta_institution_date)
    TextView pointsDeltaInstitutionDate;
    @BindView(R.id.detail_institution_date)
    TextView detailInstitutionDate;
    @BindView(R.id.receipt_institution_date)
    TextView receiptInstitutionDate;
    @BindView(R.id.pos_institution_date)
    TextView posInstitutionDate;
    @BindView(R.id.invoice_institution_date)
    TextView invoiceInstitutionDate;
    @BindView(R.id.ll_invoivce_btns)
    LinearLayout llInvoivceBtns;
    @BindView(R.id.bottom_line)
    View bottomLine;

    public ChildHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(CardLog data) {
        String transTime = data.transTime();
        String[] strings = transTime.split(" ");
        String[] substrings = strings[0].split("-");
        dateView.setText(substrings[1]+"月"+substrings[2]+"日");
        timeView.setText(strings[1]);
        iconDate.setImageURI(data.imgUrl());
        nameCardDate.setText(data.cardName());
                    /*商户名称*/
        if (TextUtils.isEmpty(data.merName())) {
            nameInstitutionDate.setText("未知商户");
        } else {
            nameInstitutionDate.setText(data.merName());
        }
        String pos = data.slipUrl();
        if (TextUtils.isEmpty(pos)) {
            posInstitutionDate.setVisibility(View.GONE);
        } else {
            posInstitutionDate.setVisibility(View.VISIBLE);
        }
        String invoice = data.invoiceUrl();
        if (TextUtils.isEmpty(invoice)) {
            invoiceInstitutionDate.setVisibility(View.GONE);
        } else {
            invoiceInstitutionDate.setVisibility(View.VISIBLE);
        }
        String receipt = data.receiptUrl();
        if (TextUtils.isEmpty(receipt)) {
            receiptInstitutionDate.setVisibility(View.GONE);
        } else {
            receiptInstitutionDate.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(pos) && TextUtils.isEmpty(receipt) && TextUtils.isEmpty(invoice)) {
                        /*交易备注*/
            detailInstitutionDate.setText(data.remark());
        } else {
            detailInstitutionDate.setText("");
        }

        int pointsDelta = data.integral();
        int amoutDelta = data.amount();
        if (pointsDelta == 0) {
            pointsDeltaInstitutionDate.setVisibility(View.GONE);
        } else {
            pointsDeltaInstitutionDate.setVisibility(View.VISIBLE);
            pointsDeltaInstitutionDate.setText("积分" + (pointsDelta >= 0 ? "+" : "") + pointsDelta);
        }
        if (amoutDelta == 0) {
            amoutDeltaInstitutionDate.setVisibility(View.GONE);
        } else {
            amoutDeltaInstitutionDate.setVisibility(View.VISIBLE);
            String s1 = Formater.money(amoutDelta);
            String result = (amoutDelta > 0 ? "+" : "") + s1;
            if (data.cardMainType() != CardType.BANK_CARD) {
//                result = "余额" + result;
            }
            amoutDeltaInstitutionDate.setText(result);
        }
    }
}
