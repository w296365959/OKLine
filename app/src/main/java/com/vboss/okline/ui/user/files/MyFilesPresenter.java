package com.vboss.okline.ui.user.files;

import android.content.Context;

import com.vboss.okline.data.model.CloudFileModel;

import java.util.ArrayList;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/7/7
 * Summary : 在这里描述Class的主要功能
 */

public class MyFilesPresenter extends MyFilesContract.Presenter {
    private Context context;

    public MyFilesPresenter(Context context) {
        this.context = context;
    }

    @Override
    public void onAttached() {

    }

    @Override
    void getDocumentList(CloudFileFetchListener listener) {
        ArrayList<CloudFileModel> cloudFileModels = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            cloudFileModels.add(new CloudFileModel("00"+i,"文件名"+i,"","2017-07-07","PDF",(121+i)+"KB","http://img0.imgtn.bdimg.com/it/u=1283728083,3152195131&fm=26&gp=0.jpg","aduifweoie3"));
        }
        listener.onFetch(cloudFileModels);
    }

    @Override
    void getSmsList(CloudFileFetchListener listener) {

    }

    @Override
    void getMailList(CloudFileFetchListener listener) {

    }

    @Override
    void onDestroy() {

    }
}
