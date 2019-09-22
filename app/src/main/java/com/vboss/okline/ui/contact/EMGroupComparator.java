package com.vboss.okline.ui.contact;

import com.hyphenate.chat.EMGroup;
import com.vboss.okline.utils.PinyinUtils;

import java.util.Comparator;

import static com.vboss.okline.utils.PinyinUtils.getFirstHeadWordChar;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/4/24 14:35
 * Desc :
 */

public class EMGroupComparator implements Comparator<EMGroup> {

    @Override
    public int compare(EMGroup o1, EMGroup o2) {

        String letter1;
        if (o1.getGroupName()!=null){
            letter1 = PinyinUtils.getFirstHeadWordChar(o1.getGroupName());
        }else{
            letter1 = "#";
        }

        String letter2;
        if (o2.getGroupName() != null){
            letter2 = PinyinUtils.getFirstHeadWordChar(o1.getGroupName());
        }else{
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

}
