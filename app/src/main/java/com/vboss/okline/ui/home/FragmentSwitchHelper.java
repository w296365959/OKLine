package com.vboss.okline.ui.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.ui.app.AppFragment;
import com.vboss.okline.ui.card.main.CardFragment;
import com.vboss.okline.ui.contact.ContactsFragment;
import com.vboss.okline.ui.record.RecordFragment;

import java.util.HashMap;

import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/6/19 <br/>
 * Summary : Fragment切换辅助类
 */
public class FragmentSwitchHelper implements IFragmentSwitcher {

    private static final int SIZE_MAIN_FRAGMENT = 4;
    private HashMap<String, BaseFragment> mainFragments;
    private SparseArray<String> mainTags = new SparseArray<>(SIZE_MAIN_FRAGMENT);
    private FragmentManager fragmentManager;
    private final int containerId;
    private final FragmentActivity activity;
    /**
     * Memory current tab tag
     */
    private String currentTab;

    public FragmentSwitchHelper(FragmentActivity activity, int containerId) {
        this.containerId = containerId;
        this.activity = activity;
        fragmentManager = activity.getSupportFragmentManager();
    }

    public void onCreate() {
        initMainFragments();
    }

    public BaseFragment getMainFragment(int index) {
        return mainFragments.get(mainTags.get(index));
    }


    /**
     * 进入下一级
     *
     * @param fClass
     * @param args
     * @param current
     */
    @Override
    public void goNext(Class<? extends Fragment> fClass, Bundle args, Fragment current) {
        String targetTag = fClass.getSimpleName();
        String currentTag = current.getClass().getSimpleName();
        Timber.tag(TAG).i("CurrentTag : %s, targetTag : %s", currentTag, targetTag);
        if (targetTag.equals(currentTag)) {
            return;
        }

        Fragment target = fragmentManager.findFragmentByTag(targetTag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (mainFragments.containsKey(targetTag)) {
            if (targetTag.equals(currentTab)) {
                topBackStack();
            } else {
                clearBackStack();
                if (target == null) {
                    target = mainFragments.get(targetTag);
                }
                transaction.add(containerId, target).addToBackStack(targetTag).commit();
                currentTab = targetTag;
            }

        } else {
            if (target == null) {
                target = Fragment.instantiate(activity, fClass.getName(), args);
                transaction.hide(current).add(containerId, target).addToBackStack(targetTag).commit();
            } else {
                transaction.hide(current).show(target).commit();
            }
        }
    }

    /**
     * 返回上一级
     */
    @Override
    public void goBack() {
        if (fragmentManager.getBackStackEntryCount() != 1) {
            fragmentManager.popBackStackImmediate();
        }
    }

    /**
     * 跳到纵深的顶部
     */
    @Override
    public void goTop() {
        topBackStack();
    }

    private void initMainFragments() {
        if (mainFragments == null) {
            mainFragments = new HashMap<>(SIZE_MAIN_FRAGMENT);
        }
        currentTab = CardFragment.class.getSimpleName();


        mainFragments.put(CardFragment.class.getSimpleName(), new CardFragment());
        mainFragments.put(RecordFragment.class.getSimpleName(), new RecordFragment());
        mainFragments.put(ContactsFragment.class.getSimpleName(), new ContactsFragment());
        mainFragments.put(AppFragment.class.getSimpleName(), new AppFragment());

        mainTags.put(0, CardFragment.class.getSimpleName());
        mainTags.put(1, RecordFragment.class.getSimpleName());
        mainTags.put(2, ContactsFragment.class.getSimpleName());
        mainTags.put(3, AppFragment.class.getSimpleName());

    }

    private void topBackStack() {
        while (fragmentManager.getBackStackEntryCount() != 1) {
            fragmentManager.popBackStackImmediate();
        }
    }

    private void clearBackStack() {
        while (fragmentManager.getBackStackEntryCount() != 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    public void onDestroy() {
        if (mainFragments != null) {
            mainFragments.clear();
            mainFragments = null;
        }
    }


}
