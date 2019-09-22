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

public class EditAddInfoFragment_ViewBinding<T extends EditAddInfoFragment> implements Unbinder {
  protected T target;

  private View view2131755741;

  @UiThread
  public EditAddInfoFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact, "field 'toolbar'", FragmentToolbar.class);
    target.etAddName = Utils.findRequiredViewAsType(source, R.id.et_add_name, "field 'etAddName'", ClearEditText.class);
    target.llEditName = Utils.findRequiredViewAsType(source, R.id.ll_edit_name, "field 'llEditName'", LinearLayout.class);
    target.etCardPhone = Utils.findRequiredViewAsType(source, R.id.et_card_phone, "field 'etCardPhone'", ClearEditText.class);
    target.llEditPhone = Utils.findRequiredViewAsType(source, R.id.ll_edit_phone, "field 'llEditPhone'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.tv_save_info, "field 'tvSaveInfo' and method 'onViewClicked'");
    target.tvSaveInfo = Utils.castView(view, R.id.tv_save_info, "field 'tvSaveInfo'", TextView.class);
    view2131755741 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    target.etCardRelationship = Utils.findRequiredViewAsType(source, R.id.et_card_relationship, "field 'etCardRelationship'", ClearEditText.class);
    target.llEditRelationship = Utils.findRequiredViewAsType(source, R.id.ll_edit_relationship, "field 'llEditRelationship'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.etAddName = null;
    target.llEditName = null;
    target.etCardPhone = null;
    target.llEditPhone = null;
    target.tvSaveInfo = null;
    target.etCardRelationship = null;
    target.llEditRelationship = null;

    view2131755741.setOnClickListener(null);
    view2131755741 = null;

    this.target = null;
  }
}
