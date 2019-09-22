// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.opay;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OPayPasswordFragment_ViewBinding<T extends OPayPasswordFragment> implements Unbinder {
  protected T target;

  @UiThread
  public OPayPasswordFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.ivIcon = Utils.findRequiredViewAsType(source, R.id.iv_icon, "field 'ivIcon'", ImageView.class);
    target.tvPaypophint = Utils.findRequiredViewAsType(source, R.id.tv_paypophint, "field 'tvPaypophint'", TextView.class);
    target.tvOpaypwdcancel = Utils.findRequiredViewAsType(source, R.id.tv_opaypwdcancel, "field 'tvOpaypwdcancel'", TextView.class);
    target.pswDot1 = Utils.findRequiredViewAsType(source, R.id.psw_dot1, "field 'pswDot1'", ImageView.class);
    target.pswDot2 = Utils.findRequiredViewAsType(source, R.id.psw_dot2, "field 'pswDot2'", ImageView.class);
    target.pswDot3 = Utils.findRequiredViewAsType(source, R.id.psw_dot3, "field 'pswDot3'", ImageView.class);
    target.pswDot4 = Utils.findRequiredViewAsType(source, R.id.psw_dot4, "field 'pswDot4'", ImageView.class);
    target.pswDot5 = Utils.findRequiredViewAsType(source, R.id.psw_dot5, "field 'pswDot5'", ImageView.class);
    target.pswDot6 = Utils.findRequiredViewAsType(source, R.id.psw_dot6, "field 'pswDot6'", ImageView.class);
    target.llPwdface = Utils.findRequiredViewAsType(source, R.id.ll_pwdface, "field 'llPwdface'", LinearLayout.class);
    target.tvForgetpwd = Utils.findRequiredViewAsType(source, R.id.tv_forgetpwd, "field 'tvForgetpwd'", TextView.class);
    target.btnPwdkey1 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey1, "field 'btnPwdkey1'", TextView.class);
    target.btnPwdkey2 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey2, "field 'btnPwdkey2'", TextView.class);
    target.btnPwdkey3 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey3, "field 'btnPwdkey3'", TextView.class);
    target.btnPwdkey4 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey4, "field 'btnPwdkey4'", TextView.class);
    target.btnPwdkey5 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey5, "field 'btnPwdkey5'", TextView.class);
    target.btnPwdkey6 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey6, "field 'btnPwdkey6'", TextView.class);
    target.btnPwdkey7 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey7, "field 'btnPwdkey7'", TextView.class);
    target.btnPwdkey8 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey8, "field 'btnPwdkey8'", TextView.class);
    target.btnPwdkey9 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey9, "field 'btnPwdkey9'", TextView.class);
    target.btnPwdkeyblank = Utils.findRequiredViewAsType(source, R.id.btn_pwdkeyblank, "field 'btnPwdkeyblank'", TextView.class);
    target.btnPwdkey0 = Utils.findRequiredViewAsType(source, R.id.btn_pwdkey0, "field 'btnPwdkey0'", TextView.class);
    target.btnPwdkeydel = Utils.findRequiredViewAsType(source, R.id.btn_pwdkeydel, "field 'btnPwdkeydel'", RelativeLayout.class);
    target.gvKeyboard = Utils.findRequiredViewAsType(source, R.id.gv_keyboard, "field 'gvKeyboard'", GridLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivIcon = null;
    target.tvPaypophint = null;
    target.tvOpaypwdcancel = null;
    target.pswDot1 = null;
    target.pswDot2 = null;
    target.pswDot3 = null;
    target.pswDot4 = null;
    target.pswDot5 = null;
    target.pswDot6 = null;
    target.llPwdface = null;
    target.tvForgetpwd = null;
    target.btnPwdkey1 = null;
    target.btnPwdkey2 = null;
    target.btnPwdkey3 = null;
    target.btnPwdkey4 = null;
    target.btnPwdkey5 = null;
    target.btnPwdkey6 = null;
    target.btnPwdkey7 = null;
    target.btnPwdkey8 = null;
    target.btnPwdkey9 = null;
    target.btnPwdkeyblank = null;
    target.btnPwdkey0 = null;
    target.btnPwdkeydel = null;
    target.gvKeyboard = null;

    this.target = null;
  }
}
