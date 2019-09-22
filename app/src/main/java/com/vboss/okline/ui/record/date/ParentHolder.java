package com.vboss.okline.ui.record.date;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.vboss.okline.R;
import com.vboss.okline.ui.user.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vboss.okline.R.id.arrow_record_date_title;
import static com.vboss.okline.R.id.text_record_date_title;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/2 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class ParentHolder extends ParentViewHolder {

    @BindView(text_record_date_title)
    TextView textRecordDateTitle;
    @BindView(arrow_record_date_title)
    ImageView arrowRecordDateTitle;
    @BindView(R.id.container)
    RelativeLayout container;

    public ParentHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull DateObj data, int position) {
        textRecordDateTitle.setText(DateUtils.getYearMonth(data.getName()));
        if (isExpanded()) {
            arrowRecordDateTitle.setImageResource(R.mipmap.upwards);
        } else {
            arrowRecordDateTitle.setImageResource(R.mipmap.downwards);
        }
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) container.getLayoutParams();
        if (position == 0) {
            layoutParams.setMargins(0, 0, 0, 1);
        } else {
            layoutParams.setMargins(0, 1, 0, 1);
        }
        container.setLayoutParams(layoutParams);
    }

    @Override
    public void onExpansionToggled(boolean expanded) {
        if (expanded) {
            arrowRecordDateTitle.setImageResource(R.mipmap.downwards);
        } else {
            arrowRecordDateTitle.setImageResource(R.mipmap.upwards);
        }
    }
}
