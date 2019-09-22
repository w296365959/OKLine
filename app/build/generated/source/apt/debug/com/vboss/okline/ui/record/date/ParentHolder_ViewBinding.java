// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.date;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class ParentHolder_ViewBinding<T extends ParentHolder> implements Unbinder {
  protected T target;

  @UiThread
  public ParentHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.textRecordDateTitle = Utils.findRequiredViewAsType(source, R.id.text_record_date_title, "field 'textRecordDateTitle'", TextView.class);
    target.arrowRecordDateTitle = Utils.findRequiredViewAsType(source, R.id.arrow_record_date_title, "field 'arrowRecordDateTitle'", ImageView.class);
    target.container = Utils.findRequiredViewAsType(source, R.id.container, "field 'container'", RelativeLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.textRecordDateTitle = null;
    target.arrowRecordDateTitle = null;
    target.container = null;

    this.target = null;
  }
}
