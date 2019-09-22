package com.vboss.okline.test;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ICE.VOIP.ui.PhoneManager;
import com.ICE.VOIP.ui.RingtoneManager;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.Cert;
import com.vboss.okline.runtimepermissions.PermissionsManager;
import com.vboss.okline.runtimepermissions.PermissionsResultAction;
import com.vboss.okline.utils.ToastUtil;

import cn.okline.icm.libary.UserTools;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class TestPhoneActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "TestPhoneActivity";
    private TextView logView;
    private Button speakerBtn;
    private PhoneListenerImpl listener;
    private EditText numberEdit;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_phone);

        scrollView = (ScrollView) findViewById(R.id.scrollView);
        logView = (TextView) findViewById(R.id.logView);
        numberEdit = (EditText) findViewById(R.id.numberEdit);
        findViewById(R.id.initBtn).setOnClickListener(this);
        findViewById(R.id.getCABtn).setOnClickListener(this);
        findViewById(R.id.decodeCABtn).setOnClickListener(this);
        findViewById(R.id.getCABtn).setOnClickListener(this);
        findViewById(R.id.callBtn).setOnClickListener(this);
        findViewById(R.id.answerBtn).setOnClickListener(this);
        findViewById(R.id.hangUpBtn).setOnClickListener(this);
        findViewById(R.id.stopBtn).setOnClickListener(this);
        speakerBtn = (Button) findViewById(R.id.speakerBtn);
        speakerBtn.setOnClickListener(this);
    }

    public void showLog(String context) {
        logView.append(context + "\n");
        scroll2Bottom(scrollView, logView);
    }

    private void scroll2Bottom(final ScrollView scroll, final View inner) {
        int offset = inner.getMeasuredHeight() - scroll.getMeasuredHeight();
        if (offset < 0) {
            System.out.println("定位...");
            offset = 0;
        }
        scroll.scrollTo(0, offset);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.initBtn:
                checkFile();
                break;
            case R.id.getCABtn:
                getCA();
                break;
            case R.id.decodeCABtn:
                break;
            case R.id.callBtn:
                try {
                    PhoneManager.getInstance().call(numberEdit.getText().toString());
                } catch (Exception e) {
                    Timber.tag(TAG).i(e.getMessage());
                }

                break;
            case R.id.answerBtn:
                if (listener.getState() == PhoneListenerImpl.STATE_RINGING) {
                    PhoneManager.getInstance().answer();
                }
                break;
            case R.id.hangUpBtn:
                if (listener.getState() == PhoneListenerImpl.STATE_RINGING) {
                    PhoneManager.getInstance().refuse();
                } else {
                    PhoneManager.getInstance().hangUp();
                }
                break;
            case R.id.speakerBtn:
                if (RingtoneManager.getInstance().isSpeakerphoneOn()) {
                    RingtoneManager.getInstance().setSpeakerphone(false);
                    speakerBtn.setText("开启免提");
                } else {
                    RingtoneManager.getInstance().setSpeakerphone(true);
                    speakerBtn.setText("关闭免提");
                }
                break;
            case R.id.stopBtn:
//                PhoneManager.getInstance().destory();
                break;
        }
    }

    private void checkFile() {
        if (UserTools.isUserFileExist(this)) {
            init();
        } else {
            getCA();
        }
    }

    private void init() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                new String[]{Manifest.permission.RECORD_AUDIO}, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        listener = new PhoneListenerImpl(TestPhoneActivity.this);
                        PhoneManager.getInstance().addPhoneListener(listener);
                        PhoneManager.getInstance().initConfigFile(getApplicationContext());
                        showLog("初始化...");
                    }

                    @Override
                    public void onDenied(String permission) {
                        ToastUtil.show(TestPhoneActivity.this, "没有权限我什么都不能做");
                    }
                });
    }

    private void getCA() {
        UserRepository userRepository = UserRepository.getInstance(this);
        userRepository.getCA()
                .map(new Func1<Cert, Boolean>() {
                    @Override
                    public Boolean call(Cert cert) {
                        return UserTools.writeUserData(getApplicationContext(), cert.getCert());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<Boolean>(TAG) {
                    @Override
                    public void onNext(Boolean result) {
                        if (result) {
                            checkFile();
                        } else {
                            showLog("初始化失败，读取不到认证文件");
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        showLog("初始化失败，读取不到认证文件");
                    }
                });
    }


}
