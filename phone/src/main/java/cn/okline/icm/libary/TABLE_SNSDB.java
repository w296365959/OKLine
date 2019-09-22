package cn.okline.icm.libary;


public class TABLE_SNSDB
{

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