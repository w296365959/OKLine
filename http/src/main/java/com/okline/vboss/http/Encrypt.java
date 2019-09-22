package com.okline.vboss.http;

/**
 * OkLine(ShenZhen) co., Ltd.<br/>
 * Author: Shi Haijun<br/>
 * Email : haijun@okline.cn<br/>
 * Date  : 2016/3/29<br/>
 * Desc  :
 */
class Encrypt {

    private static int PWD_ASC_PARA = 96;
    private static int PWD_FINAL_PARA = 128;
    private static int SSO_ASC_PARA = 64;
    private static int SSO_FINAL_PARA = 96;

    public Encrypt() {
    }

    private void U() {
    }

    private static boolean isContainChinese(String str) throws Exception {
        boolean rtn = false;
        int byteLength = str.getBytes().length;
        int strLength = str.length();
        if (byteLength != strLength) {
            rtn = true;
        }

        return rtn;
    }

    public static String e(String a, String type) throws Exception {
        if (StringUtils.isBlank(a)) {
            System.err.print(" Parameter is empty");
        }

        if (isContainChinese(a)) {
            System.err.print("Parameter contain chinese");
        }

        int asc_para = PWD_ASC_PARA;
        int final_para = PWD_FINAL_PARA;
        if (ENCRYPT_TYPE.SSO_SERVER.equals(type)) {
            asc_para = SSO_ASC_PARA;
            final_para = SSO_FINAL_PARA;
        }

        char[] ch = a.toCharArray();
        StringBuilder sb = new StringBuilder();

        for (int i = 1; i <= ch.length; ++i) {
            char tmp = ch[i - 1];
            int var5 = tmp * i + ch.length;
            var5 %= asc_para;
            var5 = (var5 + 32) % final_para;
            sb.append((char) var5);
        }

        return sb.toString();
    }

    public static class ENCRYPT_TYPE {
        public static final String USER_PWD = "pwd";     //����
        public static final String SSO_SERVER = "sso";      //sso����
    }
}
