package com.vboss.okline.ui.record.organization;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.user.DateUtils;
import com.vboss.okline.utils.Formater;
import com.vboss.okline.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

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
 * Date    : 17/5/7 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class OrganizationDetailAdapter extends RecyclerView.Adapter<OrganizationDetailAdapter.DataViewHolder> {
    private List<CardLog> dataList = new ArrayList<>();

    public void refresh(@NonNull List<CardLog> datas) {
        dataList.clear();
        dataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void addDatas(@NonNull List<CardLog> datas) {
        if (datas.size() > 0) {
            dataList.addAll(datas);
            notifyItemRangeInserted(dataList.size() - datas.size(), datas.size());
        }
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_record_orga_detail_item, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        if (position >= dataList.size()) {
            return;
        }
        CardLog data = dataList.get(position);
        holder.dateView.setText(DateUtils.getyyMdHHmmss(data.transTime()));
        if (TextUtils.isEmpty(data.merName())) {
            holder.nameInstitutionDate.setText("未知商户");
        } else {
            holder.nameInstitutionDate.setText(data.merName());
        }
        String pos = data.slipUrl();
        if (TextUtils.isEmpty(pos)) {
            holder.posInstitutionDate.setVisibility(View.GONE);
        } else {
            holder.posInstitutionDate.setVisibility(View.VISIBLE);
        }
        String invoice = data.invoiceUrl();
        if (TextUtils.isEmpty(invoice)) {
            holder.invoiceInstitutionDate.setVisibility(View.GONE);
        } else {
            holder.invoiceInstitutionDate.setVisibility(View.VISIBLE);
        }
        String receipt = data.receiptUrl();
        if (TextUtils.isEmpty(receipt)) {
            holder.receiptInstitutionDate.setVisibility(View.GONE);
        } else {
            holder.receiptInstitutionDate.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(pos) && TextUtils.isEmpty(receipt) && TextUtils.isEmpty(invoice)) {
                        /*交易备注*/
            holder.detailInstitutionDate.setText(data.remark());
        } else {
            holder.detailInstitutionDate.setText("");
        }

        int pointsDelta = data.integral();
        int amoutDelta = data.amount();
        if (pointsDelta == 0) {
            holder.pointsDeltaInstitutionDate.setVisibility(View.GONE);
        } else {
            holder.pointsDeltaInstitutionDate.setVisibility(View.VISIBLE);
            holder.pointsDeltaInstitutionDate.setText("积分" + (pointsDelta >= 0 ? "+" : "") + pointsDelta);
        }
        if (amoutDelta == 0) {
            holder.amoutDeltaInstitutionDate.setVisibility(View.GONE);
        } else {
            holder.amoutDeltaInstitutionDate.setVisibility(View.VISIBLE);
            String s1 = Formater.money(amoutDelta);
            String result = (amoutDelta > 0 ? "+" : "") + s1;
            if (data.cardMainType() != CardType.BANK_CARD) {
//                result = "余额" + result;
            }
            holder.amoutDeltaInstitutionDate.setText(result);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
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

        DataViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
