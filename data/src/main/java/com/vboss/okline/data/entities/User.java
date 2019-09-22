package com.vboss.okline.data.entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/30 <br/>
 * Summary : OKLine用户实体类
 */
public class User {
    /**
     * 证件类型：身份证
     */
    public static final String CREDIT_TYPE_ID = "1";
    /**
     * 证件类型：护照
     */
    public static final String CREDIT_TYPE_PASSPORT = "2";


    //设备类型

    /**
     * 手环
     */
    public static final int DEV_RING = 1;
    /**
     * 手表
     */
    public static final int DEV_WATCH = 2;
    /**
     * 欧卡
     */
    public static final int DEV_OCARD = 3;
    /**
     * 全POS
     */
    public static final int DEV_POS = 4;
    /**
     * 云SE
     */
    public static final int DEV_CLOUD_SE = 5;

    /**
     * 手机号
     */
    private String phone;
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 备注名
     */
    private String nickName;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * 欧乐号
     */

    private String olNo;

    /**
     * 身份证号 add by shenyi 2017-4-18 增加身份证号
     */
    private String idcardNo;

    /**
     * 欧乐会员号
     */
    private String userNo;
    /**
     * 设备号
     */
    private String deviceNo;
    /**
     * 蓝牙地址
     */
    private String bhtName;
    /**
     * 设备mac地址
     */
    private String bhtAddress;
    /**
     * 头像图片地址
     */
    @SerializedName("imgUrl")
    private String avatar;
    private int count;

    //<editor-fold desc="郑军  编辑日期2017-04-10 17:36:24">
    /**
     * 其他联系号码
     */
    private List<String> phoneList;
    /**
     * 个人名片
     */
    private VisitingCard busiCard;
    /**
     * 用户给好友添加的银行卡信息
     */
    private List<BankAccount> bankList;
    /**
     * 快递信息
     */
    private List<String> express;

    /**
     * 好友自行绑定的银行卡信息
     */
    @SerializedName("dataList")
    private List<BankAccount> friendBankList;

    /**
     * 是否是会员
     * <p>
     * 1：欧乐会员
     * 0：非欧乐会员
     */
    private int isUser;

    public User(String phone, String realName, String olNo, String deviceNo, String bhtName, String bhtAddress, String avatar) {
        this.phone = phone;
        this.realName = realName;
        this.olNo = olNo;
        this.deviceNo = deviceNo;
        this.bhtName = bhtName;
        this.bhtAddress = bhtAddress;
        this.avatar = avatar;
    }

    public User() {
    }

    public String getIdcardNo() {
        return idcardNo;
    }

    public void setIdcardNo(String idcardNo) {
        this.idcardNo = idcardNo;
    }

    /**
     * 获取欧乐会员排名
     */
    public int getCount() {
        return count;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    //</editor-fold>

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOlNo() {
        return olNo;
    }

    public void setOlNo(String olNo) {
        this.olNo = olNo;
    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }

    public String getBhtName() {
        return bhtName;
    }

    public void setBhtName(String bhtName) {
        this.bhtName = bhtName;
    }

    public String getBhtAddress() {
        return bhtAddress;
    }

    public void setBhtAddress(String bhtAddress) {
        this.bhtAddress = bhtAddress;
    }

    /**
     * 获取欧乐会员号
     */
    public String getUserNo() {
        return userNo;
    }

    /**
     * 获取备用号码列表
     */
    public List<String> getSecondPhoneArray() {
        return phoneList;
    }

    /**
     * 获取快递地址列表
     */
    public List<String> getExpress() {
        return express;
    }

    /**
     * 获取工作名片
     */
    public VisitingCard getVisitingCard() {
        return busiCard;
    }

    /**
     * 获取银行账户信息列表
     */
    public List<BankAccount> getBankInfo() {
        return bankList;
    }

    public List<BankAccount> getFriendBankList() {
        return friendBankList;
    }

    /**
     * 是否是欧乐会员
     */
    public boolean isOlMember() {
        return isUser == 1;
    }

    @Override
    public String toString() {
        return "User{" +
                "phone='" + phone + '\'' +
                ", realName='" + realName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", olNo='" + olNo + '\'' +
                ", idcardNo='" + idcardNo + '\'' +
                ", userNo='" + userNo + '\'' +
                ", deviceNo='" + deviceNo + '\'' +
                ", bhtName='" + bhtName + '\'' +
                ", bhtAddress='" + bhtAddress + '\'' +
                ", avatar='" + avatar + '\'' +
                ", count=" + count +
                ", phoneList=" + phoneList +
                ", busiCard=" + busiCard +
                ", bankList=" + bankList +
                ", express=" + express +
                ", friendBankList=" + friendBankList +
                ", isUser=" + isUser +
                '}';
    }

    public static class VisitingCard implements Serializable {
        /**
         * 所在公司
         */
        private String company;
        /**
         * 职位
         */
        private String position;
        /**
         * 所在地区
         */
        private String area;
        /**
         * 详细地址
         */
        private String address;

        public VisitingCard(String company, String position, String address) {
            this.company = company;
            this.position = position;
            this.address = address;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        /**
         * 公司名称
         */
        public String getCompany() {
            return company;
        }

        /**
         * 职位
         */
        public String getPosition() {
            return position;
        }

        /**
         * 地址
         */
        public String getAddress() {
            return address;
        }

        @Override
        public String toString() {
            return "VisitingCard{" +
                    "company='" + company + '\'' +
                    ", position='" + position + '\'' +
                    ", address='" + address + '\'' +
                    '}';
        }
    }

    public static class BankAccount implements Serializable {

        /**
         * 户主
         */
        private String cardName;
        /**
         * 账户
         */
        private String cardNo;
        /**
         * 开户行
         */
        private String bankName;

        private String bankIcon;

        public BankAccount(String name, String account, String bank, String icon) {
            this.cardName = name;
            this.cardNo = account;
            this.bankName = bank;
            this.bankIcon = icon;
        }

        public String getCardName() {
            return cardName;
        }

        public String getCardNo() {
            return cardNo;
        }

        public String getBankName() {
            return bankName;
        }

        public String getBankIcon() {
            return bankIcon;
        }


        @Override
        public String toString() {
            return "BankAccount{" +
                    "cardName='" + cardName + '\'' +
                    ", cardNo='" + cardNo + '\'' +
                    ", bankName='" + bankName + '\'' +
                    ", bankIcon='" + bankIcon + '\'' +
                    '}';
        }
    }
}
