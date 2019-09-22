// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.contact.group;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.view.widget.SideBar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CreateGroupActivity_ViewBinding<T extends CreateGroupActivity> implements Unbinder {
  protected T target;

  @UiThread
  public CreateGroupActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.llTop = Utils.findRequiredViewAsType(source, R.id.ll_top, "field 'llTop'", LinearLayout.class);
    target.lvGroupChoose = Utils.findRequiredViewAsType(source, R.id.lv_group_choose, "field 'lvGroupChoose'", ListView.class);
    target.sidebar = Utils.findRequiredViewAsType(source, R.id.sidebar, "field 'sidebar'", SideBar.class);
    target.ivSearch = Utils.findRequiredViewAsType(source, R.id.iv_search, "field 'ivSearch'", ImageView.class);
    target.linearLayoutMenu = Utils.findRequiredViewAsType(source, R.id.linearLayoutMenu, "field 'linearLayoutMenu'", LinearLayout.class);
    target.horizonMenu = Utils.findRequiredViewAsType(source, R.id.horizonMenu, "field 'horizonMenu'", HorizontalScrollView.class);
    target.etSearch = Utils.findRequiredViewAsType(source, R.id.et_search, "field 'etSearch'", EditText.class);
    target.tvSearchCount = Utils.findRequiredViewAsType(source, R.id.tv_search_count, "field 'tvSearchCount'", TextView.class);
    target.layoutCount = Utils.findRequiredViewAsType(source, R.id.layout_count, "field 'layoutCount'", LinearLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.toolbar_contact, "field 'toolbar'", FragmentToolbar.class);
    target.noReslt = Utils.findRequiredViewAsType(source, R.id.no_result, "field 'noReslt'", RelativeLayout.class);
    target.searchTv = Utils.findRequiredViewAsType(source, R.id.search_tv, "field 'searchTv'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.llTop = null;
    target.lvGroupChoose = null;
    target.sidebar = null;
    target.ivSearch = null;
    target.linearLayoutMenu = null;
    target.horizonMenu = null;
    target.etSearch = null;
    target.tvSearchCount = null;
    target.layoutCount = null;
    target.toolbar = null;
    target.noReslt = null;
    target.searchTv = null;

    this.target = null;
  }
}
