package com.vboss.okline.ui.record.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.entities.OrgzCard;
import com.vboss.okline.ui.user.DateUtils;
import com.vboss.okline.ui.user.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/10 <br/>
 * Summary : 机构的RecyclerView的ViewHolder
 */
public class OrganizationHolder extends RecyclerView.ViewHolder {
    private final Context context;
    private final RecordSearchAdapter.OnItemClickListener onItemClickListener;
    @BindView(R.id.iv_card_icon)
    SimpleDraweeView ivCardIcon;
    @BindView(R.id.text_card_name)
    TextView textCardName;
    @BindView(R.id.text_event_time)
    TextView textEventTime;
    @BindView(R.id.text_event_content)
    TextView textEventContent;
    @BindView(R.id.container)
    LinearLayout container;

    public OrganizationHolder(View itemView, RecordSearchAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        context = itemView.getContext();
        ButterKnife.bind(this, itemView);
        this.onItemClickListener = onItemClickListener;
    }

    private static final String TAG = "OrganizationHolder";

    public void bind(final OrgzCard data, final int position) {
        ivCardIcon.setImageURI(data.imgUrl());
        textCardName.setText(data.cardName());
        String date = DateUtils.getOrgaListDate(data.transTime());
        textEventTime.setText(date);
        textEventContent.setText(data.remark());
        container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showLog(TAG,"点击事件："+v);
                onItemClickListener.onItemClick(container,position, data);
            }
        });
    }
}
