package com.ICE.VOIP.ui;

public class VOIPMessage {
	public long nState;
	public long lData;

	// 并机 解并机 返回结果
	public String pAssoNum = new String();
	public long nAssoEvent;
	public long nAssoResult;

	// 预并机返回验证码
	public String verifyAssocCode = new String();
	public boolean fAssocResultState;

	// 绑定返回验证码
	public String RandomCode = new String();
	public String serverDestNum = new String();

	// 手机信息
	public String aszCountryCode;
	public String aszAreaCode;
	public String aszAssoNum;// 并机号
	public String aszAssoID;// 并机ID
	public boolean fAssociated;// 是否并机
	public String aszMobileNum;// 手机号码

	// 运营商名称、时间
	public long PlatformTime;
	public String CarrierName;

	@Override
	public String toString() {
		return "VOIPMessage [nState=" + nState + ", lData=" + lData
				+ ", pAssoNum=" + pAssoNum + ", nAssoEvent=" + nAssoEvent
				+ ", nAssoResult=" + nAssoResult + ", RandomCode=" + RandomCode
				+ ", serverDestNum=" + serverDestNum + ", aszCountryCode="
				+ aszCountryCode + ", aszAreaCode=" + aszAreaCode
				+ ", aszAssoNum=" + aszAssoNum + ", aszAssoID=" + aszAssoID
				+ ", fAssociated=" + fAssociated + ", aszMobileNum="
				+ aszMobileNum + ", PlatformTime=" + PlatformTime
				+ ", CarrierName=" + CarrierName + "]";
	}

}
