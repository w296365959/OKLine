package com.vboss.okline.ui.contact.callPhone;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ICE.VOIP.ui.PhoneListener;
import com.ICE.VOIP.ui.PhoneManager;
import com.ICE.VOIP.ui.RecordManager;
import com.ICE.VOIP.ui.RingtoneManager;
import com.facebook.drawee.view.SimpleDraweeView;
import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.EventToken;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.Cert;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.local.ContactLocalDataSource;
import com.vboss.okline.ui.contact.ContactsUtils;
import com.vboss.okline.ui.user.Utils;
import com.vboss.okline.utils.AppUtil;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.CommonDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.okline.icm.libary.UserTools;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.vboss.okline.R.id.toolbar;
import static com.vboss.okline.base.OKLineApp.context;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/4 16:30
 * Desc : IPSS8通话界面
 */

public class CallingActivity extends BaseActivity {
    public static final int STATE_NULL = 0;
    public static final int STATE_INIT_FAILED = 1;
    public static final int STATE_INIT_SUCCESS = 2;
    public static final int STATE_LOGIN = 3;
    public static final int STATE_READY = 10;
    public static final int STATE_RINGING_ACTIVE = 11;
    public static final int STATE_RINGING = 12;
    public static final int STATE_CALLING = 13;
    public static final int STATE_CALL_OUT = 14;
    private static final String TAG = "CallingActivity";
    @BindView(R.id.safeView)
    View safeView;
    @BindView(R.id.avatarImg)
    SimpleDraweeView avatarImg;
    @BindView(R.id.relationStateImg)
    ImageView relationStateImg;
    @BindView(R.id.chronometerView)
    Chronometer chronometerView;
    @BindView(R.id.silenceBtn)
    ImageView silenceBtn;
    @BindView(R.id.keyboardBtn)
    ImageView keyboardBtn;
    @BindView(R.id.handFreeBtn)
    ImageView handFreeBtn;
    @BindView(R.id.answerContainer)
    View answerContainer;
    @BindView(R.id.cardBtn)
    ImageView cardBtn;
    @BindView(R.id.minimizeBtn)
    ImageView minimizeBtn;
    @BindView(R.id.recordBtn)
    ImageView recordBtn;
    @BindView(R.id.hangupBtn)
    ImageView hangupBtn;
    @BindView(R.id.answerBtn)
    ImageView answerBtn;
    @BindView(R.id.realnameView)
    TextView realnameView;
    @BindView(R.id.nicknameView)
    TextView nicknameView;
    @BindView(R.id.statusTextView)
    TextView statusTextView;
    @BindView(R.id.phoneNumView)
    TextView phoneNumView;
    @BindView(R.id.recordTV)
    TextView recordTV;
    @BindView(R.id.addTextView)
    TextView addTextView;
    @BindView(R.id.calling_silence_text)
    TextView callingSilenceText;
    @BindView(R.id.calling_call_text)
    TextView callingCallText;
    @BindView(R.id.calling_keyboard_text)
    TextView callingKeyboardText;
    @BindView(R.id.calling_handfree_text)
    TextView callingHandfreeText;
    @BindView(R.id.calling_minimize_text)
    TextView callingMinimizeText;
    @BindView(R.id.calling_hangup_text)
    TextView callingHangupText;
    private int state = STATE_NULL;
    private String phoneNum;
    private ContactEntity contactEntity;
    PhoneListener phoneListener = new PhoneListener() {
        @Override
        public void onLoginResult(boolean isSuccess) {
            Timber.tag(TAG).i("onLoginResult: " + isSuccess + "," + phoneNum);
            if (isSuccess) {
                state = STATE_READY;
                call(phoneNum);
            } else {
                ToastUtil.show(CallingActivity.this, "登录电话服务器失败");
                statusTextView.setText("初始化...");
            }
        }

        @Override
        public void onCallOut(String phoneNumber) {
            Timber.tag(TAG).i("onCallOut: " + phoneNumber);
            phoneNumView.setText(phoneNumber);
            statusTextView.setText("正在拨号...");
            state = STATE_CALL_OUT;
        }

        @Override
        public void onRinging(boolean active, String phoneNumber) {
            Timber.tag(TAG).i("onRinging: " + active + "," + phoneNumber);
            onRing(active, phoneNumber);
        }

        @Override
        public void onHangUp(boolean active, String phoneNumber) {
            Timber.tag(TAG).i("onHangupPhone: " + active + "," + phoneNumber);
            if (!active) {
                onHangupPhone();
            }
        }

        @Override
        public void onCalling(boolean active, String phoneNumber) {
            Timber.tag(TAG).i("onCalling: " + active + "," + phoneNumber);
            phoneNumView.setText(phoneNumber);
            if (active) {
                Timber.tag(TAG).i("对方已接听，正在通话 " + phoneNumber);
            } else {
                Timber.tag(TAG).i("接听，正在通话 " + phoneNumber);
            }
            RingtoneManager.getInstance().ringback(false);
            RingtoneManager.getInstance().stopRingtone();
            RingtoneManager.getInstance().phoneTalkInit();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            state = STATE_CALLING;
            statusTextView.setVisibility(View.GONE);
            safeView.setVisibility(View.VISIBLE);
            chronometerView.setVisibility(View.VISIBLE);
            chronometerView.setBase(SystemClock.elapsedRealtime());
            chronometerView.start();
            answerContainer.setVisibility(View.GONE);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ipss8_call);
        ButterKnife.bind(this);
        phoneNum = getIntent().getStringExtra("phoneNum");
        state = getIntent().getIntExtra("state", STATE_NULL);
        PhoneManager.getInstance().addPhoneListener(phoneListener);
        checkPermission();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        phoneNum = getIntent().getStringExtra("phoneNum");
        state = getIntent().getIntExtra("state", STATE_NULL);
        checkPermission();
    }

    private void checkPermission() {
        int permission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1000);
        } else {
            onIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onIntent();
            } else {
                Utils.customToast(this, "获取录音权限失败", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void onIntent() {
        answerContainer.setVisibility(View.GONE);
        if (TextUtils.isEmpty(phoneNum)) {
            Utils.customToast(this, "号码无效", Toast.LENGTH_SHORT).show();
            return;
        }
        phoneNumView.setText(phoneNum);
        if (state == STATE_NULL) {
            initConfig();
        } else if (state == STATE_RINGING) {
            onRing(false, phoneNum);
        }
        ContactLocalDataSource.getInstance(this)
                .getAllContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ContactEntity>>() {
                    @Override
                    public void call(List<ContactEntity> contactEntities) {
                        contactEntity = ContactEntity
                                .newBuilder()
                                .relationState(0)
                                .phone(phoneNum)
                                .build();
                        if (contactEntities.contains(contactEntity)) {
                            contactEntity = contactEntities.get(contactEntities.indexOf(contactEntity));
                        }
                        setContactInfo(contactEntity);
                    }
                });
    }

    private void setContactInfo(ContactEntity contact) {
        if (contact.relationState() == 3) {
            relationStateImg.setImageResource(R.mipmap.ic_calling_trusted);
            realnameView.setText(contact.realName());
            cardBtn.setImageResource(R.mipmap.ic_call_card);
            addTextView.setText("名片");
            //modify by linzhangbin 2017/6/2 互信好友显示头像
            avatarImg.setImageURI(contact.imgUrl());
            //modify by linzhangbin 2017/6/2 互信好友显示头像
        } else {
            relationStateImg.setImageResource(R.mipmap.ic_calling_trust_not);
            if (TextUtils.isEmpty(contact.remarkName())) {
                realnameView.setText(contact.phone());
            } else {
                realnameView.setText(contact.remarkName());
            }
            cardBtn.setImageResource(R.mipmap.ic_call_add);
            addTextView.setText("添加");
        }
        nicknameView.setText(contact.remarkName());
    }

    private void showAddContectDialog() {
        new CommonDialog(this)
                .setTilte("是否将对方加入通讯录")
                .setPositiveButton("是")
                .setNegativeButton("否")
                .setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                    @Override
                    public void cancel(View view, CommonDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void ensure(View view, CommonDialog dialog) {
                        addContact();
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void addContact() {
        ContactRepository.getInstance(this).addOrUpdateContact(contactEntity)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<ContactEntity>(TAG) {
                    @Override
                    public void onNext(ContactEntity entity) {
                        Timber.tag(TAG).i("创建联系人成功");
                        setContactInfo(entity);
                        RxBus.get().post(EventToken.CONTACT_CHANGED, true);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.show(context, "创建好友失败");
                    }
                });
    }

    private void initConfig() {
        if (PhoneManager.getInstance().isInit()) {
            call(phoneNum);
        } else {
            checkFile();
        }
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
                            ToastUtil.show(CallingActivity.this, "初始化失败，读取不到认证文件");
                            showDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        ToastUtil.show(CallingActivity.this, "初始化失败，读取不到认证文件");
                        showDialog();
                    }
                });
    }

    //add by linzhangbin 初始化失败读不到认证文件 弹窗
    private void showDialog() {
        new CommonDialog(CallingActivity.this)
                .setTitleVisible(View.GONE)
                .setContent(getString(R.string.ipss8_init_failed))
                .setNegativeButton(getString(R.string.dialog_negative))
                .setPositiveButton(getString(R.string.dialog_positive))
                .setOnDialogClickListener(new CommonDialog.OnDialogInterface() {
                    @Override
                    public void cancel(View view, CommonDialog dialog) {
                        dialog.dismiss();
                        finish();
                    }

                    @Override
                    public void ensure(View view, CommonDialog dialog) {
                        dialog.dismiss();
                        ContactsUtils.requestCallPhonePermission(CallingActivity.this);
                        AppUtil.call(CallingActivity.this, phoneNum);
                        finish();
                    }
                }).show();
    }
    //add by linzhangbin 初始化失败读不到认证文件 弹窗

    private void checkFile() {
        boolean result = UserTools.isUserFileExist(this);
        if (result) {
            PhoneManager.getInstance().initConfigFile(getApplicationContext());
        } else {
            getCA();
        }
    }

    private void call(String phoneNum) {
        if (PhoneManager.getInstance().isLogin()) {
            PhoneManager.getInstance().call(phoneNum);
        }
    }

    private void onRing(boolean active, String phoneNum) {
        phoneNumView.setText(phoneNum);
        statusTextView.setText("正在响铃");
        if (!active) {
            RingtoneManager.getInstance().playRingtone();
            answerContainer.setVisibility(View.VISIBLE);
            state = STATE_RINGING;
        } else {
            RingtoneManager.getInstance().ringback(true);
            state = STATE_RINGING_ACTIVE;
        }
        setVolumeControlStream(AudioManager.STREAM_RING);
    }

    private void onHangupPhone() {
        RingtoneManager.getInstance().ringback(false);
        RingtoneManager.getInstance().stopRingtone();
        RingtoneManager.getInstance().phoneTalkOver();
        setVolumeControlStream(AudioManager.STREAM_RING);
        safeView.setVisibility(View.GONE);
        statusTextView.setVisibility(View.VISIBLE);
        statusTextView.setText("通话结束");
        //lzb edit 2017/5/22 通话结束UI
        silenceBtn.setImageResource(R.mipmap.calling_silence_unclickable);
        keyboardBtn.setImageResource(R.mipmap.calling_keyboard_unclickable);
        handFreeBtn.setImageResource(R.mipmap.calling_handfree_unclickable);
        answerBtn.setImageResource(R.mipmap.calling_call_unclickable);
        recordBtn.setImageResource(R.mipmap.calling_record_unclickable);
        minimizeBtn.setImageResource(R.mipmap.calling_minimize_unclickable);
        //TODO 这里根据relationState判断之后更换图标是添加还是名片 暂时是添加 此处空指针

        if (contactEntity != null && contactEntity.relationState() == 3) {
            cardBtn.setImageResource(R.mipmap.calling_card_unclickable);
        } else {
            cardBtn.setImageResource(R.mipmap.calling_add_unclickable);
        }
        hangupBtn.setImageResource(R.mipmap.calling_hungup_unclickable);
        silenceBtn.setClickable(false);
        keyboardBtn.setClickable(false);
        handFreeBtn.setClickable(false);
        recordBtn.setClickable(false);
        minimizeBtn.setClickable(false);
        cardBtn.setClickable(false);
        hangupBtn.setClickable(false);
        callingHandfreeText.setTextColor(ActivityCompat.getColor(CallingActivity.this, R.color.calling_text_hangup));
        callingKeyboardText.setTextColor(ActivityCompat.getColor(CallingActivity.this, R.color.calling_text_hangup));
        callingMinimizeText.setTextColor(ActivityCompat.getColor(CallingActivity.this, R.color.calling_text_hangup));
        callingSilenceText.setTextColor(ActivityCompat.getColor(CallingActivity.this, R.color.calling_text_hangup));
        recordTV.setTextColor(ActivityCompat.getColor(CallingActivity.this, R.color.calling_text_hangup));
        addTextView.setTextColor(ActivityCompat.getColor(CallingActivity.this, R.color.calling_text_hangup));
        callingHangupText.setTextColor(ActivityCompat.getColor(CallingActivity.this, R.color.calling_text_hangup));
        callingCallText.setTextColor(ActivityCompat.getColor(CallingActivity.this, R.color.calling_text_hangup));
        //lzb edit 2017/5/22 通话结束UI

        chronometerView.stop();
        //add by linzhangbin 2017/6/27 挂断电话关闭免提 start
        if (RingtoneManager.getInstance().isSpeakerphoneOn()) {
            RingtoneManager.getInstance().setSpeakerphone(false);
        }
        //add by linzhangbin 2017/6/27 挂断电话关闭免提 end
        //挂断电话的时候关闭服务
        stopService(new Intent(this,CallingService.class));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    @OnClick({R.id.silenceBtn, R.id.keyboardBtn, R.id.handFreeBtn,
            R.id.recordBtn, R.id.minimizeBtn, R.id.cardBtn,
            R.id.answerBtn, R.id.hangupBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.silenceBtn:
                if (RingtoneManager.getInstance().isSilence()) {
                    RingtoneManager.getInstance().setSilence(false);
                    silenceBtn.setSelected(false);
                } else {
                    RingtoneManager.getInstance().setSilence(true);
                    silenceBtn.setSelected(true);
                }
                break;
            case R.id.keyboardBtn:
                break;
            case R.id.handFreeBtn:
                if (RingtoneManager.getInstance().isSpeakerphoneOn()) {
                    RingtoneManager.getInstance().setSpeakerphone(false);
                    handFreeBtn.setSelected(false);
                } else {
                    RingtoneManager.getInstance().setSpeakerphone(true);
                    handFreeBtn.setSelected(true);
                }
                break;
            case R.id.recordBtn:
                onRecordClick(false);
                break;
            case R.id.minimizeBtn:
                //TODO 服务应该在进入activity的时候就已经开启, 再点击只是把小窗口show出来 startServiceCommand
//                startService(new Intent(this,CallingService.class));
                break;
            case R.id.cardBtn:
                if (contactEntity.relationState() == 3) {

                } else {
                    showAddContectDialog();
                }
                break;
            case R.id.answerBtn:
                PhoneManager.getInstance().answer();
                break;
            case R.id.hangupBtn:
                if (state == STATE_RINGING) {
                    PhoneManager.getInstance().refuse();
                } else {
                    PhoneManager.getInstance().hangUp();
                }
                onHangupPhone();
                break;
        }
    }


    private void onRecordClick(boolean stop) {
//        int permission2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAPTURE_AUDIO_OUTPUT);
//        if (permission2 == PackageManager.PERMISSION_DENIED) {
//            Utils.customToast(this, "获取系统录音权限失败", Toast.LENGTH_SHORT).show();
//            return;
//        }



        if (stop) {
            RecordManager.getInstance().stopRecord();
            recordTV.setText("录音");
        } else {
            if (RecordManager.getInstance().isRecording()) {
                RecordManager.getInstance().stopRecord();
                recordTV.setText("录音");
            } else {
                RecordManager.getInstance().startRecord(this);
                recordTV.setText("正在录音");
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
        if(keyCode==KeyEvent.KEYCODE_BACK)
            return true;//不执行父类点击事件
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //add by linzhangbin 2017/6/27 关闭免提 start
        if (RingtoneManager.getInstance().isSpeakerphoneOn()) {
            RingtoneManager.getInstance().setSpeakerphone(false);
        }
        //add by linzhangbin 2017/6/27 关闭免提 end
        PhoneManager.getInstance().removePhoneListener(phoneListener);

        onRecordClick(true);
    }
}
