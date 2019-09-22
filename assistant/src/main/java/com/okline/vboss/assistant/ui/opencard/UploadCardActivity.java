package com.okline.vboss.assistant.ui.opencard;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.okline.vboss.assistant.R;
import com.okline.vboss.assistant.R2;
import com.okline.vboss.assistant.adapter.CardAdapter;
import com.okline.vboss.assistant.base.DefaultSubscribe;
import com.okline.vboss.assistant.net.CardEntity;
import com.okline.vboss.assistant.net.OLApiService;
import com.okline.vboss.assistant.ui.notice.CardHelper;
import com.okline.vboss.assistant.ui.notice.CardNoticeActivity;
import com.okline.vboss.assistant.ui.recharge.CardRechargeActivity;
import com.okline.vboss.assistant.ui.recharge.MainFragment;
import com.okline.vboss.assistant.ui.recharge.RechargeFragment;
import com.okline.vboss.assistant.utils.StringUtils;
import com.okline.vboss.assistant.widget.shadow.ListViewShadowViewHelper;
import com.okline.vboss.assistant.widget.shadow.ShadowProperty;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/6/8 13:51 <br/>
 * Summary  : 下载中界面
 */
public class UploadCardActivity extends AppCompatActivity {
    private static final String TAG = UploadCardActivity.class.getSimpleName();
    public static final String CARD_INSTANCE = "card_instance";
    @BindView(R2.id.uploading)
    SimpleDraweeView uploading;
    @BindView(R2.id.upload_card_img)
    SimpleDraweeView upload_card_img;
    String cardImg;
    CardEntity entity;
    Boolean bluetoothState;
    Subscription subscription;
    private boolean booleanExtra;
    private CardEntity cardEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_card);
        ButterKnife.bind(this);

        booleanExtra = getIntent().getBooleanExtra(MainFragment.EXTRA_RETURN_CARD_ENTITY, false);

        IntentFilter statusFilter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mStatusReceive, statusFilter);

        initView();
        initGuideImage();
        openAndSyncCard();
    }

    private void openAndSyncCard() {
        Log.i(TAG,"开始下载卡并且同步");
        subscription = OLApiService.getInstance().downloadCard(UploadCardActivity.this,entity.getCardMainType(),entity.getAid(),null)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<CardEntity>(TAG) {
                    @Override
                    public void onNext(CardEntity cardEntity) {
                        super.onNext(cardEntity);
                        Log.i(TAG, "同步卡片");
                        cancel();
                        UploadCardActivity.this.cardEntity = cardEntity;
                        showSuccessDialog(cardEntity);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        Log.i(TAG, "同步卡片失败");
                        cancel();
                        Intent intent = new Intent(UploadCardActivity.this,CardNoticeActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(CardHelper.KEY_CARD, entity);
                        bundle.putInt(CardHelper.KEY_OPERATE,CardHelper.CARD_DOWNLOAD_FAIL_BLUETOOTH);
                        intent.putExtras(bundle);
                        startActivityForResult(intent,0);
                    }
                });
    }

    private void showSuccessDialog(CardEntity cardEntity) {
        Log.i(TAG,"同步成功返回 ： "+ cardEntity);
        Intent intent = new Intent(this,CardNoticeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(CardHelper.KEY_CARD, cardEntity);
        bundle.putInt(CardHelper.KEY_OPERATE,CardHelper.CARD_DOWNLOAD_SUCCESS);
        intent.putExtras(bundle);
        startActivityForResult(intent,0);
    }

    private void initView() {
        Intent intent = getIntent();
        if (intent != null) {
            entity = (CardEntity) intent.getSerializableExtra("card");
            upload_card_img.setImageURI(Uri.parse(entity.getImgUrl()));
            //卡片阴影
            int r2 = StringUtils.dip2px(UploadCardActivity.this, 7.5f);
            ListViewShadowViewHelper.bindShadowHelper(
                    new ShadowProperty()
                            .setShadowColor(ActivityCompat.getColor(UploadCardActivity.this, R.color.colorShadow))
                            .setShadowDy(StringUtils.dip2px(UploadCardActivity.this, 0.25f))
                            .setShadowRadius(StringUtils.dip2px(UploadCardActivity.this, 2f))
                    , upload_card_img, r2, r2);
        }

    }

    /**
     * 启动动画加载
     */
    private void initGuideImage() {
        Uri uri = Uri.parse("res://" + getPackageName() + "/" + R.drawable.uploading);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(uri)
                .build();
        uploading.setController(controller);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.i(TAG,"按下了back键");
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case RESULT_OK:
                openAndSyncCard();
                break;
            case RESULT_CANCELED:
                Log.i(TAG,"giveUp");
                //<editor-fold desc="郑军 2017-06-15 15:22:13 开卡结果返回">
                if (booleanExtra) {
                    setResult(cardEntity!=null?RESULT_OK:RESULT_CANCELED,new Intent().putExtra(CARD_INSTANCE,cardEntity));
                }
                //</editor-fold>
                finish();
                //add by yuanshoayu 2017-6-13 : cancle activity Toggle animation
                overridePendingTransition(0,0);
                break;
        }

    }

     BroadcastReceiver mStatusReceive = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch(intent.getAction()){
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch(blueState){
                        case BluetoothAdapter.STATE_TURNING_ON:
                            Log.i(TAG,"STATE_TURNING_ON");
                            break;
                        case BluetoothAdapter.STATE_ON:
                            //开始扫描
                            Log.i(TAG,"STATE_ON");
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF:
                            Log.i(TAG,"STATE_TURNING_OFF");
                            break;
                        case BluetoothAdapter.STATE_OFF:
                            Log.i(TAG,"STATE_OFF");
                            break;
                    }
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mStatusReceive != null) {
            this.unregisterReceiver(mStatusReceive);
        }
        cancel();
    }
    public void cancel(){
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
            Log.i(TAG,"取消订阅");
        }
    }
}
