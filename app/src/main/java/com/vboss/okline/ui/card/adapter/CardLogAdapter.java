package com.vboss.okline.ui.card.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.ui.card.ticket.CardTicketActivity;
import com.vboss.okline.utils.Formater;

import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/30 11:10 <br/>
 * Summary  : 在这里描述Class的主要功能
 */

public class CardLogAdapter extends RecyclerView.Adapter {
    private static final String TAG = CardLogAdapter.class.getSimpleName();
    private static final int MODE_CARD_PAPER = 1;
    private static final int MODE_CARD_NO_PAPER = 2;
    private LayoutInflater inflater;
    private List<CardLog> mData;
    private Context mContext;

    public CardLogAdapter(Context context, List<CardLog> data) {
        inflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View convertView;
        if (viewType == MODE_CARD_NO_PAPER) {
            convertView = inflater.inflate(R.layout.row_card_log, parent, false);
            return new LogViewHolder(convertView);
        } else {
            convertView = inflater.inflate(R.layout.row_card_log_paper, parent, false);
            return new PaperLogViewHolder(convertView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        CardLog cardLog = null;
        if (position < mData.size()) {
            cardLog = mData.get(position);
        }
        if (cardLog == null) {
            return MODE_CARD_NO_PAPER;
        }
        if (hasTicket(cardLog)) {
            return MODE_CARD_NO_PAPER;
        } else {
            return MODE_CARD_PAPER;
        }
    }

    /**
     * Add by 2017-04-11
     * if return true has ticket
     * return false has no ticket
     *
     * @param cardLog CardLog object
     * @return boolean
     */
    private boolean hasTicket(CardLog cardLog) {
        return TextUtils.isEmpty(cardLog.invoiceUrl())
                && TextUtils.isEmpty(cardLog.receiptUrl())
                && TextUtils.isEmpty(cardLog.slipUrl());
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        CardLog cardLog = null;
        if (position < mData.size()) {
            cardLog = mData.get(position);
        }
        if (cardLog != null) {
            if (viewType == MODE_CARD_NO_PAPER) {
                LogViewHolder logViewHolder = (LogViewHolder) holder;
                logViewHolder.tv_log_name.setText(cardLog.merName());
                logViewHolder.tv_log_dec.setText(cardLog.remark());
                logViewHolder.tv_log_date.setText(dateFormat(cardLog.transTime()));
                logViewHolder.tv_log_money.setText(com.vboss.okline.utils.TextUtils.formatMoney(cardLog.amount()));
                //Added by wangshuai 2017-06-12 消费金额显示
                if (cardLog.amount() > 0) {
                    logViewHolder.tv_log_money.setText("+" + Formater.money(cardLog.amount()));
                } else {
                    logViewHolder.tv_log_money.setText(Formater.money(cardLog.amount()));
                }
                //modify by wangshuai 2017-06-15 card log integral show
                if (cardLog.integral() > 0) {
                    logViewHolder.tv_log_integral.setText(String.format(mContext.getString(R.string.card_log_integral_1), cardLog.integral()));
                } else {
                    logViewHolder.tv_log_integral.setText(String.format(mContext.getString(R.string.card_log_integral), cardLog.integral()));
                }
                if (cardLog.integral() == 0) {
                    logViewHolder.tv_log_integral.setVisibility(View.GONE);
                } else {
                    logViewHolder.tv_log_integral.setVisibility(View.VISIBLE);
                }

            }
            if (viewType == MODE_CARD_PAPER) {
                PaperLogViewHolder viewHolder = (PaperLogViewHolder) holder;
                viewHolder.tv_log_pos.setVisibility(View.INVISIBLE);
                viewHolder.tv_log_ticket.setVisibility(View.INVISIBLE);
                viewHolder.tv_log_invoice.setVisibility(View.INVISIBLE);

                viewHolder.tv_log_name.setText(cardLog.merName());
                viewHolder.tv_log_date.setText(dateFormat(cardLog.transTime()));
                if (cardLog.integral() > 0) {
                    viewHolder.tv_log_integral.setText(String.format(mContext.getString(R.string.card_log_integral_1), cardLog.integral()));
                } else {
                    viewHolder.tv_log_integral.setText(String.format(mContext.getString(R.string.card_log_integral), cardLog.integral()));
                }
                //Added by wangshuai 2017-06-12 消费金额显示
                if (cardLog.amount() > 0) {
                    viewHolder.tv_log_money.setText("+" + Formater.money(cardLog.amount()));
                } else {
                    viewHolder.tv_log_money.setText(Formater.money(cardLog.amount()));
                }
                /*viewHolder.tv_log_money.setText(String.format(mContext.getString(R.string.card_log_balance),
                        com.vboss.okline.utils.TextUtils.formatMoney(cardLog.amount())));*/
                if (cardLog.integral() == 0) {
                    viewHolder.tv_log_integral.setVisibility(View.GONE);
                } else {
                    viewHolder.tv_log_integral.setVisibility(View.VISIBLE);
                }

                /*
                 *  just pos ticket invoice is visible
                 *
                 */
                if (!TextUtils.isEmpty(cardLog.slipUrl())) {
                    viewHolder.tv_log_pos.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(cardLog.receiptUrl())) {
                    viewHolder.tv_log_ticket.setVisibility(View.VISIBLE);
                }
                if (!TextUtils.isEmpty(cardLog.invoiceUrl())) {
                    viewHolder.tv_log_invoice.setVisibility(View.VISIBLE);
                }

                viewHolder.tv_log_pos.setOnClickListener(new TicketClickListener(0, cardLog.slipUrl()));
                viewHolder.tv_log_ticket.setOnClickListener(new TicketClickListener(1, cardLog.receiptUrl()));
                viewHolder.tv_log_invoice.setOnClickListener(new TicketClickListener(2, cardLog.invoiceUrl()));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    private class LogViewHolder extends RecyclerView.ViewHolder {
        TextView tv_log_date;
        TextView tv_log_name;
        TextView tv_log_money;
        TextView tv_log_dec;
        TextView tv_log_integral;


        LogViewHolder(View itemView) {
            super(itemView);
            tv_log_date = (TextView) itemView.findViewById(R.id.tv_log_date);
            tv_log_name = (TextView) itemView.findViewById(R.id.tv_log_name);
            tv_log_money = (TextView) itemView.findViewById(R.id.tv_log_money);
            tv_log_dec = (TextView) itemView.findViewById(R.id.tv_log_desc);
            tv_log_integral = (TextView) itemView.findViewById(R.id.tv_log_integral);
        }
    }

    private class PaperLogViewHolder extends RecyclerView.ViewHolder {
        TextView tv_log_date;
        TextView tv_log_name;
        TextView tv_log_money;
        TextView tv_log_integral;
        /*TextView tv_log_pos;
        TextView tv_log_invoice;
        TextView tv_log_ticket;*/

        ImageButton tv_log_pos;
        ImageButton tv_log_invoice;
        ImageButton tv_log_ticket;

        PaperLogViewHolder(View itemView) {
            super(itemView);
            tv_log_date = (TextView) itemView.findViewById(R.id.tv_log_date);
            tv_log_name = (TextView) itemView.findViewById(R.id.tv_log_name);
            tv_log_money = (TextView) itemView.findViewById(R.id.tv_log_money);
            tv_log_integral = (TextView) itemView.findViewById(R.id.tv_log_integral);
            /*tv_log_pos = (TextView) itemView.findViewById(R.id.tv_card_log_pos);
            tv_log_invoice = (TextView) itemView.findViewById(R.id.tv_card_log_invoice);
            tv_log_ticket = (TextView) itemView.findViewById(R.id.tv_card_log_ticket);*/

            tv_log_pos = (ImageButton) itemView.findViewById(R.id.ib_print_pos);
            tv_log_invoice = (ImageButton) itemView.findViewById(R.id.ib_print_invoice);
            tv_log_ticket = (ImageButton) itemView.findViewById(R.id.ib_print_ticket);
        }
    }

    /**
     * date format
     *
     * @param src date String source
     * @return format date string
     */
    public static String dateFormat(String src) {
        String simple = "2012-12-12";
        if (src == null || src.length() < 8) return "";
        String year = src.substring(0, 4);
        String month = src.substring(5, 7);
        String day = src.substring(8, simple.length());
        String time = "";
        if (src.length() > simple.length()) {
            time = src.substring(simple.length(), src.length());
        }
        return year + "年" + month + "月" + day + "日" + time;
    }

    private class TicketClickListener implements View.OnClickListener {
        private int position;
        private String url;

        TicketClickListener(int i, String url) {
            this.position = i;
            this.url = url;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, CardTicketActivity.class);
            intent.putExtra(CardTicketActivity.KEY_ACTION_MODE, position);
            intent.putExtra(CardTicketActivity.KEY_ACTION_URL, url);
            mContext.startActivity(intent);
        }
    }
}
