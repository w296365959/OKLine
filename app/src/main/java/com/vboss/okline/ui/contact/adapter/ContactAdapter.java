package com.vboss.okline.ui.contact.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.bean.Contact;
import com.vboss.okline.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.vboss.okline.R.id.linearLayoutMenu;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/28 16:15
 * Desc :
 */

public class ContactAdapter extends BaseAdapter {
    //TODO 这里的集合类型换成ContactItem泛型的

    private static final String TAG = "ContactsAdapter";
    /**
     * 三种类型
     */
    private final int TYPE_STAR_FRIEND = 1;
    private final int TYPE_GROUP = 2;
    private final int TYPE_FRIEND = 3;
    boolean firstGroup = true;
    private int total = 0;
    private List<String> addList = new ArrayList<>();
    private boolean firstStar = true;
    private int contentType;  //1 是创建群组界面  2 是联系人主界面 3 是搜索界面
    private List<Contact> mList = new ArrayList<>();
    private Context mContext;

    public ContactAdapter(List<Contact> mList, Context mContext, int contentType) {
        if (null != mList && mList.size() > 0) {
            this.mList = mList;
        }
        this.mContext = mContext;
        this.contentType = contentType;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void refresh(List<Contact> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mList = list;
//        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        Log.i("ContactAdapter", "getItem: " + mList.size());
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

//    @Override
//    public int getViewTypeCount() {
//        return 3;
//    }

//    @Override
//    public int getItemViewType(int position) {
//        Log.i("ContactAdapter", "getItemViewType: " + position + " ListSize:" + mList.size());
//        //TODO 这里返回viewType 根据bean里的type判断
//        if (mList.get(position).getType() == 1) {
//            return TYPE_STAR_FRIEND;
//        } else if (mList.get(position).getType() == 2) {
//            return TYPE_GROUP;
//        } else {
//            return TYPE_FRIEND;
//        }

//    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
//        int viewType = getItemViewType(position);
        Contact contact = mList.get(position);
        String name = contact.getRemark();
        String group = contact.getGroup();
        String phone = contact.getPhone();
        String realName = contact.getName();

        boolean star = contact.isStar();
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        String firstLetter = ContactsUtils.filledData(name);
        int section = firstLetter.charAt(0);
        if (1 == contentType){//创建群组选择联系人
            holder.checkBox.setVisibility(View.VISIBLE);
            //排序,如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                holder.tvCatalog.setVisibility(View.VISIBLE);
                //下划线隐藏
                holder.itemUnderline.setVisibility(View.GONE);

                holder.tvCatalog.setText(firstLetter);
            } else {
                holder.itemUnderline.setVisibility(View.VISIBLE);
                holder.tvCatalog.setVisibility(View.GONE);

            }
           /* if (addList != null && addList.contains(contact.getRemark())) {
                    holder.checkBox.setChecked(true);
                }
                if (holder.checkBox != null){
                    final ViewHolder finalHolder = holder;
                    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                Bitmap bitmap = null;
                                bitmap = ((BitmapDrawable) finalHolder.avatar.getDrawable()).getBitmap();
                                showCheckImage(bitmap, mList.get(position));
                            } else {
                                // 用户显示在滑动栏删除
                                deleteImage(mList.get(position));
                            }
                        }
                    });
                }*/

        }else{
            holder.checkBox.setVisibility(View.GONE);
            if (star){
                if (firstStar){
                    Drawable drawable = ActivityCompat.getDrawable(mContext, R.drawable.star);
                    holder.tvCatalog.setText("我关心的人");
                    holder.tvCatalog.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                    holder.tvCatalog.setCompoundDrawablePadding(10);
                    firstStar = false;
                }else{
                    holder.itemUnderline.setVisibility(View.VISIBLE);
                    holder.tvCatalog.setVisibility(View.GONE);
                }

            }else if(!StringUtils.isNullString(group)){
                holder.tvCatalog.setText("群");
            }else{
                //排序,如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
                if (position == getPositionForSection(section)) {
                    holder.tvCatalog.setVisibility(View.VISIBLE);
                    //下划线隐藏
                    holder.itemUnderline.setVisibility(View.GONE);

                    holder.tvCatalog.setText(firstLetter);
                } else {
                    holder.itemUnderline.setVisibility(View.VISIBLE);
                    holder.tvCatalog.setVisibility(View.GONE);

                }
            }
        }

        if (StringUtils.isNullString(realName)) {
            holder.ivContactIscheck.setVisibility(View.GONE);
        }
        holder.tvUserName.setText(name);
        holder.tvRealName.setText(phone);
        return view;
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {

        for (int i = 0, m = getCount(); i < m; i++) {
//            char firstChar = getFirstLetter(mList.get(i)).matches("[A-Z]") ? getFirstLetter(mList.get(i)).charAt(0) : '#';
            char firstChar = ContactsUtils.filledData(mList.get(i).getRemark()).charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

/*    //显示选择的头像
    public void showCheckImage(Bitmap bitmap, Contact glufineid) {
        total++;
        // 包含TextView的LinearLayout
        // 参数设置
        android.widget.LinearLayout.LayoutParams menuLinerLayoutParames = new LinearLayout.LayoutParams(
                75, 75, 1);
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.header_item, null);
        ImageView images = (ImageView) view.findViewById(R.id.iv_avatar);
        menuLinerLayoutParames.setMargins(6, 6, 6, 6);

        // 设置id，方便后面删除
        view.setTag(glufineid);
        if (bitmap == null) {
            images.setImageResource(R.mipmap.default_avatar);
        } else {
            images.setImageBitmap(bitmap);
        }

        linearLayoutMenu.addView(view, menuLinerLayoutParames);
        if (total > 0) {
            if (ivSearch.getVisibility() == View.VISIBLE) {
                ivSearch.setVisibility(View.GONE);
            }
        }
        addList.add(glufineid.getCardName());
    }

    //删除选择的头像
    public void deleteImage(Contact contact) {
        View view = (View) linearLayoutMenu.findViewWithTag(contact);

        linearLayoutMenu.removeView(view);
        total--;
        addList.remove(contact.getRemark());
        if (total < 1) {
            if (ivSearch.getVisibility() == View.GONE) {
                ivSearch.setVisibility(View.VISIBLE);
            }
        }
    }*/


    static class ViewHolder {
        @BindView(R.id.tv_catalog)
        TextView tvCatalog;
        @BindView(R.id.item_underline)
        TextView itemUnderline;
        @BindView(R.id.iv_contact_avatar)
        ImageView avatar;
        @BindView(R.id.iv_contact_ischeck)
        ImageView ivContactIscheck;
        @BindView(R.id.tv_contact_name)
        TextView tvUserName;
        @BindView(R.id.tv_contact_real_name)
        TextView tvRealName;
        @BindView(R.id.checkbox_item)
        CheckBox checkBox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
