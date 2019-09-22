package com.vboss.okline.ui.record.date;

import com.bignerdranch.expandablerecyclerview.model.Parent;
import com.vboss.okline.data.entities.CardLog;

import java.util.ArrayList;
import java.util.List;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/2 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class DateObj implements Parent<CardLog> {
    private String name;
    private List<CardLog> list = new ArrayList<>();

    public DateObj(String name) {
        this.name = name;
    }

    @Override
    public List<CardLog> getChildList() {
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

    public void setList(List<CardLog> list) {
        this.list.clear();
        this.list.addAll(list);
    }
}
