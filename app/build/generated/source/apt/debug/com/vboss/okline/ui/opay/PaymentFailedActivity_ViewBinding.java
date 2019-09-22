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

public class PaymentFailedActivity_ViewBinding<T extends PaymentFailedActivity> implements Unbinder {
  protected T target;

  @UiThread
  public PaymentFailedActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.anchor = Utils.findRequiredView(source, R.id.anchor, "field 'anchor'");
    target.llSettingsReturn = Utils.findRequiredViewAsType(source, R.id.ll_settings_return, "field 'llSettingsReturn'", ImageView.class);
    target.tvHeaderTitle = Utils.findRequiredViewAsType(source, R.id.tv_header_title, "field 'tvHeaderTitle'", TextView.class);
    target.tvHeaderSave = Utils.findRequiredViewAsType(source, R.id.tv_header_save, "field 'tvHeaderSave'", TextView.class);
    target.tvOpaysdkResultfailedReason = Utils.findRequiredViewAsType(source, R.id.tv_opaysdk_resultfailed_reason, "field 'tvOpaysdkResultfailedReason'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.anchor = null;
    target.llSettingsReturn = null;
    target.tvHeaderTitle = null;
    target.tvHeaderSave = null;
    target.tvOpaysdkResultfailedReason = null;

    this.target = null;
  }
}
