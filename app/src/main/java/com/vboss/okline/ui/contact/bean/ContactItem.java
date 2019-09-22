package com.vboss.okline.ui.contact.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.hyphenate.chat.EMGroup;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.utils.PinyinUtils;
import com.vboss.okline.utils.StringUtils;

import java.io.Serializable;
import java.util.Comparator;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/29 19:58
 * Desc :
 */

public class ContactItem implements Comparator<ContactItem>, Parcelable {
    public static final String EXTRA_OL_GROUP_ID = "olGroupID";
    public static final String EXTRA_MEMBERS = "members";
    public static final Parcelable.Creator<ContactItem> CREATOR = new Parcelable.Creator<ContactItem>() {
        @Override
        public ContactItem createFromParcel(Parcel source) {
            return new ContactItem(source);
        }

        @Override
        public ContactItem[] newArray(int size) {
            return new ContactItem[size];
        }
    };
    /**
     * 条目类型 1:星标朋友 2:群 3:普通好友
     */
    private int itemType;
    private String remark;
    private String realName;
    private String groupName;
    private boolean star;
    private int contactID;
    private String avatar;
    private String phoneNum;
    private String sortLetters;
    private String olNo;
    private String nickName;
    private boolean isChecked;
    private EMGroup group;
    private String easeID;
    private int olGroupID;
    private String pinyin;
    private String relationship;
    /**
     * -1 服务器未返回数据 1 对方不是欧乐会员 2 对方是欧乐会员单方面加好友 3 对方是欧乐会员同时是好友
     */
    private int relationState;

    public ContactItem() {
    }

    protected ContactItem(Parcel in) {
        this.itemType = in.readInt();
        this.remark = in.readString();
        this.realName = in.readString();
        this.groupName = in.readString();
        this.star = in.readByte() != 0;
        this.contactID = in.readInt();
        this.avatar = in.readString();
        this.phoneNum = in.readString();
        this.sortLetters = in.readString();
        this.olNo = in.readString();
        this.nickName = in.readString();
        this.isChecked = in.readByte() != 0;
        this.easeID = in.readString();
        this.olGroupID = in.readInt();
        this.relationship = in.readString();
    }

    @Override
    public String toString() {
        return "ContactItem{" +
                "itemType=" + itemType +
                ", remark='" + remark + '\'' +
                ", realName='" + realName + '\'' +
                ", groupName='" + groupName + '\'' +
                ", star=" + star +
                ", contactID=" + contactID +
                ", avatar='" + avatar + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", sortLetters='" + sortLetters + '\'' +
                ", olNo='" + olNo + '\'' +
                ", nickName='" + nickName + '\'' +
                ", isChecked=" + isChecked +
                ", group=" + group +
                ", easeID='" + easeID + '\'' +
                ", olGroupID=" + olGroupID +
                ", pinyin='" + pinyin + '\'' +
                ", relationship='" + relationship + '\'' +
                ", relationState=" + relationState +
                '}';
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public int getRelationState() {
        return relationState;
    }

    public void setRelationState(int relationState) {
        this.relationState = relationState;
    }

    public String getEaseID() {
        return easeID;
    }

    public void setEaseID(String easeID) {
        this.easeID = easeID;
    }

    public EMGroup getGroup() {
        return group;
    }

    public void setGroup(EMGroup group) {
        this.group = group;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    /**
     * 汉字转拼音
     *
     * @return
     */
//    public String getPinYin() {
//        if (relationState==3){
//            return PinyinUtils.getPingYin(realName);
//        }else{
//            return PinyinUtils.getPingYin(remark);
//        }
//
//    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public boolean isStar() {
        return star;
    }

    public void setStar(boolean star) {
        this.star = star;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }

    public String getOlNo() {
        return olNo;
    }

    public void setOlNo(String olNo) {
        this.olNo = olNo;
    }

    public int getOlGroupID() {
        return olGroupID;
    }

    public void setOlGroupID(int olGroupID) {
        this.olGroupID = olGroupID;
    }

    @Override
    public int compare(ContactItem contactItem, ContactItem t1) {
        return 0;
    }

    @Override
    public int hashCode() {
        if (StringUtils.isNullString(phoneNum)) {
            return olNo.hashCode();
        } else {
            return phoneNum.hashCode();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContactEntity) {
            ContactEntity other = (ContactEntity) obj;
            if (phoneNum != null && phoneNum.equals(other.phone())) {
                return true;
            }
        }
        if (obj instanceof ContactItem) {
            ContactItem other = (ContactItem) obj;
            if (phoneNum != null && phoneNum.equals(other.getPhoneNum())) {
                return true;
            }
            if (olNo != null && olNo.equals(other.getOlNo())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.itemType);
        dest.writeString(this.remark);
        dest.writeString(this.realName);
        dest.writeString(this.groupName);
        dest.writeByte(this.star ? (byte) 1 : (byte) 0);
        dest.writeInt(this.contactID);
        dest.writeString(this.avatar);
        dest.writeString(this.phoneNum);
        dest.writeString(this.sortLetters);
        dest.writeString(this.olNo);
        dest.writeString(this.nickName);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeString(this.easeID);
        dest.writeInt(this.olGroupID);
        dest.writeString(this.relationship);
    }

}
