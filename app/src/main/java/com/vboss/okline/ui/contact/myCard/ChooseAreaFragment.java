package com.vboss.okline.ui.contact.myCard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hwangjr.rxbus.RxBus;
import com.vboss.okline.R;
import com.vboss.okline.base.BaseFragment;
import com.vboss.okline.base.EventToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

import static android.content.ContentValues.TAG;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/6/6 11:55
 * Desc :
 */

public class ChooseAreaFragment extends BaseFragment {
    @BindView(R.id.lv_choose_area)
    ListView lvChooseArea;
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

        View view = inflater.inflate(R.layout.choose_area_fragment, null);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && null != lvChooseArea) {
            lvChooseArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //进入下个页面 将activity里的市改变
                    activity.selectedArea = activity.updateArea(activity.selectedCity)[i];
                    Timber.tag(TAG).i("onitemClick:" + activity.updateArea(activity.selectedCity)[i]);
                    Timber.tag(TAG).i("province city area:"+ activity.selectedProvince+activity.selectedCity+activity.selectedArea);
                    //传递省市县数据
                    if (activity.key.equals("EditMyWorkCard")){
                        RxBus.get().post(EventToken.CHOOSEAREA_WORKCARD,activity.selectedProvince+activity.selectedCity+activity.selectedArea);
                    }else{
                        RxBus.get().post(EventToken.CHOOSEAREA_DELIVERY,activity.selectedProvince+activity.selectedCity+activity.selectedArea);
                    }
                    activity.finish();
                }
            });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
