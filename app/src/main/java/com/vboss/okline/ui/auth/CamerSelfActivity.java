package com.vboss.okline.ui.auth;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.vboss.okline.R;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.AppConfig;
import com.vboss.okline.data.local.SPUtils;
import com.vboss.okline.ui.auth.present.CameraPresent;
import com.vboss.okline.ui.auth.present.RzContact;
import com.vboss.okline.utils.AppUtil;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.MySurfaceView;

import timber.log.Timber;

/**
 * Created by luoxiuxiu
 * 2017/4/13
 * description: 自拍
 */
public class CamerSelfActivity extends Activity implements View.OnClickListener{
    Display display;
    FrameLayout fr_sf;
    View avatar_view;
    Button btnTakePhoto;
    Bitmap bmpPic = null;//捕获的手持身份证照片
    CameraPresent camerPresent = null;
    private Camera camera = null;
    private byte[] buffer = null;
    private String imei;  //手机imei
    private String imsi;  //手机imsi
    private static final short REQUEST_PHONE_STATE = 103;
    private boolean isClick = true;
    private MySurfaceView mySurfaceView;

    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            buffer = new byte[data.length];
            buffer = data.clone();
            if (buffer != null) {
                camerPresent.savePicDatat(buffer);
                initData();
            }


        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camer_self);
        initView();
    }


    void initView() {
        camerPresent = new CameraPresent(this);
        fr_sf = (FrameLayout) this.findViewById(R.id.fg_sf);
        btnTakePhoto = (Button) this.findViewById(R.id.take_photo_btn);
        btnTakePhoto.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Timber.i("CamerSelfActivity", "onResume");
        if (camera == null) {
            camera = camerPresent.getCamera();
        }
        WindowManager mManger = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        display = mManger.getDefaultDisplay();
        //必须放在onResume中，不然会出现Home键之后，再回到该APP，黑屏
        mySurfaceView = new MySurfaceView(getApplicationContext());
        mySurfaceView.setCamera(camera);
        fr_sf.addView(mySurfaceView);
        addCamerRengle();
    }

    //添加相机布局

    void addCamerRengle() {
        View viewCamerRengle = getLayoutInflater().inflate(R.layout.layout_camera_rengle, null);
        fr_sf.addView(viewCamerRengle);
        avatar_view = viewCamerRengle.findViewById(R.id.avatar_view);
    }

    public Bitmap cutImage(Bitmap bitmap) {
        double persent = bitmap.getWidth() * 1.0 / fr_sf.getMeasuredWidth();
        double x = avatar_view.getX() * persent;
        double y = avatar_view.getY() * persent;
        double width = avatar_view.getMeasuredWidth() * persent;
        double height = avatar_view.getMeasuredHeight() * persent;
        Timber.d("cutImage : " + x
                + " , " + y
                + " , " + width
                + " , " + height
        );
        return Bitmap.createBitmap(bitmap, (int) x, (int)y, (int) width, (int) height);
    }

    @Override
    public void onClick(View view) {
        try {
            if (view == btnTakePhoto && camera != null && isClick) {
                /**
                 * ShutterCallback回调方法会在相机捕获图像时调用，但此时，图像数据还未处理完成。
                 * 第一个PictureCallback回调方法是在原始图像数据可用时调用，通常来说，是在加工处理原始图像数据且没有存储之前。
                 * 第二个PictureCallback回调方法是在JPEG版本的图像可用时调用。
                 */
                camera.takePicture(null, null, pictureCallback);
                isClick = false;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (bmpPic != null && (!bmpPic.isRecycled())) {
                bmpPic.recycle();
            }
            if (camera != null) {
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (bmpPic != null && (!bmpPic.isRecycled())) {
                bmpPic.recycle();
            }
            if (camera != null) {
                camera.setPreviewCallback((Camera.PreviewCallback) null);
                camera.release();
                camera = null;
                mySurfaceView.setCamera(camera);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化数据
     */
    private void initData() {
        //modify by wangshuai 2017-05-10 permission request
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_PHONE_STATE);
            return;
        }
        imei = AppUtil.getIMEI(this);
        imsi = AppUtil.getIMSI(this);
        Intent intent = new Intent(this, ApproveIDActivity.class);
        intent.putExtra("IMSI", imsi);
        intent.putExtra("IMEI", imei);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case REQUEST_PHONE_STATE:
                    if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                        initData();
                    } else {

                    }
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
        }
    }

}
