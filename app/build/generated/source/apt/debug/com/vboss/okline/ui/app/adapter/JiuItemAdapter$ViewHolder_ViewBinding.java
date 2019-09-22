// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.app.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class JiuItemAdapter$ViewHolder_ViewBinding<T extends JiuItemAdapter.ViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public JiuItemAdapter$ViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.jiugonggeSmallImg = Utils.findRequiredViewAsType(source, R.id.jiugongge_small_img, "field 'jiugonggeSmallImg'", SimpleDraweeView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.jiugonggeSmallImg = null;

    this.target = null;
  }
}
