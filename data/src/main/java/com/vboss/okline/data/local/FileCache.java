package com.vboss.okline.data.local;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/13 <br/>
 * Summary : 保存数据到文件并加密
 */
public class FileCache {
    private static final String TAG = FileCache.class.getSimpleName();
    private static File rootFile;
    private static FileCache instance;

    public static FileCache getDefaultFileCache(@NonNull String dirToCache) {
        if (rootFile != null && rootFile.getAbsolutePath().equals(dirToCache)) {
            return instance;
        } else {
            instance = new FileCache(dirToCache);
            return instance;
        }
    }

    private FileCache(String dirToCache) {
        rootFile = new File(dirToCache);
        if (!rootFile.exists()) {
            if (!rootFile.mkdirs()) {
                throw new IllegalStateException("Fail to make directory " + rootFile);
            }
        }

        Timber.tag(TAG).i("File path to cache : %s", dirToCache);
    }


    /**
     * 写数据
     *
     * @param fileName 待写入的文件名
     * @param content  待写入内容
     */
    public void write(@NonNull String fileName, @NonNull String content) {
        try {
            File target = new File(rootFile, fileName);
            if (!target.exists()) {
                if (!target.createNewFile()) {
                    Timber.tag(TAG).w("Fail create cache file");
                    return;
                }
            }
            FileWriter writer = new FileWriter(new File(rootFile, fileName));
//            writer.write(EncryptUtils.encodeBase64(content));
            writer.write(content);
            writer.close();
        } catch (IOException e) {
            Timber.tag(TAG).e(e);
        }
    }

    /**
     * 读数据
     *
     * @param fileName 待读取的文件名
     * @return
     */
    @CheckResult
    public String read(@NonNull String fileName) {
        Timber.tag(TAG).w("Read data from %s", new File(rootFile, fileName));
        try {
            File target = new File(rootFile, fileName);
            if (!target.exists()) {
                Timber.tag(TAG).w("%s not found", target);
                return null;
            }
            FileReader reader = new FileReader(new File(rootFile, fileName));
            BufferedReader br = new BufferedReader(reader);
            StringBuilder sb = new StringBuilder();
            while (br.read() != -1) {
                sb.append(br.readLine());
            }
            Timber.tag(TAG).i("%s not found", sb.toString());
            reader.close();
            br.close();
            return sb.toString();
//            return EncryptUtils.decodeBase64(sb.toString());
        } catch (IOException e) {
            Timber.tag(TAG).e(e);
        }
        return null;
    }

}
