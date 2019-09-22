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
 * Date : 2017/6/6 11:52
 * Desc :
 */

public class ChooseCityFragment extends BaseFragment {
    @BindView(R.id.lv_choose_city)
    ListView lvChooseCity;
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

        View view = inflater.inflate(R.layout.choose_city_fragment, null);
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
        if (isVisibleToUser && null != lvChooseCity){


            lvChooseCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //进入下个页面 将activity里的市改变
                    activity.selectedCity = activity.updateCity(activity.selectedProvince)[i];
                    String[] area = activity.mAreaDataMap.get(activity.selectedCity);
                    if (area != null){
                        activity.viewPager.setCurrentItem(2);
                    }else{
                        if (activity.key.equals("EditMyWorkCard")){
                            RxBus.get().post(EventToken.CHOOSEAREA_WORKCARD,activity.selectedProvince+activity.selectedCity);
                        }else{
                            RxBus.get().post(EventToken.CHOOSEAREA_DELIVERY,activity.selectedProvince+activity.selectedCity);
                        }

                        activity.finish();
                    }

                    Timber.tag(TAG).i("onitemClick:" + activity.updateCity(activity.selectedProvince)[i]);
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
