// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.auth;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class VerifyFailActivity_ViewBinding<T extends VerifyFailActivity> implements Unbinder {
  protected T target;

  private View view2131755368;

  @UiThread
  public VerifyFailActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btn_again_sumbit, "field 'btnAgain' and method 'onViewClicked'");
    target.btnAgain = Utils.castView(view, R.id.btn_again_sumbit, "field 'btnAgain'", Button.class);
    view2131755368 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvFail = Utils.findRequiredViewAsType(source, R.id.tv_fail, "field 'tvFail'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.btnAgain = null;
    target.tvFail = null;

    view2131755368.setOnClickListener(null);
    view2131755368 = null;

    this.target = null;
  }
}
