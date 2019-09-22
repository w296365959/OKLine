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
import com.vboss.okline.view.widget.ClearEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AddContactActivity_ViewBinding<T extends AddContactActivity> implements Unbinder {
  protected T target;

  private View view2131755470;

  @UiThread
  public AddContactActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact, "field 'toolbar'", FragmentToolbar.class);
    target.etCreateConPhoneNum = Utils.findRequiredViewAsType(source, R.id.et_createCon_phoneNum, "field 'etCreateConPhoneNum'", ClearEditText.class);
    target.llCreateConPhone = Utils.findRequiredViewAsType(source, R.id.ll_create_con_phone, "field 'llCreateConPhone'", LinearLayout.class);
    target.etAddConRemark = Utils.findRequiredViewAsType(source, R.id.et_addCon_remark, "field 'etAddConRemark'", ClearEditText.class);
    target.llCreateConName = Utils.findRequiredViewAsType(source, R.id.ll_create_con_name, "field 'llCreateConName'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.tv_add_contact, "field 'tvAddContact' and method 'onViewClicked'");
    target.tvAddContact = Utils.castView(view, R.id.tv_add_contact, "field 'tvAddContact'", TextView.class);
    view2131755470 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.etCreateConPhoneNum = null;
    target.llCreateConPhone = null;
    target.etAddConRemark = null;
    target.llCreateConName = null;
    target.tvAddContact = null;

    view2131755470.setOnClickListener(null);
    view2131755470 = null;

    this.target = null;
  }
}
