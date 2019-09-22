package com.vboss.okline.ui.contact.myCard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/6 11:50
 * Desc :
 */

public class ChooseProvinceFragment extends BaseFragment {

    @BindView(R.id.lv_choose_province)
    ListView lvChooseProvince;
    Unbinder unbinder;
    private ChooseAddressActivity activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (ChooseAddressActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_province_fragment, null);


        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditAddressAdapter adapter = new EditAddressAdapter(activity, activity.mProvinceDatas);
        lvChooseProvince.setAdapter(adapter);

        lvChooseProvince.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("adapterView = [" + adapterView + "], view = [" + view + "], i = [" + i + "], l = [" + l + "]");
                //进入下个页面 将activity里的省改变
                activity.selectedProvince = activity.mProvinceDatas[i];
                activity.viewPager.setCurrentItem(1);
                Timber.tag(TAG).i("onitemClick:" + activity.mProvinceDatas[i] + "l:");
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
