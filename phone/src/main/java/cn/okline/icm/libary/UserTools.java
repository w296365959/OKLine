package cn.okline.icm.libary;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;


/**
 * Created by okline on 17/3/2.
 */

public class UserTools {
    public static final String DBFILE = "SignalDBFile.xml";

    /**
     * 判断用户DB文件是否存在, 是否已经开户成功
     *
     * @param context
     * @return
     */
    public static String readUserFile(Context context) {
        String dirPath = context.getFilesDir().getPath();
        String filePath = dirPath + File.separator + DBFILE;
        File userFile = new File(filePath);
        String result = "";
        if (userFile.exists()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(userFile));
                String readInfo = null;
                while ((readInfo = reader.readLine()) != null) {
                    result += readInfo;
                }
            } catch (Exception e) {
            }
        }
        Log.d("UserTools", "readUserFile : " + filePath + " \n " + result);
        return result;
    }

    /**
     * 判断用户DB文件是否存在, 是否已经开户成功
     *
     * @param context
     * @return
     */
    public static boolean isUserFileExist(Context context) {
        String dirPath = context.getFilesDir().getPath();
        String filePath = dirPath + File.separator + DBFILE;
        File userFile = new File(filePath);
        boolean result = userFile.exists();
        Log.d("UserTools", "isUserFileExist : " + filePath + " , " + result);
        return result;
    }

    /**
     * 写入用户的开户数据
     *
     * @param context 上下文
     * @param bytes   开户数据
     * @return
     */
    public static boolean writeUserData(Context context, byte[] bytes, String filePath) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            OPEN_DATA_ACK obj = (OPEN_DATA_ACK) ois.readObject();
            UserTools.createDBFile(context, obj, filePath);
            ois.close();
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean writeUserData(Context context, byte[] bytes) {
        String filePath = context.getFilesDir().getPath() + File.separator + DBFILE;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            OPEN_DATA_ACK obj = (OPEN_DATA_ACK) ois.readObject();
            UserTools.createDBFile(context, obj, filePath);
            ois.close();
            bis.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    //创建一个SignalDBFile.xml文件..放在App Data目录下
    public static boolean createDBFile(Context context, OPEN_DATA_ACK data, String filePath) {
        boolean success = false;
        try {
            String dbInfo = createDBInfo(data);
            Log.d("UserTools", "createDBFile dbInfo : " + dbInfo);
            FileOutputStream fos = new FileOutputStream(filePath, false);
            byte[] buf = dbInfo.getBytes();
            fos.write(buf, 0, buf.length);
            fos.flush();
            fos.close();
            success = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }

    //data对象转成字符串
    static String createDBInfo(OPEN_DATA_ACK data) {


        String si = StringUtil.byteToHexString(data.getDbCert());

        StringBuilder sb = new StringBuilder();
        sb.append("MY_DB").append("\n");
        sb.append("600#").append(si).append("#0#0#0:0#0:0#1#aaabbbccc").append("\n");
        sb.append("GUIDE_DB").append("\n");
        sb.append("LRT_DB").append("\n");
        sb.append("SIGNAL_DB").append("\n");
        sb.append("SNS_DB").append("\n");
        sb.append("SNT_DB").append("\n");
        for (int i = 0; i < data.getSntCount(); i++) {
            TABLE_SNTDB snt = data.getDbSNT()[i];
            sb.append(StringUtil.byteToHexString(snt.getSI())).append("#").append(StringUtil.intToIp(snt.getIpAddr().getIPv()[0])).append(":").append(snt.getIpAddr().getwPort()).append("\n");
        }
        sb.append("SOS_DB").append("\n");
        for (int i = 0; i < data.getSosCount(); i++) {
            TABLE_SOSDB sos = data.getDbSOS()[i];
            sb.append(StringUtil.byteArrayToInt(sos.getSI())).append("#").append(StringUtil.intToIp(sos.getIpAddr().getIPv()[0])).append(":").append(sos.getIpAddr().getwPort()).append("\n");
        }
        return sb.toString();
    }

    public static boolean copyAppFile(Context context) {
        int byteNumbers;
        final AssetManager assetManager = context.getResources().getAssets();
        try {
            byte[] buffer = new byte[1024 + 1];
            InputStream inputStream = assetManager.open(UserTools.DBFILE);
            File file = context.getFileStreamPath(UserTools.DBFILE);
            FileOutputStream outputStream = new FileOutputStream(file);
            while (true) {
                byteNumbers = inputStream.read(buffer, 0, 1024);
                if (byteNumbers == -1)
                    break;
                else
                    outputStream.write(buffer, 0, byteNumbers);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
