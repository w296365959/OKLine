package com.vboss.okline.ui.contact.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.utils.PinyinUtils;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.baidu.location.d.a.i;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/6 10:27
 * Desc : 创建群组 联系人主界面 搜索界面adapter
 */

public class ContactListAdapter extends BaseAdapter {
    private static final String TAG = "ContactListAdapter";
    boolean firstGroup = true;
    private int total = 0;
    private boolean firstStar = true;
    private int contentType;  //1 是创建群组界面  2 是联系人主界面 3 是搜索界面
    private List<ContactItem> mList = new ArrayList<>();
    private Context mContext;

    private List<ContactItem> starList;
    private List<ContactItem> commonList;
    private List<ContactItem> groupList;

    public ContactListAdapter(List<ContactItem> mList, Context mContext, int contentType) {
        if (null != mList && mList.size() > 0) {
            this.mList = mList;
        }
        this.mContext = mContext;
        this.contentType = contentType;
        getCommonList();
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void refresh(List<ContactItem> list) {
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
        Log.i("ContactAdapter", "getItem: " + mList.size());
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
//        int viewType = getItemViewType(position);
        ContactItem item = mList.get(position);

        String name = item.getRemark();
        String avatar = item.getAvatar();
        String realName = item.getRealName();
        int relationState = item.getRelationState();
        int itemType = item.getItemType();
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        firstStar = true;
        String firstLetter = item.getSortLetters();
        int section = firstLetter.charAt(0);
        if (1 == contentType) {//创建群组选择联系人
            holder.checkboxItem.setVisibility(View.VISIBLE);
            holder.tvGroupName.setVisibility(View.GONE);
            //排序,如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
            if (position == getPositionForSection(section)) {
                //TODO 此处暂时隐藏
                holder.tvCatalog.setVisibility(View.GONE);
                //下划线隐藏
                holder.itemUnderline.setVisibility(View.GONE);

                holder.tvCatalog.setText(firstLetter);
            } else {
                holder.itemUnderline.setVisibility(View.VISIBLE);
                holder.tvCatalog.setVisibility(View.GONE);
            }
        } else if (2 == contentType) {
            holder.checkboxItem.setVisibility(View.GONE);
            switch (itemType) {
                case 1:
                    holder.tvContactName.setVisibility(View.VISIBLE);
                    holder.tvGroupName.setVisibility(View.GONE);
                    if (/*firstStar*/position == 0) {
                        holder.tvCatalog.setVisibility(View.VISIBLE);
                        holder.itemUnderline.setVisibility(View.GONE);
                        Drawable drawable = ActivityCompat.getDrawable(mContext, R.drawable.star);
                        holder.tvCatalog.setText("我关心的人");
                        holder.tvCatalog.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                        holder.tvCatalog.setCompoundDrawablePadding(10);
                        firstStar = false;
                    } else {
                        holder.itemUnderline.setVisibility(View.VISIBLE);
                        holder.tvCatalog.setVisibility(View.GONE);
                    }

                    if (3 == relationState) {
                        holder.tvGroupName.setVisibility(View.GONE);
                        holder.tvContactName.setVisibility(View.VISIBLE);
                        holder.tvContactRealName.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvGroupName.setVisibility(View.VISIBLE);
                        holder.tvContactName.setVisibility(View.INVISIBLE);
                        holder.tvContactRealName.setVisibility(View.INVISIBLE);
                        holder.tvGroupName.setText(name);
                    }

                    break;
                case 2:
                    if (position == starList.size()) {
                        holder.tvCatalog.setVisibility(View.VISIBLE);
                        holder.tvCatalog.setText("群");
                        holder.itemUnderline.setVisibility(View.GONE);
                        holder.tvCatalog.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    } else {
                        holder.itemUnderline.setVisibility(View.VISIBLE);
                        holder.tvCatalog.setVisibility(View.GONE);
                    }

                    holder.tvGroupName.setVisibility(View.VISIBLE);
                    holder.tvContactName.setVisibility(View.INVISIBLE);
                    holder.ivContactIscheck.setVisibility(View.GONE);
                    holder.tvContactRealName.setVisibility(View.GONE);
                    holder.tvGroupName.setText(item.getRemark());

                    break;
                case 3:
                    //排序,如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//                    if (position>starList.size()){
                    holder.tvContactName.setVisibility(View.VISIBLE);
                        Timber.tag(TAG).i("position:"+position+" section:"+getPositionForSection(section));
                    if (position - starList.size() - groupList.size() == getPositionForSection2(section,
                            relationState)) {
                        holder.tvCatalog.setVisibility(View.VISIBLE);
                        //下划线隐藏
                        holder.itemUnderline.setVisibility(View.GONE);

                        holder.tvCatalog.setText(firstLetter);
                        holder.tvCatalog.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    } else {
                        holder.itemUnderline.setVisibility(View.VISIBLE);
                        holder.tvCatalog.setVisibility(View.GONE);
                    }

                    if (StringUtils.isNullString(realName)) {
                        holder.tvGroupName.setVisibility(View.VISIBLE);
                        holder.tvContactName.setVisibility(View.INVISIBLE);
                        holder.tvGroupName.setText(name);
                    } else {
                        holder.tvGroupName.setVisibility(View.GONE);
                    }

//                    }


                    break;
                default:
                    break;
            }
        } else {
            holder.checkboxItem.setVisibility(View.GONE);
            holder.tvCatalog.setVisibility(View.GONE);
            if (StringUtils.isNullString(realName)) {
                holder.tvGroupName.setVisibility(View.VISIBLE);
                holder.tvContactName.setVisibility(View.INVISIBLE);
                holder.tvGroupName.setText(name);
            } else {
                holder.tvGroupName.setVisibility(View.GONE);
            }

        }
        if (relationState == 2) {
            holder.ivContactIscheck.setImageResource(R.mipmap.main_not_trusted);
            holder.ivContactIscheck.setVisibility(View.VISIBLE);
        } else if (relationState == 3) {
            holder.ivContactIscheck.setImageResource(R.mipmap.main_trusted);
            holder.ivContactIscheck.setVisibility(View.VISIBLE);
        } else {
            holder.ivContactIscheck.setVisibility(View.GONE);
        }
        holder.tvContactName.setText(name);

        if (relationState == 3) {
            holder.tvContactRealName.setVisibility(View.VISIBLE);
//            holder.tvContactRealName.setText(String.format(mContext.getString(R.string.text_real_name), realName));
            holder.tvContactRealName.setText(item.getPhoneNum());
            holder.tvContactName.setText(realName);
            holder.tvContactName.setVisibility(View.VISIBLE);
            holder.tvGroupName.setVisibility(View.INVISIBLE);
            if (!TextUtils.isEmpty(avatar)) {
                //lzb test for shenzhen
                holder.ivContactAvatar.setImageURI(Uri.parse(avatar));
//                holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
            } else {
//            holder.ivContactAvatar.setImageDrawable(mContext.getResources().getDrawable(R.mipmap.default_avatar));
                holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
            }
        } else {
            holder.tvContactRealName.setVisibility(View.VISIBLE);
            holder.tvContactRealName.setText(item.getPhoneNum());
            holder.tvGroupName.setVisibility(View.INVISIBLE);
            holder.tvContactName.setVisibility(View.VISIBLE);
            holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
        }


        if (item.getOlGroupID() > 0) {
            holder.ivContactAvatar.setImageResource(R.drawable.ease_groups_icon);
        }
        if (position == mList.size()-1) {
            Timber.tag(TAG).i("LastContact: position: %s  ListSize: %s  contact: %s",position,mList.size(),mList.get(position).toString());
            holder.bottomLine.setVisibility(View.VISIBLE);
        }
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

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection2(int section, int relationState) {

        for (int i = 0, m = commonList.size(); i < m; i++) {
            char firstChar;
            if (relationState == 3) {
                firstChar = ContactsUtils.filledData(commonList.get(i).getSortLetters()).charAt(0);
//                Timber.tag(TAG).i("111relationState %s , firstChar %s",relationState , firstChar);
//                Timber.tag(TAG).i("111 %s",commonList.get(i).toString());
            } else {
                firstChar = ContactsUtils.filledData(commonList.get(i).getRemark()).charAt(0);
//                Timber.tag(TAG).i("000relationState %s , firstChar %s , i %s",relationState , firstChar , i);
//                Timber.tag(TAG).i("000 %s " , commonList.get(i).toString());
            }
//            Timber.tag(TAG).i("first char %s section %s \n", firstChar, section);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    private void getCommonList() {
        starList = new ArrayList<>();
        groupList = new ArrayList<>();
        commonList = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getItemType() == 1) {
                starList.add(mList.get(i));
            } else if (mList.get(i).getItemType() == 2) {
                groupList.add(mList.get(i));
            } else {
                commonList.add(mList.get(i));
            }
        }
        /*for (ContactItem item : commonList) {
            Timber.tag(TAG).i("getCommonList:" + item.toString());
        }*/

    }


    static class ViewHolder {
        @BindView(R.id.tv_catalog)
        TextView tvCatalog;
        @BindView(R.id.item_underline)
        View itemUnderline;
        @BindView(R.id.checkbox_item)
        ImageView checkboxItem;
        @BindView(R.id.iv_contact_avatar)
        SimpleDraweeView ivContactAvatar;
        @BindView(R.id.iv_contact_ischeck)
        ImageView ivContactIscheck;
        @BindView(R.id.tv_contact_name)
        TextView tvContactName;
        @BindView(R.id.tv_contact_real_name)
        TextView tvContactRealName;
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.item_bottom_line)
        View bottomLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}