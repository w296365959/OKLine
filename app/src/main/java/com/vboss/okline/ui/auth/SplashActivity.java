package com.vboss.okline.ui.auth;

import android.Manifest.permission;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.common.collect.Lists;
import com.vboss.okline.R;
import com.vboss.okline.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * OKLine(luoxiuxiu) co.,Ltd.<br/>
 * Author  : luoxiuxiu <br/>
 * Email   : show@okline.cn <br/>
 * Date    : 2017/5/3 <br/>
 * Summary :  用户协议
 */
public class SplashActivity extends AccessBaseActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_PERMISSION = 1000;

    @BindView(R.id.tv_splash_info)
    TextView tvInfo;
    @BindView(R.id.btn_start_approve)
    Button btnApprove;
    @BindView(R.id.cb_selector)
    CheckBox checkBox;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private InputStream inputStream = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        ButterKnife.bind(this);
        AssetManager assetManager = getAssets();
        tvTitle.setText(getString(R.string.splash_title));
        try {
            inputStream = assetManager.open("text.txt");
            String asset = readTextFile(inputStream);
            tvInfo.setText(asset);
        } catch (IOException e) {
            e.printStackTrace();
        }
        btnApprove.setOnClickListener(this);

        CountDownTimer timer = new CountDownTimer(10000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                btnApprove.setEnabled(false);
                checkBox.setEnabled(false);
                btnApprove.setText(String.format(getString(R.string.approved), "" + millisUntilFinished / 1000));
//                btnApprove.setText((millisUntilFinished / 1000) + "秒后可重发");
            }

            @Override
            public void onFinish() {
                btnApprove.setEnabled(true);
                checkBox.setEnabled(true);
                btnApprove.setText(getString(R.string.authentication_approved));
                btnApprove.setBackgroundResource(R.drawable.shape_card_open_green);
            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_start_approve:
                boolean isCheckBox = checkBox.isChecked();
                if (isCheckBox) {
                    startActivity(new Intent(this, CameraIDActivity.class));
                    finish();
                } else {
                    ToastUtil.show(this, getString(R.string.agress_approve));
                }
                break;
            default:
                break;
        }

    }

    private void requestPermissionsBatch() {
        String[] permissions = new String[]{permission.SEND_SMS, permission.READ_PHONE_STATE, permission.CAMERA,
                permission.CALL_PHONE, permission.RECORD_AUDIO};
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            requestPermissions(permissions, REQUEST_CODE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, permissions, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == REQUEST_CODE_PERMISSION) {
                Timber.w("Permission list : " + Lists.asList(permissions, new String[0]).toString());
            }
        }
    }

    private String readTextFile(InputStream inputStream) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];

        int len;

        try {

            while ((len = inputStream.read(buf)) != -1) {

                outputStream.write(buf, 0, len);

            }

            outputStream.close();

            inputStream.close();

        } catch (IOException e) {

        }

        return outputStream.toString();

    }

}

