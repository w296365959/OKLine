// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.opay;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OPayFragment_ViewBinding<T extends OPayFragment> implements Unbinder {
  protected T target;

  @UiThread
  public OPayFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.ivIcon = Utils.findRequiredViewAsType(source, R.id.iv_icon, "field 'ivIcon'", ImageView.class);
    target.tvPaypopcancel = Utils.findRequiredViewAsType(source, R.id.tv_paypopcancel, "field 'tvPaypopcancel'", TextView.class);
    target.tvSelectedAccountName = Utils.findRequiredViewAsType(source, R.id.tv_selectedAccountName, "field 'tvSelectedAccountName'", TextView.class);
    target.rlPayableaccount = Utils.findRequiredViewAsType(source, R.id.rl_payableaccount, "field 'rlPayableaccount'", RelativeLayout.class);
    target.tvAmout = Utils.findRequiredViewAsType(source, R.id.tv_amout, "field 'tvAmout'", TextView.class);
    target.btPayconfirm = Utils.findRequiredViewAsType(source, R.id.bt_payconfirm, "field 'btPayconfirm'", Button.class);
    target.tvPayabledesc = Utils.findRequiredViewAsType(source, R.id.tv_payabledesc, "field 'tvPayabledesc'", TextView.class);
    target.rlPayabledesc = Utils.findRequiredViewAsType(source, R.id.rl_payabledesc, "field 'rlPayabledesc'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivIcon = null;
    target.tvPaypopcancel = null;
    target.tvSelectedAccountName = null;
    target.rlPayableaccount = null;
    target.tvAmout = null;
    target.btPayconfirm = null;
    target.tvPayabledesc = null;
    target.rlPayabledesc = null;

    this.target = null;
  }
}
