package cn.okline.icm.libary;

import java.io.Serializable;

public class IPHost implements Serializable
{
	private static final long serialVersionUID = 67248788883429358L;
	private int[] IPv = new int[4]; //IP data based dword for IPv4
	private int wPort; // Port associated with IP.
	private short wFlags; // If wFlags is 0x0001, the IP value is for IPv6; if wFlags is 0x0000, it is for IPv4.

	public int[] getIPv()
	{
		return IPv;
	}

	public void setIPv(int[] iPv)
	{
		IPv = iPv;
	}

	public int getwPort()
	{
		return wPort;
	}

	public void setwPort(int wPort)
	{
		this.wPort = wPort;
	}

	public short getwFlags()
	{
		return wFlags;
	}

	public void setwFlags(short wFlags)
	{
		this.wFlags = wFlags;
	}

};