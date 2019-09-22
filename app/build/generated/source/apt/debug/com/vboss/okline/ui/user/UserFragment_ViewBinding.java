// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.user;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.user.customized.SettingsButton;
import com.vboss.okline.view.widget.OKCardView;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UserFragment_ViewBinding<T extends UserFragment> implements Unbinder {
  protected T target;

  @UiThread
  public UserFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.actionBack = Utils.findRequiredViewAsType(source, R.id.action_back, "field 'actionBack'", ImageButton.class);
    target.actionBackLayout = Utils.findRequiredViewAsType(source, R.id.action_back_layout, "field 'actionBackLayout'", RelativeLayout.class);
    target.sdvLogo = Utils.findRequiredViewAsType(source, R.id.sdv_logo, "field 'sdvLogo'", SimpleDraweeView.class);
    target.actionTitle = Utils.findRequiredViewAsType(source, R.id.action_title, "field 'actionTitle'", TextView.class);
    target.actionMenuButton = Utils.findRequiredViewAsType(source, R.id.action_menu_button, "field 'actionMenuButton'", ImageButton.class);
    target.ivOcardState = Utils.findRequiredViewAsType(source, R.id.iv_ocard_state, "field 'ivOcardState'", LogoView.class);
    target.okcardView = Utils.findRequiredViewAsType(source, R.id.okcard_view, "field 'okcardView'", OKCardView.class);
    target.actionMenuLayout = Utils.findRequiredViewAsType(source, R.id.action_menu_layout, "field 'actionMenuLayout'", RelativeLayout.class);
    target.ivUserAvatar = Utils.findRequiredViewAsType(source, R.id.iv_user_avatar, "field 'ivUserAvatar'", SimpleDraweeView.class);
    target.tvNickname = Utils.findRequiredViewAsType(source, R.id.tv_nickname, "field 'tvNickname'", TextView.class);
    target.tvUserId = Utils.findRequiredViewAsType(source, R.id.tv_user_id, "field 'tvUserId'", TextView.class);
    target.slUserMyOcard = Utils.findRequiredViewAsType(source, R.id.sl_user_my_ocard, "field 'slUserMyOcard'", SettingsButton.class);
    target.slUserMyFile = Utils.findRequiredViewAsType(source, R.id.sl_user_my_file, "field 'slUserMyFile'", SettingsButton.class);
    target.slUserMyJifen = Utils.findRequiredViewAsType(source, R.id.sl_user_my_jifen, "field 'slUserMyJifen'", SettingsButton.class);
    target.slUserMyAssets = Utils.findRequiredViewAsType(source, R.id.sl_user_my_assets, "field 'slUserMyAssets'", SettingsButton.class);
    target.llMystyleBlack = Utils.findRequiredViewAsType(source, R.id.ll_mystyle_black, "field 'llMystyleBlack'", SettingsButton.class);
    target.llMystyleUsual = Utils.findRequiredViewAsType(source, R.id.ll_mystyle_usual, "field 'llMystyleUsual'", SettingsButton.class);
    target.llMystylePink = Utils.findRequiredViewAsType(source, R.id.ll_mystyle_pink, "field 'llMystylePink'", SettingsButton.class);
    target.llMystyleSetting = Utils.findRequiredViewAsType(source, R.id.ll_mystyle_setting, "field 'llMystyleSetting'", SettingsButton.class);
    target.llMyFiles = Utils.findRequiredViewAsType(source, R.id.ll_my_files, "field 'llMyFiles'", LinearLayout.class);
    target.gvMyFiles = Utils.findRequiredViewAsType(source, R.id.gv_my_files, "field 'gvMyFiles'", GridView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.actionBack = null;
    target.actionBackLayout = null;
    target.sdvLogo = null;
    target.actionTitle = null;
    target.actionMenuButton = null;
    target.ivOcardState = null;
    target.okcardView = null;
    target.actionMenuLayout = null;
    target.ivUserAvatar = null;
    target.tvNickname = null;
    target.tvUserId = null;
    target.slUserMyOcard = null;
    target.slUserMyFile = null;
    target.slUserMyJifen = null;
    target.slUserMyAssets = null;
    target.llMystyleBlack = null;
    target.llMystyleUsual = null;
    target.llMystylePink = null;
    target.llMystyleSetting = null;
    target.llMyFiles = null;
    target.gvMyFiles = null;

    this.target = null;
  }
}
