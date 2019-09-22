package com.vboss.okline.data.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;

import java.util.concurrent.Callable;

import rx.Observable;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/13 <br/>
 * Summary : OSS服务器
 */
public class AliOSSClient {
    private static final String TAG = AliOSSClient.class.getSimpleName();
    private static final String OL_ALI_BUCKET = "okline-down";
    private static final String PATH_AVATAR = "avatar";
    private static final String PATH_CERTIFICATION = "certification";
    private static final String OSS_ENDPOINT_UPLOAD = "http://oss-cn-hangzhou.aliyuncs.com";
    private static final String OSS_ENDPOINT_DOWNLOAD = "http://down.okline.com.cn";
    private OSSClient aliOss;
    private static AliOSSClient instance;

    public static AliOSSClient getInstance(Context context) {
        if (instance == null) {
            instance = new AliOSSClient(context);
        }
        return instance;
    }

    private AliOSSClient(Context context) {
        OSSCredentialProvider provider = new OSSPlainTextAKSKCredentialProvider("bokSxzDf2PkJI5Kh",
                "gC9gI8U6ZcwNpD62jqhUdZDZTVYGvk");

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        aliOss = new OSSClient(context, OSS_ENDPOINT_UPLOAD, provider);
    }

    /**
     * 上传图像
     *
     * @param fileName 文件名称
     * @param filePath 文件路径
     * @return
     */
    public Observable<String> uploadAvatar(@NonNull String fileName, @NonNull String filePath) {
        return uploadFile(PATH_AVATAR, fileName, filePath);
    }

    /**
     * 上传证书图片
     *
     * @param fileName 文件名
     * @param filePath 文件路径
     * @return
     */
    public Observable<String> uploadCert(@NonNull String fileName, @NonNull String filePath) {
        return uploadFile(PATH_CERTIFICATION, fileName, filePath);
    }

    /**
     * 上传文件
     *
     * @param targetDir      oss服务器上存储文件的目录
     * @param fileName       上传到oss上的文件名
     * @param uploadFilePath 待上传文件的本地访问路径
     * @return
     */
    private Observable<String> uploadFile(@NonNull String targetDir, @NonNull String fileName, @NonNull final String uploadFilePath) {
        final String remoteFilePath = targetDir + "/" + fileName;
        Timber.tag(TAG).i(" RemoteFilePath : %s ", remoteFilePath);
        final PutObjectRequest request = new PutObjectRequest(OL_ALI_BUCKET, remoteFilePath, uploadFilePath);
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                aliOss.putObject(request);
                return OSS_ENDPOINT_DOWNLOAD + "/" + remoteFilePath;
            }
        }).subscribeOn(Schedulers.io());
    }
}
