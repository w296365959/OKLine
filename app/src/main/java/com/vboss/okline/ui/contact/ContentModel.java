package com.vboss.okline.ui.contact;

import android.support.annotation.Nullable;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.vboss.okline.base.DefaultSubscribe;
import com.vboss.okline.data.ContactRepository;
import com.vboss.okline.data.entities.ContactEntity;
import com.vboss.okline.data.entities.ContactGroup;
import com.vboss.okline.ui.contact.bean.Contact;
import com.vboss.okline.ui.contact.bean.ContactItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * OKLine(HangZhou) co.,Ltd.
 * Author : Lin Zhangbin
 * Email : hedgehog@okline.cn
 * Date : 2017/3/31 16:42
 * Desc :
 */

public class ContentModel  {


}
