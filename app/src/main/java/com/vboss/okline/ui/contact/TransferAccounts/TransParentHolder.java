package com.vboss.okline.ui.contact.TransferAccounts;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.vboss.okline.R;
import com.vboss.okline.ui.user.DateUtils;

import butterknife.ButterKnife;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/4 10:51 <br/>
 * Summary  :
 */
public class TransParentHolder extends ParentViewHolder {

    private TextView text_record_date_title;
    private ImageView arrow_record_date_title;
    private View bottom_line;
    private Handler handler = new Handler();

    public TransParentHolder(@NonNull View itemView) {
        super(itemView);
        text_record_date_title = ButterKnife.findById(itemView, R.id.text_record_date_title);
        arrow_record_date_title = ButterKnife.findById(itemView, R.id.arrow_record_date_title);
        bottom_line = ButterKnife.findById(itemView, R.id.bottom_line);
    }

    public void bind(@NonNull TransDateObj data) {
        text_record_date_title.setText(DateUtils.getYearMonth(data.getName()));
        if (isExpanded()) {
            arrow_record_date_title.setImageResource(R.mipmap.upwards);
            bottom_line.setVisibility(View.VISIBLE);
        } else {
            arrow_record_date_title.setImageResource(R.mipmap.downwards);
            bottom_line.setVisibility(View.GONE);
        }
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        if (expanded) {
            arrow_record_date_title.setImageResource(R.mipmap.downwards);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottom_line.setVisibility(View.GONE);
                }
            }, 200);

        } else {
            arrow_record_date_title.setImageResource(R.mipmap.upwards);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottom_line.setVisibility(View.VISIBLE);
                }
            }, 10);
        }
    }
}
