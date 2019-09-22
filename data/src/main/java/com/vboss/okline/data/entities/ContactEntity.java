package com.vboss.okline.data.entities;

import android.support.annotation.IntDef;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/3/30 <br/>
 * Summary : 联系人实体类
 */

@AutoValue
public abstract class ContactEntity implements ContactModel, Serializable {
    @Retention(RetentionPolicy.SOURCE)
    @IntDef({OPERATE_TYPE_ADD, OPERATE_TYPE_FRIEND_REQUEST, OPERATE_TYPE_UPDATE})
    public @interface OperateType {
    }

    /**
     * 新增联系人
     */
    public static final int OPERATE_TYPE_ADD = 1;
    /**
     * 更新联系人
     */
    public static final int OPERATE_TYPE_UPDATE = 0;

    /**
     * 好友申请
     */
    public static final int OPERATE_TYPE_FRIEND_REQUEST = 2;


    public static final ContactEntity.Factory<ContactEntity> FACTORY = new Factory<>(new Creator<ContactEntity>() {
        @Override
        public ContactEntity create(int friendId, @Nullable String phone, @Nullable String realName, @Nullable String nickName, @Nullable String imgUrl, @Nullable String friendOlNo, @Nullable String remarkName, int operatType, int isNote, Integer relationState) {
            return new AutoValue_ContactEntity(friendId, phone, realName, nickName, imgUrl, friendOlNo, remarkName, operatType, isNote, relationState);
        }
    });
    /**
     * POJO转换器
     */
    public static final ContactEntity.Mapper<ContactEntity> MAPPER = new Mapper<>(FACTORY);

    public static TypeAdapter<ContactEntity> typeAdapter(Gson gson) {
        return new AutoValue_ContactEntity.GsonTypeAdapter(gson);
    }

    public static ContactEntity.Marshal buildMarshal(ContactModel model) {
        return new Marshal(model);
    }

    /**
     * ContactEntity构造器方法。其中{@code friendId}、{@code operatType}、{@code isNote}默认被设置0。
     *
     * @return
     */
    public static Builder newBuilder() {
        return new AutoValue_ContactEntity.Builder()
                .friendId(0)
                .operatType(-1)
                .isNote(0);
    }

    @Override
    public int hashCode() {
        return phone().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ContactEntity) {
            ContactEntity other = (ContactEntity) obj;
            if (phone() != null && phone().equals(other.phone())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 建造器
     */
    @AutoValue.Builder
    public interface Builder {
        /**
         * 联系人Id
         *
         * @param id
         * @return
         */
        Builder friendId(int id);

        /**
         * 手机号码
         *
         * @param phone
         * @return
         */
        Builder phone(String phone);

        /**
         * 昵称
         *
         * @param nickName
         * @return
         */
        Builder nickName(String nickName);

        /**
         * 备注名
         *
         * @param remarkName
         * @return
         */
        Builder remarkName(String remarkName);

        /**
         * 操作类型<br/>
         * <p>
         * 0：编辑 <br/>
         * 1：新增/导入
         * 3: 添加/发送好友请求
         *
         * @param opType
         * @return
         */
        Builder operatType(int opType);

        /**
         * 头像
         *
         * @param url
         * @return
         */
        Builder imgUrl(String url);

        /**
         * 真实姓名
         *
         * @param realName
         * @return
         */
        Builder realName(String realName);

        /**
         * 联系人欧乐号
         *
         * @param friendOlNo
         * @return
         */
        Builder friendOlNo(String friendOlNo);

        Builder relationState(Integer state);

        /**
         * 是否是我关心的人
         *
         * @param note
         * @return
         */
        Builder isNote(int note);

        ContactEntity build();
    }

    /**
     * 联系人的附加信息
     */
    public static class Addition {
        /**
         * 联系电话
         */
        public static final int DATA_TYPE_PHONE = 1;
        /**
         * 名片信息
         */
        public static final int DATA_TYPE_CARD = 2;
        /**
         * 银行卡信息
         */
        public static final int DATA_TYPE_BANK = 3;
        /**
         * 快递信息
         */
        public static final int DATA_TYPE_EXPRESS = 4;


        /**
         * 数据类型
         */
        private int dataType;
        /**
         * 数据内容
         */
        private Object content;

        /**
         * Empty constructor
         */
        public Addition() {
        }

        /**
         * @param dataType 数据类型
         * @param content  数据内容
         * @see #DATA_TYPE_BANK
         * @see #DATA_TYPE_CARD
         * @see #DATA_TYPE_PHONE
         * @see #DATA_TYPE_EXPRESS
         */
        public Addition(int dataType, Object content) {
            this.dataType = dataType;
            this.content = content;
        }

        public int getDataType() {
            return dataType;
        }

        public Object getContent() {
            return content;
        }

        public void setDataType(int dataType) {
            this.dataType = dataType;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "Addition{" +
                    "dataType=" + dataType +
                    ", content=" + content +
                    '}';
        }
    }

}
