package com.vboss.okline.ui.splash;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hyphenate.easeui.utils.DialogUtil;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.AppVersion;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.AppConfig;
import com.vboss.okline.data.local.ContactLocalDataSource;
import com.vboss.okline.data.local.SPUtils;
import com.vboss.okline.data.remote.ContactRemoteDataSource;
import com.vboss.okline.ui.auth.ApproveIDActivity;
import com.vboss.okline.ui.auth.CameraIDActivity;
import com.vboss.okline.ui.auth.SendEms;
import com.vboss.okline.ui.auth.SplashActivity;
import com.vboss.okline.ui.auth.present.CameraPresent;
import com.vboss.okline.ui.auth.present.RzContact;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.LocalContact;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.home.AppUpgradeHelper;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.home.UpgradeDialog;
import com.vboss.okline.utils.ToastUtil;

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
 * Date  : 2017/3/29 10:33 <br/>
 * Summary  : 启动页
 */

public class WelcomeActivity extends Activity implements RzContact.IResultView,RzContact.IResultUpdate {
    private static final String TAG = WelcomeActivity.class.getSimpleName();
    private static final short READ_PHONE_STATE = 103;
    private static final short REQUEST_SEND_SMS = 104;
    @BindView(R.id.sdv_welcome)
    SimpleDraweeView draweeView;
    private SendEms sendEms;
    private String imsi;
    private String imei;
    private boolean isRequest;//用户是否请求了登录接口
    private boolean isRequestResult;//登录接口是否有返回结果
    private UpgradeDialog dialog;//版本更新弹出框
    private Intent intent = new Intent();
    private boolean isDialog;
    private String updateOL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        // add by luoxx 0616 版本更新
        AppUpgradeHelper appUpgradeHelper = new AppUpgradeHelper(this,this);
        appUpgradeHelper.checkAppVersion(this);
        ButterKnife.bind(this);
        initGuideImage();
        toggleHideBar();
        initDate();
        if(getNetWorkState() == -1)
            ToastUtil.show(this,getResources().getString(R.string.no_network));
    }

    //登录数据初始化
    private void initDate() {
        String olNumber = UserRepository.getInstance(WelcomeActivity.this).getOlNo();
        if (!TextUtils.isEmpty(olNumber)) {
            isRequestResult = true;
            loadMain(olNumber);
        }else {
            setPermission();
            initSms();
        }
    }

    //短信发送初始化
    private void initSms() {
        String smsPhone = SPUtils.getSp(this, AppConfig.SP_PHONE_SMS);
        if (TextUtils.isEmpty(smsPhone)) {
            //获取发送短信的手机
            UserRepository.getInstance(getApplicationContext()).getCellPhoneForSms().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new DefaultSubscribe<String>(TAG) {
                @Override
                public void onNext(String phone) {
                    super.onNext(phone);
                    if (!TextUtils.isEmpty(phone)) {
                        Timber.i("接收短信手机号:" + phone);
                        SPUtils.saveSp(WelcomeActivity.this, AppConfig.SP_PHONE_SMS, phone);
                        sendSES(phone);
                    }
                }
            });
        } else {
            sendSES(smsPhone);
        }
    }

    //设置短信权限，手机状态权限
    private void setPermission() {
        String smsPhone = SPUtils.getSp(this, AppConfig.SP_PHONE_SMS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            //发送短信权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
            } else {
                sendSES(smsPhone);
            }
            //手机状态权限
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
            } else {
                sendSES(smsPhone);
            }

        } else {
            sendSES(smsPhone);
        }
    }

    /**
     * 发送短信
     *
     * @param phone 接收的手机号
     */
    private void sendSES(String phone) {
        if (TextUtils.isEmpty(phone)) return;
        if (isRequest) return;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SEND_SMS);
            return;
        }
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            imsi = telephonyManager.getSubscriberId();
            imei = telephonyManager.getDeviceId();
            if (TextUtils.isEmpty(imsi)) {
                ToastUtil.show(this, "手机无IMSI");
                return;
            }
            if (TextUtils.isEmpty(imei)) {
                ToastUtil.show(this, "手机无IMEI");
                return;
            }
            Timber.i("welcomeActivity  imei" + imei);
            Timber.i("welcomeActivity  imsi" + imsi);
            String selfPhone = telephonyManager.getLine1Number();
            Timber.i("本机手机号：" + selfPhone);
            sendEms = new SendEms(this, phone, selfPhone, imsi);
            CameraPresent camerapresent = new CameraPresent(this, this);
            camerapresent.login(imei, imsi);
            isRequest = true;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //短信接收成功
    @Override
    public void setResult(User user) {
        isRequestResult=true;
        if (user != null) {
            String olNumber = user.getOlNo();
            if (!TextUtils.isEmpty(olNumber)) {
                importLoad();
                loadMain(olNumber);
            }
        } else {
            loadMain(null);
        }
    }

    @Override
    public void setError(String error, int code) {
        isRequestResult=true;
        if (code == 2000) {//三次短信接收失败
            finish();
        } else if (code == 2100) {//三码不一致
            loadMain(null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case READ_PHONE_STATE:
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        initSms();
                    }
                    break;
                case REQUEST_SEND_SMS:
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        initSms();
                        SPUtils.saveSpBoolean(this, AppConfig.SP_IN_FIRST, true);
                    } else {//拒绝短信发送授权，退出App
                        boolean isFist = SPUtils.getSpBoolean(this, AppConfig.SP_IN_FIRST, true);
                        if (isFist) {
                            finish();
                            SPUtils.saveSpBoolean(this, AppConfig.SP_IN_FIRST, false);
                        } else {
                            //add by luoxx 0616 第一次进来申请短信权限拒绝，第二次重新申请
                            DialogUtil dialogUtil = new DialogUtil(WelcomeActivity.this, getString(R.string.permission_camera_title), getString(R.string.dialog_permission_sms));
                            dialogUtil.show();
                            dialogUtil.setSubmitRight(getString(R.string.dialog_sumbit_ok));
                            return;
                        }
                    }
                    break;
                default:
                    break;
            }

        } catch (Exception e) {

        }
    }

    //获取版本更新的数据回调
    @Override
    public void getResultUpdate(AppVersion appVersion) {
        if(appVersion != null){
            //if (dialog !=null && !dialog.isShowing()) {
                dialog = new UpgradeDialog(this, appVersion);
                dialog.show();
                dialog.setOnClick(click);
            //}

        }else{
            isDialog = true;
            loadMain(updateOL);
        }
    }

    //更新弹出框事件监听
    View.OnClickListener click = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            dialog.dismiss();
            isDialog = true;
            loadMain(updateOL);
        }
    };

    private void loadMain(final String olNumber) {
        //启动页停留三秒进入下一个页面
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(isDialog && isRequestResult) {
                        Thread.sleep(3000);
                        if (!TextUtils.isEmpty(olNumber))
                            startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        else ////add by luoxx 0621 用户协议界面
                            startActivity(new Intent(WelcomeActivity.this, SplashActivity.class));
                        finish();
                    }else{//弹出框没有关闭，但是注册验证已经完成
                        updateOL = olNumber;
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog!=null) {
            dialog.dismiss();
        }
        if (sendEms != null) {
            sendEms.onDestory();
        }
    }

    /**
     * 启动动画加载
     */
    private void initGuideImage() {
        Uri uri = Uri.parse("res://" + getPackageName() + "/" + R.drawable.welcome);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(uri)
                .build();
        draweeView.setController(controller);
    }

    protected int getNetWorkState() {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager
                .getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
            return activeNetworkInfo.getType();
        } else {
            return -1;
        }
    }

    //luoxx 0619 清除数据进入应用导入联系人
    private void importLoad(){
        final List<ContactItem> contact = LocalContact.getInstance(getApplicationContext()).getContact();
        //先去服务器拿如果
        ContactRemoteDataSource.getInstance().getAllContact().observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onNext(final List<ContactEntity> list) {
                        if (list!=null) {
                            ContactLocalDataSource.getInstance(WelcomeActivity.this).saveAll(list);
                        }
                    }
                });
    }

    /**
     * 隐藏底部状态栏
     */
    public void toggleHideBar() {
        int uiOptions = getWindow().getDecorView().getSystemUiVisibility();
        int newUiOptions = uiOptions;
        boolean isImmersiveModeEnabled = false;
        if (Build.VERSION.SDK_INT >= 19) {
            isImmersiveModeEnabled =
                    ((uiOptions | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY) == uiOptions);
        }
        if (isImmersiveModeEnabled) {
            Log.i(TAG, "Turning immersive mode mode off. ");
        } else {
            Log.i(TAG, "Turning immersive mode mode on.");
        }
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        }

        // Status bar hiding: Backwards compatible to Jellybean
        if (Build.VERSION.SDK_INT >= 18) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        if (Build.VERSION.SDK_INT >= 19) {
            newUiOptions ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
    }

}
