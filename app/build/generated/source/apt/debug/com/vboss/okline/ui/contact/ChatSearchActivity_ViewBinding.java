// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.view.widget.ClearEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ChatSearchActivity_ViewBinding<T extends ChatSearchActivity> implements Unbinder {
  protected T target;

  @UiThread
  public ChatSearchActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact, "field 'toolbar'", FragmentToolbar.class);
    target.etKeyboardTop = Utils.findRequiredViewAsType(source, R.id.et_keyboard_top, "field 'etKeyboardTop'", ClearEditText.class);
    target.lvChatContent = Utils.findRequiredViewAsType(source, R.id.lv_chat_content, "field 'lvChatContent'", ListView.class);
    target.btChatNow = Utils.findRequiredViewAsType(source, R.id.bt_chat_now, "field 'btChatNow'", Button.class);
    target.llChatCurrent = Utils.findRequiredViewAsType(source, R.id.ll_chat_current, "field 'llChatCurrent'", LinearLayout.class);
    target.tvNoContact = Utils.findRequiredViewAsType(source, R.id.tv_noContact, "field 'tvNoContact'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.etKeyboardTop = null;
    target.lvChatContent = null;
    target.btChatNow = null;
    target.llChatCurrent = null;
    target.tvNoContact = null;

    this.target = null;
  }
}
