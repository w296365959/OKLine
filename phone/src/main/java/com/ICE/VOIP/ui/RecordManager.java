package com.ICE.VOIP.ui;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/18 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class RecordManager {
    private static final String TAG = RecordManager.class.getSimpleName();
    private String fileName;
    private MediaRecorder mRecorder = new MediaRecorder();
    private static RecordManager instance = new RecordManager();
    private boolean isRecording = false;
    public static SimpleDateFormat format = new SimpleDateFormat(
            "yyyy-MM-dd HH:mm:ss");

    private RecordManager() {
    }

    public static RecordManager getInstance() {
        return instance;
    }


    /**
     * 拿不到 CAPTURE_AUDIO_OUTPUT 权限只能录制mic的声音
     *
     * @param context
     */
    public void startRecord(Context context) {
        if (isRecording) {
            return;
        }
        if (mRecorder == null) {
            mRecorder = new MediaRecorder();
        }
        // mRecorder.reset();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        mRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        File file = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        File file = context.getExternalCacheDir();
        fileName = new File(file.getPath(), "record_" + format.format(new Date()) + ".3gp").getAbsolutePath();
        Log.d(TAG, "start recording:" + fileName);
        mRecorder.setOutputFile(fileName);
        try {
            mRecorder.prepare();
            mRecorder.start();
            isRecording = true;
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void stopRecord() {
        if (isRecording) {
            try {
             /*   //注册一个用于记录录制时出现的错误的监听器。
                mRecorder.setOnErrorListener(null);
                //注册一个用于记录录制时出现的信息事件。
                mRecorder.setOnInfoListener(null);
                //设置使用哪个SurfaceView来显示视频预览
                mRecorder.setPreviewDisplay(null);*/
                //不写在try里,stop时可能会出错
                mRecorder.stop();
            } catch (Exception e) {
                Log.i(TAG, "Exception " + Log.getStackTraceString(e));
            }

            mRecorder.release();
            mRecorder = null;
            isRecording = false;
            Log.i(TAG, "stopRecord " + fileName);
        }
    }
}
