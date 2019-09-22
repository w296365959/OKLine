package com.vboss.okline.nfc.reader;

import com.vboss.okline.nfc.bean.NfcCard;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2016/12/14 19:00
 * Desc :
 */
public interface ReaderCompleteListener {

    void readerCardComplete(NfcCard card);

    void readerCardNoComplete(String cardNo);
}
