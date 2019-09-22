package com.vboss.okline.data;

import android.content.Context;

import com.vboss.okline.data.entities.ContactEntity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/10 <br/>
 * Summary : 在这里描述Class的主要功能
 */

@RunWith(MockitoJUnitRunner.class)
public class ContactRepositoryTest {
    private ContactRepository repository;

    @Mock
    Context mMockContext;
    @Before
    public void setUp() throws Exception {
        if (repository == null){
            repository = ContactRepository.getInstance(mMockContext);
        }
    }

    @Test
    public void getContact() throws Exception {

    }

    @Test
    public void getAllContact() throws Exception {
    }

    @Test
    public void searchContact() throws Exception {

    }

    @Test
    public void favoriteContact() throws Exception {

    }

    @Test
    public void getGroupList() throws Exception {

    }

    @Test
    public void addGroupMember() throws Exception {

    }

    @Test
    public void removeGroupMember() throws Exception {

    }

    @Test
    public void createGroup() throws Exception {

    }

    @Test
    public void dismissGroup() throws Exception {

    }

    @Test
    public void addOrUpdateContact() throws Exception {

    }

    @Test
    public void syncContacts() throws Exception {

    }

    @Test
    public void getChatLogToMe() throws Exception {

    }

    @Test
    public void getFriendByOLNO() {
        ContactEntity entity = repository.getFriendByOlNo("OLHZ310571000000000365").toBlocking().single();
        LogUtils.println(entity);
    }

}