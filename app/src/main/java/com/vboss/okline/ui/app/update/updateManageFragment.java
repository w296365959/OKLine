package com.vboss.okline.ui.app.update;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vboss.okline.R;
import com.vboss.okline.data.entities.AppEntity;
import com.vboss.okline.ui.app.adapter.CommonAdapter;
import com.vboss.okline.ui.app.adapter.ViewHolder;
import com.vboss.okline.ui.home.MainActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/3/28 9:51 <br/>
 * Summary  : App更新管理界面
 */

public class updateManageFragment extends Fragment implements UpdateAppContract.View{
    public static final String TAG = "updateManageFragment";

    @BindView(R.id.update_recyclerview)
    RecyclerView updateRecyclerview;

    @BindView(R.id.need_update_num)
    TextView need_update_num;

    MainActivity activity;

    List<AppEntity> updateList;
    CommonAdapter<AppEntity> adapter;
    UpdateAppPresenter presenter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_manage, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        activity = (MainActivity) getActivity();
        updateList = new ArrayList<>();
        presenter = new UpdateAppPresenter(activity,this);
        presenter.getUpdateApp(activity);
        initAdapter();
    }

    private void initAdapter() {
        adapter = new CommonAdapter<AppEntity>(getActivity(),R.layout.app_item_layout,updateList) {
            @Override
            public void convert(ViewHolder holder, AppEntity app, int position) {
                holder.setVisible(R.id.update_button,true);
                holder.setImageByUrl(R.id.app_icon,app.appIcon());
                holder.setText(R.id.app_name,app.appName());
                holder.setOnClickListener(R.id.update_button, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO: 2017/4/1 更新效果
                    }
                });
            }
        };
        updateRecyclerview.setLayoutManager(new GridLayoutManager(getActivity(),4));
        updateRecyclerview.setAdapter(adapter);
    }

    @OnClick({R.id.update_back,R.id.all_update})
    public void ViewOnClick(View view){
        switch (view.getId()){
            case R.id.update_back:
                activity.removeSecondFragment();
                break;
            case R.id.all_update:

                break;
            default:
                break;
        }
    }

    @Override
    public void showUpdateApp(List<AppEntity> list) {
        updateList.clear();
        if (list !=null && list.size() > 0) {
            updateList.addAll(list);
            adapter.setmDatas(updateList);
            adapter.notifyDataSetChanged();
            need_update_num.setText(String.valueOf(updateList.size()));
        }else {
            need_update_num.setText("0");
        }
    }

//    private void initToolBar() {
//        activity = (MainActivity) getActivity();
//        if (activity == null) {
//            throw new NullPointerException("activity is null");
//        }
//        toolbarHelper = new ToolbarHelper(activity);
//        toolbarHelper.showToolbar(R.string.update_manage,true);
//        toolbarHelper.showMenuButton(R.string.all_update,true);
//        //"全部更新"按钮点击
//        toolbarHelper.setMenuButtonClickListener(new ToolbarHelper.OnMenuButtonClickListener() {
//            @Override
//            public void onMenuClick() {
//
//            }
//        });
//        toolbarHelper.setNavigationListener(new ToolbarHelper.OnNavigationClickListener() {
//            @Override
//            public void onNavigationClick() {
//                // 返回一级界面
//                activity.removeSecondFragment();
//            }
//        });
//    }


}
