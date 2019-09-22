package com.vboss.okline.ui.contact;

import com.vboss.okline.ui.contact.bean.ContactItem;

import java.util.Comparator;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/5 20:34
 * Desc :
 */

public class ContactComparator implements Comparator<ContactItem> {
    /*@Override
    public int compare(ContactItem o1, ContactItem o2) {
        String letter1 = o1.getSortLetters();
        String letter2 = o2.getSortLetters();
        if (letter1 == null || !isLetter(letter1.charAt(0))) {
            letter1 = "#";
        }

        if (letter2 == null || !isLetter(letter2.charAt(0))) {
            letter2 = "#";
        }

        if ("@".equals(letter1)
                || "#".equals(letter2)) {
            return -1;
        } else if ("#".equals(letter1)
                || "@".equals(letter2)) {
            return 1;
        } else {
            return letter1.compareTo(letter2);
        }
    }

    private boolean isLetter(char ch) {
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
    }*/


    @Override
    public int compare(ContactItem o1, ContactItem o2) {
        //这里主要是用来对ListView里面的数据根据ABCDEFG...来排序
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            //lzb edit 2017/5/15 排序都按小写排
//            return o1.getPinYin().toLowerCase().compareTo(o2.getPinYin().toLowerCase());
            return o1.getPinyin().compareTo(o2.getPinyin());
        }
    }
}
