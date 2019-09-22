package com.vboss.okline.ui.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.support.v4.app.ActivityCompat;

import com.hyphenate.easeui.Constant;
import com.hyphenate.easeui.model.EaseContactModel;
import com.vboss.okline.R;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.UserRepository;
import com.vboss.okline.data.WorkerThreadScheduler;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;
import com.vboss.okline.ui.contact.bean.Contact;
import com.vboss.okline.ui.contact.bean.ContactItem;
import com.vboss.okline.ui.im.ChatActivity;
import com.vboss.okline.utils.CharacterParser;
import com.vboss.okline.utils.PinyinUtils;
import com.vboss.okline.utils.StringUtils;
import com.vboss.okline.utils.TextUtils;
import com.vboss.okline.utils.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static android.content.ContentValues.TAG;
import static com.vboss.okline.base.OKLineApp.context;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/28 21:36
 * Desc :
 */

public class ContactsUtils {


    private static final String TAG = ContactsUtils.class.getSimpleName();
    private static final int REQUEST_READ_CONTACTS = 2001;
    private static final int REQUEST_CALL_PHONE = 2002;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS};
    private static String s;
    private static List<ContactEntity> vipList;
    private static ContactsUtils utils;
    private final int DEFAULT_COUNT = 150; // 默认单次提交的条数
    private int index = 0;
    private int count;
    private List<ContactEntity> contactEntities;
    private Subscription subscription;
    private int realCount = DEFAULT_COUNT; //实际单次需要提交的次数
    private int threadCount;

    /**
     * 更改之后的分批导入通讯录
     */


    public ContactsUtils() {

    }

    public static ContactsUtils getInstance() {
        if (utils == null) {
            utils = new ContactsUtils();
        }
        return utils;
    }

    /**
     * 取首字母大写的char值
     *
     * @param con
     * @return
     */
    public static String getFirstUpperCaseLetter(Contact con) {
        if (!TextUtils.isEmpty(con.getSortLetters())) {
            s = con.getSortLetters().toUpperCase().substring(0, 1);
        } else {
            s = "#";
        }
        return s;
    }

    public static String filledData(String selling) {
        //汉字转换成拼音
        /*String pinyin = characterParser.getSelling(selling);
        String sortString = pinyin.substring(0, 1).toUpperCase();

        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            return sortString.toUpperCase();
        } else {
            return "#";
        }*/
        String letter = "#";
        if (TextUtils.isEmpty(selling)) {
            return letter;
        }
        CharacterParser characterParser = CharacterParser.getInstance();
        //汉字转换成拼音
        String pinyin = PinyinUtils.getPingYin(selling);
        String sortString = pinyin.substring(0, 1).toUpperCase(Locale.CHINESE);
        // 正则表达式，判断首字母是否是英文字母
        if (sortString.matches("[A-Z]")) {
            letter = sortString.toUpperCase(Locale.CHINESE);
        }
//        Timber.tag(TAG).i("pinyin %s ", pinyin);
//        Timber.tag(TAG).i("sortString %s ", sortString);
//        Timber.tag(TAG).i("letter %s ", letter);
        return letter;
    }

    /**
     * Checks if the app has permission to write to device storage
     * <p/>
     * If the app does not has permission then the user will be prompted to
     * grant permissions
     * 检查app是否有读取联系人的权限
     * 如果没有再弹出相应权限对话框
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.READ_CONTACTS);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_READ_CONTACTS);
        }
    }

    public static void requestCallPhonePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        }
    }
    /**
     * 将联系人实体转换为在listview显示的bean
     *
     * @param entity 联系人实体
     * @return
     */
    public static ContactItem contactEtity2contactItem(ContactEntity entity) {
        String sortLetters= "";
        String pinyin;
        ContactItem item = new ContactItem();
        item.setRemark(entity.remarkName());
        item.setNickName(entity.nickName());
        item.setRealName(entity.realName());
//        item.setSortLetters(ContactsUtils.filledData(entity.remarkName()));
        if (entity.relationState() != null && entity.relationState() == 3) {
            //是会员的取实名为排序标准
            pinyin = PinyinUtils.getPingYin(entity.realName());
//            sortLetters = PinyinUtils.getFirstHeadWordChar(entity.realName()).matches("[A-Z]")
//                    ? PinyinUtils.getFirstHeadWordChar(entity.realName()) : "#";

        } else {
            //非会员的取备注为排序标准
            pinyin = PinyinUtils.getPingYin(entity.remarkName());
//            sortLetters = PinyinUtils.getFirstHeadWordChar(entity.remarkName()).matches("[A-Z]")
//                    ? PinyinUtils.getFirstHeadWordChar(entity.remarkName()) : "#";
        }
        //modify by linzhangbin 2017/6/12 搜索群组闪退问题
        if (pinyin.length()>0){
            sortLetters = pinyin.substring(0, 1).toUpperCase().matches("[A-Z]") ? pinyin.substring(0, 1).toUpperCase() : "#";
        }else{
            sortLetters = "#";
        }
        //modify by linzhangbin 2017/6/12 搜索群组闪退问题
        item.setSortLetters(sortLetters);
        item.setOlNo(entity.friendOlNo());
        item.setContactID(entity.friendId());
        item.setAvatar(entity.imgUrl());
        item.setPhoneNum(entity.phone());
        //modify by linzhangbin 2017/06/01 用小写拼音排序
        item.setPinyin(pinyin.toLowerCase());
        //modify by linzhangbin 2017/06/01 用小写拼音排序
        item.setRelationState(entity.relationState() == null ? 0 : entity.relationState());
        if (1 == entity.isNote()) {
            item.setItemType(1);
            item.setStar(true);
        } else {
            item.setItemType(3);
            item.setStar(false);
        }

        return item;
    }

    public static ContactItem group2contactItem(ContactGroup group) {
        ContactItem item = new ContactItem();
        item.setItemType(2);
        item.setOlNo("");
        item.setNickName("");
        item.setAvatar("");
        item.setStar(false);
        item.setRemark(group.groupName());
        item.setSortLetters(PinyinUtils.getFirstHeadWordChar(group.groupName()));
        item.setEaseID(group.easeGroupId());
        item.setOlGroupID(group.groupId());
//        item.setGroupName("欧乐群");

        return item;
    }

    /**
     * 跳转个人聊天
     *
     * @param context  上下文
     * @param remark   对方昵称
     * @param olNumber 欧乐号
     * @param phone    手机号
     */
    public static void personalChat(Context context, String realName, String remark, String olNumber, String phone, String avatar) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constant.EXTRA_USER_ID, olNumber.toLowerCase());
        intent.putExtra(Constant.EXTRA_PHONE_NUM, phone);
        //update by luoxiuxiu 170614  friend relation State
        EaseContactModel model = EaseContactModel.getInstance();
        Map<String,ContactEntity> map = model.getContactMap();
        if(map!=null){
            ContactEntity contactEntity = map.get(olNumber);
            if(contactEntity!=null) {
                int state = contactEntity.relationState();
                if (state == 2) { //对方是欧乐会员通讯录存在，不是好友关系
                    intent.putExtra(Constant.EXTRA_REAL_NAME, contactEntity.remarkName());
                } else if (state == 3) {//对方为好友
                    intent.putExtra(Constant.EXTRA_TO_REMARK, contactEntity.remarkName());
                    intent.putExtra(Constant.EXTRA_REAL_NAME, contactEntity.realName());
                }
                intent.putExtra(Constant.EXTRA_TO_RELATION, state);
            }
        }else {
            intent.putExtra(Constant.EXTRA_REAL_NAME, realName);
            intent.putExtra(Constant.EXTRA_TO_REMARK, remark);
        }
        context.startActivity(intent);
    }

    public static void groupChat(Context context, ContactItem item) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constant.EXTRA_USER_ID, item.getEaseID());
        intent.putExtra(Constant.EXTRA_REAL_NAME, item.getRealName());
        intent.putExtra(Constant.EXTRA_TO_REMARK, item.getRemark());
        intent.putExtra(ContactItem.EXTRA_OL_GROUP_ID, item.getOlGroupID());
        intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
        context.startActivity(intent);
    }

    //如果电话或者邮箱的集合中内容为空,则删除此条数据
    public static List<String> filterList(List<String> list){
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String phone = iterator.next();
            if (TextUtils.isEmpty(phone))
                iterator.remove();//这里要使用Iterator的remove方法移除当前对象，如果使用List的remove方法，则同样会出现ConcurrentModificationException
        }
        return list;
    }

    /**
     * 检测NFC功能
     *
     * @param context
     * @return
     */
    public static boolean hasNfc(Context context) {
        boolean bRet = false;
        NfcAdapter adapter = NfcAdapter.getDefaultAdapter(context);

        if (adapter != null && adapter.isEnabled()) {
            // adapter存在，能启用
            bRet = true;
        } else {
            ToastUtil.show(context, R.string.hasNfc);
        }
        return bRet;
    }

    /**
     * 打安全电话,暂时调用系统电话
     *
     * @param phoneNum
     * @param activity
     */
    public static void safeCall(String phoneNum, Activity activity) {
        Timber.tag(TAG).i("打电话被点击了");

        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.CALL_PHONE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, new String[]{
                            Manifest.permission.CALL_PHONE},
                    2006);
        }

        //暂时都调用系统的拨打电话
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNum));
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 100);
//            return;
        }
        activity.startActivity(intent);
    }

    /**
     * 获取联系人中是欧乐会员的集合
     *
     * @param context 上下文
     * @return
     */
    public static List<ContactEntity> getVipList(Context context) {
        vipList = new ArrayList<>();
        ContactRepository.getInstance(context)
                .getAllContact()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {
                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                    }

                    @Override
                    public void onNext(List<ContactEntity> list) {
                        for (ContactEntity entity : list) {
                            if (!StringUtils.isNullString(entity.friendOlNo())) {
                                vipList.add(entity);
                            }
                        }
                    }
                });


        return vipList;
    }

    /**
     * 普通聊天(短信)
     */
    public static void normalChat(String phone, Context context) {
        Uri smsToUri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        //String smsContent = "（昵称）" + nickName + "\n" + "（手机号）" + conPhoneNum;
        //intent.putExtra("sms_body", smsContent);
        context.startActivity(intent);
    }

    /**
     * 对方不是欧乐会员发送短信邀请其成为欧乐会员
     *
     * @param phone     电话
     * @param context   上下文
     * @param userName  本机用户姓名
     * @param userPhone 本机用户号码
     */
    public static void normalChat(String phone, Context context, String userName, String userPhone) {
        Uri smsToUri = Uri.parse("smsto:" + phone);
        Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
        String smsContent = userName + "（" + userPhone + ")" + context.getResources().getString(R.string.invite_use_oule);
        intent.putExtra("sms_body", smsContent);
        context.startActivity(intent);
    }

    /**
     * 聊天搜索筛选
     *
     * @param input   输入的内容
     * @param conList 总集合
     * @return
     */
    public static List<ContactEntity> filterList(String input, List<ContactEntity> conList) {


        List<ContactEntity> filterList = new ArrayList<>();
        if (input.matches("^([0-9]|[/+]).*")) {// 正则表达式 匹配以数字或者加号开头的字符串(包括了带空格及-分割的号码)
            String simpleStr = input.replaceAll("\\-|\\s", "");
            for (int i = 0, m = conList.size(); i < m; i++) {
                ContactEntity con = conList.get(i);
                if (con.phone() != null && con.remarkName() != null) {
                    if (con.phone().replaceAll("\\-|\\s", "").contains(simpleStr) || con.remarkName().contains(input)) {
                        if (!filterList.contains(con)) {
                            filterList.add(con);
                        }
                    }
                }
            }
        } else {
            for (int i = 0, m = conList.size(); i < m; i++) {
                ContactEntity con = conList.get(i);
                if (null != con.phone() && null != con.remarkName()) {
                    //姓名全匹配,姓名首字母简拼匹配,姓名全字母匹配
                    if (con.relationState() == 3) {
                        if (con.realName().toLowerCase(Locale.CHINESE).contains(input.toLowerCase(Locale.CHINESE))
                                || PinyinUtils.getPingYin(con.realName()).toLowerCase(Locale.CHINESE).replace(" ", "").contains(input.toLowerCase(Locale.CHINESE))
                                || PinyinUtils.getFirstHeadWordChar(con.realName()).toLowerCase(Locale.CHINESE).contains(input.toLowerCase(Locale.CHINESE))
                                || PinyinUtils.getPingYin(con.remarkName()).toLowerCase(Locale.CHINESE).replace(" ", "").contains(input.toLowerCase(Locale.CHINESE))) {
                            if (!filterList.contains(con)) {
                                filterList.add(con);
                            }
                        }
                    } else {
                        if (con.remarkName().toLowerCase(Locale.CHINESE).replace(" ", "").contains(input.toLowerCase(Locale.CHINESE))
                                || PinyinUtils.getPingYin(con.remarkName()).toLowerCase().replace(" ", "").contains(input.toLowerCase(Locale.CHINESE))) {
                            if (!filterList.contains(con)) {
                                filterList.add(con);
                            }
                        }
                    }
                }
            }
        }
        return filterList;
    }

    /**
     * 用于展示联系人姓名(备注)格式的文本
     * @param realName 姓名
     * @param nickName 备注
     * @return
     */
    public static String realNameNremarkName(String realName, String nickName){
        String mergeName;
        if (TextUtils.isEmpty(nickName)){
            mergeName = realName;
        }else{
            mergeName = realName + "（"+nickName+"）";
        }
        return mergeName;
    }

    /**
     * 开启十个线程并发导入通讯录
     *
     */
    public void importContactsMultiThread(final Context context,final List<ContactItem> list){
        //需要开启的线程数
        int threadCount = list.size() % 150 > 0 ? list.size() / 150 + 1 : list.size() / 150;
        if (threadCount > 10){
            threadCount = 10;
        }
        for (int i = 0; i < threadCount; i++) {
            int startIndex = i * realCount; // 计算每一个线程提交的开始数据位置
            int endIndex = (i + 1) * realCount - 1; // 计算每一个线程提交的结束数据位置
            if (i == threadCount) {
                endIndex = list.size() - 1; // 如果是最后一个线程，就到最后，反之除不尽
            }
            new SubmitThread(startIndex,endIndex,list).start();
        }

    }

    /**
     * 开启一个线程去导入通讯录
     *
     * @param context
     * @param list
     */
    public void testImportContacts(final Context context, final List<ContactItem> list) {
        //add by linzhangbin 2017/6/15 每次导联系人把index置为0
        index = 0;
        //add by linzhangbin 2017/6/15 每次导联系人把index置为0
        ContactsFragment.isImportCompleted = false;
        Timber.tag(TAG).i("testImportContacts start------------------");
        WorkerThreadScheduler.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    contactEntities = new ArrayList<>();
                    Timber.tag(TAG).i("testImportContacts start2------------------");
                    for (ContactItem item : list) {
                        ContactEntity entity = ContactEntity.newBuilder()
                                .phone(item.getPhoneNum())
                                .remarkName(item.getRemark())
                                .operatType(1).build();

                        if (!item.getPhoneNum().equals(UserRepository.getInstance(context).getUser().getPhone())) {
                            contactEntities.add(entity);
//                            Timber.tag(TAG).i("contactEntities add entity :" + entity);
                        }
                    }
                    Timber.tag(TAG).i("testImportContacts start3------------------");
                    count = list.size() % 150 > 0 ? list.size() / 150 + 1 : list.size() / 150;
                    Timber.tag(TAG).i("sync count: %s, list size:%s",count,list.size());
                    sync();
                } catch (Exception e) {
                    Timber.tag(TAG).e(e, "Fail to import contact");
                }
            }
        });
    }

    private void sync() {
        Timber.tag(TAG).i("sync index:" + index);
        if (index >= count) {
            Timber.tag(TAG).i("sync complete");
            ContactsFragment.isImportCompleted = true;
            if (subscription != null && !subscription.isUnsubscribed()) {
                subscription.unsubscribe();
            }
            return;
        }
        Timber.tag(TAG).i("testImportContacts start5------------------");
        List<ContactEntity> subTestList = contactEntities.subList(index * 150, (index + 1) * 150 >
                contactEntities.size() ? contactEntities.size() : (index + 1) * 150);
        Timber.tag(TAG).i("testImportContacts start6------------------");
        subscription = ContactRepository.getInstance(context).syncContacts(subTestList).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new DefaultSubscribe<List<ContactEntity>>(TAG) {

                    @Override
                    public void onNext(List<ContactEntity> list) {
                        Timber.tag(TAG).i("testImportContacts end------------------");
                        index++;
                        Timber.tag(TAG).i("onNext index:" + index + " list size : " + list.size());
//                        if (index <= count) {
                        sync();
//                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        //add by linzhangbin 2017/6/8 同步出错也视为完成,下拉刷新可刷新
                        ContactsFragment.isImportCompleted = true;
                        //add by linzhangbin 2017/6/8 同步出错也视为完成,下拉刷新可刷新
                        if (subscription != null && !subscription.isUnsubscribed()) {
                            subscription.unsubscribe();
                        }
                    }
                });
    }

    private class SubmitThread extends Thread {
        // 当前线程需要提交的数据
        private List<ContactItem> submit = new ArrayList<>();

        public SubmitThread(int startIndex, int endIndex, List<ContactItem> list) {
            for (int i = startIndex; i <= endIndex; i++) {
                submit.add(list.get(i));
            }
        }

        @Override
        public void run() {
            // 开始提交数据
            // 数据上传完成，拿到返回结果，然后排序(排序步骤可以直接在此做完，将排好序后的数据传递到Activity中)、
            // 保存到另外一个集合、更新界面

            // 同时需要将 threadCount - 1，当threadCount <= 0 时，表示所有的都上传成功了，
            // 就可以进行添加到数据库的操作了，但是 threadCount-- 需要同步
            synchronized (ContactsUtils.class) {
                threadCount--;
                if (threadCount <= 0) {
                    // 数据全部上传完成，发送通知可以进行插入数据库操作
                }
            }
        }
    }

}
