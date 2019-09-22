// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.date;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChildHolder_ViewBinding<T extends ChildHolder> implements Unbinder {
  protected T target;

  @UiThread
  public ChildHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.dateView = Utils.findRequiredViewAsType(source, R.id.date_view, "field 'dateView'", TextView.class);
    target.timeView = Utils.findRequiredViewAsType(source, R.id.time_view, "field 'timeView'", TextView.class);
    target.iconDate = Utils.findRequiredViewAsType(source, R.id.icon_date, "field 'iconDate'", SimpleDraweeView.class);
    target.nameCardDate = Utils.findRequiredViewAsType(source, R.id.name_card_date, "field 'nameCardDate'", TextView.class);
    target.nameInstitutionDate = Utils.findRequiredViewAsType(source, R.id.name_institution_date, "field 'nameInstitutionDate'", TextView.class);
    target.amoutDeltaInstitutionDate = Utils.findRequiredViewAsType(source, R.id.amout_delta_institution_date, "field 'amoutDeltaInstitutionDate'", TextView.class);
    target.pointsDeltaInstitutionDate = Utils.findRequiredViewAsType(source, R.id.points_delta_institution_date, "field 'pointsDeltaInstitutionDate'", TextView.class);
    target.detailInstitutionDate = Utils.findRequiredViewAsType(source, R.id.detail_institution_date, "field 'detailInstitutionDate'", TextView.class);
    target.receiptInstitutionDate = Utils.findRequiredViewAsType(source, R.id.receipt_institution_date, "field 'receiptInstitutionDate'", TextView.class);
    target.posInstitutionDate = Utils.findRequiredViewAsType(source, R.id.pos_institution_date, "field 'posInstitutionDate'", TextView.class);
    target.invoiceInstitutionDate = Utils.findRequiredViewAsType(source, R.id.invoice_institution_date, "field 'invoiceInstitutionDate'", TextView.class);
    target.llInvoivceBtns = Utils.findRequiredViewAsType(source, R.id.ll_invoivce_btns, "field 'llInvoivceBtns'", LinearLayout.class);
    target.bottomLine = Utils.findRequiredView(source, R.id.bottom_line, "field 'bottomLine'");
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.dateView = null;
    target.timeView = null;
    target.iconDate = null;
    target.nameCardDate = null;
    target.nameInstitutionDate = null;
    target.amoutDeltaInstitutionDate = null;
    target.pointsDeltaInstitutionDate = null;
    target.detailInstitutionDate = null;
    target.receiptInstitutionDate = null;
    target.posInstitutionDate = null;
    target.invoiceInstitutionDate = null;
    target.llInvoivceBtns = null;
    target.bottomLine = null;

    this.target = null;
  }
}
