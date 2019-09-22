// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.app.apppool.apppooltype;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AppPoolTypeListFragment_ViewBinding<T extends AppPoolTypeListFragment> implements Unbinder {
  protected T target;

  private View view2131755971;

  private View view2131755972;

  @UiThread
  public AppPoolTypeListFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.apptTypeListRV = Utils.findRequiredViewAsType(source, R.id.apptype_list_recyclerview, "field 'apptTypeListRV'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.app_pool_button, "field 'appPoolButton' and method 'ViewOnClick'");
    target.appPoolButton = Utils.castView(view, R.id.app_pool_button, "field 'appPoolButton'", RadioButton.class);
    view2131755971 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.card_button, "field 'cardButton' and method 'ViewOnClick'");
    target.cardButton = Utils.castView(view, R.id.card_button, "field 'cardButton'", RadioButton.class);
    view2131755972 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    target.layout = Utils.findRequiredViewAsType(source, R.id.layout, "field 'layout'", TextView.class);
    target.apptype_no_app_txt = Utils.findRequiredViewAsType(source, R.id.apptype_no_app_txt, "field 'apptype_no_app_txt'", TextView.class);
    target.apptype_no_app_img = Utils.findRequiredViewAsType(source, R.id.apptype_no_app_img, "field 'apptype_no_app_img'", ImageView.class);
    target.apptype_no_app = Utils.findRequiredViewAsType(source, R.id.apptype_no_app, "field 'apptype_no_app'", LinearLayout.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
    target.ptrFrameLayout = Utils.findRequiredViewAsType(source, R.id.ptrFrameLayout, "field 'ptrFrameLayout'", PtrClassicFrameLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.apptTypeListRV = null;
    target.appPoolButton = null;
    target.cardButton = null;
    target.layout = null;
    target.apptype_no_app_txt = null;
    target.apptype_no_app_img = null;
    target.apptype_no_app = null;
    target.toolbar = null;
    target.ptrFrameLayout = null;

    view2131755971.setOnClickListener(null);
    view2131755971 = null;
    view2131755972.setOnClickListener(null);
    view2131755972 = null;

    this.target = null;
  }
}
