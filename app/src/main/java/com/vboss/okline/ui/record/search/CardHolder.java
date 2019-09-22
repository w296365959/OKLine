package com.vboss.okline.ui.record.search;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.user.DateUtils;
import com.vboss.okline.utils.Formater;
import com.vboss.okline.utils.TextUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vboss.okline.R.id.amout_delta_institution_date;
import static com.vboss.okline.R.id.date_view;
import static com.vboss.okline.R.id.detail_institution_date;
import static com.vboss.okline.R.id.invoice_institution_date;
import static com.vboss.okline.R.id.name_institution_date;
import static com.vboss.okline.R.id.points_delta_institution_date;
import static com.vboss.okline.R.id.pos_institution_date;
import static com.vboss.okline.R.id.receipt_institution_date;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/10 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class CardHolder extends RecyclerView.ViewHolder {
    @BindView(date_view)
    TextView dateView;
    @BindView(name_institution_date)
    TextView nameInstitutionDate;
    @BindView(amout_delta_institution_date)
    TextView amoutDeltaInstitutionDate;
    @BindView(points_delta_institution_date)
    TextView pointsDeltaInstitutionDate;
    @BindView(detail_institution_date)
    TextView detailInstitutionDate;
    @BindView(receipt_institution_date)
    TextView receiptInstitutionDate;
    @BindView(pos_institution_date)
    TextView posInstitutionDate;
    @BindView(invoice_institution_date)
    TextView invoiceInstitutionDate;
    @BindView(R.id.ll_invoivce_btns)
    LinearLayout llInvoivceBtns;

    public CardHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(final CardLog data) {
        dateView.setText(DateUtils.getyyMdHHmmss(data.transTime()));
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
//            if (data.cardMainType() != CardType.BANK_CARD) {
//                result = "余额" + result;
//            }
            amoutDeltaInstitutionDate.setText(result);
        }
    }
}
