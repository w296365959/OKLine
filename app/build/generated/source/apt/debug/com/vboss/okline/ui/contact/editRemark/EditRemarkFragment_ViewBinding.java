// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.editRemark;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.view.widget.ClearEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditRemarkFragment_ViewBinding<T extends EditRemarkFragment> implements Unbinder {
  protected T target;

  @UiThread
  public EditRemarkFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.etRemark = Utils.findRequiredViewAsType(source, R.id.et_remark, "field 'etRemark'", ClearEditText.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact_edit, "field 'toolbar'", FragmentToolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.etRemark = null;
    target.toolbar = null;

    this.target = null;
  }
}
