// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.opay;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PaymentResultFragment_ViewBinding<T extends PaymentResultFragment> implements Unbinder {
  protected T target;

  @UiThread
  public PaymentResultFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.ivLoading = Utils.findRequiredViewAsType(source, R.id.iv_loading, "field 'ivLoading'", ImageView.class);
    target.tvLoading = Utils.findRequiredViewAsType(source, R.id.tv_loading, "field 'tvLoading'", TextView.class);
    target.tvOpaysdkResultCancel = Utils.findRequiredViewAsType(source, R.id.tv_opaysdk_result_cancel, "field 'tvOpaysdkResultCancel'", TextView.class);
    target.tvOpaysdkResultRetry = Utils.findRequiredViewAsType(source, R.id.tv_opaysdk_result_retry, "field 'tvOpaysdkResultRetry'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivLoading = null;
    target.tvLoading = null;
    target.tvOpaysdkResultCancel = null;
    target.tvOpaysdkResultRetry = null;

    this.target = null;
  }
}
