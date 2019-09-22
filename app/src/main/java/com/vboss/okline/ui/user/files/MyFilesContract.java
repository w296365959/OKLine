package com.vboss.okline.ui.user.files;

import com.vboss.okline.base.BasePresenter;
import com.vboss.okline.base.BaseView;
import com.vboss.okline.data.model.CloudFileModel;

import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author  : Zheng Jun
 * Email   : zhengjun@okline.cn
 * Date    : 2017/7/7
 * Summary : 在这里描述Class的主要功能
 */

public interface MyFilesContract {
    interface View extends BaseView{
        void showFiles(List<CloudFileModel> fileModels);
    }

    abstract class Presenter extends BasePresenter{
        abstract void getDocumentList(CloudFileFetchListener listener);
        abstract void getSmsList(CloudFileFetchListener listener);
        abstract void getMailList(CloudFileFetchListener listener);
        abstract void onDestroy();
    }
}

interface CloudFileFetchListener {
    void onFetch(List<CloudFileModel> files);
    void onFail(String message);
}
