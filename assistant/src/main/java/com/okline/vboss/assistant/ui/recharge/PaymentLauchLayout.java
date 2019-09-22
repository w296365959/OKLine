package com.okline.vboss.assistant.ui.recharge;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.net.CardEntity;

import java.util.ArrayList;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/5/4
 * Summary : 在这里描述Class的主要功能
 */

public class PaymentLauchLayout extends RelativeLayout {

    private final Context context;
    private AdapterView.OnItemClickListener listener;

    public void setOnItemClickListener(@Nullable AdapterView.OnItemClickListener listener) {
        this.listener = listener;
    }

    private final ListView lv_bankaccount;
    private final RelativeLayout rl_no_valid_card;
    private BaseAdapter lvAdapter;

    /**
     * 获取被选中的银行卡对象
     * @return 被选中的银行卡对象
     */
    public CardEntity getSelectedCard() {
        return selectedCard;
    }

    private CardEntity selectedCard;

    /**
     * 为银行卡列表设置数据
     * @param bankcards 通过异步操作请求到的银行卡（必须全部都是可用于闪付功能的银行卡，数目不能为空）
     */
    public void setBankCards(ArrayList<CardEntity> bankcards) {
        if (bankcards == null || bankcards.isEmpty()) {
            return;
        }
        rl_no_valid_card.setVisibility(GONE);
        lv_bankaccount.setVisibility(VISIBLE);
        this.bankCards = bankcards;
        selectedCard = bankCards.get(0);
        lvAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return bankCards.size();
            }

            @Override
            public CardEntity getItem(int position) {
                return bankCards.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(getContext(), R.layout.item_bank_card_list_assistant, null);
                }
                CardEntity item = getItem(position);
                SimpleDraweeView iv_bankcardicon = (SimpleDraweeView) convertView.findViewById(R.id.iv_bankcardicon);
                TextView tv_bankcardid = (TextView) convertView.findViewById(R.id.tv_bankcardid);
                TextView tv_opaysdk_bankcard_busitype = (TextView) convertView.findViewById(R.id.tv_opaysdk_bankcard_busitype);
                //"POS通卡"字样暂不显示
//                tv_opaysdk_bankcard_busitype.setVisibility(item.isQuiclPass()? View.VISIBLE: View.INVISIBLE);
                TextView tv_opaysdk_bankcard_tailno = (TextView) convertView.findViewById(R.id.tv_opaysdk_bankcard_tailno);
                TextView tv_opaysdk_bankcard_discount = (TextView) convertView.findViewById(R.id.tv_opaysdk_bankcard_discount);
                tv_opaysdk_bankcard_discount.setVisibility(GONE);
                String logoUrl = item.getImgUrl();
                if (logoUrl != null) {
                    Utils.showThumb(iv_bankcardicon,logoUrl,220,140, 5f);
                }
                String substring = item.getCardNo().substring(0,4)+"****"+item.getCardNo().substring(item.getCardNo().length() - 4);
                tv_opaysdk_bankcard_tailno.setText(substring);
                String text =  item.getCardName();
                tv_bankcardid.setText(text);
                if (item.equals(selectedCard)) {
                    convertView.findViewById(R.id.iv_selected).setVisibility(View.VISIBLE);
                }else{
                    convertView.findViewById(R.id.iv_selected).setVisibility(View.INVISIBLE);
                }
                return convertView;
            }
        };
        lv_bankaccount.setAdapter(lvAdapter);
        lv_bankaccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (CardEntity info : bankCards) {
                    if (info.equals(lvAdapter.getItem(position))) {
                        selectedCard = info;
                        Utils.showLog(TAG,"选取卡片时候的绑定号："+ selectedCard.getBindId());
                    }
                }
                lvAdapter.notifyDataSetChanged();
                listener.onItemClick(parent,view,position,id);
            }
        });

    }

    private static final String TAG = "PaymentLauchLayout";

    private ArrayList<CardEntity> bankCards;

    public PaymentLauchLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        View view = View.inflate(context, R.layout.view_payment_lauch_layout_assistant, null);
        lv_bankaccount = (ListView) view.findViewById(R.id.lv_bankaccount);
        rl_no_valid_card = (RelativeLayout) view.findViewById(R.id.rl_no_valid_card);
        addView(view);
    }

    /**
     * 适配银行卡列表的最大高度，在onViewCreated()方法中调用
     */
    public void adjustSize(){
        if (bankCards == null || bankCards.isEmpty()) {
            return;
        }
        int count1 = lv_bankaccount.getAdapter().getCount();
        View view2 = lv_bankaccount.getAdapter().getView(0, null, lv_bankaccount);
        view2.measure(0,0);
        int measuredHeight1 = view2.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParams = lv_bankaccount.getLayoutParams();
        if (count1 == 1) {
            layoutParams.height = measuredHeight1;
        } else {
            layoutParams.height = measuredHeight1 * 2 +lv_bankaccount.getDividerHeight();
        }
        lv_bankaccount.setLayoutParams(layoutParams);
    }
}
