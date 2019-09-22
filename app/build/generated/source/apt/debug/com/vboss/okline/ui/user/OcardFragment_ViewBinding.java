// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.user;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.user.customized.OcardButton;
import com.vboss.okline.view.widget.OKCardView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OcardFragment_ViewBinding<T extends OcardFragment> implements Unbinder {
  protected T target;

  @UiThread
  public OcardFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.actionBack = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'actionBack'", ImageButton.class);
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.actionMenuButton = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'actionMenuButton'", ImageButton.class);
    target.actionMenuLayout = Utils.findRequiredViewAsType(source, R.id.action_menu_layout, "field 'actionMenuLayout'", RelativeLayout.class);
    target.ivOcardHeader = Utils.findRequiredViewAsType(source, R.id.iv_ocard_header, "field 'ivOcardHeader'", ImageView.class);
    target.tvOcardOnlineTag = Utils.findRequiredViewAsType(source, R.id.tv_ocard_online_tag, "field 'tvOcardOnlineTag'", TextView.class);
    target.ivOcardState = Utils.findRequiredViewAsType(source, R.id.iv_ocard_state, "field 'ivOcardState'", LogoView.class);
    target.okcardView = Utils.findRequiredViewAsType(source, R.id.okcard_view, "field 'okcardView'", OKCardView.class);
    target.btnOcardAbsenceDeclaration = Utils.findRequiredViewAsType(source, R.id.btn_ocard_absence_declaration, "field 'btnOcardAbsenceDeclaration'", OcardButton.class);
    target.btnOcardResume = Utils.findRequiredViewAsType(source, R.id.btn_ocard_resume, "field 'btnOcardResume'", OcardButton.class);
    target.btnOcardApplyNew = Utils.findRequiredViewAsType(source, R.id.btn_ocard_apply_new, "field 'btnOcardApplyNew'", OcardButton.class);
    target.ocardAbsenceDate = Utils.findRequiredViewAsType(source, R.id.ocard_absence_date, "field 'ocardAbsenceDate'", TextView.class);
    target.ocardAbsenceStatus = Utils.findRequiredViewAsType(source, R.id.ocard_absence_status, "field 'ocardAbsenceStatus'", LinearLayout.class);
    target.textCardNumber = Utils.findRequiredViewAsType(source, R.id.text_card_number, "field 'textCardNumber'", TextView.class);
    target.textBirthday = Utils.findRequiredViewAsType(source, R.id.text_birthday, "field 'textBirthday'", TextView.class);
    target.textTotalVolume = Utils.findRequiredViewAsType(source, R.id.text_total_volume, "field 'textTotalVolume'", TextView.class);
    target.textAvailableVolume = Utils.findRequiredViewAsType(source, R.id.text_available_volume, "field 'textAvailableVolume'", TextView.class);
    target.textVersionInfo = Utils.findRequiredViewAsType(source, R.id.text_version_info, "field 'textVersionInfo'", TextView.class);
    target.textBatteryVolume = Utils.findRequiredViewAsType(source, R.id.text_battery_volume, "field 'textBatteryVolume'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.actionBack = null;
    target.actionBackLayout = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.actionMenuLayout = null;
    target.ivOcardHeader = null;
    target.tvOcardOnlineTag = null;
    target.ivOcardState = null;
    target.okcardView = null;
    target.btnOcardAbsenceDeclaration = null;
    target.btnOcardResume = null;
    target.btnOcardApplyNew = null;
    target.ocardAbsenceDate = null;
    target.ocardAbsenceStatus = null;
    target.textCardNumber = null;
    target.textBirthday = null;
    target.textTotalVolume = null;
    target.textAvailableVolume = null;
    target.textVersionInfo = null;
    target.textBatteryVolume = null;

    this.target = null;
  }
}
