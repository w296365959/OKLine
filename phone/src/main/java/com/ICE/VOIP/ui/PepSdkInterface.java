package com.ICE.VOIP.ui;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Jiang Zhongyuan <br/>
 * Email   : jiangzhongyuan@okline.cn <br/>
 * Date    : 17/5/4 <br/>
 * Summary : 在这里描述Class的主要功能
 */
public class PepSdkInterface {

    /**
     * 初始化配置文件
     * @param pCfgPara
     * @return
     */
    public static native boolean PePCfg(Pep_Cfg_Para pCfgPara);

    public static native int PePInitialize();

    public static native void PePDestroy(int handle);

    public static class Pep_Cfg_Para {
        long sdkVerId; // 0
        long dwAutoUploadFileSize; // 自动上传文件阀值大小,单位:byte,default 3M,
        long dwAutoDownLoadFileSize; // 自动下载文件阀值大小,单位:byte,default 1M,
        String szFileSaveDir = new String(); // 文件保存路径format: c:/dir/
        String szCharSet = new String(); // 字符集如"GB2312"
        String szPepServer3N = new String(); // PeP Server 的 3N 号码
        String szAppDataSaveDir = new String();
    }

}
