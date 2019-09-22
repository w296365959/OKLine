package com.okline.vboss.assistant.ui.opay;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.net.CardEntity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * OKLine(Hangzhou) Co.,ltd.
 * Author:Zheng Jun
 * Email:zhengjun@okline.cn
 * Date: 2016-6-16 11:28:11
 * Desc:
 */
public class CardListFragment extends Fragment {
    @BindView(R2.id.iv_return3)
    ImageView ivReturn3;
    @BindView(R2.id.lv_bankaccount)
    ListView lvBankaccount;
    private View view;
    private OPaySDKActivity activity;
    private BaseAdapter adapter;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_opaysdk2_assistant, container, false);
            unbinder = ButterKnife.bind(this, view);
            activity = (OPaySDKActivity) getActivity();
            ivReturn3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.viewPager.setCurrentItem(0, false);
                }
            });
            adapter = new BaseAdapter() {
                @Override
                public int getCount() {
                    return activity.cardEntities.size();
                }

                @Override
                public CardEntity getItem(int position) {
                    return activity.cardEntities.get(position);
                }

                @Override
                public long getItemId(int position) {
                    return position;
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(getContext(), R.layout.item_bankaccountlist_assistant, null);
                    }
                    CardEntity item = getItem(position);
                    SimpleDraweeView iv_bankcardicon = (SimpleDraweeView) convertView.findViewById(R.id.iv_bankcardicon);
                    TextView tv_bankcardid = (TextView) convertView.findViewById(R.id.tv_bankcardid);
                    TextView tv_opaysdk_bankcard_busitype = (TextView) convertView.findViewById(R.id.tv_opaysdk_bankcard_busitype);
                    tv_opaysdk_bankcard_busitype.setVisibility(item.isQuiclPass() ? View.VISIBLE : View.INVISIBLE);
                    TextView tv_opaysdk_bankcard_tailno = (TextView) convertView.findViewById(R.id.tv_opaysdk_bankcard_tailno);
                    String logoUrl = item.getIconUrl();
                    if (logoUrl != null) {
                        iv_bankcardicon.setImageURI(Uri.parse(logoUrl));
                    }
                    String substring = getString(R.string.text_card_tail_no) + " " + item.getCardNo().substring(item.getCardNo().length() - 4);
                    tv_opaysdk_bankcard_tailno.setText(substring);
                    String text = item.getCardName();
                    tv_bankcardid.setText(text);
                    if (item.equals(activity.selectedCard)) {
                        convertView.findViewById(R.id.iv_selected).setVisibility(View.VISIBLE);
                    } else {
                        convertView.findViewById(R.id.iv_selected).setVisibility(View.INVISIBLE);
                    }
                    return convertView;
                }
            };

            lvBankaccount.setAdapter(adapter);
            lvBankaccount.setEmptyView(View.inflate(getContext(), R.layout.view_empty_cardlist_assistant, null));
            lvBankaccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (CardEntity info : activity.cardEntities) {
                        if (info.equals(adapter.getItem(position))) {
                            activity.selectedCard = info;
                        }
                    }
                    adapter.notifyDataSetChanged();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            activity.viewPager.setCurrentItem(0, false);
                        }
                    }, 200);
                }
            });
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        adapter.notifyDataSetChanged();

    }

    private static final String TAG = "CardListFragment";
}
