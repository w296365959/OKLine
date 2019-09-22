package com.vboss.okline.ui.contact.TransferAccounts;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.bean.Contact;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : yuan shaoyu
 * Email : yuer@okline.cn
 * Date : 2017/5/17 17:23
 * Desc :
 */

public class AccountBankAdapter extends BaseAdapter {
    private static final String TAG = "AccountBankAdapter";
    private List<Contact> mList = new ArrayList<>();
    private Context mContext;
    private ViewHolder holder;

    public AccountBankAdapter(Context context, List<Contact> lists) {
        this.mContext = context;
        if(lists.size() > 0){
            this.mList = lists;
        }
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void refresh(List<Contact> list) {
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
        //拿到集合中的联系人对象
        Contact contacts = mList.get(position);
        //联系人大写首字母
        String fistLetter = ContactsUtils.filledData(mList.get(position).getName());
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.account_bank_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int section =  getSectionForPosition(position);
        //排序,如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if (position == getPositionForSection(section)) {
            holder.select_bank_catalog.setVisibility(View.VISIBLE);

            holder.select_bank_catalog.setText(fistLetter);
        } else {
            holder.select_bank_catalog.setVisibility(View.GONE);

        }
        return convertView;
    }


    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {

        for (int i = 0, m = getCount(); i < m; i++) {
            char firstChar = getFirstLetter(mList.get(i)).matches("[A-Z]")? getFirstLetter(mList.get(i)).charAt(0) : '#';
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 获取联系人名称首字母
     * @param con
     * @return
     */
    private String getFirstLetter(Contact con){

        return ContactsUtils.getFirstUpperCaseLetter(con);
    }

    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        String value = getFirstLetter(mList.get(position));
        char a = '#';
        // return mList.get(position).getSortLetters().toUpperCase().substring(0,1).charAt(0);
        return value.matches("[A-Z]")? value.charAt(0) : a;
//        return ContactsUtils.getFirstUpperCaseLetter(mList.get(position)).charAt(0);
    }

    static class ViewHolder {
        @BindView(R.id.account_bank_name)
        TextView account_bank_name;

        @BindView(R.id.account_bank_img)
        SimpleDraweeView account_bank_img;

        @BindView(R.id.select_bank_catalog)
        TextView select_bank_catalog;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
