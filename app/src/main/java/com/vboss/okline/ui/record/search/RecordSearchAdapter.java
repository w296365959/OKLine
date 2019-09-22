package com.vboss.okline.ui.record.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vboss.okline.R;
import com.vboss.okline.data.entities.CardLog;
import com.vboss.okline.data.entities.OrgzCard;

import java.util.ArrayList;
import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/8 <br/>
 * Summary : 搜索结果的RecyclerView Adapter
 */
public class RecordSearchAdapter extends RecyclerView.Adapter {
    private Context context;
    private List<Object> dataList = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnItemClickListener listener;

    public RecordSearchAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    /**
     * 条目点击监听
     */
    public interface OnItemClickListener{
        void onItemClick(View view, int position, OrgzCard card);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 刷新
     * @param datas
     */
    public void refresh(List<Object> datas) {
        dataList.clear();
        dataList.addAll(datas);
        notifyDataSetChanged();
    }
    /**
     * 添加数据
     * @param datas
     */
    public void addDatas(List<Object> datas) {
        if (datas.size() > 0) {
            dataList.addAll(datas);
            //从第i个开始添加进数据刷新条目显示
            notifyItemRangeInserted(dataList.size() - datas.size(), datas.size());
        }
    }


    /**
     * 条目类型
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        Object object = dataList.get(position);
        if (object instanceof OrgzCard) {//机构
            return RecordSearchActivity.TYPE_ORGANIZATION;
        } else if (object instanceof CardLog) {//卡
            return RecordSearchActivity.TYPE_CARD;
        } else {//会话
            return RecordSearchActivity.TYPE_CONVERSATION;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case RecordSearchActivity.TYPE_ORGANIZATION:
                view = layoutInflater.inflate(R.layout.view_record_institution, parent, false);
                return new OrganizationHolder(view, listener);
            case RecordSearchActivity.TYPE_CARD:
                view = layoutInflater.inflate(R.layout.view_record_orga_detail_item, parent, false);
                return new CardHolder(view);
            case RecordSearchActivity.TYPE_CONVERSATION:
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Object object = dataList.get(position);
        if (object instanceof OrgzCard && holder instanceof OrganizationHolder) {
            ((OrganizationHolder) holder).bind((OrgzCard) object, position);
        } else if (object instanceof CardLog && holder instanceof CardHolder) {
            ((CardHolder) holder).bind((CardLog) object);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}
