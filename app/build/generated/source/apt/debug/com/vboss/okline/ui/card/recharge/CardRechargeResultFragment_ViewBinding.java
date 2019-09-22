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
import java.lang.IllegalStateException;
import java.lang.Override;

public class CardRechargeResultFragment_ViewBinding<T extends CardRechargeResultFragment> implements Unbinder {
  protected T target;

  @UiThread
  public CardRechargeResultFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.ivOpenCard0 = Utils.findRequiredViewAsType(source, R.id.iv_open_card0, "field 'ivOpenCard0'", SimpleDraweeView.class);
    target.tvCardName0 = Utils.findRequiredViewAsType(source, R.id.tv_card_name0, "field 'tvCardName0'", TextView.class);
    target.tvCardInfo0 = Utils.findRequiredViewAsType(source, R.id.tv_card_info0, "field 'tvCardInfo0'", TextView.class);
    target.ivOpenCard1 = Utils.findRequiredViewAsType(source, R.id.iv_open_card1, "field 'ivOpenCard1'", SimpleDraweeView.class);
    target.tvCardName1 = Utils.findRequiredViewAsType(source, R.id.tv_card_name1, "field 'tvCardName1'", TextView.class);
    target.tvCardInfo1 = Utils.findRequiredViewAsType(source, R.id.tv_card_info1, "field 'tvCardInfo1'", TextView.class);
    target.llSuccess = Utils.findRequiredViewAsType(source, R.id.ll_success, "field 'llSuccess'", LinearLayout.class);
    target.tvFailMessage = Utils.findRequiredViewAsType(source, R.id.tv_fail_message, "field 'tvFailMessage'", TextView.class);
    target.btnRetry = Utils.findRequiredViewAsType(source, R.id.btn_retry, "field 'btnRetry'", TextView.class);
    target.btnQuit = Utils.findRequiredViewAsType(source, R.id.btn_quit, "field 'btnQuit'", TextView.class);
    target.llFail = Utils.findRequiredViewAsType(source, R.id.ll_fail, "field 'llFail'", LinearLayout.class);
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

    target.ivOpenCard0 = null;
    target.tvCardName0 = null;
    target.tvCardInfo0 = null;
    target.ivOpenCard1 = null;
    target.tvCardName1 = null;
    target.tvCardInfo1 = null;
    target.llSuccess = null;
    target.tvFailMessage = null;
    target.btnRetry = null;
    target.btnQuit = null;
    target.llFail = null;
    target.actionBack = null;
    target.actionBackLayout = null;
    target.ivOcardState = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.actionMenuLayout = null;

    this.target = null;
  }
}
