package com.vboss.okline.data.local;

import com.vboss.okline.data.entities.User;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/27 <br/>
 * Summary : DATA模块数据配置类
 */
public interface DataConfig {

    /**
     * 默认的欧乐号码
     */
    String DEFAULT_OLNO = "OLHZ310571000000000436";

    /**
     * 默认用户
     */
    User DEFAULT_USER = new User("18167158550", "石海军", "OLHZ310571000000000436", "2C50C7", "xxx", "87:90:20:2C:50:C7", null);

}
