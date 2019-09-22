package com.vboss.okline.view.widget;

import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by luoxiuxiu
 * 2017/4/27
 * description: 自定义的相机
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback {


    private SurfaceHolder surfaceHolder = null;
    private Camera shoot;

    public MySurfaceView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }



    /**
     * SurfaceView从屏幕上移除时，Surface也随即被销毁。通过该方法。可以通知Surface的客户端停止使用Surface
     * 。
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("MySurfaceView", "Surface Destroyed  "+ shoot);
         if (shoot != null) {
             shoot.setPreviewCallback((Camera.PreviewCallback)null);
             shoot.stopPreview();
             shoot.release();
             shoot = null;
        }
    }

    /**
     * 包含SurfaceView的视图层级结构被放到屏幕上时调用该方法。这里也是Surface与其客户端进行关联的地方。
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (shoot != null) {
            try {
                shoot.setPreviewDisplay(holder);
            } catch (Exception e) {
                if (this.shoot != null) {
                    this.shoot.release();
                    this.shoot = null;
                }
                e.printStackTrace();
            }
        }
    }

    /**
     * Surface首次显示在屏幕上时调用该方法。通过传入的参数，可以知道Surface的像素格式以及它的宽度和高度。该方法内可以通知
     * Surface的客户端，有多大的绘制区域可以使用。
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {
        if (shoot == null)
            return;

        Camera.Parameters parameters = shoot.getParameters();
        //设置相机预览尺寸
        Camera.Size size = getBestSupportedSize(
                parameters.getSupportedPreviewSizes(), width, height);
        parameters.setPreviewSize(size.width, size.height);
        //设置图片尺寸
        size = getBestSupportedSize(parameters.getSupportedPictureSizes(), width, height);
        parameters.setPictureSize(size.width, size.height);
        shoot.setParameters(parameters);
        try {
            shoot.startPreview();
        } catch (Exception e) {
            /**
             * 启动失败时，我们通过异常控制机制释放了相机资源。任何时候，打开相机并完成任务后，必须记得及时释放它，
             * 即使是在发生异常时。
             */
            e.printStackTrace();
            shoot.release();
            shoot = null;
        }
    }


    /**
     * 找出尺寸列表中合适的尺寸。
     */
    private Camera.Size getBestSupportedSize(List<Camera.Size> sizes, int width, int height) {
        Camera.Size bestSize = null;
        for (Camera.Size size : sizes) {
            if (size.width == 640 && size.height == 480) {
                bestSize = size;
                break;
            }
            if (bestSize == null) {
                bestSize = sizes.get(sizes.size() / 2);
            }
        }

        return bestSize;
    }


    public void setCamera(Camera camera) {
        this.shoot = camera;
    }
}
