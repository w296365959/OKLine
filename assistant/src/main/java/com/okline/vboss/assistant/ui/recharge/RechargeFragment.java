package com.okline.vboss.assistant.ui.recharge;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.okline.ocp.RechargeSync;
import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.base.Config;
import com.okline.vboss.assistant.base.DefaultSubscribe;
import com.okline.vboss.assistant.net.OLApiService;
import com.okline.vboss.assistant.net.RechargeEntity;
import com.okline.vboss.assistant.ui.notice.CardHelper;
import com.okline.vboss.assistant.ui.notice.CardNoticeActivity;
import com.okline.vboss.assistant.ui.opay.OPayPasswordFragment;
import com.vboss.okline.tsm.DefaultConfig;
import com.vboss.okline.tsm.TsmCardType;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.okline.vboss.assistant.R.drawable.uploading;

/**
 * A simple {@link Fragment} subclass.
 */
public class RechargeFragment extends Fragment {


    public static final int REQUEST_CODE = 376;
    @BindView(R2.id.sdv_card_icon)
    SimpleDraweeView sdvCardIcon;
    @BindView(R2.id.sdv_loading)
    SimpleDraweeView sdvLoading;
    Unbinder unbinder;
    private CardRechargeActivity activity;
    private static final String TAG = "RechargeFragment";
    private Subscription tsmRechargeCard;

    public RechargeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (CardRechargeActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recharge_assistant, container, false);
        unbinder = ButterKnife.bind(this, view);
        sdvCardIcon.setImageURI(Uri.parse(activity.cardEntity.getImgUrl()));

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isAdded()) {
            activity.ivReturn.setVisibility(View.GONE);
            startRechargeOperation();
        }
    }

    private void startRechargeOperation() {
        tsmRechargeCard = OLApiService.getInstance().tsmRechargeCard(activity, cardTyperSwitch(activity.cardEntity.getAid()), activity.cardEntity.getAid(), activity.cardRechargeInfo.getAmount())
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<String>(TAG) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        initGuideImage();
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Utils.showLog(TAG, "充值异常：" + e.getMessage());
                        gotoResultPage(CardHelper.CARD_RECHARGE_FAIL);
                    }

                    @Override
                    public void onNext(String s) {
                        super.onNext(s);
                        Utils.showLog(TAG, "充值成功：" + s);
                        String ¥ = String.valueOf(Integer.valueOf(s.substring(0, s.indexOf("."))) * 100);
                        Utils.showLog(TAG, "充值成功后获取到的余额：" + ¥);
                        activity.cardEntity.setBalance(Integer.valueOf(¥));
                        Utils.unsubscribeRxJava(tsmRechargeCard);
                        rx.Observable.create(new rx.Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                try {
                                    String rechargeSync = new RechargeSync().rechargeSync(
                                            Config.OL_NUM,
                                            activity.bankDebitInfo.getTn(),
                                            activity.cardRechargeInfo.getAmount(),
                                            activity.cardEntity.getCardId(),
                                            activity.cardEntity.getCardMainType(),
                                            "6",
                                            OPayPasswordFragment.P_ID,
                                            OPayPasswordFragment.P_CODE
                                    );
                                    //modify by wangshuai 2017-06-15
                                    JSONObject jsonObject = new JSONObject(rechargeSync);
                                    int resultCode = jsonObject.optInt("resultCode");
                                    if (resultCode != 0) {
                                        subscriber.onError(new Exception("resultMsg ：" + jsonObject.opt("resultMsg")));
                                    } else {
                                        subscriber.onNext(rechargeSync);
                                    }
                                } catch (Exception e) {
                                    subscriber.onError(e);
                                }
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new DefaultSubscribe<String>(TAG) {
                                    @Override
                                    public void onError(Throwable e) {
                                        Utils.showLog(TAG, "同步失败：" + e.getMessage());
                                        gotoResultPage(CardHelper.CARD_RECHARGE_FAIL);
                                    }

                                    @Override
                                    public void onNext(String s) {
                                        super.onNext(s);
                                        Utils.showLog(TAG, "同步成功：" + s);
                                        gotoResultPage(CardHelper.CARD_RECHARGE_SUCCESS);
                                    }
                                });
                    }
                });
    }

    private void gotoResultPage(int cardRechargeFail) {
        int amount = activity.cardRechargeInfo.getAmount();
        RechargeEntity rechargeEntity = new RechargeEntity(
                activity.cardEntity.getCardName(),
                amount,
                0,
                amount,
                amount,
                activity.bankCards.get(0).getCardName(),
                activity.bankDebitInfo.getOrderDate(),
                activity.bankCards.get(0).getImgUrl(),
                activity.cardEntity.getImgUrl(),
                activity.bankDebitInfo.getTn()
        );
        Intent intent = new Intent(activity, CardNoticeActivity.class).putExtra(CardHelper.KEY_OPERATE, cardRechargeFail).putExtra(CardHelper.KEY_RECHARGE, rechargeEntity).putExtra(CardHelper.KEY_CARD, activity.cardEntity);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Utils.unsubscribeRxJava(tsmRechargeCard);
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            //TODO 重新发起充值流程
            startRechargeOperation();
        } else if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_CANCELED) {
            activity.finish();
        }
    }

    public static int cardTyperSwitch(String i) {
        int result = 0;
        if (i.equals(DefaultConfig.PBOC_AID)) {
            result = TsmCardType.CARD_TYPE_PBOC;
        }
        if (i.equals(DefaultConfig.VIP_AID)) {
            result = TsmCardType.CARD_TYPE_VIP;
        }
        if (i.equals(DefaultConfig.TRANSPORT_AID)) {
            result = TsmCardType.CARD_TYPE_TRANSPORT;
        }
        return result;
    }

    /**
     * 启动动画加载
     */
    private void initGuideImage() {
        Uri uri = Uri.parse("res://" + activity.getPackageName() + "/" + uploading);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(uri)
                .build();
        sdvLoading.setController(controller);
    }
}
