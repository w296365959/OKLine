package com.vboss.okline.ui.record.organization;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.entities.OrgzCard;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/7 <br/>
 * Summary : 机构的recyclerView的adapter
 */
public class RecordOrganizationAdapter extends RecyclerView.Adapter<RecordOrganizationAdapter.DataViewHolder> {
    private List<OrgzCard> dataList = new ArrayList<>();
    private Context context;

    public RecordOrganizationAdapter(Context context) {
        this.context = context;
    }

    public void refresh(@NonNull List<OrgzCard> datas) {
        dataList.clear();
        dataList.addAll(datas);
        notifyDataSetChanged();
    }

    /**
     * 添加数据,跟在原数据后面
     * @param datas
     */
    public void addDatas(@NonNull List<OrgzCard> datas) {
        if (datas.size() > 0) {
            dataList.addAll(datas);
            notifyItemRangeInserted(dataList.size() - datas.size(), datas.size());
        }
    }

    @Override
    public DataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.view_record_institution, parent, false);
        return new DataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataViewHolder holder, int position) {
        if (position >= dataList.size()) {
            return;
        }
        final OrgzCard data = dataList.get(position);
        holder.ivCardIcon.setImageURI(data.imgUrl());
        holder.textCardName.setText(data.cardName());
        String date = DateUtils.getOrgaListDate(data.transTime());
        holder.textEventTime.setText(date);
        holder.textEventContent.setText(data.remark());
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrganizationDetailFragment fragment = new OrganizationDetailFragment();
                if (context instanceof MainActivity) {
                    ((MainActivity) context).addSecondFragment(fragment);
                    fragment.setOrgzCard(data);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {
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

        DataViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
