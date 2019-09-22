package com.vboss.okline.ui.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseActivity;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.base.helper.FragmentToolbar;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.adapter.NewFriendAdapter;
import com.vboss.okline.ui.home.MainActivity;
import com.vboss.okline.ui.user.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/5/3 21:33
 * Desc :
 */

public class NewFriendFragment extends BaseFragment {
    private static final String TAG = "NewFriendFragment";

    Unbinder unbinder;
    @BindView(R.id.toolbar_contact)
    FragmentToolbar toolbar;
    @BindView(R.id.lv_new_friend)
    ListView lvNewFriend;
    @BindView(R.id.non_new_friend)
    TextView nonNewFriend;
    private View contentView;
    private MainActivity activity;

    public static Fragment newInstance(int contactID) {
        NewFriendFragment instance = new NewFriendFragment();
        Bundle args = new Bundle();
//        args.putSerializable("obj", contact);
        instance.setArguments(args);
        return instance;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.new_friend_fragment, null);
        activity = (MainActivity) getActivity();
        if (activity == null) {
            throw new NullPointerException("activity is null");
        }
        unbinder = ButterKnife.bind(this, contentView);
        return contentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //隐藏底部导航栏
        activity.hideTabs(true);
        initToolbar();
        getApplicantList();
    }

    private void getApplicantList() {
        ContactRepository.getInstance(activity)
                .getApplicantList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG){
                    @Override
                    public void onNext(List<ContactEntity> list) {
                        Timber.tag(TAG).i("getApplicantList:"+list.size());
                        if (!list.isEmpty()){
                            nonNewFriend.setVisibility(View.GONE);
                            NewFriendAdapter adapter = new NewFriendAdapter(list, activity);
                            lvNewFriend.setAdapter(adapter);
                        }else{
                            nonNewFriend.setVisibility(View.VISIBLE);
                        }

                    }
                });

        //for test
//        List<ContactEntity> list = new ArrayList<>();
//
//        for (int i = 0;i < 10; i++){
//            ContactEntity build = ContactEntity.newBuilder()
//                    .realName("sss" + i)
//                    .build();
//            list.add(build);
//        }
//        NewFriendAdapter adapter = new NewFriendAdapter(list, activity);
//        lvNewFriend.setAdapter(adapter);
    }

    private void initToolbar() {
        toolbar.setActionTitle(getResources().getString(R.string.title_new_friend));
        toolbar.setNavigationVisible(View.VISIBLE);
        toolbar.setActionMenuVisible(View.GONE);
        toolbar.setOnNavigationClickListener(new FragmentToolbar.OnNavigationClickListener() {
            @Override
            public void onNavigation(View v) {
                activity.removeSecondFragment();
                activity.hideTabs(false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
