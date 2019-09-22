package com.vboss.okline.ui.scanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.zxing.Result;
import com.google.zxing.client.result.ParsedResult;
import com.vboss.okline.R;
import com.vboss.okline.zxing.OnScannerCompletionListener;
import com.vboss.okline.zxing.ScannerView;

import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/5/3 8:43 <br/>
 * Summary  : 二维码扫描界面
 */

public class QRCodeActivity extends AppCompatActivity implements OnScannerCompletionListener {
    private static final String TAG = QRCodeActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ASK_CAMERA = 0x011;

    ScannerView scannerView;
    ImageButton imageBack;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        scannerView = (ScannerView) findViewById(R.id.scannerView);
        imageBack = (ImageButton) findViewById(R.id.iv_back);
        //request camera permission
        requestPermission();
        //init scannerView configuration
        initScannerView();
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();
            }
        });
    }

    /**
     * init ScannerView config
     */
    private void initScannerView() {
        scannerView.setLaserFrameTopMargin(120);//扫描框与屏幕上方距离
        scannerView.setLaserFrameSize(250, 250);//扫描框大小
        scannerView.setLaserFrameCornerLength(25);//设置4角长度
        scannerView.setLaserLineHeight(5);//设置扫描线高度
        scannerView.setDrawText(getResources().getString(R.string.hint_scanner_bt),
                16, Color.WHITE, true, 30);  //设置文字提示
        scannerView.setLaserLineResId(R.drawable.wx_scan_line);//线图
        /*mScannerView.setLaserGridLineResId(R.drawable.zfb_grid_scan_line);//网格图
        mScannerView.setLaserFrameBoundColor(0xFF26CEFF);//支付宝颜色*/
        scannerView.setOnScannerCompletionListener(this);
    }

    @Override
    protected void onResume() {
        scannerView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        scannerView.onPause();
        super.onPause();
    }

    private void restartPreviewAfterDelay(long delayMS) {
        scannerView.restartPreviewAfterDelay(delayMS);
    }


    @Override
    public void OnScannerCompletion(Result rawResult, ParsedResult parsedResult, Bitmap barcode) {
        // TODO: 2017/5/3  扫描结果处理
        Timber.tag(TAG).i("result %s \n parsedResult %s", rawResult == null ? "" : rawResult.toString(),
                parsedResult == null ? "" : parsedResult.toString());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 200);
        restartPreviewAfterDelay(1000);
    }

    /***
     * Added by 2017-05-03
     * Author wangshuai
     * Desc request permission
     */
    private void requestPermission() {
        Timber.tag(TAG).i("request permission ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Timber.tag(TAG).i("Build Version Code %s", Build.VERSION.SDK_INT);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CAMERA);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Timber.tag(TAG).i("requestCode %s", requestCode);
        if (requestCode == REQUEST_CODE_ASK_CAMERA) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Timber.tag(TAG).i("request permission success");
                } else {
                    Timber.tag(TAG).i("request permission fail");
                    finish();
                }
            }
        }
    }
}
