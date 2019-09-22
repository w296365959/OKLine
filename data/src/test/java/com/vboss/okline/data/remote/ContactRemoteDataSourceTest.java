package com.vboss.okline.data.remote;

import com.vboss.okline.data.LogUtils;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/9 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class ContactRemoteDataSourceTest {
    ContactRemoteDataSource remote;

    @Before
    public void setup() {
        if (remote == null) {
            remote = ContactRemoteDataSource.getInstance();
        }
    }

    @Test
    public void getContact() throws Exception {
        remote.getContact(4912).subscribe(new DefaultSubscriber<ContactEntity>());
    }

    @Test
    public void getAllContact() throws Exception {
        remote.getAllContact().subscribe(new DefaultSubscriber<List<ContactEntity>>());
    }

    @Test
    public void favoriteContact() throws Exception {
        remote.favoriteContact(4912, 1).subscribe(new DefaultSubscriber<Boolean>());
    }

    @Test
    public void getGroupList() throws Exception {
        List<ContactGroup> list = remote.getGroupList().toBlocking().single();
        System.out.println(list.toString());
    }

    @Test
    public void addGroupMember() throws Exception {

    }

    @Test
    public void removeGroupMember() throws Exception {

    }

    @Test
    public void createGroup() throws Exception {
        remote.createGroup(memberList(), "GroupToTestApi", null).subscribe(new DefaultSubscriber<ContactGroup>());
    }

    private List<String> memberList() {
        List<String> list = new ArrayList<>();
        list.add("OLHZ310571000000000365");
        return list;
    }

    @Test
    public void dismissGroup() throws Exception {
//        remote.dismissGroup()
    }

    @Test
    public void syncContacts() throws Exception {
        ContactEntity entity = ContactEntity.newBuilder()
                .remarkName("NameToTestApi")
                .build();
        remote.syncContacts(Collections.singletonList(entity)).subscribe(new DefaultSubscriber<List<ContactEntity>>());
    }

    @Test
    public void getGroupMember() {
        List<ContactEntity> list = remote.getGroupMember(1, 1, 20).toBlocking().single();
        LogUtils.println(list);
    }

    @Test
    public void getFriendByOlNo() {
        ContactEntity entity = remote.getFriendByOlNo("OLHZ310571000000000365").toBlocking().single();
        LogUtils.println(entity);
    }

}