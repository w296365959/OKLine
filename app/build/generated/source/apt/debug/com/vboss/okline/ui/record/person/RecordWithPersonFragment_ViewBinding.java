// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.record.person;

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

public class RecordWithPersonFragment_ViewBinding<T extends RecordWithPersonFragment> implements Unbinder {
  protected T target;

  @UiThread
  public RecordWithPersonFragment_ViewBinding(T target, View source) {
    this.target = target;

    target.ptrFrameLayout = Utils.findRequiredViewAsType(source, R.id.ptrFrameLayout, "field 'ptrFrameLayout'", PtrClassicFrameLayout.class);
    target.recyclerView = Utils.findRequiredViewAsType(source, R.id.recyclerView, "field 'recyclerView'", RecyclerView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.ptrFrameLayout = null;
    target.recyclerView = null;

    this.target = null;
  }
}
