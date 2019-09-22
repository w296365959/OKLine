// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.search;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class OrganizationHolder_ViewBinding<T extends OrganizationHolder> implements Unbinder {
  protected T target;

  @UiThread
  public OrganizationHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.ivCardIcon = Utils.findRequiredViewAsType(source, R.id.iv_card_icon, "field 'ivCardIcon'", SimpleDraweeView.class);
    target.textCardName = Utils.findRequiredViewAsType(source, R.id.text_card_name, "field 'textCardName'", TextView.class);
    target.textEventTime = Utils.findRequiredViewAsType(source, R.id.text_event_time, "field 'textEventTime'", TextView.class);
    target.textEventContent = Utils.findRequiredViewAsType(source, R.id.text_event_content, "field 'textEventContent'", TextView.class);
    target.container = Utils.findRequiredViewAsType(source, R.id.container, "field 'container'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ivCardIcon = null;
    target.textCardName = null;
    target.textEventTime = null;
    target.textEventContent = null;
    target.container = null;

    this.target = null;
  }
}
