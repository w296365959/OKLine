package com.okline.vboss.assistant.adapter;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cosw.sdkblecard.DeviceInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.base.Config;
import com.okline.vboss.assistant.base.DefaultSubscribe;
import com.okline.vboss.assistant.net.CardEntity;
import com.okline.vboss.assistant.net.CardType;
import com.okline.vboss.assistant.net.OLApiService;
import com.okline.vboss.assistant.ui.MainActivity;
import com.okline.vboss.assistant.ui.notice.CardHelper;
import com.okline.vboss.assistant.ui.notice.CardNoticeActivity;
import com.okline.vboss.assistant.ui.opencard.UploadCardActivity;
import com.okline.vboss.assistant.ui.recharge.CardRechargeActivity;
import com.okline.vboss.assistant.ui.recharge.Utils;
import com.okline.vboss.assistant.utils.CustomDialog;
import com.okline.vboss.assistant.utils.DensityUtil;
import com.okline.vboss.assistant.utils.StringUtils;
import com.okline.vboss.assistant.widget.shadow.ListViewShadowViewHelper;
import com.okline.vboss.assistant.widget.shadow.ShadowProperty;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/8 11:54 <br/>
 * Summary  : card adapter
 */

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ItemViewHolder> {
    private static final String TAG = CardAdapter.class.getSimpleName();
    private LayoutInflater inflater;
    private List<CardEntity> cardEntities;
    //quick pass bank card
    //Added by wangshuai 2017-06-15 activate card list
    private Context mContext;
    private int preIndex;   //
    private MainActivity act;
    private boolean isCheckingOCard;
    private int manualClickCount;

    public void setPreIndex(int preIndex) {
        this.preIndex = preIndex;
    }

    public CardAdapter(Context context, List<CardEntity> data) {
        inflater = LayoutInflater.from(context);
        this.cardEntities = data;
        this.mContext = context;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(inflater.inflate(R.layout.row_card_assistant, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        CardEntity cardEntity = cardEntities.get(position);
        if (!TextUtils.isEmpty(cardEntity.getImgUrl())) {
            holder.simple.setImageURI(Uri.parse(cardEntity.getImgUrl()));
        }
        holder.tv_card_name.setText(cardEntity.getCardName());
//        Timber.tag(TAG).i("position %s isDownload %s", position, cardEntity.isDownload());
        //modify by wangshuai 2017-06-14 NullPointerException
//        Timber.tag(TAG).i(" quick pass bank card size %s", bankCardEntities == null ? 0 : bankCardEntities.size());

        //show card image shadow
        ListViewShadowViewHelper.bindShadowHelper(
                new ShadowProperty().setShadowColor(ActivityCompat.getColor(mContext, R.color.color_shadow))
                        .setShadowDy(DensityUtil.dip2px(mContext, 0.5f)).setShadowRadius(DensityUtil.dip2px(mContext, 2.9f)),
                holder.simple,
                DensityUtil.dip2px(mContext, 3f),
                DensityUtil.dip2px(mContext, 3f)
        );

        if (position < preIndex) {
            //下载icon
            if (position == 0) {
                holder.layout_card_type.setVisibility(View.VISIBLE);
                holder.tv_card_type.setText(mContext.getResources().getString(R.string.card_down));
                Drawable left = ActivityCompat.getDrawable(mContext, R.mipmap.ic_card_down);
                left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                holder.tv_card_type.setCompoundDrawables(left, null, null, null);
            } else {
                holder.layout_card_type.setVisibility(View.GONE);
            }
            //show downloaded icon
            if (cardEntity.isDownload()) {
                holder.iv_downloaded.setVisibility(View.VISIBLE);
            } else {
                holder.iv_downloaded.setVisibility(View.INVISIBLE);
            }
            //last divider hide
            if (position == preIndex - 1) {
                holder.v_card_line.setVisibility(View.GONE);
            } else {
                holder.v_card_line.setVisibility(View.VISIBLE);
            }
            holder.tv_card_issuer.setText(cardEntity.getMerName());
            holder.layout_card_line.setVisibility(View.GONE);
            holder.tv_card_recharge.setVisibility(View.GONE);
            holder.tv_card_percentage.setVisibility(View.GONE);
            holder.simple.setOnClickListener(new OpenCardClickListener(cardEntity));
        } else {
            holder.iv_downloaded.setVisibility(View.GONE);
            //充值icon
            if (position == preIndex) {
                holder.layout_card_line.setVisibility(View.VISIBLE);
                holder.layout_card_type.setVisibility(View.VISIBLE);
                holder.tv_card_type.setText(mContext.getResources().getString(R.string.card_recharge));
                Drawable left = ActivityCompat.getDrawable(mContext, R.mipmap.ic_card_recharge);
                left.setBounds(0, 0, left.getMinimumWidth(), left.getMinimumHeight());
                holder.tv_card_type.setCompoundDrawables(left, null, null, null);
            } else {
                holder.layout_card_type.setVisibility(View.GONE);
                holder.layout_card_line.setVisibility(View.GONE);
            }
            //last divider hide
            if (position == cardEntities.size() - 1) {
                holder.v_card_line.setVisibility(View.GONE);
            } else {
                holder.v_card_line.setVisibility(View.VISIBLE);
            }
            //show recharge button
            if (CardType.COMMON_CARD == cardEntity.getCardMainType() || CardType.VIP_CARD == cardEntity.getCardMainType()) {
                holder.tv_card_recharge.setVisibility(View.VISIBLE);
                holder.tv_card_issuer.setVisibility(View.VISIBLE);
            } else {
                holder.tv_card_recharge.setVisibility(View.GONE);
                holder.tv_card_issuer.setVisibility(View.GONE);
            }
            //show percentage textView
            if (cardEntities.size() - preIndex > 1) {
                holder.tv_card_percentage.setVisibility(View.VISIBLE);
            } else {
                holder.tv_card_percentage.setVisibility(View.GONE);
            }
            StringBuilder builder = new StringBuilder();
            builder.append(mContext.getResources().getString(R.string.rmb)).append(mContext.getResources().getString(R.string.quarter_empty))
                    .append(StringUtils.formatMoney(cardEntity.getBalance()));
            holder.tv_card_issuer.setText(String.format(mContext.getResources().getString(R.string.card_balance), builder));
            holder.tv_card_percentage.setText(String.format(mContext.getResources().getString(R.string.card_percentage),
                    position - preIndex + 1, cardEntities.size() - preIndex));
            holder.simple.setOnClickListener(null);
            holder.tv_card_recharge.setOnClickListener(new CardRechargeClickListener(cardEntity));
        }
    }

    @Override
    public int getItemCount() {
        return cardEntities.size();
    }

    /**
     * open card click listener
     */
    private class OpenCardClickListener implements View.OnClickListener {
        private CardEntity entity;

        OpenCardClickListener(CardEntity cardEntity) {
            this.entity = cardEntity;
        }

        @Override
        public void onClick(View v) {
            openCard(entity);
//            test(entity);
        }
    }

    /**
     * Added by wangshuai 2017-06-13 test CardNoticeActivity
     *
     * @param entity CardEntity
     */
    private void test(CardEntity entity) {
        entity.setCardNo("3100127895461258");
        Intent intent = new Intent(mContext, CardNoticeActivity.class);
        intent.putExtra(CardHelper.KEY_CARD, entity);
        intent.putExtra(CardHelper.KEY_OPERATE, CardHelper.CARD_DOWNLOAD_SUCCESS);
        mContext.startActivity(intent);

    }

    /**
     * card recharge click listener
     */
    private class CardRechargeClickListener implements View.OnClickListener {
        private CardEntity entity;

        CardRechargeClickListener(CardEntity cardEntity) {
            this.entity = cardEntity;
        }

        @Override
        public void onClick(View v) {
            manualClickCount = 0;
            Utils.showLog(TAG,"isCheckingOCard = " + isCheckingOCard);
            if (!isCheckingOCard) {
                isCheckingOCard = true;
                Timber.tag(TAG).i("entity %s", entity.toString());
                if (MainActivity.ocardState == 1) {
                    gotoRecharge();
                } else {
                    OLApiService.getInstance().requestOCardConnection(mContext,Config.ADDRESS,5*1000)
                            .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new DefaultSubscribe<DeviceInfo>(TAG){
                                @Override
                                public void onError(Throwable e) {
                                    super.onError(e);
                                    Utils.customToast(mContext,"该业务需要蓝牙功能，请检查欧卡与手机蓝牙连接是否正常", Toast.LENGTH_LONG).show();
                                    isCheckingOCard = false;
                                }

                                @Override
                                public void onNext(DeviceInfo deviceInfo) {
                                    super.onNext(deviceInfo);
                                    if (deviceInfo != null) {
                                        Utils.showLog(TAG,"获取DeviceInfo："+deviceInfo);
                                        gotoRecharge();
                                    } else {
                                        onError(new Exception("onNext返回的DeviceInfo实例为null"));
                                    }
                                }
                            });
                }
            }
        }

        private void gotoRecharge() {
            if (0 == manualClickCount++) {
                Intent intent = new Intent(mContext, CardRechargeActivity.class);
                intent.putExtra(CardRechargeActivity.EXTRA_CARD_ENTITY, entity);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                mContext.startActivity(intent);
                isCheckingOCard = false;
            }
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R2.id.layout_card_line)
        LinearLayout layout_card_line;
        @BindView(R2.id.simple)
        SimpleDraweeView simple;
        @BindView(R2.id.tv_card_name)
        TextView tv_card_name;
        @BindView(R2.id.tv_card_issuer)
        TextView tv_card_issuer;
        @BindView(R2.id.tv_card_recharge)
        TextView tv_card_recharge;
        @BindView(R2.id.tv_card_percentage)
        TextView tv_card_percentage;
        @BindView(R2.id.tv_card_type)
        TextView tv_card_type;
        @BindView(R2.id.v_card_line)
        View v_card_line;
        @BindView(R2.id.layout_card_type)
        LinearLayout layout_card_type;
        @BindView(R2.id.iv_downloaded)
        ImageView iv_downloaded;

        ItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void openCard(final CardEntity entity) {
        //add by yuanshaoyu :没有绑定欧卡不能开卡
        if (TextUtils.isEmpty(Config.ADDRESS)) {
            ToastUtil(mContext,"此业务需要欧卡，请前往欧乐APP“我的欧卡”进行关联。",Toast.LENGTH_LONG).show();
            return;
        }
        //add by yuanshaoyu :开过的卡片判断
        if (entity.isDownload()) {
            ToastUtil(mContext,"此卡已开，不能重复开卡!",Toast.LENGTH_LONG).show();
            return;
        }
        //add by yuanshaoyu 2017-6-12:添加请求蓝牙
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Utils.customToast(mContext,"本机没有找到蓝牙硬件或驱动！", Toast.LENGTH_SHORT).show();
        }
        // 如果本地蓝牙没有开启，则开启
        if (!mBluetoothAdapter.isEnabled()) {
            // 我们通过startActivityForResult()方法发起的Intent将会在onActivityResult()回调方法中获取用户的选择，比如用户单击了Yes开启，
            // 那么将会收到RESULT_OK的结果，
            // 如果RESULT_CANCELED则代表用户不愿意开启蓝牙
            CustomDialog.DialogClickListener listener;
            new CustomDialog(mContext, null, "蓝牙未开启", "点击确定按钮开启蓝牙", "取消", "确定", new CustomDialog.DialogClickListener() {
                @Override
                public void onPositiveClick() {
                    mBluetoothAdapter.enable();
                    Log.i(TAG, "点击开启蓝牙");
                    //add by yuanshaoyu 2017-6-16 :增加欧卡连接判断
                    if (MainActivity.ocardState == 1) {
                        //modified by yuanshaoyu 2017-6-15:增加一秒延时确保蓝牙开启
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mBluetoothAdapter.isEnabled()) {
                                    Log.i(TAG,"mBluetoothAdapter.isEnabled()");
                                    //add by yuanshaoyu 2017-6-8:增加点击时间跳到开卡界面
                                    Intent intent = new Intent(mContext, UploadCardActivity.class);
                                    intent.putExtra("card", entity);
                                    mContext.startActivity(intent);
                                }
                            }
                        },1000);
                    }else {
                        Utils.customToast(mContext,"该业务需要蓝牙功能，请检查欧卡与手机蓝牙连接是否正常", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNegtiveClick() {

                }
            }, CustomDialog.MODE_OKLINE2).show();
        } else {
            //add by yuanshaoyu 2017-6-16 :增加欧卡连接判断
            if (MainActivity.ocardState == 1) {
                Intent intent = new Intent(mContext, UploadCardActivity.class);
                intent.putExtra("card", entity);
                mContext.startActivity(intent);
            }else {
                Utils.customToast(mContext,"该业务需要蓝牙功能，请检查欧卡与手机蓝牙连接是否正常", Toast.LENGTH_SHORT).show();
            }

        }

    }

    //add by yuanshaoyu :自定义Toast
    public Toast ToastUtil(Context context, CharSequence text, int duration){
        Toast result = new Toast(context);
        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast_notification, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_custom_toast);
        String string = "操作出错";
        if (text != null) {
            string = text.toString();
        }
        tv.setText(string);
        result.setView(v);
        result.setGravity(Gravity.CENTER, 0, 0);
        result.setDuration(duration);
        return result;
    }
}
