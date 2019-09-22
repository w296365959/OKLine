// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class NewFriendFragment_ViewBinding<T extends NewFriendFragment> implements Unbinder {
  protected T target;

  @UiThread
  public NewFriendFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact, "field 'toolbar'", FragmentToolbar.class);
    target.lvNewFriend = Utils.findRequiredViewAsType(source, R.id.lv_new_friend, "field 'lvNewFriend'", ListView.class);
    target.nonNewFriend = Utils.findRequiredViewAsType(source, R.id.non_new_friend, "field 'nonNewFriend'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.toolbar = null;
    target.lvNewFriend = null;
    target.nonNewFriend = null;

    this.target = null;
  }
}
