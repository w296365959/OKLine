package com.vboss.okline.ui.auth.present;

import android.content.Context;
import android.graphics.Bitmap;

import com.idcard.CardInfo;
import com.idcard.TFieldID;
import com.vboss.okline.data.local.AppConfig;
import com.vboss.okline.data.local.SPUtils;
import com.vboss.okline.utils.AppUtil;

/**
 * Okline(Hangzhou)co,Ltd<br/>
 * Author: Mengyupeng<br/>
 * Email:  mengyupeng@okline.cn</br>
 * Date : $(DATE) </br>
 * Summary:
 */

public class GetIdInfoPresent {
    Context mContext;
    private static GetIdInfoPresent  getIdInfoPresent;
    private Bitmap idImage;

    private  GetIdInfoPresent(Context mContext){
      this.mContext=mContext;
    }

    public static GetIdInfoPresent getInstance(Context context){
        if(getIdInfoPresent == null){
            getIdInfoPresent = new GetIdInfoPresent(context);
        }
        return getIdInfoPresent;
    }


    public void savePicData(Bitmap bmp,CardInfo cardInfo) {
        if(bmp!=null){
            setIdImage(bmp);
            AppUtil.saveBmpToLoalSdCard(bmp, AppConfig.IMAGE_CACHE_DIR,AppConfig.IMAGE_ID_PIC);
        }

        if(cardInfo!=null){
            SPUtils.saveSp(mContext,AppConfig.SP_ID_NAME,cardInfo.getFieldString(TFieldID.NAME));
            SPUtils.saveSp(mContext,AppConfig.SP_ID_NUM,cardInfo.getFieldString(TFieldID.NUM));
        }
    }


    public Bitmap getIdImage() {
        return idImage;
    }

    public void setIdImage(Bitmap idImage) {
        this.idImage = idImage;
    }
}
