package com.ICE.VOIP.ui;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.SoundPool;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ophone.vboss.com.phone.R;

public class RingtoneManager implements
        AudioManager.OnAudioFocusChangeListener {
    public static final String RAW_RING = "ring";
    public static final String RAW_RINGBACK = "ringback";
    public static final String RAW_BUSY = "busy";
    public static final String RAW_REFRESHING_SOUND = "refreshing_sound";
    public static final String RAW_RESET_SOUND = "reset_sound";
    public static final String RAW_OFFICE = "office";
    private static final String TAG = "MyRingtoneManager";
    public static int VoiceType = -2;
    private static RingtoneManager instance = new RingtoneManager();
    private Map<String, Integer> toneMap = new HashMap<>();
    private Map<String, Integer> ringMap = new HashMap<>();
    private SoundPool soundPool;
    private int streamId = -1;
    private Context context;
    private Ringtone ringtone;
    private AudioManager audioManager;
    private int ringtoneVolume;
    private int voiceCallVolume;
    private int currentModel = 0;
    private int phoneCallModel = 0;
    private MediaPlayer ringbackPlayer;


    private RingtoneManager() {
    }

    public static synchronized RingtoneManager getInstance() {
        return instance;
    }

    void init(Context context) {
        this.context = context;
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this,
                AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            Log.d(TAG, "could not get audio focus!\n");
        }
        if (VoiceType >= 0) {
            phoneCallModel = VoiceType;
        }
        // 设置音频模式
        currentModel = audioManager.getMode();
        if (currentModel != AudioManager.MODE_NORMAL) {
            audioManager.setMode(AudioManager.MODE_NORMAL);
            currentModel = AudioManager.MODE_NORMAL;
        }
        // boolean muteFlag = audioManager.isMicrophoneMute();
        if (audioManager.isSpeakerphoneOn()) {
            audioManager.setSpeakerphoneOn(false);
        }
        ringbackPlayer = MediaPlayer.create(context, R.raw.ringback);
        initSoundPool();
    }

    private void initSoundPool() {
        toneMap.put(RAW_RING, R.raw.ring);
        toneMap.put(RAW_RINGBACK, R.raw.ringback);
        toneMap.put(RAW_BUSY, R.raw.busy);
        toneMap.put(RAW_REFRESHING_SOUND, R.raw.refreshing_sound);
        toneMap.put(RAW_RESET_SOUND, R.raw.reset_sound);
        toneMap.put(RAW_OFFICE, R.raw.office);
        int maxStreams = toneMap.size() + ringMap.size() + 2;
        soundPool = new SoundPool(maxStreams, AudioManager.STREAM_MUSIC, 0);
        loadTones();
    }

    private void loadTones() {
        for (Iterator<Entry<String, Integer>> iterator = toneMap.entrySet()
                .iterator(); iterator.hasNext(); ) {
            Entry<String, Integer> entry = iterator.next();
            int id = soundPool.load(context, entry.getValue(), 0);
            if (id != 0) {
                // 加载成功
                ringMap.put(entry.getKey(), id);
            }
        }
        toneMap.clear();
    }

    private void unloadTones() {
        for (Iterator<Entry<String, Integer>> iterator = ringMap.entrySet()
                .iterator(); iterator.hasNext(); ) {
            Entry<String, Integer> entry = iterator.next();
            soundPool.unload(entry.getValue());
        }
        ringMap.clear();
    }

    public void playTone(String srtRes) {
        if (ringMap == null || !ringMap.containsKey(srtRes))
            return;
        if (streamId != -1) {
            soundPool.stop(streamId);
        }
        streamId = ringMap.get(srtRes);
        currentModel = audioManager.getMode();
        if (currentModel != AudioManager.MODE_RINGTONE) {
            audioManager.setMode(AudioManager.MODE_RINGTONE);
        }
        streamId = soundPool.play(streamId, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void stopTone() {
        if (streamId != -1) {
            soundPool.stop(streamId);
        }
        streamId = -1;
        if (currentModel != audioManager.getMode()) {
            audioManager.setMode(currentModel);
        }
    }

    /**
     * 回铃音
     *
     * @param ringback
     */
    public void ringback(boolean ringback) {
        if (ringback) {
//            playRing(RAW_RINGBACK, true, -1);
            playRingback();
        } else {
//            stopRing();
            stopRingBack();
        }
    }

    /**
     * 播放回铃 解决华为不能播放的问题
     */
    public void playRingback() {
        try {
            ringbackPlayer.stop();
            ringbackPlayer.prepare();
            ringbackPlayer.setLooping(true);
            ringbackPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopRingBack() {
        ringbackPlayer.stop();
    }

    public void playRing(String srtRes, boolean loopFlag, int loopCount) {
        if (ringMap == null || !ringMap.containsKey(srtRes))
            return;
        if (streamId != -1) {
            soundPool.stop(streamId);
        }
        streamId = ringMap.get(srtRes);
        currentModel = audioManager.getMode();
        if (currentModel != AudioManager.MODE_RINGTONE) {
            audioManager.setMode(AudioManager.MODE_RINGTONE);
        }

        int loop = 0;
        if (loopFlag) {
            loop = loopCount;
        }
        streamId = soundPool.play(streamId, 1.0f, 1.0f, 0, loop, 1.0f);
    }

    public void stopRing() {
        if (streamId != -1) {
            soundPool.stop(streamId);
        }
        streamId = -1;
        if (currentModel != audioManager.getMode()) {
            audioManager.setMode(currentModel);
        }
    }

    public void playRingtone() {
        ringtoneVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
        if (ringtoneVolume < 3) {
            // 设置响应的声音
        }
        if (audioManager.getStreamVolume(AudioManager.STREAM_RING) > 0) {
            // 获取系统响铃设置
            String sUriSipRingtone = Settings.System.DEFAULT_RINGTONE_URI
                    .toString();
            if (!TextUtils.isEmpty(sUriSipRingtone)) {
                ringtone = android.media.RingtoneManager.getRingtone(context,
                        Uri.parse(sUriSipRingtone));
                if (ringtone != null) {
                    currentModel = audioManager.getMode();
                    if (currentModel != AudioManager.MODE_RINGTONE) {
                        audioManager.setMode(AudioManager.MODE_RINGTONE);
                    }
                    ringtone.play();
                }
            }
        }
    }

    /**
     * 回ringtone的播放状态
     *
     * @return
     */
    public boolean isPlayRingtone() {
        if (ringtone != null) {
            if (ringtone.isPlaying()) {
                return true;
            }
        }
        return false;
    }

    public void stopRingtone() {
        // 停止响铃声音
        if (ringtone != null) {
            if (currentModel != audioManager.getMode()) {
                audioManager.setMode(currentModel);
            }
            ringtone.stop();
            ringtone = null;
        }
        // 恢复响应音量设置
    }

    public void phoneTalkInit() {
        System.out.println("phoneCallModel=" + phoneCallModel);
        if (VoiceType >= 0) {
            phoneCallModel = VoiceType;
        }
        currentModel = audioManager.getMode();
        if (currentModel != phoneCallModel) {
            audioManager.setMode(phoneCallModel);
        }
    }

    public void phoneTalkOver() {
        if (currentModel != audioManager.getMode()) {
            audioManager.setMode(currentModel);
        }
    }

    public void systemIncoming() {
        if (soundPool != null)
            soundPool.autoPause();
        audioManager.unloadSoundEffects();
    }

    public void systemStopOver() {
        if (soundPool != null)
            soundPool.autoResume();
    }

    public void setSpeakerphone(boolean open) {
        audioManager.setSpeakerphoneOn(open);
    }

    public boolean isSpeakerphoneOn() {
        return audioManager.isSpeakerphoneOn();
    }

    public boolean isSilence() {
        return audioManager.getRingerMode() == AudioManager.RINGER_MODE_SILENT;
    }

    public void setSilence(boolean open) {
        //fix bug 1402 lzb
        if (open) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        } else {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        }
        //fix bug 1402 lzb
    }

    public void onAudioFocusChange(int focusChange) {
        if (focusChange == AudioManager.AUDIOFOCUS_GAIN) {
            Log.d(TAG, "AudioManager.AUDIOFOCUS_GAIN \n");
            // play();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS) {
            // all audio playback
            Log.d(TAG, "AudioManager.AUDIOFOCUS_LOSS \n");
            // stop();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT) {
            // temporarily lost audio focus, but should receive it back shortly
            Log.d(TAG, "AudioManager.AUDIOFOCUS_LOSS_TRANSIENT \n");
            // pause();
        } else if (focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK) {
            // temporarily lost audio focus, but are allowed to continue to play
            // audio quietly (at a low volume)
            // setVolume(0.1f, 0.1f);
        }
    }

}
