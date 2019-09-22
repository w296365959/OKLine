// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.user;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.user.customized.LoadingLayout;
import com.vboss.okline.view.widget.OKCardView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OCardAttachFragment_ViewBinding<T extends OCardAttachFragment> implements Unbinder {
  protected T target;

  @UiThread
  public OCardAttachFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.actionBack = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'actionBack'", ImageButton.class);
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.actionMenuButton = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'actionMenuButton'", ImageButton.class);
    target.actionMenuLayout = Utils.findRequiredViewAsType(source, R.id.action_menu_layout, "field 'actionMenuLayout'", RelativeLayout.class);
    target.ivOcardHeader = Utils.findRequiredViewAsType(source, R.id.iv_ocard_header, "field 'ivOcardHeader'", ImageView.class);
    target.ivOcardState = Utils.findRequiredViewAsType(source, R.id.iv_ocard_state, "field 'ivOcardState'", LogoView.class);
    target.okcardView = Utils.findRequiredViewAsType(source, R.id.okcard_view, "field 'okcardView'", OKCardView.class);
    target.tvOcardOnlineTag = Utils.findRequiredViewAsType(source, R.id.tv_ocard_online_tag, "field 'tvOcardOnlineTag'", TextView.class);
    target.ivLoading = Utils.findRequiredViewAsType(source, R.id.iv_loading, "field 'ivLoading'", ImageView.class);
    target.llStep1 = Utils.findRequiredViewAsType(source, R.id.ll_step1, "field 'llStep1'", LoadingLayout.class);
    target.llStep2 = Utils.findRequiredViewAsType(source, R.id.ll_step2, "field 'llStep2'", LoadingLayout.class);
    target.llStep3 = Utils.findRequiredViewAsType(source, R.id.ll_step3, "field 'llStep3'", LoadingLayout.class);
    target.textNoNfcHeader = Utils.findRequiredViewAsType(source, R.id.text_no_nfc_header, "field 'textNoNfcHeader'", TextView.class);
    target.etOcardNumber = Utils.findRequiredViewAsType(source, R.id.et_ocard_number, "field 'etOcardNumber'", EditText.class);
    target.tv0 = Utils.findRequiredViewAsType(source, R.id.tv0, "field 'tv0'", TextView.class);
    target.tv1 = Utils.findRequiredViewAsType(source, R.id.tv1, "field 'tv1'", TextView.class);
    target.tv2 = Utils.findRequiredViewAsType(source, R.id.tv2, "field 'tv2'", TextView.class);
    target.tv3 = Utils.findRequiredViewAsType(source, R.id.tv3, "field 'tv3'", TextView.class);
    target.tv4 = Utils.findRequiredViewAsType(source, R.id.tv4, "field 'tv4'", TextView.class);
    target.tv5 = Utils.findRequiredViewAsType(source, R.id.tv5, "field 'tv5'", TextView.class);
    target.btnOcardNumber = Utils.findRequiredViewAsType(source, R.id.btn_ocard_number, "field 'btnOcardNumber'", TextView.class);
    target.llNoNFC = Utils.findRequiredViewAsType(source, R.id.ll_no_NFC, "field 'llNoNFC'", LinearLayout.class);
    target.llStep4 = Utils.findRequiredViewAsType(source, R.id.ll_step4, "field 'llStep4'", LoadingLayout.class);
    target.llStep5 = Utils.findRequiredViewAsType(source, R.id.ll_step5, "field 'llStep5'", LoadingLayout.class);
    target.llStep6 = Utils.findRequiredViewAsType(source, R.id.ll_step6, "field 'llStep6'", LoadingLayout.class);
    target.tvOcardAttatchSuccessNo = Utils.findRequiredViewAsType(source, R.id.tv_ocard_attatch_success_no, "field 'tvOcardAttatchSuccessNo'", TextView.class);
    target.llOcardAttatchSuccess = Utils.findRequiredViewAsType(source, R.id.ll_ocard_attatch_success, "field 'llOcardAttatchSuccess'", LinearLayout.class);
    target.btnReAttac = Utils.findRequiredViewAsType(source, R.id.btn_re_attac, "field 'btnReAttac'", Button.class);
    target.llOcardAttatchFail = Utils.findRequiredViewAsType(source, R.id.ll_ocard_attatch_fail, "field 'llOcardAttatchFail'", LinearLayout.class);
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
    target.ivOcardState = null;
    target.okcardView = null;
    target.tvOcardOnlineTag = null;
    target.ivLoading = null;
    target.llStep1 = null;
    target.llStep2 = null;
    target.llStep3 = null;
    target.textNoNfcHeader = null;
    target.etOcardNumber = null;
    target.tv0 = null;
    target.tv1 = null;
    target.tv2 = null;
    target.tv3 = null;
    target.tv4 = null;
    target.tv5 = null;
    target.btnOcardNumber = null;
    target.llNoNFC = null;
    target.llStep4 = null;
    target.llStep5 = null;
    target.llStep6 = null;
    target.tvOcardAttatchSuccessNo = null;
    target.llOcardAttatchSuccess = null;
    target.btnReAttac = null;
    target.llOcardAttatchFail = null;

    this.target = null;
  }
}
