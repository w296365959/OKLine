package com.vboss.okline.ui.express.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.okline.util.LogUtil;
import com.vboss.okline.R;
import com.vboss.okline.data.model.ExpressModel;
import com.vboss.okline.ui.express.MyRvItemClickListener;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.R.attr.data;
import static com.baidu.location.d.j.U;
import static com.baidu.location.d.j.p;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: wangzhongming<br/>
 * Email:  wangzhongming@okline.cn</br>
 * Date :  2017/7/4 11:19 </br>
 * Summary: 快递列表
 */

public class HorizontalRecyclerViewAdapter extends RecyclerView.Adapter {
    public static final String TAG = "HorizontalRecyclerViewAdapter";

    private Context context;
    private List<ExpressModel> list;
    private MyRvItemClickListener myRvItemClickListener;
    public Vector<Boolean> vector = new Vector<>();
    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(MyRvItemClickListener listener) {
        this.myRvItemClickListener = listener;
    }

    public HorizontalRecyclerViewAdapter(Context context, List<ExpressModel> list) {
        this.context = context;
        this.list = list;
        for (ExpressModel expressModel : list) {
            vector.add(false);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_express_company, parent, false);

        return new HorizontalRecyclerViewHolder(view, myRvItemClickListener);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HorizontalRecyclerViewHolder horizontalRecyclerViewHolder = (HorizontalRecyclerViewHolder) holder;

        horizontalRecyclerViewHolder.setData(position, list);


        if(vector.get(position)){
            horizontalRecyclerViewHolder.itemExpressCompanyLl.setSelected(true);
        }else{
            horizontalRecyclerViewHolder.itemExpressCompanyLl.setSelected(false);
        }
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        } else {
            return 0;
        }

    }

    class HorizontalRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.express_type_img_iv)
        ImageView expressTypeImgIv;
        @BindView(R.id.express_address_tv)
        TextView expressAddressTv;
        @BindView(R.id.express_company_tv)
        TextView expressCompanyTv;
        @BindView(R.id.express_username_tv)
        TextView expressUsernameTv;
        @BindView(R.id.item_express_company_new_iv)
        ImageView itemExpressCompanyNewIv;
        @BindView(R.id.express_address_and_company_fl)
        FrameLayout expressAddressAndCompanyFl;
        @BindView(R.id.item_express_company_ll)
        LinearLayout itemExpressCompanyLl;

        private MyRvItemClickListener mListener;
        private List<ExpressModel> holderList;

        HorizontalRecyclerViewHolder(View view, MyRvItemClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            this.mListener = listener;
            view.setOnClickListener(this);
        }

        public void setData(final int position, List<ExpressModel> list) {
            Utils.showLog(TAG, "集合信息:==" + list.toString());
            holderList = list;
            if (list != null && !list.isEmpty()) {
                ExpressModel expressModel = list.get(position);
                if (expressModel.getExpressType() == 1) {
                    //1我发出,写对方人名与地址
                    expressTypeImgIv.setImageResource(R.drawable.send_out);

                    expressUsernameTv.setText(expressModel.getsName());
                } else {
                    //2我接受
                    expressTypeImgIv.setImageResource(R.drawable.send_into);

                    expressUsernameTv.setText(expressModel.getdName());
                }
                expressAddressTv.setText(expressModel.getAreaName());
                expressCompanyTv.setText(expressModel.getExpressName());
            }


        }


        @Override
        public void onClick(View v) {
            if (mListener != null && holderList != null) {
                for (int i = 0; i < holderList.size(); i++) {
                    vector.set(i,false);
                 }
                vector.set(getLayoutPosition(),true);
                notifyDataSetChanged();


                mListener.onItemClick(v, getLayoutPosition());

            }
        }
    }


}
