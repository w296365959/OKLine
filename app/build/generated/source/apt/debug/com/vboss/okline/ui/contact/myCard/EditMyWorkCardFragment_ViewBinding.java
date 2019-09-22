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
import com.vboss.okline.view.widget.ClearEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class EditMyWorkCardFragment_ViewBinding<T extends EditMyWorkCardFragment> implements Unbinder {
  protected T target;

  private View view2131755557;

  private View view2131755559;

  @UiThread
  public EditMyWorkCardFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact_workcard, "field 'toolbar'", FragmentToolbar.class);
    target.etCompanyName = Utils.findRequiredViewAsType(source, R.id.et_company_name, "field 'etCompanyName'", ClearEditText.class);
    target.llCreateConPhone = Utils.findRequiredViewAsType(source, R.id.ll_create_con_phone, "field 'llCreateConPhone'", LinearLayout.class);
    target.etJobPosition = Utils.findRequiredViewAsType(source, R.id.et_job_position, "field 'etJobPosition'", ClearEditText.class);
    target.llCreateConName = Utils.findRequiredViewAsType(source, R.id.ll_create_con_name, "field 'llCreateConName'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.tv_my_location, "field 'tvMyLocation' and method 'onViewClicked'");
    target.tvMyLocation = Utils.castView(view, R.id.tv_my_location, "field 'tvMyLocation'", TextView.class);
    view2131755557 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.llLocation = Utils.findRequiredViewAsType(source, R.id.ll_location, "field 'llLocation'", LinearLayout.class);
    target.etDetailLocation = Utils.findRequiredViewAsType(source, R.id.et_detail_location, "field 'etDetailLocation'", EditText.class);
    view = Utils.findRequiredView(source, R.id.tv_save_address, "field 'tvSaveAddress' and method 'onViewClicked'");
    target.tvSaveAddress = Utils.castView(view, R.id.tv_save_address, "field 'tvSaveAddress'", TextView.class);
    view2131755559 = view;
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
    target.etCompanyName = null;
    target.llCreateConPhone = null;
    target.etJobPosition = null;
    target.llCreateConName = null;
    target.tvMyLocation = null;
    target.llLocation = null;
    target.etDetailLocation = null;
    target.tvSaveAddress = null;

    view2131755557.setOnClickListener(null);
    view2131755557 = null;
    view2131755559.setOnClickListener(null);
    view2131755559 = null;

    this.target = null;
  }
}
