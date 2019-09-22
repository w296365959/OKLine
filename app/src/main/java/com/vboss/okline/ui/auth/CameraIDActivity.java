package com.vboss.okline.ui.auth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.Text;
import com.hyphenate.easeui.utils.DialogUtil;
import com.idcard.CardInfo;
import com.idcard.TRECAPIImpl;
import com.idcard.TStatus;
import com.idcard.TengineID;
import com.ui.card.TRCardScan;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.local.AppConfig;
import com.vboss.okline.data.local.SPUtils;
import com.vboss.okline.ui.auth.present.GetIdInfoPresent;
import com.vboss.okline.ui.user.OcardFragment;
import com.vboss.okline.utils.AppUtil;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.ui.card.TRCardScan.isOpenProgress;

/**
 * Created by luoxiuxiu
 * 2017/4/10
 * description: 实名认证
 */

public class CameraIDActivity extends Activity implements View.OnClickListener {

    private static final String TAG = CameraIDActivity.class.getSimpleName();
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private ImageView ivInfo;
    private boolean isSelf = false;//当前是否为自拍

    private TRECAPIImpl engineDemo = new TRECAPIImpl();
    private TengineID tengineID = TengineID.TIDCARD2;
    private static final int PERMISSION_CAMERA = 100;
    private Bitmap takeImg = null;//身份证照片
    private GetIdInfoPresent getIdInfoPresent;
    private TextView tvStep;
    private boolean isClick = false;//区别点击事件，主要是二秒后页面调转，跟用户的手动点击事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camer_id);
        ButterKnife.bind(this);
        TRCardScan.isOpenProgress = false;
        getIdInfoPresent = GetIdInfoPresent.getInstance(this);
        findViewById(R.id.take_photo_btn).setOnClickListener(this);
        tvStep = (TextView) findViewById(R.id.tv_step);
        ivInfo = (ImageView) findViewById(R.id.iv_guide);
        Intent intent = getIntent();
        ivInfo.setImageResource(R.drawable.prompt_img);
        if (intent != null && intent.hasExtra(OcardFragment.INTENT_TAG) && intent.getStringExtra(OcardFragment.INTENT_TAG).equals(OcardFragment.TAG)) {
            tvTitle.setText(getString(R.string.id_certification));
        }
        load();
    }

    private void load() {
        //停留二秒进入下一个页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if (requestPermission()) {
                        requestOnClick();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.take_photo_btn:
                if (isClick) return;
                if (requestPermission()) {
                    requestOnClick();
                }
                break;
            default:
                break;
        }
    }

    private void requestOnClick() {
        if (isSelf) {//自拍
            if (!isClick) {
                startActivity(new Intent(this, CamerSelfActivity.class));
                finish();
                isClick = true;
            }
        } else {
            if (!isClick) {
                goToCapture();
                isClick = true;
            }
        }
    }


    //需要的权限

    private boolean requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PERMISSION_CAMERA);
            return false;
        }
        return  true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
             switch (requestCode) {
                case PERMISSION_CAMERA:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        goToCapture();
                    } else {
                        isClick = false;
//                        DialogUtil dialogUtil = new DialogUtil(CameraIDActivity.this, getString(R.string.permission_camera_title), getString(R.string.dialog_permission_camera));
//                        dialogUtil.show();
//                        dialogUtil.setSubmitRight(getString(R.string.dialog_sumbit_ok));
                    }
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToCapture() {
        if(TRCardScan.isOpenProgress)return;
        String curKeyString = "eaedf2b4cb679a6af248a862f5a1fe18";
        engineDemo.TR_StartUP(this, curKeyString);
//        TStatus tStatus = engineDemo.TR_StartUP(this, engineDemo.TR_GetEngineTimeKey());
        TRCardScan.SetEngineType(tengineID);
        TRCardScan.isOpenProgress = true;
        TRCardScan.ShowCopyRightTxt = "";
        Intent intent = new Intent(this, TRCardScan.class);
        intent.putExtra("engine", engineDemo);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            Toast.makeText(this, this.getString(R.string.tip9), Toast.LENGTH_LONG).show();
            return;
        }
        CardInfo cardInfo = (CardInfo) data.getSerializableExtra("cardinfo");
        Intent intents = getIntent();
        String tag = intents.getStringExtra("tag");
        if ((!StringUtils.isNullString(tag)) && tag.equalsIgnoreCase(OcardFragment.TAG)) {
            Intent data1 = new Intent();
            data1.putExtra("CARD", cardInfo);
            setResult(Activity.RESULT_OK, data1);
            finish();
        } else {
            takeImg = TRCardScan.TakeBitmap;
            if (takeImg != null) {
                getIdInfoPresent.savePicData(takeImg, cardInfo);
                ivInfo.setImageResource(R.drawable.prompt_self_img);
                isSelf = true;
                tvStep.setText(getString(R.string.rz_tip8));

            }else{//拍摄身份证返回
                TRCardScan.isOpenProgress = false;
            }
            load();
            isClick = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            engineDemo.TR_ClearUP();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
