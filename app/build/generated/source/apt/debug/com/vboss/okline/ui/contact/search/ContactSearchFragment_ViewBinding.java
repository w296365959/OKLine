// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.search;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.view.widget.ClearEditText;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ContactSearchFragment_ViewBinding<T extends ContactSearchFragment> implements Unbinder {
  protected T target;

  @UiThread
  public ContactSearchFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.lvSearch = Utils.findRequiredViewAsType(source, R.id.lv_search, "field 'lvSearch'", ListView.class);
    target.tvSearchCount = Utils.findRequiredViewAsType(source, R.id.tv_search_count, "field 'tvSearchCount'", TextView.class);
    target.layoutCount = Utils.findRequiredViewAsType(source, R.id.layout_count, "field 'layoutCount'", LinearLayout.class);
    target.editSearch = Utils.findRequiredViewAsType(source, R.id.edit_search, "field 'editSearch'", ClearEditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.lvSearch = null;
    target.tvSearchCount = null;
    target.layoutCount = null;
    target.editSearch = null;

    this.target = null;
  }
}
