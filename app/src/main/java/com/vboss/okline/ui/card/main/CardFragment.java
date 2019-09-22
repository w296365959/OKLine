package com.vboss.okline.ui.card.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.data.entities.CardEntity;
import com.vboss.okline.data.entities.CardType;
import com.vboss.okline.ui.card.adapter.CardAdapter;
import com.vboss.okline.ui.card.log.CardLogFragment;
import com.vboss.okline.ui.card.query.CardQueryContact;
import com.vboss.okline.ui.card.query.CardSearchFragment;
import com.vboss.okline.ui.card.widget.LogoView;
import com.vboss.okline.ui.card.widget.SliderListView;
import com.vboss.okline.ui.card.widget.SliderView;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.scanner.QRCodeActivity;
import com.vboss.okline.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import timber.log.Timber;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: WangShuai <br/>
 * Email : wangshuai@okline.cn <br/>
 * Date  : 2017/3/28 19:51 <br/>
 * Summary  : 卡证主界面
 */

public class CardFragment extends BaseFragment implements CardContact.ICardView {
    private static final String TAG = CardFragment.class.getSimpleName();
    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.tv_floating)
    TextView tv_floating;
    @BindView(R.id.sliderListView)
    SliderListView sliderListView;

    @BindView(R.id.layout_card_empty)
    LinearLayout layout_card_empty;

    //Added by wangshuai 2017-05-03 toolbar is update
    @BindView(R.id.btn_scanner)
    ImageButton btn_scanner;
    @BindView(R.id.btn_receivables)
    ImageButton btn_receivables;
    @BindView(R.id.action_menu_button)
    ImageButton btn_search;
    //Added by wangshuai 2017-06-06 add add_icon imageButton
    @BindView(R.id.ib_drop_add)
    ImageButton ib_drop_add;

    //Added by wangshuai 2017-05-25 empty card tip message
    @BindView(R.id.tv_card_empty)
    TextView tv_card_empty;

    @BindView(R.id.logoView)
    LogoView logoView;

    private static final String TAB_COMMON = "tab_common";
    private static final String TAB_MEMBER = "tab_member";
    private static final String TAB_BANK = "tab_bank";
    private static final String TAB_WORK = "tab_work";
    private static final String TAB_CERTIFICATES = "tab_certificates";
    private static final String TAB_TRAVEL = "tab_travel";
    private static final String TAB_ACCESS = "tab_access";

    public static final int REQUEST_CODE_PAY = 0x011;

    MainActivity activity;
    LayoutInflater inflater;
    List<CardEntity> mData;
    int index = 0;

    private int[] titles = new int[]{
            R.string.card_tab_common, R.string.card_tab_member,
            R.string.card_tab_bank, R.string.card_tab_work,
            R.string.card_tab_certificates, R.string.card_tab_travel,
            R.string.card_tab_access
    };
    private int[] tabIcons = new int[]{
            R.mipmap.tab_card_common, R.mipmap.tab_card_member,
            R.mipmap.tab_card_bank, R.mipmap.tab_card_work,
            R.mipmap.tab_card_certificates, R.mipmap.tab_card_travel,
            R.mipmap.tab_card_access
    };

    CardPresenter presenter;
    CardAdapter cardAdapter;

    //Added by wangshuai 2017-06-06 drop down window
    private PopupWindow popupWindow;
    CardSearchFragment cardSearchFragment;
    private boolean isBluetoothConnecting;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View convertView = inflater.inflate(R.layout.fragment_card, container, false);
        ButterKnife.bind(this, convertView);
        return convertView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        inflater = LayoutInflater.from(activity);
        cardSearchFragment = CardSearchFragment.newInstance(CardQueryContact.MODE_QUERY_CARD, null, CardType.ALL);
        //modify by wangshuai 2017-06-06 init drop down window
        initPopWindow();
        initToolbar();
        initTabLayout();
        initData();
        //logoView click listener
        logoView.setOnClickListener(activity);
    }

    private void initData() {
        presenter = new CardPresenter(activity, this);
        mData = new ArrayList<>();
        cardAdapter = new CardAdapter(activity, mData);
        cardAdapter.setCardPresenter(presenter);
        sliderListView.setAdapter(cardAdapter);
        sliderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    // ListView onItemClick if sliderView if open ,should be first close
                    SliderView sliderView = (SliderView) sliderListView.getChildAt(position - sliderListView.getFirstVisiblePosition());
                    if (sliderView != null && sliderView.open()) {
                        sliderView.reset();
                        return;
                    }
                    if (position < mData.size()) {
                        CardEntity cardEntity = mData.get(position);
                        //modify by wangshuai 2017-05-25 CREDENTIALS type don't click
                        if (cardEntity != null && cardEntity.cardMainType() != CardType.CREDENTIALS) {
                            activity.addSecondFragment(CardLogFragment.newInstance(cardEntity));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        sliderListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mData.size() == 0) {
                    return;
                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    tv_floating.setVisibility(View.INVISIBLE);
                } else {
                    tv_floating.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                tv_floating.setText((firstVisibleItem + visibleItemCount) + "/" + totalItemCount);
            }
        });
        //request card list
        queryCards(CardType.COMMON_CARD);
    }

    /**
     * init toolbar
     */
    private void initToolbar() {
        //modify by wangshuai 2017-05-03 add qr code scanner and receivables
        // qr code scanner
        btn_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, QRCodeActivity.class));
            }
        });
        // receivables
        btn_receivables.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                activity.addSecondFragment(CardPayMethodFragment.newInstance());
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //modify by wangshuai 2017-06-05 update card log query UI, use DialogFragment
                if (!cardSearchFragment.isResumed()) {
                    cardSearchFragment.show(getChildFragmentManager(), cardSearchFragment.getClass().getSimpleName());
                }
            }
        });
        //Added by wangshuai 2017-06-06 add icon add imageButton Click Listener
        ib_drop_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.showAsDropDown(v, DensityUtil.dip2px(getActivity(), 5), 0);
            }
        });
    }

    /**
     * Added by wangshuai 2017-06-06
     * init drop down popWindow
     */
    private void initPopWindow() {
        View pop_convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_card_search_pop, null);
        popupWindow = new PopupWindow(pop_convertView, DensityUtil.dip2px(getActivity(), 120),
                DensityUtil.dip2px(getActivity(), 100));
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.setOutsideTouchable(true);
        pop_convertView.findViewById(R.id.tv_pop_receivables).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                //收、付款
//                activity.addSecondFragment(CardPayMethodFragment.newInstance());
            }
        });
        pop_convertView.findViewById(R.id.tv_pop_scanner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                activity.startActivity(new Intent(activity, QRCodeActivity.class));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    /**
     * init tab
     */
    private void initTabLayout() {
        try {
            Log.i(TAG, "mTabLayout : " + mTabLayout);
            mTabLayout.addTab(mTabLayout.newTab().setTag(TAB_COMMON).setCustomView(createTabView(0)));
            mTabLayout.addTab(mTabLayout.newTab().setTag(TAB_MEMBER).setCustomView(createTabView(1)));
            mTabLayout.addTab(mTabLayout.newTab().setTag(TAB_BANK).setCustomView(createTabView(2)));
            mTabLayout.addTab(mTabLayout.newTab().setTag(TAB_WORK).setCustomView(createTabView(3)));
            mTabLayout.addTab(mTabLayout.newTab().setTag(TAB_CERTIFICATES).setCustomView(createTabView(4)));
            mTabLayout.addTab(mTabLayout.newTab().setTag(TAB_TRAVEL).setCustomView(createTabView(5)));
            mTabLayout.addTab(mTabLayout.newTab().setTag(TAB_ACCESS).setCustomView(createTabView(6)));
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    String tag = (String) tab.getTag();
                    Log.i(TAG, "tab tag :" + tag);
                    if (TextUtils.isEmpty(tag)) {
                        Log.e(TAG, "tab tag is null");
                        return;
                    }
                    Subscription subscription = presenter.queryCardSubscription;
                    if (subscription != null && subscription.isUnsubscribed()) {
                        presenter.queryCardSubscription.unsubscribe();
                        Timber.tag(TAG).i("cancel query cards Subscription");
                    }
                    //modify by wangshuai clear data adapter notifyDataSetChanged
                    mData.clear();
                    cardAdapter.notifyDataSetChanged();
                    index = tab.getPosition();
                    switch (tag) {
                        case TAB_COMMON:
                            queryCards(CardType.COMMON_CARD);
                            break;
                        case TAB_BANK:
                            queryCards(CardType.BANK_CARD);
                            break;
                        case TAB_WORK:
                            queryCards(CardType.EMPLOYEE_CARD);
                            break;
                        case TAB_CERTIFICATES:
                            queryCards(CardType.CREDENTIALS);
                            break;
                        case TAB_TRAVEL:
                            queryCards(CardType.TRANS_CARD);
                            break;
                        case TAB_ACCESS:
                            queryCards(CardType.DOOR_CARD);
                            break;
                        case TAB_MEMBER:
                            queryCards(CardType.VIP_CARD);
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * according to card type query card list
     *
     * @param cardType card type
     */
    private void queryCards(int cardType) {
        Timber.tag(TAG).i("card type : %s", cardType);
        presenter.queryCards(cardType);
        layout_card_empty.setVisibility(View.INVISIBLE);
        mData.clear();
        cardAdapter.notifyDataSetChanged();
    }

    /**
     * create tab View
     *
     * @param position 位置
     * @return tabView   tab View
     */
    private View createTabView(int position) {
        View tabView = inflater.inflate(R.layout.layout_card_tab, null);
        ImageView iv_tab_icon = (ImageView) tabView.findViewById(R.id.iv_tab_icon);
        TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
        iv_tab_icon.setImageResource(tabIcons[position]);
        tv_tab_title.setText(activity.getResources().getString(titles[position]));
        return tabView;
    }

    @Override
    public void updateData(List<CardEntity> data) {
        mData.clear();
        if (data != null) {
            mData.addAll(data);
        }
        if (cardAdapter != null) {
            Timber.tag(TAG).i(" cardAdapter notifyDataSetChanged()");
            cardAdapter.notifyDataSetChanged();
        }
        if (mData.size() > 0) {
            layout_card_empty.setVisibility(View.INVISIBLE);
            //listView scroll to top
            sliderListView.setSelection(0);
        } else {
            layout_card_empty.setVisibility(View.VISIBLE);
            tv_card_empty.setText(String.format(activity.getResources().getString(R.string.card_empty),
                    activity.getResources().getString(titles[index])));
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PAY) {
            if (cardAdapter != null) {
                Timber.tag(TAG).i("you should be refresh cardAdapter");
                cardAdapter.notifyDataSetChanged();
            }
        }
    }
}
