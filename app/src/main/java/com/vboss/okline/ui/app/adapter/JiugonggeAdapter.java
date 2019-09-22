package com.vboss.okline.ui.app.adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.ui.app.App;
import com.vboss.okline.ui.app.AppFragment;
import com.vboss.okline.ui.app.jiugongge.DraggableGridViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/28 9:51 <br/>
 * Summary  : 九宫格的adapter
 */
public class JiugonggeAdapter extends BaseAdapter {
    private static final String TAG = "JiugonggeAdapter";
    private List<App> mList = new ArrayList<>();

    List<AppEntity> commonList = new ArrayList<>();
    List<AppEntity> bankList = new ArrayList<>();
    List<AppEntity> vipList = new ArrayList<>();
    List<AppEntity> workList = new ArrayList<>();
    List<AppEntity> transList = new ArrayList<>();
    List<AppEntity> doorList = new ArrayList<>();
    List<AppEntity> cerList = new ArrayList<>();

    private Context mContext;
    private ViewHolder holder;
    AppFragment appFragment;
    JiuItemAdapter jiuItemAdapter;

    public JiugonggeAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public JiugonggeAdapter(Context context, List<App> lists) {
        this.mContext = context;
        this.mList = lists;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void refresh(List<App> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mList = list;
        notifyDataSetChanged();
    }

    public void setCommonData(List<AppEntity> list){
        if (list == null) {
            list = new ArrayList<>();
        }
        this.commonList = list;
        notifyDataSetChanged();
    }

    public void setVipData(List<AppEntity> list){
        if (list == null) {
            list = new ArrayList<>();
        }
        this.vipList = list;
        notifyDataSetChanged();
    }
    public void setBankData(List<AppEntity> list){
        if (list == null) {
            list = new ArrayList<>();
        }
        this.bankList = list;
        notifyDataSetChanged();
    }
    public void setTransData(List<AppEntity> list){
        if (list == null) {
            list = new ArrayList<>();
        }
        this.transList = list;
        notifyDataSetChanged();
    }
    public void setDoorData(List<AppEntity> list){
        if (list == null) {
            list = new ArrayList<>();
        }
        this.doorList = list;
        notifyDataSetChanged();
    }
    public void setCredentialsData(List<AppEntity> list){
        if (list == null) {
            list = new ArrayList<>();
        }
        this.cerList = list;
        notifyDataSetChanged();
    }
    public void setWorkData(List<AppEntity> list){
        if (list == null) {
            list = new ArrayList<>();
        }
        this.workList = list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.jiugongge_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.jiugongge_typeName.setText(mList.get(position).getAppName());

        final String type = mList.get(position).getAppName();
        jiuItemAdapter = new JiuItemAdapter(mContext);
        if (type.equals("常用")) {
            jiuItemAdapter.refresh(commonList);
        }else if(type.equals("会员")){
            jiuItemAdapter.refresh(vipList);
        }else if(type.equals("银行")){
            jiuItemAdapter.refresh(bankList);
        }else if(type.equals("工作")){
            jiuItemAdapter.refresh(workList);
        }else if(type.equals("证件")){
            jiuItemAdapter.refresh(cerList);
        }else if(type.equals("出行")){
            jiuItemAdapter.refresh(transList);
        }else if(type.equals("门禁")){
            jiuItemAdapter.refresh(doorList);
        }

        holder.app_gridview.setAdapter(jiuItemAdapter);
        holder.app_gridview.setEnabled(false);
        holder.app_gridview.setClickable(false);
        holder.app_gridview.setPressed(false);

        //弹出九宫格列表
        holder.jiugongge_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (appFragment != null) {
                    //Log.i(TAG,"getTypeName: " +mList.get(position).getAppName());
                    //appFragment.getTypeData("会员");
                    if (type.equals("常用")) {
                        appFragment.showJiugonggeDialog(commonList,type);
                    }else if(type.equals("会员")){
                        appFragment.showJiugonggeDialog(vipList,type);
                    }else if(type.equals("银行")){
                        appFragment.showJiugonggeDialog(bankList,type);
                    }else if(type.equals("工作")){
                        appFragment.showJiugonggeDialog(workList,type);
                    }else if(type.equals("证件")){
                        appFragment.showJiugonggeDialog(cerList,type);
                    }else if(type.equals("出行")){
                        appFragment.showJiugonggeDialog(transList,type);
                    }else if(type.equals("门禁")){
                        appFragment.showJiugonggeDialog(doorList,type);
                    }
                }
            }
        });

        return convertView;
    }


    public void setFragment(AppFragment appFragment){
        this.appFragment = appFragment;
    }

    static class ViewHolder {

        @BindView(R.id.jiugongge_item)
        LinearLayout jiugongge_item;

        @BindView(R.id.app_gridview)
        GridView app_gridview;

        @BindView(R.id.jiugongge_typeName)
        TextView jiugongge_typeName;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
