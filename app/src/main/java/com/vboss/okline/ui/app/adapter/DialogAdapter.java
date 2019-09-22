package com.vboss.okline.ui.app.adapter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.okline.vboss.assistant.base.Config;
import com.vboss.okline.R;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.app.App;
import com.vboss.okline.ui.app.AppFragment;
import com.vboss.okline.ui.app.AppHelper;
import com.vboss.okline.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/28 9:51 <br/>
 * Summary  : 九宫格弹出框的adapter
 */
public class DialogAdapter extends BaseAdapter {
    private static final String TAG = "DialogAdapter";
    private List<AppEntity> mList = new ArrayList<>();
    private Context mContext;
    private ViewHolder holder;
    PopupWindow popupWindow;
    AppFragment appFragment;
    public DialogAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public DialogAdapter(Context context, List<AppEntity> lists) {
        this.mContext = context;
        this.mList = lists;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void refresh(List<AppEntity> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mList = list;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.dialog_app_item_layout, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AppEntity app = mList.get(position);
        holder.appName.setText(app.appName());
        holder.appIcon.setImageURI(app.appIcon());
        Log.i(TAG,"dialog adapter app: " +app.appName() +" "+app.appIcon());
        //进入App
        holder.appIcon.setOnClickListener(new OnAppIconClickListener(position));

        holder.appIcon.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showPopUp(v,position);
                return true;
            }
        });
        return convertView;
    }

    class OnAppIconClickListener implements View.OnClickListener{
        AppEntity appEntity;

        public OnAppIconClickListener(int position) {
            appEntity = mList.get(position);
        }

        @Override
        public void onClick(View v) {
            try {
                UserRepository userRepository = UserRepository.getInstance(mContext);
                User user = userRepository.getUser();
                if (appEntity.appName().equals("卡助理")) {

                    if (user == null) {
                        Log.i(TAG,"user is null");
                        return;
                    }
                    AppHelper.startAssistant(mContext,user);
                }else {
                    if (!TextUtils.isEmpty(appEntity.openUrl())){
//                Intent intent = new Intent();
//                ComponentName componentName1 = ComponentName.unflattenFromString(appEntity.componentName());
//                intent.setComponent(componentName1);
                        Intent intent = mContext.getPackageManager().getLaunchIntentForPackage(appEntity.openUrl());
                        //modified by yuanshaoyu 2017-6-29 :增加欧乐社区穿欧乐号
                        intent.putExtra("olNo",user.getOlNo());
                        mContext.startActivity(intent);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void setFragment(AppFragment appFragment){
        this.appFragment = appFragment;
    }

    private void showPopUp(View v,final int position) {
        View popView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_layout,null);
        popupWindow = new PopupWindow(popView,
                (int) mContext.getResources().getDimension(R.dimen.cancle_star_dialog_width),
                (int) mContext.getResources().getDimension(R.dimen.add_star_dialog_height));
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        //总页数
        double pageSize = Math.ceil((float)mList.size()/9);
        //所在列
        double colNum = position % 3;
        //popupwindow的显示超出dialog的时候，改变popupwindow的显示方向
        if (colNum == 2.0) {
            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]-popupWindow.getWidth()+v.getWidth(), location[1]+v.getHeight()+10);
        }else {
            popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], location[1]+v.getHeight()+10);
        }

        String componentName = mList.get(position).componentName();
        //获取应用包名
        final String packageName = componentName.substring(0,componentName.indexOf("/"));

        SimpleDraweeView pop_img = (SimpleDraweeView) popView.findViewById(R.id.pop_img);
        TextView pop_title = (TextView) popView.findViewById(R.id.pop_title);
        pop_img.setImageURI(mList.get(position).appIcon());
        pop_title.setText(mList.get(position).appName());
        //加星标
        LinearLayout add_star_button = (LinearLayout) popView.findViewById(R.id.add_star);

        //LinearLayout app_upload = (LinearLayout) popView.findViewById(R.id.app_upload);
        final ImageView popupwindow_star = (ImageView) popView.findViewById(R.id.popupwindow_star);

        add_star_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appFragment.addStar((int) mList.get(position).appId());
                popupWindow.dismiss();
            }
        });
//        app_upload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                appFragment.onPoolAppUninstalled(mList,position,packageName);
//                popupWindow.dismiss();
//                notifyDataSetChanged();
//            }
//        });
    }

    static class ViewHolder {

        @BindView(R.id.app_name)
        TextView appName;

        @BindView(R.id.app_icon)
        SimpleDraweeView appIcon;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
