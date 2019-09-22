package cn.okline.icm.libary;

import java.io.Serializable;

public class OPEN_DATA_ACK implements Serializable
{
	private static final long serialVersionUID = -4404147686075552741L;
	public String uuid;
	short certCount;
	byte[] dbCert;
	short sosCount;
	TABLE_SOSDB[] dbSOS = new TABLE_SOSDB[20];
	short sntCount;
	TABLE_SNTDB[] dbSNT = new TABLE_SNTDB[20];

	public short getCertCount()
	{
		return certCount;
	}

	public void setCertCount(short certCount)
	{
		this.certCount = certCount;
	}

	public byte[] getDbCert()
	{
		return dbCert;
	}

	public void setDbCert(byte[] dbCert)
	{
		this.dbCert = dbCert;
	}

	public short getSosCount()
	{
		return sosCount;
	}

	public void setSosCount(short sosCount)
	{
		this.sosCount = sosCount;
	}

	public TABLE_SOSDB[] getDbSOS()
	{
		return dbSOS;
	}

	public void setDbSOS(TABLE_SOSDB[] dbSOS)
	{
		this.dbSOS = dbSOS;
	}

	public short getSntCount()
	{
		return sntCount;
	}

	public void setSntCount(short sntCount)
	{
		this.sntCount = sntCount;
	}

	public TABLE_SNTDB[] getDbSNT()
	{
		return dbSNT;
	}

	public void setDbSNT(TABLE_SNTDB[] dbSNT)
	{
		this.dbSNT = dbSNT;
	}

	public String getUuid()
	{
		return uuid;
	}

	public void setUuid(String uuid)
	{
		this.uuid = uuid;
	}

	@Override
	public String toString()
	{
		return "OPEN_DATA_ACK [uuid=" + uuid + ", certCount=" + certCount + ",   sosCount=" + sosCount + ", sntCount="
				+ sntCount + "]";
	}

}
