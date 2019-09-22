// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.organization;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrganizationDetailAdapter$DataViewHolder_ViewBinding<T extends OrganizationDetailAdapter.DataViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public OrganizationDetailAdapter$DataViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.dateView = Utils.findRequiredViewAsType(source, R.id.date_view, "field 'dateView'", TextView.class);
    target.nameInstitutionDate = Utils.findRequiredViewAsType(source, R.id.name_institution_date, "field 'nameInstitutionDate'", TextView.class);
    target.amoutDeltaInstitutionDate = Utils.findRequiredViewAsType(source, R.id.amout_delta_institution_date, "field 'amoutDeltaInstitutionDate'", TextView.class);
    target.pointsDeltaInstitutionDate = Utils.findRequiredViewAsType(source, R.id.points_delta_institution_date, "field 'pointsDeltaInstitutionDate'", TextView.class);
    target.detailInstitutionDate = Utils.findRequiredViewAsType(source, R.id.detail_institution_date, "field 'detailInstitutionDate'", TextView.class);
    target.receiptInstitutionDate = Utils.findRequiredViewAsType(source, R.id.receipt_institution_date, "field 'receiptInstitutionDate'", TextView.class);
    target.posInstitutionDate = Utils.findRequiredViewAsType(source, R.id.pos_institution_date, "field 'posInstitutionDate'", TextView.class);
    target.invoiceInstitutionDate = Utils.findRequiredViewAsType(source, R.id.invoice_institution_date, "field 'invoiceInstitutionDate'", TextView.class);
    target.llInvoivceBtns = Utils.findRequiredViewAsType(source, R.id.ll_invoivce_btns, "field 'llInvoivceBtns'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.dateView = null;
    target.nameInstitutionDate = null;
    target.amoutDeltaInstitutionDate = null;
    target.pointsDeltaInstitutionDate = null;
    target.detailInstitutionDate = null;
    target.receiptInstitutionDate = null;
    target.posInstitutionDate = null;
    target.invoiceInstitutionDate = null;
    target.llInvoivceBtns = null;

    this.target = null;
  }
}
