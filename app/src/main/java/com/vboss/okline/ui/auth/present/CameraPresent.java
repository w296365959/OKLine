package com.vboss.okline.ui.auth.present;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;

import com.okline.vboss.http.OLException;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.entities.User;
import com.vboss.okline.data.local.AppConfig;
import com.vboss.okline.ui.auth.CamerSelfActivity;
import com.vboss.okline.utils.AppUtil;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.ToastUtil;

import cn.jpush.android.api.JPushInterface;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: Mengyupeng<br/>
 * Email:  mengyupeng@okline.cn</br>
 * Date : $(DATE) </br>
 * Summary:  自定义相机页面的present
 */

public class CameraPresent implements RzContact.IPresenter {


    private static final String TAG = CameraPresent.class.getSimpleName();
    Activity mContext;
    private UserRepository user;
    private RzContact.IResultView contact;

    public CameraPresent(Activity mContext) {
        this.mContext = mContext;
        user = UserRepository.getInstance(mContext);
    }

    public CameraPresent(Activity mContext, RzContact.IResultView contact) {
        this.mContext = mContext;
        this.contact = contact;
        user = UserRepository.getInstance(mContext);

    }

    @Override
    public void login(String imei, String imsi) {
        user.login(mContext, imei, imsi).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<User>(TAG) {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof OLException) {
                            int code = ((OLException) throwable).getCode();
                            if (code != 0) {
                                contact.setError(((OLException) throwable).getMessage(), code);
                            }
                        }
                    }


                    @Override
                    public void onNext(User user) {
                        super.onNext(user);
                        contact.setResult(user);
                        //add by luoxx 170629 登录添加极光
                        CameraPresent.this.user.setJPushRegistrationId(user.getOlNo(), JPushInterface.getRegistrationID(mContext));
                    }
                });
    }

    @Override
    public void loginOrRegisterUser(int creditType,String idCardNo, String realName,  String imei, String imsi) {
        user.resetOrRegisterUser(mContext, creditType,idCardNo, realName, imei, imsi).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DefaultSubscribe<User>(TAG) {
                    @Override
                    public void onCompleted() {
                        //用户注册成功后，上传头像
                        String url = AppConfig.IMAGE_CACHE_DIR + AppConfig.IMAGE_ID_AND_PERSON;
                        user.updateAvatar(url).subscribe(new DefaultSubscribe<String>(TAG) {
                            @Override
                            public void onNext(String avatar) {
                                Timber.tag(TAG).i("Set user avatar %s", avatar);
                                user.getUser().setAvatar(avatar);
                            }
                        });
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        if (throwable instanceof OLException) {
                            int code = ((OLException) throwable).getCode();
                            if (code != 0) {
                                contact.setError(((OLException) throwable).getMessage(), code);
                            }
                        }

                    }


                    @Override
                    public void onNext(User user) {
                        super.onNext(user);
                        contact.setResult(user);
                        //Added by shihaijun to set JIGUANG RegisterationId
                        CameraPresent.this.user.setJPushRegistrationId(user.getOlNo(), JPushInterface.getRegistrationID(mContext));
                        //End for modification
                    }
                });
    }

    @Override
    public void savePicDatat(byte[] data) {
        try {
            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bmp != null) {
                Bitmap bitmap = AppUtil.rotateBitmapByDegree(bmp, 270);
                AppUtil.saveBmpToLoalSdCard(bitmap, AppConfig.IMAGE_CACHE_DIR, "test.jpg");
                if (mContext instanceof CamerSelfActivity) {
                    bitmap = ((CamerSelfActivity) mContext).cutImage(bitmap);
                }
                AppUtil.saveBmpToLoalSdCard(bitmap, AppConfig.IMAGE_CACHE_DIR, AppConfig.IMAGE_ID_AND_PERSON);
                bitmap.recycle();

//                String url = AppConfig.IMAGE_CACHE_DIR + AppConfig.IMAGE_ID_AND_PERSON;
//                int degree = AppUtil.readPictureDegree(url);
//                if(degree>0){
//                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Camera getCamera() {
        return getCameraInstance();
    }


    //获取相机实例
    private Camera getCameraInstance() {
        int CammeraIndex = findFrontCamera();
        try {
            Camera camera = Camera.open(CammeraIndex);
            camera.setDisplayOrientation(90);
            return camera;
        } catch (Exception e) {
            e.printStackTrace();
            //Toast.makeText(CamerSelfActivity.this,e.getMessage()+"",Toast.LENGTH_SHORT).show();
        }
        return null;
    }


    //获取前置摄像头索引
    private int findFrontCamera() {
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        // 自拍 ==》 判断当前有几个摄像头，前置优先
        if (cameraCount >= 2) {
            for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                Camera.getCameraInfo(camIdx, cameraInfo); // get camerainfo
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//前置摄像头
                    return camIdx;
                }
            }
        } else {
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//后置摄像头
                return cameraInfo.facing;
            }
        }
        return -1;
    }

}
