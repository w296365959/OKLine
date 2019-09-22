package com.vboss.okline.base;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/4/28 <br/>
 * Summary : 总线key值
 */
public interface EventToken {

    /**
     * 添加群聊
     */
    public static final String GROUP_CREATE = "GROUP_CREATE";

    /**
     * 添加群聊成员
     */
    public static final String GROUP_ADD_MEMBERS = "GROUP_ADD_MEMBERS";

    /**
     * 退出群聊
     */
    public static final String GROUP_LEAVE = "GROUP_LEAVE";

    /**
     * 删除群聊成员
     */
    public static final String GROUP_DELETE_MEMBER = "GROUP_DELETE_MEMBER";

    /**
     * 通讯录联系人变动
     */
    public static final String CONTACT_CHANGED = "CONTACT_CHANGED";

    /**
     * 通讯录联系人变动
     */
    public static final String CONVERSATION_CHANGED = "CONVERSATION_CHANGED";

    /**
     * 联系人备注更改
     */
    public static final String REMARK_CHANGED = "REMARK_CHANGED";

    //add by linzhangbin 2017/6/5 保存我的工作名片
    /**
     * 保存我的工作名片
     */
    public static final String SAVE_WORK_CARD_MYCARD = "SAVE_WORK_CARD_MYCARD";
    //add by linzhangbin 2017/6/5 保存我的工作名片

    //add by linzhangbin 2017/6/5 保存联系人详情工作名片
    /**
     * 保存我的工作名片
     */
    public static final String SAVE_WORK_CARD_DETAIL = "SAVE_WORK_CARD_DETAIL";
    //add by linzhangbin 2017/6/5 保存联系人详情工作名片

    //add by linzhangbin 2017/6/5 保存我的其他电话
    /**
     * 保存我的工作名片
     */
    public static final String SAVE_OTHER_PHONE = "SAVE_OTHER_PHONE";
    //add by linzhangbin 2017/6/5 保存我的其他电话 end

    //modify by linzhangbin 2017/6/12 选择完省市县
    /**
     * 选择完省市区
     */
    public static final String CHOOSEAREA_WORKCARD = "CHOOSEAREA_COMPLETE";
    public static final String CHOOSEAREA_DELIVERY = "CHOOSEAREA_COMPLETE";
    //modify by linzhangbin 2017/6/12 选择完省市县 end

    //add by linzhangbin 2017/6/9 保存添加的电话
    /**
     * 创建联系人保存其他的联系方式
     */
    public static final String SAVE_ADDED_PHONE = "SAVE_ADDED_PHONE";
    //add by linzhangbin 2017/6/9 保存添加的电话 end

    /**
     * 编辑联系人保存其他的联系方式
     */
    public static final String SAVE_DETAIL_PHONE = "SAVE_DETAIL_PHONE";

    /**
     * 创建联系人保存姓名和电话
     */
    public static final String SAVE_PHONE_NAME = "SAVE_PHONE_NAME";

    /**
     * 保存新的联系人成功
     */
    public static final String ADD_NEW_CONTACT = "ADD_NEW_CONTACT";


    //add by linzhangbin 2017/6/22 通过好友验证 start
    public static final String CONFIRM_REQUEST = "CONFIRM_REQUEST";
    //add by linzhangbin 2017/6/22 通过好友验证 end

    public static final String SAVE_EXPRESS = "SAVE_EXPRESS";
    public static final String SELECT_BANK = "SELECT_BANK";
    public static final String SHOW_USER_INFO = "SHOW_USER_INFO";
    public static final String SAVE_MY_ACCOUNT = "SAVE_MY_ACCOUNT";
    public static final String ADDED_ACCOUNT = "ADDED_ACCOUNT";

    //add by linzhangbin 2017/6/27 保存添加联系人编辑名片 start
    /**
     * 保存我的工作名片
     */
    public static final String SAVE_WORK_CARD_ADDNEW = "SAVE_WORK_CARD_ADDNEW";

    //add by linzhangbin 2017/6/27 保存添加联系人编辑名片 end

    /**
     * 保存添加名片的编辑保存
     */
    public static final String SAVE_ADD_ACCOUNT = "SAVE_ADD_ACCOUNT";

    /**
     * 保存名片详情的编辑保存
     */
    public static final String SAVE_DETAIL_ACCOUNT = "SAVE_DETAIL_ACCOUNT";

    /**
     * 保存添加名片的快递地址
     */
    public static final String SAVE_ADD_EXPRESS = "SAVE_ADD_EXPRESS";

    /**
     * 保存名片详情的快递地址
     */
    public static final String SAVE_DETAIL_EXPRESS = "SAVE_DETAIL_EXPRESS";

    /**
     * 删除名片详情的帐号信息
     */
    public static final String DELETE_DETAIL_ACCOUNT = "DELETE_DETAIL_ACCOUNT";

    /**
     * Added by wangshuai 2017-07-06
     * <p>
     * edit express address
     */
    String EDIT_EXPRESS = "EDIT_EXPRESS";
}
