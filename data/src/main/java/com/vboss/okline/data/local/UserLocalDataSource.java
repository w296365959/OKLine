package com.vboss.okline.data.local;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.vboss.okline.data.GsonUtils;
import com.vboss.okline.data.entities.OKCard;
import com.vboss.okline.data.entities.User;

import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/13 <br/>
 * Summary : 用户数据本地入口
 */
public class UserLocalDataSource {
    private static final String TAG = UserLocalDataSource.class.getSimpleName();
    private LoadingCache<String, Object> cache;
    private static UserLocalDataSource instance;
    private static final String KEY_USER = "user";
    private static final String KEY_OLNO = "olno";
    private static final String KEY_OKCARD = "ok_card";
//    private final FileCache fileCache;

    public static UserLocalDataSource getInstance() {
        if (instance == null) {
            instance = new UserLocalDataSource();
        }
        return instance;
    }

    private UserLocalDataSource() {

        cache = CacheBuilder.newBuilder()
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(@NonNull String key) throws Exception {
                        return getDataFromLocal(key);
                    }
                });
    }

    /**
     * 获取本地数据
     *
     * @param key
     * @return
     */
    private Object getDataFromLocal(String key) {
        Timber.tag(TAG).w("Get data from local by %s", key);
        switch (key) {
            case KEY_USER:
                return SPUtils.get(KEY_USER, User.class);
            case KEY_OLNO:
                return SPUtils.get(KEY_OLNO, String.class);
            case KEY_OKCARD:
                return SPUtils.get(KEY_OKCARD, OKCard.class);
            default:
                return null;
        }

    }

    /**
     * Cache user info
     *
     * @param user
     */
    public void setUser(@NonNull User user) {
        put(KEY_USER, user);
        Timber.tag(TAG).i("Get cached user ： %s", GsonUtils.toJson(getUser()));
    }

    /**
     * 缓存欧乐号
     */
    public void setOlNo(@NonNull String olNo) {
        put(KEY_OLNO, olNo);
    }

    /**
     * 缓存欧卡对象
     *
     * @param card
     */
    public void setOKCard(@NonNull OKCard card) {
        put(KEY_OKCARD, card);
    }

    public void put(String key, Object data) {
        SPUtils.put(key, data);
        cache.put(key, data);
    }

    /**
     * 获取欧乐号
     */
    @CheckResult
    public String getOlNo() {
       /* try {
            return (String) cache.get(KEY_OLNO);
        } catch (Exception e) {
            Timber.tag(TAG).e("No olNo is cached");
        }
        return null;*/
        return DataConfig.DEFAULT_OLNO;
    }

    /**
     * Get cached user if present, else return null.
     */
    @CheckResult
    public User getUser() {
      /*  try {
            return (User) cache.get(KEY_USER);
        } catch (Exception e) {
            Timber.tag(TAG).i("No user is cached");
        }
        return null;*/
        User user = DataConfig.DEFAULT_USER;
        user.setIdcardNo("421087198706088852");
        return user;
    }

    /**
     * Config Device info
     *
     * @param deviceNo  设备号
     * @param btName    蓝牙名称
     * @param btAddress 蓝牙地址
     */
    public void setDeviceInfo(@NonNull String deviceNo, @NonNull String btName, @NonNull String btAddress) {
        User user = getUser();
        if (user != null) {
            user.setDeviceNo(deviceNo);
            user.setBhtName(btName);
            user.setBhtAddress(btAddress);
            setUser(user);
            Timber.tag(TAG).i("Success to set device info[%s,%s,%s]", deviceNo, btName, btAddress);
        }
    }

    /**
     * 设置用户头像
     *
     * @param avatar 头像Url
     */
    public void updateAvatar(@Nullable String avatar) {
        User user = getUser();
        if (user != null) {
            user.setAvatar(avatar);
            setUser(user);
        }
    }

    /**
     * 获取本地换层的欧卡对象
     *
     * @return 欧卡对象
     */
    public OKCard getOCard() {
        try {
            return (OKCard) cache.get(KEY_OKCARD);
        } catch (Exception e) {
            Timber.tag(TAG).e("No oCard is cached");
        }
        return null;
    }

}
