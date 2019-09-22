package com.vboss.okline.ui.contact.c2cPart;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.nfc.Application;
import com.vboss.okline.nfc.NfcManager;
import com.vboss.okline.nfc.SPEC;
import com.vboss.okline.nfc.bean.NfcCard;
import com.vboss.okline.nfc.ui.NfcPage;
import com.vboss.okline.ui.user.customized.LoadingView;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TimeUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.CommonDialog;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/10 20:45
 * Desc :
 */

public class C2CwaitingActivity extends BaseActivity implements C2Contract.View {
    private static final String TAG = "C2CwaitingActivity";
    private static String cardNo1;
    private static C2CwaitingActivity activity;
    @BindView(R.id.loadingview)
    LoadingView loadingview;
    /**
     * ActionBar bind View
     */
    @BindView(R.id.action_back)
    ImageButton action_back;
    @BindView(R.id.action_menu_button)
    ImageButton action_menu;
    @BindView(R.id.action_title)
    TextView action_title;
    @BindView(R.id.sdv_logo)
    SimpleDraweeView action_logo;
    private NfcManager nfcManager;
    private C2CPresenter presenter;
    private Handler handler;
    private String mClientCardNfcId;
    private int amount;
    private String myCount;

    /**
     * NFC 得到卡片卡号的方法
     *
     * @param card
     */
    public static void getCard(NfcCard card) {
        Log.i("C2CwaitingActivity", "进来了,card: " + card);
        if (card != null) {
            Collection<Application> applications = card.getApplications();

            for (Application app : applications) {
                cardNo1 = app.getStringProperty(SPEC.PROP.SERIAL);
                Log.i("C2CwaitingActivity", "进来了,cardNo:" + cardNo1);
                activity.gathering();
            }

        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.onebyone);
        ButterKnife.bind(this);
        nfcManager = new NfcManager(this);
        onNewIntent(getIntent());
        loadingview.startLoading();
        activity = this;
        presenter = new C2CPresenter(this, new C2CModel(), this);
        initToolbar();
        Bundle bundle = getIntent().getExtras();
        amount = bundle.getInt("amount");
        myCount = bundle.getString("myCount");
        Timber.tag(TAG).i("amount:"+amount);
        Timber.tag(TAG).i("myCount:"+myCount);
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CommonDialog commonDialog = new CommonDialog(C2CwaitingActivity.this);
                commonDialog.setTilte(getString(R.string.c2c_overtime));
                commonDialog.setNegativeButton(getString(R.string.cancel));
                commonDialog.setPositiveButton(getString(R.string.confirm));
                commonDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                    @Override
                    public void cancel(View view, CommonDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void ensure(View view, CommonDialog dialog) {
                        dialog.dismiss();
                        finish();
                    }
                });
                commonDialog.show();


            }
        },20000);

    }

    private void initToolbar() {
        action_back.setImageResource(R.drawable.ic_status_back);
        action_title.setText(getResources().getString(R.string.title_gather));
        action_menu.setVisibility(View.GONE);
        action_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (!nfcManager.readCard(intent, new NfcPage(this))) ;

    }

    @Override
    public void setIntent(Intent intent) {
        if (NfcPage.isSendByMe(intent))

            super.setIntent(intent);
    }

    /**
     * NFC靠一靠执行的逻辑
     */
    public void gathering() {
        Log.i("C2CwaitingActivity", "cardNo1+++++++++++++++++++ " + cardNo1);
        if (!StringUtils.isNullString(cardNo1) && cardNo1.startsWith("6228000")) {
            Timber.tag(TAG).i("卡号匹配可转账");
            //TODO 得到金额 对方账户 日期 收款账户(默认卡)
            presenter.transfer(amount, "OLHZ310571000000000365");

        } else {

            CommonDialog commonDialog = new CommonDialog(this);
            commonDialog.setContent(getString(R.string.unkown_card));
            commonDialog.setNegativeButton(getString(R.string.cancel));
            commonDialog.setPositiveButton(getString(R.string.confirm));
            commonDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                @Override
                public void cancel(View view, CommonDialog dialog) {
                    dialog.dismiss();
                }

                @Override
                public void ensure(View view, CommonDialog dialog) {
                    dialog.dismiss();
                    onBackPressed();
                }
            });
            //TODO 此处未读出来应关闭NFC功能??
        }
    }


    @Override
    public void transfer() {

        Intent intent = new Intent(C2CwaitingActivity.this, C2CsuccessActivity.class);


        Bundle bundle = new Bundle();
        bundle.putString("myCount", myCount);
        bundle.putString("amount", String.valueOf(amount));
        bundle.putString("time", TimeUtils.getCurrentTime("yyyy-MM-dd HH:mm:ss"));
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onResume() {
        super.onResume();

        /*showWirelessSettingsDialog();*/

        nfcManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        nfcManager.onPause();
    }




}
