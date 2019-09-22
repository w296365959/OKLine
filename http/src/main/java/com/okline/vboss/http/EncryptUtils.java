package com.okline.vboss.http;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import timber.log.Timber;

/**
 * OKLine(ShenZhen) co.,Ltd.<br/>
 * 作者 : Shi Haijun <br/>
 * 邮箱 : haijun@okline.cn<br/>
 * 日期 : 2016/3/11 9:54<br/>
 * 描述 : 加解密工具
 */
public class EncryptUtils {
    public static final String DEFAULT_CHARSET_NAME = "UTF-8";
    private static final char[] DIGITS_LOWER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private static final char[] DIGITS_UPPER = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    /**
     * 原生的MD5加密
     *
     * @param src
     * @return
     */
    public static String md5Hex(String src) {
        return encodeHexString(md5(getBytesUTF8(src)));
    }

    private static byte[] getBytesUTF8(String data) {
        if (data == null) {
            return null;
        } else {
            try {
                return data.getBytes(DEFAULT_CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException();
            }
        }
    }

    private static String encodeHexString(byte[] data) {
        return new String(encodeHex(data, true));
    }

    private static char[] encodeHex(byte[] data, boolean toLowerCase) {
        return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
    }

    private static char[] encodeHex(byte[] data, char[] toDigits) {
        int l = data.length;
        char[] out = new char[l << 1];
        int i = 0;

        for (int j = 0; i < l; ++i) {
            out[j++] = toDigits[(240 & data[i]) >>> 4];
            out[j++] = toDigits[15 & data[i]];
        }

        return out;
    }

    private static byte[] md5(byte[] data) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(data);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    /**
     * 密码加密
     */
    public static String password(String src) {
        try {
            return Encrypt.e(src, Encrypt.ENCRYPT_TYPE.USER_PWD);
        } catch (Exception e) {
            Timber.tag("EncryptUtil").e(e, "Fail to encrypt password");
        }
        return null;
    }

    /**
     * AES加密
     *
     * @param src        待加密内容
     * @param encryptKey 加密密钥
     * @return
     */
    public static String encodeAes(String src, String encryptKey) {
        try {
            return AesEncrypt.aesEncrypt(src, encryptKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES解密
     *
     * @param src        待解密内容
     * @param decryptKey 解密密钥
     * @return
     */
    public static String decodeAes(String src, String decryptKey) {
        try {
            return AesEncrypt.aesDecrypt(src, decryptKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String encodeBase64(String data) {
        if (data == null) return null;
        return DESBase64Utils.encode(getBytesUTF8(data));
    }

    public static String decodeBase64(String src) {
        if (src == null) return null;
        byte[] bytes = DESBase64Utils.decode(src);
        return new String(bytes);
    }

    /**
     * 给网络请求数据签名
     *
     * @param data  消息头
     * @param pId   网络请求pId
     * @param pCode 网络请求的pCode
     * @return 签名后的字串
     */
    public static String sign(String data, String pId, String pCode) {
        if (data == null || pId == null || pCode == null) {
            throw new IllegalArgumentException("Parameter(param,pId,pCode) must not be null");
        }

        String feed = md5Hex((pId + pCode).toUpperCase());
        return md5Hex(data + feed.toUpperCase());
    }

    /**
     * 验证网络请求返回的数据合法性
     *
     * @param param  返回的param，
     * @param pId    返回的pId
     * @param pCode  签名是用到的pCode
     * @param signed 返回的验签字串
     * @return 比对成功则返回true，否则返回false
     */
    public static boolean verifySign(String param, String pId, String pCode, String signed) {
        if (param == null || pId == null || pCode == null || signed == null) return false;

        String sign = sign(param, pId, pCode);
        Timber.tag("OCardVerifier").i("sign(%s,%s,%s) -> %s(%d), compare to %s(%d)", param, pId, pCode, sign, sign.length(), signed, signed.length());

        return signed.equals(sign);
    }
}
