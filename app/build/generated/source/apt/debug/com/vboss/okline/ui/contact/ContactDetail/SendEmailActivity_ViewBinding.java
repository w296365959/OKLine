// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.ContactDetail;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SendEmailActivity_ViewBinding<T extends SendEmailActivity> implements Unbinder {
  protected T target;

  private View view2131756494;

  private View view2131756496;

  @UiThread
  public SendEmailActivity_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact, "field 'toolbar'", FragmentToolbar.class);
    target.lvChooseDocument = Utils.findRequiredViewAsType(source, R.id.lv_choose_document, "field 'lvChooseDocument'", ListView.class);
    target.receiver = Utils.findRequiredViewAsType(source, R.id.receiver, "field 'receiver'", TextView.class);
    view = Utils.findRequiredView(source, R.id.receiver_button, "field 'receiverButton' and method 'onViewClicked'");
    target.receiverButton = Utils.castView(view, R.id.receiver_button, "field 'receiverButton'", TextView.class);
    view2131756494 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.mainObject = Utils.findRequiredViewAsType(source, R.id.main_object, "field 'mainObject'", TextView.class);
    view = Utils.findRequiredView(source, R.id.accessory_button, "field 'accessoryButton' and method 'onViewClicked'");
    target.accessoryButton = Utils.castView(view, R.id.accessory_button, "field 'accessoryButton'", TextView.class);
    view2131756496 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.etRecipients = Utils.findRequiredViewAsType(source, R.id.et_recipients, "field 'etRecipients'", EditText.class);
    target.turnBack = Utils.findRequiredViewAsType(source, R.id.turn_back, "field 'turnBack'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.lvChooseDocument = null;
    target.receiver = null;
    target.receiverButton = null;
    target.mainObject = null;
    target.accessoryButton = null;
    target.etRecipients = null;
    target.turnBack = null;

    view2131756494.setOnClickListener(null);
    view2131756494 = null;
    view2131756496.setOnClickListener(null);
    view2131756496 = null;

    this.target = null;
  }
}
