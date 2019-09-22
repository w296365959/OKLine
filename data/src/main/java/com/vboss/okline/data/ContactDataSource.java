package com.vboss.okline.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;

import java.util.List;

import rx.Observable;


/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/31 <br/>
 * Summary : 联系人数据入口
 */
public interface ContactDataSource {

    /**
     * 联系人详情
     *
     * @param contactId
     * @return
     */
    Observable<ContactEntity> getContact(int contactId);

    /**
     * 获取联系人列表
     *
     * @return
     */
    Observable<List<ContactEntity>> getAllContact();

    /**
     * 查找联系人
     *
     * @param key
     * @return
     */
    Observable<List<ContactEntity>> searchContact(@Nullable String key);

    /**
     * 添加或移除关心的人
     *
     * @param contactId 联系人Id
     * @param favorite  0：移除关心；1：加入关心
     */
    Observable<Boolean> favoriteContact(int contactId, @StatusFlavor int favorite);

    /**
     * 获取群组列表
     *
     * @return
     */
    Observable<List<ContactGroup>> getGroupList();

    /**
     * 添加群成员
     *
     * @param groupId
     * @param olNoList
     * @return
     */
    Observable<Boolean> addGroupMember(int groupId, List<String> olNoList);

    /**
     * 删除群成员
     *
     * @param groupId
     * @param friendOlNoList
     * @return
     */
    Observable<Boolean> removeGroupMember(int groupId, String friendOlNoList);


    /**
     * 建群
     *
     * @param olNoList    会员欧乐号列表
     * @param groupName   群名称
     * @param easeGroupId 环信群Id
     * @return ContactGroup
     */
    Observable<ContactGroup> createGroup(List<String> olNoList, String groupName, String easeGroupId);

    /**
     * 解散群
     *
     * @param groupId
     * @return
     */
    Observable<Boolean> dismissGroup(int groupId);

    /**
     * 更新联系人.
     *
     * @return
     */
    Observable<ContactEntity> addOrUpdateContact(ContactEntity entity);

    /**
     * 添加联系人
     *
     * @param phone      手机号码
     * @param remarkName 备注名
     * @param nickName
     *@param additions  @return 返回操作成功与否
     */
    Observable<Boolean> addContact(@NonNull String phone, @NonNull String remarkName, String nickName, @NonNull List<ContactEntity.Addition> additions);

    /**
     * 好友申请
     *
     * @param phone      手机号码
     * @param friendOlNo 对方olNo
     * @return
     */
    Observable<Boolean> requestFriend(@NonNull String phone, @NonNull String friendOlNo);

    /**
     * 同步联系人。<br/>
     * <p>
     * 同步联系人到服务器并保存到本地。
     *
     * @param list
     */
    Observable<List<ContactEntity>> syncContacts(List<ContactEntity> list);

    /**
     * 根据欧乐号查询联系人详情
     *
     * @param olNo 欧乐号
     * @return
     */
    Observable<ContactEntity> getFriendByOlNo(@NonNull String olNo);

    /**
     * 分页获取群成员
     *
     * @param groupId   群id
     * @param pageIndex 页码
     * @param pageSize  页条目数
     * @return
     */
    Observable<List<ContactEntity>> getGroupMember(int groupId, int pageIndex, int pageSize);

    /**
     * 获取申请人列表
     */
    Observable<List<ContactEntity>> getApplicantList();

    /**
     * 同意好友申请
     *
     * @param friendOlNo 好友olno
     * @return
     */
    Observable<ContactEntity> agreeToApplicant(String friendOlNo);

    /**
     * 判断某欧乐会员是不是好友关系
     *
     * @param targetOlNo 对方欧乐号码
     * @return 如果是好友则返回true，否则返回false.
     */
    boolean isFriend(String targetOlNo);

}
