package com.vboss.okline;

import com.vboss.okline.utils.Formater;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/4/28 <br/>
 * Summary : 普通java测试
 */
public class CommonTest {

    @Test
    public void testStringJoin() {
        List<String> nameList = new ArrayList<>();
        nameList.add("丁娟娟");
        nameList.add("");
        nameList.add("王小二");
        nameList.add("罗秀秀");
        String groupName = org.apache.commons.lang.StringUtils.join(nameList, ",");
        System.out.println("groupName = " + groupName);
    }

    @Test
    public void testMoneyFormat() {
        int m1 = 1000;
        int m2 = -1000;
        System.out.println(Formater.money(m1 / 100.0));
        System.out.println(Formater.money(m2 / 100.0));
    }

    @Test
    public void testJust() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        Observable.from(list).map(new Func1<String, Object>() {
            @Override
            public Object call(String s) {
                System.out.println("call : " + s);
                return null;
            }
        }).subscribe(new Subscriber<Object>() {
            @Override
            public void onNext(Object o) {
                System.out.println("onNext : " + o);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError : ");
            }

            @Override
            public void onCompleted() {
                System.out.println("onCompleted : ");
            }
        });
    }
}
