// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.addContact;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditAddPhoneFragment_ViewBinding<T extends EditAddPhoneFragment> implements Unbinder {
  protected T target;

  private View view2131755584;

  private View view2131755585;

  @UiThread
  public EditAddPhoneFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact_newcard, "field 'toolbar'", FragmentToolbar.class);
    target.llItemContent = Utils.findRequiredViewAsType(source, R.id.ll_item_content, "field 'llItemContent'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.tv_add_phone, "field 'tvAddPhone' and method 'onViewClicked'");
    target.tvAddPhone = Utils.castView(view, R.id.tv_add_phone, "field 'tvAddPhone'", TextView.class);
    view2131755584 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_save_otherphone, "field 'tvSaveOtherphone' and method 'onViewClicked'");
    target.tvSaveOtherphone = Utils.castView(view, R.id.tv_save_otherphone, "field 'tvSaveOtherphone'", TextView.class);
    view2131755585 = view;
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
    target.llItemContent = null;
    target.tvAddPhone = null;
    target.tvSaveOtherphone = null;

    view2131755584.setOnClickListener(null);
    view2131755584 = null;
    view2131755585.setOnClickListener(null);
    view2131755585 = null;

    this.target = null;
  }
}
