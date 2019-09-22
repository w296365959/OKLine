package com.hyphenate.easeui.widget;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.R;
import com.hyphenate.util.EMLog;

import timber.log.Timber;

/**
 * primary menu
 *
 */
public class EaseChatPrimaryMenu extends EaseChatPrimaryMenuBase implements OnClickListener{
    private EditText editText;
//    private View buttonSetModeKeyboard;
    private LinearLayout edittext_layout;
    private ImageView ivSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    private ImageView faceNormal;
//    private ImageView faceChecked;
    private ImageView buttonMore;
    private ImageView btnCamer;
    private ImageView btnPhoto;
    private ImageView btnFile;
    private TextView tvSpeakDown;
    private boolean ctrlPress = false;
    private boolean isSelect = false;
    private Context context;

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(context, attrs);
    }

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public EaseChatPrimaryMenu(Context context) {
        super(context);
        this.context = context;
        init(context, null);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.ease_widget_chat_primary_menu, this);
        editText = (EditText) findViewById(R.id.et_sendmessage);
//        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);//
        edittext_layout = (LinearLayout) findViewById(R.id.edittext_layout);
        ivSetModeVoice = (ImageView)findViewById(R.id.iv_set_mode_voice);
        buttonSend = findViewById(R.id.tv_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        tvSpeakDown = (TextView)findViewById(R.id.tv_speak_down);
        faceNormal = (ImageView) findViewById(R.id.iv_face_normal);
//        faceChecked = (ImageView) findViewById(R.id.iv_face_checked);
        RelativeLayout faceLayout = (RelativeLayout) findViewById(R.id.rl_face);
        buttonMore = (ImageView) findViewById(R.id.iv_add);
        btnCamer = (ImageView) findViewById(R.id.iv_set_camaer);
        btnFile = (ImageView) findViewById(R.id.iv_set_file);
        btnPhoto = (ImageView) findViewById(R.id.iv_set_photo);
//        edittext_layout.setBackgroundResource(R.drawable.shape_bg_edittext_normal);

        buttonSend.setOnClickListener(this);
//        buttonSetModeKeyboard.setOnClickListener(this);
        ivSetModeVoice.setOnClickListener(this);
        buttonMore.setOnClickListener(this);
        btnPhoto.setOnClickListener(this);
        btnFile.setOnClickListener(this);
        btnCamer.setOnClickListener(this);
        faceLayout.setOnClickListener(this);
        editText.setOnClickListener(this);
        editText.requestFocus();

        //modity luoxx 170609 群聊屏蔽绿色加号
        int chatType = getChatType();
        if(chatType == EaseConstant.CHATTYPE_GROUP){
            buttonMore.setVisibility(GONE);
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                buttonSend.setBackgroundResource(R.drawable.shape_gray);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!TextUtils.isEmpty(s.toString()))
                    buttonSend.setBackgroundResource(R.drawable.chatting_set_send_img);
                else
                    buttonSend.setBackgroundResource(R.drawable.shape_gray);
            }
        });

        editText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {
//                    edittext_layout.setBackgroundResource(R.drawable.shape_bg_edittext_focused);
//                } else {
//                    edittext_layout.setBackgroundResource(R.drawable.shape_bg_edittext_normal);
//                }

            }
        });

        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EMLog.d("key", "keyCode:" + keyCode + " action:" + event.getAction());

                // test on Mac virtual machine: ctrl map to KEYCODE_UNKNOWN
                if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        ctrlPress = true;
                    } else if (event.getAction() == KeyEvent.ACTION_UP) {
                        ctrlPress = false;
                    }
                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                EMLog.d("key", "keyCode:" + event.getKeyCode() + " action" + event.getAction() + " ctrl:" + ctrlPress);
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                ctrlPress == true)) {
                    String s = editText.getText().toString();
                    editText.setText("");
                    buttonSend.setBackgroundResource(R.drawable.shape_gray);
                    listener.onSendBtnClicked(s);
                    return true;
                }
                else{
                    return false;
                }
            }
        });


        buttonPressToSpeak.setOnTouchListener(new OnTouchListener() {

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //add luoxx 170626 添加录音权限
                checkPermiss();
                tvSpeakDown.setText(context.getString(R.string.talk_end_text));
                switch (event.getAction()) {//第一个触摸点
                    case MotionEvent.ACTION_UP:    // 抬起 = 1
                        tvSpeakDown.setText(context.getString(R.string.button_pushtotalk));
                        break;
                    default:break;
                }
                if(listener != null){
                    return listener.onPressToSpeakBtnTouch(v, event);
                }
                return false;
            }

        });
    }

    /**
     * set recorder view when speak icon is touched
     * @param voiceRecorderView
     */
    public void setPressToSpeakRecorderView(EaseVoiceRecorderView voiceRecorderView){
        EaseVoiceRecorderView voiceRecorderView1 = voiceRecorderView;
    }

    /**
     * append emoji icon to editText
     * @param emojiContent
     */
    public void onEmojiconInputEvent(CharSequence emojiContent){
        editText.append(emojiContent);
    }

    /**
     * delete emojicon
     */
    public void onEmojiconDeleteEvent(){
        if (!TextUtils.isEmpty(editText.getText())) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            editText.dispatchKeyEvent(event);
        }
    }

    /**
     * on clicked event
     * @param view
     */
    @Override
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.tv_send) {
            if(listener != null){
                String s = editText.getText().toString();
                editText.setText("");
                buttonSend.setBackgroundResource(R.drawable.shape_gray);
                listener.onSendBtnClicked(s);
            }
        } else if (id == R.id.iv_set_mode_voice) {//语音
            editText.setText("");
            buttonSend.setBackgroundResource(R.drawable.shape_gray);
            if(isSelect){
                ivSetModeVoice.setImageResource(R.drawable.chatting_set_voice_img);
                isSelect = false;

                edittext_layout.setVisibility(View.VISIBLE);
//        buttonSetModeKeyboard.setVisibility(View.GONE);
                // mEditTextContent.setVisibility(View.VISIBLE);
                editText.requestFocus();
                 buttonSend.setVisibility(View.VISIBLE);
                buttonPressToSpeak.setVisibility(View.GONE);

//                setModeKeyboard();
                showNormalFaceImage();
                if(listener != null)
                    listener.onToggleVoiceBtnClicked();
            }else {
                ivSetModeVoice.setImageResource(R.drawable.ease_chatting_setmode_keyboard_btn);
                tvSpeakDown.setText(context.getString(R.string.button_pushtotalk));
                isSelect = true;
                setModeVoice();
                showNormalFaceImage();
                if(listener != null)
                    listener.onToggleVoiceBtnClicked();
            }

        }
//        else if (id == R.id.btn_set_mode_keyboard) {
//            setModeKeyboard();
//            showNormalFaceImage();
//            if(listener != null)
//                listener.onToggleVoiceBtnClicked();
//        }
        else if (id == R.id.iv_add) {
//            buttonSetModeKeyboard.setVisibility(View.GONE);
            edittext_layout.setVisibility(View.VISIBLE);
            buttonPressToSpeak.setVisibility(View.GONE);
            showNormalFaceImage();
            if(listener != null)
                listener.onToggleExtendClicked();
        } else if (id == R.id.et_sendmessage) {
//            edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
//            faceNormal.setVisibility(View.VISIBLE);
//            faceChecked.setVisibility(View.INVISIBLE);
            if(listener != null)
                listener.onEditTextClicked();
        } else if (id == R.id.rl_face) {//表情
//            setModeKeyboard();
//            toggleFaceImage();
            isSelect = false;
            ivSetModeVoice.setImageResource(R.drawable.chatting_set_voice_img);
            edittext_layout.setVisibility(View.VISIBLE);
            buttonPressToSpeak.setVisibility(View.GONE);
            editText.requestFocus();
            if(listener != null){
                listener.onToggleEmojiconClicked();
            }
        }
        else if(id == R.id.iv_set_photo){
            Timber.tag("").e("..........");
            chatPrimaryClick.onClick(activity.getString(R.string.attach_picture));
        }
     else if(id == R.id.iv_set_file){
            chatPrimaryClick.onClick(activity.getString(R.string.attach_location));
        }else if (id == R.id.iv_set_camaer){
            chatPrimaryClick.onClick(activity.getString(R.string.attach_take_pic));
        }
        else {
            return;
        }
    }


    /**
     * show voice icon when speak bar is touched
     *
     */
    protected void setModeVoice() {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
//        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
//        buttonSend.setVisibility(View.GONE);
//        buttonMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        faceNormal.setVisibility(View.VISIBLE);
//        faceChecked.setVisibility(View.INVISIBLE);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermiss() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                ) {
            ((Activity)context).requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            return;
        }
    }

    /**
     * show keyboard
     */
    protected void setModeKeyboard() {
//        edittext_layout.setVisibility(View.VISIBLE);
//        buttonSetModeKeyboard.setVisibility(View.GONE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        editText.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);

    }


    protected void toggleFaceImage(){
        if(faceNormal.getVisibility() == View.VISIBLE){
            showSelectedFaceImage();
        }else{
            showNormalFaceImage();
        }
    }

    private void showNormalFaceImage(){
        faceNormal.setVisibility(View.VISIBLE);
//        faceChecked.setVisibility(View.INVISIBLE);
    }

    private void showSelectedFaceImage(){
        faceNormal.setVisibility(View.INVISIBLE);
//        faceChecked.setVisibility(View.VISIBLE);
    }


    @Override
    public void onExtendMenuContainerHide() {
        showNormalFaceImage();
    }

    @Override
    public void onTextInsert(CharSequence text) {
        int start = editText.getSelectionStart();
        Editable editable = editText.getEditableText();
        editable.insert(start, text);
        setModeKeyboard();
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

}
