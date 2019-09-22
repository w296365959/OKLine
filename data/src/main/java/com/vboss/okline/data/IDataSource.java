package com.vboss.okline.data;

import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/4/7 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public interface IDataSource<T> {

    Boolean save(T t);

    boolean saveAll(List<T> list);



}
