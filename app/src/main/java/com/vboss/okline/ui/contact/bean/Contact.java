package com.vboss.okline.ui.contact.bean;

import java.io.Serializable;
import java.util.Comparator;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/28 16:17
 * Desc :
 */

public class Contact implements Serializable,Comparator<Contact> {
    private int type; // 1:star 2:group 3:contact



    private String name;
    private String remark;
    private String olNo;
    private String phone;
    private String group;
    private boolean isStar;
    /**
     * 拼音首字母
     */
    private String sortLetters;

    @Override
    public String toString() {
        return "Contact{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                ", olNo='" + olNo + '\'' +
                ", phone='" + phone + '\'' +
                ", group='" + group + '\'' +
                ", isStar=" + isStar +
                ", sortLetters='" + sortLetters + '\'' +
                '}';
    }

    public String getOlNo() {
        return olNo;
    }

    public void setOlNo(String olNo) {
        this.olNo = olNo;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isStar() {
        return isStar;
    }

    public void setStar(boolean star) {
        isStar = star;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        return phone != null ? phone.equals(contact.phone)&&remark.equals(contact.remark) : contact.phone == null;

    }

    @Override
    public int compare(Contact o1, Contact o2) {
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
    }
}
