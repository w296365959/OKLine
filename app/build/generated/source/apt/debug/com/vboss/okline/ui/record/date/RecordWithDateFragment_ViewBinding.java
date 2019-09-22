// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.date;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RecordWithDateFragment_ViewBinding<T extends RecordWithDateFragment> implements Unbinder {
  protected T target;

  @UiThread
  public RecordWithDateFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
    target.ptrFrameLayout = Utils.findRequiredViewAsType(source, R.id.ptrFrameLayout, "field 'ptrFrameLayout'", PtrClassicFrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.recyclerView = null;
    target.ptrFrameLayout = null;

    this.target = null;
  }
}
