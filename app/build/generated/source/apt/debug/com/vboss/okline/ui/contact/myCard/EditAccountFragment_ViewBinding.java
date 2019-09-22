// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.myCard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.view.widget.ClearEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditAccountFragment_ViewBinding<T extends EditAccountFragment> implements Unbinder {
  protected T target;

  private View view2131755551;

  private View view2131755552;

  @UiThread
  public EditAccountFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact_account, "field 'toolbar'", FragmentToolbar.class);
    target.tvCompanyName = Utils.findRequiredViewAsType(source, R.id.tv_company_name, "field 'tvCompanyName'", TextView.class);
    target.llCreateConPhone = Utils.findRequiredViewAsType(source, R.id.ll_create_con_phone, "field 'llCreateConPhone'", RelativeLayout.class);
    target.etMyAccount = Utils.findRequiredViewAsType(source, R.id.et_my_account, "field 'etMyAccount'", ClearEditText.class);
    target.llAccount = Utils.findRequiredViewAsType(source, R.id.ll_account, "field 'llAccount'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.ll_choose_bank, "field 'llChooseBank' and method 'onViewClicked'");
    target.llChooseBank = Utils.castView(view, R.id.ll_choose_bank, "field 'llChooseBank'", LinearLayout.class);
    view2131755551 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.tv_save_bank, "field 'tvSaveBank' and method 'onViewClicked'");
    target.tvSaveBank = Utils.castView(view, R.id.tv_save_bank, "field 'tvSaveBank'", TextView.class);
    view2131755552 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.tvBankName = Utils.findRequiredViewAsType(source, R.id.tv_bank_name, "field 'tvBankName'", TextView.class);
    target.etAccountName = Utils.findRequiredViewAsType(source, R.id.et_account_name, "field 'etAccountName'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.tvCompanyName = null;
    target.llCreateConPhone = null;
    target.etMyAccount = null;
    target.llAccount = null;
    target.llChooseBank = null;
    target.tvSaveBank = null;
    target.tvBankName = null;
    target.etAccountName = null;

    view2131755551.setOnClickListener(null);
    view2131755551 = null;
    view2131755552.setOnClickListener(null);
    view2131755552 = null;

    this.target = null;
  }
}
