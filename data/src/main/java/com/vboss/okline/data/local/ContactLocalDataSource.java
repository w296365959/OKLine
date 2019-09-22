package com.vboss.okline.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.okline.vboss.http.OLException;
import com.vboss.okline.data.ContactDataSource;
import com.vboss.okline.data.IDataSource;
import com.vboss.okline.data.RepoUtils;
import com.vboss.okline.data.StatusFlavor;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static com.vboss.okline.data.entities.ContactModel.TABLE_NAME;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/6 <br/>
 * Summary : 联系人本地数据操作
 */
public class ContactLocalDataSource implements ContactDataSource, IDataSource<ContactEntity> {
    private DBService dbService;
    private static ContactLocalDataSource instance;
    private final Func1<Cursor, ContactEntity> ENTITY_MAPPER;

    public static ContactLocalDataSource getInstance(Context context) {
        if (instance == null) {
            instance = new ContactLocalDataSource(context);
        }

        return instance;
    }

    private ContactLocalDataSource(Context context) {
        dbService = DBService.getInstance(context);
        ENTITY_MAPPER = new Func1<Cursor, ContactEntity>() {
            @Override
            public ContactEntity call(Cursor cursor) {
                return ContactEntity.MAPPER.map(cursor);
            }
        };
    }

    /**
     * 联系人是否已缓存
     */
    public boolean isContactExisting() {
        return dbService.isDataExisting(TABLE_NAME, null);
    }

    @Override
    public Observable<ContactEntity> getContact(int contactId) {
        return dbService.queryOneWithWhere(TABLE_NAME, ENTITY_MAPPER, ContactEntity.FRIENDID + " = ?",
                String.valueOf(contactId));
    }

    /**
     * 一页一页地返回所有的联系人
     *
     * @param pageSize 每页的最大数
     * @return
     */
    public Observable<List<ContactEntity>> getContactsPageByPage(@IntRange(from = 1) int pageSize) {
        return dbService.selectPageByPage(ContactEntity.TABLE_NAME, ENTITY_MAPPER, null, pageSize);
    }

    /**
     * 按页返回联系人
     *
     * @param pageIndex 页码
     * @param pageSize  页数据大小
     * @return
     */
    public Observable<List<ContactEntity>> getContactsByPage(@IntRange(from = 1) int pageIndex, @IntRange(from = 1) int pageSize) {
        return dbService.queryByPage(ENTITY_MAPPER, ContactEntity.TABLE_NAME, null, pageIndex, pageSize);
    }

    @Override
    public Observable<List<ContactEntity>> getAllContact() {
        return dbService.queryListWithWhere(TABLE_NAME, ENTITY_MAPPER, null);
    }

    @Override
    public Observable<List<ContactEntity>> searchContact(@Nullable final String key) {
        String whereClause = RepoUtils.buildLikeStatement(ContactEntity.REMARKNAME, key)
                + " OR " + RepoUtils.buildLikeStatement(ContactEntity.REALNAME, key)
                + " OR " + RepoUtils.buildLikeStatement(ContactEntity.PHONE, key);
        Timber.i("[SearchContactSql]: %s", whereClause);
        return dbService.queryListWithWhere(TABLE_NAME, ENTITY_MAPPER, whereClause);
    }

    @Override
    public Observable<Boolean> favoriteContact(final int contactId, @StatusFlavor final int favorite) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return dbService.update(TABLE_NAME,
                        ContactEntity.buildMarshal(null).isNote(favorite).asContentValues(),
                        ContactEntity.FRIENDID + "=?", String.valueOf(contactId)) != 0;
            }
        });
    }

    @Override
    public Observable<List<ContactGroup>> getGroupList() {
        return null;
    }

    @Override
    public Observable<Boolean> addGroupMember(int groupId, List<String> olNoList) {
        return null;
    }

    @Override
    public Observable<Boolean> removeGroupMember(int groupId, String friendOlNoList) {
        return null;
    }

    @Override
    public Observable<ContactGroup> createGroup(List<String> olNoList, String groupName, String easeGroupId) {
        return null;
    }

    @Override
    public Observable<Boolean> dismissGroup(int groupId) {
        return null;
    }

    /**
     * 更新联系人.
     *
     * @param entity
     * @return
     */
    @Override
    public Observable<ContactEntity> addOrUpdateContact(final ContactEntity entity) {
        return Observable.create(new Observable.OnSubscribe<ContactEntity>() {
            @Override
            public void call(Subscriber<? super ContactEntity> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    int rowId = dbService.insert(TABLE_NAME, ContactEntity.buildMarshal(entity).asContentValues());
                    if (rowId != -1) {
                        subscriber.onNext(entity);
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new OLException("Fail to insert contact whose phone is %s" + entity.phone()));
                    }

                }
            }
        });
    }

    /**
     * 添加联系人或者申请好友
     *
     * @param phone      手机号码
     * @param remarkName 备注名
     * @param nickName
     * @param additions  @return 返回操作成功与否
     */
    @Override
    public Observable<Boolean> addContact(@NonNull final String phone, @NonNull final String remarkName, final String nickName, @NonNull List<ContactEntity.Addition> additions) {
        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                ContactEntity entity = ContactEntity.newBuilder()
                        .phone(phone)
                        .remarkName(remarkName)
                        .nickName(nickName)
                        .build();
                return save(entity);
            }
        });
    }

    /**
     * 好友申请
     *
     * @param phone      手机号码
     * @param friendOlNo 对方olNo
     * @return
     */
    @Override
    public Observable<Boolean> requestFriend(@NonNull String phone, @NonNull String friendOlNo) {
        return null;
    }

    @Override
    public Observable<List<ContactEntity>> syncContacts(final List<ContactEntity> list) {
        return Observable.fromCallable(new Callable<List<ContactEntity>>() {
            @Override
            public List<ContactEntity> call() throws Exception {
                if (saveAll(list)) {
                    return list;
                } else {
                    throw new OLException("Fail to insert contacts into database");
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Observable<ContactEntity> getFriendByOlNo(@NonNull String olNo) {
        return dbService.queryOneQuietly(ContactEntity.TABLE_NAME, ENTITY_MAPPER, ContactEntity.FRIENDOLNO + " = ?", olNo);
    }

    /**
     * 分页获取群成员
     *
     * @param groupId   群id
     * @param pageIndex 页码
     * @param pageSize  页条目数
     * @return
     */
    @Override
    public Observable<List<ContactEntity>> getGroupMember(int groupId, int pageIndex, int pageSize) {
        RepoUtils.checkPageIndexAndSize(pageIndex, pageSize);
        return null;
    }

    @Override
    public Observable<List<ContactEntity>> getApplicantList() {
        return null;
    }

    @Override
    public Observable<ContactEntity> agreeToApplicant(String friendOlNo) {
        return null;
    }

    /**
     * 判断某欧乐会员是不是好友关系
     *
     * @param targetOlNo 对方欧乐号码
     * @return 如果是好友则返回true，否则返回false.
     */
    @Override
    public boolean isFriend(String targetOlNo) {
        /*"双边关系状态：
        1对方不是欧乐会员,
        2,对方是欧乐会员, 单方面加好友,
        3,对方是欧乐会员,同时是好友"*/
        return dbService.isDataExisting(ContactEntity.TABLE_NAME, ContactEntity.FRIENDOLNO + "=? AND "
                + ContactEntity.RELATIONSTATE + "=? LIMIT 1", targetOlNo, String.valueOf(3));
    }

    @Override
    public Boolean save(ContactEntity contactEntity) {
        return dbService.insert(TABLE_NAME, ContactEntity.buildMarshal(contactEntity).asContentValues()) != -1;
    }

    @Override
    public boolean saveAll(List<ContactEntity> list) {
        List<ContentValues> valuesList = new ArrayList<>(list.size());
        for (ContactEntity entity : list) {
            valuesList.add(ContactEntity.buildMarshal(entity).asContentValues());
        }
        return dbService.bulkInsert(TABLE_NAME, valuesList) != 0;
    }
}
