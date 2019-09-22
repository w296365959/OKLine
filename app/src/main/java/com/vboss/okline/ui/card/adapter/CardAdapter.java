package com.vboss.okline.ui.card.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.jpush.JPushActivity;
import com.vboss.okline.jpush.JPushEntity;
import com.vboss.okline.jpush.JPushHelper;
import com.vboss.okline.ui.card.main.CardPresenter;
import com.vboss.okline.ui.card.notice.CardNoticeFragment;
import com.vboss.okline.ui.card.widget.SliderView;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.FrescoUtil;
import com.vboss.okline.view.widget.CommonDialog;
import com.vboss.okline.view.widget.shadow.ListViewShadowViewHelper;
import com.vboss.okline.view.widget.shadow.ShadowHelper;

import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/30 10:54 <br/>
 * Summary  : card list Adapter
 */

public class CardAdapter extends BaseAdapter {
    private static final String TAG = CardAdapter.class.getSimpleName();
    private List<CardEntity> cardModelList;
    private LayoutInflater inflater;
    private Context mContext;
    private CardPresenter presenter;
    private boolean isVisible = false;

    public CardAdapter(Context context, List<CardEntity> cardModels) {
        inflater = LayoutInflater.from(context);
        this.cardModelList = cardModels;
        this.mContext = context;
    }

    /**
     * Add by wangshuai 2017-04-20
     * the Method controller card's number visible
     *
     * @param visible boolean
     */
    public void setCardNoVisible(boolean visible) {
        isVisible = visible;
    }


    @Override
    public int getCount() {
        return cardModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        SliderView sliderView = (SliderView) convertView;
        if (convertView == null) {
            View itemView = inflater.inflate(R.layout.row_card_silder, parent, false);
            sliderView = new SliderView(mContext);
            sliderView.setContentView(itemView);
            holder = new ViewHolder(sliderView);
            sliderView.setTag(holder);
        } else {
            holder = (ViewHolder) sliderView.getTag();
        }
        sliderView.shrink();
        if (position < getCount()) {
            initListener(holder, position);
            CardEntity cardEntity = cardModelList.get(position);
            if (cardEntity != null) {
                //modify by wangshuai 2017-05-25 identity layout visible
                if (cardEntity.cardMainType() == CardType.CREDENTIALS) {
                    holder.layout_card_identity.setVisibility(View.VISIBLE);
                    holder.layout_card_normal.setVisibility(View.GONE);
                    sliderView.setSliderEnabled(false);
                } else {
                    holder.layout_card_identity.setVisibility(View.GONE);
                    holder.layout_card_normal.setVisibility(View.VISIBLE);
                    sliderView.setSliderEnabled(true);
                }
                String url = cardEntity.imgUrl();
                if (!TextUtils.isEmpty(url)) {
//                    holder.simpleDraweeView.setImageURI(Uri.parse(url));
                    holder.simpleDraweeView.setController(FrescoUtil.loadImage(url,
                            mContext.getResources().getDimensionPixelSize(R.dimen.row_card_width) / 2,
                            mContext.getResources().getDimensionPixelSize(R.dimen.row_card_height) / 2));
                } else {
                    holder.simpleDraweeView.setController(FrescoUtil.getDefaultImage(mContext, R.mipmap.image_card_default));
                }
                if (CardType.BANK_CARD == cardEntity.cardMainType()) {
                    holder.layout_default.setVisibility(View.VISIBLE);
                } else {
                    holder.layout_default.setVisibility(View.GONE);
                }
                String temp = cardEntity.cardNo();
                if (!TextUtils.isEmpty(temp)) {
                    int length = temp.length();
                    String cardNo = (length > 4 ? temp.substring(length - 4, length) : temp);
                    String no = String.format(mContext.getResources().getString(R.string.card_cardNo), cardNo);
                    SpannableString ss = new SpannableString(no);
                    ss.setSpan(new StyleSpan(Typeface.BOLD), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    holder.tv_card_no.setText(ss);
                } else {
                    holder.tv_card_no.setText(null);
                }

                //identity content show
                holder.tv_name.setText(cardEntity.cardName());
                holder.tv_ID.setText(cardEntity.cardNo());
            }

        }
        if (isVisible) {
            holder.tv_card_no.setVisibility(View.VISIBLE);
        } else {
            holder.tv_card_no.setVisibility(View.GONE);
        }

        if (position == cardModelList.size() - 1) {
            holder.row_bottom_line.setVisibility(View.VISIBLE);
        } else {
            holder.row_bottom_line.setVisibility(View.GONE);
        }
        ListViewShadowViewHelper.bindShadowHelper(
                ShadowHelper.getInstance(mContext).getShadowProperty(),
                holder.simpleDraweeView,
                ShadowHelper.getInstance(mContext).getShadowRadius(),
                ShadowHelper.getInstance(mContext).getShadowRadius()
        );

        ListViewShadowViewHelper.bindShadowHelper(
                ShadowHelper.getInstance(mContext).getShadowProperty(),
                holder.simple_id,
                ShadowHelper.getInstance(mContext).getShadowRadius(2.5f),
                ShadowHelper.getInstance(mContext).getShadowRadius(2.5f)
        );

        return sliderView;
    }

    /**
     * init view click listener
     *
     * @param holder   ViewHolder
     * @param position position
     */
    private void initListener(ViewHolder holder, final int position) {

        holder.layout_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefault(position);
            }
        });
        holder.layout_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CardEntity cardEntity = null;
                if (position < cardModelList.size()) {
                    cardEntity = cardModelList.get(position);
                }
                Intent intent = new Intent(mContext, JPushActivity.class);
                JPushEntity entity = new JPushEntity();
                intent.putExtra(CardNoticeFragment.KEY_MODE, CardNoticeFragment.MODE_BANK_PAY);
                if (cardEntity != null) {
                    entity.setCardId(cardEntity.cardId());
                    entity.setErrorCode(-1);
                    entity.setCardMainType(cardEntity.cardMainType());
                    intent.putExtra(JPushHelper.KEY_JPUSH_DATA, entity);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    /**
     * set default card
     *
     * @param position index
     */
    private void setDefault(final int position) {
        CommonDialog dialog = new CommonDialog(mContext);
        dialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
            @Override
            public void cancel(View view, CommonDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void ensure(View view, CommonDialog dialog) {
                dialog.dismiss();
                if (presenter == null) {
                    Log.i(TAG, "CardPresenter is null ");
                } else {
                    //设置默认卡
                    CardEntity cardEntity = null;
                    if (position < cardModelList.size()) {
                        cardEntity = cardModelList.get(position);
                    }
                    if (cardEntity != null) {
                        if (cardEntity.isDefault() == 1) {
                            Utils.customToast(mContext, mContext.getResources().getString(R.string.card_is_default),
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        presenter.setCardDefault(cardEntity.cardMainType(), cardEntity.cardId());
                    }
                }
            }
        });
        dialog.show();
        Log.i(TAG, "dialog show");
    }

    /**
     * incoming CardPresenter object
     *
     * @param cardPresenter CardPresenter
     */
    public void setCardPresenter(CardPresenter cardPresenter) {
        this.presenter = cardPresenter;
    }


    private static class ViewHolder {
        SimpleDraweeView simpleDraweeView;
        LinearLayout layout_pay;
        LinearLayout layout_default;
        View row_bottom_line;
        SliderView mSliderView;
        TextView tv_card_no;

        //modify by wangshuai 2017-05-25 find identity view
        LinearLayout layout_card_normal;
        LinearLayout layout_card_identity;
        TextView tv_name;
        TextView tv_ID;
        SimpleDraweeView simple_id;

        public ViewHolder(SliderView sliderView) {
            this.mSliderView = sliderView;
            simpleDraweeView = (SimpleDraweeView) sliderView.findViewById(R.id.simpleDraweeView);
            layout_default = (LinearLayout) sliderView.findViewById(R.id.layout_default);
            layout_pay = (LinearLayout) sliderView.findViewById(R.id.layout_pay);
            row_bottom_line = sliderView.findViewById(R.id.row_bottom_line);
            tv_card_no = (TextView) sliderView.findViewById(R.id.tv_card_no);
            //modify by wangshuai 2017-05-25 find identity view
            layout_card_identity = (LinearLayout) sliderView.findViewById(R.id.layout_card_identity);
            layout_card_normal = (LinearLayout) sliderView.findViewById(R.id.layout_card);
            tv_name = (TextView) sliderView.findViewById(R.id.tv_identity_name);
            tv_ID = (TextView) sliderView.findViewById(R.id.tv_identity_id);
            simple_id = (SimpleDraweeView) sliderView.findViewById(R.id.simple_id);
        }
    }
}
