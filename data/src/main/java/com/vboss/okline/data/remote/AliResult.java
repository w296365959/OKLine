package com.vboss.okline.data.remote;

/**
 * OKLine(HangZhou) co.,Ltd.<br/>
 * Author  : Shi Haijun <br/>
 * Email   : haijun@okline.cn <br/>
 * Date    : 2017/6/2 <br/>
 * Summary : 阿里实人认证结果实体类
 */
public class AliResult {

    /**
     * Token有效时间
     */
    private long expire;
    /**
     * 认证Token
     */
    private String token;
    /**
     * 认证状态：1;初始 2;认证通过 3;认证失败 4;转人工 5;未认证用户取消
     */
    private int state;

    public long getExpire() {
        return expire;
    }

    public String getToken() {
        return token;
    }

    public int getState() {
        return state;
    }

    public static class Status {
        boolean state;
        boolean reState;

        /**
         * 状态
         * <p>
         * false未认证
         * true已认证
         *
         * @return
         */
        public boolean getState() {
            return state;
        }

        /**
         * 复核状态被置位
         * <p>
         * false未认证
         * true已认证"
         */
        public boolean getReState() {
            return reState;
        }

        @Override
        public String toString() {
            return "Status{" +
                    "state=" + state +
                    ", reState=" + reState +
                    '}';
        }
    }


}
