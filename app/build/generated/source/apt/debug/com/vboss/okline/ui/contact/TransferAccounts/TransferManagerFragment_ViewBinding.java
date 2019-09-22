// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.TransferAccounts;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class TransferManagerFragment_ViewBinding<T extends TransferManagerFragment> implements Unbinder {
  protected T target;

  private View view2131756256;

  private View view2131756259;

  private View view2131756260;

  private View view2131756261;

  private View view2131756263;

  private View view2131756264;

  @UiThread
  public TransferManagerFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.trans_recyclerView, "field 'recyclerView'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.transter_accounts_people, "field 'transter_accounts_img' and method 'onViewClicked'");
    target.transter_accounts_img = Utils.castView(view, R.id.transter_accounts_people, "field 'transter_accounts_img'", SimpleDraweeView.class);
    view2131756256 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.transter_accounts_peopleName = Utils.findRequiredViewAsType(source, R.id.transter_accounts_peopleName, "field 'transter_accounts_peopleName'", TextView.class);
    target.transter_accounts_remark = Utils.findRequiredViewAsType(source, R.id.transter_accounts_remark, "field 'transter_accounts_remark'", TextView.class);
    target.no_trans_record = Utils.findRequiredViewAsType(source, R.id.no_trans_record, "field 'no_trans_record'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.transManager_phone, "method 'onViewClicked'");
    view2131756259 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.transManager_chat, "method 'onViewClicked'");
    view2131756260 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.transManager_email, "method 'onViewClicked'");
    view2131756261 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.transManager_delivery, "method 'onViewClicked'");
    view2131756263 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.to_transfer, "method 'onViewClicked'");
    view2131756264 = view;
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

    target.recyclerView = null;
    target.transter_accounts_img = null;
    target.transter_accounts_peopleName = null;
    target.transter_accounts_remark = null;
    target.no_trans_record = null;

    view2131756256.setOnClickListener(null);
    view2131756256 = null;
    view2131756259.setOnClickListener(null);
    view2131756259 = null;
    view2131756260.setOnClickListener(null);
    view2131756260 = null;
    view2131756261.setOnClickListener(null);
    view2131756261 = null;
    view2131756263.setOnClickListener(null);
    view2131756263 = null;
    view2131756264.setOnClickListener(null);
    view2131756264 = null;

    this.target = null;
  }
}
