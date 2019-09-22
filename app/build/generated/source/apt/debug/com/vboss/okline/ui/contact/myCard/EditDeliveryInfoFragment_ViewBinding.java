// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditDeliveryInfoFragment_ViewBinding<T extends EditDeliveryInfoFragment> implements Unbinder {
  protected T target;

  private View view2131755744;

  private View view2131755745;

  @UiThread
  public EditDeliveryInfoFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_edit_delivery, "field 'toolbar'", FragmentToolbar.class);
    view = Utils.findRequiredView(source, R.id.tv_delivery_location, "field 'tvDeliveryLocation' and method 'onViewClicked'");
    target.tvDeliveryLocation = Utils.castView(view, R.id.tv_delivery_location, "field 'tvDeliveryLocation'", TextView.class);
    view2131755744 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.llLocationDelivery = Utils.findRequiredViewAsType(source, R.id.ll_location_delivery, "field 'llLocationDelivery'", LinearLayout.class);
    target.etDetailLocation = Utils.findRequiredViewAsType(source, R.id.et_detail_location, "field 'etDetailLocation'", EditText.class);
    view = Utils.findRequiredView(source, R.id.tv_save_delivery, "field 'tvSaveDelivery' and method 'onViewClicked'");
    target.tvSaveDelivery = Utils.castView(view, R.id.tv_save_delivery, "field 'tvSaveDelivery'", TextView.class);
    view2131755745 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.tvDeliveryLocation = null;
    target.llLocationDelivery = null;
    target.etDetailLocation = null;
    target.tvSaveDelivery = null;

    view2131755744.setOnClickListener(null);
    view2131755744 = null;
    view2131755745.setOnClickListener(null);
    view2131755745 = null;

    this.target = null;
  }
}
