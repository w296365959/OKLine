package com.vboss.okline.ui.contact;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;

import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.contact.callPhone.CallLogBean;
import com.vboss.okline.ui.contact.dialog.ContactAddDialog;
import com.vboss.okline.utils.PinyinUtils;
import com.vboss.okline.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import timber.log.Timber;


/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/30 10:09
 * Desc :
 */
public class LocalContact {
    private static final String TAG = "LocalContact";
    /**
     * 获取库Phon表字段
     **/
    private static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    private static final String[] PHONE_RECORD = new String[]{CallLog.Calls.CACHED_NAME// 通话记录的联系人
            , CallLog.Calls.NUMBER// 通话记录的电话号码
            , CallLog.Calls.DATE// 通话记录的日期
            , CallLog.Calls.DURATION// 通话时长
            , CallLog.Calls.TYPE};// 通话类型
    /**
     * 联系人显示名称
     **/
    private static final int PHONES_DISPLAY_NAME_INDEX = 0;
    /**
     * 电话号码
     **/
    private static final int PHONES_NUMBER_INDEX = 1;
    /**
     * 头像ID
     **/
    private static final int PHONES_PHOTO_ID_INDEX = 2;
    /**
     * 联系人的ID
     **/
    private static final int PHONES_CONTACT_ID_INDEX = 3;
    /**
     * 联系人的邮箱
     **/
    private static final int PHONES_EMAIL_ID_INDEX = 4;
    private static LocalContact manager = null;
    private ContactAddDialog.ClickListenerInterface clickListenerInterface;
    private Context mContext ;
    /**
     * 联系人名称
     **/
//    private ArrayList<String> mContactsName = new ArrayList<String>();
//
//    /**联系人头像**/
//    private ArrayList<String> mContactsNumber = new ArrayList<String>();
//
//    /**联系人头像**/
//    private ArrayList<Bitmap> mContactsPhonto = new ArrayList<Bitmap>();

    private ContactItem localContact = null;
    /**
     * 全部联系人
     */
    private List<ContactItem> list;
    //    private List<ContactEntity> entitiesList = new ArrayList<>();
    private String newPhoneNumber;
    private ErrorListener errorListener;


    public LocalContact(Context context) {
        this.mContext = context;
        list = new ArrayList<>();
        getPhoneContacts();
//        getSIMContacts();
    }

    public static LocalContact getInstance(Context context) {
        if (manager == null) {
            manager = new LocalContact(context);
        }
        return manager;
    }


    /**
     * 得到手机通讯录联系人信息
     **/
    private void getPhoneContacts() {

        try {
            // 获取手机联系人
            Cursor phoneCursor = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PHONES_PROJECTION, null, null, null);

            //linZhangbin Changed 2017/05/02
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);

                if (phoneNumber.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
                    newPhoneNumber = phoneNumber.replaceAll("\\-|\\s", "");
                } else {
                    newPhoneNumber = phoneNumber;
                }

                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber)){
                    continue;
                }

                //得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(PHONES_PHOTO_ID_INDEX);


                localContact = new ContactItem();
                localContact.setRemark(contactName);
                localContact.setPhoneNum(newPhoneNumber);


                if (!StringUtils.isNullString(contactName)) {
                    localContact.setSortLetters(PinyinUtils.getFirstHeadWordChar(contactName));
                }
                //如果有同名同手机号的就不添加进去,如果是数据库就要拿这条数据去查数据库
                //如果手机本地联系人中有我自己,筛选掉
                list.add(localContact);
            }
            Set<ContactItem> conSet = new HashSet<>();
            for (int i = 0, m = list.size(); i < m; i++) {
                conSet.add(list.get(i));
            }
            list.clear();
            list.addAll(conSet);
            //lzb edit for test log
//            for (ContactItem contact : list) {
//                Log.i(TAG, contact.toString());
//            }
            phoneCursor.close();

        } catch (Exception e) {
//            Log.e(TAG, e.getLocalizedMessage());
            Timber.tag(TAG).e(e.getMessage());
            if (null != errorListener){
                errorListener.error();
            }
//            ToastUtil.show(mContext,"导入联系人失败");
        }
    }

    /**
     * 异常事件
     */
    public void setOnErrorListener(ErrorListener errorListener) {
        this.errorListener = errorListener;
    }

    public void testGetContact() {
        Cursor cursor = mContext.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        int contactIdIndex = 0;
        int nameIndex = 0;

        if (cursor.getCount() > 0) {
            contactIdIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);
            nameIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
        }
        while (cursor.moveToNext()) {
            String contactId = cursor.getString(contactIdIndex);
            String name = cursor.getString(nameIndex);
            Log.i(TAG, contactId);
            Log.i(TAG, name);

            /*
             * 查找该联系人的phone信息
             */
            Cursor phones = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId,
                    null, null);
            int phoneIndex = 0;
            if (phones.getCount() > 0) {
                phoneIndex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            }
            while (phones.moveToNext()) {
                String phoneNumber = phones.getString(phoneIndex);
                Log.i(TAG, phoneNumber);
            }

            /*
             * 查找该联系人的email信息
             */
            Cursor emails = mContext.getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + contactId,
                    null, null);
            int emailIndex = 0;
            if (emails.getCount() > 0) {
                emailIndex = emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
            }
            while (emails.moveToNext()) {
                String email = emails.getString(emailIndex);
                Log.i(TAG, email);
            }

        }
    }

    /**
     * 读取数据
     *
     * @return 读取到的数据
     */
    private List<Map<String, String>> getDataList(Activity activity) {
        // 1.获得ContentResolver
        ContentResolver resolver = mContext.getContentResolver();
        // 2.利用ContentResolver的query方法查询通话记录数据库
        /**
         * @param uri 需要查询的URI，（这个URI是ContentProvider提供的）
         * @param projection 需要查询的字段
         * @param selection sql语句where之后的语句
         * @param selectionArgs ?占位符代表的数据
         * @param sortOrder 排序方式
         *
         */

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 100);
        }
        Cursor cursor = resolver.query(CallLog.Calls.CONTENT_URI,
                null, null, null, CallLog.Calls.NUMBER + "," + CallLog.Calls.DATE + " DESC");
        // 3.通过Cursor获得数据
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME));
            String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
            long dateLong = cursor.getLong(cursor.getColumnIndex(CallLog.Calls.DATE));
            String date = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(new Date(dateLong));
            int duration = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.DURATION));
            int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
            String typeString = "";
            switch (type) {
                case CallLog.Calls.INCOMING_TYPE:
                    typeString = "打入";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    typeString = "打出";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    typeString = "未接";
                    break;
                default:
                    break;
            }
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", (name == null) ? "未备注联系人" : name);
            map.put("number", number);
            map.put("date", date);
            map.put("duration", (duration / 60) + "分钟");
            map.put("type", typeString);
            list.add(map);
            cursor.close();
        }
        return list;
    }

    private TreeMap<String, ArrayList<CallLogBean>> getCallLogs(Activity activity) {
        TreeMap<String, ArrayList<CallLogBean>> callLogs = new TreeMap<>();
        String lastPhone = "";
        //用于记录每个号码的最后通话日期
        ContentResolver cr = mContext.getContentResolver();
        try {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALL_LOG}, 100);
            }
            Cursor cursor = cr.query(CallLog.Calls.CONTENT_URI, // 查询通话记录的URI
                    null, null, null, CallLog.Calls.NUMBER + "," + CallLog.Calls.DATE + " DESC");

            if (cursor == null) {
                return callLogs;
            }
            while (cursor.moveToNext()) {
                CallLogBean callLogBean = new CallLogBean();
                //备注名称
                callLogBean.setRemarkName(cursor.getString(cursor.getColumnIndex(CallLog.Calls.CACHED_NAME)));
                //通话类型
                int type = cursor.getInt(cursor.getColumnIndex(CallLog.Calls.TYPE));
                switch (type) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callLogBean.setType(2);
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callLogBean.setType(1);
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callLogBean.setType(0);
                        break;
                }
                //通话日期
                String date = cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DATE));
                callLogBean.setDates(date);
                //通话时间
                callLogBean.setDuration(cursor.getString(cursor.getColumnIndexOrThrow(CallLog.Calls.DURATION)));
                //通话号码
                String number = cursor.getString(cursor.getColumnIndex(CallLog.Calls.NUMBER));
                Timber.tag(TAG).i(number + "   " + callLogBean);
                //如果号码存在就继续存通话记录

                if(number.equals(lastPhone)){
                    ArrayList<CallLogBean> callLogBeens = callLogs.get(number);
                    callLogBeens.add(callLogBean);
                }else{
                    ArrayList<CallLogBean> callLogBeens = new ArrayList<>();
                    callLogBeens.add(callLogBean);
                    callLogs.put(number,callLogBeens);
                }
                lastPhone = number;
            }
            cursor.close();
        }catch(Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return callLogs;
    }

    public List<ContactItem> getContact() {
        Timber.tag(TAG).i("local Contact size:"+list.size());
        return list;
    }

    public void setConList(ContactItem con) {
        if (con != null) {
            String phone = con.getPhoneNum();
            for (int i = 0, m = list.size(); i < m; i++) {
                String phones = list.get(i).getPhoneNum();
                if (phone.equals(phones)) {
                    list.remove(list.get(i));
                    list.add(i, con);
                }
            }
        }
    }

    /**
     * 报异常的监听
     */
    public interface ErrorListener{
        void error();
    }

//    public List<ContactEntity> getEntitiesList(){
//
//        return entitiesList;
//    }




}
