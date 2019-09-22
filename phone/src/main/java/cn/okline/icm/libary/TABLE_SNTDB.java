package cn.okline.icm.libary;

import java.io.Serializable;

public class TABLE_SNTDB implements Serializable
{

	private static final long serialVersionUID = 4859453736822171139L;
	byte[] SI = new byte[13]; //代理节点SI
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