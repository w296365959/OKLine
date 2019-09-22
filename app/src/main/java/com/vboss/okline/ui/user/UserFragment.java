package com.vboss.okline.ui.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cosw.sdkblecard.DeviceInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.customized.CustomDialog;
import com.vboss.okline.ui.user.customized.SettingsButton;
import com.vboss.okline.ui.user.files.MyFilesActivity;
import com.vboss.okline.utils.FrescoUtil;
import com.vboss.okline.view.widget.OKCardView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/3/28
 * Summary : 个人中心主页面fragment
 */

public class UserFragment extends BaseFragment implements View.OnClickListener {

    public static final String BLUETOOTH_ADDRESS = "bluetoothAddress";
    public static final String BLUETOOTH_NAME = "bluetoothName";
    public static final String DEVICE_NO = "deviceNo";
    public static final String BIND_DATE = "bindDate";
    public static final String TOTAL_VOLUME = "totalVolume";
    public static final String AVAILABLE_VOLUME = "availableVolume";
    public static final String VERSION_INFO = "versionInfo";
    public static final String BATTERY_VOLUME = "batteryVolume";
    public static final String LOSS_DATE = "lossDate";
    @BindView(R.id.action_back)
    ImageButton actionBack;
    @BindView(R.id.action_back_layout)
    RelativeLayout actionBackLayout;
    @BindView(R.id.sdv_logo)
    SimpleDraweeView sdvLogo;
    @BindView(R.id.action_title)
    TextView actionTitle;
    @BindView(R.id.action_menu_button)
    ImageButton actionMenuButton;
    @BindView(R.id.iv_ocard_state)
    LogoView ivOcardState;
    @BindView(R.id.okcard_view)
    OKCardView okcardView;
    @BindView(R.id.action_menu_layout)
    RelativeLayout actionMenuLayout;
    @BindView(R.id.iv_user_avatar)
    SimpleDraweeView ivUserAvatar;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_user_id)
    TextView tvUserId;
    @BindView(R.id.sl_user_my_ocard)
    SettingsButton slUserMyOcard;
    @BindView(R.id.sl_user_my_file)
    SettingsButton slUserMyFile;
    @BindView(R.id.sl_user_my_jifen)
    SettingsButton slUserMyJifen;
    @BindView(R.id.sl_user_my_assets)
    SettingsButton slUserMyAssets;
    @BindView(R.id.ll_mystyle_black)
    SettingsButton llMystyleBlack;
    @BindView(R.id.ll_mystyle_usual)
    SettingsButton llMystyleUsual;
    @BindView(R.id.ll_mystyle_pink)
    SettingsButton llMystylePink;
    @BindView(R.id.ll_mystyle_setting)
    SettingsButton llMystyleSetting;
    @BindView(R.id.ll_my_files)
    LinearLayout llMyFiles;
    @BindView(R.id.gv_my_files)
    GridView gvMyFiles;
    Unbinder unbinder;
    private MainActivity activity;
    private UserRepository userRepository;
    private static final String TAG = "UserFragment";
    public static User user;
    private int ocardState = 0;
    private String message;
    private Subscription subscription1;
    private View view;
    private int[] picRes = new int[]{R.mipmap.myfile_pic,R.mipmap.myfile_doc,R.mipmap.myfile_sms,R.mipmap.myfile_mail,R.mipmap.myfile_clip};
    private String[] titles = new String[]{"我的照片","我的文件","我的短信","我的邮件","我的截屏"};

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        //<editor-fold desc="郑军 修改日期：2017-06-05 15:18:41 删除读取IMEI以及IMSI等内容">
        //</editor-fold>

        userRepository = UserRepository.getInstance(getContext());
        user = userRepository.getUser();
        view = inflater.inflate(R.layout.fragment_user, null);
        unbinder = ButterKnife.bind(this, view);
        processAvatar();
        actionTitle.setText("我的");
        tvUserId.setText("欧乐会员号：" + user.getUserNo());
        actionTitle.setText("我的");
        requestOKCard();
        setOnUIClickListener();
        gvMyFiles.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return picRes.length;
            }

            @Override
            public Object getItem(int position) {
                return picRes[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(getContext(),R.layout.view_my_files_item,null);
                ImageView pic = (ImageView) convertView.findViewById(R.id.pic);
                pic.setImageResource(picRes[position]);
                TextView text = (TextView) convertView.findViewById(R.id.text);
                text.setText(titles[position]);
                return convertView;
            }
        });
        gvMyFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Utils.customToast(getContext(), titles[position], Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent();
//                switch (position) {
//                    case 0:
//
//                        break;
//                    case 1:
//                        intent.setClass(getContext(), MyFilesActivity.class);
//                        intent.putExtra(MyFilesActivity.FILE_TYPE,MyFilesActivity.FILE_TYPE_DOCUMENT);
//                        break;
//                    case 2:
//                        intent.setClass(getContext(), MyFilesActivity.class);
//                        intent.putExtra(MyFilesActivity.FILE_TYPE,MyFilesActivity.FILE_TYPE_SMS);
//                        break;
//                    case 3:
//                        intent.setClass(getContext(), MyFilesActivity.class);
//                        intent.putExtra(MyFilesActivity.FILE_TYPE,MyFilesActivity.FILE_TYPE_MAIL);
//                        break;
//                    case 4:
//
//                        break;
//                }
//                startActivity(intent);
            }
        });
        actionBackLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.removeSecondFragment();
            }
        });
        return view;
    }

    private void requestOKCard() {
        subscription1 = userRepository.getOCard()
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<OKCard>(TAG) {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ocardState = 1;
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        message = throwable.getMessage();
                        Utils.showLog(TAG, "查询欧卡出错：" + message);
                        ocardState = 0;
                    }

                    @Override
                    public void onNext(OKCard okCard) {
                        super.onNext(okCard);
                        Utils.showLog(TAG, "获取到的欧卡信息：" + okCard);
                        if (okCard.getIsBind() != 0) {
                            ocardState = 3;
                            SharedPreferences.Editor edit = activity.sharedPreferences.edit();
                            edit.putString(DEVICE_NO, okCard.getDeviceNo()).apply();
                            edit.putString(BIND_DATE, okCard.getBindDate()).apply();
                            edit.putString(BLUETOOTH_NAME, okCard.getBhtName()).apply();
                            edit.putString(BLUETOOTH_ADDRESS, okCard.getBhtName()).apply();
                        } else {
                            ocardState = 2;
                        }
                    }
                });
    }

    private void processAvatar() {
        if (user != null && !TextUtils.isEmpty(user.getAvatar())) {
            Utils.setImageWithRoundCorner(user.getAvatar(), ivUserAvatar);
        } else {
            ivUserAvatar.setController(FrescoUtil.getDefaultImage(getContext(), R.mipmap.default_avatar));
        }
    }





    private void setOnUIClickListener() {
        slUserMyOcard.setOnClickListener(this);
        slUserMyAssets.setOnClickListener(this);
        slUserMyJifen.setOnClickListener(this);
        slUserMyFile.setOnClickListener(this);
        llMystyleBlack.setOnClickListener(this);
        llMystylePink.setOnClickListener(this);
        llMystyleUsual.setOnClickListener(this);
        llMystyleSetting.setOnClickListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (subscription1 != null && !subscription1.isUnsubscribed()) {
            subscription1.unsubscribe();
        }
        unbinder.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Utils.showLog(TAG, "onHiddenChanged:" + hidden);
        if (!hidden) {
            requestOKCard();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sl_user_my_ocard:
                switch (ocardState) {
                    case 0:
                        new CustomDialog(getContext(), null, "未能查询到欧卡绑定状态，是否重试？", null, getString(R.string.cancel), getString(R.string.confirm), new CustomDialog.DialogClickListener() {
                            @Override
                            public void onNegtiveClick() {

                            }

                            @Override
                            public void onPositiveClick() {
                                requestOKCard();
                            }
                        }, CustomDialog.MODE_OKLINE2).show();
                        break;
                    case 1:
                        Utils.customToast(getContext(), "正在获取欧卡状态，请稍候！", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        activity.addSecondFragment(new OCardAttachFragment());
                        break;
                    case 3:
                        activity.addSecondFragment(new OCardAttachFragment());
//                        activity.addSecondFragment(OcardFragment.newInstance());
                        break;
                }
                break;
            case R.id.sl_user_my_jifen:

                break;
            case R.id.sl_user_my_assets:

                break;
            case R.id.sl_user_my_file:
                if (llMyFiles.getVisibility() == View.VISIBLE) {
                    llMyFiles.setVisibility(View.GONE);
                    slUserMyFile.setRlBackground(R.drawable.background_empty_solid_with_stroke,R.mipmap.myfile_white);
                } else {
                    llMyFiles.setVisibility(View.VISIBLE);
                    slUserMyFile.setRlBackground(R.drawable.background_black_solid_with_stroke,R.mipmap.myfile_black);
                }
                break;
            case R.id.ll_mystyle_usual:

                break;
            case R.id.ll_mystyle_pink:

                break;
            case R.id.ll_mystyle_black:

                break;
            case R.id.ll_mystyle_setting:

                break;
        }
    }
}