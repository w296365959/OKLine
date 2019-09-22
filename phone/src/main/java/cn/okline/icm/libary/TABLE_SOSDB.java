package cn.okline.icm.libary;

import java.io.Serializable;

public class TABLE_SOSDB implements Serializable
{
	private static final long serialVersionUID = 1L;
	byte[] SI = new byte[13];
	IPHost IpAddr;

	public byte[] getSI()
	{
		return SI;
	}

	public void setSI(byte[] sI)
	{
		SI = sI;
	}

	public IPHost getIpAddr()
	{
		return IpAddr;
	}

	public void setIpAddr(IPHost ipAddr)
	{
		IpAddr = ipAddr;
	}

}