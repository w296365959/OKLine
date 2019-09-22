package com.vboss.okline.ui.auth;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.WorkerThreadScheduler;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.AppConfig;
import com.vboss.okline.data.local.ContactLocalDataSource;
import com.vboss.okline.data.local.SPUtils;
import com.vboss.okline.data.remote.ContactRemoteDataSource;
import com.vboss.okline.ui.auth.present.CameraPresent;
import com.vboss.okline.ui.auth.present.RzContact;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.contact.LocalContact;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.AppUtil;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.view.widget.CommonDialog;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/4/11 <br/>
 * Summary :身份认证
 */

public class ApproveIDActivity extends Activity implements RzContact.IResultView {
    private static final String TAG = "ApproveIDActivity";
    private static final int REQUEST_READ_CONTACTS = 2010;
    private static final String ACTION_CONNECT_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    /**
     * 没有连接网络
     */
    private static final int NETWORK_NONE = -1;
    private String imsi, imei;
    private TextView tvSure;
    private LinearLayout linearLayout;
    private ImageView ivSure;
    private SimpleDraweeView ivFunnel;
    private String idNumber;//身份证号码
    private String idName;//姓名
    BroadcastReceiver noNetWorkBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ACTION_CONNECT_CHANGE.equals(intent.getAction())) {
                ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = manager.getActiveNetworkInfo();
                /* **** no network toast *****/
                if (networkInfo == null) {
//                    noNetWorkToast();
                } else {
                    if (!networkInfo.isAvailable()) {
//                        noNetWorkToast();
                    }
                }
                if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                    int netWorkState = getNetWorkState();
                    //网络从新连接
                    if (netWorkState == NETWORK_NONE) {
                        Utils.customToast(ApproveIDActivity.this, getResources().getString(R.string.no_network),
                                Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        initData();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_approve);
        tvSure = (TextView) findViewById(R.id.tv_id_sure);
        ivSure = (ImageView) findViewById(R.id.iv_id_sure);
        ivFunnel = (SimpleDraweeView) findViewById(R.id.iv_funnel);
        linearLayout = (LinearLayout) findViewById(R.id.ll_id_ok);
        tvSure.setText(getString(R.string.id_sumbit_id));
        ivSure.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        initGuideImage();
        Intent intent = getIntent();
        if (intent != null) {
            imsi = getIntent().getStringExtra("IMSI");
            imei = getIntent().getStringExtra("IMEI");
        }
        idName = SPUtils.getSp(this, AppConfig.SP_ID_NAME);
        idNumber = SPUtils.getSp(this, AppConfig.SP_ID_NUM);
        registerNetWorkBroadcastReceiver();
    }

    private void registerNetWorkBroadcastReceiver() {
        IntentFilter intentFilter = new IntentFilter(ACTION_CONNECT_CHANGE);
        registerReceiver(noNetWorkBroadcastReceiver, intentFilter);
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
            return NETWORK_NONE;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(noNetWorkBroadcastReceiver);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        if (StringUtils.isNullString(idName)) {
            Intent intent = new Intent(this, VerifyFailActivity.class);
            intent.putExtra("FAILE", getString(R.string.no_real_name));
            startActivity(intent);
            finish();
            return;
        } else if (StringUtils.isNullString(idNumber)) {
            Intent intent = new Intent(this, VerifyFailActivity.class);
            intent.putExtra("FAILE", getString(R.string.no_number));
            startActivity(intent);
            finish();
            return;
        } else if (StringUtils.isNullString(imei)) {
            Intent intent = new Intent(this, VerifyFailActivity.class);
            intent.putExtra("FAILE", getString(R.string.no_imei));
            startActivity(intent);
            finish();
            return;
        } else if (StringUtils.isNullString(imsi)) {
            Intent intent = new Intent(this, VerifyFailActivity.class);
            intent.putExtra("FAILE", getString(R.string.no_imsi));
            startActivity(intent);
            finish();
            return;
        } else {
            CameraPresent camerapresent = new CameraPresent(this, this);
            camerapresent.loginOrRegisterUser(1, idNumber, idName, imei, imsi);
        }
    }

    private void initGuideImage() {
        Uri uri = Uri.parse("res://" + getPackageName() + "/" + R.drawable.plan_img);
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setUri(uri)
                .build();
        ivFunnel.setController(controller);
    }

    @Override
    public void setResult(User user) {
        tvSure.setText(getString(R.string.id_sumbit_id_success));
        ivSure.setVisibility(View.VISIBLE);
        if (user != null) {
            String olNUmber = user.getOlNo();
            if (!StringUtils.isNullString(olNUmber)) {
                SPUtils.saveSp(this, AppConfig.SP_EASE_NUMBER, olNUmber);
                tvSure.setText(getString(R.string.id_sumbit_text));
                linearLayout.setVisibility(View.VISIBLE);
                requestPermission();
            } else {
                startActivity(new Intent(this, VerifyFailActivity.class));
                finish();
            }

        } else {
            startActivity(new Intent(this, VerifyFailActivity.class));
            finish();
        }
    }

    @Override
    public void setError(String error, int code) {
        Timber.tag(ApproveIDActivity.class.getSimpleName()).i("setEror : %s", error);
        //add by luoxiuxiu 170613 三次后短信没有发送出去 启动身份拍照界面
        if (code == 2000) {
            Intent intent = new Intent(this, CameraIDActivity.class);
            startActivity(intent);
        } else {
            //add by luoxiuxiu 170613 启动失败界面
            Intent intent = new Intent(this, VerifyFailActivity.class);
            intent.putExtra("FAILE", error);
            startActivity(intent);
        }
        finish();
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            Timber.tag(TAG).i("requestPermission:>=23");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                Timber.tag(TAG).i("requestPermission:没有系统权限");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_READ_CONTACTS);
            } else {
                importLocal();
            }
        } else {
            importLocal();
        }

    }

    private void importLocal() {
        final List<ContactItem> contact = LocalContact.getInstance(getApplicationContext()).getContact();
        ContactsUtils.getInstance().testImportContacts(ApproveIDActivity.this, contact);

        startActivity(new Intent(ApproveIDActivity.this, VerifySuccessActivity.class));
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case REQUEST_READ_CONTACTS:
                Timber.tag(TAG).i("收到获取权限的通知");
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.tag(TAG).i("点击确定");
                    importLocal();
                } else {
                    Timber.tag(TAG).i("点击取消");
                    CommonDialog commonDialog = new CommonDialog(ApproveIDActivity.this);
                    commonDialog.setTilte(getString(R.string.cannot_import_contacts));
                    commonDialog.setNegativeButton(getString(R.string.cancel));
                    commonDialog.setPositiveButton(getString(R.string.confirm));
                    commonDialog.setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                        @Override
                        public void cancel(View view, CommonDialog dialog) {
                            dialog.dismiss();
                            startActivity(new Intent(ApproveIDActivity.this, VerifySuccessActivity.class));
                            finish();
                        }

                        @Override
                        public void ensure(View view, CommonDialog dialog) {
                            //重试
                            requestPermission();
                            dialog.dismiss();
                            startActivity(new Intent(ApproveIDActivity.this, VerifySuccessActivity.class));
                            finish();

                        }

                    });
                    commonDialog.show();
                }

                break;
            default:
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

}