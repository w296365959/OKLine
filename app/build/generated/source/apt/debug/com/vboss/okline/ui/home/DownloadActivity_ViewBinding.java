// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.home;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class DownloadActivity_ViewBinding<T extends DownloadActivity> implements Unbinder {
  protected T target;

  @UiThread
  public DownloadActivity_ViewBinding(T target, View source) {
    this.target = target;

    target.titleView = Utils.findRequiredViewAsType(source, R.id.titleView, "field 'titleView'", TextView.class);
    target.listview = Utils.findRequiredViewAsType(source, R.id.update_listView, "field 'listview'", ListView.class);
    target.download_logo = Utils.findRequiredViewAsType(source, R.id.download_logo, "field 'download_logo'", ImageView.class);
    target.wait = Utils.findRequiredViewAsType(source, R.id.wait, "field 'wait'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.titleView = null;
    target.listview = null;
    target.download_logo = null;
    target.wait = null;

    this.target = null;
  }
}
