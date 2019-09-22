package com.vboss.okline.base.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.view.widget.OKCardView;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/4/13 9:40 <br/>
 * Summary  : fragment toolbar
 * <p>
 * <p>
 * for example
 * === ----------------------------
 * ===
 * ===  <com.vboss.okline.base.helper.FragmentToolbar
 * ===          android:layout_width="match_parent"
 * ===          android:layout_height="wrap_content"/>
 * ===
 * === --------------------------------
 */

public class FragmentToolbar extends FrameLayout {
    private static final String TAG = FragmentToolbar.class.getSimpleName();
    ImageButton navigation;
    ImageButton actionMenu;
    TextView actionTitle;
    LogoView actionLogo;   //modify by wangshuai 2017-06-20
    Context mContext;
    RelativeLayout navigationLayout;
    RelativeLayout actionMenuLayout;
    //Added by wangshuai 2017-06-21 battery capacity view
    OKCardView okCardView;

    public FragmentToolbar(@NonNull Context context) {
        this(context, null);
    }

    public FragmentToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View convertView = LayoutInflater.from(context).inflate(R.layout.app_fragment_toolbar, this, false);
        navigation = (ImageButton) convertView.findViewById(R.id.action_back);
        actionMenu = (ImageButton) convertView.findViewById(R.id.action_menu_button);
        actionTitle = (TextView) convertView.findViewById(R.id.action_title);
        //modify by wangshuai 2017-06-20
        actionLogo = (LogoView) convertView.findViewById(R.id.fragment_toolbar_logo);
        navigationLayout = (RelativeLayout) convertView.findViewById(R.id.action_back_layout);
        actionMenuLayout = (RelativeLayout) convertView.findViewById(R.id.action_menu_layout);
        //Added by wangshuai 2017-06-21 battery capacity view
        okCardView = (OKCardView) convertView.findViewById(R.id.ocardView);

        addView(convertView);
    }

    /**
     * set ActionMenu Visibility
     *
     * @param visibility int
     *                   GONE
     *                   VISIBLE
     *                   INVISIBLE
     */
    public void setActionMenuVisible(int visibility) {
        actionMenu.setVisibility(visibility);
    }

    /**
     * set ActionMenu Icon
     *
     * @param resId image resource id
     */
    public void setActionMenuIcon(int resId) {
        actionMenu.setImageResource(resId);
    }

    /**
     * set Navigation Icon
     *
     * @param resId image resource id
     */
    public void setNavigationIcon(int resId) {
        navigation.setImageResource(resId);
    }

    /**
     * set navigation visible
     *
     * @param visibility GONE
     *                   VISIBLE
     *                   INVISIBLE
     */
    public void setNavigationVisible(int visibility) {
        navigation.setVisibility(visibility);
    }

    /**
     * set Action Title
     *
     * @param resId string resource id
     */
    public void setActionTitle(int resId) {
        setActionTitle(mContext.getResources().getString(resId));
    }

    /**
     * set Action Title
     *
     * @param title string
     */
    public void setActionTitle(String title) {
        actionTitle.setText(title);
    }

    /**
     * set Action Logo visible
     *
     * @param visibility GONE
     *                   VISIBLE
     *                   INVISIBLE
     */
    public void setActionLogoVisible(int visibility) {
        actionLogo.setVisibility(visibility);
    }

    /**
     * set Action Logo clickable
     *
     * @param clickable is clickable
     */
    public void setActionMenuClickable(boolean clickable) {
        actionMenuLayout.setEnabled(clickable);
        actionMenuLayout.setClickable(clickable);
    }

    /**
     * set Action Logo Icon
     *
     * @param resId image resource id
     */
    public void setActionLogoIcon(int resId) {
        //modify by wangshuai 2017-06-20
        actionLogo.setActionbarLogo(resId);
    }

    /**
     * Added by wangshuai 2017-06-20
     * setting ocard state
     * state is OCARD_BIND,the ocard is bind
     * state is OCARD_NO_BIND,the ocard is not bind
     *
     * @param state int
     *              {@link LogoView#OCARD_BIND}
     *              {@link LogoView#OCARD_NO_BIND}
     * @deprecated Use BroadcastReceiver {@link LogoView#receiver}
     */
    @Deprecated
    public void setOCardState(int state) {
        actionLogo.setOCardState(state);
    }

    /**
     * Added by wangshuai 2017-06-20
     * setting ocard connected
     * connected is true ocard connect bluetooth
     * connected is false ocard connect bluetooth
     *
     * @param connected boolean
     * @deprecated Use BroadcastReceiver {@link LogoView#receiver}
     */
    @Deprecated
    public void setOCardConnected(boolean connected) {
        if (connected) {
            actionLogo.oCardContacted();
        } else {
            actionLogo.oCardOutContacted();
        }
    }

    /**
     * set navigation click listener
     *
     * @param listener OnNavigationClickListener
     */
    public void setOnNavigationClickListener(final OnNavigationClickListener listener) {
        navigationLayout.setEnabled(true);
        navigationLayout.setClickable(true);
        navigationLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onNavigation(v);
                } else {
                    Log.e(TAG, "OnNavigationClickListener is null");
                }
            }
        });
    }

    /**
     * set action menu click listener
     *
     * @param listener OnActionMenuClickListener
     */
    public void setOnActionMenuClickListener(final OnActionMenuClickListener listener) {
        actionMenuLayout.setClickable(true);
        actionMenuLayout.setEnabled(true);
        actionMenuLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onActionMenu(v);
                } else {
                    Log.e(TAG, "OnActionMenuClickListener is null");
                }
            }
        });
    }

    /**
     * Action Navigation Click Listener
     */
    public interface OnNavigationClickListener {
        void onNavigation(View v);
    }

    public interface OnActionMenuClickListener {
        void onActionMenu(View v);
    }

    /**
     * Added by wangshuai 2017-06-21 get okCardView and setting battery capacity show
     *
     * @return OKCardView {@link OKCardView}
     * @deprecated {@link OKCardView}
     */
    public OKCardView getOkCardView() {
        return okCardView;
    }

    /**
     * Added by wangshuai 2017-06-21
     * setting OkCardView visible
     *
     * @param visible int
     *                {@link View#VISIBLE}
     *                {@link View#INVISIBLE}
     *                {@link View#GONE}
     */
    public void setOkCardViewVisible(int visible) {
        okCardView.setVisibility(visible);
    }
}
