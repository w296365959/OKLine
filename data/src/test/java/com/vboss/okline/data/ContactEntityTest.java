package com.vboss.okline.data;

import com.vboss.okline.data.entities.ContactEntity;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/4/26 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class ContactEntityTest {

    @Test
    public void testEquals() {
        ContactEntity c1 = ContactEntity.newBuilder()
                .phone("1234")
                .build();
        ContactEntity c2 = ContactEntity.newBuilder()
                .phone("1234")
                .build();
        ContactEntity c3 = ContactEntity.newBuilder()
                .phone("12345")
                .build();
        Assert.assertEquals(c1, c2);
//        Assert.assertEquals(c1, c3);

        List<ContactEntity> list = new ArrayList<>();
        list.add(c1);
        list.add(c1);
        list.add(c1);
        list.add(c2);
        list.add(c2);
        list.add(c2);
        list.add(c3);
        list.add(c3);
        list.add(c3);
        Assert.assertEquals(list.size(), 9);

        Set<ContactEntity> set = new HashSet<>();
        set.addAll(list);
        Assert.assertEquals(set.size(), 2);

        Set<ContactEntity> set2 = new HashSet<>();
        set2.add(c1);
        set2.add(c2);
        Assert.assertEquals(set2.size(), 1);
    }
}
