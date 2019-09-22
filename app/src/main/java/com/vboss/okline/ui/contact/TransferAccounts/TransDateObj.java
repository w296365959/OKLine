package com.vboss.okline.ui.contact.TransferAccounts;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.vboss.okline.data.entities.CardLog;

import java.util.ArrayList;
import java.util.List;

/**
 * OkLine(HangZhou) co., Ltd. <br/>
 * Author: Yuan shaoyu <br/>
 * Email : yuer@okline.cn <br/>
 * Date  : 2017/5/4 10:51 <br/>
 * Summary  : 在这里描述Class的主要功能
 */
public class TransDateObj implements Parent<TransLog> {
    private String name;
    private List<TransLog> list = new ArrayList<>();

    public TransDateObj(String name) {
        this.name = name;
    }

    @Override
    public List<TransLog> getChildList() {
        return list;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setList(List<TransLog> list) {
        this.list.clear();
        this.list.addAll(list);
    }
}
