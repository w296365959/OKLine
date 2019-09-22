package com.vboss.okline.ui.contact.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.ui.contact.bean.AccountItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/28 16:59
 * Desc : 账号信息的adapter(名片)
 */

public class AccountAdapter extends BaseAdapter{
    private static final String TAG = "AccountAdapter";
    private Context context;
    private List<AccountItem> cardEntities;
    private ViewHolder holder;
    //0是详细资料页面 帐号为黑色 1是添加联系人和我的名片界面 帐号为绿色
    private int type;

    public AccountAdapter(Context context, List<AccountItem> cardEntities, int type) {
        this.context = context;
        this.cardEntities = cardEntities;
        this.type = type;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void refresh(List<AccountItem> list) {
        if (list == null) {
            list = new ArrayList<>();
        }
        cardEntities = list;
        notifyDataSetChanged();
        Timber.tag(TAG).i("refresh");
    }

    @Override
    public int getCount() {
        return cardEntities.size();
    }

    @Override
    public Object getItem(int i) {
        return cardEntities.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        AccountItem item = cardEntities.get(i);


        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.account_information_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

        }
        holder.tvBankName.setText(item.getBank());
        holder.tvBankCount.setText(item.getAccount());
        if (0 == type){
            holder.tvBankCount.setTextColor(context.getResources().getColor(R.color.black));
        }

        return convertView;
    }


    class ViewHolder {
        @BindView(R.id.tv_bank_name)
        TextView tvBankName;
        @BindView(R.id.tv_bank_count)
        TextView tvBankCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
