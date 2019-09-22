package com.vboss.okline.data.remote;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.okline.vboss.http.OKLineClient;
import com.vboss.okline.data.ContactDataSource;
import com.vboss.okline.data.StatusFlavor;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/6 <br/>
 * Summary : 联系人服务器数据操作
 */
public class ContactRemoteDataSource implements ContactDataSource {
    private static final String SYNC_CONTACTS = "syncFriendListRequest";
    private static final String CONTACT_LIST = "qryFriendListRequest";
    private static final String CONTACT_DETAIL = "qryFriendDtlRequest";
    private static final String CONTACT_MYSELF = "qryBusinessCardRequest";
    private static final String CREATE_GROUP = "addUserGroupRequest";
    private static final String DISMISS_GROUP = "";
    private static final String GROUP_LIST = "qryUserGroupListRequest";
    private static final String ADD_GROUP_MEMBER = "addGroupMemberRequest";
    private static final String REMOVE_GROUP_MEMBER = "delGroupMemberRequest";
    private static final String STAR_CONTACT = "modFriendNoteRequest";
    private static final String FRIEND_DETAIL_BY_OLNO = "qryFriendByOlNoRequest";
    private static final String GROUP_MEMBER_LIST = "qryGroupMembersRequest";
    /**
     * 获取好友申请列表
     */
    private static final String QUERY_APPLICANT_LIST = "qryFriendApplyRequest";

    /**
     * 同意好友申请
     */
    private static final String AGREE_TO_APPLICATION = "passFriendApplyRequest";

    private static final String ADD_CONTACT_OR_REQUEST_FRIEND = "addUserFriendRequest";

    private static ContactRemoteDataSource instance;
    private final OKLineClient client;

    public static ContactRemoteDataSource getInstance() {
        if (instance == null) {
            instance = new ContactRemoteDataSource();
        }
        return instance;
    }

    private ContactRemoteDataSource() {
        client = OKLineClient.getInstance();
    }

    @Override
    public Observable<ContactEntity> getContact(int friendId) {
        return client.requestAsyncForData(CONTACT_DETAIL, RequestData.newBuilder()
                .friendId(friendId).build(), ContactEntity.class);
    }

    @Override
    public Observable<List<ContactEntity>> getAllContact() {
        return client.requestAsyncForData(CONTACT_LIST, RequestData.newBuilder().build(),
                new TypeToken<List<ContactEntity>>() {
                }.getType());
    }

    @Override
    public Observable<List<ContactEntity>> searchContact(@Nullable String key) {
        return null;
    }

    @Override
    public Observable<Boolean> favoriteContact(int contactId, @StatusFlavor int favorite) {
        return client.requestAsyncForBoolean(STAR_CONTACT,
                RequestData.newBuilder()
                        .friendId(contactId)
                        .isNote(favorite)
                        .build());
    }

    @Override
    public Observable<List<ContactGroup>> getGroupList() {
        return client.requestAsyncForData(GROUP_LIST,
                RequestData.newBuilder().build(),
                new TypeToken<List<ContactGroup>>() {
                }.getType());
    }

    @Override
    public Observable<Boolean> addGroupMember(int groupId, List<String> olNoList) {
        RequestData data = RequestData.newBuilder()
                .groupId(groupId)
                .friendOlNoList(olNoList)
                .build();
        return client.requestAsyncForBoolean(ADD_GROUP_MEMBER, data);
    }

    @Override
    public Observable<Boolean> removeGroupMember(int groupId, String friendOlNo) {
        RequestData data = RequestData.newBuilder()
                .groupId(groupId)
                .friendOlNo(friendOlNo)
                .build();
        return client.requestAsyncForBoolean(REMOVE_GROUP_MEMBER, data);
    }

    @Override
    public Observable<ContactGroup> createGroup(List<String> olNoList, String groupName, String easeGroupId) {
        return client.requestAsyncForData(CREATE_GROUP, RequestData.newBuilder()
                .friendOlNoList(olNoList)
                .easeGroupId(easeGroupId)
                .groupName(groupName).build(), ContactGroup.class);
    }

    @Override
    public Observable<Boolean> dismissGroup(int groupId) {
        return client.requestAsyncForBoolean(DISMISS_GROUP, RequestData.newBuilder()
                .groupId(groupId)
                .build());
    }

    /**
     * 添加或更新单个联系人.
     * 操作类型取决于{@code operatType}的值。<br/>
     * 操作类型：1 新增，0编辑 .
     * <br/>
     * <p>
     * 目前可用于更新phone和remarkName字段。
     *
     * @param entity
     * @return
     */
    @Override
    public Observable<ContactEntity> addOrUpdateContact(ContactEntity entity) {
        return syncContacts(Collections.singletonList(entity)).map(new Func1<List<ContactEntity>, ContactEntity>() {
            @Override
            public ContactEntity call(List<ContactEntity> list) {
                return list == null || list.isEmpty() ? null : list.get(0);
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
    public Observable<Boolean> addContact(@NonNull String phone, @Nullable String remarkName, String nickName, @NonNull List<ContactEntity.Addition> additions) {
        return addContactOrRequestFriend(ContactEntity.OPERATE_TYPE_ADD, phone, remarkName, nickName, null, additions);
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
        return addContactOrRequestFriend(ContactEntity.OPERATE_TYPE_FRIEND_REQUEST, phone, null, null, friendOlNo, null);
    }

    /**
     * 添加联系人或者申请好友
     *
     * @param operateType 操作类型
     * @param phone       手机号码
     * @param remarkName  备注名
     * @param friendOlNo  好友欧乐号
     * @param additions   添加联系人时的附加信息
     * @return 返回操作成功与否
     * @see ContactEntity#OPERATE_TYPE_ADD
     * @see ContactEntity#OPERATE_TYPE_FRIEND_REQUEST
     */
    private Observable<Boolean> addContactOrRequestFriend(@ContactEntity.OperateType int operateType, @NonNull String phone, @Nullable String remarkName, @Nullable String nickName,
                                                          @Nullable String friendOlNo, @Nullable List<ContactEntity.Addition> additions) {
        return client.requestAsyncForBoolean(ADD_CONTACT_OR_REQUEST_FRIEND, RequestData.newBuilder()
                .operatType(operateType)
                .phone(phone)
                .remarkName(remarkName)
                .nickName(nickName)
                .friendOlNo(friendOlNo)
                .dataList(additions).build());
    }

    @Override
    public Observable<List<ContactEntity>> syncContacts(List<ContactEntity> list) {
        return client.requestAsyncForData(SYNC_CONTACTS, RequestData.newBuilder()
                .phoneList(list)
                .build(), new TypeToken<List<ContactEntity>>() {
        }.getType());
    }

    @Override
    public Observable<ContactEntity> getFriendByOlNo(@NonNull String olNo) {
        return client.requestAsyncForData(FRIEND_DETAIL_BY_OLNO, RequestData.newBuilder()
                .friendOlNo(olNo).build(), ContactEntity.class);
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
        return client.requestAsyncForData(GROUP_MEMBER_LIST, RequestData.newBuilder()
                .groupId(groupId).build(), new TypeToken<List<ContactEntity>>() {
        }.getType());
    }

    /**
     * 获取好友申请列表
     */
    public Observable<List<ContactEntity>> getApplicantList() {
        return client.requestAsyncForData(QUERY_APPLICANT_LIST, RequestData.newBuilder().build(), new TypeToken<List<ContactEntity>>() {
        }.getType());
    }

    /**
     * 同意申请
     *
     * @param friendOlNo 申请人Id
     * @return
     */
    public Observable<ContactEntity> agreeToApplicant(String friendOlNo) {
        return client.requestAsyncForData(AGREE_TO_APPLICATION, RequestData.newBuilder()
                .friendOlNo(friendOlNo).build(), ContactEntity.class);
    }

    /**
     * 判断某欧乐会员是不是好友关系
     *
     * @param targetOlNo 对方欧乐号码
     * @return 如果是好友则返回true，否则返回false.
     */
    @Override
    public boolean isFriend(String targetOlNo) {
        throw new UnsupportedOperationException("^_^");
    }
}
