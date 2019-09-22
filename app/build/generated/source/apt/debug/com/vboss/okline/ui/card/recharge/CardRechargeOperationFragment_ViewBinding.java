// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.card.recharge;

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
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.user.customized.LoadingView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardRechargeOperationFragment_ViewBinding<T extends CardRechargeOperationFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardRechargeOperationFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.sdvCard = Utils.findRequiredViewAsType(source, R.id.sdv_card, "field 'sdvCard'", SimpleDraweeView.class);
    target.lvOperation = Utils.findRequiredViewAsType(source, R.id.lv_operation, "field 'lvOperation'", LoadingView.class);
    target.llLoading = Utils.findRequiredViewAsType(source, R.id.ll_loading, "field 'llLoading'", LinearLayout.class);
    target.llOperation = Utils.findRequiredViewAsType(source, R.id.ll_operation, "field 'llOperation'", LinearLayout.class);
    target.tvOpenCardName = Utils.findRequiredViewAsType(source, R.id.tv_open_card_name, "field 'tvOpenCardName'", TextView.class);
    target.tvOpenCardNumber = Utils.findRequiredViewAsType(source, R.id.tv_open_card_number, "field 'tvOpenCardNumber'", TextView.class);
    target.tvOpenCardBalance = Utils.findRequiredViewAsType(source, R.id.tv_open_card_balance, "field 'tvOpenCardBalance'", TextView.class);
    target.tvCardLook = Utils.findRequiredViewAsType(source, R.id.tv_card_look, "field 'tvCardLook'", TextView.class);
    target.tvCardAuto = Utils.findRequiredViewAsType(source, R.id.tv_card_auto, "field 'tvCardAuto'", TextView.class);
    target.llSuccess = Utils.findRequiredViewAsType(source, R.id.ll_success, "field 'llSuccess'", LinearLayout.class);
    target.tvCardOpenFail = Utils.findRequiredViewAsType(source, R.id.tv_card_open_fail, "field 'tvCardOpenFail'", TextView.class);
    target.llException = Utils.findRequiredViewAsType(source, R.id.ll_exception, "field 'llException'", LinearLayout.class);
    target.tvCardRetry = Utils.findRequiredViewAsType(source, R.id.tv_card_retry, "field 'tvCardRetry'", TextView.class);
    target.tvCardRechargeAmout = Utils.findRequiredViewAsType(source, R.id.tv_card_recharge_amout, "field 'tvCardRechargeAmout'", TextView.class);
    target.actionBack = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'actionBack'", ImageButton.class);
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    target.ivOcardState = Utils.findRequiredViewAsType(source, R.id.iv_ocard_state, "field 'ivOcardState'", ImageView.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.actionMenuButton = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'actionMenuButton'", ImageButton.class);
    target.actionMenuLayout = Utils.findRequiredViewAsType(source, R.id.action_menu_layout, "field 'actionMenuLayout'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.sdvCard = null;
    target.lvOperation = null;
    target.llLoading = null;
    target.llOperation = null;
    target.tvOpenCardName = null;
    target.tvOpenCardNumber = null;
    target.tvOpenCardBalance = null;
    target.tvCardLook = null;
    target.tvCardAuto = null;
    target.llSuccess = null;
    target.tvCardOpenFail = null;
    target.llException = null;
    target.tvCardRetry = null;
    target.tvCardRechargeAmout = null;
    target.actionBack = null;
    target.actionBackLayout = null;
    target.ivOcardState = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.actionMenuLayout = null;

    this.target = null;
  }
}
