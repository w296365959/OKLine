/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hyphenate.easeui;

public class EaseConstant {
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";
    
    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";
    public static final String MESSAGE_ATTR_EXPRESSION_ID = "em_expression_id";
    
    public static final String MESSAGE_ATTR_AT_MSG = "em_at_list";
    public static final String MESSAGE_ATTR_VALUE_AT_MSG_ALL = "ALL";

    public  static final String  MESSAGE_ATTR_IS_PHPONE="is_phone_call";//是不是一个扩展的电话信息
    public  static final String MESSAGE_IS_PHONE_CALL="phone_call";//是一个电话信息
    public static final String MESSAGE_PHONE_TIME="phone_time";

    public  static final String  MESSAGE_ATTR_IS_DELIVERY="is_delivery";//是不是一个扩展的快递信息

    
    
	public static final int CHATTYPE_SINGLE = 1;
    public static final int CHATTYPE_GROUP = 2;
    public static final int CHATTYPE_CHATROOM = 3;
    public static final String EXTRA_CHAT_TYPE = "chatType";
    public static final String EXTRA_USER_ID = "userId";
    public static final String  EXTRA_PHONE_NUM="phonenum";
    public static final String EXTRA_REAL_NAME ="toNick";
    public static final String EXTRA_TO_REMARK ="toRemark";
    public static final String EXTRA_TO_RELATION ="relationState";
//    public static final String  EXTRA_TO_AVATAR="avatar";
}
