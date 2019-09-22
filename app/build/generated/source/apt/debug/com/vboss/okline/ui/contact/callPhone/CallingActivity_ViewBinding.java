// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.callPhone;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CallingActivity_ViewBinding<T extends CallingActivity> implements Unbinder {
  protected T target;

  private View view2131756295;

  private View view2131756297;

  private View view2131756299;

  private View view2131756305;

  private View view2131756303;

  private View view2131756301;

  private View view2131756309;

  private View view2131755400;

  @UiThread
  public CallingActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.safeView = Utils.findRequiredView(source, R.id.safeView, "field 'safeView'");
    target.avatarImg = Utils.findRequiredViewAsType(source, R.id.avatarImg, "field 'avatarImg'", SimpleDraweeView.class);
    target.relationStateImg = Utils.findRequiredViewAsType(source, R.id.relationStateImg, "field 'relationStateImg'", ImageView.class);
    target.chronometerView = Utils.findRequiredViewAsType(source, R.id.chronometerView, "field 'chronometerView'", Chronometer.class);
    view = Utils.findRequiredView(source, R.id.silenceBtn, "field 'silenceBtn' and method 'onClick'");
    target.silenceBtn = Utils.castView(view, R.id.silenceBtn, "field 'silenceBtn'", ImageView.class);
    view2131756295 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.keyboardBtn, "field 'keyboardBtn' and method 'onClick'");
    target.keyboardBtn = Utils.castView(view, R.id.keyboardBtn, "field 'keyboardBtn'", ImageView.class);
    view2131756297 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.handFreeBtn, "field 'handFreeBtn' and method 'onClick'");
    target.handFreeBtn = Utils.castView(view, R.id.handFreeBtn, "field 'handFreeBtn'", ImageView.class);
    view2131756299 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.answerContainer = Utils.findRequiredView(source, R.id.answerContainer, "field 'answerContainer'");
    view = Utils.findRequiredView(source, R.id.cardBtn, "field 'cardBtn' and method 'onClick'");
    target.cardBtn = Utils.castView(view, R.id.cardBtn, "field 'cardBtn'", ImageView.class);
    view2131756305 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.minimizeBtn, "field 'minimizeBtn' and method 'onClick'");
    target.minimizeBtn = Utils.castView(view, R.id.minimizeBtn, "field 'minimizeBtn'", ImageView.class);
    view2131756303 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.recordBtn, "field 'recordBtn' and method 'onClick'");
    target.recordBtn = Utils.castView(view, R.id.recordBtn, "field 'recordBtn'", ImageView.class);
    view2131756301 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.hangupBtn, "field 'hangupBtn' and method 'onClick'");
    target.hangupBtn = Utils.castView(view, R.id.hangupBtn, "field 'hangupBtn'", ImageView.class);
    view2131756309 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.answerBtn, "field 'answerBtn' and method 'onClick'");
    target.answerBtn = Utils.castView(view, R.id.answerBtn, "field 'answerBtn'", ImageView.class);
    view2131755400 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.realnameView = Utils.findRequiredViewAsType(source, R.id.realnameView, "field 'realnameView'", TextView.class);
    target.nicknameView = Utils.findRequiredViewAsType(source, R.id.nicknameView, "field 'nicknameView'", TextView.class);
    target.statusTextView = Utils.findRequiredViewAsType(source, R.id.statusTextView, "field 'statusTextView'", TextView.class);
    target.phoneNumView = Utils.findRequiredViewAsType(source, R.id.phoneNumView, "field 'phoneNumView'", TextView.class);
    target.recordTV = Utils.findRequiredViewAsType(source, R.id.recordTV, "field 'recordTV'", TextView.class);
    target.addTextView = Utils.findRequiredViewAsType(source, R.id.addTextView, "field 'addTextView'", TextView.class);
    target.callingSilenceText = Utils.findRequiredViewAsType(source, R.id.calling_silence_text, "field 'callingSilenceText'", TextView.class);
    target.callingCallText = Utils.findRequiredViewAsType(source, R.id.calling_call_text, "field 'callingCallText'", TextView.class);
    target.callingKeyboardText = Utils.findRequiredViewAsType(source, R.id.calling_keyboard_text, "field 'callingKeyboardText'", TextView.class);
    target.callingHandfreeText = Utils.findRequiredViewAsType(source, R.id.calling_handfree_text, "field 'callingHandfreeText'", TextView.class);
    target.callingMinimizeText = Utils.findRequiredViewAsType(source, R.id.calling_minimize_text, "field 'callingMinimizeText'", TextView.class);
    target.callingHangupText = Utils.findRequiredViewAsType(source, R.id.calling_hangup_text, "field 'callingHangupText'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.safeView = null;
    target.avatarImg = null;
    target.relationStateImg = null;
    target.chronometerView = null;
    target.silenceBtn = null;
    target.keyboardBtn = null;
    target.handFreeBtn = null;
    target.answerContainer = null;
    target.cardBtn = null;
    target.minimizeBtn = null;
    target.recordBtn = null;
    target.hangupBtn = null;
    target.answerBtn = null;
    target.realnameView = null;
    target.nicknameView = null;
    target.statusTextView = null;
    target.phoneNumView = null;
    target.recordTV = null;
    target.addTextView = null;
    target.callingSilenceText = null;
    target.callingCallText = null;
    target.callingKeyboardText = null;
    target.callingHandfreeText = null;
    target.callingMinimizeText = null;
    target.callingHangupText = null;

    view2131756295.setOnClickListener(null);
    view2131756295 = null;
    view2131756297.setOnClickListener(null);
    view2131756297 = null;
    view2131756299.setOnClickListener(null);
    view2131756299 = null;
    view2131756305.setOnClickListener(null);
    view2131756305 = null;
    view2131756303.setOnClickListener(null);
    view2131756303 = null;
    view2131756301.setOnClickListener(null);
    view2131756301 = null;
    view2131756309.setOnClickListener(null);
    view2131756309 = null;
    view2131755400.setOnClickListener(null);
    view2131755400 = null;

    this.target = null;
  }
}
