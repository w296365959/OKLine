///////////////////////////////////////////////////////////////////////////////
//  VoiceEngine�ӿ���
//	create by jarvis, 2014-01-16
//  VoiceEngineʹ������Ĭ�ϲ���
//  VAD��Disable
//	EC: AECM
//  AGC: AdaptiveDigital
//	NS: Disable
///////////////////////////////////////////////////////////////////////////////
package org.jarvis.liu;

import android.content.Context;
import android.media.AudioManager;

public class VoiceEngineConfig {
    static {
        System.loadLibrary("VoiceEngine");
    }

    public static void init(Context context) {
        _context = context.getApplicationContext();
        sharedInstance();
    }

    public static void release() {
        sharedInstance().destroyNative();
    }

    public static VoiceEngineConfig sharedInstance() {
        if (_inst == null) {
            _inst = new VoiceEngineConfig(_context);
        }
        return _inst;
    }

    public void setTrackStream(int stream) {
        _stream = stream;
    }

    public int getTrackStream() {
        return _stream;
    }

    private static VoiceEngineConfig _inst = null;
    private static Context _context = null;

    private int _stream = AudioManager.STREAM_VOICE_CALL;

    private VoiceEngineConfig(Context context) {
        initNative(_context);
    }

    private native void initNative(Context context);

    private native void destroyNative();
}