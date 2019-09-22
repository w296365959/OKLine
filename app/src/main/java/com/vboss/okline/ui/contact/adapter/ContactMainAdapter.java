package com.vboss.okline.ui.contact.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
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
import com.vboss.okline.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;


/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/12 17:23
 * Desc :
 */

public class ContactMainAdapter extends BaseAdapter {
    private static final String TAG = "ContactMainAdapter";
    private List<ContactItem> mList = new ArrayList<>();
    private Context mContext;
    private int starListSize;
    private int groupListSize;
    private List<ContactItem> commonList = new ArrayList<>();


    public ContactMainAdapter(List<ContactItem> list, Context context, int starSize, int groupSize) {
        if (null != list && list.size() > 0) {
            this.mList = list;
        }
        mContext = context;
        starListSize = starSize;
        groupListSize = groupSize;
        Timber.tag(TAG).i("starSize : %s, groupSize : %s",starSize,groupSize);
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
        Timber.tag(TAG).i("refresh");
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ContactItem item = mList.get(position);

        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.contact_item, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        if (position<starListSize){
            holder.tvContactName.setVisibility(View.VISIBLE);
            holder.tvGroupName.setVisibility(View.GONE);
            if (position == 0) {
                holder.tvCatalog.setVisibility(View.VISIBLE);
                holder.tvCatalog.setText("");
                holder.itemUnderline.setVisibility(View.GONE);
                Drawable drawable = ActivityCompat.getDrawable(mContext, R.drawable.star);
                holder.tvCatalog.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
                holder.tvCatalog.setCompoundDrawablePadding(10);
            } else {
                holder.itemUnderline.setVisibility(View.VISIBLE);
                holder.tvCatalog.setVisibility(View.GONE);
            }

            if (3 == item.getRelationState()) {
                holder.tvContactRealName.setVisibility(View.VISIBLE);
                holder.ivContactIscheck.setVisibility(View.VISIBLE);
                holder.ivContactIscheck.setImageResource(R.mipmap.main_trusted);
                holder.tvContactName.setText(ContactsUtils.realNameNremarkName(item.getRealName(),item.getNickName()));
                if (StringUtils.isNullString(item.getAvatar())) {
                    holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
                } else {
                    holder.ivContactAvatar.setImageURI(Uri.parse(item.getAvatar()));
                }
                holder.tvContactRealName.setText(item.getPhoneNum());
            } else if (2 == item.getRelationState()) {
                holder.tvContactName.setText(ContactsUtils.realNameNremarkName(item.getRemark(),item.getNickName()));
                holder.ivContactIscheck.setVisibility(View.VISIBLE);
                holder.ivContactIscheck.setImageResource(R.mipmap.main_not_trusted);
                holder.tvContactRealName.setVisibility(View.VISIBLE);
                holder.ivContactAvatar.setImageURI(Uri.parse(item.getAvatar()));
                holder.tvContactRealName.setText(item.getPhoneNum());
            } else {
                holder.tvContactName.setText(ContactsUtils.realNameNremarkName(item.getRemark(),item.getNickName()));
                holder.tvContactRealName.setVisibility(View.VISIBLE);
                holder.tvContactRealName.setText(item.getPhoneNum());
                holder.ivContactAvatar.setImageURI(Uri.parse(item.getAvatar()));
                holder.ivContactIscheck.setVisibility(View.GONE);
            }
        }else if (position >= starListSize && position < starListSize+groupListSize){
            if (position == starListSize) {
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
        }else{
            if (position == mList.size() - 1) {
                holder.itemBottomLine.setVisibility(View.VISIBLE);
            }

            if (item.getRelationState() == 2) {
                //非信任好友groupName显示 realName和remarkName隐藏 显示灰标
                holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
                holder.ivContactIscheck.setImageResource(R.mipmap.main_not_trusted);
                holder.ivContactIscheck.setVisibility(View.VISIBLE);
                holder.tvGroupName.setVisibility(View.INVISIBLE);
                holder.tvContactName.setText(ContactsUtils.realNameNremarkName(item.getRemark(),item.getNickName()));
                holder.tvContactRealName.setText(item.getPhoneNum());
            } else if (item.getRelationState() == 3) {
                //信任好友groupName隐藏 realName和remarkName显示 显示绿标
                holder.ivContactIscheck.setImageResource(R.mipmap.main_trusted);
                holder.ivContactIscheck.setVisibility(View.VISIBLE);
                holder.tvGroupName.setVisibility(View.GONE);
                if (StringUtils.isNullString(item.getAvatar())) {
                    holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
                } else {
                    holder.ivContactAvatar.setImageURI(Uri.parse(item.getAvatar()));
                }

                holder.tvContactName.setText(ContactsUtils.realNameNremarkName(item.getRealName(),item.getNickName()));

                holder.tvContactRealName.setText(item.getPhoneNum());
            } else {
                //非VIP 显示groupName 其余隐藏
                holder.ivContactAvatar.setImageResource(R.mipmap.default_avatar);
                holder.ivContactIscheck.setVisibility(View.GONE);
                holder.tvGroupName.setVisibility(View.INVISIBLE);
                holder.tvContactName.setText(ContactsUtils.realNameNremarkName(item.getRemark(),item.getNickName()));
                holder.tvContactRealName.setText(item.getPhoneNum());
            }
            holder.tvContactName.setVisibility(View.VISIBLE);
            holder.tvContactRealName.setVisibility(View.VISIBLE);
            //add by linzhangbin 数组越界bug 2017/6/15
            if (StringUtils.isNullString(item.getSortLetters())){
                item.setSortLetters("#");
            }
            //add by linzhangbin 数组越界bug

            //modify by linzhangbin
            if (position - starListSize - groupListSize == getPositionForSection(
                    item.getSortLetters().charAt(0),item.getRelationState())) {
                holder.tvCatalog.setVisibility(View.VISIBLE);
                //下划线隐藏
                holder.itemUnderline.setVisibility(View.GONE);
                holder.tvCatalog.setText(item.getSortLetters());
                holder.tvCatalog.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
            } else {
                holder.itemUnderline.setVisibility(View.VISIBLE);
                holder.tvCatalog.setVisibility(View.GONE);
            }
        }



        holder.checkboxItem.setVisibility(View.GONE);
        if (item.getOlGroupID() > 0) {
            holder.ivContactAvatar.setImageResource(R.drawable.ease_groups_icon);
        }



        return view;
    }

    private void getCommonList() {

        int start = starListSize + groupListSize;
        if (!mList.isEmpty()) {
            commonList = mList.subList(start,mList.size());
        }
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section, int relationState) {

        for (int i = 0, m = commonList.size(); i < m; i++) {
            char firstChar;
            firstChar = commonList.get(i).getSortLetters().charAt(0);

            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    public int getPositionForSection(int section) {

        for (int i = 0, m = getCount(); i < m; i++) {
//            char firstChar = getFirstLetter(mList.get(i)).matches("[A-Z]") ? getFirstLetter(mList.get(i)).charAt(0) : '#';
            char firstChar = mList.get(i).getSortLetters().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
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
        @BindView(R.id.tv_group_name)
        TextView tvGroupName;
        @BindView(R.id.tv_contact_real_name)
        TextView tvContactRealName;
        @BindView(R.id.item_bottom_line)
        View itemBottomLine;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
