package com.okline.vboss.assistant.ui.notice;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;

import com.okline.vboss.assistant.R;

import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/6/9 9:54 <br/>
 * Summary  : card notice helper
 */

public class CardHelper {
    private static final String TAG = CardHelper.class.getSimpleName();

    //card payment or consume
    public static final int CARD_CONSUME = 0x001;
    //card recharge success
    public static final int CARD_RECHARGE_SUCCESS = 0x002;
    //card recharge fail
    public static final int CARD_RECHARGE_FAIL = 0x003;
    //card download success
    public static final int CARD_DOWNLOAD_SUCCESS = 0x004;
    //card download fail because blueTooth connected fail
    public static final int CARD_DOWNLOAD_FAIL_BLUETOOTH = 0x005;
    //card download fail because OCard not enough memory
    public static final int CARD_DOWNLOAD_FAIL_OCARD = 0x006;
    //card info
    public static final int CARD_INFO = 0x0016;
    //card recharge info
    public static final int RECHARGE_INFO = 0x0017;
    //card recharge discount active
    public static final int RECHARGE_DISCOUNT_ACTIVE = 0x0018;

    /**
     * card operate type
     * {@link CardHelper#CARD_CONSUME}
     * {@link CardHelper#CARD_RECHARGE_SUCCESS}
     * {@link CardHelper#CARD_RECHARGE_FAIL}
     * {@link CardHelper#CARD_DOWNLOAD_SUCCESS}
     * {@link CardHelper#CARD_DOWNLOAD_FAIL_BLUETOOTH}
     * {@link CardHelper#CARD_DOWNLOAD_FAIL_OCARD}
     */
    public static final String KEY_OPERATE = "operate";

    /**
     * card download
     * {@link com.okline.vboss.assistant.net.CardEntity}
     */
    public static final String KEY_CARD = "card";

    /**
     * card recharge info
     * {@link com.okline.vboss.assistant.net.RechargeEntity}
     */
    public static final String KEY_RECHARGE = "recharge";

    /**
     * SharedPreferences name
     */
    private static final String SP_NAME = "sp_card_notice";

    /**
     * key
     */
    static final String KEY_MEDIA_STATE = "media_state";

    /**
     * RxBus Event key
     */
    public static final String EVENT_REFRESH = "refresh";

    private SharedPreferences sp;

    private static CardHelper instance;
    private MediaPlayer mediaPlayer;

    private Context mContext;

    /**
     * singleton
     *
     * @param context Context
     * @return CardHelper
     */
    public static CardHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (CardHelper.class) {
                if (instance == null) {
                    instance = new CardHelper(context);
                }
            }
        }
        return instance;
    }

    private CardHelper(Context context) {
        this.mContext = context.getApplicationContext();
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    /**
     * init mediaPlayer
     *
     * @param context Context
     */
    void initMediaPlayer(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.success);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume(1.0f, 1.0f);
        Timber.tag(TAG).i("init mediaPlayer");
    }

    /**
     * media player
     */
    void playMedia() {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    /**
     * release media
     */
    private void releaseMedia() {
        if (mediaPlayer != null) {
            Timber.tag(TAG).i("mediaPlayer release ");
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    /**
     * save media mode, silent or voice
     *
     * @param silent boolean
     */
    void setSilentMode(boolean silent) {
        sp.edit().putBoolean(KEY_MEDIA_STATE, silent).apply();
    }

    /**
     * get media mode，silent or voice
     *
     * @return boolean
     */
    boolean getSilentMode() {
        return sp.getBoolean(KEY_MEDIA_STATE, false);
    }


    void destroy() {
        releaseMedia();
    }
}
