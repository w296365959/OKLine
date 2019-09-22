// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.app.apppool;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.facebook.drawee.view.SimpleDraweeView;
import com.vboss.okline.R;
import com.vboss.okline.base.helper.FragmentToolbar;
import java.lang.IllegalStateException;
import java.lang.Override;

public class AppPoolFragment_ViewBinding<T extends AppPoolFragment> implements Unbinder {
  protected T target;

  private View view2131755938;

  private View view2131755942;

  private View view2131755946;

  private View view2131755950;

  private View view2131755954;

  private View view2131755958;

  private View view2131755962;

  private View view2131755966;

  @UiThread
  public AppPoolFragment_ViewBinding(final T target, View source) {
    this.target = target;

    View view;
    target.appPoolRecyclerView = Utils.findRequiredViewAsType(source, R.id.apppool_recyclerview, "field 'appPoolRecyclerView'", RecyclerView.class);
    target.header = Utils.findRequiredViewAsType(source, R.id.apppool_header, "field 'header'", RecyclerViewHeader.class);
    target.new_app_name1 = Utils.findRequiredViewAsType(source, R.id.new_app_name1, "field 'new_app_name1'", TextView.class);
    target.new_app_name2 = Utils.findRequiredViewAsType(source, R.id.new_app_name2, "field 'new_app_name2'", TextView.class);
    target.new_app_name3 = Utils.findRequiredViewAsType(source, R.id.new_app_name3, "field 'new_app_name3'", TextView.class);
    target.new_app_name4 = Utils.findRequiredViewAsType(source, R.id.new_app_name4, "field 'new_app_name4'", TextView.class);
    target.new_app_name5 = Utils.findRequiredViewAsType(source, R.id.new_app_name5, "field 'new_app_name5'", TextView.class);
    target.new_app_name6 = Utils.findRequiredViewAsType(source, R.id.new_app_name6, "field 'new_app_name6'", TextView.class);
    target.new_app_name7 = Utils.findRequiredViewAsType(source, R.id.new_app_name7, "field 'new_app_name7'", TextView.class);
    target.new_app_name8 = Utils.findRequiredViewAsType(source, R.id.new_app_name8, "field 'new_app_name8'", TextView.class);
    view = Utils.findRequiredView(source, R.id.new_app_icon1, "field 'new_app_icon1' and method 'ViewOnClick'");
    target.new_app_icon1 = Utils.castView(view, R.id.new_app_icon1, "field 'new_app_icon1'", SimpleDraweeView.class);
    view2131755938 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.new_app_icon2, "field 'new_app_icon2' and method 'ViewOnClick'");
    target.new_app_icon2 = Utils.castView(view, R.id.new_app_icon2, "field 'new_app_icon2'", SimpleDraweeView.class);
    view2131755942 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.new_app_icon3, "field 'new_app_icon3' and method 'ViewOnClick'");
    target.new_app_icon3 = Utils.castView(view, R.id.new_app_icon3, "field 'new_app_icon3'", SimpleDraweeView.class);
    view2131755946 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.new_app_icon4, "field 'new_app_icon4' and method 'ViewOnClick'");
    target.new_app_icon4 = Utils.castView(view, R.id.new_app_icon4, "field 'new_app_icon4'", SimpleDraweeView.class);
    view2131755950 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.new_app_icon5, "field 'new_app_icon5' and method 'ViewOnClick'");
    target.new_app_icon5 = Utils.castView(view, R.id.new_app_icon5, "field 'new_app_icon5'", SimpleDraweeView.class);
    view2131755954 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.new_app_icon6, "field 'new_app_icon6' and method 'ViewOnClick'");
    target.new_app_icon6 = Utils.castView(view, R.id.new_app_icon6, "field 'new_app_icon6'", SimpleDraweeView.class);
    view2131755958 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.new_app_icon7, "field 'new_app_icon7' and method 'ViewOnClick'");
    target.new_app_icon7 = Utils.castView(view, R.id.new_app_icon7, "field 'new_app_icon7'", SimpleDraweeView.class);
    view2131755962 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.new_app_icon8, "field 'new_app_icon8' and method 'ViewOnClick'");
    target.new_app_icon8 = Utils.castView(view, R.id.new_app_icon8, "field 'new_app_icon8'", SimpleDraweeView.class);
    view2131755966 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.ViewOnClick(p0);
      }
    });
    target.installed_icon1 = Utils.findRequiredViewAsType(source, R.id.new_app_installed_icon1, "field 'installed_icon1'", ImageView.class);
    target.installed_icon2 = Utils.findRequiredViewAsType(source, R.id.new_app_installed_icon2, "field 'installed_icon2'", ImageView.class);
    target.installed_icon3 = Utils.findRequiredViewAsType(source, R.id.new_app_installed_icon3, "field 'installed_icon3'", ImageView.class);
    target.installed_icon4 = Utils.findRequiredViewAsType(source, R.id.new_app_installed_icon4, "field 'installed_icon4'", ImageView.class);
    target.installed_icon5 = Utils.findRequiredViewAsType(source, R.id.new_app_installed_icon5, "field 'installed_icon5'", ImageView.class);
    target.installed_icon6 = Utils.findRequiredViewAsType(source, R.id.new_app_installed_icon6, "field 'installed_icon6'", ImageView.class);
    target.installed_icon7 = Utils.findRequiredViewAsType(source, R.id.new_app_installed_icon7, "field 'installed_icon7'", ImageView.class);
    target.installed_icon8 = Utils.findRequiredViewAsType(source, R.id.new_app_installed_icon8, "field 'installed_icon8'", ImageView.class);
    target.update_icon1 = Utils.findRequiredViewAsType(source, R.id.new_app_update_icon1, "field 'update_icon1'", ImageView.class);
    target.update_icon2 = Utils.findRequiredViewAsType(source, R.id.new_app_update_icon2, "field 'update_icon2'", ImageView.class);
    target.update_icon3 = Utils.findRequiredViewAsType(source, R.id.new_app_update_icon3, "field 'update_icon3'", ImageView.class);
    target.update_icon4 = Utils.findRequiredViewAsType(source, R.id.new_app_update_icon4, "field 'update_icon4'", ImageView.class);
    target.update_icon5 = Utils.findRequiredViewAsType(source, R.id.new_app_update_icon5, "field 'update_icon5'", ImageView.class);
    target.update_icon6 = Utils.findRequiredViewAsType(source, R.id.new_app_update_icon6, "field 'update_icon6'", ImageView.class);
    target.update_icon7 = Utils.findRequiredViewAsType(source, R.id.new_app_update_icon7, "field 'update_icon7'", ImageView.class);
    target.update_icon8 = Utils.findRequiredViewAsType(source, R.id.new_app_update_icon8, "field 'update_icon8'", ImageView.class);
    target.toolbar = Utils.findRequiredViewAsType(source, R.id.fragment_toolbar, "field 'toolbar'", FragmentToolbar.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.appPoolRecyclerView = null;
    target.header = null;
    target.new_app_name1 = null;
    target.new_app_name2 = null;
    target.new_app_name3 = null;
    target.new_app_name4 = null;
    target.new_app_name5 = null;
    target.new_app_name6 = null;
    target.new_app_name7 = null;
    target.new_app_name8 = null;
    target.new_app_icon1 = null;
    target.new_app_icon2 = null;
    target.new_app_icon3 = null;
    target.new_app_icon4 = null;
    target.new_app_icon5 = null;
    target.new_app_icon6 = null;
    target.new_app_icon7 = null;
    target.new_app_icon8 = null;
    target.installed_icon1 = null;
    target.installed_icon2 = null;
    target.installed_icon3 = null;
    target.installed_icon4 = null;
    target.installed_icon5 = null;
    target.installed_icon6 = null;
    target.installed_icon7 = null;
    target.installed_icon8 = null;
    target.update_icon1 = null;
    target.update_icon2 = null;
    target.update_icon3 = null;
    target.update_icon4 = null;
    target.update_icon5 = null;
    target.update_icon6 = null;
    target.update_icon7 = null;
    target.update_icon8 = null;
    target.toolbar = null;

    view2131755938.setOnClickListener(null);
    view2131755938 = null;
    view2131755942.setOnClickListener(null);
    view2131755942 = null;
    view2131755946.setOnClickListener(null);
    view2131755946 = null;
    view2131755950.setOnClickListener(null);
    view2131755950 = null;
    view2131755954.setOnClickListener(null);
    view2131755954 = null;
    view2131755958.setOnClickListener(null);
    view2131755958 = null;
    view2131755962.setOnClickListener(null);
    view2131755962 = null;
    view2131755966.setOnClickListener(null);
    view2131755966 = null;

    this.target = null;
  }
}
