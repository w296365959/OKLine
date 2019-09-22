package com.vboss.okline.data;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;
import com.vboss.okline.data.local.ContactLocalDataSource;
import com.vboss.okline.data.remote.ContactRemoteDataSource;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/1 <br/>
 * Summary : 联系人数据操作入口
 */
public class ContactRepository implements ContactDataSource {
    private static ContactRepository instance;
    private final ContactLocalDataSource local;
    private final ContactRemoteDataSource remote;

    private ContactRepository(Context context) {
        local = ContactLocalDataSource.getInstance(context);
        remote = ContactRemoteDataSource.getInstance();
    }

    public static ContactRepository getInstance(Context context) {
        if (instance == null) {
            instance = new ContactRepository(context);
        }
        return instance;
    }


    @Override
    public Observable<ContactEntity> getContact(int friendId) {
        return remote.getContact(friendId).map(new Func1<ContactEntity, ContactEntity>() {
            @Override
            public ContactEntity call(ContactEntity entity) {
                local.save(entity);
                return entity;
            }
        });
    }


    /**
     * 一页一页的获取联系人
     *
     * @param pageSize 每页的数据大小
     * @return
     */
    public Observable<List<ContactEntity>> getContactPageByPage(int pageSize) {
        return local.getContactsPageByPage(pageSize);
    }

    @Override
    public Observable<List<ContactEntity>> getAllContact() {
        /*if (local.isContactExisting()) {
            return local.getAllContact();
        } else {
            return getAndSaveRemoteDataSource();
        }*/
        return local.getAllContact();
    }

    /**
     * 从服务器获取联系人数据并存储到服务器
     */
    private Observable<List<ContactEntity>> getAndSaveRemoteDataSource() {
        return remote.getAllContact().map(new Func1<List<ContactEntity>, List<ContactEntity>>() {
            @Override
            public List<ContactEntity> call(List<ContactEntity> list) {
                local.saveAll(list);
                return list;
            }
        });
    }

    @Override
    public Observable<List<ContactEntity>> searchContact(@Nullable String keyToSearch) {
        return local.searchContact(keyToSearch);
    }

    @Override
    public Observable<Boolean> favoriteContact(final int contactId, @StatusFlavor final int favorite) {
        return Observable.zip(remote.favoriteContact(contactId, favorite), local.favoriteContact(contactId, favorite), new Func2<Boolean, Boolean, Boolean>() {
            @Override
            public Boolean call(Boolean remote, Boolean local) {
                return remote && local;
            }
        });
    }

    @Override
    public Observable<List<ContactGroup>> getGroupList() {
        return remote.getGroupList();
    }

    @Override
    public Observable<Boolean> addGroupMember(int groupId, List<String> olNoList) {
        return remote.addGroupMember(groupId, olNoList);
    }

    @Override
    public Observable<Boolean> removeGroupMember(int groupId, String friendOlNoList) {
        return remote.removeGroupMember(groupId, friendOlNoList);
    }

    @Override
    public Observable<ContactGroup> createGroup(List<String> olNoList, String groupName, String easeGroupId) {
        return remote.createGroup(olNoList, groupName, easeGroupId);
    }

    @Override
    public Observable<Boolean> dismissGroup(int groupId) {
        return remote.dismissGroup(groupId);
    }

    /**
     * 新增or更新联系人.
     *
     * @param entity
     * @return
     * @see ContactEntity#OPERATE_TYPE_ADD
     * @see ContactEntity#OPERATE_TYPE_UPDATE
     */
    @Override
    public Observable<ContactEntity> addOrUpdateContact(ContactEntity entity) {
        return remote.addOrUpdateContact(entity).flatMap(new Func1<ContactEntity, Observable<ContactEntity>>() {
            @Override
            public Observable<ContactEntity> call(ContactEntity entity) {
                return local.addOrUpdateContact(entity);
            }
        });
    }

    /**
     * 添加联系人
     *
     * @param phone      手机号码
     * @param remarkName 备注名
     * @param nickName
     * @param additions  @return 返回操作成功与否
     */
    @Override
    public Observable<Boolean> addContact(@NonNull final String phone, @NonNull final String remarkName, final String nickName, @NonNull final List<ContactEntity.Addition> additions) {
        return remote.addContact(phone, remarkName, nickName, additions).flatMap(new Func1<Boolean, Observable<ContactEntity>>() {
            @Override
            public Observable<ContactEntity> call(Boolean aBoolean) {
                ContactEntity entity = ContactEntity.newBuilder()
                        .phone(phone)
                        .remarkName(remarkName)
                        .operatType(1)
                        .nickName(nickName).build();
                return addOrUpdateContact(entity);
            }
        }).flatMap(new Func1<ContactEntity, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(ContactEntity entity) {
                return requestFriend(phone, entity.friendOlNo());
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
        return remote.requestFriend(phone, friendOlNo);
    }

    @Override
    public Observable<List<ContactEntity>> syncContacts(List<ContactEntity> list) {
        return remote.syncContacts(list).flatMap(new Func1<List<ContactEntity>, Observable<List<ContactEntity>>>() {
            @Override
            public Observable<List<ContactEntity>> call(List<ContactEntity> list) {
                return local.syncContacts(list);
            }
        });
    }

    @Override
    public Observable<ContactEntity> getFriendByOlNo(@NonNull final String olNo) {
        return local.getFriendByOlNo(olNo).flatMap(new Func1<ContactEntity, Observable<ContactEntity>>() {
            @Override
            public Observable<ContactEntity> call(ContactEntity entity) {
                if (entity == null) {
                    return remote.getFriendByOlNo(olNo);
                }
                return Observable.just(entity);
            }
        });
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
        return remote.getGroupMember(groupId, pageIndex, pageSize);
    }

    @Override
    public Observable<List<ContactEntity>> getApplicantList() {
        return remote.getApplicantList();
    }

    @Override
    public Observable<ContactEntity> agreeToApplicant(final String friendOlNo) {
        return remote.agreeToApplicant(friendOlNo).flatMap(new Func1<ContactEntity, Observable<ContactEntity>>() {
            @Override
            public Observable<ContactEntity> call(ContactEntity entity) {
                return getContact(entity.friendId());
            }
        });
    }

    /**
     * 判断某欧乐会员是不是好友关系
     *
     * @param targetOlNo 对方欧乐号码
     * @return 如果是好友则返回true，否则返回false.
     */
    @Override
    public boolean isFriend(String targetOlNo) {
        return local.isFriend(targetOlNo);
    }


}
