package com.vboss.okline.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vboss.okline.data.model.CloudFileModel;

import java.util.List;

import rx.Observable;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/7/7 <br/>
 * Summary : 云端文件数据接口
 */
public interface CloudFileDataSource {

    /**
     * 查询文件
     *
     * @param keyToSearch
     * @return
     */
    Observable<List<CloudFileModel>> list(@Nullable String keyToSearch);

    /**
     * 获取文件详情
     *
     * @param fileId
     * @return
     */
    Observable<CloudFileModel> detail(@NonNull String fileId);

    /**
     * 删除文件
     *
     * @param fileId
     * @return
     */
    Observable<Boolean> delete(@NonNull String fileId);

    /**
     * 保存文件到云端
     *
     * @param model 文件对象模型
     * @return
     */
    Observable<Boolean> save(@NonNull CloudFileModel model);

}
