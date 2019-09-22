package com.vboss.okline.ui.express.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.data.model.ExpressModel;
import com.vboss.okline.ui.express.MyRvItemClickListener;
import com.vboss.okline.ui.user.Utils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/8 11:19 </br>
 * Summary: 可选择的快递公司
 */

public class HorizontalExpressCompanyRecyclerViewAdapter extends RecyclerView.Adapter {
    public static final String TAG = "HorizontalExpressCompanyRecyclerViewAdapter";


    private Context context;
    private List<ExpressModel.Company> list;
    private MyRvItemClickListener myRvItemClickListener;
    /**
     * 设置Item点击监听
     * @param listener
     */
    public void setOnItemClickListener(MyRvItemClickListener listener){
        this.myRvItemClickListener = listener;
    }

    public HorizontalExpressCompanyRecyclerViewAdapter(Context context, List<ExpressModel.Company> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_express_textview, parent, false);

        return new HorizontalExpressCompanyRecyclerViewHolder(view,myRvItemClickListener);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HorizontalExpressCompanyRecyclerViewHolder horizontalRecyclerViewHolder = (HorizontalExpressCompanyRecyclerViewHolder) holder;

        horizontalRecyclerViewHolder.setData(position, list);

    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }

    }

    static class HorizontalExpressCompanyRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.express_edit_kd_textView)
        TextView expressEditKdTextView;
        private MyRvItemClickListener mListener;
        HorizontalExpressCompanyRecyclerViewHolder(View view,MyRvItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            this.mListener=listener;
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener != null){
                mListener.onItemClick(v,getLayoutPosition());
            }
        }

        public void setData(final int position, List<ExpressModel.Company> list) {
            Utils.showLog(TAG, "集合信息:==" + list.toString());
            if (list != null && !list.isEmpty()) {
                ExpressModel.Company company = list.get(position);
                expressEditKdTextView.setText(company.getName()+"快递");
            }


        }



    }


}
