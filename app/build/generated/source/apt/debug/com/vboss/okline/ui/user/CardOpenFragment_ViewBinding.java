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
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.user.customized.LoadingView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardOpenFragment_ViewBinding<T extends CardOpenFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardOpenFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.sdvCardOpen = Utils.findRequiredViewAsType(source, R.id.sdv_card_open, "field 'sdvCardOpen'", SimpleDraweeView.class);
    target.tvOpenCardName = Utils.findRequiredViewAsType(source, R.id.tv_open_card_name, "field 'tvOpenCardName'", TextView.class);
    target.tvOpenCardNumber = Utils.findRequiredViewAsType(source, R.id.tv_open_card_number, "field 'tvOpenCardNumber'", TextView.class);
    target.llCardOpenDetail = Utils.findRequiredViewAsType(source, R.id.ll_card_open_detail, "field 'llCardOpenDetail'", LinearLayout.class);
    target.tvCardOpeningSuccess = Utils.findRequiredViewAsType(source, R.id.tv_card_opening_success, "field 'tvCardOpeningSuccess'", TextView.class);
    target.tvCardOpening = Utils.findRequiredViewAsType(source, R.id.tv_card_opening, "field 'tvCardOpening'", TextView.class);
    target.lvOpenCard = Utils.findRequiredViewAsType(source, R.id.lv_open_card, "field 'lvOpenCard'", LoadingView.class);
    target.tvCardLook = Utils.findRequiredViewAsType(source, R.id.tv_card_look, "field 'tvCardLook'", TextView.class);
    target.tvCardOpeningAlert = Utils.findRequiredViewAsType(source, R.id.tv_card_opening_alert, "field 'tvCardOpeningAlert'", TextView.class);
    target.tvCardRecharge = Utils.findRequiredViewAsType(source, R.id.tv_card_recharge, "field 'tvCardRecharge'", TextView.class);
    target.llCardOpening = Utils.findRequiredViewAsType(source, R.id.ll_card_opening, "field 'llCardOpening'", LinearLayout.class);
    target.llCardOpenMain = Utils.findRequiredViewAsType(source, R.id.ll_card_open_main, "field 'llCardOpenMain'", LinearLayout.class);
    target.tvCardOpenFail = Utils.findRequiredViewAsType(source, R.id.tv_card_open_fail, "field 'tvCardOpenFail'", TextView.class);
    target.llCardOpenFail = Utils.findRequiredViewAsType(source, R.id.ll_card_open_fail, "field 'llCardOpenFail'", LinearLayout.class);
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

    target.sdvCardOpen = null;
    target.tvOpenCardName = null;
    target.tvOpenCardNumber = null;
    target.llCardOpenDetail = null;
    target.tvCardOpeningSuccess = null;
    target.tvCardOpening = null;
    target.lvOpenCard = null;
    target.tvCardLook = null;
    target.tvCardOpeningAlert = null;
    target.tvCardRecharge = null;
    target.llCardOpening = null;
    target.llCardOpenMain = null;
    target.tvCardOpenFail = null;
    target.llCardOpenFail = null;
    target.actionBack = null;
    target.actionBackLayout = null;
    target.ivOcardState = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.actionMenuLayout = null;

    this.target = null;
  }
}
