package com.vboss.okline.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/13 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class Downloader {

    public static Observable<File> downLoadFile(final String fileUrl, final String fileName, final ProgressListener progressListener) {
        return Observable.create(new Observable.OnSubscribe<Response>() {
            @Override
            public void call(Subscriber<? super Response> subscriber) {
                OkHttpClient client = new OkHttpClient.Builder().build();
                Request request = new Request.Builder().url(fileUrl).build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        subscriber.onNext(response);
                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).flatMap(new Func1<Response, Observable<File>>() {
            @Override
            public Observable<File> call(Response response) {
                try {
                    final File file = new File(fileName);
                    long total = response.body().contentLength();
                    if (file.exists()) {
                        if (file.length() == total) {
                            progressListener.update(total, total);
                            return Observable.just(file);
                        } else {
                            file.delete();
                        }
                    }
                    InputStream inputStream = response.body().byteStream();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    byte[] buffer = new byte[4096];
                    int readSize = 0;
                    long readCount = 0;
                    while ((readSize = inputStream.read(buffer)) != -1) {
                        readCount += readSize;
                        fileOutputStream.write(buffer, 0, readSize);
                        Timber.tag("downLoadFile").d("readCount = " + readCount + " , total = " + total);
                        progressListener.update(readCount, total);
                    }
                    inputStream.close();
                    fileOutputStream.close();
                    response.body().close();
                    return Observable.just(file);
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        });
    }

    public interface ProgressListener {
        void update(long bytesRead, long contentLength);
    }
}
