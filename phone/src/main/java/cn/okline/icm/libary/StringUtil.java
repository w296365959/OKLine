package cn.okline.icm.libary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.Vector;


public class StringUtil
{
    public static final String ISO88591 = "ISO-8859-1";

    public static final String GBK = "GBK";

    public static final String UTF8 = "UTF-8";

    private StringUtil()
    {
    }

    /**
     * 整数转 IP
     * @param i
     * @return
     */
    public static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }
    /**
     * 将字节转换为对象
     * @param bytes
     * @return
     */
    public static Object byteToObject(byte[] bytes)
    {
        if (bytes == null || bytes.length < 1)
        {
            return null;
        }

        Object obj = null;
        try
        {
            // bytearray to object
            ByteArrayInputStream bi = new ByteArrayInputStream(bytes);
            ObjectInputStream oi = new ObjectInputStream(bi);

            obj = oi.readObject();
            bi.close();
            oi.close();
        }
        catch (Exception e)
        {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return obj;
    }
    
    /**
     * byte[]转换为16进制
     * @param src
     * @return
     */
	public static String byteToHexString(byte[] src)
	{
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0)
		{
			return null;
		}
		for (int i = 0; i < src.length; i++)
		{
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2)
			{
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

    /**
     * 将对像转换为字节
     * @param obj
     * @return
     */
    public static byte[] objectToByte(Object obj)
    {
        if (obj == null)
        {
            return null;
        }

        byte[] bytes = null;
        try
        {
            // object to bytearray
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);

            bytes = bo.toByteArray();

            bo.close();
            oo.close();
        }
        catch (Exception e)
        {
            System.out.println("translation" + e.getMessage());
            e.printStackTrace();
        }
        return bytes;
    }
    
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static boolean StringConvertBoolean(String key) throws Exception
    {
        if (key == null)
        {
            throw new Exception("String[" + key + "] is null.");
        }
        Object o = key;
        boolean result = false;
        if (o.equals(Boolean.FALSE) || (o instanceof String && ((String) o).equalsIgnoreCase("false")))
        {
            result = false;
        }
        else if (o.equals(Boolean.TRUE) || (o instanceof String && ((String) o).equalsIgnoreCase("true")))
        {
            result = true;
        }
        if (o.equals(Boolean.FALSE) || (o instanceof String && ((String) o).equalsIgnoreCase("0")))
        {
            result = false;
        }
        else if (o.equals(Boolean.TRUE) || (o instanceof String && ((String) o).equalsIgnoreCase("1")))
        {
            result = true;
        }
        return result;
    }

    /**
     * 将字符串数据转换成长整型
     * @param args
     * @return
     */
    public static Long[] StringConvertLong(String[] args)
    {

        if (args == null)
            return null;
        if (args.length < 1)
            return new Long[0];
        Long[] result = new Long[args.length];
        for (int i = 0; i < args.length; i++)
        {
            result[i] = new Long(args[i]);
        }
        return result;

    }

    public static boolean ignoreCaseHas(String str, String subStr)
    {
        if (null == str || null == subStr)
        {
            return false;
        }
        return str.toUpperCase().indexOf(subStr.toUpperCase()) >= 0;
    }

    /**
     * 将Unicode码字符串转为为GBK码
     *
     * @param strIn
     * @return
     */
    public static String GBToUnicode(String strIn)
    {
        String strOut = null;

        if (isEmpty(strIn))
        {
            return strIn;
        }
        try
        {
            byte[] b = strIn.getBytes("GBK");
            strOut = new String(b, "ISO8859_1");
        }
        catch (Exception e)
        {
        }
        return strOut;
    }

    /**
     * 将GBK码转换为Unicode码
     *
     * @param strIn
     * @return
     */
    public static String unicodeToGB(String strIn)
    {
        String strOut = null;

        if (isEmpty(strIn))
        {
            return strIn;
        }
        try
        {
            byte[] b = strIn.getBytes("ISO8859_1");
            strOut = new String(b, "GBK");
        }
        catch (Exception e)
        {
        }
        return strOut;
    }

    /**
     * 字符串编码类型转换
     *
     * @param str
     *            待转换的字符串
     * @param oldCharset
     *            待转换的字符串的编码类型
     * @param newCharset 新的编码类型
     * @return 转换成新编码类型的字符串
     */

    public static String encode(String str, String oldCharset, String newCharset)
    {
        if (isEmpty(str))
        {
            return str;
        }
        String newStr = null;
        try
        {
            newStr = new String(str.getBytes(oldCharset), newCharset);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return newStr;

    }

    /**
     * ISO字符串编码类型转换成GBK
     *
     * @param str
     *            待转换的字符串
     *
     * @return 转换成GBK类型的字符串
     */

    public static String ISO2GBK(String str)
    {
        if (isEmpty(str))
        {
            return str;
        }
        String newStr = null;
        try
        {
            newStr = new String(str.getBytes(ISO88591), GBK);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return newStr;

    }

    /**
     * 将以sgn为分隔符的字符串转化为数组
     *
     * @param str
     * @param sgn
     * @return 更新日志：<br>
     */
    public static String[] split(String str, String sgn)
    {
        String[] returnValue = null;
        if (isNotEmpty(str))
        {
            Vector<String> vectors = new Vector<String>();
            int i = str.indexOf(sgn);
            String tempStr = "";
            for (; i >= 0; i = str.indexOf(sgn))
            {
                tempStr = str.substring(0, i);
                str = str.substring(i + 1);
                vectors.addElement(tempStr);
            }
            if (!str.equals(""))
            {
                vectors.addElement(str);
            }
            int size = vectors.size();
            returnValue = new String[size];
            for (i = 0; i < size; i++)
            {
                returnValue[i] = (String) vectors.get(i);
                returnValue[i] = returnValue[i].trim();
            }
        }
        return returnValue;
    }

    /**
     * 把数组转化为字符串
     *
     * @param array
     *            字符串数组
     * @param split
     *            分割字符串
     * @return string whose format is like "1,2,3"
     */
    public static String arrayToStr(String[] array, String split)
    {
        if (array == null || array.length < 1)
        {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < array.length; i++)
        {
            if (i > 0)
            {
                sb.append(split);
            }
            sb.append(strnull(array[i]));
        }
        return sb.toString();

    }

    /**
     * transform array into string format
     *
     * @param array
     *            array to be transformed
     * @return string whose format like " '1','2','3'"
     */
    public static String arrayToStrWithStr(String[] array, String split)
    {
        return arrayToStrWithStr(array, split, "0");

    }

    /**
     * transform array into string format
     *
     * @param array
     *            array to be transformed
     * @param split
     * @param optType
     *            操作类型0:普通; 1:在解析角色时去掉/;
     * @return string whose format like " '1','2','3'"
     */
    public static String arrayToStrWithStr(String[] array, String split, String optType)
    {
        if (array == null || array.length < 1)
        {
            return null;
        }
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < array.length; i++)
        {
            if (i > 0)
            {
                sb.append(",");
            }
            sb.append("'");
            if (optType.equals("1"))
            {
                String temp = strnull(array[i]);
                sb.append(temp.substring(1, temp.length()));
            }
            else
            {
                sb.append(strnull(array[i]));
            }
            sb.append("'");

        }
        return sb.toString();

    }

    /**
     * 将以sgn为分隔符的字符串转化为数组
     *
     * @param str
     * @param sgn
     * @return 更新日志：<br>
     */
    public static String[] strConvertoArray(String str, String sgn)
    {
        StringTokenizer st = new StringTokenizer(str, sgn);
        String retstr[] = new String[st.countTokens()];
        for (int i = 0; st.hasMoreTokens(); i++)
        {
            retstr[i] = st.nextToken();
        }
        return retstr;
    }

    /**
     * 将以sgn为分隔符的字符串转化为List链表
     *
     * @param str
     *            String 要处理的字符串
     * @param sgn
     *            String 间隔符
     * @return List
     */
    public static List strConvertoList(String str, String sgn)
    {
        StringTokenizer st = new StringTokenizer(str, sgn);
        List<String> result = new LinkedList<String>();

        for (int i = 0; st.hasMoreTokens(); i++)
        {
            result.add(st.nextToken());
        }
        return result;
    }

    /**
     * 1 --> 00001将整数转化为指定长度字符串(lpMaxLength为5)
     *
     * @param lpInt
     * @param lpMaxLength
     * @return
     */
    public static String intToStr(int lpInt, int lpMaxLength)
    {
        int length, i;
        String returnValue = "";

        length = Integer.toString(lpInt).length();
        if (length < lpMaxLength)
        {
            i = lpMaxLength - length;
            while (i > 0)
            {
                returnValue = returnValue + "0";
                i--;
            }
            returnValue = returnValue + Integer.toString(lpInt);
        }
        else
        {
            returnValue = Integer.toString(lpInt);
        }
        return returnValue;
    }

    /**
     * 将字符串转化为int类型 Converts a string to integer. If fails is not throwing a
     * NumberFormatException, instead return 0.
     *
     * @param source
     *            待转换的字符串
     * @return int数据
     */
    public static int toInt(String source)
    {
        return toInt(source, 0);
    }

    public static int toInt(String source, int defVal)
    {
        try
        {
            return Integer.parseInt(source);
        }
        catch (NumberFormatException notint)
        {
            return defVal;
        }
    }

    public static int toInt(Object str)
    {
        return toInt(StringUtil.strnull(str), 0);
    }

    public static BigDecimal toBigDecimal(Object source)
    {
        return toBigDecimal(strnull(source, "0"));
    }

    /**
     * 将字符串转为数字型
     *
     * @param source
     * @return
     */
    public static BigDecimal toBigDecimal(String source)
    {
        try
        {
            if (null == source || "".equals(source))
            {
                return new BigDecimal(0);
            }
            else
            {

                return new BigDecimal(source);
            }
        }
        catch (NumberFormatException notint)
        {
            return new BigDecimal(0);
        }
    }

    public static Long toLong(Object source)
    {
        return toLong(strnull(source, "0"));
    }

    /**
     * 把字符串转化为Long
     *
     * @param source
     * @return
     */
    public static Long toLong(String source)
    {
        try
        {
            if (null == source || "".equals(source))
            {
                return new Long(0);
            }
            else
            {

                return new Long(source);
            }
        }
        catch (NumberFormatException notint)
        {
            return new Long(0);
        }
    }

    public static Long toLongNull(Object source)
    {
        if (isEmpty(source))
        {
            return null;
        }
        return toLongNull(source.toString().trim());
    }

    public static Double toDoubleNull(Object source)
    {
        if (isEmpty(source))
        {
            return null;
        }
        return toDoubleNull(source.toString().trim());
    }

    public static Double toDoubleNull(String source)
    {
        try
        {
            if (null == source || "".equals(source))
            {
                return null;
            }
            else
            {

                return new Double(source);
            }
        }
        catch (NumberFormatException notint)
        {
            return null;
        }
    }

    /**
     * 把字符串转化为Long,为空时返回null
     *
     * @param source
     * @return
     */
    public static Long toLongNull(String source)
    {
        try
        {
            if (null == source || "".equals(source))
            {
                return null;
            }
            else
            {

                return new Long(source);
            }
        }
        catch (NumberFormatException notint)
        {
            return null;
        }
    }

    /**
     * 取路径后的文件名，也就是路径字串最后一个斜杠后的字串
     *
     * @param path
     * @return
     */
    public static String getPathFile(String path)
    {
        String substr = "";
        try
        {
            if (isNotEmpty(path))
            {
                int i = path.lastIndexOf("/");
                substr = path.substring(i + 1).trim();
            }
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
        return substr;
    }

    /**
     * 取小数点后的字串，也就是小数点后的字串
     *
     * @param str
     * @return
     */
    public static String getLastTwo(String str)
    {
        String substr = "";
        try
        {
            if (isNotEmpty(str))
            {
                int i = str.lastIndexOf(".");
                substr = str.substring(i + 1).trim();
            }
        }
        catch (Exception ex)
        {
            System.err.println(ex);
        }
        return substr;
    }

    /**
     * 取得文件名的文件类型(如2003001.doc-->doc)
     *
     * @param lpFileName
     * @return
     */
    public static String getFileType(String lpFileName)
    {
        String lpReturnValue = "";

        if (isNotEmpty(lpFileName))
        {
            int i = lpFileName.lastIndexOf(".");
            lpReturnValue = lpFileName.substring(i, lpFileName.length());
        }
        return lpReturnValue;
    }

    /**
     * 返回位于 String 对象中指定位置的子字符串
     *
     * @param str
     * @param beginIndex
     *            指明子字符串的起始位置，该索引从 0 开始起算
     * @param endIndex
     *            指明子字符串的结束位置，该索引从 0 开始起算
     * @return 如果字符串为空则返回""
     */
    public static String getSubString(String str, int beginIndex, int endIndex)
    {
        String str1 = "";

        if (str == null)
        {
            str = "";
        }
        if (str.length() >= beginIndex && str.length() >= endIndex)
        {
            str1 = str.substring(beginIndex, endIndex);
        }
        else
        {
            str1 = str;
        }
        return str1;
    }

    /**
     * 判断参数是否为空： null, "null", " ", ""
     *
     * @param Str
     * @return true/false
     * @author chenzp mod 2006-03-28
     */
    public static boolean isEmpty(String Str)
    {
        if (null == Str || "null".equals(Str.trim()) || "".equals(Str.trim()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isEmpty(Object str)
    {
        if (null == str || "null".equals(str.toString().trim()) || "".equals(str.toString().trim()))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    public static boolean isNotEmpty(Object str)
    {
        return !isEmpty(str);
    }

    public static String strnull(Object str, String rpt)
    {
        if (isEmpty(str))
        {
            return rpt;
        }
        else
        {
            return str.toString().trim();
        }
    }

    /**
     * 将字符串逆序: 空时返回自身
     *
     * @param str
     *            ：闽A-1234
     * @return String 4321-A闽
     */
    public static String reverseString(String str)
    {
        if (isEmpty(str))
        {
            return str;
        }
        int length = str.length();
        StringBuffer temp = new StringBuffer(length);
        for (int i = length - 1; i >= 0; i--)
        {
            temp.append(str.charAt(i));
        }
        return temp.toString();
    }

    public static String trim(String str, String defaultStr)
    {
        return (null == str) ? defaultStr : str.trim();
    }

    public static String trim2Empty(Object str)
    {
        return (null == str) ? "" : trim(str.toString(), "");
    }

    public static String trim2Empty(String str)
    {
        return trim(str, "");
    }

    public static String trim2Null(String str)
    {
        return trim(str, null);
    }

    /**
     * 如果入参是null或者"",用另一入参rpt替代入参返回，否则返回入参的trim()
     *
     * @param Str
     * @return
     */
    public static String strnull(String Str, String rpt)
    {
        if (isEmpty(Str))
        {
            return rpt;
        }
        else
        {
            return Str.trim();
        }
    }

    /**
     * 为检查null值，如果为null，将返回""，不为空时将替换其非html符号
     *
     * @param strn
     * @return
     */
    public static String strnull(String strn)
    {
        return strnull(strn, "");
    }

    /**
     * 为检查null值，如果为null，将返回""，不为空时将替换其非html符号
     *
     * @param str
     * @return
     */
    public static String strnull(Object str)
    {
        String result = "";
        if (str == null)
        {
            result = "";
        }
        else
        {
            result = str.toString().trim();
        }
        return result;
    }

    /**
     * 适用于web层 为检查null值，如果为null，将返回"&nbsp;"，不为空时将替换其非html符号
     *
     * @param strn
     * @return
     */

    public static String repnull(String strn)
    {
        return strnull(strn, "&nbsp;");
    }

    /**
     * 把Date的转化为字符串，为空时将为"0000-00-00 00:00:00"
     *
     * @param strn
     * @return
     */
    public static String strnull(Date strn)
    {
        String str = "";

        if (strn == null)
        {
            str = "0000-00-00 00:00:00";
        }
        else
        {
            // strn.toGMTString();
            str = strn.toString();
        }
        return (str);
    }

    /**
     * 把BigDecimal的转换为字符串，为空将返回0
     *
     * @param strn
     * @return
     */
    public static String strnull(BigDecimal strn)
    {
        String str = "";

        if (strn == null)
        {
            str = "0";
        }
        else
        {
            str = strn.toString();
        }
        return (str);
    }

    /**
     * 把int的转换为字符串(不为空，只起转换作用)
     *
     * @param strn
     * @return
     */
    public static String strnull(int strn)
    {
        String str = String.valueOf(strn);
        return (str);
    }

    /**
     * 把float的转换为字符串(不为空，只起转换作用)
     *
     * @param strn
     * @return
     */
    public static String strnull(float strn)
    {
        String str = String.valueOf(strn);
        return (str);
    }

    /**
     * 把double的转换为字符串(不为空，只起转换作用)
     *
     * @param strn
     * @return
     */
    public static String strnull(double strn)
    {
        String str = String.valueOf(strn);
        return (str);
    }

    /**
     * 0-15转化为0-F
     *
     * @param bin
     * @return
     */
    public static char hex(int bin)
    {
        char retval;
        if (bin >= 0 && bin <= 9)
        {
            retval = (char) ('0' + bin);
        }
        else if (bin >= 10 && bin <= 15)
        {
            retval = (char) ('A' + bin - 10);
        }
        else
        {
            retval = '0';
        }
        return retval;
    }

    /**
     * 字符串替换
     *
     * @param content
     * @param oldString
     * @param newString
     */
    public static String replace(String content, String oldString, String newString)
    {
        if (content == null || oldString == null)
        {
            return content;
        }
        if (content.equals("") || oldString.equals(""))
        {
            return content;
        }

        String resultString = "";
        int stringAtLocal = content.indexOf(oldString);
        int startLocal = 0;
        while (stringAtLocal >= 0)
        {
            resultString = resultString + content.substring(startLocal, stringAtLocal) + newString;
            startLocal = stringAtLocal + oldString.length();
            stringAtLocal = content.indexOf(oldString, startLocal);
        }

        resultString = resultString + content.substring(startLocal, content.length());
        return resultString;
    }

    /**
     * 将字符串转为HTML格式输出
     *
     * @param strn
     */
    public static String formatToHTML(String strn)
    {
        StringBuffer dest = new StringBuffer();
        if (strnull(strn).equals(""))
        {
            dest.append("&nbsp;");
        }
        else
        {
            for (int i = 0; strn != null && i < strn.length(); i++)
            {
                char c = strn.charAt(i);
                if (c == '\n')
                {
                    dest.append("<br>");
                }
                else if (c == '\'')
                {
                    dest.append("&#39;");
                }
                else if (c == '\"')
                {
                    dest.append("&#34;");
                }
                else if (c == '<')
                {
                    dest.append("&lt;");
                }
                else if (c == '>')
                {
                    dest.append("&gt;");
                }
                else if (c == '&')
                {
                    dest.append("&amp;");
                }
                else if (c == 0x20)
                {
                    dest.append("&nbsp;");
                }
                else
                {
                    dest.append(c);
                }
            }
        }
        return (dest.toString());
    }

    public static String formatToHTML(String strn, int length)
    {
        int m = 0;
        StringBuffer dest = new StringBuffer();
        if (strnull(strn).equals(""))
        {
            dest.append("&nbsp;");
        }
        else
        {
            for (int i = 0; strn != null && i < strn.length(); i++)
            {
                m++;
                if (m == length)
                {
                    dest.append("...");
                    break;
                }
                char c = strn.charAt(i);
                if (c == '\n')
                {
                    dest.append("<br>");
                }
                else if (c == '\'')
                {
                    dest.append("&#39;");
                }
                else if (c == '\"')
                {
                    dest.append("&#34;");
                }
                else if (c == '<')
                {
                    dest.append("&lt;");
                }
                else if (c == '>')
                {
                    dest.append("&gt;");
                }
                else if (c == '&')
                {
                    dest.append("&amp;");
                }
                else if (c == 0x20)
                {
                    dest.append("&nbsp;");
                }
                else
                {
                    dest.append(c);
                }
            }
        }
        return (dest.toString());
    }

    public static String formate(String value, String argument)
    {
        return formate(value, new Object[]
        {
                argument
        });
    }

    public static String formate(String value, Object[] arguments)
    {
        if (value == null || value.length() == 0)
        {
            return "";
        }
        else
        {
            if (arguments != null && arguments.length > 0)
            {
                value = MessageFormat.format(value, arguments);
            }
            return value;
        }
    }

    /**
     * 将BigDecimal的转为HTML格式输出
     *
     * @param strb
     */
    public static String formatToHTML(BigDecimal strb)
    {
        String strn = "";
        if (strb == null)
        {
            strn = "&nbsp;";
        }
        else
        {
            strn = strnull(strb);
        }
        return strn;
    }

    /**
     * 将多行字符串转为有带有回车、换行的HTML格式
     *
     * @param source
     * @return
     */
    public static String nl2Br(String source)
    {
        if (strnull(source).equals(""))
        {
            return "&nbsp;";
        }
        StringBuffer dest = new StringBuffer(source.length());
        for (int i = 0; i < source.length(); i++)
        {
            char c;
            c = source.charAt(i);
            if (c == '\n')
            {
                dest.append("<br>");
            }
            else if (c == 0x20)
            {
                dest.append("&nbsp;");
            }
            else
            {
                dest.append(c);
            }
        }
        return dest.toString();
    }

    /**
     * 查找sourceStr中是否含有fieldStr,如果有返回true,如果没有，返回false
     *
     * @param sourceStr
     *            源字符串
     * @param fieldStr
     * @return boolean
     */

    public static boolean findString(String sourceStr, String fieldStr)
    {
        boolean StrExist = false;
        if (sourceStr.length() == 0)
        {
            return StrExist;
        }
        if (sourceStr.indexOf(fieldStr) >= 0)
        {
            StrExist = true;
        }
        return StrExist;
    }

    /**
     * This method takes a exception as an input argument and returns the
     * stacktrace as a string.
     *
     * @param exception
     * @return String the string of the exception's stacktrace
     */
    public static String getStackTrace(Throwable exception)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        if (exception.getCause() != null)
        {
            // exception.printStackTrace(pw);
        }
        return sw.toString();
    }

    /**
     * 功能说明： 给字符串数组排序
     *
     * @return String[] 排序后的字符串数组
     * @author Linxf@hsit.com.cn（2004-08-09）
     */
    public static String[] bubbleSort(String[] Arr)
    {
        int tag = 1;
        for (int i = 1; i < Arr.length && tag == 1; i++)
        {
            tag = 0;
            for (int j = 0; j < Arr.length - i; j++)
            {
                if (Arr[j].compareTo(Arr[j + 1]) > 0)
                {
                    String temp = Arr[j];
                    Arr[j] = Arr[j + 1];
                    Arr[j + 1] = temp;
                    tag = 1;
                }
            }
        }
        return Arr;
    }

    /**
     * 功能说明：依据ValueArr数组的排序，为ContentArr排序
     *
     * @param  ValueArr 排序依据的字符串数组
     * @param  ContentArr 要排序的内容数组
     * @return String[] 排序后的字符串数组
     * @author Linxf@hsit.com.cn（2004-08-09）
     */
    public static String[] bubbleSort(String[] ValueArr, String[] ContentArr)
    {
        int tag = 1;
        for (int i = 1; i < ValueArr.length && tag == 1; i++)
        {
            tag = 0;
            for (int j = 0; j < ValueArr.length - i; j++)
            {
                if (ValueArr[j].compareTo(ValueArr[j + 1]) > 0)
                {
                    String temp1 = ValueArr[j];
                    String temp2 = ContentArr[j];
                    ValueArr[j] = ValueArr[j + 1];
                    ContentArr[j] = ContentArr[j + 1];
                    ValueArr[j + 1] = temp1;
                    ContentArr[j + 1] = temp2;
                    tag = 1;
                }
            }
        }
        return ValueArr;
    }

    /**
     * 功能说明：冒泡排序
     *
     * @return int[] Arr 排序后的数组
     * @author Linxf@hsit.com.cn（2004-08-09）
     */
    public static int[] bubbleSort(int[] Arr)
    {
        int tag = 1;
        for (int i = 1; i < Arr.length && tag == 1; i++)
        {
            tag = 0;
            for (int j = 0; j < Arr.length - i; j++)
            {
                if (Arr[j] > Arr[j + 1])
                {
                    int temp = Arr[j];
                    Arr[j] = Arr[j + 1];
                    Arr[j + 1] = temp;
                    tag = 1;
                }
            }
        }
        return Arr;
    }

    /**
     * 将空字符串转为"0"字符串
     *
     * @param str
     * @return
     */
    public static String nullToZero(String str)
    {
        if (str == null || str.equals(""))
        {
            str = "0";
        }
        return str;
    }

    /**
     * 首字母大写
     *
     * @param word
     *            字符串
     * @author  2006-02-22
     */
    public static String upperFirstLetter(String word)
    {
        return (new StringBuffer(word.substring(0, 1).toUpperCase()).append(word.substring(1, word.length())))
                .toString();
    }

    /*
     * 如果str的长度不足len位,就把str补到len位(前面补0),补后的格式为:0..0str @param
     * len要补足的长度,str要补足的字符串 @return 补完0后的字符串(格式为:0..0str) @author
     */
    public static String appendZeroBefore(int len, String str)
    {
        if (StringUtil.isEmpty(str))
        {
            return "";
        }
        StringBuffer resultStr = new StringBuffer(str);
        int zeroLen = len - str.length();
        StringBuffer zeroStr = new StringBuffer();
        for (int i = 0; i < zeroLen; i++)
        {
            zeroStr.append("0");
        }
        resultStr = zeroStr.append(resultStr);
        return resultStr.toString();
    }

    /*
     * 将Java的Map对象转为Javascript的匿名数组 @author Linxf
     */
    public static String map2JsObject(Map map)
    {
        if (map == null)
            return "{}";
        Set<String> keySet = map.keySet();
        StringBuffer js = new StringBuffer().append("{");
        for (String key : keySet)
        {
            String value = (String) map.get(key);
            if (value != null)
            {
                js.append(", ").append(key).append(" : \\\"").append(value).append("\\\"");
            }
        }
        js.append("}");
        String jsObject = js.toString();
        if (jsObject.length() > 2)
        {
            return "{" + jsObject.substring(2);
        }
        else
        {
            return jsObject;
        }
    }

    public static Integer getLenth(String str)
    {
        int counter = 0;
        for (int i = 0; i < str.length(); i++)
        {
            char c = str.charAt(i);
            if (c < 255)
            {
                counter++;
            }
            else
            {
                counter = counter + 2;
            }
        }
        return counter;
    }

    /**
     * 是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str)
    {
        for (int i = 0, len = str.length(); i < len; i++)
        {
            char ch = str.charAt(i);
            if (ch > '9' || ch < '0')
            {
                return false;
            }
        }
        return true;
    }

    /**
     * 字符串转变为小写
     *
     * @param str
     * @return
     */
    public static String toLowerCase(String str)
    {
        if (StringUtil.isNotEmpty(str))
        {
            return str.toLowerCase();
        }
        return "";
    }

    /**
     * 字符串转变为大写
     *
     * @param str
     * @return
     */
    public static String toUpperCase(String str)
    {
        if (StringUtil.isNotEmpty(str))
        {
            return str.toUpperCase();
        }
        return "";
    }

    public static List split2List(String tar, String r)
    {
        List result = new ArrayList();
        if (isEmpty(tar))
        {
            return result;
        }
        String strs[] = tar.split(r);
        for (int i = 0; i < strs.length; i++)
        {
            result.add(strs[i]);
        }
        return result;
    }

    public static String mergeList(List list)
    {
        return mergeList(list, ",");
    }

    public static String mergeList(List list, String r)
    {
        if (list == null)
        {
            return null;
        }
        StringBuffer result = null;
        for (int j = 0, size = list.size(); j < size; j++)
        {
            String s = trim2Empty(list.get(j));
            if (isNotEmpty(s))
            {
                if (result == null)
                {
                    result = new StringBuffer(s);
                }
                else
                {
                    result.append(r);
                    result.append(s);
                }
            }
        }

        if (result != null)
        {
            return result.toString();
        }
        else
        {
            return null;
        }
    }

    public static String mergeListMap(List list, String key)
    {
        return mergeListMap(list, key, ",");
    }

    public static String mergeListMap(List list, String key, String spltstr)
    {
        StringBuffer result = null;
        Map tmp = null;
        if (list == null)
        {
            return null;
        }
        for (int j = 0, size = list.size(); j < size; j++)
        {
            tmp = (Map) list.get(j);
            String s = StringUtil.trim2Empty(tmp.get(key));
            if (isNotEmpty(s))
            {
                if (result == null)
                {
                    result = new StringBuffer(s);
                }
                else
                {
                    result.append(spltstr);
                    result.append(s);
                }
            }
        }

        if (result != null)
        {
            return result.toString();
        }
        else
        {
            return null;
        }
    }

    public static String[] getListMapByKey(List list, String key)
    {
        String[] result = null;
        if (list != null)
        {
            int size = list.size();
            result = new String[size];
            Map tmp = null;
            for (int j = 0; j < size; j++)
            {
                tmp = (Map) list.get(j);
                result[j] = StringUtil.trim2Empty(tmp.get(key));
            }
        }
        return result;
    }

    /**
     * ??list?????з??转换List 为 Map<groupKey,groupLs>
     *
     */
    public static Map groupList(List sourceLs, String groupKey)
    {
        Map result = new HashMap();
        if (null != sourceLs && sourceLs.size() > 0)
        {
            String group = null;
            Map tmp = null;
            List groupLs = null;
            for (int i = 0, len = sourceLs.size(); i < len; i++)
            {
                tmp = (Map) sourceLs.get(i);
                group = StringUtil.trim2Empty(tmp.get(groupKey));
                groupLs = (List) result.get(group);
                if (groupLs == null)
                {
                    groupLs = new ArrayList();
                }
                groupLs.add(tmp);
                result.put(group, groupLs);
            }
        }
        return result;
    }

    public static Map groupList(List sourceLs, String groupKey, String valueKey)
    {
        Map<String, List> result = new HashMap<String, List>();
        if (null != sourceLs && sourceLs.size() > 0)
        {
            String group = null, value = null;
            Map tmp = null;
            List groupLs = null;
            for (int i = 0, len = sourceLs.size(); i < len; i++)
            {
                tmp = (Map) sourceLs.get(i);
                group = StringUtil.trim2Empty(tmp.get(groupKey));
                value = StringUtil.trim2Empty(tmp.get(valueKey));
                groupLs = (List) result.get(group);
                if (groupLs == null)
                {
                    groupLs = new ArrayList();
                }
                groupLs.add(value);
                result.put(group, groupLs);
            }
        }
        return result;
    }

    /**
     * <Map>??Map<key,value>
     *
     * @param sourceLs
     * @param key
     * @param value
     * @return
     */
    public static Map list2Map(List sourceLs, String key, String value)
    {
        Map result = new HashMap();
        if (null != sourceLs && sourceLs.size() > 0)
        {
            String group = null;
            Map tmp = null;
            String val = null;
            for (int i = 0, len = sourceLs.size(); i < len; i++)
            {
                tmp = (Map) sourceLs.get(i);
                group = StringUtil.trim2Empty(tmp.get(key));
                val = StringUtil.trim2Empty(tmp.get(value));

                result.put(group, val);
            }
        }
        return result;
    }

    /**
     * ?????????е??????????????? "|a, b, c, d, e|..."
     *
     * @param collection
     *            : ??????
     *
     * @return
     */
    public static String buildCollection2Msg(Collection collection)
    {
        return buildCollection2Msg(collection, 5);
    }

    /**
     * ?????????е?map?????key????????????????? "|a, b, c, d, e|..."
     *
     * @param collection
     *            : ??????
     * @param key
     *
     * @return
     */
    public static String buildCollection2Msg(Collection collection, Object key)
    {
        return buildCollection2Msg(collection, key, 5);
    }

    /**
     * ?????????е?map?????key????????????????? "|a, b, c, d, e|..."
     *
     * @param collection
     *            : ??????
     * @param key
     * @param unitNum
     *            : ?м????
     *
     * @return
     */
    public static String buildCollection2Msg(Collection collection, Object key, int unitNum)
    {
        return buildCollection2Msg(collection, new Object[]
        {
                key
        }, unitNum);
    }

    public static String buildContent(String content, int limitlength)
    {
        if (content.length() > limitlength)
        {
            return content.substring(0, limitlength) + "...";
        }
        else
        {
            return content;
        }
    }

    /**
     * 转化成Double数据类型
     * @param source
     * @return
     */
    public static Double toDouble(Object source)
    {
        return toDouble(strnull(source, "0"));
    }

    public static Double toDouble(String source)
    {
        try
        {
            if (null == source || "".equals(source))
            {
                return new Double(0);
            }
            else
            {

                return new Double(source);
            }
        }
        catch (NumberFormatException notint)
        {
            return new Double(0);
        }
    }

    public static String getNotNullStr(String str)
    {
        if (str == null)
            return "";
        return str;
    }

    public static String getNotNullStr(String str, String defaultvalue)
    {
        if (isEmpty(str))
            return defaultvalue;
        return str;
    }

    public static String cutString(String str, int size)
    {
        if (str == null)
            return "";
        if (str.length() > size)
        {
            return str.substring(0, size - 1);
        }
        return str;
    }

    public static String getFileName(String filePath)
    {
        if (StringUtil.isEmpty(filePath))
        {
            return "";
        }
        int pos1 = filePath.lastIndexOf("/");
        int pos2 = filePath.lastIndexOf("\\");
        if (pos1 > pos2)
        {
            return filePath.substring(pos1 + 1);
        }
        else
        {
            return filePath.substring(pos2 + 1);
        }
    }

    public static Double m2(Double d)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        return StringUtil.toDoubleNull(df.format(d));
    }

    /**
     * 十进制转二进制
     * @param decimalSource
     * @return
     */
    public static String decimalToBinary(String decimalSource)
    {
        if (StringUtil.isBlank(decimalSource))
        {
            return null;
        }
        try
        {
            BigInteger bigInteger = new BigInteger(decimalSource);
            return bigInteger.toString(2);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * 十进制转二进制
     * @param decimalSource
     * @return
     */
    public static String decimalToBinary(int decimalSource)
    {
        return decimalToBinary(String.valueOf(decimalSource));
    }

    /**
     * 二进制转十进制
     * @param binarySource
     * @return
     */
    public static int binaryToDecimal(String binarySource)
    {
        if (StringUtil.isBlank(binarySource))
        {
            return 0;
        }
        try
        {
            BigInteger bi = new BigInteger(binarySource, 2);
            return StringUtil.toInt(bi.toString());
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    /**
     * 字符串 前面补位
     * @param value
     * @param item
     * @param length
     * @return
     */
    public static String strCover(String value, String item, int length)
    {
        if (StringUtil.isBlank(value) || StringUtil.isBlank(item))
        {
            return null;
        }
        int strLen = value.length();
        StringBuffer _str = null;
        while (strLen < length)
        {
            _str = new StringBuffer();
            _str.append(item).append(value);
            value = _str.toString();
            strLen = _str.length();
        }
        return value;
    }

    public static byte charToByte(char c)
	{
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	/**
     *  show byte array values as string
     * @param bArray
     * @return  
     * @throws
     */
    public static String getByteArrayString(byte[] bArray)
	{
		if (bArray == null || bArray.length <= 0)
		{
			return null;
		}
		
		StringBuilder builder = new StringBuilder();
		for (Byte b : bArray)
		{
			builder.append("-").append(b.toString());
		}
		
		return builder.substring(1);
	}
    
	public static int byteArrayToInt(byte[] b)
	{
		return b[3] & 0xFF | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
	}


    public static boolean isBlank(String value)
    {
        return value == null || value.trim().length() == 0;
    }


    public static boolean isNotBlank(String value)
    {
        return !isBlank(value);
    }



}
