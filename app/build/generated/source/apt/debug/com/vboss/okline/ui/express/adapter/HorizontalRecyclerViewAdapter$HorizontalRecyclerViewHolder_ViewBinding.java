// Generated code from Butter Knife. Do not modify!
package com.vboss.okline.ui.express.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.vboss.okline.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class HorizontalRecyclerViewAdapter$HorizontalRecyclerViewHolder_ViewBinding<T extends HorizontalRecyclerViewAdapter.HorizontalRecyclerViewHolder> implements Unbinder {
  protected T target;

  @UiThread
  public HorizontalRecyclerViewAdapter$HorizontalRecyclerViewHolder_ViewBinding(T target, View source) {
    this.target = target;

    target.expressTypeImgIv = Utils.findRequiredViewAsType(source, R.id.express_type_img_iv, "field 'expressTypeImgIv'", ImageView.class);
    target.expressAddressTv = Utils.findRequiredViewAsType(source, R.id.express_address_tv, "field 'expressAddressTv'", TextView.class);
    target.expressCompanyTv = Utils.findRequiredViewAsType(source, R.id.express_company_tv, "field 'expressCompanyTv'", TextView.class);
    target.expressUsernameTv = Utils.findRequiredViewAsType(source, R.id.express_username_tv, "field 'expressUsernameTv'", TextView.class);
    target.itemExpressCompanyNewIv = Utils.findRequiredViewAsType(source, R.id.item_express_company_new_iv, "field 'itemExpressCompanyNewIv'", ImageView.class);
    target.expressAddressAndCompanyFl = Utils.findRequiredViewAsType(source, R.id.express_address_and_company_fl, "field 'expressAddressAndCompanyFl'", FrameLayout.class);
    target.itemExpressCompanyLl = Utils.findRequiredViewAsType(source, R.id.item_express_company_ll, "field 'itemExpressCompanyLl'", LinearLayout.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    T target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");

    target.expressTypeImgIv = null;
    target.expressAddressTv = null;
    target.expressCompanyTv = null;
    target.expressUsernameTv = null;
    target.itemExpressCompanyNewIv = null;
    target.expressAddressAndCompanyFl = null;
    target.itemExpressCompanyLl = null;

    this.target = null;
  }
}
