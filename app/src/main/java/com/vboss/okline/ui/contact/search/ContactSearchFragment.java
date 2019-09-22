package com.vboss.okline.ui.contact.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.helper.ToolbarSearchHelper;
import com.vboss.okline.ui.contact.ContactDetail.ContactDetailFragment;
import com.vboss.okline.ui.contact.adapter.ContactListAdapter;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;
import com.vboss.okline.view.widget.ClearEditText;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hyphenate.chat.EMGCMListenerService.TAG;
import static com.vboss.okline.R.id.layout_count;
import static com.vboss.okline.R.id.tv_search_count;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/5 10:03
 * Desc :
 */

public class ContactSearchFragment extends BaseFragment implements ContactSearchContract.View {

    @BindView(R.id.lv_search)
    ListView lvSearch;
    Unbinder unbinder;
    @BindView(R.id.tv_search_count)
    TextView tvSearchCount;
    @BindView(R.id.layout_count)
    LinearLayout layoutCount;
    @BindView(R.id.edit_search)
    ClearEditText editSearch;
    private MainActivity activity;
    private ContactSearchPresenter presenter;
    private ToolbarSearchHelper toolbarHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_contact_search, null);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (MainActivity) getActivity();
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        activity.hideTabs(true);
        presenter = new ContactSearchPresenter(this, new ContactSearchModel(), activity);
        initToolbar();
    }

    private void initToolbar() {
        toolbarHelper = new ToolbarSearchHelper(activity);
        toolbarHelper.enableHomeAsUp(true);
        toolbarHelper.setOnActionSearchListener(new ToolbarSearchHelper.OnActionSearchListener() {
            @Override
            public void onSearch(String key) {
                Log.i(TAG, "key: " + key);
                if (StringUtils.isNullString(key)){
                    ToastUtil.show(activity,R.string.edittext_hint);
                }else{
                    presenter.search(key);
                    TextUtils.showOrHideSoftIM(editSearch,false);
                }
            }
        });
        toolbarHelper.setNavigationListener(new ToolbarSearchHelper.OnNavigationClickListener() {
            @Override
            public void onNavigationClick() {
                TextUtils.showOrHideSoftIM(tvSearchCount, false);
                activity.removeSecondFragment();
                activity.hideTabs(false);
            }
        });
        editSearch.setHint(R.string.edittext_hint);

    }

    @Override
    public void showResult(List<ContactItem> list) {

//        List<Contact> contactList = new ArrayList<>();
//        ContactAdapter adapter = new ContactAdapter(contactList, activity, 1);

        final ContactListAdapter contactListAdapter = new ContactListAdapter(list, activity, 3);
        layoutCount.setVisibility(View.VISIBLE);
        tvSearchCount.setText(String.format(activity.getResources().getString(R.string.card_search),
                String.valueOf(list.size())));
        lvSearch.setAdapter(contactListAdapter);
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                ContactItem item = (ContactItem) contactListAdapter.getItem(position);
                TextUtils.showOrHideSoftIM(tvSearchCount, false);
                activity.addSecondFragment(ContactDetailFragment.newInstance(item.getContactID(),item));
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
